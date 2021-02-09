package com.zyrox.world.content;

import com.zyrox.model.Item;
import com.zyrox.model.Skill;
import com.zyrox.model.definitions.ItemDefinition;
import com.zyrox.world.entity.impl.player.Player;

/**
 * Handles the Item Building
 * 
 * @author 2012 <https://www.rune-server.ee/members/dexter+morgan/>
 *
 */
public class ItemBuilding {

	/**
	 * The chisel id
	 */
	private static final int CHISEL = 1755;

	/**
	 * Handles the builds
	 */
	public enum Builds {
		DEVOUT_BOOTS(new int[] {42598, 52960,}, 52954, null),
		
		BOOTS_OF_BRIMSTONE(new int[] {52957, 53037,}, 52951, null),

		HEAVY_BALLISTA_PART1(new int[] { 49589, 49592 }, 49598, new SkillRequirement(Skill.FLETCHING, 72, 220)),

		HEAVY_BALLISTA_PART2(new int[] { 49598, 49601 }, 49607, new SkillRequirement(Skill.FLETCHING, 72, 220)),

		HEAVY_BALLISTA_FINISH(new int[] { 49607, 49610 }, 49481, new SkillRequirement(Skill.FLETCHING, 72, 220)),

		LIGHT_BALLISTA_PART1(new int[] { 49586, 49592 }, 49595, new SkillRequirement(Skill.FLETCHING, 47, 110)),

		LIGHT_BALLISTA_PART2(new int[] { 49595, 49601 }, 49604, new SkillRequirement(Skill.FLETCHING, 47, 110)),

		LIGHT_BALLISTA_FINISH(new int[] { 49604, 19610 }, 49478, new SkillRequirement(Skill.FLETCHING, 47, 110)),

	//	ABYSSAL_TENTACLE(new int[] { 4151, 12004, }, 12006, null),

	//	TRIDENT_OF_SWAMP(new int[] { 11907, 12932 }, 12899, new SkillRequirement(Skill.CRAFTING, 59, 0)),

	//	TOXIC_STAFF(new int[] { 11791, 12932 }, 12904, new SkillRequirement(Skill.CRAFTING, 59, 0)),

	//	TOXIC_BLOWPIPE(new int[] { CHISEL, 12922 }, 12926, new SkillRequirement(Skill.FLETCHING, 53, 0)),

		//MAX_FIRE_CAPE(new int[] { 43342, 6570, }, 13329),

		MAX_AVA_CAPE(new int[] { 43342, 10499, }, 43337),

		MAX_SARA_CAPE(new int[] { 43342, 2412, }, 43331),

		MAX_ZAMORAK_CAPE(new int[] { 43342, 2413, }, 43333),

		MAX_GUTHIX_CAPE(new int[] { 43342, 2414, }, 43335),

		MAX_ARDY_CAPE(new int[] { 43342, 13124, }, 20760),

		//MAX_INFERNAL_CAPE(new int[] { 13342, 21295, }, 21285),

		BLESSED_SPIRIT_SHIELD(new int[] { 12833, 12829 }, 12831, new SkillRequirement(Skill.PRAYER, 85, 0)),

		SPECTRAL_SPIRIT_SHIELD(new int[] { 12831, 12823 }, 12821, new SkillRequirement(Skill.PRAYER, 90, 0)),

		ELYSIAN_SPIRIT_SHIELD(new int[] { 12831, 12819 }, 12817, new SkillRequirement(Skill.PRAYER, 90, 0)),

		ARCANE_SPIRIT_SHIELD(new int[] { 12831, 12827 }, 12825, new SkillRequirement(Skill.PRAYER, 90, 0)),

		SERPENTINE_HELM(new int[] { 12927, CHISEL }, 12931, new SkillRequirement(Skill.CRAFTING, 52, 120)),

		MAGMA_HELM(new int[] { 43201, 42931 }, 43199),

		TANZANITE_HELM(new int[] { 43200, 42931 }, 43197),

