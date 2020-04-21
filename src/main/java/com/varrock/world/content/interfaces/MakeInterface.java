package com.varrock.world.content.interfaces;

import com.mysql.fabric.xmlrpc.base.Array;
import com.varrock.model.input.EnterAmount;
import com.varrock.world.content.skill.impl.cooking.Cooking;
import com.varrock.world.content.skill.impl.crafting.Flax;
import com.varrock.world.content.skill.impl.crafting.Gems;
import com.varrock.world.content.skill.impl.crafting.jewellery.JewelleryMaking;
import com.varrock.world.content.skill.impl.fletching.Fletching;
import com.varrock.world.content.skill.impl.herblore.Herblore;
import com.varrock.world.content.skill.impl.prayer.BonesOnAltar;
import com.varrock.world.entity.impl.player.Player;

import java.util.Arrays;

public class MakeInterface {
	private static final int INTERFACE_ID = 55508;
	private static final int MAKE_X = -1;
	private static final int MAKE_ALL = 28;

	private Player player;
	private int[] items;
	private MakeType makeType;
	private int interfaceId;
	private int amount;


	public MakeInterface(Player player, int[] items, MakeType makeType) {
		this.player = player;
		this.items = items;
		this.makeType = makeType;
		this.interfaceId = getInterfaceId();
		this.amount = 1;
	}

	public static void open(Player player, int[] items, MakeType makeType) {
		if (player.getMakeInterface() == null) {
			player.setMakeInterface(new MakeInterface(player, items, makeType));
		}
		player.getMakeInterface().items = items;
		player.getMakeInterface().makeType = makeType;
		player.getMakeInterface().interfaceId = player.getMakeInterface().getInterfaceId();
		player.getMakeInterface().open();
	}

	private int getInterfaceId() {
		switch (items.length) {
			case 1:
				return INTERFACE_ID;
			case 2:
				return INTERFACE_ID + 5;
			case 3:
				return INTERFACE_ID + 14;
			case 4:
				return INTERFACE_ID + 27;
		}
		return -1;
	}

	public void open() {
		player.getPacketSender().sendString(makeType.title, INTERFACE_ID - 7);
		System.out.println(items.length);
		for (int i = 0; i < items.length; i++) {
			int itemModelId = interfaceId + items.length * 3 + i + 1;
			player.getPacketSender().sendInterfaceModel(itemModelId, items[i], 225);
		}
		player.getPacketSender().sendChatboxInterface(interfaceId);
	}

	public static boolean checkButton(Player player, int buttonId) {
		switch (buttonId) {
			case 55503:
				player.getMakeInterface().amount = 1;
				return true;
			case 55504:
				player.getMakeInterface().amount = 5;
				return true;
			case 55505:
				player.getMakeInterface().amount = 10;
				return true;
			case 55506:
				player.getMakeInterface().amount = MAKE_X; // This should be x
				return true;
			case 55507:
				player.getMakeInterface().amount = MAKE_ALL;
				return true;
		}
		if (buttonId >= 55509 && buttonId <= 55545) {
			player.getMakeInterface().handleItemButton(buttonId);
			return true;
		}
		return false;
	}

	private void handleItemButton(int buttonId) {
		int buttonNumber = (buttonId - interfaceId) / 3;
		if (amount == MAKE_X) {
			player.getPacketSender().sendInterfaceRemoval();
			player.setInputHandling(new EnterAmount() {
				@Override
				public boolean handleAmount(Player player, int amount) {
					player.getMakeInterface().amount = amount;
					player.getMakeInterface().handleItemButton(buttonId);
					player.getMakeInterface().amount = MAKE_X;
					return false;
				}
			});
			player.getPacketSender().sendEnterAmountPrompt("How many would you like to make?");
			return;
		}
		switch (makeType) {
			case COOKING: //DONE
				Cooking.cook(player, amount);
				break;
			case BOW_STRINGING: //DONE
				Fletching.stringBow(player, amount);
				break;
			case BOWS: //DONE
				Fletching.fletchBow(player, items[buttonNumber], amount);
				break;
			case GEMS: //DONE
				Gems.cutGem(player, amount, items[buttonNumber]);
				break;
			case FLAX:
				Flax.spinFlax(player, amount);
				break;
			case BONES:
				BonesOnAltar.offerBones(player, amount);
				break;
			case UNFINISHED_POTION:
				Herblore.makeUnfinishedPotion(player, amount);
				break;
			case FINISHED_POTION:
				Herblore.makeFinishedPotions(player, amount);
				break;
			case JEWELLERY: //DONE
				JewelleryMaking.make(player, amount, buttonNumber);
				break;
		}
	}

	public enum MakeType {
		COOKING("What would you like to cook."), // DONE R
		BOW_STRINGING("What would you like to make."), // DONE R
		BOWS("What would you like to make."), // DONE R
		GEMS("What would you like to cut."), // DONE R
		FLAX("What would you like to make."), // DONE R
		BONES("What would you like to offer."), // DONE R
		UNFINISHED_POTION("What would you like to make."), // DONE R
		FINISHED_POTION("What would you like to make."), // DONE R
		JEWELLERY("What would you like to make."); // DONE R

		String title;

		MakeType(String title) {
			this.title = title;
		}

		public String getTitle() {
			return title;
		}
	}

}
