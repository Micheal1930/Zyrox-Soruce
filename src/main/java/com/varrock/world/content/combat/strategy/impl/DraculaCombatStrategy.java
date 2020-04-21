package com.varrock.world.content.combat.strategy.impl;

import com.varrock.engine.task.Task;
import com.varrock.engine.task.TaskManager;
import com.varrock.model.*;
import com.varrock.model.Locations.Location;
import com.varrock.model.projectile.Projectile;
import com.varrock.util.Misc;
import com.varrock.world.content.combat.CombatContainer;
import com.varrock.world.content.combat.CombatHitTask;
import com.varrock.world.content.combat.CombatType;
import com.varrock.world.content.combat.magic.CombatSpells;
import com.varrock.world.content.combat.magic.Spell;
import com.varrock.world.content.combat.strategy.CombatStrategy;
import com.varrock.world.entity.impl.GameCharacter;
import com.varrock.world.entity.impl.npc.NPC;
import com.varrock.world.entity.impl.player.Player;

public class DraculaCombatStrategy implements CombatStrategy {

	public static int CAST_SPELL_ANIM = 16071;
	public static int PUSH_BACK_ANIM = 16070;

	@Override
	public boolean canAttack(GameCharacter entity, GameCharacter victim) {
		return victim.isPlayer();
	}

	@Override
	public CombatContainer attack(GameCharacter entity, GameCharacter victim) {

		int chance = Misc.random(7);

		NPC dracula = (NPC)entity;

		if(chance > 1) {
			int att = Misc.getRandom(SPELLS.length - 1);

			if (victim.isPlayer()) {
				for (Player t : Misc.getCombinedPlayerList((Player) victim)) {
					if (t == null)
						continue;

					if(!t.getCombatBuilder().isAttacking())
						continue;

					dracula.prepareSpell(SPELLS[att].getSpell(), t);
				}
			}

			dracula.performAnimation(new Animation(CAST_SPELL_ANIM));
		} else {
			dracula.performAnimation(new Animation(PUSH_BACK_ANIM));
			dracula.performGraphic(new Graphic(253));

			if (victim.isPlayer()) {

				TaskManager.submit(new Task(3, victim, false) {
					@Override
					public void execute() {

						for (Player t : Misc.getCombinedPlayerList((Player) victim)) {
							if (t == null)
								continue;

							if(!t.getCombatBuilder().isAttacking())
								continue;

							if (t.getPosition().isWithinDistance(dracula.getPosition(), 3)) {
								dracula.getCombatBuilder().setVictim(t);
								new CombatHitTask(dracula.getCombatBuilder(), new CombatContainer(dracula, t, 1, CombatType.MELEE, true)).handleAttack();

								if (t.isPlayer()) {
									int moveX = t.getPosition().getX() - dracula.getPosition().getX();
									int moveY = t.getPosition().getY() - dracula.getPosition().getY();
									if (moveX > 0) {
										moveX = 1;
									} else if (moveX < 0) {
										moveX = -1;
									}
									if (moveY > 0) {
										moveY = 1;
									} else if (moveY < 0) {
										moveY = -1;
									}
									if (t.getMovementQueue().canWalk(moveX, moveY)) {
										t.setEntityInteraction(dracula);
										t.getMovementQueue().reset();
										t.getMovementQueue().walkStep(moveX, moveY);
									}
								}
								t.performGraphic(new Graphic(254, GraphicHeight.HIGH));
								t.getMovementQueue().freeze(6);
							}
						}
						stop();
					}
				});
			}

		}


		return new CombatContainer(entity, victim, 1, CombatType.MAGIC, true);
	}

	@Override
	public boolean customContainerAttack(GameCharacter entity, GameCharacter victim) {
		return false;
	}

	@Override
	public int attackDelay(GameCharacter entity) {
		return entity.getAttackSpeed();
	}

	@Override
	public int attackDistance(GameCharacter entity) {
		return 9;
	}

	@Override
	public CombatType getCombatType() {
		return CombatType.MIXED;
	}

	private static final CombatSpells[] SPELLS = {
			CombatSpells.BLOOD_RUSH,
			CombatSpells.BLOOD_BARRAGE,
			CombatSpells.FIRE_BLAST,
			CombatSpells.BLOOD_BLITZ,
			CombatSpells.FLAMES_OF_ZAMORAK,
			CombatSpells.FIRE_WAVE
	};
}
