package com.varrock.saving.impl;

import com.varrock.engine.task.impl.FamiliarSpawnTask;
import com.varrock.saving.SaveInteger;
import com.varrock.world.entity.impl.player.Player;

public class SaveSummonNpc extends SaveInteger {

	public SaveSummonNpc(String name) {
		super(name);
	}

	@Override
	public void setValue(Player player, int value) {
		if (value > 0)
			player.getSummoning().setFamiliarSpawnTask(new FamiliarSpawnTask(player)).setFamiliarId(value);
	}

	@Override
	public Integer getValue(Player player) {
		return player.getSummoning().getFamiliar() != null ? player.getSummoning().getFamiliar().getSummonNpc().getId() : -1;
	}

	@Override
	public int getDefaultValue() {
		return 0;
	}

}
