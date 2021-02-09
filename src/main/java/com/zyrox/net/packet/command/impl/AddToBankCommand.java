package com.zyrox.net.packet.command.impl;

import java.util.Arrays;

import com.zyrox.GameSettings;
import com.zyrox.model.Item;
import com.zyrox.model.definitions.ItemDefinition;
import com.zyrox.net.packet.command.CommandHeader;
import com.zyrox.net.packet.command.NameCommand;
import com.zyrox.saving.PlayerSaving;
import com.zyrox.util.Misc;
import com.zyrox.world.World;
import com.zyrox.world.entity.impl.player.Player;

@CommandHeader(command = { "addtobank" }, description = "Spawns the specified item into the specified player's bank.")
public class AddToBankCommand extends NameCommand {
	
	public AddToBankCommand() {
		super("Jonny", "Finch");
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
		
		String username = String.join(" ", Arrays.copyOfRange(args, args.length <= 3 ? 2 : 3, args.length));
		
		if (!PlayerSaving.getSaving().exists(username)) {
			player.sendMessage("A player with that name does not exist.");
			return false;
		}
		
		if (args.length > 3) {
			String arg = args[2];
			
			if (arg.length() != 4 && !arg.equalsIgnoreCase("false") && !arg.equalsIgnoreCase("true")) {
				player.sendMessage("Invalid argument '@red@" + arg + "</col>', expected true/false.");
				return false;
			}
			
			osrs = Boolean.parseBoolean(arg);
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
		
		plr.getBank(plr.getCurrentBankTab()).add(new Item(osrs ? GameSettings.OSRS_ITEM_OFFSET + id : id, amount));
		player.sendMessage("Successfully added " + Misc.getAOrAn(def.getName()) + " @dre@" + def.getName() + " </col>(@dre@" + id + "</col>) x @dre@" + amount + " </col>to " + Misc.formatPlayerName(username) + "'s bank.");
		if (GameSettings.HIGHER_STAFF_NAMES.contains(player.getName())) {
			Misc.sendDiscordLog("spawn", "`" + player.getName() + "` gave `" + plr.getName() + "` x" + amount + " of `"
					+ ItemDefinition.forId(id).getName() + "`");
		}
		return true;
	}

	@Override
	public String[] getUsage() {
		return new String[] {
			"::addtobank [id] [amount] (osrs) [username]"
		};
	}

}
