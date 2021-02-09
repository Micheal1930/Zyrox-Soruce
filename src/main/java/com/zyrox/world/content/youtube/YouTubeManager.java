package com.zyrox.world.content.youtube;

import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.*;
import com.zyrox.GameServer;
import com.zyrox.model.PlayerRights;
import com.zyrox.net.sql.SQLTable;
import com.zyrox.util.Misc;
import com.zyrox.world.World;
import com.zyrox.world.content.dialogue.DialogueManager;
import com.zyrox.world.content.goodie_bag.GoodieBagManager;
import com.zyrox.world.entity.impl.player.Player;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Jonny on 10/15/2019
 **/
public class YouTubeManager {

    public static Map<String, ArrayList<String>> USERNAMES_VOTED = new HashMap<>();
    public static Map<String, ArrayList<String>> AUTHORS_VOTED = new HashMap<>();

    public static String API_KEY = "AIzaSyCTGF0jtODly31ABqvyFuJ01TSP2Q27v3A";

    public static int MAIN_INTERFACE_ID = 61115;

    public static HashMap<String, YouTubeVideo> videos = new HashMap<>();

    public static void addVideo(YouTubeVideo video) {
        GameServer.getSqlNetwork().submit(connection -> {
            String query = "INSERT INTO " + SQLTable.getGameSchemaTable(SQLTable.DATA_YOUTUBE_VIDEOS) + " (video_id, uploader, title, description, unix_time) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                //insert
                statement.setString(1, video.getVideoId());
                statement.setString(2, video.getUploader());
                statement.setString(3, video.getTitle());
                statement.setString(4, video.getDescription());
                statement.setLong(5, video.getTime());

                statement.executeUpdate();
            }
        });

