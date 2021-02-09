package com.zyrox.net.packet.impl;

import com.zyrox.engine.task.Task;
import com.zyrox.engine.task.TaskManager;
import com.zyrox.model.*;
import com.zyrox.model.Locations.Location;
import com.zyrox.model.definitions.ItemDefinition;
import com.zyrox.model.item.CrawsBow;
import com.zyrox.model.item.RingOfBosses;
import com.zyrox.net.packet.Packet;
import com.zyrox.net.packet.PacketListener;
import com.zyrox.util.Misc;
import com.zyrox.util.RandomUtility;
import com.zyrox.world.content.*;
import com.zyrox.world.content.combat.range.DwarfMultiCannon;
import com.zyrox.world.content.dialogue.Dialogue;
import com.zyrox.world.content.dialogue.DialogueExpression;
import com.zyrox.world.content.dialogue.DialogueManager;
import com.zyrox.world.content.dialogue.DialogueType;
import com.zyrox.world.content.goodie_bag.GoodieBagManager;
import com.zyrox.world.content.lootboxes.LootBox;
import com.zyrox.world.content.minigames.impl.castlewars.item.CastleWarsBandage;
import com.zyrox.world.content.minigames.impl.castlewars.item.CastleWarsBarricade;
import com.zyrox.world.content.skill.impl.construction.Construction;
import com.zyrox.world.content.skill.impl.dungeoneering.ItemBinding;
import com.zyrox.world.content.skill.impl.herblore.Herblore;
import com.zyrox.world.content.skill.impl.herblore.IngridientsBook;
import com.zyrox.world.content.skill.impl.hunter.*;
import com.zyrox.world.content.skill.impl.hunter.Trap.TrapState;
import com.zyrox.world.content.skill.impl.prayer.Prayer;
import com.zyrox.world.content.skill.impl.runecrafting.Runecrafting;
import com.zyrox.world.content.skill.impl.runecrafting.RunecraftingData;
import com.zyrox.world.content.skill.impl.runecrafting.RunecraftingPouches;
import com.zyrox.world.content.skill.impl.runecrafting.RunecraftingPouches.RunecraftingPouch;
import com.zyrox.world.content.skill.impl.slayer.SlayerDialogues;
import com.zyrox.world.content.skill.impl.slayer.SlayerTasks;
import com.zyrox.world.content.skill.impl.slayer.KonarQuoMaten.SlayerTask;
import com.zyrox.world.content.skill.impl.summoning.CharmingImp;
import com.zyrox.world.content.skill.impl.summoning.SummoningData;
import com.zyrox.world.content.skill.impl.woodcutting.BirdNests;
import com.zyrox.world.content.transportation.JewelryTeleporting;
import com.zyrox.world.content.transportation.TeleportHandler;
import com.zyrox.world.content.transportation.TeleportScrolls;
import com.zyrox.world.content.transportation.TeleportType;
import com.zyrox.world.content.treasuretrails.EliteClueScroll;
import com.zyrox.world.entity.impl.player.Player;

public class ItemActionPacketListener implements PacketListener {

    public static void cancelCurrentActions(Player player) {
        player.getPacketSender().sendInterfaceRemoval();
        player.setTeleporting(false);
        player.setWalkToTask(null);
        player.setInputHandling(null);
        player.getSkillManager().stopSkilling();
        player.setEntityInteraction(null);
        player.getMovementQueue().setFollowCharacter(null);
        player.getCombatBuilder().cooldown(false);
        player.setResting(false);
    }

    public static boolean checkReqs(Player player, Location targetLocation) {
        if (player.getConstitution() <= 0)
            return false;
        if (player.getTeleblockTimer() > 0) {
            player.getPacketSender().sendMessage("A magical spell is blocking you from teleporting.");
            return false;
        }
        if (player.getLocation() != null && !player.getLocation().canTeleport(player))
            return false;
        if (player.isPlayerLocked() || player.isCrossingObstacle()) {
            player.getPacketSender().sendMessage("You cannot teleport right now.");
            return false;
        }
        return true;
    }

