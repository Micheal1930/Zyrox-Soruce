package com.zyrox.net.packet.impl;

import com.zyrox.GameSettings;
import com.zyrox.engine.task.Task;
import com.zyrox.engine.task.TaskManager;
import com.zyrox.engine.task.impl.WalkToTask;
import com.zyrox.engine.task.impl.WalkToTask.FinalizedMovementTask;
import com.zyrox.model.*;
import com.zyrox.model.Locations.Location;
import com.zyrox.model.container.impl.Equipment;
import com.zyrox.model.definitions.ItemDefinition;
import com.zyrox.model.input.impl.DonateToWell;
import com.zyrox.model.input.impl.EnterAmountOfLogsToAdd;
import com.zyrox.net.packet.Packet;
import com.zyrox.net.packet.PacketListener;
import com.zyrox.util.Misc;
import com.zyrox.util.RandomUtility;
import com.zyrox.world.World;
import com.zyrox.world.clip.region.RegionClipping;
import com.zyrox.world.content.*;
import com.zyrox.world.content.auction_house.AuctionHouseManager;
import com.zyrox.world.content.combat.magic.Autocasting;
import com.zyrox.world.content.combat.prayer.CurseHandler;
import com.zyrox.world.content.combat.prayer.PrayerHandler;
import com.zyrox.world.content.combat.range.DwarfMultiCannon;
import com.zyrox.world.content.combat.weapon.CombatSpecial;
import com.zyrox.world.content.dialogue.Dialogue;
import com.zyrox.world.content.dialogue.DialogueExpression;
import com.zyrox.world.content.dialogue.DialogueManager;
import com.zyrox.world.content.dialogue.DialogueType;
import com.zyrox.world.content.donator_zone.DonatorZone;
import com.zyrox.world.content.doors.DoorManager;
import com.zyrox.world.content.doors.DoorResponse;
import com.zyrox.world.content.freeforall.FreeForAll;
import com.zyrox.world.content.greatolm.GreatOlm;
import com.zyrox.world.content.instances.InstanceManager;
import com.zyrox.world.content.minigames.impl.Barrows;
import com.zyrox.world.content.minigames.impl.Dueling;
import com.zyrox.world.content.minigames.impl.FightCave;
import com.zyrox.world.content.minigames.impl.FightPit;
import com.zyrox.world.content.minigames.impl.Nomad;
import com.zyrox.world.content.minigames.impl.PestControl;
import com.zyrox.world.content.minigames.impl.RecipeForDisaster;
import com.zyrox.world.content.minigames.impl.WarriorsGuild;
import com.zyrox.world.content.minigames.impl.Dueling.DuelRule;
import com.zyrox.world.content.minigames.impl.castlewars.CastleWarsManager;
import com.zyrox.world.content.partyroom.PartyRoomManager;
import com.zyrox.world.content.skill.impl.agility.Agility;
import com.zyrox.world.content.skill.impl.construction.Construction;
import com.zyrox.world.content.skill.impl.cooking.CookingData;
import com.zyrox.world.content.skill.impl.crafting.Flax;
import com.zyrox.world.content.skill.impl.dungeoneering.Dungeoneering;
import com.zyrox.world.content.skill.impl.farming.Farming;
import com.zyrox.world.content.skill.impl.fishing.Fishing;
import com.zyrox.world.content.skill.impl.fishing.Fishing.Spot;
import com.zyrox.world.content.skill.impl.hunter.Hunter;
import com.zyrox.world.content.skill.impl.hunter.PuroPuro;
import com.zyrox.world.content.skill.impl.mining.Mining;
import com.zyrox.world.content.skill.impl.mining.MiningData;
import com.zyrox.world.content.skill.impl.mining.Prospecting;
import com.zyrox.world.content.skill.impl.runecrafting.Runecrafting;
import com.zyrox.world.content.skill.impl.runecrafting.RunecraftingData;
import com.zyrox.world.content.skill.impl.smithing.EquipmentMaking;
import com.zyrox.world.content.skill.impl.smithing.Smelting;
import com.zyrox.world.content.skill.impl.thieving.Stalls;
import com.zyrox.world.content.skill.impl.woodcutting.Woodcutting;
import com.zyrox.world.content.skill.impl.woodcutting.WoodcuttingData;
import com.zyrox.world.content.skill.impl.woodcutting.WoodcuttingData.Hatchet;
import com.zyrox.world.content.transportation.TeleportHandler;
import com.zyrox.world.content.transportation.TeleportType;
import com.zyrox.world.content.tutorial.TutorialStages;
import com.zyrox.world.entity.impl.player.Player;

/**
 * This packet listener is called when a player clicked on a game object.
 *
 * @author relex lawl
 */

public class ObjectActionPacketListener implements PacketListener {

