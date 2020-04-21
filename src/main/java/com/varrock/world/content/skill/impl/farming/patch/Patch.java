package com.varrock.world.content.skill.impl.farming.patch;

import com.varrock.engine.task.Task;
import com.varrock.engine.task.TaskManager;
import com.varrock.model.Item;
import com.varrock.util.Misc;
import com.varrock.world.content.skill.impl.farming.FarmingConstants;
import com.varrock.world.content.skill.impl.farming.FarmingManager;
import com.varrock.world.content.skill.impl.farming.attributes.GrowthState;
import com.varrock.world.content.skill.impl.farming.attributes.PatchAttribute;
import com.varrock.world.content.skill.impl.farming.seed.AllotmentSeedType;
import com.varrock.world.content.skill.impl.farming.seed.FlowerSeedType;
import com.varrock.world.content.skill.impl.farming.seed.SeedClass;
import com.varrock.world.content.skill.impl.farming.seed.SeedType;
import com.varrock.world.entity.impl.player.Player;

/**
 * 
 * @author relex lawl
 */
public final class Patch {
	
	public Patch(PatchType patchType, SeedType seedType, GrowthState growth, int stage) {
		this.patchType = patchType;
		this.seedType = seedType;
		this.seedTypeName = seedType == null ? null : seedType.toString();
		this.growthState = growth;
		this.stage = stage;
		this.seedClass = seedType == null ? null : seedType.getSeedClass();
	}
	
	private final PatchType patchType;
		
	private transient SeedType seedType;
	
	private final String seedTypeName;
	
	private SeedClass seedClass;
	
	private GrowthState growthState;
	
	private int stage;
	
	private int weedStage;
	
	private int attributes;
	
	private long timePlanted;
	
	private int diseaseImmunity = FarmingConstants.DISEASE_IMMUNITY;
	
	private Item harvestedItem;
	
	private Item product;
	
	private transient boolean growthTaskSubmitted;
	
	private long wateredTimer;
	
	public void submitGrowthTask(final Player player) {
		if (growthTaskSubmitted
				|| seedType == null)
			return;
		growthTaskSubmitted = true;
		
		long waitTime = 60_000;
		if (player.getFarmingTime() > 0) {
			waitTime = player.getFarmingTime();
		}/* else if (player.getGameModeAssistant().getGameMode() != null) {
			switch (player.getGameModeAssistant().getGameMode()) {
			case KNIGHT:
				waitTime = 60_000;
				break;
			case IRONMAN:
				waitTime = 45_000;
				break;
			case REALISM:
				waitTime = 30_000;
				break;
			default:
				waitTime = 60_000;
				break;
			}
		}*/
		
/*		if (player.getDonatorRights().ordinal() >= DonatorRights.EXTREME.ordinal()) {
			decreaseDiseaseImmunity();
		}*/
		
		final long waitTimer = waitTime;
		final int totalMinutes = seedType.getGrowthTime()[0] * seedType.getGrowthTime()[1];
		final int minutesForGrowth = totalMinutes / seedType.getValues().length;
		
		while (System.currentTimeMillis() - timePlanted 
						>= (minutesForGrowth * (stage + 1) * waitTimer)) {
			if (!addStage(player)
					|| isFullyGrown()) {
				break;
			}
		}
		
		TaskManager.submit(new Task(16) {
			@Override
			public void execute() {
				if (getWeedStage() <= 0) {
					stop();
					return;
				}
								
				if (growthState == GrowthState.WATERED
						&& System.currentTimeMillis() - wateredTimer 
							>= (seedType.getGrowthTime()[1] * waitTimer)) {
					growthState = GrowthState.GROWING;
					wateredTimer = System.currentTimeMillis();
				}
												
				if (System.currentTimeMillis() - timePlanted 
						>= (minutesForGrowth * (stage + 1) * waitTimer)) {
					
					if (growthState == GrowthState.DISEASED
							&& (stage >= seedType.getValues().length - 2 || Misc.random(10) == 0)) {
						growthState = GrowthState.DEAD;
						FarmingManager.updatePatch(player, patchType, Patch.this);
						stop();
						return;
					}
					
					decreaseDiseaseImmunity();
					if (addStage(player)) {
						
						if (growthState == GrowthState.WATERED
								&& isFullyGrown()) {
							growthState = GrowthState.GROWING;
						}
						
						FarmingManager.updatePatch(player, patchType, Patch.this);
					}
					if (isFullyGrown()) {
						if (getSeedType().getSeedClass() == SeedClass.BUSHES) {
							if (getHarvestedItem().getAmount() > 0
									&& growthState == GrowthState.GROWING) {
								getHarvestedItem().setAmount(getHarvestedItem().getAmount() - 1);
								FarmingManager.updatePatch(player, patchType, Patch.this);
							}
						} else if (getSeedType().getSeedClass() == SeedClass.TREES) {
							if (getHarvestedItem().getAmount() > 0
									&& growthState == GrowthState.GROWING) {
								boolean firstGrowth = getHarvestedItem().getAmount() >= getProduct().getAmount();
								final int amount = getHarvestedItem().getAmount() - (1 + Misc.random(10));
								getHarvestedItem().setAmount(amount < 0 ? 0 : amount);
								
								if (firstGrowth) {
									FarmingManager.updatePatch(player, patchType, Patch.this);
								}
							}
						} else {
							player.getPacketSender().sendMessage("One of your crops in " + patchType.getCityName() + " have fully grown!");
							stop();
						}
					}
				}
			}
		});
	}
	
