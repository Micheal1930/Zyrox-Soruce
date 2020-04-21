package com.varrock.net.packet.impl;

import com.varrock.engine.task.impl.WalkToTask;
import com.varrock.engine.task.impl.WalkToTask.FinalizedMovementTask;
import com.varrock.model.*;
import com.varrock.model.Locations.Location;
import com.varrock.model.container.impl.Bank;
import com.varrock.model.definitions.GameObjectDefinition;
import com.varrock.model.definitions.ItemDefinition;
import com.varrock.model.item.CrawsBow;
import com.varrock.model.item.ItemCombination;
import com.varrock.net.packet.Packet;
import com.varrock.net.packet.PacketListener;
import com.varrock.util.Misc;
import com.varrock.world.World;
import com.varrock.world.clip.region.RegionClipping;
import com.varrock.world.content.*;
import com.varrock.world.content.dialogue.DialogueManager;
import com.varrock.world.content.minigames.impl.WarriorsGuild;
import com.varrock.world.content.minigames.impl.castlewars.CastleWarsManager;
import com.varrock.world.content.minigames.impl.castlewars.CastleWarsMinigame;
import com.varrock.world.content.minigames.impl.castlewars.item.CastleWarsBandage;
import com.varrock.world.content.minigames.impl.castlewars.item.CastleWarsBarricade;
import com.varrock.world.content.minigames.impl.castlewars.object.catapult.CastleWarsCatapultManager;
import com.varrock.world.content.minigames.impl.castlewars.object.rocks.CastleWarsCollapsingRockManager;
import com.varrock.world.content.platinum_tokens.PlatinumTokenConversionManager;
import com.varrock.world.content.skill.impl.cooking.Cooking;
import com.varrock.world.content.skill.impl.cooking.CookingData;
import com.varrock.world.content.skill.impl.crafting.BattleStaffs;
import com.varrock.world.content.skill.impl.crafting.Gems;
import com.varrock.world.content.skill.impl.crafting.LeatherMaking;
import com.varrock.world.content.skill.impl.crafting.jewellery.JewelleryMaking;
import com.varrock.world.content.skill.impl.farming.Farming;
import com.varrock.world.content.skill.impl.farming.seed.*;
import com.varrock.world.content.skill.impl.firemaking.Firemaking;
import com.varrock.world.content.skill.impl.fletching.Fletching;
import com.varrock.world.content.skill.impl.herblore.Herblore;
import com.varrock.world.content.skill.impl.herblore.PotionCombinating;
import com.varrock.world.content.skill.impl.herblore.WeaponPoison;
import com.varrock.world.content.skill.impl.prayer.BonesOnAltar;
import com.varrock.world.content.skill.impl.prayer.Prayer;
import com.varrock.world.content.skill.impl.slayer.SlayerDialogues;
import com.varrock.world.content.skill.impl.slayer.SlayerTasks;
import com.varrock.world.content.skill.impl.smithing.EquipmentMaking;
import com.varrock.world.entity.impl.npc.NPC;
import com.varrock.world.entity.impl.player.Player;

/**
 * This packet listener is called when a player 'uses' an item on another
 * entity.
 *
 * @author relex lawl
 */

public class UseItemPacketListener implements PacketListener {

    /**
     * The PacketListener logger to debug information and print out errors.
     */
    // private final static Logger logger =
    // Logger.getLogger(UseItemPacketListener.class);
    private static void useItem(Player player, Packet packet) {
        if (player.isTeleporting() || player.getConstitution() <= 0)
            return;
        int interfaceId = packet.readLEShortA();
        int slot = packet.readShortA();
        int id = packet.readLEShort();
    }

