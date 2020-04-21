package com.varrock.net.packet.command.impl;

import com.varrock.model.Item;
import com.varrock.model.container.impl.Equipment;
import com.varrock.net.packet.command.CommandHeader;
import com.varrock.net.packet.command.NameCommand;
import com.varrock.util.Misc;
import com.varrock.world.content.dialogue.Dialogue;
import com.varrock.world.content.dialogue.DialogueManager;
import com.varrock.world.content.dialogue.DialogueType;
import com.varrock.world.content.goodie_bag.GoodieBagManager;
import com.varrock.world.content.youtube.YouTubeManager;
import com.varrock.world.content.youtube.YouTubeVideo;
import com.varrock.world.entity.impl.player.Player;

@CommandHeader(command = { "test" }, description = "used to test stuff")
public class TestCommand extends NameCommand {
	public TestCommand() {
		super("Finch", "Jonny");
	}

	@Override
	public boolean execute(Player player, String[] args) throws Exception {

		int superRareLoot = 0;
		int veryRareLoot = 0;
		int extremeRareLoot = 0;

		for(int i = 0; i < 1000; i++) {
			if (Misc.randomFloat() <= .002) {

				double randomNumber = Misc.randomFloat();
				if (randomNumber < .55) {
					superRareLoot++;
				} else if (randomNumber < .94) {
					veryRareLoot++;
				} else {
					extremeRareLoot++;
				}
			}
		}

		player.sendMessage("Super rare loot = "+superRareLoot+", very rare loot="+veryRareLoot+", extreme rare loot="+extremeRareLoot);
		return true;
	}

	@Override
	public String[] getUsage() {
		return new String[] { "::test" };
	}
}
