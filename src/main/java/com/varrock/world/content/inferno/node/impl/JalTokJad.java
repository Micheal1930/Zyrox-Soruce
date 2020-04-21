package com.varrock.world.content.inferno.node.impl;

import com.varrock.engine.task.Task;
import com.varrock.engine.task.TaskManager;
import com.varrock.model.Animation;
import com.varrock.model.Graphic;
import com.varrock.model.Locations;
import com.varrock.model.Position;
import com.varrock.model.projectile.Projectile;
import com.varrock.util.Misc;
import com.varrock.world.content.combat.CombatContainer;
import com.varrock.world.content.combat.CombatType;
import com.varrock.world.content.inferno.node.InfernoNode;
import com.varrock.world.entity.impl.GameCharacter;
import com.varrock.world.entity.impl.npc.NPC;

public class JalTokJad extends InfernoNode {

	public JalTokJad(int id, Position position) {
		super(id, position);
	}

	@Override
	public boolean customContainerAttack(GameCharacter entity, GameCharacter victim) {
		NPC JalTokJad = this;
		if (victim.getConstitution() <= 0 || victim.getConstitution() <= 0) {
			return true;
		}
		if (JalTokJad.getConstitution() <= 1000 && !JalTokJad.hasHealed()) {
			JalTokJad.performGraphic(gfx1);
			JalTokJad.heal(500 + Misc.getRandom(500));
			JalTokJad.setHealed(true);
		}
		if (JalTokJad.isChargingAttack()) {
			return true;
		}
		int random = Misc.getRandom(10);
		if (random <= 8 && Locations.goodDistance(JalTokJad.getPosition(), victim.getPosition(), 1)) {
			JalTokJad.performAnimation(anim2);
			JalTokJad.getCombatBuilder()
					.setContainer(new CombatContainer(JalTokJad, victim, 1, 2, CombatType.MELEE, true));
		} else if (random <= 4 && Locations.goodDistance(JalTokJad.getPosition(), victim.getPosition(), 14)) {
			JalTokJad.performAnimation(anim3);
			JalTokJad.setChargingAttack(true);

			TaskManager.submit(new Task(1, JalTokJad, false) {
				int tick = 0;

				@Override
				public void execute() {
					switch (tick) {
					case 0:
						JalTokJad.getCombatBuilder()
						.setContainer(new CombatContainer(JalTokJad, victim, 1, 1, CombatType.MAGIC, true));
						break;
					case 2:
						new Projectile(JalTokJad, victim, gfx5.getId(), 44, 3, 43, 31, 0).sendProjectile();
						break;
					case 3:
						JalTokJad.setChargingAttack(false);
						stop();
						break;
					}
					tick++;
				}
			});
		} else {
			JalTokJad.performAnimation(anim4);
			JalTokJad.setChargingAttack(true);
			TaskManager.submit(new Task(1, JalTokJad, false) {
				int tick = 0;

				@Override
				public void execute() {
					switch (tick) {
					case 1:
						JalTokJad.getCombatBuilder()
								.setContainer(new CombatContainer(JalTokJad, victim, 1, 1, CombatType.RANGED, true));
						break;
					case 3:
						victim.performGraphic(gfx4);
						JalTokJad.setChargingAttack(false);
						stop();
						break;
					}
					tick++;
				}
			});
		}
		return true;
	}

	@Override
	public int attackDelay(GameCharacter entity) {
		return entity.getAttackSpeed();
	}

	@Override
	public int attackDistance(GameCharacter entity) {
		return 30;
	}

	private static final Animation anim2 = new Animation(22850);
	private static final Animation anim3 = new Animation(22852);
	private static final Animation anim4 = new Animation(22853);
	private static final Graphic gfx1 = new Graphic(444);
	private static final Graphic gfx4 = new Graphic(451);
	private static final Graphic gfx5 = new Graphic(1627);

	
	@Override
	public CombatType getCombatType() {
		return CombatType.MIXED;
	}

}
