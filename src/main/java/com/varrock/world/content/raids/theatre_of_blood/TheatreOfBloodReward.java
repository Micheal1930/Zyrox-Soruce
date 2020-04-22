package com.varrock.world.content.raids.theatre_of_blood;

import com.varrock.model.Item;
import com.varrock.model.container.impl.Equipment;
import com.varrock.model.definitions.NPCDrops.NpcDropItem;
import com.varrock.util.Misc;
import com.varrock.world.World;
import com.varrock.world.entity.impl.player.Player;

public class TheatreOfBloodReward {

	public static int[] ExtremelyRares = new int[] { 15000 };

	public static int[] VeryRares = new int[] { 15001, 52323, 52326, 52327, 52328 };

	public static int[] SuperRares = new int[] { 52322 };

	public static int[] UNCOMMON_REWARDS = new int[] { 246, 1374, 1128, 1114, 5289, 5315, 5316, 1776, 560, 565, 1939, 3139, 1392, 450, 452, 454, 445, 208, 3050, 210, 212, 214, 218, 3052, 216, 2486, 220 };

	public static void grantLoot(TheatreOfBloodParty party) {
		Player rareLootPlayer = null;

		if (Misc.randomFloat() <= .002) {
			int random = Misc.getRandom(party.getPartyMembers().size() - 1);
			rareLootPlayer = party.getPartyMembers().get(random);
			double randomNumber = Misc.randomFloat();

			boolean raidsCape = rareLootPlayer.getEquipment().get(Equipment.CAPE_SLOT).getId() == 21003;

			double modifier = 1.2; // 20% modifier

			if (randomNumber < .55) {
				rareLootPlayer.setRaidsLoot(new Item(SuperRares[Misc.getRandom(SuperRares.length - 1)]));
			} else if (randomNumber < .94) {
				rareLootPlayer.setRaidsLoot(new Item(VeryRares[Misc.getRandom(VeryRares.length - 1)]));
			} else {
				rareLootPlayer.setRaidsLoot(new Item(ExtremelyRares[Misc.getRandom(ExtremelyRares.length - 1)]));
			}
			rareLootPlayer.rareRaidLoot = true;
			int loot = Misc.getRandom(UNCOMMON_REWARDS.length - 1);
			rareLootPlayer.setRaidsLootSecond(new Item(UNCOMMON_REWARDS[loot], Misc.inclusiveRandom(35, 60)));
		}

		for (Player member : party.getPartyMembers()) {
			if (member != null) {
				member.addBossPoints(4);
			}
			if (rareLootPlayer != null) {
				if (member != null && !member.equals(rareLootPlayer)) {
					int loot = Misc.getRandom(UNCOMMON_REWARDS.length - 1);

					member.setRaidsLoot(new Item(UNCOMMON_REWARDS[loot], Misc.inclusiveRandom(35, 60)));
					member.setRaidsLootSecond(new Item(UNCOMMON_REWARDS[loot], Misc.inclusiveRandom(35, 60)));
				}
			} else {
				if (member != null) {
					member.addBossPoints(4);
					int loot = Misc.getRandom(UNCOMMON_REWARDS.length - 1);

					member.setRaidsLoot(new Item(UNCOMMON_REWARDS[loot], Misc.inclusiveRandom(35, 60)));
					member.setRaidsLootSecond(new Item(UNCOMMON_REWARDS[loot], Misc.inclusiveRandom(35, 60)));
				}
			}
		}

	}

	public TheatreOfBloodReward(int id, int min, int max) {
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
		String message = "<img=483> " + player.getUsername() + " has just received <col=6666FF>" + itemMessage
				+ "</col> from Verzik Vitur!";
		World.sendMessage(message);
	}
}