    /**
     * The PacketListener logger to debug information and print out errors.
     */
    // private final static Logger logger =
    // Logger.getLogger(ObjectActionPacketListener.class);
    private static void firstClick(final Player player, Packet packet) {
        final int x = packet.readLEShortA();
        final int id = packet.readInt();
        final int y = packet.readUnsignedShortA();
        final Position position = new Position(x, y, player.getPosition().getZ());
        final GameObject gameObject = new GameObject(id, position);
        final RegionClipping region = RegionClipping.forPosition(position);
        if (player.getLocation() != Location.CONSTRUCTION && !CastleWarsManager.inCastleWars(player)) {
            if (!RegionClipping.objectExists(gameObject)) {
                player.getPacketSender().sendMessage("An error occured. Error code: " + id);
                       // .sendMessage("Please report the error to a staff member.");
                return;
            }
        }

        if(player.getTutorialStage() != TutorialStages.COMPLETED) {
            return;
        }

        if (player.getMovementQueue().isLockMovement()) {
            return;
        }

        Debug.write(player.getName(), "ObjectActionPacketListener - firstClick", new String[] {
                "objectId: "+id,
                "x: "+x,
                "y: "+y,
        });

        if (player.getRights() == PlayerRights.OWNER) {
            player.getPacketSender().sendMessage("First click object id: " + id + ", position: " + position.toString());
        }

        int sizeX = 1;
        int sizeY = 1;

        if (gameObject.getDefinition() != null) {
            sizeX = gameObject.getDefinition().getSizeX();
            sizeY = gameObject.getDefinition().getSizeY();
        }

        if (gameObject.getId() == 134515) {
            sizeX = 4;
            sizeY = 4;
        }
        if(gameObject.getId() == 132653) {
            sizeX = 5;
            sizeY = 2;
        }

        if (CastleWarsManager.inCastleWars(player)) {
            if (gameObject.getId() == 4418) {
                sizeX = 4;
            } else if (gameObject.getId() == 4458) {
                sizeY = 2;
            }
        }

        switch (gameObject.getId()) {
            case 6706:
            case 4493:
                sizeX *= 3;
                break;
            case 132653:
                sizeX += 1;
                sizeY += 3;
                break;
            case 14233: case 1423:
                player.moveTo(new Position(2657, 2581, 0));
            	
        }

        player.setInteractingObject(gameObject)
                .setWalkToTask(new WalkToTask(player, position, sizeX, sizeY, new FinalizedMovementTask() {
                    @Override
                    public void execute() {

                        GameObject realGameObject = CustomObjects.getGameObject(position, 10, 0, 4);

                        if(realGameObject != null) {
                            realGameObject.clickObject(player, GameObjectClickType.FIRST_CLICK);
                        }

                        if (ChaosTunnels.usePortal(gameObject, player)) {
                            return;
                        }

                        player.setPositionToFace(gameObject.getPosition());
                        if (player.getRegionInstance() != null) {
                            Construction.handleFifthObjectClick(x, y, id, player);
                        }
                        if (player.getRaids().getTheatreOfBlood().handleObject(gameObject)) {
                            return;
                        }
                        if (WoodcuttingData.Trees.isTree(id)) {
                            Woodcutting.cutWood(player, gameObject, false);
                            return;
                        }
                        if (MiningData.isRock(gameObject.getId())) {
                            Mining.startMining(player, gameObject);
                            return;
                        }
                        if (Farming.isGameObject(player, gameObject, 1))
                            return;
                        if (Runecrafting.runecraftingAltar(player, gameObject.getId())) {
                            RunecraftingData.RuneData rune = RunecraftingData.RuneData.forId(gameObject.getId());
                            if (rune == null)
                                return;
                            Runecrafting.craftRunes(player, rune);
                            return;
                        }
                        if (Agility.handleObject(player, gameObject)) {
                            return;
                        }
                        if (Barrows.handleObject(player, gameObject)) {
                            return;
                        }
                        if (player.getLocation() == Location.WILDERNESS
                                && WildernessObelisks.handleObelisk(gameObject.getId())) {
                            return;
                        }
                        if (CastleWarsManager.handleObjects(player, id, x, y, 1)) {
                            return;
                        }

                        if (player.getRaids().getTheatreOfBlood().isObject(gameObject)) {
                            return;
                        }
                        if (DonatorZone.isObject(player, gameObject)) {
                            return;
                        }
                        switch (id) {
                            case 126760: //gate @ resource area in wild
                                if (player.getPosition().getY() > gameObject.getPosition().getY())
                                    player.moveTo(new Position(gameObject.getPosition().getX(), gameObject.getPosition().getY()));
                                else
                                    player.moveTo(new Position(gameObject.getPosition().getX(), gameObject.getPosition().getY() + 1));
                                break;
                            case 115:
                            case 116:
                            case 117:
                            case 118:
                            case 119:
                            case 120:
                            case 121:
                            case 122:
                                PartyRoomManager.burstBalloon(player, gameObject);
                                break;
                            case PartyRoomManager.LEVER:
                                PartyRoomManager.sendLever(player);
                                break;
                            case PartyRoomManager.CHEST:
                                PartyRoomManager.open(player);
                                break;

                            case 1814: // Ardy lever
                                if (player.getPosition().getX() == 2561 && player.getPosition().getY() == 3311) {
                                    player.setPositionToFace(new Position(x - 2, y));
                                    TeleportHandler.teleportPlayer(player, new Position(3153, 3923), TeleportType.LEVER);
                                }
                                break;

                            case 24161:
                                if (PlayerRights.SUPER_DONATOR.hasEnoughDonated(player)) {
                                    if (!player.specTimer.elapsed(1000 * 60 * 2)) {
                                        player.getPacketSender()
                                                .sendMessage("You can restore your special attack again in "+Misc.getTimeLeftForTimer(1000 * 60 * 2, player.specTimer));
                                        return;
                                    }
                                    player.performAnimation(new Animation(1327));
                                    player.setSpecialPercentage(100);
                                    CombatSpecial.updateBar(player);
                                    int max = player.getSkillManager().getMaxLevel(Skill.CONSTITUTION);
                                    player.setConstitution(max);
                                    player.getSkillManager().setCurrentLevel(Skill.PRAYER,
                                            player.getSkillManager().getMaxLevel(Skill.PRAYER));
                                    player.setPoisonDamage(0);
                                    player.setVenomDamage(0);
                                    player.getPacketSender().sendConstitutionOrbPoison(false);
                                    player.getPacketSender().sendConstitutionOrbVenom(false);
                                    player.getPacketSender().sendMessage(
                                            "<img=10><col=570057><shad=0> You take a drink from the fountain... and feel revived!");
                                    player.specTimer.reset();
                                } else {
                                    if (!player.specTimer.elapsed(1000 * 60 * 5)) {
                                        player.getPacketSender()
                                                .sendMessage("You can restore your special attack again in "+Misc.getTimeLeftForTimer(1000 * 60 * 15, player.specTimer));
                                        return;
                                    }
                                    player.performAnimation(new Animation(1327));
                                    player.setSpecialPercentage(100);
                                    CombatSpecial.updateBar(player);
                                    int max = player.getSkillManager().getMaxLevel(Skill.CONSTITUTION);
                                    player.setConstitution(max);
                                    player.getSkillManager().setCurrentLevel(Skill.PRAYER,
                                            player.getSkillManager().getMaxLevel(Skill.PRAYER));
                                    player.setPoisonDamage(0);
                                    player.setVenomDamage(0);
                                    player.getPacketSender().sendConstitutionOrbPoison(false);
                                    player.getPacketSender().sendConstitutionOrbVenom(false);
                                    player.getPacketSender().sendMessage(
                                            "<icon=2><shad=ff0000> You take a drink from the fountain... and feel revived!");
                                    player.specTimer.reset();
                                }
                                break;

                            case 102030:
                                Smelting.openInterface(player);
                                break;

                            case 100076:
                            case 110060:
                                player.setTempBankTabs(null);
                                player.getBank(player.getCurrentBankTab()).open();
                                break;

                            case 125035:
                                Runecrafting.handleTalisman(player, RunecraftingData.TalismanData.DEATH_TALISMAN);
                                break;

                            case 125380:
                                Runecrafting.handleTalisman(player, RunecraftingData.TalismanData.BLOOD_TALISMAN);
                                break;

                            case 124975:
                                Runecrafting.handleTalisman(player, RunecraftingData.TalismanData.NATURE_TALISMAN);
                                break;
                           /* case 54408:

                                int totalHas = 0;

                                for(Item item : LootBox.boxes.get(Items.HALLOWEEN_BOX).commonItems()) {
                                    if(item.getId() == 6914 || item.getId() == 4151) {
                                        continue;
                                    }

                                    if(player.getInventory().contains(item)) {
                                        totalHas += player.getInventory().getAmount(item.getId());
                                        player.getInventory().delete(item.getId(), player.getInventory().getAmount(item.getId()));
                                    }

                                }

                                if(totalHas == 0) {
                                    DialogueManager.sendStatement(player, "You don't have any halloween items.");
                                    return;
                                }

                                player.performAnimation(new Animation(827));

                                for(int i = 0; i < totalHas; i++) {
                                    player.getInventory().add(4561, Misc.inclusiveRandom(1, 10));
                                    int[] rareItems = new int[] {
                                            1053, 1055, 1057, 11289, 11290, 11292
                                    };

                                    int rareItem = rareItems[Misc.random(rareItems.length - 1)];

                                    if(Misc.random(500) == 1) {
                                        player.getInventory().add(rareItem, 1);
                                        LootBox.sendAnnouncement(":shortalert: :n:" + player.getUsername()
                                                + " opened the Halloween chest@whi@ to receive " + ItemDefinition.forId(rareItem).getName() + "!");
                                    }
                                }
                                break;*/

                            case 124971:
                                Runecrafting.handleTalisman(player, RunecraftingData.TalismanData.FIRE_TALISMAN);
                                break;

                            case 125378:
                                Runecrafting.handleTalisman(player, RunecraftingData.TalismanData.AIR_TALISMAN);
                                break;

                            case 124972:
                                Runecrafting.handleTalisman(player, RunecraftingData.TalismanData.EARTH_TALISMAN);
                                break;

                            case 124973:
                                Runecrafting.handleTalisman(player, RunecraftingData.TalismanData.BODY_TALISMAN);
                                break;

                            case 125376:
                                Runecrafting.handleTalisman(player, RunecraftingData.TalismanData.WATER_TALISMAN);
                                break;

                            case 125379:
                                Runecrafting.handleTalisman(player, RunecraftingData.TalismanData.MIND_TALISMAN);
                                break;

                            case 124976:
                                Runecrafting.handleTalisman(player, RunecraftingData.TalismanData.CHAOS_TALISMAN);
                                break;

                            case 127552:
                                if(!player.getInventory().hasEmptySlot()) {
                                    player.sendMessage("You don't have enough inventory space.");
                                    return;
                                }
                                if(!player.fishBarrelTimer.elapsed(2000)) {
                                    return;
                                }

                                player.fishBarrelTimer.reset();

                                player.setAnimation(new Animation(881));
                                TaskManager.submit(new Task(2, player, false) {

                                    @Override
                                    public void execute() {
                                        stop();
                                    }

                                    @Override
                                    public void stop() {
                                        setEventRunning(false);

                                        int itemId = CookingData.getFirstRawFish(player.getSkillManager().getCurrentLevel(Skill.COOKING));

                                        player.getInventory().add(itemId, Misc.inclusiveRandom(1, 3));

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
                                                        "You pull a "+ ItemDefinition.forId(itemId).getName()+" out of the barrel.",
                                                };
                                            }

                                            @Override
                                            public String[] item() {
                                                return new String[] {
                                                        ""+itemId+"",
                                                        "180",
                                                        ""+ItemDefinition.forId(itemId).getName()+""
                                                };
                                            }
                                        });
                                    }
                                });
                                break;

                            case 124974:
                                Runecrafting.handleTalisman(player, RunecraftingData.TalismanData.COSMIC_TALISMAN);
                                break;

                            case 26760: // Wilderness resource arena
                                if (player.getPosition().getX() == 3184 && player.getPosition().getY() == 3945) {
                                    if (!player.coinPayment(750_000)) {
                                        return;
                                    }

                                    player.moveTo(new Position(x, y, 0));

                                    player.sendMessage("You have paid 750,000 coins and entered the resource arena.");
                                } else {
                                    player.moveTo(new Position(x, y + 1, 0));
                                }
                                break;

                            case 131561:
                                GameObject pillar = RegionClipping.getGameObject(position);

                                if (pillar.getFace() == 1) {
                                    if (player.getPosition().getX() > x) {
                                        player.moveTo(
                                                new Position(pillar.getPosition().getX() - 2, pillar.getPosition().getY()));
                                    } else {
                                        player.moveTo(
                                                new Position(pillar.getPosition().getX() + 2, pillar.getPosition().getY()));
                                    }
                                } else {
                                    if (player.getPosition().getY() > y) {
                                        player.moveTo(
                                                new Position(pillar.getPosition().getX(), pillar.getPosition().getY() - 2));
                                    } else {
                                        player.moveTo(
                                                new Position(pillar.getPosition().getX(), pillar.getPosition().getY() + 2));
                                    }
                                }
                                break;
                            case 131556:
                                player.moveTo(new Position(3241, 10234));
                                break;
                            case 131558:
                                player.moveTo(new Position(3126, 3833));
                                break;
                            case 38660:
                                if (ShootingStar.CRASHED_STAR != null) {

                                }
                                break;
                            case 11434:
                                if (EvilTrees.SPAWNED_TREE != null) {

                                }
                                break;

                            case 2079:
                                TrioBosses.openChest(player);
                                break;

                            case 10621:
                            case 18804:
                            case 24204:
                            case 29577:
                                TreasureChest.handleChest(player, gameObject);
                                break;

                            case 5259:
                                if (player.getPosition().getX() >= 3653) {
                                    player.moveTo(new Position(3651, player.getPosition().getY()));
                                    player.getCombatBuilder().reset(true);

                                } else {
                                    player.setDialogueActionId(73);
                                    DialogueManager.start(player, 115);
                                }
                                break;

                            case 132996:
                                player.getRaids().getTheatreOfBlood().teleportToEntrance();
                                break;

                            case 133016:
                            case 130028:
                                if (player.getRaidsLoot() != null) {
                                    player.getPacketSender().sendItemOnInterface(85077, player.getRaidsLoot().getId(),
                                            player.getRaidsLoot().getAmount());
                                }

                                if (player.getRaidsLootSecond() != null) {
                                    player.getPacketSender().sendItemOnInterface(85078, player.getRaidsLootSecond().getId(),
                                            player.getRaidsLootSecond().getAmount());
                                }

                                player.getPacketSender().sendInterface(85075);
                                break;
                            case 129777: // new olm entrace
//						case 30224: // olm
                                //	player.moveTo(new Position(1232, 3573));
                                if (player.getMinigameAttributes().getRaidsAttributes().getParty() == null) {
                                    player.sendMessage("You need to be in a party to battle the Great Olm.");
                                    return;
                                }
                                if (player.getMinigameAttributes().getRaidsAttributes().getParty().getOwner() != player) {
                                    player.sendMessage("Only the party leader can initiate the Great Olm fight.");
                                    return;
                                }
                                GreatOlm.start(player);

                                break;

                            case 129879: // olm

                                if (player.getRaidsParty() == null) {
                                    player.sendMessage("You must be inside a party to do this.");
                                    return;
                                }

                                int olmX = player.getPosition().getX();
                                if (olmX <= 3231)
                                    olmX = 3231;
                                if (olmX >= 3234)
                                    olmX = 3234;

                                if (player.getPosition().getY() < gameObject.getPosition().getY()) {
                                    player.moveTo(new Position(olmX, 5730, player.getPosition().getZ()));
                                    player.setInsideRaids(true);
                                    player.getPacketSender().sendInteractionOption("null", 2, true);
                                    player.getRaidsParty().getPlayersInRaids().add(player);
                                } else {
                                    /*
                                     * player.moveTo(new Position(olmX, 5728, player.getPosition().getZ()));
                                     * player.setInsideRaids(false);
                                     * player.getRaidsParty().getPlayersInRaids().remove(player);
                                     */
                                    player.sendMessage("The only way to leave is to kill or be killed!");
                                }
                                break;

                            case 129996: // olm

                                player.moveTo(new Position(1254, 3571));
                                player.getPacketSender().sendInteractionOption("Invite", 2, true);

                                player.setInsideRaids(false);
                                if (player.getMinigameAttributes().getRaidsAttributes().getParty() != null)
                                    player.getMinigameAttributes().getRaidsAttributes().getParty().remove(player, false,
                                            true);
                                break;

                            case 129778: // olm
                                player.moveTo(new Position(1254, 3571));
                                player.getPacketSender().sendInteractionOption("Invite", 2, true);

                                player.setInsideRaids(false);
                                if (player.getMinigameAttributes().getRaidsAttributes().getParty() != null)
                                    player.getMinigameAttributes().getRaidsAttributes().getParty().remove(player, false,
                                            true);
                                break;

                            case 130203: // olm
                                player.moveTo(GameSettings.DEFAULT_POSITION);
                                player.setInsideRaids(false);
                                if (player.getMinigameAttributes().getRaidsAttributes().getParty() != null)
                                    player.getMinigameAttributes().getRaidsAttributes().getParty().remove(player, false,
                                            true);
                                break;

                            case 13405:
                                if (!TeleportHandler.checkReqs(player, null)) {
                                    return;
                                }
                                if (!player.getClickDelay().elapsed(4500) || player.getMovementQueue().isLockMovement()) {
                                    return;
                                }
                                Position tele = new Position(3212, 3422,0);
                                if (player.getLocation() == Location.CONSTRUCTION) {
                                    player.setInHome(false);
                                    player.moveTo(tele);
                                    return;
                                }
                                Construction.newHouse(player);
                                Construction.enterHouse(player, player, true, true);
                                player.getPacketSender().sendMessage(
                                        "@red@If your construction area map bugs out, teleport home and back in!");
                                break;
                            case 8987:
                                FreeForAll.handlePortal(player);
                                break;
                            case 38700:
                                player.moveTo(new Position(3092, 3502));
                                break;
                            case 2465:
                                if (player.getLocation() == Location.EDGE) {
                                    player.getPacketSender().sendMessage(
                                            "<img=10> @blu@Welcome to the free-for-all arena! You will not lose any items on death here.");
                                    player.moveTo(new Position(2815, 5511));
                                } else {
                                    player.getPacketSender()
                                            .sendMessage("The portal does not seem to be functioning properly.");
                                }
                                break;
                            case 45803:
                            case 1767:
                                DialogueManager.start(player, 114);
                                player.setDialogueActionId(72);
                                break;
                            case 7352:
                                if (Dungeoneering.doingDungeoneering(player) && player.getMinigameAttributes()
                                        .getDungeoneeringAttributes().getParty().getGatestonePosition() != null) {
                                    player.moveTo(player.getMinigameAttributes().getDungeoneeringAttributes().getParty()
                                            .getGatestonePosition());
                                    player.setEntityInteraction(null);
                                    player.getPacketSender().sendMessage("You are teleported to your party's gatestone.");
                                    player.performGraphic(new Graphic(1310));
                                } else
                                    player.getPacketSender().sendMessage(
                                            "Your party must drop a Gatestone somewhere in the dungeon to use this portal.");
                                break;
                            case 7353:
                                player.moveTo(new Position(2439, 4956, player.getPosition().getZ()));
                                break;
                            case 7321:
                                player.moveTo(new Position(2452, 4944, player.getPosition().getZ()));
                                break;
                            case 7322:
                                player.moveTo(new Position(2455, 4964, player.getPosition().getZ()));
                                break;
                            case 7315:
                                player.moveTo(new Position(2447, 4956, player.getPosition().getZ()));
                                break;
                            case 7316:
                                player.moveTo(new Position(2471, 4956, player.getPosition().getZ()));
                                break;
                            case 7318:
                                player.moveTo(new Position(2464, 4963, player.getPosition().getZ()));
                                break;
                            case 7319:
                                player.moveTo(new Position(2467, 4940, player.getPosition().getZ()));
                                break;
                            case 7324:
                                player.moveTo(new Position(2481, 4956, player.getPosition().getZ()));
                                break;
                            case 11356:
                                player.moveTo(new Position(2860, 9741));
                                player.getPacketSender().sendMessage("You step through the portal..");
                                break;
                            case 4389:
                                player.moveTo(new Position(2640, 4044, 1));
                                player.getPacketSender().sendMessage("There you go..");
                                break;
                            case 21172: // ruby door for nightbeast zone
                                if (player.getAmountDonated() < 2000) {
                                    player.getPacketSender()
                                            .sendMessage("You need a higher donator rank to enter this area!");
                                    return;
                                } else {
                                    player.moveTo(new Position(2652, 4039, 1));
                                }
                                break;
                            case 31435: // Passage from Superior boss cave
                                player.getPacketSender()
                                        .sendMessage("It seems that a magical force is blocking me from entering.");
                                break;
                            case 46935: // dark portal to Superior hall zone
                                if (player.getAmountDonated() < 3000) {
                                    player.getPacketSender().sendMessage("This portal brings you to the dark side.");
                                    return;
                                } else {
                                    TeleportHandler.teleportPlayer(player, new Position(3039, 4402, 0),
                                            player.getSpellbook().getTeleportType());
                                }
                                break;
                            case 49653: // axe statue teleporter
                                if (player.getAmountDonated() < 8000) {
                                    player.getPacketSender().sendMessage("Get ready to fight some lizards.");
                                    return;
                                } else {
                                    TeleportHandler.teleportPlayer(player, new Position(2462, 5293, 0),
                                            player.getSpellbook().getTeleportType());
                                }
                                break;
                            case 41026: // risen statue teleporter
                                if (player.getAmountDonated() < 8000) {
                                    player.getPacketSender().sendMessage("Get ready to fight some rocks.");
                                    return;
                                } else {
                                    TeleportHandler.teleportPlayer(player, new Position(3169, 4941, 0),
                                            player.getSpellbook().getTeleportType());
                                }
                                break;
                            case 4407: // gorilla teleporter home red portal
                                TeleportHandler.teleportPlayer(player, new Position(2803, 10023, 0),
                                        player.getSpellbook().getTeleportType());
                                player.getPacketSender().sendMessage("Lets kick some gorilla ass!");
                                break;
                            case 48675: // diamond demon teleporter
                                TeleportHandler.teleportPlayer(player, new Position(3229, 9697, 0),
                                        player.getSpellbook().getTeleportType());
                                player.getPacketSender().sendMessage("Let's kick some ass!");
                                break;
                            case 4408: // ancient wyvern portal
                                TeleportHandler.teleportPlayer(player, new Position(1662, 5686, 0),
                                        player.getSpellbook().getTeleportType());
                                player.getPacketSender().sendMessage("Let's kick some ass!");
                                break;
                            case 48661: // Arthur's dream entrance teleporter
                                player.getPacketSender().sendMessage("A statue of the elite player Exilee.");
                                break;
                            case 10780:
                                if (player.getSkillManager().getCurrentLevel(Skill.SLAYER) < 90) {
                                    player.getPacketSender()
                                            .sendMessage("You need an Slayer level of at least 90 to enter Karuulm Slayer Dungeon.");
                                    return;
                                }
                                TeleportHandler.teleportPlayer(player, new Position(1311, 10206, 0),
                                        player.getSpellbook().getTeleportType());
                                player.performGraphic(new Graphic(4260));
                                break;
                            case 4406: // nightbeast teleporter
                                if (player.getAmountDonated() < 1500) {
                                    player.getPacketSender()
                                            .sendMessage("This teleport is only available for emerald members!");
                                    return;
                                } else {
                                    TeleportHandler.teleportPlayer(player, new Position(2637, 4052, 1),
                                            player.getSpellbook().getTeleportType());
                                }
                                break;
                            case 14073: // Crystal PvM hall
                                if (player.getAmountDonated() < 6000) {
                                    player.getPacketSender()
                                            .sendMessage("This teleport is only available for crystal members!");
                                    return;
                                } else {
                                    TeleportHandler.teleportPlayer(player, new Position(2637, 4052, 1),
                                            player.getSpellbook().getTeleportType());
                                }
                                break;
                            case 31292: // lava cave back to edgeville
                                player.moveTo(new Position(3088, 3484));
                                break;
                            case 37636: // Crystal rock guardian cave back to crystal zone
                                player.moveTo(new Position(3044, 4531));
                                break;
                            case 27254: // portal to new zone in arthur's dream
                                player.moveTo(new Position(2325, 4587));
                                break;
                            case 20465: // Tarn back to edge
                                player.moveTo(new Position(3088, 3484));
                                break;
                            case 20467: // Tarn back to edge
                                player.moveTo(new Position(3088, 3484));
                                break;
                            case 48669: // sapphire stattue teleporter
                                if (player.getAmountDonated() < 1000) {
                                    player.getPacketSender()
                                            .sendMessage("This teleport is only available for sapphire members!");
                                    return;
                                } else {
                                    TeleportHandler.teleportPlayer(player, new Position(3422, 2916, 0),
                                            player.getSpellbook().getTeleportType());
                                }
                                break;
                            case 48671: // emerald stattue teleporter
                                if (player.getAmountDonated() < 1500) {
                                    player.getPacketSender()
                                            .sendMessage("This teleport is only available for emerald members!");
                                    return;
                                } else {
                                    TeleportHandler.teleportPlayer(player, new Position(2637, 4052, 1),
                                            player.getSpellbook().getTeleportType());
                                }
                                break;
                            case 48673: // ruby stattue teleporter
                                if (player.getAmountDonated() < 2000) {
                                    player.getPacketSender()
                                            .sendMessage("This teleport is only available for ruby members!");
                                    return;
                                } else {
                                    TeleportHandler.teleportPlayer(player, new Position(3417, 2772, 0),
                                            player.getSpellbook().getTeleportType());
                                }
                                break;
                            case 47180:
                                player.getPacketSender().sendMessage("You activate the device..");
                                player.moveTo(new Position(2586, 3912));
                                break;
                            case 10091:
                            case 8702:
                                Fishing.setupFishing(player, Spot.ROCKTAIL);
                                break;
                            case 10087: // dark crab
                                Fishing.setupFishing(player, Spot.DARK_CRAB);
                                break;

                            case 10089: // waterfall anglerfish
                                Fishing.setupFishing(player, Spot.ANGLERFISH);
                                break;

                            case 21314:
                                if (player.getSkillManager().getCurrentLevel(Skill.AGILITY) < 50) {
                                    player.getPacketSender().sendMessage(
                                            "You need an Agility level of at least 50 or higher to cross this ghost bridge");
                                    return;
                                }
                                if (gameObject.getPosition().getX() == 2378 && gameObject.getPosition().getY() == 3840)
                                    player.moveTo(new Position(2378, 3848, 0));
                                break;

                            case 21315:
                                if (player.getSkillManager().getCurrentLevel(Skill.AGILITY) < 50) {
                                    player.getPacketSender().sendMessage(
                                            "You need an Agility level of at least 50 or higher to cross this ghost bridge");
                                    return;
                                }
                                if (gameObject.getPosition().getX() == 2378 && gameObject.getPosition().getY() == 3847)
                                    player.moveTo(new Position(2378, 3840, 0));
                                break;

                            case 29828: // to do
                                if (player.getSkillManager().getCurrentLevel(Skill.AGILITY) < 50) {
                                    player.getPacketSender().sendMessage(
                                            "You need an Agility level of at least 50 or higher to enter this hole");
                                    return;
                                }
                                if (gameObject.getPosition().getX() == 2374 && gameObject.getPosition().getY() == 3851)
                                    player.moveTo(new Position(1351, 10250, 0));
                                break;


                            case 21313:
                                if (player.getSkillManager().getCurrentLevel(Skill.AGILITY) < 50) {
                                    player.getPacketSender().sendMessage(
                                            "You need an Agility level of at least 50 or higher to cross this ghost bridge");
                                    return;
                                }
                                if (gameObject.getPosition().getX() == 2355 && gameObject.getPosition().getY() == 3847)
                                    player.moveTo(new Position(2355, 3839, 0));
                                break;

                            case 21312:
                                if (player.getSkillManager().getCurrentLevel(Skill.AGILITY) < 50) {
                                    player.getPacketSender().sendMessage(
                                            "You need an Agility level of at least 50 or higher to cross this ghost bridge");
                                    return;
                                }
                                if (gameObject.getPosition().getX() == 2355 && gameObject.getPosition().getY() == 3840)
                                    player.moveTo(new Position(2355, 3848, 0));
                                break;

                            case 9319:
                                if (player.getSkillManager().getCurrentLevel(Skill.AGILITY) < 61) {
                                    player.getPacketSender().sendMessage(
                                            "You need an Agility level of at least 61 or higher to climb this");
                                    return;
                                }
                                if (player.getPosition().getZ() == 0)
                                    player.moveTo(new Position(3422, 3549, 1));
                                else if (player.getPosition().getZ() == 1) {
                                    if (gameObject.getPosition().getX() == 3447)
                                        player.moveTo(new Position(3447, 3575, 2));
                                    else
                                        player.moveTo(new Position(3447, 3575, 0));
                                }
                                break;

                            case 9320:
                                if (player.getSkillManager().getCurrentLevel(Skill.AGILITY) < 61) {
                                    player.getPacketSender().sendMessage(
                                            "You need an Agility level of at least 61 or higher to climb this");
                                    return;
                                }
                                if (player.getPosition().getZ() == 1)
                                    player.moveTo(new Position(3422, 3549, 0));
                                else if (player.getPosition().getZ() == 0)
                                    player.moveTo(new Position(3447, 3575, 1));
                                else if (player.getPosition().getZ() == 2)
                                    player.moveTo(new Position(3447, 3575, 1));
                                player.performAnimation(new Animation(828));
                                break;
                            case 2274:
                                if (player.getTeleblockTimer() > 0) {
                                    player.getPacketSender().sendMessage("A magical spell is blocking you from teleporting.");
                                    break;
                                }
                                if (gameObject.getPosition().getX() == 2912 && gameObject.getPosition().getY() == 5300) {
                                    player.moveTo(new Position(2914, 5300, 1));
                                } else if (gameObject.getPosition().getX() == 2914
                                        && gameObject.getPosition().getY() == 5300) {
                                    player.moveTo(new Position(2912, 5300, 2));
                                } else if (gameObject.getPosition().getX() == 2919
                                        && gameObject.getPosition().getY() == 5276) {
                                    player.moveTo(new Position(2918, 5274));
                                } else if (gameObject.getPosition().getX() == 2918
                                        && gameObject.getPosition().getY() == 5274) {
                                    player.moveTo(new Position(2919, 5276, 1));
                                } else if (gameObject.getPosition().getX() == 3001
                                        && gameObject.getPosition().getY() == 3931
                                        || gameObject.getPosition().getX() == 3652
                                        && gameObject.getPosition().getY() == 3488) {
                                    player.moveTo(GameSettings.DEFAULT_POSITION.copy());
                                    player.getPacketSender().sendMessage("The portal teleports you to Edgeville.");
                                }
                                break;
                            case 7839:
                            case 7836:
                            case 7808:
                                int amt = player.getInventory().getAmount(6055);
                                if (amt > 0) {
                                    player.getInventory().delete(6055, amt);
                                    player.getPacketSender().sendMessage("You put the weed in the compost bin.");
                                    player.getSkillManager().addExperience(Skill.FARMING, 20 * amt);
                                } else {
                                    player.getPacketSender().sendMessage("You do not have any weeds in your inventory.");
                                }
                                break;
                            case 5960: // Levers
                            case 5959:
                                player.setDirection(Direction.WEST);
                                TeleportHandler.teleportPlayer(player, new Position(3090, 3475), TeleportType.LEVER);
                                break;
                            case 5096:
                                if (gameObject.getPosition().getX() == 2644 && gameObject.getPosition().getY() == 9593)
                                    player.moveTo(new Position(2649, 9591));
                                break;

                            case 5094:
                                if (gameObject.getPosition().getX() == 2648 && gameObject.getPosition().getY() == 9592)
                                    player.moveTo(new Position(2643, 9594, 2));
                                break;

                            case 5098:
                                if (gameObject.getPosition().getX() == 2635 && gameObject.getPosition().getY() == 9511)
                                    player.moveTo(new Position(2637, 9517));
                                break;

                            case 5097:
                                if (gameObject.getPosition().getX() == 2635 && gameObject.getPosition().getY() == 9514)
                                    player.moveTo(new Position(2636, 9510, 2));
                                break;
                            case 26428:
                            case 26426:
                            case 26425:
                            case 26427:
                                String bossRoom = "Armadyl";
                                boolean leaveRoom = player.getPosition().getY() > 5295;
                                int index = 0;
                                Position movePos = new Position(2839, !leaveRoom ? 5296 : 5295, 2);
                                if (id == 26425) {
                                    bossRoom = "Bandos";
                                    leaveRoom = player.getPosition().getX() > 2863;
                                    index = 1;
                                    movePos = new Position(!leaveRoom ? 2864 : 2863, 5354, 2);
                                } else if (id == 26427) {
                                    bossRoom = "Saradomin";
                                    leaveRoom = player.getPosition().getX() < 2908;
                                    index = 2;
                                    movePos = new Position(leaveRoom ? 2908 : 2907, 5265);
                                } else if (id == 26428) {
                                    bossRoom = "Zamorak";
                                    leaveRoom = player.getPosition().getY() <= 5331;
                                    index = 3;
                                    movePos = new Position(2925, leaveRoom ? 5332 : 5331, 2);
                                }
                                if (!leaveRoom && player.getMinigameAttributes().getGodwarsDungeonAttributes()
                                        .getKillcount()[index] < 20) {
                                    if (player.getAmountDonated() > 49) {

                                    } else {
                                        player.getPacketSender().sendMessage("You need " + Misc.anOrA(bossRoom) + " "
                                                + bossRoom + " killcount of at least 20 to enter this room.");
                                        return;
                                    }

                                }
                                player.moveTo(movePos);
                                player.getMinigameAttributes().getGodwarsDungeonAttributes()
                                        .setHasEnteredRoom(leaveRoom ? false : true);
                                player.getMinigameAttributes().getGodwarsDungeonAttributes().getKillcount()[index] = 0;
                                player.getPacketSender().sendString(16216 + index, "0");
                                break;
                            case 26289:
                            case 26286:
                            case 26288:
                            case 26287:
                                if (System.currentTimeMillis() - player.getMinigameAttributes()
                                        .getGodwarsDungeonAttributes().getAltarDelay() < 600000) {
                                    player.getPacketSender().sendMessage("");
                                    player.getPacketSender()
                                            .sendMessage("You can only pray at a God's altar once every 10 minutes.");
                                    player.getPacketSender().sendMessage("You must wait another "
                                            + (int) ((600 - (System.currentTimeMillis() - player.getMinigameAttributes()
                                            .getGodwarsDungeonAttributes().getAltarDelay()) * 0.001))
                                            + " seconds before being able to do this again.");
                                    return;
                                }
                                int itemCount = id == 26289 ? Equipment.getItemCount(player, "Bandos", false)
                                        : id == 26286 ? Equipment.getItemCount(player, "Zamorak", false)
                                        : id == 26288 ? Equipment.getItemCount(player, "Armadyl", false)
                                        : id == 26287 ? Equipment.getItemCount(player, "Saradomin", false)
                                        : 0;
                                int toRestore = player.getSkillManager().getMaxLevel(Skill.PRAYER) + (itemCount * 10);
                                if (player.getSkillManager().getCurrentLevel(Skill.PRAYER) >= toRestore) {
                                    player.getPacketSender()
                                            .sendMessage("You do not need to recharge your Prayer points at the moment.");
                                    return;
                                }
                                player.performAnimation(new Animation(645));
                                player.getSkillManager().setCurrentLevel(Skill.PRAYER, toRestore);
                                player.getMinigameAttributes().getGodwarsDungeonAttributes()
                                        .setAltarDelay(System.currentTimeMillis());
                                break;
                            case 23093:
                                if (player.getSkillManager().getCurrentLevel(Skill.AGILITY) < 70) {
                                    player.getPacketSender().sendMessage(
                                            "You need an Agility level of at least 70 to go through this portal.");
                                    return;
                                }
                                if (!player.getClickDelay().elapsed(2000))
                                    return;
                                int plrHeight = player.getPosition().getZ();
                                if (plrHeight == 2)
                                    player.moveTo(new Position(2914, 5300, 1));
                                else if (plrHeight == 1) {
                                    int x = gameObject.getPosition().getX();
                                    int y = gameObject.getPosition().getY();
                                    if (x == 2914 && y == 5300)
                                        player.moveTo(new Position(2912, 5299, 2));
                                    else if (x == 2920 && y == 5276)
                                        player.moveTo(new Position(2920, 5274, 0));
                                } else if (plrHeight == 0)
                                    player.moveTo(new Position(2920, 5276, 1));
                                player.getClickDelay().reset();
                                break;
                            case 26439:
                                if (player.getSkillManager().getMaxLevel(Skill.CONSTITUTION) <= 700) {
                                    player.getPacketSender()
                                            .sendMessage("You need a Constitution level of at least 70 to swim across.");
                                    return;
                                }
                                if (!player.getClickDelay().elapsed(1000))
                                    return;
                                if (player.isCrossingObstacle())
                                    return;
                                final String startMessage = "You jump into the icy cold water..";
                                final String endMessage = "You climb out of the water safely.";
                                final int jumpGFX = 68;
                                final int jumpAnimation = 772;
                                player.setSkillAnimation(773);
                                player.setCrossingObstacle(true);
                                player.getUpdateFlag().flag(Flag.APPEARANCE);
                                player.performAnimation(new Animation(3067));
                                final boolean goBack2 = player.getPosition().getY() >= 5344;
                                player.getPacketSender().sendMessage(startMessage);
                                player.moveTo(new Position(2885, !goBack2 ? 5335 : 5342, 2));
                                player.setDirection(goBack2 ? Direction.SOUTH : Direction.NORTH);
                                player.performGraphic(new Graphic(jumpGFX));
                                player.performAnimation(new Animation(jumpAnimation));
                                TaskManager.submit(new Task(1, player, false) {
                                    int ticks = 0;

                                    @Override
                                    public void execute() {
                                        ticks++;
                                        player.getMovementQueue().walkStep(0, goBack2 ? -1 : 1);
                                        if (ticks >= 10)
                                            stop();
                                    }

                                    @Override
                                    public void stop() {
                                        player.setSkillAnimation(-1);
                                        player.setCrossingObstacle(false);
                                        player.getUpdateFlag().flag(Flag.APPEARANCE);
                                        player.getPacketSender().sendMessage(endMessage);
                                        player.moveTo(
                                                new Position(2885, player.getPosition().getY() < 5340 ? 5333 : 5345, 2));
                                        setEventRunning(false);
                                    }
                                });
                                player.getClickDelay().reset((System.currentTimeMillis() + 9000));
                                break;
                            case 26384:
                                if (player.isCrossingObstacle())
                                    return;
                                if (!player.getInventory().contains(2347)) {
                                    player.getPacketSender()
                                            .sendMessage("You need to have a hammer to bang on the door with.");
                                    return;
                                }
                                player.setCrossingObstacle(true);
                                final boolean goBack = player.getPosition().getX() <= 2850;
                                player.performAnimation(new Animation(377));
                                TaskManager.submit(new Task(2, player, false) {
                                    @Override
                                    public void execute() {
                                        player.moveTo(new Position(goBack ? 2851 : 2850, 5333, 2));
                                        player.setCrossingObstacle(false);
                                        stop();
                                    }
                                });
                                break;
                            case 26303:
                                if (!player.getClickDelay().elapsed(1200))
                                    return;
                                if (player.getSkillManager().getCurrentLevel(Skill.RANGED) < 70)
                                    player.getPacketSender()
                                            .sendMessage("You need a Ranged level of at least 70 to swing across here.");
                                else if (!player.getInventory().contains(9418)) {
                                    player.getPacketSender().sendMessage(
                                            "You need a Mithril grapple to swing across here. Explorer Jack might have one.");
                                    return;
                                } else {
                                    player.performAnimation(new Animation(789));
                                    TaskManager.submit(new Task(2, player, false) {
                                        @Override
                                        public void execute() {
                                            player.getPacketSender().sendMessage(
                                                    "You throw your Mithril grapple over the pillar and move across.");
                                            player.moveTo(new Position(2871,
                                                    player.getPosition().getY() <= 5270 ? 5279 : 5269, 2));
                                            stop();
                                        }
                                    });
                                    player.getClickDelay().reset();
                                }
                                break;
                            case 4493:
                                if (player.getPosition().getX() >= 3432) {
                                    player.moveTo(new Position(3433, 3538, 1));
                                }
                                break;
                            case 4494:
                                player.moveTo(new Position(3438, 3538, 0));
                                break;
                            case 4495:
                                player.moveTo(new Position(3417, 3541, 2));
                                break;
                            case 4496:
                                player.moveTo(new Position(3412, 3541, 1));
                                break;
                            case 2491:
                                player.setDialogueActionId(48);
                                DialogueManager.start(player, 87);
                                break;
                            case 25339:
                            case 25340:
                                player.moveTo(new Position(1778, 5346, player.getPosition().getZ() == 0 ? 1 : 0));
                                break;
                            case 10229:
                            case 10230:
                                boolean up = id == 10229;
                                player.performAnimation(new Animation(up ? 828 : 827));
                                player.getPacketSender().sendMessage("You climb " + (up ? "up" : "down") + " the ladder..");
                                TaskManager.submit(new Task(1, player, false) {
                                    @Override
                                    protected void execute() {
                                        player.moveTo(up ? new Position(1912, 4367) : new Position(2900, 4449));
                                        stop();
                                    }
                                });
                                break;
                            case 1765:
                            case 118988:
                                up = id == 118988;
                                player.performAnimation(new Animation(up ? 828 : 827));
                                player.getPacketSender().sendMessage("You climb " + (up ? "up" : "down") + " the ladder..");
                                TaskManager.submit(new Task(1, player, false) {
                                    @Override
                                    protected void execute() {
                                        player.moveTo(up ? new Position(3017, 3848) : new Position(3069, 10255));
                                        stop();
                                    }
                                });
                                break;
                            case 1817: // Levers
                            case 101816: // Levers
                                if (player.getTeleblockTimer() > 0) {
                                    player.getPacketSender()
                                            .sendMessage("A magical spell is blocking you from teleporting.");
                                    return;
                                }

                               // player.getPacketSender().sendObjectAnimation(realGameObject, new Animation(1));

                                if (player.getPosition().getX() == 3091 && player.getPosition().getY() == 3487) {
                                    player.setPositionToFace(new Position(x - 2, y));
                                    TeleportHandler.teleportPlayer(player, new Position(3153, 3923), TeleportType.LEVER);
                                    return;
                                } else if (player.getPosition().getX() == 3153 && player.getPosition().getY() == 3923) {
                                    player.setPositionToFace(new Position(x - 2, y));
                                    TeleportHandler.teleportPlayer(player, new Position(3091, 3487), TeleportType.LEVER);
                                    return;
                                }

                                if (gameObject.getPosition().getX() == 2539 && gameObject.getPosition().getY() == 4712) {
                                    player.setDirection(Direction.SOUTH);
                                    TeleportHandler.teleportPlayer(player, new Position(3090, 3956), TeleportType.LEVER);
                                } else if (gameObject.getPosition().getX() == 3067
                                        && gameObject.getPosition().getY() == 10253) {
                                    TaskManager.submit(new Task(1, player, true) {
                                        int tick = 1;

                                        @Override
                                        public void execute() {
                                            tick++;
                                            player.performAnimation(new Animation(2140));
                                            if (tick >= 2) {
                                                stop();
                                            }
                                        }

                                        @Override
                                        public void stop() {
                                            setEventRunning(false);
                                            TeleportHandler.teleportPlayer(player, new Position(2271, 4680, 0),
                                                    TeleportType.LEVER);
                                        }
                                    });
                                } else if (gameObject.getPosition().getX() == 2271
                                        && gameObject.getPosition().getY() == 4680) {
                                    TaskManager.submit(new Task(1, player, true) {
                                        int tick = 1;

                                        @Override
                                        public void execute() {
                                            tick++;
                                            player.performAnimation(new Animation(2140));
                                            if (tick >= 2) {
                                                stop();
                                            }
                                        }

                                        @Override
                                        public void stop() {
                                            setEventRunning(false);
                                            TeleportHandler.teleportPlayer(player, new Position(3067, 10253),
                                                    TeleportType.LEVER);
                                        }
                                    });
                                }
                                break;
                            case 1568:
                                player.moveTo(new Position(3097, 9868));
                                break;
                            case 5103: // Brimhaven vines
                            case 5104:
                            case 5105:
                            case 5106:
                            case 5107:
                                if (!player.getClickDelay().elapsed(4000))
                                    return;
                                if (player.getSkillManager().getCurrentLevel(Skill.WOODCUTTING) < 30) {
                                    player.getPacketSender()
                                            .sendMessage("You need a Woodcutting level of at least 30 to do this.");
                                    return;
                                }
                                if (WoodcuttingData.getHatchet(player) < 0) {
                                    player.getPacketSender().sendMessage(
                                            "You do not have a hatchet which you have the required Woodcutting level to use.");
                                    return;
                                }
                                final Hatchet axe = Hatchet.forId(WoodcuttingData.getHatchet(player));
                                player.performAnimation(new Animation(axe.getAnim()));
                                gameObject.setFace(-1);
                                TaskManager.submit(new Task(3 + RandomUtility.getRandom(4), player, false) {
                                    @Override
                                    protected void execute() {
                                        if (player.getMovementQueue().isMoving()) {
                                            stop();
                                            return;
                                        }
                                        int x = 0;
                                        int y = 0;
                                        if (player.getPosition().getX() == 2689
                                                && player.getPosition().getY() == 9564) {
                                            x = 2;
                                            y = 0;
                                        } else if (player.getPosition().getX() == 2691
                                                && player.getPosition().getY() == 9564) {
                                            x = -2;
                                            y = 0;
                                        } else if (player.getPosition().getX() == 2695
                                                && player.getPosition().getY() == 9482) {
                                            x = -2;
                                            y = 0;
                                        } else if (player.getPosition().getX() == 2683
                                                && player.getPosition().getY() == 9568) {
                                            x = 0;
                                            y = 2;
                                        } else if (player.getPosition().getX() == 2683
                                                && player.getPosition().getY() == 9570) {
                                            x = 0;
                                            y = -2;
                                        } else if (player.getPosition().getX() == 2674
                                                && player.getPosition().getY() == 9479) {
                                            x = 2;
                                            y = 0;
                                        } else if (player.getPosition().getX() == 2676
                                                && player.getPosition().getY() == 9479) {
                                            x = -2;
                                            y = 0;
                                        } else if (player.getPosition().getX() == 2693
                                                && player.getPosition().getY() == 9482) {
                                            x = 2;
                                            y = 0;
                                        } else if (player.getPosition().getX() == 2672
                                                && player.getPosition().getY() == 9499) {
                                            x = 2;
                                            y = 0;
                                        } else if (player.getPosition().getX() == 2674
                                                && player.getPosition().getY() == 9499) {
                                            x = -2;
                                            y = 0;
                                        }
                                        CustomObjects.objectRespawnTask(player,
                                                new GameObject(-1, gameObject.getPosition().copy()), gameObject, 10);
                                        player.getPacketSender().sendMessage("You chop down the vines..");
                                        player.getSkillManager().addExperience(Skill.WOODCUTTING, 45 * Skill.WOODCUTTING.getModifier());
                                        player.performAnimation(new Animation(65535));
                                        player.getMovementQueue().walkStep(x, y);
                                        stop();
                                    }
                                });
                                player.getClickDelay().reset();
                                break;
                            case 29942:
                                if (player.getSkillManager().getCurrentLevel(Skill.SUMMONING) == player.getSkillManager()
                                        .getMaxLevel(Skill.SUMMONING)) {
                                    player.getPacketSender()
                                            .sendMessage("You do not need to recharge your Summoning points right now.");
                                    return;
                                }
                                player.performGraphic(new Graphic(1517));
                                player.getSkillManager().setCurrentLevel(Skill.SUMMONING,
                                        player.getSkillManager().getMaxLevel(Skill.SUMMONING), true);
                                player.getPacketSender().sendString(18045,
                                        " " + player.getSkillManager().getCurrentLevel(Skill.SUMMONING) + "/"
                                                + player.getSkillManager().getMaxLevel(Skill.SUMMONING));
                                player.getPacketSender().sendMessage("You recharge your Summoning points.");
                                break;
                            case 57225:
                                if (!player.getMinigameAttributes().getGodwarsDungeonAttributes().hasEnteredRoom()) {
                                    player.setDialogueActionId(44);
                                    DialogueManager.start(player, 131);
                                } else {
                                    player.moveTo(new Position(2906, 5204));
                                    player.getMinigameAttributes().getGodwarsDungeonAttributes().setHasEnteredRoom(false);
                                }
                                break;
                            case 884:
                                player.setDialogueActionId(41);
                                DialogueManager.start(player, 75);
                                break;
                            case 9294:
                                if (player.getSkillManager().getCurrentLevel(Skill.AGILITY) < 80) {
                                    player.getPacketSender()
                                            .sendMessage("You need an Agility level of at least 80 to use this shortcut.");
                                    return;
                                }
                                player.performAnimation(new Animation(769));
                                TaskManager.submit(new Task(1, player, false) {
                                    @Override
                                    protected void execute() {
                                        player.moveTo(
                                                new Position(player.getPosition().getX() >= 2880 ? 2878 : 2880, 9813));
                                        stop();
                                    }
                                });
                                break;
                            case 9293:
                                boolean back = player.getPosition().getX() > 2888;
                                player.moveTo(back ? new Position(2886, 9799) : new Position(2891, 9799));
                                break;
                            case 2320:
                                back = player.getPosition().getY() == 9969 || player.getPosition().getY() == 9970;
                                player.moveTo(back ? new Position(3120, 9963) : new Position(3120, 9969));
                                break;
                            case 1755:
                                player.performAnimation(new Animation(828));
                                player.getPacketSender().sendMessage("You climb the stairs..");
                                TaskManager.submit(new Task(1, player, false) {
                                    @Override
                                    protected void execute() {
                                        if (gameObject.getPosition().getX() == 2547
                                                && gameObject.getPosition().getY() == 9951) {
                                            player.moveTo(new Position(2548, 3551));
                                        } else if (gameObject.getPosition().getX() == 3005
                                                && gameObject.getPosition().getY() == 10363) {
                                            player.moveTo(new Position(3005, 3962));
                                        } else if (gameObject.getPosition().getX() == 3084
                                                && gameObject.getPosition().getY() == 9672) {
                                            player.moveTo(new Position(3117, 3244));
                                        } else if (gameObject.getPosition().getX() == 3097
                                                && gameObject.getPosition().getY() == 9867) {
                                            player.moveTo(new Position(3076, 3481));
                                        }
                                        stop();
                                    }
                                });
                                break;
                            case 5110:
                                player.moveTo(new Position(2647, 9557));
                                player.getPacketSender().sendMessage("You pass the stones..");
                                break;
                            case 5111:
                                player.moveTo(new Position(2649, 9562));
                                player.getPacketSender().sendMessage("You pass the stones..");
                                break;
                            case 6434:
                                player.performAnimation(new Animation(827));
                                player.getPacketSender().sendMessage("You enter the trapdoor..");
                                TaskManager.submit(new Task(1, player, false) {
                                    @Override
                                    protected void execute() {
                                        player.moveTo(new Position(3085, 9672));
                                        stop();
                                    }
                                });
                                break;
                            case 110048:
                                player.performAnimation(new Animation(827));
                                player.getPacketSender().sendMessage("You enter the trapdoor..");
                                TaskManager.submit(new Task(1, player, false) {
                                    @Override
                                    protected void execute() {
                                        player.moveTo(new Position(3097, 9868));
                                        stop();
                                    }
                                });
                                break;
                            case 19187:
                            case 19175:
                                Hunter.dismantle(player, gameObject);
                                break;
                            case 25029:
                                PuroPuro.goThroughWheat(player, gameObject);
                                break;
                            case 47976:
                                Nomad.endFight(player, false);
                                break;
                            case 2182:
                                if (!player.getMinigameAttributes().getRecipeForDisasterAttributes().hasFinishedPart(0)) {
                                    player.getPacketSender()
                                            .sendMessage("You have no business with this chest. Talk to the Gypsy first!");
                                    return;
                                }

                                RecipeForDisaster.openRFDShop(player);
                                break;
                            case 12356:
                                if (!player.getMinigameAttributes().getRecipeForDisasterAttributes().hasFinishedPart(0)) {
                                    player.getPacketSender()
                                            .sendMessage("You have no business with this portal. Talk to the Gypsy first!");
                                    return;
                                }

                                if (player.getPosition().getZ() > 0) {
                                    RecipeForDisaster.leave(player);
                                } else {
                                    player.getMinigameAttributes().getRecipeForDisasterAttributes().setPartFinished(1,
                                            true);
                                    RecipeForDisaster.enter(player);
                                }
                                break;
                            case 9369:
                                if (player.getPosition().getY() > 5175) {
                                    FightPit.addPlayer(player);
                                } else {
                                    FightPit.removePlayer(player, "leave room");
                                }
                                break;
                            case 9368:
                                if (player.getPosition().getY() < 5169) {
                                    FightPit.removePlayer(player, "leave game");
                                }
                                break;
                            case 357:

                                break;

                            case 170:
                                if (!player.getClickDelay().elapsed(3500))
                                    return;

                                if(player.getInventory().getFreeSlots() < 1) {
                                    player.sendMessage("You don't have any inventory space..");
                                    return;
                                }

                                int[] rewards = {7937, 7937, 7937, 214, 214, 12163, 12160, 454, 454, 454, 2360, 25, 15335,
                                        1620, 1748, 1748, 15271, 15271, 1359, 309, 1275, 1632, 1618, 1516, 1514, 2996, 314,
                                        537, 224, 995, 2364, 52, 15517, 11256};
                                int[] rewardsAmount = {20, 50, 100, 2, 4, 3, 3, 5, 10, 15, 3, 7, 1, 3, 2, 4, 2, 4, 1, 1, 1,
                                        1, 2, 20, 10, 5, 20, 3, 5, 10000, 1, 100, 1, 1};
                                int rewardPos = Misc.getRandom(rewards.length - 1);

                                player.performAnimation(new Animation(827));
                                player.getClickDelay().reset();
                                player.getSkillManager().addExperience(Skill.THIEVING, 6500);

                                player.getInventory().add(rewards[rewardPos], (int) ((rewardsAmount[rewardPos])),
                                        "Thieving reward");

                                break;
                            case 1:

                                break;
                            case 9357:
                                FightCave.leaveCave(player, false);
                                break;
                            case 9356:
                                FightCave.enterCave(player);
                                break;
                            case 6704:
                                player.moveTo(new Position(3577, 3282, 0));
                                break;
                            case 6706:
                                player.moveTo(new Position(3554, 3283, 0));
                                break;
                            case 6705:
                                player.moveTo(new Position(3566, 3275, 0));
                                break;
                            case 6702:
                                player.moveTo(new Position(3564, 3289, 0));
                                break;
                            case 6703:
                                player.moveTo(new Position(3574, 3298, 0));
                                break;
                            case 6707:
                                player.moveTo(new Position(3556, 3298, 0));
                                break;
                            case 3203:
                                if (player.getLocation() == Location.DUEL_ARENA && player.getDueling().duelingStatus == 5) {
                                    if (Dueling.checkRule(player, DuelRule.NO_FORFEIT)) {
                                        player.getPacketSender().sendMessage("Forfeiting has been disabled in this duel.");
                                        return;
                                    }
                                    player.getCombatBuilder().reset(true);
                                    if (player.getDueling().duelingWith > -1) {
                                        Player duelEnemy = World.getPlayers().get(player.getDueling().duelingWith);
                                        if (duelEnemy == null)
                                            return;
                                        duelEnemy.getCombatBuilder().reset(true);
                                        duelEnemy.getMovementQueue().reset();
                                        duelEnemy.getDueling().duelVictory();
                                    }
                                    player.moveTo(new Position(3368 + RandomUtility.getRandom(5),
                                            3267 + RandomUtility.getRandom(3), 0));
                                    player.getDueling().reset();
                                    player.getCombatBuilder().reset(true);
                                    player.restart();
                                }
                                break;
                            case 14315:
                            	if(!GameSettings.PC_ENABLED) {
                            		player.sendMessage("<img=3><col=e30000><shad=0> [SECURITY] Pest Control is disabled at the moment, we're sorry");
                            		return;
                            	}
                                PestControl.boardBoat(player);
                                //player.sendParallellInterfaceVisibility(21119, true);
                                break;
                            case 14314:
                                if (player.getLocation() == Location.PEST_CONTROL_BOAT) {
                                    player.getLocation().leave(player);
                                }
                                break;
                            case 1738:
                                if (player.getLocation() == Location.LUMBRIDGE && player.getPosition().getZ() == 0) {
                                    player.moveTo(
                                            new Position(player.getPosition().getX(), player.getPosition().getY(), 1));
                                } else {
                                    player.moveTo(new Position(2840, 3539, 2));
                                }
                                break;
                            case 15638:
                                player.moveTo(new Position(2840, 3539, 0));
                                break;
                            case 15644:
                            case 15641:
                                if(player.getPosition().getZ() == 0)
                                    break;
                                switch (player.getPosition().getZ()) {
                                    case 2:
                                        if (player.getPosition().getX() == 2846) {
                                            if (player.getInventory().getAmount(8851) < 70) {
                                                player.getPacketSender()
                                                        .sendMessage("You need at least 70 tokens to enter this area.");
                                                return;
                                            }
                                            DialogueManager.start(player, WarriorsGuild.warriorsGuildDialogue(player));
                                            player.moveTo(new Position(2847, player.getPosition().getY(), 2));
                                            WarriorsGuild.handleTokenRemoval(player);
                                        } else if (player.getPosition().getX() == 2847) {
                                            WarriorsGuild.resetCyclopsCombat(player);
                                            player.moveTo(new Position(2846, player.getPosition().getY(), 2));
                                            player.getMinigameAttributes().getWarriorsGuildAttributes()
                                                    .setEnteredTokenRoom(false);
                                        }
                                        break;
                                }
                                return;
                            case 10527:
                            case 10529:
                                switch (player.getPosition().getZ()) {
                                    case 1:
                                        if (player.getPosition().getY() >= 3556) {
                                            player.moveTo(new Position(player.getPosition().getX(), 3555, 1));
                                        } else if (player.getPosition().getY() <= 3555) {
                                            player.moveTo(new Position(player.getPosition().getX(), 3556, 1));
                                        }
                                        break;
                                }
                                return;
                            case 5126:
                                switch (player.getPosition().getZ()) {
                                    case 2:
                                        if (player.getPosition().getY() >= 3555) {
                                            player.moveTo(new Position(player.getPosition().getX(), 3554, 2));
                                        } else if (player.getPosition().getY() <= 3554) {
                                            player.moveTo(new Position(player.getPosition().getX(), 3555, 2));
                                        }
                                        break;
                                }
                                return;
                            case 28714:
                                player.performAnimation(new Animation(828));
                                player.delayedMoveTo(new Position(3089, 3492), 2);
                                break;
                            case 1746:
                                player.performAnimation(new Animation(827));
                                player.delayedMoveTo(new Position(2209, 5348), 2);
                                break;
                            case 19191:
                            case 19189:
                            case 19180:
                            case 19184:
                            case 19182:
                            case 19178:
                                Hunter.lootTrap(player, gameObject);
                                break;
                            case 13493:

                                double c = Math.random() * 100;
                                int reward = c >= 70 ? 13003
                                        : c >= 45 ? 4131
                                        : c >= 35 ? 1113
                                        : c >= 25 ? 1147
                                        : c >= 18 ? 1163 : c >= 12 ? 1079 : c >= 5 ? 1201 : 1127;
                                Stalls.stealFromStall(player, 95, 24800, reward, "You stole some rune equipment.");
                                break;
                            case 30205:
                            case 3192:
                                player.setDialogueActionId(11);
                                DialogueManager.start(player, 20);
                                break;
                            case 28716:
                                if (!player.busy()) {
                                    player.getSkillManager().updateSkill(Skill.SUMMONING);
                                    player.getPacketSender().sendInterface(63471);
                                } else
                                    player.getPacketSender()
                                            .sendMessage("Please finish what you're doing before opening this.");
                                break;
                            case 100_006:
                                DwarfCannon cannon = player.getCannon();
                                if (cannon == null || cannon.getOwnerIndex() != player.getIndex()) {
                                    player.getPacketSender().sendMessage("This is not your cannon!");
                                } else {
                                    DwarfMultiCannon.startFiringCannon(player, cannon);
                                }
                                break;
                            case 2:
                                player.moveTo(new Position(player.getPosition().getX() > 2690 ? 2687 : 2694, 3714));
                                player.getPacketSender().sendMessage("You walk through the entrance..");
                                break;
                            case 2026:
                            case 2028:
                            case 2029:
                            case 2030:
                            case 2031:
                                player.setEntityInteraction(gameObject);
                                Fishing.setupFishing(player, Fishing.forSpot(gameObject.getId(), false));
                                return;
                            case 12692:
                            case 2783:
                            case 4306:
                            case 2097:
                            case 102031:
                            case 102672:
                                player.setInteractingObject(gameObject);
                                EquipmentMaking.handleAnvil(player);
                                // player.getPacketSender().sendMessage("Temporarily Disabled until fixed.");

                                break;
                            case 2732:
                            case 52709:
                            case 104265:
                                EnterAmountOfLogsToAdd.openInterface(player, gameObject);
                                break;
                            case 100409:
                            case 409:
                            case 27661:
                            case 2640:
                            case 36972:
                                player.performAnimation(new Animation(645));
                                if (player.getSkillManager().getCurrentLevel(Skill.PRAYER) < player.getSkillManager()
                                        .getMaxLevel(Skill.PRAYER)) {
                                    player.getSkillManager().setCurrentLevel(Skill.PRAYER,
                                            player.getSkillManager().getMaxLevel(Skill.PRAYER), true);
                                    player.getPacketSender().sendMessage("You recharge your Prayer points.");
                                }
                                break;
                            case 11398: // Leave teleporter from Arthur's dream
                                player.moveTo(new Position(3091, 3503, 0));
                                player.performGraphic(new Graphic(4261));
                                break;
                            case 21893: // special restore altar Onyx Hall
                            case 46984: // Arthur's Dream II Statue
                            case 35469: // MMA ZONE
                            case 8749: // special restore altar
                                player.setSpecialPercentage(100);
                                CombatSpecial.updateBar(player);
                                player.getSkillManager().setCurrentLevel(Skill.PRAYER,
                                        player.getSkillManager().getMaxLevel(Skill.PRAYER), true);
                                player.getSkillManager().setCurrentLevel(Skill.CONSTITUTION,
                                        player.getSkillManager().getMaxLevel(Skill.CONSTITUTION), true);
                                player.getPacketSender()
                                        .sendMessage("Your special attack, prayer, and health has been restored.");
                                player.performGraphic(new Graphic(1302));
                                player.setVenomDamage(0);
                                player.getPacketSender().sendConstitutionOrbVenom(false);
                                player.setPoisonDamage(0);
                                player.getPacketSender().sendConstitutionOrbPoison(false);
                                break;
                            case 4859:
                                player.performAnimation(new Animation(645));
                                if (player.getSkillManager().getCurrentLevel(Skill.PRAYER) < player.getSkillManager()
                                        .getMaxLevel(Skill.PRAYER)) {
                                    player.getSkillManager().setCurrentLevel(Skill.PRAYER,
                                            player.getSkillManager().getMaxLevel(Skill.PRAYER), true);
                                    player.getPacketSender().sendMessage("You recharge your Prayer points.");
                                }
                                break;
                            case 411:
                                if (player.getSkillManager().getMaxLevel(Skill.DEFENCE) < 30) {
                                    player.getPacketSender()
                                            .sendMessage("You need a Defence level of at least 30 to use this altar.");
                                    return;
                                }
                                player.performAnimation(new Animation(645));
                                if (player.getPrayerbook() == Prayerbook.NORMAL) {
                                    player.getPacketSender()
                                            .sendMessage("You sense a surge of power flow through your body!");
                                    player.setPrayerbook(Prayerbook.CURSES);
                                } else {
                                    player.getPacketSender()
                                            .sendMessage("You sense a surge of purity flow through your body!");
                                    player.setPrayerbook(Prayerbook.NORMAL);
                                }
                                player.getPacketSender().sendTabInterface(GameSettings.PRAYER_TAB,
                                        player.getPrayerbook().getInterfaceId());
                                PrayerHandler.deactivateAll(player);
                                CurseHandler.deactivateAll(player);
                                break;
                            case 6552:
                                player.performAnimation(new Animation(645));
                                player.setSpellbook(player.getSpellbook() == MagicSpellbook.ANCIENT ? MagicSpellbook.NORMAL
                                        : MagicSpellbook.ANCIENT);
                                player.getPacketSender()
                                        .sendTabInterface(GameSettings.MAGIC_TAB, player.getSpellbook().getInterfaceId())
                                        .sendMessage("Your magic spellbook is changed..");
                                Autocasting.resetAutocast(player, true);
                                break;
                            case 13179:
                                if (player.getSkillManager().getMaxLevel(Skill.DEFENCE) < 40) {
                                    player.getPacketSender()
                                            .sendMessage("You need a Defence level of at least 40 to use this altar.");
                                    return;
                                }
                                player.performAnimation(new Animation(645));
                                player.setSpellbook(player.getSpellbook() == MagicSpellbook.LUNAR ? MagicSpellbook.NORMAL
                                        : MagicSpellbook.LUNAR);
                                player.getPacketSender()
                                        .sendTabInterface(GameSettings.MAGIC_TAB, player.getSpellbook().getInterfaceId())
                                        .sendMessage("Your magic spellbook is changed..");
                                ;
                                Autocasting.resetAutocast(player, true);
                                break;
                            case 172:
                                CrystalChest.handleChest(player, gameObject);
                                break;
                            case 134584:
                            case 134586:
                            case 10624:
                                StoneChest.handleChest(player, gameObject);
                                break;
                            case 37010:
                                Wilderness2Chest.handleChest(player, gameObject);
                                break;
                            case 6910:
                            case 4483:
                            case 3193:
                            case 2213:
                            case 11758:
                            case 14367:
                            case 42192:
                            case 130087:
                            case 103194:
                            case 106943:
                            case 110083:
                            case 127260:
                            case 75:
                                player.setTempBankTabs(null);
                                player.getBank(player.getCurrentBankTab()).open();
                                break;

                            case 134553:
                            case 134554:
                                if (!player.getClickDelay().elapsed(2000) || player.getMovementQueue().isLockMovement()) {
                                    return;
                                }
                                if (player.getPosition().getX() <= 1355) {
                                    InstanceManager.get().enterHydra(player);
                                } else {
                                    player.moveTo(new Position(1355, 10258, 0), true);
                                }
                                player.getClickDelay().reset();
                                break;

                            case 134548:
                                if (player.getSkillManager().getCurrentLevel(Skill.SLAYER) < 90) {
                                    player.getPacketSender()
                                            .sendMessage("You need an Slayer level of at least 90 to climb this rock.");
                                    return;
                                }
                                if (!player.getClickDelay().elapsed(2000) || player.getMovementQueue().isLockMovement()) {
                                    return;
                                }
                                player.moveTo(new Position(player.getPosition().getX(), player.getPosition().getY() == 10250 ? 10252 : 10250, 0));
                                player.getClickDelay().reset();
                                break;
                            case 134544:
                                if (!player.getClickDelay().elapsed(2000) || player.getMovementQueue().isLockMovement()) {
                                    return;
                                }
                                if (gameObject.getPosition().getX() == 1311 && gameObject.getPosition().getY() == 10215) {
                                    player.moveTo(new Position(player.getPosition().getX() >= 1312 ? 1312 : 1311, player.getPosition().getY() == 10216 ? 10214 : 10216, 0));
                                } else if (gameObject.getPosition().getX() == 1302 && gameObject.getPosition().getY() == 10205) {
                                    player.moveTo(new Position(player.getPosition().getX() == 1303 ? 1301 : 1303, player.getPosition().getY() >= 10206 ? 10206 : 10205, 0));
                                } else if (gameObject.getPosition().getX() == 1321 && gameObject.getPosition().getY() == 10205) {
                                    player.moveTo(new Position(player.getPosition().getX() == 1320 ? 1322 : 1320, player.getPosition().getY() >= 10206 ? 10206 : 10205, 0));
                                }
                                player.getClickDelay().reset();
                                break;
                            case 134515:
                                if (!player.getClickDelay().elapsed(2000) || player.getMovementQueue().isLockMovement()) {
                                    return;
                                }
                                int x = 0;
                                int y = 0;
                                if (position.getX() == 1269 && position.getY() == 10171) {
                                    y = player.getPosition().getY() < position.getY() ? 10175 : 10169;
                                    x = player.getPosition().getX();
                                } else {
                                    x = player.getPosition().getX() > 1310 ? 1310 : player.getPosition().getX() < 1307 ? 1310 : player.getPosition().getX();
                                    y = player.getPosition().getY() > 10252 ? 10252 : 10257;
                                }
                                player.moveTo(new Position(x, y, 0));
                                player.getClickDelay().reset();
                                break;
                        }


                        if(realGameObject != null) {

                            DoorResponse doorResponse = DoorManager.isDoorOperated(realGameObject, false);

                            if (doorResponse != DoorResponse.INVALID_DOOR) {
                                if(doorResponse == DoorResponse.STUCK_OPEN) {
                                    player.getPA().sendMessage("The door seems to be stuck.");
                                    return;
                                }
                                DoorManager.doorUsed(player);
                                return;
                            }
                        }
                    }
                }));
    }

    private static void secondClick(final Player player, Packet packet) {
        final int id = packet.readInt();
        final int y = packet.readLEShort();
        final int x = packet.readUnsignedShortA();

        final Position position = new Position(x, y, player.getPosition().getZ());
        final GameObject gameObject = new GameObject(id, position);
        final RegionClipping region = RegionClipping.forPosition(position);
        if (!RegionClipping.objectExists(gameObject)) {
           player.getPacketSender().sendMessage("An error occured. Error code: "+id).sendMessage("Please report the error to a staff member.");
            return;
       }
        if(player.getTutorialStage() != TutorialStages.COMPLETED) {
            return;
        }

        Debug.write(player.getName(), "ObjectActionPacketListener - secondClick", new String[] {
                "objectId: "+id,
                "x: "+x,
                "y: "+y,
        });

        player.setPositionToFace(gameObject.getPosition());

        int sizeX = 1;
        int sizeY = 1;

        if (gameObject.getDefinition() != null) {
            sizeX = gameObject.getDefinition().getSizeX();
            sizeY = gameObject.getDefinition().getSizeY();
        }

        player.setInteractingObject(gameObject)
                .setWalkToTask(new WalkToTask(player, position, sizeX, sizeY, new FinalizedMovementTask() {
                    public void execute() {
                        GameObject realGameObject = CustomObjects.getGameObject(position, 10, 0);

                        if(realGameObject != null) {
                            realGameObject.clickObject(player, GameObjectClickType.SECOND_CLICK);
                        }
                        if (Prospecting.prospectOre(player,
                                MiningData.forRock(gameObject.getId()))) {
                            return;
                        }
                        if (CastleWarsManager.handleObjects(player, id, x, y, 2)) {
                            return;
                        }
                        if (Farming.isGameObject(player, gameObject, 2))
                            return;
                        switch (gameObject.getId()) {
                            case 6910:
                            case 4483:
                            case 3193:
                            case 2213:
                            case 11758:
                            case 14367:
                            case 42192:
                            case 75:
                            case 130087:
                            case 103194:
                            case 106943:
                            case 110083:
                            case 127260:
                                player.getPresets().open();
                                break;
                            case 884:
                                player.setDialogueActionId(41);
                                player.setInputHandling(new DonateToWell());
                                player.getPacketSender().sendInterfaceRemoval()
                                        .sendEnterAmountPrompt("How much money would you like to contribute with?");
                                break;
                            case 2646:
                            case 312:
                                if (!player.getClickDelay().elapsed(1200))
                                    return;
                                if (player.getInventory().isFull()) {
                                    player.getPacketSender().sendMessage("You don't have enough free inventory space.");
                                    return;
                                }
                                String type = gameObject.getId() == 312 ? "Potato" : "Flax";
                                player.performAnimation(new Animation(827));
                                if (Location.inResource(player)) {
                                    player.getInventory().add(gameObject.getId() == 312 ? 1943 : 1780, 1, "Pickables");

                                } else {
                                    player.getInventory().add(gameObject.getId() == 312 ? 1942 : 1779, 1, "Pickables");

                                }
                                player.getPacketSender().sendMessage("You pick some " + type + "..");
                                gameObject.setPickAmount(gameObject.getPickAmount() + 1);
                                if (RandomUtility.getRandom(3) == 1 && gameObject.getPickAmount() >= 1
                                        || gameObject.getPickAmount() >= 6) {
                                    player.getPacketSender().sendClientRightClickRemoval();
                                    gameObject.setPickAmount(0);
                                    CustomObjects.globalObjectRespawnTask(new GameObject(-1, gameObject.getPosition()),
                                            gameObject, 10);
                                }
                                player.getClickDelay().reset();
                                break;

                            case 110060:
                                AuctionHouseManager.open(player);
                                break;

                            case 111338:
                                player.getBank(player.getCurrentBankTab()).open();
                                break;

                            case 28716:
                                if (player.getSkillManager().getCurrentLevel(Skill.SUMMONING) == player.getSkillManager()
                                        .getMaxLevel(Skill.SUMMONING)) {
                                    player.getPacketSender()
                                            .sendMessage("You do not need to recharge your Summoning points right now.");
                                    return;
                                }
                                player.performGraphic(new Graphic(1517));
                                player.getSkillManager().setCurrentLevel(Skill.SUMMONING,
                                        player.getSkillManager().getMaxLevel(Skill.SUMMONING), true);
                                player.getPacketSender().sendString(18045,
                                        " " + player.getSkillManager().getCurrentLevel(Skill.SUMMONING) + "/"
                                                + player.getSkillManager().getMaxLevel(Skill.SUMMONING));
                                player.getPacketSender().sendMessage("You recharge your Summoning points.");
                                break;

                            case 104309:
                            case 2644:
                                Flax.showSpinInterface(player);
                                break;
                            case 100_006:
                                DwarfCannon cannon = player.getCannon();
                                if (cannon == null || cannon.getOwnerIndex() != player.getIndex()) {
                                    player.getPacketSender().sendMessage("This is not your cannon!");
                                } else {
                                    DwarfMultiCannon.pickupCannon(player, cannon, false);
                                }
                                break;
                            case 4875:
                                Stalls.stealFromStall(player, 1, 5100, 18199, "You steal a banana.");
                                break;
                            case 4874:
                                Stalls.stealFromStall(player, 30, 6130, 15009, "You steal a golden ring.");
                                break;
                            case 4876:
                                Stalls.stealFromStall(player, 60, 7370, 17401, "You steal a damaged hammer.");
                                break;
                            case 4877:
                                Stalls.stealFromStall(player, 65, 7990, 1389, "You steal a staff.");
                                break;
                            case 4878:
                                Stalls.stealFromStall(player, 80, 9230, 11998, "You steal a scimitar.");
                                break;
                            case 6189:

                            case 26814:
                            case 11666:
                            case 26300:
                            case 102030:
                            case 124009:
                                Smelting.openInterface(player);
                                break;
                            case 2152:
                                player.performAnimation(new Animation(8502));
                                player.performGraphic(new Graphic(1308));
                                player.getSkillManager().setCurrentLevel(Skill.SUMMONING,
                                        player.getSkillManager().getMaxLevel(Skill.SUMMONING));
                                player.getPacketSender().sendMessage("You renew your Summoning points.");
                                break;
                        }
                    }
                }));
    }

    private static void thirdClick(Player player, Packet packet) {
        final int x = packet.readShort();
        final int y = packet.readShort();
        final int id = packet.readInt();

        final Position position = new Position(x, y, player.getPosition().getZ());
        final GameObject gameObject = new GameObject(id, position);
        if (!Construction.buildingHouse(player)) {
           if (!RegionClipping.objectExists(gameObject)) {
                player.getPacketSender().sendMessage("An error occured. Error code: "+id).sendMessage("Please report the error to a staff member.");
               return;
           }
        }

        if(player.getTutorialStage() != TutorialStages.COMPLETED) {
            return;
        }

        Debug.write(player.getName(), "ObjectActionPacketListener - thirdClick", new String[] {
                "objectId: "+id,
                "x: "+x,
                "y: "+y,
        });

        player.setPositionToFace(gameObject.getPosition());

        int sizeX = 1;
        int sizeY = 1;

        if (gameObject.getDefinition() != null) {
            sizeX = gameObject.getDefinition().getSizeX();
            sizeY = gameObject.getDefinition().getSizeY();
        }

        player.setInteractingObject(gameObject);
        player.setWalkToTask(new WalkToTask(player, position, sizeX, sizeY, new FinalizedMovementTask() {
            @Override
            public void execute() {
                GameObject realGameObject = CustomObjects.getGameObject(position);

                if(realGameObject != null) {
                    realGameObject.clickObject(player, GameObjectClickType.THIRD_CLICK);
                }
                if (Farming.isGameObject(player, gameObject, 3))
                    return;
                switch (id) {
                    case 884:
                        World.getWell().displayStatus(player);
                        break;

                    case 110060:
                    case 111338:
                        AuctionHouseManager.pickupCollections(player);
                        break;
                }
            }
        }));
    }

    private static void fourthClick(Player player, Packet packet) {
    }

    private static void fifthClick(final Player player, Packet packet) {
        final int id = packet.readInt();
        final int y = packet.readUnsignedShortA();
        final int x = packet.readShort();

        final Position position = new Position(x, y, player.getPosition().getZ());
        final GameObject gameObject = new GameObject(id, position);
        if (!Construction.buildingHouse(player)) {
            if (!RegionClipping.objectExists(gameObject)) {
                player.getPacketSender().sendMessage("An error occured. Error code:  "+id).sendMessage("Please report the error to a staff member.");
                return;
            }
        }
        if(player.getTutorialStage() != TutorialStages.COMPLETED) {
            return;
        }
        player.setPositionToFace(gameObject.getPosition());

        Debug.write(player.getName(), "ObjectActionPacketListener - fifthClick", new String[] {
                "objectId: "+id,
                "x: "+x,
                "y: "+y,
        });

        int sizeX = 1;
        int sizeY = 1;

        if (gameObject.getDefinition() != null) {
            sizeX = gameObject.getDefinition().getSizeX();
            sizeY = gameObject.getDefinition().getSizeY();
        }

        player.setInteractingObject(gameObject);
        player.setWalkToTask(new WalkToTask(player, position, sizeX, sizeY, new FinalizedMovementTask() {
            @Override
            public void execute() {
                if (Farming.isGameObject(player, gameObject, 5))
                    return;
                switch (id) {
                }
                Construction.handleFifthObjectClick(x, y, id, player);
            }
        }));
    }

    @Override
    public void handleMessage(Player player, Packet packet) {
        if (player.isTeleporting() || player.isPlayerLocked() || player.getMovementQueue().isLockMovement()
                || player.requiresUnlocking())
            return;

        switch (packet.getOpcode()) {
            case FIRST_CLICK:
                firstClick(player, packet);
                break;
            case SECOND_CLICK:
                secondClick(player, packet);
                break;
            case THIRD_CLICK:
                thirdClick(player, packet);
                break;
            case FOURTH_CLICK:
                // fourthClick(player, packet);
                break;
            case FIFTH_CLICK:
                fifthClick(player, packet);
                break;
        }
    }

    public static final int FIRST_CLICK = 132, SECOND_CLICK = 252, THIRD_CLICK = 70, FOURTH_CLICK = 234,
            FIFTH_CLICK = 228;
}
