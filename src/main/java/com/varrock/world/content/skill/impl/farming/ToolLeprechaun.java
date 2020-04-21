package com.varrock.world.content.skill.impl.farming;

import com.varrock.model.Item;
import com.varrock.model.Position;
import com.varrock.net.packet.impl.UseItemPacketListener;
import com.varrock.world.content.dialogue.Dialogue;
import com.varrock.world.content.dialogue.DialogueExpression;
import com.varrock.world.content.dialogue.DialogueManager;
import com.varrock.world.content.dialogue.DialogueType;
import com.varrock.world.content.transportation.TeleportHandler;
import com.varrock.world.content.transportation.TeleportType;
import com.varrock.world.entity.impl.player.Player;

/**
 * Created by Jonny on 8/25/2019
 **/
public class ToolLeprechaun {

    public static void talkTo(Player player) {
        DialogueManager.start(player, new Dialogue() {

            @Override
            public DialogueType type() {
                return DialogueType.NPC_STATEMENT;
            }

            @Override
            public DialogueExpression animation() {
                return DialogueExpression.THINKING_STILL;
            }

            @Override
            public int npcId() {
                return 3021;
            }

            @Override
            public String[] dialogue() {
                return new String[] {"I can't find that end of that damn rainbow.", "What can I do for you?"};
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
                        return new String[] {"Exchange Crops", "Teleport"};
                    }

                    @Override
                    public boolean action(int option) {
                        switch(option) {
                            case 1:
                                exchange(player);
                                break;
                            case 2:
                                teleport(player);
                                break;
                        }
                        return true;
                    }
                };
            }
        });
    }

    public static void exchange(Player player) {
        for (Item item : player.getInventory().getItems()) {
            Item noted = UseItemPacketListener.getNotedHarvest(player, item);
            if(noted == null) {

            } else {
                int amount = player.getInventory().getAmount(item.getId());
                player.getInventory().delete(new Item(item.getId(), amount));
                player.getInventory().add(new Item(noted.getId(), amount));
            }
        }
        DialogueManager.start(player, new Dialogue() {

            @Override
            public DialogueType type() {
                return DialogueType.NPC_STATEMENT;
            }

            @Override
            public DialogueExpression animation() {
                return DialogueExpression.THINKING_STILL;
            }

            @Override
            public int npcId() {
                return 3021;
            }

            @Override
            public String[] dialogue() {
                return new String[] {"I have noted all your crops for you."};
            }
        });
    }

    public static void teleport(Player player) {
        DialogueManager.start(player, new Dialogue() {

            @Override
            public DialogueType type() {
                return DialogueType.OPTION;
            }

            @Override
            public String[] dialogue() {
                return new String[] {"Falador Farming Patch", "Catherby Farming Patch", "Ardougne Farming Patch", "Canifis Farming Patch"};
            }

            @Override
            public boolean action(int option) {
                switch(option) {
                    case 1:
                        TeleportHandler.teleportPlayer(player, new Position(3052, 3304, 0), TeleportType.NORMAL);
                        break;
                    case 2:
                        TeleportHandler.teleportPlayer(player, new Position(2816, 3461, 0), TeleportType.NORMAL);
                        break;
                    case 3:
                        TeleportHandler.teleportPlayer(player, new Position(2664, 3374, 0), TeleportType.NORMAL);
                        break;
                    case 4:
                        TeleportHandler.teleportPlayer(player, new Position(3600, 3523, 0), TeleportType.NORMAL);
                        break;
                }
                return true;
            }
        });
    }
}
