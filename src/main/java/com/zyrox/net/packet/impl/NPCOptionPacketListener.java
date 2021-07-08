package com.zyrox.net.packet.impl;

import com.zyrox.GameServer;
import com.zyrox.engine.task.impl.WalkToTask;
import com.zyrox.engine.task.impl.WalkToTask.FinalizedMovementTask;
import com.zyrox.model.Graphic;
import com.zyrox.model.PlayerRights;
import com.zyrox.model.Position;
import com.zyrox.model.Skill;
import com.zyrox.model.definitions.NpcDefinition;
import com.zyrox.model.input.impl.GambleAmount;
import com.zyrox.model.item.Items;
import com.zyrox.net.packet.Packet;
import com.zyrox.net.packet.PacketListener;
import com.zyrox.world.World;
import com.zyrox.world.content.*;
import com.zyrox.world.content.auction_house.AuctionHouseManager;
import com.zyrox.world.content.combat.CombatFactory;
import com.zyrox.world.content.combat.magic.CombatSpell;
import com.zyrox.world.content.combat.magic.CombatSpells;
import com.zyrox.world.content.combat.weapon.CombatSpecial;
import com.zyrox.world.content.dialogue.Dialogue;
import com.zyrox.world.content.dialogue.DialogueManager;
import com.zyrox.world.content.dialogue.DialogueType;
import com.zyrox.world.content.gamble.GambleItems;
import com.zyrox.world.content.greatolm.Phases;
import com.zyrox.world.content.minigames.impl.WarriorsGuild;
import com.zyrox.world.content.partyroom.PartyRoomManager;
import com.zyrox.world.content.shop.ShopManager;
import com.zyrox.world.content.skill.impl.crafting.Tanning;
import com.zyrox.world.content.skill.impl.dungeoneering.Dungeoneering;
import com.zyrox.world.content.skill.impl.farming.ToolLeprechaun;
import com.zyrox.world.content.skill.impl.fishing.Fishing;
import com.zyrox.world.content.skill.impl.hunter.PuroPuro;
import com.zyrox.world.content.skill.impl.runecrafting.DesoSpan;
import com.zyrox.world.content.skill.impl.slayer.KonarQuoMaten;
import com.zyrox.world.content.skill.impl.slayer.SlayerDialogues;
import com.zyrox.world.content.skill.impl.slayer.SlayerTasks;
import com.zyrox.world.content.skill.impl.slayer.KonarQuoMaten.SlayerTask;
import com.zyrox.world.content.skill.impl.summoning.BossPets;
import com.zyrox.world.content.skill.impl.summoning.Summoning;
import com.zyrox.world.content.skill.impl.summoning.SummoningData;
import com.zyrox.world.content.skill.impl.thieving.PickPocketAction;
import com.zyrox.world.content.skill.impl.thieving.PickPocketableNPC;
import com.zyrox.world.content.transportation.TeleportHandler;
import com.zyrox.world.content.tutorial.TutorialStages;
import com.zyrox.world.entity.impl.npc.NPC;
import com.zyrox.world.entity.impl.npc.click_type.NpcClickType;
import com.zyrox.world.entity.impl.npc.impl.summoning.Pet;
import com.zyrox.world.entity.impl.npc.impl.summoning.SuperiorOlmlet;
import com.zyrox.world.entity.impl.player.Player;

public class NPCOptionPacketListener implements PacketListener {

