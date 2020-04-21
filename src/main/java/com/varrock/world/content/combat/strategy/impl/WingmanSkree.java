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

public class WingmanSkree implements CombatStrategy {

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
		NPC skree = (NPC)entity;
		if(victim.getConstitution() <= 0) {
			return true;
		}
		if(skree.isChargingAttack()) {
			return true;
		}
		
		skree.performAnimation(new Animation(skree.getDefinition().getAttackAnimation()));
		skree.setChargingAttack(true);

		skree.getCombatBuilder().setContainer(new CombatContainer(skree, victim, 1, 3, CombatType.MAGIC, true));
		
		TaskManager.submit(new Task(1, skree, false) {
			int tick = 0;
			@Override
			public void execute() {
				if(tick == 1) {
					new Projectile(skree, victim, 1505, 44, 3, 43, 43, 0).sendProjectile();
					skree.setChargingAttack(false);
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
		return CombatType.MAGIC;
	}
}
