package com.varrock.world.content;

import java.util.*;
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

public class Dracula extends NPC {

	public static int[] COMMON_LOOT = { 10833 };
	public static int[] RARE_LOOT = { 1959 };

	public static final int NPC_ID = 21332;

	public static final DraculaLocation[] LOCATIONS = {
			new DraculaLocation(3237, 3620, 0, "Chaos Alter"),
			new DraculaLocation(3421, 3524, 0, "Outside Slayer Tower"),
			new DraculaLocation(3348, 3281, 0, "North of Duel Arena"),

	};

	public Dracula(Position position) {

		super(NPC_ID, position);

	}

	@Override
	public void dropItems(Player killer) {
		handleDrop(this);
	}

	/**
	 * 
	 * @param npc
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
		}

	}

	public static void giveLoot(Player player, NPC npc, Position pos) {

		int chance = Misc.getRandom(5000);
		int rareItem = RARE_LOOT[Misc.getRandom(RARE_LOOT.length - 1)];

		GroundItemManager.spawnGroundItem(player,
				new GroundItem(new Item(532, 1),
						pos, player.getUsername(), false, 150, true, 200));

		GroundItemManager.spawnGroundItem(player,
				new GroundItem(new Item(1550, 1),
						pos, player.getUsername(), false, 150, true, 200));

		GroundItemManager.spawnGroundItem(player,
				new GroundItem(new Item(995, Misc.random(500_000, 5_000_000)),
						pos, player.getUsername(), false, 150, true, 200));

		if(chance <= 1000) {
			GroundItemManager.spawnGroundItem(player,
					new GroundItem(new Item(COMMON_LOOT[Misc.getRandom(COMMON_LOOT.length - 1)], 1),
							pos, player.getUsername(), false, 150, true, 200));
		}

		if (chance == 1) {
			GroundItemManager.spawnGroundItem(player,
					new GroundItem(new Item(rareItem, 1), pos, player.getUsername(), false, 150, true, 200));

			World.sendMessage("<img=469> <shad=dbffba>" + player.getUsername() + " received a "+new Item(rareItem).getDefinition().getName()+" from Dracula!");
			return;
		}

	}

	/**
	 * 
	 * @param npc
	 * @param player
	 * @param damage
	 */
	private static void handleDrop(NPC npc, Player player, int damage) {
		Position pos = npc.getPosition();
		giveLoot(player, npc, pos);
	}

	/**
	 * 
	 * @param map
	 * @return
	 */
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

	/**
	 * 
	 * @author Nick Hartskeerl <apachenick@hotmail.com>
	 *
	 */
	public static class DraculaLocation extends Position {

		/**
		 *
		 */
		private String location;

		/**
		 * @param x
		 * @param y
		 * @param z
		 * @param location
		 */
		public DraculaLocation(int x, int y, int z, String location) {
			super(x, y, z);
			setLocation(location);
		}

		/**
		 * @return
		 */

		public String getLocation() {
			return location;
		}

		/**
		 * @param location
		 */
		public void setLocation(String location) {
			this.location = location;
		}


	}

	static {
		Map<Integer, NpcDropItem> items = new HashMap<>();

		Arrays.stream(COMMON_LOOT).filter(i -> !items.containsKey(i))
				.forEach(i -> items.put(i, new NpcDropItem(i, new int[] { 20 + Misc.getRandom(20) }, 3)));
		Arrays.stream(RARE_LOOT).filter(i -> !items.containsKey(i))
				.forEach(i -> items.put(i, new NpcDropItem(i, new int[] { 30 + Misc.getRandom(20) }, 10)));

		NPCDrops drops = new NPCDrops();
		drops.setDrops(items.values().toArray(new NpcDropItem[items.size()]));

		NPCDrops.getDrops().put(NPC_ID, drops);
	}

}