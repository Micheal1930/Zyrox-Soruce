package com.zyrox.saving;

import com.google.common.collect.Multiset.Entry;
import com.zyrox.world.entity.impl.player.Player;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

public class SaveBlowPipe extends SaveObject {

	public SaveBlowPipe(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean save(Player player, BufferedWriter writer) throws IOException {
		writer.write(getName());
		writer.newLine();
		for (Entry<Integer> dartItem : player.getBlowpipeLoading().getContents().entrySet()) {
			writer.write(dartItem.getElement() + " " + player.getBlowpipeLoading().getContents().count(dartItem.getElement()));
			writer.newLine();
		}
		return true;
	}

	@Override
	public void load(Player player, String values, BufferedReader reader) throws IOException {
		// TODO Auto-generated method stub
		String line;
		while ((line = reader.readLine()).length() > 0) {
			String[] parts = line.split(" ");
			int element = Integer.parseInt(parts[0]);
			int count = Integer.parseInt(parts[1]);
			player.getBlowpipeLoading().getContents().setCount(element, count);
		}
		
	}

}
