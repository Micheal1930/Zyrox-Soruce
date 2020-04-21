package com.varrock.world.entity.impl.npc.impl;

import com.varrock.model.Position;
import com.varrock.world.entity.impl.npc.NPC;
import com.varrock.world.entity.impl.player.Player;

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
