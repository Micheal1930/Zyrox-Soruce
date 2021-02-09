package com.zyrox.world.content.skill;

import com.zyrox.model.Animation;
import com.zyrox.model.Skill;
import com.zyrox.util.Misc;
import com.zyrox.world.entity.impl.player.Player;

/**
 * This interface is used to represent any non-combat
 * related skills.
 *
 * @author relex lawl
 */
public abstract class AbstractSkill {

	/**
	 * The AbstractSkill constructor.
	 * @param player	The {@link org.niobe.world.Player} setting off the skill.
	 */
	public AbstractSkill(Player player) {
		this.player = player;
		player.setSkill(this);
		player.setStartPosition(player.getPosition().copy());
	}
	
	/**
	 * The associated {@link org.niobe.world.Player} performing 
	 * the skill action.
	 */
	private final Player player;
	
	/**
	 * A success formula used for varies {@link AbstractSkill}s.
	 * @param requirement	The level requirement for said skill to be executed.	
	 * @return				{@value true} is {@link #player} succeeds in skill.
	 */
	public boolean success(int requirement) {
        int factor = Misc.random(getPlayer().getSkillManager().getMaxLevel(getSkill()));
        int fluke = Misc.random(requirement == 1 ? 3 : requirement);
        return factor > fluke;
	}

	/**
	 * A success formula used for varies {@link AbstractSkill}s.
	 * @param requirement	The level requirement for said skill to be executed.	
	 * @return				{@value true} is {@link #player} succeeds in skill.
	 */
	public boolean success(int requirement, int toolRequirement) {
		double level = ((double) player.getSkillManager().getMaxLevel(getSkill()) + (double) toolRequirement) / 2;
		int successChance = Misc.random((int) (Math.ceil((level - requirement))));
		int roll = Misc.random(requirement);
		return successChance >= roll;
	}
	
	/**
	 * The delay between each skill action.
	 * @return	The wait delay (in ms) between each skill action.
	 */
	public long getActionDelay() {
		return 0;
	}
	
	/**
	 * Gets the associated {@link #player} for this skill action.	
	 * @return	The {@link #player} value.
	 */
	public Player getPlayer() {
		return player;
	}
	
	/**
	 * The default flag that checks if this skill performs
	 * an action upon entering an amount in an input dialogue.
	 */
	public boolean enterAmountInput(int amount) {
		return false;
	}

	/**
	 * This flag checks if the {@code player} can successfully
	 * execute the skill action.
	 * @return			If {@value true} the {@code player} will start the skill execution.
	 */
	public abstract boolean canInitiate();
	
	/**
	 * Gets the {@link org.niobe.model.SkillManager.Skill} being
	 * trained and executed.
	 * @return	The associated {@link org.niobe.model.SkillManager.Skill}.
	 */
	public abstract Skill getSkill();
	
	/**
	 * Gets the experience the associated player will receive
	 * in the {@link #getSkill()} upon the finalization of the skill action.
	 * @return	The amount of experience to add to {@link #getSkill()} experience.
	 */
	public abstract double getExperience();
	
	/**
	 * Gets the {@link org.niobe.model.Animation} that will be
	 * performed upon the execution of the skill action.
	 * @return	The {@link org.niobe.model.Animation} that will be performed.
	 */
	public abstract Animation getAnimation();

}
