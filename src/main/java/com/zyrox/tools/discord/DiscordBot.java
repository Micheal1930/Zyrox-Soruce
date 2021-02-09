package com.zyrox.tools.discord;

import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IRole;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.EmbedBuilder;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;

import java.awt.*;
import java.util.*;
import java.util.List;

import com.zyrox.GameServer;
import com.zyrox.GameSettings;
import com.zyrox.model.definitions.ItemDefinition;
import com.zyrox.util.Misc;
import com.zyrox.util.Stopwatch;

/**
 * Created by Jonny on 8/11/2019
 **/
public class DiscordBot {

    /**
     * Store messages in here, 1 is sent every second maximum.
     */
    public static ArrayList<String> messageQueue = new ArrayList<String>();

    public static long timeMessageSent;

    public static Timer timer = new Timer();

    public static HashMap<Long, Stopwatch> MESSAGE_TIMERS = new HashMap<>();

    public DiscordBot() {

    }

    public IDiscordClient client;

    public void start() {
        createClient(DiscordBotConstants.DISCORD_BOT_TYPE.getToken(), true);
        client.getDispatcher().registerListener(new DiscordBotEvents(this));
        timer.schedule(myTask, 0, 100);
    }

    public void createClient(String token, boolean login) {
        ClientBuilder clientBuilder = new ClientBuilder();
        clientBuilder.withToken(token);

        try {
            if (login) {
                this.client = clientBuilder.login();
            } else {
                this.client = clientBuilder.login();
            }
        } catch (DiscordException e) {
            e.printStackTrace();
        }
    }

    public void process() {
        //updatePlayersOnline();
       // updateCountdown();
    }

    public void messageSent(IMessage message) {
        String content = message.getContent();
        String channelId = Long.toString(message.getChannel().getLongID());
        String messageId = Long.toString(message.getLongID());
        String nickName = message.getAuthor().getNicknameForGuild(message.getGuild());
        String senderName = message.getAuthor().getName();

        DiscordChannel channel = DiscordChannel.getChannelForId(channelId);

        if(channel != null) {

        }

    }

