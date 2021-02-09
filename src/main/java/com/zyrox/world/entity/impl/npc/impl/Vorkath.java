package com.zyrox.world.entity.impl.npc.impl;

import com.zyrox.model.Position;
import com.zyrox.world.entity.impl.npc.NPC;
import com.zyrox.world.entity.impl.player.Player;

public class Vorkath extends NPC {

    public Vorkath(int id, Position position) {
        super(id, position);
    }

    @Override

    public void dropItems(Player killer) {
        killer.addBossPoints(2);
        super.dropItems(killer);
        
    }
}
