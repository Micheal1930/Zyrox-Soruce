package com.zyrox.world.content.skill.impl.fletching;

import com.zyrox.engine.task.Task;
import com.zyrox.engine.task.TaskManager;
import com.zyrox.model.Animation;
import com.zyrox.model.Item;
import com.zyrox.model.Skill;
import com.zyrox.model.input.impl.EnterAmountOfBowsToString;
import com.zyrox.world.content.Achievements.AchievementData;

import com.zyrox.world.content.Achievements;
import com.zyrox.world.content.Sounds;
import com.zyrox.world.content.Sounds.Sound;
import com.zyrox.world.content.interfaces.MakeInterface;
import com.zyrox.world.entity.impl.player.Player;
import com.zyrox.model.input.impl.EnterAmountToFletch;
import com.zyrox.model.definitions.ItemDefinition;

/**
 * Handles the Fletching skill
 * @author Gabriel Hannason
 *
 */
public class Fletching {

	/**
	 * Handles the Fletching interface
	 * @param player	The player Fletching
	 * @param log	The log to fletch
	 */
	public static void openSelection(final Player player, int log) {
		player.getSkillManager().stopSkilling();
		player.setSelectedSkillingItem(log);
		BowData shortBow = BowData.forLog(log, false);
		BowData longBow = BowData.forLog(log, true);
		if(shortBow == null || longBow == null)
			return;
		if(log == 1511) {
			player.getPacketSender().sendChatboxInterface(8880);
			player.getPacketSender().sendInterfaceModel(8884, longBow.getBowID(), 250);
			player.getPacketSender().sendInterfaceModel(8883, shortBow.getBowID(), 250);
			player.getPacketSender().sendString(8889, ""+ItemDefinition.forId(shortBow.getBowID()).getName()+"");
			player.getPacketSender().sendString(8893, ""+ItemDefinition.forId(longBow.getBowID()).getName()+"");
			player.getPacketSender().sendString(8897, "Shafts");
			player.getPacketSender().sendInterfaceModel(8885, 52, 250);
		} else {
			player.getPacketSender().sendChatboxInterface(8866);
			player.getPacketSender().sendInterfaceModel(8870, longBow.getBowID(), 250);
			player.getPacketSender().sendInterfaceModel(8869, shortBow.getBowID(), 250);
			player.getPacketSender().sendString(8874, ""+ItemDefinition.forId(shortBow.getBowID()).getName()+"");
			player.getPacketSender().sendString(8878, ""+ItemDefinition.forId(longBow.getBowID()).getName()+"");
		}
		player.setInputHandling(new EnterAmountToFletch());
	}

