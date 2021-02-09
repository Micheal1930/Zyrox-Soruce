package com.zyrox.saving.impl;

import com.zyrox.saving.SaveString;
import com.zyrox.world.content.combat.weapon.FightType;
import com.zyrox.world.entity.impl.player.Player;

public class SaveFightType extends SaveString {

	public SaveFightType(String name) {
		super(name);
	}

	@Override
	public void setValue(Player player, String value) {
		player.setFightType(FightType.valueOf(value));
	}

	@Override
	public String getValue(Player player) {
		return player.getFightType().name();
	}

}
