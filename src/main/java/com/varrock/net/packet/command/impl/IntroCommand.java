package com.varrock.net.packet.command.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.varrock.GameServer;
import com.varrock.model.GameMode;
import com.varrock.model.Item;
import com.varrock.model.PlayerRights;
import com.varrock.model.definitions.ItemDefinition;
import com.varrock.model.log.impl.StoreClaimLog;
import com.varrock.net.packet.command.Command;
import com.varrock.net.packet.command.CommandHeader;
import com.varrock.net.sql.SQLTable;
import com.varrock.util.Misc;
import com.varrock.world.World;
import com.varrock.world.content.PlayerPanel;
import com.varrock.world.content.dialogue.Dialogue;
import com.varrock.world.content.dialogue.DialogueManager;
import com.varrock.world.content.dialogue.DialogueType;
import com.varrock.world.content.introduction.IntroductionAutomation;
import com.varrock.world.content.introduction.IntroductionRedeemAutomation;
import com.varrock.world.entity.impl.player.Player;

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