	/**
	 * Checks if a button that was clicked is from the Fletching interface
	 * @param player	The Player clicking a buttonbutton the player clicked
	 * @return
	 */
	public static boolean fletchingButton(final Player player, int button) {
		switch (button) {
		case 8889:
			switch (player.getSelectedSkillingItem()) {
			case 1511: //Normal log
				fletchBow(player, 48, 1);
				return true;
			}
			return false;
		case 8888:
			switch (player.getSelectedSkillingItem()) {
			case 1511: //Normal log
				fletchBow(player, 48, 5);
				return true;
			}
			return false;
		case 8887:
			switch (player.getSelectedSkillingItem()) {
			case 1511: //Normal log
				fletchBow(player, 48, 10);
				return true;
			}
			return false;
		case 49850:
			switch (player.getSelectedSkillingItem()) {
			case 1511: //Normal log
				fletchBow(player, 48, player.getInventory().getAmount(player.getSelectedSkillingItem()));
				return true;
			}
			return false;
		case 8893:
			switch (player.getSelectedSkillingItem()) {
			case 1511: //Normal log
				fletchBow(player, 50, 1);
				return true;
			}
			return false;
		case 8892:
			switch (player.getSelectedSkillingItem()) {
			case 1511: //Normal log
				fletchBow(player, 50, 5);
				return true;
			}
			return false;
		case 8891:
			switch (player.getSelectedSkillingItem()) {
			case 1511: //Normal log
				fletchBow(player, 50, 10);
				return true;
			}
			return false;
		case 49851:
			switch (player.getSelectedSkillingItem()) {
				case 1511: //Normal log
					fletchBow(player, 50, player.getInventory().getAmount(player.getSelectedSkillingItem()));
					return true;
			}
			return false;
		case 8874:
			switch (player.getSelectedSkillingItem()) {
			case 1521: //Oak log
				fletchBow(player, 56, 1);
				return true;
			case 1519: //Willow log
				fletchBow(player, 58, 1);
				return true;
			case 1517: //Maple log
				fletchBow(player, 62, 1);
				return true;
			case 1515: //Yew log
				fletchBow(player, 66, 1);
				return true;
			case 1513: //Magic logs
				fletchBow(player, 70, 1);
				return true;
			}
			return false;
		case 8873:
			switch (player.getSelectedSkillingItem()) {
			case 1521: //Oak log
				fletchBow(player, 56, 5);
				return true;
			case 1519: //Willow log
				fletchBow(player, 58, 5);
				return true;
			case 1517: //Maple log
				fletchBow(player, 62, 5);
				return true;
			case 1515: //Yew log
				fletchBow(player, 66, 5);
				return true;
			case 1513: //Magic logs
				fletchBow(player, 70, 5);
				return true;
			}
			return false;
		case 8872:
			switch (player.getSelectedSkillingItem()) {
			case 1521: //Oak log
				fletchBow(player, 56, 10);
				return true;
			case 1519: //Willow log
				fletchBow(player, 58, 10);
				return true;
			case 1517: //Maple log
				fletchBow(player, 62, 10);
				return true;
			case 1515: //Yew log
				fletchBow(player, 66, 10);
				return true;
			case 1513: //Magic logs
				fletchBow(player, 70, 10);
				return true;
			}
			return false;
		case 49853:
			switch (player.getSelectedSkillingItem()) {
			case 1521: //Oak log
				fletchBow(player, 56, 28);
				return true;
			case 1519: //Willow log
				fletchBow(player, 58, 28);
				return true;
			case 1517: //Maple log
				fletchBow(player, 62, 28);
				return true;
			case 1515: //Yew log
				fletchBow(player, 66, 28);
				return true;
			case 1513: //Magic logs
				fletchBow(player, 70, 28);
				return true;
			}
			return false;
		case 8878:
			switch (player.getSelectedSkillingItem()) {
			case 1521: //Oak log
				fletchBow(player, 54, 1);
				return true;
			case 1519: //Willow log
				fletchBow(player, 60, 1);
				return true;
			case 1517: //Maple log
				fletchBow(player, 64, 1);
				return true;
			case 1515: //Yew log
				fletchBow(player, 68, 1);
				return true;
			case 1513: //Magic logs
				fletchBow(player, 72, 1);
				return true;
			}
			return false;
		case 8877:
			switch (player.getSelectedSkillingItem()) {
			case 1521: //Oak log
				fletchBow(player, 54, 5);
				return true;
			case 1519: //Willow log
				fletchBow(player, 60, 5);
				return true;
			case 1517: //Maple log
				fletchBow(player, 64, 5);
				return true;
			case 1515: //Yew log
				fletchBow(player, 68, 5);
				return true;
			case 1513: //Magic logs
				fletchBow(player, 72, 5);
				return true;
			}
			return false;
		case 8876:
			switch(player.getSelectedSkillingItem()) {
			case 1521: //Oak log
				fletchBow(player, 54, 10);
				return true;
			case 1519: //Willow log
				fletchBow(player, 60, 10);
				return true;
			case 1517: //Maple log
				fletchBow(player, 64, 10);
				return true;
			case 1515: //Yew log
				fletchBow(player, 68, 10);
				return true;
			case 1513: //Magic logs
				fletchBow(player, 72, 10);
				return true;
			}
			return false;
		case 49854:
			switch(player.getSelectedSkillingItem()) {
			case 1521: //Oak log
				fletchBow(player, 54, 28);
				return true;
			case 1519: //Willow log
				fletchBow(player, 60, 28);
				return true;
			case 1517: //Maple log
				fletchBow(player, 64, 28);
				return true;
			case 1515: //Yew log
				fletchBow(player, 68, 28);
				return true;
			case 1513: //Magic logs
				fletchBow(player, 72, 28);
				return true;
			}
			return false;
		case 8897: //Arrow shafts
		case 8896: //Arrow shafts
		case 8895: //Arrow shafts
			if(player.getSelectedSkillingItem() == 1511) {
				int amt = button == 8897 ? 1 : button == 8896 ? 5 : 10;
				fletchBow(player, 52, amt);
				return true;
			}
			return false;

		case 49852:
			switch (player.getSelectedSkillingItem()) {
				case 1511: //Normal log
					fletchBow(player, 52, player.getInventory().getAmount(player.getSelectedSkillingItem()));
					return true;
			}
			return false;
		}
		return false;
	}

