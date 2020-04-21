package com.varrock.net.packet.impl;

import com.varrock.engine.task.impl.WalkToTask;
import com.varrock.engine.task.impl.WalkToTask.FinalizedMovementTask;
import com.varrock.model.GroundItem;
import com.varrock.model.Item;
import com.varrock.model.Position;
import com.varrock.model.definitions.ItemDefinition;
import com.varrock.model.log.impl.DropItemLog;
import com.varrock.model.log.impl.PickupItemLog;
import com.varrock.net.packet.Packet;
import com.varrock.net.packet.PacketListener;
import com.varrock.util.Misc;
import com.varrock.world.content.Debug;
import com.varrock.world.content.PlayerLogs;
import com.varrock.world.entity.impl.GroundItemManager;
import com.varrock.world.entity.impl.player.Player;

/**
 * This packet listener is used to pick up ground items
 * that exist in the world.
 * 
 * @author relex lawl
 */

public class PickupItemPacketListener implements PacketListener {

	@Override
	public void handleMessage(final Player player, Packet packet) {
		final int y = packet.readLEShort();
		final int itemId = packet.readUnsignedShort();
		final int x = packet.readLEShort();

		if(player.isTeleporting())
			return;
		final Position position = new Position(x, y, player.getPosition().getZ());
		if(!player.getLastItemPickup().elapsed(500))
			return;
		if(player.getConstitution() <= 0 || player.isTeleporting())
			return;

		Debug.write(player.getName(), "PickupItemPacketListener", new String[] {
				"itemId: "+itemId,
				"x: "+x,
				"y: "+y,
		});

		player.setWalkToTask(new WalkToTask(player, position, 1, new FinalizedMovementTask() {
			@Override
			public void execute() {
				if (Math.abs(player.getPosition().getX() - x) > 25 || Math.abs(player.getPosition().getY() - y) > 25) {
					player.getMovementQueue().reset();
					return;
				}
				boolean canPickup = player.getInventory().getFreeSlots() > 0 || (player.getInventory().getFreeSlots() == 0 && ItemDefinition.forId(itemId).isStackable() && player.getInventory().contains(itemId));


				if(!canPickup) {
					player.getInventory().full();
					return;
				}
				GroundItem gItem = GroundItemManager.getGroundItem(player, new Item(itemId), position);
				if(gItem != null) {

					int playerInvAmount = player.getInventory().getAmount(gItem.getItem().getId());
					long totalAmount = (long) playerInvAmount + gItem.getItem().getAmount();

					if (totalAmount > Integer.MAX_VALUE) {
						player.getPacketSender().sendMessage("You cannot hold that amount of this item. Clear your inventory!");
						return;
					}

					if(player.getInventory().getAmount(gItem.getItem().getId()) + gItem.getItem().getAmount() <= 0) {
						player.getPacketSender().sendMessage("You cannot hold that amount of this item. Clear your inventory!");
						return;
					}

					GroundItemManager.pickupGroundItem(player, new Item(itemId), new Position(x, y, player.getPosition().getZ()));

					new PickupItemLog(player.getName(),
							gItem.getItem().getDefinition().getName(),
							gItem.getItem().getId(), gItem.getItem().getAmount(),
							player.getLocation().toString(),
							Misc.getTime()).submit();

				}
			}
		}));
	}
}
