package com.varrock.saving;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

import com.varrock.world.entity.impl.player.Player;


public abstract class SaveIntArray extends SaveObject {

	public SaveIntArray(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	public static final int DEFAULT_VALUE = 0;

	@Override
	public boolean save(Player player, BufferedWriter writer) throws IOException {
		writer.write(getName());
		writer.newLine();
		int[] array = getIntArray(player);
		for(int i = 0; i < array.length; i++) {
			int value = array[i];
			if(value != DEFAULT_VALUE) {
				writer.write(i + " " + value);
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
			int index = Integer.parseInt(parts[0]);
			int value = DEFAULT_VALUE;
			if (parts.length > 1) {
				value = Integer.parseInt(parts[1]);
			}
			setIntValue(player, index, value);
		}
	}

	public abstract int[] getIntArray(Player player);

	public abstract void setIntValue(Player player, int index, int value);
}