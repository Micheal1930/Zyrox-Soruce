package com.varrock.world.content.alchemical_hydra;

import java.util.ArrayList;
import java.util.List;

import com.varrock.GameSettings;
import com.varrock.engine.task.Task;
import com.varrock.engine.task.TaskManager;
import com.varrock.model.Animation;
import com.varrock.model.Graphic;
import com.varrock.model.Hit;
import com.varrock.model.Position;
import com.varrock.model.projectile.Projectile;
import com.varrock.util.Misc;
import com.varrock.util.area.Polygon;
import com.varrock.util.area.Rectangle;
import com.varrock.util.area.Shape;
import com.varrock.world.content.combat.CombatFactory;
import com.varrock.world.content.combat.effect.CombatPoisonEffect.PoisonType;
import com.varrock.world.content.combat.prayer.CurseHandler;
import com.varrock.world.content.combat.prayer.PrayerHandler;
import com.varrock.world.entity.impl.GameCharacter;
import com.varrock.world.entity.impl.player.Player;

public enum HydraAttack {
	
	RANGED {
		@Override
		public void attack(AlchemicalHydra attacker, GameCharacter victim) {
			attacker.performAnimation(attacker.getAttackAnimation());
			int proj = 1663;
			int gfxDelay;
			if (Position.isWithinDistance(attacker, victim, 1)) {
				gfxDelay = 80;
			} else if (Position.isWithinDistance(attacker, victim, 5)) {
				gfxDelay = 100;
			} else if (Position.isWithinDistance(attacker, victim, 8)) {
				gfxDelay = 120;
			} else {
				gfxDelay = 140;
			}
			Projectile proj1 = new Projectile(attacker.getCentrePosition().transform(1, 0, 0), victim.getPosition(), victim.getProjectileIndex(), proj + GameSettings.OSRS_GFX_OFFSET, 44, 3, 70, 20, 0);
			Projectile proj2 = new Projectile(attacker.getCentrePosition().transform(0, 0, 0), victim.getPosition(), victim.getProjectileIndex(), proj + GameSettings.OSRS_GFX_OFFSET, 44, 3, 70, 20, 0);
			proj1.sendProjectile();
			proj2.sendProjectile();
			int hitDelay = (gfxDelay / 20) - 1;
			TaskManager.submit(new Task(hitDelay) {
				@Override
				public void execute() {
					int max = (int) (attacker.getDefinition().getMaxHit() );
					int prayer = PrayerHandler.PROTECT_FROM_MISSILES;
					if (PrayerHandler.isActivated((Player) victim, prayer)) {
						max *= 0.4;
					}
					Hit hit = new Hit(Misc.random(0, max));
					if (attacker.hasShield() || attacker.isEnraged()) {
						hit.setDamage((int) (hit.getDamage() * 1.5));
					}
					victim.dealDamage(hit);
					stop();
				}
			});
		}
	},
	
	MAGIC() {
		@Override
		public void attack(AlchemicalHydra attacker, GameCharacter victim) {
			attacker.performAnimation(attacker.getAttackAnimation());
			int proj = 1662;
			int gfxDelay;
			if (Position.isWithinDistance(attacker, victim, 1)) {
				gfxDelay = 80;
			} else if (Position.isWithinDistance(attacker, victim, 5)) {
				gfxDelay = 100;
			} else if (Position.isWithinDistance(attacker, victim, 8)) {
				gfxDelay = 120;
			} else {
				gfxDelay = 140;
			}
			Projectile projectile = new Projectile(attacker.getCentrePosition(), victim.getPosition(), victim.getProjectileIndex(), proj + GameSettings.OSRS_GFX_OFFSET, 44, 3, 70, 20, 0);
			projectile.sendProjectile();
			int hitDelay = (gfxDelay / 20) - 1;
			TaskManager.submit(new Task(hitDelay) {
				@Override
				public void execute() {
					int max = (int) (attacker.getDefinition().getMaxHit() );
					int prayer = PrayerHandler.PROTECT_FROM_MAGIC;
					if (PrayerHandler.isActivated((Player) victim, prayer)) {
						max *= 0.4;
					}
					Hit hit = new Hit(Misc.random(0, max));
					if (attacker.hasShield() || attacker.isEnraged()) {
						hit.setDamage((int) (hit.getDamage() * 1.5));
					}
					victim.dealDamage(hit);
					stop();
				}
			});
		}
	},
	
