package com.zyrox.world.content.lootboxes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zyrox.GameServer;
import com.zyrox.engine.task.Task;
import com.zyrox.engine.task.TaskManager;
import com.zyrox.model.Graphic;
import com.zyrox.model.Item;
import com.zyrox.model.definitions.ItemDefinition;
import com.zyrox.model.item.Items;
import com.zyrox.model.log.impl.RareLootLog;
import com.zyrox.util.Misc;
import com.zyrox.world.World;
import com.zyrox.world.content.lootboxes.impl.*;
import com.zyrox.world.entity.impl.player.Player;

/**
 * A simple loot box interface.
 *
 * @author Gabriel || Wolfsdarker
 */
public interface LootBox {

    /**
     * The map of lootboxes.
     */
    Map<Integer, LootBox> boxes = new HashMap<>();

    /**
     * The amount of free slots required.
     *
     * @return the amount
     */
    int freeSlotsRequired();

    /**
     * The amount of rewards the box will give.
     *
     * @param player
     * @return the amount
     */
    int lootCount(Player player);

    /**
     * The chance to receive an uncommon item.
     *
     * @param player
     * @return the chance
     */
    double uncommonChance(Player player);

    /**
     * The chance to receive a rare item.
     *
     * @param player
     * @return the chance
     */
    double rareChance(Player player);

    /**
     * The chance to receive a super rare item.
     *
     * @param player
     * @return the chance
     */
    double superRareChance(Player player);

    /**
     * The chance to receive a ultimate item.
     *
     * @param player
     * @return the chance
     */
    double ultimateChance(Player player);

    /**
     * The list of common items inside the box.
     *
     * @return the items
     */
    List<Item> commonItems();

    /**
     * The list of uncommon items inside the box.
     *
     * @return the items
     */
    List<Item> uncommonItems();

    /**
     * The list of rare items inside the box.
     *
     * @return the items
     */
    List<Item> rareItems();

    /**
     * The list of super rare items inside the box.
     *
     * @return the items
     */
    List<Item> superRareItems();

    /**
     * The list of ultimate items inside the box.
     *
     * @return the items
     */
    List<Item> ultimateItems();

    /**
     * The list of columns our loot will appear in
     *
     * @return the columns
     */
    List<LootColumn> lootColumns();

    String boxName();

    /**
     * Loads all the boxes.
     */
    static void init() {
        boxes.put(Items.HALLOWEEN_BOX, new HalloweenBox());
        boxes.put(Items.MYSTERY_BOX, new MysteryBox());
        boxes.put(Items.SUPER_MYSTERY_BOX, new SuperMysteryBox());
        boxes.put(Items.ULTIMATE_MYSTERY_BOX, new UltimateMysteryBox());
        boxes.put(Items.SUPREME_MYSTERY_BOX, new SupremeMysteryBox());
    }

    static boolean isButton(Player player, int buttonId) {
        if(buttonId == 56016) {
            Item firstBox = getFirstBox(player);
            if(firstBox != null) {
                return open(player, firstBox, true);
            }
        }
        return false;
    }

    static Item getFirstBox(Player player) {
        for(Map.Entry entry : boxes.entrySet()) {
            int itemId = (int) entry.getKey();

            if(player.getInventory().contains(itemId)) {
                return new Item(itemId, 1);
            }
        }

        return null;
    }

