package com.varrock.world.content.transportation;

import com.varrock.GameSettings;
import com.varrock.engine.task.Task;
import com.varrock.engine.task.TaskManager;
import com.varrock.model.*;
import com.varrock.model.Locations.Location;
import com.varrock.world.World;
import com.varrock.world.content.Sounds;
import com.varrock.world.content.Sounds.Sound;
import com.varrock.world.content.combat.strategy.zulrah.ZulrahConstants;
import com.varrock.world.content.dialogue.DialogueManager;
import com.varrock.world.content.inferno.Inferno;
import com.varrock.world.content.instances.BossInstance;
import com.varrock.world.content.instances.InstanceManager;
import com.varrock.world.content.skill.impl.dungeoneering.Dungeoneering;
import com.varrock.world.content.skill.impl.summoning.SummoningTab;
import com.varrock.world.entity.impl.npc.NPC;
import com.varrock.world.entity.impl.npc.NPCMovementCoordinator.Coordinator;
import com.varrock.world.entity.impl.player.Player;

public class TeleportHandler {
	
	public static final int WILDERNESS_WARNING = 1_138_913;

	public static boolean teleportPlayer(final Player player, final Position targetLocation,
			final TeleportType teleportType) {
		if (!player.getClickDelay().elapsed(4500) || player.getMovementQueue().isLockMovement()) {
			return false;
		}

		if (player.getLocation() == Location.CONSTRUCTION) {
			player.getPacketSender().sendMessage("Please use the portal to exit your house");
			return false;
		}

		if (teleportType != TeleportType.LEVER && teleportType != TeleportType.RING_TELE) {
			if (!checkReqs(player, targetLocation)) {
				return false;
			}
		}

		BossInstance inferno = InstanceManager.get().getInstance(player);

		if (inferno != null && inferno instanceof Inferno) {
			player.sendMessage("You cannot teleport out of here.");
			return false;
		}
		int x = targetLocation.getX();
		int y = targetLocation.getY();
		if (Location.inLocation(x, y, Location.WILDERNESS) && player.getLocation() != Location.WILDERNESS) {
			DialogueManager.start(player, 70);
			player.setDialogueActionId(WILDERNESS_WARNING);
			player.targetLocation = targetLocation;
			return false;
		}
		completeTeleport(player, teleportType, targetLocation);
		return true;
	}

	public static void completeTeleport(Player player, TeleportType teleportType, Position targetLocation) {
		player.setTeleporting(true).getMovementQueue().setLockMovement(true).reset();
		cancelCurrentActions(player);
		player.performAnimation(teleportType.getStartAnimation());
		player.performGraphic(teleportType.getStartGraphic());
		Sounds.sendSound(player, Sound.TELEPORT);
		TaskManager.submit(new Task(1, player, true) {
			int tick = 0;

			@Override
			public void execute() {
				switch (teleportType) {
				case LEVER:
					if (tick == 0)
						player.performAnimation(new Animation(2140));
					else if (tick == 2) {
						player.performAnimation(new Animation(8939, 20));
						player.performGraphic(new Graphic(1576));
					} else if (tick == 4) {
						player.performAnimation(new Animation(8941));
						player.performGraphic(new Graphic(1577));
						player.moveTo(targetLocation).setPosition(targetLocation);
						player.getMovementQueue().setLockMovement(false).reset();
						stop();
					}
					break;
				default:
					if (tick == teleportType.getStartTick()) {
						cancelCurrentActions(player);
						player.performAnimation(teleportType.getEndAnimation());
						player.performGraphic(teleportType.getEndGraphic());

						if (Dungeoneering.doingDungeoneering(player)) {
							final Position dungEntrance = player.getMinigameAttributes().getDungeoneeringAttributes()
									.getParty().getDungeoneeringFloor().getEntrance().copy()
									.setZ(player.getPosition().getZ());
							player.moveTo(dungEntrance).setPosition(dungEntrance);
						} else {
							player.moveTo(targetLocation).setPosition(targetLocation);
						}

						player.setTeleporting(false);
					} else if (tick == teleportType.getStartTick() + 3) {
						player.getMovementQueue().setLockMovement(false).reset();
					} else if (tick == teleportType.getStartTick() + 4)
						stop();
					break;
				}
				tick++;
			}

			@Override
			public void stop() {
				setEventRunning(false);
				player.setTeleporting(false);
				player.getClickDelay().reset(0);
				BossInstance instance = InstanceManager.get().getInstance(player);
				Inferno inferno = instance != null && instance instanceof Inferno ? (Inferno) instance : null;
				if (inferno != null) {
					inferno.removeAll();
					InstanceManager.get().remove(inferno);
				}

				SummoningTab.callFollower(player, true);
			}
		});
		player.getClickDelay().reset();
	}

