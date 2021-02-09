package com.zyrox.world.content;

import com.zyrox.model.Animation;
import com.zyrox.model.GameObject;
import com.zyrox.model.Position;
import com.zyrox.util.Misc;
import com.zyrox.util.Stopwatch;
import com.zyrox.world.World;
import com.zyrox.world.entity.impl.player.Player;

public class ShootingStar {

	private static final int TIME = 1000000;
	                              
	public static final int MAXIMUM_MINING_AMOUNT = 800;
	
	private static Stopwatch timer = new Stopwatch().reset();
	public static CrashedStar CRASHED_STAR = null;
	private static LocationData LAST_LOCATION = null;
	
	public static class CrashedStar {
		
		public CrashedStar(GameObject starObject, LocationData starLocation) {
			this.starObject = starObject;
			this.starLocation = starLocation;
		}
		
		private GameObject starObject;
		private LocationData starLocation;
		
		public GameObject getStarObject() {
			return starObject;
		}
		
		public LocationData getStarLocation() {
			return starLocation;
		}
	}

	public static enum LocationData {
		LOCATION_1(new Position(3053, 3301), "in the Falador Farming patches", "Farming"),
		LOCATION_2(new Position(3091, 3483), "south of the Edgeville bank", "Edgeville"),
		LOCATION_3(new Position(2480, 3433), "at the Gnome Agility Course", "Gnome Course"),
		LOCATION_4(new Position(2745, 3445), "in the middle of the Flax field", "Flax Field"),
		LOCATION_5(new Position(3206, 3269), "in the yak field", "Yak Field"),
		LOCATION_6(new Position(2594, 4326), "in Puro Puro", "Puro Puro"),
		LOCATION_7(new Position(3368, 3269), "in the Duel Arena", "Duel Arena"),
		LOCATION_8(new Position(1746, 5327), "in the Ancient cavern", "Ancient Cavern"),
		LOCATION_9(new Position(2882, 9800), "in the Taverly dungeon", "Taverly Dung."),
		LOCATION_10(new Position(2666, 2648), "at the Void knight island", "Pest Control"),
		LOCATION_11(new Position(3566, 3297), "on the Barrows hills", "Barrows"),
		LOCATION_12(new Position(2986, 3599), "in the Wilderness (West dragons)", "West Dragons"),
		LOCATION_13(new Position(3091, 3528), "in the Wilderness (Edgeville)", "Edgeville Wild"),
		LOCATION_14(new Position(2582, 4844), "near the Runecrafting Fire altar", "Fire Altar"),
		LOCATION_15(new Position(3478, 4834), "near the Runecrafting Water altar", "Water Altar"),
		LOCATION_16(new Position(2793, 4830), "near the Runecrafting Mind altar", "Mind Altar"),
		LOCATION_17(new Position(2846, 4829), "near the Runecrafting Air altar", "Air Altar"),
		LOCATION_18(new Position(2995, 3911), "near the Wilderness Agility Course", "Wild. Course");

		private LocationData(Position spawnPos, String clue, String playerPanelFrame) {
			this.spawnPos = spawnPos;
			this.clue = clue;
			this.playerPanelFrame = playerPanelFrame;
		}

		private Position spawnPos;
		private String clue;
		public String playerPanelFrame;
	}

	public static LocationData getRandom() {
		LocationData star = LocationData.values()[Misc.getRandom(LocationData.values().length - 1)];
		return star;
	}

	public static void sequence() {
		if(CRASHED_STAR == null) {
			if(timer.elapsed(TIME)) {
				LocationData locationData = getRandom();
				if(LAST_LOCATION != null) {
					if(locationData == LAST_LOCATION) {
						locationData = getRandom();
					}
				}
				LAST_LOCATION = locationData;
				CRASHED_STAR = new CrashedStar(new GameObject(38660, locationData.spawnPos), locationData);
				CustomObjects.spawnGlobalObject(CRASHED_STAR.starObject);
				World.sendMessage("<img=733> <shad=FFA500>A Shooting Star has just crashed "+locationData.clue+"!</col>");
				World.getPlayers().forEach(p -> p.getPacketSender().sendString(26703, "@or2@Crashed star: @gre@"+ShootingStar.CRASHED_STAR.getStarLocation().playerPanelFrame+""));
				timer.reset();
			}
		} else {
			if(CRASHED_STAR.starObject.getPickAmount() >= MAXIMUM_MINING_AMOUNT) {
				despawn(false);
				timer.reset();
			}
		}
	}

	public static void despawn(boolean respawn) {
		if(respawn) {
			timer.reset(0);
		} else {
			timer.reset();
		}
		if(CRASHED_STAR != null) {
			for(Player p : World.getPlayers()) {
				if(p == null) {
					continue;
				}
				p.getPacketSender().sendString(26703, "@or2@Crashed star: @gre@N/A ");
				if(p.getInteractingObject() != null && p.getInteractingObject().getId() == CRASHED_STAR.starObject.getId()) {
					p.performAnimation(new Animation(65535));
					p.getPacketSender().sendClientRightClickRemoval();
					p.getSkillManager().stopSkilling();
					p.getPacketSender().sendMessage("The star has been fully mined.");
				}
			}
			CustomObjects.deleteGlobalObject(CRASHED_STAR.starObject);
			CRASHED_STAR = null;
		}
	}
}