    private static void firstAction(final Player player, Packet packet) {
        int interfaceId = packet.readUnsignedShort();
        int slot = packet.readShort();
        int itemId = packet.readUnsignedShort();

        Location targetLocation = player.getLocation();

        if (interfaceId == 38274) {
            Construction.handleItemClick(itemId, player);
            return;
        }

        if (slot < 0 || slot > player.getInventory().capacity())
            return;

        Debug.write(player.getName(), "ItemActionPacketListener - firstClick", new String[]{
                "itemId: " + itemId,
                "slot: " + slot,
                "interfaceId: " + interfaceId,
        });

        player.setInteractingItem(player.getInventory().getItems()[slot]);

        if (EliteClueScroll.read(player, itemId)) {
            return;
        }
        if (Prayer.isBone(itemId)) {
            Prayer.buryBone(player, itemId);
            return;
        }
        if (Consumables.isFood(player, itemId, slot))
            return;
        if (Consumables.isPotion(itemId)) {
            Consumables.handlePotion(player, itemId, slot);
            return;
        }
        if (BirdNests.isNest(itemId)) {
        	if(player.hasItem(5070) || player.hasItem(5071) || player.hasItem(5072) || player.hasItem(5073) || player.hasItem(5074)) {//{5070, 5071, 5072, 5073, 5074};
            OpenBirdsNests.openNest(player, itemId);
        	}// where would i add for caskets and charm boxes ect id ?
            return;
        }
        if (Herblore.cleanHerb(player, itemId))
            return;

        if (Effigies.isEffigy(itemId)) {
            Effigies.handleEffigy(player, itemId);
            return;
        }
        if (ExperienceLamps.handleLamp(player, itemId)) {
            return;
        }

        if (TeleportScrolls.handle(player, itemId, slot)) {
            player.getInventory().delete(itemId, 1, true);
            return;
        }

        if (LootBox.open(player, player.getInventory().getItems()[slot], false)) {
            return;
        }
        switch (itemId) {
            case 4053:
                CastleWarsBarricade.setup(player);
                break;
            case 18338:
                GoodieBagManager.open(player);
                break;

            case 4049:
                if (player.getInventory().contains(itemId)) {
                    player.getInventory().delete(4049, 1);
                    CastleWarsBandage.heal(player);
                }
                break;

            case 42791:
                player.getRunePouch().open();
                break;

            case 18339:
                ScratchCards.check(player);
                break;

            case 10942:
                MemberScrolls.giveWarning(player);
                player.setScrollAmount(1);
                break;
            case 10934:
                MemberScrolls.giveWarning(player);
                player.setScrollAmount(2);
                break;
            case 10935:
                MemberScrolls.giveWarning(player);
                player.setScrollAmount(3);
                break;
            case 10943:
                MemberScrolls.giveWarning(player);
                player.setScrollAmount(4);
                break;

            case 11614:
                player.performAnimation(new Animation(4381));
                player.performGraphic(new Graphic(2745));
                break;

            case 43329:
                player.performAnimation(new Animation(22381));
                player.performGraphic(new Graphic(4137));
                break;

            case 13077:
                VoteCasket.open(player);
                break;

            case 14019:
                player.performAnimation(new Animation(22381));
                player.performGraphic(new Graphic(4250));
                break;

            case 52316:
                player.performAnimation(new Animation(14610));
                player.performGraphic(new Graphic(333));
                break;

            case 7509:

                if (player.getConstitution() < 20) {
                    player.getPA().sendMessage("Don't kill yourself!");
                    return;
                }
                player.dealDamage(new Hit((10), Hitmask.CRITICAL, CombatIcon.BLUE_SHIELD));
                if (Misc.getRandom(4) == 1) {
                    player.forceChat("Ouch!");
                }
                break;

            case 13663:
                if (player.getInterfaceId() > 0) {
                    player.getPacketSender().sendMessage("Please close the interface you have open before doing this.");
                    return;
                }
                player.setUsableObject(new Object[2]).setUsableObject(0, "reset");
                player.getPacketSender().sendString(38006, "Choose stat to reset!")
                        .sendMessage("@red@Please select a skill you wish to reset and then click on the 'Confim' button.")
                        .sendString(38090, "Which skill would you like to reset?");
                player.getPacketSender().sendInterface(38000);
                break;
            case 19670:
                if (player.busy()) {
                    player.getPacketSender().sendMessage("You can not do this right now.");
                    return;
                }
                player.setDialogueActionId(70);
                DialogueManager.start(player, player.getGameMode() == GameMode.NORMAL ? 108 : 109);
                break;
            case 8007: // Varrock

                if (!player.getClickDelay().elapsed(1200) || player.getMovementQueue().isLockMovement()) {
                    return;
                }
                if (player.getLocation() == Location.CONSTRUCTION) {
                    player.getPacketSender().sendMessage("Please use the portal to exit your house");
                    return;
                }

                if (!checkReqs(player, targetLocation)) {
                    return;
                }
                player.setTeleporting(true).getMovementQueue().setLockMovement(true).reset();
                cancelCurrentActions(player);
                player.performAnimation(new Animation(4731));
                player.performGraphic(new Graphic(678));
                player.getInventory().delete(8007, 1);
                player.getClickDelay().reset();

                TaskManager.submit(new Task(2, player, false) {
                    @Override
                    public void execute() {
                        Position position = new Position(3210, 3424, 0);
                        player.moveTo(position);
                        player.getMovementQueue().setLockMovement(false).reset();
                        this.stop();
                    }
                });

                break;
            case 8008: // Lumbridge

                if (!player.getClickDelay().elapsed(1200) || player.getMovementQueue().isLockMovement()) {
                    return;
                }
                if (player.getLocation() == Location.CONSTRUCTION) {
                    player.getPacketSender().sendMessage("Please use the portal to exit your house");
                    return;
                }

                if (!checkReqs(player, targetLocation)) {
                    return;
                }
                player.setTeleporting(true).getMovementQueue().setLockMovement(true).reset();
                cancelCurrentActions(player);
                player.performAnimation(new Animation(4731));
                player.performGraphic(new Graphic(678));
                player.getInventory().delete(8008, 1);
                player.getClickDelay().reset();

                TaskManager.submit(new Task(2, player, false) {
                    @Override
                    public void execute() {
                        Position position = new Position(3222, 3218, 0);
                        player.moveTo(position);
                        player.getMovementQueue().setLockMovement(false).reset();
                        this.stop();
                    }
                });

                break;
            case 8009: // Falador

                if (!player.getClickDelay().elapsed(1200) || player.getMovementQueue().isLockMovement()) {
                    return;
                }
                if (player.getLocation() == Location.CONSTRUCTION) {
                    player.getPacketSender().sendMessage("Please use the portal to exit your house");
                    return;
                }

                if (!checkReqs(player, targetLocation)) {
                    return;
                }
                player.setTeleporting(true).getMovementQueue().setLockMovement(true).reset();
                cancelCurrentActions(player);
                player.performAnimation(new Animation(4731));
                player.performGraphic(new Graphic(678));
                player.getInventory().delete(8009, 1);
                player.getClickDelay().reset();

                TaskManager.submit(new Task(2, player, false) {
                    @Override
                    public void execute() {
                        Position position = new Position(2964, 3378, 0);
                        player.moveTo(position);
                        player.getMovementQueue().setLockMovement(false).reset();
                        this.stop();
                    }
                });

                break;
            case 8010: // Camelot

                if (!player.getClickDelay().elapsed(1200) || player.getMovementQueue().isLockMovement()) {
                    return;
                }
                if (player.getLocation() == Location.CONSTRUCTION) {
                    player.getPacketSender().sendMessage("Please use the portal to exit your house");
                    return;
                }

                if (!checkReqs(player, targetLocation)) {
                    return;
                }
                player.setTeleporting(true).getMovementQueue().setLockMovement(true).reset();
                cancelCurrentActions(player);
                player.performAnimation(new Animation(4731));
                player.performGraphic(new Graphic(678));
                player.getInventory().delete(8010, 1);
                player.getClickDelay().reset();

                TaskManager.submit(new Task(2, player, false) {
                    @Override
                    public void execute() {
                        Position position = new Position(2757, 3477, 0);
                        player.moveTo(position);
                        player.getMovementQueue().setLockMovement(false).reset();
                        this.stop();
                    }
                });

                break;
            case 8011: // Ardy

                if (!player.getClickDelay().elapsed(1200) || player.getMovementQueue().isLockMovement()) {
                    return;
                }
                if (player.getLocation() == Location.CONSTRUCTION) {
                    player.getPacketSender().sendMessage("Please use the portal to exit your house");
                    return;
                }

                if (!checkReqs(player, targetLocation)) {
                    return;
                }
                player.setTeleporting(true).getMovementQueue().setLockMovement(true).reset();
                cancelCurrentActions(player);
                player.performAnimation(new Animation(4731));
                player.performGraphic(new Graphic(678));
                player.getInventory().delete(8011, 1);
                player.getClickDelay().reset();

                TaskManager.submit(new Task(2, player, false) {
                    @Override
                    public void execute() {
                        Position position = new Position(2662, 3305, 0);
                        player.moveTo(position);
                        player.getMovementQueue().setLockMovement(false).reset();
                        this.stop();
                    }
                });

                break;
            case 8012: // Watchtower Tele

                if (!player.getClickDelay().elapsed(1200) || player.getMovementQueue().isLockMovement()) {
                    return;
                }
                if (player.getLocation() == Location.CONSTRUCTION) {
                    player.getPacketSender().sendMessage("Please use the portal to exit your house");
                    return;
                }

                if (!checkReqs(player, targetLocation)) {
                    return;
                }
                player.setTeleporting(true).getMovementQueue().setLockMovement(true).reset();
                cancelCurrentActions(player);
                player.performAnimation(new Animation(4731));
                player.performGraphic(new Graphic(678));
                player.getInventory().delete(8012, 1);
                player.getClickDelay().reset();

                TaskManager.submit(new Task(2, player, false) {
                    @Override
                    public void execute() {
                        Position position = new Position(2728, 3349, 0);
                        player.moveTo(position);
                        player.getMovementQueue().setLockMovement(false).reset();
                        this.stop();
                    }
                });

                break;
            case 8013: // Home Tele
                TeleportHandler.teleportPlayer(player, new Position(3087, 3491), TeleportType.NORMAL);
                break;
            case 13598: // Runecrafting Tele
                TeleportHandler.teleportPlayer(player, new Position(2595, 4772), TeleportType.NORMAL);
                break;
            case 13599: // Air Altar Tele
                TeleportHandler.teleportPlayer(player, new Position(2845, 4832), TeleportType.NORMAL);
                break;
            case 13600: // Mind Altar Tele
                TeleportHandler.teleportPlayer(player, new Position(2796, 4818), TeleportType.NORMAL);
                break;
            case 13601: // Water Altar Tele
                TeleportHandler.teleportPlayer(player, new Position(2713, 4836), TeleportType.NORMAL);
                break;
            case 13602: // Earth Altar Tele
                TeleportHandler.teleportPlayer(player, new Position(2660, 4839), TeleportType.NORMAL);
                break;
            case 13603: // Fire Altar Tele
                TeleportHandler.teleportPlayer(player, new Position(2584, 4836), TeleportType.NORMAL);
                break;

            case 13604: // Body Altar Tele
                TeleportHandler.teleportPlayer(player, new Position(2527, 4833), TeleportType.NORMAL);
                break;
            case 13605: // Cosmic Altar Tele
                TeleportHandler.teleportPlayer(player, new Position(2162, 4833), TeleportType.NORMAL);
                break;
            case 13606: // Chaos Altar Tele
                TeleportHandler.teleportPlayer(player, new Position(2269, 4843), TeleportType.NORMAL);
                break;
            case 13607: // Nature Altar Tele
                TeleportHandler.teleportPlayer(player, new Position(2398, 4841), TeleportType.NORMAL);
                break;
            case 13608: // Law Altar Tele
                TeleportHandler.teleportPlayer(player, new Position(2464, 4834), TeleportType.NORMAL);
                break;
            case 13609: // Death Altar Tele
                TeleportHandler.teleportPlayer(player, new Position(2207, 4836), TeleportType.NORMAL);
                break;
            case 18809: // Rimmington Tele
                TeleportHandler.teleportPlayer(player, new Position(2957, 3214), TeleportType.NORMAL);
                break;
            case 18811: // Pollnivneach Tele
                TeleportHandler.teleportPlayer(player, new Position(3359, 2910), TeleportType.NORMAL);
                break;
            case 18812: // Rellekka Tele
                TeleportHandler.teleportPlayer(player, new Position(2659, 3661), TeleportType.NORMAL);
                break;
            case 18814: // Yanielle Tele
                TeleportHandler.teleportPlayer(player, new Position(2606, 3093), TeleportType.NORMAL);
                break;

            case 10025:
                CharmBox.open(player);
                break;

            case 2677:
            case 2678:
            case 2679:
            case 2680:
            case 2681:
            case 2682:
            case 2683:
            case 2684:
            case 2685:
            case 2686:
            case 2687:
            case 2688:
            case 2689:
            case 2690:
                ClueScrolls.giveHint(player, itemId);
                break;
            case 7956:

                if (player.getInventory().getFreeSlots() < 3) {
                    player.sendMessage("You need at least 3 free slots on your inventory to do this.");
                    return;
                }

                if (Misc.randomFloat() >= player.getRights().getLuckChance()) {
                    player.getInventory().delete(7956, 1);
                }

                int[] rewards = {200, 202, 204, 206, 208, 210, 212, 214, 216, 218, 220, 2486, 3052, 1624, 1622, 1620, 1618,
                        1632, 1516, 1514, 454, 448, 450, 452, 378, 372, 7945, 384, 390, 15271, 533, 535, 537, 18831, 556,
                        558, 555, 554, 557, 559, 564, 562, 566, 9075, 563, 561, 560, 565, 888, 890, 892, 11212, 9142, 9143,
                        9144, 9341, 9244, 866, 867, 868, 2, 10589, 10564, 6809, 4131, 15126, 4153, 1704, 1149};
                int[] rewardsAmount = {200, 200, 200, 120, 50, 100, 70, 60, 90, 40, 30, 15, 10, 230, 140, 70, 20, 10, 400,
                        200, 400, 250, 100, 100, 1000, 800, 500, 200, 100, 50, 150, 100, 50, 5, 1500, 1500, 1500, 1500,
                        1500, 1500, 1000, 1000, 500, 500, 500, 500, 500, 500, 3000, 2500, 800, 300, 3500, 3500, 500, 150,
                        80, 3000, 1500, 400, 500, 1, 1, 1, 1, 1, 1, 1, 1};
                int rewardPos = Misc.getRandom(rewards.length - 1);
        		if (!player.getInventory().contains(7956)) {//this should be fine i need to add this casket id
        			return;
        		}
                player.getInventory().add(rewards[rewardPos],
                        (int) ((rewardsAmount[rewardPos] * 0.5) + (Misc.getRandom(rewardsAmount[rewardPos]))),
                        "Casket reward");
                break;

            // Clue Scroll
            case 2714:
                ClueScrolls.addClueReward(player);
                break;
            case EliteClueScroll.CASKET:
                EliteClueScroll.addReward(player);
                break;

            case 15387:
                player.getInventory().delete(15387, 1);
                rewards = new int[]{1377, 1149, 7158, 3000, 219, 5016, 6293, 6889, 2205, 3051, 269, 329, 3779, 6371, 2442,
                        347, 247};
                player.getInventory().add(rewards[RandomUtility.getRandom(rewards.length - 1)], 1, "Backpack");
                break;
            case 407:
                player.getInventory().delete(407, 1);
                if (RandomUtility.getRandom(3) < 3) {
                    player.getInventory().add(409, 1, "Oyster");
                } else if (RandomUtility.getRandom(4) < 4) {
                    player.getInventory().add(411, 1, "Oyster");
                } else
                    player.getInventory().add(413, 1, "Oyster");
                break;
            case 405:
                player.getInventory().delete(405, 1);
                if (RandomUtility.getRandom(1) < 1) {
                    int coins = RandomUtility.getRandom(30000);
                    player.getInventory().add(995, coins, "Casket");
                    player.getPacketSender().sendMessage("The casket contained " + coins + " coins!");
                } else
                    player.getPacketSender().sendMessage("The casket was empty.");
                break;
            case 15084:
                Gambling.rollDice(player);
                break;
            case 299:
                Gambling.plantSeed(player);
                break;
            case 4155:
                if (player.getSlayer().getSlayerTask() == SlayerTasks.NO_TASK
                        && player.getSlayer().getTask().equals(SlayerTask.NO_TASK)) {
                    player.getPacketSender().sendInterfaceRemoval();
                    player.getPacketSender().sendMessage("Your Enchanted gem will only work if you have a Slayer task.");
                    return;
                }
                DialogueManager.start(player, SlayerDialogues.dialogue(player));
                break;
            case 11858:
            case 11860:
            case 11862:
            case 11848:
            case 11856:
            case 11850:
            case 11854:
            case 11852:
            case 11846:
                if (!player.getClickDelay().elapsed(2000) || !player.getInventory().contains(itemId))
                    return;
                if (player.busy()) {
                    player.getPacketSender().sendMessage("You cannot open this right now.");
                    return;
                }

                int[] items = itemId == 11858 ? new int[]{10350, 10348, 10346, 10352}
                        : itemId == 11860 ? new int[]{10334, 10330, 10332, 10336}
                        : itemId == 11862 ? new int[]{10342, 10338, 10340, 10344}
                        : itemId == 11848 ? new int[]{4716, 4720, 4722, 4718}
                        : itemId == 11856 ? new int[]{4753, 4757, 4759, 4755}
                        : itemId == 11850 ? new int[]{4724, 4728, 4730, 4726}
                        : itemId == 11854 ? new int[]{4745, 4749, 4751, 4747}
                        : itemId == 11852
                        ? new int[]{4732, 4734, 4736, 4738}
                        : itemId == 11846
                        ? new int[]{4708, 4712, 4714,
                        4710}
                        : new int[]{itemId};

                if (player.getInventory().getFreeSlots() < items.length) {
                    player.getPacketSender().sendMessage("You do not have enough space in your inventory.");
                    return;
                }
                player.getInventory().delete(itemId, 1);
                for (int i : items) {
                    player.getInventory().add(i, 1, "Item sets");
                }
                player.getPacketSender().sendMessage("You open the set and find items inside.");
                player.getClickDelay().reset();
                break;
            case 952:
                Digging.dig(player);
                break;
            case 10006:
                // Hunter.getInstance().laySnare(client);
                Hunter.layTrap(player, new SnareTrap(new GameObject(19175, new Position(player.getPosition().getX(),
                        player.getPosition().getY(), player.getPosition().getZ())), TrapState.SET, 200, player));
                break;
            case 10008:
                Hunter.layTrap(player, new BoxTrap(new GameObject(19187, new Position(player.getPosition().getX(),
                        player.getPosition().getY(), player.getPosition().getZ())), TrapState.SET, 200, player));
                break;
            case 5509:
            case 5510:
            case 5512:
                RunecraftingPouches.fill(player, RunecraftingPouch.forId(itemId));
                break;
            case 292:
                IngridientsBook.readBook(player, 0, false);
                break;
            case 11882:
                player.getInventory().delete(11882, 1);
                player.getInventory().add(2595, 1, "Armor set").refreshItems();
                player.getInventory().add(2591, 1, "Armor set").refreshItems();
                player.getInventory().add(3473, 1, "Armor set").refreshItems();
                player.getInventory().add(2597, 1, "Armor set").refreshItems();
                break;
            case 11884:
                player.getInventory().delete(11884, 1);
                player.getInventory().add(2595, 1, "Armor set").refreshItems();
                player.getInventory().add(2591, 1, "Armor set").refreshItems();
                player.getInventory().add(2593, 1, "Armor set").refreshItems();
                player.getInventory().add(2597, 1, "Armor set").refreshItems();
                break;
            case 11906:
                player.getInventory().delete(11906, 1);
                player.getInventory().add(7394, 1, "Armor set").refreshItems();
                player.getInventory().add(7390, 1, "Armor set").refreshItems();
                player.getInventory().add(7386, 1, "Armor set").refreshItems();
                break;
            case 15262:
                if (!player.getClickDelay().elapsed(1000))
                    return;
                player.getInventory().delete(15262, 1);
                player.getInventory().add(18016, 10000, "Spirit shard pack").refreshItems();
                player.getClickDelay().reset();
                break;
            case 6:
                DwarfMultiCannon.setupCannon(player);
                break;
        }
    }

