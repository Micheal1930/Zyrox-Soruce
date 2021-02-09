package com.zyrox.world.entity.impl.npc.impl.summoning;

import com.zyrox.model.Position;
import com.zyrox.world.entity.impl.npc.NPC;
import com.zyrox.world.entity.impl.player.Player;

public class Familiar extends NPC {

    private Player summoner;

    public Familiar(int id, Position position, Player summoner) {
        super(id, position);
    }

    public Player getSummoner() {
        return summoner;
    }

}
