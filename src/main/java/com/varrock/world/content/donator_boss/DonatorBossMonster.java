package com.varrock.world.content.donator_boss;

import com.varrock.model.definitions.NpcDefinition;
import com.varrock.world.entity.impl.player.Player;

/**
 * Created by Jonny on 7/6/2019
 **/
public enum DonatorBossMonster {

    SCORPIA(250_000_000, 2001, 1700
    ),

    VENENATIS(250_000_000, 2000, 1700
    ),

    KING_BLACK_DRAGON(250_000_000, 50, 1700
    ),

    CHAOS_ELEMENTAL(250_000_000, 3200, 1700
    ),

    ABYSSAL_SIRE(250_000_000, 5886, 1700
    ),

    ;

    private final int price;
    private final int npcId;
    private final int modelZoom;
    private final String name;

    DonatorBossMonster(int price, int npcId, int modelZoom) {
        this.price = price;
        this.npcId = npcId;
        this.modelZoom = modelZoom;
        this.name = NpcDefinition.forId(npcId).getName();
    }


    public int getPrice() {
        return price;
    }

    public int getNpcId() {
        return npcId;
    }

    public int getModelZoom() {
        return modelZoom;
    }

    public String getName() {
        return name;
    }

    public static int getTotalPrice() {
        int total = 0;
        for(DonatorBossMonster donatorBossMonster : DonatorBossMonster.values()) {
            total += donatorBossMonster.getPrice();
        }
        return total;
    }

    public boolean customSpawn(Player player) {
        return false;
    }
}
