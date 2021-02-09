package com.zyrox.model;

import com.zyrox.util.Misc;
import com.zyrox.world.World;
import com.zyrox.world.content.PlayerPanel;
import com.zyrox.world.content.Achievements.AchievementData;
import com.zyrox.world.content.skill.impl.slayer.SlayerMaster;
import com.zyrox.world.content.skill.impl.slayer.SlayerTasks;
import com.zyrox.world.entity.impl.player.Player;

/**
 * Do not use .ordinal() for this class, use .getId() instead.
 */
public enum GameMode {

	NORMAL, ULTIMATE_IRONMAN, HARDCORE_IRONMAN, IRONMAN;

	public static void set(Player player, GameMode newMode, boolean death) {
		if (!death && !player.getClickDelay().elapsed(1000)) {
			return;
		}
		player.getClickDelay().reset();
		player.getPacketSender().sendInterfaceRemoval();
		if (newMode != player.getGameMode() || death) {
			if (!player.newPlayer()) {
				player.getSlayer().resetSlayerTask();
				player.getSlayer().setSlayerTask(SlayerTasks.NO_TASK).setAmountToSlay(0).setTaskStreak(0).setSlayerMaster(SlayerMaster.VANNAKA);
				for (AchievementData d : AchievementData.values()) {
					player.getAchievementAttributes().setCompletion(d.ordinal(), false);
				}
				player.getMinigameAttributes().getRecipeForDisasterAttributes().reset();
				player.getMinigameAttributes().getNomadAttributes().reset();
				player.getKillsTracker().clear();
				player.getDropLog().clear();
				player.getPointsHandler().reset();
				PlayerPanel.refreshPanel(player);
				player.getUpdateFlag().flag(Flag.APPEARANCE);
			}
		}
		
		if (!player.getGameMode().equals(NORMAL) && newMode != player.getGameMode()) {
			player.setRights(PlayerRights.getDonatorRights(player));
			player.getPacketSender().sendRights();
		}
		
		player.setGameMode(newMode);
		player.getPacketSender().sendIronmanMode(player.getGameMode().getId());

		if (!death) {
			player.getPacketSender().sendMessage("").sendMessage("You've set your gamemode to " + newMode.toString() + ".").sendMessage("If you wish to change it, please talk to the town crier in Edgeville.");
		} else {
			player.getPacketSender().sendMessage("Your account progress has been reset.");
		}

		if (player.newPlayer()) {
			//player.setPlayerLocked(true);
		} else {
			player.setPlayerLocked(false);
		}
		
	}

    /**
     * Use this instead of .ordinal() because the indexes are messed up due to some other dev.
     * @return the id to insert into player updating etc.
     */
    public int getId() {
        switch (this) {
            case NORMAL:
                return 0;
            case ULTIMATE_IRONMAN:
                return 1;
            case HARDCORE_IRONMAN:
                return 3;
            case IRONMAN:
                return 2;
        }
        return 0;
    }

    static Locations.Location[] locations = {
            Locations.Location.PEST_CONTROL_GAME,
            Locations.Location.INFERNO,
            Locations.Location.CASTLE_WARS_GAME,
            Locations.Location.RECIPE_FOR_DISASTER,
            Locations.Location.ARMOURED_ZOMBIES,
            Locations.Location.FIGHT_CAVES,
            Locations.Location.FIGHT_PITS,
            Locations.Location.DUEL_ARENA,
            Locations.Location.RAIDS,
            Locations.Location.FFAARENA,
            Locations.Location.FREE_FOR_ALL_ARENA,
            Locations.Location.DUNGEONEERING,

    };

    public static void hardcoreDeath(Player player) {
        //If player dies in a safe location, don't do a hardcore death.
        for (Locations.Location location : locations) {
	        if (player.getLocation().equals(location)) {
	            return;
            }
        }
        player.setGameMode(GameMode.IRONMAN);
        player.getPacketSender().sendIronmanMode(player.getGameMode().getId());
        player.getUpdateFlag().flag(Flag.APPEARANCE);
        player.getPacketSender().sendMessage("@red@You have fallen as a Hardcore Iron Man, your Hardcore status has been revoked.");
        int totalLevel = player.getSkillManager().getTotalLevel();
        if (totalLevel >= 1000) {
            World.sendMessage("[<col=FF0000>Death</col>]<col=FF0000> " + player.getUsername() + " has died as a Hardcore with a total of " + totalLevel + " and time of " + Misc.getTimePlayed((player.getTotalPlayTime() + player.getRecordedLogin().elapsed())) + ".</col>");
        }
    }

	@Override
	public String toString() {
		return name().toLowerCase().replaceAll("_", " ");
	}
}
