package com.varrock.world.content.combat;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.varrock.engine.task.Task;
import com.varrock.engine.task.TaskManager;
import com.varrock.model.Animation;
import com.varrock.model.Graphic;
import com.varrock.model.GraphicHeight;
import com.varrock.model.Locations.Location;
import com.varrock.model.container.impl.Equipment;
import com.varrock.model.definitions.WeaponAnimations;
import com.varrock.util.Misc;
import com.varrock.world.content.Achievements;
import com.varrock.world.content.Sounds;
import com.varrock.world.content.Achievements.AchievementData;
import com.varrock.world.content.combat.strategy.impl.Nex;
import com.varrock.world.content.greatolm.Phases;
import com.varrock.world.entity.impl.GameCharacter;
import com.varrock.world.entity.impl.npc.NPC;
import com.varrock.world.entity.impl.npc.NPCMovementCoordinator.CoordinateState;
import com.varrock.world.entity.impl.player.Player;

public class HitQueue {
	
	/*
	 * @author AuguryPS @ Rune-server.org
	 * Combat Hit Queueing
	 */

	public final CopyOnWriteArrayList<CombatHit> combat_hits = new CopyOnWriteArrayList<CombatHit>();

	public void append(CombatHit c) {
		if(c == null) {
			return;
		}
		if(c.initialRun()) {
			c.handleAttack();
		} else {
			combat_hits.add(c);
		}
	}

	public void process() {
		for(CombatHit c : combat_hits) {
			if(c == null) {
				combat_hits.remove(c);
				continue;
			}
			if(c.delay > 0) {
				c.delay--;
			} else {
				c.handleAttack();
				combat_hits.remove(c);
			}
		}
	}

	public static class CombatHit {

		/** The attacker instance. */
		private GameCharacter attacker;

		/** The victim instance. */
		private GameCharacter victim;

		/** The attacker's combat builder attached to this task. */
		private CombatBuilder builder;

		/** The attacker's combat container that will be used. */
		private CombatContainer container;

		/** The total damage dealt during this hit. */
		private int damage;

		private int initialDelay;
		private int delay;


		public CombatHit(CombatBuilder builder, CombatContainer container) {
			this.builder = builder;
			this.container = container;
			this.attacker = builder.getCharacter();
			this.victim = builder.getVictim();
		}

