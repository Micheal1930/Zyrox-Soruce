package com.varrock.world.content.combat.strategy.impl;

import com.varrock.engine.task.Task;
import com.varrock.engine.task.TaskManager;
import com.varrock.model.Animation;
import com.varrock.model.Graphic;
import com.varrock.model.GraphicHeight;
import com.varrock.model.Locations;
import com.varrock.model.Skill;
import com.varrock.model.Locations.Location;
import com.varrock.model.projectile.Projectile;
import com.varrock.util.Misc;
import com.varrock.world.content.combat.CombatContainer;
import com.varrock.world.content.combat.CombatHitTask;
import com.varrock.world.content.combat.CombatType;
import com.varrock.world.content.combat.strategy.CombatStrategy;
import com.varrock.world.entity.impl.GameCharacter;
import com.varrock.world.entity.impl.npc.NPC;
import com.varrock.world.entity.impl.player.Player;

public class Tsutsuroth implements CombatStrategy {

	private static final Animation anim1 = new Animation(6947);
	private static final Graphic graphic1 = new Graphic(1211, GraphicHeight.MIDDLE);
	private static final Graphic graphic2 = new Graphic(390);

	@Override
	public boolean canAttack(GameCharacter entity, GameCharacter victim) {
		return victim.isPlayer() && ((Player)victim).getMinigameAttributes().getGodwarsDungeonAttributes().hasEnteredRoom();
	}

	@Override
	public CombatContainer attack(GameCharacter entity, GameCharacter victim) {
		return null;
	}

	@Override
	public boolean customContainerAttack(GameCharacter entity, GameCharacter victim) {
		NPC tsutsuroth = (NPC)entity;
		if(victim.getConstitution() <= 0) {
			return true;
		}
		if(tsutsuroth.isChargingAttack()) {
			return true;
		}
		Player target = (Player)victim;
		CombatType style = Misc.getRandom(8) >= 6 && Locations.goodDistance(tsutsuroth.getPosition(), victim.getPosition(), 2) ? CombatType.MELEE : CombatType.MAGIC;
		if(style == CombatType.MELEE) {
			tsutsuroth.performAnimation(new Animation(6945));
			tsutsuroth.getCombatBuilder().setContainer(new CombatContainer(tsutsuroth, victim, 1, 1, CombatType.MELEE, true));
			int specialAttack = Misc.getRandom(4);
			if (specialAttack == 2) {
				int amountToDrain = Misc.getRandom(400);
				target.getPacketSender().sendMessage("K'ril Tsutsaroth slams through your defence and steals some Prayer points..");
				if(amountToDrain > target.getSkillManager().getCurrentLevel(Skill.PRAYER)) {
					amountToDrain = target.getSkillManager().getCurrentLevel(Skill.PRAYER);
				}
				target.getSkillManager().setCurrentLevel(Skill.PRAYER, target.getSkillManager().getCurrentLevel(Skill.PRAYER) - amountToDrain);
				if(target.getSkillManager().getCurrentLevel(Skill.PRAYER) <= 0) {
					target.getPacketSender().sendMessage("You have run out of Prayer points!");
				}
			}
		} else {
			tsutsuroth.performAnimation(anim1);
			tsutsuroth.setChargingAttack(true);
			TaskManager.submit(new Task(2, target, false) {
				int tick = 0;
				@Override
				public void execute() {
					switch(tick) {
					case 0:
						for (Player t : Misc.getCombinedPlayerList(target)) {
							if(t == null || t.getLocation() != Location.GODWARS_DUNGEON || t.isTeleporting())
								continue;
							if(t.getPosition().distanceToPoint(tsutsuroth.getPosition().getX(), tsutsuroth.getPosition().getY()) > 20)
								continue;
							new Projectile(tsutsuroth, target, graphic1.getId(), 44, 3, 43, 43, 0).sendProjectile();
						}
						break;
					case 2:
						for (Player t : Misc.getCombinedPlayerList(target)) {
							if(t == null || t.getLocation() != Location.GODWARS_DUNGEON)
								continue;
							target.performGraphic(graphic2);
							tsutsuroth.getCombatBuilder().setVictim(t);
							new CombatHitTask(tsutsuroth.getCombatBuilder(), new CombatContainer(tsutsuroth, t, 1, CombatType.MAGIC, true)).handleAttack();
						}
						tsutsuroth.setChargingAttack(false);
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
		return 4;
	}

	@Override
	public CombatType getCombatType() {
		return CombatType.MIXED;
	}
}
