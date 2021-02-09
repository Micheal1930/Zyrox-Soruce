package com.zyrox.world.content.combat.strategy.impl;

import com.zyrox.model.container.impl.Equipment;
import com.zyrox.util.Misc;
import com.zyrox.util.RandomUtility;
import com.zyrox.world.content.combat.CombatContainer;
import com.zyrox.world.content.combat.CombatType;
import com.zyrox.world.content.combat.magic.CombatSpells;
import com.zyrox.world.content.combat.strategy.CombatStrategy;
import com.zyrox.world.content.minigames.impl.Dueling;
import com.zyrox.world.content.minigames.impl.Dueling.DuelRule;
import com.zyrox.world.content.raids.theatre_of_blood.verzik_vitur.VerzikViturPhase;
import com.zyrox.world.entity.Entity;
import com.zyrox.world.entity.impl.GameCharacter;
import com.zyrox.world.entity.impl.npc.NPC;
import com.zyrox.world.entity.impl.player.Player;

/**
 * The default combat strategy assigned to an {@link Entity} during a magic
 * based combat session.
 * 
 * @author lare96
 */
public class DefaultMagicCombatStrategy implements CombatStrategy {

	@Override
	public boolean canAttack(GameCharacter entity, GameCharacter victim) {

		// Npcs don't need to be checked.
		if (entity.isNpc()) {
			if(victim.isPlayer()) {
				Player p = (Player)victim;
				if(Nex.nexMinion(((NPC) entity).getId())) {
					if(!p.getMinigameAttributes().getGodwarsDungeonAttributes().hasEnteredRoom()) {
						return false;
					}
					return true;
				}
			}
			return true;
		}

		// Create the player instance.
		Player player = (Player) entity;

		if(Dueling.checkRule(player, DuelRule.NO_MAGIC)) {
			player.getPacketSender().sendMessage("Magic-attacks have been turned off in this duel!");
			player.getCombatBuilder().reset(true);
			return false;
		}


		// We can't attack without a spell.
		if(player.getCastSpell() == null)
			player.setCastSpell(player.getAutocastSpell());

		if(player.getRaids().getTheatreOfBlood().inFinalRoom() && player.getRaids().getTheatreOfBlood().verzikVitur != null) {
			if (player.getRaids().getTheatreOfBlood().verzikVitur.getPhase() == VerzikViturPhase.PHASE_1) {
				if (player.getEquipment().get(Equipment.WEAPON_SLOT).getId() == 52516) {
					player.setCastSpell(CombatSpells.DAWNBRINGER.getSpell());
				}
			}
		}

		if(player.getEquipment().get(Equipment.WEAPON_SLOT).getId() == 13058) {
			player.setCastSpell(CombatSpells.TRIDENT_OF_THE_SEAS.getSpell());
		}

		if(player.getEquipment().get(Equipment.WEAPON_SLOT).getId() == 52323) {
			player.setCastSpell(CombatSpells.SANGUINESTI_STAFF.getSpell());
		}

		if (player.getCastSpell() == null) {
			return false;
		}

		// Check the cast using the spell implementation.
		return player.getCastSpell().canCast(player, true);
	}

