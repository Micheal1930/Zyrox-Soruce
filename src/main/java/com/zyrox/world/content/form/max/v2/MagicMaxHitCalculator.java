package com.zyrox.world.content.form.max.v2;

import com.zyrox.model.Item;
import com.zyrox.model.Skill;
import com.zyrox.model.definitions.NpcDefinition;
import com.zyrox.world.content.BonusManager;
import com.zyrox.world.content.combat.magic.CombatSpell;
import com.zyrox.world.content.form.max.MaxHitCalculator;
import com.zyrox.world.entity.impl.GameCharacter;
import com.zyrox.world.entity.impl.npc.NPC;
import com.zyrox.world.entity.impl.player.Player;

/**
 * An implementation of {@link MaxHitCalculator} used for our {@link com.runelive.world.content.combat.CombatType#MAGIC}
 * attack type.
 *
 * @author Relex
 */
public class MagicMaxHitCalculator implements MaxHitCalculator {

	@Override
	public int getMaxHit(GameCharacter source, GameCharacter victim) {

		if(source.isNpc()) {
			return ((NPC)source).getDefinition().getMaxHit();
		}
		
		/*
		 * Defining some constants.
		 */
		CombatSpell spell = source.getCurrentlyCasting();
		
		/*
		 * If the source is a mob and they have not been set
		 * to attack with a spell, simply return their pre-defined max hit
		 * from {@link org.niobe.model.definition.MobDefinition}
		 */
		if (spell == null && source.isNpc()) {
			return NpcDefinition.forId(((NPC)source).getId()).getMaxHit();
		}
		
		/*
		 * Base damage
		 * 
		 * WARNING: {@value spell} should not be used to access any members (example: spell#getMaxHit) from this point forward.
		 */
		int baseDamage = spell != null ? spell.maximumHit() : 0;
		
		/*
		 * Special weapon and/or spell damage
		 */
//		if (MagicCombatAction.isUsingTridentOfSeas(source)) {
//			int base = source.getEquipment().getItems()[Equipment.WEAPON_SLOT].getId() == 12899 ? 230 : 200;
//			int levels = source.getSkillManager().getMaxLevel(Skill.MAGIC) - 75;
//			baseDamage = (int) (base + Math.ceil((levels * 3) + (levels / 3)));
//		}
		
		/*
		 * Equipment bonus
		 */
		double equipmentBonus = 1.0;

		if(source.isPlayer()) {
			equipmentBonus += (((Player)source).getBonusManager().getOtherBonus()[BonusManager.MAGIC_DAMAGE] / 100);
		}
		
		baseDamage *= equipmentBonus;
		
		/*
		 * Boosted magic level
		 */
		int boostedLevels = 0;
		if(source.isPlayer()) {
			boostedLevels = ((Player)source).getSkillManager().getCurrentLevel(Skill.MAGIC) - ((Player)source).getSkillManager().getMaxLevel(Skill.MAGIC);
		}

		if (boostedLevels > 0) {
			double levelBase = 1.0 + (boostedLevels * .03);
			baseDamage *= levelBase;
		}

		/*if(source.isPlayer()) {
			Player player = ((Player)source);
			Item[] equipment = ((Player)source).getEquipment().getItems();
			if (victim.isNpc() && equipment[Equipment.RING_SLOT].getId() >= 15398 && equipment[Equipment.RING_SLOT].getId() <= 15402) {
				if(player.getSlayer().getSlayerMaster() != null) {
					if(player.getSlayer().getSlayerMaster() == SlayerMasters.KURADAL) {
						if(player.getLocation() == Locations.Location.KURADALS_DUNGEON) {
							baseDamage *= 1.1;
						}
					}
				}
			}
		}*/
		
		/*
		 * Passive equipment bonus
		 */
		if (source.isPlayer()) {
			Item[] equipment = ((Player)source).getEquipment().getItems();
			
			//if (source.getEquipment().getItems()[Equipment.AMULET_SLOT].getId() == 12002) {
				//occult necklace
				//baseDamage *= 1.1;
			//}
			
			if (victim.isNpc()) {
//				if (source.getPlayerFields().getSkillAttributes().getSlayerAssignment() != null
//						&& (equipment[Equipment.HEAD_SLOT].getId() == 15492) || equipment[Equipment.HEAD_SLOT].getId() == 15488) {
//					//full slayer helmet and hexcrest bonuses
//					SlayerKey key = MobDefinition.forId(victim.getId()).getSlayerKey();
//					if (source.getPlayerFields().getSkillAttributes().getSlayerAssignment().getKey() == key) {
//						baseDamage *= 1.166666666666667;
//					}
//				} else
				if (((NPC)victim).getId() == 9462) {
					//ice strykewyrm
//					if (equipment[Equipment.CAPE_SLOT].getId() == 6570) {
//						//fire cape bonus
//						baseDamage += 40;
//						//fire spells have an additional bonus
//						if (MagicConstants.isFireSpell(spell)) {
//							baseDamage *= 2;
//						}
//					} else if (MagicConstants.isFireSpell(spell)) {
//						//if source is not wearing fire cape, but is using fire spell
//						baseDamage *= 1.5;
//					}
				}
			}
		}
		
		return baseDamage;
	}
}
