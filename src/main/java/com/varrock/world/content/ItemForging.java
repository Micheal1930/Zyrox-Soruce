package com.varrock.world.content;

import com.varrock.model.Graphic;
import com.varrock.model.Item;
import com.varrock.model.Skill;
import com.varrock.model.definitions.ItemDefinition;
import com.varrock.util.Misc;
import com.varrock.world.content.Achievements.AchievementData;
import com.varrock.world.entity.impl.player.Player;

/**
 * Handles item forging, such as Spirit shields making etc.
 * @author Gabriel Hannason and Samy
 */
public class ItemForging {

	public static void forgeItem(final Player p, final int item1, final int item2) {
		if(item1 == item2)
			return;
		ItemForgeData data = ItemForgeData.getDataForItems(item1, item2);
		if(data == null || !p.getInventory().contains(item1) || !p.getInventory().contains(item2))
			return;
		if(!p.getClickDelay().elapsed(3000)) 
			return;
		if(p.getInterfaceId() > 0) {
			p.getPacketSender().sendMessage("Please close the interface you have open before doing this.");
			return;
		}
		Skill skill = Skill.forId(data.skillRequirement[0]);
		int skillReq = data.skillRequirement[1];
		if(p.getSkillManager().getCurrentLevel(skill) >= skillReq) {
			for(Item reqItem : data.requiredItems) {
				if(!p.getInventory().contains(reqItem.getId()) || p.getInventory().getAmount(reqItem.getId()) < reqItem.getAmount()) {
					p.getPacketSender().sendMessage("You need "+Misc.anOrA(reqItem.getDefinition().getName())+" "+reqItem.getDefinition().getName()+" to forge a new item.");
					return;
				}
			}
			//p.performGraphic(new Graphic(2010));
			for(Item reqItem : data.requiredItems) {
				if(reqItem.getId() == 1755 || reqItem.getId() == 1595)
					continue;
				p.getInventory().delete(reqItem);
			}
			p.getInventory().add(data.product, true);
			final String itemName = Misc.formatText(ItemDefinition.forId(data.product.getId()).getName().toLowerCase());
			p.getPacketSender().sendMessage("You make "+Misc.anOrA(itemName)+" "+itemName+".");
			p.getClickDelay().reset();
			p.getSkillManager().addExperience(skill, data.skillRequirement[2]);
		} else {
			if(skill != null)
				p.getPacketSender().sendMessage("You need "+Misc.anOrA(skill.getFormatName())+" "+skill.getFormatName()+" level of at least "+skillReq+" to forge this item.");
			return;
		}
		if(data == ItemForgeData.ARMADYL_GODSWORD || data == ItemForgeData.BANDOS_GODSWORD || data == ItemForgeData.ZAMORAK_GODSWORD || data == ItemForgeData.SARADOMIN_GODSWORD) {
			Achievements.finishAchievement(p, AchievementData.ASSEMBLE_A_GODSWORD);
			Achievements.doProgress(p, AchievementData.ASSEMBLE_5_GODSWORDS);
		}
		if(data == ItemForgeData.SPECTRAL_SPIRIT_SHIELD || data == ItemForgeData.ARCANE_SPIRIT_SHIELD || data == ItemForgeData.ELYSIAN_SPIRIT_SHIELD || data == ItemForgeData.DIVINE_SPIRIT_SHIELD) {
			Achievements.finishAchievement(p, AchievementData.MAKE_A_SPIRIT_SHIELD);
			Achievements.doProgress(p, AchievementData.MAKE_A_SPIRIT_SET);
		}
	}

	/**
	 ** The enum holding all our data
	 */
	private static enum ItemForgeData {
		BLESSED_SPIRIT_SHIELD(new Item[] {new Item(13754), new Item(13734)}, new Item(13736), new int[] {1, -1, 0}),
		SPECTRAL_SPIRIT_SHIELD(new Item[] {new Item(13752), new Item(13736)}, new Item(13744), new int[] {13, 85, 40000}),
		ARCANE_SPIRIT_SHIELD(new Item[] {new Item(13746), new Item(13736)}, new Item(13738), new int[] {13, 85, 40000}),
		ELYSIAN_SPIRIT_SHIELD(new Item[] {new Item(13750), new Item(13736)}, new Item(13742), new int[] {13, 85, 40000}),
		DIVINE_SPIRIT_SHIELD(new Item[] {new Item(13748), new Item(13736)}, new Item(13740), new int[] {13, 85, 40000}),

