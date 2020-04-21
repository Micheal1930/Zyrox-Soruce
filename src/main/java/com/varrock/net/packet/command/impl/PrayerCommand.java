package com.varrock.net.packet.command.impl;

import com.varrock.GameSettings;
import com.varrock.model.*;
import com.varrock.net.packet.command.Command;
import com.varrock.net.packet.command.CommandHeader;
import com.varrock.world.content.combat.prayer.CurseHandler;
import com.varrock.world.content.combat.prayer.PrayerHandler;
import com.varrock.world.content.dialogue.Dialogue;
import com.varrock.world.content.dialogue.DialogueExpression;
import com.varrock.world.content.dialogue.DialogueManager;
import com.varrock.world.content.dialogue.DialogueType;
import com.varrock.world.entity.impl.player.Player;

@CommandHeader(command = { "prayer" }, description = "Allows you to change your prayer book.")
public class PrayerCommand extends Command {

	public static final int DIALOGUE_ACTION = 690;

	/**
	 * Constructs a new {@link PrayerCommand}.
	 */
	public PrayerCommand() {
		super(PlayerRights.EXTREME_DONATOR);
	}
	
	@Override
	public boolean execute(Player player, String[] args) {
		if (player.getLocation() == Locations.Location.WILDERNESS) {
			player.getPacketSender().sendMessage("You cannot use this command to change your prayers while in the wild!");
			return false;
		}

		if (player.getCombatBuilder().isAttacking()) {
			player.getPacketSender().sendMessage("You cannot use this command while in combat!");
			return false;
		}

		player.setDialogueActionId(DIALOGUE_ACTION);
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
				return new String[] { "Normal", "Curses", "Cancel" };
			}
		});
		return true;
	}

	@Override
	public String[] getUsage() {
		return new String[] {
			"::prayer"
		};
	}

	public static void handleSwitch(Player player, Prayerbook book) {
		if (book == Prayerbook.CURSES) {
			if (player.getSkillManager().getMaxLevel(Skill.DEFENCE) < 30) {
				player.getPacketSender()
						.sendMessage("You need a Defence level of at least 30 to use this altar.");
				return;
			}
		}
		player.performAnimation(new Animation(645));
		String message = book == Prayerbook.CURSES ? "purity" : "power";
		player.getPacketSender().sendMessage("You sense a surge of " + message + " flow through your body!");
		player.setPrayerbook(book);
		player.getPacketSender().sendTabInterface(GameSettings.PRAYER_TAB, player.getPrayerbook().getInterfaceId());
		PrayerHandler.deactivateAll(player);
		CurseHandler.deactivateAll(player);
		player.getPacketSender().sendInterfaceRemoval();
	}

}
