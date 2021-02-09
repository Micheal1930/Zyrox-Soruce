package com.zyrox.net.packet.impl;

import com.zyrox.model.Animation;
import com.zyrox.model.CharacterAnimations;
import com.zyrox.model.Flag;
import com.zyrox.model.GameMode;
import com.zyrox.model.Graphic;
import com.zyrox.model.Item;
import com.zyrox.model.Locations.Location;
import com.zyrox.model.container.impl.*;
import com.zyrox.model.container.impl.currency.FreeCurrency;
import com.zyrox.model.definitions.ItemDefinition;
import com.zyrox.model.definitions.WeaponAnimations;
import com.zyrox.model.definitions.WeaponInterfaces;
import com.zyrox.model.input.EnterAmount;
import com.zyrox.model.input.impl.*;
import com.zyrox.model.item.CrawsBow;
import com.zyrox.model.option.InterfaceOption;
import com.zyrox.model.option.InterfaceOptionManager;
import com.zyrox.model.option.impl.DefaultInterfaceOption;
import com.zyrox.net.packet.Packet;
import com.zyrox.net.packet.PacketListener;
import com.zyrox.util.Misc;
import com.zyrox.world.content.BonusManager;
import com.zyrox.world.content.Debug;
import com.zyrox.world.content.RunePouch;
import com.zyrox.world.content.Trading;
import com.zyrox.world.content.combat.CombatFactory;
import com.zyrox.world.content.combat.magic.Autocasting;
import com.zyrox.world.content.combat.weapon.CombatSpecial;
import com.zyrox.world.content.degrade.DegradingManager;
import com.zyrox.world.content.greatolm.RaidsReward;
import com.zyrox.world.content.itemtransform.ItemTransformation;
import com.zyrox.world.content.minigames.impl.Dueling;
import com.zyrox.world.content.minigames.impl.Dueling.DuelRule;
import com.zyrox.world.content.minigames.impl.castlewars.CastleWarsManager;
import com.zyrox.world.content.minigames.impl.castlewars.item.flag.CastleWarsFlag;
import com.zyrox.world.content.minigames.impl.castlewars.item.flag.CastleWarsFlagManager;
import com.zyrox.world.content.partyroom.PartyRoomManager;
import com.zyrox.world.content.skill.impl.smithing.EquipmentMaking;
import com.zyrox.world.content.skill.impl.smithing.SmithingData;
import com.zyrox.world.content.transportation.JewelryTeleporting;
import com.zyrox.world.entity.impl.player.Player;

public class ItemContainerActionPacketListener implements PacketListener {

