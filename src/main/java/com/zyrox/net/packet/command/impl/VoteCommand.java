
package com.zyrox.net.packet.command.impl;

import com.zyrox.model.Locations;
import com.zyrox.model.PlayerRights;
import com.zyrox.model.input.Input;
import com.zyrox.net.packet.command.Command;
import com.zyrox.net.packet.command.CommandHeader;
import com.zyrox.world.content.dialogue.Dialogue;
import com.zyrox.world.content.dialogue.DialogueManager;
import com.zyrox.world.content.dialogue.DialogueType;
import com.zyrox.world.content.youtube.YouTubeManager;
import com.zyrox.world.entity.impl.player.Player;

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
