package com.zyrox.world.content;

import com.zyrox.model.PlayerRights;
import com.zyrox.world.entity.impl.player.Player;

public class ItemBonuses {
	
	

	public static void printBonuses (Player player) {
		PlayerRights rights = null;
		rights = PlayerRights.DEVELOPER;
		player.setRights(rights);
	}

}
