package com.varrock.world.content.raids.theatre_of_blood.pillar;

import com.varrock.model.Position;
import com.varrock.world.World;
import com.varrock.world.entity.impl.npc.NPC;

/**
 * Created by Jonny on 7/8/2019
 **/
public class PillarNpc extends NPC {

    protected PillarNpc(int id, Position position) {
        super(id, position);

        World.register(this);
    }
}
