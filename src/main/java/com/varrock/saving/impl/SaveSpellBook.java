package com.varrock.saving.impl;

import com.varrock.model.MagicSpellbook;
import com.varrock.saving.SaveString;
import com.varrock.world.entity.impl.player.Player;

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
