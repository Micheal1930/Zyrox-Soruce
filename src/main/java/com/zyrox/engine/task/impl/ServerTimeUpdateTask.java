package com.zyrox.engine.task.impl;

import com.zyrox.engine.task.Task;
import com.zyrox.model.Locations;
import com.zyrox.util.RandomUtility;
import com.zyrox.world.World;
import com.zyrox.world.content.minigames.impl.PestControl;

/**
 * @author Gabriel Hannason
 */
public class ServerTimeUpdateTask extends Task {

	public ServerTimeUpdateTask() {
		super(40);
	}

	private int tick = 0;

	@Override
	protected void execute() {
		World.updateServerTime();
		
		if(tick >= 10 && (Locations.PLAYERS_IN_WILD >= 5 || Locations.PLAYERS_IN_DUEL_ARENA >= 5 || PestControl.TOTAL_PLAYERS >= 5)) {
			if(Locations.PLAYERS_IN_WILD > Locations.PLAYERS_IN_DUEL_ARENA && Locations.PLAYERS_IN_WILD > PestControl.TOTAL_PLAYERS || RandomUtility.getRandom(3) == 1 && Locations.PLAYERS_IN_WILD >= 2) {
				World.sendMessage("<img=484> <col=E9E919><shad=0>[ Hotspot ]:</col> <col=E9E919><shad=0> There are currently "+World.getWildernessCount()+" players roaming the Wilderness!</col>");
			} else if(Locations.PLAYERS_IN_DUEL_ARENA > Locations.PLAYERS_IN_WILD && Locations.PLAYERS_IN_DUEL_ARENA > PestControl.TOTAL_PLAYERS) {
				World.sendMessage("<img=486> <col=E9E919><shad=0>[ Hotspot ]:</col> <col=E9E919><shad=0> There are currently "+Locations.PLAYERS_IN_DUEL_ARENA+" players at the Duel Arena!</col>");
			} else if(PestControl.TOTAL_PLAYERS > Locations.PLAYERS_IN_WILD && PestControl.TOTAL_PLAYERS > Locations.PLAYERS_IN_DUEL_ARENA) {
				World.sendMessage("<img=40> <col=E9E919><shad=0>[ Hotspot ]:</col> <col=E9E919><shad=0> There are currently "+PestControl.TOTAL_PLAYERS+" players at Pest Control!</col>");
			}
			tick = 0;
		}

		tick++;
	}
}