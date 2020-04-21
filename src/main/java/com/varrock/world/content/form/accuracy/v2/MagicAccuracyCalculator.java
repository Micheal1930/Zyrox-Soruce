package com.varrock.world.content.form.accuracy.v2;

import java.util.concurrent.ThreadLocalRandom;

import com.varrock.model.Item;
import com.varrock.model.Prayerbook;
import com.varrock.model.Skill;
import com.varrock.model.container.impl.Equipment;
import com.varrock.world.content.BonusManager;
import com.varrock.world.content.combat.CombatType;
import com.varrock.world.content.combat.effect.EquipmentBonus;
import com.varrock.world.content.combat.prayer.CurseHandler;
import com.varrock.world.content.combat.prayer.PrayerHandler;
import com.varrock.world.content.combat.weapon.CombatSpecial;
import com.varrock.world.content.combat.weapon.FightStyle;
import com.varrock.world.content.form.accuracy.AccuracyCalculator;
import com.varrock.world.entity.Entity;
import com.varrock.world.entity.impl.GameCharacter;
import com.varrock.world.entity.impl.npc.NPC;
import com.varrock.world.entity.impl.player.Player;

/**
 * An implementation of {@link AccuracyCalculator} used to calculate
 * whether a {@link com.runelive.world.entity.impl.Character} will land their next hit
 * in combat while using {@link com.runelive.world.content.combat.CombatType#MAGIC}.
 *
 * @author Relex
 */
public final class MagicAccuracyCalculator implements AccuracyCalculator {

	@Override
	public double getAccuracy(GameCharacter source, GameCharacter victim) {
		
		/*
		 * Defining some constants
		 */
		Prayerbook victimPrayerBook = Prayerbook.NORMAL;
		if(victim.isPlayer()) {
			victimPrayerBook = ((Player)victim).getPrayerbook();
		}

		boolean[] victimPrayer = victim.getPrayerActive();
		boolean[] victimCurses = victim.getCurseActive();
		
		/*
		 * Prayer protection
		 */
		double prayerProtection = 1.0;
		
		if (source.isNpc()) {
			//if {@param victim} has magic protection prayer and our {@param source} is a
			//mob, block the hit completely
			if (victimPrayerBook == Prayerbook.NORMAL && victimPrayer[PrayerHandler.PROTECT_FROM_MAGIC]) {
				prayerProtection = 0.6;
			} else if (victimPrayerBook == Prayerbook.CURSES && victimCurses[CurseHandler.CurseData.DEFLECT_MAGIC.ordinal()]) {
				prayerProtection = 0.6;
			}
		} else if (source.isPlayer()) {
			//if {@param victim} has magic protection prayer and our {@param source}
			//is a {@link org.niobe.world.Player}, set our {@value prayerProtection} to 0.6
			//to reduce 40% of the damage
			if (victimPrayerBook == Prayerbook.NORMAL && victimPrayer[PrayerHandler.PROTECT_FROM_MAGIC]) {
				prayerProtection = 0.6;
			} else if (victimPrayerBook == Prayerbook.CURSES && victimCurses[CurseHandler.CurseData.DEFLECT_MAGIC.ordinal()]) {
				prayerProtection = 0.6;
			}
		}
				
		/*
		 * Now let's get our rolls for both parties and determine
		 * whether the {@param source} will land the hit.
		 */
		int attackerRoll = getAttackerRoll(source, victim);
		int defenderRoll = getDefenderRoll(source, victim);
		
		ThreadLocalRandom random = ThreadLocalRandom.current();
						
		if (attackerRoll >= defenderRoll) {
			int chanceToBlock = (int) Math.ceil(attackerRoll / (defenderRoll / 2));
			if (chanceToBlock > 0 && random.nextInt(chanceToBlock) == 0) {
				return random.nextDouble(0.5, 1.0) * prayerProtection;
			} else {	
				return 1 * prayerProtection;
			}
		} else {
			int chanceToHit = (defenderRoll / 32) - (attackerRoll / 24);
			if (chanceToHit <= 0) {
				chanceToHit = 1;
			}
			if (chanceToHit > 0 && random.nextInt(chanceToHit) == 0) {
				return random.nextDouble(0.5, 1.0) * prayerProtection;
			} else {
				return 0;
			}
		}
	}
	
