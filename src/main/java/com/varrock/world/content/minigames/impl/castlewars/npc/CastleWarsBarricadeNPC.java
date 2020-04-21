package com.varrock.world.content.minigames.impl.castlewars.npc;

import com.varrock.model.Hit;
import com.varrock.model.Position;
import com.varrock.world.content.combat.strategy.CombatStrategies;
import com.varrock.world.content.combat.strategy.CombatStrategy;
import com.varrock.world.entity.Entity;
import com.varrock.world.entity.impl.GameCharacter;
import com.varrock.world.entity.impl.npc.NPC;

/**
 * Represents a castle wars barricade NPC
 * 
 * @author 2012 <https://www.rune-server.ee/members/dexter+morgan/>
 *
 */
public class CastleWarsBarricadeNPC extends NPC {

	/**
	 * Creates a new barricade npc
	 * 
	 * @param id       the id
	 * @param position the position
	 */
	public CastleWarsBarricadeNPC(int id, Position position) {
		super(id, position);
		this.getMovementQueue().setLockMovement(true);
		setConstitution(50);
	}

	@Override
	public GameCharacter setPositionToFace(Position positionToFace) {
		return this;
	}

	@Override
	public GameCharacter setEntityInteraction(Entity entity) {
		return this;
	}

	@Override
	public Hit decrementHealth(Hit hit) {
		super.decrementHealth(hit);
		return hit;
	}

	@Override
	public CombatStrategy determineStrategy() {
		return CombatStrategies.getEmptyCombatStrategy();
	}

	@Override
	public void setPoisonDamage(int poisonDamage) {
	}

	@Override
	public void setVenomDamage(int abc) {
	}
}
