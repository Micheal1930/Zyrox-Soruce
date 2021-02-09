package com.zyrox.world.content.raids;

import com.zyrox.world.content.raids.theatre_of_blood.TheatreOfBlood;
import com.zyrox.world.entity.impl.player.Player;

/**
 * Created by Jonny on 7/2/2019
 **/
public class Raids {

    private Player player;

    private TheatreOfBlood theatreOfBlood;

    public Raids(Player player) {
        this.player = player;
        this.theatreOfBlood = new TheatreOfBlood(player);
    }

    public TheatreOfBlood getTheatreOfBlood() {
        return theatreOfBlood;
    }
}
