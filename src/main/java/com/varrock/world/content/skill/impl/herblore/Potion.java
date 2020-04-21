package com.varrock.world.content.skill.impl.herblore;

import com.varrock.GameSettings;

public enum Potion {

	STRENGTH(113, 115, 117, 119), ATTACK(2428, 121, 123, 125), RESTORE(2430, 127, 129, 131),
	DEFENCE(2432, 133, 135, 137), PRAYER(2434, 139, 141, 143), FISHING(2438, 151, 153, 155),
	RANGING(2444, 169, 171, 173), ANTIFIRE(2452, 2454, 2456, 2458), ENERGY(3008, 3010, 3012, 3014),
	AGILITY(3032, 3034, 3036, 3038), MAGIC(3040, 3042, 3044, 3046), COMBAT(9739, 9741, 9743, 9745),
	SUPER_COMBAT(GameSettings.OSRS_ITEM_OFFSET + 12695, GameSettings.OSRS_ITEM_OFFSET + 12697,
			GameSettings.OSRS_ITEM_OFFSET + 12699, GameSettings.OSRS_ITEM_OFFSET + 12701),
	SUMMONING(12140, 12142, 12144, 12146), SUPER_ATTACK(2436, 145, 147, 149), SUPER_STRENGTH(2440, 157, 159, 161),
	SUPER_DEFENCE(2442, 163, 165, 167), SUPER_ENERGY(3016, 3018, 3020, 3022), SUPER_RESTORE(3024, 3026, 3028, 3030),
	SUPER_PRAYER(15328, 15329, 15330, 15331), OVERLOAD(15332, 15333, 15334, 15335),
	SUPER_ANTIFIRE(15304, 15305, 15306, 15307), EXTREME_ATTACK(15308, 15309, 15310, 15311),
	EXTREME_STRENGTH(15312, 15313, 15314, 15315), EXTREME_DEFENCE(15316, 15317, 15318, 15319),
	EXTREME_MAGIC(15320, 15321, 15322, 15323), SARADOMIN_BREW(6685, 6687, 6689, 6691),
	EXTREME_RANGING(15324, 15325, 15326, 15327),
	ZAMORAK_BREW(2450, 189, 191, 193),
	SUPER_ANTIPOISON(2448, 181, 183, 185),
	ANTIPOISON(2446, 175, 177, 179),
	RECOVER_SPECIAL(15300, 15301, 15302, 15303),

	SUPER_ATTACK_FLASK(14188, 14186, 14184, 14182, 14180, 14178),
	SUPER_STR_FLASK(14176, 14174, 14172, 14170, 14168, 14166),
	SUPER_DEF_FLASK(14164, 14162, 14160, 14158, 14156, 14154),
	RANGING_FLASK(14152, 14150, 14148, 14146, 14144, 14142),
	SARA_BREW_FLASK(14128, 14126, 14124, 14122, 14120, 14118),
	SUPER_RESTORE_FLASK(14415, 14413, 14411, 14409, 14407, 14405),
	MAGIC_FLASK(14403, 14401, 14399, 14397, 14395, 14393),
	RECOVER_SPEC_FLASK(14385, 14383, 14381, 14379, 14377, 14375),
	EXTREME_ATTACK_FLASK(14373, 14371, 14369, 14367, 14365, 14363),
	EXTREME_STR_FLASK(14361, 14359, 14357, 14355, 14353, 15351),
	EXTREME_DEF_FLASK(14349, 14347, 14345, 14343, 14341, 14339),
	EXTREME_MAGIC_FLASK(14337, 14335, 14333, 14331, 14329, 14327),
	EXTREME_RANGE_FLASK(14325, 14323, 14321, 14319, 14317, 14315),
	OVERLOAD_FLASK(14301, 14299, 14297, 14295, 14293, 14921);

	Potion(int fullId, int threeQuartersId, int halfId, int quarterId) {
		this.quarterId = quarterId;
		this.halfId = halfId;
		this.threeQuartersId = threeQuartersId;
		this.fullId = fullId;
	}

	Potion(int flaskSix, int flaskFive, int flaskFour, int flaskThree, int flaskTwo, int flaskOne) {
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

	private int quarterId, halfId, threeQuartersId, fullId;

	public int getQuarterId() {
		return this.quarterId;
	}

	public int getHalfId() {
		return this.halfId;
	}

	public int getThreeQuartersId() {
		return this.threeQuartersId;
	}

	public int getFullId() {
		return this.fullId;
	}
}
