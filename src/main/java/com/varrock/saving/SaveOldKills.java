package com.varrock.saving;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

import com.varrock.world.content.KillsTracker;
import com.varrock.world.content.KillsTracker.KillsEntry;
import com.varrock.world.entity.impl.player.Player;

public class SaveOldKills extends SaveObject {

	public SaveOldKills(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean save(Player player, BufferedWriter writer) throws IOException {
		writer.write(getName());
		writer.newLine();
		for(KillsEntry entry: player.getKillsTracker()) {
			if(entry != null) {
				writer.write(entry.npcName + "," + entry.amount + "," + (entry.boss ? 1: 0));
				writer.newLine();
			}
		}
		return true;
	}

	@Override
	public void load(Player player, String values, BufferedReader reader) throws IOException {
		// TODO Auto-generated method stub
		String line;
		while ((line = reader.readLine()).length() > 0) {
			String[] parts = line.split(",");
			String name = parts[0];
			int amount = Integer.parseInt(parts[1]);
			int boss = Integer.parseInt(parts[2]);
			KillsEntry entry = new KillsEntry(name, amount, boss > 0 ? true: false);
			KillsTracker.submit(player, entry);
		}
		

	}

}