	/**
	 * Manages an item's first action.
	 * 
	 * @param player The player clicking the item.
	 * @param packet The packet to read values from.
	 */
	private static void firstAction(Player player, Packet packet) {
		long start = System.currentTimeMillis();
		int interfaceId = packet.readInt();
		int slot = packet.readShortA();
		int id = packet.readUnsignedShortA();

		Item item = new Item(id);

		Debug.write(player.getName(), "ItemContainerActionPacketListener - firstAction", new String[] {
				"itemId: "+id,
				"slot: "+slot,
				"interfaceId: "+interfaceId,
		});

		InterfaceOption interfaceOption = InterfaceOptionManager.getOption(interfaceId, DefaultInterfaceOption.OPTION_1);

		if(interfaceOption != null) {
			interfaceOption.onAction(player, id);
		}

		if (interfaceId == 85077) {
			boolean note = false;
			for (int i = 5; i < RaidsReward.commonLoot.length; i++) {
				if (RaidsReward.commonLoot[i].getId() == player.getRaidsLoot().getId())
					note = true;
			}
			if (note)
				player.getInventory().add(player.getRaidsLoot().getId() + 1, player.getRaidsLoot().getAmount());
			else
				player.getInventory().add(player.getRaidsLoot());

			player.getPacketSender().sendItemOnInterface(85077, -1, -1);
			player.setRaidsLoot(new Item(-1, -1));
			return;
		}

		if (interfaceId == 85078) {
			boolean note = false;
			for (int i = 5; i < RaidsReward.commonLoot.length; i++) {
				if (RaidsReward.commonLoot[i].getId() == player.getRaidsLootSecond().getId())
					note = true;
			}
			if (note)
				player.getInventory().add(player.getRaidsLootSecond().getId() + 1,
						player.getRaidsLootSecond().getAmount());
			else
				player.getInventory().add(player.getRaidsLootSecond());

			player.getPacketSender().sendItemOnInterface(85078, -1, -1);
			player.setRaidsLootSecond(new Item(-1, -1));
			return;
		}

		switch (interfaceId) {
		case PartyRoomManager.INVENTORY_ITEMS:
			PartyRoomManager.deposit(player, new Item(id), slot);
			break;
		case PartyRoomManager.DEPOSIT_INVENTORY:
			PartyRoomManager.withdraw(player, new Item(id), slot);
			break;
		case RunePouch.RUNE_CONTAINER_ID:
			player.getRunePouch().withdraw(id, slot, 1);
			break;
		case RunePouch.INVENTORY_CONTAINER_ID:
			player.getRunePouch().deposit(id, slot, 1);
			break;
		case Trading.INTERFACE_ID:
			if (player.getTrading().inTrade()) {
				player.getTrading().tradeItem(id, 1, slot);
			} else if (Dueling.checkDuel(player, 1) || Dueling.checkDuel(player, 2)) {
				player.getDueling().stakeItem(id, 1, slot);
			}
			break;
		case Trading.INTERFACE_REMOVAL_ID:
			if (player.getTrading().inTrade())
				player.getTrading().removeTradedItem(id, 1);
			break;
		case Dueling.INTERFACE_REMOVAL_ID:
			if (Dueling.checkDuel(player, 1) || Dueling.checkDuel(player, 2)) {
				player.getDueling().removeStakedItem(id, 1);
				return;
			}
			break;
		case Equipment.INVENTORY_INTERFACE_ID:
			item = slot < 0 ? null : player.getEquipment().getItems()[slot];
			if (item == null || item.getId() != id)
				return;
			if (player.getLocation() == Location.DUEL_ARENA) {
				if (player.getDueling().selectedDuelRules[DuelRule.LOCK_WEAPON.ordinal()]) {
					if (item.getDefinition().getEquipmentSlot() == Equipment.WEAPON_SLOT
							|| item.getDefinition().isTwoHanded()) {
						player.getPacketSender().sendMessage("Weapons have been locked during this duel!");
						return;
					}
				}
			}
			if (CastleWarsManager.inCastleWars(player)) {
				if (slot == Equipment.CAPE_SLOT) {
					player.getPacketSender().sendMessage("You can't remove your team cape.");
					return;
				}
				if (id == CastleWarsFlag.SARADOMIN.getId() || id == CastleWarsFlag.ZAMORAK.getId()) {
					CastleWarsFlagManager.dropFlag(player);
					return;
				}
			}
			int inventorySlot = player.getInventory().getEmptySlot();
			if (inventorySlot != -1) {
				player.getEquipment().transferItem(slot, player.getInventory(), true, true);

				BonusManager.update(player);
				if (item.getDefinition().getEquipmentSlot() == Equipment.WEAPON_SLOT) {
					WeaponInterfaces.assign(player, player.getEquipment().get(Equipment.WEAPON_SLOT));
					WeaponAnimations.assign(player, player.getEquipment().get(Equipment.WEAPON_SLOT));
					if (player.getAutocastSpell() != null || player.isAutocast()) {
						Autocasting.resetAutocast(player, true);
						player.getPacketSender().sendMessage("Autocast spell cleared.");
					}
					player.setSpecialActivated(false);
					player.getPacketSender().sendSpriteChange(41006, 945);
					CombatSpecial.updateBar(player);
					if (player.hasStaffOfLightEffect()) {
						player.setStaffOfLightEffect(-1);
						player.getPacketSender()
								.sendMessage("You feel the spirit of the Staff of Light begin to fade away...");
					}
				}
				player.getEquipment().refreshItems();
				player.getInventory().refreshItems();
				player.getUpdateFlag().flag(Flag.APPEARANCE);
				ItemTransformation.onWieldAction(player, item, true);
			} else {
				player.getInventory().full();
			}

			player.getPacketSender().updateSpecialAttackOrb();

			break;
		case Bank.INTERFACE_ID:
			if (!player.isBanking() || player.getInterfaceId() != 5292)
				break;
			if (player.getGameMode() == GameMode.ULTIMATE_IRONMAN) {
				player.getPacketSender()
						.sendMessage("@red@Warning: you can't attempt to deposit more than 30 items as ultimate iron man");
				player.getPacketSender().sendMessage(
						"@red@Warning: if you try they will be lost. The bank is just for storing your 28 items.");

			}
			player.getBank(player.getCurrentBankTab()).switchItem(player.getInventory(), item, slot, true, true);
			player.getBank(player.getCurrentBankTab()).open(player, false);
			break;
		case Bank.INVENTORY_INTERFACE_ID:
			if (!player.isBanking() || !player.getInventory().contains(item.getId()) || player.getInterfaceId() != 5292)
				return;
			if (player.getGameMode() == GameMode.ULTIMATE_IRONMAN) {
				player.getPacketSender().sendMessage("You can only withdraw items here, not deposit");
				break;
			}
			player.setCurrentBankTab(Bank.getTabForItem(player, item.getId()));
			player.getInventory().switchItem(player.getBank(player.getCurrentBankTab()), item, slot, false, true);
			break;
			case Shop.INVENTORY_INTERFACE_ID:
				Shop shop = player.getShop();
				if (shop != null) {
					item = player.getInventory().getItems()[slot].copy();
					final int price = shop.getCurrency().getBuyPrice(item);
					if (!shop.buys(item) || price <= 0) {
						player.getPacketSender().sendMessage("You cannot sell this here!");
						return;
					}
					if (shop.getId() == 27 && price >= 10_000_000) {
						player.sendMessage("You cannot sell items worth over 10,000,000 gp in total!");
						return;
					}
					ItemDefinition definition = ItemDefinition.forId(item.getId());
					player.getPacketSender().sendMessage(definition.getName() + ": the store will currently buy this for "
							+ Misc.insertCommasToNumber(price) + "x " + shop.getCurrency().getName() + ".");
				}
				break;
			case Shop.ITEM_CHILD_ID:
				shop = player.getShop();
				if (shop != null) {
					item = shop.getItems()[slot];
					ItemDefinition definition = ItemDefinition.forId(item.getId());
					if (!(shop.getCurrency() instanceof FreeCurrency))
						player.getPacketSender().sendMessage(definition.getName() + ": currently costs " + Misc.insertCommasToNumber(shop.getCurrency().getSellPrice(item)) + "x " + shop.getCurrency().getName() + ".");
					else
						player.getPacketSender().sendMessage(definition.getName() + ": currently free of charge!");
				}
				break;
		case BeastOfBurden.INTERFACE_ID:
			if (player.getInterfaceId() == BeastOfBurden.INTERFACE_ID
					&& player.getSummoning().getBeastOfBurden() != null) {
				if (item.getDefinition().isStackable()) {
					player.getPacketSender().sendMessage("You cannot store stackable items.");
					return;
				}
				player.getInventory().switchItem(player.getSummoning().getBeastOfBurden(), item, slot, false, true);
			}
			break;
		case PriceChecker.INTERFACE_PC_ID:
			if (player.getInterfaceId() == PriceChecker.INTERFACE_ID && player.getPriceChecker().isOpen()) {
				player.getInventory().switchItem(player.getPriceChecker(), item, slot, false, true);
			}
			break;
		case 1119: // smithing interface row 1
		case 1120: // row 2
		case 1121: // row 3
		case 1122: // row 4
		case 1123: // row 5
			int barsRequired = SmithingData.getBarAmount(item);
			Item bar = new Item(player.getSelectedSkillingItem(), barsRequired);
			int x = 1;
			if (x > (player.getInventory().getAmount(bar.getId()) / barsRequired))
				x = (player.getInventory().getAmount(bar.getId()) / barsRequired);

			EquipmentMaking.smithItem(player, new Item(player.getSelectedSkillingItem(), barsRequired),
					new Item(item.getId(), SmithingData.getItemAmount(item)), x);
			break;
		}

		if (BeastOfBurden.beastOfBurdenSlot(interfaceId) >= 0) {
			if (player.getInterfaceId() == BeastOfBurden.INTERFACE_ID
					&& player.getSummoning().getBeastOfBurden() != null) {
				player.getSummoning().getBeastOfBurden().switchItem(player.getInventory(), item,
						BeastOfBurden.beastOfBurdenSlot(interfaceId), false, true);
			}
		} else if (PriceChecker.priceCheckerSlot(interfaceId) >= 0) {
			if (player.getPriceChecker().isOpen()) {
				player.getPriceChecker().switchItem(player.getInventory(), new Item(id, 1),
						PriceChecker.priceCheckerSlot(interfaceId), false, true);
			}
		}
		long end = System.currentTimeMillis();
		long cycle = end - start;
		if (cycle >= 500) {
			System.err.println(cycle + "ms - item container packet 1 - " + interfaceId + " - " + slot + " - " + id);
		}
	}

