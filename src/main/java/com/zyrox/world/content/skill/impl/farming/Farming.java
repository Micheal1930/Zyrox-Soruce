package com.zyrox.world.content.skill.impl.farming;

import com.zyrox.engine.task.Task;
import com.zyrox.engine.task.TaskManager;
import com.zyrox.model.Animation;
import com.zyrox.model.GameObject;
import com.zyrox.model.Item;
import com.zyrox.model.Skill;
import com.zyrox.model.container.impl.Equipment;
import com.zyrox.model.definitions.ItemDefinition;
import com.zyrox.util.Misc;
import com.zyrox.world.content.skill.AbstractHarvestSkill;
import com.zyrox.world.content.skill.impl.farming.attributes.GrowthState;
import com.zyrox.world.content.skill.impl.farming.attributes.PatchAttribute;
import com.zyrox.world.content.skill.impl.farming.patch.Patch;
import com.zyrox.world.content.skill.impl.farming.patch.PatchType;
import com.zyrox.world.content.skill.impl.farming.seed.FlowerSeedType;
import com.zyrox.world.content.skill.impl.farming.seed.SeedClass;
import com.zyrox.world.content.skill.impl.farming.seed.SeedType;
import com.zyrox.world.content.skill.impl.farming.seed.TreeSeedType;
import com.zyrox.world.entity.Entity;
import com.zyrox.world.entity.impl.player.Player;

/**
 *
 * @author relex lawl
 */
public final class Farming extends AbstractHarvestSkill {
	
	/**
	 * TODO
	 * 		COMPOST CROPS
	 * 		PROTECTING CROPS
	 */

	public Farming(Player player, Entity entity) {
		super(player, entity);
	}
	
	private Patch patch;
		
	private int amountToHarvest;
	
	public Patch getPatch() {
		return patch;
	}
	
