package com.zyrox.world.content.raids.theatre_of_blood.verzik_vitur;

import java.util.*;

import com.zyrox.model.Flag;
import com.zyrox.model.Position;
import com.zyrox.model.definitions.NPCDrops;
import com.zyrox.world.content.combat.strategy.CombatStrategy;
import com.zyrox.world.content.dialogue.Dialogue;
import com.zyrox.world.content.dialogue.DialogueManager;
import com.zyrox.world.content.dialogue.DialogueType;
import com.zyrox.world.content.raids.theatre_of_blood.TheatreOfBlood;
import com.zyrox.world.content.raids.theatre_of_blood.TheatreOfBloodReward;
import com.zyrox.world.content.raids.theatre_of_blood.verzik_vitur.combat_strategy.VerzikViturCombatStrategy;
import com.zyrox.world.entity.impl.npc.NPC;
import com.zyrox.world.entity.impl.npc.click_type.NpcClickType;
import com.zyrox.world.entity.impl.player.Player;

/**
 * Created by Jonny on 7/2/2019
 **/
public class VerzikVitur extends NPC {

    private TheatreOfBlood theatreOfBlood;

    private VerzikViturPhase phase = VerzikViturPhase.PHASE_X;

    private VerzikViturCombatStrategy combatStrategy;

    private int bombCount;

    private int electricCount;

    private ArrayList<NPC> minions = new ArrayList<>();

    public VerzikVitur(TheatreOfBlood theatreOfBlood, int id, Position position) {
        super(id, position);

        this.theatreOfBlood = theatreOfBlood;

        this.combatStrategy = phase.getCombatStrategy();

        if(phase.getCombatStrategy() != null)
            this.combatStrategy = phase.getCombatStrategy().setVerzikVitur(this);
    }

    public VerzikViturPhase getPhase() {
        return phase;
    }

    @Override
    public void clickNpc(Player player, NpcClickType npcClickType) {
        if(npcClickType == NpcClickType.FIRST_CLICK) {

            startingDialogs(player);

          //  transform(VerzikViturPhase.PHASE_1);
            
        }
    }

    public void transform(VerzikViturPhase form, int npcId) {
        this.phase = form;

        setTransformationId(npcId);

        getUpdateFlag().flag(Flag.TRANSFORM);

        this.combatStrategy = form.getCombatStrategy();

        if(phase.getCombatStrategy() != null)
            this.combatStrategy = form.getCombatStrategy().setVerzikVitur(this);

        super.determineStrategy();
    }

    public void startingDialogs(Player player) {
        DialogueManager.start(player, new Dialogue() {

            @Override
            public DialogueType type() {
                return DialogueType.STATEMENT;
            }

            @Override
            public String[] dialogue() {
                return new String[] {
                        "Now that was quite the show!",
                        "I haven't been that entertained in a long time."
                };
            }

            @Override
            public Dialogue nextDialogue() {
                return new Dialogue() {

                    @Override
                    public DialogueType type() {
                        return DialogueType.STATEMENT;
                    }

                    @Override
                    public String[] dialogue() {
                        return new String[] {
                                "Of course, you know I can't let you leave here alive.",
                                "Time for your final performance..."
                        };
                    }

                    @Override
                    public Dialogue nextDialogue() {
                        return new Dialogue() {

                            @Override
                            public DialogueType type() {
                                return DialogueType.STATEMENT;
                            }

                            @Override
                            public String[] dialogue() {
                                return new String[]{
                                        "Is your party ready to fight?",
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
                                                "Yes, let's begin.",
                                                "No, don't start yet.",
                                        };
                                    }

                                    @Override
                                    public boolean action(int option) {
                                        player.getPacketSender().closeDialogueOnly();
                                        if(option == 1) {
                                            DialogueManager.start(player, new Dialogue() {

                                                @Override
                                                public DialogueType type() {
                                                    return DialogueType.STATEMENT;
                                                }

                                                @Override
                                                public String[] dialogue() {
                                                    return new String[] {"Oh I'm going to enjoy this..."};
                                                }
                                            });
                                            transform(VerzikViturPhase.PHASE_1, VerzikViturPhase.PHASE_1.getNpcId());
                                            //getCombatBuilder().attack(player);

                                        }
                                        return true;
                                    }
                                };
                            }
                        };
                    }
                };
            }
        });
    }

    @Override
    public CombatStrategy determineStrategy() {
        return combatStrategy;
    }

    public TheatreOfBlood getTheatreOfBlood() {
        return theatreOfBlood;
    }

    @Override
    public boolean sendsBlockAnimation() {
        return false;
    }

    @Override
    public void appendDeath() {
        VerzikViturPhase nextPhase = getPhase().getNextPhase();

        if(nextPhase == VerzikViturPhase.DEATH) {
            super.appendDeath();
            theatreOfBlood.completed();
        } else {
            combatStrategy.changePhase();
        }
    }

    @Override
    public void sequence() {
        super.sequence();

        if(getTransformationId() == VerzikViturConstants.VITUR_WALKING_SOUTH_NPC_ID) {
            if(getPosition().sameAsMinusFloor(VerzikViturConstants.CENTER_OF_ROOM)) {
                transform(getPhase(), VerzikViturConstants.VITUR_OUT_OF_CHAIR_NPC_ID);

                ArrayList<Player> possibleTargets = getPossibleTargets();

                if(possibleTargets.size() >= 1) {
                    Collections.shuffle(possibleTargets);
                    getCombatBuilder().attack(possibleTargets.get(0));
                }

                this.getCombatBuilder().setTransforming(false);

            }
        }
    }

    public int getBombCount() {
        return bombCount;
    }

    public void setBombCount(int bombCount) {
        this.bombCount = bombCount;
    }

    public int getElectricCount() {
        return electricCount;
    }

    public void setElectricCount(int electricCount) {
        this.electricCount = electricCount;
    }

    public ArrayList<NPC> getMinions() {
        return minions;
    }

    public static final void loadDrops() {
        Map<Integer, NPCDrops.NpcDropItem> items = new HashMap<>();

        for(int itemId : TheatreOfBloodReward.VeryRares) {
            items.put(itemId, new NPCDrops.NpcDropItem(itemId, new int[] { 1 }, 8));
        }

        Arrays.stream(TheatreOfBloodReward.ExtremelyRares).filter(i -> !items.containsKey(i)).forEach(i -> items.put(i, new NPCDrops.NpcDropItem(i, new int[] { 1 }, 10)));

        Arrays.stream(TheatreOfBloodReward.SuperRares).filter(i -> !items.containsKey(i)).forEach(i -> items.put(i, new NPCDrops.NpcDropItem(i, new int[] { 1 }, 7)));

        Arrays.stream(TheatreOfBloodReward.UNCOMMON_REWARDS).filter(i -> !items.containsKey(i)).forEach(i -> items.put(i, new NPCDrops.NpcDropItem(i, new int[] { 1 }, 3)));

        NPCDrops drops = new NPCDrops();
        drops.setDrops(items.values().toArray(new NPCDrops.NpcDropItem[items.size()]));

        NPCDrops.getDrops().put(VerzikViturConstants.VITUR_SITTING_IDLE_NPC_ID, drops);
    }

    @Override
    public void dropItems(Player killer) {

    }
}
