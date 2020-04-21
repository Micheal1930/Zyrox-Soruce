package com.varrock.model.definitions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.varrock.GameServer;
import com.varrock.GameSettings;
import com.varrock.model.AccountValue;
import com.varrock.model.GameMode;
import com.varrock.model.Graphic;
import com.varrock.model.GraphicHeight;
import com.varrock.model.GroundItem;
import com.varrock.model.Item;
import com.varrock.model.PlayerRights;
import com.varrock.model.Position;
import com.varrock.model.Skill;
import com.varrock.model.Locations.Location;
import com.varrock.model.container.impl.Equipment;
import com.varrock.model.item.CrawsBow;
import com.varrock.model.item.RingOfBosses;
import com.varrock.model.log.impl.RareLootLog;
import com.varrock.util.JsonLoader;
import com.varrock.util.Misc;
import com.varrock.util.RandomUtility;
import com.varrock.world.World;
import com.varrock.world.content.DropLog;
import com.varrock.world.content.KillsTracker;
import com.varrock.world.content.WildernessDrops;
import com.varrock.world.content.DropLog.DropLogEntry;
import com.varrock.world.content.clan.ClanChatManager;
import com.varrock.world.content.minigames.impl.WarriorsGuild;
import com.varrock.world.content.skill.impl.prayer.BonesData;
import com.varrock.world.content.skill.impl.summoning.BossPets;
import com.varrock.world.content.skill.impl.summoning.CharmingImp;
import com.varrock.world.entity.impl.GroundItemManager;
import com.varrock.world.entity.impl.npc.NPC;
import com.varrock.world.entity.impl.player.Player;

/**
 * Controls the npc drops
 *
 * @author 2012 <http://www.rune-server.org/members/dexter+morgan/>, Gabbe &
 * Samy
 */
public class NPCDrops {

    public static boolean shouldOrb = true;
    public static boolean canDrop = true;

    public static boolean coinOrbed = false;

    /**
     * The monsters that won't activate the double drop perk.
     */
    public static final List<Integer> BLACK_LISTED_MONSTER = new ArrayList<>();

    static {
        // BLACK_LISTED_MONSTER.add(8133); //Corporeal beast
        BLACK_LISTED_MONSTER.add(23060); // Vorkath
        // BLACK_LISTED_MONSTER.add(8349); //Torment demon
        BLACK_LISTED_MONSTER.add(23612); // DRAKE
        BLACK_LISTED_MONSTER.add(22409); // night beast
        BLACK_LISTED_MONSTER.add(22405); // king kurask
        BLACK_LISTED_MONSTER.add(22795); // ancient wyvern
        BLACK_LISTED_MONSTER.add(2042); // zulrah
        BLACK_LISTED_MONSTER.add(2043); // zulrah
        BLACK_LISTED_MONSTER.add(2044); // zulrah
        BLACK_LISTED_MONSTER.add(23615); // zulrah
        BLACK_LISTED_MONSTER.add(23616); // zulrah
        BLACK_LISTED_MONSTER.add(23617); // zulrah
        BLACK_LISTED_MONSTER.add(23618); // zulrah
        BLACK_LISTED_MONSTER.add(23619); // zulrah
        BLACK_LISTED_MONSTER.add(23620); // zulrah
        BLACK_LISTED_MONSTER.add(23621); // Hydra final

    }

    /**
     * The map containing all the npc drops.
     */
    private static Map<Integer, NPCDrops> dropControllers = new HashMap<Integer, NPCDrops>();

    public static JsonLoader parseDrops() {

        ItemDropAnnouncer.init();

        return new JsonLoader() {

            @Override
            public void load(JsonObject reader, Gson builder) {
                int[] npcIds = builder.fromJson(reader.get("npcIds"), int[].class);
                NpcDropItem[] drops = builder.fromJson(reader.get("drops"), NpcDropItem[].class);

                NPCDrops d = new NPCDrops();
                d.npcIds = npcIds;
                d.drops = drops;
                for (int id : npcIds) {
                    dropControllers.put(id, d);
                }
            }

            @Override
            public String filePath() {
                return "./data/def/json/drops.json";
            }

            @Override
            public void completed() {
               // DropListDumper.dump();
            }
        };
    }

