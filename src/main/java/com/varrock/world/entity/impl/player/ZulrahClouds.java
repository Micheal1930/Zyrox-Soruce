package com.varrock.world.entity.impl.player;

import com.varrock.engine.task.Task;
import com.varrock.model.CombatIcon;
import com.varrock.model.GameObject;
import com.varrock.model.Hit;
import com.varrock.model.Hitmask;
import com.varrock.model.Locations;
import com.varrock.model.Position;
import com.varrock.model.Locations.Location;
import com.varrock.util.Misc;
import com.varrock.world.content.CustomObjects;
import com.varrock.world.entity.impl.npc.NPC;

public class ZulrahClouds extends Task {

	public ZulrahClouds(Player player) {
		this.player = player;
	}

	final Player player;

	public static NPC ZULRAH;
	public static NPC CLOUD1;
	public static NPC CLOUD2;
	public static NPC CLOUD3;
	public static NPC CLOUD4;
	public static NPC CLOUD5;
	public static NPC CLOUD6;
	public static NPC CLOUD7;
	public static NPC CLOUD8;
	
	
	private int[][] toxicFumeLocations = { { 2263, 3076 }, { 2263, 3073 }, { 2263, 3070 }, { 2266, 3069 },
			{ 2269, 3069 }, { 2272, 3070 }, { 2273, 3073 }, { 2273, 3076 } };
	
	private NPC[] CloudTiles = {CLOUD1, CLOUD2, CLOUD3, CLOUD4, CLOUD5, CLOUD6, CLOUD7, CLOUD8 };
	private Location[] CloudLocations = { Locations.Location.ZULRAH_CLOUD_ONE,
										  Locations.Location.ZULRAH_CLOUD_TWO,
										  Locations.Location.ZULRAH_CLOUD_THREE,
									  	  Locations.Location.ZULRAH_CLOUD_FOUR,
										  Locations.Location.ZULRAH_CLOUD_FIVE,
										  Locations.Location.ZULRAH_CLOUD_SIX,
										  Locations.Location.ZULRAH_CLOUD_SEVEN,
										  Locations.Location.ZULRAH_CLOUD_EIGHT};
	
	public void tiles() {
		for(int i = 0; i < 8; i++) {
			CloudTiles[i] = NPC.of(1, new Position(toxicFumeLocations[i][0], toxicFumeLocations[i][1], player.getIndex() * 4));
		}
	}

	
	@Override
	public void execute() {
		tiles();
		if(player == null || !player.isRegistered()) {
			stop();
			return;
		}
			
		if(player.cloudsSpawned()) {
			for(int i = 0; i<8; i++) {
				if(CloudLocations[i] == player.getLocation()) {
					for (GameObject objects : CustomObjects.CUSTOM_OBJECTS) {
						int objX = objects.getPosition().getX();
						int objY = objects.getPosition().getY();
						if(GameObject.inLocation(objX, objY, CloudLocations[i])) {
							player.dealDamage(new Hit(Misc.getRandom(4)*10, Hitmask.DARK_GREEN, CombatIcon.NONE));
						}	
					}
				}
			}
		}
	}
}