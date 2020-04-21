package com.varrock.world.content.skill.impl.herblore;

import com.varrock.world.content.skill.impl.herblore.decanting.DecantableFlask;
import com.varrock.world.entity.impl.player.Player;

/**
 * Combinates potions into doses
 * @author Gabriel Hannason
 */
public class PotionCombinating {
	
	public static void startCombiningPotion(Player p, int firstPotID, int secondPotID) {
		if(DecantableFlask.combinePotion(p, firstPotID, secondPotID)) {
			return;
		}
		
		CombiningDoses potion = CombiningDoses.getPotionByID(firstPotID);

		if (potion == null || !p.getInventory().contains(firstPotID) || !p.getInventory().contains(secondPotID))
			return;

		if (potion.getDoseForID(secondPotID) > 0) {
			int firstPotAmount = potion.getDoseForID(firstPotID);
			int secondPotAmount = potion.getDoseForID(secondPotID);
			if (firstPotAmount + secondPotAmount <= 4) {
				p.getInventory().delete(firstPotID, 1);
				p.getInventory().delete(secondPotID, 1);
				p.getInventory().add(potion.getIDForDose(firstPotAmount + secondPotAmount), 1, "Potion combinating");
				p.getInventory().add(EMPTY_VIAL, 1, "Potion combinating");
			} else {
				int overflow = (firstPotAmount + secondPotAmount) - 4;
				p.getInventory().delete(firstPotID, 1);
				p.getInventory().delete(secondPotID, 1);
				p.getInventory().add(potion.getIDForDose(4), 1, "Potion combinating");
				p.getInventory().add(potion.getIDForDose(overflow), 1, "Potion combinating");
			}
		}
	}
	
	private static final int VIAL = 227;
	private static final int EMPTY_VIAL = 229;

	public enum CombiningDoses {

		STRENGTH(119, 117, 115, 113, VIAL, "Strength"),
		SUPER_STRENGTH(161, 159, 157, 2440, VIAL, "Super strength"),
		ATTACK(125, 123, 121,2428, VIAL, "Attack"),
		SUPER_ATTACK(149, 147, 145, 2436, VIAL, "Super attack"), 
		DEFENCE(137, 135, 133, 2432, VIAL, "Defence"),
		SUPER_DEFENCE(167, 165, 163, 2442, VIAL, "Super defence"),
		RANGING_POTION(173, 171, 169, 2444, VIAL, "Ranging"),
		FISHING(155, 153, 151,2438, VIAL, "Fishing"), 
		PRAYER(143, 141, 139, 2434, VIAL,"Prayer"),
		ANTIFIRE(2458, 2456, 2454, 2452, VIAL, "Antifire"),
		ZAMORAK_BREW(193, 191, 189, 2450, VIAL, "Zamorakian brew"),
		ANTIPOISON(179, 177, 175, 2446, VIAL, "Antipoison"),
		RESTORE(131, 129, 127, 2430, VIAL, "Restoration"),
		MAGIC_POTION(3046, 3044, 3042, 3040, VIAL, "Magic"),
		SUPER_RESTORE(3030, 3028, 3026, 3024, VIAL, "Super Restoration"),
		ENERGY(3014, 3012, 3010, 3008, VIAL, "Energy"),
		SUPER_ENERGY(3022, 3020, 3018, 3016, VIAL, "Super Energy"),
		AGILITY(3038, 3036, 3034, 3032, VIAL, "Agility"), 
		SARADOMIN_BREW(6691, 6689, 6687, 6685, VIAL, "Saradomin brew"),
		ANTIPOISON1(5949, 5947, 5945, 5943, VIAL, "Antipoison(+)"),
		ANTIPOISON2(5958, 5956, 5954, 5952, VIAL, "Antipoison(++)"),
		SUPER_ANTIPOISON(185, 183, 181, 2448, VIAL, "Super Antipoison"),
		RELICYMS_BALM(4848, 4846, 4844, 4842, VIAL, "Relicym's balm"),
		SERUM_207(3414, 3412, 3410, 3408, VIAL, "Serum 207"),
		COMBAT(9745, 9743, 9741, 9739, VIAL, "Combat"),
		EXTR_RANGE(15327, 15326, 15325, 15324, VIAL, "Extreme ranging"),
		EXTR_STR(15315, 15314, 15313, 15312, VIAL, "Extreme stength"),
		EXTR_MAGE(15323, 15322, 15321, 15320, VIAL, "Extreme magic"),
		EXTR_ATK(15311, 15310, 15309, 15308, VIAL, "Extreme attack"),
		EXTR_DEF(15319, 15318, 15317, 15316, VIAL, "Extreme defence"),
		SUPER_PRAYER(15331, 15330, 15329, 15328, VIAL, "Super prayer"),
		OVERLOAD(15335, 15334, 15333, 15332, VIAL, "Overload"),
		SUPER_FIRE(15307, 15306, 15305, 15304, VIAL, "Super antifire"),
		REC_SPEC(15303, 15302, 15301, 15300, VIAL, "Recover special"),
		
