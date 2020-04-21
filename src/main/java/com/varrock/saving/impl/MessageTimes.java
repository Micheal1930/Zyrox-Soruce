package com.varrock.saving.impl;

import com.varrock.saving.SaveString;
import com.varrock.world.entity.impl.player.Player;

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