		GODSWORD_SHARD(new int[] { 41818, 41820 }, 11794),

		GODSWORD_BLADE(new int[] { 41822, 41794 }, 11798),

		BANDOS_GODSWORD(new int[] { 41812, 41798 }, 11808),

		SARADOMIN_GODSWORD(new int[] { 41814, 41798 }, 11806),

		ZAMORAK_GODSWORD(new int[] { 41816, 41798 }, 11804),

		ARMADYL_GODSWORD(new int[] { 41810, 41798 }, 11802),

		PGEASIAN_BOOTS(new int[] { 43229, 2577 }, 13237, new SkillRequirement(Skill.RUNECRAFTING, 60, 0)),

		PRIMORDIAL_BOOTS(new int[] { 43231, 11840 }, 13239, new SkillRequirement(Skill.RUNECRAFTING, 60, 0)),

		ETERNAL_BOOTS(new int[] { 43227, 2579 }, 13235, new SkillRequirement(Skill.RUNECRAFTING, 60, 0)),

		INFERNAL_AXE(new int[] { 6739, 13233 }, 13241, new SkillRequirement(Skill.WOODCUTTING, 61, 200)),

		INFERNAL_PICKAXE(new int[] { 11920, 13233 }, 13243, new SkillRequirement(Skill.SMITHING, 85, 200)),

		//VOLCANIC_WHIP(new int[] { 4151, 12771, }, 12773),

		//FROZEN_WHIP(new int[] { 4151, 12769 }, 12774),

		ZGS_OR(new int[] { 41808, 50077 }, 50374),

		SGS_OR(new int[] { 41806, 50074 }, 50372),

		BGS_OR(new int[] { 41804, 50071 }, 50370),

		//AGS_OR(new int[] { 41802, 50068 }, 50368),

	//	TORTURE_OR(new int[] { 49553, 50062 }, 50366), // MAX OP NECKLACE 

		OCCULT_OR(new int[] { 42002, 50065 }, 49720),

		FURY_OR(new int[] { 6585, 42526 }, 42436),

		BLUE_DBOW(new int[] { 41235, 12757 }, 42766),

		YELLOW_DBOW(new int[] { 41235, 12761 }, 42767),

		GREEN_DBOW(new int[] { 41235, 12759 }, 42765),

		WHITE_DBOW(new int[] { 41235, 12763 }, 42768),

		;

		/**
		 * The components required
		 */
		private int[] components;

		/**
		 * The finished product
		 */
		private int result;

		/**
		 * The skill requirement
		 */
		private SkillRequirement skillRequirement;

		/**
		 * Represents an item build
		 * 
		 * @param components
		 *            the components
		 * @param result
		 *            the result
		 * @param skillRequirement
		 *            the skill req
		 * @param confirmation
		 *            if confirmation required
		 */
		Builds(int[] components, int result, SkillRequirement skillRequirement) {
			this.setComponents(components);
			this.setResult(result);
			this.setSkillRequirement(skillRequirement);
		}

		/**
		 * Represents an item build
		 * 
		 * @param components
		 *            the components
		 * @param result
		 *            the result
		 */
		Builds(int[] components, int result) {
			this.setComponents(components);
			this.setResult(result);
			this.setSkillRequirement(null);
		}

		/**
		 * Sets the components
		 *
		 * @return the components
		 */
		public int[] getComponents() {
			return components;
		}

		/**
		 * Sets the components
		 * 
		 * @param components
		 *            the components
		 */
		public void setComponents(int[] components) {
			this.components = components;
		}

		/**
		 * Sets the result
		 *
		 * @return the result
		 */
		public int getResult() {
			return result;
		}

		/**
		 * Sets the result
		 * 
		 * @param result
		 *            the result
		 */
		public void setResult(int result) {
			this.result = result;
		}

		/**
		 * Sets the skillRequirement
		 *
		 * @return the skillRequirement
		 */
		public SkillRequirement getSkillRequirement() {
			return skillRequirement;
		}