    private static void itemOnItem(Player player, Packet packet) {
        int usedWithSlot = packet.readUnsignedShort();
        int itemUsedSlot = packet.readUnsignedShortA();
        if (usedWithSlot < 0 || itemUsedSlot < 0 || itemUsedSlot > player.getInventory().capacity()
                || usedWithSlot > player.getInventory().capacity())
            return;

        Item usedWith = player.getInventory().getItems()[usedWithSlot];
        Item itemUsedWith = player.getInventory().getItems()[itemUsedSlot];

        Debug.write(player.getName(), "ItemOnItemPacketListener - itemOnItem", new String[]{
                "usedWith: " + usedWith.getId(),
                "itemUsedWith: " + itemUsedWith.getId(),
        });

        if (usedWith.getId() == 6573 || itemUsedWith.getId() == 6573) {
            player.getPacketSender().sendMessage("To make an Amulet of Fury, you need to put an onyx in a furnace.");
            return;
        }

        if (ItemCombination.handleItemOnItem(player, usedWith, itemUsedWith)) {
            return;
        }
        if (ItemBuilding.build(player, usedWith.getId(), itemUsedWith.getId())) {
            return;
        }
        if (usedWith.getId() == CrawsBow.CRAWS_BOW || itemUsedWith.getId() == CrawsBow.CRAWS_BOW) {
            CrawsBow.load(player, usedWith, itemUsedWith);
            return;
        }

        if (usedWith.getId() == 12926) {
            player.getBlowpipeLoading().handleLoadBlowpipe(itemUsedWith);
            return;
        }
        if (itemUsedWith.getId() == 12926) {
            player.getBlowpipeLoading().handleLoadBlowpipe(usedWith);
            return;
        }
        if (usedWith.getId() == 12927) {
            player.getBlowpipeLoading().handleLoadBlowpipe(itemUsedWith);
            return;
        }
        if (itemUsedWith.getId() == 12927) {
            player.getBlowpipeLoading().handleLoadBlowpipe(usedWith);
            return;
        }
        if (usedWith.getId() == 3065) {
            player.getBlowpipeLoading().handleLoadBlowpipe(itemUsedWith);
            return;
        }
        if (itemUsedWith.getId() == 3065) {
            player.getBlowpipeLoading().handleLoadBlowpipe(usedWith);
            return;
        }
        if (itemUsedWith.getId() == 1759) {
            if ((usedWith.getDefinition().getName().contains("amulet")
                    || usedWith.getDefinition().getName().contains("Dragonstone ammy"))
                    && usedWith.getDefinition().getEquipmentType() != ItemDefinition.EquipmentType.AMULET && usedWith.getId() != 49541
                    && usedWith.getId() != 1702) {
                int id = (usedWith.getId() == 6579 ? 6581 : usedWith.getId() == 49501 ? 49541 : usedWith.getId() + 19);
                player.getInventory().delete(usedWith.getId(), 1);
                player.getInventory().delete(1759, 1);
                player.getInventory().add(id, 1);
                return;
            }
        }
        if (usedWith.getId() == 1759) {
            if ((itemUsedWith.getDefinition().getName().contains("amulet")
                    || itemUsedWith.getDefinition().getName().contains("Dragonstone ammy"))
                    && usedWith.getDefinition().getEquipmentType() != ItemDefinition.EquipmentType.AMULET && itemUsedWith.getId() != 49541
                    && itemUsedWith.getId() != 1702) {
                int id = (itemUsedWith.getId() == 6579 ? 6581
                        : itemUsedWith.getId() == 49501 ? 49541 : itemUsedWith.getId() + 19);
                player.getInventory().delete(itemUsedWith.getId(), 1);
                player.getInventory().delete(1759, 1);
                player.getInventory().add(id, 1);
                return;
            }
        }
        WeaponPoison.execute(player, itemUsedWith.getId(), usedWith.getId());
        if (itemUsedWith.getId() == 590 || usedWith.getId() == 590)
            Firemaking.lightFire(player, itemUsedWith.getId() == 590 ? usedWith.getId() : itemUsedWith.getId(), false,
                    1, false);
        if (itemUsedWith.getDefinition().getName().contains("(") && usedWith.getDefinition().getName().contains("("))
            PotionCombinating.startCombiningPotion(player, usedWith.getId(), itemUsedWith.getId());
        if (usedWith.getId() == Herblore.VIAL || itemUsedWith.getId() == Herblore.VIAL) {
            if (Herblore.startMakingUnfinishedPotion(player, usedWith.getId())
                    || Herblore.startMakingUnfinishedPotion(player, itemUsedWith.getId()))
                return;
        }
        if (Herblore.startMakingFinishedPotions(player, usedWith.getId(), itemUsedWith.getId())
                || Herblore.startMakingFinishedPotions(player, itemUsedWith.getId(), usedWith.getId()))
            return;
        if (usedWith.getId() == 946 || itemUsedWith.getId() == 946)
            Fletching.openSelection(player, usedWith.getId() == 946 ? itemUsedWith.getId() : usedWith.getId());
        if (usedWith.getId() == 1777 || itemUsedWith.getId() == 1777)
            Fletching.openBowStringSelection(player,
                    usedWith.getId() == 1777 ? itemUsedWith.getId() : usedWith.getId());
        if (usedWith.getId() == 53 || itemUsedWith.getId() == 53 || usedWith.getId() == 52
                || itemUsedWith.getId() == 52)
            Fletching.makeArrows(player, usedWith.getId(), itemUsedWith.getId());
        if (usedWith.getId() == 1391 || itemUsedWith.getId() == 1391)

            BattleStaffs.makeBattleStaff(player, usedWith.getId(), itemUsedWith.getId());

        if (usedWith.getId() == 13263 || itemUsedWith.getId() == 13263)
            ColoredSlayerHelmets.changeColor(player, usedWith.getId(), itemUsedWith.getId());

        if (itemUsedWith.getId() == 314 || usedWith.getId() == 314)
            Fletching.makeDarts(player, itemUsedWith.getId(), usedWith.getId());

        if (itemUsedWith.getId() == 1755 || usedWith.getId() == 1755) {
            if (itemUsedWith.getId() == 1611 || usedWith.getId() == 1611) {
                Fletching.makeTip(player, usedWith.getId(), itemUsedWith.getId());
                return;
            }
            if (itemUsedWith.getId() == 1613 || usedWith.getId() == 1613) {
                Fletching.makeTip(player, usedWith.getId(), itemUsedWith.getId());
                return;
            }
            if (itemUsedWith.getId() == 1607 || usedWith.getId() == 1607) {
                Fletching.makeTip(player, usedWith.getId(), itemUsedWith.getId());
                return;
            }
            if (itemUsedWith.getId() == 1605 || usedWith.getId() == 1605) {
                Fletching.makeTip(player, usedWith.getId(), itemUsedWith.getId());
                return;
            }
            if (itemUsedWith.getId() == 1603 || usedWith.getId() == 1603) {
                Fletching.makeTip(player, usedWith.getId(), itemUsedWith.getId());
                return;
            }
            if (itemUsedWith.getId() == 1601 || usedWith.getId() == 1601) {
                Fletching.makeTip(player, usedWith.getId(), itemUsedWith.getId());
                return;
            }
            if (itemUsedWith.getId() == 1615 || usedWith.getId() == 1615) {
                Fletching.makeTip(player, usedWith.getId(), itemUsedWith.getId());
                return;
            } else {
                if (itemUsedWith.getId() == 1755 || usedWith.getId() == 1755) {
                    Gems.selectionInterface(player, usedWith.getId() == 1755 ? itemUsedWith.getId() : usedWith.getId());
                }
            }
        }

        if (usedWith.getId() == 314 || itemUsedWith.getId() == 314)
            Fletching.makeBolts(player, usedWith.getId(), itemUsedWith.getId());
        if (itemUsedWith.getId() == 1755 || usedWith.getId() == 1755)
            Gems.selectionInterface(player, usedWith.getId() == 1755 ? itemUsedWith.getId() : usedWith.getId());
        if (usedWith.getId() == 1733 || itemUsedWith.getId() == 1733)
            LeatherMaking.craftLeatherDialogue(player, usedWith.getId(), itemUsedWith.getId());
        Herblore.startCreatingSpecialPotion(player, itemUsedWith.getId(), usedWith.getId());
        ItemForging.forgeItem(player, itemUsedWith.getId(), usedWith.getId());
        if (Farming.hasItemOnItemInteraction(player, new Item[]{itemUsedWith, usedWith})
                || Farming.hasItemOnItemInteraction(player, new Item[]{usedWith, itemUsedWith})) {

            return;
        }
        if (player.getRights() == PlayerRights.DEVELOPER)
            player.getPacketSender().sendMessage(
                    "ItemOnItem - [usedItem, usedWith] : [" + usedWith.getId() + ", " + itemUsedWith + "]");
    }

