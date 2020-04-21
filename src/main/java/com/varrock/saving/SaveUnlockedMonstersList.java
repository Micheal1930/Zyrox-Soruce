package com.varrock.saving;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;

import com.varrock.world.content.donator_boss.DonatorBossMonster;
import com.varrock.world.entity.impl.player.Player;


public abstract class SaveUnlockedMonstersList extends SaveObject {

	/**
	 * The default item amount.
	 */
	public static final int DEFAULT_ITEM_AMOUNT = 1;

	/**
	 * The default item id.
	 */
	public static final int DEFAULT_ITEM_ID = 1;

	/**
	 * Constructs a new SaveContainer.
	 *
	 * @param name
	 */
	public SaveUnlockedMonstersList(String name) {
		super(name);
	}

	@Override
	public boolean save(Player player, BufferedWriter writer) throws IOException {
		writer.write(getName());
		writer.newLine();
		List<DonatorBossMonster> list = getMonsterList(player);
		for(int i = 0; i < list.size(); i++) {
			DonatorBossMonster achievement = list.get(i);
			if(achievement != null) {
				writer.write(achievement.toString());
				writer.newLine();
			}
		}
		return true;
	}

    @Override
    public void load(Player player, String values, BufferedReader reader) throws IOException {
		String line;
		while ((line = reader.readLine()).length() > 0) {
			DonatorBossMonster monster = DonatorBossMonster.valueOf(line);
			loadBossMonster(player, monster);
		}
	}

	public abstract List<DonatorBossMonster> getMonsterList(Player player);

	public abstract void loadBossMonster(Player player, DonatorBossMonster achievement);
}