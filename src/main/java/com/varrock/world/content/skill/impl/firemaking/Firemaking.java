package com.varrock.world.content.skill.impl.firemaking;

import com.varrock.engine.task.Task;
import com.varrock.engine.task.TaskManager;
import com.varrock.model.Animation;
import com.varrock.model.GameObject;
import com.varrock.model.Skill;
import com.varrock.model.definitions.ItemDefinition;
import com.varrock.model.movement.MovementQueue;
import com.varrock.util.Misc;
import com.varrock.world.content.Achievements;
import com.varrock.world.content.CustomObjects;
import com.varrock.world.content.Sounds;
import com.varrock.world.content.Achievements.AchievementData;
import com.varrock.world.content.Sounds.Sound;
import com.varrock.world.content.skill.impl.dungeoneering.Dungeoneering;
import com.varrock.world.entity.impl.player.Player;

/**
 * The Firemaking skill
 * @author Gabriel Hannason
 */

public class Firemaking {

	public static void lightFire(final Player player, int logId, final boolean addingToFire, final int amount, boolean noted) {
		if (!player.getClickDelay().elapsed(2000) || player.getMovementQueue().isLockMovement())
			return;
		if(!player.getLocation().isFiremakingAllowed()) {
			player.getPacketSender().sendMessage("You can not light a fire in this area. "+player.getLocation());
			return;
		}
		boolean objectExists = CustomObjects.objectExists(player.getPosition().copy());
		if(!Dungeoneering.doingDungeoneering(player)) {
			if(objectExists && !addingToFire || player.getPosition().getZ() > 0 || !player.getMovementQueue().canWalk(1, 0) && !player.getMovementQueue().canWalk(-1, 0) && !player.getMovementQueue().canWalk(0, 1) && !player.getMovementQueue().canWalk(0, -1)) {
				player.getPacketSender().sendMessage("You can not light a fire here.");
				return;
			}
		}
		final LogData.Log log = LogData.getLogForLogId(player, logId);
		
		if(log == null)
			return;
		player.getMovementQueue().reset();
		if(objectExists && addingToFire)
			MovementQueue.stepAway(player);
		player.getPacketSender().sendInterfaceRemoval();
		player.setEntityInteraction(null);
		player.getSkillManager().stopSkilling();
		int cycle = 2 + Misc.getRandom(3);
		if (player.getSkillManager().getMaxLevel(Skill.FIREMAKING) < log.getLevel()) {
			player.getPacketSender().sendMessage("You need a Firemaking level of atleast "+ log.getLevel()+" to light this.");
			return;
		}
		if(!addingToFire) {
			player.getPacketSender().sendMessage("You attempt to light a fire..");
			player.performAnimation(new Animation(733));
			player.getMovementQueue().setLockMovement(true);
		}
		player.setCurrentTask(new Task(addingToFire ? 2 : cycle, player, addingToFire ? true : false) {
			int added = 0;
			@Override
			public void execute() {
				player.getPacketSender().sendInterfaceRemoval();
				if(addingToFire && player.getInteractingObject() == null) { //fire has died
					player.getSkillManager().stopSkilling();
					player.getPacketSender().sendMessage("The fire has died out.");
					return;
				}

				player.getInventory().delete(noted ? ItemDefinition.forId(log.getLogId()).getNotedId() : log.getLogId(), 1);

				if(addingToFire) {
					player.performAnimation(new Animation(827));
					player.getPacketSender().sendMessage("You add some logs to the fire..");
				} else {
					if(!player.getMovementQueue().isMoving()) {
						player.getMovementQueue().setLockMovement(false);
						player.performAnimation(new Animation(65535));
						MovementQueue.stepAway(player);
					}
					CustomObjects.globalFiremakingTask(new GameObject(2732, player.getPosition().copy()), player, log.getBurnTime());
					player.getPacketSender().sendMessage("The fire catches and the logs begin to burn.");
					stop();
				}
				if(log == LogData.Log.OAK) {
					Achievements.finishAchievement(player, AchievementData.BURN_AN_OAK_LOG);
				} else if(log == LogData.Log.MAGIC) {
					Achievements.doProgress(player, AchievementData.BURN_100_MAGIC_LOGS);
					Achievements.doProgress(player, AchievementData.BURN_2500_MAGIC_LOGS);
				}
				Sounds.sendSound(player, Sound.LIGHT_FIRE);
				player.getSkillManager().addExperience(Skill.FIREMAKING, log.getXp() * Skill.FIREMAKING.getModifier());
				added++;

				if(added >= amount || !player.getInventory().contains(noted ? ItemDefinition.forId(logId).getNotedId() : logId)) {
					stop();
					if(added < amount && addingToFire && LogData.getLogForLogId(player, -1) != null && LogData.getLogForLogId(player, -1).getLogId() != logId) {
						player.getClickDelay().reset(0);
						Firemaking.lightFire(player, -1, true, (amount-added), false);
					}
					return;
				}
			}

			@Override
			public void stop() {
				setEventRunning(false);
				player.performAnimation(new Animation(65535));
				player.getMovementQueue().setLockMovement(false);
			}
		});
		TaskManager.submit(player.getCurrentTask());
		player.getClickDelay().reset(System.currentTimeMillis() + 500);
	}

}