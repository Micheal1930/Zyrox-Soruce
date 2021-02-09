package com.zyrox.model.input.impl;

import com.zyrox.model.container.impl.Bank.BankSearchAttributes;
import com.zyrox.model.input.Input;
import com.zyrox.world.entity.impl.player.Player;

public class EnterSyntaxToBankSearchFor extends Input {

	@Override
	public void handleSyntax(Player player, String syntax) {
		if (syntax.length() < 1) {
			player.getPacketSender().sendInterfaceRemoval();
			player.sendMessage("Invalid syntax.");
			return;
		}
		
		boolean searchingBank = player.isBanking() && player.getBankSearchingAttribtues().isSearchingBank();
		
		if (searchingBank) {
			BankSearchAttributes.beginSearch(player, syntax);
		}
	}
}
