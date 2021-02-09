package com.zyrox.world.content.raids.theatre_of_blood.pillar;

import com.zyrox.model.Position;
import com.zyrox.world.World;
import com.zyrox.world.entity.impl.npc.NPC;

/**
 * Created by Jonny on 7/8/2019
 **/
public class PillarNpc extends NPC {

    protected PillarNpc(int id, Position position) {
        super(id, position);

        World.register(this);
    }
}
