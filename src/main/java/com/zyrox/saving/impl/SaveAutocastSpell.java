package com.zyrox.saving.impl;

import com.zyrox.saving.SaveInteger;
import com.zyrox.world.content.combat.magic.CombatSpells;
import com.zyrox.world.entity.impl.player.Player;

public class SaveAutocastSpell extends SaveInteger {

	public SaveAutocastSpell(String name) {
		super(name);
	}

	@Override
	public void setValue(Player player, int value) {
		if (value != -1)
			player.setAutocastSpell(CombatSpells.getSpell(value));
	}

	@Override
	public Integer getValue(Player player) {
		return player.getAutocastSpell() != null ? player.getAutocastSpell().spellId() : -1;
	}

	@Override
	public int getDefaultValue() {
		return 0;
	}

}
