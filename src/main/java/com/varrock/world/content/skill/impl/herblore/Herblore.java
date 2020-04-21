package com.varrock.world.content.skill.impl.herblore;

import com.varrock.engine.task.Task;
import com.varrock.engine.task.TaskManager;
import com.varrock.model.Animation;
import com.varrock.model.Item;
import com.varrock.model.Skill;
import com.varrock.model.definitions.ItemDefinition;
import com.varrock.model.input.Input;
import com.varrock.model.input.impl.EnterAmountOfFinishedPotionsToMake;
import com.varrock.model.input.impl.EnterAmountOfSpecialPotionsToMake;
import com.varrock.model.input.impl.EnterAmountOfUnfPotionsToMake;
import com.varrock.util.Misc;
import com.varrock.world.content.Achievements;
import com.varrock.world.content.Achievements.AchievementData;
import com.varrock.world.content.interfaces.MakeInterface;
import com.varrock.world.entity.impl.player.Player;

public class Herblore {

	/*public static void openInterface(Player player, int itemId, Input inputHandling) {
		if(itemId == -12753) {
			itemId = 52783;
		}
		if(itemId == -12756) {
			itemId = 52780;
		}
		player.getSkillManager().stopSkilling();
		player.setSelectedSkillingItem(itemId);
		player.setInputHandling(inputHandling);
		player.getPacketSender().sendString(2799, "\\n\\n\\n\\n"+ItemDefinition.forId(itemId).getName()).sendInterfaceModel(1746, itemId, 150) .sendChatboxInterface(4429);
		player.getPacketSender().sendString(2800, "How many would you like to make?");
	}*/

	public static void openInterface(Player player, int itemId, Input inputHandling) {
		if(itemId == -12753) {
			itemId = 52783;
		}
		if(itemId == -12756) {
			itemId = 52780;
		}
		player.getSkillManager().stopSkilling();
		player.setSelectedSkillingItem(itemId);
		if (inputHandling instanceof EnterAmountOfUnfPotionsToMake) {
			MakeInterface.open(player, new int[] {itemId}, MakeInterface.MakeType.UNFINISHED_POTION);
		} else {
			MakeInterface.open(player, new int[] {itemId}, MakeInterface.MakeType.FINISHED_POTION);
		}
	}

	public static final int VIAL = 227;
	private static final Animation ANIMATION = new Animation(363);

	public static boolean cleanHerb(final Player player, final int herbId) {
		Herbs herb = Herbs.forId(herbId);
		if (herb == null) {
			return false;
		}
		if(player.getInventory().contains(herb.getGrimyHerb())) {
			if (player.getSkillManager().getCurrentLevel(Skill.HERBLORE) < herb.getLevelReq()) {
				player.getPacketSender().sendMessage("You need a Herblore level of at least " + herb.getLevelReq() + " to clean this leaf.");
				return false;
			}
			
			player.getInventory().delete(herb.getGrimyHerb(), 1);
			player.getInventory().add(herb.getCleanHerb(), 1, "Herblore");
			player.getSkillManager().addExperience(Skill.HERBLORE, herb.getExp() * Skill.HERBLORE.getModifier());
			player.getPacketSender().sendMessage("You clean the dirt off the leaf.");
			
			if (herb == Herbs.TORSTOL) {
				Achievements.doProgress(player, AchievementData.CLEAN_1000_TORSTOL);
			}
			return true;
		}
		return false;
	}

	public static boolean startMakingUnfinishedPotion(final Player player, final int herbId) {
		final UnfinishedPotions unf = UnfinishedPotions.getPotionForHerb(herbId);
		if (unf == null) {
			System.out.println("null");
			return false;
		}

		if (player.getSkillManager().getCurrentLevel(Skill.HERBLORE) < unf.getLevelReq()) {
			player.getPacketSender().sendMessage("You need a Herblore level of at least " + unf.getLevelReq() + " to make this potion.");
			return false;
		}

		openInterface(player, unf.getUnfPotion(), new EnterAmountOfUnfPotionsToMake());
		return true;
	}

