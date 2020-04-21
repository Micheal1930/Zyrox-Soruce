package com.varrock.world.entity.impl.player;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.google.common.collect.Multiset.Entry;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.varrock.saving.PlayerSaving;
import com.varrock.util.Misc;
import com.varrock.world.content.ClueScrolls;
import com.varrock.world.content.skill.impl.construction.ConstructionSave;


public class JsonPlayerSaving {

	public static void save(Player player) {
		//if(player.getName().equalsIgnoreCase("Saint") || true) {
		//	PlayerSaving.getSaving().save(player);
		//	return;
		//}
			
		//if (player.newPlayer())
			//return;
		// Create the path and file objects.

		Path path = Paths.get("./data/saves/characters/", player.getUsername() + ".json");
		File file = path.toFile();
		file.getParentFile().setWritable(true);

		// Attempt to make the player save directory if it doesn't
		// exist.
		if (!file.getParentFile().exists()) {
			try {
				file.getParentFile().mkdirs();
			} catch (SecurityException e) {
				System.out.println("Unable to create directory for player data!");
			}
		}
		//try (FileWriter writer = new FileWriter(file)) {

			Gson builder = new GsonBuilder().setPrettyPrinting().create();
			JsonObject object = new JsonObject();
			object.addProperty("total-play-time-ms", player.getTotalPlayTime()); //OK
			object.addProperty("username", player.getUsername().trim()); //OK
			object.addProperty("password", player.getPassword().trim()); //OK
			object.addProperty("databaseId", player.getDatabaseId()); //OK
			object.addProperty("email", player.getEmailAddress() == null ? "null" : player.getEmailAddress().trim()); //OK
			object.addProperty("staff-rights", player.getRights().name()); //OK
			object.addProperty("game-mode", player.getGameMode().name()); //OK
			object.addProperty("boss-points", player.getBossPoints()); //OK
			object.addProperty("loyalty-title", player.getLoyaltyTitle().name()); //OK
			object.add("position", builder.toJsonTree(player.getPosition())); //OK
			object.addProperty("online-status", player.getRelations().getStatus().name()); //OK
			object.addProperty("given-starter", new Boolean(player.didReceiveStarter())); //OK
			object.addProperty("is-instanced", new Boolean(player.isPlayerInstanced())); //OK
			object.addProperty("money-pouch", new Long(player.getMoneyInPouch())); //OK
			object.addProperty("donated", new Long(player.getAmountDonated())); //OK
			object.addProperty("password-reset", player.getPasswordPlayer()); //OK
			object.addProperty("minutes-bonus-exp", new Integer(player.getMinutesBonusExp())); //OK
			object.addProperty("total-gained-exp", new Long(player.getSkillManager().getTotalGainedExp())); //OK
			object.addProperty("prestige-points", new Integer(player.getPointsHandler().getPrestigePoints())); //OK
			object.addProperty("achievement-points", new Integer(player.getPointsHandler().getAchievementPoints()));//OK
			object.addProperty("dung-tokens", new Integer(player.getPointsHandler().getDungeoneeringTokens())); //OK
			object.addProperty("commendations", new Integer(player.getPointsHandler().getCommendations())); //OK
			object.addProperty("loyalty-points", new Integer(player.getPointsHandler().getLoyaltyPoints())); //OK
			object.addProperty("total-loyalty-points", new Double(player.getAchievementAttributes().getTotalLoyaltyPointsEarned()));
			object.addProperty("voting-points", new Integer(player.getPointsHandler().getVotingPoints())); //OK
			object.addProperty("slayer-points", new Integer(player.getPointsHandler().getSlayerPoints())); //OK
			object.addProperty("hide-player", new Boolean(player.isHidePlayer())); //OK
			object.addProperty("pk-points", new Integer(player.getPointsHandler().getPkPoints())); //OK
			object.addProperty("donation-points", new Integer(player.getPointsHandler().getDonationPoints())); //OK
			object.addProperty("trivia-points", new Integer(player.getPointsHandler().getTriviaPoints())); //OK
			object.addProperty("cluescompleted", new Integer(ClueScrolls.getCluesCompleted())); //OK TODO: it's a global variable

			object.addProperty("player-kills", new Integer(player.getPlayerKillingAttributes().getPlayerKills())); //OK
			object.addProperty("player-killstreak",	new Integer(player.getPlayerKillingAttributes().getKillstreak())); //OK
			object.addProperty("player-deaths", new Integer(player.getPlayerKillingAttributes().getPlayerDeaths())); //OK
			object.addProperty("target-percentage",	new Integer(player.getPlayerKillingAttributes().getTargetPercentage())); //OK
			object.addProperty("bh-rank", new Integer(player.getAppearance().getBountyHunterSkull())); //OK
			object.addProperty("gender", player.getAppearance().getGender().name()); //OK
			object.addProperty("spell-book", player.getSpellbook().name()); //OK
			object.addProperty("prayer-book", player.getPrayerbook().name()); //OK
			object.addProperty("running", new Boolean(player.isRunning())); //OK
			object.addProperty("run-energy", new Integer(player.getRunEnergy())); //OK
			object.addProperty("music", new Boolean(player.musicActive())); //OK
			object.addProperty("sounds", new Boolean(player.soundsActive())); //OK
			object.addProperty("auto-retaliate", new Boolean(player.isAutoRetaliate())); //OK
			object.addProperty("xp-locked", new Boolean(player.experienceLocked())); //OK
			object.addProperty("veng-cast", new Boolean(player.hasVengeance())); //OK
			object.addProperty("last-veng", new Long(player.getLastVengeance().elapsed())); //OK
			object.addProperty("fight-type", player.getFightType().name()); //OK
			object.addProperty("sol-effect", new Integer(player.getStaffOfLightEffect())); //OK
			object.addProperty("skull-timer", new Integer(player.getSkullTimer())); //OK
			object.addProperty("accept-aid", new Boolean(player.isAcceptAid())); //OK
			object.addProperty("venom-damage", new Integer(player.getVenomDamage())); //OK
			object.addProperty("venom-immunity", new Integer(player.getVenomImmunity())); //OK
			object.addProperty("poison-damage", new Integer(player.getPoisonDamage())); //OK
			object.addProperty("poison-immunity", new Integer(player.getPoisonImmunity())); //OK
			object.addProperty("overload-timer", new Integer(player.getOverloadPotionTimer())); //OK
			object.addProperty("fire-immunity", new Integer(player.getFireImmunity())); //OK
			object.addProperty("fire-damage-mod", new Integer(player.getFireDamageModifier())); //OK
			object.addProperty("prayer-renewal-timer", new Integer(player.getPrayerRenewalPotionTimer())); //OK
			object.addProperty("teleblock-timer", new Integer(player.getTeleblockTimer())); //OK
			object.addProperty("special-amount", new Integer(player.getSpecialPercentage())); //OK
			object.addProperty("entered-gwd-room", new Boolean(player.getMinigameAttributes().getGodwarsDungeonAttributes().hasEnteredRoom())); //OK
			object.addProperty("gwd-altar-delay", new Long(player.getMinigameAttributes().getGodwarsDungeonAttributes().getAltarDelay())); //OK
			object.add("gwd-killcount", builder.toJsonTree(player.getMinigameAttributes().getGodwarsDungeonAttributes().getKillcount())); //OK
			object.addProperty("effigy", new Integer(player.getEffigy())); //OK
			object.addProperty("summon-npc", new Integer(player.getSummoning().getFamiliar() != null ? player.getSummoning().getFamiliar().getSummonNpc().getId() : -1)); //OK
			object.addProperty("summon-death", new Integer(player.getSummoning().getFamiliar() != null ? player.getSummoning().getFamiliar().getDeathTimer() : -1)); //OK
			object.addProperty("process-farming", new Boolean(player.shouldProcessFarming())); //OK
			object.addProperty("clanchat", player.getClanChatName() == null ? "null" : player.getClanChatName().trim()); //OK
			object.addProperty("autocast", new Boolean(player.isAutocast())); //OK
			object.addProperty("autocast-spell", player.getAutocastSpell() != null ? player.getAutocastSpell().spellId() : -1); //OK
			object.addProperty("dfs-charges", player.getDfsCharges()); //OK
			object.addProperty("coins-gambled", new Integer(player.getAchievementAttributes().getCoinsGambled())); //OK
			object.addProperty("slayer-master", player.getSlayer().getSlayerMaster().name()); //OK
			
			object.addProperty("slayer-task", player.getSlayer().getSlayerTask().name()); //OK
			object.addProperty("prev-slayer-task", player.getSlayer().getLastTask().name()); //OK
			object.addProperty("task-amount", player.getSlayer().getAmountToSlay()); //OK
			object.addProperty("task-streak", player.getSlayer().getTaskStreak()); //OK
			object.addProperty("duo-partner", player.getSlayer().getDuoPartner() == null ? "null" : player.getSlayer().getDuoPartner()); //OK
			object.addProperty("double-slay-xp", player.getSlayer().doubleSlayerXP); //OK
			object.addProperty("recoil-deg", new Integer(player.getRecoilCharges())); //OK
        object.addProperty("forging-deg", new Integer(player.getForgingCharges())); //OK
			object.add("brawler-deg", builder.toJsonTree(player.getBrawlerChargers())); //OK
			object.add("killed-players", builder.toJsonTree(player.getPlayerKillingAttributes().getKilledPlayers())); //OK
			object.add("killed-gods", builder.toJsonTree(player.getAchievementAttributes().getGodsKilled())); //OK
			object.add("barrows-brother",
					builder.toJsonTree(player.getMinigameAttributes().getBarrowsMinigameAttributes().getBarrowsData())); //OK
			object.addProperty("random-coffin", new Integer(player.getMinigameAttributes().getBarrowsMinigameAttributes().getRandomCoffin())); //OK
			object.addProperty("barrows-killcount", new Integer(player.getMinigameAttributes().getBarrowsMinigameAttributes().getKillcount())); //OK
			object.add("nomad",
					builder.toJsonTree(player.getMinigameAttributes().getNomadAttributes().getQuestParts())); //OK
			object.add("recipe-for-disaster", builder
					.toJsonTree(player.getMinigameAttributes().getRecipeForDisasterAttributes().getQuestParts())); //OK
			object.addProperty("recipe-for-disaster-wave",
					new Integer(player.getMinigameAttributes().getRecipeForDisasterAttributes().getWavesCompleted())); //OK
			object.add("dung-items-bound",
					builder.toJsonTree(player.getMinigameAttributes().getDungeoneeringAttributes().getBoundItems())); //OK
			object.addProperty("rune-ess", new Integer(player.getStoredRuneEssence())); //OK
			object.addProperty("pure-ess", new Integer(player.getStoredPureEssence())); //OK
			object.addProperty("has-bank-pin", new Boolean(player.getBankPinAttributes().hasBankPin())); //OK
			object.addProperty("last-pin-attempt", new Long(player.getBankPinAttributes().getLastAttempt())); //OK
			object.addProperty("invalid-pin-attempts", new Integer(player.getBankPinAttributes().getInvalidAttempts())); //OK
			object.add("bank-pin", builder.toJsonTree(player.getBankPinAttributes().getBankPin())); //OK
			object.add("appearance", builder.toJsonTree(player.getAppearance().getLook())); //OK
			object.add("agility-obj", builder.toJsonTree(player.getCrossedObstacles())); //OK
			object.add("skills", builder.toJsonTree(player.getSkillManager().getSkills())); //OK
			object.add("inventory", builder.toJsonTree(player.getInventory().getItems())); //OK
			object.add("equipment", builder.toJsonTree(player.getEquipment().getItems()));//OK
			/**
			 * Bank saving
			 */
			object.add("bank-0", builder.toJsonTree(player.getBank(0).getValidItems())); //OK
			object.add("bank-1", builder.toJsonTree(player.getBank(1).getValidItems()));
			object.add("bank-2", builder.toJsonTree(player.getBank(2).getValidItems()));
			object.add("bank-3", builder.toJsonTree(player.getBank(3).getValidItems()));
			object.add("bank-4", builder.toJsonTree(player.getBank(4).getValidItems()));
			object.add("bank-5", builder.toJsonTree(player.getBank(5).getValidItems()));
			object.add("bank-6", builder.toJsonTree(player.getBank(6).getValidItems()));
			object.add("bank-7", builder.toJsonTree(player.getBank(7).getValidItems()));
			object.add("bank-8", builder.toJsonTree(player.getBank(8).getValidItems()));


			/** STORE SUMMON **/
			if (player.getSummoning().getBeastOfBurden() != null) {
				object.add("store", builder.toJsonTree(player.getSummoning().getBeastOfBurden().getValidItems())); //OK
			}
			object.add("charm-imp", builder.toJsonTree(player.getSummoning().getCharmImpConfigs())); //OK

			for (Entry<Integer> dartItem : player.getBlowpipeLoading().getContents().entrySet()) {
				object.addProperty("blowpipe-charge-item", new Integer(dartItem.getElement())); //OK
				object.addProperty("blowpipe-charge-amount",
						new Integer(player.getBlowpipeLoading().getContents().count(dartItem.getElement()))); //OK
			}

			object.add("friends", builder.toJsonTree(player.getRelations().getFriendList().toArray())); //OK
			object.add("ignores", builder.toJsonTree(player.getRelations().getIgnoreList().toArray())); //OK
			object.add("loyalty-titles", builder.toJsonTree(player.getUnlockedLoyaltyTitles())); //OK
			object.add("kills", builder.toJsonTree(player.getKillsTracker().toArray())); //OK
			object.add("drops", builder.toJsonTree(player.getDropLog().toArray())); //OK
			object.add("achievements-completion",
					builder.toJsonTree(player.getAchievementAttributes().getCompletion())); //OK
			object.add("achievements-progress", builder.toJsonTree(player.getAchievementAttributes().getProgress())); //OK
			object.add("max-cape-colors", builder.toJsonTree(player.getMaxCapeColors())); //OK
			object.add("comp-cape-colors", builder.toJsonTree(player.getCompCapeColors())); //OK
			object.addProperty("player-title", new String(player.getTitle())); //OK
			object.addProperty("login-reward", new Boolean(player.isLoginReward())); //OK
			
			String json = builder.toJson(object);
			//PlayerBucket.getSingleton().store(player.getUsername(), json);
			try {
				PlayerSaving.getSaving().save(player);
			} catch(Exception e) {
				e.printStackTrace();
			}
			
			try {
			
			File file2 = new File("./data/saves/construction/"+player.getUsername() + ".obj");
			
			if(!file2.exists()) {
				file2.createNewFile();
			}
			
			FileOutputStream fileOut = new FileOutputStream(file2);
			ConstructionSave save = new ConstructionSave();
			save.supply(player);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(save);
			out.close();
			fileOut.close();
			
		} catch(Throwable t) {
			t.printStackTrace();
		}
	}

	public static boolean playerExists(String p) {
		p = Misc.formatPlayerName(p.toLowerCase());
		return PlayerSaving.getSaving().exists(p); //return PlayerBucket.getSingleton().exists(p);
	}
}
