package com.varrock.world.content.presets;

import com.varrock.model.MagicSpellbook;
import com.varrock.model.Prayerbook;
import com.varrock.world.entity.impl.player.Player;

public class Preset {
	
	private int index;
	
	private String name;
	
	private Prayerbook prayerBook = Prayerbook.NORMAL;
	
	private MagicSpellbook spellBook = MagicSpellbook.NORMAL;
	
	private PresetContainer equipment;
	
	private PresetContainer inventory;
	
	private static final int INVENTORY_CONTAINER_ID = 86048;
	
	private static final int EQUIPMENT_CONTAINER_ID = 86150;
	
	private Player player;
	
	public Preset(int index, Player player) {
		this.index = index;
		this.player = player;
		this.equipment = new PresetContainer(EQUIPMENT_CONTAINER_ID, 14, player);
		this.inventory = new PresetContainer(INVENTORY_CONTAINER_ID, 28, player);
	}
	
	public Prayerbook getPrayerBook() {
		return prayerBook;
	}
	
	public void setPrayerBook(Prayerbook prayerBook) {
		this.prayerBook = prayerBook;
	}
	
	public MagicSpellbook getSpellBook() {
		return spellBook;
	}
	
	public void setSpellBook(MagicSpellbook spellBook) {
		this.spellBook = spellBook;
	}
	
	public PresetContainer getEquipment() {
		return equipment;
	}
	
	public PresetContainer getInventory() {
		return inventory;
	}
	
	public boolean isEmpty() {
		return equipment.isEmpty() && inventory.isEmpty();
	}
	
	public int getIndex() {
		return index;
	}
	
	public String getName() {
		if (name == null) {
			return "Preset " + (index + 1);
		}
		
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void rename(String name, int index) {
		setName(name);
		sendName(index);
	}
	
	public void delete(int index) {
		if (isEmpty() && getName() == null) {
			onEmpty();
			return;
		}
		
		getEquipment().resetItems();
		
		getInventory().resetItems();
		
		player.sendMessage("Successfully deleted @dre@" + getName() + "</col>.");
		
		setName(null);
		
		player.getPacketSender().sendString(86009 + index, getName());
		
		refresh();
	}
	
	public void refresh() {
		getEquipment().refreshItems();
		getInventory().refreshItems();
		
		sendPrayerBook();
		sendSpellBook();
	}
	
	public void sendName() {
		int idx = index;
		
		if (idx > 0) {
			idx += idx;
		}
		
		player.getPacketSender().sendString(86009 + idx, getName());
	}
	
	public void sendName(int index) {
		player.getPacketSender().sendString(86009 + index, getName());
	}
	
	public void sendPrayerBook() {
		player.getPacketSender().sendString(86060, "Prayer: @yel@" + getPrayerBook().toString());		
	}
	
	public void sendSpellBook() {
		player.getPacketSender().sendString(86064, "Spells: @yel@" + getSpellBook().toString());
	}
	
	public void onEmpty() {
		player.sendMessage("This preset is empty. Click on Save/Update to load your current gear into this slot.");
	}

}
