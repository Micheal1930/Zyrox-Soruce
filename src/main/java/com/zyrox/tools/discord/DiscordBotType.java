package com.zyrox.tools.discord;

/**
 * Created by Jonny on 8/12/2019
 **/
public enum DiscordBotType {

    LOCAL("ODU2Njg1ODY5NjkwNTE5NTgy.YNEo4w.hmuC2dtD1HXPOAI-vcu97IacX5Y"),
    LIVE("ODU2Njg1ODY5NjkwNTE5NTgy.YNEo4w.hmuC2dtD1HXPOAI-vcu97IacX5Y");

    private final String token;

    DiscordBotType(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }}
