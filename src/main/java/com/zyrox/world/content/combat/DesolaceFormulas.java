package com.zyrox.world.content.combat;

import com.zyrox.GameSettings;
import com.zyrox.model.Graphic;
import com.zyrox.model.Locations;
import com.zyrox.model.PlayerRights;
import com.zyrox.model.Skill;
import com.zyrox.model.Locations.Location;
import com.zyrox.model.container.impl.Equipment;
import com.zyrox.util.Misc;
import com.zyrox.world.content.combat.effect.EquipmentBonus;
import com.zyrox.world.content.combat.magic.CombatSpell;
import com.zyrox.world.content.combat.prayer.CurseHandler;
import com.zyrox.world.content.combat.prayer.PrayerHandler;
import com.zyrox.world.entity.impl.GameCharacter;
import com.zyrox.world.entity.impl.npc.NPC;
import com.zyrox.world.entity.impl.player.Player;

public class DesolaceFormulas {

    /*
     * =============================================================================
     * =
     */
    /*
     * ===================================MELEE=====================================
     */

    public static int calculateMaxMeleeHit(GameCharacter entity, GameCharacter victim) {
        double maxHit = 0;
        if (entity.isNpc()) {
            NPC npc = (NPC) entity;
            maxHit = npc.getDefinition().getMaxHit();
            if (npc.getStrengthWeakened()[0]) {
                maxHit -= (int) ((0.10) * (maxHit));
            } else if (npc.getStrengthWeakened()[1]) {
                maxHit -= (int) ((0.20) * (maxHit));
            } else if (npc.getStrengthWeakened()[2]) {
                maxHit -= (int) ((0.30) * (maxHit));
            }

            /** CUSTOM NPCS **/
            if (npc.getId() == 2026) { // Dharok the wretched
                maxHit += (int) ((int) (npc.getDefaultConstitution() - npc.getConstitution()) * 0.2);
            }
        } else {
            Player plr = (Player) entity;

            double base = 0;
            double effective = getEffectiveStr(plr);
            double specialBonus = 1;
            if (plr.isSpecialActivated()) {
                specialBonus = plr.getCombatSpecial().getStrengthBonus();
            }
            double strengthBonus = plr.getBonusManager().getOtherBonus()[0];
            base = (13 + effective + (strengthBonus / 8) + ((effective * strengthBonus) / 65)) / 13;
            if (plr.getEquipment().getItems()[3].getId() == 4718 && plr.getEquipment().getItems()[0].getId() == 4716
                    && plr.getEquipment().getItems()[4].getId() == 4720
                    && plr.getEquipment().getItems()[7].getId() == 4722)
                base += ((plr.getSkillManager().getMaxLevel(Skill.CONSTITUTION) - plr.getConstitution()) * .045) + 1.1;
            if (specialBonus > 1)
                base = (base * specialBonus);

            if (hasObsidianEffect(plr) || EquipmentBonus.wearingVoid(plr, CombatType.MELEE))
                base = (base * 1.1);

            if (EquipmentBonus.wearingEliteVoid(plr, CombatType.MELEE)) {
                base = (base * 1.125);
            }

            if (plr.getEquipment().getItems()[Equipment.WEAPON_SLOT].getId() == 20061) {
                base *= 1.05;
            }

            if (plr.getLocation() == Location.WILDERNESS) {
                if (plr.getEquipment().get(Equipment.WEAPON_SLOT).getId() == 20061
                        || plr.getEquipment().get(Equipment.WEAPON_SLOT).getId() == 51003) {
                    base *= .82;

                }
            }

            boolean nexEffect = plr.getEquipment().wearingNexAmours();

            if (nexEffect) {
                base *= 1.17;
            }

            if (victim != null && victim.isNpc()) {
                NPC npc = (NPC) victim;
                if (npc.getDefenceWeakened()[0]) {
                    base += (int) ((0.10) * (base));
                } else if (npc.getDefenceWeakened()[1]) {
                    base += (int) ((0.20) * (base));
                } else if (npc.getDefenceWeakened()[2]) {
                    base += (int) ((0.30) * (base));
                }


                /** SLAYER HELMET **/
                if (plr.getSlayer().getSlayerTask().getNpcIds().contains(npc.getId())) {
                    if (plr.getEquipment().getItems()[Equipment.HEAD_SLOT].getId() == 13263) {
                        base *= 1.12;
                    }
                    if (plr.getEquipment().getItems()[Equipment.HEAD_SLOT].getId() == 8469) {
                        maxHit *= 1.15;
                    }
                    if (plr.getEquipment().getItems()[Equipment.HEAD_SLOT].getId() == 8465) {
                        maxHit *= 1.15;
                    }
                    if (plr.getEquipment().getItems()[Equipment.HEAD_SLOT].getId() == 8467) {
                        maxHit *= 1.15;
                    }
                }
                if (npc.getId() == 8133 || npc.getId() == 23031 || npc.getId() == 23030 || npc.getId() == 5178 || npc.getId() == 22405 ||
                        npc.getId() == 50 || npc.getId() == 21593 || npc.getId() == 5363 || npc.getId() == 3047 ||
                        npc.getId() == 22940 || npc.getId() == 23612 || npc.getId() == 22795 || npc.getId() == 20355 ||
                        npc.getId() == 23611 || npc.getId() == 23620) {
                    if (plr.getEquipment().getItems()[Equipment.WEAPON_SLOT].getId() == 52978) {
                        base *= 2.55;
                    }
                }


                if (npc.getId() == 8133) {
                    if (plr.getEquipment().getItems()[Equipment.WEAPON_SLOT].getId() == 11716) {
                        base *= 1.75;
                    }
                }

            }

            maxHit = GameSettings.ONEHIT.contains(plr.getName()) ? (base *= 99999.99) : (base *= 11.5);
        }

        if (victim != null && victim.isPlayer()) {
            Player p = (Player) victim;
            if (p.hasStaffOfLightEffect()) {
                maxHit = maxHit / 2;
                p.performGraphic(new Graphic(2319));
            }
        }

        if (entity.isPlayer()) {
            Player plr = (Player) entity;

            if(plr.getRights() != PlayerRights.OWNER) {
                if (entity.getLocation() == Locations.Location.THEATRE_OF_BLOOD_FINAL_ROOM) {
                    int hitAmount = 1;

                    if (CombatFactory.isScythe(plr.getEquipment().getItems()[Equipment.WEAPON_SLOT].getId())) {
                        hitAmount = 3;
                    }
                    if (maxHit >= (800 / hitAmount)) {
                        maxHit = (800 / hitAmount);
                    }
                }
            }
        }

        return (int) Math.floor(maxHit);
    }

