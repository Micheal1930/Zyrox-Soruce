package com.zyrox.world.content.combat.strategy.impl;

import com.zyrox.model.Animation;
import com.zyrox.model.Locations.Location;
import com.zyrox.model.projectile.Projectile;
import com.zyrox.util.Misc;
import com.zyrox.world.content.combat.CombatContainer;
import com.zyrox.world.content.combat.CombatType;
import com.zyrox.world.content.combat.strategy.CombatStrategy;
import com.zyrox.world.entity.impl.GameCharacter;
import com.zyrox.world.entity.impl.npc.NPC;

public class Spinolyp implements CombatStrategy {

	@Override
	public boolean canAttack(GameCharacter entity, GameCharacter victim) {
		return true;
	}

	@Override
	public CombatContainer attack(GameCharacter entity, GameCharacter victim) {
		return null;
	}

	@Override
	public boolean customContainerAttack(GameCharacter entity, GameCharacter victim) {
		NPC spinolyp = (NPC)entity;
		if(spinolyp.getConstitution() <= 0 || victim.getConstitution() <= 0) {
			return true;
		}
		spinolyp.performAnimation(new Animation(spinolyp.getDefinition().getAttackAnimation()));
		boolean mage = Misc.getRandom(10) <= 7;
		new Projectile(spinolyp, victim, mage ? 1658 : 1017, 44, 3, 43, 43, 0).sendProjectile();
		spinolyp.getCombatBuilder().setContainer(new CombatContainer(spinolyp, victim, 1, mage ? 3 : 2, mage ? CombatType.MAGIC : CombatType.RANGED, true));
		return true;
	}


	@Override
	public int attackDelay(GameCharacter entity) {
		return entity.getAttackSpeed();
	}

	@Override
	public int attackDistance(GameCharacter entity) {
		return entity.getLocation() == Location.DUNGEONEERING ? 6 : 50;
	}
	
	@Override
	public CombatType getCombatType() {
		return CombatType.MIXED;
	}
}
