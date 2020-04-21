package com.varrock.model.input.impl;

import com.varrock.model.input.EnterAmount;
import com.varrock.world.content.minigames.impl.Dueling;
import com.varrock.world.entity.impl.player.Player;

public class EnterAmountToRemoveFromStake extends EnterAmount {

	public EnterAmountToRemoveFromStake(int item) {
		super(item);
	}
	
	@Override
	public boolean handleAmount(Player player, int amount) {
		if((Dueling.checkDuel(player, 1) || Dueling.checkDuel(player, 2)) && getItem() > 0) {
			player.getDueling().removeStakedItem(getItem(), amount);
		} else
			player.getPacketSender().sendInterfaceRemoval();

		return false;
	}

}
