package com.varrock.model.input.impl;

import com.varrock.model.input.EnterAmount;
import com.varrock.world.content.skill.impl.crafting.LeatherData;
import com.varrock.world.content.skill.impl.crafting.LeatherMaking;
import com.varrock.world.entity.impl.player.Player;

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
