package com.zyrox.tools.discord;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.requests.GatewayIntent;

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

    public JDA client;

    public void start() {
        createClient(DiscordBotConstants.DISCORD_BOT_TYPE.getToken(), true);
        //client.getDispatcher().registerListener(new DiscordBotEvents(this));
        timer.schedule(myTask, 0, 100);
    }

    public void createClient(String token, boolean login) {
        try {
            client = JDABuilder.createLight(
                    token,
                    GatewayIntent.GUILD_MESSAGES, GatewayIntent.DIRECT_MESSAGES)
                    //.addEventListeners(new Bot())
                    .setActivity(Activity.playing("Zyrox"))
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void process() {
        //updatePlayersOnline();
       // updateCountdown();
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
            for(TextChannel channel : client.getTextChannels()) {
                System.out.println(
                        channel.getName()
                                .replaceAll("[^A-Za-z0-9]","")
                                .toUpperCase()
                                .replaceAll(" ", "_")
                        + "('" + channel.getIdLong() + "'),"
                );
            }
            client.getTextChannelById(channelId).sendMessage(message).queue();
            timeMessageSent = System.currentTimeMillis();
            messageQueue.remove(0);
        } catch (Exception e) {
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
                if (client.getTextChannelById(Long.parseLong(parse[0])) == null) {
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

    public void discordCommand(Message message) {
        String currentChannelId = message.getChannel().getIdLong() + "";
        User user = message.getAuthor();
        String command = message.getContentRaw().substring(2, message.getContentRaw().length());
        if (!command.contains("announce")) {
            command = command.toLowerCase();
        }
        //localCommands(command, currentChannelId, user, message);
    }

    public void updatePlayersOnline() {

        if(client == null)
            return;

        if(!GameServer.isLive()) {
            return;
        }
    }

    public void sendAuctionHouseMessage(int itemId, int amount, String timeRemaining, long price, long auction, int bids, String action) {

        EmbedBuilder eb = new EmbedBuilder();

        String itemName = ItemDefinition.forId(itemId).getName();

        eb.setAuthor((amount > 1 ? amount+"x " : "") + itemName + " "+ action);

        eb.setColor(new Color(138, 167, 255));

        eb.appendDescription("Auction: "+(auction > 0 ? Misc.insertCommasToNumber(auction) : "N/A")+" coins\n");

        eb.appendDescription("Buy Now: "+Misc.insertCommasToNumber(price)+" coins\n");

        if(bids > 0) {
            eb.appendDescription("Bids: " + Misc.insertCommasToNumber(bids) + "\n");
        }

        eb.setThumbnail("https://solaceps.com/images/32x32/"+itemId+".png");

        eb.setFooter("Time remaining: "+timeRemaining+" ~ Type ::auction in-game to buy it");
        try {
            client.getTextChannelById(DiscordChannel.GAMEMARKET.getChannelId())
                    .sendMessage(
                            eb.build()).queue();
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

        eb.setTitle(username+" received "+(amount > 1 ? amount+"x" : "") + itemName+" from "+Misc.anOrA(receivedFrom)+" "+receivedFrom+"!");

        eb.appendDescription("Received at: "+Misc.getTime()+"\n");

        eb.setColor(new Color(170, 255, 133));

        eb.setThumbnail("https://solaceps.com/images/32x32/"+itemId+".png");

        eb.setFooter(footer);


        try {
            client.getTextChannelById(DiscordChannel.GAME_LOOTS.getChannelId())
                    .sendMessage(
                            eb.build()).queue();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendUpdateEventMessage(String title, String initiatedBy, String description, String footer) {
       if(!GameServer.isLive()) {
            return;
        }

        EmbedBuilder eb = new EmbedBuilder();

        eb.setAuthor(title);

        eb.setColor(new Color(255, 74, 72));

        if(description != null) {
            eb.appendDescription(description+"\n");
        }

        eb.appendDescription("Initiated by: "+initiatedBy+"\n");
        eb.appendDescription("Server Time: "+Misc.getTime()+"\n");

        eb.setThumbnail("http://solaceps.com/images/icon.png");

        eb.setFooter(footer);
        try {
            client.getTextChannelById(DiscordChannel.GAME_EVENTS.getChannelId())
                    .sendMessage(
                            eb.build()).queue();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendStartupMessage() {
      //  if(!GameServer.isLive()) {
         //   return;
      //  }

        EmbedBuilder eb = new EmbedBuilder();

        eb.setAuthor("Zyrox is online!");

        eb.setColor(new Color(255, 74, 72));

        eb.appendDescription("Started in "+GameSettings.GAME_TYPE+" mode.\n");
        eb.appendDescription("Server Time: "+Misc.getTime()+"\n");

        eb.setThumbnail("http://solaceps.com/images/icon.png");

        eb.setFooter("Zyrox is now online and you can login.");

        try {
            client.getTextChannelById(DiscordChannel.GAME_TYPE.getChannelId())
                    .sendMessage(
                            eb.build()).queue();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public boolean isPlayer(List<Role> roles) {
        for(Role iRole : roles) {
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
