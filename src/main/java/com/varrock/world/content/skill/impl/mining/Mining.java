package com.varrock.world.content.skill.impl.mining;

import com.varrock.engine.task.Task;
import com.varrock.engine.task.TaskManager;
import com.varrock.model.Animation;
import com.varrock.model.GameObject;
import com.varrock.model.Locations;
import com.varrock.model.Skill;
import com.varrock.model.Locations.Location;
import com.varrock.model.container.impl.Equipment;
import com.varrock.model.item.Items;
import com.varrock.util.Misc;
import com.varrock.world.World;
import com.varrock.world.clip.region.RegionClipping;
import com.varrock.world.content.Achievements;
import com.varrock.world.content.CustomObjects;
import com.varrock.world.content.ShootingStar;
import com.varrock.world.content.Sounds;
import com.varrock.world.content.Achievements.AchievementData;
import com.varrock.world.content.Sounds.Sound;
import com.varrock.world.content.skill.impl.mining.MiningData.Ores;
import com.varrock.world.entity.impl.player.Player;

public class Mining {

	public static void startMining(final Player player, final GameObject oreObject) {
		player.getSkillManager().stopSkilling();
		player.getPacketSender().sendInterfaceRemoval();
		if(!Locations.goodDistance(player.getPosition().copy(), oreObject.getPosition(), 1) && oreObject.getId() != 24444  && oreObject.getId() != 24445 && oreObject.getId() != 38660)
			return;
		if(player.busy() || player.getCombatBuilder().isBeingAttacked() || player.getCombatBuilder().isAttacking()) {
			player.getPacketSender().sendMessage("You cannot do that right now.");
			return;
		}
		if(player.getInventory().getFreeSlots() == 0) {
			player.getPacketSender().sendMessage("You do not have any free inventory space left.");
			return;
		}
		player.setInteractingObject(oreObject);
		player.setPositionToFace(oreObject.getPosition());
		
		final RegionClipping region = RegionClipping.forPosition(oreObject.getPosition());
		final Ores o = MiningData.forRock(oreObject.getId());
		final boolean giveGem = o != Ores.RUNE_ESSENCE && o != Ores.PURE_ESSENCE;
		final int reqCycle = o == Ores.RUNITE ? 6 + Misc.getRandom(2) : Misc.getRandom(o.getTicks() - 1);
		
		if (o != null) {
			final int pickaxe = MiningData.getPickaxe(player);
			final int miningLevel = player.getSkillManager().getCurrentLevel(Skill.MINING);
			if (pickaxe > 0) {
				if (miningLevel >= o.getLevelReq()) {
					final MiningData.Pickaxe p = MiningData.forPick(pickaxe);
					if (miningLevel >= p.getReq()) {
						player.performAnimation(new Animation(p.getAnim()));
						final int delay = o.getTicks() - MiningData.getReducedTimer(player, p);
						if(Misc.getRandom(15000) == 3) {
							player.getInventory().add(13321, 1, "Mining");
							World.sendMessage("<col=6666FF> <img=10> [ Skilling Pets ]:</col> "+player.getUsername()+" has received the Rock Golem pet!");
							player.getPacketSender().sendMessage("@red@You have received a skilling pet!");
							player.getPacketSender().sendMessage("@red@Your account has been saved!");
							player.save();
						}
						player.setCurrentTask(new Task(delay >= 2 ? delay : 1, player, false) {
							int cycle = 0;
							@Override
							public void execute() {
								if(player.getInteractingObject() == null || player.getInteractingObject().getId() != oreObject.getId()) {
									player.getSkillManager().stopSkilling();
									player.performAnimation(new Animation(65535));
									stop();
									return;
								}
								if(player.getInventory().getFreeSlots() == 0) {
									player.performAnimation(new Animation(65535));
									stop();
									player.getPacketSender().sendMessage("You do not have any free inventory space left.");
									return;
								}
								if (cycle != reqCycle) {
									cycle++;
									player.performAnimation(new Animation(p.getAnim()));
								}
								if(giveGem) {
									boolean onyx = (o == Ores.RUNITE || o == Ores.CRASHED_STAR) && Misc.getRandom(o == Ores.CRASHED_STAR ? 20000 : 5000) == 1;
									if(onyx || Misc.getRandom(o == Ores.CRASHED_STAR ? 35 : 50) == 15) {
										int gemId = onyx ? 6571 : MiningData.RANDOM_GEMS[(int)(MiningData.RANDOM_GEMS.length * Math.random())];
										player.getInventory().add(gemId, 1, "Mining gem");
										player.getPacketSender().sendMessage("You've found a gem!");
										if(gemId == 6571) {
											String s = o == Ores.RUNITE ? "Runite ore" : "Crashed star";
											World.sendMessage("<img=10><col=009966> "+player.getUsername()+" has just received an Uncut Onyx from mining a "+s+"!");
										}
									}
								}
								if (cycle == reqCycle) {
									if(o == Ores.IRON) {
										Achievements.finishAchievement(player, AchievementData.MINE_SOME_IRON);
									} else if(o == Ores.RUNITE) {
										Achievements.doProgress(player, AchievementData.MINE_25_RUNITE_ORES);
										Achievements.doProgress(player, AchievementData.MINE_2000_RUNITE_ORES);
									}
									if(o.getItemId() != -1) {
										if(Location.inResource(player)) {
											player.getInventory().add(o.getItemId() + 1, 1, "Mining");
											if(player.getEquipment().getItems()[Equipment.WEAPON_SLOT].getId() == 15259) {
												if(Misc.getRandom(3) == 1) {
													player.getInventory().add(o.getItemId() + 1, 1, "Mining");	
												}
											}
										} else {
										player.getInventory().add(o.getItemId(), 1, "Mining");
										if(player.getEquipment().getItems()[Equipment.WEAPON_SLOT].getId() == 15259) {
											
											if(Misc.getRandom(3) == 1) {
												player.getInventory().add(o.getItemId(), 1, "Mining");	
											}
										}
										}
										
										}
									if(player.getEquipment().getItems()[Equipment.WEAPON_SLOT].getId() == 15259) {
										if(Misc.getRandom(3) == 1) {
											player.getSkillManager().addExperience(Skill.MINING, (int) (o.getXpAmount()) * Skill.MINING.getModifier());

										}
									}
									player.getSkillManager().addExperience(Skill.MINING, (int) (o.getXpAmount()) * Skill.MINING.getModifier());

									if(o == Ores.CRASHED_STAR) {
										int boxChance = Misc.random(500);
										if(boxChance == 1) {
											player.getInventory().add(Items.HALLOWEEN_BOX, 1);
											player.sendMessage(Misc.getHalloweenIcon()+"A halloween box has appeared in your inventory!");
										}
										player.getPacketSender().sendMessage("You mine the crashed star..");
									} else {
										player.getPacketSender().sendMessage("You mine some ore.");
									}
									Sounds.sendSound(player, Sound.MINE_ITEM);
									cycle = 0;
									this.stop();
									if(o.getRespawn() > 0) {
										player.performAnimation(new Animation(65535));
										oreRespawn(player, oreObject, o);
									} else {
										if(oreObject.getId() == 38660) {
											if(ShootingStar.CRASHED_STAR == null || ShootingStar.CRASHED_STAR.getStarObject().getPickAmount() >= ShootingStar.MAXIMUM_MINING_AMOUNT) {
												player.getPacketSender().sendClientRightClickRemoval();
												player.getSkillManager().stopSkilling();
												return;
											} else {
												ShootingStar.CRASHED_STAR.getStarObject().incrementPickAmount();
											}
										} else {
											player.performAnimation(new Animation(65535));
										}
										startMining(player, oreObject);
									}
								}
							}
						});
						TaskManager.submit(player.getCurrentTask());
					} else {
						player.getPacketSender().sendMessage("You need a Mining level of at least "+p.getReq()+" to use this pickaxe.");
					}
				} else {
					player.getPacketSender().sendMessage("You need a Mining level of at least "+o.getLevelReq()+" to mine this rock.");
				}
			} else {
				player.getPacketSender().sendMessage("You don't have a pickaxe to mine this rock with.");
			}
		}
	}

	public static void oreRespawn(final Player player, final GameObject oldOre, Ores o) {
		if(oldOre == null || oldOre.getPickAmount() >= 1)
			return;
		oldOre.setPickAmount(1);
		for(Player players : player.getLocalPlayers()) {
			if(players == null)
				continue;
			if(players.getInteractingObject() != null && players.getInteractingObject().getPosition().equals(player.getInteractingObject().getPosition().copy())) {
				players.getPacketSender().sendClientRightClickRemoval();
				players.getSkillManager().stopSkilling();
			}
		}
		player.getPacketSender().sendClientRightClickRemoval();
		player.getSkillManager().stopSkilling();
		CustomObjects.globalObjectRespawnTask(new GameObject(452, oldOre.getPosition().copy(), 10, 0), oldOre, o.getRespawn());
	}
}
