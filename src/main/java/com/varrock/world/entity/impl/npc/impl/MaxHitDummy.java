package com.varrock.world.entity.impl.npc.impl;

import com.varrock.model.Hit;
import com.varrock.model.Position;
import com.varrock.world.content.combat.strategy.CombatStrategies;
import com.varrock.world.content.combat.strategy.CombatStrategy;
import com.varrock.world.entity.Entity;
import com.varrock.world.entity.impl.GameCharacter;
import com.varrock.world.entity.impl.npc.NPC;

public class MaxHitDummy extends NPC {

	public MaxHitDummy(int id, Position position) {
		super(id, position);
		this.getMovementQueue().setLockMovement(true);
		maxHitDummy = true;
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
