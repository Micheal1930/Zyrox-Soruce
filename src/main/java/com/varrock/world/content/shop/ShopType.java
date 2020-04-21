package com.varrock.world.content.shop;

import com.varrock.world.entity.impl.npc.click_type.NpcClickType;

/**
 * Created by Jonny on 8/28/2019
 **/
public enum ShopType {
    ESTATE_SHOP(4247,
            new ShopData[] {
                    ShopData.ESTATE_PLANK,
            }, NpcClickType.FIRST_CLICK),
    MAGIC_SHOP(546,
            new ShopData[] {
                    ShopData.MAGIC_ARMOUR,
                    ShopData.MAGIC_RUNES,
                    ShopData.MAGIC_STAFFS,
            }, NpcClickType.FIRST_CLICK, NpcClickType.SECOND_CLICK),

    RANGE_SHOP(683,
            new ShopData[] {
                    ShopData.RANGE_WEAPONS,
                    ShopData.RANGE_ARMOUR,
                    ShopData.RANGE_AMMUNITION,
            }, NpcClickType.FIRST_CLICK, NpcClickType.SECOND_CLICK),

    MELEE_SHOP(541,
            new ShopData[] {
                    ShopData.MELEE_WEAPONS,
                    ShopData.MELEE_ARMOUR,
            }, NpcClickType.FIRST_CLICK, NpcClickType.SECOND_CLICK),

    COOKING_SHOP(278,
            new ShopData[] {
                    ShopData.COOKING_FISH
            }, NpcClickType.FIRST_CLICK, NpcClickType.SECOND_CLICK),

    CHARLIE_COOKING_SHOP(794,
            new ShopData[] {
                    ShopData.COOKING_FISH
            }, NpcClickType.FIRST_CLICK, NpcClickType.SECOND_CLICK),

    BLOOD_MONEY_SHOP(23390, true,
            new ShopData[] {
                    ShopData.BLOOD_MONEY_MISC,
                    ShopData.BLOOD_MONEY_UNTRADEABLES,
                    ShopData.BLOOD_MONEY_BARROWS,
            }, NpcClickType.SECOND_CLICK, NpcClickType.THIRD_CLICK),

    BOSS_POINT_SHOP(241,
            new ShopData[] {
                    ShopData.BOSS_POINT_STORE,
            }, NpcClickType.FIRST_CLICK),

    SKILL_CAPE_SHOP(2253, true,
            new ShopData[] {
                    ShopData.SKILL_CAPES,
                    ShopData.SKILL_CAPES_T,
                    ShopData.SKILL_HOODS,
            }, NpcClickType.SECOND_CLICK, NpcClickType.THIRD_CLICK, NpcClickType.FOURTH_CLICK),

    MERCHANT_SHOP(2292,
            new ShopData[] {
                    ShopData.MERCHANT,
            }, NpcClickType.FIRST_CLICK, NpcClickType.SECOND_CLICK),

    GENERAL_STORE(520,
            new ShopData[] {
                    ShopData.GENERAL_STORE,
            }, NpcClickType.FIRST_CLICK, NpcClickType.SECOND_CLICK),

    MINING_SHOP(948,
            new ShopData[] {
                    ShopData.MINING_TOOLS,
                    ShopData.MINING_BARS,
            }, NpcClickType.FIRST_CLICK, NpcClickType.SECOND_CLICK),

    STARDUST_SHOP(1396,
            new ShopData[] {
                    ShopData.STARDUST,
            }, NpcClickType.SECOND_CLICK),

    CASTLE_WARS_SHOP(1526,
            new ShopData[] {
                    ShopData.CASTLE_WARS,
            }, NpcClickType.FIRST_CLICK, NpcClickType.THIRD_CLICK),

    TRIVIA_SHOP(725,
            new ShopData[] {
                    ShopData.TRIVIA_STORE,
            }, NpcClickType.FIRST_CLICK, NpcClickType.THIRD_CLICK),

    PRESTIGE_SHOP(2579, true,
            new ShopData[] {
                    ShopData.PRESTIGE_STORE,
            }, NpcClickType.SECOND_CLICK),

    DUNGEONEERING_COIN_SHOP(11226,
            new ShopData[] {
                    ShopData.DUNGEONEERING_COIN_STORE,
            }, NpcClickType.FIRST_CLICK, NpcClickType.SECOND_CLICK),

    CONSUMABLES(2538,
            new ShopData[] {
                    ShopData.CONSUMABLES,
            }, NpcClickType.FIRST_CLICK, NpcClickType.SECOND_CLICK),

    CLOTHES_STORE(548,
            new ShopData[] {
                    ShopData.CLOTHES,
            }, NpcClickType.FIRST_CLICK, NpcClickType.SECOND_CLICK),

