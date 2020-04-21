package com.varrock.saving.impl;

import com.varrock.saving.SaveLong;
import com.varrock.world.entity.impl.player.Player;

public class SaveTotalExperience extends SaveLong {

	public SaveTotalExperience(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public long getDefaultValue() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setValue(Player player, long value) {
		player.getSkillManager().setTotalGainedExp(value);
	}

	@Override
	public Long getValue(Player player) {
		return player.getSkillManager().getTotalExp();
	}

}