	POISON(HydraState.NORMAL) {
		
		@Override
		public void attack(AlchemicalHydra hydra, GameCharacter victim) {
			hydra.performAnimation(hydra.getAttackAnimation());
			int count = Misc.random(4, 6);
			Position[] tiles = new Position[count];
			int added = 0;
			for (;;) {
				int diffX = Misc.random(7), diffY = Misc.random(7);
				if (Misc.random(5) > 10) {
					diffX *= -1;
				}
				if (Misc.random(5) > 10) {
					diffY *= -1;
				}
				Position to = victim.getPosition().transform(diffX, diffY, 0);
				tiles[added] = to;
				added++;
				if (added == count) break;
			}
			added = 0;
			for (Position target : tiles) {
				int delay = 60;
				if (hydra.getCentrePosition().getDistance(target) == 1) {
					delay = 60;
				} else if (hydra.getCentrePosition().getDistance(target) <= 5) {
					delay = 80;
				} else if (hydra.getCentrePosition().getDistance(target) <= 8) {
					delay = 100;
				} else {
					delay = 120;
				}
				Projectile projectile = new Projectile(hydra.getCentrePosition(), target, -1, 1644 + GameSettings.OSRS_GFX_OFFSET, 44, 3, 70, 20, 0);
				projectile.sendProjectile();
				TaskManager.submit(new Task((delay / 20) - 3) {
					@Override
					public void execute() {
						TaskManager.submit(new Task(1) {
							int cycle = 0;
							@Override
							public void execute() {
								for (Player player : hydra.getPlayers()) {
									if (player.getPosition().equals(target)) {
										CombatFactory.poisonEntity(player, PoisonType.SUPER);
									}
								}
								switch(++cycle) {
								case 1:
									for (Player player : hydra.getPlayers()) {
										player.getPacketSender().sendGraphic(new Graphic(1645 + GameSettings.OSRS_GFX_OFFSET), target);
									}
									break;
								case 2:
									for (Player player : hydra.getPlayers()) {
										player.getPacketSender().sendGraphic(new Graphic(1654 + Misc.random(7) + GameSettings.OSRS_GFX_OFFSET), target);
									}
									break;
								case 10:
									stop();
									break;
								}
							}
						});
						stop();
					}
				});
			}
		}
	},
	