	/**
	 * Manages an item's second action.
	 * 
	 * @param player The player clicking the item.
	 * @param packet The packet to read values from.
	 */
	private static void secondAction(Player player, Packet packet) {
		int interfaceId = packet.readLEShortA();
		int id = packet.readLEShortA() & 0xffff;
		int slot = packet.readLEShort();

		Item item = new Item(id);

		Debug.write(player.getName(), "ItemContainerActionPacketListener - secondAction", new String[] {
				"id: "+id,
				"slot: "+slot,
				"interfaceId: "+interfaceId,
		});

		InterfaceOption interfaceOption = InterfaceOptionManager.getOption(interfaceId, DefaultInterfaceOption.OPTION_2);

		if(interfaceOption != null) {
			interfaceOption.onAction(player, id);
		}

		switch (interfaceId) {
		case PartyRoomManager.INVENTORY_ITEMS:
			PartyRoomManager.deposit(player, new Item(id, 5), slot);
			break;
		case PartyRoomManager.DEPOSIT_INVENTORY:
			PartyRoomManager.withdraw(player, new Item(id, 5), slot);
			break;
		case -29426:
			player.getRunePouch().withdraw(id, slot, 5);
			break;
		case -29425:
			player.getRunePouch().deposit(id, slot, 5);
			break;
		case Trading.INTERFACE_ID:
			if (player.getTrading().inTrade()) {
				player.getTrading().tradeItem(id, 5, slot);
			} else if (Dueling.checkDuel(player, 1) || Dueling.checkDuel(player, 2)) {
				player.getDueling().stakeItem(id, 5, slot);
			}
			break;
		case Trading.INTERFACE_REMOVAL_ID:
			if (player.getTrading().inTrade())
				player.getTrading().removeTradedItem(id, 5);
			break;
		case Dueling.INTERFACE_REMOVAL_ID:
			if (Dueling.checkDuel(player, 1) || Dueling.checkDuel(player, 2)) {
				player.getDueling().removeStakedItem(id, 5);
				return;
			}
			break;
		case Bank.INTERFACE_ID:
			if (!player.isBanking() || item.getId() != id || player.getInterfaceId() != 5292)
				return;
			if (player.getGameMode() == GameMode.ULTIMATE_IRONMAN) {
				player.getPacketSender()
						.sendMessage("@red@Warning: you can't attempt to deposit more than 30 items as hc iron man");
				player.getPacketSender().sendMessage(
						"@red@Warning: if you try they will be lost. The bank is just for storing your 28 items.");

			}
			player.getBank(player.getCurrentBankTab()).switchItem(player.getInventory(), new Item(id, 5), slot, true,
					true);
			player.getBank(player.getCurrentBankTab()).open(player, false);
			break;
		case Bank.INVENTORY_INTERFACE_ID:
			item = player.getInventory().forSlot(slot).copy().setAmount(5).copy();
			if (!player.isBanking() || item.getId() != id || !player.getInventory().contains(item.getId())
					|| player.getInterfaceId() != 5292)
				return;
			if (player.getGameMode() == GameMode.ULTIMATE_IRONMAN) {
				player.getPacketSender().sendMessage("You can only withdraw items here, not deposit");
				break;
			}
			player.setCurrentBankTab(Bank.getTabForItem(player, item.getId()));
			player.getInventory().switchItem(player.getBank(player.getCurrentBankTab()), item, slot, false, true);
			break;
			case Shop.INVENTORY_INTERFACE_ID:
				Shop shop = player.getShop();
				if (shop != null) {
					item = player.getInventory().getItems()[slot].copy().setAmount(1);
					if (!shop.buys(item)) {
						player.getPacketSender().sendMessage("You cannot sell this here!");
						return;
					}
					player.getInventory().switchItemShops(shop, item, slot, false, true);
					player.getPacketSender().sendItemContainer(player.getInventory(), Shop.INVENTORY_INTERFACE_ID);
				}
				break;
			case Shop.ITEM_CHILD_ID:
				shop = player.getShop();
				if (shop != null) {
					item = shop.getItems()[slot].copy().setAmount(1);
					shop.switchItemShops(player.getInventory(), item, slot, false, true);
				}
				break;
		case BeastOfBurden.INTERFACE_ID:
			if (player.getInterfaceId() == BeastOfBurden.INTERFACE_ID
					&& player.getSummoning().getBeastOfBurden() != null) {
				if (item.getDefinition().isStackable()) {
					player.getPacketSender().sendMessage("You cannot store stackable items.");
					return;
				}
				player.getInventory().switchItem(player.getSummoning().getBeastOfBurden(), new Item(id, 5), slot, false,
						true);
			}
			break;
		case PriceChecker.INTERFACE_PC_ID:
			if (player.getInterfaceId() == PriceChecker.INTERFACE_ID && player.getPriceChecker().isOpen()) {
				player.getInventory().switchItem(player.getPriceChecker(), new Item(id, 5), slot, false, true);
			}
			break;
		case 1119: // smithing interface row 1
		case 1120: // row 2
		case 1121: // row 3
		case 1122: // row 4
		case 1123: // row 5
			int barsRequired = SmithingData.getBarAmount(item);
			Item bar = new Item(player.getSelectedSkillingItem(), barsRequired);
			int x = 5;
			if (x > (player.getInventory().getAmount(bar.getId()) / barsRequired))
				x = (player.getInventory().getAmount(bar.getId()) / barsRequired);
			EquipmentMaking.smithItem(player, new Item(player.getSelectedSkillingItem(), barsRequired),
					new Item(item.getId(), SmithingData.getItemAmount(item)), x);
			break;
		}

		if (BeastOfBurden.beastOfBurdenSlot(interfaceId) >= 0) {
			if (player.getInterfaceId() == BeastOfBurden.INTERFACE_ID
					&& player.getSummoning().getBeastOfBurden() != null) {
				player.getSummoning().getBeastOfBurden().switchItem(player.getInventory(), new Item(id, 5),
						BeastOfBurden.beastOfBurdenSlot(interfaceId), false, true);
			}
		} else if (PriceChecker.priceCheckerSlot(interfaceId) >= 0) {
			if (player.getPriceChecker().isOpen()) {
				player.getPriceChecker().switchItem(player.getInventory(), new Item(id, 5),
						PriceChecker.priceCheckerSlot(interfaceId), false, true);
			}
		}
	}