    public static void secondAction(Player player, Packet packet) {
        int interfaceId = packet.readLEShortA();
        int slot = packet.readLEShort();
        int itemId = packet.readUnsignedShortA();
        if (slot < 0 || slot > player.getInventory().capacity())
            return;
        if (player.getInventory().getItems()[slot].getId() != itemId)
            return;


        if (SummoningData.isPouch(player, itemId, 2))
            return;

        Debug.write(player.getName(), "ItemActionPacketListener - secondClick", new String[]{
                "itemId: " + itemId,
                "slot: " + slot,
                "interfaceId: " + interfaceId,
        });

        if(itemId >= 1588 && itemId <= 1591) {
            DialogueManager.start(player, new Dialogue() {
                @Override
                public DialogueType type() {
                    return DialogueType.OPTION;
                }

                @Override
                public String[] dialogue() {
                    return new String[] {
                            "Red",
                            "Blue",
                            "Green",
                            "Purple"
                    };
                }

                @Override
                public boolean action(int option) {
                    player.getPA().closeDialogueOnly();

                    int newSkull = -1;

                    switch(option) {
                        case 1:
                            newSkull = 1588;
                            break;
                        case 2:
                            newSkull = 1589;
                            break;
                        case 3:
                            newSkull = 1590;
                            break;
                        case 4:
                            newSkull = 1591;
                            break;
                    }

                    if(player.getInventory().contains(itemId) && newSkull > 0) {
                        player.getInventory().delete(itemId, 1);
                        player.getInventory().add(newSkull, 1);

                        int finalNewSkull = newSkull;
                        DialogueManager.start(player, new Dialogue() {
                            @Override
                            public DialogueType type() {
                                return DialogueType.ITEM_STATEMENT;
                            }

                            @Override
                            public DialogueExpression animation() {
                                return null;
                            }

                            @Override
                            public String[] dialogue() {
                                return new String[] {
                                        "You changed the color of your flaming skull.",
                                };
                            }

                            @Override
                            public String[] item() {
                                return new String[] {
                                        ""+ finalNewSkull +"",
                                        "180",
                                        ""+ItemDefinition.forId(finalNewSkull).getName()+""
                                };
                            }
                        });
                    }

                    return false;
                }
            });
        }

        switch (itemId) {
            case RingOfBosses.ID:
                RingOfBosses.toggleConfig(player);
                break;
            case 6500:
                if (player.getCombatBuilder().isAttacking() || player.getCombatBuilder().isBeingAttacked()) {
                    player.getPacketSender().sendMessage("You cannot configure this right now.");
                    return;
                }
                player.getPacketSender().sendInterfaceRemoval();
                DialogueManager.start(player, 101);
                player.setDialogueActionId(60);
                break;
            case 12926:
            case 12927:
            case 3065:
                player.getBlowpipeLoading().handleUnloadBlowpipe();
                break;
            case 1712:
            case 1710:
            case 1708:
            case 1706:
            case 11118:
            case 11120:
            case 11122:
            case 11124:
                JewelryTeleporting.rub(player, itemId);
                break;
            case 1704:
                player.getPacketSender().sendMessage("Your amulet has run out of charges.");
                break;
            case 11126:
                player.getPacketSender().sendMessage("Your bracelet has run out of charges.");
                break;
            case 13281:
            case 13282:
            case 13283:
            case 13284:
            case 13285:
            case 13286:
            case 13287:
            case 13288:
                player.getSlayer().handleSlayerRingTP(itemId, false);
                break;
            case 5509:
            case 5510:
            case 5512:
                RunecraftingPouches.check(player, RunecraftingPouch.forId(itemId));
                break;
            case 995:
                MoneyPouch.depositMoney(player, player.getInventory().getAmount(995));
                break;
            case 1438:
            case 1448:
            case 1440:
            case 1442:
            case 1444:
            case 1446:
            case 1454:
            case 1452:
            case 1462:
            case 1458:
            case 1456:
            case 1450:
                RunecraftingData.TalismanData talisman = RunecraftingData.TalismanData.forId(itemId);
                Runecrafting.handleTalisman(player, talisman);
                break;
        }
    }

