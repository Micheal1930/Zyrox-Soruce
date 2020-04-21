package com.varrock.world.content.greatolm;

import com.varrock.world.content.dialogue.Dialogue;
import com.varrock.world.content.dialogue.DialogueExpression;
import com.varrock.world.content.dialogue.DialogueType;
import com.varrock.world.entity.impl.player.Player;

/**
 * Represents a Raids party invitation dialogue
 * @author Gabriel Hannason
 */

public class RaidsPartyInvitation extends Dialogue {
	
	public RaidsPartyInvitation(Player inviter, Player p) {
		this.inviter = inviter;
		this.p = p;
	}
	
	private Player inviter, p;
	
	@Override
	public DialogueType type() {
		return DialogueType.STATEMENT;
	}
	
	@Override
	public DialogueExpression animation() {
		return null;
	}
	
	@Override
	public String[] dialogue() {
		return new String[]{"" + inviter.getUsername() + " has invited you to join their Raids party."};
	}
	
	@Override
	public int npcId() {
		return -1;
	}
	
	@Override
	public Dialogue nextDialogue() {
		return new Dialogue() {
			
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
				return new String[]{"Join " + inviter.getUsername() + "'s party", "Don't join " + inviter.getUsername() + "'s party."};
			}
			
			@Override
			public void specialAction() {
				p.getMinigameAttributes().getRaidsAttributes().setPartyInvitation(inviter.getMinigameAttributes().getRaidsAttributes().getParty());
				p.setDialogueActionId(67);
			}
			
		};
	}
}