package com.varrock.saving;

import java.util.ArrayList;
import java.util.List;

import com.varrock.model.Item;
import com.varrock.model.container.impl.Bank;
import com.varrock.world.entity.impl.player.Player;

public class SaveBankTab extends SaveItemList {
	
	private int tab;

	public SaveBankTab(String name, int tab) {
		super(name);
		this.tab = tab;
		// TODO Auto-generated constructor stub
	}

	@Override
	public List<Item> getItemList(Player player) {
		// TODO Auto-generated method stub
		if(player.getBank(tab) != null) {
			return player.getBank(tab).getValidItems();
		} else {
			return new ArrayList<Item>();
		}
	}

	@Override
	public void loadItem(Player player, Item item, int slot) {
		// TODO Auto-generated method stub
		if(player.getBank(tab) == null) {
			player.setBank(tab, new Bank(player));
		}
		player.getBank(tab).add(item, false);
	}

}
