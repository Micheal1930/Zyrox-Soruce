package com.varrock.world.content.raids.theatre_of_blood;

import com.varrock.model.Position;
import com.varrock.world.content.raids.theatre_of_blood.verzik_vitur.VerzikViturConstants;

/**
 * Created by Jonny on 7/2/2019
 **/
public enum TheatreOfBloodFight {

    VERZIK_VITUR(VerzikViturConstants.VITUR_SITTING_IDLE_NPC_ID, new Position(3166, 4323));

    private int npcId;
    private Position spawnPosition;

    TheatreOfBloodFight(int npcId, Position spawnPosition) {
        this.npcId = npcId;
        this.spawnPosition = spawnPosition;
    }

    public int getNpcId() {
        return npcId;
    }

    public Position getSpawnPosition() {
        return spawnPosition;
    }
}
