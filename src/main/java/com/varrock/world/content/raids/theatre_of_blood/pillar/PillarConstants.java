package com.varrock.world.content.raids.theatre_of_blood.pillar;

import com.varrock.GameSettings;
import com.varrock.model.Position;

/**
 * Created by Jonny on 7/8/2019
 **/
public class PillarConstants {

    public static int PILLAR_OBJECT_ID = GameSettings.OSRS_OBJECT_OFFSET + 32687;

    public static int SUPPORTING_PILLAR_NPC_ID = GameSettings.OSRS_NPC_OFFSET + 8379;

    public static int COLLAPSED_PILLAR_NPC_ID = GameSettings.OSRS_NPC_OFFSET + 8377;

    public static int COLLAPSING_PILLAR_NPC_ID = GameSettings.OSRS_NPC_OFFSET + 8378;

    public static Position[] PILLAR_POSITIONS = new Position[] {

            new Position(3161, 4306),
            new Position(3161, 4312),
            new Position(3161, 4318),

            new Position(3173, 4306),
            new Position(3173, 4312),
            new Position(3173, 4318),

    };

}