	LIGHTNING(HydraState.BLUE) {

		@Override
		public void attack(AlchemicalHydra hydra, GameCharacter victim) {
			hydra.performAnimation(hydra.getAttackAnimation());
			Position target = hydra.getBase().transform(39, 14, 0);
			int delay = 60;
			if (hydra.getCentrePosition().getDistance(target) == 1) {
				delay = 60;
			} else if (hydra.getCentrePosition().getDistance(target) <= 5) {
				delay = 80;
			} else if (hydra.getCentrePosition().getDistance(target) <= 8) {
				delay = 100;
			} else {
				delay = 120;
			}
			Projectile projectile = new Projectile(hydra.getPosition(), target, -1, 1664 + GameSettings.OSRS_GFX_OFFSET, 44, 3, 70, 20, 0);
			projectile.sendProjectile();
			TaskManager.submit(new Task((delay / 20) - 3) {
				@Override
				public void execute() {
					TaskManager.submit(new Task(1) {
						int cycle = 0;
						@Override
						public void execute() {
						switch(++cycle) {
							case 5:
								Position[] to = new Position[] { hydra.getBase().transform(44,15,0), hydra.getBase().transform(34, 15, 0), hydra.getBase().transform(44, 24, 0), hydra.getBase().transform(33, 24, 0) };
								for (Position t : to) {
									Projectile projectile = new Projectile(target, t, -1, 1665 + GameSettings.OSRS_GFX_OFFSET, 44, 3, 70, 20, 0);
									projectile.sendProjectile();
									int delay = 60;
									if (hydra.getCentrePosition().getDistance(t) == 1) {
										delay = 60;
									} else if (hydra.getCentrePosition().getDistance(t) <= 5) {
										delay = 80;
									} else if (hydra.getCentrePosition().getDistance(t) <= 8) {
										delay = 100;
									} else {
										delay = 120;
									}
									TaskManager.submit(new Task((delay / 20) - 3) {
										@Override
										public void execute() {
											throttleLightning(t);
											stop();
										}

										private void throttleLightning(Position t) {
											TaskManager.submit(new Task(1) {
												int y = 0,x = 0;
												@Override
												public void execute() {
													Position l = t.transform(x, y, 0);
													for (Player playerTarget : hydra.getPlayers()) {
														playerTarget.getPacketSender().sendGraphic(new Graphic(1666 + GameSettings.OSRS_GFX_OFFSET), l);
														if (playerTarget.getPosition().equals(l)) {
															playerTarget.dealDamage(new Hit(Misc.random(15, 30)));
															PrayerHandler.deactivateAll(playerTarget);
															CurseHandler.deactivateAll(playerTarget);
															playerTarget.sendMessage("<col=ff1a1a>You've been electrocuted to the spot!");
															playerTarget.setDragonScimInjury(System.currentTimeMillis());
															playerTarget.setAttribute("stunned", true);
															playerTarget.setAttribute("protection_prayers_disabled", true);
//															for (int i = 0; i < 26; i++) {
//																Prayers.deActivatePrayer(mob, i);
//															}
															playerTarget.sendMessage("You've been injured and can't use protection prayers!");
															TaskManager.submit(new Task(5) {
																int count = 0;
																@Override
																public void execute() {
																	if (count == 3) {
																		playerTarget.removeAttribute("stunned");
																		playerTarget.removeAttribute("protection_prayers_disabled");
																		stop();
																	}
																	count++;
																}
															});
														}
													}
													if (y == (36)) {
														stop();
													}
													y+= 1;
													mod: for (Player player : hydra.getPlayers()) {
														if (player.getPosition().getDistance(l) < 64) {
															if (player.getPosition().getX() < l.getX()) {
																x =- 1;
															} else if (player.getPosition().getX() > l.getX()) {
																x += 1;
															}
															break mod;
														}
													}
												
												}
											});
										}
									});
								}
								stop();
								break;
							}
							for (Player player : hydra.getPlayers()) {
								player.getPacketSender().sendGraphic(new Graphic(1664 + GameSettings.OSRS_GFX_OFFSET), target);
							}
						}
					});
					stop();
				}
			});
			
			
		}
	},
	
	
	FIREWALL(HydraState.RED) {
		
		@Override
		public boolean canStart(AlchemicalHydra hydra, GameCharacter victim) {
			if (System.currentTimeMillis() - hydra.getWallDelta() < 60000 || hydra.getTransformTicks() > 0) {
				return false;
			}
			Position centre = hydra.getBase().transform(36, 16, 0);
			if (!hydra.getLocation().equals(centre)) {
				hydra.setFireWallTicks(10);
				hydra.setResetMovementQueue(true);
				hydra.getCombatBuilder().reset(true);
//				hydra.forceChat("NOT CENTRERED " + hydra.getLocation() + "/" + centre + " -> "+ hydra.getLocation().equals(centre));
				hydra.moveTo(centre);
				hydra.getCombatBuilder().setAttackTimer(1);
//				hydra.setAttribute("walking_to_centre", true);
//				hydra.setSpecialCount(1);
//			} else {
				return true;
			}
			return true;
		}

		@Override
		public void attack(AlchemicalHydra hydra, GameCharacter victim) {
			hydra.getCombatBuilder().reset(true);
			Shape[] sections = new Shape[8];
			sections[0] = new Rectangle(hydra.getBase().transform(37, 18, 0), hydra.getBase().transform(27, 8, 0));
			sections[1] = new Rectangle(hydra.getBase().transform(36, 30, 0), hydra.getBase().transform(28, 21, 0));
			sections[2] = new Rectangle(hydra.getBase().transform(48, 29, 0), hydra.getBase().transform(40, 21, 0));
			sections[3] = new Rectangle(hydra.getBase().transform(50, 18, 0), hydra.getBase().transform(40, 8, 0));
			sections[4] = new Polygon(hydra.getBase().transform(28, 29, 0), hydra.getBase().transform(36,21,0), hydra.getBase().transform(36, 18, 0), hydra.getBase().transform(28, 12,0));
			sections[5] = new Polygon(hydra.getBase().transform(39, 30, 0), hydra.getBase().transform(37, 22, 0), hydra.getBase().transform(40, 22, 0), hydra.getBase().transform(47,30,0));
			sections[6] = new Polygon(hydra.getBase().transform(49, 29, 0), hydra.getBase().transform(41,21,0), hydra.getBase().transform(41,18,0), hydra.getBase().transform(49,10,0));
			sections[7] = new Polygon(hydra.getBase().transform(49, 9, 0), hydra.getBase().transform(40,17,0), hydra.getBase().transform(37, 17, 0), hydra.getBase().transform(28,9,0));
			
			int ordinal = -1;
			for (int i = 0; i < sections.length; i++) {
				if (sections[i].inside(victim.getPosition())) {
					ordinal = i;
					break;
				}
			}
			if (ordinal == -1) return;
			hydra.setResetMovementQueue(true);
			hydra.getCombatBuilder().reset(true);
			HydraFireWall wall = HydraFireWall.values()[ordinal];
			hydra.setFireWallTicks(10);
			hydra.setPositionToFace(wall.getFirstDirection(hydra));
			hydra.performAnimation(new Animation(8248 + GameSettings.OSRS_ANIM_OFFSET));
			wall.renderWall(hydra, victim);
			hydra.setWallDelta(System.currentTimeMillis());
			hydra.getCombatBuilder().setAttackTimer(10);
		}

	},
	
