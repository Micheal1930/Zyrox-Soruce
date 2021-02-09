package com.zyrox.world.content.skill.impl.slayer;

import java.util.HashMap;

import com.zyrox.GameSettings;
import com.zyrox.model.Item;
import com.zyrox.model.Locations;
import com.zyrox.model.PlayerRights;
import com.zyrox.model.Position;
import com.zyrox.model.Skill;
import com.zyrox.model.Locations.Location;
import com.zyrox.model.container.impl.Equipment;
import com.zyrox.model.definitions.NpcDefinition;
import com.zyrox.util.Misc;
import com.zyrox.world.World;
import com.zyrox.world.content.Achievements;
import com.zyrox.world.content.PlayerPanel;
import com.zyrox.world.content.Achievements.AchievementData;
import com.zyrox.world.content.Emotes.Skillcape_Data;
import com.zyrox.world.content.dialogue.DialogueManager;
import com.zyrox.world.content.skill.impl.slayer.KonarQuoMaten.SlayerTask;
import com.zyrox.world.content.transportation.TeleportHandler;
import com.zyrox.world.content.well_of_goodwill.WellBenefit;
import com.zyrox.world.entity.impl.npc.NPC;
import com.zyrox.world.entity.impl.player.Player;

public class Slayer {

	private Player player;

	public Slayer(Player p) {
		this.player = p;
	}

	private SlayerTask task = SlayerTask.NO_TASK;

	private HashMap<String, Integer> log = new HashMap<String, Integer>();
	private SlayerTasks slayerTask = SlayerTasks.NO_TASK, lastTask = SlayerTasks.NO_TASK;
	private SlayerMaster slayerMaster = SlayerMaster.VANNAKA;
	private int amountToSlay, taskStreak;
	private String duoPartner, duoInvitation;
	private Location slayerLocation;

	public void assignTask() {
		boolean hasTask = getSlayerTask() != SlayerTasks.NO_TASK && player.getSlayer().getLastTask() != getSlayerTask();
		boolean duoSlayer = duoPartner != null;
		if (duoSlayer && !player.getSlayer().assignDuoSlayerTask())
			return;
		if (hasTask) {
			player.getPacketSender().sendInterfaceRemoval();
			return;
		}
		int[] taskData = SlayerTasks.getNewTaskData(slayerMaster);
		int slayerTaskId = taskData[0], slayerTaskAmount = taskData[1];
		SlayerTasks taskToSet = SlayerTasks.forId(slayerTaskId);
		if (taskToSet == player.getSlayer().getLastTask() || NpcDefinition.forId(taskToSet.getNpcIds().get(0)).getSlayerLevel() > player.getSkillManager().getMaxLevel(Skill.SLAYER)) {
			assignTask();
			return;
		}
		player.getPacketSender().sendInterfaceRemoval();
		this.amountToSlay = slayerTaskAmount;
		this.slayerTask = taskToSet;
		DialogueManager.start(player, SlayerDialogues.receivedTask(player, getSlayerMaster(), getSlayerTask()));
		PlayerPanel.refreshPanel(player);
		if (duoSlayer) {
			Player duo = World.getPlayerByName(duoPartner);
			duo.getSlayer().setSlayerTask(taskToSet);
			duo.getSlayer().setAmountToSlay(slayerTaskAmount);
			duo.getPacketSender().sendInterfaceRemoval();
			DialogueManager.start(duo, SlayerDialogues.receivedTask(duo, slayerMaster, taskToSet));
			PlayerPanel.refreshPanel(duo);
		}
	}

