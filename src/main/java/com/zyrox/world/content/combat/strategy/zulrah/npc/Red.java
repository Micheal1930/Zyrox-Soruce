package com.zyrox.world.content.combat.strategy.zulrah.npc;

import com.zyrox.engine.task.Task;
import com.zyrox.engine.task.TaskManager;
import com.zyrox.model.Animation;
import com.zyrox.util.Misc;
import com.zyrox.world.content.combat.CombatContainer;
import com.zyrox.world.content.combat.CombatHitTask;
import com.zyrox.world.content.combat.CombatType;
import com.zyrox.world.content.combat.strategy.CombatStrategy;
import com.zyrox.world.entity.impl.GameCharacter;
import com.zyrox.world.entity.impl.npc.impl.Zulrah;
import com.zyrox.world.entity.impl.player.Player;

public class Red implements CombatStrategy {

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
			case 2:
			case 5:
			case 9:
				normalAttack(entity, victim);
				break;
		}
		return true; 
	}

	private void normalAttack(GameCharacter entity, GameCharacter victim) {
		int rand = Misc.getRandom(10);
		if(rand != 9) {
			TaskManager.submit(new Task(2, entity, true) {
				int tick;
				@Override
				public void execute() {
					if(tick == 0) {
						entity.performAnimation(new Animation(5807));
					} 
					if(tick == 3) {
						if(victim.isPlayer()) {
							new CombatHitTask(entity.getCombatBuilder(), new CombatContainer(entity, victim, 1, CombatType.MELEE, true)).handleAttack();
						}
						stop();
					}
					tick++;
				}
			});
		} else {
			((Zulrah) entity).despawn(entity, (Player) victim);
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
