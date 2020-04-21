package com.varrock.world.entity.impl.npc.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.varrock.model.GroundItem;
import com.varrock.model.Item;
import com.varrock.model.Position;
import com.varrock.model.definitions.NPCDrops;
import com.varrock.model.definitions.NPCDrops.NpcDropItem;
import com.varrock.util.Misc;
import com.varrock.world.World;
import com.varrock.world.content.combat.CombatFactory;
import com.varrock.world.content.combat.CombatBuilder.CombatDamageCache;
import com.varrock.world.entity.impl.GroundItemManager;
import com.varrock.world.entity.impl.npc.NPC;
import com.varrock.world.entity.impl.player.Player;

/**
 * A class that handles the Warmonger boss.
 * 
 * @author Blake
 *
 */
public class Warmonger extends NPC {
	
	public static int spawnTime = 10;

	public static int[] COMMONLOOT = { 15273, 13883, 1726, 248, 1620, 9244, 868, 12154, 1516, 1272, 2358, 1457, 1459,
			1514, 2358, 537, 13883, 13879, 220, 1290, 2504, 4132, 1334, 1705, 4100, 4094, 4114, 15271, 6686 };

	public static int[] MEDIUMLOOT = { 15273, 13883, 242, 248, 1620, 1451, 1457, 2362, 2360, 1459, 1276, 1726, 9244,
			868, 1514, 2358, 537, 13883, 13879, 220, 1290, 2504, 4132, 1334, 1705, 4100, 4094, 4114, 15271, 6686 };

	public static int[] RARELOOT = { 15273, 13883, 242, 248, 1620, 1451, 1457, 2362, 2360, 1459, 1276, 18831, 1726,
			9244, 868, 1514, 2358, 2571, 537, 13883, 13879, 220, 1290, 2504, 4132, 1334, 1705, 4100, 4094, 4114, 6686,
			1080, 1202, 11127, 1164, 6529, 15271, 6586, 2578 };
	/**
	 * The super rare loot.
	 */
	public static int[] SUPERRARELOOT = {20000, 20002, 20001, 49553, 19673, 19674, 19672, 52954, 12927};
	
	/**
	 * The npc id.
	 */
	public static final int NPC_ID = 12841;

	/**
	 * Constructs a new {@link Warmonger}.
	 * 
	 * @param position
	 *            The position.
	 */
	public Warmonger(Position position) {
		super(NPC_ID, position);
	}
	
	@Override
	public void dropItems(Player killer) {
		handleDrop(this);
		spawnTime = 30;
	}

	/**
	 * Handles the drops for the Warmonger.
	 * 
	 * @param npc
	 *            The npc.
	 */
	public static void handleDrop(NPC npc) {
		if (npc.getCombatBuilder().getDamageMap().size() == 0) {
			return;
		}

		Map<Player, Integer> killers = new HashMap<>();

		for (Entry<Player, CombatDamageCache> entry : npc.getCombatBuilder().getDamageMap().entrySet()) {

			if (entry == null) {
				continue;
			}

			long timeout = entry.getValue().getStopwatch().elapsed();

			if (timeout > CombatFactory.DAMAGE_CACHE_TIMEOUT) {
				continue;
			}

			Player player = entry.getKey();

			if (player.getConstitution() <= 0 || !player.isRegistered()) {
				continue;
			}

			killers.put(player, entry.getValue().getDamage());
			
		}

		npc.getCombatBuilder().getDamageMap().clear();

		List<Entry<Player, Integer>> result = sortEntries(killers);
		
		int count = 0;
		
		for (Entry<Player, Integer> entry : result) {

			Player killer = entry.getKey();
			int damage = entry.getValue();

			handleDrop(npc, killer, damage);

			if (Misc.random(5) == 1) {
				killer.addBossPoints(2);
			}

			if (++count >= 5) {
				break;
			}

		}

	}
	
