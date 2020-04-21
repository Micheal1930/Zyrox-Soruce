package com.varrock.world.content.combat.strategy.impl;

import com.varrock.util.Misc;
import com.varrock.world.content.combat.CombatContainer;
import com.varrock.world.content.combat.CombatType;
import com.varrock.world.content.combat.magic.CombatSpells;
import com.varrock.world.content.combat.strategy.CombatStrategy;
import com.varrock.world.entity.impl.GameCharacter;
import com.varrock.world.entity.impl.npc.NPC;

/**
 * @author Jonathan Sirens
 */

public class Cobra implements CombatStrategy {

	@Override
	public boolean canAttack(GameCharacter entity, GameCharacter victim) {
		return true;
	}

	@Override
	public boolean customContainerAttack(GameCharacter entity, GameCharacter victim) {
		return false;
	}

	@Override
	public CombatContainer attack(GameCharacter entity, GameCharacter victim) {
		NPC cobra = (NPC)entity;
		int att = Misc.getRandom(3);
		if(att == 1) {
			cobra.prepareSpell(CombatSpells.BLOOD_BARRAGE.getSpell(), victim);
		}
		if(att == 2) {
			cobra.prepareSpell(CombatSpells.ICE_BARRAGE.getSpell(), victim);
		}
		if(att == 3) {
			cobra.prepareSpell(CombatSpells.ICE_BLITZ.getSpell(), victim);
		}
		return new CombatContainer(entity, victim, 1, CombatType.MAGIC, true);
	}
	@Override
	public int attackDelay(GameCharacter entity) {
		return entity.getAttackSpeed();
	}

	@Override
	public int attackDistance(GameCharacter entity) {
		return 15;
	}

	@Override
	public CombatType getCombatType() {
		return CombatType.MAGIC;
	}
}
