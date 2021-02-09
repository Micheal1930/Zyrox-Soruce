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
 * Handles NAZASTAROOL Combat strategy
 * 
 * @author 2012 <https://www.rune-server.ee/members/dexter+morgan/>
 *
 */
public class NazastaroolCombatStrategy implements CombatStrategy {

	/**
	 * Attacking melee
	 */
	private static final Animation MELEE = new Animation(20795);
	
		/**
	 * Attacking mage
	 */
	private static final Animation MAGIC = new Animation(20796);

	/**
	 * The NAZASTAROOL npc id
	 */
	public static final int NAZASTAROOL = 20355;

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
		NPC nazastarool = (NPC) entity;
		if (nazastarool.isChargingAttack() || nazastarool.getConstitution() <= 0) {
			nazastarool.getCombatBuilder().setAttackTimer(4);
			return true;
		}
		attack = 0;
		if (attack == 7) {
			Position pos = victim.getPosition().copy();
			nazastarool.setChargingAttack(true).getCombatBuilder().setAttackTimer(6);
			new Projectile(nazastarool, victim, 1741, 44, 3, 43, 43, 0).sendProjectile();
			TaskManager.submit(new Task(4, nazastarool, false) {

				@Override
				public void execute() {
					if (victim.getPosition().sameAs(pos)) {
						nazastarool.getCombatBuilder()
						.setContainer(new CombatContainer(nazastarool, victim, 1, 1, CombatType.MAGIC, true));
					}
					nazastarool.setChargingAttack(false);
					stop();
				}
			});
			attack = 0;
		} else {
			if (Locations.goodDistance(nazastarool.getPosition().copy(), victim.getPosition().copy(), 1)
					&& Misc.getRandom(5) <= 3) {
				nazastarool.getCombatBuilder().setContainer(new CombatContainer(nazastarool, victim, 1, 1, CombatType.MELEE, true));
			} else {
				new Projectile(nazastarool, victim, 1290 + GameSettings.OSRS_GFX_OFFSET, 44, 3, 43, 43, 0).sendProjectile();
				nazastarool.getCombatBuilder()
						.setContainer(new CombatContainer(nazastarool, victim, 1, 1, CombatType.RANGED, true));
			}
			nazastarool.performAnimation(MELEE);
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
