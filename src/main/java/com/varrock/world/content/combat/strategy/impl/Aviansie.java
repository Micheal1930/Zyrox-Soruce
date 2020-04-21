package com.varrock.world.content.combat.strategy.impl;

import com.varrock.engine.task.Task;
import com.varrock.engine.task.TaskManager;
import com.varrock.model.Animation;
import com.varrock.model.Locations;
import com.varrock.model.projectile.Projectile;
import com.varrock.util.Misc;
import com.varrock.world.content.combat.CombatContainer;
import com.varrock.world.content.combat.CombatType;
import com.varrock.world.content.combat.strategy.CombatStrategy;
import com.varrock.world.entity.impl.GameCharacter;
import com.varrock.world.entity.impl.npc.NPC;

public class Aviansie implements CombatStrategy {

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
		NPC aviansie = (NPC)entity;
		if(aviansie.isChargingAttack() || victim.getConstitution() <= 0) {
			return true;
		}
		if(Locations.goodDistance(aviansie.getPosition().copy(), victim.getPosition().copy(), 1) && Misc.getRandom(5) <= 3) {
			aviansie.performAnimation(new Animation(aviansie.getDefinition().getAttackAnimation()));
			aviansie.getCombatBuilder().setContainer(new CombatContainer(aviansie, victim, 1, 1, CombatType.MELEE, true));
		} else {
			aviansie.setChargingAttack(true);
			aviansie.performAnimation(new Animation(aviansie.getDefinition().getAttackAnimation()));
			aviansie.getCombatBuilder().setContainer(new CombatContainer(aviansie, victim, 1, 3, aviansie.getId() == 6231 ? CombatType.MAGIC : CombatType.RANGED, true));
			TaskManager.submit(new Task(1, aviansie, false) {
				int tick = 0;
				@Override
				public void execute() {
					if(tick == 0) {
						new Projectile(aviansie, victim, getGfx(aviansie.getId()), 44, 3, 43, 43, 0).sendProjectile();
					} else if(tick == 1) {
						aviansie.setChargingAttack(false);
						stop();
					}
					tick++;
				}
			});
		}
		return true;
	}

	public static int getGfx(int npc) {
		switch(npc) {
		case 6230:
			return 1837;
		case 6231:
			return 2729;
		}
		return 37;
	}

	@Override
	public int attackDelay(GameCharacter entity) {
		return entity.getAttackSpeed();
	}

	@Override
	public int attackDistance(GameCharacter entity) {
		return 4;
	}

	@Override
	public CombatType getCombatType() {
		return CombatType.MIXED;
	}
}
