package com.varrock.world.content.alchemical_hydra;

import java.util.ArrayList;

import com.varrock.GameSettings;
import com.varrock.model.Animation;
import com.varrock.model.Flag;
import com.varrock.model.GameObject;
import com.varrock.model.Hit;
import com.varrock.model.Position;
import com.varrock.net.SessionState;
import com.varrock.util.Misc;
import com.varrock.world.World;
import com.varrock.world.content.combat.CombatContainer;
import com.varrock.world.content.combat.CombatType;
import com.varrock.world.content.combat.strategy.CombatStrategy;
import com.varrock.world.entity.impl.GameCharacter;
import com.varrock.world.entity.impl.npc.NPC;
import com.varrock.world.entity.impl.player.Player;

/**
 * Alchemical Hydra boss
 * @author Adil
 *
 */
public class AlchemicalHydra extends NPC implements CombatStrategy {

	private Player instanceOwner;

	private HydraState state = HydraState.NORMAL;

	private Position base;

	private int transformTicks, attackSwitch, fireWallTicks;
	
	private boolean shield, enraged;
	
	private HydraAttack attackType;
	
	private GameObject[] vents;
	
	private int ventDelay = 30;
	
	private int specialCount = 0;
	
	private long wallDelta;
	
	protected AlchemicalHydra(int id, Position position, Player instanceOwner) {
		super(id, position);
		this.instanceOwner = instanceOwner;
		this.attackType = Misc.random(10) > 5 ? HydraAttack.RANGED : HydraAttack.MAGIC;
		attackSwitch = 3;
		setSpecialCount(Misc.random(3, 5));
	}
	
	public void findObjs() {
		vents = new GameObject[3];
		vents[0] = new GameObject(134570, base.transform(34, 24, 0), 10);
		vents[1] = new GameObject(134569, base.transform(43, 24, 0), 10);
		vents[2] = new GameObject(134568, base.transform(43, 15, 0), 10); //1328, 10248
		for (GameObject vent : vents) {
			World.register(vent);
		}
	}
	
	public boolean decrementAttackCounter() {
		if (getSpecialCount() > 0) {
			setSpecialCount(getSpecialCount() - 1);
		}
		if (getSpecialCount() == 0) {
			setSpecialCount(5);
			return true;
		}
		if (--attackSwitch == 0) {
			attackType = attackType == HydraAttack.MAGIC ? HydraAttack.RANGED : HydraAttack.MAGIC;
			attackSwitch = state.getAttackCount();
		}
		return false;
	}
	
	public void transformNPC(int pnpc) {
		setTransformationId(pnpc);
		getUpdateFlag().flag(Flag.TRANSFORM);
	}
	
	@Override
	public void dealDamage(Hit hit) {
		if (shield || this.isEnraged()) {
			hit.setDamage((int) (hit.getDamage() * 0.25));
		}

		if (getConstitution() <= hit.getDamage()) {
			changeStage();
			setConstitution(0);
			return;
		}


		super.dealDamage(hit);

		double healthAmount = (getConstitution() * 1.0D) / (getDefinition().getHitpoints() * 1.0D);

		if(transformTicks == 0) {
			if(healthAmount <= 0.75 && healthAmount > 0.50 && state == HydraState.NORMAL) {
				changeStage();
			}
			if(healthAmount <= 0.50 && healthAmount > 0.25 && state == HydraState.BLUE) {
				changeStage();
			}
			if(healthAmount <= 0.25 && state == HydraState.RED) {
				changeStage();
			}
		}
	}
	
	@Override
	public void performAnimation(Animation animation) {
		if (transformTicks > 0 || animation == null) return;
		super.performAnimation(animation);
	}
	
	@Override
	public void sequence() {
		super.sequence();
		tick();
	}
	
	public void tick() {
			if (--ventDelay == 5) {
				findObjs();
				boolean shieldDropped = false;
				for (GameObject vent : vents) {
					if (vent == null) continue;
					switch (vent.getId()) {
					case 134570:
						if (getState() == HydraState.RED) {
							shieldDropped = getPosition().isWithinDiagonalDistance(vent.getPosition(), getSize(), 1);
						}
						break;
					case 134569:
						if (getState() == HydraState.BLUE) {
							shieldDropped = getPosition().isWithinDiagonalDistance(vent.getPosition(), getSize(), 1);
						}
						break;
					case 134568:
						if (getState() == HydraState.NORMAL) {
							shieldDropped = getPosition().isWithinDiagonalDistance(vent.getPosition(), getSize(), 1);
						}
						break;
					}
					for (Player player : World.getPlayers()) {
						if (player == null) continue;
						player.getPacketSender().sendObjectAnimation(vent, new Animation(8279 + GameSettings.OSRS_ANIM_OFFSET));
					}
				}
				if (shieldDropped) {
					System.out.println("shield dropped");
					forceChat("Roaaaaaaaaaaar!");
					if (state == HydraState.JAD) {
						this.setEnraged(true);
					} else {
						this.shield = false;
					}
				} else {
					this.shield = true;
				}
			} else if (ventDelay <= 0) {
				for (GameObject vent : vents) {
					if (vent == null) continue;
					vent.performAnimation(new Animation(8280 + GameSettings.OSRS_ANIM_OFFSET));
				}
				ventDelay = 30;
			}
		if (fireWallTicks > 0) {
			fireWallTicks--;
		}
		if (transformTicks > 0) {
			//performAnimation(new Animation(getState().getAnimation()));
			switch(--transformTicks) {
			case 1:
				getCombatBuilder().reset(true);
				break;
			case 0:
				transformNPC(getState().getPnpc());
				performAnimation(new Animation(HydraState.values()[getState().ordinal() - 1].getAnimation() + 1, 5));
				id = getTransformationId();
				break;
			}
		} else if(fireWallTicks < 1) {
			if(getCombatBuilder().getVictim() == null) {
				if(instanceOwner.getSession().getState() == SessionState.LOGGING_OUT || instanceOwner.getSession().getState() == SessionState.LOGGED_OUT) {
					setConstitution(0);
					World.deregister(this);
				}

				getCombatBuilder().attack(instanceOwner);
			}
		}
	}
	
