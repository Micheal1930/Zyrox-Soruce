package com.varrock.model.item;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.varrock.model.Animation;
import com.varrock.model.Item;
import com.varrock.model.Skill;
import com.varrock.world.content.Achievements;
import com.varrock.world.entity.impl.player.Player;

/***
 * Handles the combination of items to create another.
 * @author Gabriel || Wolfsdarker
 */
public enum ItemCombination {

	GOLD_AMULET(Item.of(1692), Item.of(1673), Item.of(1759)) {
		@Override
		public boolean canCombine(Player player) {
			if (player.getSkillManager().getCurrentLevel(Skill.CRAFTING) < 8) {
				player.sendMessage("You need a Crafting level of 8 to do this.");
				return false;
			}
			return true;
		}

		@Override
		public void addExperience(Player player) {
			player.getSkillManager().addExperience(Skill.CRAFTING, 4 * Skill.CRAFTING.getModifier());
		}
	},

	SAPPHIRE_AMULET(Item.of(1694), Item.of(1675), Item.of(1759)) {
		@Override
		public boolean canCombine(Player player) {
			if (player.getSkillManager().getCurrentLevel(Skill.CRAFTING) < 24) {
				player.sendMessage("You need a Crafting level of 24 to do this.");
				return false;
			}
			return true;
		}

		@Override
		public void addExperience(Player player) {
			player.getSkillManager().addExperience(Skill.CRAFTING, 4 * Skill.CRAFTING.getModifier());
		}
	},

	EMERALD_AMULET(Item.of(1696), Item.of(1677), Item.of(1759)) {
		@Override
		public boolean canCombine(Player player) {
			if (player.getSkillManager().getCurrentLevel(Skill.CRAFTING) < 31) {
				player.sendMessage("You need a Crafting level of 31 to do this.");
				return false;
			}
			return true;
		}

		@Override
		public void addExperience(Player player) {
			player.getSkillManager().addExperience(Skill.CRAFTING, 4 * Skill.CRAFTING.getModifier());
		}
	},

	RUBY_AMULET(Item.of(1698), Item.of(1679), Item.of(1759)) {
		@Override
		public boolean canCombine(Player player) {
			if (player.getSkillManager().getCurrentLevel(Skill.CRAFTING) < 50) {
				player.sendMessage("You need a Crafting level of 50 to do this.");
				return false;
			}
			return true;
		}

		@Override
		public void addExperience(Player player) {
			player.getSkillManager().addExperience(Skill.CRAFTING, 4 * Skill.CRAFTING.getModifier());
		}
	},

	DIAMOND_AMULET(Item.of(1700), Item.of(1681), Item.of(1759)) {
		@Override
		public boolean canCombine(Player player) {
			if (player.getSkillManager().getCurrentLevel(Skill.CRAFTING) < 70) {
				player.sendMessage("You need a Crafting level of 70 to do this.");
				return false;
			}
			return true;
		}

		@Override
		public void addExperience(Player player) {
			player.getSkillManager().addExperience(Skill.CRAFTING, 4 * Skill.CRAFTING.getModifier());
		}
	},

	DRAGONSTONE_AMULET(Item.of(1702), Item.of(1683), Item.of(1759)) {
		@Override
		public boolean canCombine(Player player) {
			if (player.getSkillManager().getCurrentLevel(Skill.CRAFTING) < 80) {
				player.sendMessage("You need a Crafting level of 80 to do this.");
				return false;
			}
			return true;
		}

		@Override
		public void addExperience(Player player) {
			player.getSkillManager().addExperience(Skill.CRAFTING, 4 * Skill.CRAFTING.getModifier());
		}
	},

	ONYX_AMULET(Item.of(6581), Item.of(6579), Item.of(1759)) {
		@Override
		public boolean canCombine(Player player) {
			if (player.getSkillManager().getCurrentLevel(Skill.CRAFTING) < 90) {
				player.sendMessage("You need a Crafting level of 90 to do this.");
				return false;
			}
			return true;
		}

		@Override
		public void addExperience(Player player) {
			player.getSkillManager().addExperience(Skill.CRAFTING, 4 * Skill.CRAFTING.getModifier());
		}
	},

	// Upgrade to Turquoise Slayer helm
	TURQUOISE_SLAYER_HELM(Item.of(51888), Item.of(51907), Item.of(15492)),
	
	// Upgrade to Ancient wyvern shield
	ANCIENT_WYVERN_SHIELD(Item.of(51633), Item.of(51637), Item.of(52284)),
	
	// Upgrade for Avernic defender
	AVERNIC_DEFENDER(Item.of(52322), Item.of(52477), Item.of(52441)),
	
	// Upgrade to Infernal max cape
	INFERNAL_MAX_CAPE(Item.of(51285), Item.of(51295), Item.of(51293)),	
	
	// Upgrade to Dragon Hunter Lance
	DRAGON_HUNTER_LANCE(Item.of(52978), Item.of(41889), Item.of(52966)),

