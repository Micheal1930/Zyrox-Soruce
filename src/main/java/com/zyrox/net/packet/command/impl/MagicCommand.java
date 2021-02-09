package com.zyrox.net.packet.command.impl;

import static com.zyrox.world.content.combat.magic.Autocasting.resetAutocast;

import com.zyrox.GameSettings;
import com.zyrox.model.*;
import com.zyrox.net.packet.command.Command;
import com.zyrox.net.packet.command.CommandHeader;
import com.zyrox.world.content.dialogue.Dialogue;
import com.zyrox.world.content.dialogue.DialogueExpression;
import com.zyrox.world.content.dialogue.DialogueManager;
import com.zyrox.world.content.dialogue.DialogueType;
import com.zyrox.world.entity.impl.player.Player;

@CommandHeader(command = { "magic" }, description = "Allows you to change your magic book.")
public class MagicCommand extends Command {

	public static final int DIALOGUE_ACTION = 695;

	/**
	 * Constructs a new {@link MagicCommand}.
	 */
	public MagicCommand() {
		super(PlayerRights.EXTREME_DONATOR, true);
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
				return new String[] { "Modern", "Ancient", "Lunar", "Cancel" };
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

	public static void handleSwitch(Player player, MagicSpellbook book) {
		if (book == MagicSpellbook.LUNAR) {
			if (player.getSkillManager().getMaxLevel(Skill.DEFENCE) < 40) {
				player.getPacketSender()
						.sendMessage("You need a Defence level of at least 40 to use this altar.");
				return;
			}
		}
		player.performAnimation(new Animation(645));
		player.setSpellbook(book);
		player.getPacketSender()
				.sendTabInterface(GameSettings.MAGIC_TAB, player.getSpellbook().getInterfaceId())
				.sendMessage("Your magic spellbook has changed...");
		resetAutocast(player, true);
		player.getPacketSender().sendInterfaceRemoval();
	}

}
