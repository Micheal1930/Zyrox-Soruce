package com.varrock.net.packet.command.impl;

import com.varrock.GameSettings;
import com.varrock.model.definitions.ItemDefinition;
import com.varrock.net.packet.command.CommandHeader;
import com.varrock.net.packet.command.NameCommand;
import com.varrock.world.entity.impl.player.Player;

@CommandHeader(command = { "getid" }, description = "Get an item ID by its name.")
public class GetIdCommand extends NameCommand {

	public GetIdCommand() {
		super(GameSettings.SPECIAL_STAFF_NAMES, GameSettings.HIGHER_STAFF_NAMES, GameSettings.INVESTIGATOR);
	}
	
	@Override
	public boolean execute(Player player, String[] args) {
		if (args.length == 0) {
			throw new IllegalArgumentException();
		}
		
		String itemName = String.join(" ", args).toLowerCase();
		
		int i = 0;
		
		for (ItemDefinition definition : ItemDefinition.getDefinitions()) {
			if (definition == null) {
				continue;
			}
			
			if (definition.isNoted()) {
				continue;
			}
			
			if (definition.getName().toLowerCase().contains(itemName)) {
				player.sendMessage(definition.getName() + " @dre@- </col>" + definition.getId());
				
				if (i++ == 100) {
					player.sendMessage("Over @dre@100 </col>results have been found!");
					break;
				}
			}
		}
		
		return true;
	}

	@Override
	public String[] getUsage() {
		return new String[] {
			"::getid [item name]"
		};
	}

}