	private int getAttackerRoll(GameCharacter attacker, GameCharacter victim) {
		
		/*
		 * Defining some constants
		 */
		Item[] equipment = attacker.isPlayer() ? ((Player)attacker).getEquipment().getItems() : null;
		
		Prayerbook prayerBook = Prayerbook.NORMAL;
		if(attacker.isPlayer()) {
			prayerBook = ((Player)attacker).getPrayerbook();
		}

		boolean[] activePrayer = attacker.getPrayerActive();
		boolean[] activeCurses = attacker.getCurseActive();

		Item weapon = attacker.isPlayer() ? ((Player)attacker).getEquipment().get(Equipment.WEAPON_SLOT) : null;

		boolean usingSpecial = false;

		if(attacker.isPlayer()) {
			if(weapon != null) {
				usingSpecial = ((Player)attacker).isSpecialActivated();
			}
		}
				
		/*
		 * Effective level
		 */
		int effectiveLevel = 0;

		if(attacker.isPlayer()) {
			effectiveLevel = ((Player)attacker).getSkillManager().getCurrentLevel(Skill.MAGIC);
		} else {
			effectiveLevel = (int) (((NPC)attacker).getDefinition().getAttackBonus() * 0.7);
		}
		
		if (prayerBook == Prayerbook.CURSES
				&& activeCurses[CurseHandler.CurseData.LEECH_MAGIC.ordinal()]) {
			//currently for every 25 seconds of our leech curse being active, it will add +1 to our effectiveLevel
//			int timerBoost = (int) ((System.currentTimeMillis() - attacker.getFields().getPrayerActiveTime()[Curse.LEECH_MAGIC.ordinal()]) / 25000);
//			effectiveLevel += 4 + (timerBoost > 5 ? 5 : timerBoost);
		}
		
		/*
		 * Prayer bonus
		 */
		double prayerBonus = 1.0;
		
		if (prayerBook == Prayerbook.NORMAL) {
			
			if (activePrayer[PrayerHandler.MYSTIC_WILL]) {
				prayerBonus += 0.05;
			} else if (activePrayer[PrayerHandler.MYSTIC_LORE]) {
				prayerBonus += 0.1;
			} else if (activePrayer[PrayerHandler.MYSTIC_MIGHT]) {
				prayerBonus += 0.15;
			} else if (activePrayer[PrayerHandler.AUGURY]) {
				prayerBonus += 0.2;
			}
			
		} else if (prayerBook == Prayerbook.CURSES) {
			if (activeCurses[CurseHandler.CurseData.LEECH_MAGIC.ordinal()]) {
				prayerBonus += 0.05;
			}
		}
		
		/*
		 * Combat style bonuses
		 */
		int effectiveMagic = (int) (effectiveLevel * prayerBonus) + 8;
		
		/*
		 * Void knight set bonuses
		 */
		if(attacker.isPlayer()) {
			if (EquipmentBonus.wearingEliteVoid((Player) attacker, CombatType.MAGIC)) {
				effectiveMagic *= 1.15;
			} else if (EquipmentBonus.wearingVoid((Player) attacker, CombatType.MAGIC)) {
				effectiveMagic *= 1.1;
			}
		}
		
		/*
		 * Equipment bonuses
		 */
		double equipmentBonus = 0;

		if(attacker.isPlayer()) {
			equipmentBonus = ((Player)attacker).getBonusManager().getAttackBonus()[BonusManager.ATTACK_MAGIC];
		}
		
		/*
		 * Maximum roll
		 */
		int roll = (int) (effectiveMagic * (1 + (equipmentBonus / 64)));

		if(attacker.isPlayer()) {
			if (usingSpecial) {
				CombatSpecial special = ((Player)attacker).getCombatSpecial();
				if (special != null) {
					roll *= special.getAccuracyBonus();
				}
			}
		}
		
		if (equipment != null) {
			
//			if (victim.isNpc() && equipment[Equipment.RING_SLOT].getId() >= 15398 && equipment[Equipment.RING_SLOT].getId() <= 15402
//					&& attacker.getPlayerFields().getSkillAttributes().getSlayerAssignment() != null) {
//				//ferocious ring bonus on slayer tasks
//				SlayerKey key = MobDefinition.forId(victim.getId()).getSlayerKey();
//				if (attacker.getPlayerFields().getSkillAttributes().getSlayerAssignment().getKey() == key) {
//					roll += 4;
//				}
//			} else if (victim.isMob() && equipment[Equipment.HEAD_SLOT].getId() == 15492
//					&& attacker.getPlayerFields().getSkillAttributes().getSlayerAssignment() != null) {
//				//full slayer helmet bonus on slayer tasks
//				SlayerKey key = MobDefinition.forId(victim.getId()).getSlayerKey();
//				if (attacker.getPlayerFields().getSkillAttributes().getSlayerAssignment().getKey() == key) {
//					roll += 4;
//				}
//			}
		}
		
		return roll;
	}
	
