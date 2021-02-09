package com.zyrox.world.content;

import com.zyrox.util.Misc;
import com.zyrox.world.entity.impl.player.Player;

public class Achievements {

	public enum AchievementData {

		ENTER_THE_LOTTERY(Difficulty.EASY, "Enter The Lottery", 45005, null),
		FILL_WELL_OF_GOODWILL_1M(Difficulty.EASY, "Pour 1M Into The Well", 45006, null),
		CUT_AN_OAK_TREE(Difficulty.EASY, "Cut An Oak Tree", 45007, null),
		BURN_AN_OAK_LOG(Difficulty.EASY, "Burn An Oak Log", 45008, null),
		FISH_A_SALMON(Difficulty.EASY, "Fish A Salmon", 45009, null),
		COOK_A_SALMON(Difficulty.EASY, "Cook A Salmon", 45010, null),
		EAT_A_SALMON(Difficulty.EASY, "Eat A Salmon", 45011, null),
		MINE_SOME_IRON(Difficulty.EASY, "Mine Some Iron", 45012, null),
		SMELT_AN_IRON_BAR(Difficulty.EASY, "Smelt An Iron Bar", 45013, null),
		HARVEST_A_CROP(Difficulty.EASY, "Harvest A Crop", 45014, null),
		INFUSE_A_DREADFOWL_POUCH(Difficulty.EASY, "Infuse A Dreadfowl Pouch", 45015, null),
		CATCH_A_YOUNG_IMPLING(Difficulty.EASY, "Catch A Young Impling", 45016, null),
		CRAFT_A_PAIR_OF_LEATHER_BOOTS(Difficulty.EASY, "Craft A Pair of Leather Boots", 45017, null),
		CLIMB_AN_AGILITY_OBSTACLE(Difficulty.EASY, "Climb An Agility Obstacle", 45018, null),
		FLETCH_SOME_ARROWS(Difficulty.EASY, "Fletch Some Arrows", 45019, null),
		STEAL_A_RING(Difficulty.EASY, "Steal A Ring", 45020, null),
		MIX_A_POTION(Difficulty.EASY, "Mix A Potion", 45021, null),
		RUNECRAFT_SOME_RUNES(Difficulty.EASY, "Runecraft Some Runes", 45022, null),
		BURY_A_BIG_BONE(Difficulty.EASY, "Bury A Big Bone", 45023, null),
		COMPLETE_A_SLAYER_TASK(Difficulty.EASY, "Complete A Slayer Task", 45024, null),
		SET_UP_A_CANNON(Difficulty.EASY, "Set Up A Cannon", 45025, null),
		KILL_A_MONSTER_USING_MELEE(Difficulty.EASY, "Kill a Monster Using Melee", 45026, null),
		KILL_A_MONSTER_USING_RANGED(Difficulty.EASY, "Kill a Monster Using Ranged", 45027, null),
		KILL_A_MONSTER_USING_MAGIC(Difficulty.EASY, "Kill a Monster Using Magic", 45028, null),
		DEAL_EASY_DAMAGE_USING_MELEE(Difficulty.EASY, "Deal 1000 Melee Damage", 45029, new int[]{0, 1000}),
		DEAL_EASY_DAMAGE_USING_RANGED(Difficulty.EASY, "Deal 1000 Ranged Damage", 45030, new int[]{1, 1000}),
		DEAL_EASY_DAMAGE_USING_MAGIC(Difficulty.EASY, "Deal 1000 Magic Damage", 45031, new int[]{2, 1000}),
		PERFORM_A_SPECIAL_ATTACK(Difficulty.EASY, "Perform a Special Attack", 45032, null),
		FIGHT_ANOTHER_PLAYER(Difficulty.EASY, "Fight Another Player", 45033, null),
		SET_AN_EMAIL_ADDRESS(Difficulty.EASY, "Set An Email Address", 45034, null),

