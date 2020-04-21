package com.varrock.world.content.inferno.node;

import com.varrock.engine.task.Task;
import com.varrock.engine.task.TaskManager;
import com.varrock.model.*;
import com.varrock.util.Misc;
import com.varrock.world.content.combat.CombatContainer;
import com.varrock.world.content.combat.prayer.PrayerHandler;
import com.varrock.world.content.combat.strategy.CombatStrategy;
import com.varrock.world.entity.impl.GameCharacter;
import com.varrock.world.entity.impl.npc.NPC;
import com.varrock.world.entity.impl.player.Player;

public abstract class InfernoNode extends NPC implements CombatStrategy  {

	public InfernoNode(int id, Position position) {
		super(id, position);
	}

	@Override
	public boolean canAttack(GameCharacter entity, GameCharacter victim) {
		return victim.isPlayer();
	}

	@Override
	public CombatContainer attack(GameCharacter entity, GameCharacter victim) {
		return null;
	}

	@Override
	public int attackDelay(GameCharacter entity) {
		return 5;
	}

	@Override
	public int attackDistance(GameCharacter entity) {
		return 64;
	}
	
	@Override
	public CombatStrategy determineStrategy() {
		return this;
	}
	
	public void throttleFarcast(NPC attacker, GameCharacter victim, int delay, int prayer) {
		TaskManager.submit(new Task(delay) {
			
			@Override
			protected void execute() {
			 
				int max = (int) (attacker.getDefinition().getMaxHit() );
				if (PrayerHandler.isActivated((Player) victim,prayer)) {
					max *= 0.4;
				}

				if(victim.getLocation() == Locations.Location.INFERNO)
					victim.dealDamage(new Hit(Misc.random(max), Hitmask.RED, prayer ==  PrayerHandler.PROTECT_FROM_MISSILES ? CombatIcon.RANGED : CombatIcon.MAGIC));
				stop();
				
			}
		});
	}


}
