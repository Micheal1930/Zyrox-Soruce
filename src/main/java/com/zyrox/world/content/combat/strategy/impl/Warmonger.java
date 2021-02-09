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

public class Warmonger implements CombatStrategy {

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
		NPC warmonger = (NPC)entity;
		if(warmonger.isChargingAttack() || victim.getConstitution() <= 0) {
			return true;
		}
		if(Locations.goodDistance(warmonger.getPosition().copy(), victim.getPosition().copy(), 1) && Misc.getRandom(5) <= 3) {
			warmonger.performAnimation(new Animation(warmonger.getDefinition().getAttackAnimation()));
			warmonger.getCombatBuilder().setContainer(new CombatContainer(warmonger, victim, 1, 1, CombatType.MELEE, true));
		} else if(!Locations.goodDistance(warmonger.getPosition().copy(), victim.getPosition().copy(), 3) && Misc.getRandom(5) == 1) {
			warmonger.setChargingAttack(true);
			final Position pos = new Position(victim.getPosition().getX() - 2, victim.getPosition().getY());
			((Player)victim).getPacketSender().sendGlobalGraphic(new Graphic(1639), pos);
			warmonger.performAnimation(new Animation(14374));
			warmonger.forceChat("WRAHHHHHHHHHHHHHHHH!");
			TaskManager.submit(new Task(2) {
				@Override
				protected void execute() {
					warmonger.moveTo(pos);
					warmonger.performAnimation(new Animation(warmonger.getDefinition().getAttackAnimation()));
					warmonger.getCombatBuilder().setContainer(new CombatContainer(warmonger, victim, 1, 1, CombatType.MELEE, false));
					warmonger.setChargingAttack(false);
					warmonger.getCombatBuilder().setAttackTimer(0);
					stop();
				}
			});
		} else {
			warmonger.setChargingAttack(true);
			boolean barrage = Misc.getRandom(4) <= 2;
			warmonger.performAnimation(new Animation(barrage ? 14374 : 14374));
			warmonger.getCombatBuilder().setContainer(new CombatContainer(warmonger, victim, 1, 3, CombatType.MAGIC, true));
			TaskManager.submit(new Task(1, warmonger, false) {
				int tick = 0;
				@Override
				public void execute() {
					if(tick == 0 && !barrage) {
						new Projectile(warmonger, victim, 1264, 44, 3, 43, 43, 0).sendProjectile();
					} else if(tick == 1) {
						if(barrage && victim.isPlayer() && Misc.getRandom(10) <= 5) {
							victim.getMovementQueue().freeze(15);
							victim.performGraphic(new Graphic(2144));
						}
						if(barrage && Misc.getRandom(6) <= 3) {
							warmonger.performAnimation(new Animation(14374));
							for(Player toAttack : Misc.getCombinedPlayerList((Player)victim)) {
								if(toAttack != null && Locations.goodDistance(warmonger.getPosition(), toAttack.getPosition(), 7) && toAttack.getConstitution() > 0) {
									warmonger.forceChat("I WILL NEVER FALL!!");
									new CombatHitTask(warmonger.getCombatBuilder(), new CombatContainer(warmonger, toAttack, 2, CombatType.MAGIC, false)).handleAttack();
									toAttack.performGraphic(new Graphic(2345));
								}
							}
						}
						warmonger.setChargingAttack(false).getCombatBuilder().setAttackTimer(attackDelay(warmonger) - 2);
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
