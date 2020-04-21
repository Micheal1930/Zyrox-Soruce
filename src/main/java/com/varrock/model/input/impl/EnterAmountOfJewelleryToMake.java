package com.varrock.model.input.impl;

import com.varrock.model.input.EnterAmount;
import com.varrock.world.content.skill.impl.crafting.jewellery.JewelleryMaking;
import com.varrock.world.entity.impl.player.Player;

public class EnterAmountOfJewelleryToMake extends EnterAmount {

	private JewelleryMaking.GemData gemData;
	private int index;

	public EnterAmountOfJewelleryToMake() {

	}

	@Override
	public boolean handleAmount(Player player, int amount) {
		JewelleryMaking.make(player, amount, gemData.getJewllery()[index], gemData);
		return false;
	}

	public boolean handleAmount(Player player, int amount, int buttonId) {

		int index = (buttonId >= 8906 && buttonId <= 8910) || (buttonId >= 8871 && buttonId <= 8874) || buttonId == 49853 ? 0
				: (buttonId >= 8875 && buttonId <= 8878) || (buttonId >= 8910 && buttonId <= 8913) || buttonId == 49854 ? 1
				: (buttonId >= 8914 && buttonId <= 8917) ? 2
				: (buttonId >= 8918 && buttonId <= 8921) ? 3 : 0;

		amount = buttonId == 8908 || buttonId == 8912 || buttonId == 8916 || buttonId == 8920 ? 5
				: buttonId == 8907 || buttonId == 8911 || buttonId == 8915 || buttonId == 8919 ? 10 : 1;

		boolean amountX = buttonId == 8906 || buttonId == 8910 || buttonId == 8914 || buttonId == 8918 || buttonId == 8875 || buttonId == 8871;
		boolean makeAll = buttonId == 49853 || buttonId == 49854;

		this.gemData = JewelleryMaking.GemData.getGemData(player.getSelectedSkillingItem());
		this.index = index;

		if(amountX) {
			player.setInputHandling(new EnterAmount() {
				@Override
				public boolean handleAmount(Player player, int amount) {
					JewelleryMaking.make(player, amount, gemData.getJewllery()[index], gemData);
					return false;
				}
			});
			player.getPacketSender().sendEnterAmountPrompt("How many would you like to make?");
		} else {
			JewelleryMaking.make(player, makeAll ? player.getInventory().getAmount(gemData.getGemId()) : amount,
					gemData.getJewllery()[index], gemData);
		}
		return true;
	}
}
