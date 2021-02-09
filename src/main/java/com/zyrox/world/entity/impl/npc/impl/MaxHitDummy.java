package com.zyrox.world.entity.impl.npc.impl;

import com.zyrox.model.Hit;
import com.zyrox.model.Position;
import com.zyrox.world.content.combat.strategy.CombatStrategies;
import com.zyrox.world.content.combat.strategy.CombatStrategy;
import com.zyrox.world.entity.Entity;
import com.zyrox.world.entity.impl.GameCharacter;
import com.zyrox.world.entity.impl.npc.NPC;

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
