package com.zyrox.test;

import java.io.File;

import com.zyrox.model.OfflineCharacter;
import com.zyrox.util.Misc;
import com.zyrox.world.entity.impl.player.Player;

/**
 * Created by Jonny on 10/6/2019
 **/
public class CheckDupedAccounts {

    public static void check() {
        System.out.println("Checking for duped accounts...");

        File charFiles = new File("./data/characters/");
        for (final File f : charFiles.listFiles()) {

            if (f.isDirectory()) {
                continue;
            }

            if (f.isFile()) {
                String username = f.getName().replaceAll(".txt", "");

                Player target = OfflineCharacter.getOfflineCharacter(username);


                if(target.getMoneyInPouch() > 100_000_000) {
                    System.out.println(username+" has "+ Misc.formatRunescapeStyle(target.getMoneyInPouch())+" in money pouch.");
                }

                if(target.getInventory().getAmount(995) > 100_000_000) {
                    System.out.println(username+" has "+ Misc.formatRunescapeStyle(target.getInventory().getAmount(995))+" in inventory.");
                }

                if(target.getBank().getAmount(995) > 100_000_000) {
                    System.out.println(username+" has "+ Misc.formatRunescapeStyle(target.getBank().getAmount(995))+" in bank.");
                }
            }

        }
    }

}
