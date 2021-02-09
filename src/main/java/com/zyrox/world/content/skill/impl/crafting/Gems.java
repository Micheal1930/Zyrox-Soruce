package com.zyrox.world.content.skill.impl.crafting;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Optional;
import java.util.Set;

import com.zyrox.engine.task.Task;
import com.zyrox.engine.task.TaskManager;
import com.zyrox.model.Animation;
import com.zyrox.model.Skill;
import com.zyrox.world.content.Achievements;
import com.zyrox.world.content.Achievements.AchievementData;
import com.zyrox.world.content.interfaces.MakeInterface;
import com.zyrox.world.entity.impl.player.Player;

public class Gems {

	enum GEM_DATA {

		OPAL(1625, 1609, 8, 15, new Animation(886)),
		JADE(1627, 1611, 13, 20, new Animation(886)),
		RED_TOPAZ(1629, 1613, 16, 25, new Animation(887)),
		SAPPHIRE(1623, 1607, 20, 50, new Animation(888)),
		EMERALD(1621, 1605, 27, 67.5, new Animation(889)),
		RUBY(1619, 1603, 34, 85, new Animation(892)),
		DIAMOND(1617, 1601, 43, 107.5, new Animation(886)),
		DRAGONSTONE(1631, 1615, 55, 137.5, new Animation(885)),
		ONYX(6571, 6573, 67, 167.5, new Animation(885));

		GEM_DATA(int uncutGem, int cutGem, int levelReq, double xpReward, Animation animation) {
			this.uncutGem = uncutGem;
			this.cutGem = cutGem;
			this.levelReq = levelReq;
			this.xpReward = xpReward;
			this.animation = animation;
		}

		private int uncutGem, cutGem, levelReq;

		private double xpReward;

		private Animation animation;

		public int getUncutGem() {
			return uncutGem;
		}

		public int getCutGem() {
			return cutGem;
		}

		public int getLevelReq() {
			return levelReq;
		}

		public double getXpReward() {
			return xpReward;
		}

		public Animation getAnimation() {
			return animation;
		}

		public static GEM_DATA forUncutGem(int uncutGem) {
			for (GEM_DATA data : GEM_DATA.values()) {
				if (data.getUncutGem() == uncutGem)
					return data;
			}
			return null;
		}

		public static GEM_DATA forCutGem(int cutGem) {
			for (GEM_DATA data : GEM_DATA.values()) {
				if (data.getCutGem() == cutGem)
					return data;
			}
			return null;
		}
	}

	public enum Amulet {

		SAPPHIRE(1607, 1694, 24, 65, new Animation(888)),

		EMERALD(1605, 1696, 31, 70, new Animation(889)),

		RUBY(1603, 1698, 50, 85, new Animation(892)),

		DIAMOND(1601, 1700, 70, 100, new Animation(886)),

		DRAGONSTONE(1615, 1712, 80, 150, new Animation(885)),

		ONYX(6573, 6585, 90, 165, new Animation(885));

		;

		private final int id;
		private final int result;
		private final int level;
		private final int experience;
		private final Animation animation;

		Amulet(final int id, final int result, final int level, final int experience, final Animation animation) {
			this.id = id;
			this.result = result;
			this.level = level;
			this.experience = experience;
			this.animation = animation;
		}

		public int getId() {
			return id;
		}

		public int getResult() {
			return result;
		}

		public int getLevel() {
			return level;
		}

		public int getExperience() {
			return experience;
		}

		public Animation getAnimation() {
			return animation;
		}

		private static final Set<Amulet> VALUES = Collections.unmodifiableSet(EnumSet.allOf(Amulet.class));

		public static java.util.Optional<Amulet> forId(int id) {
			return VALUES.stream().filter(tabs -> tabs.id == id).findFirst();
		}
	}

	public static void selectionInterface(Player player, int gem) {
		player.getPacketSender().sendInterfaceRemoval();
		if(gemToAmulet(player, gem)) {
			return;
		}
		GEM_DATA data = GEM_DATA.forUncutGem(gem);
		if (data == null) {
			return;
		}
		if (player.getSkillManager().getMaxLevel(Skill.CRAFTING) < data.getLevelReq()) {
			player.getPacketSender()
					.sendMessage("You need a Crafting level of atleast " + data.getLevelReq() + " to craft this gem.");
			return;
		}
		/*player.setSelectedSkillingItem(gem);
		player.setInputHandling(new EnterAmountOfGemsToCut());
		player.getPacketSender().sendString(2799, ItemDefinition.forId(gem).getName())
				.sendInterfaceModel(1746, gem, 150).sendChatboxInterface(4429);
		player.getPacketSender().sendString(2800, "How many would you like to craft?");*/
		player.setSelectedSkillingItem(gem);
		MakeInterface.open(player, new int[]{data.cutGem}, MakeInterface.MakeType.GEMS);
	}

	public static void cutGem(final Player player, final int amount, final int cutGem) {
		player.getPacketSender().sendInterfaceRemoval();
		player.getSkillManager().stopSkilling();
		final GEM_DATA data = GEM_DATA.forCutGem(cutGem);
		if (data == null)
			return;
		player.setCurrentTask(new Task(2, player, true) {
			int amountCut = 0;

			@Override
			public void execute() {
				if (!player.getInventory().contains(data.uncutGem)) {
					stop();
					return;
				}
				player.performAnimation(data.getAnimation());
				player.getInventory().delete(data.uncutGem, 1);
				player.getInventory().add(cutGem, 1, "Cutting gems");
				if (data == GEM_DATA.DIAMOND) {
					Achievements.doProgress(player, AchievementData.CRAFT_1000_DIAMOND_GEMS);
				} else if (data == GEM_DATA.ONYX) {
					Achievements.finishAchievement(player, AchievementData.CUT_AN_ONYX_STONE);
				}
				player.getSkillManager().addExperience(Skill.CRAFTING, data.getXpReward() * Skill.CRAFTING.getModifier());
				amountCut++;
				if (amountCut >= amount)
					stop();
			}
		});
		TaskManager.submit(player.getCurrentTask());
	}

	public static boolean gemToAmulet(final Player player, int gem) {
		player.getPacketSender().sendInterfaceRemoval();
		player.getSkillManager().stopSkilling();
		final Optional<Amulet> data = Amulet.forId(gem);
		if (!data.isPresent()) {
			return false;
		}
		if (player.getSkillManager().getMaxLevel(Skill.CRAFTING) < data.get().getLevel()) {
			player.getPacketSender()
					.sendMessage("You need a Crafting level of atleast " + data.get().getLevel() + " to craft this gem.");
			return true;
		}
		player.setCurrentTask(new Task(2, player, true) {
			@Override
			public void execute() {
				if (!player.getInventory().contains(gem)) {
					stop();
					return;
				}
				player.performAnimation(data.get().getAnimation());
				player.getInventory().delete(gem, 1);
				if(data.get().getResult() == 1712) { 
					Achievements.doProgress(player, AchievementData.MAKE_1000_GLORYS);
				}
				player.getInventory().add(data.get().getResult(), 1, "Cutting gems");
				player.getSkillManager().addExperience(Skill.CRAFTING, data.get().getExperience() * Skill.CRAFTING.getModifier());
			}
		});
		TaskManager.submit(player.getCurrentTask());
		return true;
	}

}