	public void resetSlayerTask() {
		SlayerTasks task = getSlayerTask();
		if (player.getSlayer().getTask().equals(SlayerTask.NO_TASK)) {
			if (task == SlayerTasks.NO_TASK) {
				return;
			}
		}
		this.slayerTask = SlayerTasks.NO_TASK;
		this.amountToSlay = 0;
		this.taskStreak = 0;
		player.getSlayer().setTask(SlayerTask.NO_TASK);
		if (player.getRights() != PlayerRights.PLAYER) {
			player.getPA().sendMessage("You reset your slayer task for free!");
		} else {
			player.getPointsHandler().setSlayerPoints(player.getPointsHandler().getSlayerPoints() - 5, false);
		}
		PlayerPanel.refreshPanel(player);
		Player duo = duoPartner == null ? null : World.getPlayerByName(duoPartner);
		if (duo != null) {
			duo.getSlayer().setSlayerTask(SlayerTasks.NO_TASK).setAmountToSlay(0).setTaskStreak(0);
			duo.getPacketSender()
					.sendMessage("Your partner exchanged 5 Slayer points to reset your team's Slayer task.");
			duo.getSlayer().setTask(SlayerTask.NO_TASK);
			PlayerPanel.refreshPanel(duo);
			player.getPacketSender().sendMessage("You've successfully reset your team's Slayer task.");
		} else {
			player.getPacketSender().sendMessage("Your Slayer task has been reset.");
		}
	}

	public void killedNpc(NPC npc) {
		if (slayerTask != SlayerTasks.NO_TASK) {

			boolean sufficient = slayerTask.getNpcIds().contains(npc.getId());

			if (slayerTask.equals(SlayerTasks.BLACK_DRAGON) && npc.getId() == 50) {
				sufficient = true;
			}

			if (sufficient) {
				handleSlayerTaskDeath(true);
				if (duoPartner != null) {
					Player duo = World.getPlayerByName(duoPartner);
					if (duo != null) {
						if (checkDuoSlayer(player, false)) {
							duo.getSlayer().handleSlayerTaskDeath(
									Locations.goodDistance(player.getPosition(), duo.getPosition(), 20));
						} else {
							resetDuo(player, duo);
						}
					}
				}
			}
		}
	}

	public void handleSlayerTaskDeath(boolean giveXp) {
		if (slayerTask.getTaskMaster() == SlayerMaster.KRYSTILIA) {
			if (player.getWildernessLevel() <= 0) {
				System.out.println("You must be in the wild in order for a Krystilia task to count towards your progress.");
				return;
			}
		}

		int xp = slayerTask.getXP() + Misc.getRandom(slayerTask.getXP() / 5);

		if (amountToSlay > 1) {
			amountToSlay--;
		} else {
			player.getPacketSender().sendMessage("")
					.sendMessage("You've completed your Slayer task! Return to a Slayer master for another one.");
			taskStreak++;
			Achievements.finishAchievement(player, AchievementData.COMPLETE_A_SLAYER_TASK);
			if (slayerTask.getTaskMaster() == SlayerMaster.KURADEL) {
				Achievements.finishAchievement(player, AchievementData.COMPLETE_A_HARD_SLAYER_TASK);
			} else if (slayerTask.getTaskMaster() == SlayerMaster.SUMONA) {
				Achievements.finishAchievement(player, AchievementData.COMPLETE_AN_ELITE_SLAYER_TASK);
			}
			lastTask = slayerTask;
			slayerTask = SlayerTasks.NO_TASK;
			amountToSlay = 0;
			givePoints(slayerMaster);
		}

		if (giveXp) {
			player.getSkillManager().addExperience(Skill.SLAYER, doubleSlayerXP ? xp * 2 : xp);
		}

		PlayerPanel.refreshPanel(player);
	}

