package com.varrock.world.content.partyroom;

import com.varrock.model.container.ItemContainer;
import com.varrock.model.container.StackType;
import com.varrock.world.entity.impl.player.Player;

/**
 * Represents the Party Room deposit item container
 * 
 * @author 2012 <https://www.rune-server.ee/members/dexter+morgan/>
 *
 */
public class PartyRoomDepositItemContainer extends ItemContainer {
	/**
	 * The deposit chest container size
	 */
	private static final int DEPOSIT_CONTAINER_SIZE = 7;
	
	/**
	 * Represents the deposit container
	 * @param player the player
	 */
	public PartyRoomDepositItemContainer(Player player) {
		super(player, DEPOSIT_CONTAINER_SIZE);
	}

	@Override
	public int capacity() {
		return DEPOSIT_CONTAINER_SIZE;
	}

	@Override
	public StackType stackType() {
		return null;
	}

	@Override
	public ItemContainer refreshItems() {
		getPlayer().getPacketSender().sendItemContainer(this, PartyRoomManager.DEPOSIT_INVENTORY);
		return null;
	}

	@Override
	public ItemContainer full() {
		return null;
	}
}
