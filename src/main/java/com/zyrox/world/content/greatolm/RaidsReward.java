package com.zyrox.world.content.greatolm;

import com.zyrox.GameSettings;
import com.zyrox.model.Item;
import com.zyrox.model.definitions.NPCDrops.NpcDropItem;
import com.zyrox.util.Misc;
import com.zyrox.world.World;
import com.zyrox.world.entity.impl.player.Player;

public class RaidsReward {

	private static int[] ExtremelyRares = new int[] { 20998 };

	private static int[] VeryRares = new int[] { 51018, 51021, 51024, 51003, 1959 };

	private static int[] SuperRares = new int[] { 51000, 20555, };

	public static RaidsReward[] commonLoot = new RaidsReward[] { //
			new RaidsReward(560, 1, 3000), new RaidsReward(565, 1, 4095), new RaidsReward(566, 1, 6554),
			new RaidsReward(892, 1, 9437), new RaidsReward(11212, 1, 926),
			//
			new RaidsReward(3049, 1, 354), new RaidsReward(207, 1, 164), new RaidsReward(209, 1, 668),
			new RaidsReward(211, 1, 354), new RaidsReward(213, 1, 323), new RaidsReward(3051, 1, 131),
			new RaidsReward(215, 1, 319), new RaidsReward(2485, 1, 446), new RaidsReward(217, 1, 616),
			new RaidsReward(219, 1, 153),
			//
			new RaidsReward(442, 1, 6553), new RaidsReward(453, 1, 6553), new RaidsReward(444, 1, 2892),
			new RaidsReward(447, 1, 2892), new RaidsReward(449, 1, 729), new RaidsReward(451, 1, 87),
			new RaidsReward(1623, 1, 642), new RaidsReward(1621, 1, 923), new RaidsReward(1619, 1, 524),
			new RaidsReward(1617, 1, 253),
			//
			new RaidsReward(7936, 1, 65535), new RaidsReward(8780, 1, 310), new RaidsReward(8782, 1, 550) };

	public static NpcDropItem[] getDrops() {
		NpcDropItem[] drops = new NpcDropItem[commonLoot.length + SuperRares.length
				+ VeryRares.length + ExtremelyRares.length];

		int idx = 0;

		for (RaidsReward r : commonLoot) {
			drops[idx++] = new NpcDropItem(r.getId(), new int[] { r.getMin(), r.getMax() }, 3);
		}

		for (int id : SuperRares) {
			drops[idx++] = new NpcDropItem(id, new int[] { 1 }, 6);
		}

		for (int id : VeryRares) {
			drops[idx++] = new NpcDropItem(id, new int[] { 1 }, 8);
		}

		for (int id : ExtremelyRares) {
			drops[idx++] = new NpcDropItem(id, new int[] { 1 }, 10);
		}

		return drops;
	}

	public static void grantLoot(RaidsParty party) {
		Player rareLootPlayer = null;

		if (Misc.randomFloat() < 0.16) {
			int random = Misc.getRandom(party.getPlayers().size() - 1);
			rareLootPlayer = party.getPlayers().get(random);
			double randomNumber = Misc.randomFloat();

			double modifier = 2; // 50% modifier

			if (randomNumber < (GameSettings.DOUBLE_DROP_RATE ? 0.7498 / modifier : 0.7498)) {
				rareLootPlayer.setRaidsLoot(new Item(SuperRares[Misc.getRandom(SuperRares.length - 1)]));
			} else if (randomNumber < (GameSettings.DOUBLE_DROP_RATE ? 0.9283 / modifier : 0.9283)) {
				rareLootPlayer.setRaidsLoot(new Item(VeryRares[Misc.getRandom(VeryRares.length - 1)]));
			} else {
				if (Misc.getRandom(4) == 1) {
					rareLootPlayer.setRaidsLoot(new Item(ExtremelyRares[Misc.getRandom(ExtremelyRares.length - 1)]));
				} else {
					rareLootPlayer.setRaidsLoot(new Item(VeryRares[Misc.getRandom(VeryRares.length - 1)]));
				}
			}
			announce(rareLootPlayer, rareLootPlayer.getRaidsLoot());
			rareLootPlayer.setRaidsLootSecond(new Item(-1, -1));
		}

		for (Player member : party.getPlayers()) {
			if (member != null) {
				member.addBossPoints(4);
			}
			if (rareLootPlayer != null) {
				if (member != null && !member.equals(rareLootPlayer)) {
					int loot = Misc.getRandom(commonLoot.length - 1);

					member.setRaidsLoot(new Item(commonLoot[loot].getId(), commonLoot[loot].getMin()
							+ Misc.getRandom((commonLoot[loot].getMax() - commonLoot[loot].getMin()))));
					int loot2 = Misc.getRandom(commonLoot.length - 1);
					member.setRaidsLootSecond(new Item(commonLoot[loot2].getId(), commonLoot[loot2].getMin()
							+ Misc.getRandom((commonLoot[loot2].getMax() - commonLoot[loot2].getMin()))));
				}
			} else {
				if (member != null) {
					member.addBossPoints(4);
					int loot = Misc.getRandom(commonLoot.length - 1);

					member.setRaidsLoot(new Item(commonLoot[loot].getId(), commonLoot[loot].getMin()
							+ Misc.getRandom((commonLoot[loot].getMax() - commonLoot[loot].getMin()))));
					int loot2 = Misc.getRandom(commonLoot.length - 1);
					member.setRaidsLootSecond(new Item(commonLoot[loot2].getId(), commonLoot[loot2].getMin()
							+ Misc.getRandom((commonLoot[loot2].getMax() - commonLoot[loot2].getMin()))));
				}
			}
		}

	}

	public RaidsReward(int id, int min, int max) {
		this.id = id;
		this.min = min;
		this.max = max;
	}

	private int id;
	private int min;
	private int max;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getMin() {
		return min;
	}

	public void setMin(int min) {
		this.min = min;
	}

	public int getMax() {
		return max;
	}

	public void setMax(int max) {
		this.max = max;
	}

	public static void announce(Player player, Item item) {
		String itemName = item.getDefinition().getName();
		String itemMessage = Misc.anOrA(itemName) + " " + itemName;
		String message = "<img=469> " + player.getUsername() + " has just received <col=6666FF>" + itemMessage
				+ "</col> from Great Olm!";
		World.sendMessage(message);
	}
}