    public static void addCustom(int[] npcIds, NpcDropItem[] drops) {
        NPCDrops d = new NPCDrops();
        d.npcIds = npcIds;
        d.drops = drops;

        for (int id : npcIds) {
            dropControllers.put(id, d);
        }
    }

    /**
     * The id's of the NPC's that "owns" this class.
     */
    private int[] npcIds;

    /**
     * All the drops that belongs to this class.
     */
    private NpcDropItem[] drops;

    /**
     * Gets the NPC drop controller by an id.
     *
     * @return The NPC drops associated with this id.
     */
    public static NPCDrops forId(int id) {
        return dropControllers.get(id);
    }

    public static Map<Integer, NPCDrops> getDrops() {
        return dropControllers;
    }

    /**
     * Gets the drop list
     *
     * @return the list
     */
    public NpcDropItem[] getDropList() {
        return drops;
    }

    /**
     * Gets the npcIds
     *
     * @return the npcIds
     */
    public int[] getNpcIds() {
        return npcIds;
    }

    /**
     * Represents a npc drop item
     */
    public static class NpcDropItem {

        /**
         * The id.
         */
        private final int id;

        /**
         * Array holding all the amounts of this item.
         */
        private final int[] count;

        /**
         * The chance of getting this item.
         */
        private final int chance;

        /**
         * New npc drop item
         *
         * @param id the item
         * @param count the count
         * @param chance the chance
         */
        public NpcDropItem(int id, int[] count, int chance) {
            this.id = id;
            this.count = count;
            this.chance = chance;
        }

        /**
         * Gets the item id.
         *
         * @return The item id.
         */
        public int getId() {
            return id;
        }

        /**
         * Gets the chance.
         *
         * @return The chance.
         */
        public int[] getCount() {
            return count;
        }

        /**
         * Gets the chance.
         *
         * @return The chance.
         */
        public DropChance getChance() {
            switch (chance) {
                case 1:
                    return DropChance.ALMOST_ALWAYS; // 50% <-> 1/2
                case 2:
                    return DropChance.VERY_COMMON; // 20% <-> 1/5
                case 3:
                    return DropChance.COMMON; // 5% <-> 1/20
                case 4:
                    return DropChance.UNCOMMON; // 2% <-> 1/50
                case 5:
                    return DropChance.RARE; // 0.5% <-> 1/200
                case 6:
                    return DropChance.LEGENDARY; // 0.2% <-> 1/500
                case 7:
                    return DropChance.LEGENDARY_2;
                case 8:
                    return DropChance.LEGENDARY_3;
                case 9:
                    return DropChance.LEGENDARY_4;
                case 10:
                    return DropChance.LEGENDARY_5;
                default:
                    return DropChance.ALWAYS; // 100% <-> 1/1
            }
        }

        /**
         * Gets the item
         *
         * @return the item
         */
        public Item getItem() {
            int amount = 0;
            for (int i = 0; i < count.length; i++)
                amount += count[i];
            if (amount > count[0])
                amount = count[0] + RandomUtility.getRandom(count[1]);
            return new Item(id, amount);
        }
    }

    public enum DropChance {

        ALWAYS(0),
        ALMOST_ALWAYS(2),
        VERY_COMMON(5),
        COMMON(15),
        UNCOMMON(40),
        NOTTHATRARE(100),
        RARE(155),
        LEGENDARY(320),
        LEGENDARY_2(500),
        LEGENDARY_3(850),
        LEGENDARY_4(680),
        LEGENDARY_5(900);

        // so lets combine:
        // almost always (2) & very common (5)
        // ---so we got 7 slots on the npc drop table--
        // heres what the 7 slots
        // 1)always | 2) almost always and very common | 4) common | 5) uncommon | 6)
        // notthatrare
        // 7)rare | 8) legendary

        DropChance(int randomModifier) {
            this.random = randomModifier;
        }

        private int random;

        public int getRandom() {
            return this.random;
        }
    }