		ENTER_THE_LOTTERY_THREE_TIMES(Difficulty.MEDIUM, "Enter The Lottery Three Times", 45037, new int[]{3, 3}),
		FILL_WELL_OF_GOODWILL_50M(Difficulty.MEDIUM, "Pour 50M Into The Well", 45038, new int[]{4, 50000000}),
		CUT_100_MAGIC_LOGS(Difficulty.MEDIUM, "Cut 100 Magic Logs", 45039, new int[]{5, 100}),
		BURN_100_MAGIC_LOGS(Difficulty.MEDIUM, "Burn 100 Magic Logs", 45040, new int[]{6, 100}),
		FISH_25_ROCKTAILS(Difficulty.MEDIUM, "Fish 25 Rocktails", 45041, new int[]{7, 25}),
		COOK_25_ROCKTAILS(Difficulty.MEDIUM, "Cook 25 Rocktails", 45042, new int[]{8, 25}),
		MINE_25_RUNITE_ORES(Difficulty.MEDIUM, "Mine 25 Runite Ores", 45043, new int[]{9, 25}),
		SMELT_25_RUNE_BARS(Difficulty.MEDIUM, "Smelt 25 Rune Bars", 45044, new int[]{10, 25}),
		HARVEST_10_TORSTOLS(Difficulty.MEDIUM, "Harvest 10 Torstols", 45045, new int[]{11, 10}),
		INFUSE_25_TITAN_POUCHES(Difficulty.MEDIUM, "Infuse 25 Steel Titans", 45046, new int[]{12, 25}),
		CATCH_5_KINGLY_IMPLINGS(Difficulty.MEDIUM, "Catch 5 Kingly Implings", 45047, new int[]{13, 5}),
		COMPLETE_A_HARD_SLAYER_TASK(Difficulty.MEDIUM, "Complete A Hard Slayer Task", 45048, null),
		CRAFT_20_BLACK_DHIDE_BODIES(Difficulty.MEDIUM, "Craft 20 Black D'hide Bodies", 45049, new int[]{14, 20}),
		FLETCH_450_RUNE_ARROWS(Difficulty.MEDIUM, "Fletch 450 Rune Arrows", 45050, new int[]{15, 450}),
		STEAL_140_SCIMITARS(Difficulty.MEDIUM, "Steal 140 Scimitars", 45051, new int[]{16, 140}),
		MIX_AN_OVERLOAD_POTION(Difficulty.MEDIUM, "Mix An Overload Potion", 45052, null),
		ASSEMBLE_A_GODSWORD(Difficulty.MEDIUM, "Assemble A Godsword", 45053, null),
		CLIMB_50_AGILITY_OBSTACLES(Difficulty.MEDIUM, "Climb 50 Agility Obstacles", 45054, new int[]{17, 50}),
		RUNECRAFT_500_BLOOD_RUNES(Difficulty.MEDIUM, "Runecraft 500 Blood Runes", 45055, new int[]{18, 500}),
		BURY_25_FROST_DRAGON_BONES(Difficulty.MEDIUM, "Bury 25 Frost Dragon Bones", 45056, new int[]{19, 25}),
		FIRE_500_CANNON_BALLS(Difficulty.MEDIUM, "Fire 500 Cannon Balls", 45057, new int[]{20, 500}),
		DEAL_MEDIUM_DAMAGE_USING_MELEE(Difficulty.MEDIUM, "Deal 100K Melee Damage", 45058, new int[]{21, 100000}),
		DEAL_MEDIUM_DAMAGE_USING_RANGED(Difficulty.MEDIUM, "Deal 100K Ranged Damage", 45059, new int[]{22, 100000}),
		DEAL_MEDIUM_DAMAGE_USING_MAGIC(Difficulty.MEDIUM, "Deal 100K Magic Damage", 45060, new int[]{23, 100000}),
		DEFEAT_THE_KING_BLACK_DRAGON(Difficulty.MEDIUM, "Defeat The King Black Dragon", 45061, null),
		DEFEAT_THE_CHAOS_ELEMENTAL(Difficulty.MEDIUM, "Defeat The Chaos Elemental", 45062, null),
		DEFEAT_A_TORMENTED_DEMON(Difficulty.MEDIUM, "Defeat A Tormented Demon", 45063, null),
		DEFEAT_THE_CULINAROMANCER(Difficulty.MEDIUM, "Defeat The Culinaromancer", 45064, null),
		DEFEAT_NOMAD(Difficulty.MEDIUM, "Defeat Nomad", 45065, null),
		DEFEAT_10_PLAYERS(Difficulty.MEDIUM, "Defeat 10 Players", 45066, new int[]{24, 10}),
		REACH_A_KILLSTREAK_OF_3(Difficulty.MEDIUM, "Reach A Killstreak Of 3", 45067, null),