	/**
	 * Manages an item's third action.
	 * 
	 * @param player The player clicking the item.
	 * @param packet The packet to read values from.
	 */
	private static void thirdAction(Player player, Packet packet) {
		int interfaceId = packet.readLEShort();
		int id = packet.readUnsignedShortA();
		int slot = packet.readShortA();

		Item item1 = new Item(id);

		Debug.write(player.getName(), "ItemContainerActionPacketListener - thirdAction", new String[] {
				"id: "+id,
				"slot: "+slot,
				"interfaceId: "+interfaceId,
		});

		InterfaceOption interfaceOption = InterfaceOptionManager.getOption(interfaceId, DefaultInterfaceOption.OPTION_3);

		if(interfaceOption != null) {
			interfaceOption.onAction(player, id);
		}

		switch (interfaceId) {
		case PartyRoomManager.INVENTORY_ITEMS:
			PartyRoomManager.deposit(player, new Item(id, 10), slot);
			break;
		case PartyRoomManager.DEPOSIT_INVENTORY:
			PartyRoomManager.withdraw(player, new Item(id, 10), slot);
			break;
			case Shop.INVENTORY_INTERFACE_ID:
				Shop shop = player.getShop();
				if (shop != null) {
					item1 = player.getInventory().getItems()[slot].copy().setAmount(5);
					if (!shop.buys(item1)) {
						player.getPacketSender().sendMessage("You cannot sell this here!");
						return;
					}
					player.getInventory().switchItemShops(shop, item1, slot, false, true);
					player.getPacketSender().sendItemContainer(player.getInventory(), Shop.INVENTORY_INTERFACE_ID);
				}
				break;
			case Shop.ITEM_CHILD_ID:
				shop = player.getShop();
				if (shop != null) {
					item1 = shop.getItems()[slot].copy().setAmount(5);
					if (item1.getId() == id)
						shop.switchItemShops(player.getInventory(), item1, slot, false, true);
				}
				break;
		case -29426:
			player.getRunePouch().withdraw(id, slot, 10);
			break;
		case -29425:
			player.getRunePouch().deposit(id, slot, 10);
		break;
		case Equipment.INVENTORY_INTERFACE_ID:
			if (!player.getEquipment().contains(id))
				return;
			
			DegradingManager.checkCharge(player, id);
			switch (id) {
			case CrawsBow.CRAWS_BOW:
				CrawsBow.check(player);
				break;
			case 1712:
			case 1710:
			case 1708:
			case 1706:
			case 11118:
			case 11120:
			case 11122:
			case 11124:
				JewelryTeleporting.rub(player, id);
				break;
			case 13281:
			case 13282:
			case 13283:
			case 13284:
			case 13285:
			case 13286:
			case 13287:
			case 13288:
				player.getSlayer().handleSlayerRingTP(id, true);
				break;
			case 1704:
				player.getPacketSender().sendMessage("Your amulet has run out of charges.");
				break;
			case 11126:
				player.getPacketSender().sendMessage("Your bracelet has run out of charges.");
				break;

			case 51285:
				player.performGraphic(new Graphic(4381));
				player.performAnimation(new Animation(1500));
				player.setCharacterAnimations(new CharacterAnimations(1501, 1501, 1501));
				player.getUpdateFlag().flag(Flag.APPEARANCE);
				player.toggleFloating();
				break;

			case 21000:
				player.performAnimation(new Animation(751));
				player.performGraphic(new Graphic(503));
				break;

			case 11614:
				player.performAnimation(new Animation(4381));
				player.performGraphic(new Graphic(2745));
				break;

			case 43329:
				player.performAnimation(new Animation(22381));
				player.performGraphic(new Graphic(4137));
				break;

			case 14019:
			case 14022:
				player.performAnimation(new Animation(22381));
				player.performGraphic(new Graphic(4250));
				break;

			case 52316:
				player.performAnimation(new Animation(14610));
				player.performGraphic(new Graphic(333));
				break;

			case 11613:
			case 11283:
			case 11284:
			case 52003:

				int charges = player.getDfsCharges();
				if (charges >= 20) {
					if (player.getCombatBuilder().isAttacking())
						CombatFactory.handleDragonFireShield(player, player.getCombatBuilder().getVictim());
					else
						player.getPacketSender().sendMessage("You can only use this in combat.");
				} else
					player.getPacketSender().sendMessage("Your shield doesn't have enough power yet. It has "
							+ player.getDfsCharges() + "/20 dragon-fire charges.");
				break;
			}
			break;
		case Trading.INTERFACE_ID:
			if (player.getTrading().inTrade()) {
				player.getTrading().tradeItem(id, 10, slot);
			} else if (Dueling.checkDuel(player, 1) || Dueling.checkDuel(player, 2)) {
				player.getDueling().stakeItem(id, 10, slot);
			}
			break;
		case Trading.INTERFACE_REMOVAL_ID:
			if (player.getTrading().inTrade())
				player.getTrading().removeTradedItem(id, 10);
			break;
		case Dueling.INTERFACE_REMOVAL_ID:
			if (Dueling.checkDuel(player, 1) || Dueling.checkDuel(player, 2)) {
				player.getDueling().removeStakedItem(id, 10);
				return;
			}
			break;
		case Bank.INTERFACE_ID:
			if (!player.isBanking() || player.getInterfaceId() != 5292)
				return;
			if (player.getGameMode() == GameMode.ULTIMATE_IRONMAN) {
				player.getPacketSender()
						.sendMessage("@red@Warning: you can't attempt to deposit more than 30 items as hc iron man");
				player.getPacketSender().sendMessage(
						"@red@Warning: if you try they will be lost. The bank is just for storing your 28 items.");

			}
			player.getBank(player.getCurrentBankTab()).switchItem(player.getInventory(), new Item(id, 10), slot, true,
					true);
			player.getBank(player.getCurrentBankTab()).open(player, false);
			break;
		case Bank.INVENTORY_INTERFACE_ID:
			Item item = player.getInventory().forSlot(slot).copy().setAmount(10).copy();
			if (!player.isBanking() || item.getId() != id || !player.getInventory().contains(item.getId())
					|| player.getInterfaceId() != 5292)
				return;
			if (player.getGameMode() == GameMode.ULTIMATE_IRONMAN) {
				player.getPacketSender().sendMessage("You can only withdraw items here, not deposit");
				break;
			}
			player.setCurrentBankTab(Bank.getTabForItem(player, item.getId()));
			player.getInventory().switchItem(player.getBank(player.getCurrentBankTab()), item, slot, false, true);
			break;
		case BeastOfBurden.INTERFACE_ID:
			if (player.getInterfaceId() == BeastOfBurden.INTERFACE_ID
					&& player.getSummoning().getBeastOfBurden() != null) {
				Item storeItem = new Item(id, 10);
				if (storeItem.getDefinition().isStackable()) {
					player.getPacketSender().sendMessage("You cannot store stackable items.");
					return;
				}
				player.getInventory().switchItem(player.getSummoning().getBeastOfBurden(), storeItem, slot, false,
						true);
			}
			break;
		case PriceChecker.INTERFACE_PC_ID:
			if (player.getInterfaceId() == PriceChecker.INTERFACE_ID && player.getPriceChecker().isOpen()) {
				player.getInventory().switchItem(player.getPriceChecker(), new Item(id, 10), slot, false, true);
			}
			break;
		case 1119: // smithing interface row 1
		case 1120: // row 2
		case 1121: // row 3
		case 1122: // row 4
		case 1123: // row 5
			int barsRequired = SmithingData.getBarAmount(item1);
			Item bar = new Item(player.getSelectedSkillingItem(), barsRequired);
			int x = 10;
			if (x > (player.getInventory().getAmount(bar.getId()) / barsRequired))
				x = (player.getInventory().getAmount(bar.getId()) / barsRequired);
			EquipmentMaking.smithItem(player, new Item(player.getSelectedSkillingItem(), barsRequired),
					new Item(item1.getId(), SmithingData.getItemAmount(item1)), x);
			break;
		}

		if (BeastOfBurden.beastOfBurdenSlot(interfaceId) >= 0) {
			if (player.getInterfaceId() == BeastOfBurden.INTERFACE_ID
					&& player.getSummoning().getBeastOfBurden() != null) {
				player.getSummoning().getBeastOfBurden().switchItem(player.getInventory(), new Item(id, 10),
						BeastOfBurden.beastOfBurdenSlot(interfaceId), false, true);
			}
		} else if (PriceChecker.priceCheckerSlot(interfaceId) >= 0) {
			if (player.getPriceChecker().isOpen()) {
				player.getPriceChecker().switchItem(player.getInventory(), new Item(id, 10),
						PriceChecker.priceCheckerSlot(interfaceId), false, true);
			}
		}
	}

