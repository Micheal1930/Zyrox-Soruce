package com.varrock.world.content.platinum_tokens;

import com.varrock.model.Item;
import com.varrock.util.Misc;
import com.varrock.world.content.dialogue.Dialogue;
import com.varrock.world.content.dialogue.DialogueManager;
import com.varrock.world.content.dialogue.DialogueType;
import com.varrock.world.entity.impl.player.Player;

/**
 * Created by Jonny on 6/23/2019
 **/
public class PlatinumTokenConversionManager {

    public static boolean onBank(Player player, int itemId) {
        if(itemId == PlatinumTokenConstants.PLATINUM_TOKEN_ID) {

            long tokensToConvert = player.getInventory().getAmount(PlatinumTokenConstants.PLATINUM_TOKEN_ID);

            long coinsInInventory = player.getInventory().getAmount(995);

            long coinsToReceive = tokensToConvert * PlatinumTokenConstants.PRICE_PER_TOKEN;

            long coinsPossible = coinsToReceive + coinsInInventory;

            if(coinsPossible > Integer.MAX_VALUE) {
                tokensToConvert = (Integer.MAX_VALUE - coinsInInventory) / PlatinumTokenConstants.PRICE_PER_TOKEN;

                coinsToReceive = tokensToConvert * PlatinumTokenConstants.PRICE_PER_TOKEN;
            }

            int finalCoinsToReceive = (int) coinsToReceive;
            int finalTokensToConvert = (int) tokensToConvert;
            Dialogue TOKENS_TO_COINS_DIALOG = new Dialogue() {

                @Override
                public DialogueType type() {
                    return DialogueType.ITEM_STATEMENT;
                }

                @Override
                public String[] item() {
                    return new String[]{
                            "43204",
                            "180",
                            "Platinum Tokens"
                    };
                }

                @Override
                public String[] dialogue() {
                    return new String[]{
                            "Are you sure you want to convert", Misc.format(finalTokensToConvert)+" tokens to "+Misc.formatRunescapeStyle(finalCoinsToReceive)+" coins?",
                    };
                }

                @Override
                public Dialogue nextDialogue() {
                    return new Dialogue() {

                        @Override
                        public DialogueType type() {
                            return DialogueType.OPTION;
                        }

                        @Override
                        public String[] dialogue() {
                            return new String[]{
                                    "Convert "+ Misc.format(finalTokensToConvert)+" tokens to "+Misc.formatRunescapeStyle(finalCoinsToReceive)+" coins",
                                    "Cancel"
                            };
                        }

                        @Override
                        public boolean action(int option) {
                            player.getPA().closeDialogueOnly();

                            if(option == 1) {
                                if (player.getInventory().contains(new Item(PlatinumTokenConstants.PLATINUM_TOKEN_ID, finalTokensToConvert))) {
                                    player.getInventory().delete(PlatinumTokenConstants.PLATINUM_TOKEN_ID, finalTokensToConvert);
                                    player.getInventory().add(995, finalCoinsToReceive);
                                    player.sendMessage("<col=2F519F>You have converted " + Misc.format(finalTokensToConvert) + " tokens to " + Misc.formatRunescapeStyle(finalCoinsToReceive) + " coins.");
                                }
                            }

                            return false;
                        }

                    };
                }

            };

            DialogueManager.start(player, TOKENS_TO_COINS_DIALOG);
            return true;
        } else if(itemId == 995) {
            int coinsToConvert = player.getInventory().getAmount(995);
            int tokensInInventory = player.getInventory().getAmount(PlatinumTokenConstants.PLATINUM_TOKEN_ID);

            int tokensToReceive = coinsToConvert / PlatinumTokenConstants.PRICE_PER_TOKEN;

            if(tokensToReceive + tokensInInventory > Integer.MAX_VALUE) {
                player.sendMessage("<col=ff0000>You have too many tokens already in your inventory to do this.");
                return true;
            }

            int finalTokensToReceive = tokensToReceive;
            int finalCoinsToConvert = coinsToConvert;
            Dialogue COINS_TO_TOKENS_DIALOG = new Dialogue() {

                @Override
                public DialogueType type() {
                    return DialogueType.ITEM_STATEMENT;
                }

                @Override
                public String[] item() {
                    return new String[]{
                            "995",
                            "180",
                            "Coins"
                    };
                }

                @Override
                public String[] dialogue() {
                    return new String[]{
                            "Are you sure you want to convert", Misc.formatRunescapeStyle(finalCoinsToConvert) + " coins to " + Misc.format(finalTokensToReceive) + " tokens?",
                    };
                }

                @Override
                public Dialogue nextDialogue() {
                    return new Dialogue() {

                        @Override
                        public DialogueType type() {
                            return DialogueType.OPTION;
                        }

                        @Override
                        public String[] dialogue() {
                            return new String[]{
                                    "Convert " + Misc.formatRunescapeStyle(finalCoinsToConvert) + " coins to " + Misc.format(finalTokensToReceive) + " tokens",
                                    "Cancel"
                            };
                        }

                        @Override
                        public boolean action(int option) {
                            player.getPA().closeDialogueOnly();

                            if (option == 1) {
                                if (player.getInventory().contains(new Item(995, finalCoinsToConvert))) {
                                    player.getInventory().delete(995, finalCoinsToConvert);
                                    player.getInventory().add(PlatinumTokenConstants.PLATINUM_TOKEN_ID, finalTokensToReceive);
                                    player.sendMessage("<col=2F519F>You have converted " + Misc.formatRunescapeStyle(finalCoinsToConvert) + " coins to " + Misc.format(finalTokensToReceive) + " tokens.");
                                }
                            }
                            return false;
                        }

                    };
                }
            };

            DialogueManager.start(player, COINS_TO_TOKENS_DIALOG);
            return true;
        }
        return false;
    }


}
