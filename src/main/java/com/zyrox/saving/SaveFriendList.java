package com.zyrox.saving;

import java.util.List;

import com.zyrox.world.entity.impl.player.Player;

public class SaveFriendList extends SaveLongArray {

	public SaveFriendList(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public long[] getLongArray(Player player) {
		// TODO Auto-generated method stub
		List<Long> list = player.getRelations().getFriendList();
		long[] array = new long[list.size()];
		for(int i = 0; i < array.length; i++) {
			array[i] = list.get(i);
		}
		return array;
	}

	@Override
	public void setLongValue(Player player, int index, long value) {
		// TODO Auto-generated method stub
		player.getRelations().getFriendList().add(value);
	}

}