	public static void fletchBow(final Player player, final int product, final int amountToMake) {
		player.getPacketSender().sendInterfaceRemoval();
		final int log = player.getSelectedSkillingItem();
		player.getSkillManager().stopSkilling();
		player.setCurrentTask(new Task(2, player, true) {
			int amount = 0;
			@Override
			public void execute() {
				BowData bow = BowData.forBow(product);
				boolean shafts = product == 52;
				if(bow == null && !shafts || !player.getInventory().contains(log)) {
					player.performAnimation(new Animation(65535));
					stop();
					return;
				}
				if(bow != null && player.getSkillManager().getCurrentLevel(Skill.FLETCHING) < bow.getLevelReq()) {
					player.getPacketSender().sendMessage("You need a Fletching level of at least "+ bow.getLevelReq()+" to make this.");
					player.performAnimation(new Animation(65535));
					stop();
					return;
				}
				if(!player.getInventory().contains(946)) {
					player.getPacketSender().sendMessage("You need a Knife to fletch this log.");
					player.performAnimation(new Animation(65535));
					stop();
					return;
				}
				player.performAnimation(new Animation(1248));
				player.getInventory().delete(log, 1);
				player.getInventory().add(product, shafts ? 15 : 1, "Fletching");
				player.getSkillManager().addExperience(Skill.FLETCHING, (shafts ? 15 : bow.getXp()) * Skill.FLETCHING.getModifier());
				Sounds.sendSound(player, Sound.FLETCH_ITEM);
				amount++;
				if(amount >= amountToMake)
					stop();
			}
		});
		TaskManager.submit(player.getCurrentTask());
	}

	/**
	 * Bow stringing
	 */
	private static int BOW_STRING = 1777;

	public static void openBowStringSelection(Player player, int log) {
		for (final StringingData g : StringingData.values()) {
			if (log == g.unStrung()) {
				player.getSkillManager().stopSkilling();
				player.setSelectedSkillingItem(log);
				MakeInterface.open(player, new int[]{g.Strung()}, MakeInterface.MakeType.BOW_STRINGING);
				player.setInputHandling(new EnterAmountOfBowsToString());
				player.getPacketSender().sendString(2799, ItemDefinition.forId(g.Strung()).getName()).sendInterfaceModel(1746, g.Strung(), 150).sendChatboxInterface(4429);
				player.getPacketSender().sendString(2800, "How many would you like to make?");
			}
		}
	}

	public static void stringBow(final Player player, final int amount) {
		final int log = player.getSelectedSkillingItem();
		player.getSkillManager().stopSkilling();
		player.getPacketSender().sendInterfaceRemoval();
		for (final StringingData g : StringingData.values()) {
			if (log == g.unStrung()) {
				if (player.getSkillManager().getCurrentLevel(Skill.FLETCHING) < g.getLevel()) {
					player.getPacketSender().sendMessage("You need a Fletching level of at least "+ g.getLevel()+" to make this.");					
					return;
				}
				if(!player.getInventory().contains(log) || !player.getInventory().contains(BOW_STRING)) 
					return;
				player.performAnimation(new Animation(g.getAnimation()));
				player.setCurrentTask(new Task(2, player, false) {
					int amountMade = 0;
					@Override
					public void execute() {
						if(!player.getInventory().contains(log) || !player.getInventory().contains(BOW_STRING)) 
							return;
						player.getInventory().delete(BOW_STRING, 1);
						player.getInventory().delete(log, 1);
						player.getInventory().add(g.Strung(), 1, "Fletching");
						player.getPacketSender().sendMessage("You attach the Bow string on to the bow.");
						player.getSkillManager().addExperience(Skill.FLETCHING, g.getXP() * Skill.FLETCHING.getModifier());
						amountMade++;
						if(amountMade >= amount)
							stop();
					}
				});
				TaskManager.submit(player.getCurrentTask());
				break;
			}
		}
	}

