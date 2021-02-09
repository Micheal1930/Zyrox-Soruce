package com.zyrox.saving;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.zyrox.model.Item;
import com.zyrox.world.entity.impl.player.Player;


public class SaveBoB extends SaveObject {

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
	public SaveBoB(String name) {
		super(name);
	}

	@Override
	public boolean save(Player player, BufferedWriter writer) throws IOException {
		if (player.getSummoning().getBeastOfBurden() != null) {
			writer.write(getName());
			writer.newLine();
			List<Item> items = player.getSummoning().getBeastOfBurden().getValidItems();
			int slot = 0;
			for(Item item: items) {
				if(item != null) {
					if(item.getId() != DEFAULT_ITEM_ID) {
						writer.write(item.getId() + " " + item.getCount() + " " + slot);
						writer.newLine();
					}
				}
				slot++;
			}
		}
		return true;
	}

    @Override
    public void load(Player player, String values, BufferedReader reader) throws IOException {
		String line;
		ArrayList<Item> list = new ArrayList<Item>(28);
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
			list.add(nextItem);
		}
		
		Item[] validStoredItems = new Item[list.size()];
		for(int i = 0; i < validStoredItems.length; i++) {
			validStoredItems[i] = list.get(i);
		}
		if(player.getSummoning().getSpawnTask() != null) {
			player.getSummoning().getSpawnTask().setValidItems(validStoredItems);
		}
		
	}

}