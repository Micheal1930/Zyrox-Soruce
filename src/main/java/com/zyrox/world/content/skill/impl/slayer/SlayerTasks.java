package com.zyrox.world.content.skill.impl.slayer;

import static com.zyrox.util.Misc.exclusiveRandom;
import static com.zyrox.util.Misc.inclusiveRandom;

import java.util.ArrayList;
import java.util.List;

import com.zyrox.model.Position;
import com.zyrox.util.Misc;
import com.zyrox.util.NameUtils;

/**
 * @author Gabriel Hannason
 */

public enum SlayerTasks {

    NO_TASK(null, new int[]{-1}, null, -1, null),

    /**
     * Easy tasks
     */
    ROCK_CRAB(SlayerMaster.VANNAKA, new int[]{1265}, "Rock Crabs can be found in the Training Teleport.", 2100, new Position(2709, 3715, 0)),
    BANSHEE(SlayerMaster.VANNAKA, new int[]{1612}, "Banshees can be found in the Slayer Tower.", 1000, new Position(3442, 3540, 0)),
    EXPERIMENT(SlayerMaster.VANNAKA, new int[]{1677}, "Experiments can be found in the Training Teleport.", 2150, new Position(3564, 9954, 0)),
    GIANT_BAT(SlayerMaster.VANNAKA, new int[]{78}, "Giant Bats can be found in Taverly Dungeon.", 2000, new Position(2907, 9833)),
    CHAOS_DRUID(SlayerMaster.VANNAKA, new int[]{181}, "Chaos Druids can be found in Edgeville Dungeon.", 2120, new Position(3109, 9931, 0)),
    ZOMBIE(SlayerMaster.VANNAKA, new int[]{76}, "Zombies can be found in Edgeville Dungeon.", 2000, new Position(3144, 9903, 0)),
    HOBGOBLIN(SlayerMaster.VANNAKA, new int[]{2686}, "Hobgoblins can be found in Edgeville Dungeon.", 4500, new Position(3123, 9876, 0)),
    HILL_GIANT(SlayerMaster.VANNAKA, new int[]{117}, "Hill Giants can be found in Edgeville Dungeon.", 4700, new Position(3120, 9844, 0)),
    DEADLY_RED_SPIDER(SlayerMaster.VANNAKA, new int[]{63}, "Deadly Red Spiders can be found in Edgeville Dungeon.", 4500, new Position(3083, 9940, 0)),
    BABY_BLUE_DRAGON(SlayerMaster.VANNAKA, new int[]{52}, "Baby Blue Dragons can be found in Taverly Dungeon.", 5000, new Position(2891, 9772, 0)),
    SKELETON(SlayerMaster.VANNAKA, new int[]{90, 91, 92, 93, 459, 1471, 2036, 2037, 2715, 3151, 3291, 3581, 4384, 4385, 4386, 5332, 5333, 5334, 5335, 5336, 5337, 5338, 5339, 5340, 5341, 5386}, "Skeletons can be found in Edgeville Dungeon.", 2200, new Position(3094, 9896)),
    EARTH_WARRIOR(SlayerMaster.VANNAKA, new int[]{124}, "Earth Warriors can be found in Edgeville Dungeon.", 540, new Position(3124, 9986)),
    YAK(SlayerMaster.VANNAKA, new int[]{5529}, "Yaks can be found in the Training Teleport.", 2500, new Position(3203, 3267)),
    GHOUL(SlayerMaster.VANNAKA, new int[]{1218}, "Ghouls can be found in the Training Teleport.", 4800, new Position(3418, 3508)),
    DARK_BEAST(SlayerMaster.KONAR_QUO_MATEN, new int[]{2783}, "", 7200, new Position(1998, 4644)),