    private static void firstClick(Player player, Packet packet) {
        int index = packet.readLEShort();
        if (index < 0 || index > World.getNpcs().capacity())
            return;
        final NPC npc = World.getNpcs().get(index);
        if (npc == null)
            return;

        if (npc.getId() == PartyRoomManager.WHITE_KNIGHT) {
            return;
        }

        if (player.getTutorialStage() != TutorialStages.COMPLETED) {
            return;
        }

        Debug.write(player.getName(), "NpcOptionPacketListener - firstClick", new String[] {
                "npcId: "+npc.getId(),
        });

        player.setEntityInteraction(npc);

        if (player.getRights() == PlayerRights.DEVELOPER)
            player.getPacketSender().sendMessage("First click npc id: " + npc.getId());

        if (BossPets.pickup(player, npc)) {
            player.getMovementQueue().reset();
            return;
        }
        player.setWalkToTask(new WalkToTask(player, npc.getPosition(), npc.getSize(), new FinalizedMovementTask() {
            @Override
            public void execute() {
                if (SummoningData.beastOfBurden(npc.getId())) {
                    Summoning summoning = player.getSummoning();
                    if (summoning.getBeastOfBurden() != null && summoning.getFamiliar() != null
                            && summoning.getFamiliar().getSummonNpc() != null
                            && summoning.getFamiliar().getSummonNpc().getIndex() == npc.getIndex()) {
                        summoning.store();
                        player.getMovementQueue().reset();
                    } else {
                        player.getPacketSender().sendMessage("That familiar is not yours!");
                    }
                    return;
                }

                if (ShopManager.isShop(player, npc.getId(), NpcClickType.FIRST_CLICK)) {
                    return;
                }

                npc.clickNpc(player, NpcClickType.FIRST_CLICK);

                switch (npc.getId()) {
                    case 15013:
                        player.setDialogueActionId(504);
                        DialogueManager.start(player, 503);
                        break;

                    case 2580:
                        DialogueManager.sendStatement(player, "Prestige zone / skilling coin shop coming soon");
                        /*npc.forceChat("Off you go!");
                        TeleportHandler.teleportPlayer(player, new Position(2336, 3802),
                                player.getSpellbook().getTeleportType());*/
                        break;
                    case 5210:
                        GameServer.donatorBossManager.openDialog(player);
                        break;
                    case 947:
                        AuctionHouseManager.open(player);
                        break;
                    case 1552:
                        DialogueManager.start(player, 130);
                        break;
                    case 367: // gambler npc
                        player.getPacketSender().sendEnterInputPrompt("How many coins would you like to gamble?");
                        player.setInputHandling(new GambleAmount(GambleItems.COINS, Items.COINS_1, -1));
                        break;

                    case 3021:
                        ToolLeprechaun.talkTo(player);
                        break;

                    case 565:
                        DialogueManager.start(player, new Dialogue() {

                            @Override
                            public DialogueType type() {
                                return DialogueType.OPTION;
                            }

                            @Override
                            public String[] dialogue() {
                                return new String[] {
                                        "Teleport to Bandos",
                                        "Teleport to Saradomin",
                                        "Teleport to Zamorak",
                                        "Teleport to Armadyl"
                                };
                            }

                            @Override
                            public boolean action(int option) {
                                Position position = null;

                                switch(option) {
                                    case 1:
                                        position = new Position(2845, 5335, 2);
                                        break;
                                    case 2:
                                        position = new Position(2917, 5272, 0);
                                        break;
                                    case 3:
                                        position = new Position(2891, 5356, 2);
                                        break;
                                    case 4:
                                        position = new Position(2872, 5268, 2);
                                        break;
                                }

                                if(position != null) {
                                    npc.forceChat("Off you go!");
                                    TeleportHandler.teleportPlayer(player, position,
                                            player.getSpellbook().getTeleportType());
                                }
                                return true;
                            }
                        });
                        break;

                    case 465:
                        player.setDialogueActionId(478);
                        DialogueManager.start(player, 301);
                        break;

                    case 1396:
                        DialogueManager.start(player, 157);
                        player.setDialogueActionId(-1);
                        break;

                    case 543:
                        DialogueManager.start(player, 159);
                        player.setDialogueActionId(709);
                        break;

                    case 457:
                        DialogueManager.start(player, 117);
                        player.setDialogueActionId(74);
                        break;

                    case 8710:
                    case 8707:
                    case 8706:
                    case 8705:
                        EnergyHandler.rest(player);
                        break;

                    case 11226:
                        if (Dungeoneering.doingDungeoneering(player)) {

                        }
                        break;
                    case 241: // Boss point shop npc id ca
                        player.sendMessage("<img=483> You currently have @red@" + player.getBossPoints() + " Boss Points.");
                        break;
                    case 9713:
                        DialogueManager.start(player, 107);
                        player.setDialogueActionId(69);
                        break;
                    case 3101:
                        DialogueManager.start(player, 90);
                        player.setDialogueActionId(57);
                        break;

                    case 1597:
                    case 8275:
                    case 9085:
                    case 7780:
                    case 23623:
                    case 22663:
                        if (npc.getId() != player.getSlayer().getSlayerMaster().getNpcId()) {
                            player.getPacketSender().sendMessage("This is not your current Slayer master.");
                        }
                        DialogueManager.start(player, SlayerDialogues.dialogue(player));
                        break;
                    case 437:
                        DialogueManager.start(player, 99);
                        player.setDialogueActionId(58);
                        break;

                    case 8591:
                        // player.nomadQuest[0] = player.nomadQuest[1] = player.nomadQuest[2] = false;
                        if (!player.getMinigameAttributes().getNomadAttributes().hasFinishedPart(0)) {
                            DialogueManager.start(player, 48);
                            player.setDialogueActionId(23);
                        } else if (player.getMinigameAttributes().getNomadAttributes().hasFinishedPart(0)
                                && !player.getMinigameAttributes().getNomadAttributes().hasFinishedPart(1)) {
                            DialogueManager.start(player, 50);
                            player.setDialogueActionId(24);
                        } else if (player.getMinigameAttributes().getNomadAttributes().hasFinishedPart(1))
                            DialogueManager.start(player, 53);
                        break;
                    case 3385:
                        if (player.getMinigameAttributes().getRecipeForDisasterAttributes().hasFinishedPart(0) && player
                                .getMinigameAttributes().getRecipeForDisasterAttributes().getWavesCompleted() < 6) {
                            DialogueManager.start(player, 39);
                            return;
                        }
                        if (player.getMinigameAttributes().getRecipeForDisasterAttributes().getWavesCompleted() == 6) {
                            DialogueManager.start(player, 46);
                            return;
                        }
                        DialogueManager.start(player, 38);
                        player.setDialogueActionId(20);
                        break;
                    case 6139:
                        DialogueManager.start(player, 29);
                        player.setDialogueActionId(17);
                        break;
                    case 741:
                       // ShopManager.openShop(player, 56);
                        break;
                    case 3789:
                        player.getPacketSender().sendInterface(18730);
                        player.getPacketSender().sendString(18729,
                                "Commendations: " + Integer.toString(player.getPointsHandler().getCommendations()));
                        break;
                    case 2948:
                        DialogueManager.start(player, WarriorsGuild.warriorsGuildDialogue(player));
                        break;

                    case 6055:
                    case 6056:
                    case 6057:
                    case 6058:
                    case 6059:
                    case 6060:
                    case 6061:
                    case 6062:
                    case 6063:
                    case 6064:
                    case 7903:

                        PuroPuro.catchImpling(player, npc);
                        break;
                    case 8022:
                    case 8028:
                        DesoSpan.siphon(player, npc);
                        break;
                    case 2579:
                        player.setDialogueActionId(13);
                        DialogueManager.start(player, 24);
                        break;
                    case 5555:
                        player.setDialogueActionId(368);
                        DialogueManager.start(player, 368);
                        break;
                    case 23390:
                        player.setDialogueActionId(10);
                        DialogueManager.start(player, 19);
                        break;
                    case 4249:
                        player.setDialogueActionId(9);
                        DialogueManager.start(player, 64);
                        break;
                   /* case 3777: // First option
                        player.getPacketSender().sendInterface(72000);
                        player.sendMessage("This is not your First pet.");
                        break;*/
                    case 6807:
                    case 6994:
                    case 6995:
                    case 6867:
                    case 6868:
                    case 6794:
                    case 6795:
                    case 6815:
                    case 6816:
                    case 6874:
                    case 6873:
                    case 3594:
                    case 3590:
                    case 3596:
                        if (player.getSummoning().getFamiliar() == null
                                || player.getSummoning().getFamiliar().getSummonNpc() == null
                                || player.getSummoning().getFamiliar().getSummonNpc().getIndex() != npc.getIndex()) {
                            player.getPacketSender().sendMessage("That is not your familiar.");
                            return;
                        }
                        player.getSummoning().store();
                        break;
                    case 3641:
                        player.setDialogueActionId(8);
                        DialogueManager.start(player, 13);
                        break;
                    case 6970:
                        player.setDialogueActionId(3);
                        DialogueManager.start(player, 3);
                        break;
                    case 4657:
                        player.setDialogueActionId(5);
                        DialogueManager.start(player, 5);
                        break;
                    case 318:
                    case 316:
                    case 313:
                    case 312:
                        player.setEntityInteraction(npc);
                        Fishing.setupFishing(player, Fishing.forSpot(npc.getId(), false));
                        break;

                    case 2676:
                        player.getPacketSender().sendInterface(3559);
                        player.getAppearance().setCanChangeAppearance(true);
                        break;
                    case 494:
                    case 8003:
                    case 1360:
                        player.setTempBankTabs(null);
                        player.getBank(player.getCurrentBankTab()).open();
                        break;
                }
                if (!(npc.getId() >= 8705 && npc.getId() <= 8710)) {
                    npc.setPositionToFace(player.getPosition());
                }
                player.setPositionToFace(npc.getPosition());
            }
        }));
    }

