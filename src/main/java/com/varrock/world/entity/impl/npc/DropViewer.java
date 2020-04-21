package com.varrock.world.entity.impl.npc;

import java.util.*;

import com.varrock.model.GameMode;
import com.varrock.model.Item;
import com.varrock.model.container.impl.Equipment;
import com.varrock.model.definitions.ItemDefinition;
import com.varrock.model.definitions.NPCDrops;
import com.varrock.model.definitions.NpcDefinition;
import com.varrock.model.definitions.NPCDrops.DropChance;
import com.varrock.util.Misc;
import com.varrock.world.content.WildernessDrops;
import com.varrock.world.content.treasuretrails.EliteClueScroll;
import com.varrock.world.entity.impl.player.Player;

/**
 * Displays the drops for a amount of kills or find a monster's drop.
 *
 * @author Gabriel || Wolfsdarker
 */
public class DropViewer {

    /**
     * The player who will see the information.
     */
    private Player player;

    /**
     * The amount of kills to be tested.
     */
    private int killCount;

    /**
     * The monster to be tested.
     */
    private int npcId;

    /**
     * The monster to be searched.
     */
    private String search;

    private boolean sendsErrorMessage = true;

    public DropViewer(Player player, String search, int totalKills) {
        this.player = player;
        this.search = search;
        this.killCount = totalKills;
        player.dropCommandCooldown = System.currentTimeMillis() + 5000;
        dropSimulator();
    }

    public DropViewer(Player player) {
        this.player = player;
        player.npcToSim = "Click Here";
        player.npcAmountToSim = 100;
        player.getPacketSender().sendString(72005, player.npcToSim);
        player.getPacketSender().sendString(72007, "" + player.npcAmountToSim);
        player.getPacketSender().sendString(72003, "Drop Simulator");
        player.getPacketSender().resetItemsOnInterface(58800, 110);
        player.getPacketSender().sendInterface(72000);
    }
    
    public DropViewer(Player player, int npcId, int totalKills) {
        this.player = player;
        this.killCount = totalKills;
        this.npcId = npcId;
        player.dropCommandCooldown = System.currentTimeMillis() + 5000;
        simulateLoot();
    }

    public DropViewer(Player player, String search, boolean item) {
        this.player = player;
        this.search = search;
        player.dropCommandCooldown = System.currentTimeMillis() + 5000;
        if (item) {
            findDrop();
        } else {
            findMonsters();
        }
    }
    
	public static void sendItemInfoInterface(Player player, String title, List<Item> items) {

    	player.getPacketSender().sendString(72003, title);
        player.getPacketSender().sendItemsOnInterface(58800, 110, items, true);

        player.getPacketSender().sendInterface(72000);
    }
	
	public static void sendItemInfoInterfaceForSimulator(Player player, String title, List<Item> items) {

    	player.getPacketSender().sendString(72003, title);
        player.getPacketSender().sendItemsOnInterface(58813, 110, items, true);

        player.getPacketSender().sendInterface(72000);
    }
	
