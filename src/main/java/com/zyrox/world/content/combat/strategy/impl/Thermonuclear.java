package com.zyrox.world.content.combat.strategy.impl;

import com.zyrox.engine.task.Task;
import com.zyrox.engine.task.TaskManager;
import com.zyrox.model.projectile.Projectile;
import com.zyrox.util.Misc;
import com.zyrox.world.content.combat.CombatContainer;
import com.zyrox.world.content.combat.CombatType;
import com.zyrox.world.content.combat.strategy.CombatStrategy;
import com.zyrox.world.entity.impl.GameCharacter;
import com.zyrox.world.entity.impl.npc.NPC;

public class Thermonuclear implements CombatStrategy {

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
		NPC thermo = (NPC)entity;
		if(victim.getConstitution() <= 0) {
			return true;
		}
		if(thermo.isChargingAttack()) {
			return true;
		}
		thermo.setChargingAttack(true);
		final CombatType attkType = Misc.getRandom(5) <= 2 ? CombatType.RANGED : CombatType.MAGIC;
		thermo.getCombatBuilder().setContainer(new CombatContainer(thermo, victim, 1, 4, attkType, Misc.getRandom(5) <= 1 ? false : true));
		TaskManager.submit(new Task(1, thermo, false) {
			int tick = 0;
			@Override
			public void execute() {
				if(tick == 2) {
					new Projectile(thermo, victim, (attkType == CombatType.RANGED ? 605 : 971), 44, 3, 43, 43, 0).sendProjectile();
					thermo.setChargingAttack(false);
					stop();
				}
				tick++;
			}
		});
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