    @SuppressWarnings("unused")
    private static void itemOnObject(Player player, Packet packet) {
        @SuppressWarnings("unused")
        int interfaceType = packet.readShort();
        final int objectId = packet.readInt();
        final int objectY = packet.readLEShortA();
        final int itemSlot = packet.readLEShort();
        final int objectX = packet.readLEShortA();
        final int itemId = packet.readInt();

        if (itemSlot < 0 || itemSlot > player.getInventory().capacity())
            return;
        final Item item = player.getInventory().getItems()[itemSlot];
        if (item == null)
            return;
        final GameObject gameObject = new GameObject(objectId,
                new Position(objectX, objectY, player.getPosition().getZ()));
        if (!RegionClipping.objectExists(gameObject)) {
            player.getPacketSender().sendMessage("An error occured. Error code: ").sendMessage("Please report the error to a staff member.");
            return;
        }

        Debug.write(player.getName(), "ItemOnItemPacketListener - itemOnObject", new String[]{
                "itemId: " + itemId,
                "objectId: " + objectId,
        });

        player.setInteractingObject(gameObject);
        player.setWalkToTask(new WalkToTask(player, gameObject.getPosition().copy(), gameObject.getSize(),
                new FinalizedMovementTask() {
                    @Override
                    public void execute() {
                        if (Farming.hasItemInteraction(player, item, gameObject)) {
                            return;
                        }
                        if (itemId == CastleWarsManager.EXPLOSIVE_POTION.getId()) {
                            if (objectId == CastleWarsCollapsingRockManager.CAVE_WALL) {
                                CastleWarsCollapsingRockManager.collapse(player, false);
                                return;
                            } else if (objectId == CastleWarsCollapsingRockManager.FULL_ROCKS
                                    || objectId == CastleWarsCollapsingRockManager.HALF_ROCKS) {
                                CastleWarsCollapsingRockManager.breakRock(player, objectId, false);
                                return;
                            }
                        }
                        if (itemId == CastleWarsManager.TINDERBOX.getId() && (objectId == 4381 || objectId == 4382)) {
                            CastleWarsCatapultManager.setCatapultOnFire(player, objectId);
                            return;
                        }
                        if (itemId == CastleWarsMinigame.BUCKET_OF_WATER.getId() && (objectId == 4904 || objectId == 4905)) {
                            CastleWarsCatapultManager.extinguishFire(player, objectId);
                            return;
                        }
                        if (CookingData.forFish(item.getId()) != null && CookingData.isRange(objectId)) {
                            player.setPositionToFace(gameObject.getPosition());
                            Cooking.selectionInterface(player, CookingData.forFish(item.getId()));
                            return;
                        }

                        if (Prayer.isBone(itemId) && (objectId == 409 || objectId == 100409)) {
                            BonesOnAltar.openInterface(player, itemId);
                            return;
                        }

                        if (objectId == 15621) { // Warriors guild
                            // animator
                            if (!WarriorsGuild.itemOnAnimator(player, item, gameObject))
                                player.getPacketSender().sendMessage("Nothing interesting happens..");
                            return;
                        }
                        if (player.getGameMode() == GameMode.ULTIMATE_IRONMAN) {
                            if (GameObjectDefinition.getDefinition(objectId) != null) {
                                GameObjectDefinition def = GameObjectDefinition.getDefinition(objectId);
                                if (def.name != null && def.name.toLowerCase().contains("bank") && def.actions != null
                                        && def.actions[0] != null && def.actions[0].toLowerCase().contains("use")
                                        || objectId == 75) {

                                    ItemDefinition def1 = ItemDefinition.forId(itemId);
                                    ItemDefinition def2;
                                    int newId = def1.isNoted() ? itemId - 1 : itemId + 1;
                                    def2 = ItemDefinition.forId(newId);
                                    if (def2 != null && def1.getName().equals(def2.getName())) {
                                        int amt = player.getInventory().getAmount(itemId);
                                        if (!def2.isNoted()) {
                                            if (amt > player.getInventory().getFreeSlots())
                                                amt = player.getInventory().getFreeSlots();
                                        }
                                        if (amt == 0) {
                                            player.getPacketSender().sendMessage(
                                                    "You do not have enough space in your inventory to do that.");
                                            return;
                                        }
                                        player.getInventory().delete(itemId, amt).add(newId, amt, "Bank item noting");

                                    } else {
                                        player.getPacketSender().sendMessage("You cannot do this with that item.");
                                    }
                                    return;
                                }
                            }
                        }

                        switch (objectId) {
                            case 6910:
                            case 4483:
                            case 103194:
                            case 106943:
                            case 3193:
                            case 2213:
                            case 11758:
                            case 14367:
                            case 42192:
                            case 30087:
                            case 110083:
                            case 127260:
                            case 75:
                                if (PlatinumTokenConversionManager.onBank(player, itemId))
                                    return;

                                Bank.noteAll(player, itemId);
                                break;

                            case 6189:

                            case 26814:
                            case 11666:
                            case 26300:
                            case 102030:
                            case 124009:
                                if (itemId == 2357) {
                                    JewelleryMaking.selectionInterface(player);
                                    break;
                                }
                                break;
                            case 7836:
                            case 7808:
                            case 7839:
                                if (itemId == 6055) {
                                    int amt = player.getInventory().getAmount(6055);
                                    if (amt > 0) {
                                        player.getInventory().delete(6055, amt);
                                        player.getPacketSender().sendMessage("You put the weed in the compost bin.");
                                        player.getSkillManager().addExperience(Skill.FARMING, 20 * amt);
                                    }
                                }
                                break;
                            case 4306:
                            case 2097:
                            case 102031:
                            case 102672:

                                EquipmentMaking.handleAnvil(player);
                                // player.getPacketSender().sendMessage("Temporarily
                                // Disabled");

                                break;
                        }
                    }
                }));
    }

