package com.zyrox.world.content.combat.effect;

import java.util.ArrayList;
import java.util.List;

import com.zyrox.util.Misc;

/**
 * Handles the armour piercing effect for NPCs.
 *
 * @author Gabriel || Wolfsdarker
 */
public class ArmourPiercingEffect {

    /**
     * The list of NPCs with piercing effect.
     */
    private static final List<Integer> NPC_IDS = new ArrayList<>();

    /**
     * The chance to pierce on each attack.
     */
    private static final double npcPierceChance = 0.15;

    static {
        NPC_IDS.add(2881);
        NPC_IDS.add(6263);
        NPC_IDS.add(6265);
        NPC_IDS.add(6223);
        NPC_IDS.add(6225);
        NPC_IDS.add(6208);
        NPC_IDS.add(6206);
        NPC_IDS.add(6250);
    }

    /**
     * If the npc ID has the piercing effect.
     *
     * @param npcID
     * @return if the effect is happening
     */
    public static boolean isPiercing(int npcID) {
        return Misc.randomFloat() < npcPierceChance && NPC_IDS.contains(npcID);
    }

}
