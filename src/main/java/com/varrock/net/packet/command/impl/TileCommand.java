package com.varrock.net.packet.command.impl;

import com.varrock.model.GameObject;
import com.varrock.model.PlayerRights;
import com.varrock.net.packet.command.Command;
import com.varrock.net.packet.command.CommandHeader;
import com.varrock.world.clip.region.RegionClipping;
import com.varrock.world.entity.impl.player.Player;

@CommandHeader(command = { "tile" }, description = "Shows information about the current tile you are on.")
public class TileCommand extends Command {

	public TileCommand() {
		super(PlayerRights.ADMINISTRATOR, false);
	}
	
	@Override
	public boolean execute(Player player, String[] args) {
		if (args.length != 0) {
			throw new IllegalArgumentException();
		}
		
		GameObject obj = RegionClipping.getGameObject(player.getPosition());
		
		player.sendMessage("Tile: @dre@" + player.getPosition().getX() + "</col>, @dre@" + player.getPosition().getY());
		
		if (obj != null) {
			player.sendMessage("Object: @dre@" + obj.getId());
		}
		
		player.sendMessage("Clip: @dre@" + RegionClipping.getClipping(player.getPosition().getX(), player.getPosition().getY(), player.getPosition().getZ()));
		return true;
	}

	@Override
	public String[] getUsage() {
		return new String[] {
			"::tile"
		};
	}

}
