package com.varrock.net.packet.impl;

import com.varrock.GameSettings;
import com.varrock.model.CombatIcon;
import com.varrock.model.Graphic;
import com.varrock.model.GroundItem;
import com.varrock.model.Hit;
import com.varrock.model.Hitmask;
import com.varrock.model.Item;
import com.varrock.model.Locations.Location;
import com.varrock.model.definitions.ItemDefinition;
import com.varrock.model.item.CrawsBow;
import com.varrock.model.log.impl.DropItemLog;
import com.varrock.net.packet.Packet;
import com.varrock.net.packet.PacketListener;
import com.varrock.util.Misc;
import com.varrock.world.World;
import com.varrock.world.content.BankPin;
import com.varrock.world.content.Debug;
import com.varrock.world.content.PlayerLogs;
import com.varrock.world.content.Sounds;
import com.varrock.world.content.PlayerPunishment.Jail;
import com.varrock.world.content.Sounds.Sound;
import com.varrock.world.content.skill.impl.dungeoneering.ItemBinding;
import com.varrock.world.content.skill.impl.summoning.SummoningData;
import com.varrock.world.entity.impl.GroundItemManager;
import com.varrock.world.entity.impl.player.Player;

/**
 * This packet listener is called when a player drops an item they have placed
 * in their inventory.
 * 
 * @author relex lawl
 */

public class DropItemPacketListener implements PacketListener {

	@Override
	public void handleMessage(Player player, Packet packet) {
		int id = packet.readUnsignedShortA();
		int interfaceIndex = packet.readUnsignedShort();
		int itemSlot = packet.readUnsignedShortA();
		if (player.getConstitution() <= 0 || player.getInterfaceId() > 0)
			return;
		if (itemSlot < 0 || itemSlot > player.getInventory().capacity())
			return;
		if (player.getConstitution() <= 0 || player.isTeleporting())
			return;
		if(!GameSettings.DROP_ENABLED){
			player.getPacketSender().sendMessage("Dropping is disabled at the moment!");
			return;
		}
		if (player.requiresUnlocking()) {
			BankPin.init(player, false);
			return;
		}
		if (player.isAccountCompromised()) {
			player.sendMessage("@red@We have a reason to believe that this account was compromised.");
			player.sendMessage("@red@Please contact a staff member to recover this account.");
			return;
		}

		Debug.write(player.getName(), "DropItemPacketListener", new String[] {
				"item id: "+id,
				"interfaceIndex: "+interfaceIndex,
				"itemSlot: "+itemSlot,
		});


		/*if(EasterEvent.canDrop(id)) {
			return;
		}*/

		Item item = player.getInventory().getItems()[itemSlot];

		if (item.getId() != id) {
			return;
		}
		if (SummoningData.isPouch(player, id, 3))
			return;
		if (id == CrawsBow.CRAWS_BOW) {
			CrawsBow.uncharge(player);
			return;
		}

		player.getPacketSender().sendInterfaceRemoval();
		player.getCombatBuilder().cooldown(false);
		if (item != null && item.getId() != -1 && item.getAmount() >= 1) {
			if (item.tradeable(player) && !ItemBinding.isBoundItem(item.getId())) {
				player.getInventory().setItem(itemSlot, new Item(-1, 0)).refreshItems();
				if (item.getId() == 4045) {
					if (Jail.isJailed(player)) {
						return;
					}
					int damage = (player.getConstitution() - 1) == 0 ? 1 : player.getConstitution() - 1;
					if (player.getLocation().equals(Location.CASTLE_WARS_GAME)) {
						damage = 150;
						if (damage > player.getConstitution()) {
							damage = player.getConstitution();
						}
						player.forceChat("Ow!");
					}
					player.dealDamage(new Hit(damage, Hitmask.CRITICAL, CombatIcon.MELEE));
					player.performGraphic(new Graphic(1750));
					player.getPacketSender().sendMessage("The potion explodes in your face as you drop it!");
				} else {

					int value = item.getDefinition().getValue() * item.getAmount();

					if(value >= 10_000_000) {
						World.sendAdminMessage("<img=483> @red@[ Economy ]</col> " + player.getUsername() + " dropped Id: " + item.getId() + "(" + item.getDefinition().getName() + ", amount: " + Misc.insertCommasToNumber(String.valueOf(item.getAmount())));
					}

					GroundItemManager.spawnGroundItem(player, new GroundItem(item, player.getPosition().copy(),
							player.getUsername(), player.getHostAddress(), false, player.getLocation() == Location.THEATRE_OF_BLOOD_FINAL_ROOM ? 0 : 80,
							player.getPosition().getZ() >= 0 && player.getPosition().getZ() < 4 ? true : false, player.getLocation() == Location.THEATRE_OF_BLOOD_FINAL_ROOM ? 0 : 80));

					new DropItemLog(player.getName(),
							item.getDefinition().getName(),
							item.getId(), item.getAmount(),
							player.getLocation().toString(),
							Misc.getTime()).submit();
				}
				Sounds.sendSound(player, Sound.DROP_ITEM);
			} else
				destroyItemInterface(player, item);
		}
	}

	public static void destroyItemInterface(Player player, Item item) {// Destroy item created by Remco
		player.setUntradeableDropItem(item);
		String[][] info = { // The info the dialogue gives
				{ "Are you sure you want to discard this item?", "14174" }, { "Yes.", "14175" }, { "No.", "14176" },
				{ "", "14177" }, { "This item will vanish once it hits the floor.", "14182" },
				{ "You cannot get it back if discarded.", "14183" }, { item.getDefinition().getName(), "14184" } };
		player.getPacketSender().sendItemOnInterface(14171, item.getId(), item.getAmount());
		for (int i = 0; i < info.length; i++)
			player.getPacketSender().sendString(Integer.parseInt(info[i][1]), info[i][0]);
		player.getPacketSender().sendChatboxInterface(14170);
	}
}
