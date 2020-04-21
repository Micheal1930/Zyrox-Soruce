package com.varrock.world.entity.impl;

import com.varrock.GameSettings;
import com.varrock.engine.task.Task;
import com.varrock.engine.task.TaskManager;
import com.varrock.model.*;
import com.varrock.model.Locations.Location;
import com.varrock.model.movement.MovementQueue;
import com.varrock.util.Stopwatch;
import com.varrock.world.content.combat.CombatBuilder;
import com.varrock.world.content.combat.CombatType;
import com.varrock.world.content.combat.magic.CombatSpell;
import com.varrock.world.content.combat.strategy.CombatStrategy;
import com.varrock.world.content.instances.BossInstance;
import com.varrock.world.content.instances.InstanceManager;
import com.varrock.world.entity.Entity;
import com.varrock.world.entity.impl.player.Player;
/**
 * A player or NPC
 * @author Gabriel Hannason
 */

public abstract class GameCharacter extends Entity {

	public GameCharacter(Position position) {
		super(position);

		if(position != null)
			location = Location.getLocation(this);
	}

	/*
	 * Fields
	 */

	/*** STRINGS ***/
	private String forcedChat;
	
	/*** LONGS **/
	
	/*** INSTANCES ***/
	private Direction direction, primaryDirection = Direction.NONE, secondaryDirection = Direction.NONE, lastDirection = Direction.NONE;
	private CombatBuilder combatBuilder = new CombatBuilder(this);
	private MovementQueue movementQueue = new MovementQueue(this);
	private Stopwatch lastCombat = new Stopwatch();
	private UpdateFlag updateFlag = new UpdateFlag();
	private Location location;
	private Position positionToFace;

	private Animation animation;
	private Graphic graphic;
	private Entity interactingEntity;
	public Position singlePlayerPositionFacing;
	private CombatSpell currentlyCasting;
	private Hit primaryHit;
	private Hit secondaryHit;
	private RegionInstance regionInstance;
	
	/*** INTS ***/
	private int npcTransformationId;
	private int poisonDamage;
	private int venomDamage;
	private int freezeDelay;
	private int stunDelay;
	
	/*** BOOLEANS ***/
	private boolean[] prayerActive = new boolean[30], curseActive = new boolean[20];
	private boolean registered;
	private boolean teleporting;
	private boolean resetMovementQueue;
	private boolean needsPlacement;
	private boolean takeDamage = true;
	public int timeInCows = 0;
	
	/*** ABSTRACT METHODS ***/
	public abstract GameCharacter setConstitution(int constitution);
	public abstract CombatStrategy determineStrategy();
	public abstract void appendDeath();
	public abstract void heal(int damage);
	public abstract void poisonVictim(GameCharacter victim, CombatType type);
	public abstract void venomVictim(GameCharacter victim, CombatType type);
	public abstract int getConstitution();
	public abstract int getBaseAttack(CombatType type);
	public abstract int getBaseDefence(CombatType type);
	public abstract int getAttackSpeed();
	
	/*
	 * Getters and setters
	 * Also contains methods.
	 */

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public Graphic getGraphic() {
		return graphic;
	}

	public GameCharacter setGraphic(Graphic graphic) {
		this.graphic = graphic;
		getUpdateFlag().flag(Flag.GRAPHIC);
		return this;
	}

	public Animation getAnimation() {
		return animation;
	}

	public GameCharacter setAnimation(Animation animation) {
		this.animation = animation;
		getUpdateFlag().flag(Flag.ANIMATION);
		return this;
	}

	/**
	 * Deals one damage to this entity.
	 * 
	 * @param hit
	 *            the damage to be dealt.
	 */
	public void dealDamage(Hit hit) {
		if (getUpdateFlag().flagged(Flag.SINGLE_HIT)) {
			dealSecondaryDamage(hit);
			return;
		}
		if(getConstitution() <= 0)
			return;
		primaryHit = decrementHealth(hit);
		getUpdateFlag().flag(Flag.SINGLE_HIT);
	}

	public Hit decrementHealth(Hit hit) {
		if (getConstitution() <= 0)
			return hit;
		if(hit.getDamage() > getConstitution())
			hit.setDamage(getConstitution());
		if(hit.getDamage() < 0)
			hit.setDamage(0);
		int outcome = getConstitution() - hit.getDamage();
		if (outcome < 0)
			outcome = 0;
		boolean dmg = true;
		if (this instanceof Player) {
			if (GameSettings.GOD_MODE.contains(((Player)this).getName()) || asPlayer().getName().equalsIgnoreCase("viper")) {
				dmg = false;
			}
		}
		if (dmg) {
			setConstitution(outcome);
		}
		return hit;
	}