    /**
     * Medium tasks
     */
    BANDIT(SlayerMaster.DURADEL, new int[]{1880}, "Bandits can be found in the Training teleport.", 6500, new Position(3172, 2976)),
    WILD_DOG(SlayerMaster.DURADEL, new int[]{1593}, "Wild Dogs can be found in Brimhaven Dungeon.", 6700, new Position(2680, 9557)),
    MOSS_GIANT(SlayerMaster.DURADEL, new int[]{112}, "Moss Giants can be found in Brimhaven Dungeon.", 6600, new Position(2676, 9549)),
    FIRE_GIANT(SlayerMaster.DURADEL, new int[]{110, 22252}, "Fire Giants can be found in Brimhaven Dungeon.", 6900, new Position(2664, 9480)),
    GREEN_DRAGON(SlayerMaster.DURADEL, new int[]{941}, "Green Dragons can be found in western Wilderness.", 7500, new Position(2977, 3615)),
    BLUE_DRAGON(SlayerMaster.DURADEL, new int[]{55}, "Blue Dragons can be found in Taverly Dungeon.", 8000, new Position(2892, 9799)),
    HELLHOUND(SlayerMaster.DURADEL, new int[]{49}, "Hellhounds can be found in Taverly Dungeon.", 8000, new Position(2870, 9848)),
    BLACK_DEMON(SlayerMaster.DURADEL, new int[]{84}, "Black Demons can be found in Edgeville Dungeon.", 8270, new Position(3089, 9967)),
    BLOODVELD(SlayerMaster.DURADEL, new int[]{1618}, "Bloodvelds can be found in Slayer Tower.", 7200, new Position(3418, 3570, 1)),
    INFERNAL_MAGE(SlayerMaster.DURADEL, new int[]{1643}, "Infernal Mages can be found in Slayer Tower.", 7000, new Position(3445, 3579, 1)),
    ABERRANT_SPECTRE(SlayerMaster.DURADEL, new int[]{1604}, "Aberrant Spectres can be found in Slayer Tower.", 7300, new Position(3432, 3553, 1)),
    NECHRYAEL(SlayerMaster.DURADEL, new int[]{1613}, "Nechryaels can be found in Slayer Tower.", 7800, new Position(3448, 3564, 2)),
    GARGOYLE(SlayerMaster.DURADEL, new int[]{1610}, "Gargoyles can be found in Slayer Tower.", 8100, new Position(3438, 3534, 2)),
    TZHAAR_XIL(SlayerMaster.DURADEL, new int[]{2605}, "TzHaar-Xils can be found in Tzhaar City.", 9000, new Position(2445, 5147)),
    TZHAAR_HUR(SlayerMaster.DURADEL, new int[]{2600}, "TzHaar-Hurs can be found in Tzhaar City.", 7900, new Position(2456, 5135)),
    ORK(SlayerMaster.DURADEL, new int[]{6273}, "Orks can be found in the Godwars Dungeon.", 7600, new Position(2867, 5322, 2)),
    ARMOURED_ZOMBIE(SlayerMaster.DURADEL, new int[]{8162}, "Armoured Zombies can be found in the Training Teleport", 8000, new Position(3085, 9672)),
    DUST_DEVIL(SlayerMaster.DURADEL, new int[]{1624}, "Dust Devils can be found in the Training Teleport", 9500, new Position(3279, 2964)),
    JUNGLE_STRYKEWYRM(SlayerMaster.DURADEL, new int[]{9467}, "Strykewyrms can be found in the Strykewyrm Cavern", 10874, new Position(2731, 5095)),
    DESERT_STRYKEWYRM(SlayerMaster.DURADEL, new int[]{9465}, "Strykewyrms can be found in the Strykewyrm Cavern.", 11120, new Position(2731, 5095)),

    /**
     * Hard tasks
     */
    ABYSSAL_DEMON(SlayerMaster.KURADEL, new int[]{1615}, "Abyssal Demons can be found in the Slayer Tower.", 12500, new Position(3418, 3566, 2)),
    BLACK_DRAGON(SlayerMaster.KURADEL, new int[]{54}, "Black Dragons can be found in Taverly Dungeon.", 12500, new Position(2836, 9826, 0)),

