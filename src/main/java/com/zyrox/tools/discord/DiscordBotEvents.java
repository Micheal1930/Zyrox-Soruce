package com.zyrox.tools.discord;

import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

public class DiscordBotEvents {

	private DiscordBot discordBot;

	public DiscordBotEvents(DiscordBot discordBot) {
		this.discordBot = discordBot;
	}

	@EventSubscriber
	public void onMessageReceived(MessageReceivedEvent event) {
		if (event.getMessage().getContent().startsWith("::")) {
			discordBot.discordCommand(event.getMessage());
		}
		discordBot.messageSent(event.getMessage());
	}

}