	public void dropSimulator() {

		boolean found = false;
		for (NpcDefinition def1 : NpcDefinition.getDefinitions()) {
            if (def1 == null) {
                continue;
            }
            if (def1.getName().toLowerCase().contains(search) && !found) {
            	npcId = def1.getId();
				found = true;
				continue;
			}
		}
		if(!found) {
			player.getPacketSender().sendMessage("NPC not found! If you think this is a mistake, contact staff!");
			return;
		}
        NpcDefinition def = NpcDefinition.forId(npcId);

        if (def == null) {
            player.sendMessage("This monster has no definition.");
            return;
        }

        Map<Integer, Integer> loot_list = new HashMap<>();
        List<Item> items = new ArrayList<>();
        final boolean ringOfWealth = player.getEquipment().get(Equipment.RING_SLOT).getId() == 2572;
        final boolean ringOfBosses = player.getEquipment().get(Equipment.RING_SLOT).getId() == 42603;
        final boolean vorkathCape = npcId == 23060 && player.getEquipment().get(Equipment.CAPE_SLOT).getId() == 21000;
        final boolean nexCape = npcId == 13447 && player.getEquipment().get(Equipment.CAPE_SLOT).getId() == 21008;
        final boolean corpCape = npcId == 8133 && player.getEquipment().get(Equipment.CAPE_SLOT).getId() == 21011;
        final boolean hydraCape = npcId == 23615 && player.getEquipment().get(Equipment.CAPE_SLOT).getId() == 21021;

        NPCDrops drops = NPCDrops.forId(npcId);

        try {
	        for (int x = 0; x < killCount; x++) {
	
	            boolean[] dropsReceived = new boolean[12];
	
	            for (NPCDrops.NpcDropItem drop : drops.getDropList()) {
	
	                if (drop.getItem().getId() <= 0 || drop.getItem().getId() > ItemDefinition.getMaxAmountOfItems() || drop.getItem().getAmount() <= 0) {
	                    continue;
	                }
	
	                int maxAmount = Misc.arrayMax(drop.getCount());
	                int minAmount = Misc.arrayMax(drop.getCount());
	
	                int current = Misc.random(maxAmount - minAmount) + minAmount;
	
	                DropChance dropChance = drop.getChance();
	
	                if (dropChance == DropChance.ALWAYS) {
	                    if (loot_list.containsKey(drop.getId()))
	                        current += loot_list.get(drop.getId());
	
	                    loot_list.put(drop.getId(), current);
	                } else {
						if (NPCDrops.shouldDrop(dropsReceived, npcId, dropChance, ringOfWealth, ringOfBosses, vorkathCape, nexCape, corpCape, hydraCape,
	                            player.getGameMode() == GameMode.HARDCORE_IRONMAN || player.getGameMode() == GameMode.IRONMAN || player.getGameMode() == GameMode.ULTIMATE_IRONMAN, player)) {
	                        if (loot_list.containsKey(drop.getId()))
	                            current += loot_list.get(drop.getId());
	                        loot_list.put(drop.getId(), current);
	                        dropsReceived[dropChance.ordinal()] = true;
	                    }
	                }
	            }
	
	            if (EliteClueScroll.drops(def.getName())) {
	                items.add(new Item(42073));
	                loot_list.put(42073, 1);
	            }
	
	            Item wildyDrop = WildernessDrops.getDrop(player, npcId, 1.0);
	            Item bloodMoney = WildernessDrops.getBloodMoneyDrop(player, npcId, false);
	
	            if (wildyDrop != null) {
	                int current = wildyDrop.getAmount();
	                if (loot_list.containsKey(wildyDrop.getId()))
	                    current += loot_list.get(wildyDrop.getId());
	                loot_list.put(wildyDrop.getId(), current);
	            }
	
	            if (bloodMoney != null) {
	                int current = bloodMoney.getAmount();
	                if (loot_list.containsKey(bloodMoney.getId()))
	                    current += loot_list.get(bloodMoney.getId());
	                loot_list.put(bloodMoney.getId(), current);
	            }
	
	        }


	        for (int item_id : loot_list.keySet()) {
	            Item loot = new Item(item_id, loot_list.get(item_id));
	            items.add(loot);
	        }
	
	        sendItemInfoInterfaceForSimulator(player, "Loot for " + killCount + " kills on '" + def.getName() + "'", items);
        } catch(Exception e) {
        	player.sendMessage("Error! Try a different NPC! ");
        }
    }