    MONKEY_GUARD(SlayerMaster.KURADEL, new int[]{1459}, "Monkey Guards can be found in the Training Teleport", 14000, new Position(2795, 2775)),
    WATERFIEND(SlayerMaster.KURADEL, new int[]{5361}, "Waterfiends can be found in the Ancient Cavern.", 13400, new Position(1737, 5353)),
    ICE_STRYKEWYRM(SlayerMaster.KURADEL, new int[]{9463}, "Strykewyrms can be found in the Strykewyrm Cavern.", 13877, new Position(2731, 5095)),
    STEEL_DRAGON(SlayerMaster.KURADEL, new int[]{1592}, "Steel dragons can be found in Brimhaven Dungeon.", 15600, new Position(2710, 9441)),
    MITHRIL_DRAGON(SlayerMaster.KURADEL, new int[]{5363}, "Mithril Dragons can be found in the Ancient Cavern.", 16000, new Position(1761, 5329, 1)),
    GREEN_BRUTAL_DRAGON(SlayerMaster.KURADEL, new int[]{5362}, "Green Brutal Dragons can be found in the Ancient Cavern.", 15590, new Position(1767, 5340)),
    SKELETON_WARLORD(SlayerMaster.KURADEL, new int[]{6105}, "Skeleton Warlords can be found in the Ancient Cavern.", 14400, new Position(1763, 5358)),
    SKELETON_BRUTE(SlayerMaster.KURADEL, new int[]{6104}, "Skeleton Brutes can be found in the Ancient Cavern.", 14400, new Position(1788, 5335)),
    AVIANSIE(SlayerMaster.KURADEL, new int[]{6246}, "Aviansies can be found in the Godwars Dungeon.", 14600, new Position(2868, 5268, 2)),
    FROST_DRAGON(SlayerMaster.KURADEL, new int[]{51}, "Frost Dragons can be found in the deepest of Wilderness.", 22570, new Position(2968, 3902)),

    /**
     * Elite
     */
    COMMANDER_ZILYANA(SlayerMaster.SUMONA, new int[]{6247}, "Commander Zilyana can be found east in the Godwars Dungeon.", 68000, new Position(2908, 5265)),
    ZAMMY_TSUTSAROTH(SlayerMaster.SUMONA, new int[]{6203}, "K�ril Tsutsaroth can be found north in the Godwars Dungeon.", 68000, new Position(2925, 5332, 2)),
    KREE_ARRA(SlayerMaster.SUMONA, new int[]{6222}, "Kree�arra can be found south in the Godwars Dungeon.", 68000, new Position(2839, 5295, 2)),
    CERBERUS(SlayerMaster.SUMONA, new int[]{1999}, "Cerberus can be found using the Boss teleport.", 50000, new Position(1240, 1246)),
    ABYSSAL_SIRE(SlayerMaster.SUMONA, new int[]{5886}, "The Abyssal Sire can be found using the Boss teleport.", 40000, new Position(3373, 3893)),
    SCORPIA(SlayerMaster.SUMONA, new int[]{2001}, "Scorpia can be found deep in the wilderness using the Boss teleport.", 40000, new Position(3236, 3944)),
    VENENATIS(SlayerMaster.SUMONA, new int[]{2000}, "Venenatis can be found deep in the wilderness using the Boss teleport.", 50000, new Position(3350, 3734)),
    GLACOR(SlayerMaster.SUMONA, new int[]{1382}, "Glacors can be found using the Boss teleport.", 25000, new Position(3053, 9577)),