    public boolean hasRank(String rankName, IUser user, String currentChannelId) {
        try {
            //List<IRole> list = user.getRolesForGuild(user.getClient().getChannelByID(currentChannelId).getGuild());
            List<IRole> list = user.getRolesForGuild(user.getClient().getChannelByID(Long.parseLong(currentChannelId)).getGuild());
            for (int index = 0; index < list.size(); index++) {
                String roleId = list.get(index).toString().replace("<@&", "");
                roleId = roleId.replace(">", "");
                if (roleId.contains("@everyone")) {
                    continue;
                }
                if (user.getClient().getRoleByID(Long.parseLong(roleId)) != null) {
                    if (user.getClient().getRoleByID(Long.parseLong(roleId)).getName().contains(rankName)) {
                        return true;
                    }
                }
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void sendMessage(DiscordChannel discordChannel, String message) {
      //  if(!GameServer.isLive()) {
           // return;
      //  }

        String splitMessage = "";
        if (message.length() > 2000) {
            splitMessage = message.substring(2000);
            message = message.substring(0, 2000);
        }
        messageQueue.add(discordChannel.getChannelId() + GameSettings.TEXT_SEPERATOR + message);
        if (!splitMessage.isEmpty()) {
            sendMessage(discordChannel, splitMessage);
        }
    }

    private void sendMessageCore(String channelId, String message) {
        try {
            client.getChannelByID(Long.parseLong(channelId)).sendMessage(message);
            timeMessageSent = System.currentTimeMillis();
            messageQueue.remove(0);
        } catch (MissingPermissionsException | RateLimitException | DiscordException e) {
            e.printStackTrace();
        }
    }

    public TimerTask myTask = new TimerTask() {
        @Override
        public void run() {
            if (System.currentTimeMillis() - timeMessageSent <= DiscordBotConstants.MESSAGE_DELAY) {
                return;
            }

            for (int index = 0; index < messageQueue.size(); index++) {
                String[] parse = messageQueue.get(index).split(GameSettings.TEXT_SEPERATOR);
                if (client.getChannelByID(Long.parseLong(parse[0])) == null) {
                    continue;
                }
                if (parse.length >= 2) {
                    sendMessageCore(parse[0], parse[1]);
                } else {
                    messageQueue.remove(index);
                }
                break;
            }
        }
    };

    public void discordCommand(IMessage message) {
        String currentChannelId = message.getChannel().getLongID() + "";
        IUser user = message.getAuthor();
        String command = message.getContent().substring(2, message.getContent().length());
        if (!command.contains("announce")) {
            command = command.toLowerCase();
        }
        //localCommands(command, currentChannelId, user, message);
    }

    public void updatePlayersOnline() {

        if(client == null)
            return;

        if(!client.isLoggedIn())
            return;

        if(!GameServer.isLive()) {
            return;
        }

       /* if(lastPlayersOnline != World.getPlayersOnline()) {
            client.getVoiceChannelByID(Long.parseLong(DiscordChannel.PLAYERS_ONLINE.getChannelId())).changeName("Players Online: " + World.getPlayersOnline());
            WebsiteStatisticUpdater.updatePlayersOnline();
            lastPlayersOnline = World.getPlayersOnline();
        }*/
    }
/*
    public void updateCountdown() {

        if(client == null)
            return;

        if(!client.isLoggedIn())
            return;

        long secondsLeft = (1570381200000L - System.currentTimeMillis()) / 1000;

        if(secondsLeft <= 0)
            return;

        String timeLeft = Misc.getShortTimeLeft((int) secondsLeft);

        if(!lastCountdown.equals(timeLeft)) {
            client.getVoiceChannelByID(Long.parseLong(DiscordChannel.COUNTDOWN.getChannelId())).changeName(timeLeft);
            lastCountdown = timeLeft;
        }
    }*/

    public static ArrayList<String> ALREADY_SENT_TO = new ArrayList<String>();

 /*   public static void loadOutputList() {
        ALREADY_SENT_TO.clear();

        System.out.println("Loading output list...");
        File file = new File("./output.txt");
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
        } catch(FileNotFoundException e) {
            System.out.println("Output list not found.");
            return;
        }
        String line;
        try {
            while ((line = reader.readLine()) != null) {

                if(line.isEmpty())
                    continue;

                try {

                    if(line == null)
                        continue;

                    if(line.isEmpty())
                        continue;

                    ALREADY_SENT_TO.add(line);
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
*/
    public void localCommands(String command, String currentChannelId, IUser user, IMessage message) {
        System.out.println(command);
       // loadOutputList();
        if(DiscordBotConstants.DISCORD_BOT_TYPE == DiscordBotType.LIVE) {
            return;
        }
       /* if (!hasRank("Owner", user, currentChannelId)) {
            return;
        }
        if (command.startsWith("collect")) {
            Writer writer = null;

            try {
                writer = new BufferedWriter(new OutputStreamWriter(
                        new FileOutputStream("output.txt", true), "utf-8"));

                int alreadySent = 0;

                for (int index = 0; index < client.getUsers().size(); index++) {
                    IUser loop = client.getUsers().get(index);
                    if (loop.isBot()) {
                        continue;
                    }

                    try {
                        writer.write(String.valueOf(loop.getLongID()));
                        ((BufferedWriter) writer).newLine();
                    } catch (MissingPermissionsException | RateLimitException | DiscordException e) {
                        e.printStackTrace();
                    }
                }

                System.out.println("Dumping member list.. already sent = "+alreadySent);

                writer.close();

            } catch (IOException ex) {
                // Report
            } finally {
                try {writer.close();} catch (Exception ex) {*//*ignore*//*}
            }
            return;
        }
        if (command.startsWith("announce")) {
            String text = command.replace("announce ", "");
            //sendMessage(currentChannelId, text);

            Writer writer = null;

            try {
                writer = new BufferedWriter(new OutputStreamWriter(
                        new FileOutputStream("output.txt", true), "utf-8"));

                int alreadySent = 0;

                for (int index = 0; index < client.getUsers().size(); index++) {
                    IUser loop = client.getUsers().get(index);
                    if (loop.isBot()) {
                        continue;
                    }

                    if(ALREADY_SENT_TO.contains(String.valueOf(loop.getLongID()))*//* && !hasRank("Owner", loop, currentChannelId)*//*) {
                        alreadySent++;
                        continue;
                    }

                   *//* if(!hasRank("Owner", loop, currentChannelId)) {
                        continue;
                    }*//*
                    try {
                        client.getOrCreatePMChannel(loop).sendMessage(text);
                        writer.write(String.valueOf(loop.getLongID()));
                        ((BufferedWriter) writer).newLine();
                        System.out.println(index + "/" + client.getUsers().size() + " Sent private message to: " + loop.getName());
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } catch (MissingPermissionsException | RateLimitException | DiscordException e) {
                        e.printStackTrace();
                    }
                }

                System.out.println("Finished sending mass pm... already sent = "+alreadySent);

                writer.close();

            } catch (IOException ex) {
                // Report
            } finally {
                try {writer.close();} catch (Exception ex) {*//*ignore*//*}
            }

            return;
        }*/
    }

    public void sendAuctionHouseMessage(int itemId, int amount, String timeRemaining, long price, long auction, int bids, String action) {
        if(!GameServer.isLive()) {
            return;
        }

        EmbedBuilder eb = new EmbedBuilder();

        String itemName = ItemDefinition.forId(itemId).getName();

        eb.withAuthorName((amount > 1 ? amount+"x " : "") + itemName + " "+ action);

        eb.withColor(new Color(138, 167, 255));

        eb.appendDesc("Auction: "+(auction > 0 ? Misc.insertCommasToNumber(auction) : "N/A")+" coins\n");

        eb.appendDesc("Buy Now: "+Misc.insertCommasToNumber(price)+" coins\n");

        if(bids > 0) {
            eb.appendDesc("Bids: " + Misc.insertCommasToNumber(bids) + "\n");
        }

        eb.withThumbnail("https://solaceps.com/images/32x32/"+itemId+".png");

        eb.withFooterText("Time remaining: "+timeRemaining+" ~ Type ::auction in-game to buy it");
        try {
            client.getChannelByID(
                    Long.parseLong(
                            DiscordChannel.GAME_MARKET.getChannelId()))
                    .sendMessage(
                            eb.build());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendGameLoot(String username, int itemId, int amount, String footer, String receivedFrom, String authorImage) {
        if(!GameServer.isLive()) {
            return;
        }

        EmbedBuilder eb = new EmbedBuilder();

        String itemName = ItemDefinition.forId(itemId).getName();

        eb.withTitle(username+" received "+(amount > 1 ? amount+"x" : "") + itemName+" from "+Misc.anOrA(receivedFrom)+" "+receivedFrom+"!");

        eb.appendDesc("Received at: "+Misc.getTime()+"\n");

        eb.withColor(new Color(170, 255, 133));

        eb.withThumbnail("https://solaceps.com/images/32x32/"+itemId+".png");

        eb.withFooterText(footer);

        if(authorImage != null) {
            eb.withFooterIcon(authorImage);
        }

        try {
            client.getChannelByID(
                    Long.parseLong(DiscordChannel.GAME_LOOTS.getChannelId()))
                    .sendMessage(
                            eb.build());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendUpdateEventMessage(String title, String initiatedBy, String description, String footer) {
        if(!GameServer.isLive()) {
            return;
        }

        EmbedBuilder eb = new EmbedBuilder();

        eb.withAuthorName(title);

        eb.withColor(new Color(255, 74, 72));

        if(description != null) {
            eb.appendDesc(description+"\n");
        }

        eb.appendDesc("Initiated by: "+initiatedBy+"\n");
        eb.appendDesc("Server Time: "+Misc.getTime()+"\n");

        eb.withThumbnail("http://solaceps.com/images/icon.png");

        eb.withFooterText(footer);

        try {
            client.getChannelByID(
                    Long.parseLong(DiscordChannel.GAME_EVENTS.getChannelId()))
                    .sendMessage(
                            eb.build());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendStartupMessage() {
      //  if(!GameServer.isLive()) {
         //   return;
      //  }

        EmbedBuilder eb = new EmbedBuilder();

        eb.withAuthorName("Varrock is online!");

        eb.withColor(new Color(255, 74, 72));

        eb.appendDesc("Started in "+GameSettings.GAME_TYPE+" mode.\n");
        eb.appendDesc("Server Time: "+Misc.getTime()+"\n");

        eb.withThumbnail("http://solaceps.com/images/icon.png");

        eb.withFooterText("Varrock is now online and you can login.");

        try {
            client.getChannelByID(
                    Long.parseLong(
                            DiscordChannel.GAME_EVENTS.getChannelId()))
                    .sendMessage(
                            eb.build());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isPlayer(List<IRole> roles) {
        for(IRole iRole : roles) {
            if(iRole.getName().toLowerCase().contains("owner")
                    || iRole.getName().toLowerCase().contains("developer")
                    || iRole.getName().toLowerCase().contains("administrator")
                    || iRole.getName().toLowerCase().contains("moderator")
                    || iRole.getName().toLowerCase().contains("server support")
                    || iRole.getName().toLowerCase().contains("youtuber")
                    || iRole.getName().toLowerCase().contains("graphic designer")
                    || iRole.getName().toLowerCase().contains("beta tester")
                    || iRole.getName().toLowerCase().contains("donator")
                    || iRole.getName().toLowerCase().contains("investor")
                    || iRole.getName().toLowerCase().contains("ex-staff")
                    || iRole.getName().toLowerCase().contains("completionist")
                    || iRole.getName().toLowerCase().contains("maxed")
                    || iRole.getName().toLowerCase().contains("veteran")
                    || iRole.getName().toLowerCase().contains("nitro booster")) {
                return false;
            }
        }
        return true;
    }

}
