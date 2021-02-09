package com.zyrox.saving.impl;

import java.util.ArrayList;
import java.util.List;

import com.zyrox.saving.SaveStringList;
import com.zyrox.world.content.degrade.DegradingItem;
import com.zyrox.world.entity.impl.player.Player;

public class SaveDegradingItems extends SaveStringList {

	public SaveDegradingItems(String name) {
		super(name);
	}

	@Override
	public List<String> getList(Player player) {
		List<String> list = new ArrayList<String>();
		
		for(DegradingItem item : player.getDegrading().getDegradable()) {
			if(item == null) {
				continue;
			}
			String s = item.toString();
			System.out.println(s);
			list.add(s);
		}
		return list;
	}

	@Override
	public void setString(Player player, int index, String value) {
		
		if(value.length() < 1) {
			return;
		}
		
		DegradingItem item = DegradingItem.fromString(value);
		
		player.getDegrading().getDegradable().add(item);
		
	}
}