	/**
	 * Manages an item's fourth action.
	 * 
	 * @param player The player clicking the item.
	 * @param packet The packet to read values from.
	 */
	private static void fourthAction(Player player, Packet packet) {
		int slot = packet.readShortA();
		int interfaceId = packet.readShort();
		int id = packet.readUnsignedShortA();

		Debug.write(player.getName(), "ItemContainerActionPacketListener - fourthAction", new String[] {
				"id: "+id,
				"slot: "+slot,
				"interfaceId: "+interfaceId,
		});

		InterfaceOption interfaceOption = InterfaceOptionManager.getOption(interfaceId, DefaultInterfaceOption.OPTION_4);

		if(interfaceOption != null) {
			interfaceOption.onAction(player, id);
		}

		switch (interfaceId) {
		case PartyRoomManager.INVENTORY_ITEMS:
			PartyRoomManager.deposit(player, new Item(id, player.getInventory().getAmount(id)), slot);
			break;
		case PartyRoomManager.DEPOSIT_INVENTORY:
			PartyRoomManager.withdraw(player, new Item(id, player.getPartyRoom().getDeposit().getAmount(id)), slot);
			break;
		case -29426:
			player.getRunePouch().withdraw(id, slot, player.getRunePouch().getAmountForSlot(slot));
			break;
		case -29425:
			player.getRunePouch().deposit(id, slot, player.getInventory().getAmount(id));
			break;
		case Trading.INTERFACE_ID:
			if (player.getTrading().inTrade()) {
				player.getTrading().tradeItem(id, player.getInventory().getAmount(id), slot);
			} else if (Dueling.checkDuel(player, 1) || Dueling.checkDuel(player, 2)) {
				player.getDueling().stakeItem(id, player.getInventory().getAmount(id), slot);
			}
			break;
		case Trading.INTERFACE_REMOVAL_ID:
			if (player.getTrading().inTrade()) {
				for (Item item : player.getTrading().offeredItems) {
					if (item != null && item.getId() == id) {
						player.getTrading().removeTradedItem(id, item.getAmount());
						if (ItemDefinition.forId(id) != null && ItemDefinition.forId(id).isStackable())
							break;
					}
				}
			}
			break;
			case Shop.INVENTORY_INTERFACE_ID:
				Shop shop = player.getShop();
				if (shop != null) {
					Item item = player.getInventory().getItems()[slot].copy().setAmount(10);
					if (!shop.buys(item)) {
						player.getPacketSender().sendMessage("You cannot sell this here!");
						return;
					}
					player.getInventory().switchItemShops(shop, item, slot, false, true);
					player.getPacketSender().sendItemContainer(player.getInventory(), Shop.INVENTORY_INTERFACE_ID);
				}
				break;
			case Shop.ITEM_CHILD_ID:
				shop = player.getShop();
				if (shop != null) {
					Item item = shop.getItems()[slot].copy().setAmount(10);
					if (item.getId() == id)
						shop.switchItemShops(player.getInventory(), item, slot, false, true);
				}
				break;
		case Dueling.INTERFACE_REMOVAL_ID:
			if (Dueling.checkDuel(player, 1) || Dueling.checkDuel(player, 2)) {
				for (Item item : player.getDueling().stakedItems) {
					if (item != null && item.getId() == id) {
						player.getDueling().removeStakedItem(id, item.getAmount());
						if (ItemDefinition.forId(id) != null && ItemDefinition.forId(id).isStackable())
							break;
					}
				}
			}
			break;
		case Bank.INTERFACE_ID:
			if (!player.isBanking() || player.getBank(Bank.getTabForItem(player, id)).getAmount(id) <= 0
					|| player.getInterfaceId() != 5292)
				return;
			if (player.getGameMode() == GameMode.ULTIMATE_IRONMAN) {
				player.getPacketSender()
						.sendMessage("@red@Warning: you can't attempt to deposit more than 30 items as hc iron man");
				player.getPacketSender().sendMessage(
						"@red@Warning: if you try they will be lost. The bank is just for storing your 28 items.");

			}
			player.getBank(player.getCurrentBankTab()).switchItem(player.getInventory(),
					new Item(id, player.getBank(Bank.getTabForItem(player, id)).getAmount(id)), slot, true, true);
			player.getBank(player.getCurrentBankTab()).open(player, false);
			break;
		case Bank.INVENTORY_INTERFACE_ID:
			Item fromSlot = player.getInventory().forSlot(slot);
			if (fromSlot == null) {
				return;
			}
			Item item = fromSlot.copy().setAmount(player.getInventory().getAmount(id));
			if (!player.isBanking() || item.getId() != id || !player.getInventory().contains(item.getId())
					|| player.getInterfaceId() != 5292)
				return;
			if (player.getGameMode() == GameMode.ULTIMATE_IRONMAN) {
				player.getPacketSender().sendMessage("You can only withdraw items here, not deposit");
				break;
			}
			player.setCurrentBankTab(Bank.getTabForItem(player, item.getId()));
			player.getInventory().switchItem(player.getBank(player.getCurrentBankTab()), item, slot, false, true);
			break;
		case BeastOfBurden.INTERFACE_ID:
			if (player.getInterfaceId() == BeastOfBurden.INTERFACE_ID
					&& player.getSummoning().getBeastOfBurden() != null) {
				Item storeItem = new Item(id, 29);
				if (storeItem.getDefinition().isStackable()) {
					player.getPacketSender().sendMessage("You cannot store stackable items.");
					return;
				}
				player.getInventory().switchItem(player.getSummoning().getBeastOfBurden(), storeItem, slot, false,
						true);
			}
			break;
		case PriceChecker.INTERFACE_PC_ID:
			if (player.getInterfaceId() == PriceChecker.INTERFACE_ID && player.getPriceChecker().isOpen()) {
				player.getInventory().switchItem(player.getPriceChecker(),
						new Item(id, player.getInventory().getAmount(id)), slot, false, true);
			}
			break;
		}

		if (BeastOfBurden.beastOfBurdenSlot(interfaceId) >= 0) {
			if (player.getInterfaceId() == BeastOfBurden.INTERFACE_ID
					&& player.getSummoning().getBeastOfBurden() != null) {
				player.getSummoning().getBeastOfBurden().switchItem(player.getInventory(), new Item(id, 29),
						BeastOfBurden.beastOfBurdenSlot(interfaceId), false, true);
			}
		} else if (PriceChecker.priceCheckerSlot(interfaceId) >= 0) {
			if (player.getPriceChecker().isOpen()) {
				player.getPriceChecker().switchItem(player.getInventory(),
						new Item(id, player.getPriceChecker().getAmount(id)),
						PriceChecker.priceCheckerSlot(interfaceId), false, true);
			}
		}
	}