		/**
		 * Sets the skillRequirement
		 * 
		 * @param skillRequirement
		 *            the skillRequirement
		 */
		public void setSkillRequirement(SkillRequirement skillRequirement) {
			this.skillRequirement = skillRequirement;
		}

		/**
		 * Gets the build
		 * 
		 * @param first
		 *            the first
		 * @param second
		 *            the second
		 * @return the build
		 */
		public static Builds getBuild(int first, int second) {
			for (Builds b : Builds.values()) {
				/*
				 * The first found
				 */
				int firstFound = 0;
				/*
				 * The second found
				 */
				int secondFound = 0;
				/*
				 * Loop through list
				 */
				for (int componenets : b.getComponents()) {
					if (componenets == first) {
						firstFound = componenets;
					}
				}
				/*
				 * Loop through list
				 */
				for (int componenets : b.getComponents()) {
					if (componenets == second) {
						secondFound = componenets;
					}
				}
				/*
				 * Same
				 */
				if (firstFound == secondFound) {
					continue;
				}
				/*
				 * Nothing found
				 */
				if (firstFound == 0 || secondFound == 0) {
					continue;
				}
				return b;
			}
			return null;
		}
	}

	/**
	 * Builds the item
	 * 
	 * @param player
	 *            the player
	 * @param first
	 *            the first
	 * @param second
	 *            the second
	 * @return building
	 */
	public static boolean build(Player player, int first, int second) {
		/*
		 * The build
		 */
		Builds build = Builds.getBuild(first, second);
		/*
		 * No build
		 */
		if (build == null) {
			return false;
		}
		/*
		 * Checks level
		 */
		if (build.getSkillRequirement() != null) {
			if (player.getSkillManager().getCurrentLevel(build.getSkillRequirement().getSkill()) < build
					.getSkillRequirement().getLevelRequired()) {
				player.getPacketSender().sendMessage("You don't have the required level to do this.");
				player.getPacketSender().sendMessage("You need a " + build.getSkillRequirement().getLevelRequired()
						+ " " + build.getSkillRequirement().getSkill().name() + " level to do this.");
				return true;
			}
		}
		build(player, first, second, build);
		return true;
	}

	/**
	 * Building items
	 * 
	 * @param player
	 *            the player
	 * @param first
	 *            the first
	 * @param second
	 *            the second
	 * @param build
	 *            the build
	 */
	private static void build(Player player, int first, int second, Builds build) {
		/*
		 * The build requirement
		 */
		if (build.getSkillRequirement() != null) {
			if (player.getSkillManager().getCurrentLevel(build.getSkillRequirement().getSkill()) < build
					.getSkillRequirement().getLevelRequired()) {
				player.getPacketSender()
						.sendMessage("You don't have the required " + build.getSkillRequirement().getSkill().getName()
								+ " to do this. You need a level of " + build.getSkillRequirement().getLevelRequired());
				return;
			}
		}
		/*
		 * The first found
		 */
		int firstFound = 0;
		/*
		 * The second found
		 */
		int secondFound = 0;
		/*
		 * Loop through list
		 */
		for (int componenets : build.getComponents()) {
			if (componenets == first) {
				firstFound = componenets;
			}
		}
		/*
		 * Loop through list
		 */
		for (int componenets : build.getComponents()) {
			if (componenets == second) {
				secondFound = componenets;
			}
		}
		/*
		 * Same
		 */
		if (firstFound == secondFound) {
			return;
		}
		/*
		 * Nothing found
		 */
		if (firstFound == 0 || secondFound == 0) {
			return;
		}
		/*
		 * Builds the item
		 */
		if (combine(player, first, second, firstFound, secondFound, build.getResult())) {
			/*
			 * The result
			 */
			String resultItem = new Item(build.getResult()).getDefinition().getName().toLowerCase();
			/*
			 * Only two pieces
			 */
			if (build.getComponents().length == 2) {
				String firstItem = new Item(build.getComponents()[0]).getDefinition().getName().toLowerCase();
				String secondItem = new Item(build.getComponents()[1]).getDefinition().getName().toLowerCase();

				/*
				 * Delete first item
				 */
				if (build.getComponents()[0] != CHISEL) {
					player.getInventory().delete(new Item(build.getComponents()[0]));
				}
				/*
				 * Delete second
				 */
				if (build.getComponents()[1] != CHISEL) {
					player.getInventory().delete(new Item(build.getComponents()[1]));
				}
				/*
				 * Add result
				 */
				player.getInventory().add(new Item(build.getResult()));
				player.getPacketSender()
						.sendMessage("You use the " + firstItem + " with " + secondItem + " and make " + resultItem);
			} else {
				/*
				 * Loops through componenets
				 */
				for (int needed : build.getComponents()) {
					/*
					 * Doesn't have all
					 */
					if (!player.getInventory().contains(needed)) {
						player.getPacketSender()
								.sendMessage("You don't have all the required pieces to craft this item. You need "
										+ ItemDefinition.forId(needed).getName());
						return;
					}
				}
				/*
				 * Deletes
				 */
				for (int needed : build.getComponents()) {
					player.getInventory().delete(new Item(needed));
				}
				player.getInventory().add(new Item(build.getResult()));
				player.getPacketSender().sendMessage("You put all the pieces together and make " + resultItem);
			}
			/*
			 * Adds experience
			 */
			if (build.getSkillRequirement() != null) {
				player.getSkillManager().addExperience(build.getSkillRequirement().getSkill(),
						(int) build.getSkillRequirement().getExperienceReceived());
			}
		}
	}