    private static void attackNPC(Player player, Packet packet) {
        int index = packet.readShortA();

        if (index < 0 || index > World.getNpcs().capacity())
            return;

        final NPC interact = World.getNpcs().get(index);

        if (player.getTutorialStage() != TutorialStages.COMPLETED) {
            return;
        }

        if (interact == null || NpcDefinition.getDefinitions()[interact.getId()] == null)
            return;

        if (player.isSpectatorMode()) {
            player.sendMessage("You cannot do this in spectator mode.");
            return;
        }

        if (!NpcDefinition.getDefinitions()[interact.getId()].isAttackable()) {
            return;
        }

        if (interact.getConstitution() <= 0) {
            player.getMovementQueue().reset();
            return;
        }

        if (interact.getId() == Phases.OLM_HEAD
                && player.getMinigameAttributes().getRaidsAttributes().getParty().isTransitionPhase()) {
            return;
        }

        Debug.write(player.getName(), "NpcOptionPacketListener - attackNpc", new String[] {
                "npcId: "+interact.getId(),
        });


        if (player.getCombatBuilder().getStrategy() == null) {
            player.getCombatBuilder().determineStrategy();
        }

        if (CombatFactory.checkAttackDistance(player, interact)) {
            player.getMovementQueue().reset();
        }

        player.getCombatBuilder().attack(interact);
    }

