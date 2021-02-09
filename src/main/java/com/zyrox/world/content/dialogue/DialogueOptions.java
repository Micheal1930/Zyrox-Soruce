package com.zyrox.world.content.dialogue;

import com.zyrox.model.*;
import com.zyrox.model.Locations.Location;
import com.zyrox.model.input.impl.BuyShards;
import com.zyrox.model.input.impl.ChangePassword;
import com.zyrox.model.input.impl.DonateToWell;
import com.zyrox.model.input.impl.PaidYell;
import com.zyrox.model.input.impl.SetEmail;
import com.zyrox.net.packet.command.impl.MagicCommand;
import com.zyrox.net.packet.command.impl.PrayerCommand;
import com.zyrox.util.Misc;
import com.zyrox.world.World;
import com.zyrox.world.content.*;
import com.zyrox.world.content.Achievements.AchievementData;
import com.zyrox.world.content.Gambling.FlowersData;
import com.zyrox.world.content.LoyaltyProgramme.LoyaltyTitles;
import com.zyrox.world.content.clan.ClanChatManager;
import com.zyrox.world.content.combat.strategy.impl.CorporealBeast;
import com.zyrox.world.content.dialogue.impl.AgilityTicketExchange;
import com.zyrox.world.content.dialogue.impl.BloodMoneyDealer;
import com.zyrox.world.content.freeforall.FreeForAll;
import com.zyrox.world.content.instance.PlayerInstanceManager;
import com.zyrox.world.content.instance.impl.CorporealBeastPlayerInstance;
import com.zyrox.world.content.instance.impl.NexPlayerInstance;
import com.zyrox.world.content.instances.InstanceManager;
import com.zyrox.world.content.minigames.impl.Graveyard;
import com.zyrox.world.content.minigames.impl.Nomad;
import com.zyrox.world.content.minigames.impl.RecipeForDisaster;
import com.zyrox.world.content.partyroom.PartyRoomManager;
import com.zyrox.world.content.shop.ShopManager;
import com.zyrox.world.content.skill.SkillManager;
import com.zyrox.world.content.skill.impl.dungeoneering.Dungeoneering;
import com.zyrox.world.content.skill.impl.dungeoneering.DungeoneeringFloor;
import com.zyrox.world.content.skill.impl.herblore.Decanting;
import com.zyrox.world.content.skill.impl.mining.Mining;
import com.zyrox.world.content.skill.impl.slayer.KonarQuoMaten;
import com.zyrox.world.content.skill.impl.slayer.Slayer;
import com.zyrox.world.content.skill.impl.slayer.SlayerDialogues;
import com.zyrox.world.content.skill.impl.slayer.SlayerMaster;
import com.zyrox.world.content.skill.impl.summoning.CharmingImp;
import com.zyrox.world.content.skill.impl.summoning.SummoningTab;
import com.zyrox.world.content.transportation.JewelryTeleporting;
import com.zyrox.world.content.transportation.TeleportHandler;
import com.zyrox.world.content.transportation.TeleportScrolls;
import com.zyrox.world.content.transportation.TeleportType;
import com.zyrox.world.content.tutorial.TutorialStages;
import com.zyrox.world.content.well_of_goodwill.WellBenefit;
import com.zyrox.world.entity.impl.npc.NpcAggression;
import com.zyrox.world.entity.impl.npc.click_type.NpcClickType;
import com.zyrox.world.entity.impl.player.Player;

public class DialogueOptions {

