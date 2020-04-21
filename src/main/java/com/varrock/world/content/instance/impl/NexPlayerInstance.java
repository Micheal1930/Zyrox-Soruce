package com.varrock.world.content.instance.impl;

import com.varrock.engine.task.Task;
import com.varrock.model.Position;
import com.varrock.world.content.combat.strategy.impl.Nex;
import com.varrock.world.content.instance.PlayerInstance;
import com.varrock.world.entity.impl.npc.NPC;
import com.varrock.world.entity.impl.player.Player;

/**
 * Handles the nex instance
 * 
 * @author 2012
 *
 */
public class NexPlayerInstance extends PlayerInstance {
	
	/**
	 * The player spawn position
	 */
	private static final Position PLAYER_SPAWN_POSITION = new Position(2911, 5203, 0);

	/**
	 * Creates the nex instance
	 * @param player the player
	 */
	public NexPlayerInstance(Player player) {
		super(player);
	}

	@Override
	public void start(Player player) {
		Nex nex = new Nex();
		
		player.nexInstance = nex;
		
		nex.spawn(getHeightLevel());
		
		for(NPC npc : nex.getNpcList()) {
			getNpcs().add(npc);
		}
	}

	@Override
	public Task getTask(Player player) {
		return null;
	}

	@Override
	public void end(Player player) {
		
	}

	@Override
	public Position getPlayerSpawnPosition() {
		return PLAYER_SPAWN_POSITION;
	}

	@Override
	public boolean canTeleportOut() {
		return true;
	}

	@Override
	public int[] getRegionIds() {
		return new int[] {11601};
	}
}
