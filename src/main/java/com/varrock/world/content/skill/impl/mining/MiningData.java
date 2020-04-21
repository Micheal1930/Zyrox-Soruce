package com.varrock.world.content.skill.impl.mining;

import com.varrock.GameSettings;
import com.varrock.model.Skill;
import com.varrock.model.container.impl.Equipment;
import com.varrock.util.Misc;
import com.varrock.world.entity.impl.player.Player;

public class MiningData {

	public static final int[] RANDOM_GEMS = {1623,1621,1619,1617,1631};

	public static enum Pickaxe {

		Bronze(1265, 1, 625, 1.0),
		Iron(1267, 1, 626, 1.05),
		Steel(1269, 6, 627, 1.1),
		Mithril(1273, 21, 628, 1.2),
		Adamant(1271, 31, 629, 1.25),
		Rune(1275, 41, 624, 1.3),
		Dragon(15259, 61, 12188, 1.50),
		Adze(13661, 41, 10226, 1.60),
		ThirdAge(50014, 99, 7281 + GameSettings.OSRS_ANIM_OFFSET, 2.0);

		private int id, req, anim;
		private double speed;
		
		private Pickaxe(int id, int req, int anim, double speed) {
			this.id = id;
			this.req = req;
			this.anim = anim;
			this.speed = speed;
		}

		public int getId() {
			return id;
		}
		public int getReq() {
			return req;
		}

		public int getAnim() {
			return anim;
		}
		
		public double getSpeed() {
			return this.speed;
		}
	}

	public static Pickaxe forPick(int id) {
		for (Pickaxe p : Pickaxe.values()) {
			if (p.getId() == id) {
				return p;
			}
		}
		return null;
	}

	public static enum Ores {

		RUNE_ESSENCE(new int[]{24444}, 1, 5, 1436, 2, -1),

		PURE_ESSENCE(new int[]{24445}, 17, 5, 7936, 3, -1),
		
		CLAY(new int[]{9711, 9712, 9713, 15503, 15504, 15505}, 1, 5, 434, 3, 2),

		COPPER(new int[]{9708, 9709, 9710, 11936, 11960, 11961, 11962, 11189, 11190, 11191, 29231, 29230, 2090, 107484}, 1, 17.5, 436, 4, 4),

		TIN(new int[]{9714, 9715, 9716, 11933, 11957, 11958, 11959, 11186, 11187, 11188, 2094, 29227, 29229, 107485}, 1, 17.5, 438, 4, 4),

		IRON(new int[]{107_488, 9717, 9718, 9719, 2093, 2092, 11954, 11955, 11956, 29221, 29222, 29223, 107455}, 15, 35, 440, 5, 5),

		SILVER(new int[]{107490, 107485, 2100, 2101, 29226, 29225, 11948, 11949}, 20, 40, 442, 5, 7),

		COAL(new int[]{2097, 5770, 29216, 29215, 29217, 11965, 11964, 11963, 11930, 11931, 11932, 107489}, 30, 50, 453, 5, 7),

		GOLD(new int[]{9720, 9721, 9722, 11951, 11183, 11184, 11185, 2099, 107491}, 40, 65, 444, 5, 10),

		MITHRIL(new int[]{25370, 25368, 5786, 5784, 11942, 11943, 11944, 11945, 11946, 29236, 11947, 11942, 11943, 107459, 107492}, 50, 80, 447, 6, 11),

		ADAMANTITE(new int[]{11941, 11939, 29233, 29235, 107493}, 70, 95, 449, 7, 14),

		RUNITE(new int[]{14860, 14859, 4860, 2106, 2107, 107494}, 85, 125, 451, 7, 45),

		CRYSTAL_ROCK(new int[]{16284}, 1, 150, 51540, 3, -1),

		CRASHED_STAR(new int[]{38660}, 80, 160, 13727, 7, -1);


		private int objId[];
		private int itemid;
		private int req;
		private double xp;
		private int ticks;
		private int respawnTimer;
		
		private Ores(int[] objId, int req, double xp, int itemid, int ticks, int respawnTimer) {
			this.objId = objId;
			this.req = req;
			this.xp = xp;
			this.itemid = itemid;
			this.ticks = ticks;
			this.respawnTimer = respawnTimer;
		}

		public int getRespawn() {
			return respawnTimer;
		}

		public int getLevelReq(){
			return req;
		}

		public double getXpAmount(){
			return xp;
		}

		public int getItemId(){
			return itemid;
		}

		public int getTicks() {
			return ticks;
		}

		public String getName() {
			return Misc.capitalize(name().toLowerCase().replaceAll("_", " "));
		}
	}

	public static Ores forRock(int id) {
		for (Ores ore : Ores.values()) {
			for (int obj : ore.objId) {
				if (obj == id) {
					return ore;
				}
			}
		}
		return null;
	}

	public static boolean isRock(int id) {
		for (Ores ore : Ores.values()) {
			for (int obj : ore.objId) {
				if (obj == id) {
					return true;
				}
			}
		}
		return false;
	}

	public static int getPickaxe(final Player plr) {
		for (Pickaxe p : Pickaxe.values()) {
			if (plr.getEquipment().getItems()[Equipment.WEAPON_SLOT].getId() == p.getId())
				return p.getId();
			else if (plr.getInventory().contains(p.getId()))
				return p.getId();
		}
		return -1;
	}
	
	public static int getReducedTimer(final Player plr, Pickaxe pickaxe) {
		int skillReducement = (int) (plr.getSkillManager().getMaxLevel(Skill.MINING) * 0.03);
		int pickaxeReducement = (int) pickaxe.getSpeed();
		return skillReducement + pickaxeReducement;
	}
}