	@Override
	public void heal(int heal) {
//		super.heal(heal);
	}
	@Override
	public Position getCentrePosition() {
		return super.getCentrePosition().transform(1, 1, 0);
	}
	
	private void changeStage() {
		getState().renderDeath(this);
		if (getState() != HydraState.JAD) {
			setState(HydraState.values()[getState().ordinal()+1]);
		}
	}
	
	@Override
	public boolean canAttack(GameCharacter entity, GameCharacter victim) {
		return victim.isPlayer();
	}

	@Override
	public CombatContainer attack(GameCharacter entity, GameCharacter victim) {
		return null;
	}

	@Override
	public int attackDelay(GameCharacter entity) {
		return 5;
	}

	@Override
	public int attackDistance(GameCharacter entity) {
		return 6;
	}
	
	@Override
	public CombatStrategy determineStrategy() {
		return this;
	}

	@Override
	public boolean customContainerAttack(GameCharacter entity, GameCharacter victim) {
		if (!decrementAttackCounter()) {
			getAttackType().attack(this, victim);
		} else {
			getAttackType().specialAttack(this, victim);
		}
		return true;
	}

	@Override
	public CombatType getCombatType() {
		return CombatType.MIXED;
	}

	/**
	 * @return the base
	 */
	public Position getBase() {
		return base;
	}

	/**
	 * @param base the base to set
	 */
	public void setBase(Position base) {
		this.base = base;
	}

	/**
	 * @return the state
	 */
	public HydraState getState() {
		return state;
	}

	/**
	 * @param state the state to set
	 */
	public void setState(HydraState state) {
		this.state = state;
	}

	/**
	 * @return the transformTicks
	 */
	public int getTransformTicks() {
		return transformTicks;
	}

	/**
	 * @param transformTicks the transformTicks to set
	 */
	public void setTransformTicks(int transformTicks) {
		this.transformTicks = transformTicks;
	}

	/**
	 * @return the attackSwitch
	 */
	public int getAttackSwitch() {
		return attackSwitch;
	}

	/**
	 * @param attackSwitch the attackSwitch to set
	 */
	public void setAttackSwitch(int attackSwitch) {
		this.attackSwitch = attackSwitch;
	}

	/**
	 * @return the attackType
	 */
	public HydraAttack getAttackType() {
		return attackType;
	}

	/**
	 * @param attackType the attackType to set
	 */
	public void setAttackType(HydraAttack attackType) {
		this.attackType = attackType;
	}
	
	/**
	 * @return the specialCount
	 */
	public int getSpecialCount() {
		return specialCount;
	}

	/**
	 * @param specialCount the specialCount to set
	 */
	public void setSpecialCount(int specialCount) {
		this.specialCount = specialCount;
	}

	public Animation getAttackAnimation() {
		if (state.getAttackAnimations().length > (attackSwitch - 1)){
			return state.getAttackAnimations()[attackSwitch == 0 ? 0 : attackSwitch - 1];
		}
		return new Animation(super.getDefinition().getAttackAnimation());
	}

	public ArrayList<Player> getPlayers() {
		ArrayList<Player> players = new ArrayList<>();
		for (Player player : World.getPlayers()) {
			if (player != null && player.getRegionID() == 5536 && player.getPosition().getZ() == getPosition().getZ()) {
				players.add(player);
			}
		}
		return players;
	}

	/**
	 * @return the fireWallTicks
	 */
	public int getFireWallTicks() {
		return fireWallTicks;
	}

	/**
	 * @param fireWallTicks the fireWallTicks to set
	 */
	public void setFireWallTicks(int fireWallTicks) {
		this.fireWallTicks = fireWallTicks;
	}

	/**
	 * @return the shield
	 */
	public boolean hasShield() {
		return shield;
	}

	/**
	 * @param shield the shield to set
	 */
	public void setShield(boolean shield) {
		this.shield = shield;
	}

	/**
	 * @return the vents
	 */
	public GameObject[] getVents() {
		return vents;
	}

	/**
	 * @param vents the vents to set
	 */
	public void setVents(GameObject[] vents) {
		this.vents = vents;
	}

	/**
	 * @return the wallDelta
	 */
	public long getWallDelta() {
		return wallDelta;
	}

	/**
	 * @param wallDelta the wallDelta to set
	 */
	public void setWallDelta(long wallDelta) {
		this.wallDelta = wallDelta;
	}

	/**
	 * @return the enraged
	 */
	public boolean isEnraged() {
		return enraged;
	}

	/**
	 * @param enraged the enraged to set
	 */
	public void setEnraged(boolean enraged) {
		this.enraged = enraged;
	}

	@Override
	public boolean isHydra() {
		return true;
	}

	@Override
	public void dropItems(Player killer) {
		super.dropItems(killer);
		killer.addBossPoints(2);
	}
}