	/**
	 * Manages an item's fifth action.
	 * 
	 * @param player The player clicking the item.
	 * @param packet The packet to read values from.
	 */
	private static void fifthAction(Player player, Packet packet) {
		int slot = packet.readLEShort();
		int interfaceId = packet.readShortA();
		int id = packet.readLEShort() & 0xffff;

		Item item1 = new Item(id);

		Debug.write(player.getName(), "ItemContainerActionPacketListener - fifthAction", new String[] {
				"id: "+id,
				"slot: "+slot,
				"interfaceId: "+interfaceId,
		});

		InterfaceOption interfaceOption = InterfaceOptionManager.getOption(interfaceId, DefaultInterfaceOption.OPTION_5);

		if(interfaceOption != null) {
			interfaceOption.onAction(player, id);
		}

		switch (interfaceId) {
		case PartyRoomManager.INVENTORY_ITEMS:
			player.setInputHandling(new EnterAmount() {

				@Override
				public boolean handleAmount(Player player, int amount) {
					PartyRoomManager.deposit(player, new Item(id, amount), slot);
					return false;
				}
			});
			player.getPacketSender().sendEnterAmountPrompt("How many would you like to deposit?");
			break;
		case PartyRoomManager.DEPOSIT_INVENTORY:
			player.setInputHandling(new EnterAmount() {

				@Override
				public boolean handleAmount(Player player, int amount) {
					PartyRoomManager.withdraw(player, new Item(id, amount), slot);
					return false;
				}
			});
			player.getPacketSender().sendEnterAmountPrompt("How many would you like to withdraw?");
			break;
		case -29426:
			player.setInputHandling(new EnterAmount() {

				@Override
				public boolean handleAmount(Player player, int amount) {
					player.getRunePouch().withdraw(id, slot, amount);
					return false;
				}

			});
			player.getPacketSender().sendEnterAmountPrompt("How many would you like to withdraw?");
			break;
		case -29425:
			player.setInputHandling(new EnterAmount() {

				@Override
				public boolean handleAmount(Player player, int amount) {
					player.getRunePouch().deposit(id, slot, amount);
					return false;
				}

			});
			player.getPacketSender().sendEnterAmountPrompt("How many would you like to deposit?");
			break;
		case Trading.INTERFACE_ID:
			if (player.getTrading().inTrade()) {
				player.setInputHandling(new EnterAmountToTrade(id, slot));
				player.getPacketSender().sendEnterAmountPrompt("How many would you like to trade?");
			} else if (Dueling.checkDuel(player, 1) || Dueling.checkDuel(player, 2)) {
				player.setInputHandling(new EnterAmountToStake(id, slot));
				player.getPacketSender().sendEnterAmountPrompt("How many would you like to stake?");
			}
			break;
		case Trading.INTERFACE_REMOVAL_ID:
			if (player.getTrading().inTrade()) {
				player.setInputHandling(new EnterAmountToRemoveFromTrade(id));
				player.getPacketSender().sendEnterAmountPrompt("How many would you like to remove?");
			}
			break;
		case Shop.INVENTORY_INTERFACE_ID:
			if (player.getShop() == null)
				return;
			Item item = player.getInventory().getItems()[slot].copy();
			if (item.getId() == id) {
				player.setInputHandling(new EnterAmountToSellShop(item));
				player.getPacketSender().sendEnterAmountPrompt("How many would you like to sell?");
			}
			break;
		case Shop.ITEM_CHILD_ID:
			if(player.lastBuyX > 0) {
				Shop shop = player.getShop();
				if (shop != null) {

					if(player.lastBuyX > 500 && shop.getItems()[slot].getDefinition().isStackable()) {
						player.sendMessage("You can only buy 500 at a time of stackable items.");
						player.lastBuyX = 500;
					}

					Item item2 = shop.getItems()[slot].copy().setAmount(player.lastBuyX);
					if (item2.getId() == id)
						shop.switchItemShops(player.getInventory(), item2, slot, false, true);
				}
			} else {
				player.setInputHandling(new EnterAmountToBuyShop(slot));
				player.getPacketSender().sendEnterAmountPrompt("How many would you like to buy?");
			}
			break;
		case Dueling.INTERFACE_REMOVAL_ID:
			if (Dueling.checkDuel(player, 1) || Dueling.checkDuel(player, 2)) {
				player.setInputHandling(new EnterAmountToRemoveFromStake(id));
				player.getPacketSender().sendEnterAmountPrompt("How many would you like to remove?");
			}
			break;
		case Bank.INVENTORY_INTERFACE_ID: // BANK X
			if (player.getGameMode() == GameMode.ULTIMATE_IRONMAN) {
				player.getPacketSender()
						.sendMessage("@red@Warning: you can't attempt to deposit more than 30 items as hc iron man");
				player.getPacketSender().sendMessage(
						"@red@Warning: if you try they will be lost. The bank is just for storing your 28 items.");

			}
			if (player.isBanking()) {
				player.setInputHandling(new EnterAmountToBank(id, slot));
				player.getPacketSender().sendEnterAmountPrompt("How many would you like to bank?");
			}
			break;

		case Bank.INTERFACE_ID:
		case 11:
			if (player.getGameMode() == GameMode.ULTIMATE_IRONMAN) {
				player.getPacketSender().sendMessage("You can only withdraw items here, not deposit");
				break;
			}
			if (player.isBanking()) {
				if (interfaceId == 11) {
					player.setInputHandling(new EnterAmountToRemoveFromBank(id, slot));
					player.getPacketSender().sendEnterAmountPrompt("How many would you like to withdraw?");
				} else {
					int amount = player.getBank(Bank.getTabForItem(player, id)).getAmount(id);
					player.getBank(player.getCurrentBankTab()).switchItem(player.getInventory(),
							new Item(id, amount == 1 ? 1 : amount - 1), slot, true,
							true);
					player.getBank(player.getCurrentBankTab()).open(player, false);
				}
			}
			break;
		case PriceChecker.INTERFACE_PC_ID:
			if (player.getInterfaceId() == PriceChecker.INTERFACE_ID && player.getPriceChecker().isOpen()) {
				player.setInputHandling(new EnterAmountToPriceCheck(id, slot));
				player.getPacketSender().sendEnterAmountPrompt("How many would you like to pricecheck?");
			}
			break;
		case BeastOfBurden.INTERFACE_ID:
			if (player.getInterfaceId() == BeastOfBurden.INTERFACE_ID
					&& player.getSummoning().getBeastOfBurden() != null) {
				Item storeItem = new Item(id, 10);
				if (storeItem.getDefinition().isStackable()) {
					player.getPacketSender().sendMessage("You cannot store stackable items.");
					return;
				}
				player.setInputHandling(new EnterAmountToStore(id, slot));
				player.getPacketSender().sendEnterAmountPrompt("How many would you like to store?");
			}
			break;

			case 1119: // smithing interface row 1
			case 1120: // row 2
			case 1121: // row 3
			case 1122: // row 4
			case 1123: // row 5
				int barsRequired = SmithingData.getBarAmount(item1);
				Item bar = new Item(player.getSelectedSkillingItem(), barsRequired);
				int x = player.getInventory().getAmount(bar.getId());
				if (x > (player.getInventory().getAmount(bar.getId()) / barsRequired))
					x = (player.getInventory().getAmount(bar.getId()) / barsRequired);
				EquipmentMaking.smithItem(player, new Item(player.getSelectedSkillingItem(), barsRequired),
						new Item(item1.getId(), SmithingData.getItemAmount(item1)), x);
				break;

		}

		if (BeastOfBurden.beastOfBurdenSlot(interfaceId) >= 0) {
			if (player.getInterfaceId() == BeastOfBurden.INTERFACE_ID
					&& player.getSummoning().getBeastOfBurden() != null) {
				player.setInputHandling(new EnterAmountToRemoveFromBob(id, slot));
				player.getPacketSender().sendEnterAmountPrompt("How many would you like to remove?");
			}
		} else if (PriceChecker.priceCheckerSlot(interfaceId) >= 0) {
			if (player.getPriceChecker().isOpen()) {
				player.setInputHandling(new EnterAmountToRemoveFromPriceCheck(id, slot));
				player.getPacketSender().sendEnterAmountPrompt("How many would you like to remove?");
			}
		}
	}