    /**
     * Drops items for a player after killing an npc. A player can max receive one
     * item per drop chance.
     *
     * @param p Player to receive drop.
     * @param npc NPC to receive drop FROM.
     */
    public static void dropItems(Player p, NPC npc) {
        if (npc.getLocation() == Location.WARRIORS_GUILD)
            WarriorsGuild.handleDrop(p, npc);
        NPCDrops drops = NPCDrops.forId(npc.getId());
        if (drops == null || npc.getId() == 22554) // Great Olm
            return;
        final boolean goGlobal = p.getPosition().getZ() >= 0 && p.getPosition().getZ() < 4;
        final boolean ringOfWealth = p.getEquipment().get(Equipment.RING_SLOT).getId() == 2572;
        final boolean ringOfBosses = RingOfBosses.isWearing(p);
        final boolean vorkathCape = npc.getId() == 23060 && p.getEquipment().get(Equipment.CAPE_SLOT).getId() == 21000;
        final boolean nexCape = npc.getId() == 13447 && p.getEquipment().get(Equipment.CAPE_SLOT).getId() == 21008;
        final boolean corpCape = npc.getId() == 8133 && p.getEquipment().get(Equipment.CAPE_SLOT).getId() == 21011;
        final boolean hydraCape = (npc.getId() == 23621 || npc.getId() == 23622) && p.getEquipment().get(Equipment.CAPE_SLOT).getId() == 21021;
        final Position npcPos = npc.getPosition().copy();
        boolean[] dropsReceived = new boolean[12];

        if (drops.getDropList().length > 0 && p.getPosition().getZ() >= 0 && p.getPosition().getZ() < 4) {

            if (npc.getId() == 3847 || npc.getId() == 2042 || npc.getId() == 2043 || npc.getId() == 2044) {
                casketDrop(p, npc.getDefinition().getCombatLevel(), p.getPosition());
            } else {
                casketDrop(p, npc.getDefinition().getCombatLevel(), npcPos);
            }
        }

        if (drops.getDropList().length > 0 && p.getPosition().getZ() >= 0 && p.getPosition().getZ() < 4) {
            if (npc.getId() == 3847 || npc.getId() == 2042 || npc.getId() == 2043 || npc.getId() == 2044) {
                clueDrop(p, npc.getDefinition().getCombatLevel(), p.getPosition());
            } else {
                clueDrop(p, npc.getDefinition().getCombatLevel(), npcPos);
            }

        }
        Item wildernessDrop = WildernessDrops.getDrop(p, npc.getId(), 1.0);
        if (wildernessDrop != null) {
            GroundItemManager.spawnGroundItem(p, new GroundItem(wildernessDrop, npcPos, p.getUsername(), false, 150, goGlobal, 200));
        }
        Item bloodMoney = WildernessDrops.getBloodMoneyDrop(p, npc.getId(), false);
        if (bloodMoney != null) {
            GroundItemManager.spawnGroundItem(p, new GroundItem(bloodMoney, npcPos, p.getUsername(), false, 150, goGlobal, 200));
        }

        for (int i = 0; i < drops.getDropList().length; i++) {
            if (drops.getDropList()[i] == null || drops.getDropList()[i].getItem() == null
                    || drops.getDropList()[i].getItem().getId() <= 0
                    || drops.getDropList()[i].getItem().getId() > ItemDefinition.getMaxAmountOfItems()
                    || drops.getDropList()[i].getItem().getAmount() <= 0) {
                continue;
            }

            DropChance dropChance = drops.getDropList()[i].getChance();
            boolean cow = npc.getId() == 81 && npc.getLocation() == Location.COWS;
            if (dropChance == DropChance.ALWAYS) {
                if (!cow) {
                    drop(p, drops.getDropList()[i].getItem(), npc, npcPos, goGlobal);
                }
            } else {
				if (shouldDrop(dropsReceived, npc.getId(), dropChance, ringOfWealth, ringOfBosses, vorkathCape, nexCape, corpCape, hydraCape,
						p.getGameMode() == GameMode.IRONMAN || p.getGameMode() == GameMode.ULTIMATE_IRONMAN || p.getGameMode() == GameMode.HARDCORE_IRONMAN, p)) {
                    drop(p, drops.getDropList()[i].getItem(), npc, npcPos, goGlobal);
                    dropsReceived[dropChance.ordinal()] = true;
                }
            }
        }

        if (npc.getDefinition().getName().toLowerCase().contains("revenant")) {
            double level = Math.sqrt(npc.getDefinition().getCombatLevel());
            int amountOfEther = 1 + Misc.random((int) Math.ceil(level));
            GroundItemManager.spawnGroundItem(p, new GroundItem(new Item(CrawsBow.REVENANT_ETHER, amountOfEther),
                    npc.getPosition(), p.getUsername(), false, 150, true, 200));
        }

        if (npc.getId() == 5886) {
            p.addBossPoints(1);
        }
    }

