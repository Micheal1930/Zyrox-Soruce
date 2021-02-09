package com.zyrox.world.content.instance.impl;

import com.zyrox.engine.task.Task;
import com.zyrox.model.Position;
import com.zyrox.world.World;
import com.zyrox.world.content.dialogue.DialogueManager;
import com.zyrox.world.content.instance.PlayerInstance;
import com.zyrox.world.content.instance.PlayerInstanceManager;
import com.zyrox.world.entity.impl.npc.NPC;
import com.zyrox.world.entity.impl.player.Player;

/**
 * Handles the Corporeal beast instance
 * 
 * @author 2012
 *
 */
public class CorporealBeastPlayerInstance extends PlayerInstance {

	/**
	 * Corporeal beast id
	 */
	private static final int CORPOREAL_BEAST = 8133;

	/**
	 * The player spawn position
	 */
	private static final Position PLAYER_SPAWN = new Position(2886, 4376, 0);

	/**
	 * The corporeal beast spawn position
	 */
	private static final Position CORPOREAL_BEAST_SPAWN = new Position(2902, 4394, 0);

	/**
	 * The dialogue action id
	 */
	public static final int DIALOGUE_ACTION = 1_292_111;

	/**
	 * Send the dialogue
	 * @param player
	 */
	public static void sendDialogue(Player player) {
		DialogueManager.start(player, 131);
		player.setDialogueActionId(DIALOGUE_ACTION);
	}
	
	/**
	 * Corporeal beast instance
	 * @param player the player
	 */
	public CorporealBeastPlayerInstance(Player player) {
		super(player);
	}

	@Override
	public void start(Player player) {
		
		Position pos = CORPOREAL_BEAST_SPAWN.copy();
		
		PlayerInstanceManager.removeAllNpcsOnHeight(CORPOREAL_BEAST, getHeightLevel());
		
		pos.setZ(getHeightLevel());
		NPC corp = NPC.of(CORPOREAL_BEAST, pos);
		
		World.register(corp);
		
		getNpcs().add(corp);
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
		return PLAYER_SPAWN;
	}

	@Override
	public boolean canTeleportOut() {
		return true;
	}

	@Override
	public int[] getRegionIds() {
		return new int[] {11588};
	}
}