	private static void sixthAction(Player player, Packet packet) {
		int interfaceId = packet.readShortA();
		int slot = packet.readShortA();
		int id = packet.readUnsignedShortA();

		Debug.write(player.getName(), "ItemContainerActionPacketListener - sixthAction", new String[] {
				"id: "+id,
				"slot: "+slot,
				"interfaceId: "+interfaceId,
		});

		switch (interfaceId) {
			case Shop.ITEM_CHILD_ID:
				player.setInputHandling(new EnterAmountToBuyShop(slot));
				player.getPacketSender().sendEnterAmountPrompt("How many would you like to buy?");
				break;
		}
	}

	private static void eightAction(Player player, Packet packet) {
		int interfaceId = packet.readInt();
		int slot = packet.readLEShort();
		int id = packet.readLEShort();

		Debug.write(player.getName(), "ItemContainerActionPacketListener - eightAction", new String[] {
				"id: "+id,
				"slot: "+slot,
				"interfaceId: "+interfaceId,
		});

		// Bank withdrawal..
		if (interfaceId == Bank.INTERFACE_ID) {
			boolean temp = player.isPlaceholders();
			player.setPlaceholders(true);
			player.getBank(player.getCurrentBankTab()).switchItem(player.getInventory(),
					new Item(id, player.getBank(player.getCurrentBankTab()).getAmount(id)), slot, true, true);
			player.setPlaceholders(temp);
		}
	}