	@SuppressWarnings("incomplete-switch")
	public void givePoints(SlayerMaster master) {
		int pointsReceived = 4;
		switch (master) {
		case DURADEL:
			pointsReceived = 6 + Misc.random(3);
			break;
		case KURADEL:
			pointsReceived = 7 + Misc.random(4);
			break;
		case KONAR_QUO_MATEN:
			pointsReceived = 7 + Misc.random(6);
			break;
		case SUMONA:
			pointsReceived = 8 + Misc.random(5);
			break;
			case KRYSTILIA:
			pointsReceived = 10 + Misc.random(10);
			break;
		}
		if (Skillcape_Data.SLAYER.isWearingCape(player)) {
			pointsReceived += pointsReceived + 2;
		}
		int per5 = pointsReceived * 2;
		int per10 = pointsReceived * 3;
		int per25 = pointsReceived * 6;

		if (World.getWell().isActive(WellBenefit.SLAYER_POINTS)) {
			pointsReceived *= 2;
		}

		if (player.getSlayer().getTaskStreak() == 5) {
			if(GameSettings.DOUBLE_POINTS) {
				per5 *= 2;
			}

			player.getPointsHandler().setSlayerPoints(per5, true);
			player.getPacketSender().sendMessage("You received " + per5 + " Slayer points.");
		} else if (player.getSlayer().getTaskStreak() == 10) {
			if(GameSettings.DOUBLE_POINTS) {
				per10 *= 2;
			}

			player.getPointsHandler().setSlayerPoints(per10, true);
			player.getPacketSender().sendMessage("You received " + per10 + " Slayer points.");
		} else if (player.getSlayer().getTaskStreak() == 25) {

			if(GameSettings.DOUBLE_POINTS) {
				per25 *= 2;
			}

			player.getPointsHandler().setSlayerPoints(per25, true);
			player.getPacketSender()
					.sendMessage("You received " + per25 + " Slayer points and your Task Streak has been reset.");
			player.getSlayer().setTaskStreak(0);
		} else if (player.getSlayer().getTaskStreak() >= 0 && player.getSlayer().getTaskStreak() < 5
				|| player.getSlayer().getTaskStreak() >= 6 && player.getSlayer().getTaskStreak() < 25) {

			if(GameSettings.DOUBLE_POINTS) {
				pointsReceived *= 2;
			}

			player.getPointsHandler().setSlayerPoints(pointsReceived, true);
			player.getPacketSender().sendMessage("You received " + pointsReceived + " Slayer points.");
		}
	}

	public boolean assignDuoSlayerTask() {
		player.getPacketSender().sendInterfaceRemoval();
		if (player.getSlayer().getSlayerTask() != SlayerTasks.NO_TASK) {
			player.getPacketSender().sendMessage("You already have a Slayer task.");
			return false;
		}
		Player partner = World.getPlayerByName(duoPartner);
		if (partner == null) {
			player.getPacketSender().sendMessage("");
			player.getPacketSender().sendMessage("You can only get a new Slayer task when your duo partner is online.");
			return false;
		}
		if (partner.getSlayer().getDuoPartner() == null
				|| !partner.getSlayer().getDuoPartner().equals(player.getUsername())) {
			resetDuo(player, null);
			return false;
		}
		if (partner.getSlayer().getSlayerTask() != SlayerTasks.NO_TASK) {
			player.getPacketSender().sendMessage("Your partner already has a Slayer task.");
			return false;
		}
		if (partner.getSlayer().getSlayerMaster() != player.getSlayer().getSlayerMaster()) {
			player.getPacketSender().sendMessage("You and your partner need to have the same Slayer master.");
			return false;
		}
		if (partner.getInterfaceId() > 0) {
			player.getPacketSender().sendMessage("Your partner must close all their open interfaces.");
			return false;
		}
		return true;
	}

	public static boolean checkDuoSlayer(Player p, boolean login) {
		if (p.getSlayer().getDuoPartner() == null) {
			return false;
		}
		Player partner = World.getPlayerByName(p.getSlayer().getDuoPartner());
		if (partner == null) {
			return false;
		}
		if (partner.getSlayer().getDuoPartner() == null
				|| !partner.getSlayer().getDuoPartner().equals(p.getUsername())) {
			resetDuo(p, null);
			return false;
		}
		if (partner.getSlayer().getSlayerMaster() != p.getSlayer().getSlayerMaster()) {
			resetDuo(p, partner);
			return false;
		}
		if (!p.getSlayer().getSlayerTask().equals(partner.getSlayer().getSlayerTask())) {
			resetDuo(p, partner);
			return false;
		}
		if (login) {
			if (!partner.getSlayer().getTask().equals(SlayerTask.NO_TASK)) {
				p.getSlayer().setTask(partner.getSlayer().getTask());
			} else {
				p.getSlayer().setSlayerTask(partner.getSlayer().getSlayerTask());
			}
			p.getSlayer().setAmountToSlay(partner.getSlayer().getAmountToSlay());
		}
		return true;
	}