    public void simulateLoot() {

        NpcDefinition def = NpcDefinition.forId(npcId);

        if (def == null) {
            player.sendMessage("This monster has no definition.");
            return;
        }

        Map<Integer, Integer> loot_list = new HashMap<>();
        List<Item> items = new ArrayList<>();
        List<String> informations = new ArrayList<>();
        final boolean ringOfWealth = player.getEquipment().get(Equipment.RING_SLOT).getId() == 2572;
        final boolean ringOfBosses = player.getEquipment().get(Equipment.RING_SLOT).getId() == 42603;
        final boolean vorkathCape = npcId == 23060 && player.getEquipment().get(Equipment.CAPE_SLOT).getId() == 21000;
        final boolean nexCape = npcId == 13447 && player.getEquipment().get(Equipment.CAPE_SLOT).getId() == 21008;
        final boolean corpCape = npcId == 8133 && player.getEquipment().get(Equipment.CAPE_SLOT).getId() == 21011;
        final boolean hydraCape = npcId == 23615 && player.getEquipment().get(Equipment.CAPE_SLOT).getId() == 21021;

        NPCDrops drops = NPCDrops.forId(npcId);

        for (int x = 0; x < killCount; x++) {

            boolean[] dropsReceived = new boolean[12];

            for (NPCDrops.NpcDropItem drop : drops.getDropList()) {

                if (drop.getItem().getId() <= 0 || drop.getItem().getId() > ItemDefinition.getMaxAmountOfItems() || drop.getItem().getAmount() <= 0) {
                    continue;
                }

                int maxAmount = Misc.arrayMax(drop.getCount());
                int minAmount = Misc.arrayMax(drop.getCount());

                int current = Misc.random(maxAmount - minAmount) + minAmount;

                DropChance dropChance = drop.getChance();

                if (dropChance == DropChance.ALWAYS) {
                    if (loot_list.containsKey(drop.getId()))
                        current += loot_list.get(drop.getId());

                    loot_list.put(drop.getId(), current);
                } else {
					if (NPCDrops.shouldDrop(dropsReceived, npcId, dropChance, ringOfWealth, ringOfBosses, vorkathCape, nexCape, corpCape, hydraCape,
                            player.getGameMode() == GameMode.HARDCORE_IRONMAN || player.getGameMode() == GameMode.IRONMAN || player.getGameMode() == GameMode.ULTIMATE_IRONMAN, player)) {
                        if (loot_list.containsKey(drop.getId()))
                            current += loot_list.get(drop.getId());
                        loot_list.put(drop.getId(), current);
                        dropsReceived[dropChance.ordinal()] = true;
                    }
                }
            }

            if (EliteClueScroll.drops(def.getName())) {
                items.add(new Item(42073));
                loot_list.put(42073, 1);
            }

            Item wildyDrop = WildernessDrops.getDrop(player, npcId, 1.0);
            Item bloodMoney = WildernessDrops.getBloodMoneyDrop(player, npcId, false);

            if (wildyDrop != null) {
                int current = wildyDrop.getAmount();
                if (loot_list.containsKey(wildyDrop.getId()))
                    current += loot_list.get(wildyDrop.getId());
                loot_list.put(wildyDrop.getId(), current);
            }

            if (bloodMoney != null) {
                int current = bloodMoney.getAmount();
                if (loot_list.containsKey(bloodMoney.getId()))
                    current += loot_list.get(bloodMoney.getId());
                loot_list.put(bloodMoney.getId(), current);
            }

        }


        for (int item_id : loot_list.keySet()) {
            Item loot = new Item(item_id, loot_list.get(item_id));
            items.add(loot);
            informations.add("@gre@" + loot.getDefinition().getName());
            informations.add("ID: @whi@" + item_id);
        }

        sendItemInfoInterface(player, "Loot for " + killCount + " kills on '" + def.getName() + "'", items, informations);
    }

