package com.varrock.world.content.presets;

import java.util.Arrays;

import com.varrock.GameSettings;
import com.varrock.model.Flag;
import com.varrock.model.Item;
import com.varrock.model.container.impl.Bank;
import com.varrock.model.definitions.ItemDefinition.EquipmentType;
import com.varrock.model.input.Input;
import com.varrock.net.packet.impl.EquipPacketListener;
import com.varrock.world.content.BonusManager;
import com.varrock.world.content.Sounds;
import com.varrock.world.content.Sounds.Sound;
import com.varrock.world.content.combat.magic.Autocasting;
import com.varrock.world.content.combat.prayer.CurseHandler;
import com.varrock.world.content.combat.prayer.PrayerHandler;
import com.varrock.world.entity.impl.player.Player;

public class Presets {
	
	/**
	 * The interface id.
	 */
	private static final int INTERFACE_ID = 86000;
	
	/**
	 * The amount of total presets.
	 */
	private static final int TOTAL_PRESETS = 10;
	
	private int currentIndex;
	
	private Preset[] presets = new Preset[TOTAL_PRESETS];
	
	private Player player;
	
	public Presets(Player player) {
		this.player = player;
		
		for (int i = 0; i < presets.length; i++) {
			this.presets[i] = new Preset(i, player);
		}
	}
	
	public void open() {
		Arrays.stream(presets).forEach(preset -> preset.sendName());
		getPreset().refresh();
		player.getPacketSender().sendInterface(INTERFACE_ID);
	}
	
	public void save() {
		if (player.getInterfaceId() != INTERFACE_ID) {
			return;
		}
		
		getPreset().getEquipment().setItems(player.getEquipment().getCopiedItems());
		getPreset().getInventory().setItems(player.getInventory().getCopiedItems());
		getPreset().refresh();
		player.sendMessage("Succesfully saved the current setup as @dre@" + getPreset().getName() + "</col>.");
	}
	
	public void load() {
		if (player.getInterfaceId() != INTERFACE_ID) {
			return;
		}
		
		Preset current = getPreset();
		
		if (current.isEmpty()) {
			getPreset().onEmpty();
			return;
		}
		
		player.setPrayerbook(current.getPrayerBook());
		player.getPacketSender().sendTabInterface(GameSettings.PRAYER_TAB, player.getPrayerbook().getInterfaceId());
		PrayerHandler.deactivateAll(player);
		CurseHandler.deactivateAll(player);

		player.setSpellbook(current.getSpellBook());
		player.getPacketSender().sendTabInterface(GameSettings.MAGIC_TAB, player.getSpellbook().getInterfaceId()).sendMessage("Your magic spellbook is changed..");
		Autocasting.resetAutocast(player, true);
		
		Item[] equipment = current.getEquipment().getItems();
		
		if (!player.getBank().containsAny(equipment) && !player.getInventory().containsAny(equipment) && !player.getEquipment().containsAny(equipment)) {
			player.sendMessage("You don't have any of the required items to load that gear.");
			return;
		}
		
		Bank.depositItems(player, player.getEquipment(), true);
		Bank.depositItems(player, player.getInventory(), true);
		
		if (!player.getEquipment().isEmpty() || !player.getInventory().isEmpty()) {
			player.sendMessage("Error loading preset.");
			return;
		}
		
		boolean fail = false;
		for (Item item : current.getEquipment().getItems()) {
			if (item.getId() == -1) {
				continue;
			}
			
			int tab = Bank.getTabForItemStrict(player, item.getId());
			
			if (tab == -1) {
				fail = true;
				continue;
			}
			
			int equipmentSlot = item.getDefinition().getEquipmentSlot();

			if (equipmentSlot == -1) {
				fail = true;
				continue;
			}

			if ((item.getDefinition().getEquipmentType() == EquipmentType.WEAPON && !item.getDefinition().isWeapon()) || item.getDefinition().isNoted()) {
				fail = true;
				continue;
			}
			
			Item bankItem = player.getBank(tab).getById(item.getId());
			
			int amount = item.getAmount();
			
			if (amount < 1) {
				continue;
			}
			
			if(bankItem.getAmount() < 1) {
				continue;
			}
			
			
			if (amount > bankItem.getAmount()) {
				amount = bankItem.getAmount();
			}
			
			player.getBank(tab).delete(item.getId(), amount, false);
			
			player.getEquipment().set(equipmentSlot, new Item(item.getId(), amount));
		}
		
		for (Item item : current.getInventory().getItems()) {
			if (item.getId() == -1) {
				continue;
			}
			
			int tab = Bank.getTabForItemStrict(player, item.getId());
			
			if (tab == -1) {
				fail = true;
				continue;
			}
			
			Item bankItem = player.getBank(tab).getById(item.getId());
			
			int amount = item.getAmount();
			
			if (amount < 1) {
				continue;
			}
			
			if(bankItem.getAmount() < 1) {
				continue;
			}
			
			
			if (amount > bankItem.getAmount()) {
				amount = bankItem.getAmount();
			}
			
			player.getBank(tab).delete(item.getId(), amount, false);
			player.getInventory().add(new Item(item.getId(), amount), false);
		}

		if (fail) {
			player.sendMessage("Some of the items from your preset @dre@" + current.getName() + " </col>were not able to be loaded.");
		} else {
			player.sendMessage("Successfully loaded @dre@" + current.getName() + "</col>.");
		}
		
		player.setCastSpell(null);
		BonusManager.update(player);
		player.getEquipment().refreshItems();
		player.getInventory().refreshItems();
		player.getUpdateFlag().flag(Flag.APPEARANCE);
		Sounds.sendSound(player, Sound.EQUIP_ITEM);
		EquipPacketListener.resetWeapon(player);
	}
	
