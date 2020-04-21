package com.varrock.world.content.combat.strategy.zulrah.npc;

import com.varrock.engine.task.Task;
import com.varrock.engine.task.TaskManager;
import com.varrock.model.Animation;
import com.varrock.model.projectile.Projectile;
import com.varrock.util.Misc;
import com.varrock.world.content.combat.CombatContainer;
import com.varrock.world.content.combat.CombatHitTask;
import com.varrock.world.content.combat.CombatType;
import com.varrock.world.content.combat.strategy.CombatStrategy;
import com.varrock.world.entity.impl.GameCharacter;
import com.varrock.world.entity.impl.npc.impl.Zulrah;
import com.varrock.world.entity.impl.player.Player;

public class Blue implements CombatStrategy {

	@Override
	public boolean canAttack(GameCharacter entity, GameCharacter victim) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public CombatContainer attack(GameCharacter entity, GameCharacter victim) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean customContainerAttack(GameCharacter entity, GameCharacter victim) {
		if (((Zulrah) entity).isIdle()) {
			return true;
		}
		
		switch(((Zulrah) entity).getPhase()) {
			case 3:
			case 6:
				normalAttack(entity, victim);
				break;
		}
		return true; 
	}
	
	private void normalAttack(GameCharacter entity, GameCharacter victim) {
		int rand = Misc.getRandom(10);
		if (rand != 9 && rand != 4 && rand != 5 && rand != 6) {
			TaskManager.submit(new Task(2, entity, true) {
				int tick;
				@Override
				public void execute() {
					if(tick == 0) {
						entity.performAnimation(new Animation(5069));
						new Projectile(entity, victim, 1046, 44, 1, 43, 43, 0).sendProjectile();
					}
					if(tick == 1) {
						if(victim.isPlayer()) {
						new CombatHitTask(entity.getCombatBuilder(), new CombatContainer(entity, victim, 1, CombatType.MAGIC, true)).handleAttack();
						}
						stop();
					}
					tick++;
				}
			});
		} else if (rand == 4 || rand == 5 || rand == 6) {
			TaskManager.submit(new Task(2, entity, true) {
				int tick;
				@Override
				public void execute() {
					if(tick == 0) {
						entity.performAnimation(new Animation(5069));
						new Projectile(entity, victim, 1044, 44, 1, 43, 43, 0).sendProjectile();
					} 
					if(tick == 1) {
						if(victim.isNpc()) {
							return;
						}
						new CombatHitTask(entity.getCombatBuilder(), new CombatContainer(entity, victim, 1, CombatType.RANGED, true)).handleAttack();
						stop();
					}
					tick++;
				}
			});
		} else {
			if (entity.isNpc()) {
				((Zulrah) entity).despawn(entity, (Player) victim);
			}
		}
	}


	@Override
	public int attackDelay(GameCharacter entity) {
		return 7;
	}

	@Override
	public int attackDistance(GameCharacter entity) {
		return 30;
	}

	@Override
	public CombatType getCombatType() {
		// TODO Auto-generated method stub
		return null;
	}

}
