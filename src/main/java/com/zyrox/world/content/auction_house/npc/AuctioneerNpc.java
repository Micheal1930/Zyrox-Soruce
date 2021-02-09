package com.zyrox.world.content.auction_house.npc;

import com.zyrox.model.Position;
import com.zyrox.util.Misc;
import com.zyrox.util.Stopwatch;
import com.zyrox.world.content.auction_house.AuctionHouseManager;
import com.zyrox.world.content.dialogue.Dialogue;
import com.zyrox.world.content.dialogue.DialogueExpression;
import com.zyrox.world.content.dialogue.DialogueManager;
import com.zyrox.world.content.dialogue.DialogueType;
import com.zyrox.world.entity.impl.npc.NPC;
import com.zyrox.world.entity.impl.npc.NPCIdentity;
import com.zyrox.world.entity.impl.npc.click_type.NpcClickType;
import com.zyrox.world.entity.impl.player.Player;

/**
 * Created by Jonny on 9/5/2019
 **/
@NPCIdentity(ids = {947})
public class AuctioneerNpc extends NPC {

    public Stopwatch announceTimer = new Stopwatch().reset();

    public long DURATION_BETWEEN_ANNOUNCEMENT = 6000;

    public static String[] ANNOUNCEMENTS = new String[] {
            "Going once... going twice... SOLD!",
            "Place your auctions here!",
            "Sell your items in my Auction House!",
            "One fifty, Two fifty, Three fifty, SOLD!",
            "Sold to the man in the bandos tassets!",
            "Step right up, bid on items in my Auction House!",
    };

    public AuctioneerNpc(int id, Position position) {
        super(id, position);
    }

    @Override
    public void clickNpc(Player player, NpcClickType npcClickType) {
        if(player.isIronman()) {
            player.sendMessage("Ironman can't use the auction house.");
            return;
        }

        switch(npcClickType) {
            case FIRST_CLICK:
                DialogueManager.start(player, new Dialogue() {

                    @Override
                    public int npcId() {
                        return 947;
                    }

                    @Override
                    public DialogueType type() {
                        return DialogueType.NPC_STATEMENT;
                    }

                    @Override
                    public DialogueExpression animation() {
                        return DialogueExpression.NORMAL;
                    }

                    @Override
                    public String[] dialogue() {
                        return new String[] {"Hello, what can I do for you?"};
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
                                return new String[] {
                                        "Search for items",
                                        "Open my history",
                                        "Collect Items",
                                        "Cancel"
                                };
                            }

                            @Override
                            public boolean action(int action) {
                                player.getPA().closeDialogueOnly();
                                if(action == 1) {
                                    openSearch(player);
                                } else if(action == 2) {
                                    openHistory(player);
                                } else if(action == 3) {
                                    collectItems(player);
                                }
                                return true;
                            }
                        };
                    }
                });
                break;
            case SECOND_CLICK:
                openSearch(player);
                break;
            case THIRD_CLICK:
                openHistory(player);
                break;
        }
    }

    @Override
    public void sequence() {
        if(announceTimer.elapsed(DURATION_BETWEEN_ANNOUNCEMENT) && Misc.random(5) == 1) {
            forceChat(ANNOUNCEMENTS[Misc.random(ANNOUNCEMENTS.length - 1)]);
            announceTimer.reset();
        }
    }

    public void openSearch(Player player) {
        AuctionHouseManager.open(player);
    }

    public void openHistory(Player player) {

    }

    public void collectItems(Player player) {
        AuctionHouseManager.pickupCollections(player);
    }

    /**
     * Creates a new instance of this npc with the given index.
     *
     * @param index the new index of this npc.
     * @return the new instance.
     */
    @Override
    public NPC copy(int npcId, Position position) {
        return new AuctioneerNpc(npcId, position);
    }
}
