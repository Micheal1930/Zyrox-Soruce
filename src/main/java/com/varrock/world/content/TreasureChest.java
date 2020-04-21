package com.varrock.world.content;

import com.varrock.engine.task.Task;
import com.varrock.engine.task.TaskManager;
import com.varrock.model.Animation;
import com.varrock.model.GameObject;
import com.varrock.model.Item;
import com.varrock.model.Position;
import com.varrock.model.definitions.NPCDrops;
import com.varrock.util.Misc;
import com.varrock.world.entity.impl.npc.NPC;
import com.varrock.world.entity.impl.player.Player;

/**
 * Handles the Treasure Chests at Treasure Island
 * Requires all 4 keys in order to open
 *
 * @Author Jonny/Aj
 */
public class TreasureChest {

    /**
     * Open the chest
     *
     * @param p
     * @param chest
     */
    public static void handleChest(final Player p, final GameObject chest) {
        int objectId = chest.getId();
        if (objectId != 10621 && objectId != 24204) {
            if (!p.getClickDelay().elapsed(3000))
                return;
        }
        if (!p.getInventory().contains(14678) || !p.getInventory().contains(18689) || !p.getInventory().contains(13758) || !p.getInventory().contains(13158)) {
            p.getPacketSender().sendMessage("You can only open this chest with 1 key from each boss. (Total of 4 keys)");
            return;
        }
        p.performAnimation(new Animation(827));
        p.getInventory().delete(14678, 1);
        p.getInventory().delete(18689, 1);
        p.getInventory().delete(13758, 1);
        p.getInventory().delete(13158, 1);
        p.getPacketSender().sendMessage("You open the treasure chest..");
        if (objectId == 10621 || objectId == 24204) {
            handleLoot(p);
        } else {
            TaskManager.submit(new Task(1, p, false) {
                int tick = 0;

                @Override
                public void execute() {
                    switch (tick) {
                        case 1:
                            if (objectId == 29577) {
                                CustomObjects.objectRespawnTask(p, new GameObject(29578, chest.getPosition().copy(), 10, 2), chest, 3);
                            } else if (objectId == 18804) {
                                CustomObjects.objectRespawnTask(p, new GameObject(18805, chest.getPosition().copy(), 10, 2), chest, 3);
                            }
                            break;
                        case 2:
                            handleLoot(p);
                            stop();
                            break;
                    }
                    tick++;
                }
            });
            p.getClickDelay().reset();
        }
    }


    public static void handleDrops(Player player, NPC npc, Position pos) {
        int key = 0;
        int npcId = npc.getId();
        if (npcId == 133) {
            key = 14678;
        }
        if (npcId == 135) {
            key = 13158;
        }
        if (npcId == 1472) {
            key = 13758;
        }
        if (npcId == 132) {
            key = 18689;
        }
        if (Misc.getRandom(2) == 1) {
            player.getPA().sendMessage("@blu@You have good luck and recieve a key!");
            NPCDrops.drop(player, new Item(key), npc, pos, false);
        } else {
            player.getPA().sendMessage("@red@You have bad luck and don't recieve a key...");
        }
    }

    public static void handleLoot(Player p) {
        /**
         * 2 total common rewards
         */
        for (int i = 0; i < 1; i++) {
            Item item = commonRewards[Misc.getRandom(commonRewards.length - 1)];
            Item item2 = alwaysRewards[Misc.getRandom(alwaysRewards.length - 1)];
            int amount2 = item2.getAmount();
            int amount = item.getAmount();

            //Randomize amount
            if (item.getAmount() >= 2) {
                amount = Misc.inclusiveRandom(amount / 2, amount);
            }
            if (item2.getAmount() >= 2) {
                amount = Misc.inclusiveRandom(amount / 2, amount);
            }

            p.getInventory().add(item.getId(), amount, "Treasure chest");
            p.getInventory().add(item2.getId(), amount2, "Treasure chest");


            p.getInventory().add(995, 100000 + Misc.getRandom(50000), "Treasure chest");
        }

        if (Misc.inclusiveRandom(1, 2) == 2) { // 1/5 chance
            p.getInventory().add(uncommonRewards[Misc.getRandom(uncommonRewards.length - 1)], "Treasure chest");
        }

        /**
         * Regular = 1/250
         */
        int chance = 55;
        if (Misc.inclusiveRandom(1, chance) == 2) {
            Item item = rareRewards[Misc.getRandom(rareRewards.length - 1)];
            p.getInventory().add(item, "Treasure chest loot");
            //sendAnnouncment(p.getUsername(), item);
        }

        p.getPacketSender().sendMessage("..and find some loot!");
    }

