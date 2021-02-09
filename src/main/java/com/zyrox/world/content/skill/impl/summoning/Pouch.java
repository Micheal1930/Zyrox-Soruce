package com.zyrox.world.content.skill.impl.summoning;

import java.util.ArrayList;
import java.util.Arrays;

public enum Pouch {

    SPIRIT_WOLF(63480, 12047, 12158, 7, 2859, 1, 4.8),
    SPIRIT_DREADFOWL(63488, 12043, 12158, 8, 2138, 4, 9.3),
    SPIRIT_SPIDER(63496, 12059, 12158, 8, 6291, 8, 12.6),
    THORNY_SNAIL(63504, 12019, 12158, 9, 3363, 13, 12.6),
    GRANITE_CRAB(63512, 12009, 12158, 7, 440, 16, 21.6),
    SPIRIT_MOSQUITO(63520, 12778, 12158, 1, 6319, 17, 46.5),
    DESERT_WYRM(63528, 12049, 12159, 45, 1783, 18, 31.2),
    SPIRIT_SCORPION(63536, 12055, 12160, 57, 3095, 19, 83.2),
    SPIRIT_TZ_KIH(63544, 12808, 12160, 64, 12168, 22, 96.8),
    ALBINO_RAT(63552, 12067, 12163, 75, 2134, 23, 202.4),
    SPIRIT_KALPHITE(63560, 12063, 12163, 51, 3138, 25, 220),
    COMPOST_MOUND(63568, 12091, 12159, 47, 6032, 28, 49.8),
    GIANT_CHINCHOMPA(63576, 12800, 12163, 84, 9976, 29, 255.2),
    VAMPIRE_BAT(63584, 12053, 12160, 81, 3325, 31, 136),
    HONEY_BADGER(63592, 12065, 12160, 84, 12156, 32, 140.8),
    BEAVER(63600, 12021, 12159, 72, 1519, 33, 57.6),
    VOID_RAVAGER(63608, 12818, 12159, 74, 12164, 34, 59.6),
    VOID_SPINNER(63616, 12780, 12163, 74, 12166, 34, 59.6),
    VOID_TORCHER(63624, 12798, 12163, 74, 12167, 34, 59.6),
    VOID_SHIFTER(63632, 12814, 12163, 74, 12165, 34, 59.6),
    BRONZE_MINOTAUR(63640, 12073, 12163, 102, 2349, 36, 316.8),
    BULL_ANT(63648, 12087, 12158, 11, 6010, 40, 52.8),
    MACAW(63656, 12071, 12159, 78, 249, 41, 72.4),
    EVIL_TURNIP(63664, 12051, 12160, 104, 12153, 42, 184.8),
    SPIRIT_COCKATRICE(63672, 12095, 12159, 88,12109,43,75.2),
    SPIRIT_GUTHATRICE(63680, 12097, 12159, 88, 12111, 43, 75.2),
    SPIRIT_SARATRICE(63688, 12099, 12159, 88, 12113, 43, 75.2),
    SPIRIT_ZAMATRICE(63696, 12101, 12159, 88, 12115, 43, 75.2),
    SPIRIT_PENGATRICE(63704, 12103, 12159, 88, 12117, 43, 75.2),
    SPIRIT_CORAXATRICE(63712, 12105, 12159, 88, 12119, 43, 75.2),
    SPIRIT_VULATRICE(63720, 12107, 12159, 88, 12121, 43, 75.2),
    IRON_MINOTAUR(63728, 12075, 12163, 125, 2351, 46, 404.8),
    PYRELORD(63736, 12816, 12160, 111, 590, 46, 202.4),
    MAGPIE(63744, 12041, 12159, 88, 1635, 47, 83.2),
    BLOATED_LEECH(63752, 12061, 12160, 117, 2132, 49, 215.2),
    SPIRIT_TERRORBIRD(63760, 12007, 12158, 12, 9978, 52, 68.4),
    ABYSSAL_PARASITE(63768, 12035, 12159, 106, 12161, 54, 94.8),
    SPIRIT_JELLY(63776, 12027, 12163, 151, 1937, 55, 484),
    STEEL_MINOTAUR(63784, 12077, 12163, 141, 2353, 56, 492.8),
    IBIS(63792, 12531, 12159, 109, 311, 56, 98.8),
    SPIRIT_GRAAHK(63800, 12810, 12163, 154, 10099, 57, 501.6),
    SPIRIT_KYATT(63808, 12812, 12163, 153, 10103, 57, 501.6),
    SPIRIT_LARUPIA(63816, 12784, 12163, 155, 10095, 57, 501.6),
    KHARAMTHULHU_OVERLORD(63824, 12023, 12163, 144, 6667, 58, 510.4),
    SMOKE_DEVIL(63832, 12085, 12160, 141, 9736, 61, 268),
    ABYSSAL_LURKER(63840, 12037, 12159, 119, 12161, 62, 109.6),
    SPIRIT_COBRA(63848, 12015, 12160, 116, 6287, 63, 276.8),
    STRANGER_PLANT(63856, 12045, 12160, 128, 8431, 64, 281.6),
    MITHRIL_MINOTAUR(63864, 12079, 12163, 152, 2359, 66, 580.8),
    BARKER_TOAD(63872, 12123, 12158, 11, 2150, 66, 87),
    WAR_TORTOISE(63880, 12031, 12158, 1, 7939, 67, 58.6),
    BUNYIP(63888, 12029, 12159, 110, 383, 68, 119.2),
    FRUIT_BAT(63896, 12033, 12159, 130, 1963, 69, 121.2),
    RAVENOUS_LOCUST(63904, 12820, 12160, 79, 1933, 70, 132),
    ARCTIC_BEAR(63912, 12057, 12158, 14, 10117, 71, 93.2),
    PHEONIX(63920, 14623, 12160, 165, 14616, 72, 301),
    OBSIDIAN_GOLEM(63928, 12792, 12163, 195, 12168, 73, 642.4),
    GRANITE_LOBSTER(63936, 12069, 12160, 166, 6979, 74, 325.6),
    PRAYING_MANTIS(63944, 12011, 12160, 168, 2460, 75, 329.6),
    ADAMANT_MINOTAUR(63952, 12081, 12163, 144, 2361, 76, 668.8),
    FORGE_REGENT(63960, 12782, 12159, 141, 10020, 76, 134),
    TALON_BEAST(63968, 12794, 12160, 174, 12162, 77, 174),
    GIANT_ENT(63976, 12013, 12159, 124, 5933, 78, 136.8),
    FIRE_TITAN(63984, 12802, 12163, 198, 1442, 79, 695.2),
    MOSS_TITAN(63992, 12804, 12163, 202, 1440, 79, 695.2),
    ICE_TITAN(64000, 12806, 12163, 198, 1444, 79, 695.2),
    HYDRA(64008, 12025, 12159, 128, 571, 80, 140.8),
    SPIRIT_DAGGANOTH(64016, 12017, 12160, 1, 6155, 83, 364.8),
    LAVA_TITAN(64024, 12788, 12163, 219, 12168, 83, 730.4),
    SWAMP_TITAN(64032, 12776, 12160, 150, 10149, 85, 373.6),
    RUNE_MINOTAUR(64040, 12083, 12163, 1, 2363, 86, 756.8),
    UNICORN_STALLION(64048, 12039, 12159, 140, 237, 88, 154.4),
    GEYSER_TITAN(64056, 12786, 12163, 222, 1444, 89, 783.2),
    WOLPERTINGER(64064, 12089, 12160, 203, 3226, 92, 404.8),
    ABYSSAL_TITAN(64072, 12796, 12159, 113, 12161, 93, 163.2),
    IRON_TITAN(64080, 12822, 12160, 198, 1115, 95, 417.6),
    PACK_YAK(64088, 12093, 12160, 211, 10818, 96, 422.4),
    STEEL_TITAN(64096, 12790, 12163, 178, 1119, 99, 435.2);

