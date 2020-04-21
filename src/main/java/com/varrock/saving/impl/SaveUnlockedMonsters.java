package com.varrock.saving.impl;

import java.util.List;

import com.varrock.saving.SaveUnlockedMonstersList;
import com.varrock.world.content.donator_boss.DonatorBossMonster;
import com.varrock.world.entity.impl.player.Player;

public class SaveUnlockedMonsters extends SaveUnlockedMonstersList {

	/**
	 * Constructs a new SaveContainer.
	 *
	 * @param name
	 */
	public SaveUnlockedMonsters(String name) {
		super(name);
	}

	@Override
	public List<DonatorBossMonster> getMonsterList(Player player) {
		return player.donatorBossMonstersUnlocked;
	}

	@Override
	public void loadBossMonster(Player player, DonatorBossMonster monster) {
		if(monster != null)
			player.donatorBossMonstersUnlocked.add(monster);
	}
}

