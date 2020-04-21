package com.varrock.net.packet.command.impl;

import com.varrock.net.packet.command.Command;
import com.varrock.net.packet.command.CommandHeader;
import com.varrock.world.content.TriviaBot;
import com.varrock.world.entity.impl.player.Player;

@CommandHeader(command = { "answer" }, description = "Answers the trivia question.")
public class TriviaAnswerCommand extends Command {

	@Override
	public boolean execute(Player player, String[] args) throws Exception {
		if (args.length < 1) {
			throw new IllegalArgumentException();
		}
		
		String triviaAnswer = String.join(" ", args);
		
		if (TriviaBot.acceptingQuestion()) {
			TriviaBot.attemptAnswer(player, triviaAnswer);
		} else {
			player.sendMessage("There are no active trivia questions at the moment.");
		}
		
		return false;
	}

	@Override
	public String[] getUsage() {
		return new String[] {
			"::answer [your answer]"
		};
	}

}
