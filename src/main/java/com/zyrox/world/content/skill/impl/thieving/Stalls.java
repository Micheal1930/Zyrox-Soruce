package com.zyrox.world.content.skill.impl.thieving;

import com.zyrox.model.Animation;
import com.zyrox.model.Item;
import com.zyrox.model.PlayerRights;
import com.zyrox.model.Position;
import com.zyrox.model.Skill;
import com.zyrox.model.Locations.Location;
import com.zyrox.util.Misc;
import com.zyrox.util.RandomUtility;
import com.zyrox.world.World;
import com.zyrox.world.content.Achievements;
import com.zyrox.world.content.Achievements.AchievementData;
import com.zyrox.world.content.transportation.TeleportHandler;
import com.zyrox.world.entity.impl.player.Player;

public class Stalls {

	public static void stealFromStall(Player player, int lvlreq, int xp, int reward, String message) {
		if (player.getInventory().getFreeSlots() < 1) {
			player.getPacketSender().sendMessage("You need some more inventory space to do this.");
			return;
		}
		if (player.getCombatBuilder().isBeingAttacked()) {
			player.getPacketSender().sendMessage("You must wait a few seconds after being out of combat before doing this.");
			return;
		}
		if (!player.getClickDelay().elapsed(2000))
			return;
		if (player.getSkillManager().getMaxLevel(Skill.THIEVING) < lvlreq) {
			player.getPacketSender().sendMessage("You need a Thieving level of at least " + lvlreq + " to steal from this stall.");
			return;
		}
		if (player.getLocation() == Location.DONATOR_ZONE) {
			if (RandomUtility.RANDOM.nextInt(35) == 5) {
				TeleportHandler.teleportPlayer(player, new Position(2338, 9800), player.getSpellbook().getTeleportType());
				player.getPacketSender().sendMessage("You were caught stealing and teleported away from the stall!");
				return;
			}
		}
		player.performAnimation(new Animation(881));
		if (!player.getLocation().equals(Location.DONATOR_ZONE)) {
			player.getPacketSender().sendMessage(message);
		}
		player.getPacketSender().sendInterfaceRemoval();
		player.getSkillManager().addExperience(Skill.THIEVING, xp);
		player.getClickDelay().reset();

		if (Misc.getRandom(14000) == 3) {
			player.getInventory().add(13324, 1, "Steal from stall");
			World.sendMessage("<col=6666FF> <img=10> [ Skilling Pets ]:</col> " + player.getUsername() + " has received the Rocky pet!");
			player.getPacketSender().sendMessage("@red@You have received a skilling pet!");
			player.getPacketSender().sendMessage("@red@Your account has been saved!");
			player.save();
		}


		if (PlayerRights.isExecutiveDonator(player)) {
			player.getInventory().add(995, 15000, "Stalls");
		} else if (PlayerRights.isPlatinumDonator(player)) {
			player.getInventory().add(995, 10000, "Stalls");
		} else if (PlayerRights.isExtremeDonator(player)) {
			player.getInventory().add(995, 7500, "Stalls");
		} else if (PlayerRights.isSuperDonator(player)) {
			player.getInventory().add(995, 5000, "Stalls");
		} else if (PlayerRights.isRegularDonator(player)) {
			player.getInventory().add(995, 3000, "Stalls");
		}
		
		if (player.getLocation() == Location.DONATOR_ZONE) {
			Item item = new Item(reward);
			
			int value = 10_000;
			
			value += Misc.random((int) (value * 0.1), (int) (value * 0.25));
			int bonus = 0;
			
			player.sendMessage("You steal @dre@" + Misc.format(value) + "</col> Coins.");
			player.getInventory().add(995, value, "Stalls dzone");
		} else {
			player.getInventory().add(reward, 1, "Steal from stall");
		}

		player.getSkillManager().stopSkilling();
		if (reward == 15009)
			Achievements.finishAchievement(player, AchievementData.STEAL_A_RING);
		else if (reward == 11998) {
			Achievements.doProgress(player, AchievementData.STEAL_140_SCIMITARS);
			Achievements.doProgress(player, AchievementData.STEAL_5000_SCIMITARS);
		}
	}

}