	public boolean handleButton(int buttonId) {
		if (player.getInterfaceId() != INTERFACE_ID) {
			return false;
		}
		
		if (!player.getClickDelay().elapsed(300)) {
			return false;
		}

		
		player.getClickDelay().reset();
		
		if (buttonId == 86049) {
			save();
			return true;
		}
		
		if (buttonId == 86053) {
			load();
			return true;
		}
		
		if (buttonId == 86057) {
			getPreset().setPrayerBook(getPreset().getPrayerBook().next());
			getPreset().sendPrayerBook();
			return true;
		}
		
		if (buttonId == 86061) {
			getPreset().setSpellBook(getPreset().getSpellBook().next());
			getPreset().sendSpellBook();
			return true;
		}
		
		return false;
	}
	
	public void handleMenu(int interfaceId, int menuId) {
		if (player.getInterfaceId() != INTERFACE_ID) {
			return;
		}
		
		if (!player.getClickDelay().elapsed(300)) {
			return;
		}
		
		player.getClickDelay().reset();
		
		int index = interfaceId - 20472;
		
		if (index > 0) {
			index -= index / 2;
		}
		
		if (getCurrentIndex() != index) {
			setCurrentIndex(index);
		}
		
		if (menuId == 1) {
			getPreset().delete(interfaceId - 20472);
			return;
		}
		
		if (menuId == 2) {
			player.setInputHandling(new Input() {
				
				@Override
				public void handleSyntax(Player player, String text) {
					getPreset().rename(text, interfaceId - 20472);
				}

			});
			
			player.getPacketSender().sendEnterInputPrompt("Enter a new name for your preset:");
			return;
		}
		
		if (menuId == 3) {
			if (getPreset().isEmpty()) {
				getPreset().onEmpty();
			}
			
			getPreset().refresh();
			return;
		}
	}
	
	public int getCurrentIndex() {
		return currentIndex;
	}
	
	public void setCurrentIndex(int currentIndex) {
		this.currentIndex = currentIndex;
	}
	
	public Preset getPreset() {
		return presets[currentIndex];
	}
	
	public Preset[] getPresets() {
		return presets;
	}
	
	public Preset get(int index) {
		return presets[index];
	}
	
	public void setPreset(Preset preset) {
		this.presets[preset.getIndex()] = preset;
	}

}