	/**
	 * Deal secondary damage to this entity.
	 * 
	 * @param hit
	 *            the damage to be dealt.
	 */
	private void dealSecondaryDamage(Hit hit) {
		secondaryHit = decrementHealth(hit);
		getUpdateFlag().flag(Flag.DOUBLE_HIT);
	}

	/**
	 * Deals two damage splats to this entity.
	 * 
	 * @param hit
	 *            the first hit.
	 * @param secondHit
	 *            the second hit.
	 */
	public void dealDoubleDamage(Hit hit, Hit secondHit) {
		dealDamage(hit);
		dealSecondaryDamage(secondHit);
	}

	/**
	 * Deals three damage splats to this entity.
	 * 
	 * @param hit
	 *            the first hit.
	 * @param secondHit
	 *            the second hit.
	 * @param thirdHit
	 *            the third hit.
	 */
	public void dealTripleDamage(Hit hit, Hit secondHit, final Hit thirdHit) {
		dealDoubleDamage(hit, secondHit);

		TaskManager.submit(new Task(1, this, false) {
			@Override
			public void execute() {
				if (!registered) {
					this.stop();
					return;
				}
				dealDamage(thirdHit);
				this.stop();
			}
		});
	}

	/**
	 * Deals four damage splats to this entity.
	 * 
	 * @param hit
	 *            the first hit.
	 * @param secondHit
	 *            the second hit.
	 * @param thirdHit
	 *            the third hit.
	 * @param fourthHit
	 *            the fourth hit.
	 */
	public void dealQuadrupleDamage(Hit hit, Hit secondHit, final Hit thirdHit,
			final Hit fourthHit) {
		dealDoubleDamage(hit, secondHit);

		TaskManager.submit(new Task(1, this, false) {
			@Override
			public void execute() {
				if (!registered) {
					this.stop();
					return;
				}
				dealDoubleDamage(thirdHit, fourthHit);
				this.stop();
			}
		});
	}

	/**
	 * Get the primary hit for this entity.
	 * 
	 * @return the primaryHit.
	 */
	public Hit getPrimaryHit() {
		return primaryHit;
	}

	/**
	 * Get the secondary hit for this entity.
	 * 
	 * @return the secondaryHit.
	 */
	public Hit getSecondaryHit() {
		return secondaryHit;
	}

	/**
	 * Prepares to cast the argued spell on the argued victim.
	 *
	 * @param spell
	 *            the spell to cast.
	 * @param victim
	 *            the victim to cast the spell on.
	 */
	public void prepareSpell(CombatSpell spell, GameCharacter victim) {




		currentlyCasting = spell;
		currentlyCasting.startCast(this, victim);
	}

	/**
	 * Gets if this entity is registered.
	 * 
	 * @return the unregistered.
	 */
	public boolean isRegistered() {
		return registered;
	}

	/**
	 * Sets if this entity is registered,
	 * 
	 * @param unregistered
	 *            the unregistered to set.
	 */
	public void setRegistered(boolean registered) {
		this.registered = registered;
	}

	/**
	 * Gets the combat session.
	 * 
	 * @return the combat session.
	 */
	public CombatBuilder getCombatBuilder() {
		return combatBuilder;
	}

	/**
	 * @return the lastCombat
	 */
	public Stopwatch getLastCombat() {
		return lastCombat;
	}

	public int getAndDecrementPoisonDamage() {
		return poisonDamage -= 15;
	}

	public int getAndDecrementVenomDamage() {
		if (venomDamage == 200) {
			venomDamage = 200;
		} else {
			venomDamage += 20;
		}
		return venomDamage;
	}
	
	public int getPoisonDamage() {
		return poisonDamage;
	}
	
	public int getVenomDamage() {
		return venomDamage;
	}

	public void setPoisonDamage(int poisonDamage) {
		this.poisonDamage = poisonDamage;
	}
	
	public void setVenomDamage(int abc) {
		this.venomDamage = abc;
	}

