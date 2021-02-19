package com.zyrox.world.content.tutorial;

import com.zyrox.GameSettings;
import com.zyrox.Server;
import com.zyrox.model.Position;
import com.zyrox.world.content.dialogue.Dialogue;
import com.zyrox.world.content.dialogue.DialogueExpression;
import com.zyrox.world.content.dialogue.DialogueManager;
import com.zyrox.world.content.dialogue.DialogueType;
import com.zyrox.world.content.interfaces.StartScreen2;
import com.zyrox.world.entity.impl.player.Player;

/**
 * The tutorial stages for the tutorial intro.
 *
 * @author Gabriel || Wolfsdarker
 */
public enum TutorialStages {


    INITIAL_STAGE(null, "Welcome to " + Server.NAME + "!", "We have a small tutorial that will", "teach you some things about the server.", "Would you like to see it?"),

    SKIP_STAGE(null) {
        @Override
        public Dialogue getDialogue() {
            return new Dialogue() {

                @Override
                public DialogueType type() {
                    return DialogueType.OPTION;
                }

                @Override
                public DialogueExpression animation() {
                    return DialogueExpression.NORMAL;
                }

                public String[] dialogue() {
                    return new String[]{"Yes, show me the tutorial", "No, I would like to skip it"};
                }

                @Override
                public void specialAction(Player player) {
                    player.setDialogueActionId(705);
                    player.setTutorialStage(TutorialStages.values()[ordinal()]);
                    movePlayer(player);
                }
            };
        }

        @Override
        public void sendDialogueText(Player player) {
            player.setDialogueActionId(705);
            super.sendDialogueText(player);
        }

        @Override
        public void stageAction(Player player) {
            player.setDialogueActionId(705);
        }
    },

    SHOPS(new Position(2586, 2559, 1), "These are the shops where you can", "buy items. You can also sell items to", "the general store to get coins!"),
    AUCTION_HOUSE(new Position(2553, 2564, 1), "This is the auction house where you", "can buy, or sell items with other players.", "It's basically player owned shops on crack!"),
    DUEL_ARENA(new Position(3080, 3493), "When you get rich and wanna gamble", "come on over to the duel arena and stake", "your bank against other players!"),
    SKILLING(new Position(2515, 2560,1), "This is the home skilling area.", "You can skill with players that", "are doing other types of skills!"),
    PKING(new Position(3086, 3516), "When you feel like your ready to", "pwn some noobs, come out into the wildy", "and show em what you got."),
    ALTAR_ROOM(new Position(2553, 2547,1), "Switch your spell/prayer books here.", "Don't be a noob, taste vengeance!"),
    THIEVING(new Position(2559, 2572,1), "Steal items from these stalls, and sell", "the items to the merchant for quick cash.", "Be careful, don't get caught or he will call the cops."),
    DONATOR_ZONE(new Position(2066, 3275), "When you wanna show the server", "some love, donate and you will have", "access to this zone that has tons of features."),
    DONATOR2_ZONE(new Position(2914, 2675,1), "Oh yeah we have more features", "access to this area by donating", "You can gain Donor Points and more!"),

    SKILL_TELEPORT(null, "Now to train skills, it is as simple as clicking", "on them and it will teleport you to one of", "the training locations.") {
        @Override
        public void stageAction(Player player) {
            player.getPacketSender().sendTab(GameSettings.SKILLS_TAB);
        }
    },

    QUEST_TAB(null, "Also, within your quest tab, you can see your", "player statistics, which will display how many", "points you have in each currency.") {
        @Override
        public void stageAction(Player player) {
            player.getPacketSender().sendTab(GameSettings.QUESTS_TAB);
        }
    },

    ACHIEVEMENT_TAB(null, "Within the achievements tab, you can see everything", "you must complete to earn the completionist cape.") {
        @Override
        public void stageAction(Player player) {
            player.getPacketSender().sendTab(GameSettings.ACHIEVEMENT_TAB);
        }
    },

    COMMANDS(null, "If you ever need help, remember you have the", "command to check all available commands to you.", "Just simply type '::commands'."),

    DROPS_COMMAND(null, "When you don't know which boss to kill, you can", "simply examine any NPC to bring up its drop table,", "or use ::finddrop / ::findmonster for specifics."),

    SETTINGS_TAB(null, "And if you need to change any settings, you can go", "in the settings tab and change the client's configuration.") {
        @Override
        public void stageAction(Player player) {
            player.getPacketSender().sendTab(GameSettings.OPTIONS_TAB);
        }
    },

    COMPLETED(GameSettings.EDGEVILLE, "The tutorial is over!", "You are now free to play as you wish, Good Luck!") {
        @Override
        public void stageAction(Player player) {

            player.setPlayerLocked(false);

            if (player.newPlayer()) {
                player.setPasswordPlayer(2);
                StartScreen2.open(player);
                player.setHidePlayer(true);
                player.setPlayerLocked(true);

            } else {
                player.setHidePlayer(false);
            }
        }
    },
    ;


    /**
     * The location the player will be teleported once the stage is active.
     */
    private final Position teleportPosition;

    /**
     * The dialogue text.
     */
    private final String[] dialogueText;

    TutorialStages(Position teleportPosition, String... dialogueText) {
        this.teleportPosition = teleportPosition;
        this.dialogueText = dialogueText;
    }

    public String[] getDialogueText() {
        return dialogueText;
    }

    /**
     * Returns the dialogue.
     *
     * @param player
     */
    public void movePlayer(Player player) {
        if (teleportPosition != null) {
            player.moveTo(teleportPosition);
        }
    }

    /**
     * Returns the dialogue for the stage.
     *
     * @return the dialogue
     */
    public Dialogue getDialogue() {
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
            public String[] dialogue() {
                return dialogueText;
            }

            @Override
            public int npcId() {
                return 6139;
            }

            @Override
            public Dialogue nextDialogue() {

                if (TutorialStages.values()[ordinal()] == COMPLETED) {
                    return null;
                }

                TutorialStages next = TutorialStages.values()[ordinal() + 1];

                if (next != null && next.dialogueText != null) {
                    return next.getDialogue();
                }

                return null;
            }

            @Override
            public void specialAction(Player player) {
                player.setTutorialStage(TutorialStages.values()[ordinal()]);
                movePlayer(player);
                stageAction(player);
            }
        };
    }

    /**
     * Sends the dialogue to the player.
     *
     * @param player
     */
    public void sendDialogueText(Player player) {
        DialogueManager.start(player, getDialogue());
        stageAction(player);
    }

    /**
     * The special action for the player.
     *
     * @param player
     */
    public void stageAction(Player player) {
    }
}