	public static boolean isGameObject(final Player player, GameObject gameObject, int option) {
		final PatchType patchType = PatchType.forObjectId(gameObject.getId());
		if (patchType != null) {
						
			final Farming farming = new Farming(player, gameObject);
			farming.patch = player.getPatches().get(patchType);
			
			if (farming.patch != null
					&& farming.patch.getSeedType() != null) {
				
				boolean fullyGrown = farming.patch.isFullyGrown();
				final String stateMessage = farming.patch.getGrowthState() != GrowthState.WATERED ?
						farming.patch.getSeedType() != null
							&& (farming.patch.getSeedType().getSeedClass() == SeedClass.BUSHES || farming.patch.getSeedType().getSeedClass() == SeedClass.TREES
								|| farming.patch.getSeedType().getSeedClass() == SeedClass.MUSHROOMS
								|| farming.patch.getSeedType() == FlowerSeedType.WHITE_LILY) ?
								"The crops are looking healthy." : "The crops are looking healthy; should probably water them to keep it that way!"
						: "The crops are looking healthy and watered!";
				
				//player.sendMessage("opt=" + option + "; grown=" + fullyGrown);
				
				if (farming.patch.getSeedType().getSeedClass() == SeedClass.TREES) {
					if (option == 1) {
						if (!farming.patch.hasAttribute(PatchAttribute.CHECKED_HEALTH)) {
							final int experience = farming.patch.getSeedType().getExperience()[1];
							farming.patch.addAttribute(PatchAttribute.CHECKED_HEALTH);
							player.getSkillManager().addExperience(farming.getSkill(), experience * farming.getSkill().getModifier());
							player.getPacketSender().sendMessage("You check the tree's health and gain experience.");
							FarmingManager.updatePatch(player, patchType, farming.patch);
							return true;
						} else if (farming.patch.getHarvestedItem().getAmount() >= farming.patch.getProduct().getAmount()) {
							player.getPacketSender().sendMessage("The tree must first grow at least one log before chopping it down.");
							return true;
						}
						
						int treeId = -1;
						if (farming.patch.getSeedType() == TreeSeedType.WILLOW) {
							treeId = 1308;
						} else if (farming.patch.getSeedType() == TreeSeedType.MAPLE) {
							treeId = 1307;
						} else if (farming.patch.getSeedType() == TreeSeedType.YEW) {
							treeId = 1309;
						} else if (farming.patch.getSeedType() == TreeSeedType.MAGIC) {
							treeId = 1306;
						}
						
						if (treeId == -1) {
							player.getPacketSender().sendMessage("Error attempting to chop tree! Please report to an administrator.");
							return true;
						}
						
						GameObject tree = new GameObject(treeId, gameObject.getPosition());
						//if (Woodcutting.isTree(tree)) {
							//new Woodcutting(player, tree, farming).execute();
							return true;
						//}
					} else if (option == 2) {
												
						if (farming.patch.getGrowthState() == GrowthState.DISEASED) {
														
							if (farming.patch.getSeedType().getSeedClass() == SeedClass.TREES
									|| farming.patch.getSeedType().getSeedClass() == SeedClass.BUSHES) {
								
								boolean hasSecateurs = player.getEquipment().getItems()[Equipment.WEAPON_SLOT].getId() == FarmingConstants.MAGIC_SECATEURS
										|| player.getEquipment().getItems()[Equipment.WEAPON_SLOT].getId() == FarmingConstants.SECATEURS_ITEM_ID;
								
								if (!hasSecateurs) {
									for (Item item : player.getInventory().getItems()) {
										if (item.getId() == FarmingConstants.MAGIC_SECATEURS
												|| item.getId() == FarmingConstants.SECATEURS_ITEM_ID) {
											hasSecateurs = true;
											break;
										}
									}
								}
								
								if (hasSecateurs) {
									player.performAnimation(FarmingConstants.SECATEURS_ANIMATION);
									farming.patch.setGrowthState(GrowthState.GROWING);
									FarmingManager.updatePatch(player, patchType, farming.patch);
									return true;
								}
							}
							
							String cure = farming.patch.getSeedType().getSeedClass() == SeedClass.TREES
									|| farming.patch.getSeedType().getSeedClass() == SeedClass.BUSHES ?
									 "a pair of secateurs" : "a cure plant potion";
							player.getPacketSender().sendMessage("You need to use " + cure + " on this crop to cure it.");
							return true;
						}
						
						String message = !fullyGrown ? "This tree is still growing..." 
								: farming.patch.getHarvestedItem().getAmount() < farming.patch.getProduct().getAmount() ?
								"This tree seems sturdy enough to chop down." : "This tree has recently been chopped down.";
						player.getPacketSender().sendMessage(message);
						return true;
					} else if (option == 4) {
						if (fullyGrown) {
							final int left = (farming.patch.getProduct().getAmount() - farming.patch.getHarvestedItem().getAmount());
							player.getPacketSender().sendMessage("This tree has " + left + " log" + (left != 1 ? "s" : "") + " left to harvest.");
						} else {
							player.getPacketSender().sendMessage("You can only do this once the tree is fully grown.");
						}
						return true;
					}
				}
				
				if (option == 4) {
					//guide
					if (fullyGrown) {
						final int left = (farming.patch.getProduct().getAmount() - farming.patch.getHarvestedItem().getAmount());
						player.getPacketSender().sendMessage("This patch has " + left + " crop" + (left != 1 ? "s" : "") + " left to harvest.");
					} else {
						player.getPacketSender().sendMessage("You can only do this once the crop is fully grown.");
					}
					return true;
				} else if (option == 2
						&& farming.patch.getSeedType().getSeedClass() == SeedClass.BUSHES) {
					//inspect
					
					if (farming.patch.getGrowthState() == GrowthState.DISEASED) {
						if (player.getInventory().contains(6036)) {
							player.performAnimation(new Animation(2288));
							player.getInventory().delete(new Item(6036));
							player.getInventory().add(new Item(229));
							farming.patch.setGrowthState(GrowthState.GROWING);
							FarmingManager.updatePatch(player, patchType, farming.patch);
							return true;
						}
						player.getPacketSender().sendMessage("The crops are diseased; you need a plant cure potion.");
						return true;
					}
					
					final int experience = farming.patch.getSeedType().getExperience()[1];
					if (experience <= 0) {
						player.getPacketSender().sendMessage(stateMessage);
						return true;
					} else if (!farming.patch.isFullyGrown()) {
						player.getPacketSender().sendMessage("You can only do this once the crop is fully grown.");
						return true;
					}
					if (!farming.patch.hasAttribute(PatchAttribute.CHECKED_HEALTH)) {
						farming.patch.addAttribute(PatchAttribute.CHECKED_HEALTH);
						player.getSkillManager().addExperience(farming.getSkill(), experience * farming.getSkill().getModifier());
						player.getPacketSender().sendMessage("You check the crop's health and gain experience.");
					} else {
						player.getPacketSender().sendMessage("You have already checked this crops' health.");
					}
					return true;
				}
				
				if (farming.patch.getGrowthState() == GrowthState.DISEASED) {
					if (player.getInventory().contains(6036)) {
						player.performAnimation(new Animation(2288));
						player.getInventory().delete(new Item(6036));
						player.getInventory().add(new Item(229));
						farming.patch.setGrowthState(GrowthState.GROWING);
						FarmingManager.updatePatch(player, patchType, farming.patch);
						return true;
					}
					player.getPacketSender().sendMessage("The crops are diseased; you need a plant cure potion.");
					return true;
				} else if (farming.patch.getGrowthState() == GrowthState.DEAD) {
					player.getPacketSender().sendMessage("This crop has died; you should probably dig it out with a spade.");
					return true;
				} else if (fullyGrown) {
					if (option == 2) {
						player.getPacketSender().sendMessage("The crops are ready to harvest.");
						return true;
					}

					farming.amountToHarvest = 1;
					if (farming.amountToHarvest <= 0)
						farming.amountToHarvest = 1;
				} else if (!fullyGrown) {
					player.getPacketSender().sendMessage(stateMessage);
					return true;
				}
				
			} else {
				
				if (option == 4) {
					player.getPacketSender().sendMessage("You can only do this once the crop is fully grown.");
					return true;
				}

				if (farming.patch == null)
					farming.patch = new Patch(patchType, null, null, 0);
				
			}
			
			farming.execute();
			
			player.getPatches().put(patchType, farming.patch);
			return true;
		}
		return false;
	}
	
