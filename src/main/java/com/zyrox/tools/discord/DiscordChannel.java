package com.zyrox.tools.discord;

/**
 * Created by Jonny on 9/22/2019
 **/
public enum DiscordChannel {

    GAME_YELL("673195322310393867"),
    PLAYERS_ONLINE("673195347027427368"),
    COUNTDOWN("673195372067422213"),
    GAME_MARKET("673195394150170664"),
    GAME_EVENTS("673195416610668562"),
    GAME_LOOTS("673195165548281912"),

    ;

    private final String channelId;

    DiscordChannel(String channelId) {
        this.channelId = channelId;
    }

    public String getChannelId() {
        return channelId;
    }

    public static DiscordChannel getChannelForId(String id) {
        for(DiscordChannel channel : DiscordChannel.values()) {
            if(id.equals(channel.getChannelId()))
                return channel;
        }

        return null;
    }
}
