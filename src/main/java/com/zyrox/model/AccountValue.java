package com.zyrox.model;

import com.zyrox.commands.Command;
import com.zyrox.commands.CommandHandler;
import com.zyrox.model.container.impl.Bank;
import com.zyrox.model.definitions.ItemDefinition;
import com.zyrox.world.entity.impl.player.Player;

public class AccountValue {

	private Player player;

	public AccountValue(Player player) {
		this.player = player;
	}

	
	public int getTotalValue(boolean print) {
		int counter = 0;
		counter += getInventoryValue();
		counter += getEquipmentValue();
		counter += getBankValue();
		/*counter += getTradeValue();
		counter += getDuelValue();*/
		counter +=  getPkpValue()/100;
		counter += player.getPointsHandler().getDonationPoints();
		if(print)
			System.out.println("Checking account value for " + player.getName() + " -- " + counter);
		lastValue = counter;
		return counter;
	}
	public long getTotalCoinValue(boolean print) {
		long counter = 0;
		counter += getInventoryCoinValue();
		counter += getEquipmentCoinValue();
		counter += getBankCoinValue();
		if(print)
			System.out.println("Checking account coin value for " + player.getName() + " -- " + counter);
		//lastValue = counter;
		return counter;
	}
	/**
	 * Gets the total account value in donator points.
	 *
	 * @return
	 */
	public int getTotalValue() {
		return getTotalValue(false);
	}
	
	public long getTotalCoinValue(){
		return getTotalCoinValue(false);
	}
	
	private int lastValue = 0;
	
	public int getCachedTotalValue() {
		return lastValue;
	}
	
	public int getPkpValue() {
		int counter = 0;
		counter += player.getPointsHandler().getPkPoints();
		counter += getPkTickets();
		return counter;
	}  
	
	
	public long searchForItem(int id) {
		long counter = 0;
		for(int k = 0; k < 9; k++) {
			Bank bank = player.getBank(k);
			if(bank != null) {
				for(int i = 0; i < bank.getValidItems().size(); i++) {
					if(bank.getItems()[i].getId() == id) {
						counter += bank.getItems()[i].getAmount();
					}
				}
			}
		}
		for(Item item: player.getInventory().getItems()) {
			if(item != null) {
				if(item.getId() == id) {
					counter += item.getAmount();
				}
			}
		}
		for(Item item : player.getEquipment().getItems()) {
			if(item != null) {
				if(id == item.getId()) {
					counter+= item.getAmount();
				}
			}
		}
		return counter;
	}
	
	public long getPkTickets() {
		return searchForItem(2996);
	}
	
	public long getCoinsAmount() {
		return searchForItem(995) + player.getMoneyInPouch() + (searchForItem(12632) * 100000000);
	}
	
	public long getTicketsAmount() {
			return searchForItem(52410);
	}
	
	

	public int getInventoryValue() {
		return getContainerValue(player.getInventory().getItems());
	}
	
	public long getInventoryCoinValue() {
		return getContainerCoinValue(player.getInventory().getItems());
	}

	public int getEquipmentValue() {
		return getContainerValue(player.getEquipment().getItems());
	}
	
	public long getEquipmentCoinValue() {
		return getContainerCoinValue(player.getEquipment().getItems());
	}

	public int getBankValue() {
		int counter = 0;
		for(int k = 0; k < 9; k++) {
			Bank bank = player.getBank(k);
			if(bank != null) {
				counter += getContainerValue(bank.getItems());
			}
		}
		return counter;
	}
	
	public long getBankCoinValue() {
		long counter = 0;
		for(int k = 0; k < 9; k++) {
			Bank bank = player.getBank(k);
			if(bank != null) {
				counter += getContainerCoinValue(bank.getItems());
			}
		}
		return counter;
	}

	/*
	public int getTradeValue() {
		return getContainerValue(player.getTrading());
	}

	public int getDuelValue() {
		return getContainerValue(player.getDuelContainer());
	}
	*/

	
	
	public static int getContainerValue(Item[] items) {
		if(items == null)
			return 0;
		int counter = 0;
		for (Item item : items) {
			counter += getItemValue(item);
		}
		return counter;
	}
	
	public static long getContainerCoinValue(Item[] items) {
		if(items == null)
			return 0;
		long counter = 0;
		for (Item item : items) {
			long value = getItemCoinValue(item);
			counter += value;
		}
		return counter;
	}

	
	/**
	 * Gets the account value of the item, not forgetting about the item amount/items being noted.
	 *
	 * @param item
	 * @return
	 */
	public static int getItemValue(Item item) {
		if (item == null)
			return 0;
		if(item.getId() <= 0) {
			return 0;
		}
		if(ItemDefinition.getDefinitions()[item.getId()] == null)
			return 0;

		int id = item.getId();
		return ItemDefinition.getDefinitions()[item.getId()].getValue() * item.getCount();
	}

	public static long getItemCoinValue(Item item) {
		if (item == null)
			return 0;
		int id = item.getId();
		return (long)ItemDefinition.forId(id).getValue() * (long)item.getCount();
	}
	

	static {
		CommandHandler.submit(new Command("accountvalue", Command.NORMAL_RIGHTS) {

			@Override
			public boolean execute(Player player, String input) {
				player.sendMessage("Acc value: " + player.getAccountValue().getTotalValue());
				return true;
			}

		});
		
		CommandHandler.submit(new Command("pkpvalue", Command.NORMAL_RIGHTS) {

			@Override
			public boolean execute(Player player, String input) {
				player.sendMessage("Pk value: " + player.getAccountValue().getPkpValue());
				return true;
			}

		});
		CommandHandler.submit(new Command("pktickets", Command.NORMAL_RIGHTS) {

			@Override
			public boolean execute(Player player, String input) {
				player.sendMessage("Pk ticks: " + player.getAccountValue().getPkTickets());
				return true;
			}

		});
	}
}