		public void handleAttack() {
			if (attacker.getConstitution() <= 0 || !attacker.isRegistered()) {
				return;
			}
			if(victim == null) {
				return;
			}
			// Do any hit modifications to the container here first.

			/*if(attacker.isPlayer() && victim.isNpc()) { //Instanced Kraken
				NPC npc = (NPC)victim;
				if(Kraken.isWhirpool(npc)) {
					Kraken.attackPool(((Player)attacker), npc);
					return;
				}
			}*/
			
			if(container.getModifiedDamage() > 0) {
				container.allHits(context -> {
					context.getHit().setDamage(container.getModifiedDamage());
					context.setAccurate(true);
				});
			}

			// Now we send the hitsplats if needed! We can't send the hitsplats
			// there are none to send, or if we're using magic and it splashed.
			if (container.getHits().length != 0 && container.getCombatType() != CombatType.MAGIC || container.isAccurate()) {

				/** PRAYERS **/
				CombatFactory.applyPrayerProtection(container, builder);

				this.damage = container.getDamage();
				victim.getCombatBuilder().addDamage(attacker, damage);
				container.dealDamage();
				
				/** MISC **/
				if(attacker.isPlayer()) {
					Player p = (Player)attacker;
					if(damage > 0) {
						if(p.getLocation() == Location.PEST_CONTROL_GAME) {
							p.getMinigameAttributes().getPestControlAttributes().incrementDamageDealt(damage);
						} else if(p.getLocation() == Location.DUNGEONEERING) {
							p.getMinigameAttributes().getDungeoneeringAttributes().incrementDamageDealt(damage);
						}
						/** ACHIEVEMENTS **/
						if(container.getCombatType() == CombatType.MELEE) {
							Achievements.doProgress(p, AchievementData.DEAL_EASY_DAMAGE_USING_MELEE, damage);
							Achievements.doProgress(p, AchievementData.DEAL_MEDIUM_DAMAGE_USING_MELEE, damage);
							Achievements.doProgress(p, AchievementData.DEAL_HARD_DAMAGE_USING_MELEE, damage);
						} else if(container.getCombatType() == CombatType.RANGED) {
							Achievements.doProgress(p, AchievementData.DEAL_EASY_DAMAGE_USING_RANGED, damage);
							Achievements.doProgress(p, AchievementData.DEAL_MEDIUM_DAMAGE_USING_RANGED, damage);
							Achievements.doProgress(p, AchievementData.DEAL_HARD_DAMAGE_USING_RANGED, damage);
						} else if(container.getCombatType() == CombatType.MAGIC) {
							Achievements.doProgress(p, AchievementData.DEAL_EASY_DAMAGE_USING_MAGIC, damage);
							Achievements.doProgress(p, AchievementData.DEAL_MEDIUM_DAMAGE_USING_MAGIC, damage);
							Achievements.doProgress(p, AchievementData.DEAL_HARD_DAMAGE_USING_MAGIC, damage);
						}
						if(victim.isPlayer()) {
							Achievements.finishAchievement(p, AchievementData.FIGHT_ANOTHER_PLAYER);
						} else {
							if (p.getMinigameAttributes().getRaidsAttributes().getParty() != null) {
								if (((NPC) victim).getId() == Phases.OLM_HEAD) {
									if (p.getMinigameAttributes().getRaidsAttributes().getParty().getCurrentPhase() < 3) {
										p.sendMessage("Your hits don't seem to have any effect on the Great olm.");
										return;
									}
								}

								if (((NPC) victim).getId() == Phases.OLM_LEFT_HAND
										&& p.getMinigameAttributes().getRaidsAttributes().getParty().isLeftHandProtected()) {
									if (p.getMinigameAttributes().getRaidsAttributes().getParty().getCurrentPhase() < 3) {
										p.sendMessage(
												"The Olm is currently protecting its left claw! You cannot see a way to attack it");
										return;
									}
								}
							}
						}
					}
				} else {
					if(victim.isPlayer() && container.getCombatType() == CombatType.DRAGON_FIRE) {
						Player p = (Player)victim;
						if(Misc.getRandom(20) <= 15 && p.getEquipment().getItems()[Equipment.SHIELD_SLOT].getId() == 11283) {
							p.setPositionToFace(attacker.getPosition().copy());
							CombatFactory.chargeDragonFireShield(p);
						}
						if(damage >= 160) {
							((Player)victim).getPacketSender().sendMessage("You are badly burnt by the dragon's fire!");
						}
					}
				}
			}


			// Give experience based on the hits.
			CombatFactory.giveExperience(builder, container, damage, victim);

			if (!container.isAccurate()) {
				if (container.getCombatType() == CombatType.MAGIC && attacker.getCurrentlyCasting() != null) {
					victim.performGraphic(new Graphic(85, GraphicHeight.MIDDLE));
					attacker.getCurrentlyCasting().finishCast(attacker, victim, false, 0);
					attacker.setCurrentlyCasting(null);
				}
			} else if (container.isAccurate()) {

				CombatFactory.handleArmorEffects(attacker, victim, damage, container.getCombatType());
				CombatFactory.handlePrayerEffects(attacker, victim, damage, container.getCombatType());
				CombatFactory.handleSpellEffects(attacker, victim, damage, container.getCombatType());

				attacker.poisonVictim(victim, container.getCombatType());
				
				int random = Misc.getRandom(3);
				if (random == 2)
					attacker.venomVictim(victim, container.getCombatType());

				// Finish the magic spell with the correct end graphic.
				if (container.getCombatType() == CombatType.MAGIC && attacker.getCurrentlyCasting() != null) {
					attacker.getCurrentlyCasting().endGraphic().ifPresent(victim::performGraphic);
					attacker.getCurrentlyCasting().finishCast(attacker, victim, true, damage);
					attacker.setCurrentlyCasting(null);
				}
			}

			// Send the defensive animations.
			if(victim.getCombatBuilder().getAttackTimer() <= 2 && attacker.sendsBlockAnimation() && victim.sendsBlockAnimation()) {
				if (victim.isPlayer()) {
					Player plr = (Player) victim;
					if(plr.getWeapon().getSpeed() <= 4) {

	        		} else {
	                    victim.performAnimation(new Animation(WeaponAnimations.getBlockAnimation(((Player) victim))));

	        		}
					if(((Player)victim).getInterfaceId() > 0)
						((Player)victim).getPacketSender().sendInterfaceRemoval();
				} else if (victim.isNpc()) {
					if(!(((NPC)victim).getId() >= 6142 && ((NPC)victim).getId() <= 6145))
						victim.performAnimation(new Animation(((NPC) victim).getDefinition().getDefenceAnimation()));
				}
			}

			// Fire the container's dynamic hit method.
			container.onHit(damage, container.isAccurate());
			
			// And finally auto-retaliate if needed.
			if(!victim.getCombatBuilder().isAttacking() || victim.getCombatBuilder().isCooldown() || victim.isNpc() && ((NPC)victim).findNewTarget()) {
				if(shouldRetaliate()) {
					if(initialDelay == 0) {
						TaskManager.submit(new Task(1, victim, false) {
							@Override
							protected void execute() {
								if(shouldRetaliate()) {
									retaliate();
								}
								stop();
							}
						});
					} else {
						retaliate();
					}
				}
			}

			if(attacker.isNpc() && victim.isPlayer()) {
				NPC npc = (NPC)attacker;
				Player p = (Player)victim;
				if(npc.switchesVictim() && Misc.getRandom(6) <= 1) {
					if(npc.getDefinition().isAggressive()) {
						npc.setFindNewTarget(true);
					} else {
						if(p.getLocalPlayers().size() >= 1) {
							List<Player> list = p.getLocalPlayers();
							Player c = list.get(Misc.getRandom(list.size() - 1));
							npc.getCombatBuilder().attack(c);
						}
					}
				}

				Sounds.sendSound(p, Sounds.getPlayerBlockSounds(p.getEquipment().get(Equipment.WEAPON_SLOT).getId()));
				/** CUSTOM ON DAMAGE STUFF **/
				if(victim.isPlayer() && npc.getId() == 13447) {
					Player v = (Player) victim;
	            	Nex.getNex(v).dealtDamage(v, damage);
				}

			} else if(attacker.isPlayer()) {
				Player player = (Player)attacker;

				//player.getPacketSender().sendCombatBoxData(victim); //HP Overlay

				/** SKULLS **/
				if(player.getLocation() == Location.WILDERNESS && victim.isPlayer()) {
					boolean didRetaliate = player.getCombatBuilder().didAutoRetaliate();
					if(!didRetaliate) {
						boolean soloRetaliate = !player.getCombatBuilder().isBeingAttacked();
						boolean multiRetaliate = player.getCombatBuilder().isBeingAttacked() && player.getCombatBuilder().getLastAttacker() != victim && Location.inMulti(player);
						if (soloRetaliate || multiRetaliate) {
							//CombatFactory.skullPlayer(player);
							//player.getPacketSender().sendMessage("gothere2");
						}
					}
				}

				player.setLastCombatType(container.getCombatType());

				Sounds.sendSound(player, Sounds.getPlayerAttackSound(player));

				/** CUSTOM ON DAMAGE STUFF **/
				if(victim.isNpc()) {
					if(((NPC)victim).getId() == 13447) {
						Nex.getNex(player).takeDamage(player, damage);
					}
				} else {
					Sounds.sendSound((Player)victim, Sounds.getPlayerBlockSounds(((Player)victim).getEquipment().get(Equipment.WEAPON_SLOT).getId()));
				}
			}
		}

		public boolean shouldRetaliate() {
			if(victim.isPlayer()) {
				if(attacker.isNpc()) {
					if(!((NPC)attacker).getDefinition().isAttackable()) {
						return false;
					}
				}
				return victim.isPlayer() && ((Player)victim).isAutoRetaliate() && !victim.getMovementQueue().isMoving() && ((Player)victim).getWalkToTask() == null;
			} else if(!(attacker.isNpc() && ((NPC)attacker).isFamiliar())) {
				NPC npc = (NPC)victim;
				return npc.getMovementCoordinator().getCoordinateState() == CoordinateState.HOME && npc.getLocation() != Location.PEST_CONTROL_GAME;
			}
			return false;
		}

		public void retaliate() {
			if (victim.isPlayer()) {
				victim.getCombatBuilder().setDidAutoRetaliate(true);
				victim.getCombatBuilder().attack(attacker);
			} else if(victim.isNpc()) {
				NPC npc = (NPC)victim;
				npc.getCombatBuilder().attack(attacker);
				npc.setFindNewTarget(false);
			}
		}

		private boolean initialRun() {
			return this.delay == 0;
		}
	}
}