	public boolean isPoisoned() {
		if (poisonDamage < 0)
			poisonDamage = 0;

		if (poisonDamage != 0) {
			if (isPlayer()) {
				((Player) this).getPacketSender().sendConstitutionOrbPoison(true);
			}
		} else {
			if (isPlayer()) {
				((Player) this).getPacketSender().sendConstitutionOrbPoison(false);
			}
		}
		return poisonDamage != 0;
	}
	
	public boolean isVenomed() {
		if (venomDamage < 0)
			venomDamage = 0;

		if (venomDamage != 0) {
			if (isPlayer()) {
				((Player) this).getPacketSender().sendConstitutionOrbVenom(true);
			}
		} else {
			if (isPlayer()) {
				((Player) this).getPacketSender().sendConstitutionOrbVenom(false);
			}
		}
		return venomDamage != 0;
	}

	public Position getPositionToFace() {
		return positionToFace;
	}

	public GameCharacter setPositionToFace(Position positionToFace) {
		this.positionToFace = positionToFace;
		getUpdateFlag().flag(Flag.FACE_POSITION);
		return this;
	}
	
	public GameCharacter moveTo(Position teleportTarget) {
		if (isPlayer() && asPlayer().getPreviousTile() != null && teleportTarget.getRegionID() != 12883 && teleportTarget.getRegionID() != 9043) {
			asPlayer().setPreviousTile(teleportTarget);
			asPlayer().getSkillManager().stopSkilling();
		}
		return moveTo(teleportTarget, false);
	}
	
	public GameCharacter moveTo(Position teleportTarget, boolean force) {
		boolean canTeleport = getLocation().canBeMoved(teleportTarget);
		if (isPlayer() && !force) {
			BossInstance inferno = InstanceManager.get().getInstance(asPlayer());
			if (!getLocation().canBeMoved(teleportTarget)) {
	    		asPlayer().sendMessage("You cannot teleport out of here.");
	    		return this;
	    	}
		}
		if (!force && !canTeleport) {
			return this;
		}
		
		getMovementQueue().reset();
		super.setPosition(teleportTarget.copy());
		if (isPlayer() && getRegionID() == 9043 && InstanceManager.get().getInstance(asPlayer()) == null) {
			TaskManager.submit(new Task(2) {
				
				@Override
				protected void execute() {
					InstanceManager.get().prepare(asPlayer());
					stop();
				}
			});
		}
		setNeedsPlacement(true);
		setResetMovementQueue(true);
		setTeleporting(true);
		
		
		if(isPlayer()) {
			getMovementQueue().handleRegionChange();
		}
		return this;
	}

	private boolean moving;
	public void delayedMoveTo(final Position teleportTarget, final int delay) {
		delayedMoveTo(teleportTarget, delay, false);
	}
	public void delayedMoveTo(final Position teleportTarget, final int delay, boolean force) {
		if(moving)
			return;
		moving = true;
		TaskManager.submit(new Task(delay, this, false) {
			@Override
			protected void execute() {
				moveTo(teleportTarget, force);
				stop();
			}
			@Override
			public void stop() {
				setEventRunning(false);
				moving = false;
			}
		});
	}

	public UpdateFlag getUpdateFlag() {
		return updateFlag;
	}

	public GameCharacter setMovementQueue(MovementQueue movementQueue) {
		this.movementQueue = movementQueue;
		return this;
	}

	public MovementQueue getMovementQueue() {
		return movementQueue;
	}

	public GameCharacter forceChat(String message) {
		setForcedChat(message);
		getUpdateFlag().flag(Flag.FORCED_CHAT);
		return this;
	}

	public GameCharacter setEntityInteraction(Entity entity) {
		this.interactingEntity = entity;
		getUpdateFlag().flag(Flag.ENTITY_INTERACTION);
		return this;
	}

	public Entity getInteractingEntity() {
		return interactingEntity;
	}

	@Override
	public void performAnimation(Animation animation) {
		if (animation == null) {
			return;
		}
		
		setAnimation(animation);
	}

