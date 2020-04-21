
package com.varrock.net.packet.command.impl;

import com.varrock.model.Locations;
import com.varrock.model.PlayerRights;
import com.varrock.model.input.Input;
import com.varrock.model.log.impl.PunishmentLog;
import com.varrock.model.punish.Punishment;
import com.varrock.net.packet.command.Command;
import com.varrock.net.packet.command.CommandHeader;
import com.varrock.util.Misc;
import com.varrock.util.Stopwatch;
import com.varrock.world.World;
import com.varrock.world.content.dialogue.Dialogue;
import com.varrock.world.content.dialogue.DialogueManager;
import com.varrock.world.content.dialogue.DialogueType;
import com.varrock.world.content.youtube.YouTubeManager;
import com.varrock.world.entity.impl.player.Player;

@CommandHeader(command = { "vote", "yt", "youtube" }, description = "Opens the voting page.")
public class VoteCommand extends Command {

	@Override
	public boolean execute(Player player, String[] args) throws Exception {
		if (args.length != 0) {
			throw new IllegalArgumentException();
		}

		if(player.getLocation() == Locations.Location.DUNGEONEERING) {

		}

		if(player.isStaff() || player.getRights() == PlayerRights.YOUTUBER) {
			DialogueManager.start(player, new Dialogue() {

				@Override
				public DialogueType type() {
					return DialogueType.OPTION;
				}

				@Override
				public String[] dialogue() {
					return new String[] {
							"Open youtube voting page",
							"Post youtube video",
							"Delete video",
					};
				}

				@Override
				public boolean action(int option) {
					switch(option) {
						case 1:
							YouTubeManager.open(player);
							break;
						case 2:
							player.getPacketSender().sendInterface(61800);
							break;
						case 3:
							player.setInputHandling(new Input() {

								@Override
								public void handleSyntax(Player player, String text) {
									YouTubeManager.deleteVideo(player, text);
								}

							});
							player.getPA().sendEnterInputPrompt("Enter youtube video ID:");
							break;
					}
					return true;
				}
			});
		} else {
			YouTubeManager.open(player);
		}
		return true;
	}

	@Override
	public String[] getUsage() {
		return new String[] {
			"::vote"
		};
	}

}