    /**
     * Searchs for the NPC and displays its drops on screen.
     *
     * @return if it was successful
     */
    boolean findDrop() {

        if (search == null || search.length() == 0) {
            return false;
        }

        search = search.replace("_", " ").toLowerCase();

        String prefix = "@bla@[ @blu@Drops</col>] ";
        boolean found = false;

        List<Item> items = new ArrayList<Item>();
        List<String> informations = new ArrayList<String>();

        for (NpcDefinition def : NpcDefinition.getDefinitions()) {
            if (def == null) {
                continue;
            }
            if (def.getName().toLowerCase().contains(search)) {

                NPCDrops drops = NPCDrops.forId(def.getId());

                if (drops == null || drops.getDropList() == null) {
                    continue;
                }

                Map<Integer, Integer> loot_list = new HashMap<>();

                for (NPCDrops.NpcDropItem drop : drops.getDropList()) {

                    if (drop.getId() < 0 || loot_list.containsKey(drop.getId())) {
                        continue;
                    }

                    ItemDefinition item_def = ItemDefinition.forId(drop.getId());

                    int amount = (item_def.isStackable() || item_def.isNoted()) ? Misc.arrayMax(drop.getCount()) : 1;

                    if (loot_list.containsKey(drop.getId()) && (item_def.isStackable() || item_def.isNoted()))
                        amount += loot_list.get(drop.getId());

                    loot_list.put(drop.getId(), amount);

                    items.add(new Item(drop.getId(), amount));

                    informations.add("@gre@" + ItemDefinition.forId(drop.getId()).getName());

                    informations.add("@whi@Drop Chance: " + getRarity(drop.getChance().getRandom())
                            + (drop.getChance().getRandom() == 0 ? "Always" : "1/" + drop.getChance().getRandom()));

                }

                Item bloodMoney = WildernessDrops.getBloodMoneyDrop(player, def.getId(), true);

                if (bloodMoney != null) {
                    ItemDefinition item_def = bloodMoney.getDefinition();
                    if (loot_list.containsKey(bloodMoney.getId()) && (item_def.isStackable() || item_def.isNoted()))
                        loot_list.put(bloodMoney.getId(), bloodMoney.getAmount());

                    items.add(new Item(bloodMoney.getId(), bloodMoney.getAmount()));

                    informations.add("@gre@" + ItemDefinition.forId(bloodMoney.getId()).getName());

                    informations.add("@whi@Drop Chance: @red@Unknown");
                }

                if (EliteClueScroll.drops(def.getName())) {

                    Item clue = new Item(EliteClueScroll.EliteClue.CLUE_1.getId(), 1);
                    ItemDefinition item_def = clue.getDefinition();
                    if (loot_list.containsKey(clue.getId()) && (item_def.isStackable() || item_def.isNoted()))
                        loot_list.put(clue.getId(), 1);

                    items.add(new Item(clue.getId(), 1));

                    informations.add("@gre@" + ItemDefinition.forId(clue.getId()).getName());

                    informations.add("@whi@Drop Chance: @or1@1/50");
                }

                sendItemInfoInterface(player, def.getName().replaceAll("_", " ") + " drops", items, informations);

                found = true;
                break;
            }
        }
        if (!found && sendsErrorMessage) {
            player.sendMessage(prefix + "No monsters found for that drop.");
        }
        return true;
    }