	DHAROK_SET(Item.of(11848), Item.of(4716), Item.of(4718), Item.of(4720), Item.of(4722)),
	AHRIMS_SET(Item.of(11846), Item.of(4708), Item.of(4710), Item.of(4712), Item.of(4714)),
	GUTHANS_SET(Item.of(11850), Item.of(4724), Item.of(4726), Item.of(4728), Item.of(4730)),
	VERACS_SET(Item.of(11856), Item.of(4753), Item.of(4755), Item.of(4757), Item.of(4759)),
	TORAGS_SET(Item.of(11854), Item.of(4745), Item.of(4747), Item.of(4749), Item.of(4751)),
	KARILS_SET(Item.of(11852), Item.of(4732), Item.of(4734), Item.of(4736), Item.of(4738)),
	THIRD_AGE_MELEE_SET(Item.of(11858), Item.of(10350), Item.of(10348), Item.of(10346), Item.of(10352)),
	THIRD_AGE_RANGE_SET(Item.of(11860), Item.of(10334), Item.of(10330), Item.of(10332), Item.of(10336)),
	THIRD_AGE_MAGE_SET(Item.of(11862), Item.of(10342), Item.of(10338), Item.of(10340), Item.of(10344)),

	// Mini Grind Verzik's Cape
	VERZIK_CAPE_TIER_2(Item.of(52496), Item.of(52494), Item.of(52768)),
	VERZIK_CAPE_TIER_3(Item.of(52498), Item.of(52496), Item.of(52767)),
	VERZIK_CAPE_TIER_4(Item.of(52500), Item.of(52498), Item.of(52766)),
	VERZIK_CAPE_TIER_5(Item.of(52502), Item.of(52500), Item.of(52765)),
	VERZIK_CAPE_COMPLETED(Item.of(21022), Item.of(52502), Item.of(981)),

	;

	private final List<Item> results;
	private final List<Item> ingredients;

	private static Map<Integer, ItemCombination> combinations = new HashMap<>();

	/**
	 * Constructor for the combination.
	 *
	 * @param result
	 * @param ingredients
	 */
	ItemCombination(Item result, Item... ingredients) {
		this.results = new ArrayList<>();
		this.results.add(result);
		this.ingredients = Arrays.stream(ingredients).collect(Collectors.toList());
	}

	/**
	 * Returns if the player can combine the items.
	 *
	 * @return if can combine
	 */
	public boolean canCombine(Player player) {
		return true;
	}

	/**
	 * Performs an animation when combining the items.
	 *
	 * @param player
	 */
	public void performAnimation(Player player) {
	}

	/**
	 * Adds experience when the items are combined.
	 *
	 * @param player
	 */
	public void addExperience(Player player) {
	}

	/**
	 * Loads all the combinations.
	 */
	public static void init() {
		for (ItemCombination value : ItemCombination.values()) {
			for (int i = 0; i < value.ingredients.size(); i++) {
				for (int i1 = i; i1 < value.ingredients.size(); i1++) {
					int source = value.ingredients.get(i).getId();
					int target = value.ingredients.get(i1).getId();

					combinations.put(source << 16 | target, value);
					combinations.put(target << 16 | source, value);
				}
			}
		}
	}

	/**
	 * Handles the item on item action.
	 *
	 * @param player
	 * @param used
	 * @param usedWith
	 */
	public static boolean handleItemOnItem(Player player, Item used, Item usedWith) {

		ItemCombination combination = combinations.get(used.getId() << 16 | usedWith.getId());

		if (combination == null || used.getId() == usedWith.getId()) {
			return false;
		}

		boolean allIngredients = true;

		for (Item ingredient : combination.ingredients) {
			if (!player.getInventory().contains(ingredient)) {
				allIngredients = false;
			}
		}

		if (!combination.canCombine(player)) {
			return true;
		}

		if (!allIngredients) {
			player.sendMessage("You don't have all the required items to do this.");
			return true;
		}

		combination.performAnimation(player);

		for (Item ingredient : combination.ingredients) {
			player.getInventory().delete(ingredient);
		}

		for (Item result : combination.results) {
			player.getInventory().add(result);
		}

		combination.addExperience(player);
		int startDialogueChildId = 4890;
		int headChildId = startDialogueChildId - 2;
		player.getPacketSender().sendInterfaceAnimation(headChildId, new Animation(-1));
		player.getPacketSender().sendInterfaceModel(headChildId, combination.results.get(0).getId(),  250); //TODO: Verify
		player.getPacketSender().sendString(startDialogueChildId - 1, "Combining items...");
		player.getPacketSender().sendString(startDialogueChildId, "You combine the items and the result is a");
		player.getPacketSender().sendString(startDialogueChildId + 1, combination.results.get(0).getDefinition().getName());
		player.getPacketSender().sendChatboxInterface(startDialogueChildId - 3);
		return true;
	}
}
