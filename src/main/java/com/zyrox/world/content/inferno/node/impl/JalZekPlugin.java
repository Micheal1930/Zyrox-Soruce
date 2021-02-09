package com.zyrox.world.content.inferno.node.impl;

import com.zyrox.GameSettings;
import com.zyrox.model.Animation;
import com.zyrox.model.Position;
import com.zyrox.model.projectile.Projectile;
import com.zyrox.world.content.combat.CombatType;
import com.zyrox.world.content.combat.prayer.PrayerHandler;
import com.zyrox.world.content.inferno.node.InfernoNode;
import com.zyrox.world.entity.impl.GameCharacter;

public class JalZekPlugin extends InfernoNode {

	public JalZekPlugin(int id, Position position) {
		super(id, position);
	}

	@Override
	public CombatType getCombatType() {
		return CombatType.MAGIC;
	}
	
	@Override
	public void sequence() {
		super.sequence();
	}

	@Override
	public boolean customContainerAttack(GameCharacter attacker, GameCharacter victim) {
		attacker.performAnimation(new Animation(attacker.getAsNpc().getDefinition().getAttackAnimation()));
		double distance = attacker.getPosition().getDistance(victim.getPosition());
       int clientSpeed = 0, gfxDelay = 0;
		if(distance <= 1) {
            clientSpeed = 70;
            gfxDelay = 80;
        } else if(distance <= 5) {
            clientSpeed = 90;
            gfxDelay = 100;
        } else if(distance <= 8) {
            clientSpeed = 110;
            gfxDelay = 120;
        } else {
            clientSpeed = 130;
            gfxDelay = 140;
        }
        int delay = (gfxDelay / 20) - 1;
		Projectile proj =  new Projectile(attacker.getCentrePosition(), victim.getPosition(), victim.getProjectileIndex(), 1376 + GameSettings.OSRS_GFX_OFFSET, 44, 3, 43, 43, 0);
        proj.sendProjectile();
		throttleFarcast(attacker.getAsNpc(), victim, delay, PrayerHandler.PROTECT_FROM_MAGIC);
		return true;
	}
	
	@Override
	public Position getCentrePosition() {
		return super.getCentrePosition().transform(1, 1, 0);
	}


}