    private int buttonId;
    private int pouchId;
    private int charmId;
    private int shardsRequired;
    private int secondIngredientId;
    private int levelRequired;
    private double experience;

    /**
     * Sets the enumeration data format
     *  @param sacrificeId   returns the value of the sacrificeId
     * @param buttonId      returns the value of the actionbutton
     * @param pouchId       returns the value of the pouchId
     * @param charmId       returns the value of the charmId
     * @param levelRequired returns the value of the levelRequired
     * @param experience    returns the value of the experience given
     */

    private Pouch(final int buttonId, final int pouchId, final int charmId, final int shardsRequired,
                  final int secondIngredientId, final int levelRequired, final double experience) {
        this.buttonId = buttonId;
        this.pouchId = pouchId;
        this.charmId = charmId;
        this.shardsRequired = shardsRequired;
        this.secondIngredientId = secondIngredientId;
        this.levelRequired = levelRequired;
        this.experience = experience;
    }

    /**
     * Loops through all the pouch enumeration
     *
     * @param itemName returns the itemName
     * @return the pouchId
     */

    public final static Pouch get(final int buttonId) {
        for (Pouch p : Pouch.values()) {
            if (p.getButtonId() == buttonId) {
                return p;
            }
        }
        return null;
    }

    /**
     * Accessor for the action button id pouch creation interface
     *
     * @param buttonId parses the value of the buttonId
     * @return buttonId
     */

    public int getButtonId() {
        return this.buttonId;
    }

    /**
     * Accessor for the Pouch Id it gives the player
     *
     * @return pouchId
     */

    public int getPouchId() {
        return this.pouchId;
    }

    /**
     * Accessor for the charm id required
     *
     * @return charmId
     */

    public int getCharmId() {
        return this.charmId;
    }

    /**
     * Accessor for the shards required to create the pouch
     *
     * @return shardsRequired
     */

    public int getShardsRequired() {
        return this.shardsRequired;
    }

    /**
     * Accessor for the second Ingredient required to create the pouch
     *
     * @return sacrificeId
     */
    public int getsecondIngredientId() {
        return this.secondIngredientId;
    }

    /**
     * Accessor for the level required to create the pouch
     *
     * @return levelRequired
     */

    public int getLevelRequired() {
        return this.levelRequired;
    }

    /**
     * Experience it gives the player for creating the pouch
     *
     * @return experience
     */

    public double getExp() {
        return this.experience;
    }

    public static double getMaxExperience(int charmId, int currentLevel) {

        double currentExperience = 0;

        for(Pouch pouch : POUCHES) {
            if(pouch.getCharmId() == charmId) {
                if(currentLevel >= pouch.getLevelRequired()) {
                    if(currentExperience < pouch.getExp()) {
                        currentExperience = pouch.getExp();
                    }
                }
            }
        }

        return currentExperience;
    }

    public static ArrayList<Pouch> POUCHES = new ArrayList<Pouch>();

    static {
        POUCHES.addAll(Arrays.asList(Pouch.values()));
        //Collections.reverse(POUCHES);
    }
}
