package com.varrock.net.packet.command.impl;

import java.util.Arrays;

import com.varrock.model.OfflineCharacter;
import com.varrock.model.input.Input;
import com.varrock.net.packet.command.CommandHeader;
import com.varrock.net.packet.command.NameCommand;
import com.varrock.saving.PlayerSaving;
import com.varrock.world.content.dialogue.DialogueManager;
import com.varrock.world.entity.impl.player.Player;

@CommandHeader(command = { "removewallet" }, description = "Remove's an amount from a player's wallet.")

public class RemoveWalletCommand extends NameCommand {

	public RemoveWalletCommand() {
		super("Finch", "Jonny");
	}

	@Override
	public boolean execute(Player player, String[] args) throws Exception {
		if (args.length < 1) {
			throw new IllegalArgumentException();
		}

		String username = String.join(" ", Arrays.copyOfRange(args, 0, args.length));

		player.setInputHandling(new Input() {
			@Override
			public boolean handleAmount(Player player, int amount) {

				Player other = OfflineCharacter.getOfflineCharacter(username);

				if(other == null) {
					DialogueManager.sendStatement(player, "@red@"+username+" does not exist.");
					return true;
				}

				if(other.wallet < amount) {
					DialogueManager.sendStatement(player, "@red@This player only has $"+player.wallet+" in his wallet.");
					return true;
				}

				other.wallet -= amount;

				PlayerSaving.getSaving().save(other);

				DialogueManager.sendStatement(player, "@mag@Removed $"+amount+" from "+username+"'s wallet.");
				return true;
			}
		});

		player.getPA().sendEnterAmountPrompt("Enter amount to remove from "+username+"'s wallet:");

		return true;
	}

	@Override
	public String[] getUsage() {
		return new String[] {
			"::removewallet [name]",
		};
	}

}
