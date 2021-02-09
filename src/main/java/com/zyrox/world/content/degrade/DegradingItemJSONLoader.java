package com.zyrox.world.content.degrade;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.zyrox.util.JsonLoader;

/**
 * Loading degrading items
 * 
 * @author 2012 <https://www.rune-server.ee/members/dexter+morgan/>
 *
 */
public class DegradingItemJSONLoader extends JsonLoader {

	/**
	 * The file location
	 */
	private static final String FILE_LOCATION = "./data/def/json/degradables.json";


	/**
	 * The degradable definitions
	 */
	private static final ArrayList<DegradingDataItem> degradables = new ArrayList<DegradingDataItem>();
	
	private static HashMap<Integer, DegradingDataItem> deg = new HashMap<Integer, DegradingDataItem>();

	/**
	 * Loads the definition
	 */
	public DegradingItemJSONLoader() {
		load();
		System.out.println("Loaded "+degradables.size()+" degradables.");
	}

	@Override
	public void load(JsonObject reader, Gson builder) {
		int id = reader.get("id").getAsInt();
		int hitsRemaining = reader.get("hitsRemaining").getAsInt();
		int slot = reader.get("slot").getAsInt();
		int dropItem = reader.get("dropItem").getAsInt();
		int nextItem = reader.get("nextItem").getAsInt();
		boolean degradeOnCombat = true;
		if (reader.has("degradeOnCombat")) {
			degradeOnCombat = reader.get("degradeOnCombat").getAsBoolean();
		}
		DegradingDataItem item =
				new DegradingDataItem(id, hitsRemaining, slot, dropItem, nextItem, degradeOnCombat);

		degradables.add(item);
		
		deg.put(id, item);
	}

	/**
	 * Gets the id
	 * 
	 * @param id the id
	 * @return the main id
	 */
	public static DegradingDataItem getId(int id) {
		Optional<DegradingDataItem> item = degradables.stream().filter(i -> i.getId() == id).findFirst();
		if(!item.isPresent()) {
			return null;
		}
		return item.get();
	}
	
	public static DegradingDataItem get(int id) {
		return deg.get(id);
	}

	@Override
	public String filePath() {
		return FILE_LOCATION;
	}
}