    public void handleSecondClick(Player player, Packet packet) {
        int index = packet.readLEShortA();
        if (index < 0 || index > World.getNpcs().capacity())
            return;
        final NPC npc = World.getNpcs().get(index);
        if (npc == null)
            return;

        if (player.getTutorialStage() != TutorialStages.COMPLETED) {
            return;
        }

        Debug.write(player.getName(), "NpcOptionPacketListener - secondCLick", new String[] {
                "npcId: "+npc.getId(),
        });

        player.setEntityInteraction(npc);
        final int npcId = npc.getId();
        if (player.getRights() == PlayerRights.DEVELOPER)
            player.getPacketSender().sendMessage("Second click npc id: " + npcId);
        player.setWalkToTask(new WalkToTask(player, npc.getPosition(), npc.getSize(), new FinalizedMovementTask() {
            @Override
            public void execute() {

                npc.clickNpc(player, NpcClickType.SECOND_CLICK);

                PickPocketableNPC pocket = PickPocketableNPC.get(npc.getId());
                if (pocket != null) {
                    if (!player.getClickDelay().elapsed(3000))
                        return;
                    player.getClickDelay().reset();
                    player.getActionManager().setAction(new PickPocketAction(npc, pocket));
                    return;
                }

                if (ShopManager.isShop(player, npc.getId(), NpcClickType.SECOND_CLICK)) {
                    return;
                }

                switch (npc.getId()) {
                    case SuperiorOlmlet.ID:

                        if (player.getSummoning().getFamiliar() == null) {
                            player.sendMessage("This is not your pet.");
                            return;
                        }

                        if (player.getSummoning().getFamiliar().getSummonNpc() instanceof SuperiorOlmlet) {
                            Pet.toggleEffect(player);
                        }
                        break;
                    case 3777: // First option
                        player.getPacketSender().sendInterface(72000);
                        player.sendMessage("[Npc Manager]:@blu@ Viewing Drop Simulator Interface.");
                        break;

                    case 2580:
                        DialogueManager.sendStatement(player, "Skilling coin shop coming soon");
                        /*npc.forceChat("Off you go!");
                        TeleportHandler.teleportPlayer(player, new Position(2336, 3802),
                                player.getSpellbook().getTeleportType());*/
                        break;


                    case 23492:
                    case 23493:
                    case 23494:
                    case 23495:

                        if (player.getSummoning().getFamiliar() == null) {
                            player.sendMessage("This is not your pet.");
                            return;
                        }
                        BossPets.metamorphosis(player, npc);
                        break;

                    case 5210:
                        GameServer.donatorBossManager.openInstanceSelection(player);
                        break;


                    case 457:
                        player.getPacketSender().sendMessage("The ghost teleports you away.");
                        player.getPacketSender().sendInterfaceRemoval();
                        player.moveTo(new Position(3651, 3486));
                        break;

                    case 3021:
                        ToolLeprechaun.exchange(player);
                        break;
                    case 462:
                        npc.performAnimation(CombatSpells.CONFUSE.getSpell().castAnimation().get());
                        npc.forceChat("Off you go!");
                        TeleportHandler.teleportPlayer(player, new Position(2911, 4832),
                                player.getSpellbook().getTeleportType());
                        break;
                    case 3101:
                        DialogueManager.start(player, 95);
                        player.setDialogueActionId(57);
                        break;
                    case 4657:
                        // MySQLController.getStore().claim(player);
                        break;
                    case 1597:
                    case 9085:
                    case 7780:
                    case 8275:
                    case 22663:
                        if (npc.getId() != player.getSlayer().getSlayerMaster().getNpcId()) {
                            player.getPacketSender().sendMessage("This is not your current Slayer master.");
                            return;
                        }
                        if (player.getSlayer().getSlayerTask() == SlayerTasks.NO_TASK)
                            player.getSlayer().assignTask();
                        else
                            DialogueManager.start(player, SlayerDialogues.findAssignment(player));
                        break;
                    case 23623:
                        if (npc.getId() != player.getSlayer().getSlayerMaster().getNpcId()) {
                            player.getPacketSender().sendMessage("This is not your current Slayer master.");
                            return;
                        }
                        if (player.getSlayer().getTask().equals(SlayerTask.NO_TASK)) {
                            KonarQuoMaten.getAssignment(player);
                        } else {
                            DialogueManager.start(player, SlayerDialogues.findAssignment(player));
                        }
                        break;
                    case 805:
                        Tanning.selectionInterface(player);
                        break;
                    case 318:
                    case 316:
                    case 313:
                    case 312:
                        player.setEntityInteraction(npc);
                        Fishing.setupFishing(player, Fishing.forSpot(npc.getId(), true));
                        break;
                    case 6970:
                        player.setDialogueActionId(35);
                        DialogueManager.start(player, 63);
                        break;
                }
                npc.setPositionToFace(player.getPosition());
                player.setPositionToFace(npc.getPosition());
            }
        }));
    }

