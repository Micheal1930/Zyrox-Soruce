package com.varrock.world.content.combat.strategy.impl;

import com.varrock.GameSettings;
import com.varrock.engine.task.Task;
import com.varrock.engine.task.TaskManager;
import com.varrock.model.*;
import com.varrock.model.projectile.Projectile;
import com.varrock.util.Misc;
import com.varrock.util.RandomUtility;
import com.varrock.world.World;
import com.varrock.world.content.CustomObjects;
import com.varrock.world.content.combat.CombatContainer;
import com.varrock.world.content.combat.CombatType;
import com.varrock.world.content.combat.prayer.CurseHandler;
import com.varrock.world.content.combat.prayer.PrayerHandler;
import com.varrock.world.content.combat.strategy.CombatStrategy;
import com.varrock.world.entity.impl.GameCharacter;
import com.varrock.world.entity.impl.npc.NPC;
import com.varrock.world.entity.impl.player.Player;

public class VorkathCombatScript implements CombatStrategy {
	
	@Override
	public boolean canAttack(GameCharacter entity, GameCharacter victim) {
		return true;
	}
	
	@Override
	public CombatContainer attack(GameCharacter entity, GameCharacter victim) {
		return null;
	}
	
	private static final Animation shoot = new Animation(7952 + GameSettings.OSRS_ANIM_OFFSET);
	private static final Animation up = new Animation(7960 + GameSettings.OSRS_ANIM_OFFSET);
	private static final Animation splash = new Animation(7957 + GameSettings.OSRS_ANIM_OFFSET);

	private static final int projectileOffsetX = 2;
	private static final int projectileOffsetY = 2;

	private static final int blue = 1479 + GameSettings.OSRS_GFX_OFFSET;
	private static final int blueBlast = 1480+ GameSettings.OSRS_GFX_OFFSET;
	private static final int pink = 1471+ GameSettings.OSRS_GFX_OFFSET;
	private static final int pinkBlast = 1473+ GameSettings.OSRS_GFX_OFFSET;
	private static final int green = 1470+ GameSettings.OSRS_GFX_OFFSET;
	private static final int greenBlast = 1472+ GameSettings.OSRS_GFX_OFFSET;
	private static final int red = 1481+ GameSettings.OSRS_GFX_OFFSET;
	private static final int redBlast = 157 + GameSettings.OSRS_GFX_OFFSET;
	
	private static final int fire = 393+ GameSettings.OSRS_GFX_OFFSET;
	private static final int white = 395+ GameSettings.OSRS_GFX_OFFSET;
	
	private static final int ranged = 1477+ GameSettings.OSRS_GFX_OFFSET;
	private static final int rangedSplat = 1478+ GameSettings.OSRS_GFX_OFFSET;
	
	private static final int greenSplat = 1486 + GameSettings.OSRS_GFX_OFFSET;
	
	private static final int redSplat = 1482 + GameSettings.OSRS_GFX_OFFSET;
	
