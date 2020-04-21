package com.varrock.net.packet.command.impl;

import com.varrock.model.Locations;
import com.varrock.net.packet.command.Command;
import com.varrock.net.packet.command.CommandHeader;
import com.varrock.util.Stopwatch;
import com.varrock.world.content.dialogue.Dialogue;
import com.varrock.world.content.dialogue.DialogueExpression;
import com.varrock.world.content.dialogue.DialogueManager;
import com.varrock.world.content.dialogue.DialogueType;
import com.varrock.world.entity.impl.player.Player;

@CommandHeader(command = { "empty" }, description = "Empties your inventory.")
public class EmptyCommand extends Command {

	@Override
	public boolean execute(Player player, String[] args) throws Exception {
		if (args.length != 0) {
			throw new IllegalArgumentException();
		}

		if(player.getLocation() == Locations.Location.WILDERNESS) {
			player.sendMessage("You can't use the empty command in the wildrness.");
			return true;
		}

		long day = 1000 * 60 * 60 * 24;

		if(player.ignoreEmptyStopwatch != null && !player.ignoreEmptyStopwatch.elapsed(day)) {
			emptyInventory(player);
		} else {
			DialogueManager.start(player, new Dialogue() {

				@Override
				public DialogueType type() {
					return DialogueType.OPTION;
				}

				@Override
				public DialogueExpression animation() {
					return null;
				}

				@Override
				public String[] dialogue() {
					return new String[]{
							"Empty my inventory",
							"Empty and don't show this for 24 hours",
							"No, cancel",
					};
				}

				@Override
				public boolean closeInterface() {
					return false;
				}

				@Override
				public boolean action(int option) {
					switch (option) {
						case 1:
							emptyInventory(player);
							player.getPacketSender().closeDialogueOnly();
							break;
						case 2:
							emptyInventory(player);
							player.ignoreEmptyStopwatch = new Stopwatch();
							player.ignoreEmptyStopwatch.reset();
							player.getPacketSender().closeDialogueOnly();
							break;
						case 3:
							player.getPacketSender().sendMessage("You don't empty your inventory.");
							player.getPacketSender().closeDialogueOnly();
							break;
					}
					return true;
				}

			});
		}
		return true;
	}

	@Override
	public String[] getUsage() {
		return new String[] {
			"::empty"
		};
	}

	public void emptyInventory(Player player) {
		player.getPacketSender().sendMessage("You clear your inventory.");
		player.getSkillManager().stopSkilling();
		player.getInventory().resetItems().refreshItems();
	}

}
