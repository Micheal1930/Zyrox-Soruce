package com.varrock.world.content.teleport;

import com.varrock.util.Misc;
import com.varrock.world.content.teleport.impl.*;

/**
 * Created by Jonny on 4/25/2019
 **/
public enum TeleportCategory {

    MONSTERS(MonsterTeleport.VALUES),
    MINIGAMES(MinigameTeleport.VALUES),
    BOSSES(BossTeleport.VALUES),
    WILDERNESS(WildernessTeleport.VALUES),
    DUNGEONS(DungeonTeleport.VALUES),
    ;

    private Teleport[] values;

    TeleportCategory(Teleport[] values) {
        this.values = values;
    }

    public Teleport[] getValues() {
        return values;
    }

    public String getName() {
        return Misc.formatText(name().toLowerCase().replaceAll("_", " "));
    }
}