	@Override
	public boolean customContainerAttack(GameCharacter entity, GameCharacter victim) {
		NPC dragon = (NPC) entity;
		if(dragon.isChargingAttack() || dragon.getConstitution() <= 0) {
			dragon.getCombatBuilder().setAttackTimer(4);
			return true;
		}
		Player target = (Player) victim;
		
		int random = Misc.getRandom(4);
		int randomStage = Misc.getRandom(0);

		if(target.stageAmount >= 5) {
			if(randomStage == 0) {
				acidSpecial(target, dragon, victim);
			} else if(randomStage == 1) {
				dragon.setChargingAttack(true);
				dragon.performAnimation(shoot);
				Position savew = new Position(2263 + Misc.getRandom(19), 4056 + Misc.getRandom(4));
				NPC position = NPC.of(5090, savew);
				World.register(target, position);
				TaskManager.submit(new Task(1, dragon, false) {
					int tick = 0;
					
					@Override
					public void execute() {
						switch(tick) {
							case 0:
								new Projectile(dragon.getPosition().transform(projectileOffsetX, projectileOffsetY, 0), target.getPosition(),
										target.getProjectileIndex(), white, 80, 1, 20, 31, 0).sendProjectile();
								break;
							case 2:
								new Projectile(dragon, position, 1484, 80, 1, 20, 31, 0).sendProjectile();
								break;
							case 3:
								target.getMovementQueue().freeze(15);
								target.getPacketSender().sendGlobalGraphic(new Graphic(389), target.getPosition());
								break;
							case 4:
								NPC zombie = NPC.of(28063, savew);
								World.register(target, zombie);
								zombie.getCombatBuilder().attack(target);
								break;
							case 5:
								dragon.setChargingAttack(false).getCombatBuilder().setAttackTimer(5);
								stop();
								break;
						}
						tick++;
					}
				});
			}
			target.stageAmount = 0;
		} else {
			if(random == 0) {
				dragon.setChargingAttack(true);
				dragon.performAnimation(shoot);
				dragon.getCombatBuilder().setContainer(new CombatContainer(dragon, victim, 1, 3, CombatType.DRAGON_FIRE, true));
				TaskManager.submit(new Task(1, dragon, false) {
					int tick = 0;

					@Override
					public void execute() {

						switch(tick) {
							case 0:
								new Projectile(dragon.getPosition().transform(projectileOffsetX, projectileOffsetY, 0), target.getPosition(),
										target.getProjectileIndex(), fire, 44, 3, 31, 31, 0).sendProjectile();
								break;
							case 2:
								dragon.setChargingAttack(false).getCombatBuilder().setAttackTimer(6);
								stop();
								break;
						}
						tick++;
					}
				});
				target.stageAmount++;
				//
			} else if(random == 1) {
				dragon.setChargingAttack(true);
				dragon.performAnimation(shoot);
				dragon.getCombatBuilder().setContainer(new CombatContainer(dragon, victim, 1, 3, CombatType.RANGED, true));
				TaskManager.submit(new Task(1, dragon, true) {
					int tick = 0;

					@Override
					public void execute() {

						switch(tick) {
							case 0:
								new Projectile(dragon.getPosition().transform(projectileOffsetX, projectileOffsetY, 0), target.getPosition(),
										target.getProjectileIndex(), ranged, 44, 3, 25, 31, 0).sendProjectile();
								break;
							case 2:
								// target.getPacketSender().sendGlobalGraphicOsrs(new Graphic(rangedSplat,
								// GraphicHeight.HIGH),
								// victim.getPosition());
								dragon.setChargingAttack(false).getCombatBuilder().setAttackTimer(6);
								stop();
								break;
						}
						tick++;
					}
				});
				target.stageAmount++;

			} else if(random == 2) {
				dragon.setChargingAttack(true);
				dragon.performAnimation(shoot);
				dragon.getCombatBuilder().setContainer(new CombatContainer(dragon, victim, 1, 3, CombatType.MAGIC, true));
				TaskManager.submit(new Task(1, dragon, true) {
					int tick = 0;

					@Override
					public void execute() {

						switch(tick) {
							case 0:
								new Projectile(dragon.getPosition().transform(projectileOffsetX, projectileOffsetY, 0), target.getPosition(),
										target.getProjectileIndex(), pink, 44, 3, 31, 31, 0).sendProjectile();
								break;
							case 2:
								target.sendMessage("@red@Your prayers have been disabled!");
								CurseHandler.deactivateAll(target);
								PrayerHandler.deactivateAll(target);
								target.getPacketSender().sendGlobalGraphic(new Graphic(pinkBlast, GraphicHeight.HIGH), victim.getPosition());
								// victim.performGraphic(new Graphic(pinkBlast, GraphicHeight.HIGH));
								dragon.setChargingAttack(false).getCombatBuilder().setAttackTimer(6);
								stop();
								break;
						}
						tick++;
					}
				});
				target.stageAmount++;

			} else if(random == 3) {
				dragon.setChargingAttack(true);
				dragon.performAnimation(shoot);
				dragon.getCombatBuilder().setContainer(new CombatContainer(dragon, victim, 1, 3, CombatType.MAGIC, true));
				TaskManager.submit(new Task(1, dragon, true) {
					int tick = 0;

					@Override
					public void execute() {

						switch(tick) {
							case 0:
								new Projectile(dragon.getPosition().transform(projectileOffsetX, projectileOffsetY, 0), target.getPosition(),
										target.getProjectileIndex(), green, 44, 3, 31, 31, 0).sendProjectile();
								break;
							case 2:
								target.venomVictim(target, CombatType.MAGIC);
								target.getPacketSender().sendGlobalGraphic(new Graphic(greenBlast, GraphicHeight.HIGH), victim.getPosition());

								// victim.performGraphic(new Graphic(greenBlast, GraphicHeight.HIGH));
								dragon.setChargingAttack(false).getCombatBuilder().setAttackTimer(6);
								stop();
								break;
						}
						tick++;
					}
				});
				target.stageAmount++;

			} else if(random >= 4) {
				dragon.setChargingAttack(true);
				dragon.performAnimation(up);
				Position savew = victim.getPosition();
				NPC position = NPC.of(5090, victim.getPosition());
				World.register(target, position);
				TaskManager.submit(new Task(1, dragon, false) {
					int tick = 0;

					@Override
					public void execute() {

						switch(tick) {
							case 0:
								new Projectile(dragon, position, red, 80, 1, 100, 31, 0).sendProjectile();
								break;
							case 3:
								target.getPacketSender().sendGlobalGraphic(new Graphic(redBlast, GraphicHeight.HIGH), savew);

								if (target.getPosition() == savew)
									target.dealDamage(new Hit(400 + RandomUtility.getRandom(300), Hitmask.RED, CombatIcon.NONE));
								dragon.setChargingAttack(false).getCombatBuilder().setAttackTimer(6);
								stop();
								break;
						}
						tick++;
					}
				});
			} else {
				dragon.setChargingAttack(true);
				dragon.performAnimation(shoot);
				dragon.getCombatBuilder().setContainer(new CombatContainer(dragon, victim, 1, 3, CombatType.MAGIC, true));
				TaskManager.submit(new Task(1, dragon, true) {
					int tick = 0;

					@Override
					public void execute() {

						switch(tick) {
							case 0:
								new Projectile(dragon.getPosition().transform(projectileOffsetX, projectileOffsetY, 0), target.getPosition(),
										target.getProjectileIndex(), blue, 44, 3, 31, 31, 0).sendProjectile();
								break;
							case 2:
								// victim.performGraphic(new Graphic(blueBlast, GraphicHeight.HIGH));
								target.getPacketSender().sendGlobalGraphic(new Graphic(blueBlast, GraphicHeight.HIGH), target.getPosition());
								dragon.setChargingAttack(false).getCombatBuilder().setAttackTimer(6);
								stop();
								break;
						}
						tick++;
					}

				});
				target.stageAmount++;

			}
		}
		return true;
	}

