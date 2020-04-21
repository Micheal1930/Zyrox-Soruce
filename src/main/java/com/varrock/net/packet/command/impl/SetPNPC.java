package com.varrock.net.packet.command.impl;

import java.util.Arrays;

import com.varrock.GameSettings;
import com.varrock.model.Flag;
import com.varrock.net.packet.command.CommandHeader;
import com.varrock.net.packet.command.NameCommand;
import com.varrock.world.World;
import com.varrock.world.entity.impl.player.Player;

@CommandHeader(command = { "setpnpc" }, description = "Sets the PNPC id of another player.")
public class SetPNPC extends NameCommand {
	
	public SetPNPC() {
		super(GameSettings.SPECIAL_STAFF_NAMES, GameSettings.HIGHER_STAFF_NAMES);
	}

	@Override
	public boolean execute(Player player, String[] args) throws Exception {
		if (args.length < 1) {
			throw new IllegalArgumentException();
		}
		
		String username = args.length == 1 ? player.getUsername() : String.join(" ", Arrays.copyOfRange(args, 1, args.length));
		
		Player p = World.getPlayerByName(username);
		
		if (p == null) {
			player.sendMessage("That player doesn't seem to be online!");
			return false;
		}
		
		int id = Integer.parseInt(args[0]);
		p.setNpcTransformationId(id);
		p.getUpdateFlag().flag(Flag.APPEARANCE);
		player.getPacketSender().sendMessage("Successfully set the PNPC of '@dre@" + p.getUsername() + "@bla@ to @dre@" + id + "@bla@.");
		return true;
	}

	@Override
	public String[] getUsage() {
		return new String[] {
			"::setpnpc [id]",
			"::setpnpc [id] [player_name]"
		};
	}

}
