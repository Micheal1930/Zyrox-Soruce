package com.zyrox.net.packet.command.impl;

import com.zyrox.GameSettings;
import com.zyrox.model.PlayerRights;
import com.zyrox.net.packet.command.Command;
import com.zyrox.net.packet.command.CommandHeader;
import com.zyrox.world.World;
import com.zyrox.world.entity.impl.player.Player;

@CommandHeader(command = { "togglelogin" }, description = "Disables the login of new players.")
public class ToggleLoginCommand extends Command {

	public ToggleLoginCommand() {
		super(PlayerRights.ADMINISTRATOR, false);
	}
	
	@Override
	public boolean execute(Player player, String[] args) {
		if (args.length != 0) {
			throw new IllegalArgumentException();
		}
		
		GameSettings.LOGIN_NEW_PLAYERS = !GameSettings.LOGIN_NEW_PLAYERS;
		World.sendAdminMessage(player.getUsername() + " has just toggled the login for new players. It's now @dre@" + (GameSettings.LOGIN_NEW_PLAYERS ? "enabled" : "disabled") + "</col>.");
		return true;
	}

	@Override
	public String[] getUsage() {
		return new String[] {
			"::togglelogin"
		};
	}

}