	public static boolean interfaceOpen(Player player) {
		if (player.getInterfaceId() > 0 && player.getInterfaceId() != 50100) {
			player.getPacketSender().sendMessage("Please close the interface you have open before opening another.");
			return true;
		}
		return false;
	}

	public static boolean checkXTeleReqs(Player player, Position targetLocation) {
		if (player.getRegionID() == 9043) {
			return false;
		}
		return true;
	}

	public static boolean checkReqs(Player player, Position targetLocation) {
		return checkReqs(player, targetLocation, false);
	}

	public static boolean checkReqs(Player player, Position targetLocation, boolean jewlery) {
		if (player.getConstitution() <= 0) {
			return false;
		}
		if (player.getTeleblockTimer() > 0) {
			player.getPacketSender().sendMessage("A magical spell is blocking you from teleporting.");
			return false;
		}


		System.out.println(player.getLocation()+", "+jewlery);

		if (player.getLocation() != null) {
			if(player.getLocation() == Location.WILDERNESS && jewlery) {
				if (player.getWildernessLevel() > 30) {
					player.getPacketSender().sendInterfaceRemoval();
					player.sendMessage("You can't teleport above level 30 Wilderness.");
					return false;
				}
			} else {
				if (!player.getLocation().canTeleport(player)) {
					return false;
				}
			}
		}

		if (player.isPlayerLocked() || player.isCrossingObstacle()) {
			player.getPacketSender().sendMessage("You cannot teleport right now.");
			return false;
		}

		return true;
	}

	public static boolean checkWildernessLevel(Player player) {
		if (player.getWildernessLevel() > 20) {
			player.getPacketSender().sendInterfaceRemoval();
			player.sendMessage("You can't teleport above level 20 Wilderness.");
			return false;
		}
		return true;
	}

	public static void cancelCurrentActions(Player player) {
		player.getPacketSender().sendInterfaceRemoval();
		player.setTeleporting(false);
		player.setWalkToTask(null);
		player.setInputHandling(null);
		player.getSkillManager().stopSkilling();
		player.setEntityInteraction(null);
		player.getMovementQueue().setFollowCharacter(null);
		player.getCombatBuilder().cooldown(false);
		player.setResting(false);
	}

	public static void teleportZulrah(Player player) {
		if (!TeleportHandler.checkWildernessLevel(player)) {
			return;
		}
		if (GameSettings.ZULRAH_ENABLED) {
			player.getPA().sendInterfaceRemoval();
			player.moveTo(new Position(2268, 3070, player.getIndex() * 4));
			ZulrahConstants.startBossFight(player);
		} else {
			player.sendMessage("@dre@Zulrah is Disabled at the moment. Try again soon.");
		}
	}

