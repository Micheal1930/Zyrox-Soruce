package com.zyrox.world.content.teleportation;

import com.zyrox.model.Item;
import com.zyrox.model.Position;

public enum TeleportData {
	
	DEFAULT(TeleportDescription.NONE, "N/A", new Position(1, 1), TeleportCategory.DEFAULT, 1, 200, new Item(995, 1)),
	
	KING_BLACK_DRAGON(TeleportDescription.KBD, "King Black Dragon", new Position(2273, 4681, 0), TeleportCategory.BOSSES, 50, 2500, new Item(995, 100), new Item(995, 100), new Item(995, 100)),
	SKOTIZO(TeleportDescription.NONE, "Skotizo", new Position(2771, 9185, 0), TeleportCategory.BOSSES, 7286, 2500, new Item(4151, 1), new Item(4152, 100), new Item(15501, 100));
	
	TeleportData(TeleportDescription description, String name, Position position, TeleportCategory category, int npcID, int npcZoom, Item... items) {
		this.description = description;
		this.name = name;
		this.position = position;
		this.category = category;
		this.npcID = npcID;
		this.npcZoom = npcZoom;
		this.items = items;
	}
	
	private String name;
	private Position position;
	private TeleportDescription description;
	private TeleportCategory category;
	private int npcID, npcZoom;
	private Item[] items;
	
	public static TeleportData findFirst(TeleportCategory category) {
		for(TeleportData data : TeleportData.values()) {
			if(data == null)
				continue;
			if(data.getCategory() == category)
				return data;
		}
		return null;
	}
	
	public static TeleportData findSpot(TeleportCategory category, int value) {
		int index = 0;
		for(TeleportData data : TeleportData.values()) {
			if(data == null)
				continue;
			if(category == TeleportCategory.DEFAULT)
				continue;
			if(data.getCategory() != category)
				continue;
			if(index == value)
				return data;
			index++;
		}
		return null;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public Position getPosition() {
		return this.position;
	}
	
	public void setPosition(Position position) {
		this.position = position;
	}
	
	public TeleportCategory getCategory() {
		return this.category;
	}
	
	public void setPosition(TeleportCategory category) {
		this.category = category;
	}
	
	public TeleportDescription getDescription() {
		return this.description;
	}
	
	public void setDescription(TeleportDescription description) {
		this.description = description;
	}
	
	public int getNPCID() {
		return this.npcID;
	}
	
	public void setNPCID(int npcID) {
		this.npcID = npcID;
	}
	
	public int getNPCZoom() {
		return this.npcZoom;
	}
	
	public void setNPCZoom(int npcZoom) {
		this.npcZoom = npcZoom;
	}
	
	public Item[] getItems() {
		return this.items;
	}
	
	public void setItems(Item[] items) {
		this.items = items;
	}
}
