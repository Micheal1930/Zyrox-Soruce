package com.zyrox.saving;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

import com.zyrox.model.Item;
import com.zyrox.model.container.ItemContainer;
import com.zyrox.world.entity.impl.player.Player;


public abstract class SaveContainer extends SaveObject {

	/**
	 * The default item amount.
	 */
	public static final int DEFAULT_ITEM_AMOUNT = 1;

	/**
	 * The default item id.
	 */
	public static final int DEFAULT_ITEM_ID = -1;

	/**
	 * Constructs a new SaveContainer.
	 *
	 * @param name
	 */
	public SaveContainer(String name) {
		super(name);
	}

	@Override
	public boolean save(Player player, BufferedWriter writer) throws IOException {
		writer.write(getName());
		writer.newLine();
		Item[] items = getContainer(player).getItems();
		for(int i = 0; i < items.length; i++) {
			Item item = items[i];
			if(item != null) {
				if(item.getId() != DEFAULT_ITEM_ID) {
					writer.write(item.getId() + " " + item.getCount() + " " + i);
					writer.newLine();
				}
			}
		}
		return true;
	}

    @Override
    public void load(Player player, String values, BufferedReader reader) throws IOException {
		String line;
		while ((line = reader.readLine()).length() > 0) {
			
			String[] parts = line.split(" ");
			int id = Integer.parseInt(parts[0]);
			int amount = DEFAULT_ITEM_AMOUNT;
			int slot = -1;
			if (parts.length > 1) {
				amount = Integer.parseInt(parts[1]);
			}
			if(parts.length > 2) {
				slot = Integer.parseInt(parts[2]);
			}
			Item nextItem = new Item(id, amount);
			loadItem(player, nextItem, slot);
		}
	}



	public static Item lineToItem(String line) {
		String[] parts = line.split(" ");
		int id = Integer.parseInt(parts[0]);
		int amount = DEFAULT_ITEM_AMOUNT;
		int slot = -1;
		if (parts.length > 1) {
			amount = Integer.parseInt(parts[1]);
		}
		if(parts.length > 2) {
			slot = Integer.parseInt(parts[2]);
		}
		return new Item(id, amount);
	}

	public abstract ItemContainer getContainer(Player player);

	public abstract void loadItem(Player player, Item item, int slot);
}