    private static void itemOnNpc(final Player player, Packet packet) {
        int itemId = packet.readUnsignedShortA();
        int index = packet.readUnsignedShortA();
        // OK I WILL BE FURIOUS IF THIS DOESNT WORK, R U SURE? no this is what
        // i had first time HM !
        final int slot = packet.readLEShort();
        int lastItemSelectedInterface = packet.readUnsignedShortA();

        if (slot < 0 || slot > 28) {
            return;
        }

        NPC npc = World.getNpcs().get(index);
        if (npc == null) {
            player.getPacketSender().sendMessage("Invalid use item on NPC " + slot);
            return;
        }

        Item usedItem = player.getInventory().forSlot(slot);

        Debug.write(player.getName(), "ItemOnItemPacketListener - itemOnNpc", new String[]{
                "itemId: " + itemId,
                "npc: " + npc.getId(),
        });

        switch (npc.getDefinition().getId()) {
            case CastleWarsBarricade.BARRICADE:
                if (itemId == CastleWarsManager.EXPLOSIVE_POTION.getId()) {
                    CastleWarsBarricade.explodeBarricade(player, npc);
                    break;
                } else if (itemId == CastleWarsManager.TINDERBOX.getId()) {
                    CastleWarsBarricade.setBarricadeOnFire(player, npc);
                    break;
                }
                break;
            case 367:
                GamblingAction.handleGambleItem(player, slot, usedItem);
                break;

            case 1360:
            case 494:
            case 5384:
                if (player.getGameMode() == GameMode.ULTIMATE_IRONMAN && Locations.goodDistance(player.getPosition(), npc.getPosition(), 3)) {
                    ItemDefinition def1 = ItemDefinition.forId(itemId);
                    ItemDefinition def2;
                    int newId = def1.isNoted() ? itemId - 1 : itemId + 1;
                    def2 = ItemDefinition.forId(newId);
                    if (def2 != null && def1.getName().equals(def2.getName())) {
                        int amt = player.getInventory().getAmount(itemId);
                        if (!def2.isNoted()) {
                            if (amt > player.getInventory().getFreeSlots())
                                amt = player.getInventory().getFreeSlots();
                        }
                        if (amt == 0) {
                            player.getPacketSender()
                                    .sendMessage("You do not have enough space in your inventory to do that.");
                            return;
                        }
                        player.getInventory().delete(itemId, amt).add(newId, amt, "Ironman item noting");

                    } else {
                        player.getPacketSender().sendMessage("You cannot do this with that item.");
                    }
                    return;
                }

                break;
        }
    }