    public void handleThirdClick(Player player, Packet packet) {
        int index = packet.readShort();
        if (index < 0 || index > World.getNpcs().capacity())
            return;
        final NPC npc = World.getNpcs().get(index);
        if (npc == null)
            return;

        if (player.getTutorialStage() != TutorialStages.COMPLETED) {
            return;
        }

        Debug.write(player.getName(), "NpcOptionPacketListener - thirdClick", new String[] {
                "npcId: "+npc.getId(),
        });

        player.setEntityInteraction(npc).setPositionToFace(npc.getPosition().copy());
        npc.setPositionToFace(player.getPosition());
        if (player.getRights() == PlayerRights.DEVELOPER)
            player.getPacketSender().sendMessage("Third click npc id: " + npc.getId());
        player.setWalkToTask(new WalkToTask(player, npc.getPosition(), npc.getSize(), new FinalizedMovementTask() {
            @Override
            public void execute() {

                npc.clickNpc(player, NpcClickType.THIRD_CLICK);

                if (ShopManager.isShop(player, npc.getId(), NpcClickType.THIRD_CLICK)) {
                    return;
                }

                switch (npc.getId()) {
                    case 3777: // seccond option
                        player.getPacketSender().sendInterface(37600);
                        player.sendMessage("[Npc Manager]:@blu@ You can 'Examine' any other Npc In-game for his drops! enjoy.");
                        break;
                    case 3021:
                        ToolLeprechaun.teleport(player);
                        break;
                    case 3641:
                        LoyaltyProgramme.open(player);
                        break;
                    case 4657:
                        DialogueManager.start(player, MemberScrolls.getTotalFunds(player));
                        break;
                    case 546:
                        Enchanting.open(player);
                        break;
                    case 3101:
                        ShopManager.openShop(player, 55);
                        break;
                    case 1597:
                    case 9085:
                    case 8275:
                    case 7780:
                    case 23623:
                    case 22663:
                        ShopManager.openShop(player, 28);
                        break;
                    case 2580:
                        DialogueManager.sendStatement(player, "Prestige zone coming soon");
                        /*npc.forceChat("Off you go!");
                        TeleportHandler.teleportPlayer(player, new Position(2336, 3802),
                                player.getSpellbook().getTeleportType());*/
                        break;
                    case 961:
                        if (player.getRights() == PlayerRights.PLAYER) {
                            player.getPacketSender().sendMessage("This feature is currently only available for members.");
                            return;
                        }
                        boolean restore = player.getSpecialPercentage() < 100;
                        if (restore) {
                            player.setSpecialPercentage(100);
                            CombatSpecial.updateBar(player);
                            player.getPacketSender().sendMessage("Your special attack energy has been restored.");
                        }
                        for (Skill skill : Skill.values()) {
                            if (player.getSkillManager().getCurrentLevel(skill) < player.getSkillManager()
                                    .getMaxLevel(skill)) {
                                player.getSkillManager().setCurrentLevel(skill,
                                        player.getSkillManager().getMaxLevel(skill));
                                restore = true;
                            }
                        }
                        if (restore) {
                            player.performGraphic(new Graphic(1302));
                            player.getPacketSender().sendMessage("Your stats have been restored.");
                        } else
                            player.getPacketSender().sendMessage("Your stats do not need to be restored at the moment.");
                        break;
                }
                npc.setPositionToFace(player.getPosition());
                player.setPositionToFace(npc.getPosition());
            }
        }));
    }