	public static void makeUnfinishedPotion(Player player, int amount) {

		final int potionId = player.getSelectedSkillingItem();

		final UnfinishedPotions unf = UnfinishedPotions.getPotionForPotion(potionId);

		if (unf == null)
			return;

		player.getPacketSender().sendInterfaceRemoval();

		player.setCurrentTask(new Task(1, player, true) {
			int amountCreated = 0;
			@Override
			public void execute() {
				if(amountCreated >= amount) {
					stop();
					return;
				}
				if(!player.getInventory().contains(unf.getHerbNeeded())) {
					player.getPacketSender().sendMessage("You have run out of "+ItemDefinition.forId(unf.getHerbNeeded()).getName()+".");
					stop();
					return;
				}
				if(!player.getInventory().contains(VIAL)) {
					player.getPacketSender().sendMessage("You have run out of "+ItemDefinition.forId(VIAL).getName()+".");
					stop();
					return;
				}
				player.performAnimation(ANIMATION);
				player.getInventory().delete(VIAL, 1).delete(unf.getHerbNeeded(), 1).add(unf.getUnfPotion(), 1, "Herblore");
				player.getPacketSender().sendMessage("You put the " + ItemDefinition.forId(unf.getHerbNeeded()).getName() + " into the vial of water.");
				player.getSkillManager().addExperience(Skill.HERBLORE, 1 * Skill.HERBLORE.getModifier());
				amountCreated++;

			}

			@Override
			public void stop() {
				setEventRunning(false);
				player.getPacketSender().sendMessage("You have finished making "+amountCreated+"x "+ItemDefinition.forId(unf.getUnfPotion()).getName()+".");
			}
		});

		TaskManager.submit(player.getCurrentTask());

	}

	public static boolean startMakingFinishedPotions(final Player player, final int itemUsed, final int usedWith) {
		final FinishedPotions pot = FinishedPotions.forId(itemUsed, usedWith);

		if(pot == FinishedPotions.MISSING_INGRIDIENTS) {
			player.getPacketSender().sendMessage("You don't have the required items to make this potion.");
			return false;
		}

		if (pot == null) {
			startCreatingSpecialPotion(player, itemUsed, usedWith);
			return false;
		}

		if (player.getSkillManager().getCurrentLevel(Skill.HERBLORE) < pot.getLevelReq()) {
			player.getPacketSender().sendMessage("You need a Herblore level of at least " + pot.getLevelReq() + " to make this potion.");
			return false;
		}

		openInterface(player, pot.getFinishedPotion(), new EnterAmountOfFinishedPotionsToMake());

		return true;
	}

	public static void makeFinishedPotions(Player player, int amount) {

		final int potionId = player.getSelectedSkillingItem();

		final FinishedPotions pot = FinishedPotions.forFinishedPotion(potionId);

		if(pot == null)
			return;

		player.getPacketSender().sendInterfaceRemoval();
		player.getSkillManager().stopSkilling();

		player.setCurrentTask(new Task(1, player, true) {
			int amountCreated = 0;
			@Override
			public void execute() {
				if(amountCreated >= amount) {
					stop();
					return;
				}
				if(player.getInventory().contains(pot.getUnfinishedPotion()) && player.getInventory().contains(pot.getItemNeeded())) {

					player.performAnimation(ANIMATION);

					player.getInventory().delete(pot.getUnfinishedPotion(), 1).delete(pot.getItemNeeded(), 1).add(pot.getFinishedPotion(), 1, "Herblore");
					player.getSkillManager().addExperience(Skill.HERBLORE, pot.getExpGained() * Skill.HERBLORE.getModifier());
					String name = ItemDefinition.forId(pot.getFinishedPotion()).getName();
					player.getPacketSender().sendMessage("You combine the ingredients to make "+Misc.anOrA(name)+" "+name+".");
					Achievements.finishAchievement(player, AchievementData.MIX_A_POTION);
				} else {
					player.getPacketSender().sendMessage("You don't have the required items to make this potion.");
					stop();
					return;
				}
				amountCreated++;
			}

			@Override
			public void stop() {
				setEventRunning(false);
				player.getPacketSender().sendMessage("You have finished making "+amountCreated+"x "+ItemDefinition.forId(pot.getFinishedPotion()).getName()+".");
			}
		});

		TaskManager.submit(player.getCurrentTask());
	}

