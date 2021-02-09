package com.zyrox.world.content.raids.theatre_of_blood.pillar;

import com.zyrox.model.GameObject;
import com.zyrox.model.Position;
import com.zyrox.world.content.CustomObjects;

/**
 * Created by Jonny on 7/8/2019
 **/
public class PillarObject extends GameObject {

    public PillarObject(int id, Position position, int type, int face) {
        super(id, position, type, face);

        CustomObjects.spawnGlobalObject(this);
    }
}