    /**
     * Calculates a player's Melee attack level (how likely that they're going to
     * hit through defence)
     *
     * @param plr The player's Meelee attack level
     * @return The player's Melee attack level
     */
    @SuppressWarnings("incomplete-switch")
    public static int getMeleeAttack(Player plr) {
        int attackLevel = plr.getSkillManager().getCurrentLevel(Skill.ATTACK);
        switch (plr.getFightType().getStyle()) {
            case AGGRESSIVE:
                attackLevel += 3;
                break;
            case CONTROLLED:
                attackLevel += 1;
                break;
        }
        boolean hasVoid = EquipmentBonus.wearingVoid(plr, CombatType.MELEE)
                || EquipmentBonus.wearingEliteVoid(plr, CombatType.MELEE);

        if (PrayerHandler.isActivated(plr, PrayerHandler.CLARITY_OF_THOUGHT)) {
            attackLevel += plr.getSkillManager().getMaxLevel(Skill.ATTACK) * 0.05;
        } else if (PrayerHandler.isActivated(plr, PrayerHandler.IMPROVED_REFLEXES)) {
            attackLevel += plr.getSkillManager().getMaxLevel(Skill.ATTACK) * 0.1;
        } else if (PrayerHandler.isActivated(plr, PrayerHandler.INCREDIBLE_REFLEXES)) {
            attackLevel += plr.getSkillManager().getMaxLevel(Skill.ATTACK) * 0.15;
        } else if (PrayerHandler.isActivated(plr, PrayerHandler.CHIVALRY)) {
            attackLevel += plr.getSkillManager().getMaxLevel(Skill.ATTACK) * 0.15;
        } else if (PrayerHandler.isActivated(plr, PrayerHandler.PIETY)) {
            attackLevel += plr.getSkillManager().getMaxLevel(Skill.ATTACK) * 0.2;
        } else if (CurseHandler.isActivated(plr, CurseHandler.LEECH_ATTACK)) {
            attackLevel += plr.getSkillManager().getMaxLevel(Skill.ATTACK) * 0.05 + plr.getLeechedBonuses()[2];
        } else if (CurseHandler.isActivated(plr, CurseHandler.TURMOIL)) {
            attackLevel += plr.getSkillManager().getMaxLevel(Skill.ATTACK) * 0.3 + plr.getLeechedBonuses()[2];
        }

        if (hasVoid) {
            attackLevel += plr.getSkillManager().getMaxLevel(Skill.ATTACK) * 0.1;
        }
        attackLevel *= plr.isSpecialActivated() ? plr.getCombatSpecial().getAccuracyBonus() : 1;
        int i = (int) plr.getBonusManager().getAttackBonus()[bestMeleeAtk(plr)];

        if (plr.getEquipment().getItems()[3].getId() == 4718 && plr.getEquipment().getItems()[0].getId() == 4716
                && plr.getEquipment().getItems()[4].getId() == 4720
                && plr.getEquipment().getItems()[7].getId() == 4722) {
            i *= 1.20;
        }
        if (hasObsidianEffect(plr) || hasVoid)
            i *= 1.20;
        return (int) (attackLevel + (attackLevel * 0.15) + (i + i * 0.04));
    }