	public enum SpecialPotion {
		EXTREME_ATTACK(new Item[] {new Item(145), new Item(261)}, new Item(15309), 88, 235),
		EXTREME_STRENGTH(new Item[] {new Item(157), new Item(267)}, new Item(15313), 88, 235),
		EXTREME_DEFENCE(new Item[] {new Item(163), new Item(2481)}, new Item(15317), 90, 250),
		EXTREME_MAGIC(new Item[] {new Item(3042), new Item(9594)}, new Item(15321), 91, 250),
		EXTREME_RANGED(new Item[] {new Item(169), new Item(12539, 5)}, new Item(15325), 92, 260),
		OVERLOAD(new Item[] {new Item(15309), new Item(15313), new Item(15317), new Item(15321), new Item(15325)}, new Item(15333), 96, 300);

		SpecialPotion(Item[] ingridients, Item product, int lvlReq, int exp) {
			this.ingridients = ingridients;
			this.product = product;
			this.lvlReq = lvlReq;
			this.exp = exp;
		}

		private Item[] ingridients;

		public Item[] getIngridients() {
			return ingridients;
		}

		private Item product;

		public Item getProduct() {
			return product;
		}

		private int lvlReq, exp;

		public int getLevelReq() {
			return lvlReq;
		}

		public int getExperience() {
			return exp;
		}

		public static SpecialPotion forItems(int item1, int item2) {
			for(SpecialPotion potData : SpecialPotion.values()) {
				int found = 0;
				for(Item it : potData.getIngridients()) {
					if(it.getId() == item1 || it.getId() == item2)
						found++;
				}
				if(found >= 2)
					return potData;
			}
			return null;
		}
	}

	public static void startCreatingSpecialPotion(Player p, int item1, int item2) {
		if(item1 == item2)
			return;
		if(!p.getInventory().contains(item1) || !p.getInventory().contains(item2)) 
			return;
		SpecialPotion specialPotData = SpecialPotion.forItems(item1, item2);
		if(specialPotData == null)
			return;

		openInterface(p, specialPotData.getProduct().getId(), new EnterAmountOfSpecialPotionsToMake(specialPotData));
	}

	public static void finishMakingSpecialPotion(Player p, int amount, SpecialPotion specialPotData) {

		if(specialPotData == null)
			return;

		p.setCurrentTask(new Task(1, p, true) {
			int amountCreated = 0;

			@Override
			public void execute() {

				if (amountCreated >= amount) {
					stop();
					return;
				}

				if (p.getSkillManager().getCurrentLevel(Skill.HERBLORE) < specialPotData.getLevelReq()) {
					p.getPacketSender().sendMessage("You need a Herblore level of at least " + specialPotData.getLevelReq() + " to make this potion.");
					stop();
					return;
				}

				for (Item ingridients : specialPotData.getIngridients()) {
					if (!p.getInventory().contains(ingridients.getId()) || p.getInventory().getAmount(ingridients.getId()) < ingridients.getAmount()) {
						p.getPacketSender().sendMessage("You do not have all ingridients for this potion.");
						p.getPacketSender().sendMessage("Remember: You can purchase an Ingridient's book from the Druid Spirit.");
						stop();
						return;
					}
				}
				for (Item ingridients : specialPotData.getIngridients())
					p.getInventory().delete(ingridients);
				p.getInventory().add(specialPotData.getProduct(), "Create special potion");
				p.performAnimation(new Animation(363));
				p.getSkillManager().addExperience(Skill.HERBLORE, specialPotData.getExperience() * Skill.HERBLORE.getModifier());
				String name = specialPotData.getProduct().getDefinition().getName();
				p.getPacketSender().sendMessage("You make " + Misc.anOrA(name) + " " + name + ".");
				p.getClickDelay().reset();
				if (specialPotData == SpecialPotion.OVERLOAD) {
					Achievements.finishAchievement(p, AchievementData.MIX_AN_OVERLOAD_POTION);
					Achievements.doProgress(p, AchievementData.MIX_100_OVERLOAD_POTIONS);
				}

			}

			@Override
			public void stop() {
				setEventRunning(false);
				if(amountCreated > 0)
					p.getPacketSender().sendMessage("You have finished making "+amountCreated+"x "+specialPotData.getProduct().getDefinition().getName()+".");
				p.getPA().sendInterfaceRemoval();
			}
		});

		TaskManager.submit(p.getCurrentTask());
	}
}