    public static boolean shouldDrop(boolean[] b, int npcId, DropChance chance, boolean ringOfWealth, boolean ringOfBosses,
                                     boolean vorkathCape, boolean nexCape, boolean corpCape, boolean hydraCape, boolean extreme, Player player) {

        int killRequirement = chance.getRandom();

        if (ringOfWealth && killRequirement >= 60)
            killRequirement -= (killRequirement / 10);

        if (ringOfBosses && killRequirement >= 60)
            killRequirement -= (killRequirement / 15);

/*        if ((nexCape || vorkathCape || corpCape || hydraCape) && killRequirement >= 60)
            killRequirement -= (killRequirement * 0.1);*/

        if(killRequirement >= 60) {
            if (player.getSummoning().getFamiliar() != null) {
                if (player.getSummoning().getFamiliar().getSummonNpc() != null) {
                    if (player.getSummoning().getFamiliar().getSummonNpc().getId() == BossPets.BossPet.PRE_RELEASE_PETE.getNpcID()) {
                        killRequirement -= (killRequirement * .05);
                    }
                    if (player.getSummoning().getFamiliar().getSummonNpc().getId() == BossPets.BossPet.PUMPKIN_PETE.getNpcID()) {
                        killRequirement -= (killRequirement * .05);
                    }
                }
            }
        }

        if(killRequirement >= 60) {
            killRequirement -= (killRequirement * (double)((double)player.prestige / 100));
        }

        if(killRequirement >= 60) {
            double boost = 0;
            switch(player.getRights()) {
                case DONATOR:
                    boost = .02;
                    break;
                case SUPER_DONATOR:
                    boost = .04;
                    break;
                case EXTREME_DONATOR:
                    boost = .06;
                    break;
                case PLATINUM_DONATOR:
                    boost = .08;
                    break;
                case EXECUTIVE_DONATOR:
                    boost = .10;
                    break;
            }
            killRequirement -= (killRequirement * boost);
        }

        if(killRequirement >= 60) {
            if(player.getSlayer().getSlayerTask().getNpcIds().contains(npcId)) {
                killRequirement -= (killRequirement * .025);
            }
        }

        if(killRequirement >= 60) {
            if(player.getEquipment().contains(3064)) {
                killRequirement -= (killRequirement * .05);
            } else if(player.getEquipment().contains(3065)) { //magma blowpipe (c)
                killRequirement -= (killRequirement * .05);
            }
        }

        if(killRequirement >= 60) {
            if(GameSettings.DOUBLE_DROP_RATE) {
                killRequirement /= 2;
            }
        }

        return !b[chance.ordinal()] && RandomUtility.getRandom(killRequirement) == 1;
    }

