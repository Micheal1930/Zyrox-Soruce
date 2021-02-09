package com.zyrox.model.input.impl;

import com.zyrox.model.input.EnterAmount;
import com.zyrox.world.content.skill.impl.smithing.Smelting;
import com.zyrox.world.content.skill.impl.smithing.SmithingData;
import com.zyrox.world.entity.impl.player.Player;

public class EnterAmountOfBarsToSmelt extends EnterAmount {

	public EnterAmountOfBarsToSmelt(int bar) {
		this.bar = bar;
	}
	
	@Override
	public boolean handleAmount(Player player, int amount) {
		for(int barId : SmithingData.SMELT_BARS) {
			if(barId == bar) {
				Smelting.smeltBar(player, barId, amount);
				break;
			}
		}
		return false;
	}
	
	private int bar;
	
	public int getBar() {
		return bar;
	}
	
}
