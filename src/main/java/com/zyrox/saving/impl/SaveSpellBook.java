package com.zyrox.saving.impl;

import com.zyrox.model.MagicSpellbook;
import com.zyrox.saving.SaveString;
import com.zyrox.world.entity.impl.player.Player;

public class SaveSpellBook extends SaveString {

	public SaveSpellBook(String name) {
		super(name);
	}

	@Override
	public void setValue(Player player, String value) {
		player.setSpellbook(MagicSpellbook.valueOf(value));
	}

	@Override
	public String getValue(Player player) {
		return player.getSpellbook().name();
	}

}