    public static void drop(Player player, Item item, NPC npc, Position pos, boolean goGlobal) {

        if (item == null || item.getDefinition() == null || item.getDefinition().getName().equals("null")
                || (item.getAmount() > 15000 && !item.getDefinition().isStackable()
                && !item.getDefinition().isNoted())) {
            return;
        }

        if (item.getId() == 12926 && Misc.random(5) != 3) {
            return;
        }

        if (item.getId() == 995) {
            if (player.getInventory().contains(6821)) {

                int toadd = item.getAmount();

                player.getPacketSender().sendMessage("@or2@Your coin drop has been added to your pouch!");
                player.performGraphic(new Graphic(94, GraphicHeight.MIDDLE));

                player.setMoneyInPouch((player.getMoneyInPouch() + toadd));
                player.getPacketSender().sendString(8135, "" + player.getMoneyInPouch() + "");

                coinOrbed = true;
            } else {
                coinOrbed = false;

            }

        }

        if (player.getInventory().contains(18337) && BonesData.forId(item.getId()) != null) {
            player.getPacketSender().sendGlobalGraphic(new Graphic(777), pos);
            player.getSkillManager().addExperience(Skill.PRAYER, BonesData.forId(item.getId()).getBuryingXP() * Skill.PRAYER.getModifier());
            return;
        }

        int itemId = item.getId();
        int amount = item.getAmount();

        if (itemId == 18778) {
            player.getPacketSender().sendMessage("@red@ You have received an starved ancient effigy.");
        }

        if (itemId == 6731 || itemId == 6914 || itemId == 7158 || itemId == 6889 || itemId == 6733 || itemId == 15019
                || itemId == 11235 || itemId == 15020 || itemId == 15018 || itemId == 15220 || itemId == 6735
                || itemId == 6737 || itemId == 6585 || itemId == 4151 || itemId == 4087 || itemId == 2577
                || itemId == 2581 || itemId == 11732 || itemId == 18782) {
            player.getPacketSender().sendMessage("@red@ YOU HAVE RECEIVED A MEDIUM DROP, CHECK THE GROUND!");
            shouldOrb = false;
        }
        shouldOrb = true;
        if (itemId == CharmingImp.GOLD_CHARM || itemId == CharmingImp.GREEN_CHARM || itemId == CharmingImp.CRIM_CHARM
                || itemId == CharmingImp.BLUE_CHARM) {
            shouldOrb = false;
            if (player.getInventory().contains(6500) && CharmingImp.handleCharmDrop(player, itemId, amount)) {
                return;
            }
        }

        shouldOrb = true;

        Player toGive = player;

        if (itemId == 995) {
            shouldOrb = false;
        }
        shouldOrb = true;
        if (itemId == 995 && amount > 89000000) {
            shouldOrb = false;
            String message = "@blu@[ RARE ] " + toGive.getUsername() + " has just received @red@"
                    + Misc.insertCommasToNumber(String.valueOf(amount)) + "x Coins as a rare drop!";
            World.sendMessage(message);
            player.performGraphic(new Graphic(1177, GraphicHeight.MIDDLE));

        }
        shouldOrb = true;
        boolean ccAnnounce = false;

        if (Location.inMulti(player)) {
            if (player.getCurrentClanChat() != null && player.getCurrentClanChat().getLootShare()) {
                CopyOnWriteArrayList<Player> playerList = new CopyOnWriteArrayList<Player>();
                for (Player member : player.getCurrentClanChat().getMembers()) {
                    if (member != null) {
                        if (member.getPosition().isWithinDistance(player.getPosition())) {
                            playerList.add(member);
                        }
                    }
                }
                if (playerList.size() > 0) {
                    toGive = playerList.get(RandomUtility.getRandom(playerList.size() - 1));
                    if (toGive == null || toGive.getCurrentClanChat() == null
                            || toGive.getCurrentClanChat() != player.getCurrentClanChat()) {
                        toGive = player;
                    }
                    ccAnnounce = true;
                }
            }
        }

        shouldOrb = true;
        if (ItemDropAnnouncer.announce(itemId)) {
            String itemName = item.getDefinition().getName();
            String itemMessage = Misc.anOrA(itemName) + " " + itemName;
            String npcName = Misc.formatText(npc.getDefinition().getName());

            shouldOrb = false;

/*            if (!BLACK_LISTED_MONSTER.contains(npc.getId())) {
                player.getLastDoubleDrop().reset();
                if (!RingOfBosses.collectLoot(player, item)) {
                    if (PlayerRights.isExecutiveDonator(player) || player.getRights() == PlayerRights.DEVELOPER
                            || player.getRights() == PlayerRights.ADMINISTRATOR) {
                        if (npc.getId() == 23060 || npc.getId() == 3847 || npc.getId() == 2042 || npc.getId() == 2043
                                || npc.getId() == 2044) {
                            GroundItemManager.spawnGroundItem(player, new GroundItem(item, player.getPosition(),
                                    player.getUsername(), false, 150, goGlobal, 200));
                        } else {
                            GroundItemManager.spawnGroundItem(player,
                                    new GroundItem(item, pos, player.getUsername(), false, 150, goGlobal, 200));
                        }
                    }
                }
            }*/

            switch (itemId) {
                case 14484:
                    itemMessage = "Dragon Claws";
                    break;
                case 20000:
                case 20001:
                case 20002:
                    itemMessage = itemName;
                    break;
            }
            switch (npc.getId()) {
                case 81:
                    npcName = "a Cow";
                    break;
                case 50:
                case 3200:
                case 8133:
                case 4540:
                case 1160:
                case 8549:
                    npcName = "The " + npcName + "";
                    break;
                case 51:
                case 54:
                case 5363:
                case 8349:
                case 22405:
                case 1592:
                case 1591:
                case 1590:
                case 1615:
                case 9463:
                case 9465:
                case 9467:
                case 1382:
                case 13659:
                case 11235:
                    npcName = "" + Misc.anOrA(npcName) + " " + npcName + "";
                    break;
            }

            int count = KillsTracker.getCount(player, new KillsTracker.KillsEntry(npc.getDefinition().getName(), 1, false));

            String message = "" + toGive.getUsername() + " received @red@" + itemMessage
                    + "</col> from " + npcName + " on kill "+count+"!";

            if(count == -1) {
                message = "" + toGive.getUsername() + " received @red@" + itemMessage
                        + "</col> from " + npcName + " on kill 1!";

                GameServer.discordBot.sendGameLoot(player.getName(),
                        item.getId(), item.getAmount(),
                        player.getUsername() + " has killed " + npcName + " 1 time.",
                        npcName,
                        "https://solaceps.com/images/32x32/" + item.getId() + ".png");

                new RareLootLog(player.getName(), item.getDefinition().getName(), item.getId(), item.getAmount(), npcName, Misc.getTime()).submit();


            } else {
                GameServer.discordBot.sendGameLoot(player.getName(),
                        item.getId(), item.getAmount(),
                        player.getUsername() + " has killed " + npcName + " " + count + " times.",
                        npcName,
                        "https://solaceps.com/images/32x32/" + item.getId() + ".png");

                new RareLootLog(player.getName(), item.getDefinition().getName(), item.getId(), item.getAmount(), npcName, Misc.getTime()).submit();

            }

            World.sendGlobalMessage("Loot", message);

            player.performGraphic(new Graphic(1177, GraphicHeight.MIDDLE));

            if (ccAnnounce) {
                ClanChatManager.sendMessage(player.getCurrentClanChat(),
                        "<col=16777215>[<col=255>Lootshare<col=16777215>]<col=3300CC> " + toGive.getUsername()
                                + " received " + itemMessage + " from " + npcName + "!");
            }
        }


        if (canDrop) {

            /*
             * kraken and zulrah dropping under player pos vs npc pos
             */

            if (!RingOfBosses.collectLoot(player, item)) {
                if (npc.getId() == 23060 || npc.getId() == 3847 || npc.getId() == 2042 || npc.getId() == 2043
                        || npc.getId() == 2044) {
                    GroundItemManager.spawnGroundItem(player, new GroundItem(item, player.getPosition(),
                            player.getUsername(), false, 150, goGlobal, 200));
                } else {
                    GroundItemManager.spawnGroundItem(toGive,
                            new GroundItem(item, pos, toGive.getUsername(), false, 150, goGlobal, 200));
                }
            }

        }

        DropLog.submit(toGive, new DropLogEntry(itemId, item.getAmount()));
        int item_id = item.getId();
        int value = AccountValue.getItemValue(item);
        int item_amount = item.getCount();
        int npc_id = npc.getId();
        int owner_id = player.getDatabaseId();
        HashSet<Integer> set = new HashSet<Integer>();
        set.add(526);
        set.add(532);
        set.add(560);
        set.add(561);
        set.add(1617);
        set.add(1619);
        set.add(199);
        set.add(1279);
        set.add(1623);
        set.add(1621);
        set.add(1299);
        set.add(592);
        set.add(1217);
        set.add(201);
        set.add(536);
        set.add(1331);
    }

