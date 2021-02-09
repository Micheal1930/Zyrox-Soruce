package com.zyrox.net.packet.command.impl;

import java.util.Arrays;

import com.zyrox.model.OfflineCharacter;
import com.zyrox.model.PlayerRights;
import com.zyrox.net.packet.command.CommandHeader;
import com.zyrox.net.packet.command.NameCommand;
import com.zyrox.saving.PlayerSaving;
import com.zyrox.world.World;
import com.zyrox.world.content.PlayerPanel;
import com.zyrox.world.content.dialogue.Dialogue;
import com.zyrox.world.content.dialogue.DialogueManager;
import com.zyrox.world.content.dialogue.DialogueType;
import com.zyrox.world.entity.impl.player.Player;

@CommandHeader(command = { "givedonator" }, description = "Gives a player donator.")

public class GiveDonatorCommand extends NameCommand {

	public GiveDonatorCommand() {
		super("Harrison", "Adam");
	}

	@Override
	public boolean execute(Player player, String[] args) throws Exception {
		if (args.length < 1) {
			throw new IllegalArgumentException();
		}

		String username = String.join(" ", Arrays.copyOfRange(args, 0, args.length));

		DialogueManager.start(player, new Dialogue() {
			@Override
			public DialogueType type() {
				return DialogueType.OPTION;
			}

			@Override
			public String[] dialogue() {
				return new String[] {
						"Regular Donator",
						"Super Donator",
						"Extreme Donator",
						"Platinum Donator",
						"Executive Donator"
				};
			}

			@Override
			public boolean action(int option) {

				Player target = World.getPlayerByName(username);

				if(target == null) {
					target = OfflineCharacter.getOfflineCharacter(username);

					if(target == null) {
						DialogueManager.sendStatement(player, "@red@"+username+" does not exist.");
						return true;
					}

				}

				player.getPA().closeDialogueOnly();

				PlayerRights rightsToGive = PlayerRights.PLAYER;

				switch(option) {
					case 1:
						rightsToGive = PlayerRights.DONATOR;
						break;
					case 2:
						rightsToGive = PlayerRights.SUPER_DONATOR;
						break;
					case 3:
						rightsToGive = PlayerRights.EXTREME_DONATOR;
						break;
					case 4:
						rightsToGive = PlayerRights.PLATINUM_DONATOR;
						break;
					case 5:
						rightsToGive = PlayerRights.EXECUTIVE_DONATOR;
						break;
				}

				if(target.getAmountDonated() >= rightsToGive.getAmountDonatedRequired()) {
					DialogueManager.sendStatement(player, username+" already has "+rightsToGive.toString()+".");
					return true;
				}

				target.setAmountDonated(rightsToGive.getAmountDonatedRequired());
				ClaimCommand.checkForRankUpdate(target);
				PlayerPanel.refreshPanel(target);

				PlayerSaving.getSaving().save(target);

				DialogueManager.sendStatement(player, "You have given "+username+" "+rightsToGive.toString()+".");
				DialogueManager.sendStatement(target, "You have been given "+rightsToGive.toString()+".");
				return true;
			}
		});


		return true;
	}

	@Override
	public String[] getUsage() {
		return new String[] {
			"::givedonator [name]",
		};
	}

}
