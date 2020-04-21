package com.varrock.world.content.combat.strategy.impl;

import com.varrock.engine.task.Task;
import com.varrock.engine.task.TaskManager;
import com.varrock.model.Animation;
import com.varrock.model.Locations;
import com.varrock.model.Locations.Location;
import com.varrock.model.projectile.Projectile;
import com.varrock.util.Misc;
import com.varrock.world.content.combat.CombatContainer;
import com.varrock.world.content.combat.CombatType;
import com.varrock.world.content.combat.strategy.CombatStrategy;
import com.varrock.world.entity.impl.GameCharacter;
import com.varrock.world.entity.impl.npc.NPC;

public class Revenant implements CombatStrategy {

	enum REVENANT_DATA {

		REVENANT_IMP(6715, new Animation(7500), new Animation(7501)),
		REVENANT_GOBLIN(6716, new Animation(7499), new Animation(7513)),
		REVENANT_WEREWOLF(6701, new Animation(7496), new Animation(7521)),
		REVENANT_ORK(6725, new Animation(7505), new Animation(7518)),
		REVENANT_DARK_BEAST(6691, new Animation(7502), new Animation(7514)),
		REVENANT_DARK_BEAST2(22938, new Animation(2731), new Animation(2731)),
		
		REVENANT_GARGOYLE(8832, new Animation(23071), new Animation(23071)),
		
		REVENANT_DRAGON(22940, new Animation(80), new Animation(80));

		REVENANT_DATA(int npc, Animation magicAttack, Animation rangedAttack) {
			this.npc = npc;
			this.magicAttack = magicAttack;
			this.rangedAttack = rangedAttack;
		}

		private int npc;
		public Animation magicAttack, rangedAttack;

		public static REVENANT_DATA getData(int npc) {
			for(REVENANT_DATA data : REVENANT_DATA.values()) {
				if(data != null && data.npc == npc) {
					return data;
				}
			}
			return null;
		}
	}

	@Override
	public boolean canAttack(GameCharacter entity, GameCharacter victim) {
		return victim.getLocation() == Location.WILDERNESS;
	}

	@Override
	public CombatContainer attack(GameCharacter entity, GameCharacter victim) {
		return null;
	}

	@SuppressWarnings("incomplete-switch")
	@Override
	public boolean customContainerAttack(GameCharacter entity, GameCharacter victim) {
		NPC revenant = (NPC)entity;
		if(revenant.isChargingAttack() || victim.getConstitution() <= 0) {
			return true;
		}
		final CombatType attkType = Misc.getRandom(5) <= 2 && Locations.goodDistance(revenant.getPosition(), victim.getPosition(), 2) ? CombatType.MELEE : Misc.getRandom(10) <= 5 ? CombatType.MAGIC : CombatType.RANGED;
		switch(attkType) {
		case MELEE:
			revenant.performAnimation(new Animation(revenant.getDefinition().getAttackAnimation()));
			revenant.getCombatBuilder().setContainer(new CombatContainer(revenant, victim, 1, 1, CombatType.MELEE, true));
			break;
		case MAGIC:
		case RANGED:
			final REVENANT_DATA revData = REVENANT_DATA.getData(revenant.getId());
			revenant.setChargingAttack(true);
			revenant.performAnimation(attkType == CombatType.MAGIC ? revData.magicAttack : revData.rangedAttack);
			revenant.getCombatBuilder().setContainer(new CombatContainer(revenant, victim, 1, 2, attkType, true));
			TaskManager.submit(new Task(1, revenant, false) {
				int tick = 0;
				@Override
				public void execute() {
					switch(tick) {
					case 1:
						new Projectile(revenant, victim, (attkType == CombatType.RANGED ? 970 : 280), 44, 3, 43, 43, 0).sendProjectile();
						break;
					case 3:
						revenant.setChargingAttack(false);
						stop();
						break;
					}
					tick++;
				}
			});
			break;
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
