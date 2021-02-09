package com.zyrox.world.content;

import java.util.Collections;
import java.util.Comparator;

import com.zyrox.model.definitions.NpcDefinition;
import com.zyrox.world.entity.impl.player.Player;

public class KillsTracker {

	//Grahaminterface
	public static boolean bossOpen = false;
	
	public static int numBosses = 0;

	public static void submit(Player player, KillsEntry[] kills) {
		for(KillsEntry kill : kills) {
			if(kill != null)
				submit(player, kill);
		}
	}

	public static void submit(Player player, KillsEntry kill) {
		int index = getIndex(player, kill);
		if(index >= 0) {
			player.getKillsTracker().get(index).amount += kill.amount;
		} else {
			player.getKillsTracker().add(kill);
		}
		if(player.isKillsTrackerOpen()) {
			//open(player);
		}
	}
	//Grahamnterface
	public static void openBoss(Player player) {
		try {
			/* RESORT THE KILLS */
			Collections.sort(player.getKillsTracker(), new Comparator<KillsEntry>() {
				@Override
				public int compare(KillsEntry kill1, KillsEntry kill2) {
					if(kill1.boss && !kill2.boss) {
						return -1;
					}
					if(kill2.boss && !kill1.boss) {
						return 1;
					}
					if(kill1.boss && kill2.boss || !kill1.boss && !kill2.boss) {
						if(kill1.amount > kill2.amount) {
							return -1;
						} else if(kill2.amount > kill1.amount) {
							return 1;
						} else {
							if(kill1.npcName.compareTo(kill2.npcName) > 0) {
								return 1;
							} else {
								return -1;
							}
						}
					}
					return 0;
				}
			});
			/* SHOW THE INTERFACE */
			player.setKillsTrackerOpen(true);
			resetInterface(player);
			player.getPacketSender().sendInterface(71000);
			int index = -1;
			index++;	
			for(KillsEntry entry : player.getKillsTracker()) {
				if(71017+index >= 71167)
					break;
				if(entry.boss) {
				player.getPacketSender().sendString(71017+index, ""+entry.npcName);
				player.getPacketSender().sendString(71018+index, ""+entry.amount);
				if(index == 0) {
					NpcDefinition def = NpcDefinition.forName(entry.npcName);
					player.getPacketSender().sendNpcOnInterface(def.getId(), 1500);
					player.getPacketSender().sendString(71005, ""+entry.npcName);
					player.getPacketSender().sendString(71010, "Killcount: "+entry.amount);
				}	
				index += 3;
				}
			}
			bossOpen = true;
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	//Grahaminterface
	public static boolean openNpc(Player player, int button) {
		if(button >= 71016 && button <= 71316) {
			int index = 0;
			int stop = 0;
			numBosses = 0;
			for(KillsEntry entry : player.getKillsTracker()) {
				stop = 71016 + (getIndex(player, entry) * 3);
				countBoss(player, entry, index);
				if(stop == (button + (!bossOpen ? (numBosses * 3) : 0))) {
					NpcDefinition def = NpcDefinition.forName(entry.npcName);
					player.getPacketSender().sendNpcOnInterface(def.getId(), 1500);
					player.getPacketSender().sendString(71005, ""+entry.npcName);
					player.getPacketSender().sendString(71010, "Killcount: "+entry.amount);
				}
				index++;
			}
			return true;
		}
		return false;
	}

	public static int getCount(Player player, KillsEntry kill) {
		int index = getIndex(player, kill);
		if(index == -1) {
			return 0;
		}
		int amount = player.getKillsTracker().get(index).amount;
		return amount;
	}
 
	//Grahaminterface
	public static void open(Player player) {
		try {
			/* RESORT THE KILLS */
			Collections.sort(player.getKillsTracker(), new Comparator<KillsEntry>() {
				@Override
				public int compare(KillsEntry kill1, KillsEntry kill2) {
					if(kill1.boss && !kill2.boss) {
						return -1;
					}
					if(kill2.boss && !kill1.boss) {
						return 1;
					}
					if(kill1.boss && kill2.boss || !kill1.boss && !kill2.boss) {
						if(kill1.amount > kill2.amount) {
							return -1;
						} else if(kill2.amount > kill1.amount) {
							return 1;
						} else {
							if(kill1.npcName.compareTo(kill2.npcName) > 0) {
								return 1;
							} else {
								return -1;
							}
						}
					}
					return 0;
				}
			});
			/* SHOW THE INTERFACE */
			player.setKillsTrackerOpen(true);
			resetInterface(player);
			player.getPacketSender().sendInterface(71000);
			int index = -1;
			index++;	
			for(KillsEntry entry : player.getKillsTracker()) {
				if(entry.boss)
					continue;
				if(71017+index >= 71167)
					break;
				player.getPacketSender().sendString(71017+index, ""+entry.npcName);
				player.getPacketSender().sendString(71018+index, ""+entry.amount);
				if(index == 0) {
					NpcDefinition def = NpcDefinition.forName(entry.npcName);
					player.getPacketSender().sendNpcOnInterface(def.getId(), 1500);
					player.getPacketSender().sendString(71005, ""+entry.npcName);
					player.getPacketSender().sendString(71010, "Killcount: "+entry.amount);
				}	
				index += 3;
			}
			bossOpen = false;
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	//Grahaminterface
	public static void resetInterface(Player player) {
		for(int i = 71017; i < 71317; i++) {
			player.getPacketSender().sendString(i, "");
		}
	}

	public static int getIndex(Player player, KillsEntry kill) {
		for(int i = 0; i < player.getKillsTracker().size(); i++) {
			if(player.getKillsTracker().get(i).npcName.equals(kill.npcName)) {
				return i;
			}
		}
		return -1;
	}
	
	//Grahaminterface
	public static int countBoss(Player player, KillsEntry kill, int i) {
		if(player.getKillsTracker().get(i).boss) {
			if(player.getKillsTracker().get(i).npcName.equals(kill.npcName)) {
				numBosses += 1;
			}
		}
		return -1;
	}

	public static class KillsEntry {

		public KillsEntry(String npcName, int amount, boolean boss) {
			this.npcName = npcName;
			this.amount = amount;
			this.boss = boss;
		}

		public String npcName;
		public int amount;
		public boolean boss;
	}

}
