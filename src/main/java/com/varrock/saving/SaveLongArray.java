package com.varrock.saving;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

import com.varrock.world.entity.impl.player.Player;


public abstract class SaveLongArray extends SaveObject {

	public SaveLongArray(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	public static final long DEFAULT_VALUE = 0L;

	@Override
	public boolean save(Player player, BufferedWriter writer) throws IOException {
		writer.write(getName());
		writer.newLine();
		long[] array = getLongArray(player);
		for(int i = 0; i < array.length; i++) {
			long value = array[i];
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
			long value = DEFAULT_VALUE;
			if (parts.length > 1) {
				value = Long.parseLong(parts[1]);
			}
			setLongValue(player, index, value);
		}
	}




	public abstract long[] getLongArray(Player player);

	public abstract void setLongValue(Player player, int index, long value);
}