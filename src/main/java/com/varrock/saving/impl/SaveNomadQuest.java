package com.varrock.saving.impl;

import com.varrock.saving.SaveBooleanArray;
import com.varrock.world.entity.impl.player.Player;

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
