package com.varrock.world.content.partyroom;

import com.varrock.world.content.dialogue.Dialogue;
import com.varrock.world.content.dialogue.DialogueExpression;
import com.varrock.world.content.dialogue.DialogueType;

/**
 * Handles the lever dialogue
 * 
 * @author 2012 <https://www.rune-server.ee/members/dexter+morgan/>
 *
 */
public class PartyRoomConfirmDepositDialogue extends Dialogue {

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
		return new String[] { "Yes, add the items.",
				"No, I want to keep my items." };
	}
}
