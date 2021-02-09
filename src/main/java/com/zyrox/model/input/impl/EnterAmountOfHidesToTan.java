package com.zyrox.model.input.impl;

import com.zyrox.model.input.EnterAmount;
import com.zyrox.world.content.skill.impl.crafting.Tanning;
import com.zyrox.world.entity.impl.player.Player;

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
