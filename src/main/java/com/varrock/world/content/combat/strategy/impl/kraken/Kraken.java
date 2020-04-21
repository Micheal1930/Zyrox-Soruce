package com.varrock.world.content.combat.strategy.impl.kraken;

import com.varrock.world.content.combat.CombatContainer;
import com.varrock.world.content.combat.CombatType;
import com.varrock.world.content.combat.magic.CombatSpells;
import com.varrock.world.content.combat.strategy.CombatStrategy;
import com.varrock.world.entity.impl.GameCharacter;
import com.varrock.world.entity.impl.npc.NPC;

public class Kraken implements CombatStrategy {

	public boolean canAttack(GameCharacter entity, GameCharacter victim) {
		return true;
	}

	@Override
	public CombatContainer attack(GameCharacter entity, GameCharacter victim) {
		NPC kraken = (NPC)entity;
		kraken.prepareSpell(CombatSpells.KRAKEN_STRIKE.getSpell(), victim);
		return new CombatContainer(entity, victim, 1, CombatType.MAGIC, true);
	}

	@Override
	public boolean customContainerAttack(GameCharacter entity, GameCharacter victim) {
		return false;
	}

	@Override
	public int attackDelay(GameCharacter entity) {
		return 5;
	}

	@Override
	public int attackDistance(GameCharacter entity) {
		return 30;
	}

	@Override
	public CombatType getCombatType() {
		return CombatType.MAGIC;
	}

}