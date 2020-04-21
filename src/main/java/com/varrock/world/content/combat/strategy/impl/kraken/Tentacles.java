package com.varrock.world.content.combat.strategy.impl.kraken;

import com.varrock.world.content.Kraken;
import com.varrock.world.content.combat.CombatContainer;
import com.varrock.world.content.combat.CombatType;
import com.varrock.world.content.combat.magic.CombatSpells;
import com.varrock.world.content.combat.strategy.CombatStrategy;
import com.varrock.world.entity.impl.GameCharacter;
import com.varrock.world.entity.impl.npc.NPC;
import com.varrock.world.entity.impl.player.Player;

public class Tentacles implements CombatStrategy {
	
	@Override
	public boolean canAttack(GameCharacter entity, GameCharacter victim) {
		return true;
	}

	@Override
	public CombatContainer attack(GameCharacter entity, GameCharacter victim) {
		NPC tentacle = (NPC)entity;
		Player player = (Player)victim;
		if(player.getKraken().getKrakenStage() == Kraken.KrakenStage.DEFEATED) {
			return null;
		}
		tentacle.prepareSpell(CombatSpells.TENTACLE_STRIKE.getSpell(), victim);
		return new CombatContainer(entity, victim, 1, CombatType.MAGIC, true);
	}

	@Override
	public boolean customContainerAttack(GameCharacter entity, GameCharacter victim) {
		return false;
	}

	@Override
	public int attackDelay(GameCharacter entity) {
		return 3;
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
