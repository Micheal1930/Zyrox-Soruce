package com.zyrox.net.packet.command.impl;

import java.util.Arrays;

import com.zyrox.model.OfflineCharacter;
import com.zyrox.model.input.Input;
import com.zyrox.net.packet.command.CommandHeader;
import com.zyrox.net.packet.command.NameCommand;
import com.zyrox.saving.PlayerSaving;
import com.zyrox.world.content.dialogue.DialogueManager;
import com.zyrox.world.entity.impl.player.Player;

@CommandHeader(command = { "addwallet" }, description = "Adds an amount to a wallet.")

public class AddWalletCommand extends NameCommand {

	public AddWalletCommand() {
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
				if(amount > 1000) {
					DialogueManager.sendStatement(player, "@red@You can only add up to $1000 at a time.");
					return true;
				}

				Player other = OfflineCharacter.getOfflineCharacter(username);

				if(other == null) {
					DialogueManager.sendStatement(player, "@red@"+username+" does not exist.");
					return true;
				}

				other.wallet += amount;

				PlayerSaving.getSaving().save(other);

				DialogueManager.sendStatement(player, "@mag@Added $"+amount+" to "+username+"'s wallet.");
				return true;
			}
		});

		player.getPA().sendEnterAmountPrompt("Enter amount to add to "+username+"'s wallet:");

		return true;
	}

	@Override
	public String[] getUsage() {
		return new String[] {
			"::addwallet [name]",
		};
	}

}
