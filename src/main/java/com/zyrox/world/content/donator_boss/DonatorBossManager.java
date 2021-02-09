package com.zyrox.world.content.donator_boss;

import com.zyrox.model.Graphic;
import com.zyrox.model.Item;
import com.zyrox.model.Position;
import com.zyrox.util.Misc;
import com.zyrox.world.World;
import com.zyrox.world.content.dialogue.Dialogue;
import com.zyrox.world.content.dialogue.DialogueExpression;
import com.zyrox.world.content.dialogue.DialogueManager;
import com.zyrox.world.content.dialogue.DialogueType;
import com.zyrox.world.entity.impl.npc.NPC;
import com.zyrox.world.entity.impl.player.Player;

/**
 * Created by Jonny on 7/6/2019
 **/
public class DonatorBossManager {

    private NPC boss;

    public DonatorBossManager() {

    }

    public void openDialog(Player player) {

        DialogueManager.start(player, new Dialogue() {

            @Override
            public String[] dialogue() {
                return new String[] {"Amongst the greatest fighters live a champion.",
                        "Would you like to solo bosses?"};
            }

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
                return 5210;
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
                                "Open monster selection",
                                "Cancel",
                        };
                    }

                    @Override
                    public boolean action(int option) {
                        switch (option) {
                            case 1:
                                openInstanceSelection(player);
                                break;
                        }
                        return false;
                    }
                };
            }
        });
    }

    public void openInstanceSelection(Player player) {

        viewMonster(player, player.viewingMonster);

        viewMonsterList(player);

        int scrollMax = DonatorBossMonster.values().length * 27;

        if(scrollMax < 231) {
            scrollMax = 231;
        }

        player.getPA().sendScrollMax(60100, scrollMax);

        player.getPA().sendInterface(DonatorBossConstants.INTERFACE_ID);
    }

    public void viewMonsterList(Player player) {
        int id = 60100;

        for(DonatorBossMonster donatorBossMonster : DonatorBossMonster.values()) {
            boolean unlocked = player.donatorBossMonstersUnlocked.contains(donatorBossMonster);

            player.getPA().sendFrame126((unlocked ? "@gre@" : "@red@") + Misc.capitalize(donatorBossMonster.getName()), id += 2);
        }
    }

    public void viewMonster(Player player, DonatorBossMonster donatorBossMonster) {

        player.viewingMonster = donatorBossMonster;

        player.getPA().sendNpcOnInterface(donatorBossMonster.getNpcId(), donatorBossMonster.getModelZoom());

        player.getPA().sendFrame126(Misc.capitalize(donatorBossMonster.getName()), 60030);

        if(player.donatorBossMonstersUnlocked.contains(donatorBossMonster)) {
            player.getPA().sendFrame126("@gre@This boss is currently unlocked.", 60034);
            player.getPA().sendFrame126("@gre@Unlock fee: "+ Misc.formatRunescapeStyle(donatorBossMonster.getPrice())+" coins.", 60035);
            player.getPA().sendFrame126("Spawn boss", 60037);
        } else {
            player.getPA().sendFrame126("@red@This boss is currently locked.", 60034);
            player.getPA().sendFrame126("@red@Unlock fee: "+ Misc.formatRunescapeStyle(donatorBossMonster.getPrice())+" coins.", 60035);
            player.getPA().sendFrame126("Unlock", 60037);
        }

    }

    public void spawnInstance(Player player, DonatorBossMonster donatorBossMonster) {

        if(!inArea(player)) {
            player.sendMessage("You must be at the donator area in order to spawn this.");
            return;
        }

        Position spawnPosition = DonatorBossConstants.BOSS_SPAWN_POSITION.copy();

        if(boss != null && boss.getConstitution() > 0) {
            player.sendMessage("You must kill the current boss before spawning another.");
            return;
        }


		if(!donatorBossMonster.customSpawn(player)) {
            boss = new NPC(donatorBossMonster.getNpcId(), spawnPosition);
            World.register(boss);
        }

        NPC instanceFighter = World.getNpcById(5210);

        if(instanceFighter != null) {
            instanceFighter.performGraphic(new Graphic(110));
        }

        player.getPA().sendInterfaceRemoval();
    }

    public void unlockInstance(Player player, DonatorBossMonster donatorBossMonster) {
        DialogueManager.start(player, new Dialogue() {

            @Override
            public boolean closeInterface() {
                return false;
            }

            @Override
            public DialogueType type() {
                return DialogueType.ITEM_STATEMENT;
            }

            @Override
            public String[] dialogue() {
                return new String[]{"Do you want to unlock "+Misc.capitalize(donatorBossMonster.getName())+" for "+Misc.formatRunescapeStyle(donatorBossMonster.getPrice())+" coins?"
                };
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
            public Dialogue nextDialogue() {
                return new Dialogue() {

                    @Override
                    public boolean closeInterface() {
                        return false;
                    }

                    @Override
                    public DialogueType type() {
                        return DialogueType.OPTION;
                    }

                    @Override
                    public String[] dialogue() {
                        return new String[]{
                                "Yes, unlock for "+Misc.formatRunescapeStyle(donatorBossMonster.getPrice())+" coins",
                                "Cancel",
                        };
                    }

                    @Override
                    public boolean action(int option) {
                        player.getPA().closeDialogueOnly();
                        switch(option) {
                            case 1:
                                Item coins = new Item(995, donatorBossMonster.getPrice());
                                if(player.getInventory().contains(coins)) {
                                    player.getInventory().delete(coins);
                                    player.donatorBossMonstersUnlocked.add(donatorBossMonster);
                                    viewMonster(player, donatorBossMonster);
                                    viewMonsterList(player);
                                } else {
                                    player.sendMessage("<col=ff0000>You need to have "+Misc.formatRunescapeStyle(donatorBossMonster.getPrice())+" coins in your inventory to unlock this.");
                                }
                                break;
                        }
                        return false;
                    }
                };
            }
        });
    }

    public boolean isButton(Player player, int buttonId) {

        int buttonToCheck = 60101;

        for(DonatorBossMonster donatorBossMonster : DonatorBossMonster.values()) {

            if(buttonId == buttonToCheck) {
                viewMonster(player, donatorBossMonster);
                return true;
            }

            buttonToCheck += 2;
        }

        switch(buttonId) {
            case 60029:
                player.getPA().sendInterfaceRemoval();
                return true;
            case 60031: //unlock/spawn
            case 60036: //unlock/spawn

                DonatorBossMonster donatorBossMonster = player.viewingMonster;

                if(donatorBossMonster != null) {
                    if(player.donatorBossMonstersUnlocked.contains(donatorBossMonster)) {
                        spawnInstance(player, donatorBossMonster);
                    } else {
                        unlockInstance(player, donatorBossMonster);
                    }
                }
                break;
        }
        return false;
    }

    public boolean inArea(Player player) {
        return player.getPosition().isWithinBoundary(2025, 2073, 3230, 3264);
    }

    public int getUnlockAllPrice(Player player) {
        int price = 0;

        for(DonatorBossMonster donatorBossMonster : DonatorBossMonster.values()) {
            if(!player.donatorBossMonstersUnlocked.contains(donatorBossMonster)) {
                price += (int)(donatorBossMonster.getPrice() * .8);
            }
        }
        return price;
    }

}
