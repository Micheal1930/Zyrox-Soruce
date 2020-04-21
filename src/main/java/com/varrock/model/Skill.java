package com.varrock.model;

import com.varrock.util.Misc;


/**
 * This enum contains data used as constants for skill configurations
 * such as experience rates, string id's for interface updating.
 * @author Gabriel Hannason
 */
public enum Skill {

	ATTACK(6247, 60),
	DEFENCE(6253, 60),
	STRENGTH(6206, 60),
	CONSTITUTION(6216, 60),
	RANGED(4443, 40),
	PRAYER(6242, 20),
	MAGIC(6211, 40),
	COOKING(6226, 40),
	WOODCUTTING(4272, 40),
	FLETCHING(6231, 40),
	FISHING(6258, 40),
	FIREMAKING(4282, 40),
	CRAFTING(6263, 40),
	SMITHING(6221, 40),
	MINING(4416, 40),
	HERBLORE(6237, 40),
	AGILITY(4277, 40),
	THIEVING(4261, 1),
	SLAYER(12122, 1),
	FARMING(45400, 40),
	RUNECRAFTING(4267, 40),
	CONSTRUCTION(7267, 1),
	HUNTER(8267, 40),
	SUMMONING(9267, 40),
	DUNGEONEERING(10267, 1);

	private Skill(int chatboxInterface, int modifier) {
		this.chatboxInterface = chatboxInterface;
		this.modifier = modifier;
	}

	/**
	 * The skill's chatbox interface
	 * The interface which will be sent
	 * on levelup.
	 */
	private int chatboxInterface;

	/**
	 * The amount the skills experience
	 * is multiplied by
	 */
	private final int modifier;
	
	/**
	 * Gets the Skill's chatbox interface.
	 * @return The interface which will be sent on levelup.
	 */
	public int getChatboxInterface() {
		return chatboxInterface;
	}
	
	/**
	 * Gets the Skill's name.
	 * @return	The skill's name in a lower case format.
	 */
	public String getName() {
		return toString().toLowerCase();
	}

	/**
	 * Gets the Skill's name.
	 * @return	The skill's name in a formatted way.
	 */
	public String getFormatName() {
		return Misc.formatText(getName());
	}

	/**
	 * Gets the Skill value which ordinal() matches {@code id}.
	 * @param id	The index of the skill to fetch Skill instance for.
	 * @return		The Skill instance.
	 */
	public static Skill forId(int id) {
		for (Skill skill : Skill.values()) {
			if (skill.ordinal() == id) {
				return skill;
			}
		}
		return null;
	}

	/**
	 * Gets the Skill value which name matches {@code name}.
	 * @param string	The name of the skill to fetch Skill instance for.
	 * @return		The Skill instance.
	 */
	public static Skill forName(String name) {
		for (Skill skill : Skill.values()) {
			if (skill.toString().equalsIgnoreCase(name)) {
				return skill;
			}
		}
		return null;
	}

	public int getModifier() {
		return modifier;
	}
}