	public static boolean hasItemInteraction(Player player, Item item, GameObject gameObject) {
		final PatchType patchType = PatchType.forObjectId(gameObject.getId());
		if (patchType != null) {
			
			//if (player.getFields().isAnimationBusy())
				//return true;
			
			final Patch patch = player.getPatches().get(patchType);
			final boolean weedRemoved = patch == null ? false : patch.getWeedStage() >= FarmingConstants.WEED_CONFIG.length - 1;
			
			final boolean magicWateringCan = item.getId() == FarmingConstants.MAGIC_WATERING_CAN_ITEM_ID;
			if (item.getId() >= 5333 && item.getId() <= 5340
					|| magicWateringCan) {
				if (patch == null) {
					player.getPacketSender().sendMessage("You cannot water this crop!");
					return true;
				}
				if (patch.getSeedType() != null) {
					if (patch.getSeedType() == FlowerSeedType.WHITE_LILY) {
						player.getPacketSender().sendMessage("You cannot water this crop!");
						return true;
					}
					switch (patch.getSeedType().getSeedClass()) {
					case MUSHROOMS:
					case BUSHES:
					case TREES:
						player.getPacketSender().sendMessage("You cannot water this crop!");
						return true;
					}
				}
				if (patch.getGrowthState() != GrowthState.GROWING) {
					final String message = patch.getSeedType() != null ? "You cannot water these crops anymore!"
							: "You must first plant seeds on this patch before watering it.";
					player.getPacketSender().sendMessage(message);
					return true;
				} else if (patch.isFullyGrown()) {
					player.getPacketSender().sendMessage("You cannot water these crops; they are ready to be harvested.");
					return true;
				}
				//player.getFields().setAnimationBusy(true);
				player.performAnimation(FarmingConstants.POUR_WATER_ANIMATION);

				TaskManager.submit(new Task(4) {
					@Override
					public void execute() {
						useWateringCan(player, item, magicWateringCan);
						patch.setDiseaseImmunity(patch.getDiseaseImmunity() + 50);
						patch.setGrowthState(GrowthState.WATERED);
						FarmingManager.updatePatch(player, patchType, patch);
						//player.getFields().setAnimationBusy(false);
						stop();
					}
				});
				return true;
			} else if (item.getId() == FarmingConstants.COMPOST_ITEM_ID) {
				if (patch == null) {
					player.getPacketSender().sendMessage("You cannot compost this crop!");
					return true;
				}
				if (patch.getSeedType() != null) {
					player.getPacketSender().sendMessage("You can no longer place compost on this patch.");
					return true;
				}
				if (patch.hasAttribute(PatchAttribute.COMPOSTED)
						|| patch.hasAttribute(PatchAttribute.SUPER_COMPOSTED)) {
					player.getPacketSender().sendMessage("This patch already contains compost.");
					return true;
				}
				patch.addAttribute(PatchAttribute.COMPOSTED);
				//player.getFields().setAnimationBusy(true);
				player.performAnimation(FarmingConstants.POUR_COMPOST_ANIMATION);

				TaskManager.submit(new Task(4) {
					@Override
					public void execute() {
						patch.setDiseaseImmunity(patch.getDiseaseImmunity() + 50);
						int nextId = 3727;
						player.getInventory().delete(item);
						player.getInventory().add(new Item(nextId));
						patch.hasAttribute(PatchAttribute.COMPOSTED);
						//player.getFields().setAnimationBusy(false);
						stop();
					}
				});
				return true;
			} else if (item.getId() == FarmingConstants.SUPER_COMPOST_ITEM_ID) {
				if (patch == null) {
					player.getPacketSender().sendMessage("You cannot compost this crop!");
					return true;
				}
				if (patch.getSeedType() != null) {
					player.getPacketSender().sendMessage("You can no longer place super compost on this patch.");
					return true;
				}
				if (patch.hasAttribute(PatchAttribute.COMPOSTED)
						|| patch.hasAttribute(PatchAttribute.SUPER_COMPOSTED)) {
					player.getPacketSender().sendMessage("This patch already contains compost.");
					return true;
				}
				patch.addAttribute(PatchAttribute.SUPER_COMPOSTED);
				//player.getFields().setAnimationBusy(true);
				player.performAnimation(FarmingConstants.POUR_COMPOST_ANIMATION);

				TaskManager.submit(new Task(4) {
					@Override
					public void execute() {
						patch.setDiseaseImmunity(patch.getDiseaseImmunity() + 150);
						int nextId = 3727;
						player.getInventory().delete(item);
						player.getInventory().add(new Item(nextId));
						patch.hasAttribute(PatchAttribute.SUPER_COMPOSTED);
						//player.getFields().setAnimationBusy(false);
						stop();
					}
				});
				return true;
			} else if (item.getId() == FarmingConstants.RAKE_ITEM_ID) {
				if (weedRemoved) {
					player.getPacketSender().sendMessage("This patch has already been raked.");
				} else {
					isGameObject(player, gameObject, 1);
				}
				return true;
			} else if (item.getId() == FarmingConstants.SPADE_ITEM_ID
					&& patch != null
					&& patch.getSeedType() != null) {
				
				//player.getFields().setAnimationBusy(true);
				player.performAnimation(new Animation(830));
				
				TaskManager.submit(new Task(3) {
					@Override
					public void execute() {
						player.getPatches().remove(patch.getPatchType());
						patch.setWeedStage(0);
						FarmingManager.updatePatch(player, patch.getPatchType(), patch);
						
						player.getPacketSender().sendMessage("You dig the crop from the patch.");
						stop();
					}
				});
				return true;
			} else if (item.getId() == 5350) {
				if (patch == null
						|| !weedRemoved) {
					player.getPacketSender().sendMessage("You cannot collect soil from this patch.");
					return true;
				}
				if (!player.getInventory().contains(FarmingConstants.TROWEL_ITEM_ID)) {
					player.getPacketSender().sendMessage("You need a gardening trowel to do this.");
					return true;
				}
				player.performAnimation(new Animation(827));
				player.getInventory().delete(item);
				player.getInventory().add(new Item(5356));
				return true;
			} else if (item.getId() == 6036) {
				if (patch == null
						|| patch.getSeedType() == null)
					return true;
				if (patch.getGrowthState() != GrowthState.DISEASED) {
					player.getPacketSender().sendMessage("This potion can only be used on diseased crops.");
					return true;
				}
				if (patch.getSeedType().getSeedClass() == SeedClass.TREES
							|| patch.getSeedType().getSeedClass() == SeedClass.BUSHES) {
					player.getPacketSender().sendMessage("This potion cannot be used on this crop. You need a pair of secateurs.");
					return true;
				}
				player.performAnimation(new Animation(2288));
				player.getInventory().delete(item);
				player.getInventory().add(new Item(229));
				patch.setGrowthState(GrowthState.GROWING);
				FarmingManager.updatePatch(player, patchType, patch);
				return true;
			} else if (item.getId() == FarmingConstants.SECATEURS_ITEM_ID
					|| item.getId() == FarmingConstants.MAGIC_SECATEURS) {
				if (patch == null
						|| patch.getSeedType() == null)
					return true;
				if (patch.getSeedType().getSeedClass() != SeedClass.TREES
							&& patch.getSeedType().getSeedClass() != SeedClass.BUSHES)
					return true;
				if (patch.getGrowthState() != GrowthState.DISEASED) {
					String name = patch.getSeedType().getSeedClass() == SeedClass.TREES ?
							"tree" : "bush";
					player.getPacketSender().sendMessage("You should only use these when your " + name + " is diseased.");
					return true;
				}
				player.performAnimation(FarmingConstants.SECATEURS_ANIMATION);
				patch.setGrowthState(GrowthState.GROWING);
				FarmingManager.updatePatch(player, patchType, patch);
				return true;
			}
					
			SeedType seedType = null;
			
			for (SeedType validSeed : patchType.getValidSeeds()) {
				if (validSeed.getSeed().getId() == item.getId()) {
					seedType = validSeed;
					break;
				}
			}
			
			if (seedType != null) {	

				if (patch == null) {
					player.getPacketSender().sendMessage("You must first rake this patch before planting anything!");
					return true;
				} else if (!weedRemoved) {
					player.getPacketSender().sendMessage("You must finish raking this patch before planting anything.");
					return true;
				} else if (patch.getSeedType() != null) {
					if (item.getId() == patch.getSeedType().getToolId()) {
						isGameObject(player, gameObject, 1);
					} else {
						player.getPacketSender().sendMessage("This patch is already growing crops!");
					}
					return true;
				} else if (!player.getInventory().contains(seedType.getSeed().getId())) {
					player.getPacketSender().sendMessage("You need " + seedType.getSeed().getAmount() + " "
							+ seedType.getSeed().getDefinition().getName().toLowerCase() + (seedType.getSeed().getAmount() != 1 ? "s" : "") + " to plant this crop.");
					return true;
				} else if (seedType.getSeedClass() != SeedClass.TREES 
						&& !player.getInventory().contains(FarmingConstants.SEED_DIBBER_ITEM_ID)) {
					player.getPacketSender().sendMessage("You need a seed dibber to plant these seeds.");
					return true;
				} else if (seedType.getSeedClass() == SeedClass.TREES 
						&& !player.getInventory().contains(FarmingConstants.TROWEL_ITEM_ID)) {
					player.getPacketSender().sendMessage("You need a seed trowel to plant this seedling.");
					return true;
				} else if (seedType.getLevelRequirement() > player.getSkillManager().getCurrentLevel(Skill.FARMING)) {
					player.getPacketSender().sendMessage("You need a farming level of " + seedType.getLevelRequirement() + " to plant this crop.");
					return false;
				}
				
				//player.getFields().setAnimationBusy(true);
				player.performAnimation(FarmingConstants.SEED_DIBBER_ANIMATION);
				
				final SeedType finalSeedType = seedType;
				TaskManager.submit(new Task(2) {
					@Override
					public void execute() {
						player.performAnimation(new Animation(65535));
						player.getInventory().delete(finalSeedType.getSeed());
						
						if (finalSeedType.getSeedClass() == SeedClass.TREES) {
							player.getInventory().add(new Item(5350));
						}
						
						final Patch newPatch = new Patch(patchType, finalSeedType, patch.getGrowthState() == null ? GrowthState.GROWING : patch.getGrowthState(), 0);
						newPatch.setWeedStage(FarmingConstants.WEED_CONFIG.length - 1);
						newPatch.setProduct(finalSeedType.getRewards()[0].copy().setAmount(Misc.inclusiveRandom(6, 25)));
						newPatch.setHarvestedItem(finalSeedType.getRewards()[0].copy().setAmount(0));
						newPatch.setPlantConfigurations(player);
						player.getPatches().put(patchType, newPatch);
						
						FarmingManager.updatePatch(player, patchType, newPatch);
						newPatch.submitGrowthTask(player);
						//player.getFields().setAnimationBusy(false);
						player.getSkillManager().addExperience(Skill.FARMING, finalSeedType.getExperience()[0] * Skill.FARMING.getModifier());
						stop();
					}
					
				});
			} else {
				player.getPacketSender().sendMessage("You cannot use this item on the patch.");
			}
			return true;
		}
		return false;
	}

