package com.varrock.saving.impl;

import com.varrock.saving.SaveLong;
import com.varrock.world.entity.impl.player.Player;

public class SaveLastVeng extends SaveLong {

	public SaveLastVeng(String name) {
		super(name);
	}

	@Override
	public long getDefaultValue() {
		return 0;
	}

	@Override
	public void setValue(Player player, long value) {
		player.getLastVengeance().reset(value);
	}

	@Override
	public Long getValue(Player player) {
		return player.getLastVengeance().elapsed();
	}

}
