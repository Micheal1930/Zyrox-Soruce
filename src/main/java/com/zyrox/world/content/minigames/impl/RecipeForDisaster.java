package com.zyrox.world.content.minigames.impl;

import com.zyrox.engine.task.Task;
import com.zyrox.engine.task.TaskManager;
import com.zyrox.model.Item;
import com.zyrox.model.Position;
import com.zyrox.model.RegionInstance;
import com.zyrox.model.Locations.Location;
import com.zyrox.model.RegionInstance.RegionInstanceType;
import com.zyrox.world.World;
import com.zyrox.world.content.PlayerPanel;
import com.zyrox.world.content.combat.prayer.CurseHandler;
import com.zyrox.world.content.combat.prayer.PrayerHandler;
import com.zyrox.world.content.dialogue.DialogueManager;
import com.zyrox.world.content.shop.ShopManager;
import com.zyrox.world.entity.impl.npc.NPC;
import com.zyrox.world.entity.impl.player.Player;

/**
 * @author Gabriel Hannason
 * Wrote this quickly!!
 * Handles the RFD quest
 */
public class RecipeForDisaster {

	public static void enter(Player player) {
		if(player.getMinigameAttributes().getRecipeForDisasterAttributes().getWavesCompleted() == 6)
			return;
		player.moveTo(new Position(1900, 5346, player.getIndex() * 4 + 2));
		player.setRegionInstance(new RegionInstance(player, RegionInstanceType.RECIPE_FOR_DISASTER));
		spawnWave(player, player.getMinigameAttributes().getRecipeForDisasterAttributes().getWavesCompleted());
		CurseHandler.deactivateAll(player); PrayerHandler.deactivateAll(player);
	}
	
	public static void leave(Player player) {
		Location.RECIPE_FOR_DISASTER.leave(player);
	}

	public static void spawnWave(final Player p, final int wave) {
		if(wave > 5 || p.getRegionInstance() == null)
			return;
		TaskManager.submit(new Task(2, p, false) {
			@Override
			public void execute() {
				if(p.getRegionInstance() == null) {
					stop();
					return;
				}
				int npc = wave >= 5 ? 3491 : 3493 + wave;
				NPC n = NPC.of(npc, new Position(spawnPos.getX(), spawnPos.getY(), p.getPosition().getZ())).setSpawnedFor(p);
				World.register(n);
				p.getRegionInstance().getNpcsList().add(n);	
				n.getCombatBuilder().attack(p);
				stop();
			}
		});
	}

	public static void handleNPCDeath(final Player player, NPC n) {
		if(player.getRegionInstance() == null)
			return;
		player.getRegionInstance().getNpcsList().remove(n);
		player.getMinigameAttributes().getRecipeForDisasterAttributes().setWavesCompleted(player.getMinigameAttributes().getRecipeForDisasterAttributes().getWavesCompleted() + 1);
		switch(n.getId()) {
		case 3493:
		case 3494:
		case 3495:
		case 3496:
		case 3497:
			int index = n.getId() - 3490;
			player.getMinigameAttributes().getRecipeForDisasterAttributes().setPartFinished(index, true);
			break;
		case 3491:
			player.getMinigameAttributes().getRecipeForDisasterAttributes().setPartFinished(8, true);
			player.moveTo(new Position(3081, 3500, 0));
			player.restart();
			DialogueManager.start(player, 46);
			PlayerPanel.refreshPanel(player);
			break;
		}
		if(player.getLocation() != Location.RECIPE_FOR_DISASTER || player.getMinigameAttributes().getRecipeForDisasterAttributes().getWavesCompleted() == 6)
			return;
		TaskManager.submit(new Task(3, player, false) {
			@Override
			public void execute() {
				stop();
				if(player.getLocation() != Location.RECIPE_FOR_DISASTER || player.getMinigameAttributes().getRecipeForDisasterAttributes().getWavesCompleted() == 6)
					return;
				spawnWave(player, player.getMinigameAttributes().getRecipeForDisasterAttributes().getWavesCompleted());
			}
		});
	}

