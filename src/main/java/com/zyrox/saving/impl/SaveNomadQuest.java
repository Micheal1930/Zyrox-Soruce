package com.zyrox.saving.impl;

import com.zyrox.saving.SaveBooleanArray;
import com.zyrox.world.entity.impl.player.Player;

public class SaveNomadQuest extends SaveBooleanArray {

	public SaveNomadQuest(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean[] getBooleanArray(Player player) {
		// TODO Auto-generated method stub
		return player.getMinigameAttributes().getNomadAttributes().getQuestParts();
	}

	@Override
	public void setBooleanValue(Player player, int index, boolean value) {
		// TODO Auto-generated method stub
		player.getMinigameAttributes().getNomadAttributes().setPartFinished(index, value);

	}

}
