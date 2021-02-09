package com.zyrox.world.content.skill;

import com.zyrox.engine.task.Task;
import com.zyrox.engine.task.TaskManager;
import com.zyrox.model.Animation;
import com.zyrox.model.Item;
import com.zyrox.model.Skill;
import com.zyrox.model.container.impl.Equipment;
import com.zyrox.net.packet.impl.MovementPacketListener;
import com.zyrox.world.content.transportation.TeleportHandler;
import com.zyrox.world.entity.Entity;
import com.zyrox.world.entity.impl.player.Player;

/**
 * This abstract implementation of {@link AbstractSkill} is used
 * for skills that follow a general procedure - 
 * 
 * click entity - perform animation - harvest reward - repeat.
 *
 * @author relex lawl
 */
public abstract class AbstractHarvestSkill extends AbstractSkill {

	/**
	 * The AbstractHarvestSkill constructor.
	 * @param entity	The {@link org.niobe.model.Entity} being clicked upon.
	 */
	public AbstractHarvestSkill(Player player, Entity entity) {
		super(player);
		this.entity = entity;
	}

	private final Entity entity;
	
	/**
	 * The default ticks in which animation will begin.
	 * @return	{@value 4} by default.
	 */
	public int getAnimationTicks() {
		return 4;
	}
	
	/**
	 * The default flag that is checked every task
	 * tick to see if the skill can be continued or should
	 * it stop.
	 * @return	{@value true} by default.
	 */
	public boolean canContinueHarvest() {
		return true;
	}
	
	/**
	 * Executes the harvest action.
	 * @param entity	Entity being clicked upon.
	 */
	public void execute() {
		if (!MovementPacketListener.checkReqs(getPlayer(), MovementPacketListener.GAME_MOVEMENT_OPCODE)) {
			stopHarvest();
			return;
		}
		boolean canDoIt = TeleportHandler.checkReqs(getPlayer(), getPlayer().getPosition());
		if (!canDoIt) {
			getPlayer().getPacketSender().sendMessage("You cannot do this here!");
			return;
		}
		final Long delay_object = getPlayer().getDelays().get(getSkill());
		long delay = 0;
		if (delay_object != null) {
			delay = delay_object;
		}
		if (System.currentTimeMillis() - delay < getActionDelay()
				/*|| !getPlayer().getDragonSpear().elapsed(3000)*/)
			return;
		
		if (!canInitiate()) {
			stopHarvest();
			return;
		}
		
		if (getPlayer().isBusy()[getSkill().ordinal()])
			return;
		
		if (getPlayer().getHarvestSkill() != null) {
			getPlayer().getHarvestSkill().stopHarvest();
			return;
		}
		getPlayer().getDelays().put(getSkill(), System.currentTimeMillis());
		
		
		getPlayer().setHarvestSkill(this);
		getPlayer().setBusy(getSkill(), true);

//		getPlayer().setWalkToTask(null);
//		getPlayer().setCastSpell(null);
//		getPlayer().getCombatBuilder().cooldown(false);
		
		final Item[] offered = getOfferedItem();
		final Animation animation = getAnimation();
		
		if (animation != null) {
			getPlayer().performAnimation(animation);
		}
		
		TaskManager.submit(new Task(2, getPlayer(), false) {
			Item reward = getReward();
			boolean start_task = true;
			int animation_ticks = getAnimationTicks();
			int rewardTime = getCycles();
			int harvest = getHarvestAmount();
			@Override
			public void execute() {
				if (!canContinueHarvest() 
						|| getPlayer().getCombatBuilder().isBeingAttacked()
						|| !getPlayer().isBusy()[getSkill().ordinal()]
						|| getPlayer().getHarvestSkill() != AbstractHarvestSkill.this
						/*|| !getPlayer().getDragonSpear().elapsed(3000)*/) {
					if (getSkill() != Skill.FIREMAKING)
						stopHarvest();
					else
						performOutcome();
					stop();
					return;
				}
				
				if (offered != null) {
					for (Item item : offered) {
						if (getSkill() == Skill.SMITHING
								&& item.getId() == 453
								&& getPlayer().getEquipment().getItems()[Equipment.RING_SLOT].getId() == 2568) {
							continue;
						}
						if (item != null && !getPlayer().getInventory().contains(item.getId())) {
							stopHarvest();
							return;
						}
					}
				}
				
				if (reward != null) {
					int spaceRequired = reward.getDefinition().isStackable() ?
							1 : reward.getAmount();
					
					if (reward.getDefinition().isStackable()
							&& getPlayer().getInventory().contains(reward.getId())) {
						spaceRequired = 0;
					}
					
					if (offered != null) {
						for (Item item : offered) {
							if (item != null
									&& (!item.getDefinition().isStackable()
										|| item.getDefinition().isStackable() 
											&& getPlayer().getInventory().getAmount(item.getId()) - item.getAmount() <= 0)) {
								spaceRequired--;
							}
						}
					}
															
					if (getPlayer().getInventory().getFreeSlots() < spaceRequired) {
						getPlayer().getPacketSender().sendMessage("Not enough space in your inventory.");
						stopHarvest();
						return;
					}
				}
				
				if (start_task) {
					final String harvestMessage = getHarvestMessage();
					if (harvestMessage != null)
						getPlayer().getPacketSender().sendMessage(harvestMessage);
					
					setDelay(1);
					start_task = false;
				}
				
				rewardTime--;
												
				if (animation_ticks <= 0) {
					if (animation != null) {
						getPlayer().performAnimation(animation);
						animation_ticks = getAnimationTicks();
					}
				}
				
				animation_ticks--;
								
				if (rewardTime <= 0) {										
					if (entity != null)
						getPlayer().setPositionToFace(entity.getPosition());
					
					harvest--;
						
					if (offered != null) {
						for (Item item : offered) {
							if (item == null)
								continue;
							if (getSkill() == Skill.SMITHING
									&& item.getId() == 453
									&& getPlayer().getEquipment().getItems()[Equipment.RING_SLOT].getId() == 2568) {
								continue;
							}
							if (getPlayer().getInventory().contains(item.getId())) {
								getPlayer().getInventory().delete(item);
							} else {
								stopHarvest();
								stop();
								return;
							}
						}
					}

					double experience = getExperience();

					if (reward != null) {
						getPlayer().getInventory().add(reward);
					}
					final String message = getRewardMessage();
					if (message != null)
						getPlayer().getPacketSender().sendMessage(message);

					harvestAction();

					if (getSkill() != null && experience > 0) {
						getPlayer().getSkillManager().addExperience(getSkill(), (int) (experience) * getSkill().getModifier());
					}

					if (consecutiveRewards() && harvest > 0)
						rewardTime = getCycles();
					else {
						performOutcome();
						stopHarvest();
						stop();
					}
				}
			}
		});
	}
	