    /**
     * Checks to see if the item should get an announcement then processes if it can.
     * @param playerName
     * @param item
     */
    //public static void sendAnnouncment(String playerName, Item item) {
    //World.sendMessage("@blu@<img=10> " + playerName + " has received a " + item.getDefinition().getName() + " from Treasure Island.");
    //}

    /**
     * All common rewards
     */
    public static final Item[] commonRewards = {

            new Item(537, 25), //Dragon bones
            new Item(18831, 20), //Dragon bones
            new Item(18831, 10), //Frost dragon bones
            new Item(9244, 75), //Dragon bolts (e)
            new Item(9242, 75), //Ruby bolts (e)
            new Item(9243, 75), //Diamond bolts (e)
            new Item(1633, 2), //Uncut dragonstone
            new Item(1618, 12), //Uncut diamond
            new Item(1620, 10), //Uncut ruby
            new Item(1320, 10), //Rune 2h sword
            new Item(7158, 1), //Dragon 2h sword
            new Item(4154, 20), //Granite maul
            new Item(2, 500), //Cannonballs
            new Item(565, 500), //Blood runes
            new Item(560, 500), //Death runes
            new Item(9075, 500), //Astral runes
            new Item(14428, 25), //Saradomin brew flask (6)
            new Item(14416, 25), //Super restore flask (6)
            new Item(384, 50), //Raw shark
            new Item(15271, 25), //Raw rocktail
            new Item(1360, 10), //Rune hatchet
            new Item(1276, 10), //Rune pickaxe
            new Item(1334, 10), //Rune scimitar
            new Item(4588, 5), //Dragon scimitar
            new Item(2364, 15), //Rune bar
            new Item(15273, 30), //Rune bar
            new Item(2362, 25), //Adamant bar
            new Item(2360, 50), //Mithril bar
            new Item(987, 1), //half keys
            new Item(985, 1), //half keys
            new Item(989, 1), //half keys
            new Item(7956, 1), //half keys
            new Item(995, 5000000), //Coins
            new Item(995, 1000000),
            new Item(995, 2500000),
            new Item(995, 10000000),

    };
    public static final Item[] alwaysRewards = {

            new Item(206, 80),
            new Item(537, 50),
            new Item(220, 80),
            new Item(1514, 150),
            new Item(390, 90),
            new Item(384, 170),
            new Item(3052, 20),
            new Item(9143, 2000),
            new Item(202, 85),
            new Item(9144, 450),
            new Item(204, 180),
            new Item(1642, 180),
            new Item(9341, 160),
            new Item(206, 90),
            new Item(448, 85),
            new Item(454, 190),
            new Item(868, 430),
            new Item(11212, 140),
            new Item(452, 110),
            new Item(866, 565),
            new Item(1632, 5),
            new Item(1622, 7),


    };