	SPIT(HydraState.JAD) {
		
		@Override
		public void attack(AlchemicalHydra hydra, GameCharacter victim) {
			hydra.performAnimation(hydra.getAttackAnimation());
			int count = Misc.random(1, 3);
			Position[] tiles = new Position[count];
			Player t = hydra.getPlayers().get(0);
			int added = 0;
			for (;;) {
				int diffX = Misc.random(7), diffY = Misc.random(7);
				if (Misc.random(5) > 10) {
					diffX *= -1;
				}
				if (Misc.random(5) > 10) {
					diffY *= -1;
				}
				Position to = victim.getPosition().transform(diffX, diffY, 0);
				tiles[added] = to;
				added++;
				if (added == count) break;
			}
			added = 0;
			tiles[count - 1] = t.getPosition();
			for (Position target : tiles) {
				int delay = 60;
				if (hydra.getCentrePosition().getDistance(target) == 1) {
					delay = 60;
				} else if (hydra.getCentrePosition().getDistance(target) <= 5) {
					delay = 80;
				} else if (hydra.getCentrePosition().getDistance(target) <= 8) {
					delay = 100;
				} else {
					delay = 120;
				}
				Projectile projectile = new Projectile(hydra.getCentrePosition(), target, -1, 1644 + GameSettings.OSRS_GFX_OFFSET, 44, 3, 70, 20, 0);
				projectile.sendProjectile();
				TaskManager.submit(new Task((delay / 20) - 3) {
					@Override
					public void execute() {
						TaskManager.submit(new Task(1) {
							int cycle = 0;
							@Override
							public void execute() {
								for (Player player : hydra.getPlayers()) {
									if (player.getPosition().equals(target)) {
										CombatFactory.poisonEntity(player, PoisonType.SUPER);
									}
								}
								switch(++cycle) {
								case 1:
									for (Player player : hydra.getPlayers()) {
										player.getPacketSender().sendGraphic(new Graphic(1645 + GameSettings.OSRS_GFX_OFFSET), target);
									}
									break;
								case 2:
									for (Player player : hydra.getPlayers()) {
										player.getPacketSender().sendGraphic(new Graphic(1654 + Misc.random(7) + GameSettings.OSRS_GFX_OFFSET), target);
									}
									break;
								case 10:
									stop();
									break;
								}
							}
						});
						stop();
					}
				});
			}
		}
	}
	
	;
	private HydraState state;
	
	private HydraAttack() {
		// TODO Auto-generated constructor stub
	}
	
	private HydraAttack(HydraState state) {
		this.state = state;
	}

	public void attack(AlchemicalHydra hydra, GameCharacter victim) {
		
	}
	
	public boolean canStart(AlchemicalHydra hydra, GameCharacter victim) {
		return true;
	}

	/**
	 * @return the state
	 */
	public HydraState getState() {
		return state;
	}

	public void specialAttack(AlchemicalHydra hydra, GameCharacter victim) {

		List<HydraAttack> possibleAttacks = new ArrayList<>();

		for (HydraAttack attack : values()) {
			if (attack.ordinal() <= 1) {
				continue;
			}
			if (attack.state == hydra.getState()) {
				possibleAttacks.add(attack);
			}
		}

		HydraAttack attack = possibleAttacks.get(possibleAttacks.size() <= 1 ? 0 : Misc.random(possibleAttacks.size()));

		if (attack.canStart(hydra, victim)) {
			attack.attack(hydra, victim);
		}
	}

}