	/**
	 * Gives the loot to the player.
	 * 
	 * @param player
	 *            The player.
	 * @param npc
	 *            The npc.
	 * @param pos
	 *            The position.
	 */
	public static void giveLoot(Player player, NPC npc, Position pos) {
		int chance = Misc.getRandom(500);
		int superrare = SUPERRARELOOT[Misc.getRandom(SUPERRARELOOT.length - 1)];
		
		World.sendMessage("<img=469> <col=dbffba><shad=1>"+player.getUsername()+ " received a loot from the @gre@Warmonger!");
		
		GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(995, Misc.inclusiveRandom(5_000_000, 10_000_000)), pos, player.getUsername(), false, 150, true, 200));

		if (chance == 1) {
			// super rare
			GroundItemManager.spawnGroundItem(player,
					new GroundItem(new Item(RARELOOT[Misc.getRandom(RARELOOT.length - 1)], 1),
							pos, player.getUsername(), false, 150, true, 200));
			GroundItemManager.spawnGroundItem(player,
					new GroundItem(new Item(COMMONLOOT[Misc.getRandom(COMMONLOOT.length - 1)], 1),
							pos, player.getUsername(), false, 150, true, 200));
			GroundItemManager.spawnGroundItem(player,
					new GroundItem(new Item(superrare), pos, player.getUsername(), false, 150, true, 200));
			return;
		} else if (chance >= 490) {
			// rare
			GroundItemManager.spawnGroundItem(player,
					new GroundItem(new Item(RARELOOT[Misc.getRandom(RARELOOT.length - 1)], 1),
							pos, player.getUsername(), false, 150, true, 200));
			GroundItemManager.spawnGroundItem(player,
					new GroundItem(new Item(RARELOOT[Misc.getRandom(RARELOOT.length - 1)], 1),
							pos, player.getUsername(), false, 150, true, 200));
			GroundItemManager.spawnGroundItem(player,
					new GroundItem(new Item(RARELOOT[Misc.getRandom(RARELOOT.length - 1)], 1),
							pos, player.getUsername(), false, 150, true, 200));
			return;
		} else if (chance >= 250) {
			// medium
			GroundItemManager.spawnGroundItem(player,
					new GroundItem(new Item(MEDIUMLOOT[Misc.getRandom(MEDIUMLOOT.length - 1)], 1),
							pos, player.getUsername(), false, 150, true, 200));
			GroundItemManager.spawnGroundItem(player,
					new GroundItem(new Item(MEDIUMLOOT[Misc.getRandom(MEDIUMLOOT.length - 1)], 1),
							pos, player.getUsername(), false, 150, true, 200));
			GroundItemManager.spawnGroundItem(player,
					new GroundItem(new Item(MEDIUMLOOT[Misc.getRandom(MEDIUMLOOT.length - 1)], 1),
							pos, player.getUsername(), false, 150, true, 200));
			return;
		} else {
			// common
			GroundItemManager.spawnGroundItem(player,
					new GroundItem(new Item(COMMONLOOT[Misc.getRandom(COMMONLOOT.length - 1)], 1),
							pos, player.getUsername(), false, 150, true, 200));
			GroundItemManager.spawnGroundItem(player,
					new GroundItem(new Item(COMMONLOOT[Misc.getRandom(COMMONLOOT.length - 1)], 1),
							pos, player.getUsername(), false, 150, true, 200));
			return;
		}
	}
	
	/**
	 * Handles the drop.
	 * 
	 * @param npc
	 *            The npc.
	 * @param player
	 *            The player.
	 * @param damage
	 *            The damage.
	 */
	private static void handleDrop(NPC npc, Player player, int damage) {
		Position pos = npc.getPosition();
		giveLoot(player, npc, pos);
	}
	
	public static <K, V extends Comparable<? super V>> List<Entry<K, V>> sortEntries(Map<K, V> map) {

		List<Entry<K, V>> sortedEntries = new ArrayList<Entry<K, V>>(map.entrySet());

		Collections.sort(sortedEntries, new Comparator<Entry<K, V>>() {
			
			@Override
			public int compare(Entry<K, V> e1, Entry<K, V> e2) {
				return e2.getValue().compareTo(e1.getValue());
			}
			
		});

		return sortedEntries;
		
	}

	public static final void loadDrops() {
		Map<Integer, NpcDropItem> items = new HashMap<>();
		
		items.put(995, new NpcDropItem(995, new int[] { 3_500_000 }, 0));
		
		Arrays.stream(COMMONLOOT).filter(i -> !items.containsKey(i)).forEach(i -> items.put(i, new NpcDropItem(i, new int[] { 20 + Misc.getRandom(20) }, 3)));
		Arrays.stream(MEDIUMLOOT).filter(i -> !items.containsKey(i)).forEach(i -> items.put(i, new NpcDropItem(i, new int[] { 22 + Misc.getRandom(13) }, 5)));
		Arrays.stream(RARELOOT).filter(i -> !items.containsKey(i)).forEach(i -> items.put(i, new NpcDropItem(i, new int[] { 30 + Misc.getRandom(20) }, 8)));
		Arrays.stream(SUPERRARELOOT).filter(i -> !items.containsKey(i)).forEach(i -> items.put(i, new NpcDropItem(i, new int[] { 1 }, 10)));
		
		NPCDrops drops = new NPCDrops();
		drops.setDrops(items.values().toArray(new NpcDropItem[items.size()]));
		
		NPCDrops.getDrops().put(NPC_ID, drops);
	}
	
}