	@Override
	public CombatContainer attack(GameCharacter entity, GameCharacter victim) {

		CombatType combatType = CombatType.MAGIC;

		if (entity.isPlayer()) {
			Player player = (Player) entity;
			player.prepareSpell(player.getCastSpell(), victim);
			if(player.isAutocast() && player.getAutocastSpell() != null)
				player.setCastSpell(player.getAutocastSpell());
		
			player.setPreviousCastSpell(player.getCastSpell());

			if(player.getRaids().getTheatreOfBlood().inFinalRoom() && player.getRaids().getTheatreOfBlood().verzikVitur != null) {
				if(player.getRaids().getTheatreOfBlood().verzikVitur.getPhase() == VerzikViturPhase.PHASE_1) {
					if (player.getEquipment().get(Equipment.WEAPON_SLOT).getId() == 52516) {
						player.setCastSpell(CombatSpells.DAWNBRINGER.getSpell());
						combatType = CombatType.DAWNBRINGER;
					}
				}
			}

			if(player.getEquipment().get(Equipment.WEAPON_SLOT).getId() == 13058) {
				player.setCastSpell(CombatSpells.TRIDENT_OF_THE_SEAS.getSpell());
			}
			if(player.getEquipment().get(Equipment.WEAPON_SLOT).getId() == 52323) {
				player.setCastSpell(CombatSpells.SANGUINESTI_STAFF.getSpell());
			}
		} else if (entity.isNpc()) {
			NPC npc = (NPC) entity;

			switch (npc.getId()) {
			case 13:
			case 172:
			case 174:
				npc.prepareSpell(RandomUtility.randomElement(new CombatSpells[] { CombatSpells.WEAKEN, CombatSpells.FIRE_STRIKE, CombatSpells.EARTH_STRIKE, CombatSpells.WATER_STRIKE }).getSpell(), victim);
				break;
			case 2025:
			case 1643:
				npc.prepareSpell(RandomUtility.randomElement(new CombatSpells[] {CombatSpells.FIRE_WAVE, CombatSpells.EARTH_WAVE, CombatSpells.WATER_WAVE }).getSpell(), victim);
				break;
			case 3495:
				npc.prepareSpell(RandomUtility.randomElement(new CombatSpells[] {CombatSpells.SMOKE_BLITZ, CombatSpells.ICE_BLITZ, CombatSpells.ICE_BURST, CombatSpells.SHADOW_BARRAGE}).getSpell(), victim);
				break;
			case 3496:
				npc.prepareSpell(RandomUtility.randomElement(new CombatSpells[] {CombatSpells.BLOOD_BARRAGE, CombatSpells.BLOOD_BURST, CombatSpells.BLOOD_BLITZ, CombatSpells.BLOOD_RUSH}).getSpell(), victim);
				break;
			case 3491:
				npc.prepareSpell(RandomUtility.randomElement(new CombatSpells[] {CombatSpells.ICE_BARRAGE, CombatSpells.ICE_BLITZ, CombatSpells.ICE_BURST, CombatSpells.ICE_RUSH}).getSpell(), victim);
				break;
			case 13454:
				npc.prepareSpell(CombatSpells.ICE_BLITZ.getSpell(), victim);
				break;
			case 13453:
				npc.prepareSpell(CombatSpells.BLOOD_BURST.getSpell(), victim);
				break;
			case 13452:
				npc.prepareSpell(CombatSpells.SHADOW_BARRAGE.getSpell(), victim);
				break;
			case 13451:
				npc.prepareSpell(CombatSpells.SHADOW_BURST.getSpell(), victim);
				break;
			case 2896:
				npc.prepareSpell(CombatSpells.WATER_STRIKE.getSpell(), victim);
				break;
			case 2882:
				npc.prepareSpell(CombatSpells.DAGANNOTH_PRIME.getSpell(), victim);
				break;
			case 6254:
				npc.prepareSpell(CombatSpells.WIND_WAVE.getSpell(), victim);
				break;
			case 6257:
				npc.prepareSpell(CombatSpells.WATER_WAVE.getSpell(), victim);
				break;
			case 6278:
				npc.prepareSpell(CombatSpells.SHADOW_RUSH.getSpell(), victim);
				break;
			case 6221:
				npc.prepareSpell(CombatSpells.FIRE_BLAST.getSpell(), victim);
				break;
			}

			if (npc.getCurrentlyCasting() == null)
				npc.prepareSpell(CombatSpells.WIND_STRIKE.getSpell(), victim);
		}

		if (entity.getCurrentlyCasting().maximumHit() == -1) {
			CombatContainer combatContainer = new CombatContainer(entity, victim, CombatType.MAGIC, true);
			return combatContainer;
		}

		int hitAmount = 1;
		boolean checkAccuracy = false;

		if(entity.getCurrentlyCasting() != null) {
			if (entity.getCurrentlyCasting() == CombatSpells.SANGUINESTI_STAFF_X.getSpell()) {
				hitAmount = 2;
				checkAccuracy = Misc.random(2) != 1; //50% chance to not check accuracy
			}
		}

		return new CombatContainer(entity, victim, hitAmount, CombatType.MAGIC, checkAccuracy);
	}

	@Override
	public int attackDelay(GameCharacter entity) {
		return entity.getAttackSpeed();
	}

	@Override
	public int attackDistance(GameCharacter entity) {
		int distance = 8;
		if(entity.isNpc()) {
			switch(((NPC) entity).getId()) {
			case 2896:
			case 13451:
			case 13452:
			case 13453:
			case 13454:
				distance = 40;
				break;
			}
		}
		return distance;
	}

	@Override
	public boolean customContainerAttack(GameCharacter entity, GameCharacter victim) {
		return false;
	}

	@Override
	public CombatType getCombatType() {
		return CombatType.MAGIC;
	}
}