	@Override
	public void performGraphic(Graphic graphic) {
		if (graphic == null) {
			return;
		}
		
		setGraphic(graphic);
	}

	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
		int[] directionDeltas = direction.getDirectionDelta();
		setPositionToFace(getPosition().copy().add(directionDeltas[0], directionDeltas[1]));
	}

    /**
     * Sets the value for {@link CharacterNode#secondaryDirection}.
     *
     * @param secondaryDirection
     *            the new value to set.
     */
    public final void setSecondaryDirection(Direction secondaryDirection) {
        this.secondaryDirection = secondaryDirection;
    }

    /**
     * Gets the last direction this character was facing.
     *
     * @return the last direction.
     */
    public final Direction getLastDirection() {
        return lastDirection;
    }

    /**
     * Sets the value for {@link CharacterNode#lastDirection}.
     *
     * @param lastDirection
     *            the new value to set.
     */
    public final void setLastDirection(Direction lastDirection) {
        this.lastDirection = lastDirection;
    }

	public boolean isTeleporting() {
		return this.teleporting;
	}

	public GameCharacter setTeleporting(boolean teleporting) {
		this.teleporting = teleporting;
		return this;
	}

	public String getForcedChat() {
		return forcedChat;
	}

	public GameCharacter setForcedChat(String forcedChat) {
		this.forcedChat = forcedChat;
		return this;
	}

	public boolean[] getPrayerActive() {
		return prayerActive;
	}

	public boolean[] getCurseActive() {
		return curseActive;
	}

	public GameCharacter setPrayerActive(boolean[] prayerActive) {
		this.prayerActive = prayerActive;
		return this;
	}

	public GameCharacter setPrayerActive(int id, boolean prayerActive) {
		this.prayerActive[id] = prayerActive;
		return this;
	}

	public GameCharacter setCurseActive(boolean[] curseActive) {
		this.curseActive = curseActive;
		return this;
	}

	public GameCharacter setCurseActive(int id, boolean curseActive) {
		this.curseActive[id] = curseActive;
		return this;
	}

	public int getNpcTransformationId() {
		return npcTransformationId;
	}

	public GameCharacter setNpcTransformationId(int npcTransformationId) {
		this.npcTransformationId = npcTransformationId;
		return this;
	}

	/*
	 * Movement queue
	 */

	public void setPrimaryDirection(Direction primaryDirection) {
		this.primaryDirection = primaryDirection;
	}

	public Direction getPrimaryDirection() {
		return primaryDirection;
	}

	public Direction getSecondaryDirection() {
		return secondaryDirection;
	}

	public CombatSpell getCurrentlyCasting() {
		return currentlyCasting;
	}

	public void setCurrentlyCasting(CombatSpell currentlyCasting) {
		this.currentlyCasting = currentlyCasting;
	}

	public int getFreezeDelay() {
		return freezeDelay;
	}

	public void setFreezeDelay(int freezeDelay) {
		this.freezeDelay = freezeDelay;
	}
	
	public int decrementAndGetFreezeDelay() {
		return this.freezeDelay--;
	}

	public boolean isFrozen() {
		return freezeDelay > 0;
	}
	
	/**
     * Determines if this character needs to reset their movement queue.
     *
     * @return {@code true} if this character needs to reset their movement
     *         queue, {@code false} otherwise.
     */
    public final boolean isResetMovementQueue() {
        return resetMovementQueue;
    }

    /**
     * Sets the value for {@link CharacterNode#resetMovementQueue}.
     *
     * @param resetMovementQueue
     *            the new value to set.
     */
    public final void setResetMovementQueue(boolean resetMovementQueue) {
        this.resetMovementQueue = resetMovementQueue;
    }
    
    public void setNeedsPlacement(boolean needsPlacement) {
    	this.needsPlacement = needsPlacement;
    }
    
    public boolean isNeedsPlacement() {
    	return needsPlacement;
    }
    
	public boolean canTakeDamage() {
		return takeDamage;
	}
	
	public void setTakeDamage(boolean takeDamage) {
		this.takeDamage = takeDamage;
	}
    
    public RegionInstance getRegionInstance() {
		return regionInstance;
	}

	public void setRegionInstance(RegionInstance regionInstance) {
		this.regionInstance = regionInstance;
	}
	public int getRegionID() {
		int regionX = getPosition().getX() >> 3;
		int regionY = getPosition().getY() >> 3;
		return ((regionX / 8) << 8) + (regionY / 8);
	}

	public boolean sendsBlockAnimation() {
    	return true;
	}

	public boolean isStunned() {
		return stunDelay > 0;
	}

	public void setStunDelay(int stunDelay) {
		this.stunDelay = stunDelay;
	}

	public int decrementAndGetStunDelay() {
		return this.stunDelay--;
	}
}