	public static String getQuestTabPrefix(Player player) {
		if(player.getMinigameAttributes().getRecipeForDisasterAttributes().hasFinishedPart(0) && player.getMinigameAttributes().getRecipeForDisasterAttributes().getWavesCompleted() < 6) {
			return "@yel@";
		}
		if(player.getMinigameAttributes().getRecipeForDisasterAttributes().getWavesCompleted() == 6) {
			return "@gre@";
		}
		return "@red@";
	}
	
	public static void openQuestLog(Player p) {
		for(int i = 8145; i < 8196; i++)
			p.getPacketSender().sendString(i, "");
		p.getPacketSender().sendInterface(8134);
		p.getPacketSender().sendString(8136, "Close window");
		p.getPacketSender().sendString(8144, ""+questTitle);
		p.getPacketSender().sendString(8145, "");
		int questIntroIndex = 0;
		for(int i = 8147; i < 8147+questIntro.length; i++) {
			p.getPacketSender().sendString(i, "@dre@"+questIntro[questIntroIndex]);
			questIntroIndex++;
		}
		int questGuideIndex = 0;
		for(int i = 8147+questIntro.length; i < 8147+questIntro.length+questGuide.length; i++) {
			p.getPacketSender().sendString(i, "@str@"+questGuide[questGuideIndex]+"");
			if(questGuideIndex == 2) {
				if(p.getMinigameAttributes().getRecipeForDisasterAttributes().getWavesCompleted() > 0 && !p.getMinigameAttributes().getRecipeForDisasterAttributes().hasFinishedPart(questGuideIndex))
					p.getPacketSender().sendString(i, "@yel@"+questGuide[questGuideIndex]);
				if(p.getMinigameAttributes().getRecipeForDisasterAttributes().getWavesCompleted() == 6)
					p.getPacketSender().sendString(i, "@str@"+questGuide[questGuideIndex]+"");
			}
			questGuideIndex++;
		}
		if(p.getMinigameAttributes().getRecipeForDisasterAttributes().getWavesCompleted() == 6)
			p.getPacketSender().sendString(8147+questIntro.length+questGuide.length, "@dre@Quest complete!");
	}

	public static void openRFDShop(final Player player) {
		ShopManager.openShop(player, 25);
	}
	
	public static Item[] getChestItems(Player player) {
		Item[] items = new Item[10];
		
		for(int i = 0; i <= player.getMinigameAttributes().getRecipeForDisasterAttributes().getWavesCompleted(); i++) {
			switch(i) {
			case 1:
				items[0] = new Item(7453);
				break;
			case 2:
				items[1] = new Item(7454);
				items[2] = new Item(7455);
				break;
			case 3:
				items[3] = new Item(7456);
				items[4] = new Item(7457);
				break;
			case 4:
				items[5] = new Item(7458);
				break;
			case 5:
				items[6] = new Item(7459);
				items[7] = new Item(7460);
				break;
			case 6:
				items[8] = new Item(7461);
				items[9] = new Item(7462);
				break;
			}
		}
		
		
		return items;
	}


	private static final Position spawnPos = new Position(1900, 5354);
	private static final String questTitle = "Recipe for Disaster";
	private static final String[] questIntro ={
		"The Culinaromancer has returned and only you", 
		"             can stop him!                  ",
		"",
	};
	private static final String[] questGuide ={
		"Find recipe for disaster in the minigame teleports.",
		"Talk to the gypsy & agree to help her.",
		"Enter the portal.",
		"Defeat the following servants:",
		"* Agrith-Na-Na",
		"* Flambeed",
		"* Karamel",
		"* Dessourt",
		"* Gelatinnoth mother",
		"And finally.. Defeat the Culinaromancer!"
	};
}