	@Override
	public Item getReward() {
		if (patch.getSeedType() == null)
			return new Item(FarmingConstants.WEED_ITEM_ID);
		return new Item(patch.getProduct().getId(), amountToHarvest);
	}

	@Override
	public String getHarvestMessage() {
		return null;
	}

	@Override
	public String getRewardMessage() {
		if (patch.getSeedType() == null)
			return "You rake up the patch.";
		return "You pick " + Misc.getArticle(patch.getSeedType().getRewards()[0].getDefinition().getName().toLowerCase()) + " from the patch.";
	}

	@Override
	public boolean consecutiveRewards() {
		return true;
	}

	@Override
	public int getCycles() {
		return 2;
	}

	@Override
	public int getHarvestAmount() {
		return patch.getSeedType() == null ? (FarmingConstants.WEED_CONFIG.length - 1) - patch.getWeedStage() : patch.getProduct().getAmount() - patch.getHarvestedItem().getAmount();
	}
	
	@Override
	public void harvestAction() {
		super.harvestAction();
		
		if (patch.getSeedType() != null) {
						
			if (patch.getHarvestedItem().getAmount() + amountToHarvest > patch.getProduct().getAmount())
				amountToHarvest = patch.getProduct().getAmount() - patch.getHarvestedItem().getAmount();

			patch.getHarvestedItem().setAmount(patch.getHarvestedItem().getAmount() + amountToHarvest);
			if (patch.getHarvestedItem().getAmount() >= patch.getProduct().getAmount()) {
				stopHarvest();

				if (patch.getSeedType().getSeedClass() == SeedClass.TREES) {
					FarmingManager.updatePatch(getPlayer(), patch.getPatchType(), patch);
				} else if (patch.getSeedType().getSeedClass() != SeedClass.BUSHES) {
					getPlayer().getPatches().remove(patch.getPatchType());
					patch.setWeedStage(0);
					FarmingManager.updatePatch(getPlayer(), patch.getPatchType(), patch);
				}
			}
			if (patch.getSeedType().getSeedClass() == SeedClass.BUSHES
					|| patch.getSeedType().getSeedClass() == SeedClass.MUSHROOMS) {
				FarmingManager.updatePatch(getPlayer(), patch.getPatchType(), patch);
			}
			
		} else {
			patch.setWeedStage(patch.getWeedStage() + 1);
			if (patch.getWeedStage() >= FarmingConstants.WEED_CONFIG.length - 1) {
				stopHarvest();
			}
			FarmingManager.updatePatch(getPlayer(), patch.getPatchType(), patch);
		}
	}

