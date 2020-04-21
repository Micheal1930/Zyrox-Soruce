package com.varrock.world.content.skill.impl.summoning;

import com.varrock.model.Skill;

/**
 * Handles pet abilities
 * 
 * @author 2012
 *
 */
public class SkillingPet {

	/**
	 * Represents the skilling benefits
	 */
	public enum SkillingPetBenefit {

		VORKI(154, Skill.ATTACK, 1.0),
		OMLET(153, Skill.STRENGTH, 1.0),
		ZUK(23009, Skill.ATTACK, 1.0),

		;

		/**
		 * The pet id
		 */
		private int id;

		/**
		 * The skill
		 */
		private Skill skill;

		/**
		 * The skill bonus
		 */
		private double skillBonus;

		/**
		 * Represents a skilling pet benefit
		 * 
		 * @param id         the id
		 * @param skill      the skill
		 * @param skillBonus the skill bonus
		 */
		SkillingPetBenefit(int id, Skill skill, double skillBonus) {
			this.id = id;
			this.skill = skill;
			this.skillBonus = skillBonus;
		}

		/**
		 * Gets the id
		 *
		 * @return the id
		 */
		public int getId() {
			return id;
		}

		/**
		 * Gets the skill
		 *
		 * @return the skill
		 */
		public Skill getSkill() {
			return skill;
		}

		/**
		 * Gets the skillBonus
		 *
		 * @return the skillBonus
		 */
		public double getSkillBonus() {
			return skillBonus;
		}
	}
}