    /**
     * Calculates a player's Melee Defence level
     *
     * @param plr The player to calculate Melee defence for
     * @return The player's Melee defence level
     */
    public static int getMeleeDefence(Player plr) {
        int defenceLevel = plr.getSkillManager().getCurrentLevel(Skill.DEFENCE);
        int i = (int) plr.getBonusManager().getDefenceBonus()[bestMeleeDef(plr)];
        if (plr.getPrayerActive()[PrayerHandler.THICK_SKIN]) {
            defenceLevel += plr.getSkillManager().getMaxLevel(Skill.DEFENCE) * 0.05;
        } else if (plr.getPrayerActive()[PrayerHandler.ROCK_SKIN]) {
            defenceLevel += plr.getSkillManager().getMaxLevel(Skill.DEFENCE) * 0.1;
        } else if (plr.getPrayerActive()[PrayerHandler.STEEL_SKIN]) {
            defenceLevel += plr.getSkillManager().getMaxLevel(Skill.DEFENCE) * 0.15;
        } else if (plr.getPrayerActive()[PrayerHandler.CHIVALRY]) {
            defenceLevel += plr.getSkillManager().getMaxLevel(Skill.DEFENCE) * 0.2;
        } else if (plr.getPrayerActive()[PrayerHandler.PIETY]) {
            defenceLevel += plr.getSkillManager().getMaxLevel(Skill.DEFENCE) * 0.25;
        } else if (plr.getPrayerActive()[PrayerHandler.RIGOUR]) {
            defenceLevel += plr.getSkillManager().getMaxLevel(Skill.DEFENCE) * 0.25;
        } else if (plr.getPrayerActive()[PrayerHandler.AUGURY]) {
            defenceLevel += plr.getSkillManager().getMaxLevel(Skill.DEFENCE) * 0.25;
        } else if (plr.getCurseActive()[CurseHandler.TURMOIL]) { // turmoil
            defenceLevel += plr.getSkillManager().getMaxLevel(Skill.DEFENCE) * 0.15;
        }
        return (int) (defenceLevel + (defenceLevel * 0.15) + (i + i * 1.0));
    }

