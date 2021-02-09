package com.zyrox.world.content.combat.magic;

import com.zyrox.model.Item;

public class Enchantables {

	private final static int ASTRAL = 9075;
	private final static int DEATH = 560;
	private final static int BLOOD = 565;
	private final static int WRATH = 51880;
	private final static int WATER = 555;
	private final static int AIR = 556;
	private final static int EARTH = 557;
	private final static int FIRE = 554;
	private final static int MIND = 558;

	private final static int NATURE = 561;
	private final static int COSMIC = 564;
	private final static int CHAOS = 562;
	private final static int LAW = 563;
	private final static int SOUL = 566;

	public enum Bolts {

		OPAL(MagicSpells.SAPPHIRE, 879, 9236, 4, new Item[] { new Item(COSMIC, 1), new Item(AIR, 2) }, 9), //
		SAPPHIRE(MagicSpells.SAPPHIRE, 9337, 9240, 7,
				new Item[] { new Item(COSMIC, 1), new Item(MIND, 1), new Item(WATER, 1) }, 17), //
		JADE(MagicSpells.EMERALD, 9335, 9237, 14, new Item[] { new Item(COSMIC, 1), new Item(EARTH, 2) }, 19), //
		EMERALD(MagicSpells.EMERALD, 9338, 9241, 27,
				new Item[] { new Item(NATURE, 1), new Item(COSMIC, 1), new Item(AIR, 3) }, 37), //
		TOPAZ(MagicSpells.RUBY, 9336, 9239, 29, new Item[] { new Item(COSMIC, 1), new Item(FIRE, 2) }, 33), //
		RUBY(MagicSpells.RUBY, 9339, 9242, 49,
				new Item[] { new Item(BLOOD, 1), new Item(COSMIC, 1), new Item(FIRE, 5) }, 59), //
		DIAMOND(MagicSpells.DIAMOND, 9340, 9243, 57,
				new Item[] { new Item(LAW, 2), new Item(COSMIC, 1), new Item(EARTH, 10) }, 67), //
		DRAGONSTONE(MagicSpells.DRAGONSTONE, 9341, 9244, 68,
				new Item[] { new Item(SOUL, 1), new Item(COSMIC, 1), new Item(EARTH, 15) }, 78), //
		ONYX(MagicSpells.ONYX, 9342, 9245, 87,
				new Item[] { new Item(DEATH, 1), new Item(COSMIC, 1), new Item(FIRE, 20) }, 97), //

		;
		Bolts(MagicSpells spell, int item, int product, int lvl, Item[] runes, int experience) {
			this.spell = spell;
			this.item = item;
			this.product = product;
			this.lvl = lvl;
			this.runes = runes;
			this.experience = experience;
		}

		private int item, lvl, experience, product;
		private MagicSpells spell;
		private Item[] runes;

		public int getItem() {
			return item;
		}

		public int getLvl() {
			return lvl;
		}

		public int getExperience() {
			return experience;
		}

		public int getProduct() {
			return product;
		}

		public MagicSpells getSpell() {
			return spell;
		}

		public Item[] getRunes() {
			return runes;
		}

	}

	public enum Sapphire {

		RING(1637, 2550), //
		NECKLACE(1656, 3853), //
		AMULET(1694, 1727), //
		BRACELET(11072, 11074), //
		;
		Sapphire(int itemNeeded, int product) {
			this.itemNeeded = itemNeeded;
			this.product = product;
		}

		private int itemNeeded, product;

		public int getItemNeeded() {
			return itemNeeded;
		}

		public int getProduct() {
			return product;
		}

	}

	public enum Emerald {

		RING(1639, 2552), //
		NECKLACE(1658, 5521), //
		AMULET(1696, 1729), //
		BRACELET(11076, 11079), //
		;
		Emerald(int itemNeeded, int product) {
			this.itemNeeded = itemNeeded;
			this.product = product;
		}

		private int itemNeeded, product;

		public int getItemNeeded() {
			return itemNeeded;
		}

		public int getProduct() {
			return product;
		}

	}

	public enum Ruby {

		RING(1641, 2568), //
		NECKLACE(1660, 11194), //
		AMULET(1698, 1725), //
		BRACELET(11085, 11088), //
		;
		Ruby(int itemNeeded, int product) {
			this.itemNeeded = itemNeeded;
			this.product = product;
		}

		private int itemNeeded, product;

		public int getItemNeeded() {
			return itemNeeded;
		}

		public int getProduct() {
			return product;
		}

	}

	public enum Diamond {

		RING(1643, 2570), //
		NECKLACE(1662, 11090), //
		AMULET(1700, 1731), //
		BRACELET(11092, 11095), //
		;
		Diamond(int itemNeeded, int product) {
			this.itemNeeded = itemNeeded;
			this.product = product;
		}

		private int itemNeeded, product;

		public int getItemNeeded() {
			return itemNeeded;
		}

		public int getProduct() {
			return product;
		}

	}

	public enum Dragonstone {

		RING(1645, 2572), //
		NECKLACE(1664, 11105), //
		AMULET(1702, 1712), //
		BRACELET(11115, 11118), //
		;
		Dragonstone(int itemNeeded, int product) {
			this.itemNeeded = itemNeeded;
			this.product = product;
		}

		private int itemNeeded, product;

		public int getItemNeeded() {
			return itemNeeded;
		}

		public int getProduct() {
			return product;
		}

	}

	public enum Onyx {

		RING(6575, 6583), //
		NECKLACE(6577, 11128), //
		AMULET(6581, 6585), //
		BRACELET(11130, 11133), //
		;
		Onyx(int itemNeeded, int product) {
			this.itemNeeded = itemNeeded;
			this.product = product;
		}

		private int itemNeeded, product;

		public int getItemNeeded() {
			return itemNeeded;
		}

		public int getProduct() {
			return product;
		}

	}

	public enum Zenyte {

		RING(49538, 49550), //
		NECKLACE(49535, 49547), //
		AMULET(49541, 49553), //
		BRACELET(49532, 49544), //
		;
		Zenyte(int itemNeeded, int product) {
			this.itemNeeded = itemNeeded;
			this.product = product;
		}

		private int itemNeeded, product;

		public int getItemNeeded() {
			return itemNeeded;
		}

		public int getProduct() {
			return product;
		}

	}

}
