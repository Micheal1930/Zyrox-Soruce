package com.varrock.world.content.skill.impl.woodcutting;

import java.util.HashMap;
import java.util.Map;

import com.varrock.model.Skill;
import com.varrock.model.container.impl.Equipment;
import com.varrock.world.entity.impl.player.Player;

public class WoodcuttingData {
	
	public static enum Hatchet {
		BRONZE(1351, 1, 879, 1.0),
		IRON(1349, 1, 877, 1.3),
		STEEL(1353, 6, 875, 1.5),
		BLACK(1361, 6, 873, 1.7),
		MITHRIL(1355, 21, 871, 1.9),
		ADAMANT(1357, 31, 869, 2),
		RUNE(1359, 41, 867, 3),
		DRAGON(6739, 61, 2846, 4),
		ADZE(13661, 61, 10227, 4),
		INFERNAL(43241, 90, 2876, 4),
		THIRDAGE(50011, 99, 2876, 5);

		private int id, req, anim;
		private double speed;

		private Hatchet(int id, int level, int animation, double speed) {
			this.id = id;
			this.req = level;
			this.anim = animation;
			this.speed = speed;
		}

		public static Map<Integer, Hatchet> hatchets = new HashMap<Integer, Hatchet>();


		public static Hatchet forId(int id) {
			return hatchets.get(id);
		}

		static {
			for(Hatchet hatchet : Hatchet.values()) {
				hatchets.put(hatchet.getId(), hatchet);
			}
		}

		public int getId() {
			return id;
		}

		public int getRequiredLevel() {
			return req;
		}

		public int getAnim() {
			return anim;
		}
		
		public double getSpeed() {
			return speed;
		}
	}

	public static enum Trees {

		NORMAL(1, 25, 1511, new int[] { 101276, 101278, 101277, 1276, 1277, 1278, 1279, 1280, 1282, 1283, 1284, 1285, 1286, 1289, 1290, 1291, 1315, 1316, 1318, 1319, 1330, 1331, 1332, 1365, 1383, 1384, 3033, 3034, 3035, 3036, 3881, 3882, 3883, 5902, 5903, 5904 }, 4, false),
		ACHEY(1, 25, 2862, new int[] { 2023 }, 4, false),
		OAK(15, 37.5, 1521, new int[] { 101751, 1281, 3037 }, 5, true),
		WILLOW(30, 67.5, 1519, new int[] { 101758, 101760, 101756, 1308, 5551, 5552, 5553 }, 6, true),
		TEAK(35, 85, 6333, new int[] { 9036 }, 7, true),
		DRAMEN(36, 86.5, 771, new int[] { 1292 }, 7, true),
		MAPLE(45, 100, 1517, new int[] { 101759, 1307, 4677 }, 7, true),
		MAHOGANY(50, 125, 6332, new int[] { 9034 }, 7, true),
		YEW(60, 175, 1515, new int[] { 1309, 101753 }, 8, true),
		MAGIC(75, 250, 1513, new int[] { 1306, 101761 }, 9, true),
		EVIL_TREE(80, 285, 14666, new int[] { 11434 }, 9, true),
		WHITE_TREE(99, 300, 49669, new int[] { 61551 }, 9, true), // Arthur's dream
		CHRISTMAS_TREE(1, 25, 6051, new int[] { Woodcutting.AFK_TREE }, 9, true), // Christmas tree for event
		GOLD_MAGIC_TREE(99, 400, 51247, new int[] { 11435 }, 9, true), // GOLD+ DONATORS ONLY
		EVIL_MAGIC_TREE(99, 400, 43355, new int[] { 11921 }, 9, true); // RUBY+ DONATORS ONLY

		private int[] objects;
		private int req, log, ticks;
		private double xp;
		private boolean multi;

		private Trees(int req, double xp, int log, int[] obj, int ticks, boolean multi) {
			this.req = req;
			this.xp = xp;
			this.log = log;
			this.objects = obj;
			this.ticks = ticks;
			this.multi = multi;
		}
		
		public boolean isMulti() {
			return multi;
		}
		
		public int getTicks() {
			return ticks;
		}

		public int getReward() {
			return log;
		}

		public double getXp() {
			return xp;
		}

		public int getReq() {
			return req;
		}

		private static final Map<Integer, Trees> tree = new HashMap<Integer, Trees>();

		public static Trees forId(int id) {
			return tree.get(id);
		}

		public static boolean isTree(int id) {
			return tree.get(id) != null;
		}

		static {
			for (Trees t : Trees.values()) {
				for (int obj : t.objects) {
					tree.put(obj, t);
				}
			}
		}
	}

	public static int getHatchet(Player p) {
		for (Hatchet h : Hatchet.values()) {
			if (p.getEquipment().getItems()[Equipment.WEAPON_SLOT].getId() == h.getId()) {
				return h.getId();
			} else if (p.getInventory().contains(h.getId())) {
				return h.getId();
			}
		}
		return -1;
	}

	public static int getChopTimer(Player player, Hatchet h) {
		int skillReducement = (int) (player.getSkillManager().getMaxLevel(Skill.WOODCUTTING) * 0.05);
		int axeReducement = (int) Math.round(h.getSpeed());
		return skillReducement + axeReducement;
	}
}
