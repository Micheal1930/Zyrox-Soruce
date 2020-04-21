package com.varrock.world.content.skill.impl.herblore;

public enum FinishedPotions {

	MISSING_INGRIDIENTS(-1, -1, -1, -1, -1),
	ATTACK_POTION(121, 91, 221, 1, 25),
	ANTIPOISON(175, 93, 235, 5, 37.5),
	STRENGTH_POTION(115, 95, 225, 12, 50),
	RESTORE_POTION(127, 97, 223, 22, 62.5),
	ENERGY_POTION(3010, 97, 1975, 26, 68),
	DEFENCE_POTION(133, 99, 239, 30, 75),
	AGILITY_POTION(3034, 3002, 2152, 34, 80),
	COMBAT_POTION(9741, 97, 9736, 36, 84),
	PRAYER_POTION(139, 99, 231, 38, 87.5),
	SUMMONING_POTION(12142, 12181, 12109, 40, 92),
	CRAFTING_POTION(14840, 14856, 5004, 42, 92),
	SUPER_ATTACK(145, 101, 221, 45, 100),
	VIAL_OF_STENCH(18661, 101, 1871, 46, 0),
	FISHING_POTION(181, 101, 231, 48, 106),
	SUPER_ENERGY(3018, 103, 2970, 52, 117.5),
	SUPER_STRENGTH(157, 105, 225, 55, 125),
	WEAPON_POISON(187, 105, 241, 60, 138),
	SUPER_RESTORE(3026, 3004, 223, 63, 142.5),
	SUPER_DEFENCE(163, 107, 239, 66, 150),
	ANTIFIRE(2454, 2483, 241, 69, 157.5),
	RANGING_POTION(169, 109, 245, 72, 162.5),
	MAGIC_POTION(3042, 2483, 3138, 76, 172.5),
	ZAMORAK_BREW(189, 111, 247, 78, 175),
	SARADOMIN_BREW(6687, 3002, 6693, 81, 180),
	RECOVER_SPECIAL(15301, 3018, 5972, 84, 200),
	SUPER_ANTIFIRE(15305, 2454, 4621, 85, 210),
	SUPER_PRAYER(15329, 139, 4255, 94, 270),
	SUPER_ANTIPOISON(181, 101, 235, 48, 103),
	HUNTER_POTION(10000, 103, 10111, 53, 110),
	FLETCHING_POTION(14848, 103, 11525, 58, 105),
	ANTIPOISON_PLUS(5945, 3002, 6049, 68, 154),
	
    PRAYER_FLASK(14200, 14198, 14196, 14194, 14192, 14190),
    SUPER_ATTACK_FLASK(14188, 14186, 14184, 14182, 14180, 14178),
    SUPER_STR_FLASK(14176, 14174, 14172, 14170, 14168, 14166),
    SUPER_DEF_FLASK(14164, 14162, 14160, 14158, 14156, 14154),
    RANGING_FLASK(14152, 14150, 14148, 14146, 14144, 14142),
    SARA_BREW_FLASK(14128, 14126, 14124, 14122, 14420, 14418),
    SUPER_RESTORE_FLASK(14415, 14413, 14411, 14409, 14407, 14405),
    MAGIC_FLASK(14403, 14401, 14399, 14397, 14395, 14393),
    RECOVER_SPEC_FLASK(14385, 14383, 14381, 14379, 14377, 14375),
    EXTREME_ATTACK_FLASK(14373, 14371, 14369, 14367, 14365, 14363),
    EXTREME_STR_FLASK(14361, 14359, 14357, 14355, 14353, 15351),
    EXTREME_DEF_FLASK(14349, 14347, 14345, 14343, 14341, 14339),
    EXTREME_MAGIC_FLASK(14337, 14335, 14333, 14331, 14329, 14327),
    EXTREME_RANGE_FLASK(14325, 14323, 14321, 14319, 14317, 14315),
    OVERLOAD_FLASK(14301, 14299, 14297, 14295, 14293, 14921);


    FinishedPotions(int flaskSix, int flaskFive, int flaskFour, int flaskThree, int flaskTwo, int flaskOne) {
        this.flaskSix = flaskSix;
        this.flaskFive = flaskFive;
        this.flaskFour = flaskFour;
        this.flaskThree = flaskThree;
        this.flaskTwo = flaskTwo;
        this.flaskOne = flaskOne;
    }

    private int flaskSix, flaskFive, flaskFour, flaskThree, flaskTwo, flaskOne;

    public int getFlaskSix() {
        return flaskSix;
    }

    public int getFlaskFive() {
        return flaskFive;
    }

    public int getFlaskFour() {
        return flaskFour;
    }

    public int getFlaskThree() {
        return flaskThree;
    }

    public int getFlaskTwo() {
        return flaskTwo;
    }

    public int getFlaskOne() {
        return flaskOne;
    }
    
	private int finishedPotion, unfinishedPotion, itemNeeded, levelReq;

    private double expGained;

	private FinishedPotions(int finishedPotion, int unfinishedPotion, int itemNeeded, int levelReq, double expGained) {
		this.finishedPotion = finishedPotion;
		this.unfinishedPotion = unfinishedPotion;
		this.itemNeeded = itemNeeded;
		this.levelReq = levelReq;
		this.expGained = expGained;
	}

	public int getFinishedPotion(){
		return finishedPotion;
	}

	public int getUnfinishedPotion() {
		return unfinishedPotion;
	}

	public int getItemNeeded() {
		return itemNeeded;
	}

	public int getLevelReq() {
		return levelReq;
	}

	public double getExpGained() {
		return expGained;
	}

	public static FinishedPotions forId(int id, int id2) {
		boolean hasOnePart = false;
		for(FinishedPotions pot : FinishedPotions.values()){
			if ((pot.getUnfinishedPotion() == id || pot.getUnfinishedPotion() == id2)) {
				hasOnePart = true;
			}
			if((pot.getItemNeeded() == id || pot.getItemNeeded() == id2) && (pot.getUnfinishedPotion() == id || pot.getUnfinishedPotion() == id2)) {
				return pot;
			}
		}
		return hasOnePart ? MISSING_INGRIDIENTS : null;
	}

	public static FinishedPotions forFinishedPotion(int id) {
		for(FinishedPotions pot : FinishedPotions.values()) {
			if(pot.getFinishedPotion() == id) {
				return pot;
			}
		}
		return null;
	}
}