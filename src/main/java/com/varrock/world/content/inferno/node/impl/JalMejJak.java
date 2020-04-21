package com.varrock.world.content.inferno.node.impl;

import com.varrock.GameSettings;
import com.varrock.engine.task.Task;
import com.varrock.engine.task.TaskManager;
import com.varrock.model.*;
import com.varrock.model.projectile.Projectile;
import com.varrock.util.Misc;
import com.varrock.world.content.combat.CombatType;
import com.varrock.world.content.combat.prayer.PrayerHandler;
import com.varrock.world.content.inferno.TzkalZuk;
import com.varrock.world.content.inferno.node.InfernoNode;
import com.varrock.world.entity.impl.GameCharacter;
import com.varrock.world.entity.impl.npc.NPC;
import com.varrock.world.entity.impl.player.Player;

public class JalMejJak extends InfernoNode {
	
	private static final int ATTACK_ANIM = 18124;
	
	private TzkalZuk boss;
	
	private int healDelta = 1;

	public JalMejJak(Position position, TzkalZuk boss) {
		super(22708, position);
		this.boss = boss;
		performAnimation(new Animation(18128));
	}
	
	@Override
	public void sequence() {
		super.sequence();
		if (--healDelta == 0 && getConstitution() > 0) {
			getCombatBuilder().attack(boss);
			customContainerAttack(this, boss);
			healDelta = 10;
		}
	}

	@Override
	public boolean customContainerAttack(GameCharacter entity, GameCharacter victim) {
		this.performAnimation(new Animation(ATTACK_ANIM + 5));
		double distance = this.getPosition().getDistance(boss.getPosition());
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
        int delay = (gfxDelay / 20) - 3;
        Projectile proj =  new Projectile(getCentrePosition(), victim.getPosition(), victim.getProjectileIndex(), 660 + GameSettings.OSRS_GFX_OFFSET, 44, 3, 10, 43, 0);
        proj.sendProjectile();
		throttleFarcast(getAsNpc(), victim, delay, 0);
		return true;
	}
	
	@Override
	public void throttleFarcast(NPC attacker, GameCharacter victim, int delay, int prayer) {
		TaskManager.submit(new Task(delay) {
			
			@Override
			protected void execute() {
				int max = 10;
                boss.getTarget().getPacketSender().sendGlobalGraphic(new Graphic(661 + GameSettings.OSRS_GFX_OFFSET), boss.getPosition());

				if (PrayerHandler.isActivated((Player) victim,prayer)) {
					max *= 0.4;
				}

                if(victim.getLocation() == Locations.Location.INFERNO)
					victim.dealDamage(new Hit(Misc.random(max), TzkalZuk.HEAL_MASK, CombatIcon.NONE));

				stop();
				
			}
		});
	}

	@Override
	public CombatType getCombatType() {
		return CombatType.MAGIC;
	}

}
