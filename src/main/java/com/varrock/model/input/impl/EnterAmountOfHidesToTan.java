package com.varrock.model.input.impl;

import com.varrock.model.input.EnterAmount;
import com.varrock.world.content.skill.impl.crafting.Tanning;
import com.varrock.world.entity.impl.player.Player;

public class EnterAmountOfHidesToTan extends EnterAmount {

	private int button;
	public EnterAmountOfHidesToTan(int button) {
		this.button = button;
	}
	
	@Override
	public boolean handleAmount(Player player, int amount) {
		Tanning.tanHide(player, button, amount);
		return false;
	}

}