	@Override
	public void performOutcome() {
	}

	@Override
	public boolean canInitiate() {
		if (patch.getSeedType() != null) {
			
			
			if (patch.isFullyGrown()) {
				if (patch.getSeedType().getToolId() != -1
						&& !getPlayer().getInventory().contains(patch.getSeedType().getToolId())) {
					getPlayer().getPacketSender().sendMessage("You need "
						+ Misc.getArticle(ItemDefinition.forId(patch.getSeedType().getToolId()).getName())
						+ " to harvest these crops.");
					return false;
				}
			}
			
			if (patch.getSeedType().getLevelRequirement() > getPlayer().getSkillManager().getCurrentLevel(getSkill())) {
				getPlayer().getPacketSender().sendMessage("You need a farming level of " + patch.getSeedType().getLevelRequirement() + " to plant this crop.");
				return false;
			}
			
			if (patch.getHarvestedItem().getAmount() >= patch.getProduct().getAmount()) {
				stopHarvest();
				getPlayer().getPacketSender().sendMessage("This crop does not have anything else to harvest at the time.");
				return false;
			}
			
			final boolean hasMagicSecateurs = getPlayer().getEquipment().getItems()[Equipment.WEAPON_SLOT].getId() == FarmingConstants.MAGIC_SECATEURS
					|| getPlayer().getInventory().contains(FarmingConstants.MAGIC_SECATEURS);
			if (hasMagicSecateurs
					&& !patch.hasAttribute(PatchAttribute.MAGIC_SECATEURS)) {
				patch.addAttribute(PatchAttribute.MAGIC_SECATEURS);
				patch.getProduct().setAmount((int) (patch.getProduct().getAmount() * 1.1));
				getPlayer().getPacketSender().sendMessage("Your magic secateurs increase your harvest yield by 10%");
			}
			if (patch.hasAttribute(PatchAttribute.SUPER_COMPOSTED)) {
				patch.addAttribute(PatchAttribute.SUPER_COMPOSTED);
				patch.getProduct().setAmount((int) (patch.getProduct().getAmount() * 1.1));
				getPlayer().getPacketSender().sendMessage("The super compost increases your harvest yield by 10%");
			} else if (patch.hasAttribute(PatchAttribute.COMPOSTED)) {
				patch.addAttribute(PatchAttribute.COMPOSTED);
				patch.getProduct().setAmount((int) (patch.getProduct().getAmount() * 1.05));
				getPlayer().getPacketSender().sendMessage("The compost increases your harvest yield by 5%");
			}
		} else {
			if (!getPlayer().getInventory().contains(FarmingConstants.RAKE_ITEM_ID)) {
				getPlayer().getPacketSender().sendMessage("You need a rake to do this.");
				return false;
			}
			if (patch.getWeedStage() >= FarmingConstants.WEED_CONFIG.length - 1) {
				if (!patch.hasAttribute(PatchAttribute.COMPOSTED)
						&& !patch.hasAttribute(PatchAttribute.SUPER_COMPOSTED)) {
					getPlayer().getPacketSender().sendMessage("You should probably place compost on this patch.");
				} else {
					getPlayer().getPacketSender().sendMessage("This patch is ready to grow crops.");
				}
				return false;
			}
		}
		return true;
	}