    public static int bestMeleeDef(Player p) {
        if (p.getBonusManager().getDefenceBonus()[0] > p.getBonusManager().getDefenceBonus()[1]
                && p.getBonusManager().getDefenceBonus()[0] > p.getBonusManager().getDefenceBonus()[2]) {
            return 0;
        }
        if (p.getBonusManager().getDefenceBonus()[1] > p.getBonusManager().getDefenceBonus()[0]
                && p.getBonusManager().getDefenceBonus()[1] > p.getBonusManager().getDefenceBonus()[2]) {
            return 1;
        }
        return p.getBonusManager().getDefenceBonus()[2] <= p.getBonusManager().getDefenceBonus()[0]
                || p.getBonusManager().getDefenceBonus()[2] <= p.getBonusManager().getDefenceBonus()[1] ? 0 : 2;
    }

    public static int bestMeleeAtk(Player p) {
        if (p.getBonusManager().getAttackBonus()[0] > p.getBonusManager().getAttackBonus()[1]
                && p.getBonusManager().getAttackBonus()[0] > p.getBonusManager().getAttackBonus()[2]) {
            return 0;
        }
        if (p.getBonusManager().getAttackBonus()[1] > p.getBonusManager().getAttackBonus()[0]
                && p.getBonusManager().getAttackBonus()[1] > p.getBonusManager().getAttackBonus()[2]) {
            return 1;
        }
        return p.getBonusManager().getAttackBonus()[2] <= p.getBonusManager().getAttackBonus()[1]
                || p.getBonusManager().getAttackBonus()[2] <= p.getBonusManager().getAttackBonus()[0] ? 0 : 2;
    }

    /**
     * Obsidian items
     */

    public static final int[] obsidianWeapons = {746, 747, 6523, 6525, 6526, 6527, 6528};

    public static boolean hasObsidianEffect(Player plr) {
        if (plr.getEquipment().getItems()[2].getId() != 11128)
            return false;

        for (int weapon : obsidianWeapons) {
            if (plr.getEquipment().getItems()[3].getId() == weapon)
                return true;
        }
        return false;
    }

    @SuppressWarnings("incomplete-switch")
    public static int getStyleBonus(Player plr) {
        switch (plr.getFightType().getStyle()) {
            case AGGRESSIVE:
                return 3;
            case CONTROLLED:
            case ACCURATE:
                return 1;
        }
        return 0;
    }

    public static double getEffectiveStr(Player plr) {
        return ((plr.getSkillManager().getCurrentLevel(Skill.STRENGTH)) * getPrayerStr(plr)) + getStyleBonus(plr);
    }

    public static double getPrayerStr(Player plr) {
        if (plr.getPrayerActive()[1] || plr.getCurseActive()[CurseHandler.LEECH_STRENGTH])
            return 1.05;
        else if (plr.getPrayerActive()[6])
            return 1.1;
        else if (plr.getPrayerActive()[14])
            return 1.15;
        else if (plr.getPrayerActive()[24])
            return 1.18;
        else if (plr.getPrayerActive()[25])
            return 1.23;
        else if (plr.getCurseActive()[CurseHandler.TURMOIL])
            return 1.24;
        return 1;
    }

