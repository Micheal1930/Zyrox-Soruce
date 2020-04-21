package com.varrock.world.content.skill.impl.fletching;

public enum DartData {
	
	BRONZE(819, 314, 806, 2, 10),
	IRON(820, 314, 807, 4, 22),
	STEEL(821, 314, 808, 8, 37),
	MITHRIL(822, 314, 809, 11, 52),
	ADAMANT(823, 314, 810, 15, 67),
	RUNE(824, 314, 811, 19, 81),
	DRAGON(11232, 314, 11230, 25, 95);

	public int item1, item2, outcome, xp, levelReq;

	private DartData(int item1, int item2, int outcome, int xp, int levelReq) {
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

	public int getXp() {
		return xp;
	}

	public int getLevelReq() {
		return levelReq;
	}

	public static DartData forDart(int id) {
		for(DartData ar : DartData.values()) {
			if(ar.getItem1() == id) {
				return ar;
			}
		}
		return null;
	}
}
