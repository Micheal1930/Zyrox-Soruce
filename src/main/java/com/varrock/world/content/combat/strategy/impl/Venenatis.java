package com.varrock.world.content.combat.strategy.impl;

import com.varrock.model.Animation;
import com.varrock.model.Graphic;
import com.varrock.model.Skill;
import com.varrock.util.Misc;
import com.varrock.world.content.combat.CombatContainer;
import com.varrock.world.content.combat.CombatHitTask;
import com.varrock.world.content.combat.CombatType;
import com.varrock.world.content.combat.strategy.CombatStrategy;
import com.varrock.world.entity.impl.GameCharacter;
import com.varrock.world.entity.impl.player.Player;

/** @author Lukafurlan **/

public class Venenatis implements CombatStrategy {

	public static int phase;
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
		Player player = (Player)victim;
		if(player.getConstitution() <= 0 || entity.getConstitution() <= 0) {
			return true;
		}

		if(phase == 0)
		{
			int rand = Misc.getRandom(10);
			int amountToDrain = Misc.getRandom(200);
			if(rand == 5) {
				new CombatHitTask(entity.getCombatBuilder(), new CombatContainer(entity, player, 1, CombatType.MAGIC, true)).handleAttack();
				entity.performAnimation(new Animation(5327));
				player.performGraphic(new Graphic(377));
				player.getPacketSender().sendMessage("@red@You were hit by powerfull magic attack!");
			} else if(rand == 2) {
				new CombatHitTask(entity.getCombatBuilder(), new CombatContainer(entity, player, 1, CombatType.MAGIC, true)).handleAttack();
				new CombatHitTask(entity.getCombatBuilder(), new CombatContainer(entity, player, 1, CombatType.MAGIC, true)).handleAttack();
				entity.performAnimation(new Animation(5327));
				player.performGraphic(new Graphic(377));
				player.getPacketSender().sendMessage("@red@Venenatis casts two powerfull spells at once!");
			} else if(rand == 4 || rand == 3){
				new CombatHitTask(entity.getCombatBuilder(), new CombatContainer(entity, player, 1, CombatType.MELEE, true)).handleAttack();
				player.getSkillManager().setCurrentLevel(Skill.PRAYER, player.getSkillManager().getCurrentLevel(Skill.PRAYER) - amountToDrain);
				player.getPacketSender().sendMessage("@blu@Venenatis has drained "+amountToDrain+" prayer points!");
				entity.performAnimation(new Animation(5327));
			} else {
				new CombatHitTask(entity.getCombatBuilder(), new CombatContainer(entity, player, 1, CombatType.MELEE, true)).handleAttack();
				entity.performAnimation(new Animation(5327));
			}
		}
		return true;
	}

	@Override
	public int attackDelay(GameCharacter entity) {
		return entity.getAttackSpeed();
	}

	@Override
	public int attackDistance(GameCharacter entity) {
		return 3;
	}


	

	@Override
	public CombatType getCombatType() {
		// TODO Auto-generated method stub
		return null;
	}

}