package com.zyrox;

import com.zyrox.commands.CommandHandler;
import com.zyrox.util.font.FontUtils;
import com.zyrox.world.content.shop.ShopManager;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    public static final String NAME = "Varrock";

    private static ExecutorService httpThread;

    public static boolean isLocal = false;

    public static ExecutorService getHttpThread() {
        return httpThread;
    }

    public static void init() {

        if (isLocal)
            return;

        FontUtils.initialize();

        httpThread = Executors.newSingleThreadExecutor();


        CommandHandler.init();
        try {
            ShopManager.init();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
}