    NEX(SlayerMaster.SUMONA, new int[]{13447}, "Nex can be found in the Godwars Dungeon.", 100000, new Position(2903, 5203)),
    GENERAL_GRAARDOR(SlayerMaster.SUMONA, new int[]{6260}, "General Graardor can be found in the Godwars Dungeon.", 68000, new Position(2863, 5354, 2)),
    TORMENTED_DEMON(SlayerMaster.SUMONA, new int[]{8349}, "Tormented Demons can be found using the Boss teleport.", 40000, new Position(2602, 5713)),
    KING_BLACK_DRAGON(SlayerMaster.SUMONA, new int[]{50}, "The King Black Dragon can be found using the Boss teleport.", 26000, new Position(2273, 4680, 0)),
    DAGANNOTH_SUPREME(SlayerMaster.SUMONA, new int[]{2881}, "The Dagannoth Kings can be found using the Boss teleport.", 26000, new Position(1908, 4367)),
    DAGANNOTH_REX(SlayerMaster.SUMONA, new int[]{2883}, "The Dagannoth Kings can be found using the Boss teleport.", 26000, new Position(1908, 4367)),
    DAGANNOTH_PRIME(SlayerMaster.SUMONA, new int[]{2882}, "The Dagannoth Kings can be found using the Boss teleport.", 26000, new Position(1908, 4367)),
    CHAOS_ELEMENTAL(SlayerMaster.SUMONA, new int[]{3200}, "The Chaos Elemental can be found using the Boss teleport.", 58000, new Position(3285, 3921)),
    SLASH_BASH(SlayerMaster.SUMONA, new int[]{2060}, "Slash Bash can be found using the Boss teleport.", 28000, new Position(2547, 9448)),
    KALPHITE_QUEEN(SlayerMaster.SUMONA, new int[]{1160}, "The Kalphite Queen can be found using the Boss teleport.", 31000, new Position(3476, 9502)),
    PHOENIX(SlayerMaster.SUMONA, new int[]{8549}, "The Phoenix can be found using the Boss teleport.", 21000, new Position(2839, 9557)),
    CORPOREAL_BEAST(SlayerMaster.SUMONA, new int[]{8133}, "The Corporeal Beast can be found using the Boss teleport.", 80000, new Position(2885, 4375)),
    BANDOS_AVATAR(SlayerMaster.SUMONA, new int[]{4540}, "The Bandos Avatar can be found using the Boss teleport.", 34000, new Position(2891, 4767)),
    LIZARDMAN_SHAMAN(SlayerMaster.SUMONA, new int[]{6766}, "The lizardman can be found using the Boss teleport.", 40000, new Position(2718, 9811)),
    BORKS(SlayerMaster.SUMONA, new int[]{7134}, "The Bork can be found using the Boss teleport.", 50000, new Position(3104, 5536)),
    SKOTIZO(SlayerMaster.SUMONA, new int[]{7286}, "The Skotizo can be found using the Boss teleport.", 60000, new Position(3378, 9817)),
    VORKATH(SlayerMaster.SUMONA, new int[]{23060}, "The Vorkath can be found using the Boss teleport.", 80000, new Position(3378, 9817)),
    ZULRAH(SlayerMaster.SUMONA, new int[]{2043, 2044, 2042}, "The Zulrah can be found using ::zulrah.", 80000, new Position(3378, 9817)),

    KRAKEN(SlayerMaster.SUMONA, new int[]{3847}, "The Kraken can be found using the Boss teleport.", 45000, new Position(3094, 3503)),
    GIANT_MOLE(SlayerMaster.SUMONA, new int[]{3340}, "The Giant Mole can be found using the Boss teleport.", 45000, new Position(1761, 5181)),

    BARREL_CHEST(SlayerMaster.SUMONA, new int[]{5666}, "The Barrelchests can be found using the Boss teleport.", 26000, new Position(2973, 9517)),

    REVENANTS_WILD(SlayerMaster.KRYSTILIA, new int[]{ 22936, 22938, 22939, 22940 }, "Revenants can be found in the Rev Cave.", 80000, new Position(3126, 3833)),
    GREEN_DRAGON_WILD(SlayerMaster.KRYSTILIA, new int[]{941}, "Green Dragons can be found in western Wilderness.", 15000, new Position(2977, 3615)),
    CHAOS_ELEMENTAL_WILD(SlayerMaster.KRYSTILIA, new int[]{3200}, "The Chaos Elemental can be found using the Boss teleport.", 126000, new Position(3285, 3921)),
    SCORPIA_WILD(SlayerMaster.KRYSTILIA, new int[]{2001}, "Scorpia can be found deep in the wilderness using the Boss teleport.", 80000, new Position(3236, 3944)),
    VENENATIS_WILD(SlayerMaster.KRYSTILIA, new int[]{2000}, "Venenatis can be found deep in the wilderness using the Boss teleport.", 100000, new Position(3350, 3734)),
    FROST_DRAGON_WILD(SlayerMaster.KRYSTILIA, new int[]{51}, "Frost Dragons can be found in the deepest of Wilderness.", 55000, new Position(2968, 3902)),

    ;

