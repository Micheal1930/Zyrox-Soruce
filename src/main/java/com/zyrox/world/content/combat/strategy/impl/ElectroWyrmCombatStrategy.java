package com.zyrox.world.content.combat.strategy.impl;

import com.zyrox.GameSettings;
import com.zyrox.engine.task.Task;
import com.zyrox.engine.task.TaskManager;
import com.zyrox.model.Animation;
import com.zyrox.model.Locations;
import com.zyrox.model.Position;
import com.zyrox.model.projectile.Projectile;
import com.zyrox.util.Misc;
import com.zyrox.world.content.combat.CombatContainer;
import com.zyrox.world.content.combat.CombatType;
import com.zyrox.world.content.combat.strategy.CombatStrategy;
import com.zyrox.world.entity.impl.GameCharacter;
import com.zyrox.world.entity.impl.npc.NPC;

/**
 * Handles Drake Combat strategy
 * 
 * @author 2012 <https://www.rune-server.ee/members/dexter+morgan/>
 *
 */
public class ElectroWyrmCombatStrategy implements CombatStrategy {

	/**
	 * Attacking melee
	 */
	private static final Animation MELEE = new Animation(23528);

	/**
	 * The WYRM npc id
	 */
	public static final int WYRM = 23611;

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
		NPC wyrm = (NPC) entity;
		if (wyrm.isChargingAttack() || wyrm.getConstitution() <= 0) {
			wyrm.getCombatBuilder().setAttackTimer(4);
			return true;
		}
		attack = 0;
		if (attack == 7) {
			Position pos = victim.getPosition().copy();
			wyrm.setChargingAttack(true).getCombatBuilder().setAttackTimer(6);
			new Projectile(wyrm, victim, 1740, 44, 3, 43, 43, 0).sendProjectile();
			TaskManager.submit(new Task(4, wyrm, false) {

				@Override
				public void execute() {
					if (victim.getPosition().sameAs(pos)) {
						wyrm.getCombatBuilder()
						.setContainer(new CombatContainer(wyrm, victim, 1, 1, CombatType.DRAGON_FIRE, true));
					}
					wyrm.setChargingAttack(false);
					stop();
				}
			});
			attack = 0;
		} else {
			if (Locations.goodDistance(wyrm.getPosition().copy(), victim.getPosition().copy(), 1)
					&& Misc.getRandom(5) <= 3) {
				wyrm.getCombatBuilder().setContainer(new CombatContainer(wyrm, victim, 1, 1, CombatType.MELEE, true));
			} else {
				new Projectile(wyrm, victim, 1251 + GameSettings.OSRS_GFX_OFFSET, 44, 3, 43, 43, 0).sendProjectile();
				wyrm.getCombatBuilder()
						.setContainer(new CombatContainer(wyrm, victim, 1, 1, CombatType.RANGED, true));
			}
			wyrm.performAnimation(MELEE);
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