    @SuppressWarnings("unused")
    private static void itemOnPlayer(Player player, Packet packet) {
        int interfaceId = packet.readUnsignedShortA();
        int targetIndex = packet.readUnsignedShort();
        int itemId = packet.readUnsignedShort();
        int slot = packet.readLEShort();
        if (slot < 0 || slot > player.getInventory().capacity() || targetIndex > World.getPlayers().capacity())
            return;
        Player target = World.getPlayers().get(targetIndex);
        if (target == null)
            return;

        Debug.write(player.getName(), "ItemOnItemPacketListener - itemOnPlayer", new String[]{
                "interfaceId: " + interfaceId,
                "itemId: " + itemId,
        });

        switch (itemId) {
            case 4049:
                CastleWarsBandage.useOnPlayer(player, target);
                break;
            case 962:
                if (!player.getInventory().contains(962))
                    return;
                if (player.getInventory().getFreeSlots() < 2) {
                    player.getPacketSender().sendMessage("You need more space to do that.");
                    return;
                }
                player.setPositionToFace(target.getPosition());
                player.performGraphic(new Graphic(1006));
                player.performAnimation(new Animation(451));
                player.getPacketSender().sendMessage("You pull the Christmas cracker...");
                target.getPacketSender().sendMessage("" + player.getUsername() + " pulls a Christmas cracker on you..");
                player.getInventory().delete(962, 1);
                target.getPacketSender().sendMessage("" + player.getUsername() + " has received a Party hat!");
                if (Misc.getRandom(5) == 2) {
                    player.getInventory().add(ChristmasCracker.customs[Misc.getRandom(ChristmasCracker.customs.length - 1)],
                            1, "Christmas cracker");
                    player.getPacketSender().sendMessage("Wow, Lucky! A custom rare partyhat.");
                } else {
                    player.getInventory().add(ChristmasCracker.regular[Misc.getRandom(ChristmasCracker.regular.length - 1)],
                            1, "Christmas cracker");
                    player.getPacketSender().sendMessage("A regular partyhat!");

                }

                break;
            case 4155:
                if (player.getSlayer().getDuoPartner() != null) {
                    player.getPacketSender().sendMessage("You already have a duo partner.");
                    return;
                }
                if (player.getSlayer().getSlayerTask() != SlayerTasks.NO_TASK) {
                    player.getPacketSender().sendMessage("You already have a Slayer task. You must reset it first.");
                    return;
                }
                Player duoPartner = World.getPlayers().get(targetIndex);
                if (duoPartner != null) {
                    if (duoPartner.getSlayer().getDuoPartner() != null) {
                        player.getPacketSender().sendMessage("This player already has a duo partner.");
                        return;
                    }
                    if (duoPartner.getSlayer().getSlayerTask() != SlayerTasks.NO_TASK) {
                        player.getPacketSender().sendMessage("This player already has a Slayer task.");
                        return;
                    }
                    if (duoPartner.getSlayer().getSlayerMaster() != player.getSlayer().getSlayerMaster()) {
                        player.getPacketSender().sendMessage("You do not have the same Slayer master as that player.");
                        return;
                    }
                    if (duoPartner.busy() || duoPartner.getLocation() == Location.WILDERNESS) {
                        player.getPacketSender().sendMessage("This player is currently busy.");
                        return;
                    }
                    DialogueManager.start(duoPartner, SlayerDialogues.inviteDuo(duoPartner, player));
                    player.getPacketSender()
                            .sendMessage("You have invited " + duoPartner.getUsername() + " to join your Slayer duo team.");
                }
                break;
        }
    }

