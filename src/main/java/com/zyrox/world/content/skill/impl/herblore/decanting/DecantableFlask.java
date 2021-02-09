package com.zyrox.world.content.skill.impl.herblore.decanting;

import com.zyrox.world.entity.impl.player.Player;

/**
 * Represents decantable flaasks
 *
 * @author 2012.
 */
public enum DecantableFlask {

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
	SARADOMIN_BREW_FLASH(14427, 14425, 14423, 14421, 14419, 14417),
	OVERLOAD_FLASK(14301, 14299, 14297, 14295, 14293, 14921);

	private final int[] itemIds;

	DecantableFlask(int... itemIds) {
		this.itemIds = itemIds;
	}

	private static final DecantableFlask[] flasks = DecantableFlask.values();

	public static DecantableFlask forId(int itemId) {
		for (DecantableFlask pot : DecantableFlask.values()) {
			for (int pot_id : pot.getIds()) {
				if (pot_id == itemId) {
					return pot;
				}
			}
		}
		return null;
	}

	public int getDoseForId(int itemId) {
		if (itemId == this.getIds()[0]) {
			return 6;
		}
		if (itemId == this.getIds()[1]) {
			return 5;
		}
		if (itemId == this.getIds()[2]) {
			return 4;
		}
		if (itemId == this.getIds()[3]) {
			return 3;
		}
		if (itemId == this.getIds()[4]) {
			return 2;
		}
		if (itemId == this.getIds()[5]) {
			return 1;
		}
		return -1;
	}

	public int getIdForDose(int itemdose) {
		if (itemdose == 6) {
			return this.getIds()[0];
		}
		if (itemdose == 5) {
			return this.getIds()[1];
		}
		if (itemdose == 4) {
			return this.getIds()[2];
		}
		if (itemdose == 3) {
			return this.getIds()[3];
		}
		if (itemdose == 2) {
			return this.getIds()[4];
		}
		if (itemdose == 1) {
			return this.getIds()[5];
		}
		return -1;
	}

	public static boolean combinePotion(Player p, int firstPotID, int secondPotID) {
		DecantableFlask potion = DecantableFlask.forId(firstPotID);
		if (potion == null || !p.getInventory().contains(firstPotID) || !p.getInventory().contains(secondPotID))
			return false;
		if (potion.getDoseForId(secondPotID) > 0) {
			int firstPotAmount = potion.getDoseForId(firstPotID);
			int secondPotAmount = potion.getDoseForId(secondPotID);
			if (firstPotAmount + secondPotAmount <= 4) {
				p.getInventory().delete(firstPotID, 1);
				p.getInventory().delete(secondPotID, 1);
				p.getInventory().add(potion.getIdForDose(firstPotAmount + secondPotAmount), 1, "Potion combinating");
			} else {
				int overflow = (firstPotAmount + secondPotAmount) - 6;
				p.getInventory().delete(firstPotID, 1);
				p.getInventory().delete(secondPotID, 1);
				p.getInventory().add(potion.getIdForDose(6), 1, "Potion combinating");
				p.getInventory().add(potion.getIdForDose(overflow), 1, "Potion combinating");
			}
			return true;
		}
		return false;
	}

	public static void decantAllFlask(Player player) {
		for (DecantableFlask flask : flasks) {
			int totalDoses = 0;
			for (int i = 1; i < 7; i++) {
				if (player.getInventory().contains(flask.getIdForDose(i))) {
					int amount = player.getInventory().getAmount(flask.getIdForDose(i));
					totalDoses += (i * amount);
					player.getInventory().delete(flask.getIdForDose(i), amount);
				}
			}

			while (totalDoses > 0) {
				if (totalDoses >= 6) {
					player.getInventory().add(flask.getIdForDose(6), 1, "Decanting");
					totalDoses -= 6;
				} else {
					for(int i = 1; i < 6; i++) {
						if (totalDoses == i) {
							player.getInventory().add(flask.getIdForDose(i), 1, "Decanting");
							totalDoses -= i;
						}
					}
				}
			}
		}
	}

	public int[] getIds() {
		return itemIds;
	}
}