	@Override
	public void handleMessage(Player player, Packet packet) {
		if (player.getConstitution() <= 0) {
			return;
		}

		if (player.isAccountCompromised() || player.requiresUnlocking()) {
			return;
		}

		switch (packet.getOpcode()) {
		case FIRST_ITEM_ACTION_OPCODE:
			firstAction(player, packet);
			break;
		case SECOND_ITEM_ACTION_OPCODE:
			secondAction(player, packet);
			break;
		case THIRD_ITEM_ACTION_OPCODE:
			thirdAction(player, packet);
			break;
		case FOURTH_ITEM_ACTION_OPCODE:
			fourthAction(player, packet);
			break;
		case FIFTH_ITEM_ACTION_OPCODE:
			fifthAction(player, packet);
			break;
		case SIXTH_ITEM_ACTION_OPCODE:
			sixthAction(player, packet);
			break;
		case EIGHT_ITEM_ACTION_OPCODE:
			eightAction(player, packet);
			break;
		}
	}

	public static final int FIRST_ITEM_ACTION_OPCODE = 145;
	public static final int SECOND_ITEM_ACTION_OPCODE = 117;
	public static final int THIRD_ITEM_ACTION_OPCODE = 43;
	public static final int FOURTH_ITEM_ACTION_OPCODE = 129;
	public static final int FIFTH_ITEM_ACTION_OPCODE = 135;
	public static final int SIXTH_ITEM_ACTION_OPCODE = 138;
	public static final int EIGHT_ITEM_ACTION_OPCODE = 142;
}