    public void handleFourthClick(Player player, Packet packet) {
        int index = packet.readLEShort();
        if (index < 0 || index > World.getNpcs().capacity())
            return;
        final NPC npc = World.getNpcs().get(index);
        if (npc == null)
            return;

        if (player.getTutorialStage() != TutorialStages.COMPLETED) {
            return;
        }

        Debug.write(player.getName(), "NpcOptionPacketListener - fourthClick", new String[] {
                "npcId: "+npc.getId(),
        });

        player.setEntityInteraction(npc);
        if (player.getRights() == PlayerRights.DEVELOPER)
            player.getPacketSender().sendMessage("Fourth click npc id: " + npc.getId());
        player.setWalkToTask(new WalkToTask(player, npc.getPosition(), npc.getSize(), new FinalizedMovementTask() {
            @Override
            public void execute() {

                npc.clickNpc(player, NpcClickType.FOURTH_CLICK);

                if (ShopManager.isShop(player, npc.getId(), NpcClickType.FOURTH_CLICK)) {
                    return;
                }

                switch (npc.getId()) {
                    case 3777: // seccond option
                        player.setKillsTrackerOpen(true);
                        KillsTracker.resetInterface(player);
                        player.getPacketSender().sendInterface(71000);
                        player.sendMessage("[Npc Manager]:@blu@ Viewing Npc Kill Tracker Log."); //Third
                        break;
                    case 4657:
                        if (player.getRights() == PlayerRights.PLAYER) {
                            player.getPacketSender().sendMessage("You need to be a member to teleport to this zone.")
                                    .sendMessage("To become a member, visit Zyrox.org and purchase a scroll.");
                            return;
                        }
                        TeleportHandler.teleportPlayer(player, new Position(2337, 9799),
                                player.getSpellbook().getTeleportType());
                        break;
                    case 1597:
                    case 9085:
                    case 8275:
                    case 7780:
                    case 23623:
                    case 22663:
                        player.getPacketSender().sendString(36030,
                                "Current Points:   " + player.getPointsHandler().getSlayerPoints());
                        player.getPacketSender().sendInterface(36000);
                        break;
                }
                npc.setPositionToFace(player.getPosition());
                player.setPositionToFace(npc.getPosition());
            }
        }));
    }