	public void decreaseDiseaseImmunity() {
		diseaseImmunity -= Misc.random(15);
		if (diseaseImmunity < FarmingConstants.DISEASE_IMMUNITY)
			diseaseImmunity = FarmingConstants.DISEASE_IMMUNITY;
	}
	
	public void setPlantConfigurations(Player player) {
		timePlanted = System.currentTimeMillis();
		wateredTimer = System.currentTimeMillis();
		
		if (getSeedType() != null && getSeedType().getSeedClass() == SeedClass.ALLOTMENT) {
			PatchType otherPatchType = null;
			switch (patchType) {
			case FALADOR_NORTH_WEST_ALLOTMENT:
			case FALADOR_SOUTH_EAST_ALLOTMENT:
				otherPatchType = PatchType.FALADOR_FLOWER_PATCH;
				break;
			case CATHERBY_NORTH_ALLOTMENT:
			case CATHERBY_SOUTH_ALLOTMENT:
				otherPatchType = PatchType.CATHERBY_FLOWER_PATCH;
				break;
			case ARDOUGNE_NORTH_ALLOTMENT:
			case ARDOUGNE_SOUTH_ALLOTMENT:
				otherPatchType = PatchType.ARDOUGNE_FLOWER_PATCH;
				break;
			case CANIFIS_NORTH_WEST_ALLOTMENT:
			case CANIFIS_SOUTH_EAST_ALLOTMENT:
				otherPatchType = PatchType.CANIFIS_FLOWER_PATCH;
				break;
			default:
				return;
			}
			final Patch otherPatch = player.getPatches().get(otherPatchType);
			if (otherPatch != null
					&& otherPatch.getSeedType() != null) {
				
				final int seedId = otherPatch.getSeedType().getSeed().getId();
				
				boolean protectedPatch = false;
				if (seedId == FlowerSeedType.WHITE_LILY.getSeed().getId()) {
					protectedPatch = true;
				} else if (seedId == FlowerSeedType.MARIGOLD.getSeed().getId()) {
					protectedPatch = getSeedType().getSeed().getId() == AllotmentSeedType.ONION.getSeed().getId()
							|| getSeedType().getSeed().getId() == AllotmentSeedType.TOMATO.getSeed().getId()
							|| getSeedType().getSeed().getId() == AllotmentSeedType.POTATO.getSeed().getId();
					
				} else if (seedId == FlowerSeedType.NASTURTIUM.getSeed().getId()) {
					protectedPatch = getSeedType().getSeed().getId() == AllotmentSeedType.WATERMELON.getSeed().getId();
				}
				
				if (protectedPatch
						&& !hasAttribute(PatchAttribute.PROTECTED)) {
					addAttribute(PatchAttribute.PROTECTED);
					player.getPacketSender().sendMessage("Your patch feels protected...");
				}
			}
		}
	}
	
	public boolean isFullyGrown() {
		return getSeedType() != null && getStage() >= getSeedType().getValues().length - 1;
	}
	