    public static void handle(Player player, int id) {
        if (player.getRights().getPrivilegeLevel() >= PlayerRights.DEVELOPER.getPrivilegeLevel()) {
            player.getPacketSender()
                    .sendMessage("Dialogue button id: " + id + ", action id: " + player.getDialogueActionId())
                    .sendConsoleMessage("Dialogue button id: " + id + ", action id: " + player.getDialogueActionId());
        }

        System.out.println(id);

        if (Effigies.handleEffigyAction(player, id)) {
            return;
        }
        if (id == FIRST_OPTION_OF_FIVE) {

            if(player.getDialogue() != null) {
                if(player.getDialogue().action(1)) {
                    return;
                }
            }

            switch (player.getDialogueActionId()) {
                case 500:
                    Gambling.plantSeed(player, FlowersData.PASTEL_FLOWERS);
                    break;
                case 501:
                    Gambling.plantSeed(player, FlowersData.PURPLE_FLOWERS);
                    break;
                case 491:
                    if (player.getInventory().getAmount(995) > 9999999) {
                        player.getPA().sendInterfaceRemoval();
                        player.getInventory().delete(995, 10000000);
                        player.getPA().sendMessage("You have successfully purchased this achievement");
                        Achievements.finishAchievement(player, AchievementData.FIGHT_ANOTHER_PLAYER);
                        Achievements.updateInterface(player);

                    } else {
                        player.getPA().sendInterfaceRemoval();
                        player.getPA().sendMessage("@red@You don't have enough coins in your inventory");
                    }

                    break;
                case 490:
                    if (player.getInventory().getAmount(995) > 9999999) {
                        player.getPA().sendInterfaceRemoval();
                        player.getInventory().delete(995, 10000000);
                        player.getPA().sendMessage("You have successfully purchased this title");
                        LoyaltyProgramme.unlock(player, LoyaltyTitles.KILLER);
                    } else {
                        player.getPA().sendInterfaceRemoval();
                        player.getPA().sendMessage("@red@You don't have enough coins in your inventory");
                    }

                    break;

                case 133:
                    FreeForAll.startDh(player);
                    break;

                case 0:
                    TeleportHandler.teleportPlayer(player, new Position(2695, 3714),
                            player.getSpellbook().getTeleportType());
                    break;
                case 1:
                    TeleportHandler.teleportPlayer(player, new Position(3420, 3510),
                            player.getSpellbook().getTeleportType());
                    break;
                case 2:
                    TeleportHandler.teleportPlayer(player, new Position(3235, 3295),
                            player.getSpellbook().getTeleportType());
                    break;
                case 9:
                    DialogueManager.start(player, 16);
                    break;
                case 10:
                    Artifacts.sellArtifacts(player);
                    break;
                case 11:
                    Scoreboards.open(player, Scoreboards.TOP_KILLSTREAKS);
                    break;
                case 12:
                    TeleportHandler.teleportPlayer(player, new Position(3087, 3517),
                            player.getSpellbook().getTeleportType());
                    break;
                case 13:
                    player.setDialogueActionId(78);
                    DialogueManager.start(player, 124);
                    break;
                case 14:
                    TeleportHandler.teleportPlayer(player, new Position(3097 + Misc.getRandom(1), 9869 + Misc.getRandom(1)),
                            player.getSpellbook().getTeleportType());
                    break;
                case 15:
                    player.getPacketSender().sendInterfaceRemoval();
                    int total = player.getSkillManager().getMaxLevel(Skill.ATTACK)
                            + player.getSkillManager().getMaxLevel(Skill.STRENGTH);
                    boolean has99 = player.getSkillManager().getMaxLevel(Skill.ATTACK) >= 99
                            || player.getSkillManager().getMaxLevel(Skill.STRENGTH) >= 99;
                    if (total < 130 && !has99) {
                        player.getPacketSender().sendMessage("");
                        player.getPacketSender().sendMessage("You do not meet the requirements of a Warrior.");
                        player.getPacketSender()
                                .sendMessage("You need to have a total Attack and Strength level of at least 140.");
                        player.getPacketSender().sendMessage("Having level 99 in either is fine aswell.");
                        return;
                    }
                    TeleportHandler.teleportPlayer(player, new Position(2855, 3543),
                            player.getSpellbook().getTeleportType());
                    break;
                case 17:
                    player.setInputHandling(new SetEmail());
                    player.getPacketSender().sendEnterInputPrompt("Enter your email address:");
                    break;
                case 29:
                    SlayerMaster.changeSlayerMaster(player, SlayerMaster.VANNAKA);
                    break;
                case 36:
                    TeleportHandler.teleportPlayer(player, new Position(2871, 5318, 2),
                            player.getSpellbook().getTeleportType());
                    break;
                case 38:
                    TeleportHandler.teleportPlayer(player, new Position(2273, 4681),
                            player.getSpellbook().getTeleportType());
                    break;
                case 40:
                    TeleportHandler.teleportPlayer(player, new Position(3476, 9502),
                            player.getSpellbook().getTeleportType());
                    break;
                case 41:
                    TeleportHandler.teleportPlayer(player, new Position(2886, 4376),
                            player.getSpellbook().getTeleportType());
                    break;
                // corp
                case 48:
                    JewelryTeleporting.teleport(player, new Position(3088, 3506));
                    break;
                case 59:
                    if (player.getClickDelay().elapsed(1500)) {
                        PkSets.buySet(player, PkSets.PURE_SET);
                    }
                    break;
                case 60:
                    player.setDialogueActionId(61);
                    DialogueManager.start(player, 102);
                    break;
                case 67:
                    player.getPacketSender().sendInterfaceRemoval();
                    if (player.getMinigameAttributes().getDungeoneeringAttributes().getParty() != null) {
                        if (player.getMinigameAttributes().getDungeoneeringAttributes().getParty().getOwner().getUsername()
                                .equals(player.getUsername())) {
                            player.getMinigameAttributes().getDungeoneeringAttributes().getParty()
                                    .setDungeoneeringFloor(DungeoneeringFloor.FIRST_FLOOR);
                            player.getMinigameAttributes().getDungeoneeringAttributes().getParty()
                                    .sendMessage("The party leader has changed floor.");
                            player.getMinigameAttributes().getDungeoneeringAttributes().getParty().refreshInterface();
                        }
                    }
                    break;
                case 68:
                    player.getPacketSender().sendInterfaceRemoval();
                    if (player.getMinigameAttributes().getDungeoneeringAttributes().getParty() != null) {
                        if (player.getMinigameAttributes().getDungeoneeringAttributes().getParty().getOwner().getUsername()
                                .equals(player.getUsername())) {
                            player.getMinigameAttributes().getDungeoneeringAttributes().getParty().setComplexity(1);
                            player.getMinigameAttributes().getDungeoneeringAttributes().getParty()
                                    .sendMessage("The party leader has changed complexity.");
                            player.getMinigameAttributes().getDungeoneeringAttributes().getParty().refreshInterface();
                        }
                    }
                    break;

            }
        } else if (id == SECOND_OPTION_OF_FIVE) {

            if(player.getDialogue() != null) {
                if(player.getDialogue().action(2)) {
                    return;
                }
            }

            switch (player.getDialogueActionId()) {
                case 500:
                    Gambling.plantSeed(player, FlowersData.RED_FLOWERS);
                    break;
                case 501:
                    Gambling.plantSeed(player, FlowersData.ORANGE_FLOWERS);
                    break;
                case 491:
                    if (player.getInventory().getAmount(995) > 49999999) {
                        player.getPA().sendInterfaceRemoval();
                        player.getInventory().delete(995, 50000000);
                        player.getPA().sendMessage("You have successfully purchased this achievement");
                        Achievements.finishAchievement(player, AchievementData.DEFEAT_10_PLAYERS);
                        Achievements.updateInterface(player);

                    } else {
                        player.getPA().sendInterfaceRemoval();
                        player.getPA().sendMessage("@red@You don't have enough coins in your inventory");
                    }

                    break;
                case 490:
                    if (player.getInventory().getAmount(995) > 99999999) {
                        player.getPA().sendInterfaceRemoval();
                        player.getInventory().delete(995, 100000000);
                        player.getPA().sendMessage("You have successfully purchased this title");
                        LoyaltyProgramme.unlock(player, LoyaltyTitles.SLAUGHTERER);
                    } else {
                        player.getPA().sendInterfaceRemoval();
                        player.getPA().sendMessage("@red@You don't have enough coins in your inventory");
                    }

                    break;
                case 133:
                    FreeForAll.startZerk(player);
                    break;

                case 0:
                    TeleportHandler.teleportPlayer(player,
                            new Position(3557 + (Misc.getRandom(2)), 9946 + Misc.getRandom(2)),
                            player.getSpellbook().getTeleportType());
                    break;
                case 1:
                    TeleportHandler.teleportPlayer(player, new Position(2933, 9849),
                            player.getSpellbook().getTeleportType());
                    break;
                case 2:
                    TeleportHandler.teleportPlayer(player, new Position(2802, 9148),
                            player.getSpellbook().getTeleportType());
                    break;
                case 10:
                    player.setDialogueActionId(59);
                    DialogueManager.start(player, 100);
                    break;
                case 11:
                    Scoreboards.open(player, Scoreboards.TOP_PKERS);
                    break;
                case 12:
                    TeleportHandler.teleportPlayer(player, new Position(2980 + Misc.getRandom(3), 3596 + Misc.getRandom(3)),
                            player.getSpellbook().getTeleportType());
                    break;
                case 13:
                    player.getPacketSender().sendInterfaceRemoval();
                    for (AchievementData d : AchievementData.values()) {
                        if (!player.getAchievementAttributes().getCompletion()[d.ordinal()]) {
                            player.getPacketSender()
                                    .sendMessage("You must have completed all achievements in order to buy this cape.");
                            return;
                        }
                    }
                    boolean usePouch = player.getMoneyInPouch() >= 100000000;
                    if (!usePouch && player.getInventory().getAmount(995) < 100000000) {
                        player.getPacketSender().sendMessage("You do not have enough coins.");
                        return;
                    }
                    if (usePouch) {
                        player.setMoneyInPouch(player.getMoneyInPouch() - 100000000);
                        player.getPacketSender().sendString(8135, "" + player.getMoneyInPouch());
                    } else
                        player.getInventory().delete(995, 100000000);
                    player.getInventory().add(14022, 1, "Comp cape");
                    World.sendMessage("<img=483> @red@[ Amazing News ]" + player.getUsername()
                            + " has obtained a completionist's cape. Huge congratulations!");

                    player.getPacketSender().sendMessage("You've purchased an Completionist cape.");
                    break;
                case 14:
                    TeleportHandler.teleportPlayer(player, new Position(3184 + Misc.getRandom(1), 5471 + Misc.getRandom(1)),
                            player.getSpellbook().getTeleportType());
                    break;
                case 15:
                    TeleportHandler.teleportPlayer(player, new Position(2663 + Misc.getRandom(1), 2651 + Misc.getRandom(1)),
                            player.getSpellbook().getTeleportType());
                    break;
                case 17:
                    if (player.getBankPinAttributes().hasBankPin()) {
                        DialogueManager.start(player, 12);
                        player.setDialogueActionId(8);
                    } else {
                        BankPin.init(player, false);
                    }
                    break;
                case 29:
                    SlayerMaster.changeSlayerMaster(player, SlayerMaster.DURADEL);
                    break;
                case 36:
                    TeleportHandler.teleportPlayer(player, new Position(1908, 4367),
                            player.getSpellbook().getTeleportType());
                    break;
                case 38:
                    DialogueManager.start(player, 70);
                    player.setDialogueActionId(39);
                    break;
                case 40:
                    TeleportHandler.teleportPlayer(player, new Position(2839, 9557),
                            player.getSpellbook().getTeleportType());
                    break;
                case 41:
                    TeleportHandler.teleportPlayer(player, new Position(2903, 5203),
                            player.getSpellbook().getTeleportType());
                    break;
                case 48:
                    JewelryTeleporting.teleport(player, new Position(3213, 3423));
                    break;
                case 59:
                    if (player.getClickDelay().elapsed(1500)) {
                        PkSets.buySet(player, PkSets.MELEE_MAIN_SET);
                    }
                    break;
                case 60:
                    player.setDialogueActionId(62);
                    DialogueManager.start(player, 102);
                    break;
                case 68:
                    player.getPacketSender().sendInterfaceRemoval();
                    if (player.getMinigameAttributes().getDungeoneeringAttributes().getParty() != null) {
                        if (player.getMinigameAttributes().getDungeoneeringAttributes().getParty().getOwner().getUsername()
                                .equals(player.getUsername())) {
                            player.getMinigameAttributes().getDungeoneeringAttributes().getParty().setComplexity(2);
                            player.getMinigameAttributes().getDungeoneeringAttributes().getParty()
                                    .sendMessage("The party leader has changed complexity.");
                            player.getMinigameAttributes().getDungeoneeringAttributes().getParty().refreshInterface();
                        }
                    }
                    break;
            }
        } else if (id == THIRD_OPTION_OF_FIVE) {

            if(player.getDialogue() != null) {
                if(player.getDialogue().action(3)) {
                    return;
                }
            }

            switch (player.getDialogueActionId()) {
                case 500:
                    Gambling.plantSeed(player, FlowersData.BLUE_FLOWERS);
                    break;
                case 501:
                    Gambling.plantSeed(player, FlowersData.RAINBOW_FLOWERS);
                    break;
                case 491:
                    if (player.getInventory().getAmount(995) > 49999999) {
                        player.getPA().sendInterfaceRemoval();
                        player.getInventory().delete(995, 50000000);
                        player.getPA().sendMessage("You have successfully purchased this achievement");
                        Achievements.finishAchievement(player, AchievementData.REACH_A_KILLSTREAK_OF_3);
                        Achievements.updateInterface(player);

                    } else {
                        player.getPA().sendInterfaceRemoval();
                        player.getPA().sendMessage("@red@You don't have enough coins in your inventory");
                    }

                    break;
                case 490:
                    if (player.getInventory().getAmount(995) > 199999999) {
                        player.getPA().sendInterfaceRemoval();
                        player.getInventory().delete(995, 200000000);
                        player.getPA().sendMessage("You have successfully purchased this title");
                        LoyaltyProgramme.unlock(player, LoyaltyTitles.IMMORTAL);
                    } else {
                        player.getPA().sendInterfaceRemoval();
                        player.getPA().sendMessage("@red@You don't have enough coins in your inventory");
                    }

                    break;
                case 133:
                    FreeForAll.startPure(player);
                    break;

                case 0:
                    TeleportHandler.teleportPlayer(player,
                            new Position(3204 + (Misc.getRandom(2)), 3263 + Misc.getRandom(2)),
                            player.getSpellbook().getTeleportType());
                    break;
                case 1:
                    TeleportHandler.teleportPlayer(player, new Position(2480, 5175),
                            player.getSpellbook().getTeleportType());
                    break;
                case 2:
                    TeleportHandler.teleportPlayer(player, new Position(2793, 2773),
                            player.getSpellbook().getTeleportType());
                    break;
                case 10:
                    DialogueManager.start(player, BloodMoneyDealer.getDialogue(player));
                    break;
                case 11:
                    Scoreboards.open(player, Scoreboards.TOP_TOTAL_EXP);
                    break;
                case 12:
                    TeleportHandler.teleportPlayer(player, new Position(3239 + Misc.getRandom(2), 3619 + Misc.getRandom(2)),
                            player.getSpellbook().getTeleportType());
                    break;
                case 13:
                    player.getPacketSender().sendInterfaceRemoval();
                    if (!player.getUnlockedLoyaltyTitles()[LoyaltyProgramme.LoyaltyTitles.VETERAN.ordinal()]) {
                        player.getPacketSender().sendMessage(
                                "You must have unlocked the 'Veteran' Loyalty Title in order to buy this cape.");
                        return;
                    }
                    boolean usePouch = player.getMoneyInPouch() >= 75000000;
                    if (!usePouch && player.getInventory().getAmount(995) < 75000000) {
                        player.getPacketSender().sendMessage("You do not have enough coins.");
                        return;
                    }
                    if (usePouch) {
                        player.setMoneyInPouch(player.getMoneyInPouch() - 75000000);
                        player.getPacketSender().sendString(8135, "" + player.getMoneyInPouch());
                    } else
                        player.getInventory().delete(995, 75000000);
                    player.getInventory().add(14021, 1, "Veteran cape");
                    player.getPacketSender().sendMessage("You've purchased a Veteran cape.");
                    DialogueManager.start(player, 122);
                    player.setDialogueActionId(76);
                    break;
                case 14:
                    TeleportHandler.teleportPlayer(player, new Position(2713, 9564),
                            player.getSpellbook().getTeleportType());
                    break;
                case 15:
                    TeleportHandler.teleportPlayer(player, new Position(3368 + Misc.getRandom(5), 3267 + Misc.getRandom(3)),
                            player.getSpellbook().getTeleportType());
                    player.getPacketSender().sendMessage("Don't Lose all of your items, Goodluck!");
                    break;
               /* case 17:
                    player.getPacketSender().sendInterfaceRemoval();
                    if (player.getSummoning().getFamiliar() != null) {
                        player.getPacketSender().sendMessage("Please dismiss your familiar first.");
                        return;
                    }
                    if (player.getGameMode() == GameMode.NORMAL) {
                        DialogueManager.start(player, 83);
                    } else {
                        player.setDialogueActionId(46);
                        DialogueManager.start(player, 84);
                    }
                    break;*/
                case 29:
                    SlayerMaster.changeSlayerMaster(player, SlayerMaster.KONAR_QUO_MATEN);
                    break;
                case 36:
                    player.setDialogueActionId(37);
                    DialogueManager.start(player, 70);
                    break;
                case 38:
                    TeleportHandler.teleportPlayer(player, new Position(2547, 9448),
                            player.getSpellbook().getTeleportType());
                    break;
                case 40:
                    TeleportHandler.teleportPlayer(player, new Position(2891, 4767),
                            player.getSpellbook().getTeleportType());
                    break;
                case 41:
                    TeleportHandler.teleportPlayer(player, new Position(3236, 3941),
                            player.getSpellbook().getTeleportType());
                    break;
                // scorp
                case 48:
                    JewelryTeleporting.teleport(player, new Position(3368, 3267));
                    break;
                case 59:
                    if (player.getClickDelay().elapsed(1500)) {
                        PkSets.buySet(player, PkSets.RANGE_MAIN_SET);
                    }
                    break;
                case 60:
                    player.setDialogueActionId(63);
                    DialogueManager.start(player, 102);
                    break;
                case 68:
                    player.getPacketSender().sendInterfaceRemoval();
                    if (player.getMinigameAttributes().getDungeoneeringAttributes().getParty() != null) {
                        if (player.getMinigameAttributes().getDungeoneeringAttributes().getParty().getOwner().getUsername()
                                .equals(player.getUsername())) {
                            player.getMinigameAttributes().getDungeoneeringAttributes().getParty().setComplexity(3);
                            player.getMinigameAttributes().getDungeoneeringAttributes().getParty()
                                    .sendMessage("The party leader has changed complexity.");
                            player.getMinigameAttributes().getDungeoneeringAttributes().getParty().refreshInterface();
                        }
                    }
                    break;
            }
        } else if (id == FOURTH_OPTION_OF_FIVE) {

            if(player.getDialogue() != null) {
                if(player.getDialogue().action(4)) {
                    return;
                }
            }

            switch (player.getDialogueActionId()) {
                case 500:
                    Gambling.plantSeed(player, FlowersData.YELLOW_FLOWERS);
                    break;
                case 10:
                    ShopManager.isShop(player, 23390, NpcClickType.SECOND_CLICK);
                    break;
                case 501:
                    player.setDialogueActionId(502);
                    DialogueManager.start(player, 502);
                    break;
                case 491:
                    if (player.getInventory().getAmount(995) > 99999999) {
                        player.getPA().sendInterfaceRemoval();
                        player.getInventory().delete(995, 100000000);
                        player.getPA().sendMessage("You have successfully purchased this achievement");
                        Achievements.finishAchievement(player, AchievementData.REACH_A_KILLSTREAK_OF_6);
                        Achievements.updateInterface(player);

                    } else {
                        player.getPA().sendInterfaceRemoval();
                        player.getPA().sendMessage("@red@You don't have enough coins in your inventory");
                    }

                    break;
                case 490:
                    if (player.getInventory().getAmount(995) > 299999999) {
                        player.getPA().sendInterfaceRemoval();
                        player.getInventory().delete(995, 300000000);
                        player.getPA().sendMessage("You have successfully purchased this title");
                        LoyaltyProgramme.unlock(player, LoyaltyTitles.GENOCIDAL);
                    } else {
                        player.getPA().sendInterfaceRemoval();
                        player.getPA().sendMessage("@red@You don't have enough coins in your inventory");
                    }

                    break;
                case 133:
                    FreeForAll.startF2p(player);
                    break;

                case 0:
                    TeleportHandler.teleportPlayer(player,
                            new Position(3173 - (Misc.getRandom(2)), 2981 + Misc.getRandom(2)),
                            player.getSpellbook().getTeleportType());
                    break;
                case 1:
                    TeleportHandler.teleportPlayer(player, new Position(3279, 2964),
                            player.getSpellbook().getTeleportType());
                    break;
                case 2:
                    TeleportHandler.teleportPlayer(player, new Position(3085, 9672),
                            player.getSpellbook().getTeleportType());
                    break;

                case 11:
                    Scoreboards.open(player, Scoreboards.TOP_ACHIEVER);
                    break;
                case 12:
                    TeleportHandler.teleportPlayer(player,
                            new Position(3329 + Misc.getRandom(2), 3660 + Misc.getRandom(2), 0),
                            player.getSpellbook().getTeleportType());
                    break;
                case 13:
                    player.getPacketSender().sendInterfaceRemoval();
                    if (!player.getUnlockedLoyaltyTitles()[LoyaltyProgramme.LoyaltyTitles.MAXED.ordinal()]) {
                        player.getPacketSender()
                                .sendMessage("You must have unlocked the 'Maxed' Loyalty Title in order to buy this cape.");
                        return;
                    }
                    boolean usePouch = player.getMoneyInPouch() >= 50000000;
                    if (!usePouch && player.getInventory().getAmount(995) < 50000000) {
                        player.getPacketSender().sendMessage("You do not have enough coins.");
                        return;
                    }
                    if (usePouch) {
                        player.setMoneyInPouch(player.getMoneyInPouch() - 50000000);
                        player.getPacketSender().sendString(8135, "" + player.getMoneyInPouch());
                    } else
                        player.getInventory().delete(995, 50000000);
                    player.getInventory().add(14019, 1, "Max cape");
                    World.sendMessage("<img=483> @red@[ Amazing News ]" + player.getUsername()
                            + " has obtained a max cape. Huge congratulations!");

                    player.getPacketSender().sendMessage("You've purchased a Max cape.");
                    break;
                case 14:
                    TeleportHandler.teleportPlayer(player, new Position(2884, 9797),
                            player.getSpellbook().getTeleportType());
                    break;
                case 15:
                    TeleportHandler.teleportPlayer(player, new Position(3565, 3313),
                            player.getSpellbook().getTeleportType());
                    break;
                case 17:
                    player.setInputHandling(new ChangePassword());
                    player.getPacketSender().sendEnterInputPrompt("Enter a new password:");
                    break;
                case 29:
                    SlayerMaster.changeSlayerMaster(player, SlayerMaster.KURADEL); //fourth
                    break;
                case 36:
                    TeleportHandler.teleportPlayer(player, new Position(2717, 9805),
                            player.getSpellbook().getTeleportType());
                    break;
                case 38:
                    TeleportHandler.teleportPlayer(player, new Position(1891, 3177),
                            player.getSpellbook().getTeleportType());
                    break;
                case 40:
                    TeleportHandler.teleportPlayer(player, new Position(3050, 9573),
                            player.getSpellbook().getTeleportType());
                    break;
                case 41:
                    TeleportHandler.teleportPlayer(player, new Position(3350, 3734),
                            player.getSpellbook().getTeleportType());
                    break;
                case 48:
                    JewelryTeleporting.teleport(player, new Position(2447, 5169));
                    break;
                case 59:
                    if (player.getClickDelay().elapsed(1500)) {
                        PkSets.buySet(player, PkSets.MAGIC_MAIN_SET);
                    }
                    break;
                case 60:
                    player.setDialogueActionId(64);
                    DialogueManager.start(player, 102);
                    break;
                case 68:
                    player.getPacketSender().sendInterfaceRemoval();
                    if (player.getMinigameAttributes().getDungeoneeringAttributes().getParty() != null) {
                        if (player.getMinigameAttributes().getDungeoneeringAttributes().getParty().getOwner().getUsername()
                                .equals(player.getUsername())) {
                            player.getMinigameAttributes().getDungeoneeringAttributes().getParty().setComplexity(4);
                            player.getMinigameAttributes().getDungeoneeringAttributes().getParty()
                                    .sendMessage("The party leader has changed complexity.");
                            player.getMinigameAttributes().getDungeoneeringAttributes().getParty().refreshInterface();
                        }
                    }
                    break;
            }
        } else if (id == FIFTH_OPTION_OF_FIVE) {

            if(player.getDialogue() != null) {
                if(player.getDialogue().action(5)) {
                    return;
                }
            }

            switch (player.getDialogueActionId()) {
                case 500:
                    player.setDialogueActionId(501);
                    DialogueManager.start(player, 501);
                    break;
                case 501:
                    player.setDialogueActionId(500);
                    DialogueManager.start(player, 500);
                    break;
                case 490:
                    player.getPA().sendInterfaceRemoval();
                    break;
                case 491:
                    if (player.getInventory().getAmount(995) > 149999999) {
                        player.getPA().sendInterfaceRemoval();
                        player.getInventory().delete(995, 150000000);
                        player.getPA().sendMessage("You have successfully purchased this achievement");
                        Achievements.finishAchievement(player, AchievementData.DEFEAT_30_PLAYERS);
                        Achievements.updateInterface(player);

                    } else {
                        player.getPA().sendInterfaceRemoval();
                        player.getPA().sendMessage("@red@You don't have enough coins in your inventory");
                    }

                    break;

                case 133:
                    player.setDialogueActionId(134);
                    DialogueManager.start(player, 288);
                    break;
                case 0:
                    player.setDialogueActionId(1);
                    DialogueManager.next(player);
                    break;
                case 1:
                    player.setDialogueActionId(2);
                    DialogueManager.next(player);
                    break;
                case 2:
                    player.setDialogueActionId(0);
                    DialogueManager.start(player, 0);
                    break;
                case 29:
                    player.setDialogueActionId(30);
                    DialogueManager.next(player);
                    break;
                case 9:
                case 10:
                case 11:
                case 13:
                case 17:
                case 48:
                case 60:
                case 67:
                case 68:
                    player.getPacketSender().sendInterfaceRemoval();
                    break;
                case 12:
                    int random = Misc.getRandom(4);
                    switch (random) {
                        case 0:
                            TeleportHandler.teleportPlayer(player, new Position(3035, 3701, 0),
                                    player.getSpellbook().getTeleportType());
                            break;
                        case 1:
                            TeleportHandler.teleportPlayer(player, new Position(3036, 3694, 0),
                                    player.getSpellbook().getTeleportType());
                            break;
                        case 2:
                            TeleportHandler.teleportPlayer(player, new Position(3045, 3697, 0),
                                    player.getSpellbook().getTeleportType());
                            break;
                        case 3:
                            TeleportHandler.teleportPlayer(player, new Position(3043, 3691, 0),
                                    player.getSpellbook().getTeleportType());
                            break;
                        case 4:
                            TeleportHandler.teleportPlayer(player, new Position(3037, 3687, 0),
                                    player.getSpellbook().getTeleportType());
                            break;
                    }
                    break;
                case 14:
                    DialogueManager.start(player, 23);
                    player.setDialogueActionId(14);
                    break;
                case 15:
                    DialogueManager.start(player, 32);
                    player.setDialogueActionId(18);
                    break;
                case 36:
                    DialogueManager.start(player, 66);
                    player.setDialogueActionId(38);
                    break;
                case 38:
                    DialogueManager.start(player, 68);
                    player.setDialogueActionId(40);
                    break;
                case 40:
                    DialogueManager.start(player, 69);
                    player.setDialogueActionId(41);
                    break;
                case 41:
                    DialogueManager.start(player, 65);
                    player.setDialogueActionId(36);
                    break;
                case 59:
                    if (player.getClickDelay().elapsed(1500)) {
                        PkSets.buySet(player, PkSets.HYBRIDING_MAIN_SET);
                    }
                    break;
            }
        } else if (id == FIRST_OPTION_OF_FOUR) {

            if(player.getDialogue() != null) {
                if(player.getDialogue().action(1)) {
                    return;
                }
            }

            switch (player.getDialogueActionId()) {
                case MagicCommand.DIALOGUE_ACTION:
                    MagicCommand.handleSwitch(player, MagicSpellbook.NORMAL);
                    break;
                case 134:
                    FreeForAll.startDDS(player);
                    break;
                case 5:
                    player.getPacketSender().sendInterfaceRemoval();
//				MySQLController.getStore().claim(player);
                    break;
                case 14:
                    TeleportHandler.teleportPlayer(player, new Position(2871, 5318, 2),
                            player.getSpellbook().getTeleportType());
                    break;
                case 18:
                    TeleportHandler.teleportPlayer(player, new Position(2439 + Misc.getRandom(2), 5171 + Misc.getRandom(2)),
                            player.getSpellbook().getTeleportType());
                    break;
                case 26:
                    TeleportHandler.teleportPlayer(player, new Position(2480, 3435),
                            player.getSpellbook().getTeleportType());
                    break;
                case 27:
                    ClanChatManager.createClan(player);
                    break;
                case 28:
                    player.setDialogueActionId(29);
                    DialogueManager.start(player, 62);
                    break;
                case 30:
                    if (player.getSlayer().getSlayerMaster().equals(SlayerMaster.KONAR_QUO_MATEN)) {
                        KonarQuoMaten.getAssignment(player);
                    } else {
                        player.getSlayer().assignTask();
                    }
                    break;
                case 31:
                    DialogueManager.start(player, SlayerDialogues.findAssignment(player));
                    break;
                case 41:
                    DialogueManager.start(player, 76);
                    break;
                case 45:
                    GameMode.set(player, GameMode.NORMAL, false);
                    break;
            }
        } else if (id == SECOND_OPTION_OF_FOUR) {

            if(player.getDialogue() != null) {
                if(player.getDialogue().action(2)) {
                    return;
                }
            }

            switch (player.getDialogueActionId()) {
                case MagicCommand.DIALOGUE_ACTION:
                    MagicCommand.handleSwitch(player, MagicSpellbook.ANCIENT);
                    break;
                case 502:
                    Gambling.plantSeed(player, FlowersData.BLACK_FLOWERS);
                    break;
                case 134:
                    FreeForAll.startRange(player);
                    break;
                case 5:
                    DialogueManager.start(player, MemberScrolls.getTotalFunds(player));
                    break;
                case 8:
                    LoyaltyProgramme.open(player);
                    break;
                case 9:
                    DialogueManager.start(player, 14);
                    break;
                case 14:
                    player.getPacketSender().sendInterfaceRemoval();
                    if (player.getSkillManager().getCurrentLevel(Skill.SLAYER) < 50) {
                        player.getPacketSender()
                                .sendMessage("You need a Slayer level of at least 50 to visit this dungeon.");
                        return;
                    }
                    TeleportHandler.teleportPlayer(player, new Position(2731, 5095),
                            player.getSpellbook().getTeleportType());
                    break;
                case 18:
                    TeleportHandler.teleportPlayer(player, new Position(2399, 5177),
                            player.getSpellbook().getTeleportType());
                    break;
                case 26:
                    player.getPacketSender().sendInterfaceRemoval();
                    if (player.getSkillManager().getMaxLevel(Skill.AGILITY) < 35) {
                        player.getPacketSender()
                                .sendMessage("You need an Agility level of at least level 35 to use this course.");
                        return;
                    }
                    TeleportHandler.teleportPlayer(player, new Position(2552, 3556),
                            player.getSpellbook().getTeleportType());
                    break;
                case 27:
                    ClanChatManager.clanChatSetupInterface(player, true);
                    break;
                case 28:
                    if (player.getSlayer().getSlayerMaster().getPosition() != null) {
                        TeleportHandler.teleportPlayer(player,
                                new Position(player.getSlayer().getSlayerMaster().getPosition().getX(),
                                        player.getSlayer().getSlayerMaster().getPosition().getY(),
                                        player.getSlayer().getSlayerMaster().getPosition().getZ()),
                                player.getSpellbook().getTeleportType());
                        if (player.getSkillManager().getCurrentLevel(Skill.SLAYER) <= 50)
                            player.getPacketSender().sendMessage("")
                                    .sendMessage("You can train Slayer with a friend by using a Slayer gem on them.")
                                    .sendMessage("Slayer gems can be bought from all Slayer masters.");
                        ;
                    }
                    break;
                case 31:
                    DialogueManager.start(player, SlayerDialogues.resetTaskDialogue(player));
                    break;
                case 41:
                    World.getWell().view(player);
                    break;
                case 45:
                    GameMode.set(player, GameMode.IRONMAN, false);
                    break;
            }
        } else if (id == THIRD_OPTION_OF_FOUR) {

            if(player.getDialogue() != null) {
                if(player.getDialogue().action(3)) {
                    return;
                }
            }

            switch (player.getDialogueActionId()) {
                case MagicCommand.DIALOGUE_ACTION:
                    MagicCommand.handleSwitch(player, MagicSpellbook.LUNAR);
                    break;
                case 502:
                    player.setDialogueActionId(501);
                    DialogueManager.start(player, 501);
                    break;

                case 134:
                    FreeForAll.startMonk(player);
                    break;
                case 5:
                    player.getPacketSender().sendInterfaceRemoval();
                    if (player.getRights() == PlayerRights.PLAYER) {
                        player.getPacketSender().sendMessage("You need to be a member to teleport to this zone.")
                                .sendMessage("To become a member, visit Varrock.io and purchase a scroll.");
                        return;
                    }
                    TeleportHandler.teleportPlayer(player, new Position(3424, 2919),
                            player.getSpellbook().getTeleportType());
                    break;
                case 8:
                    player.setTitle("");
                    player.getUpdateFlag().flag(Flag.APPEARANCE);
                    player.getPacketSender().sendInterfaceRemoval();
                    break;
                case 14:
                    TeleportHandler.teleportPlayer(player, new Position(1745, 5325),
                            player.getSpellbook().getTeleportType());
                    break;
                case 18:
                    TeleportHandler.teleportPlayer(player, new Position(3503, 3562),
                            player.getSpellbook().getTeleportType());
                    break;
                case 26:
                    player.getPacketSender().sendInterfaceRemoval();
                    if (player.getSkillManager().getMaxLevel(Skill.AGILITY) < 55) {
                        player.getPacketSender()
                                .sendMessage("You need an Agility level of at least level 55 to use this course.");
                        return;
                    }
                    TeleportHandler.teleportPlayer(player, new Position(2998, 3914),
                            player.getSpellbook().getTeleportType());
                    break;
                case 27:
                    ClanChatManager.deleteClan(player);
                    break;
                case 28:
                    TeleportHandler.teleportPlayer(player, new Position(3427, 3537, 0),
                            player.getSpellbook().getTeleportType());
                    break;
                case 31:
                    DialogueManager.start(player, SlayerDialogues.totalPointsReceived(player));
                    break;
                case 41:
                    player.setInputHandling(new DonateToWell());
                    player.getPacketSender().sendInterfaceRemoval()
                            .sendEnterAmountPrompt("How much money would you like to contribute with?");
                    break;
                case 45:
                    GameMode.set(player, GameMode.ULTIMATE_IRONMAN, false);
                    break;
            }
        } else if (id == FOURTH_OPTION_OF_FOUR) {

            if(player.getDialogue() != null) {
                if(player.getDialogue().action(4)) {
                    return;
                }
            }

            switch (player.getDialogueActionId()) {

                case 134:
                    FreeForAll.startTorva(player);
                    break;
                case MagicCommand.DIALOGUE_ACTION:
                case 101:
                case 5:
                case 8:
                case 9:
                case 26:
                case 27:
                case 28:
                case 41:
                    player.getPacketSender().sendInterfaceRemoval();
                    break;
                case 14:
                    player.setDialogueActionId(14);
                    DialogueManager.start(player, 22);
                    break;
                case 18:
                    DialogueManager.start(player, 25);
                    player.setDialogueActionId(15);
                    break;
                case 30:
                case 31:
                    player.getPacketSender().sendInterfaceRemoval();
                    if (player.getSlayer().getDuoPartner() != null) {
                        Slayer.resetDuo(player, World.getPlayerByName(player.getSlayer().getDuoPartner()));
                    }
                    break;
                case 45:
                    player.getPacketSender().sendString(1, "http://varrock.io/ironman");
                    break;
            }
        } else if (id == FIRST_OPTION_OF_TWO) {

            if(player.getDialogue() != null) {
                if(player.getDialogue().action(1)) {
                    return;
                }
            }

            switch (player.getDialogueActionId()) {
                case 705:
                    player.setTutorialStage(TutorialStages.SHOPS);
                    TutorialStages.SHOPS.sendDialogueText(player);
                    break;
                case 78:
                    player.getPA().sendInterfaceRemoval();

                    if(player.getEquipment().getFreeSlots() != player.getEquipment().capacity()) {
                        player.getPacketSender().sendMessage("Please unequip all your items first.");
                        return;
                    }
                    if(player.getLocation() == Location.WILDERNESS || player.getCombatBuilder().isBeingAttacked()) {
                        player.getPacketSender().sendMessage("You cannot do this at the moment");
                        return;
                    }

                    if(player.prestige >= 10) {
                        player.sendMessage("<col=A77510>You have achieved the max prestige available, amazing!");
                        return;
                    }

                    for(Skill skill : Skill.values()) {
                        if(skill == Skill.CONSTRUCTION)
                            continue;

                        if(skill == Skill.DUNGEONEERING && player.getSkillManager().getMaxLevel(skill) < 99) {
                            player.getPacketSender().sendMessage("<col=A77510>You have not achieved 99 in "+skill.getName()+" yet.");
                            return;
                        }
                        if(player.getSkillManager().getMaxLevel(skill) < SkillManager.getMaxAchievingLevel(skill) && skill != Skill.DUNGEONEERING) {
                            player.getPacketSender().sendMessage("<col=A77510>You have not achieved 99 in "+skill.getName()+" yet.");
                            return;
                        }
                    }

                    for(Skill skill : Skill.values()) {
                        player.getSkillManager().resetSkill(skill, true);
                    }

                    player.achievedMax = -1;
                    player.prestige += 1;

                    player.sendMessage("<col=A77510>Congratulations you have achieved prestige "+player.prestige+".");

                    player.getPointsHandler().setPrestigePoints((player.prestige * 100) + Misc.random(50), true);

                    World.sendMessage("<img=731><shad=FF8C38>[News] " + player.getName() + " has just prestiged for the "+(Misc.ordinal(player.prestige))+" time!");

                    player.getPacketSender().sendPrestige(player.prestige);
                    player.getUpdateFlag().flag(Flag.APPEARANCE);

                    break;
                case TeleportHandler.WILDERNESS_WARNING:
                    if (player.targetLocation != null) {
                        TeleportScrolls scroll = null;
                        for (TeleportScrolls teleportScroll: TeleportScrolls.values()) {
                            if (teleportScroll.getPosition().sameAs(player.targetLocation)) {
                                scroll = teleportScroll;
                            }
                        }
                        if (scroll != null) {
                            player.getInventory().delete(scroll.getId(), 1, true);
                        } else if (player.slayerRing > 1) {
                            Slayer.deleteRing(player, player.slayerRing, false);
                            player.slayerRing = -1;
                        }
                            TeleportHandler.completeTeleport(player, player.getSpellbook().getTeleportType(), player.targetLocation);
                        }
                    break;
                case 2508:

                    break;
                case PartyRoomManager.DIALOGUE_ACTION_ID:
                    player.getPacketSender().sendInterfaceRemoval();
                    PartyRoomManager.sendBalloons(player);
                    break;
                case PartyRoomManager.DIALOGUE_ACTION_ID + 1:
                    PartyRoomManager.open(player);
                    PartyRoomManager.accept(player);
                    break;
                case 504:
                    int ITEMS[][] = {{451, 452}, {41934, 41935}, {440, 441}, {453, 454}, {444, 445},
                            {447, 448}, {449, 450}, {1515, 1516}, {1513, 1514}};

                    boolean note = false;

                    for (int i = 0; i < ITEMS.length; i++) {

                        if (player.getInventory().contains(new Item(ITEMS[i][0]))) {
                            int amount = player.getInventory().getAmount(ITEMS[i][0]);
                            int payment = player.getInventory().getAmount(ITEMS[i][0]) * 50;

                            if (!player.getInventory().contains(new Item(995, payment))) {
                                DialogueManager.sendStatement(player,
                                        payment + " coins is required to do this; which you do not have!");
                                break;
                            }

                            player.getInventory().delete(new Item(ITEMS[i][0], amount));
                            player.getInventory().add(new Item(ITEMS[i][1], amount));

                            DialogueManager.sendStatement(player, "You have noted: @blu@" + amount
                                    + " @bla@items. You have paid: @blu@" + payment + " @bla@coins");

                            note = true;
                        }

                        if (!note) {
                            DialogueManager.sendStatement(player,
                                    "You do not have any items that are allowed to be noted!");
                        }
                    }
                    break;
                case 67:
                case 753:
                    player.getPacketSender().sendInterfaceRemoval();
                    if (player.getLocation() == Location.RAIDS
                            && player.getMinigameAttributes().getRaidsAttributes().getParty() == null) {
                        if (player.getMinigameAttributes().getRaidsAttributes().getPartyInvitation() != null) {
                            player.getMinigameAttributes().getRaidsAttributes().getPartyInvitation().add(player);
                        }
                        player.getMinigameAttributes().getRaidsAttributes().setPartyInvitation(null);
                    }
                    break;
                case 489:
                    player.setDialogueActionId(490);
                    DialogueManager.start(player, 498);
                    break;
                case 391:
                    MemberScrolls.handleScrollClaim(player);
                    break;
                case 478:
                    TeleportHandler.teleportZulrah(player);
                    break;
                case 479:
                    player.getPA().sendInterfaceRemoval();
                    InstanceManager.get().enterInferno(player);
                    break;
                case 111:
                    player.getPacketSender().sendInterfaceRemoval();
                    player.setInputHandling(new PaidYell());
                    player.getPacketSender().sendEnterInputPrompt("Enter your yell message");
                    break;
                case 202:
                    CorporealBeast.startMulti(player);
                    break;

                case 4:
                    SummoningTab.handleDismiss(player, true);
                    break;
                case 709:
                    player.getPacketSender().sendInterfaceRemoval();
                    Decanting.checkRequirements(player);
                    break;
                case 7:
                    BankPin.init(player, false);
                    break;
                case 8:
                    BankPin.deletePin(player);
                    break;
                case 16:
                    player.getPacketSender().sendInterfaceRemoval();
                    if (player.getMinigameAttributes().getBarrowsMinigameAttributes().getKillcount() < 5) {
                        player.getPacketSender()
                                .sendMessage("You must have a killcount of at least 5 to enter the tunnel.");
                        return;
                    }
                    player.moveTo(new Position(3552, 9692));
                    break;
                case 20:
                    player.getPacketSender().sendInterfaceRemoval();
                    DialogueManager.start(player, 39);
                    player.getMinigameAttributes().getRecipeForDisasterAttributes().setPartFinished(0, true);
                    PlayerPanel.refreshPanel(player);
                    break;
                case 23:
                    DialogueManager.start(player, 50);
                    player.getMinigameAttributes().getNomadAttributes().setPartFinished(0, true);
                    player.setDialogueActionId(24);
                    PlayerPanel.refreshPanel(player);
                    break;
                case 24:
                    player.getPacketSender().sendInterfaceRemoval();
                    break;
                case 33:
                    player.getPacketSender().sendInterfaceRemoval();
                    player.getSlayer().resetSlayerTask();
                    break;
                case 34:
                    player.getPacketSender().sendInterfaceRemoval();
                    player.getSlayer().handleInvitation(true);
                    break;
                case 37:
                    TeleportHandler.teleportPlayer(player, new Position(2961, 3882),
                            player.getSpellbook().getTeleportType());
                    break;
                case 39:
                    TeleportHandler.teleportPlayer(player, new Position(3281, 3914),
                            player.getSpellbook().getTeleportType());
                    break;

                case 42:
                    player.getPacketSender().sendInterfaceRemoval();
                    if (player.getInteractingObject() != null && player.getInteractingObject().getDefinition() != null
                            && player.getInteractingObject().getDefinition().getName().equalsIgnoreCase("flowers")) {
                        if (CustomObjects.objectExists(player.getInteractingObject().getPosition())) {
                            player.getInventory().add(FlowersData.forObject(player.getInteractingObject().getId()).itemId,
                                    1, "Pickup flowers");
                            CustomObjects.deleteGlobalObject(player.getInteractingObject());
                            player.setInteractingObject(null);
                        }
                    }
                    break;
                case 46:
                    player.getPacketSender().sendInterfaceRemoval();
                    DialogueManager.start(player, 82);
                    player.setPlayerLocked(true).setDialogueActionId(45);
                    break;
                case 57:
                    Graveyard.start(player);
                    break;
                case 66:
                    player.getPacketSender().sendInterfaceRemoval();
                    if (player.getLocation() == Location.DUNGEONEERING
                            && player.getMinigameAttributes().getDungeoneeringAttributes().getParty() == null) {
                        if (player.getMinigameAttributes().getDungeoneeringAttributes().getPartyInvitation() != null) {
                            player.getMinigameAttributes().getDungeoneeringAttributes().getPartyInvitation().add(player);
                        }
                    }
                    player.getMinigameAttributes().getDungeoneeringAttributes().setPartyInvitation(null);
                    break;
                case 71:
                    if (player.getClickDelay().elapsed(1000)) {
                        if (Dungeoneering.doingDungeoneering(player)) {
                            Dungeoneering.leave(player, false, true);
                            player.getClickDelay().reset();
                        }
                    }
                    break;
                case 72:
                    if (player.getClickDelay().elapsed(1000)) {
                        if (Dungeoneering.doingDungeoneering(player)) {
                            Dungeoneering.leave(player, false, player.getMinigameAttributes().getDungeoneeringAttributes()
                                    .getParty().getOwner() == player ? false : true);
                            player.getClickDelay().reset();
                        }
                    }
                    break;
                case 73:
                    player.getPacketSender().sendInterfaceRemoval();
                    player.moveTo(new Position(3653, player.getPosition().getY()));
                    break;
                case 74:
                    player.getPacketSender().sendMessage("The ghost teleports you away.");
                    player.getPacketSender().sendInterfaceRemoval();
                    player.moveTo(new Position(3651, 3486));
                    break;
                case 76:
                    player.getPacketSender().sendInterfaceRemoval();
                    player.sendMessage("This feature has been disabled.");
                    break;
            }
        } else if (id == SECOND_OPTION_OF_TWO) {

            if(player.getDialogue() != null) {
                if(player.getDialogue().action(2)) {
                    return;
                }
            }

            switch (player.getDialogueActionId()) {
                case 705:
                    player.setTutorialStage(TutorialStages.COMPLETED);
                    TutorialStages.COMPLETED.sendDialogueText(player);
                    break;
                case 360:
                    player.getPacketSender().sendInterfaceRemoval();
                    break;
                case PartyRoomManager.DIALOGUE_ACTION_ID:
                    player.getPacketSender().sendInterfaceRemoval();
                    PartyRoomManager.sendWhiteKnights(player);
                    break;
                case 504:
                case PartyRoomManager.DIALOGUE_ACTION_ID + 1:
                    player.getPacketSender().sendInterfaceRemoval();
                    break;
                case 754:
                    player.getPacketSender().sendInterfaceRemoval();
                    break;
                case 67:
                    player.getPacketSender().sendInterfaceRemoval();
                    if (player.getMinigameAttributes().getRaidsAttributes().getPartyInvitation() != null
                            && player.getMinigameAttributes().getRaidsAttributes().getPartyInvitation().getOwner() != null)
                        player.getMinigameAttributes().getRaidsAttributes().getPartyInvitation().getOwner()
                                .getPacketSender()
                                .sendMessage("" + player.getUsername() + " has declined your invitation.");
                    player.getMinigameAttributes().getRaidsAttributes().setPartyInvitation(null);
                    break;
                case 489:
                    player.setDialogueActionId(491);
                    DialogueManager.start(player, 499);
                    break;
                case 478:
                    player.getPA().sendInterfaceRemoval();
                    break;
                case 391:
                    player.getPacketSender().sendInterfaceRemoval();
                    break;
                case 111:
                    player.getPacketSender().sendInterfaceRemoval();
                    break;
                case 4:
                case 16:
                case 420:
                case 20:
                case 23:
                case 33:
                case 37:
                case 39:
                case 42:
                case 44:
                case 46:
                case 57:
                case 71:
                case 72:
                case 73:
                case 74:
                case 76:
                case 78:
                case 709:
                    player.getPacketSender().sendInterfaceRemoval();
                    break;
                case 7:
                case 8:
                    player.getPacketSender().sendInterfaceRemoval();
                    player.setTempBankTabs(null);
                    player.getBank(player.getCurrentBankTab()).open();
                    break;
                case 24:
                    Nomad.startFight(player);
                    break;
                case 34:
                    player.getPacketSender().sendInterfaceRemoval();
                    player.getSlayer().handleInvitation(false);
                    break;
                case 66:
                    player.getPacketSender().sendInterfaceRemoval();
                    if (player.getMinigameAttributes().getDungeoneeringAttributes().getPartyInvitation() != null && player
                            .getMinigameAttributes().getDungeoneeringAttributes().getPartyInvitation().getOwner() != null)
                        player.getMinigameAttributes().getDungeoneeringAttributes().getPartyInvitation().getOwner()
                                .getPacketSender()
                                .sendMessage("" + player.getUsername() + " has declined your invitation.");
                    player.getMinigameAttributes().getDungeoneeringAttributes().setPartyInvitation(null);
                    break;
            }
        } else if (id == FIRST_OPTION_OF_THREE) {

            if(player.getDialogue() != null) {
                if(player.getDialogue().action(1)) {
                    return;
                }
            }

            switch (player.getDialogueActionId()) {
                case 30:
                    SlayerMaster.changeSlayerMaster(player, SlayerMaster.KRYSTILIA);
                    break;
                case 9:
                    TeleportHandler.teleportPlayer(player, new Position(2440, 3090),
                            player.getSpellbook().getTeleportType());
                    break;
                case PrayerCommand.DIALOGUE_ACTION:
                        PrayerCommand.handleSwitch(player, Prayerbook.NORMAL);
                    break;
                case CorporealBeastPlayerInstance.DIALOGUE_ACTION:
                    TeleportHandler.teleportPlayer(player, new Position(2886, 4376, 0),
                            player.getSpellbook().getTeleportType());
                    break;
                case 44:
                    player.getPacketSender().sendInterfaceRemoval();
                    player.getMinigameAttributes().getGodwarsDungeonAttributes().setHasEnteredRoom(true);
                    player.getPacketSender().sendMessage("You enter Nex's lair..");
                    player.moveTo(new Position(2911, 5203, 0));
                    NpcAggression.target(player);
                    break;
                case 502:
                    Gambling.plantSeed(player, FlowersData.WHITE_FLOWERS);
                    break;

                case 15:
                    DialogueManager.start(player, 35);
                    player.setDialogueActionId(19);
                    break;
                case 19:
                    DialogueManager.start(player, 33);
                    player.setDialogueActionId(21);
                    break;
                case 21:
                    TeleportHandler.teleportPlayer(player, new Position(3080, 3498),
                            player.getSpellbook().getTeleportType());
                    break;
                case 22:
                    TeleportHandler.teleportPlayer(player, new Position(1891, 3177),
                            player.getSpellbook().getTeleportType());
                    break;
                case 25:
                    TeleportHandler.teleportPlayer(player, new Position(2589, 4319), TeleportType.PURO_PURO);
                    break;
                case 35:
                    player.getPacketSender()
                            .sendEnterAmountPrompt("How many shards would you like to buy? (You can use K, M, B prefixes)");
                    player.setInputHandling(new BuyShards());
                    break;
                case 41:
                    TeleportHandler.teleportPlayer(player, new Position(2884 + Misc.getRandom(1), 4374 + Misc.getRandom(1)),
                            player.getSpellbook().getTeleportType());
                    break;
                case 43:
                    TeleportHandler.teleportPlayer(player, new Position(2773, 9195),
                            player.getSpellbook().getTeleportType());
                    break;
                case 47:
                    TeleportHandler.teleportPlayer(player, new Position(2911, 4832),
                            player.getSpellbook().getTeleportType());
                    break;
                case 48:
                    if (player.getInteractingObject() != null) {
                        Mining.startMining(player, new GameObject(24444, player.getInteractingObject().getPosition()));
                    }
                    player.getPacketSender().sendInterfaceRemoval();
                    break;
                case 56:
                    TeleportHandler.teleportPlayer(player, new Position(2717, 3499),
                            player.getSpellbook().getTeleportType());
                    break;
                case 58:
                    DialogueManager.start(player, AgilityTicketExchange.getDialogue(player));
                    break;
                case 61:
                    CharmingImp.changeConfig(player, 0, 0);
                    break;
                case 62:
                    CharmingImp.changeConfig(player, 1, 0);
                    break;
                case 63:
                    CharmingImp.changeConfig(player, 2, 0);
                    break;
                case 64:
                    CharmingImp.changeConfig(player, 3, 0);
                    break;
                case 65:
                    player.getPacketSender().sendInterfaceRemoval();
                    if (player.getSlayer().getDuoPartner() != null) {
                        player.getPacketSender().sendMessage("You already have a duo partner.");
                        return;
                    }
                    player.getPacketSender()
                            .sendMessage("<img=10> To do Social slayer, simply use your Slayer gem on another player.");
                    break;
                case 69:
                    ShopManager.openShop(player, 31);
                    break;
            }
        } else if (id == SECOND_OPTION_OF_THREE) {

            if(player.getDialogue() != null) {
                if(player.getDialogue().action(2)) {
                    return;
                }
            }

            switch (player.getDialogueActionId()) {
                case 30:
                    SlayerMaster.changeSlayerMaster(player, SlayerMaster.SUMONA);
                    break;
                case PrayerCommand.DIALOGUE_ACTION:
                    PrayerCommand.handleSwitch(player, Prayerbook.CURSES);
                    break;
                case CorporealBeastPlayerInstance.DIALOGUE_ACTION:
                    PlayerInstanceManager.startInstance(player, new CorporealBeastPlayerInstance(player), 0);
                    break;
                case 58:
                    ShopManager.openShop(player, 47);
                    break;
                case 44:
                    player.getPacketSender().sendInterfaceRemoval();
                    player.getMinigameAttributes().getGodwarsDungeonAttributes().setHasEnteredRoom(true);
                    player.getPacketSender().sendMessage("You enter Nex's lair on your own..");
                    PlayerInstanceManager.startInstance(player, new NexPlayerInstance(player), 0);
                    NpcAggression.target(player);
                    break;
                case 9:
                    ShopManager.openShop(player, 48);
                    break;
                case 502:
                    Gambling.plantSeed(player, FlowersData.BLACK_FLOWERS);
                    break;

                case 15:
                    DialogueManager.start(player, 25);
                    player.setDialogueActionId(15);
                    break;
                case 21:
                    RecipeForDisaster.openQuestLog(player);
                    break;
                case 19:
                    DialogueManager.start(player, 33);
                    player.setDialogueActionId(22);
                    break;
                case 22:
                    Nomad.openQuestLog(player);
                    break;
                case 25:
                    player.getPacketSender().sendInterfaceRemoval();
                    if (player.getSkillManager().getCurrentLevel(Skill.HUNTER) < 23) {
                        player.getPacketSender()
                                .sendMessage("You need a Hunter level of at least 23 to visit the hunting area.");
                        return;
                    }
                    TeleportHandler.teleportPlayer(player, new Position(2922, 2885),
                            player.getSpellbook().getTeleportType());
                    break;
                case 41:
                    TeleportHandler.teleportPlayer(player, new Position(2903, 5204),
                            player.getSpellbook().getTeleportType());
                    break;
                case 43:
                    TeleportHandler.teleportPlayer(player, new Position(2577, 9881),
                            player.getSpellbook().getTeleportType());
                    break;
                case 47:
                    TeleportHandler.teleportPlayer(player, new Position(3023, 9740),
                            player.getSpellbook().getTeleportType());
                    break;
                case 48:
                    if (player.getInteractingObject() != null) {
                        Mining.startMining(player, new GameObject(24445, player.getInteractingObject().getPosition()));
                    }
                    player.getPacketSender().sendInterfaceRemoval();
                    break;
                case 56:
                    player.getPacketSender().sendInterfaceRemoval();
                    if (player.getSkillManager().getCurrentLevel(Skill.WOODCUTTING) < 60) {
                        player.getPacketSender()
                                .sendMessage("You need a Woodcutting level of at least 60 to teleport there.");
                        return;
                    }
                    TeleportHandler.teleportPlayer(player, new Position(2711, 3463),
                            player.getSpellbook().getTeleportType());
                    break;
                case 61:
                    CharmingImp.changeConfig(player, 0, 1);
                    break;
                case 62:
                    CharmingImp.changeConfig(player, 1, 1);
                    break;
                case 63:
                    CharmingImp.changeConfig(player, 2, 1);
                    break;
                case 64:
                    CharmingImp.changeConfig(player, 3, 1);
                    break;
                case 65:
                    player.getPacketSender().sendInterfaceRemoval();
                    if (player.getSlayer().getDuoPartner() != null) {
                        Slayer.resetDuo(player, World.getPlayerByName(player.getSlayer().getDuoPartner()));
                    }
                    break;
                case 69:
                    if (player.getClickDelay().elapsed(1000)) {
                        Dungeoneering.start(player);
                    }
                    break;
                case 70:
                case 71:
                    final boolean all = player.getDialogueActionId() == 71;
                    player.getPacketSender().sendInterfaceRemoval();
                    if (player.getInventory().getFreeSlots() == 0) {
                        player.getPacketSender().sendMessage("You do not have enough free inventory space to do that.");
                        return;
                    }
                    if (player.getInventory().contains(19670) && player.getClickDelay().elapsed(700)) {
                        int amt = !all ? 1 : player.getInventory().getAmount(19670);
                        player.getInventory().delete(19670, amt);
                        if (World.getWell().isActive(WellBenefit.VOTE_POINTS)) {
                            player.getPointsHandler().incrementVotingPoints(amt * 2);
                        } else {
                            player.getPointsHandler().incrementVotingPoints(amt);
                        }

                        if (player.getGameMode() == GameMode.NORMAL) {
                            player.getInventory().add(995, 1_000_000 * amt, "Vote scroll normal");
                        } else {
                            player.getInventory().add(995, 150_000 * amt, "Vote scroll ironman");
                        }
                        player.getPacketSender().sendMessage(
                                "You claim the " + (amt > 1 ? "scrolls" : "scroll") + " and receive your reward.");
                        player.getClickDelay().reset();
                    }
                    break;
            }
        } else if (id == THIRD_OPTION_OF_THREE) {

            if(player.getDialogue() != null) {
                if(player.getDialogue().action(3)) {
                    return;
                }
            }

            switch (player.getDialogueActionId()) {
                case 30:
                    player.getPacketSender().sendInterfaceRemoval();
                    break;
                case 502:
                    player.setDialogueActionId(501);
                    DialogueManager.start(player, 501);
                    break;
                case 421:
                case 420:
                case 5:
                case 10:
                case 15:
                case 19:
                case 21:
                case 22:
                case 25:
                case 35:
                case 47:
                case 48:
                case 56:
                case 58:
                case 61:
                case 62:
                case 63:
                case 64:
                case 65:
                case 69:
                case 70:
                case 71:
                case 77:
                case 9:
                case CorporealBeastPlayerInstance.DIALOGUE_ACTION:
                case PrayerCommand.DIALOGUE_ACTION:
                    player.getPacketSender().sendInterfaceRemoval();
                    break;
                case 43:
                    DialogueManager.start(player, 65);
                    player.setDialogueActionId(36);
                    break;
                case 41:
                    player.setDialogueActionId(36);
                    DialogueManager.start(player, 65);
                    break;
            }
        }
    }

    public static int FIRST_OPTION_OF_FIVE = 2494;
    public static int SECOND_OPTION_OF_FIVE = 2495;
    public static int THIRD_OPTION_OF_FIVE = 2496;
    public static int FOURTH_OPTION_OF_FIVE = 2497;
    public static int FIFTH_OPTION_OF_FIVE = 2498;

    public static int FIRST_OPTION_OF_FOUR = 2482;
    public static int SECOND_OPTION_OF_FOUR = 2483;
    public static int THIRD_OPTION_OF_FOUR = 2484;
    public static int FOURTH_OPTION_OF_FOUR = 2485;

    public static int FIRST_OPTION_OF_THREE = 2471;
    public static int SECOND_OPTION_OF_THREE = 2472;
    public static int THIRD_OPTION_OF_THREE = 2473;

    public static int FIRST_OPTION_OF_TWO = 2461;
    public static int SECOND_OPTION_OF_TWO = 2462;

}