    /**
     * Searchs NPCs that drop certain items.
     *
     * @return if it was successful
     */
    boolean findMonsters() {

        if (search == null || search.length() == 0) {
            return false;
        }

        search = search.replace("_", " ").toLowerCase();
        /*int itemId = ItemDefinition.getItemId(search);
        if (itemId == -1) {
            return false;
        }*/

        String prefix = "@bla@[ @blu@Drops</col>] ";
        boolean found = false;

        List<Item> items = new ArrayList<Item>();
        List<Integer> npcs = new ArrayList<>();
        List<String> informations = new ArrayList<>();

        for (NpcDefinition def : NpcDefinition.getDefinitions()) {
            if (def == null) {
                continue;
            }

            NPCDrops drops = NPCDrops.forId(def.getId());

            if (drops == null || drops.getDropList() == null || drops.getDropList().length == 0) {
                continue;
            }

            for (NPCDrops.NpcDropItem drop : drops.getDropList()) {

                if(drop == null)
                    continue;

                ItemDefinition item_def = ItemDefinition.forId(drop.getId());

                if(!item_def.getName().toLowerCase().contains(search)) {
                    continue;
                }

                int amount = (item_def.isStackable() || item_def.isNoted()) ? Misc.arrayMax(drop.getCount()) : 1;

                npcs.add(def.getId());

                items.add(new Item(drop.getId(), amount));

                informations.add("@gre@" + def.getName());

                informations.add("@whi@Drop Chance: " + getRarity(drop.getChance().getRandom())
                        + (drop.getChance().getRandom() == 0 ? "Always" : "1/" + drop.getChance().getRandom()));

                break;
            }
        }

        if (search.contains("clue scroll")) {
            int itemId = EliteClueScroll.EliteClue.CLUE_1.getId();
            for (String npc : EliteClueScroll.DROPPERS) {
                NpcDefinition def = NpcDefinition.forName(npc);
                if (def != null) {
                    npcs.add(def.getId());
                    items.add(new Item(itemId));
                    informations.add("@gre@" + npc);
                    informations.add("@whi@Drop Chance: 1/" + EliteClueScroll.chanceForID(def.getId()));
                }
            }
        }

        if (search.contains("blood money")) {
            for (Integer npc : WildernessDrops.NPCS) {
                Item money = WildernessDrops.getBloodMoneyDrop(player, npc, true);
                NpcDefinition def = NpcDefinition.forId(npc);
                if (def == null || money == null)
                    continue;
                npcs.add(npc);
                items.add(money);
                informations.add("@gre@" + def.getName());
                informations.add("@whi@Drop Chance: @red@Unknown");
            }
        }

        if (npcs.isEmpty()) {
            player.sendMessage(prefix + "No monsters found for that drop.");
            return false;
        }

        //sendNPCInfoInterface(player, "Monsters that drop '" + search + "'", npcs, informations);
        sendItemInfoInterface(player, "Monsters that drop '" + search + "'", items, informations);
        return true;
    }

    /**
     * Returns the rarity by the chance;
     *
     * @param chance
     * @return the rarity
     */
    static String getRarity(int chance) {
        if (chance >= 155)
            return "@red@";
        if (chance >= 100)
            return "@or2@";
        if (chance >= 40)
            return "@or1@";
        if (chance > 1)
            return "@yel@";
        else
            return "@gre@";
    }

    /**
     * Sends a interface for lists.
     *
     * @param title
     * @param items
     * @param informations
     */
    public static void sendItemInfoInterface(Player player, String title, List<Item> items, Collection<String> informations) {

        player.getPacketSender().sendString(58002, title);

        int interfaceId = 58302;

        int index = 0;

        for (String information : informations) {
            player.getPacketSender().sendString(interfaceId + index, information);
            index++;
        }

        for (int i = index; i < 400; i++) {
            player.getPacketSender().sendString(interfaceId + i, "");
        }

        player.getPacketSender().sendItemsOnInterface(58800, 200, items, true);

        player.getPacketSender().sendInterface(58000);
    }

    /**
     * Sends a interface for lists.
     *
     * @param title
     * @param npcs
     * @param informations
     */
    public static void sendNPCInfoInterface(Player player, String title, List<Integer> npcs, List<String> informations) {

        player.getPacketSender().sendString(30332, title);

        int interfaceId = 30334;

        int size = informations.size();

        if (size > 400) {
            size = 400;
        }

        for (int i = 0; i < 400; i++) {
            if (i < size) {
                player.getPacketSender().sendString(interfaceId + i, informations.get(i));
            } else {
                player.getPacketSender().sendString(interfaceId + i, "");
            }
        }


        for (int i = 0; i < 200; i++) {
            if (i < npcs.size())
                player.getPacketSender().sendNpcHeadOnInterface(npcs.get(i), 30334 + 400 + i);
            else
                player.getPacketSender().sendNpcHeadOnInterface(4000, 30334 + 400 + i);
        }

        player.getPacketSender().sendInterface(30330);
    }


    public boolean isSendsErrorMessage() {
        return sendsErrorMessage;
    }

    public void setSendsErrorMessage(boolean sendsErrorMessage) {
        this.sendsErrorMessage = sendsErrorMessage;
    }
}
