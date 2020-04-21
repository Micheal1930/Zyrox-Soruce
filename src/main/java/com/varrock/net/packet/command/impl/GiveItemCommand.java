package com.varrock.net.packet.command.impl;

import java.util.Arrays;

import com.varrock.GameSettings;
import com.varrock.model.Item;
import com.varrock.model.definitions.ItemDefinition;
import com.varrock.net.packet.command.CommandHeader;
import com.varrock.net.packet.command.NameCommand;
import com.varrock.saving.PlayerSaving;
import com.varrock.util.Misc;
import com.varrock.world.World;
import com.varrock.world.entity.impl.player.Player;

@CommandHeader(command = { "giveitem" }, description = "Spawns the specified item for the specified player.")
public class GiveItemCommand extends NameCommand {
	
	public GiveItemCommand() {
		super(GameSettings.SPECIAL_STAFF_NAMES, GameSettings.HIGHER_STAFF_NAMES);
	}

	@Override
	public boolean execute(Player player, String[] args) throws Exception {
		if (!GameSettings.SPAWN_ENABLED) {
			return false;
		}
		
		if (args.length < 3) {
			throw new IllegalArgumentException();
		}
		
		int id = Integer.parseInt(args[0]);
		
		int amount = Integer.parseInt(args[1]);
		
		boolean osrs = false;
		
		int from = 3;
		
		if (args.length > 3) {
			String arg = args[2];
			
			if (!arg.equalsIgnoreCase("false") && !arg.equalsIgnoreCase("true")) {
				osrs = Boolean.parseBoolean(arg);
				from = 2;
			}
		}
		
		String username = String.join(" ", Arrays.copyOfRange(args, args.length <= 3 ? 2 : from, args.length));
		
		if (!PlayerSaving.getSaving().exists(Misc.formatPlayerName(username))) {
			player.sendMessage("A player with that name does not exist.");
			return false;
		}
		
		if (osrs && id < 10603) {
			osrs = false;
		}
		
		ItemDefinition def = ItemDefinition.forId(osrs ? GameSettings.OSRS_ITEM_OFFSET + id : id);
		
		if (def == null) {
			player.sendMessage("Invalid item.");
			return false;
		}
		
		Player plr = World.getPlayerByName(username);
		
		if (plr == null) {
			player.sendMessage("The player '@dre@" + Misc.formatPlayerName(username) + "</col>' is offline.");
			return false;
		}
		
		plr.getInventory().add(new Item(osrs ? GameSettings.OSRS_ITEM_OFFSET + id : id, amount));
		player.sendMessage("Successfully gave " + Misc.getAOrAn(def.getName()) + " @dre@" + def.getName() + " </col>(@dre@" + id + "</col>) x @dre@" + amount + " </col>to " + Misc.formatPlayerName(username) + ".");
		Misc.sendDiscordLog("spawn", "`"+player.getName() + "` gave `"+plr.getName()+"` x" +amount + " of `" + ItemDefinition.forId(id).getName()+"`");
		return true;
	}

	@Override
	public String[] getUsage() {
		return new String[] {
			"::giveitem [id] [amount] (osrs) [username]"
		};
	}

}