		FILL_WELL_OF_GOODWILL_250M(Difficulty.HARD, "Pour 250M Into The Well", 45070, new int[]{25, 250000000}),
		CUT_5000_MAGIC_LOGS(Difficulty.HARD, "Cut 5000 Magic Logs", 45071, new int[]{26, 5000}),
		BURN_2500_MAGIC_LOGS(Difficulty.HARD, "Burn 2500 Magic Logs", 45072, new int[]{27, 2500}),
		FISH_2000_ROCKTAILS(Difficulty.HARD, "Fish 2000 Rocktails", 45073, new int[]{28, 2000}),
		COOK_1000_ROCKTAILS(Difficulty.HARD, "Cook 1000 Rocktails", 45074, new int[]{29, 1000}),
		MINE_2000_RUNITE_ORES(Difficulty.HARD, "Mine 2000 Runite Ores", 45075, new int[]{30, 2000}),
		SMELT_1000_RUNE_BARS(Difficulty.HARD, "Smelt 1000 Rune Bars", 45076, new int[]{31, 1000}),
		HARVEST_1000_TORSTOLS(Difficulty.HARD, "Harvest 1000 Torstols", 45077, new int[]{32, 1000}),
		INFUSE_500_STEEL_TITAN_POUCHES(Difficulty.HARD, "Infuse 500 Steel Titans", 45078, new int[]{33, 500}),
		CRAFT_1000_DIAMOND_GEMS(Difficulty.HARD, "Craft 1000 Diamond Gems", 45079, new int[]{34, 1000}),
		CATCH_100_KINGLY_IMPLINGS(Difficulty.HARD, "Catch 100 Kingly Imps", 45080, new int[]{35, 100}),
		FLETCH_5000_RUNE_ARROWS(Difficulty.HARD, "Fletch 5000 Rune Arrows", 45081, new int[]{36, 5000}),
		STEAL_5000_SCIMITARS(Difficulty.HARD, "Steal 5000 Scimitars", 45082, new int[]{57, 5000}),
		RUNECRAFT_8000_BLOOD_RUNES(Difficulty.HARD, "Runecraft 8000 Blood Runes", 45083, new int[]{38, 8000}),
		BURY_500_FROST_DRAGON_BONES(Difficulty.HARD, "Bury 500 Frost Dragon Bones", 45084, new int[]{39, 500}),
		FIRE_5000_CANNON_BALLS(Difficulty.HARD, "Fire 5000 Cannon Balls", 45085, new int[]{40, 5000}),
		MIX_100_OVERLOAD_POTIONS(Difficulty.HARD, "Mix 100 Overload Potions", 45086, new int[]{41, 100}),
		COMPLETE_AN_ELITE_SLAYER_TASK(Difficulty.HARD, "Complete An Elite Slayer Task", 45087, null),
		ASSEMBLE_5_GODSWORDS(Difficulty.HARD, "Assemble 5 Godswords", 45088, new int[]{42, 5}),
		DEAL_HARD_DAMAGE_USING_MELEE(Difficulty.HARD, "Deal 10M Melee Damage", 45089, new int[]{43, 10000000}),
		DEAL_HARD_DAMAGE_USING_RANGED(Difficulty.HARD, "Deal 10M Ranged Damage", 45090, new int[]{44, 10000000}),
		DEAL_HARD_DAMAGE_USING_MAGIC(Difficulty.HARD, "Deal 10M Magic Damage", 45091, new int[]{45, 10000000}),
		DEFEAT_JAD(Difficulty.HARD, "Defeat Jad", 45092, null),
		DEFEAT_BANDOS_AVATAR(Difficulty.HARD, "Defeat Bandos Avatar", 45093, null),
		DEFEAT_GENERAL_GRAARDOR(Difficulty.HARD, "Defeat General Graardor", 45094, null),
		DEFEAT_KREE_ARRA(Difficulty.HARD, "Defeat Kree'Arra", 45095, null),
		DEFEAT_COMMANDER_ZILYANA(Difficulty.HARD, "Defeat Commander Zilyana", 45096, null),
		DEFEAT_KRIL_TSUTSAROTH(Difficulty.HARD, "Defeat K'ril Tsutsaroth", 45097, null),
		DEFEAT_THE_CORPOREAL_BEAST(Difficulty.HARD, "Defeat The Corporeal Beast", 45098, null),
		DEFEAT_NEX(Difficulty.HARD, "Defeat Nex", 45099, null),
		DEFEAT_30_PLAYERS(Difficulty.HARD, "Defeat 30 Players", 45100, new int[]{46, 30}),
		REACH_A_KILLSTREAK_OF_6(Difficulty.HARD, "Reach A Killstreak Of 6", 45101, null),