    public void thirdClickAction(Player player, Packet packet) {
        int itemId = packet.readUnsignedShortA();
        int slot = packet.readLEShortA();
        int interfaceId = packet.readLEShortA();
        if (slot < 0 || slot > player.getInventory().capacity())
            return;
        if (player.getInventory().getItems()[slot].getId() != itemId)
            return;

        Debug.write(player.getName(), "ItemActionPacketListener - thirdClick", new String[]{
                "itemId: " + itemId,
                "slot: " + slot,
                "interfaceId: " + interfaceId,
        });

        if (JarData.forJar(itemId) != null) {
            PuroPuro.lootJar(player, new Item(itemId, 1), JarData.forJar(itemId));
            return;
        }
        if (SummoningData.isPouch(player, itemId, 3)) {
            return;
        }
        if (ItemBinding.isBindable(itemId)) {
            ItemBinding.bindItem(player, itemId);
            return;
        }
        switch (itemId) {
            case CrawsBow.CRAWS_BOW:
                CrawsBow.check(player);
                break;
            case RingOfBosses.ID:
                RingOfBosses.sendConfig(player);
                break;
            case 18338:
                GoodieBagManager.openAll(player);
                break;
            case 14019:
            case 14022:
                player.setCurrentCape(itemId);
                int[] colors = itemId == 14019 ? player.getMaxCapeColors() : player.getCompCapeColors();
                String[] join = new String[colors.length];
                for (int i = 0; i < colors.length; i++) {
                    join[i] = Integer.toString(colors[i]);
                }
                player.getPacketSender().sendString(60000, "[CUSTOMIZATION]" + itemId + "," + String.join(",", join));
                player.getPacketSender().sendInterface(60000);
                break;
            case 12926:
            case 12927:
            case 3065:
                player.getBlowpipeLoading().handleCheckBlowpipe();
                break;
            case 19670:
                if (player.busy()) {
                    player.getPacketSender().sendMessage("You can not do this right now.");
                    return;
                }
                player.setDialogueActionId(71);
                DialogueManager.start(player, player.getGameMode() == GameMode.NORMAL ? 108 : 109);
                break;
            case 6500:
                CharmingImp.sendConfig(player);
                break;
            case 4155:
                player.getPacketSender().sendInterfaceRemoval();
                DialogueManager.start(player, 103);
                player.setDialogueActionId(65);
                break;
            case 13281:
            case 13282:
            case 13283:
            case 13284:
            case 13285:
            case 13286:
            case 13287:
            case 13288:
                player.getPacketSender().sendInterfaceRemoval();

                if (!player.getSlayer().getTask().equals(SlayerTask.NO_TASK)) {
                    player.getPacketSender().sendMessage("Your current task is to kill another " + (player.getSlayer().getAmountToSlay()) + " "
                            + Misc.formatText(
                            player.getSlayer().getTask().toString().toLowerCase().replaceAll("_", " "))
                            + "s.");
                    break;
                }

                player.getPacketSender().sendMessage(player.getSlayer().getSlayerTask() == SlayerTasks.NO_TASK
                        ? ("You do not have a Slayer task.")
                        : ("Your current task is to kill another " + (player.getSlayer().getAmountToSlay()) + " "
                        + Misc.formatText(
                        player.getSlayer().getSlayerTask().toString().toLowerCase().replaceAll("_", " "))
                        + "s."));
                break;
            case 6570:
                if (player.getInventory().contains(6570) && player.getInventory().getAmount(6529) >= 50000) {
                    player.getInventory().delete(6570, 1).delete(6529, 50000).add(19111, 1, "Fire cape to tokhaar");
                    player.getPacketSender().sendMessage("You have upgraded your Fire cape into a TokHaar-Kal cape!");
                } else {
                    player.getPacketSender().sendMessage(
                            "You need at least 50.000 Tokkul to upgrade your Fire Cape into a TokHaar-Kal cape.");
                }
                break;
            case 15262:
                if (!player.getClickDelay().elapsed(1300))
                    return;
                int amt = player.getInventory().getAmount(15262);
                if (amt > 0)
                    player.getInventory().delete(15262, amt).add(18016, 10000 * amt, "Spirit shards");
                player.getClickDelay().reset();
                break;
            case 5509:
            case 5510:
            case 5512:
                RunecraftingPouches.empty(player, RunecraftingPouch.forId(itemId));
                break;
            case 11283: // DFS
                player.getPacketSender()
                        .sendMessage("Your Dragonfire shield has " + player.getDfsCharges() + "/20 dragon-fire charges.");
                break;
            case 11613: // dkite
                player.getPacketSender()
                        .sendMessage("Your Dragonfire shield has " + player.getDfsCharges() + "/20 dragon-fire charges.");
                break;
        }
    }

    @Override
    public void handleMessage(Player player, Packet packet) {
        if (player.getConstitution() <= 0) {
            return;
        }

        if (player.isAccountCompromised() || player.requiresUnlocking()) {
            return;
        }

        switch (packet.getOpcode()) {
            case SECOND_ITEM_ACTION_OPCODE:
                secondAction(player, packet);
                break;
            case FIRST_ITEM_ACTION_OPCODE:
                firstAction(player, packet);
                break;
            case THIRD_ITEM_ACTION_OPCODE:
                thirdClickAction(player, packet);
                break;
        }
    }

    public static final int SECOND_ITEM_ACTION_OPCODE = 75;

    public static final int FIRST_ITEM_ACTION_OPCODE = 122;

    public static final int THIRD_ITEM_ACTION_OPCODE = 16;

}