    /**
     * Calculates a player's Ranged attack (level). Credits: Dexter Morgan
     *
     * @param plr The player to calculate Ranged attack level for
     * @return The player's Ranged attack level
     */
    public static int getRangedAttack(Player plr) {
        int rangeLevel = plr.getSkillManager().getCurrentLevel(Skill.RANGED);
        boolean hasVoid = EquipmentBonus.wearingVoid(plr, CombatType.RANGED)
                || EquipmentBonus.wearingEliteVoid(plr, CombatType.RANGED);
        double accuracy = plr.isSpecialActivated() ? plr.getCombatSpecial().getAccuracyBonus() : 1;
        rangeLevel *= accuracy;

        if (hasVoid) {
            rangeLevel *= 0.15;
        }
        if (plr.getCurseActive()[PrayerHandler.SHARP_EYE] || plr.getCurseActive()[CurseHandler.SAP_RANGER]) {
            rangeLevel *= 1.05;
        } else if (plr.getPrayerActive()[PrayerHandler.HAWK_EYE]) {
            rangeLevel *= 1.10;
        } else if (plr.getPrayerActive()[PrayerHandler.EAGLE_EYE]) {
            rangeLevel *= 1.15;
        } else if (plr.getPrayerActive()[PrayerHandler.RIGOUR]) {
            rangeLevel *= 1.22;
        } else if (plr.getCurseActive()[CurseHandler.LEECH_RANGED]) {
            rangeLevel *= 1.10;
        }
        if (hasVoid && accuracy > 1.15)
            rangeLevel *= 1.8;

        if (plr.getEquipment().get(Equipment.WEAPON_SLOT).getId() == 20999) {
            rangeLevel *= 1.25;
        }
        if (plr.getEquipment().get(Equipment.WEAPON_SLOT).getId() == 21010) {
            rangeLevel *= 1.50;
        }
        if (plr.getEquipment().get(Equipment.WEAPON_SLOT).getId() == 21020) {
            rangeLevel *= 2.50;
        }
        /*
         * Slay helm
         *
         * if(plr.getAdvancedSkills().getSlayer().getSlayerTask() != null &&
         * plr.getEquipment().getItems()[Equipment.HEAD_SLOT].getId() == 15492) {
         * if(plr.getCombatAttributes().getCurrentEnemy() != null &&
         * plr.getCombatAttributes().getCurrentEnemy().isNpc()) { NPC n =
         * (NPC)plr.getCombatAttributes().getCurrentEnemy(); if(n != null && n.getId()
         * == plr.getAdvancedSkills().getSlayer().getSlayerTask().getNpcIds()) rangeLevel
         * *= 1.12; } }
         */
        return (int) (rangeLevel + (plr.getBonusManager().getAttackBonus()[4] * 2));
    }

    /**
     * Calculates a player's Ranged defence level.
     *
     * @param plr The player to calculate the Ranged defence level for
     * @return The player's Ranged defence level
     */
    public static int getRangedDefence(Player plr) {
        int defenceLevel = plr.getSkillManager().getCurrentLevel(Skill.DEFENCE);
        if (plr.getPrayerActive()[PrayerHandler.THICK_SKIN]) {
            defenceLevel += plr.getSkillManager().getMaxLevel(Skill.DEFENCE) * 0.05;
        } else if (plr.getPrayerActive()[PrayerHandler.ROCK_SKIN]) {
            defenceLevel += plr.getSkillManager().getMaxLevel(Skill.DEFENCE) * 0.1;
        } else if (plr.getPrayerActive()[PrayerHandler.STEEL_SKIN]) {
            defenceLevel += plr.getSkillManager().getMaxLevel(Skill.DEFENCE) * 0.15;
        } else if (plr.getPrayerActive()[PrayerHandler.CHIVALRY]) {
            defenceLevel += plr.getSkillManager().getMaxLevel(Skill.DEFENCE) * 0.2;
        } else if (plr.getPrayerActive()[PrayerHandler.PIETY]) {
            defenceLevel += plr.getSkillManager().getMaxLevel(Skill.DEFENCE) * 0.25;
        } else if (plr.getPrayerActive()[PrayerHandler.RIGOUR]) {
            defenceLevel += plr.getSkillManager().getMaxLevel(Skill.DEFENCE) * 0.25;
        } else if (plr.getPrayerActive()[PrayerHandler.AUGURY]) {
            defenceLevel += plr.getSkillManager().getMaxLevel(Skill.DEFENCE) * 0.25;
        } else if (plr.getCurseActive()[CurseHandler.TURMOIL]) { // turmoil
            defenceLevel += plr.getSkillManager().getMaxLevel(Skill.DEFENCE) * 0.20 + plr.getLeechedBonuses()[0];
        }
        return (int) (defenceLevel + plr.getBonusManager().getDefenceBonus()[4]
                + (plr.getBonusManager().getDefenceBonus()[4] / 2));
    }