	/**
	 * Starts a instance of vorkath.
	 * @param player
	 */
	public static void startVorkath(Player player) {

		if(player.getSummoning().getFamiliar() != null) {
			player.sendMessage("Your follower seems to block your teleport...");
			return;
		}

		TeleportHandler.teleportPlayer(player, new Position(2272, 4054, player.getIndex() * 4), player.getSpellbook().getTeleportType());
		TaskManager.submit(new Task(5) {
			@Override
			protected void execute() {
				if (player.getRegionInstance() != null
						&& player.getRegionInstance().getType().equals(RegionInstance.RegionInstanceType.VORKATH)) {
					World.getNpcs().forEach(n -> n.removeInstancedNpcs(Locations.Location.VORKATH, player.getIndex() * 4));
				} else {
					player.setRegionInstance(
							new RegionInstance(player, RegionInstance.RegionInstanceType.VORKATH));
				}

				player.setRegionInstance(
						new RegionInstance(player, RegionInstance.RegionInstanceType.VORKATH));

				NPC npc = NPC.of(23060, new Position(2269, 4062, player.getIndex() * 4));
				World.register(player, npc);
				npc.getMovementCoordinator().setCoordinator(new Coordinator(false, 10));
				npc.setPositionToFace(new Position(2272, 1));
				npc.setTransformationId(23058);
				npc.getUpdateFlag().flag(Flag.TRANSFORM);
				TaskManager.submit(new Task(3) {
					@Override
					protected void execute() {
						npc.setTransformationId(23060);
						npc.getUpdateFlag().flag(Flag.TRANSFORM);
						npc.performAnimation(new Animation(7950 + GameSettings.OSRS_ANIM_OFFSET));
						this.stop();
					}
				});
				TaskManager.submit(new Task(10) {
					@Override
					protected void execute() {
						npc.getCombatBuilder().attack(player);
						this.stop();
					}
				});
				this.stop();
			}
		});
	}

	/**
	 * Starts a instance of tarn.
	 * @param player
	 */
	public static void startTarn(Player player) {

		if (player.getRegionInstance() != null
				&& player.getRegionInstance().getType().equals(RegionInstance.RegionInstanceType.MUTANT_TARN)) {
			World.getNpcs().forEach(n -> n.removeInstancedNpcs(Locations.Location.MUTANT_TARN, player.getIndex() * 4));
		}

		TeleportHandler.teleportPlayer(player, new Position(3186, 4621, player.getIndex() * 4), player.getSpellbook().getTeleportType());
		TaskManager.submit(new Task(10) {
			@Override
			protected void execute() {

				player.setRegionInstance(
						new RegionInstance(player, RegionInstance.RegionInstanceType.MUTANT_TARN));

				NPC demon1 = NPC.of(22865, new Position(3182, 4621, player.getIndex() * 4));
				World.register(player, demon1);

				NPC demon2 = NPC.of(22865, new Position(3189, 4621, player.getIndex() * 4));
				World.register(player, demon2);

				NPC demon3 = NPC.of(22865, new Position(3182, 4617, player.getIndex() * 4));
				World.register(player, demon3);

				NPC demon4 = NPC.of(22865, new Position(3189, 4617, player.getIndex() * 4));
				World.register(player, demon4);

				NPC npc = NPC.of(21475, new Position(3186, 4624, player.getIndex() * 4));
				World.register(player, npc);

				npc.getMovementQueue().setLockMovement(true);
				npc.setPositionToFace(new Position(3186, 4623));
				npc.performAnimation(new Animation(5611 + GameSettings.OSRS_ANIM_OFFSET));

				npc.setGraphic(new Graphic(3968));
				npc.forceChat("Muwhahahah!");

				TaskManager.submit(new Task(7) {
					@Override
					protected void execute() {
						npc.setTransformationId(21477);
						npc.getUpdateFlag().flag(Flag.TRANSFORM);
						this.stop();
					}
				});

				TaskManager.submit(new Task(10) {
					@Override
					protected void execute() {
						npc.getMovementQueue().setLockMovement(false);
						npc.getCombatBuilder().attack(player);
						this.stop();
					}
				});
				this.stop();
			}
		});
	}
}