	private int getDefenderRoll(GameCharacter attacker, GameCharacter victim) {
		
		/*
		 * Victim constants
		 */		
		Prayerbook prayerBook = Prayerbook.NORMAL;
		if(victim.isPlayer()) {
			prayerBook = ((Player)victim).getPrayerbook();
		}

		boolean[] activePrayer = victim.getPrayerActive();
		boolean[] activeCurses = victim.getCurseActive();

		//FamiliarData familiar = victim.isPlayer() ? victim.getPlayerFields().getSkillAttributes().getFamiliar() : null;
		
		Item defenderWeapon = null;
		if(victim.isPlayer()) {
			defenderWeapon = ((Player)victim).getEquipment().get(Equipment.WEAPON_SLOT);
		}

		FightStyle attackStyle = FightStyle.CONTROLLED;
		if(defenderWeapon != null) {
			if(victim.isPlayer()) {
				attackStyle = ((Player)victim).getFightType().getStyle();
			}
		}

		/*
		 * Effective level
		 */
		int effectiveLevel = 0;

		if(victim.isPlayer()) {
			effectiveLevel = (int)
					((((Player)victim).getSkillManager().getCurrentLevel(Skill.DEFENCE) * 0.3)
							+ (((Player)victim).getSkillManager().getCurrentLevel(Skill.MAGIC) * 0.7));
		} else {
			effectiveLevel = (int)
					((((NPC)victim).getDefinition().getDefenceMage() * 0.3)
							+ (((NPC)victim).getDefinition().getAttackBonus() * 0.7));
		}
		
		if (prayerBook == Prayerbook.CURSES
				&& activeCurses[CurseHandler.CurseData.LEECH_DEFENCE.ordinal()]) {
			//currently for every 25 seconds of our leech curse being active, it will add +1 to our effectiveLevel
//			int timerBoost = (int) ((System.currentTimeMillis() - victim.getFields().getPrayerActiveTime()[Curse.LEECH_DEFENCE.ordinal()]) / 25000);
//			effectiveLevel += 4 + (timerBoost > 5 ? 5 : timerBoost);
		}
		/*
		 * Prayer bonus
		 */
		double prayerBonus = 1.0;
		
		if (prayerBook == Prayerbook.NORMAL) {
			
			if (activePrayer[PrayerHandler.THICK_SKIN]) {
				prayerBonus += 0.05;
			} else if (activePrayer[PrayerHandler.ROCK_SKIN]) {
				prayerBonus += 0.1;
			} else if (activePrayer[PrayerHandler.STEEL_SKIN]) {
				prayerBonus += 0.15;
			} else if (activePrayer[PrayerHandler.CHIVALRY]) {
				prayerBonus += 0.2;
			} else if (activePrayer[PrayerHandler.PIETY]) {
				prayerBonus += 0.25;
			} else if (activePrayer[PrayerHandler.RIGOUR]) {
				prayerBonus += 0.25;
			} else if (activePrayer[PrayerHandler.AUGURY]) {
				prayerBonus += 0.25;
			}
			
		} else if (prayerBook == Prayerbook.CURSES) {
			
			if (activePrayer[CurseHandler.CurseData.LEECH_DEFENCE.ordinal()]) {
				prayerBonus += 0.05;
			} else if (activePrayer[CurseHandler.CurseData.TURMOIL.ordinal()]) {
				prayerBonus += 0.15;
			}
		}
		
		/*
		 * Combat style bonuses
		 */
		int effectiveDefence = (int) (effectiveLevel * prayerBonus) + 8;
		
		if (attackStyle == FightStyle.DEFENSIVE) {
			effectiveDefence += 3;
		} else if (attackStyle == FightStyle.CONTROLLED) {
			effectiveDefence += 1;
		}
		
		/*
		 * Equipment bonuses
		 */
		double equipmentBonus = 0;

		if(victim.isPlayer()) {
			equipmentBonus = ((Player)victim).getBonusManager().getDefenceBonus()[BonusManager.DEFENCE_MAGIC];
		}
		
		/*
		 * Maximum roll
		 */
		int roll = (int) (effectiveDefence + (equipmentBonus * 2));

//		if (familiar != null) {
//			if (familiar.getType() == FamiliarType.WOLPERTINGER) {
//				roll *= 1.05;
//			}
//		}
		
		return roll;
	}
}