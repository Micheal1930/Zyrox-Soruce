package com.zyrox.world.content.combat.strategy.impl;

import com.zyrox.engine.task.Task;
import com.zyrox.engine.task.TaskManager;
import com.zyrox.model.Animation;
import com.zyrox.model.Graphic;
import com.zyrox.model.Locations;
import com.zyrox.model.Position;
import com.zyrox.model.projectile.Projectile;
import com.zyrox.util.Misc;
import com.zyrox.world.content.combat.CombatContainer;
import com.zyrox.world.content.combat.CombatHitTask;
import com.zyrox.world.content.combat.CombatType;
import com.zyrox.world.content.combat.strategy.CombatStrategy;
import com.zyrox.world.entity.impl.GameCharacter;
import com.zyrox.world.entity.impl.npc.NPC;
import com.zyrox.world.entity.impl.player.Player;

public class Tekton implements CombatStrategy {

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
		NPC tekton = (NPC)entity;
		if(tekton.isChargingAttack() || victim.getConstitution() <= 0) {
			return true;
		}
		if(Locations.goodDistance(tekton.getPosition().copy(), victim.getPosition().copy(), 1) && Misc.getRandom(5) <= 3) {
			tekton.performAnimation(new Animation(tekton.getDefinition().getAttackAnimation()));
			tekton.getCombatBuilder().setContainer(new CombatContainer(tekton, victim, 1, 1, CombatType.MELEE, true));
		} else if(!Locations.goodDistance(tekton.getPosition().copy(), victim.getPosition().copy(), 3) && Misc.getRandom(5) == 1) {
			tekton.setChargingAttack(true);
			final Position pos = new Position(victim.getPosition().getX() - 3, victim.getPosition().getY());
			((Player)victim).getPacketSender().sendGlobalGraphic(new Graphic(1640), pos);
			tekton.performAnimation(new Animation(22752));
			tekton.forceChat("I AM THE MIGHTY TEKTON!!");
			TaskManager.submit(new Task(2) {
				@Override
				protected void execute() {
					tekton.moveTo(pos);
					tekton.performAnimation(new Animation(tekton.getDefinition().getAttackAnimation()));
					tekton.getCombatBuilder().setContainer(new CombatContainer(tekton, victim, 1, 1, CombatType.MELEE, false));
					tekton.setChargingAttack(false);
					tekton.getCombatBuilder().setAttackTimer(0);
					stop();
				}
			});
		} else {
			tekton.setChargingAttack(true);
			boolean barrage = Misc.getRandom(4) <= 2;
			tekton.performAnimation(new Animation(barrage ? 22752 : 22754));
			tekton.forceChat("WRAHHHH!!!");
			tekton.getCombatBuilder().setContainer(new CombatContainer(tekton, victim, 1, 3, CombatType.MAGIC, true));
			TaskManager.submit(new Task(1, tekton, false) {
				int tick = 0;
				@Override
				public void execute() {
					if(tick == 0 && !barrage) {
						new Projectile(tekton, victim, 2051, 44, 3, 43, 43, 0).sendProjectile();
					} else if(tick == 1) {
						if(barrage && victim.isPlayer() && Misc.getRandom(10) <= 5) {
							victim.getMovementQueue().freeze(30);
							victim.performGraphic(new Graphic(2051));
						}
						if(barrage && Misc.getRandom(6) <= 3) {
							tekton.performAnimation(new Animation(22754));
							tekton.forceChat("YOU ARE ALL SMALL MAN!!!");
							for(Player toAttack : Misc.getCombinedPlayerList((Player)victim)) {
								if(toAttack != null && Locations.goodDistance(tekton.getPosition(), toAttack.getPosition(), 7) && toAttack.getConstitution() > 0) {
									new CombatHitTask(tekton.getCombatBuilder(), new CombatContainer(tekton, toAttack, 2, CombatType.MAGIC, false)).handleAttack();
									toAttack.performGraphic(new Graphic(2348));
									tekton.forceChat("MUWHAHAHA!!!");
								}
							}
						}
						tekton.setChargingAttack(false).getCombatBuilder().setAttackTimer(attackDelay(tekton) - 2);
						stop();
					}
					tick++;
				}
			});
		}
		return true;
	}

	public static int getAnimation(int npc) {
		int anim = 12259;
		if(npc == 50)
			anim = 14374;
		else if(npc == 5362 || npc == 5363)
			anim = 14374;
		else if(npc == 51)
			anim = 14374;
		return anim;
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