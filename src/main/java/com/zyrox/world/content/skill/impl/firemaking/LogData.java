package com.zyrox.world.content.skill.impl.firemaking;

import com.zyrox.model.definitions.ItemDefinition;
import com.zyrox.world.entity.impl.player.Player;


public class LogData {

	public static enum Log {
		
		LOG(1511, 1, 40, 30),
		ACHEY(2862, 1, 40, 30),
		OAK(1521, 15, 60, 40),
		WILLOW(1519, 30, 90, 45),
		TEAK(6333, 35, 105, 45),
		ARCTIC_PINE(10810, 42, 125, 45),
		MAPLE(1517, 45, 135, 45),
		MAHOGANY(6332, 50, 157.5, 45),
		EUCALYPTUS(12581, 58, 170, 45),
		YEW(1515, 60, 202.5, 50),
		KINDLINGS(14666, 50, 157.5, 50),
		MAGIC(1513, 75, 303.8, 50);
		
		private int logId, level, burnTime;
		private double xp;
		
		private Log(int logId, int level, double xp, int burnTime) {
			this.logId = logId;
			this.level = level;
			this.xp = xp;
			this.burnTime = burnTime;
		}
		
		public int getLogId() {
			return logId;
		}
		
		public int getLevel() {
			return level;
		}
		
		public double getXp() {
			return xp;
		}		
		
		public int getBurnTime() {
			return this.burnTime;
		}
	}

	public static Log getLogForLogId(Player p, int log) {
		for (final Log l : Log.values()) {
			if(log == l.getLogId()) {
				return l;
			}
		}
		return null;
	}

	public static int getFirstLogInInventory(Player p, boolean canBeNoted) {
		for (final Log l : Log.values()) {
			if(p.getInventory().contains(l.getLogId())) {
				return l.getLogId();
			}
			if(canBeNoted) {
				if (p.getInventory().contains(ItemDefinition.forId(l.getLogId()).getNotedId())) {
					return ItemDefinition.forId(l.getLogId()).getNotedId();
				}
			}
		}
		return -1;
	}

}