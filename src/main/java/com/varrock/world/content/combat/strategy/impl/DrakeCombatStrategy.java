package com.varrock.world.content.combat.strategy.impl;

import com.varrock.GameSettings;
import com.varrock.engine.task.Task;
import com.varrock.engine.task.TaskManager;
import com.varrock.model.Animation;
import com.varrock.model.Locations;
import com.varrock.model.Position;
import com.varrock.model.projectile.Projectile;
import com.varrock.util.Misc;
import com.varrock.world.content.combat.CombatContainer;
import com.varrock.world.content.combat.CombatType;
import com.varrock.world.content.combat.strategy.CombatStrategy;
import com.varrock.world.entity.impl.GameCharacter;
import com.varrock.world.entity.impl.npc.NPC;

/**
 * Handles Drake Combat strategy
 * 
 * @author 2012 <https://www.rune-server.ee/members/dexter+morgan/>
 *
 */
public class DrakeCombatStrategy implements CombatStrategy {

	/**
	 * Attacking melee
	 */
	private static final Animation MELEE = new Animation(23535);
	/**
	 * Attacking melee
	 */
	private static final Animation RANGE = new Animation(23536);

	/**
	 * The drake npc id
	 */
	public static final int DRAKE = 23612;

	/**
	 * The attack number
	 */
	private int attack;

	@Override
	public boolean canAttack(GameCharacter entity, GameCharacter victim) {
		return true;
	}

	@Override
	public CombatContainer attack(GameCharacter entity, GameCharacter victim) {
		return null;
	}

	@Override
	public boolean customContainerAttack(GameCharacter entity, GameCharacter victim) {
		NPC drake = (NPC) entity;
		if (drake.isChargingAttack() || drake.getConstitution() <= 0) {
			drake.getCombatBuilder().setAttackTimer(4);
			return true;
		}
		attack = 0;
		if (attack == 7) {
			Position pos = victim.getPosition().copy();
			drake.setChargingAttack(true).getCombatBuilder().setAttackTimer(6);
			new Projectile(drake, victim, 1664 + GameSettings.OSRS_GFX_OFFSET, 44, 3, 43, 43, 0).sendProjectile();
			TaskManager.submit(new Task(4, drake, false) {

				@Override
				public void execute() {
					if (victim.getPosition().sameAs(pos)) {
						drake.getCombatBuilder()
						.setContainer(new CombatContainer(drake, victim, 1, 1, CombatType.DRAGON_FIRE, true));
					}
					drake.setChargingAttack(false);
					stop();
				}
			});
			attack = 0;
		} else {
			if (Locations.goodDistance(drake.getPosition().copy(), victim.getPosition().copy(), 1)
					&& Misc.getRandom(5) <= 3) {
				drake.getCombatBuilder().setContainer(new CombatContainer(drake, victim, 1, 1, CombatType.MELEE, true));
				drake.performAnimation(MELEE);
			} else {
				new Projectile(drake, victim, 1664 + GameSettings.OSRS_GFX_OFFSET, 44, 3, 43, 43, 0).sendProjectile();
				drake.getCombatBuilder()
						.setContainer(new CombatContainer(drake, victim, 1, 1, CombatType.RANGED, true));
				drake.performAnimation(RANGE);
			}
		}
		attack++;
		return true;
	}

	@Override
	public int attackDelay(GameCharacter entity) {
		return entity.getAttackSpeed();
	}

	@Override
	public int attackDistance(GameCharacter entity) {
		return 5;
	}

	@Override
	public CombatType getCombatType() {
		return CombatType.MIXED;
	}
}
