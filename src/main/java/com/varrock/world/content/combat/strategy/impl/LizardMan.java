package com.varrock.world.content.combat.strategy.impl;

import com.varrock.engine.task.Task;
import com.varrock.engine.task.TaskManager;
import com.varrock.model.Animation;
import com.varrock.model.Graphic;
import com.varrock.model.Position;
import com.varrock.model.movement.PathFinder;
import com.varrock.model.projectile.Projectile;
import com.varrock.util.Misc;
import com.varrock.world.content.combat.CombatContainer;
import com.varrock.world.content.combat.CombatType;
import com.varrock.world.content.combat.strategy.CombatStrategy;
import com.varrock.world.entity.impl.GameCharacter;
import com.varrock.world.entity.impl.npc.NPC;

public class LizardMan implements CombatStrategy {

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
		NPC lizardman = (NPC)entity;
		if(lizardman.isChargingAttack() || victim.getConstitution() <= 0) {
			return true;
		}
		if(Misc.getRandom(15) <= 2){
			int hitAmount = 1;
			lizardman.performGraphic(new Graphic(69));
			lizardman.setConstitution(lizardman.getConstitution() + hitAmount);
			//((Player)victim).getPacketSender().sendMessage(MessageType.NPC_ALERT, "lizardman absorbs his next attack, healing himself a bit.");
		}
		if(lizardman.getPosition().isWithinDiagonalDistance(victim.getPosition(), 4, victim.getSize()) && Misc.randomFloat() < 0.61) {
			lizardman.performAnimation(new Animation(lizardman.getDefinition().getAttackAnimation()));
			lizardman.getCombatBuilder().setContainer(new CombatContainer(lizardman, victim, 1, 1, CombatType.MELEE, true));
			if(Misc.randomFloat() < 0.41) {

				Position pos = null;

				for (int i = 0; i < 100; i++) {
					Position test = victim.getPosition().transform(Misc.getRandom(6), Misc.getRandom(6), 0);
					if(PathFinder.findPath(victim, test.getX(), test.getY(), false, 0, 0)) {
						pos = test;
						break;
					}
				}

				if(pos != null) {
					victim.moveTo(pos);
					lizardman.performAnimation(new Animation(7192));
					victim.performAnimation(new Animation(534));
				}

				//((Player)victim).getPacketSender().sendMessage(MessageType.NPC_ALERT, "You have been knocked back!");
			}
		} else {
			lizardman.setChargingAttack(true);
			lizardman.performAnimation(new Animation(7193));
			lizardman.getCombatBuilder().setContainer(new CombatContainer(lizardman, victim, 1, 3, CombatType.MAGIC, true));
			TaskManager.submit(new Task(1, lizardman, false) {
				int tick = 0;
				@Override
				public void execute() {
					if(tick == 0) {
						new Projectile(lizardman, victim, 69, 44, 3, 41, 31, 0).sendProjectile();
					} else if(tick == 1) {
						lizardman.setChargingAttack(false);
						stop();
					}
					tick++;
				}
			});
		}
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