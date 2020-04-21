package com.varrock.world.content.presets;

import com.varrock.model.container.ItemContainer;
import com.varrock.model.container.StackType;
import com.varrock.world.entity.impl.player.Player;

public class PresetContainer extends ItemContainer {
	
	private int interfaceId;
	
	private int capacity;
	
	public PresetContainer(int interfaceId, int capacity, Player player) {
		super(player, capacity);
		this.interfaceId = interfaceId;
		this.capacity = capacity;
	}

	@Override
	public int capacity() {
		return capacity;
	}

	@Override
	public StackType stackType() {
		return StackType.DEFAULT;
	}

	@Override
	public ItemContainer refreshItems() {
		getPlayer().getPacketSender().sendItemContainer(this, interfaceId);
		return this;
	}

	@Override
	public ItemContainer full() {
		return this;
	}

}
