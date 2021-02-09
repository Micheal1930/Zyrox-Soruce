package com.zyrox.world.content;

import com.zyrox.model.Animation;
import com.zyrox.model.Graphic;
import com.zyrox.model.Item;
import com.zyrox.model.Skill;
import com.zyrox.world.entity.impl.player.Player;

/**
 * Handles enchanting of interfaces calculations such as combat level and total
 * level.
 * 
 * @author Jonathan Sirens
 * @author High105
 */

public class Enchanting {

	enum Data {

		OPAL(49006, 4, 49009, 879, 9236, new Item[] { new Item(564, 1), new Item(556, 2) }, 9),
		SAPPHIRE(49015, 7, 49017, 9337, 9240, new Item[] { new Item(564, 2), new Item(555, 4) }, 17),
		JADE(49023, 14, 49025, 9335, 9237, new Item[] { new Item(564, 3), new Item(557, 6) }, 19),
		PEARL(49031, 24, 49033, 880, 9238, new Item[] { new Item(564, 4), new Item(555, 8) }, 29),
		EMERALD(49039, 27, 49041, 9338, 9241, new Item[] { new Item(564, 5), new Item(561, 5) }, 37),
		RED_TOPAZ(49047, 29, 49049, 9336, 9239, new Item[] { new Item(564, 6), new Item(554, 12) }, 33),
		RUBY(49055, 49, 49057, 9339, 9242, new Item[] { new Item(565, 5), new Item(9075, 50) }, 59),
		DIAMOND(49063, 57, 49065, 9340, 9243, new Item[] { new Item(563, 5), new Item(9075, 75) }, 67),
		DRAGONSTONE(49071, 68, 49073, 9341, 9244, new Item[] { new Item(566, 5), new Item(9075, 100) }, 78),
		ONYX(49079, 87, 49081, 9342, 9245, new Item[] { new Item(560, 10), new Item(9075, 115) }, 97);

		private int buttonId, levelRequired, stringId, regularBoltId, enchantedBoltId, experience;

		private Item[] items_required;

		Data(int buttonId, int levelRequired, int stringId, int regularId, int enchantedId, Item[] req, int xp) {
			this.buttonId = buttonId;
			this.levelRequired = levelRequired;
			this.stringId = stringId;
			this.regularBoltId = regularId;
			this.enchantedBoltId = enchantedId;
			this.items_required = req;
			this.experience = xp;
		}

		public int getButtonId() {
			return buttonId;
		}

		public int getRegularBoltId() {
			return regularBoltId;
		}

		public int getEnchantedBoltId() {
			return enchantedBoltId;
		}

		public int getLevelRequired() {
			return levelRequired;
		}

		public int getStringId() {
			return stringId;
		}

		public int getExperience() {
			return experience;
		}

		public Item[] getRequiredItems() {
			return items_required;
		}
	}

	public static boolean enchantButtons(Player player, int buttonId) {
		int magicLevel = player.getSkillManager().getCurrentLevel(Skill.MAGIC);
		for (Data d : Data.values()) {
			if (d.getButtonId() == buttonId) {
				if (magicLevel >= d.getLevelRequired()) {
					if (player.getInventory().contains(d.getRegularBoltId())
							&& player.getInventory().getAmount(d.getRegularBoltId()) >= 10) {
						if(!player.enchantTimer.elapsed(1000)) {
							return true;
						}
						if (player.getInventory().contains(d.getRequiredItems())) {
							player.getInventory().deleteItemSet(d.getRequiredItems());
							player.getInventory().delete(new Item(d.getRegularBoltId(), 10));
							player.performAnimation(new Animation(4462));
							player.performGraphic(new Graphic(759));
							player.getInventory().add(new Item(d.getEnchantedBoltId(), 10));
							player.getSkillManager().addExperience(Skill.MAGIC, d.getExperience() * Skill.MAGIC.getModifier());
							//Achievements.doProgress(player, Achievements.AchievementData.ENCHANT_1000_BOLTS, 10);
						} else {
							player.getPacketSender().sendMessage("You do not have the required runes for this spell.");
						}
					} else {
						player.getPacketSender().sendMessage("You do not have the required bolts to enchant!");
					}
				} else {
					player.getPacketSender().sendMessage(
							"You need a magic level of " + d.getLevelRequired() + " to enchant these bolts.");
				}

				return true;
			}
		}

		return false;
	}

	public static void open(Player player) {
		player.getPacketSender().sendInterface(49000);
		int magic_level = player.getSkillManager().getCurrentLevel(Skill.MAGIC);
		for (Data d : Data.values()) {
			if (magic_level < d.getLevelRequired()) {
				player.getPacketSender().sendString(d.getStringId(), "@red@Magic " + d.getLevelRequired());
			}
		}
	}

}
