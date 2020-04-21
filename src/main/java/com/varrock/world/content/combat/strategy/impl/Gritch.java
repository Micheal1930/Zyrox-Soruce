package com.varrock.world.content.combat.strategy.impl;

import com.varrock.engine.task.Task;
import com.varrock.engine.task.TaskManager;
import com.varrock.model.Animation;
import com.varrock.model.Graphic;
import com.varrock.model.Locations;
import com.varrock.model.projectile.Projectile;
import com.varrock.util.Misc;
import com.varrock.world.content.combat.CombatContainer;
import com.varrock.world.content.combat.CombatType;
import com.varrock.world.content.combat.strategy.CombatStrategy;
import com.varrock.world.entity.impl.GameCharacter;
import com.varrock.world.entity.impl.npc.NPC;

public class Gritch implements CombatStrategy {

	private static final Animation anim = new Animation(69);
	private static final Graphic gfx = new Graphic(386);
	
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
		NPC gritch = (NPC)entity;
		if(gritch.isChargingAttack() || victim.getConstitution() <= 0) {
			gritch.getCombatBuilder().setAttackTimer(4);
			return true;
		}
		if(Locations.goodDistance(gritch.getPosition().copy(), victim.getPosition().copy(), 1) && Misc.getRandom(5) <= 3) {
			gritch.performAnimation(new Animation(gritch.getDefinition().getAttackAnimation()));
			gritch.getCombatBuilder().setContainer(new CombatContainer(gritch, victim, 1, 1, CombatType.MELEE, true));
		} else {
			gritch.setChargingAttack(true);
			gritch.performAnimation(anim);
			gritch.getCombatBuilder().setContainer(new CombatContainer(gritch, victim, 1, 3, CombatType.RANGED, true));
			TaskManager.submit(new Task(1, gritch, false) {
				int tick = 0;
				@Override
				public void execute() {
					if(tick == 1) {
						new Projectile(gritch, victim, gfx.getId(), 44, 3, 43, 43, 0).sendProjectile();
						gritch.setChargingAttack(false);
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
