package com.varrock.world.content.dialogue.impl;

import com.varrock.util.Misc;
import com.varrock.world.content.dialogue.Dialogue;
import com.varrock.world.content.dialogue.DialogueExpression;
import com.varrock.world.content.dialogue.DialogueManager;
import com.varrock.world.content.dialogue.DialogueType;
import com.varrock.world.entity.impl.player.Player;

public class BloodMoneyDealer {

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
                return 23390;
            }

            @Override
            public String[] dialogue() {
                String KDR = "N/A";
                int kc = player.getPlayerKillingAttributes().getPlayerKills();
                int dc = player.getPlayerKillingAttributes().getPlayerDeaths();
                if (kc >= 5 && dc >= 5) {
                    KDR = String.valueOf((double) (kc / dc));
                }
                return new String[]{"You have killed " + player.getPlayerKillingAttributes().getPlayerKills() + " players. You have died " + player.getPlayerKillingAttributes().getPlayerDeaths() + " times.", "You currently have a killstreak of " + player.getPlayerKillingAttributes().getKillstreak() + " and your", "KDR is currently " + KDR + "."};
            }

            public Dialogue nextDialogue() {
                return DialogueManager.getDialogues().get(19);
            }
        };
    }

    public static Dialogue getRedeemDialogue(Player player, int points) {
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
                return 23390;
            }

            @Override
            public String[] dialogue() {
                return new String[]{"Are you sure you would like to convert all",
                        "of your PK points into " + Misc.format(points * 40) + " blood money?"};
            }

            public Dialogue nextDialogue() {
                player.pkPointsTemp = points;
                player.setDialogueActionId(360);
                return DialogueManager.getDialogues().get(360);
            }
        };
    }
}
