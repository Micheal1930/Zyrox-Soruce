package com.zyrox.model;

/**
 * Represents a player's privilege rights.
 * 
 * @author relex lawl
 */

public enum PlayerInteractingOption {

	NONE,
	INVITE,
	CHALLENGE,
	ATTACK;

	public static PlayerInteractingOption forName(String name) {
		if(name.toLowerCase().contains("null"))
			return NONE;
		for(PlayerInteractingOption option : PlayerInteractingOption.values()) {
			if(option.toString().equalsIgnoreCase(name)) {
				return option;
			}
		}
		return null;
	}
}