    public static void casketDrop(Player player, int combat, Position pos) {
        int chance = (6 + (combat / 2));
        if (RandomUtility.getRandom(combat <= 50 ? 1300 : 1000) < chance) {
            Item item = new Item(7956);
            if (!RingOfBosses.collectLoot(player, item)) {
                GroundItemManager.spawnGroundItem(player,
                        new GroundItem(item, pos, player.getUsername(), false, 150, true, 200));
            }
        }
    }

    private static final int[] CLUESBOY = new int[]{2677, 2678, 2679, 2680, 2681, 2682, 2683, 2684, 2685, 2686, 2687,
            2688, 2689, 2690};

    public static void clueDrop(Player player, int combat, Position pos) {
        int chance = (6 + (combat / 4));
        if (RandomUtility.getRandom(combat <= 80 ? 1300 : 1000) < chance) {
            int clueId = CLUESBOY[Misc.getRandom(CLUESBOY.length - 1)];
            Item clue = new Item(clueId);
            if (!RingOfBosses.collectLoot(player, clue)) {
                GroundItemManager.spawnGroundItem(player,
                        new GroundItem(clue, pos, player.getUsername(), false, 150, true, 200));
            }
            player.getPacketSender().sendMessage("@or2@You have received a clue scroll!");
        }
    }

