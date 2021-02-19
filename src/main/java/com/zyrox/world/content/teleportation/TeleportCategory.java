package com.zyrox.world.content.teleportation;

public enum TeleportCategory {
	
	BEGINNER_MONSTERS("Beginner Mobs"), 
	MID_LEVEL_MONSTERS("Mid-Level Mobs"), 
	BOSSES("Bosses"), 
	MINIGAMES("Minigames"), 
	MISC("Misc"), 
	DEFAULT("N/A");
	
	TeleportCategory(String name) {
		this.name = name;
	}
	
	private String name;
	
	public String getName() {
		return this.name;
	}

}
