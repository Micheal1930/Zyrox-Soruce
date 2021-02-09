package com.zyrox.net.packet.command.impl;

import com.zyrox.net.packet.command.Command;
import com.zyrox.net.packet.command.CommandHeader;
import com.zyrox.world.content.dialogue.Dialogue;
import com.zyrox.world.content.dialogue.DialogueManager;
import com.zyrox.world.content.dialogue.DialogueType;
import com.zyrox.world.content.introduction.IntroductionAutomation;
import com.zyrox.world.content.introduction.IntroductionRedeemAutomation;
import com.zyrox.world.entity.impl.player.Player;

@CommandHeader(command = { "intro", "introduction" }, description = "Create an introduction for rewards.")
public class IntroCommand extends Command {

	@Override
	public boolean execute(Player player, String[] args) throws Exception {

		DialogueManager.start(player, new Dialogue() {

			@Override
			public DialogueType type() {
				return DialogueType.OPTION;
			}

			@Override
			public String[] dialogue() {
				return new String[] {
						"Create introduction",
						"Check for introduction reward",
						"Check for redeem reward"
				};
			}

			@Override
			public boolean action(int option) {
				switch(option) {
					case 1:
						player.getPacketSender().sendString(1, "https://varrock.io/forums/index.php?/forum/6-introductions-and-farewells/");
						player.getPA().closeDialogueOnly();
						break;
					case 2:
						IntroductionAutomation.checkForTopics(player);
						break;
					case 3:
						IntroductionRedeemAutomation.checkForRedeem(player);
						break;
				}
				return true;
			}
		});

		return true;
	}

	@Override
	public String[] getUsage() {
		return new String[] {
			"::intro", "::introduction"
		};
	}

}
