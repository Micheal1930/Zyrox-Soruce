package com.varrock.saving;

import java.util.List;

import com.varrock.world.entity.impl.player.Player;

public abstract class SaveStringList extends SaveStringArray {

	public SaveStringList(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String[] getStringArray(Player player) {
		// TODO Auto-generated method stub
		List<String> list = getList(player);
		String[] array = new String[list.size()];
		int i = 0;
		for(String str: list) {
			array[i] = str;
			i++;
		}
		return array;
	}

	public abstract List<String> getList(Player player);
}