		DRAGON_SQ_SHIELD(new Item[] {new Item(2368), new Item(2366)}, new Item(1187), new int[] {13, 60, 10000}),
		DRAGON_PLATEBY(new Item[] {new Item(14472), new Item(14474), new Item(14476)}, new Item(14479), new int[] {13, 92, 120000}),
		DRAGONFIRE_SHIELD(new Item[] {new Item(11286), new Item(1540)}, new Item(11283), new int[] {13, 82, 36000}),

		CRYSTAL_KEY(new Item[] {new Item(985), new Item(987)}, new Item(989), new int[] {1, -1, 0}),

		HALLOWEEN_TBOW(new Item[] {new Item(1959), new Item(20998)}, new Item(21010), new int[] {1, -1, 0}),

		GODSWORD_BLADE(new Item[] {new Item(11710), new Item(11712), new Item(11714)}, new Item(11690), new int[] {1, -1, 0}),
		ARMADYL_GODSWORD(new Item[] {new Item(11702), new Item(11690)}, new Item(11694), new int[] {1, -1, 0}),
		BANDOS_GODSWORD(new Item[] {new Item(11704), new Item(11690)}, new Item(11696), new int[] {1, -1, 0}),
		SARADOMIN_GODSWORD(new Item[] {new Item(11706), new Item(11690)}, new Item(11698), new int[] {1, -1, 0}),
		ZAMORAK_GODSWORD(new Item[] {new Item(11708), new Item(11690)}, new Item(11700), new int[] {1, -1, 0}),

		DRAGONSTONE_BOLTS(new Item[] { new Item(9144, 15), new Item(9193, 15) }, new Item(9341, 15), new int[] { 9, 80, 5000 }),
		DIAMOND_BOLTS(new Item[] { new Item(9143, 15), new Item(9192, 15) }, new Item(9340, 15), new int[] { 9, 70, 4000 }),
		RUBY_BOLTS(new Item[] { new Item(9143, 15), new Item(9191, 15) }, new Item(9339, 15), new int[] { 9, 60, 2000 }),
		EMERALD_BOLTS(new Item[] { new Item(9142, 15), new Item(9190, 15) }, new Item(9238, 15), new int[] { 9, 40, 1000 }),
		SAPPHIRE_BOLTS(new Item[] { new Item(9142, 15), new Item(9189, 15) }, new Item(9337, 15), new int[] { 9, 30, 500 }),

		AMULET_OF_FURY_ORNAMENT(new Item[] {new Item(19333), new Item(6585)}, new Item(19335), new int[] {1, -1, 0}),
		DRAGON_FULL_HELM_SPIKE(new Item[] {new Item(19354), new Item(11335)}, new Item(19341), new int[] {1, -1, 0}),
		DRAGON_PLATELEGS_SPIKE(new Item[] {new Item(19356), new Item(4087)}, new Item(19343), new int[] {1, -1, 0}),
		DRAGON_PLATEBODY_SPIKE(new Item[] {new Item(19358), new Item(14479)}, new Item(19342), new int[] {1, -1, 0}),
		DRAGON_SQUARE_SHIELD_SPIKE(new Item[] {new Item(19360), new Item(1187)}, new Item(19345), new int[] {1, -1, 0}),
		DRAGON_FULL_HELM_GOLD(new Item[] {new Item(19346), new Item(11335)}, new Item(19336), new int[] {1, -1, 0}),
		DRAGON_PLATELEGS_GOLD(new Item[] {new Item(19348), new Item(4087)}, new Item(19338), new int[] {1, -1, 0}),
		DRAGON_PLATEBODY_GOLD(new Item[] {new Item(19350), new Item(14479)}, new Item(19337), new int[] {1, -1, 0}),
		DRAGON_SQUARE_SHIELD_GOLD(new Item[] {new Item(19352), new Item(1187)}, new Item(19340), new int[] {1, -1, 0}),

		FULL_SLAYER_HELMET(new Item[] {new Item(13263), new Item(15490), new Item(15488)}, new Item(15492), new int[] {18, 75, 0}),

		;


		ItemForgeData(Item[] requiredItems, Item product, int[] skillRequirement) {
			this.requiredItems = requiredItems;
			this.product = product;
			this.skillRequirement = skillRequirement;
		}

		private Item[] requiredItems;
		private Item product;
		private int[] skillRequirement;

		public static ItemForgeData getDataForItems(int item1, int item2) {
			for(ItemForgeData shieldData : ItemForgeData.values()) {
				int found = 0;
				for(Item it : shieldData.requiredItems) {
					if(it.getId() == item1 || it.getId() == item2)
						found++;
				}
				if(found >= 2)
					return shieldData;
			}
			return null;
		}
	}
}
