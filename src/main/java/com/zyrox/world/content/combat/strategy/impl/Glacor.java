package com.zyrox.world.content.combat.strategy.impl;

import com.zyrox.engine.task.Task;
import com.zyrox.engine.task.TaskManager;
import com.zyrox.model.Animation;
import com.zyrox.model.Graphic;
import com.zyrox.model.Locations;
import com.zyrox.model.projectile.Projectile;
import com.zyrox.util.Misc;
import com.zyrox.world.content.combat.CombatContainer;
import com.zyrox.world.content.combat.CombatType;
import com.zyrox.world.content.combat.strategy.CombatStrategy;
import com.zyrox.world.entity.impl.GameCharacter;
import com.zyrox.world.entity.impl.npc.NPC;

public class Glacor implements CombatStrategy {


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
		NPC glacor = (NPC)entity;
		if(glacor.isChargingAttack() || victim.getConstitution() <= 0) {
			return true;
		}
		if(Locations.goodDistance(glacor.getPosition().copy(), victim.getPosition().copy(), 1) && Misc.getRandom(5) <= 3) {
			glacor.performAnimation(new Animation(glacor.getDefinition().getAttackAnimation()));
			glacor.getCombatBuilder().setContainer(new CombatContainer(glacor, victim, 1, 1, CombatType.MELEE, true));
		} else {
			glacor.performAnimation(new Animation(9952));
			glacor.setChargingAttack(true);
			glacor.getCombatBuilder().setContainer(new CombatContainer(glacor, victim, 1, 2, CombatType.MAGIC, Misc.getRandom(10) <= 2 ? false : true));
			TaskManager.submit(new Task(1, glacor, false) {
				int tick = 0;
				@Override
				protected void execute() {
					switch(tick) {
					case 0:
						new Projectile(glacor, victim, 1017, 44, 3, 43, 31, 0).sendProjectile();
						break;
					case 1:
						victim.performGraphic(new Graphic(504));
						glacor.setChargingAttack(false);
						stop();
						break;
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
		return 8;
	}

	@Override
	public CombatType getCombatType() {
		return CombatType.MIXED;
	}
}
