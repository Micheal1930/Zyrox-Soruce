package com.zyrox.tools.discord;

/**
 * Created by Jonny on 8/12/2019
 **/
public enum DiscordBotType {

    LOCAL("NjY5NjA3MDMyNDI4NDI5MzIz.XjWfIw.wx1ZaPGHpcUo8Uf_myRNR9Yg7iM"),
    LIVE("NjY5NjA3MDMyNDI4NDI5MzIz.XjWfIw.wx1ZaPGHpcUo8Uf_myRNR9Yg7iM");

    private final String token;

    DiscordBotType(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }}