    public static class ItemDropAnnouncer {

        private static List<Integer> ITEM_LIST;

        private static final int[] TO_ANNOUNCE = new int[]{14484, 4212, 11702, 49553, 11704, 11706, 11708, 11704, 12646,
                11724, 19780, 10548, 15241, 15243, 11730, 11726, 11728, 11718, 11720, 11722, 11730, 11967, 13247, 12655,
                11969, 11970, 13058, 11971, 11972, 11973, 11974, 11975, 12927, 11976, 11716, 14876, 11286, 13427, 2513, 15259,
                13902, 13890, 13884, 13861, 13858, 52322, 42357, 1543, 18339, 42791, 13864,
                13905, 13887, 13893, 17927, 13235, 7986, 7981, 7980, 12708, 13239, 13045, 13047, 13899, 13051, 2572, 52428, 51012,
                11614, 12601, 4565, 1038, 1040, 1042, 1044, 1046, 1048, 962, 4566, 113873, 13879, 13876, 13870, 6571, 52978, 52378, 3135,
                14008, 14009, 14010, 14011, 20555, 11283, 13263, 11716, 6500, 14012, 2572, 12926, 1037, 1053, 1055, 41889,
                1057, 1050, 11862, 13051, 11860, 11858, 5607, 13058, 12601, 12603, 12605, 14013, 14014, 14015, 52966, 52746, 52748, 52750, 52752,
                14016, 13750, 13748, 17291, 13746, 15490, 15488, 13752, 13740, 18349, 18351, 6570, 12284, 18353, 18355, 49547,
                18357, 18359, 13263, 11283, 11694, 14484, 11335, 15486, 13870, 13873, 13876, 13884, 13890, 13896, 14479, 52246, 14044, 43319,
                11554, 4151, 4084, 4706, 2572, 11848, 11700, 11694, 11730, 11698, 11696, 11850, 11852, 11854, 42598,
                11856, 11846, 13902, 13858, 13861, 13864, 13867, 11995, 11996, 11997, 11978, 12001, 12002, 13742, 12003,
                12004, 19335, 12005, 50056, 50113, 52244, 51000, 50053, 51646, 43190, 50050, 43317, 14050,
                43318, 50199, 12006, 11990, 11991, 11992, 11993, 11994, 13744, 13738, 11989, 11988, 11987, 51804, 52326, 52327, 52328,
                1053, 1054, 1055, 1038, 1040, 1042, 1044, 1046, 1048, 1050, 11986, 11985, 11984, 11983, 11982, 11981, 50714, 52323,
                11979, 13659, 51637, 49699, 50199, 42414, 10935, 52550, 50020, 52494, 15000,
                20000, 20001, 11613, 11287, 20002, 16425, 11970, 1419, 20061, 15589, 52477, 15492, 20019, 3157, 52768, 52767, 52766, 52765, 981,
                11290, 50884, 12279, 21074, 6500, 12601, 11283, 42773, 51907, 51992, 20000,
                20001, 20002, 51003};

        private static void init() {
            ITEM_LIST = new ArrayList<Integer>();
            for (int i : TO_ANNOUNCE) {
                ITEM_LIST.add(i);
            }
        }

        public static boolean announce(int item) {
            return ITEM_LIST.contains(item);
        }
    }

    public void setDrops(NpcDropItem[] dropItems) {
        this.drops = dropItems;
    }
}