	@Override
	public Skill getSkill() {
		return Skill.FARMING;
	}

	@Override
	public double getExperience() {
		return patch.getSeedType() == null ? 3 : patch.getSeedType().getExperience()[2];
	}

	@Override
	public Animation getAnimation() {
		return patch.getSeedType() == null ? FarmingConstants.RAKE_ANIMATION : patch.getSeedType().getAnimation();
	}
	
	@Override
	public int getAnimationTicks() {
		return 2;
	}
	
	private static void useWateringCan(Player player, Item item, boolean magicWateringCan) {
		if (!magicWateringCan) {
			int nextId = item.getId() == 5333 ? 5331 : item.getId() - 1;
			player.getInventory().delete(item);
			player.getInventory().add(new Item(nextId));
		}
	}
	
	public static boolean hasItemOnItemInteraction(Player player, Item[] items) {
		if (items[0].getId() == 5356) {
			
			int[] data = null;
			
			for (int[] itemOnItem : ITEM_ON_ITEM_SAPLING) {
				if (itemOnItem[0] == items[1].getId()) {
					data = itemOnItem;
					break;
				}
			}
			
			if (data == null)
				return false;
			
			Item wateringCan = null;
			
			for (Item item : player.getInventory().getItems()) {
				if (item.getId() >= 5333 && item.getId() <= 5340
						|| item.getId() == FarmingConstants.MAGIC_WATERING_CAN_ITEM_ID) {
					wateringCan = item;
					break;
				}
			}
			
			if (wateringCan == null) {
				player.getPacketSender().sendMessage("You need a watering can to do this.");
				return true;
			}
			
			if (!player.getInventory().contains(FarmingConstants.TROWEL_ITEM_ID)) {
				player.getPacketSender().sendMessage("You need a gardening trowel to do this.");
				return true;
			}
			
			useWateringCan(player, wateringCan, wateringCan.getId() == FarmingConstants.MAGIC_WATERING_CAN_ITEM_ID);
			player.getInventory().delete(items[0]);
			player.getInventory().delete(items[1].copy().setAmount(1));
			player.getInventory().add(new Item(data[1]));
			return true;
		}
		return false;
	}
	
	private static final int[][] ITEM_ON_ITEM_SAPLING = {
		{ 5313, 5371 }, //willow
		{ 5314, 5372 }, //maple
		{ 5315, 5373 }, //yew
		{ 5316, 5374 }, //magic
	};
}
