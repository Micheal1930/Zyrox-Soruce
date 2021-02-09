package com.zyrox.saving.impl;

import com.zyrox.saving.SaveString;
import com.zyrox.world.entity.impl.player.Player;

public class MessageTimes extends SaveString {

	public MessageTimes(String name) {
		super(name);
	}

	@Override
	public void setValue(Player player, String value) {
		player.setDisplayAnnouncementTimers(Boolean.parseBoolean(value));
	}

	@Override
	public String getValue(Player player) {
		return Boolean.toString(player.isDisplayAnnouncementTimers());
	}

}
