package com.varrock.saving.impl;

import com.varrock.saving.SaveInteger;
import com.varrock.world.content.combat.magic.CombatSpells;
import com.varrock.world.entity.impl.player.Player;

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
