package com.zyrox.net.packet.command.impl;

import java.util.Arrays;

import com.zyrox.model.OfflineCharacter;
import com.zyrox.net.packet.command.CommandHeader;
import com.zyrox.net.packet.command.NameCommand;
import com.zyrox.world.content.dialogue.DialogueManager;
import com.zyrox.world.entity.impl.player.Player;

@CommandHeader(command = { "checkwallet" }, description = "Checks a player's wallet.")

public class CheckWalletCommand extends NameCommand {

	public CheckWalletCommand() {
		super("Finch", "Jonny");
	}

	@Override
	public boolean execute(Player player, String[] args) throws Exception {
		if (args.length < 1) {
			throw new IllegalArgumentException();
		}

		String username = String.join(" ", Arrays.copyOfRange(args, 0, args.length));

		Player other = OfflineCharacter.getOfflineCharacter(username);

		if(other == null) {
			DialogueManager.sendStatement(player, "@red@"+username+" does not exist.");
			return true;
		}

		DialogueManager.sendStatement(player, "@mag@"+username+" has $"+other.wallet+" in his wallet.");

		return true;
	}

	@Override
	public String[] getUsage() {
		return new String[] {
			"::checkwallet [name]",
		};
	}

}
