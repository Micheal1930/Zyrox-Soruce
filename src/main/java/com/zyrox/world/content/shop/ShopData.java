package com.zyrox.world.content.shop;

/**
 * Created by Jonny on 8/29/2019
 **/
public enum ShopData {
	
	DONATOR_EQUIPMENT("Equipment", 56, true),
	DONATOR_MISC("Misc", 57, true),
	DONATOR_RARES("Rares", 58, true),
	DONATOR_SUPERRARES("Super Rares", 59, true),

    MAGIC_STAFFS("Staffs", 1),
    MAGIC_RUNES("Runes", 7),
    MAGIC_ARMOUR("Armour", 8),
    ESTATE_PLANK("Estate shop", 66, true),//try it

    COOKING_FISH("Fish", 4),

    BLOOD_MONEY_MISC("Misc", 9, true),
    BLOOD_MONEY_BARROWS("Barrows", 10, true),
    BLOOD_MONEY_UNTRADEABLES("Untradeable", 11, true),

    RANGE_WEAPONS("Weapons", 12),
    RANGE_ARMOUR("Armour", 13),
    RANGE_AMMUNITION("Ammunition", 14),

    MELEE_WEAPONS("Weapons", 3),
    MELEE_ARMOUR("Armour", 15),

    BOSS_POINT_STORE("Misc", 16, true),

    SKILL_HOODS("Hoods", 17, true),
    SKILL_CAPES("Capes", 18, true),
    SKILL_CAPES_T("Capes (t)", 19, true),

    MERCHANT("Merchant", 20, true),

    GENERAL_STORE("General", 21),

    MINING_TOOLS("Tools", 22),
    MINING_BARS("Bars", 23),

    STARDUST("Rewards", 24, true),

    CASTLE_WARS("Rewards", 26),

    TRIVIA_STORE("Rewards", 27, true),

    PRESTIGE_STORE("Rewards", 29, true),

    DUNGEONEERING_COIN_STORE("Stock", 30, true),

    DUNGEONEERING_TOKEN_STORE("Rewards", 31, true),

    CONSUMABLES("Stock", 32),

    CLOTHES("Stock", 33, true),

    WILFRED_TOOLS("Tools", 34),

    WILFRED_LOGS("Logs", 35),

    FIREMAKING_STORE("Stock", 36),

    FISHING_STORE("Stock", 37),

    FARMING_STORE("Stock", 38),

    SUMMONING_MATERIALS_I("Materials I", 39, true),

    SUMMONING_MATERIALS_II("Materials II", 40, true),

    HERBLORE_STORE("Materials", 41, true),

    RUNECRAFTING_STORE("Stock", 42, true),

    ENERGY_FRAGMENT_STORE("Rewards", 43, true),

    CRAFTING_STORE("Stock", 44),

    NOMADS_SHOP("Rewards", 45, true),

    HUNTER_SHOP("Stock", 46, true),

    AGILITY_SHOP("Stock", 47, true),

    TOKKUL_SHOP("Stock", 50, true),

    IRONMAN_SHOP("Stock", 51, true),

    DONATOR_FLASKS("Flasks", 52, true),

    DONATOR_EXTREME("Extreme", 53, true),

    VOTE_SHOP("Stock", 54, true),

            ;

    private String name;
    private int shopId;
    private boolean ironman;

    ShopData(String name, int shopId) {
        this.name = name;
        this.shopId = shopId;
    }

    ShopData(String name, int shopId, boolean ironman) {
        this.name = name;
        this.shopId = shopId;
        this.ironman = ironman;
    }

    public String getName() {
        return name;
    }

    public int getShopId() {
        return shopId;
    }

    public boolean isIronman() {
        return ironman;
    }
}