	@Override
	public int attackDelay(GameCharacter entity) {
		return entity.getAttackSpeed();
	}

	@Override
	public int attackDistance(GameCharacter entity) {
		return 50;
	}

	@Override
	public CombatType getCombatType() {
		return CombatType.MIXED;
	}

	public static void acidSpecial(Player target, NPC dragon, GameCharacter victim) {

		dragon.setChargingAttack(true);
		dragon.performAnimation(splash);
		dragon.setAnimation(splash);

		for(int i = 0; i < 16; i++) {
			Position pool = new Position(2263 + Misc.getRandom(19), 4056 + Misc.getRandom(4), victim.getPosition().getZ());
			dragon.getVorkathSpawns().add(NPC.of(5090, pool));
		}
		for(int i = 16; i < 32; i++) {
			Position pool = new Position(2263 + Misc.getRandom(19), 4070 + Misc.getRandom(4), victim.getPosition().getZ());
			dragon.getVorkathSpawns().add(NPC.of(5090, pool));
		}
		for(int i = 32; i < 42; i++) {
			Position pool = new Position(2263 + Misc.getRandom(4), 4061 + Misc.getRandom(7), victim.getPosition().getZ());
			dragon.getVorkathSpawns().add(NPC.of(5090, pool));
		}
		for(int i = 42; i < 52; i++) {
			Position pool = new Position(2278 + Misc.getRandom(4), 4061 + Misc.getRandom(7), victim.getPosition().getZ());
			dragon.getVorkathSpawns().add(NPC.of(5090, pool));
		}

		TaskManager.submit(new Task(1, dragon, false) {
			int tick = 0;
			Position hitPosition = null;

			@Override
			public void execute() {

				dragon.setEntityInteraction(target);
				switch(tick) {
					case 0:
						for(int i = 0; i < 52; i++) {
							new Projectile(dragon.getPosition().transform(projectileOffsetX, projectileOffsetY, 0), dragon.getVorkathSpawns().get(i).getPosition(),
									dragon.getVorkathSpawns().get(i).getProjectileIndex(), greenSplat, 80, 1, 100, 31, 0).sendProjectile();
						}
						break;
					case 2:
						// for (int i = 0; i < 52; i++) {
						//    World.deregister(dragon.getVorkathSpawns().get(i));
						// }
						break;
					case 3:
						for(int i = 0; i < 52; i++) {
							CustomObjects.acidPool(new GameObject(132000, dragon.getVorkathSpawns().get(i).getPosition(), 11, Misc.getRandom(3)),
									target, 1, 25);
						}
						break;
				}
				for(int i = 4; i <= 28; i += 1) {
					if(tick == i) {
						new Projectile(dragon.getPosition().transform(projectileOffsetX, projectileOffsetY, 0), target.getPosition(),
								target.getProjectileIndex(), redSplat, 44, 3, 20, 1, 0).sendProjectile();
						target.getPacketSender().sendGlobalGraphic(new Graphic(131 + GameSettings.OSRS_GFX_OFFSET, GraphicHeight.HIGH), target.getPosition());
						hitPosition = target.getPosition();
					}
					if(tick == i + 1) {
						if(target.getPosition() == hitPosition)
							target.dealDamage(new Hit(150 + RandomUtility.getRandom(200), Hitmask.RED, CombatIcon.NONE));
					}
				}
				
				if(target.getLocation() != Locations.Location.VORKATH || target.getConstitution() == 0 || target.isDying()) {
					dragon.getVorkathSpawns().clear();
					stop();
				}
				
				for(int i = 0; i < dragon.getVorkathSpawns().size(); i++) {
					if(victim.getPosition().equals(dragon.getVorkathSpawns().get(i).getPosition())) {
						target.dealDamage(new Hit(50 + Misc.getRandom(100), Hitmask.RED, CombatIcon.NONE));
					}
				}
				
				if(tick == 26) {
					dragon.getCombatBuilder().attack(target);
					dragon.setChargingAttack(false).getCombatBuilder().setAttackTimer(6);
					dragon.getVorkathSpawns().clear();
					stop();
				}
				
				tick++;
				
			}
		});
	}
	
}
