package com.varrock.model;

import java.io.File;

import com.varrock.net.PlayerSession;
import com.varrock.saving.PlayerSaving;
import com.varrock.world.World;
import com.varrock.world.entity.impl.player.Player;
import com.varrock.world.entity.impl.player.PlayerLoading;

/**
 * Created by Jonny on 8/12/2019
 **/
public class OfflineCharacter {

    public static Player getOfflineCharacter(String username) {
        if(!exists(username)) {
            return null;
        }

        Player other = World.getPlayerByName(username);

        if(other == null) {
            other = new Player(new PlayerSession(null)).setUsername(username);
            PlayerLoading.getResult(other, true);

            other.setHostAddress(other.getLastHostAddress());
            other.setSuperSerialNumber(other.getLastSuperSerial());
            other.setjSerial(other.getLastSuperSerial());
            other.setSerialNumber(other.getSerialNumber());
        }

        return other;
    }

    public static boolean exists(String username) {
    	System.out.println("/data/characters/"+username+".txt");
        return new File("./data/characters/"+username.toLowerCase()+".txt").exists();
    }
}