    @Override
    public void handleMessage(Player player, Packet packet) {
        if (player.getConstitution() <= 0)
            return;
        switch (packet.getOpcode()) {
            case ITEM_ON_ITEM:
                itemOnItem(player, packet);
                break;
            case USE_ITEM:
                useItem(player, packet);
                break;
            case ITEM_ON_OBJECT:
                itemOnObject(player, packet);
                break;
            case ITEM_ON_GROUND_ITEM:
                // TODO
                break;
            case ITEM_ON_NPC:
                itemOnNpc(player, packet);
                break;
            case ITEM_ON_PLAYER:
                itemOnPlayer(player, packet);
                break;
        }
    }

    public static Item getNotedHarvest(Player player, Item harvest) {
        ItemDefinition def = ItemDefinition.forId(harvest.getDefinition().getNotedId());

        final int notedId = harvest.getDefinition().getNotedId();


        if (!def.getName().equalsIgnoreCase(harvest.getDefinition().getName())) {
            return null;
        }
        if (notedId <= 0)
            return null;
        for (SeedType seedType : AllotmentSeedType.values()) {
            if (harvest.getId() == seedType.getRewards()[0].getId()) {
                return new Item(notedId);
            }
        }
        for (SeedType seedType : HerbSeedType.values()) {
            if (harvest.getId() == seedType.getRewards()[0].getId()) {
                return new Item(notedId);
            }
        }
        for (SeedType seedType : FlowerSeedType.values()) {
            if (harvest.getId() == seedType.getRewards()[0].getId()) {
                return new Item(notedId);
            }
        }
        if (harvest.getId() == MushroomSeedType.BITTERCAP.getRewards()[0].getId()) {
            return new Item(notedId);
        }
        return null;
    }

    public final static int USE_ITEM = 122;

    public final static int ITEM_ON_NPC = 57;

    public final static int ITEM_ON_ITEM = 53;

    public final static int ITEM_ON_OBJECT = 192;

    public final static int ITEM_ON_GROUND_ITEM = 25;

    public static final int ITEM_ON_PLAYER = 14;
}
