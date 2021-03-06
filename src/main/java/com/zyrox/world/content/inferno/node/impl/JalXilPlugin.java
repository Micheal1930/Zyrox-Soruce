package com.zyrox.world.content.inferno.node.impl;

import com.zyrox.GameSettings;
import com.zyrox.model.Animation;
import com.zyrox.model.Position;
import com.zyrox.model.projectile.Projectile;
import com.zyrox.world.content.combat.CombatType;
import com.zyrox.world.content.combat.prayer.PrayerHandler;
import com.zyrox.world.content.inferno.node.InfernoNode;
import com.zyrox.world.entity.impl.GameCharacter;

public class JalXilPlugin extends InfernoNode {

	public JalXilPlugin(int id, Position position) {
		super(id, position);
	}

	@Override
	public CombatType getCombatType() {
		return CombatType.RANGED;
	}

	@Override
	public boolean customContainerAttack(GameCharacter attacker, GameCharacter victim) {
		attacker.performAnimation(new Animation(attacker.getAsNpc().getDefinition().getAttackAnimation()));
		double distance = attacker.getPosition().getDistance(victim.getPosition());
       int gfxDelay = 0;
		if(distance <= 1) {
            gfxDelay = 80;
        } else if(distance <= 5) {
            gfxDelay = 100;
        } else if(distance <= 8) {
            gfxDelay = 120;
        } else {
            gfxDelay = 140;
        }
        int delay = (gfxDelay / 20) - 1;
        Projectile second = new Projectile(attacker.getCentrePosition().transform(-1, 0, 0), victim.getPosition(), victim.getProjectileIndex(), 1377 + GameSettings.OSRS_GFX_OFFSET, 44, 3, 100, 43, 0);
        Projectile proj =  new Projectile(attacker.getCentrePosition().transform(1, 0, 0), victim.getPosition(), victim.getProjectileIndex(), 1377 + GameSettings.OSRS_GFX_OFFSET, 44, 3, 100, 43, 0);
        proj.sendProjectile();
        second.sendProjectile();
		throttleFarcast(attacker.getAsNpc(), victim, delay, PrayerHandler.PROTECT_FROM_MISSILES);
		return true;
	}
	

}
