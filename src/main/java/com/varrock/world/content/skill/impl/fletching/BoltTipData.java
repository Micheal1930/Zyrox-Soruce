package com.varrock.world.content.skill.impl.fletching;

public enum BoltTipData {

	JADE(1611, 9187, 2, 9), TOPAZ(1613, 9188, 4, 15), SAPPHIRE(1607, 9189, 5, 54), EMERALD(1605, 9190, 11,
			57), RUBY(1603, 9191, 14, 61), DIAMOND(1601, 9192, 1, 63), DRAGON(1615, 9193, 20, 71);

	public int item, outcome, xp, levelReq;

	private BoltTipData(int item, int outcome, int xp, int levelReq) {
		this.item = item;
		this.outcome = outcome;
		this.xp = xp;
		this.levelReq = levelReq;
	}

	public int getOutcome() {
		return outcome;
	}

	public int getItem() {
		return item;
	}

	public int getXp() {
		return xp;
	}

	public int getLevelReq() {
		return levelReq;
	}

	public static BoltTipData forTip(int id) {
		for (BoltTipData tip : BoltTipData.values()) {
			if (tip.getItem() == id) {
				return tip;
			}
		}
		return null;
	}
}