	/**
	 * Arrows making
	 */
	public static int getPrimary(int item1, int item2) {
		return item1 == 52 || item1 == 53 ? item2 : item1;
	}
	public static void makeBolts(final Player player, int item1, int item2) {
		player.getSkillManager().stopSkilling();
		BoltData arr = BoltData.forArrow(getPrimary(item1, item2));
		if (arr != null) {
			if (player.getSkillManager().getCurrentLevel(Skill.FLETCHING) >= arr.getLevelReq()) {
				if (player.getInventory().getAmount(arr.getItem1()) >= 15 && player.getInventory().getAmount(arr.getItem2()) >= 15) {
					player.getInventory().delete(new Item(arr.getItem1()).setAmount(15), player.getInventory().getSlot(arr.getItem1()), true); 
					player.getInventory().delete(new Item(arr.getItem2()).setAmount(15), player.getInventory().getSlot(arr.getItem2()), true);
					player.getInventory().add(arr.getOutcome(), 15, "Fletching");
					player.getSkillManager().addExperience(Skill.FLETCHING, arr.getXp() * Skill.FLETCHING.getModifier());
					
					if(arr == BoltData.RUNE) {
					
					}
				} else {
					player.getPacketSender().sendMessage("You must have at least 15 of each supply to make arrows.");
				}
			} else {
				player.getPacketSender().sendMessage("You need a Fletching level of at least "+arr.getLevelReq()+" to fletch this.");
			}
		}
	}
	public static void makeArrows(final Player player, int item1, int item2) {
		player.getSkillManager().stopSkilling();
		ArrowData arr = ArrowData.forArrow(getPrimary(item1, item2));
		if (arr != null) {
			if (player.getSkillManager().getCurrentLevel(Skill.FLETCHING) >= arr.getLevelReq()) {
				if (player.getInventory().getAmount(arr.getItem1()) >= 15 && player.getInventory().getAmount(arr.getItem2()) >= 15) {
					player.getInventory().delete(new Item(arr.getItem1()).setAmount(15), player.getInventory().getSlot(arr.getItem1()), true); 
					player.getInventory().delete(new Item(arr.getItem2()).setAmount(15), player.getInventory().getSlot(arr.getItem2()), true);
					player.getInventory().add(arr.getOutcome(), 15, "Fletching");
					player.getSkillManager().addExperience(Skill.FLETCHING, arr.getXp() * Skill.FLETCHING.getModifier());
					Achievements.finishAchievement(player, AchievementData.FLETCH_SOME_ARROWS);
					if(arr == ArrowData.RUNE) {
						Achievements.doProgress(player, AchievementData.FLETCH_450_RUNE_ARROWS, 15);
						Achievements.doProgress(player, AchievementData.FLETCH_5000_RUNE_ARROWS, 15);
					}
				} else {
					player.getPacketSender().sendMessage("You must have at least 15 of each supply to make arrows.");
				}
			} else {
				player.getPacketSender().sendMessage("You need a Fletching level of at least "+arr.getLevelReq()+" to fletch this.");
			}
		}
	}

	public static int getTipPrimary(int item1, int item) {
		return item1 == 1755 ? item : item1;
	}

	public static void makeTip(final Player player, int item1, int item2) {
		player.getSkillManager().stopSkilling();
		BoltTipData tip = BoltTipData.forTip(getTipPrimary(item1, item2));
		if (tip != null) {
			if (player.getSkillManager().getCurrentLevel(Skill.FLETCHING) >= tip.getLevelReq()) {
				if (player.getInventory().getAmount(tip.getItem()) >= 1) {
					player.getInventory().delete(new Item(tip.getItem()).setAmount(1),
							player.getInventory().getSlot(tip.getItem()), true);
					player.getInventory().add(tip.getOutcome(), 15);
					player.getSkillManager().addExperience(Skill.FLETCHING, (tip.getXp()) * Skill.FLETCHING.getModifier());
				} else {
					player.getPacketSender().sendMessage("You must have at least 15 of each supply to make bolts.");
				}
			} else {
				player.getPacketSender().sendMessage(
						"You need a Fletching level of at least " + tip.getLevelReq() + " to fletch this.");
			}
		}
	}

	/**
	 * Bolts making
	 */
	public static int getPrimaryDarts(int item1, int item2) {
		return item1 == 314 || item1 == 314 ? item2 : item1;
	}

	public static void makeDarts(final Player player, int item1, int item2) {
		player.getSkillManager().stopSkilling();
		DartData dart = DartData.forDart(getPrimaryDarts(item1, item2));

		if (dart != null) {
			if (player.getSkillManager().getCurrentLevel(Skill.FLETCHING) >= dart.getLevelReq()) {
				if (player.getInventory().getAmount(dart.getItem1()) >= 10
						&& player.getInventory().getAmount(dart.getItem2()) >= 10) {
					player.getInventory().delete(new Item(dart.getItem1()).setAmount(10),
							player.getInventory().getSlot(dart.getItem1()), true);
					player.getInventory().delete(new Item(dart.getItem2()).setAmount(10),
							player.getInventory().getSlot(dart.getItem2()), true);
					player.getInventory().add(dart.getOutcome(), 10);
					player.getSkillManager().addExperience(Skill.FLETCHING, dart.getXp() * Skill.FLETCHING.getModifier());

				} else {
					player.getPacketSender().sendMessage("You must have at least 10 of each supply to make darts.");
				}
			} else {
				player.getPacketSender().sendMessage(
						"You need a Fletching level of at least " + dart.getLevelReq() + " to fletch this.");
			}
		}
	}
}