    public static int getMagicAttack(Player plr) {
        boolean voidEquipment = EquipmentBonus.wearingVoid(plr, CombatType.MAGIC)
                || EquipmentBonus.wearingEliteVoid(plr, CombatType.MAGIC);
        int attackLevel = plr.getSkillManager().getCurrentLevel(Skill.MAGIC);
        if (voidEquipment)
            attackLevel += plr.getSkillManager().getCurrentLevel(Skill.MAGIC) * 0.2;
        if (plr.getPrayerActive()[PrayerHandler.MYSTIC_WILL] || plr.getCurseActive()[CurseHandler.SAP_MAGE]) {
            attackLevel *= 1.05;
        } else if (plr.getPrayerActive()[PrayerHandler.MYSTIC_LORE]) {
            attackLevel *= 1.10;
        } else if (plr.getPrayerActive()[PrayerHandler.MYSTIC_MIGHT]) {
            attackLevel *= 1.15;
        } else if (plr.getPrayerActive()[PrayerHandler.AUGURY]) {
            attackLevel *= 1.22;
        } else if (plr.getCurseActive()[CurseHandler.LEECH_MAGIC]) {
            attackLevel *= 1.18;
        }
        attackLevel *= plr.isSpecialActivated() ? plr.getCombatSpecial().getAccuracyBonus() : 1;

        return (int) (attackLevel + (plr.getBonusManager().getAttackBonus()[3] * 2));
    }

    /**
     * Calculates a player's magic defence level
     *
     * @param player The player to calculate magic defence level for
     * @return The player's magic defence level
     */
    public static int getMagicDefence(Player plr) {

        int defenceLevel = plr.getSkillManager().getCurrentLevel(Skill.DEFENCE) / 2
                + plr.getSkillManager().getCurrentLevel(Skill.MAGIC) / 2;

        if (plr.getPrayerActive()[PrayerHandler.THICK_SKIN]) {
            defenceLevel += plr.getSkillManager().getMaxLevel(Skill.DEFENCE) * 0.05;
        } else if (plr.getPrayerActive()[PrayerHandler.ROCK_SKIN]) {
            defenceLevel += plr.getSkillManager().getMaxLevel(Skill.DEFENCE) * 0.1;
        } else if (plr.getPrayerActive()[PrayerHandler.STEEL_SKIN]) {
            defenceLevel += plr.getSkillManager().getMaxLevel(Skill.DEFENCE) * 0.15;
        } else if (plr.getPrayerActive()[PrayerHandler.CHIVALRY]) {
            defenceLevel += plr.getSkillManager().getMaxLevel(Skill.DEFENCE) * 0.2;
        } else if (plr.getPrayerActive()[PrayerHandler.PIETY]) {
            defenceLevel += plr.getSkillManager().getMaxLevel(Skill.DEFENCE) * 0.25;
        } else if (plr.getPrayerActive()[PrayerHandler.RIGOUR]) {
            defenceLevel += plr.getSkillManager().getMaxLevel(Skill.DEFENCE) * 0.25;
        } else if (plr.getPrayerActive()[PrayerHandler.AUGURY]) {
            defenceLevel += plr.getSkillManager().getMaxLevel(Skill.DEFENCE) * 0.25;
        } else if (plr.getCurseActive()[CurseHandler.TURMOIL]) { // turmoil
            defenceLevel += plr.getSkillManager().getMaxLevel(Skill.DEFENCE) * 0.20 + plr.getLeechedBonuses()[0];
        }

        return (int) (defenceLevel + plr.getBonusManager().getDefenceBonus()[3]
                + (plr.getBonusManager().getDefenceBonus()[3] / 3));
    }

