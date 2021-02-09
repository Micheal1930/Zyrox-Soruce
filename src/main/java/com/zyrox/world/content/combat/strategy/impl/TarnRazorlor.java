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

public class TarnRazorlor implements CombatStrategy {

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
		NPC tarn = (NPC)entity;
		if(tarn.isChargingAttack() || victim.getConstitution() <= 0) {
			return true;
		}
		if(Locations.goodDistance(tarn.getPosition().copy(), victim.getPosition().copy(), 1) && Misc.getRandom(5) <= 3) {
			tarn.performAnimation(new Animation(tarn.getDefinition().getAttackAnimation()));
			tarn.getCombatBuilder().setContainer(new CombatContainer(tarn, victim, 1, 1, CombatType.MELEE, true));
		} if(!Locations.goodDistance(tarn.getPosition().copy(), victim.getPosition().copy(), 3) && Misc.getRandom(5) == 1) {
			tarn.setChargingAttack(true);
			final Position pos = new Position(victim.getPosition().getX() - 2, victim.getPosition().getY());
			((Player)victim).getPacketSender().sendGlobalGraphic(new Graphic(2367), pos);
			tarn.performAnimation(new Animation(5613));
			tarn.forceChat("WWWRAHHHHHHHHHHHHHHH!!!!!");
			TaskManager.submit(new Task(2) {
				@Override
				protected void execute() {
					tarn.performAnimation(new Animation(tarn.getDefinition().getAttackAnimation()));
					tarn.getCombatBuilder().setContainer(new CombatContainer(tarn, victim, 1, 1, CombatType.MELEE, false));
					tarn.setChargingAttack(false);
					tarn.getCombatBuilder().setAttackTimer(0);
					stop();
				}
			});
		} else {
			tarn.setChargingAttack(true);
			boolean barrage = Misc.getRandom(4) <= 2;
			tarn.performAnimation(new Animation(barrage ? 5613 : 5613));
			victim.performGraphic(new Graphic(1655));
			tarn.forceChat("WRAAAAAHHHHHHH!!!!!");
			tarn.getCombatBuilder().setContainer(new CombatContainer(tarn, victim, 1, 3, CombatType.MAGIC, true));
			TaskManager.submit(new Task(1, tarn, false) {
				int tick = 0;
				@Override
				public void execute() {
					if(tick == 0 && !barrage) {
						new Projectile(tarn, victim, 2368, 44, 3, 43, 43, 0).sendProjectile();
					} else if(tick == 1) {
						if(barrage && victim.isPlayer() && Misc.getRandom(10) <= 5) {
							victim.getMovementQueue().freeze(3);
							victim.performGraphic(new Graphic(1680));
						}
						if(barrage && Misc.getRandom(6) <= 3) {
							tarn.performAnimation(new Animation(5617));
							for(Player toAttack : Misc.getCombinedPlayerList((Player)victim)) {
								if(toAttack != null && Locations.goodDistance(tarn.getPosition(), toAttack.getPosition(), 7) && toAttack.getConstitution() > 0) {
									tarn.forceChat("RAAAAAARRRRHHHHHH!!");
									victim.performGraphic(new Graphic(2367));
									new CombatHitTask(tarn.getCombatBuilder(), new CombatContainer(tarn, toAttack, 2, CombatType.MAGIC, false)).handleAttack();
									toAttack.performGraphic(new Graphic(2367));
								}
							}
						}
						tarn.setChargingAttack(false).getCombatBuilder().setAttackTimer(attackDelay(tarn) - 2);
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
