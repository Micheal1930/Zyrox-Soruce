package com.varrock.saving;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

import com.varrock.world.content.DropLog;
import com.varrock.world.content.DropLog.DropLogEntry;
import com.varrock.world.entity.impl.player.Player;

public class SaveOldDrops extends SaveObject {

	public SaveOldDrops(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean save(Player player, BufferedWriter writer) throws IOException {
		writer.write(getName());
		writer.newLine();
		for(DropLogEntry entry: player.getDropLog()) {
			if(entry != null) {
				writer.write(entry.item + " " + entry.amount + " " + (entry.rareDrop ? 1: 0));
				writer.newLine();
			}
		}
		return true;
	}

	@Override
	public void load(Player player, String values, BufferedReader reader) throws IOException {
		String line;
		while ((line = reader.readLine()).length() > 0) {
			String[] parts = line.split(" ");
			int item = Integer.parseInt(parts[0]);
			int amount = Integer.parseInt(parts[1]);
			int rare = Integer.parseInt(parts[2]); //unused var cause awesome coders worked on this before
			DropLog.submit(player, new DropLogEntry(item,amount));
		}
	}

}
