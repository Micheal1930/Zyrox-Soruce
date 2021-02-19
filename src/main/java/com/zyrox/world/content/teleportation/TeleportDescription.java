package com.zyrox.world.content.teleportation;

public enum TeleportDescription {
	
	NONE(""),
	KBD("The King Black Dragon is an easy monster to practice bossing on.");
	
	TeleportDescription(String description) {
		this.description = description;
	}
	
	private String description;
	
	public String getDescription() {
		return this.description;
	}
	
	public void setDescription(String description) {
		
	}

}
