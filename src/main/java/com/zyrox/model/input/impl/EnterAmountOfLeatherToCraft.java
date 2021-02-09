package com.zyrox.model.input.impl;

import com.zyrox.model.input.EnterAmount;
import com.zyrox.world.content.skill.impl.crafting.LeatherData;
import com.zyrox.world.content.skill.impl.crafting.LeatherMaking;
import com.zyrox.world.entity.impl.player.Player;

public class EnterAmountOfLeatherToCraft extends EnterAmount {
	
	@Override
	public boolean handleAmount(Player player, int amount) {
		for (final LeatherData l : LeatherData.values()) {
			if (player.getSelectedSkillingItem() == l.getLeather()) {
				LeatherMaking.craftLeather(player, l, amount);
				break;
			}
		}
		return false;
	}
}