    /**
     * All uncommon rewards
     */
    public static final Item[] uncommonRewards = {
            new Item(10025, 1), //Rune bar
            new Item(2615, 1), //Rune platebody (g)
            new Item(2623, 1), //Rune platebody (t)
            new Item(2617, 1), //Rune platelegs (g)
            new Item(2625, 1), //Rune platelegs (t)
            new Item(2619, 1), //Rune full helm (g)
            new Item(2627, 1), //Rune platelegs (t)
            new Item(2621, 1), //Rune kiteshield (g)
            new Item(2629, 1), //Rune kiteshield (t)
            new Item(2653, 1), //Zamorak platebody
            new Item(2655, 1), //Zamorak platelegs
            new Item(2657, 1), //Zamorak full helm
            new Item(2659, 1), //Zamorak kite
            new Item(2669, 1), //Guthix platebody
            new Item(2671, 1), //Guthix platelegs
            new Item(2673, 1), //Guthix full helm
            new Item(2675, 1), //Guthix kite
            new Item(2661, 1), //Saradomin plate
            new Item(2663, 1), //Saradomin legs
            new Item(2665, 1), //Saradomin full
            new Item(2667, 1), //Saradomin kite

            new Item(7319, 1), //Red boater
            new Item(7321, 1), //Orange boater
            new Item(7323, 1), //Green boater
            new Item(7325, 1), //Blue boater
            new Item(7327, 1), //Black boater
            new Item(10400, 1), //Black elegant shirt
            new Item(10402, 1), //Black elegant legs
            new Item(10404, 1), //Red elegant shirt
            new Item(10406, 1), //Red elegant legs
            new Item(10408, 1), //Blue elegant shirt
            new Item(10410, 1), //Blue elegant legs
            new Item(10412, 1), //Green elegant shirt
            new Item(10414, 1), //Green elegant legs
            new Item(10416, 1), //Purple elegant shirt
            new Item(10418, 1), //Purple elegant legs
            new Item(10420, 1), //White elegant shirt
            new Item(10422, 1), //White elegant legs
            new Item(19368, 1), //Armadyl cloak
            new Item(19370, 1), //Bandos cloak
            new Item(19372, 1), //Ancient cloak
            new Item(10446, 1), //Saradomin cloak
            new Item(10448, 1), //Guthix cloak
            new Item(10450, 1), //Zamorak cloak
            new Item(2579, 1), //Wizard boots

            new Item(19281, 1), //Green dragon mask
            new Item(19284, 1), //Blue dragon mask
            new Item(19287, 1), //Red dragon mask
            new Item(19290, 1), //Black dragon mask
            new Item(19293, 1), //Frost dragon mask
            new Item(19296, 1), //Bronze dragon mask
            new Item(19299, 1), //Iron dragon mask
            new Item(19302, 1), //Steel dragon mask
            new Item(19305, 1), //Mithril dragon mask
            new Item(13099, 1), //Rune cane
            new Item(10362, 1), //Amulet of glory (t)
            new Item(11716, 1), //Zamorakian spear
            new Item(2577, 1),
            new Item(2581, 1),
            new Item(6585, 1),
            new Item(19335, 1),
            new Item(4151, 1),
            new Item(2572, 1),
            new Item(6914, 1),
            new Item(6889, 1),
            new Item(6737, 1),
            new Item(6735, 1),
            new Item(6733, 1),
            new Item(2577, 1),
            new Item(2581, 1),
            new Item(6585, 1),
            new Item(19335, 1),
            new Item(4151, 1),
            new Item(2572, 1),
            new Item(6914, 1),
            new Item(6889, 1),
            new Item(6737, 1),
            new Item(2572, 1),
            new Item(6735, 1),
            new Item(6733, 1),
            new Item(6739, 1),
            new Item(18782, 1),
            new Item(11235, 1),
            new Item(15259, 1),
            new Item(6739, 1),
            new Item(15126, 1),
            new Item(6731, 1),
            new Item(2677, 1),
            new Item(2678, 1),
            new Item(2679, 1),
            new Item(2680, 1),
            new Item(2681, 1),
            new Item(2682, 1),
            new Item(2683, 1),
            new Item(2684, 1),
            new Item(2685, 1),
            new Item(2686, 1),
            new Item(2687, 1),
            new Item(2688, 1),
            new Item(2689, 1),
            new Item(2690, 1),


            new Item(995, 15000000),
            new Item(995, 10000000),
            new Item(995, 2000000), //Coins
            new Item(995, 2500000),
            new Item(995, 1200000),

            new Item(15220, 1),
            new Item(15018, 1),
            new Item(15019, 1),
            new Item(15020, 1),
            new Item(11283, 1),

            new Item(4716, 1),
            new Item(4718, 1),
            new Item(4720, 1),
            new Item(4722, 1),
            new Item(4753, 1),
            new Item(4755, 1),
            new Item(4757, 1),
            new Item(4759, 1),
            new Item(4732, 1),
            new Item(4734, 1),
            new Item(4736, 1),
            new Item(4738, 1),
            new Item(4724, 1),
            new Item(4726, 1),
            new Item(4728, 1),
            new Item(4730, 1),
            new Item(4708, 1),
            new Item(4710, 1),
            new Item(4712, 1),
            new Item(4714, 1),
            new Item(4745, 1),
            new Item(4747, 1),
            new Item(4749, 1),
            new Item(4751, 1),
            new Item(11732, 1),
            new Item(11732, 1),
            new Item(4716, 1),
            new Item(4718, 1),
            new Item(4720, 1),
            new Item(4722, 1),
            new Item(4753, 1),
            new Item(4755, 1),
            new Item(4757, 1),
            new Item(4759, 1),
            new Item(4732, 1),
            new Item(4734, 1),
            new Item(4736, 1),
            new Item(4738, 1),
            new Item(4724, 1),
            new Item(4726, 1),
            new Item(4728, 1),
            new Item(4730, 1),
            new Item(4708, 1),
            new Item(4710, 1),
            new Item(4712, 1),
            new Item(4714, 1),
            new Item(4745, 1),
            new Item(4747, 1),
            new Item(4749, 1),
            new Item(4751, 1),
            new Item(11732, 1),
            new Item(11732, 1),

    };

    /**
     * All rare rewards
     */
    public static final Item[] rareRewards = {

            new Item(20057, 1), //drygore
            new Item(20058, 1), //drygore
            new Item(20059, 1), //drygore



    };

}
