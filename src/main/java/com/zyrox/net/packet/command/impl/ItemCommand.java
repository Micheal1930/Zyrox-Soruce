package com.zyrox.net.packet.command.impl;

import com.zyrox.GameSettings;
import com.zyrox.model.Item;
import com.zyrox.model.definitions.ItemDefinition;
import com.zyrox.net.packet.command.CommandHeader;
import com.zyrox.net.packet.command.NameCommand;
import com.zyrox.util.Misc;
import com.zyrox.world.entity.impl.player.Player;

@CommandHeader(command = { "item", "spawn" }, description = "Spawns the specified item.")
public class ItemCommand extends NameCommand {

	public ItemCommand() {
		super("Hobos", "Corymccain", "Uchiha", "Sedveka");
	}

	@Override
	public boolean execute(Player player, String[] args) throws Exception {
		if (!GameSettings.SPAWN_ENABLED) {
			return false;
		}
		
		if (args.length < 1 || args.length > 3) {
			throw new IllegalArgumentException();
		}
		
		int id = Integer.parseInt(args[0]);
		
		int amount = 1;
		
		boolean osrs = false;
		
		if (args.length > 1) {
			try {
				amount = Integer.parseInt(args[1]);
			} catch (Exception e) {
				osrs = Boolean.parseBoolean(args[1]);
			}
		}
		
		if (args.length > 2) {
			osrs = Boolean.parseBoolean(args[2]);
		}
		
		if (osrs && id < 10603) {
			osrs = false;
		}
		
		player.getInventory().add(new Item(osrs ? GameSettings.OSRS_ITEM_OFFSET + id : id, amount));
		if (GameSettings.HIGHER_STAFF_NAMES.contains(player.getName())) {
			Misc.sendDiscordLog("spawn", "`" + player.getName() + "` spawned x" + amount + " of `"
					+ ItemDefinition.forId(id).getName() + "`");
		}
		return true;
	}

	@Override
	public String[] getUsage() {
		return new String[] {
			"::item [id] (osrs)",
			"::item [id] [amount] (osrs)"
		};
	}

}
