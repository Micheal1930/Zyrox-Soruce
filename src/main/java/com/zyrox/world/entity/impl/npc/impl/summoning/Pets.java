package com.zyrox.world.entity.impl.npc.impl.summoning;

import java.util.HashMap;
import java.util.Map;

/**
 * Pets for Simplicity.
 * @author r_g19
 *
 */
public enum Pets {
	
	TZ_REK_ZUK(52319, 23009);
	
	private int itemId, npcId;
	
	private static Map<Integer, Pets> vars = new HashMap<>();
	
	static {
		
		for (Pets pet : Pets.values()) {
			vars.put(pet.getItemId(), pet);
		}
		
	}
	
	public static Pets of(int itemId) {
		return vars.get(itemId);
	}
	
	private Pets(int itemId, int npcId) {
		this.itemId = itemId;
		this.npcId = npcId;
	}

	public int getItemId() {
		return itemId;
	}


	public int getNpcId() {
		return npcId;
	}

}