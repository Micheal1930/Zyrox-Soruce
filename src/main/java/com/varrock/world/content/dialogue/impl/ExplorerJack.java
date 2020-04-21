package com.varrock.world.content.dialogue.impl;

import com.varrock.model.input.impl.ItemSearch;
import com.varrock.util.Misc;
import com.varrock.world.content.dialogue.Dialogue;
import com.varrock.world.content.dialogue.DialogueExpression;
import com.varrock.world.content.dialogue.DialogueType;
import com.varrock.world.entity.impl.player.Player;

public class ExplorerJack {

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
				return 7969;
			}
			
			@Override
			public String[] dialogue() {
				return new String[]{"Hello adventurer.", "Wan't to know what npc drops a desired item?", "Search it with my hefty database", "Which npc drop would you like to know about?"};
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
						return 7969;
					}
					
					@Override
					public String[] dialogue() {
				return new String[]{"Hello adventurer.", "Wan't to know what npc drops a desired item?", "Search it with my hefty database", "Which npc drop would you like to know about?"};
					}
				
					@Override
					public void specialAction() {
						player.getPacketSender().sendInterfaceRemoval();
						player.setInputHandling(new ItemSearch());
						player.getPacketSender().sendEnterInputPrompt("Enter an item's full name below...");
					}
				};
				
			}
		};
	}
	
	public static Dialogue foundDrop(String itemName, String npcName) {
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
				return 7969;
			}
			
			@Override
			public String[] dialogue() {
				return new String[]{"Ah, yes! The "+Misc.formatText(itemName)+".", "I believe the "+Misc.anOrA(npcName)+" "+npcName+"", "drops this item!", "Good luck in your adventure Simplician!"};
			}
		};
	}
}
