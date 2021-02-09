package com.zyrox.world.content.skill.impl.crafting;


public enum BattleStaffData {
	
		AIR(1391, 573, 1397, 137.5, 66),
		WATER(1391, 571, 1395, 100, 54),
		EARTH(1391, 575, 1399, 112.5, 58),
		FIRE(1391, 569, 1393, 125, 62);

		public int item1, item2, outcome, levelReq;

		public double xp;

		BattleStaffData(int item1, int item2, int outcome, double xp, int levelReq) {
			this.item1 = item1;
			this.item2 = item2;
			this.outcome = outcome;
			this.xp = xp;
			this.levelReq = levelReq;
		}
		public int getItem1() {
			return item1;
		}

		public int getItem2() {
			return item2;
		}

		public int getOutcome() {
			return outcome;
		}

		public double getXp() {
			return xp;
		}

		public int getLevelReq() {
			return levelReq;
		}

		public static BattleStaffData forStaff(int id) {
			for (BattleStaffData ar : BattleStaffData.values()) {
				if (ar.getItem2() == id) {
					return ar;
				}
			}
			return null;
		}
}
