package com.zyrox.world.content.dialogue.impl;

import com.zyrox.model.input.impl.BuyAgilityExperience;
import com.zyrox.world.content.dialogue.Dialogue;
import com.zyrox.world.content.dialogue.DialogueExpression;
import com.zyrox.world.content.dialogue.DialogueType;
import com.zyrox.world.entity.impl.player.Player;

public class AgilityTicketExchange {

	public static Dialogue getDialogue(Player player) {
		return new Dialogue() {

			@Override
			public DialogueType type() {
				return DialogueType.NPC_STATEMENT;
			}

			@Override
			public DialogueExpression animation() {
				return DialogueExpression.NORMAL;
			}

			@Override
			public int npcId() {
				return 437;
			}
			
			@Override
			public String[] dialogue() {
				return new String[]{"@bla@How many tickets would you like to exchange", "for experience?"};
			}
			
			public Dialogue nextDialogue() {
				return new Dialogue() {

					@Override
					public DialogueType type() {
						return DialogueType.NPC_STATEMENT;
					}

					@Override
					public DialogueExpression animation() {
						return DialogueExpression.NORMAL;
					}

					@Override
					public int npcId() {
						return 437;
					}
					
					@Override
					public String[] dialogue() {
						return new String[]{"@bla@How many tickets would you like to exchange", "for experience?"};
					}
				
					@Override
					public void specialAction() {
						player.getPacketSender().sendInterfaceRemoval();
						player.setInputHandling(new BuyAgilityExperience());
						player.getPacketSender().sendEnterAmountPrompt("How many tickets would you like to exchange?");
					}
				};
				
			}
		};
	}
	
}
