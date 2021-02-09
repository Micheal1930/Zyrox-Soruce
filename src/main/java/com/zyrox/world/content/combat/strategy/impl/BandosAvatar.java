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

public class BandosAvatar implements CombatStrategy {

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
		NPC bandosAvatar = (NPC)entity;
		if(bandosAvatar.isChargingAttack() || victim.getConstitution() <= 0) {
			return true;
		}
		if(Locations.goodDistance(bandosAvatar.getPosition().copy(), victim.getPosition().copy(), 1) && Misc.getRandom(5) <= 3) {
			bandosAvatar.performAnimation(new Animation(bandosAvatar.getDefinition().getAttackAnimation()));
			bandosAvatar.getCombatBuilder().setContainer(new CombatContainer(bandosAvatar, victim, 1, 1, CombatType.MELEE, true));
		} else if(!Locations.goodDistance(bandosAvatar.getPosition().copy(), victim.getPosition().copy(), 3) && Misc.getRandom(5) == 1) {
			bandosAvatar.setChargingAttack(true);
			final Position pos = new Position(victim.getPosition().getX() - 2, victim.getPosition().getY());
			((Player)victim).getPacketSender().sendGlobalGraphic(new Graphic(1549), pos);
			bandosAvatar.performAnimation(new Animation(11246));
			bandosAvatar.forceChat("You shall perish!");
			TaskManager.submit(new Task(2) {
				@Override
				protected void execute() {
					bandosAvatar.moveTo(pos);
					bandosAvatar.performAnimation(new Animation(bandosAvatar.getDefinition().getAttackAnimation()));
					bandosAvatar.getCombatBuilder().setContainer(new CombatContainer(bandosAvatar, victim, 1, 1, CombatType.MELEE, false));
					bandosAvatar.setChargingAttack(false);
					bandosAvatar.getCombatBuilder().setAttackTimer(0);
					stop();
				}
			});
		} else {
			bandosAvatar.setChargingAttack(true);
			boolean barrage = Misc.getRandom(4) <= 2;
			bandosAvatar.performAnimation(new Animation(barrage ? 11245 : 11252));
			bandosAvatar.getCombatBuilder().setContainer(new CombatContainer(bandosAvatar, victim, 1, 3, CombatType.MAGIC, true));
			TaskManager.submit(new Task(1, bandosAvatar, false) {
				int tick = 0;
				@Override
				public void execute() {
					if(tick == 0 && !barrage) {
						new Projectile(bandosAvatar, victim, 2706, 44, 3, 43, 43, 0).sendProjectile();
					} else if(tick == 1) {
						if(barrage && victim.isPlayer() && Misc.getRandom(10) <= 5) {
							victim.getMovementQueue().freeze(15);
							victim.performGraphic(new Graphic(369));
						}
						if(barrage && Misc.getRandom(6) <= 3) {
							bandosAvatar.performAnimation(new Animation(11245));
							for(Player toAttack : Misc.getCombinedPlayerList((Player)victim)) {
								if(toAttack != null && Locations.goodDistance(bandosAvatar.getPosition(), toAttack.getPosition(), 7) && toAttack.getConstitution() > 0) {
									bandosAvatar.forceChat("DIE!");
									new CombatHitTask(bandosAvatar.getCombatBuilder(), new CombatContainer(bandosAvatar, toAttack, 2, CombatType.MAGIC, false)).handleAttack();
									toAttack.performGraphic(new Graphic(1556));
								}
							}
						}
						bandosAvatar.setChargingAttack(false).getCombatBuilder().setAttackTimer(attackDelay(bandosAvatar) - 2);
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
			anim = 81;
		else if(npc == 5362 || npc == 5363)
			anim = 14246;
		else if(npc == 51)
			anim = 13152;
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
