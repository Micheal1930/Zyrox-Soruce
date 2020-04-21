package com.varrock.world.content.combat.strategy.impl;

import com.varrock.engine.task.Task;
import com.varrock.engine.task.TaskManager;
import com.varrock.model.Animation;
import com.varrock.model.Graphic;
import com.varrock.model.Locations;
import com.varrock.model.Locations.Location;
import com.varrock.util.Misc;
import com.varrock.world.content.combat.CombatContainer;
import com.varrock.world.content.combat.CombatHitTask;
import com.varrock.world.content.combat.CombatType;
import com.varrock.world.content.combat.strategy.CombatStrategy;
import com.varrock.world.entity.impl.GameCharacter;
import com.varrock.world.entity.impl.npc.NPC;
import com.varrock.world.entity.impl.player.Player;

public class Death implements CombatStrategy {

	

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
		NPC death = (NPC)entity;
		Animation attack_anim = death.getAnimation();
		Graphic graphic1 = death.getGraphic();
		if(death.isChargingAttack() || death.getConstitution() <= 0) {
			return true;
		}
		CombatType style = Misc.getRandom(4) <= 1 && Locations.goodDistance(death.getPosition(), victim.getPosition(), 1) ? CombatType.MELEE : CombatType.MELEE;	
		if(style == CombatType.MELEE) {
			
			death.performAnimation(new Animation(death.getDefinition().getAttackAnimation()));
			death.getCombatBuilder().setContainer(new CombatContainer(death, victim, 1, 1, CombatType.MELEE, true));
		} else {
			death.performAnimation(attack_anim);
			death.setChargingAttack(true);
			Player target = (Player)victim;
			for (Player t : Misc.getCombinedPlayerList(target)) {
				if(t == null || t.getLocation() != Location.TREASURE_ISLAND || t.isTeleporting())
					continue;
				if(t.getPosition().distanceToPoint(death.getPosition().getX(), death.getPosition().getY()) > 20)
					continue;
				new CombatHitTask(death.getCombatBuilder(), new CombatContainer(death, t, 1, CombatType.MELEE, true)).handleAttack();

			}
			TaskManager.submit(new Task(2, target, false) {
				@Override
				public void execute() {
					for (Player t : Misc.getCombinedPlayerList(target)) {
						if(t == null || t.getLocation() != Location.TREASURE_ISLAND)
							continue;
						death.getCombatBuilder().setVictim(t);
						new CombatHitTask(death.getCombatBuilder(), new CombatContainer(death, t, 1, CombatType.MELEE, true)).handleAttack();
					}
					death.setChargingAttack(false);
					stop();
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
		return 7;
	}

	@Override
	public CombatType getCombatType() {
		return CombatType.MELEE;
	}
}
