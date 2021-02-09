package com.zyrox.saving;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

import com.zyrox.world.entity.impl.player.Player;


public abstract class SaveStringArray extends SaveObject {

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
	public SaveStringArray(String name) {
		super(name);
	}

	@Override
	public boolean save(Player player, BufferedWriter writer) throws IOException {
		writer.write(getName());
		writer.newLine();
		for(String str: getStringArray(player)) {
			writer.write(str);
			writer.newLine();
		}
		return true;
	}

    @Override
    public void load(Player player, String values, BufferedReader reader) throws IOException {
		String line;
		int index = 0;
		while ((line = reader.readLine()).length() > 0) {
			setString(player, index, line);
			index++;
		}
	}



	public abstract String[] getStringArray(Player player);

	public abstract void setString(Player player, int index, String value);
}