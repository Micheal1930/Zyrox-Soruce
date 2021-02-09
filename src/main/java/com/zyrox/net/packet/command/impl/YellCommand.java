package com.zyrox.net.packet.command.impl;

import java.util.Arrays;

import com.zyrox.GameServer;
import com.zyrox.model.GameMode;
import com.zyrox.model.PlayerRights;
import com.zyrox.model.log.impl.YellMessageLog;
import com.zyrox.net.packet.command.Command;
import com.zyrox.net.packet.command.CommandHeader;
import com.zyrox.tools.discord.DiscordBot;
import com.zyrox.tools.discord.DiscordChannel;
import com.zyrox.util.Misc;
import com.zyrox.world.World;
import com.zyrox.world.content.dialogue.DialogueManager;
import com.zyrox.world.entity.impl.player.Player;

@CommandHeader(command = { "yell" }, description = "Yells the specified message.")
public class YellCommand extends Command {

	private static String[] bannedChars = { ":chalreq", ":duelreq", ":tradereq", "<img", "@everyone" };

	public static boolean isBannedChar(String msg) {
		for (String s : bannedChars)
			if (msg.contains(s))
				return true;
		return false;
	}

	@Override
	public boolean execute(Player player, String[] args) throws Exception {

		if(GameServer.punishmentManager.isMuted(player))
			return false;
		
		int delay = player.getRights().getYellDelay();

		if (delay > 0 && !player.getLastYell().elapsed((delay * 1000))) {
			player.getPacketSender()
					.sendMessage("You must wait at least " + delay + " seconds between every yell-message you send.");
			return false;
		}

		String yellMessage = String.join(" ", Arrays.copyOfRange(args, 0, args.length));

		if (isBannedChar(yellMessage)) {
			DialogueManager.sendStatement(player, "Your text contains invalid symbols.");
			return false;
		}

		if (!player.getRights().isStaff() && player.getAmountDonated() < 10 && !player.getRights().isExtraStaff()) {
			DialogueManager.start(player, 291);
			player.setDialogueActionId(111);
			player.setYellMsg(yellMessage);
			return false;
		}
		String title = "";

		if (!player.getRights().equals(PlayerRights.PLAYER)) {
			title = player.getRights().toString() + "";
		}

		if (player.getName().equalsIgnoreCase("Jonny")) {
			title = "Owner";
		}
		if (player.yellTitle != null && !player.yellTitle.contains("null")) {
			title = player.yellTitle.length() > 1 ? player.yellTitle : title;
		}

		String prestige = (player.prestige > 0 ? "<img="+(720 + player.prestige)+">" : "");

		if (player.getRights().equals(PlayerRights.PLAYER)) {
			if (player.getGameMode() == GameMode.IRONMAN) {
				World.sendPlayerYell(player,
						"[<img=33>"+prestige+" <col=787878>Iron Man</col>] " + title + " " + player.getUsername() + ": " + yellMessage);
			} else if (player.getGameMode() == GameMode.ULTIMATE_IRONMAN) {
				World.sendPlayerYell(player,
						"[<img=32>"+prestige+" <col=E4E4E4>Ult Iron Man</col>] " + title + " " + player.getUsername() + ": " + yellMessage);
			} else if (player.getGameMode() == GameMode.HARDCORE_IRONMAN) {
                World.sendPlayerYell(player,
                        "[<img=463>"+prestige+" <col=861818>HC Iron Man</col>] " + title + " " + player.getUsername() + ": " + yellMessage);
            }
		} else {
			int spriteId = player.getRights().getSpriteId();

			World.sendPlayerYell(player,
					"<img=" + spriteId + ">"+prestige+"</col> [" + player.getRights().getYellPrefix() + ""
							+ title + "</shad></col>] " + player.getUsername() + ": " + yellMessage+"");
		}

		DiscordBot.sendMessage(DiscordChannel.GAME_YELL, (player.isStaff() ? "\uD83D\uDC51 " : "")+ "[**" + title + "**] "+player.getUsername()+": "+yellMessage+"");

		new YellMessageLog(player.getName(), yellMessage, Misc.getTime()).submit();

		if (delay > 0) {
			player.getLastYell().reset();
		}

		return true;
	}

	@Override
	public String[] getUsage() {
		return new String[] { "::yell [message]" };
	}

}