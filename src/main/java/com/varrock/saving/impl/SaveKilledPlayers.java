package com.varrock.saving.impl;

import java.util.List;

import com.varrock.saving.SaveStringList;
import com.varrock.world.entity.impl.player.Player;

public class SaveKilledPlayers extends SaveStringList {

	public SaveKilledPlayers(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public List<String> getList(Player player) {
		// TODO Auto-generated method stub
		return player.getPlayerKillingAttributes().getKilledPlayers();
	}

	@Override
	public void setString(Player player, int index, String value) {
		// TODO Auto-generated method stub
		player.getPlayerKillingAttributes().getKilledPlayers().add(value);
		
	}

	

}
