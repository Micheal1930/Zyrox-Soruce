package com.varrock.world.content;

import java.util.ArrayList;
import java.util.Collections;

import com.varrock.model.Item;
import com.varrock.model.Locations;
import com.varrock.model.definitions.NpcDefinition;
import com.varrock.util.Misc;
import com.varrock.world.entity.impl.player.Player;

/**
 * Handles custom wilderness items
 * @author Jonny
 */
public class WildernessDrops {

    /**
     * Drops that will be distributed through a drop for a specific NPC
     */
    public static ArrayList<Integer> DROPS = new ArrayList<Integer>();

    /**
     * The NPCS that have this extra drop table
     */
    public static ArrayList<Integer> NPCS = new ArrayList<Integer>();

    static {
        /**
         * Items that have a chance to be dropped
         */
        DROPS.add(13905); //vesta's spear
        DROPS.add(13899); //vesta's longsword
        DROPS.add(13887); //vesta's chainbody
        DROPS.add(13893); //vesta's plateskirt

        DROPS.add(13902); //statius's warhammer
        DROPS.add(13896); //statius's full helm
        DROPS.add(13884); //statius's platebody
        DROPS.add(13890); //statius's platelegs

        DROPS.add(13867); //zuriel's staff
        DROPS.add(13864); //zuriel's hood
        DROPS.add(13858); //zuriel's robe top
        DROPS.add(13861); //zuriel's robe bottom

        DROPS.add(13883); //morrigan's throwing axe
        DROPS.add(13879); //morrigan's javelin
        DROPS.add(13876); //morrigan's coif
        DROPS.add(13870); //morrigan's leather body
        DROPS.add(13873); //morrigan's leather chaps

        NPCS.add(6715); //revenant imp
        NPCS.add(6716); //revenant goblin
        NPCS.add(6701); //revenant werewolf
        NPCS.add(6725); //revenant ork
        NPCS.add(6691); //revenant dark beast
        NPCS.add(22936); //revenant demon
        NPCS.add(22938); //revenant dark beast 2
        NPCS.add(22939); //revenant knight
        NPCS.add(8832); //revenant gargoyle
        NPCS.add(22940); //revenant dragon

        NPCS.add(2009); //callisto
        NPCS.add(3334); //wildywyrm
        NPCS.add(5886); //abyssal sire
        NPCS.add(3033); //chaos elemental
        NPCS.add(3200); //chaos elemental
        NPCS.add(2001); //scorpia
        NPCS.add(2000); //venenatis
        NPCS.add(50); //king black dragon

    }

    public static boolean hasWildernessDrops(int id) {
        return NPCS.contains(id);
    }

    /**
     * Roll a wilderness drop for the npc
     * @param npcId
     * @return
     */
    public static Item getDrop(Player player, int npcId, double modifier) {

        if(!hasWildernessDrops(npcId) || player.getLocation() != Locations.Location.WILDERNESS) {
            return null;
        }

        NpcDefinition npcDefinitions = NpcDefinition.forId(npcId);

        int combatLevel = npcDefinitions.getCombatLevel();

        double chance = (1000 - (combatLevel * 3)) * modifier;

        if(combatLevel <= 80) {
            chance += 200;
        }

        if(chance <= 300) {
            chance = 300;
        }

        if(player.getSkullTimer() > 0) { //if user is skulled, give him a 25% boost
            chance *= .75;
        }

        boolean hasDrop = Misc.inclusiveRandom(1, (int) chance) == 1;

        if (hasDrop) {
            ArrayList<Integer> POTENTIAL_DROPS = new ArrayList<Integer>(DROPS);
            Collections.shuffle(POTENTIAL_DROPS);

            int itemId = POTENTIAL_DROPS.get(0);
            int amount = 1;

            if (itemId == 13883 || itemId == 13879) {
                amount = 15;
            }

            return new Item(itemId, amount);
        }
        return null;
    }

    /**
     * Returns the blood money drop.
     *
     * @param player
     * @param npcId
     * @return the blood money
     */
    public static Item getBloodMoneyDrop(Player player, int npcId, boolean findDrop) {

        if (!hasWildernessDrops(npcId) || (!findDrop && player.getLocation() != Locations.Location.WILDERNESS)) {
            return null;
        }

        NpcDefinition definition = NpcDefinition.forId(npcId);
        int possibleBloodMoneyDrop = 70;
        if (definition != null) {
            if (definition.getCombatLevel() <= 25) {
                possibleBloodMoneyDrop = 75;
            } else if (definition.getCombatLevel() <= 50) {
                possibleBloodMoneyDrop = 100;
            } else if (definition.getCombatLevel() <= 100) {
                possibleBloodMoneyDrop = 125;
            } else if (definition.getCombatLevel() <= 200) {
                possibleBloodMoneyDrop = 150;
            } else {
                possibleBloodMoneyDrop = 200;
            }
        }
        return new Item(43307, findDrop ? possibleBloodMoneyDrop : Misc.inclusiveRandom(100, 25 + possibleBloodMoneyDrop));
    }

}