	public boolean isProtected(Player player) {
		if (getSeedType() == null)
			return false;
		if (getSeedType() == FlowerSeedType.WHITE_LILY
				|| hasAttribute(PatchAttribute.PROTECTED))
			return true;
		if (player == null)
			return false;
		PatchType otherPatchType = null;
		switch (patchType) {
		case FALADOR_NORTH_WEST_ALLOTMENT:
		case FALADOR_SOUTH_EAST_ALLOTMENT:
			otherPatchType = PatchType.FALADOR_FLOWER_PATCH;
			break;
		case CATHERBY_NORTH_ALLOTMENT:
		case CATHERBY_SOUTH_ALLOTMENT:
			otherPatchType = PatchType.CATHERBY_FLOWER_PATCH;
			break;
		case ARDOUGNE_NORTH_ALLOTMENT:
		case ARDOUGNE_SOUTH_ALLOTMENT:
			otherPatchType = PatchType.ARDOUGNE_FLOWER_PATCH;
			break;
		case CANIFIS_NORTH_WEST_ALLOTMENT:
		case CANIFIS_SOUTH_EAST_ALLOTMENT:
			otherPatchType = PatchType.CANIFIS_FLOWER_PATCH;
			break;
		default:
			return false;
		}
		final Patch otherPatch = player.getPatches().get(otherPatchType);
		if (otherPatch != null
				&& otherPatch.getSeedType() != null) {
			
			final int seedId = otherPatch.getSeedType().getSeed().getId();
			
			if (seedId == FlowerSeedType.WHITE_LILY.getSeed().getId()) {
				return true;
				
			} else if (seedId == FlowerSeedType.MARIGOLD.getSeed().getId()) {
				return getSeedType().getSeed().getId() == AllotmentSeedType.ONION.getSeed().getId()
						|| getSeedType().getSeed().getId() == AllotmentSeedType.TOMATO.getSeed().getId()
						|| getSeedType().getSeed().getId() == AllotmentSeedType.POTATO.getSeed().getId();
				
			} else if (seedId == FlowerSeedType.NASTURTIUM.getSeed().getId()) {
				return getSeedType().getSeed().getId() == AllotmentSeedType.WATERMELON.getSeed().getId();
			}
		}
		return false;
	}
	
	public PatchType getPatchType() {
		return patchType;
	}
	
	public SeedType getSeedType() {
		return seedType;
	}
	
	public void setSeedType(SeedType seedType) {
		this.seedType = seedType;
	}
	
	public SeedClass getSeedClass() {
		return seedClass;
	}
	
	public void setSeedClass(SeedClass seedClass) {
		this.seedClass = seedClass;
	}
	
	public String getSeedTypeName() {
		return seedTypeName;
	}
	
	public GrowthState getGrowthState() {
		return growthState;
	}
	
	public void setGrowthState(GrowthState growthState) {
		this.growthState = growthState;
	}
	
	public int getStage() {
		return stage;
	}
	
	public void setStage(int stage) {
		this.stage = stage;
	}
	
	public boolean addStage(Player player) {
		if (weedStage < FarmingConstants.WEED_CONFIG.length - 1)
			return false;
		final int length = seedType.getValues().length;
		
		stage++;
		if (stage >= length) {
			stage = length - 1;
			return false;
		}
		
		if (growthState != GrowthState.DISEASED
				&& growthState != GrowthState.DEAD
				&& stage < length - 1
				&& Misc.random(getDiseaseImmunity()) == 0 //TODO
				&& !isProtected(player)) {
			growthState = GrowthState.DISEASED;
		}
		
		return true;
	}
	
	public int getWeedStage() {
		return weedStage;
	}
	
	public void setWeedStage(int weedStage) {
		this.weedStage = weedStage;
	}
	
	public void addAttribute(PatchAttribute patchAttribute) {
		attributes |= patchAttribute.getShift();
	}
	
	public void removeAttribute(PatchAttribute patchAttribute) {
		attributes &= ~patchAttribute.getShift();
	}
	
	public boolean hasAttribute(PatchAttribute patchAttribute) {
		return (attributes & patchAttribute.getShift()) != 0;
	}
	
	public long getTimePlanted() {
		return timePlanted;
	}
	
	public void setTimePlanted(long timePlanted) {
		this.timePlanted = timePlanted;
	}
	
	public int getDiseaseImmunity() {
		return diseaseImmunity;
	}
	
	public void setDiseaseImmunity(int diseaseImmunity) {
		this.diseaseImmunity = diseaseImmunity;
	}
	
	public Item getHarvestedItem() {
		return harvestedItem;
	}
	
	public void setHarvestedItem(Item harvestedItem) {
		this.harvestedItem = harvestedItem;
	}
	
	public Item getProduct() {
		return product;
	}
	
	public void setProduct(Item product) {
		this.product = product;
	}
}