	public static void resetDuo(Player player, Player partner) {
		if (partner != null) {
			if (partner.getSlayer().getDuoPartner() != null
					&& partner.getSlayer().getDuoPartner().equals(player.getUsername())) {
				partner.getSlayer().setDuoPartner(null);
				partner.getPacketSender().sendMessage("Your Slayer duo team has been disbanded.");
				PlayerPanel.refreshPanel(partner);
			}
		}
		player.getSlayer().setDuoPartner(null);
		player.getPacketSender().sendMessage("Your Slayer duo team has been disbanded.");
		PlayerPanel.refreshPanel(player);
	}

	public void handleInvitation(boolean accept) {
		if (duoInvitation != null) {
			Player inviteOwner = World.getPlayerByName(duoInvitation);
			if (inviteOwner != null) {
				if (accept) {
					if (duoPartner != null) {
						player.getPacketSender().sendMessage("You already have a Slayer duo partner.");
						inviteOwner.getPacketSender()
								.sendMessage("" + player.getUsername() + " already has a Slayer duo partner.");
						return;
					}
					if (inviteOwner.getSlayer().getSlayerTask() != SlayerTasks.NO_TASK) {
						player.getPacketSender().sendMessage("Your partner already has a Slayer task.");
						return;
					}
					if (inviteOwner.getSlayer().getTask() != SlayerTask.NO_TASK) {
						player.getPacketSender().sendMessage("Your partner already has a Slayer task.");
						return;
					}
					inviteOwner.getPacketSender()
							.sendMessage("" + player.getUsername() + " has joined your duo Slayer team.")
							.sendMessage("Seek respective Slayer master for a task.");
					inviteOwner.getSlayer().setDuoPartner(player.getUsername());
					PlayerPanel.refreshPanel(inviteOwner);
					player.getPacketSender()
							.sendMessage("You have joined " + inviteOwner.getUsername() + "'s duo Slayer team.");
					player.getSlayer().setDuoPartner(inviteOwner.getUsername());
					PlayerPanel.refreshPanel(player);
				} else {
					player.getPacketSender().sendMessage("You've declined the invitation.");
					inviteOwner.getPacketSender()
							.sendMessage("" + player.getUsername() + " has declined your invitation.");
				}
			} else
				player.getPacketSender().sendMessage("Failed to handle the invitation.");
		}
	}

	public void handleSlayerRingTP(int itemId, boolean equipment) {
		if (!player.getClickDelay().elapsed(4500))
			return;
		if (player.getMovementQueue().isLockMovement())
			return;
		SlayerTasks task = getSlayerTask();
		SlayerTask konar = player.getSlayer().getTask();

		if (konar.equals(SlayerTask.NO_TASK)) {
			if (task == SlayerTasks.NO_TASK) {
				return;
			}
		}
		if (!konar.equals(SlayerTask.NO_TASK)) {
			for (SlayerTasks ts : SlayerTasks.values()) {
				if (ts.getNpcIds().contains(konar.getId())) {
					task = ts;
				}
			}
		}

		if (task == null) {
			return;
		}

		if (task.getTaskPosition() == null) {
			player.getPacketSender().sendMessage("No teleport possible");
			return;
		}

		if(task == SlayerTasks.KRAKEN) {
			if(player.getLocation() == Location.KRAKEN) {
				player.sendMessage("You are already at Kraken!");
				return;
			}
			player.getKraken().enter(player, true);
		} else if(task == SlayerTasks.VORKATH) {
			if (!TeleportHandler.checkWildernessLevel(player)) {
				return;
			}
			if(player.getLocation() == Location.VORKATH) {
				player.sendMessage("You are already at Vorkath!");
				return;
			}
			TeleportHandler.startVorkath(player);
		} else if(task == SlayerTasks.ZULRAH) {
			if (!TeleportHandler.checkWildernessLevel(player)) {
				return;
			}
			if(player.getLocation() == Location.ZULRAH) {
				player.sendMessage("You are already at Zulrah!");
				return;
			}
			TeleportHandler.teleportZulrah(player);
		} else {
			Position slayerTaskPos = new Position(task.getTaskPosition().getX(), task.getTaskPosition().getY(),
					task.getTaskPosition().getZ());

			if (!TeleportHandler.checkReqs(player, slayerTaskPos))
				return;
			if (!TeleportHandler.teleportPlayer(player, slayerTaskPos, player.getSpellbook().getTeleportType())) {
				player.slayerRing = itemId;
				return;
			}
		}
		deleteRing(player, itemId, equipment);
	}

