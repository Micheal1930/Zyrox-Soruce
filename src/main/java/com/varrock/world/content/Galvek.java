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
import com.varrock.world.content.combat.strategy.impl.GalvekCombatStrategy;
import com.varrock.world.entity.impl.GroundItemManager;
import com.varrock.world.entity.impl.npc.NPC;
import com.varrock.world.entity.impl.player.Player;

public class Galvek extends NPC {

	public static int[] COMMONLOOT = { 6199, 8850, 4587, 1434, 1305, 6528, 1377, 11133, 11126, 1187, 1127, 1079, 1712, };

	public static int[] MEDIUMLOOT = { 4003, 4151, 6585, 15486, 11235, 6914, 2577, 6920, 15220, 15020, 15018, 15019, 15241, 10887,   };

	public static int[] RARELOOT = { 14484, 11694, 19780, 12926, 11726, 11724, 4005, 49481, 4706, };

	public static int[] SUPERRARELOOT = { 20057, 20058, 20059, 4007, 51003,   };


	public GalvekCombatStrategy.Stage stage;
	/**
	 *
	 * @param stage
	 * @param position
	 */
	public Galvek(GalvekCombatStrategy.Stage stage, Position position) {
		super(stage.getId(), position);
		this.stage = stage;

		// setConstitution(96500/3); //7,650
		// setDefaultConstitution(96500);

	}

	@Override
	public void dropItems(Player killer) {
		if (stage == GalvekCombatStrategy.Stage.GREEN) {
			handleDrop(this);
		}
	}

	/**
	 * 
	 * @param npc
	 */
	public static void handleDrop(NPC npc) {
		World.getPlayers().forEach(p -> p.getPacketSender().sendString(26707, "@or2@Galvek: @gre@N/A"));

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
				System.out.println("timeout on damage - galvek");
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

			if (++count >= 3) {
				break;
			}

		}

	}

	public static void giveLoot(Player player, NPC npc, Position pos) {


		Item wildernessDrop = WildernessDrops.getDrop(player, npc.getId(), 1.0);
		if(wildernessDrop != null) {
			GroundItemManager.spawnGroundItem(player, new GroundItem(wildernessDrop, pos, player.getUsername(), false, 150, false, 200));
		}

		Item bloodMoney = WildernessDrops.getBloodMoneyDrop(player, npc.getId(), false);
		if(bloodMoney != null) {
			GroundItemManager.spawnGroundItem(player, new GroundItem(bloodMoney, pos, player.getUsername(), false, 150, false, 200));
		}

		int chance = Misc.getRandom(500);
		int superrare = SUPERRARELOOT[Misc.getRandom(SUPERRARELOOT.length - 1)];

		// player.getPacketSender().sendMessage("chance: "+chance);
		GroundItemManager.spawnGroundItem(player,
				new GroundItem(new Item(995, Misc.inclusiveRandom(20_000_000, 40_000_000)), pos, player.getUsername(),
						false, 150, true, 200));

		GroundItemManager.spawnGroundItem(player,
				new GroundItem(new Item(COMMONLOOT[Misc.getRandom(COMMONLOOT.length - 1)], 1),
						pos, player.getUsername(), false, 150, true, 200));

		if (chance == 1) {

			GroundItemManager.spawnGroundItem(player,
					new GroundItem(new Item(superrare, 1), pos, player.getUsername(), false, 150, true, 200));

			World.sendMessage("<img=469> <shad=dbffba>" + player.getUsername() + " received a "+new Item(superrare).getDefinition().getName()+" from Galvek!");

			return;
		} else if (chance >= 490) {
			Item rareItem = new Item(RARELOOT[Misc.getRandom(RARELOOT.length - 1)], 1);

			// rare
			GroundItemManager.spawnGroundItem(player,
					new GroundItem(rareItem,
							pos, player.getUsername(), false, 150, true, 200));

			World.sendMessage("<img=469> <shad=dbffba>" + player.getUsername() + " received a "+rareItem.getDefinition().getName()+" from Galvek!");


			return;
		} else if (chance >= 250) {
			// medium
			GroundItemManager.spawnGroundItem(player,
					new GroundItem(new Item(MEDIUMLOOT[Misc.getRandom(MEDIUMLOOT.length - 1)], 1),
							pos, player.getUsername(), false, 150, true, 200));
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
		player.sendMessage("handle drop.");
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

}