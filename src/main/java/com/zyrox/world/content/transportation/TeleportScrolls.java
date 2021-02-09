package com.zyrox.world.content.transportation;

import com.zyrox.model.Position;
import com.zyrox.world.entity.impl.player.Player;

public enum TeleportScrolls {

	TAI_BWO_WANNAI(42409, new Position(2789, 3066, 0)),
	MOS_LE_HARMLESS(42411, new Position(3681, 2975, 0)),
	REVENANT_CAVE(51802, new Position(3133, 3833, 0)),
	LAVA_DRAGONS(51803, new Position(3202, 3859, 0));

	TeleportScrolls(int id, Position location) {
		this.id = id;
		this.location = location;
	}
	private int id;
	private Position location;

	public int getId() {
		return id;
	}

	public Position getPosition() {
		return location;
	}

	public static boolean handle(Player player, int itemId, int slot) {
		TeleportScrolls scroll = forId(itemId);
		if (scroll != null && TeleportHandler.checkReqs(player, player.getPosition())) {
			if (TeleportHandler.teleportPlayer(player, scroll.getPosition(), TeleportType.SCROLL)){
				return true;
			}
		}
		return false;
	}

	public static TeleportScrolls forId(int id) {
		for (TeleportScrolls scroll : TeleportScrolls.values()) {
			if (scroll.id == id)
				return scroll;
		}
		return null;
	}

}