		PRAYER_FLASK(
				14200,
				14198,
				14196,
				14194,
				14192,
				14190,
				VIAL,
				"Prayer Flask"), SUPER_ATTACK_FLASK(
						14188,
						14186,
						14184,
						14182,
						14180,
						14178,
						VIAL,
						"Super Attack Flask"), SUPER_STR_FLASK(
								14176,
								14174,
								14172,
								14170,
								14168,
								14166,
								VIAL,
								"Super Strength Flask"), SUPER_DEF_FLASK(
										14164,
										14162,
										14160,
										14158,
										14156,
										14154,
										VIAL,
										"Super Defence Flask"), RANGING_FLASK(
												14152,
												14150,
												14148,
												14146,
												14144,
												14142,
												VIAL,
												"Ranging Flask"), SARA_BREW_FLASK(
														14128,
														14126,
														14124,
														14122,
														14420,
														14418,
														VIAL,
														"Saradomin Brew Flask"), SUPER_RESTORE_FLASK(
																14415,
																14413,
																14411,
																14409,
																14407,
																14405,
																VIAL,
																"Super Restore Flask"), MAGIC_FLASK(
																		14403,
																		14401,
																		14399,
																		14397,
																		14395,
																		14393,
																		VIAL,
																		"Magic Flask"), RECOVER_SPEC_FLASK(
																				14385,
																				14383,
																				14381,
																				14379,
																				14377,
																				14375,
																				VIAL,
																				"Recover Special Flask"), EXTREME_ATTACK_FLASK(
																						14373,
																						14371,
																						14369,
																						14367,
																						14365,
																						14363,
																						VIAL,
																						"Extreme Attack Flask"), EXTREME_STR_FLASK(
																								14361,
																								14359,
																								14357,
																								14355,
																								14353,
																								15351,
																								VIAL,
																								"Extreme Strength Flask"), EXTREME_DEF_FLASK(
																										14349,
																										14347,
																										14345,
																										14343,
																										14341,
																										14339,
																										VIAL,
																										"Extreme Defence Flask"), EXTREME_MAGIC_FLASK(
																												14337,
																												14335,
																												14333,
																												14331,
																												14329,
																												14327,
																												VIAL,
																												"Extreme Magic Flask"), EXTREME_RANGE_FLASK(
																														14325,
																														14323,
																														14321,
																														14319,
																														14317,
																														14315,
																														VIAL,
																														"Extreme Range Flask"), OVERLOAD_FLASK(
																																14301,
																																14299,
																																14297,
																																14295,
																																14293,
																																14921,
																																VIAL,
																																"Overload Flask");


		/*
		 * This is what the data in the above enumeration is, in order. EX:
		 * COMBAT(oneDosePotionID, twoDosePotionID, threeDosePotionID,
		 * fourDosePotionID, vial, "potionName");
		 */

		int oneDosePotionID, twoDosePotionID, threeDosePotionID,
				fourDosePotionID, vial;
		String potionName;