	/**
	 * Gets the entity that was clicked upon.
	 * @return	The {@link #entity} value.
	 */
	public Entity getEntity() {
		return entity;
	}

	/**
	 * Gets the reward item instance that player will harvest.
	 */
	public abstract Item getReward();
	
	/**
	 * Gets the message player receives upon harvest action.
	 */
	public abstract String getHarvestMessage();
	
	/**
	 * Gets the reward message.
	 */
	public abstract String getRewardMessage();
	
	/**
	 * Checks if player will get periodic rewards over time, such as in woodcutting and fishing.
	 */
	public abstract boolean consecutiveRewards();
	
	/**
	 * Gets the cycles player needs to wait to get rewarded.
	 */
	public abstract int getCycles();
	
	/**
	 * Gets the total amount of harvested items to be given.
	 */
	public abstract int getHarvestAmount();
	
	/**
	 * Performs the outcome of the harvest, once the player
	 * has harvested everything possible.
	 */
	public abstract void performOutcome();
	
	/**
	 * Gets the {@link org.niobe.model.Item} offered every harvest.
	 * @return	{@value null} by default.
	 */
	public Item[] getOfferedItem() {
		return null;
	}
	
	/**
	 * Handles extra harvest actions.
	 */
	public void harvestAction() {

	}
	
	/**
	 * This is called when the harvest task is
	 * stopped.
	 */
	public void stopHarvest() {
		getPlayer().setBusy(getSkill(), false);
		if (consecutiveRewards())
			getPlayer().performAnimation(new Animation(65535));
		getPlayer().setHarvestSkill(null);
	}
}