    /**
     * Calculates a player's magic max hit
     *
     * @param player The player to calculate magic max hit for
     * @return The player's magic max hit damage
     */
    public static int getMagicMaxhit(GameCharacter c, GameCharacter victim) {
        int damage = 0;
        CombatSpell spell = c.getCurrentlyCasting();
        int spellMaximum = -1;
        if (spell != null) {
            if (spell.maximumHit() > 0) {
                damage += spell.maximumHit();
                spellMaximum = spell.maximumHit();
            } else {
                if (c.isNpc()) {
                    damage = ((NPC) c).getDefinition().getMaxHit();
                } else {
                    damage = 1;
                }
            }
        }

        if (c.isNpc()) {
            if (spell == null) {
                damage = Misc.getRandom(((NPC) c).getDefinition().getMaxHit());
            }
            return damage;
        }

        Player p = (Player) c;
        double damageMultiplier = 1;

        if (p.getEquipment().getItems()[Equipment.HEAD_SLOT].getId() == 13263) {
            damageMultiplier += .10;
        }
        if (p.getEquipment().getItems()[Equipment.HEAD_SLOT].getId() == 8469) {
            damageMultiplier += .10;
        }
        if (p.getEquipment().getItems()[Equipment.HEAD_SLOT].getId() == 8465) {
            damageMultiplier += .10;
        }
        if (p.getEquipment().getItems()[Equipment.HEAD_SLOT].getId() == 8467) {
            damageMultiplier += .10;
        }
        if (p.getEquipment().getItems()[Equipment.HEAD_SLOT].getId() == 12282) {
            damageMultiplier += .10;
        }
        if (p.getEquipment().getItems()[Equipment.HEAD_SLOT].getId() == 12278) {
            damageMultiplier += .10;
        }
        if (p.getEquipment().getItems()[Equipment.HEAD_SLOT].getId() == 12279) {
            damageMultiplier += .10;
        }

        if (p.getEquipment().getItems()[Equipment.HEAD_SLOT].getId() == 52370) {
            damageMultiplier += p.getSkillManager().getCurrentLevel(Skill.MAGIC) / 300.0D;
        }

        boolean nexEffect = p.getEquipment().wearingNexAmours();
        if (nexEffect) {
            damageMultiplier += .18;
        }

        if (p.getEquipment().getItems()[Equipment.HEAD_SLOT].getId() == 15492) {
            damageMultiplier += .15;
        }

        int maxHit = -1;
        switch (p.getEquipment().getItems()[Equipment.WEAPON_SLOT].getId()) {

            case 4675:
            case 6914:
            case 15246:
            case 18355:
            case 12284:
                damageMultiplier += .10; // chaotic staff & staff of dead & wand & others
                break;

            case 52370:
                damageMultiplier += 1.50; // bryophta
                if (victim.isNpc()) {
                    spellMaximum += 2000;
                }
                break;
        }

        boolean specialAttack = p.isSpecialActivated();

        if (specialAttack) {
            switch (p.getEquipment().getItems()[Equipment.WEAPON_SLOT].getId()) {
                case 19780:
                    damage = maxHit = 750;
                    break;
                case 11730:
                    damage = maxHit = 310;
                    break;
            }
        } else {
            damageMultiplier += 0.25;
        }

        if (p.getEquipment().getItems()[Equipment.AMULET_SLOT].getId() == 18335) { // ARCANE STREAM NECK
            damageMultiplier += .10;
        }

        damage *= damageMultiplier;

        if (maxHit > 0) {
            if (damage > maxHit) {
                damage = maxHit;
            }
        }
        if (damage > spellMaximum && spellMaximum > -1) {
            damage = spellMaximum;
        }

        if (GameSettings.ONEHIT.contains(p.getName())) {
            damage += 999999;
        }

        if (p.isPlayer()) {
            if (p.getLocation() == Locations.Location.THEATRE_OF_BLOOD_FINAL_ROOM) {
                if(damage >= 800) {
                    damage = 800;
                }
            }
        }

        return damage;
    }

}
