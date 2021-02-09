package com.zyrox.world.content;

import com.zyrox.world.content.dialogue.Dialogue;
import com.zyrox.world.content.dialogue.DialogueExpression;
import com.zyrox.world.content.dialogue.DialogueManager;
import com.zyrox.world.content.dialogue.DialogueType;
import com.zyrox.world.entity.impl.player.Player;

public class MemberScrolls {
	
	public static void giveWarning(Player player) {
		DialogueManager.start(player, 391);
		player.setDialogueActionId(391);
	}
	

	
	public static void handleScrollClaim(Player player) {
		player.getPacketSender().sendInterfaceRemoval();
			if(player.getScrollAmount() == 1) {
				player.getInventory().delete(10942, 1);
				player.incrementAmountDonated(10);
				player.getPacketSender().sendMessage("Your account has gained funds worth $10. Your total is now at $"+player.getAmountDonated()+".");

				PlayerPanel.refreshPanel(player);
			}
			if(player.getScrollAmount() == 2) {
				player.getInventory().delete(10934, 1);
				player.incrementAmountDonated(20);
				player.getPacketSender().sendMessage("Your account has gained funds worth $20. Your total is now at $"+player.getAmountDonated()+".");

				PlayerPanel.refreshPanel(player);	
			}
			if(player.getScrollAmount() == 3) {
				player.getInventory().delete(10935, 1);
				player.incrementAmountDonated(50);
				player.getPacketSender().sendMessage("Your account has gained funds worth $50. Your total is now at $"+player.getAmountDonated()+".");

				PlayerPanel.refreshPanel(player);
			}
			if(player.getScrollAmount() == 4) {
				player.getInventory().delete(10943, 1);
				player.incrementAmountDonated(100);
				player.getPacketSender().sendMessage("Your account has gained funds worth $100. Your total is now at $"+player.getAmountDonated()+".");

				PlayerPanel.refreshPanel(player);
			}
				
	}
	
	public static Dialogue getTotalFunds(final Player player) {
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
				return 4657;
			}

			@Override
			public String[] dialogue() {
				return player.getAmountDonated() > 0 ? new String[]{"Your account has claimed scrolls worth $"+player.getAmountDonated()+" in total.", "Thank you for supporting us!"} : new String[]{"Your account has claimed scrolls worth $"+player.getAmountDonated()+" in total."};
			}
			
			@Override
			public Dialogue nextDialogue() {
				return DialogueManager.getDialogues().get(5);
			}
		};
	}
}
