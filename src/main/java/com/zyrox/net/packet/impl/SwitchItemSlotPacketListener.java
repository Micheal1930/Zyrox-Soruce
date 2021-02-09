package com.zyrox.net.packet.impl;

import com.zyrox.model.Item;
import com.zyrox.model.container.impl.Bank;
import com.zyrox.model.container.impl.Inventory;
import com.zyrox.net.packet.Packet;
import com.zyrox.net.packet.PacketListener;
import com.zyrox.world.content.Debug;
import com.zyrox.world.entity.impl.player.Player;

/**
 * This packet listener is called when an item is dragged onto another slot.
 * 
 * @author relex lawl
 */

public class SwitchItemSlotPacketListener implements PacketListener {

	@Override
	public void handleMessage(Player player, Packet packet) {
		if (player.getConstitution() <= 0)
			return;
		int interfaceId = packet.readLEShortA();
		packet.readByteC();
		int fromSlot = packet.readLEShortA();
		int toSlot = packet.readLEShort();

		Debug.write(player.getName(), "SwitchItemPacketListener", new String[] {
				"interfaceId: "+interfaceId,
				"fromSlot: "+fromSlot,
				"toSlot: "+toSlot,
		});

		switch (interfaceId) {
		case Inventory.INTERFACE_ID:
		case Bank.INVENTORY_INTERFACE_ID:
			if(fromSlot >= 0 && fromSlot < player.getInventory().capacity() && toSlot >= 0 && toSlot < player.getInventory().capacity() && toSlot != fromSlot) {
				player.getInventory().swap(fromSlot, toSlot).refreshItems();
			}
			break;
		case Bank.INTERFACE_ID:
			if(player.isBanking() && player.getInterfaceId() == 5292) {
				if(fromSlot == toSlot)
					return;
				if(player.getBankSearchingAttribtues().isSearchingBank()) {
					Bank bank = player.getBankSearchingAttribtues().getSearchedBank();
					if(bank != null) {
						if(player.swapMode()) {
							bank.shiftSwap(fromSlot, toSlot);
						} else {
							bank.swap(fromSlot, toSlot).sortItems();
						}
						bank.open(player, false);
					}
				} else {
					if(player.swapMode()) {
						player.getBank(player.getCurrentBankTab()).shiftSwap(fromSlot, toSlot);
					} else {
						player.getBank(player.getCurrentBankTab()).swap(fromSlot, toSlot);
					}
					player.getBank(player.getCurrentBankTab()).open(player, false);
				}
				return;
			} else {
				player.getPacketSender().sendInterfaceRemoval();
			}
			break;
		}
		if(player.isBanking() && player.getInterfaceId() == 5292) {
			boolean toBankTab = false;
			for(int i = 0; i < Bank.BANK_TAB_INTERFACES.length; i++) {
				toBankTab = Bank.BANK_TAB_INTERFACES[i][0] == interfaceId;
				Item item = new Item(player.getBank(player.getCurrentBankTab()).getItems()[fromSlot].getId(), player.getBank(player.getCurrentBankTab()).getItems()[fromSlot].getAmount());
				if(!player.getBank(player.getCurrentBankTab()).contains(item.getId()) || player.getBank(player.getCurrentBankTab()).getAmount(item.getId()) < item.getAmount())
					return;
				if(toBankTab) {
					if(player.getBankSearchingAttribtues().isSearchingBank()) {
						player.getPacketSender().sendMessage("You cannot do that right now.");
						return;
					}
					int bankTab = Bank.BANK_TAB_INTERFACES[i][1];
					int slot = player.getBank(player.getCurrentBankTab()).getSlot(item.getId());
					if(slot < 0)
						return;
					player.setNoteWithdrawal(false);
					player.getBank(Bank.getTabForItem(player, item.getId())).switchItem(player.getBank(bankTab), item, slot, true, false);
					Bank.sendTabs(player, null);
					player.getBank(player.getCurrentBankTab()).open();
					return;
				}
			}
		}
	}
}