		COMPLETE_ALL_HARD_TASKS(Difficulty.ELITE, "Complete All Hard Tasks", 45104, new int[]{47, 32}),
		CUT_AN_ONYX_STONE(Difficulty.ELITE, "Cut An Onyx Stone", 45105, null),
		REACH_MAX_EXP_IN_A_SKILL(Difficulty.ELITE, "Reach Max Exp In A Skill", 45106, null),
		REACH_LEVEL_99_IN_ALL_SKILLS(Difficulty.ELITE, "Reach Level 99 In All Skills", 45107, new int[]{48, 22}),
		DEFEAT_10000_MONSTERS(Difficulty.ELITE, "Defeat 10,000 Monsters", 45108, new int[]{49, 10000}),
		DEFEAT_500_BOSSES(Difficulty.ELITE, "Defeat 500 Boss Monsters", 45109, new int[]{50, 500}),
		VOTE_100_TIMES(Difficulty.ELITE, "Vote 50 Times", 45110, new int[]{51, 50}),
		UNLOCK_ALL_LOYALTY_TITLES(Difficulty.ELITE, "Unlock All Loyalty Titles", 45111, new int[]{52, 11}),
		MAKE_1000_GLORYS(Difficulty.ELITE, "Craft 1000 Amulet of Glorys", 45112, new int[]{53, 1000}),
		CLEAN_1000_TORSTOL(Difficulty.ELITE, "Clean 1000 Torstol", 45113, new int[]{54, 1000}),
		FILL_WELL_OF_GOODWILL_1000M(Difficulty.ELITE, "Pour 1000M Into The Well", 45114, new int[]{55, 1000000000}),
		MAKE_A_SPIRIT_SET(Difficulty.ELITE, "Make all 4 Spirit Shields", 45115, new int[]{56, 4}),
		MAKE_A_SPIRIT_SHIELD(Difficulty.ELITE, "Make a Spirit Shield", 45116, null),
		;

		AchievementData(Difficulty difficulty, String interfaceLine, int interfaceFrame, int[] progressData) {
			this.difficulty = difficulty;
			this.interfaceLine = interfaceLine;
			this.interfaceFrame = interfaceFrame;
			this.progressData = progressData;
		}

		private Difficulty difficulty;
		private String interfaceLine;
		private int interfaceFrame;
		private int[] progressData;

		public Difficulty getDifficulty() {
			return difficulty;
		}

		public static AchievementData getAchievementForButton(int button) {
			for(AchievementData achievementData : values()) {
				if(achievementData.interfaceFrame == button) {
					return achievementData;
				}
			}
			return null;
		}
	}

	public enum Difficulty {
		BEGINNER, EASY, MEDIUM, HARD, ELITE;
	}

	public static boolean handleButton(Player player, int button) {
		if (button >= 45120 || button <= 45004) {
			return false;
		}

		AchievementData achievement = AchievementData.getAchievementForButton(button);

		if(achievement == null) {
			player.sendMessage("Error loading achievement... Contact admin with code: "+button);
			return true;
		}

		if (player.getAchievementAttributes().getCompletion()[achievement.ordinal()]) {
			player.getPacketSender().sendMessage("<img=10> <col=660000>You have completed the achievement: " + achievement.interfaceLine + "!");
		} else if (achievement.progressData == null) {
			player.getPacketSender().sendMessage("<img=10> <col=660000>You have not completed the achievement: " + achievement.interfaceLine + ".");
		} else {
			int progress = player.getAchievementAttributes().getProgress()[achievement.progressData[0]];

			int requiredProgress = achievement.progressData[1];

			if (progress == 0) {
				player.getPacketSender().sendMessage("<img=10> <col=660000>You have not started the achievement: " + achievement.interfaceLine + ".");
			} else {
				player.getPacketSender().sendMessage("<img=10> <col=660000>" + achievement.interfaceLine + ": " + Misc.insertCommasToNumber("" + progress) + "/" + Misc.insertCommasToNumber("" + requiredProgress) + ".");
			}
		}
		
		return true;
	}