	/**
	 * Combing items
	 * 
	 * @param player
	 *            the player
	 * @param first
	 *            the first
	 * @param second
	 *            the second
	 * @param item1
	 *            the item 1
	 * @param item2
	 *            the item 2
	 * @param result
	 *            the result
	 * @return combined
	 */
	private static boolean combine(Player player, int first, int second, int item1, int item2, int result) {
		if ((first == item1 && second == item2) || (second == item1 && first == item2)) {
			return true;
		}
		return false;
	}
	
	public static class SkillRequirement {
		private Skill skill;
		private int levelRequired;
		private double experienceReceived;
		private int id;

		public SkillRequirement(Skill skill, int levelRequired, double experienceReceived) {
			this.setSkill(skill);
			this.setId(0);
			this.setLevelRequired(levelRequired);
			this.setExperienceReceived(experienceReceived);
		}

		public SkillRequirement(int id, int levelRequired, double experienceReceived) {
			this.setSkill(skill);
			this.setId(id);
			this.setLevelRequired(levelRequired);
			this.setExperienceReceived(experienceReceived);
		}

		/**
		 * Sets the skill
		 *
		 * @return the skill
		 */
		public Skill getSkill() {
			return skill;
		}

		/**
		 * Sets the skill
		 * 
		 * @param skill
		 *            the skill
		 */
		public void setSkill(Skill skill) {
			this.skill = skill;
		}

		/**
		 * Sets the levelRequired
		 *
		 * @return the levelRequired
		 */
		public int getLevelRequired() {
			return levelRequired;
		}

		/**
		 * Sets the levelRequired
		 * 
		 * @param levelRequired
		 *            the levelRequired
		 */
		public void setLevelRequired(int levelRequired) {
			this.levelRequired = levelRequired;
		}

		/**
		 * Sets the experienceReceived
		 *
		 * @return the experienceReceived
		 */
		public double getExperienceReceived() {
			return experienceReceived;
		}

		/**
		 * Sets the experienceReceived
		 * 
		 * @param experienceReceived
		 *            the experienceReceived
		 */
		public void setExperienceReceived(double experienceReceived) {
			this.experienceReceived = experienceReceived;
		}

		/**
		 * Sets the id
		 *
		 * @return the id
		 */
		public int getId() {
			return id;
		}

		/**
		 * Sets the id
		 * 
		 * @param id
		 *            the id
		 */
		public void setId(int id) {
			this.id = id;
		}
	}
}