		/**
		 * @param oneDosePotionID
		 *            - This is the ID for the potion when it contains one dose.
		 * 
		 * @param twoDosePotionID
		 *            - This is the ID for the potion when it contains two
		 *            doses.
		 * 
		 * @param threeDosePotionID
		 *            - This is the ID for the potion when it contains three
		 *            doses.
		 * 
		 * @param fourDosePotionID
		 *            - This is the ID for the (full) potion when it contains
		 *            four doses.
		 * 
		 * @param vial
		 *            - This is referenced from: private static final int VIAL =
		 *            227; It's a constant and its value never changes.
		 * 
		 * @param potionName
		 *            - This is a string which is used to set a name for the
		 *            potion. Within an enumeration you can use the name().
		 *            method to take the name in-front of the stored data. This
		 *            however could not be done because of some potion names.
		 */

		private CombiningDoses(int oneDosePotionID, int twoDosePotionID,
				int threeDosePotionID, int fourDosePotionID, int vial,
				String potionName) {
			this.oneDosePotionID = oneDosePotionID;
			this.twoDosePotionID = twoDosePotionID;
			this.threeDosePotionID = threeDosePotionID;
			this.fourDosePotionID = fourDosePotionID;
			this.vial = vial;
			this.potionName = potionName;
		}
		
		private CombiningDoses(int flaskSix, int flaskFive, int flaskFour, int flaskThree, int flaskTwo, int flaskOne,
				int vial, String name) {
			this.flaskSix = flaskSix;
			this.flaskFive = flaskFive;
			this.flaskFour = flaskFour;
			this.flaskThree = flaskThree;
			this.flaskTwo = flaskTwo;
			this.flaskOne = flaskOne;
			this.vial = vial;
			this.name = name;
		}
		private String name;

		public String getName() {
			return name;
		}

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
		private int flaskSix, flaskFive, flaskFour, flaskThree, flaskTwo, flaskOne;

		/*
		 * These are code getters to use the data stored in the above
		 * enumeration.
		 */

		public int getDoseID1() {
			return oneDosePotionID;
		}

		public int getDoseID2() {
			return twoDosePotionID;
		}

		public int getDoseID3() {
			return threeDosePotionID;
		}

		public int getFourDosePotionID() {
			return fourDosePotionID;
		}

		public int getVial() {
			return vial;
		}

		public String getPotionName() {
			return potionName;
		}

		/**
		 * 
		 * @param id
		 * @return The dose that this id represents for this potion, or -1 if it
		 *         doesn't belong to this potion.
		 * @date 2/28/12
		 * @author 0021sordna
		 */

		public int getDoseForID(int id) {
			if (id == this.oneDosePotionID) {
				return 1;
			}
			if (id == this.twoDosePotionID) {
				return 2;
			}
			if (id == this.threeDosePotionID) {
				return 3;
			}
			if (id == this.fourDosePotionID) {
				return 4;
			}
			return -1;
		}

		/**
		 * 
		 * @param dose
		 * @return The ID for this dose of the potion, or -1 if this dose
		 *         doesn't exist.
		 * @date 2/28/12
		 * @author 0021sordna
		 */

		public int getIDForDose(int dose) {
			if (dose == 1) {
				return this.oneDosePotionID;
			}
			if (dose == 2) {
				return this.twoDosePotionID;
			}
			if (dose == 3) {
				return this.threeDosePotionID;
			}
			if (dose == 4) {
				return this.fourDosePotionID;
			}
			if (dose == 0) {
				return EMPTY_VIAL;
			}
			return -1;
		}

		/**
		 * 
		 * @param ID
		 * @return The potion that matches the ID. ID can be any dose of the
		 *         potion.
		 * @date 2/28/12
		 * @author 0021sordna
		 */

		public static CombiningDoses getPotionByID(int id) {
			for (CombiningDoses potion : CombiningDoses.values()) {
				if (id == potion.oneDosePotionID) {
					return potion;
				}
				if (id == potion.twoDosePotionID) {
					return potion;
				}
				if (id == potion.threeDosePotionID) {
					return potion;
				}
				if (id == potion.fourDosePotionID) {
					return potion;
				}
			}
			return null;
		}
	}	
}