	public static void updateInterface(Player player) {
		for(AchievementData achievement : AchievementData.values()) {
			boolean completed = player.getAchievementAttributes().getCompletion()[achievement.ordinal()];
			boolean progress = achievement.progressData != null && player.getAchievementAttributes().getProgress()[achievement.progressData[0]] > 0;
			player.getPacketSender().sendString(achievement.interfaceFrame, (completed ? "@gre@" : progress ? "@yel@" : "@red@") + achievement.interfaceLine);
		}
		player.getPacketSender().sendString(45001, "Achievements: "+player.getPointsHandler().getAchievementPoints()+"/"+AchievementData.values().length);
	}
	
	public static void setPoints(Player player) {
		int points = 0;
		for(AchievementData achievement : AchievementData.values()) {
			if(player.getAchievementAttributes().getCompletion()[achievement.ordinal()]) {
				points++;
			}
		}
		player.getPointsHandler().setAchievementPoints(points, false);
	}

	public static void doProgress(Player player, AchievementData achievement) {
		doProgress(player, achievement, 1);
	}

	public static void doProgress(Player player, AchievementData achievement, int amt) {
		if(player.getAchievementAttributes().getCompletion()[achievement.ordinal()])
			return;
		if(achievement.progressData != null) {
			int progressIndex = achievement.progressData[0];
			int amountNeeded = achievement.progressData[1];
			int previousDone = player.getAchievementAttributes().getProgress()[progressIndex];
			if((previousDone+amt) < amountNeeded) {
				player.getAchievementAttributes().getProgress()[progressIndex] = previousDone+amt;
				if(previousDone == 0) 
					player.getPacketSender().sendString(achievement.interfaceFrame, "@yel@"+ achievement.interfaceLine);
			} else {
				finishAchievement(player, achievement);
			}
		}
	}

	public static void finishAchievement(Player player, AchievementData achievement) {
		if(player.getAchievementAttributes().getCompletion()[achievement.ordinal()])
			return;
		player.getAchievementAttributes().getCompletion()[achievement.ordinal()] = true;
		player.getPacketSender().sendString(achievement.interfaceFrame, ("@gre@") + achievement.interfaceLine).sendMessage("<img=10> <col=339900>You have completed the achievement "+Misc.formatText(achievement.toString().toLowerCase()+".")).sendString(45001, "Achievements: "+player.getPointsHandler().getAchievementPoints()+"/"+AchievementData.values().length);

		if(achievement.getDifficulty() == Difficulty.HARD) {
			doProgress(player, AchievementData.COMPLETE_ALL_HARD_TASKS);
		}
		
		player.getPointsHandler().setAchievementPoints(1, true);
	}

	public static class AchievementAttributes {

		public AchievementAttributes(){}

		/** ACHIEVEMENTS **/
		private boolean[] completed = new boolean[AchievementData.values().length];
		private int[] progress = new int[59];

		public boolean[] getCompletion() {
			return completed;
		}

		public void setCompletion(int index, boolean value) {
			this.completed[index] = value;
		}

		public void setCompletion(boolean[] completed) {
			this.completed = completed;
		}

		public int[] getProgress() {
			return progress;
		}

		public void setProgress(int index, int value) {
			this.progress[index] = value;
		}

		public void setProgress(int[] progress) {
			this.progress = progress;
		}

		/** MISC **/
		private int coinsGambled;
		private double totalLoyaltyPointsEarned;
		private boolean[] godsKilled = new boolean[5];

		public int getCoinsGambled() {
			return coinsGambled;
		}

		public void setCoinsGambled(int coinsGambled) {
			this.coinsGambled = coinsGambled;
		}

		public double getTotalLoyaltyPointsEarned() {
			return totalLoyaltyPointsEarned;
		}

		public void incrementTotalLoyaltyPointsEarned(double totalLoyaltyPointsEarned) {
			this.totalLoyaltyPointsEarned += totalLoyaltyPointsEarned;
		}

		public boolean[] getGodsKilled() {
			return godsKilled;
		}

		public void setGodKilled(int index, boolean godKilled) {
			this.godsKilled[index] = godKilled;
		}

		public void setGodsKilled(boolean[] b) {
			this.godsKilled = b;
		}
	}
}