	public static void deleteRing(Player player, int id, boolean equipment) {
		Item slayerRing = new Item(id);

		if(!equipment)
			player.getInventory().delete(slayerRing);
		if (slayerRing.getId() < 13288) {
			if(equipment) {
				player.getEquipment().set(Equipment.RING_SLOT, new Item(slayerRing.getId() + 1, 1));
				player.getEquipment().refreshItems();
			} else {
				player.getInventory().add(slayerRing.getId() + 1, 1, "Slayer ring");
			}
		} else {
			if(equipment) {
				player.getEquipment().delete(id, 1);
				player.getEquipment().refreshItems();
			}
			player.getPacketSender().sendMessage("Your Ring of Slaying crumbles to dust.");
		}
	}

	public int getAmountToSlay() {
		return this.amountToSlay;
	}

	public Slayer setAmountToSlay(int amountToSlay) {
		this.amountToSlay = amountToSlay;
		return this;
	}

	public int getTaskStreak() {
		return this.taskStreak;
	}

	public Slayer setTaskStreak(int taskStreak) {
		this.taskStreak = taskStreak;
		return this;
	}

	public SlayerTasks getLastTask() {
		return this.lastTask;
	}

	public void setLastTask(SlayerTasks lastTask) {
		this.lastTask = lastTask;
	}

	public boolean doubleSlayerXP = false;

	public Slayer setDuoPartner(String duoPartner) {
		this.duoPartner = duoPartner;
		return this;
	}

	public String getDuoPartner() {
		return duoPartner;
	}

	public SlayerTasks getSlayerTask() {
		return slayerTask;
	}

	public Slayer setSlayerTask(SlayerTasks slayerTask) {
		this.slayerTask = slayerTask;
		return this;
	}

	public SlayerMaster getSlayerMaster() {
		return slayerMaster;
	}

	public void setSlayerMaster(SlayerMaster master) {
		this.slayerMaster = master;
	}

	public void setDuoInvitation(String player) {
		this.duoInvitation = player;
	}

	public static boolean handleRewardsInterface(Player player, int button) {
		if (player.getInterfaceId() == 36000) {
			switch (button) {
			case 36002:
				player.getPacketSender().sendInterfaceRemoval();
				break;
			case 36014:
				if (player.getPointsHandler().getSlayerPoints() < 10) {
					player.getPacketSender().sendMessage("You do not have 10 Slayer points.");
					return true;
				}
				player.getPointsHandler().setSlayerPoints(-10, true);
				player.getSkillManager().addExperience(Skill.SLAYER, 10000);
				player.getPacketSender().sendMessage("You've bought 10000 Slayer XP for 10 Slayer points.");
				break;
			case 36017:
				if (player.getPointsHandler().getSlayerPoints() < 300) {
					player.getPacketSender().sendMessage("You do not have 300 Slayer points.");
					return true;
				}
				if (player.getSlayer().doubleSlayerXP) {
					player.getPacketSender().sendMessage("You already have this buff.");
					return true;
				}
				player.getPointsHandler().setSlayerPoints(-300, true);
				player.getSlayer().doubleSlayerXP = true;
				player.getPacketSender().sendMessage("You will now permanently receive double Slayer experience.");
				break;
			}
			player.getPacketSender().sendString(36030,
					"Current Points:   " + player.getPointsHandler().getSlayerPoints());
			return true;
		}
		return false;
	}

	/**
	 * Gets the task
	 *
	 * @return the task
	 */
	public SlayerTask getTask() {
		return task;
	}

	/**
	 * Sets the task
	 *
	 * @param task the task
	 */
	public void setTask(SlayerTask task) {
		this.task = task;
	}

	/**
	 * Gets the slayerLocation
	 *
	 * @return the slayerLocation
	 */
	public Location getSlayerLocation() {
		return slayerLocation;
	}

	/**
	 * Sets the slayerLocation
	 *
	 * @param slayerLocation the slayerLocation
	 */
	public void setSlayerLocation(Location slayerLocation) {
		this.slayerLocation = slayerLocation;
	}

	/**
	 * Gets the log
	 *
	 * @return the log
	 */
	public HashMap<String, Integer> getLog() {
		return log;
	}
}