    @Override
    public void handleMessage(Player player, Packet packet) {
        if (player.isTeleporting() || player.isPlayerLocked() || player.getMovementQueue().isLockMovement()) {
            return;
        }

        if (player.isAccountCompromised() || player.requiresUnlocking()) {
            return;
        }

        switch (packet.getOpcode()) {
            case ATTACK_NPC:
                attackNPC(player, packet);
                break;
            case FIRST_CLICK_OPCODE:
                firstClick(player, packet);
                break;
            case SECOND_CLICK_OPCODE:
                handleSecondClick(player, packet);
                break;
            case THIRD_CLICK_OPCODE:
                handleThirdClick(player, packet);
                break;
            case FOURTH_CLICK_OPCODE:
                handleFourthClick(player, packet);
                break;
            case MAGE_NPC:
                int npcIndex = packet.readLEShortA();
                int spellId = packet.readShortA();

                if (npcIndex < 0 || spellId < 0 || npcIndex > World.getNpcs().capacity()) {
                    return;
                }

                if (player.getTutorialStage() != TutorialStages.COMPLETED) {
                    return;
                }

                NPC n = World.getNpcs().get(npcIndex);
                player.setEntityInteraction(n);

                CombatSpell spell = CombatSpells.getSpell(spellId);

                if (n == null || spell == null) {
                    player.getMovementQueue().reset();
                    return;
                }

                Debug.write(player.getName(), "NpcOptionPacketListener - mageNpc", new String[] {
                        "npcId: "+n.getId(),
                });

                if (!NpcDefinition.getDefinitions()[n.getId()].isAttackable()) {
                    player.getMovementQueue().reset();
                    return;
                }

                if (n.getConstitution() <= 0) {
                    player.getMovementQueue().reset();
                    return;
                }

                if (n.getId() == Phases.OLM_HEAD
                        && player.getMinigameAttributes().getRaidsAttributes().getParty().isTransitionPhase()) {
                    return;
                }

                if (player.getEquipment().contains(51015)) {
                    player.getPacketSender().sendMessage("Your bulwark gets in the way.");
                    player.getMovementQueue().reset();
                    player.getCombatBuilder().reset(true);
                    return;
                }

                player.setPositionToFace(n.getPosition());
                player.setCastSpell(spell);
                if (player.getCombatBuilder().getStrategy() == null) {
                    player.getCombatBuilder().determineStrategy();
                }
                if (CombatFactory.checkAttackDistance(player, n)) {
                    player.getMovementQueue().reset();
                }
                player.getCombatBuilder().resetCooldown();
                player.getCombatBuilder().attack(n);
                break;
        }
    }

    public static final int ATTACK_NPC = 72, FIRST_CLICK_OPCODE = 155, MAGE_NPC = 131, SECOND_CLICK_OPCODE = 17,
            THIRD_CLICK_OPCODE = 21, FOURTH_CLICK_OPCODE = 18;
}
