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
import com.varrock.world.entity.impl.player.Player;

public class Geerin implements CombatStrategy {

	@Override
	public boolean canAttack(GameCharacter entity, GameCharacter victim) {
		return victim.isPlayer() && ((Player)victim).getMinigameAttributes().getGodwarsDungeonAttributes().hasEnteredRoom();
	}

	@Override
	public CombatContainer attack(GameCharacter entity, GameCharacter victim) {
		return null;
	}

	@Override
	public boolean customContainerAttack(GameCharacter entity, GameCharacter victim) {
		NPC geerin = (NPC)entity;
		if(geerin.isChargingAttack() || victim.getConstitution() <= 0 || geerin.getConstitution() <= 0) {
			return true;
		}
		
		geerin.performAnimation(new Animation(geerin.getDefinition().getAttackAnimation()));
		geerin.setChargingAttack(true);

		geerin.getCombatBuilder().setContainer(new CombatContainer(geerin, victim, 1, 3, CombatType.RANGED, true));
		
		TaskManager.submit(new Task(1, geerin, false) {
			int tick = 0;
			@Override
			public void execute() {
				if(tick == 1) {
					new Projectile(geerin, victim, 1837, 44, 3, 43, 43, 0).sendProjectile();
					geerin.setChargingAttack(false);
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
		return 6;
	}

	@Override
	public CombatType getCombatType() {
		return CombatType.RANGED;
	}
}
