package com.varrock.saving.impl;

import com.varrock.saving.SaveBoolean;
import com.varrock.world.entity.impl.player.Player;

public class SaveHidePlayer extends SaveBoolean {

	public SaveHidePlayer(String name) {
		super(name);
	}

	@Override
	public void setValue(Player player, boolean value) {
		player.getMinigameAttributes().getGodwarsDungeonAttributes().setHasEnteredRoom(value);
	}

	@Override
	public Boolean getValue(Player player) {
		return player.getMinigameAttributes().getGodwarsDungeonAttributes().hasEnteredRoom();
	}

	@Override
	public boolean getDefaultValue() {
		return false;
	}


}
