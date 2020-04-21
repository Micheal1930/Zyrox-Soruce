package com.varrock.saving.impl;

import com.varrock.saving.SaveString;
import com.varrock.world.content.combat.weapon.FightType;
import com.varrock.world.entity.impl.player.Player;

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