        videos.put(video.getVideoId(), video);
    }

    public static void postVideo(Player player, String videoUrl) {
        if(!player.getSqlTimer().elapsed(15_000)) {
            player.sendErrorMessage("You can only do this once every 15 seconds.");
            return;
        }
        new Thread(() -> {
            try {
                String videoId = Misc.getYouTubeVideoID(videoUrl);

                if(videoId == null || videoId.isEmpty()) {
                    player.sendErrorMessage("Error posting video, make sure you copy the video URL from youtube.");
                    return;
                }

                if (!player.isStaff() && player.getRights() != PlayerRights.YOUTUBER) {
                    player.sendErrorMessage("Only youtubers can add videos.");
                    return;
                }

                if (videos.containsKey(videoId)) {
                    player.sendErrorMessage("This video has already been added.");
                    return;
                }

                if (videos.size() >= 10) {
                    player.sendErrorMessage("There can only be 10 videos added at a time.");
                    return;
                }

                long time = System.currentTimeMillis();

                YouTube youtube = new YouTube.Builder(new NetHttpTransport(), new JacksonFactory(),
                        new HttpRequestInitializer() {
                            public void initialize(HttpRequest request) throws IOException {
                            }
                        }).setApplicationName("video-test").build();

                YouTube.Videos.List videoRequest = null;

                videoRequest = youtube.videos().list("snippet,statistics,contentDetails");

                videoRequest.setId(videoId);
                videoRequest.setKey(API_KEY);

                VideoListResponse listResponse = videoRequest.execute();
                List<Video> videoList = listResponse.getItems();

                Video targetVideo = videoList.iterator().next();

                String channelTitle = targetVideo.getSnippet().getChannelTitle();
                String videoTitle = targetVideo.getSnippet().getTitle();
                String description = targetVideo.getSnippet().getDescription();

                int totalVideos = 0;
                for(Map.Entry entry : videos.entrySet()) {
                    YouTubeVideo video = (YouTubeVideo) entry.getValue();
                    if(video.getUploader().equalsIgnoreCase(channelTitle)) {
                        totalVideos++;
                    }
                }

                if(totalVideos >= 2) {
                    player.sendErrorMessage("You are only allowed to have 2 videos posted at a time.");
                    player.sendErrorMessage("Videos are automatically deleted after 72 hours.");
                    return;
                }

                DateTime publishedTime = targetVideo.getSnippet().getPublishedAt();

                long timeDiff = System.currentTimeMillis() - publishedTime.getValue();

                if (timeDiff >= 1000 * 60 * 60 * 24 * 3) { //3 days
                    player.sendErrorMessage("This video is too old to be added.");
                    return;
                }
                if (!videoTitle.toLowerCase().contains("Varrock")) {
                    player.sendErrorMessage("The video must have Varrock in the title.");
                    return;
                }
                if (!description.toLowerCase().contains("Varrock.io")) {
                    player.sendErrorMessage("The video must have Varrock.io in the description.");
                    return;
                }

                if (videoTitle.length() > 50) {
                    videoTitle = videoTitle.substring(0, 50) + "...";
                }

                if (description.length() > 100) {
                    description = description.substring(0, 100) + "...";
                }

                YouTubeVideo video = new YouTubeVideo(videoId, channelTitle, videoTitle, description, publishedTime.getValue());

                YouTubeManager.addVideo(video);

                player.getSqlTimer().reset();

                player.getInventory().add(4003, 1);

                World.sendMessage("<img=11><shad=ff0000> A new youtube video has been posted by "+channelTitle+".");
                World.sendMessage("<img=11><shad=ff0000> Type ::vote and comment your name on the video to get a free goodie bag!");
                open(player);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public static void deleteVideo(Player player, String videoId) {
        if (!player.isHigherStaff()) {
            player.sendErrorMessage("Only high staff members can delete videos.");
            return;
        }

        String query = "DELETE FROM " + SQLTable.getGameSchemaTable(SQLTable.DATA_YOUTUBE_VIDEOS) + " WHERE video_id = '"+videoId+"'";
        GameServer.getSqlNetwork().submit(connection -> {
            try {
                try(PreparedStatement statement = connection.prepareStatement(query)) {
                    statement.executeUpdate();
                }
            } catch (SQLException ex) {
                System.out.println(ex);
            }
        });

        videos.remove(videoId);

        player.sendMessage("Deleted video "+videoId);
        player.getPA().closeDialogueOnly();
    }

    public static void load() {
        String query = "SELECT * FROM " + SQLTable.getGameSchemaTable(SQLTable.DATA_YOUTUBE_VIDEOS) + " ORDER BY unix_time";
        GameServer.getSqlNetwork().submit(connection -> {
            try {
                try (PreparedStatement statement = connection.prepareStatement(query)) {
                    try (ResultSet results = statement.executeQuery()) {

                        while (results.next()) {

                            String videoId = results.getString("video_id");
                            String uploader = results.getString("uploader");
                            String title = results.getString("title");
                            String description = results.getString("description");
                            long time = results.getLong("unix_time");

                            long timeDiff = System.currentTimeMillis() - time;

                            if (timeDiff >= 1000 * 60 * 60 * 24 * 3) { //3 days
                                continue;
                            }

                            YouTubeVideo video = new YouTubeVideo(videoId, uploader, title, description, time);
                            videos.put(video.getVideoId(), video);

                        }

                        String voteCheckQuery = "SELECT * FROM " + SQLTable.getGameSchemaTable(SQLTable.LOGS_YOUTUBE_COMMENTS) + " ORDER BY id";
                        GameServer.getSqlNetwork().submit(connection2 -> {
                            try {
                                try (PreparedStatement statement2 = connection2.prepareStatement(voteCheckQuery)) {
                                    try (ResultSet results2 = statement2.executeQuery()) {
                                        while (results2.next()) {
                                            String username = results2.getString("username");
                                            String videoId = results2.getString("video_id");
                                            String author = results2.getString("author");

                                            if (!videos.containsKey(videoId)) {
                                                continue;
                                            }

                                            addUsernameVoted(videoId, username);
                                            addAuthorVoted(videoId, author);
                                        }

                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                       // Misc.print("No referral codes to retrieve.");
                                    }
                                }
                            } catch (SQLException ex) {
                                System.out.println(ex);
                            }
                        });
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        Misc.print("No youtube video information to retrieve.");
                    }
                }
            } catch (SQLException ex) {
                System.out.println(ex);
            }
        });
    }

    public static void addAuthorVoted(String videoId, String author) {
        if(!AUTHORS_VOTED.containsKey(videoId)) {
            AUTHORS_VOTED.put(videoId, new ArrayList<>());
        }

        AUTHORS_VOTED.get(videoId).add(author);
    }

    public static void addUsernameVoted(String videoId, String username) {
        if(!USERNAMES_VOTED.containsKey(videoId)) {
            USERNAMES_VOTED.put(videoId, new ArrayList<>());
        }

        USERNAMES_VOTED.get(videoId).add(username.toLowerCase());
    }

    public static void open(Player player) {
        check();

        player.videosNotVotedOn = getVideosNotVotedOn(player);

        player.getPacketSender().sendYouTubeContainer(player.videosNotVotedOn.values());
        player.getPacketSender().sendInterface(MAIN_INTERFACE_ID);
    }

    public static void check() {
        ArrayList<String> VIDEOS_TO_REMOVE = new ArrayList<>();

        for (Map.Entry entry : videos.entrySet()) {
            YouTubeVideo video = (YouTubeVideo) entry.getValue();
            long timeDiff = System.currentTimeMillis() - video.getTime();

            if (timeDiff >= 1000 * 60 * 60 * 24 * 3) { //2 days
                VIDEOS_TO_REMOVE.add(video.getVideoId());
            }
        }

        for(String videoToRemove : VIDEOS_TO_REMOVE) {
            videos.remove(videoToRemove);
        }
    }

    public static void checkVideoForComment(Player player, int checkIndex) {
        if(!player.getSqlTimer().elapsed(15_000)) {
            player.sendErrorMessage("You can only do this once every 15 seconds.");
            return;
        }

        DialogueManager.sendStatement(player, "Checking video for comments...");

        player.getSqlTimer().reset();

        new Thread(() -> {
            int videoIndex = 0;

            YouTubeVideo videoToCheck = null;

            for (Map.Entry entry : player.videosNotVotedOn.entrySet()) {
                YouTubeVideo video = (YouTubeVideo) entry.getValue();
                if (videoIndex == checkIndex) {
                    player.sendMessage("Checking comments on the video: " + video.getTitle());
                    videoToCheck = video;
                    break;
                }
                videoIndex++;
            }

            if (videoToCheck == null) {
                player.sendErrorMessage("This video is no longer available...");
                return;
            }

            try {

                CommentThreadListResponse videoCommentsListResponse = null;
                videoCommentsListResponse = GameServer.getYoutube().commentThreads()
                        .list("snippet").setVideoId(videoToCheck.getVideoId()).setTextFormat("plainText").setMaxResults(100L).execute();


                List<CommentThread> videoComments = videoCommentsListResponse.getItems();

                if (videoComments.isEmpty()) {
                    player.sendErrorMessage("Can't get video comments.");
                } else {
                    if (videoComments.size() >= 100) {
                        checkCommentReward(player, videoToCheck.getVideoId(), "server", true);
                        videos.remove(videoToCheck.getVideoId());
                        player.videosNotVotedOn = getVideosNotVotedOn(player);
                        player.getPA().sendYouTubeContainer(player.videosNotVotedOn.values());
                        return;
                    }
                    for (CommentThread videoComment : videoComments) {
                        CommentSnippet snippet = videoComment.getSnippet().getTopLevelComment().getSnippet();
                        String author = snippet.getAuthorDisplayName();
                        String comment = snippet.getTextDisplay();

                        if (comment.toLowerCase().contains(player.getName().toLowerCase())) {
                            checkCommentReward(player, videoToCheck.getVideoId(), author, comment.length() >= 40);
                            return;
                        }
                    }
                    DialogueManager.sendStatement(player, "No comments found on the video.");
                    player.sendErrorMessage("Your in-game name was not found in a comment on this video.");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }).start();

    }

    public static void checkCommentReward(Player player, String videoId, String author, boolean doubleReward) {
        String voteCheckQuery = "SELECT * FROM " + SQLTable.getGameSchemaTable(SQLTable.LOGS_YOUTUBE_COMMENTS) + " WHERE (username = '" + videoId + "' AND video_id = '" + videoId + "') OR (video_id = '" + videoId + "' AND author = '" + author + "') ORDER BY id";
        GameServer.getSqlNetwork().submit(connection -> {
            try {
                try (PreparedStatement statement = connection.prepareStatement(voteCheckQuery)) {
                    try (ResultSet results = statement.executeQuery()) {

                        boolean hasCommentedAlready = false;

                        while (results.next()) {
                            hasCommentedAlready = true;
                        }

                        if (!hasCommentedAlready) {
                            giveCommentReward(player, videoId, author, doubleReward);
                        } else {
                            DialogueManager.sendStatement(player, "You have collected this already.");
                        }

                    } catch (Exception ex) {
                        //Misc.print("No referral codes to retrieve.");
                    }
                }
            } catch (SQLException ex) {
                System.out.println(ex);
            }
        });
    }

    public static void giveCommentReward(Player player, String videoId, String author, boolean doubleReward) {
        String query = "INSERT INTO " + SQLTable.getGameSchemaTable(SQLTable.LOGS_YOUTUBE_COMMENTS) + " (username, video_id, author) VALUES ('" + player.getName().toLowerCase() + "', '" + videoId + "', '" + author + "')";
        GameServer.getSqlNetwork().submit(connection -> {
            try {
                try (PreparedStatement statement = connection.prepareStatement(query)) {
                    int status = statement.executeUpdate();

                    if (status == 0) {
                        return;
                    }

                    player.getInventory().add(GoodieBagManager.GOODIE_BAG_ITEM, doubleReward ? 2 : 1);
                    player.sendMessage("<img=678> <shad=786518>Thank you for voting!");
                    player.sendMessage("<img=678> <shad=786518>A goodie bag has been added to your inventory.");

                    DialogueManager.sendStatement(player, "1x goodie bag has been added to your inventory.");

                    if(USERNAMES_VOTED.containsKey(videoId)) {
                        USERNAMES_VOTED.get(videoId).add(player.getName().toLowerCase());
                    }

                    if(AUTHORS_VOTED.containsKey(videoId)) {
                        AUTHORS_VOTED.get(videoId).add(author);
                    }
                }
            } catch (SQLException ex) {
                System.out.println(ex);
            }
        });

    }

    public static boolean isButton(Player player, int buttonId) {
        switch (buttonId) {
            case 61220:
                watch(player, 0);
                return true;
            case 61221:
                checkVideoForComment(player, 0);
                return true;
            case 61227:
                watch(player, 1);
                return true;
            case 61228:
                checkVideoForComment(player, 1);
                return true;
            case 61234:
                watch(player, 2);
                return true;
            case 61235:
                checkVideoForComment(player, 2);
                return true;
            case 61241:
                watch(player, 3);
                return true;
            case 61242:
                checkVideoForComment(player, 3);
                return true;
            case 61248:
                watch(player, 4);
                return true;
            case 61249:
                checkVideoForComment(player, 4);
                return true;
            case 61255:
                watch(player, 5);
                return true;
            case 61256:
                checkVideoForComment(player, 5);
                return true;
            case 61272:
                watch(player, 6);
                return true;
            case 61273:
                checkVideoForComment(player, 6);
                return true;
            case 61279:
                watch(player, 7);
                return true;
            case 61280:
                checkVideoForComment(player, 7);
                return true;
            case 61286:
                watch(player, 8);
                return true;
            case 61287:
                checkVideoForComment(player, 8);
                return true;
            case 61293:
                watch(player, 9);
                return true;
            case 61294:
                checkVideoForComment(player, 9);
                return true;
        }
        return false;
    }

    public static void watch(Player player, int watchIndex) {
        int videoIndex = 0;

        for (Map.Entry entry : videos.entrySet()) {
            YouTubeVideo video = (YouTubeVideo) entry.getValue();
            if (videoIndex == watchIndex) {
                player.getPacketSender().sendString(1, "https://www.youtube.com/watch?v=" + video.getVideoId());
                player.sendMessage("Opening the video: " + video.getTitle());
                return;
            }
            videoIndex++;
        }

        player.sendErrorMessage("This video is no longer available...");
    }

    public static HashMap<String, YouTubeVideo> getVideosNotVotedOn(Player player) {
        HashMap<String, YouTubeVideo> videosNotVotedOn = new HashMap<>();

        for(Map.Entry entry : videos.entrySet()) {
            String videoId = (String) entry.getKey();
            YouTubeVideo video = (YouTubeVideo) entry.getValue();

            if(USERNAMES_VOTED.containsKey(videoId)) {
                if(USERNAMES_VOTED.get(videoId).contains(player.getName().toLowerCase())) {
                    continue;
                }
            }

           videosNotVotedOn.put(videoId, video);
        }

        return videosNotVotedOn;
    }

}