    /**
     * Opens the loot box.
     *
     * @param player
     * @param box
     * @return if the item is a loot box
     */
    static boolean open(Player player, Item box, boolean spin) {

        LootBox lootBox = boxes.get(box.getId());
        String boxName = box.getDefinition().getName();

        if (lootBox == null || !player.getInventory().contains(box.getId())) {
            return false;
        }

        int boxId = box.copy().getId();

/*        if(!player.getBoxTimer().elapsed(6500)) {
            player.sendMessage("You must wait until your current box is finished being opened.");
            return false;
        }*/


        player.getBoxTimer().reset();

        player.getPacketSender().sendString(56019, lootBox.boxName());

        int[] chanceIds = new int[] {56020, 56032, 56033};
        int index = 0;

        double totalChance = 100;

        for(Integer chanceId : chanceIds) {
            if(lootBox.lootColumns().size() <= index) {
                break;
            }

            LootColumn lootColumn = lootBox.lootColumns().get(index);
            double chance = 100;

            switch(lootColumn) {
                case ULTIMATE:
                    chance = index == lootBox.lootColumns().size() - 1 ? totalChance : lootBox.ultimateChance(player) * 100;
                    player.getPacketSender().sendItemContainer(lootBox.ultimateItems(), index == 0 ? 56102 : index == 1 ? 56104 : 56106);
                    break;
                case SUPER_RARE:
                    chance = index == lootBox.lootColumns().size() - 1 ? totalChance : lootBox.superRareChance(player) * 100;
                    player.getPacketSender().sendItemContainer(lootBox.superRareItems(), index == 0 ? 56102 : index == 1 ? 56104 : 56106);
                    break;
                case RARE:
                    chance = index == lootBox.lootColumns().size() - 1 ? totalChance : lootBox.rareChance(player) * 100;
                    player.getPacketSender().sendItemContainer(lootBox.rareItems(), index == 0 ? 56102 : index == 1 ? 56104 : 56106);
                    break;
                case UNCOMMON:
                    chance = index == lootBox.lootColumns().size() - 1 ? totalChance : lootBox.uncommonChance(player) * 100;
                    player.getPacketSender().sendItemContainer(lootBox.uncommonItems(), index == 0 ? 56102 : index == 1 ? 56104 : 56106);
                    break;
                case COMMON:
                    chance = totalChance;
                    player.getPacketSender().sendItemContainer(lootBox.commonItems(), index == 0 ? 56102 : index == 1 ? 56104 : 56106);
                    break;
            }

            totalChance -= chance;

            player.getPacketSender().sendStringColour(chanceId, lootColumn.color);
            player.getPacketSender().sendString(chanceId, lootColumn.getName()+" - "+String.format("%.2f", chance)+"%");

            index++;
        }

        List<Item> lootTable;

        int lootCount = lootBox.lootCount(player);

        if (lootCount < 1) {
            return false;
        }

        if(!spin && player.getInterfaceId() != 56000) {
            player.getPacketSender().sendInterface(56000);
            return false;
        }

        if (player.getInventory().contains(box.getId())) {

            if(player.getCurrentTask() != null && player.getCurrentTask().isRunning()) {
                player.getCurrentTask().stop();
            }

            player.getInventory().delete(new Item(box.getId(), 1));

            boolean ultimatePulled = false;
            boolean superRarePulled = false;
            boolean rarePulled = false;

            String alert = ":shortalert:";

            int color = -1;

            double randomFloat = Misc.randomFloat();
            if ((!lootBox.ultimateItems().isEmpty() && randomFloat <= lootBox.ultimateChance(player)) || (lootBox.superRareItems().isEmpty() && !lootBox.ultimateItems().isEmpty())) {
                lootTable = lootBox.ultimateItems();
                ultimatePulled = true;
                color = 0xff0000;
            } else if ((!lootBox.superRareItems().isEmpty() && randomFloat <= lootBox.superRareChance(player)) || (lootBox.rareItems().isEmpty() && !lootBox.superRareItems().isEmpty())) {
                lootTable = lootBox.superRareItems();
                superRarePulled = true;

                if(box.getId() != Items.SUPREME_MYSTERY_BOX)
                    color = 0xadd8e6;

            } else if ((!lootBox.rareItems().isEmpty() && randomFloat <= lootBox.rareChance(player)) || (lootBox.uncommonItems().isEmpty() && !lootBox.rareItems().isEmpty())) {
                lootTable = lootBox.rareItems();
                rarePulled = true;

                if(box.getId() == Items.MYSTERY_BOX)
                    color = 0x00ff00;

                if(box.getId() == Items.HALLOWEEN_BOX) {
                    color = 0xffa500;
                    ultimatePulled = true;
                }

            } else if ((!lootBox.uncommonItems().isEmpty() && randomFloat <= lootBox.uncommonChance(player)) || (lootBox.commonItems().isEmpty() && !lootBox.uncommonItems().isEmpty())) {
                lootTable = lootBox.uncommonItems();
            } else {
                lootTable = lootBox.commonItems();
            }

            int randomIndex = Misc.random(lootTable.size() - 1);

            if (randomIndex >= lootTable.size()) {
                randomIndex = lootTable.size() - 1;
            }

            Item loot = lootTable.get(randomIndex);

            player.getInventory().add(loot, false);

            player.getPacketSender().sendBoxReward(boxId, loot.getId(), loot.getAmount(), color);
            player.getPacketSender().sendInterface(56000);

            boolean finalRarePulled = rarePulled;
            boolean finalUltimatePulled = ultimatePulled;
            boolean finalSuperRarePulled = superRarePulled;
            TaskManager.submit(new Task(9, player, false) {
                @Override
                public void execute() {
                    this.stop();
                }

                @Override
                public void stop() {
                    super.stop();
                    if (finalRarePulled || finalSuperRarePulled || finalUltimatePulled) {

                        if(finalSuperRarePulled) {
                            String text = player.getUsername() + " received a " + loot.getDefinition().getName() + " "
                                    + (loot.getAmount() > 1 ? ("x" + loot.getAmount() + " ") : "") + "from the " + boxName;
                            sendAnnouncement(Misc.getBoxIcon() + "<shad=996633>" + text);
                        }

                        if(finalUltimatePulled) {
                            sendAnnouncement(alert + " :n:" + player.getUsername()
                                    + " opened the " + boxName + "@whi@ to receive " + ItemDefinition.forId(loot.getId()).getName() + "! ");
                        }

                        GameServer.discordBot.sendGameLoot(player.getName(),
                                loot.getId(), loot.getAmount(),
                                "Type ::donate in-game to buy boxes",
                                boxName,
                                "https://varrock.io/images/32x32/" + box.getId() + ".png");

                        new RareLootLog(player.getName(), loot.getDefinition().getName(), loot.getId(), loot.getAmount(), boxName, Misc.getTime()).submit();
                    }

                    player.performGraphic(new Graphic(1944));

                    player.getInventory().refreshItems();
                }
            });

            return true;
        }

        return true;
    }

    static void sendAnnouncement(String announcement) {
        for (Player player : World.getPlayers()) {
            if (player != null) {
                if (player.boxAlertEnabled)
                    player.getPacketSender().sendMessage(announcement);
            }
        }

    }
}