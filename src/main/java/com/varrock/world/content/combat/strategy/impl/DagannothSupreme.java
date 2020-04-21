package com.varrock.world.content.combat.strategy.impl;

import com.varrock.engine.task.Task;
import com.varrock.engine.task.TaskManager;
import com.varrock.model.Animation;
import com.varrock.model.projectile.Projectile;
import com.varrock.world.content.combat.CombatContainer;
import com.varrock.world.content.combat.CombatType;
import com.varrock.world.content.combat.strategy.CombatStrategy;
import com.varrock.world.entity.impl.GameCharacter;
import com.varrock.world.entity.impl.npc.NPC;

public class DagannothSupreme implements CombatStrategy {

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
		NPC prime = (NPC)entity;
		if(prime.getConstitution() <= 0 || victim.getConstitution() <= 0) {
			return true;
		}
		prime.performAnimation(new Animation(prime.getDefinition().getAttackAnimation()));
		TaskManager.submit(new Task(1, prime, false) {

			@Override
			protected void execute() {
				new Projectile(prime, victim, 1937, 44, 3, 43, 43, 0).sendProjectile();
				prime.getCombatBuilder().setContainer(new CombatContainer(prime, victim, 1, 2, CombatType.RANGED, true));
				stop();
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
		return CombatType.RANGED;
	}
}