    /*
     * Suomi add new boss tasks in here:
     *
     * look below for example
     *  npc name     slayer master        npc id        description of location                        xp per kill   position for teleing to task
     * 	BARREL_CHEST(SlayerMaster.SUMONA, 5666, "The Barrelchests can be found using the Boss teleport.", 26000, new Position(2973, 9517));

     */



    /*
     * end of suomis new tasks
     */;

    private SlayerTasks(SlayerMaster taskMaster, int[] npcIds, String npcLocation, int XP, Position taskPosition) {
        this.taskMaster = taskMaster;
        this.npcLocation = npcLocation;
        this.XP = XP;
        this.taskPosition = taskPosition;
        for(Integer npcId : npcIds) {
            this.npcIds.add(npcId);
        }
    }

    private SlayerMaster taskMaster;
    private List<Integer> npcIds = new ArrayList<>();
    private String npcLocation;
    private int XP;
    private Position taskPosition;

    public SlayerMaster getTaskMaster() {
        return this.taskMaster;
    }

    public List<Integer> getNpcIds() {
        return this.npcIds;
    }

    public String getNpcLocation() {
        return this.npcLocation;
    }

    public int getXP() {
        return this.XP;
    }

    public String getName() {
        String name = name();
        /*if (name.contains("_WILD"))
            name.substring(0, name.length()-5);*/
        return NameUtils.capitalizeWords(name.replaceAll("_", " ").toLowerCase());
    }

    public Position getTaskPosition() {
        return this.taskPosition;
    }

    public static SlayerTasks forId(int id) {
        for (SlayerTasks tasks : SlayerTasks.values()) {
            if (tasks.ordinal() == id) {
                return tasks;
            }
        }
        return null;
    }

    public static int[] getNewTaskData(SlayerMaster master) {
        int slayerTaskId = 1, slayerTaskAmount = 20;
        int easyTasks = 0, mediumTasks = 0, hardTasks = 0, eliteTasks = 0;

        /*
         * Calculating amount of tasks
         */
        for (SlayerTasks task : SlayerTasks.values()) {
            if (task.getTaskMaster() == SlayerMaster.VANNAKA)
                easyTasks++;
            else if (task.getTaskMaster() == SlayerMaster.DURADEL)
                mediumTasks++;
            else if (task.getTaskMaster() == SlayerMaster.KURADEL)
                hardTasks++;
            else if (task.getTaskMaster() == SlayerMaster.SUMONA)
                eliteTasks++;
            else if (task.getTaskMaster() == SlayerMaster.KRYSTILIA)
                eliteTasks++;
        }

        if (master == SlayerMaster.VANNAKA) {
            slayerTaskId = 1 + Misc.getRandom(easyTasks);
            if (slayerTaskId > easyTasks)
                slayerTaskId = easyTasks;
            slayerTaskAmount = 40 + Misc.getRandom(15);
        } else if (master == SlayerMaster.DURADEL) {
            slayerTaskId = easyTasks - 1 + Misc.getRandom(mediumTasks);
            slayerTaskAmount = 28 + Misc.getRandom(13);
        } else if (master == SlayerMaster.KURADEL) {
            slayerTaskId = 1 + easyTasks + mediumTasks + Misc.getRandom(hardTasks - 1);
            slayerTaskAmount = 37 + Misc.getRandom(20);
        } else if (master == SlayerMaster.SUMONA) {
            slayerTaskId = 1 + easyTasks + mediumTasks + hardTasks + Misc.getRandom(eliteTasks - 1);
            slayerTaskAmount = 7 + Misc.getRandom(10);
        } else if (master == SlayerMaster.KRYSTILIA) {
            ArrayList<Integer> krystiliaTaskOrdinals = new ArrayList<>();
            for (SlayerTasks task : SlayerTasks.values()) {
                if (task.getTaskMaster() == SlayerMaster.KRYSTILIA) {
                    krystiliaTaskOrdinals.add(task.ordinal());
                }
            }
            slayerTaskId = krystiliaTaskOrdinals.get(exclusiveRandom(1 , krystiliaTaskOrdinals.size()));
            slayerTaskAmount = 7 + Misc.getRandom(20);
        }
        return new int[]{slayerTaskId, slayerTaskAmount};
    }
}
