package com.zyrox.world.content.teleportation;

public enum TeleportCategory {
	
	MONSTERS("Monsters"),
	SKILLING("Skilling"),
	BOSSES("Bosses"), 
	MINIGAMES("Minigames"),
	ZONES("Zones"),
	DONATORS("Donators"),
	DEFAULT("N/A");
	
	TeleportCategory(String name) {
		this.name = name;
	}
	
	private String name;
	
	public String getName() {
		return this.name;
	}

}