    WILFRED_LOGS(4906,
            new ShopData[] {
                    ShopData.WILFRED_TOOLS,
                    ShopData.WILFRED_LOGS,
            }, NpcClickType.FIRST_CLICK, NpcClickType.SECOND_CLICK),

    FIREMAKING_STORE(4946,
            new ShopData[] {
                    ShopData.FIREMAKING_STORE,
            }, NpcClickType.FIRST_CLICK, NpcClickType.SECOND_CLICK),

    FISHING_STORE(308,
            new ShopData[] {
                    ShopData.FISHING_STORE,
            }, NpcClickType.FIRST_CLICK, NpcClickType.SECOND_CLICK),

    FARMING_STORE(3299,
            new ShopData[] {
                    ShopData.FARMING_STORE,
            }, NpcClickType.FIRST_CLICK, NpcClickType.SECOND_CLICK),

    SUMMONING_STORE(6970,
            new ShopData[] {
                    ShopData.SUMMONING_MATERIALS_I,
                    ShopData.SUMMONING_MATERIALS_II,
            }, NpcClickType.FIRST_CLICK),

    HERBLORE_STORE(8459,
            new ShopData[] {
                    ShopData.HERBLORE_STORE,
            }, NpcClickType.FIRST_CLICK),

    RUNECRAFTING_STORE(462,
            new ShopData[] {
                    ShopData.RUNECRAFTING_STORE,
            }, NpcClickType.FIRST_CLICK),

    ENERGY_FRAGMENT_STORE(1263,
            new ShopData[] {
                    ShopData.ENERGY_FRAGMENT_STORE,
            }, NpcClickType.FIRST_CLICK),

    CRAFTING_STORE(805,
            new ShopData[] {
                    ShopData.CRAFTING_STORE,
            }, NpcClickType.FIRST_CLICK),

  /*  NOMADS_SHOP(8591,
            new ShopData[] {
                    ShopData.NOMADS_SHOP,
            }, NpcClickType.SECOND_CLICK),*/

    HUNTING_SHOP(5112,
            new ShopData[] {
                    ShopData.HUNTER_SHOP,
            }, NpcClickType.FIRST_CLICK, NpcClickType.SECOND_CLICK),

    TOKKUL_SHOP(2622,
            new ShopData[] {
                    ShopData.TOKKUL_SHOP,
            }, NpcClickType.FIRST_CLICK, NpcClickType.SECOND_CLICK),

    IRONMAN_SHOP(15311,
            new ShopData[] {
                    ShopData.IRONMAN_SHOP,
            }, NpcClickType.FIRST_CLICK, NpcClickType.SECOND_CLICK),

    DONATOR_SHOP(5111,
            new ShopData[] {
                    ShopData.DONATOR_FLASKS, ShopData.DONATOR_EXTREME,
            }, NpcClickType.FIRST_CLICK, NpcClickType.SECOND_CLICK, NpcClickType.THIRD_CLICK),

    DONATOR_STORE(741,
            new ShopData[] {
            		ShopData.DONATOR_EQUIPMENT, ShopData.DONATOR_MISC, ShopData.DONATOR_RARES, ShopData.DONATOR_SUPERRARES,
            }, NpcClickType.FIRST_CLICK, NpcClickType.SECOND_CLICK, NpcClickType.THIRD_CLICK),

    VOTE_SHOP(605,
            new ShopData[] {
                    ShopData.VOTE_SHOP
            }, NpcClickType.SECOND_CLICK),

    ;
	/*
			
	DONATOR_STORE(741,
			new ShopData[] {
					ShopData.DONATOR_EQUIPMENT , ShopData.DONATOR_MISC, ShopData.DONATOR_RARES, ShopData.DONATOR_SUPERRARES,
			}, NpcClickType.FIRST_CLICK),*/

    private int npcId;
    private ShopData[] shopData;
    private NpcClickType[] npcClickType;
    private boolean followClickType;

    ShopType(int npcId, ShopData[] shopData, NpcClickType... npcClickType) {
        this.npcId = npcId;
        this.shopData = shopData;
        this.npcClickType = npcClickType;
    }

    ShopType(int npcId, boolean followClickType, ShopData[] shopData, NpcClickType... npcClickType) {
        this.npcId = npcId;
        this.followClickType = followClickType;
        this.shopData = shopData;
        this.npcClickType = npcClickType;
    }

    public int getNpcId() {
        return npcId;
    }

    public NpcClickType[] getNpcClickType() {
        return npcClickType;
    }

    public ShopData[] getShopData() {
        return shopData;
    }

    public boolean isFollowClickType() {
        return followClickType;
    }}
