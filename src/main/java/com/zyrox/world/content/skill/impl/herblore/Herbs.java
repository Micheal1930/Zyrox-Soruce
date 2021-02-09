package com.zyrox.world.content.skill.impl.herblore;

public enum Herbs {
	
	GUAM(199, 249, 1, 2.5),
	MARRENTILL(201, 251, 5, 3.8),
	TARROMIN(203, 253, 11, 5),
	HARRALANDER(205, 255, 20, 6.3),
	RANARR(207, 257, 25, 7.5),
	TOADFLAX(3049, 2998, 30, 8),
	SPIRITWEED(12174, 12172, 35, 8.5),
	IRIT(209, 259, 40, 8.8),
	WERGALI(14836, 14854, 30, 8.3),
	AVANTOE(211, 261, 48, 10),
	KWUARM(213, 263, 54, 11.3),
	SNAPDRAGON(3051, 3000, 59, 11.8),
	CADANTINE(215, 265, 65, 12.5),
	LANTADYME(2485, 2481, 67, 13.1),
	DWARFWEED(217, 267, 70, 13.8),
	TORSTOL(219, 269, 75, 15);
	
	private final int grimyHerb, cleanHerb, levelReq;

	private final double cleaningExp;
	
	private Herbs(int grimyHerb, int cleanHerb, int levelReq, double cleaningExp) {
		this.grimyHerb = grimyHerb;
		this.cleanHerb = cleanHerb;
		this.levelReq = levelReq;
		this.cleaningExp = cleaningExp;
	}
	
	public int getGrimyHerb() {
		return grimyHerb;
	}
	
	public int getCleanHerb() {
		return cleanHerb;
	}
	
	public int getLevelReq() {
		return levelReq;
	}
	
	public double getExp() {
		return cleaningExp;
	}
	
	public static Herbs forId(int herbId){
		for(Herbs herb : Herbs.values()) {
			if (herb.getGrimyHerb() == herbId) {
				return herb;
			}
		}
		return null;
	}
	
}