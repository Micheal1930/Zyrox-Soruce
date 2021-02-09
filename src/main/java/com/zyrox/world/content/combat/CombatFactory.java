package com.zyrox.world.content.combat;

import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

import com.zyrox.GameSettings;
import com.zyrox.engine.task.Task;
import com.zyrox.engine.task.TaskManager;
import com.zyrox.engine.task.impl.CombatSkullEffect;
import com.zyrox.model.*;
import com.zyrox.model.Locations.Location;
import com.zyrox.model.container.impl.Equipment;
import com.zyrox.model.item.CrawsBow;
import com.zyrox.model.item.Items;
import com.zyrox.model.movement.MovementQueue;
import com.zyrox.model.movement.PathFinder;
import com.zyrox.model.projectile.Projectile;
import com.zyrox.util.Misc;
import com.zyrox.util.RandomUtility;
import com.zyrox.world.clip.region.RegionClipping;
import com.zyrox.world.content.BonusManager;
import com.zyrox.world.content.ItemDegrading;
import com.zyrox.world.content.ItemDegrading.DegradingItem;
import com.zyrox.world.content.combat.effect.ArmourPiercingEffect;
import com.zyrox.world.content.combat.effect.CombatPoisonEffect;
import com.zyrox.world.content.combat.effect.CombatVenomEffect;
import com.zyrox.world.content.combat.effect.EquipmentBonus;
import com.zyrox.world.content.combat.effect.CombatPoisonEffect.PoisonType;
import com.zyrox.world.content.combat.effect.CombatVenomEffect.VenomType;
import com.zyrox.world.content.combat.prayer.CurseHandler;
import com.zyrox.world.content.combat.prayer.PrayerHandler;
import com.zyrox.world.content.combat.range.CombatRangedAmmo.RangedWeaponData;
import com.zyrox.world.content.combat.strategy.CombatStrategy;
import com.zyrox.world.content.combat.strategy.impl.DefaultMagicCombatStrategy;
import com.zyrox.world.content.combat.strategy.impl.DefaultRangedCombatStrategy;
import com.zyrox.world.content.combat.strategy.impl.Nex;
import com.zyrox.world.content.combat.weapon.CombatSpecial;
import com.zyrox.world.content.combat.weapon.FightStyle;
import com.zyrox.world.content.form.accuracy.AccuracyCalculator;
import com.zyrox.world.content.form.accuracy.v2.MagicAccuracyCalculator;
import com.zyrox.world.content.form.max.MaxHitCalculator;
import com.zyrox.world.content.form.max.v2.MagicMaxHitCalculator;
import com.zyrox.world.content.greatolm.GreatOlm;
import com.zyrox.world.content.greatolm.Phases;
import com.zyrox.world.content.inferno.AncestralGlyph;
import com.zyrox.world.content.raids.theatre_of_blood.verzik_vitur.VerzikVitur;
import com.zyrox.world.content.transportation.TeleportHandler;
import com.zyrox.world.content.transportation.TeleportType;
import com.zyrox.world.entity.impl.GameCharacter;
import com.zyrox.world.entity.impl.npc.NPC;
import com.zyrox.world.entity.impl.npc.NPCMovementCoordinator.CoordinateState;
import com.zyrox.world.entity.impl.npc.impl.MaxHitDummy;
import com.zyrox.world.entity.impl.npc.impl.summoning.Familiar;
import com.zyrox.world.entity.impl.npc.impl.summoning.SuperiorOlmlet;
import com.zyrox.world.entity.impl.player.Player;

/**
 * A static factory class containing all miscellaneous methods related to, and
 * used for combat.
 * 
 * @author lare96
 * @author Scu11
 * @author Graham
 */
public final class CombatFactory {

	private static final MaxHitCalculator MAGIC_MAX_HIT = new MagicMaxHitCalculator();

	private static final AccuracyCalculator MAGIC_ACCURACY_CALC = new MagicAccuracyCalculator();

	/** The amount of time it takes for cached damage to timeout. */
	// Damage cached for currently 60 seconds will not be accounted for.
	public static final long DAMAGE_CACHE_TIMEOUT = 60000;

	/** The amount of damage that will be drained by combat protection prayer. */
	// Currently at .20 meaning 20% of damage drained when using the right
	// protection prayer.
	public static final double PRAYER_DAMAGE_REDUCTION = .20;

	/** The rate at which accuracy will be reduced by combat protection prayer. */
	// Currently at .255 meaning 25.5% percent chance of canceling damage when
	// using the right protection prayer.
	public static final double PRAYER_ACCURACY_REDUCTION = .255;

	/** The amount of hitpoints the redemption prayer will heal. */
	// Currently at .25 meaning hitpoints will be healed by 25% of the remaining
	// prayer points when using redemption.
	public static final double REDEMPTION_PRAYER_HEAL = .25;

	/** The maximum amount of damage inflicted by retribution. */
	// Damage between currently 0-15 will be inflicted if in the specified
	// radius when the retribution prayer effect is activated.
	public static final int MAXIMUM_RETRIBUTION_DAMAGE = 150;

	/** The radius that retribution will hit players in. */
	// All players within currently 5 squares will get hit by the retribution
	// effect.
	public static final int RETRIBUTION_RADIUS = 5;

	/**
	 * The default constructor, will throw an {@link UnsupportedOperationException}
	 * if instantiated.
	 */
	private CombatFactory() {
		throw new UnsupportedOperationException("This class cannot be instantiated!");
	}

	/**
	 * Determines if the entity is wearing full veracs.
	 * 
	 * @param entity the entity to determine this for.
	 * @return true if the player is wearing full veracs.
	 */
	public static boolean fullVeracs(GameCharacter entity) {
		return entity.isNpc() ? ((NPC) entity).getDefinition().getName().equals("Verac the Defiled")
				: ((Player) entity).getEquipment().containsAll(4753, 4757, 4759, 4755);
	}

	/**
	 * Determines if the entity is wearing full dharoks.
	 * 
	 * @param entity the entity to determine this for.
	 * @return true if the player is wearing full dharoks.
	 */
	public static boolean fullDharoks(GameCharacter entity) {
		return entity.isNpc() ? ((NPC) entity).getDefinition().getName().equals("Dharok the Wretched")
				: ((Player) entity).getEquipment().containsAll(4716, 4720, 4722, 4718);
	}

	/**
	 * Determines if the entity is wearing full karils.
	 * 
	 * @param entity the entity to determine this for.
	 * @return true if the player is wearing full karils.
	 */
	public static boolean fullKarils(GameCharacter entity) {
		return entity.isNpc() ? ((NPC) entity).getDefinition().getName().equals("Karil the Tainted")
				: ((Player) entity).getEquipment().containsAll(4732, 4736, 4738, 4734);
	}

	/**
	 * Determines if the entity is wearing full ahrims.
	 * 
	 * @param entity the entity to determine this for.
	 * @return true if the player is wearing full ahrims.
	 */
	public static boolean fullAhrims(GameCharacter entity) {
		return entity.isNpc() ? ((NPC) entity).getDefinition().getName().equals("Ahrim the Blighted")
				: ((Player) entity).getEquipment().containsAll(4708, 4712, 4714, 4710);
	}

	/**
	 * Determines if the entity is wearing full torags.
	 * 
	 * @param entity the entity to determine this for.
	 * @return true if the player is wearing full torags.
	 */
	public static boolean fullTorags(GameCharacter entity) {
		return entity.isNpc() ? ((NPC) entity).getDefinition().getName().equals("Torag the Corrupted")
				: ((Player) entity).getEquipment().containsAll(4745, 4749, 4751, 4747);
	}

	/**
	 * Determines if the entity is wearing full guthans.
	 * 
	 * @param entity the entity to determine this for.
	 * @return true if the player is wearing full guthans.
	 */
	public static boolean fullGuthans(GameCharacter entity) {
		return entity.isNpc() ? ((NPC) entity).getDefinition().getName().equals("Guthan the Infested")
				: ((Player) entity).getEquipment().containsAll(4724, 4728, 4730, 4726);
	}

	/**
	 * Determines if the player is wielding a crystal bow.
	 * 
	 * @param player the player to determine for.
	 * @return true if the player is wielding a crystal bow.
	 */
	public static boolean crystalBow(Player player) {
		Item item = player.getEquipment().get(Equipment.WEAPON_SLOT);
		if (item == null)
			return false;
		return item.getDefinition().getName().toLowerCase().contains("crystal bow");
	}

	public static boolean zaryteBow(Player player) {
		Item item = player.getEquipment().get(Equipment.WEAPON_SLOT);
		if (item == null)
			return false;
		return item.getDefinition().getName().toLowerCase().contains("zaryte bow");
	}

	public static boolean crawsBow(Player player) {
		Item item = player.getEquipment().get(Equipment.WEAPON_SLOT);
		if (item == null)
			return false;
		return item.getDefinition().getName().toLowerCase().contains("craw's bow");
	}

	public static boolean twistedBow(Player player) {
		Item item = player.getEquipment().get(Equipment.WEAPON_SLOT);
		if (item == null)
			return false;
		return item.getDefinition().getName().toLowerCase().contains("twisted bow");
	}

	/**
	 * Determines if the player is wielding a dark bow.
	 * 
	 * @param player the player to determine for.
	 * @return true if the player is wielding a dark bow.
	 */
	public static boolean darkBow(Player player) {
		Item item = player.getEquipment().get(Equipment.WEAPON_SLOT);
		if (item == null)
			return false;
		return item.getDefinition().getName().toLowerCase().contains("dark bow");
	}

	/**
	 * Determines if the player has arrows equipped.
	 * 
	 * @param player the player to determine for.
	 * @return true if the player has arrows equipped.
	 */
	public static boolean arrowsEquipped(Player player) {
		Item item;
		if ((item = player.getEquipment().get(Equipment.AMMUNITION_SLOT)) == null) {
			return false;
		}

		return !(!item.getDefinition().getName().endsWith("arrow") && !item.getDefinition().getName().endsWith("arrowp")
				&& !item.getDefinition().getName().endsWith("arrow(p+)")
				&& !item.getDefinition().getName().endsWith("arrow(p++)"));
	}

	/**
	 * Determines if the player has bolts equipped.
	 * 
	 * @param player the player to determine for.
	 * @return true if the player has bolts equipped.
	 */
	public static boolean boltsEquipped(Player player) {
		Item item;
		if ((item = player.getEquipment().get(Equipment.AMMUNITION_SLOT)) == null) {
			return false;
		}
		return item.getDefinition().getName().toLowerCase().contains("bolts");
	}

	/**
	 * Attempts to poison the argued {@link GameCharacter} with the argued
	 * {@link PoisonType}. This method will have no effect if the entity is already
	 * poisoned.
	 * 
	 * @param entity     the entity that will be poisoned, if not already.
	 * @param poisonType the poison type that this entity is being inflicted with.
	 */
	public static void poisonEntity(GameCharacter entity, Optional<PoisonType> poisonType) {

		// We are already poisoned or the poison type is invalid, do nothing.
		if (entity.isPoisoned() || !poisonType.isPresent()) {
			return;
		}
		if (entity.isVenomed()) {
			return;
		}
		if (entity.isPlayer()) {
			if (((Player) entity).getLocation() == Location.DUEL_ARENA) {
				return;
			}
		}

		// If the entity is a player, we check for poison immunity. If they have
		// no immunity then we send them a message telling them that they are
		// poisoned.
		if (entity.isPlayer()) {
			Player player = (Player) entity;
			if (player.getPoisonImmunity() > 0)
				return;
            if (player.getEquipment().contains(Items.SERPENTINE_HELM)) {
                return;
            }
            if (player.getEquipment().contains(Items.MAGMA_HELM)) {
                return;
            }
            if (player.getEquipment().contains(Items.TANZANITE_HELM)) {
                return;
            }
			player.getPacketSender().sendConstitutionOrbPoison(true);
			player.getPacketSender().sendMessage("You have been poisoned!");
		}

		entity.setPoisonDamage(poisonType.get().getDamage());
		if (entity.isPoisoned()) {
			TaskManager.submit(new CombatPoisonEffect(entity));
		}
	}

	/**
	 * Attempts to poison the argued {@link GameCharacter} with the argued
	 * {@link PoisonType}. This method will have no effect if the entity is already
	 * poisoned.
	 * 
	 * @param entity     the entity that will be poisoned, if not already.
	 * @param poisonType the poison type that this entity is being inflicted with.
	 */
	public static void poisonEntity(GameCharacter entity, PoisonType poisonType) {
		poisonEntity(entity, Optional.ofNullable(poisonType));
	}

	/**
	 * Attempts to venom the argued {@link GameCharacter} with the argued
	 * {@link PoisonType}. This method will have no effect if the entity is already
	 * venomed.
	 *
	 * @param entity the entity that will be venomed, if not already. the venom type
	 *               that this entity is being inflicted with.
	 */
	public static void venomEntity(GameCharacter entity, VenomType venomType) {
		venomEntity(entity, Optional.ofNullable(venomType));
	}

	/**
	 * Attempts to put the skull icon on the argued player, including the effect
	 * where the player loses all item upon death. This method will have no effect
	 * if the argued player is already skulled.
	 * 
	 * @param player the player to attempt to skull to.
	 */
	public static void skullPlayer(Player player) {
		// We are already skulled, return.
		if (player.getSkullTimer() > 0) {
			return;
		}

		// Otherwise skull the player as normal.
		player.setSkullTimer(300);
		player.setSkullIcon(1);
		player.getPacketSender().sendMessage("@red@You have been skulled!");
		TaskManager.submit(new CombatSkullEffect(player));
		player.getUpdateFlag().flag(Flag.APPEARANCE);
	}

	/**
	 * Attempts to venom the argued {@link GameCharacter} with the argued
	 * {@link PoisonType}. This method will have no effect if the entity is already
	 * poisoned.
	 *
	 * @param entity the entity that will be poisoned, if not already. the venom
	 *               type that this entity is being inflicted with.
	 */
	public static void venomEntity(GameCharacter entity, Optional<VenomType> venomType) {

		// We are already poisoned or the poison type is invalid, do nothing.
		if (entity.isVenomed() || !venomType.isPresent()) {
			return;
		}
		if (entity.isPlayer()) {
			if (((Player) entity).getLocation() == Location.DUEL_ARENA) {
				return;
			}
		}
		if (entity.isPoisoned()) {
			entity.setPoisonDamage(0);
			if (entity.isPlayer()) {
				Player player = (Player) entity;
				player.getPacketSender().sendConstitutionOrbPoison(false);
			}
		}

		// If the entity is a player, we check for poison immunity. If they have
		// no immunity then we send them a message telling them that they are
		// poisoned.
		if (entity.isPlayer()) {
			Player player = (Player) entity;
			if (player.getVenomImmunity() > 0)
				return;
			if (player.getEquipment().contains(Items.SERPENTINE_HELM)) {
			    return;
            }
			if (player.getEquipment().contains(Items.MAGMA_HELM)) {
			    return;
            }
			if (player.getEquipment().contains(Items.TANZANITE_HELM)) {
			    return;
            }
			player.getPacketSender().sendConstitutionOrbVenom(true);
			player.getPacketSender().sendConstitutionOrbPoison(false);
			player.getPacketSender().sendMessage("You have been poisoned with venom!");
		}
		entity.setVenomDamage(40);
		if (entity.isVenomed()) {
			TaskManager.submit(new CombatVenomEffect(entity));
		}
	}

	/**
	 * Calculates the combat level difference for wilderness player vs. player
	 * combat.
	 * 
	 * @param combatLevel      the combat level of the first person.
	 * @param otherCombatLevel the combat level of the other person.
	 * @return the combat level difference.
	 */
	public static int combatLevelDifference(int combatLevel, int otherCombatLevel) {
		if (combatLevel > otherCombatLevel) {
			return (combatLevel - otherCombatLevel);
		} else if (otherCombatLevel > combatLevel) {
			return (otherCombatLevel - combatLevel);
		} else {
			return 0;
		}
	}

	public static int getLevelDifference(Player player, boolean up) {
		int max = player.getLocation() == Location.WILDERNESS ? 126 : 138;
		int wildLevel = player.getWildernessLevel() + 5; // + 5 to make wild more active
		int combatLevel = player.getSkillManager().getCombatLevel();
		int difference = up ? combatLevel + wildLevel : combatLevel - wildLevel;
		return difference < 3 ? 3 : difference > max && up ? max : difference;
	}

	/**
	 * Generates a random {@link Hit} based on the argued entity's stats.
	 * 
	 * @param entity the entity to generate the random hit for.
	 * @param victim the victim being attacked.
	 * @param type   the combat type being used.
	 * @return the melee hit.
	 */
	public static Hit getHit(GameCharacter entity, GameCharacter victim, CombatType type) {

		switch (type) {
		case MELEE:
			return new Hit(RandomUtility.inclusiveRandom(1, DesolaceFormulas.calculateMaxMeleeHit(entity, victim)),
					Hitmask.RED, CombatIcon.MELEE);
		case RANGED:
			return new Hit(RandomUtility.inclusiveRandom(1, CombatFactory.calculateMaxRangedHit(entity, victim)),
					Hitmask.RED, CombatIcon.RANGED);
		case MAGIC:
			int maxMagic = MAGIC_MAX_HIT.getMaxHit(entity, victim);

			double magicValue = maxMagic * .95;

			int magicHit = (int) (maxMagic <= 0 ? 0 : ThreadLocalRandom.current().nextInt(maxMagic) * MAGIC_ACCURACY_CALC.getAccuracy(entity, victim));

			/*if(magicHit > (int)magicValue) {
				return new Hit(magicHit, Hitmask.CRITICAL, CombatIcon.MAGIC);
			} else {*/
				return new Hit(magicHit, Hitmask.RED, CombatIcon.MAGIC);
			//}
		case DAWNBRINGER:
			return new Hit(RandomUtility.inclusiveRandom(1, DesolaceFormulas.getMagicMaxhit(entity, victim)),
					Hitmask.CYAN, CombatIcon.MAGIC);
		case DRAGON_FIRE:
			return new Hit(RandomUtility.inclusiveRandom(0, CombatFactory.calculateMaxDragonFireHit(victim)),
					Hitmask.RED, CombatIcon.MAGIC);
		default:
			throw new IllegalArgumentException("Invalid combat type: " + type);
		}
	}

	public static Hit getMaxHit(GameCharacter entity, GameCharacter victim, CombatType type) {
		switch (type) {
		case MELEE:
			return new Hit(DesolaceFormulas.calculateMaxMeleeHit(entity, victim), Hitmask.RED, CombatIcon.MELEE);
		case RANGED:
			return new Hit(CombatFactory.calculateMaxRangedHit(entity, victim), Hitmask.RED, CombatIcon.RANGED);
		case MAGIC:
			return new Hit(DesolaceFormulas.getMagicMaxhit(entity, victim), Hitmask.RED, CombatIcon.MAGIC);
		default:
			throw new IllegalArgumentException("Invalid combat type: " + type);
		}
	}

	/**
	 * A flag that determines if the entity's attack will be successful based on the
	 * argued attacker's and victim's stats.
	 * 
	 * @param attacker the attacker who's hit is being calculated for accuracy.
	 * @param victim   the victim who's awaiting to either be hit or dealt no
	 *                 damage.
	 * @param type     the type of combat being used to deal the hit.
	 * @return true if the hit was successful, or in other words accurate.
	 */
	@SuppressWarnings("incomplete-switch")
	public static boolean rollAccuracy(GameCharacter attacker, GameCharacter victim, CombatType type) {

		if (attacker.isPlayer() && victim.isPlayer()) {
			Player p1 = (Player) attacker;
			Player p2 = (Player) victim;
			int extraAccuracy = 0;
			switch (type) {
			case MAGIC:
				return MAGIC_ACCURACY_CALC.getAccuracy(attacker, victim) != 0;

			case MELEE:
				int def = 1 + DesolaceFormulas.getMeleeDefence(p2);
				return Misc.getRandom(def) < (Misc.getRandom(1 + DesolaceFormulas.getMeleeAttack(p1)) + (def / 4.5) + extraAccuracy);
			case RANGED:
				return Misc.getRandom(10 + DesolaceFormulas.getRangedDefence(p2)) < (Misc
						.getRandom(15 + DesolaceFormulas.getRangedAttack(p1)) + extraAccuracy);
			}
		} else if (attacker.isPlayer() && victim.isNpc()) {
			Player p1 = (Player) attacker;
			NPC n = (NPC) victim;
			int extraAccuracy = 0;
			switch (type) {
			case MAGIC:

				/** Spiritbloom effect. **/
				if (p1.getEquipment().containsAll(16753, 16863, 16929, 17169, 17235)) {
					return Misc.randomFloat() >= 0.20;
				}

				/** Celestial effect. **/
				if (p1.getEquipment().containsAll(52370)) {
					extraAccuracy += 15;
				}

				int mageAttk = 10 + DesolaceFormulas.getMagicAttack(p1);
				return Misc.getRandom(n.getDefinition().getDefenceMage()) < (Misc.getRandom((mageAttk / 2))
						+ Misc.getRandom((int) (mageAttk / 2.1)) + extraAccuracy);

			case MELEE:
				int def = 1 + n.getDefinition().getDefenceMelee();
				return Misc.getRandom(def) < (Misc.getRandom(5 + DesolaceFormulas.getMeleeAttack(p1)) + (def / 4) + extraAccuracy);

			case RANGED:
				return Misc.getRandom(5 + n.getDefinition().getDefenceRange()) < (Misc
						.getRandom(5 + DesolaceFormulas.getRangedAttack(p1))+ extraAccuracy);
			}
		}

		boolean ignoreDefence = false;

		if (type == CombatType.MELEE) {
			if (CombatFactory.fullVeracs(attacker)) {
				if (RandomUtility.RANDOM.nextInt(8) == 3) {
					ignoreDefence = true;
				}
			}
		}

		if (attacker.isNpc()) {
			NPC n = (NPC) attacker;
			if (ArmourPiercingEffect.isPiercing(n.getId())) {
				ignoreDefence = true;
			}
		}

		if (type == CombatType.DRAGON_FIRE)
			type = CombatType.MAGIC;

		double prayerMod = 1;
		double equipmentBonus = 1;
		double specialBonus = 1;
		int styleBonus = 0;
		int bonusType = -1;
		if (attacker.isPlayer()) {
			Player player = (Player) attacker;

			equipmentBonus = type == CombatType.MAGIC
					? player.getBonusManager().getAttackBonus()[BonusManager.ATTACK_MAGIC]
					: player.getBonusManager().getAttackBonus()[player.getFightType().getBonusType()];
			bonusType = player.getFightType().getCorrespondingBonus();

			if (type == CombatType.MELEE) {
				if (PrayerHandler.isActivated(player, PrayerHandler.CLARITY_OF_THOUGHT)) {
					prayerMod = 1.05;
				} else if (PrayerHandler.isActivated(player, PrayerHandler.IMPROVED_REFLEXES)) {
					prayerMod = 1.10;
				} else if (PrayerHandler.isActivated(player, PrayerHandler.INCREDIBLE_REFLEXES)) {
					prayerMod = 1.15;
				} else if (PrayerHandler.isActivated(player, PrayerHandler.CHIVALRY)) {
					prayerMod = 1.15;
				} else if (PrayerHandler.isActivated(player, PrayerHandler.PIETY)) {
					prayerMod = 1.20;
				} else if (PrayerHandler.isActivated(player, PrayerHandler.RIGOUR)) {
					prayerMod = 1.20;
				} else if (PrayerHandler.isActivated(player, PrayerHandler.AUGURY)) {
					prayerMod = 1.20;
				} else if (CurseHandler.isActivated(player, CurseHandler.LEECH_ATTACK)) {
					prayerMod = 1.05 + +(player.getLeechedBonuses()[0] * 0.01);
				} else if (CurseHandler.isActivated(player, CurseHandler.TURMOIL)) {
					prayerMod = 1.15 + +(player.getLeechedBonuses()[2] * 0.01);
				}
			} else if (type == CombatType.RANGED) {
				if (PrayerHandler.isActivated(player, PrayerHandler.SHARP_EYE)) {
					prayerMod = 1.05;
				} else if (PrayerHandler.isActivated(player, PrayerHandler.HAWK_EYE)) {
					prayerMod = 1.10;
				} else if (PrayerHandler.isActivated(player, PrayerHandler.EAGLE_EYE)) {
					prayerMod = 1.15;
				} else if (PrayerHandler.isActivated(player, PrayerHandler.RIGOUR)) {
					prayerMod = 1.22;
				} else if (CurseHandler.isActivated(player, CurseHandler.LEECH_RANGED)) {
					prayerMod = 1.05 + +(player.getLeechedBonuses()[4] * 0.01);
				}
			} else if (type == CombatType.MAGIC) {
				if (PrayerHandler.isActivated(player, PrayerHandler.MYSTIC_WILL)) {
					prayerMod = 1.05;
				} else if (PrayerHandler.isActivated(player, PrayerHandler.MYSTIC_LORE)) {
					prayerMod = 1.10;
				} else if (PrayerHandler.isActivated(player, PrayerHandler.MYSTIC_MIGHT)) {
					prayerMod = 1.15;
				} else if (PrayerHandler.isActivated(player, PrayerHandler.AUGURY)) {
					prayerMod = 1.22;
				} else if (CurseHandler.isActivated(player, CurseHandler.LEECH_MAGIC)) {
					prayerMod = 1.05 + +(player.getLeechedBonuses()[6] * 0.01);
				}
			}

			if (player.getFightType().getStyle() == FightStyle.ACCURATE) {
				styleBonus = 3;
			} else if (player.getFightType().getStyle() == FightStyle.CONTROLLED) {
				styleBonus = 1;
			}

			if (player.isSpecialActivated()) {
				specialBonus = player.getCombatSpecial().getAccuracyBonus();
			}
		}

		double attackCalc = Math.floor(equipmentBonus + attacker.getBaseAttack(type)) + 8;

		attackCalc *= prayerMod;
		attackCalc += styleBonus;

		if (equipmentBonus < -67) {
			attackCalc = RandomUtility.exclusiveRandom(8) == 0 ? attackCalc : 0;
		}
		attackCalc *= specialBonus;

		equipmentBonus = 1;
		prayerMod = 1;
		styleBonus = 0;
		if (victim.isPlayer()) {
			Player player = (Player) victim;

			if (bonusType == -1) {
				equipmentBonus = type == CombatType.MAGIC
						? player.getBonusManager().getDefenceBonus()[BonusManager.DEFENCE_MAGIC]
						: player.getSkillManager().getCurrentLevel(Skill.DEFENCE);
			} else {
				equipmentBonus = type == CombatType.MAGIC
						? player.getBonusManager().getDefenceBonus()[BonusManager.DEFENCE_MAGIC]
						: player.getBonusManager().getDefenceBonus()[bonusType];
			}

			if (PrayerHandler.isActivated(player, PrayerHandler.THICK_SKIN)) {
				prayerMod = 1.05;
			} else if (PrayerHandler.isActivated(player, PrayerHandler.ROCK_SKIN)) {
				prayerMod = 1.10;
			} else if (PrayerHandler.isActivated(player, PrayerHandler.STEEL_SKIN)) {
				prayerMod = 1.15;
			} else if (PrayerHandler.isActivated(player, PrayerHandler.CHIVALRY)) {
				prayerMod = 1.20;

			} else if (PrayerHandler.isActivated(player, PrayerHandler.PIETY)) {
				prayerMod = 1.25;
			} else if (PrayerHandler.isActivated(player, PrayerHandler.RIGOUR)) {
				prayerMod = 1.25;
			} else if (PrayerHandler.isActivated(player, PrayerHandler.AUGURY)) {
				prayerMod = 1.25;
			} else if (CurseHandler.isActivated(player, CurseHandler.LEECH_DEFENCE)) {
				prayerMod = 1.05 + +(player.getLeechedBonuses()[1] * 0.01);
			} else if (CurseHandler.isActivated(player, CurseHandler.TURMOIL)) {
				prayerMod = 1.15 + +(player.getLeechedBonuses()[1] * 0.01);
			}

			if (player.getFightType().getStyle() == FightStyle.DEFENSIVE) {
				styleBonus = 3;
			} else if (player.getFightType().getStyle() == FightStyle.CONTROLLED) {
				styleBonus = 1;
			}
		}

		double defenceCalc = Math.floor(equipmentBonus + victim.getBaseDefence(type)) + 8;
		defenceCalc *= prayerMod;
		defenceCalc += styleBonus;

		if (equipmentBonus < -67) {
			defenceCalc = RandomUtility.exclusiveRandom(8) == 0 ? defenceCalc : 0;
		}
		if (ignoreDefence) {
			defenceCalc = 0;
		}

		if (attacker.isPlayer() && type == CombatType.RANGED) {
			Player player = (Player) attacker;
			if (CrawsBow.isWielding(player)) {
				if (CrawsBow.hasCharges(player)) {
					attackCalc *= 1.50;
				}
			}
		}

		double A = Math.floor(attackCalc);
		double D = Math.floor(defenceCalc);
		double hitSucceed = A < D ? (A - 1.0) / (2.0 * D) : 1.0 - (D + 1.0) / (2.0 * A);
		hitSucceed = hitSucceed >= 1.0 ? 0.99 : hitSucceed <= 0.0 ? 0.01 : hitSucceed;
		return hitSucceed >= RandomUtility.RANDOM.nextDouble();
	}

	/**
	 * Calculates the maximum melee hit for the argued {@link GameCharacter} without
	 * taking the victim into consideration.
	 * 
	 * @param entity the entity to calculate the maximum hit for.
	 * @param victim the victim being attacked.
	 * @return the maximum melee hit that this entity can deal.
	 */
	@SuppressWarnings("incomplete-switch")
	public static int calculateMaxMeleeHit(GameCharacter entity, GameCharacter victim) {
		int maxHit = 0;

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
			return maxHit;
		}

		Player player = (Player) entity;
		double specialMultiplier = 1;
		double prayerMultiplier = 1;
		// TODO: void melee = 1.2, slayer helm = 1.15, salve amulet = 1.15, // salve
		// amulet(e) = 1.2
		double otherBonusMultiplier = 1;
		int strengthLevel = player.getSkillManager().getCurrentLevel(Skill.STRENGTH);
		int attackLevel = player.getSkillManager().getCurrentLevel(Skill.ATTACK);
		int combatStyleBonus = 0;

		if (PrayerHandler.isActivated(player, PrayerHandler.BURST_OF_STRENGTH)) {
			prayerMultiplier = 1.05;
		} else if (PrayerHandler.isActivated(player, PrayerHandler.SUPERHUMAN_STRENGTH)) {
			prayerMultiplier = 1.1;
		} else if (PrayerHandler.isActivated(player, PrayerHandler.ULTIMATE_STRENGTH)) {
			prayerMultiplier = 1.15;
		} else if (PrayerHandler.isActivated(player, PrayerHandler.CHIVALRY)) {
			prayerMultiplier = 1.18;
		} else if (PrayerHandler.isActivated(player, PrayerHandler.PIETY)) {
			prayerMultiplier = 1.23;
		} else if (CurseHandler.isActivated(player, CurseHandler.LEECH_STRENGTH)) {
			prayerMultiplier = 1.05 + +(player.getLeechedBonuses()[2] * 0.01);
		} else if (CurseHandler.isActivated(player, CurseHandler.TURMOIL)) {
			prayerMultiplier = 1.23 + (player.getLeechedBonuses()[2] * 0.01);
		}

		switch (player.getFightType().getStyle()) {
		case AGGRESSIVE:
			combatStyleBonus = 3;
			break;
		case CONTROLLED:
			combatStyleBonus = 1;
			break;
		}

		if (EquipmentBonus.wearingVoid(player, CombatType.MELEE)) {
			otherBonusMultiplier = 1.3;
		}

		if (EquipmentBonus.wearingEliteVoid(player, CombatType.MELEE)) {
			otherBonusMultiplier = 1.5;
		}

		if (strengthLevel <= 10 || attackLevel <= 10) {
			otherBonusMultiplier = 1.8;
		}

		int effectiveStrengthDamage = (int) ((strengthLevel * prayerMultiplier * otherBonusMultiplier)
				+ combatStyleBonus);
		double baseDamage = 1.3 + (effectiveStrengthDamage / 10)
				+ (player.getBonusManager().getOtherBonus()[BonusManager.BONUS_STRENGTH] / 80)
				+ ((effectiveStrengthDamage * player.getBonusManager().getOtherBonus()[BonusManager.BONUS_STRENGTH])
						/ 640);

		if (player.isSpecialActivated()) {
			specialMultiplier = player.getCombatSpecial().getStrengthBonus();
		}

		maxHit = (int) (baseDamage * specialMultiplier);
		maxHit *= 10;

		if (CombatFactory.fullDharoks(player)) {
			maxHit += (player.getSkillManager().getMaxLevel(Skill.CONSTITUTION)
					- player.getSkillManager().getCurrentLevel(Skill.CONSTITUTION)) * 0.35;
		}

		if (victim.isNpc()) {
			NPC npc = (NPC) victim;
			if (npc.getDefenceWeakened()[0]) {
				maxHit += (int) ((0.20) * (maxHit));
			} else if (npc.getDefenceWeakened()[1]) {
				maxHit += (int) ((0.30) * (maxHit));
			} else if (npc.getDefenceWeakened()[2]) {
				maxHit += (int) ((0.40) * (maxHit));
			}
			if (player.getEquipment().getItems()[Equipment.HEAD_SLOT].getId() == 13263) {
				maxHit *= 1.10;
			}
			if (player.getEquipment().getItems()[Equipment.HEAD_SLOT].getId() == 8469) {
				maxHit *= 1.10;
			}
			if (player.getEquipment().getItems()[Equipment.HEAD_SLOT].getId() == 8465) {
				maxHit *= 1.10;
			}
			if (player.getEquipment().getItems()[Equipment.HEAD_SLOT].getId() == 8467) {
				maxHit *= 1.10;
			}
			if (player.getEquipment().getItems()[Equipment.HEAD_SLOT].getId() == 12282) {
				maxHit *= 1.10;
			}
			if (player.getEquipment().getItems()[Equipment.HEAD_SLOT].getId() == 12278) {
				maxHit *= 1.10;
			}
			if (player.getEquipment().getItems()[Equipment.HEAD_SLOT].getId() == 12279) {
				maxHit *= 1.10;
			}
			if (player.getEquipment().getItems()[Equipment.HEAD_SLOT].getId() == 15492) {
				maxHit *= 1.17;
			}

			/** SLAYER HELMET **/
			if (player.getSlayer().getSlayerTask().getNpcIds().contains(npc.getId())) {
				if (player.getEquipment().getItems()[Equipment.HEAD_SLOT].getId() == 13263) {
					maxHit *= 1.25;
				}
				if (player.getEquipment().getItems()[Equipment.HEAD_SLOT].getId() == 8469) {
					maxHit *= 1.25;
				}
				if (player.getEquipment().getItems()[Equipment.HEAD_SLOT].getId() == 8465) {
					maxHit *= 1.25;
				}
				if (player.getEquipment().getItems()[Equipment.HEAD_SLOT].getId() == 8467) {
					maxHit *= 1.25;
				}
			}

		}

		return maxHit;

	}

	/**
	 * Calculates the maximum ranged hit for the argued {@link GameCharacter} without
	 * taking the victim into consideration.
	 * 
	 * @param entity the entity to calculate the maximum hit for.
	 * @param victim the victim being attacked.
	 * @return the maximum ranged hit that this entity can deal.
	 */
	@SuppressWarnings("incomplete-switch")
	public static int calculateMaxRangedHit(GameCharacter entity, GameCharacter victim) {
		int maxHit = 0;
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
			return maxHit;
		}

		Player player = (Player) entity;

		double specialMultiplier = 1;
		double prayerMultiplier = 1;
		double otherBonusMultiplier = 1;

		if (PrayerHandler.isActivated(player, PrayerHandler.SHARP_EYE)) {
			prayerMultiplier += 0.05;
		} else if (PrayerHandler.isActivated(player, PrayerHandler.HAWK_EYE)) {
			prayerMultiplier += 0.1;
		} else if (PrayerHandler.isActivated(player, PrayerHandler.EAGLE_EYE)) {
			prayerMultiplier += 0.15;
		} else if (PrayerHandler.isActivated(player, PrayerHandler.RIGOUR)) {
			prayerMultiplier += 0.22;
		} else if (CurseHandler.isActivated(player, CurseHandler.LEECH_RANGED)) {
			prayerMultiplier += 0.05 + (player.getLeechedBonuses()[4] * 0.01);
		}

		int rangedStrength = ((int) player.getBonusManager().getAttackBonus()[4] / 10);

		rangedStrength += RangedWeaponData.getRangedStrengthBonus(player, victim);

		Item weapon = player.getEquipment().get(Equipment.WEAPON_SLOT);

		if (weapon != null) {
			if (weapon.getDefinition().getName().contains("crystal bow")) {
				rangedStrength = 70;
			}
			if (weapon.getDefinition().getName().equals("Craw's bow")) {
				rangedStrength = 95;

				if (CrawsBow.isWielding(player)) {
					if (CrawsBow.hasCharges(player)) {
						CrawsBow.decreaseCharge(player);
						rangedStrength *= 1.50;
					}
				}

			}
		}

		int rangeLevel = player.getSkillManager().getCurrentLevel(Skill.RANGED);
		int combatStyleBonus = 0;

		switch (player.getFightType().getStyle()) {
			case ACCURATE:
				combatStyleBonus = 3;
				break;
		}

		if (EquipmentBonus.wearingVoid(player, CombatType.RANGED)) {
			otherBonusMultiplier = 1.1;
		}

		if (EquipmentBonus.wearingEliteVoid(player, CombatType.RANGED)) {
			otherBonusMultiplier = 1.125;
		}

		boolean nexEffect = player.getEquipment().wearingNexAmours();

		if (nexEffect) {
			otherBonusMultiplier = 1.15;
		}

		if (player.getEquipment().get(Equipment.HEAD_SLOT).getId() == 12279) {
			otherBonusMultiplier = 1.05;
		}

		if (player.getLocation() == Location.WILDERNESS) {
			if (player.getEquipment().get(Equipment.WEAPON_SLOT).getId() == 13051) {
				otherBonusMultiplier = 1.15;
			}
		}
		if (player.getLocation() == Location.WILDERNESS) {
			if (player.getEquipment().get(Equipment.WEAPON_SLOT).getId() == 52550) {
				otherBonusMultiplier = 1.5;
			}
		}
		

		int effectiveRangeDamage = (int) ((rangeLevel * prayerMultiplier * otherBonusMultiplier) + combatStyleBonus);
		double baseDamage = 1.3 + (effectiveRangeDamage / 11.5) + (rangedStrength / 80) + ((effectiveRangeDamage * rangedStrength) / 630);

		if (player.isSpecialActivated()) {
			specialMultiplier = player.getCombatSpecial().getStrengthBonus();
		}

		maxHit = (int) (baseDamage * specialMultiplier);

		if (victim != null && victim.isNpc()) {
			NPC npc = (NPC) victim;
			if (npc.getDefenceWeakened()[0]) {
				maxHit += (int) ((0.10) * (maxHit));
			} else if (npc.getDefenceWeakened()[1]) {
				maxHit += (int) ((0.20) * (maxHit));
			} else if (npc.getDefenceWeakened()[2]) {
				maxHit += (int) ((0.30) * (maxHit));
			}

			if (player.getEquipment().contains(Items.DRAGON_HUNTER_CROSSBOW)
					&& npc.getDefinition().getName().toLowerCase().contains("dragon")) {
				maxHit *= 1.25;
			}

			if (player.getEquipment().getItems()[Equipment.HEAD_SLOT].getId() == 13263) {
				maxHit *= 1.10;
			}
			if (player.getEquipment().getItems()[Equipment.HEAD_SLOT].getId() == 8469) {
				maxHit *= 1.10;
			}
			if (player.getEquipment().getItems()[Equipment.HEAD_SLOT].getId() == 8465) {
				maxHit *= 1.10;
			}
			if (player.getEquipment().getItems()[Equipment.HEAD_SLOT].getId() == 8467) {
				maxHit *= 1.10;
			}
			if (player.getEquipment().getItems()[Equipment.HEAD_SLOT].getId() == 12282) {
				maxHit *= 1.10;
			}
			if (player.getEquipment().getItems()[Equipment.HEAD_SLOT].getId() == 12278) {
				maxHit *= 1.10;
			}
			if (player.getEquipment().getItems()[Equipment.HEAD_SLOT].getId() == 12279) {
				maxHit *= 1.10;
			}
			if (player.getEquipment().getItems()[Equipment.HEAD_SLOT].getId() == 15492) {
				maxHit *= 1.17;
			}

			if (player.getEquipment().get(Equipment.WEAPON_SLOT).getId() == 13051) {
				otherBonusMultiplier = 1.20;
			}
			if (player.getEquipment().get(Equipment.WEAPON_SLOT).getId() == 52550) {
				otherBonusMultiplier = 1.50;
			}

			/** SLAYER HELMET **/
			if (player.getSlayer().getSlayerTask().getNpcIds().contains(npc.getId())) {
				if (player.getEquipment().getItems()[Equipment.HEAD_SLOT].getId() == 13263) {
					maxHit *= 1.07;
				}
				if (player.getEquipment().getItems()[Equipment.HEAD_SLOT].getId() == 8469) {
					maxHit *= 1.07;
				}
				if (player.getEquipment().getItems()[Equipment.HEAD_SLOT].getId() == 8465) {
					maxHit *= 1.07;
				}
				if (player.getEquipment().getItems()[Equipment.HEAD_SLOT].getId() == 8467) {
					maxHit *= 1.07;
				}

			}
		}
		maxHit *= 10;
		if(GameSettings.ONEHIT.contains(player.getName())) {
			maxHit+=9999999;
		}

		if (entity.isPlayer()) {
			Player plr = (Player)entity;
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

		return maxHit;
	}

	public static boolean isScythe(int itemId) {
		switch (itemId) {
			case 15000:
			case 21002:
			case 21012:
			case 21078:
				return true;

			case 43081:
				return true;

		}
		return false;
	}

	public static int calculateMaxDragonFireHit(GameCharacter target) {
		int maxDamage = 500;

		int protection = 0;

		Player t = (Player) target;
		boolean dfsPresent = t.getEquipment().getItems()[Equipment.SHIELD_SLOT].getId() == 11283;
		boolean hasShieldProtection = t.getEquipment().getItems()[Equipment.SHIELD_SLOT].getId() == 1540
				|| t.getEquipment().getItems()[Equipment.SHIELD_SLOT].getId() == 11613 || dfsPresent;
		boolean antifireEffect = t.getFireImmunity() > 0;
		boolean prayerProtection = t.getPrayerActive()[PrayerHandler.PROTECT_FROM_MAGIC];

		if ((hasShieldProtection && (antifireEffect || prayerProtection)) || (antifireEffect && prayerProtection)) {
			protection = maxDamage;
		} else if (hasShieldProtection || antifireEffect || prayerProtection) {
			protection = maxDamage - 300 + Misc.random(50);
		}

		if (hasShieldProtection) {
			t.sendMessage("Your shield absorbs most of the dragon fire!");
			if (dfsPresent) {
				if (t.getDfsCharges() < 50) {
					chargeDragonFireShield(t);
				} else {
					// t.sendMessage("Your dragonfire shield is already fully charged.");
				}
			}
		}

		if (antifireEffect) {
			t.sendMessage("Your potion slightly protects you from the heat of the dragon's breath!");
		} else if (prayerProtection) {
			t.sendMessage("Your prayer slightly protects you from the heat of the dragon's breath!");
		}

		if (protection == 0) {
			t.sendMessage("You are badly burned by the dragon's breath.");
		}

		return maxDamage - protection;
	}

	// /**
	// * The percentage of the hit reducted by antifire.
	// */
	// protected static double dragonfireReduction(Mob mob) {
	// boolean dragonfireShield = mob.getEquipment() != null
	// && (mob.getEquipment().contains(1540)
	// || mob.getEquipment().contains(11283)
	// || mob.getEquipment().contains(11284) || mob
	// .getEquipment().contains(11285));
	// boolean dragonfirePotion = false;
	// boolean protectPrayer = mob.getCombatState().getPrayer(
	// CombatPrayer.PROTECT_FROM_MAGIC);
	// if (dragonfireShield && dragonfirePotion) {
	// if (mob.getActionSender() != null) {
	// mob.getActionSender().sendMessage(
	// "You shield absorbs most of the dragon fire!");
	// mob.getActionSender()
	// .sendMessage(
	// "Your potion protects you from the heat of the dragon's breath!");
	// }
	// return 1;
	// } else if (dragonfireShield) {
	// if (mob.getActionSender() != null) {
	// mob.getActionSender().sendMessage(
	// "You shield absorbs most of the dragon fire!");
	// }
	// return 0.8; // 80%
	// } else if (dragonfirePotion) {
	// if (mob.getActionSender() != null) {
	// mob.getActionSender()
	// .sendMessage(
	// "Your potion protects you from the heat of the dragon's breath!");
	// }
	// return 0.8; // 80%
	// } else if (protectPrayer) {
	// if (mob.getActionSender() != null) {
	// mob.getActionSender().sendMessage(
	// "Your prayers resist some of the dragon fire.");
	// }
	// return 0.6; // 60%
	// }
	// return /* mob.getEquipment() != null */0;
	// }s

	/**
	 * A series of checks performed before the entity attacks the victim.
	 * 
	 * @param builder the builder to perform the checks with.
	 * @return true if the entity passed the checks, false if they did not.
	 */
	public static boolean checkHook(GameCharacter entity, GameCharacter victim) {

		// Check if we need to reset the combat session.
		if (!victim.isRegistered() || !entity.isRegistered() || entity.getConstitution() <= 0
				|| victim.getConstitution() <= 0) {
			entity.getCombatBuilder().reset(true);
			return false;
		}

		// Here we check if the victim has teleported away.
		if (victim.isPlayer()) {
			if (((Player) victim).isTeleporting()
					|| !Location.ignoreFollowDistance(entity)
							&& !Locations.goodDistance(victim.getPosition(), entity.getPosition(), 40)
					|| ((Player) victim).isPlayerLocked()) {
				entity.getCombatBuilder().cooldown = 10;
				entity.getMovementQueue().setFollowCharacter(null);
				return false;
			}
		}

		if(victim.isPlayer()) {
			if(((Player)victim).isHidePlayer()) {
				if(entity.isPlayer()) {
					((Player) entity).sendMessage("A magical force prevents you from attacking.");
					return false;
				}
			}
		}

		if(entity.isPlayer() && victim.isNpc()) {
			Player player = (Player) entity;
			if(victim.getLocation() == Location.COWS && !GameSettings.COWS_ENABLED) {
				player.sendMessage("A magical force prevents you from attacking.");
				return false;
			}
		}

		if (entity.isPlayer() && (((Player) entity).requiresUnlocking() || ((Player) entity).isAccountCompromised())
				|| victim.isPlayer()
						&& (((Player) victim).requiresUnlocking() || ((Player) victim).isAccountCompromised())) {
			return false;
		}

		if (victim.isNpc() && entity.isPlayer()) {

			if (victim instanceof MaxHitDummy) {
				return true;
			}

			NPC npc = (NPC) victim;

			if (npc.getId() == Phases.OLM_HEAD || npc.getId() == Phases.OLM_LEFT_HAND || npc.getId() == Phases.OLM_RIGHT_HAND) {
				if (!GreatOlm.withinOlmRange(entity.asPlayer())) {
					return false;
				}
			}

			if (npc.getId() == 2044 || npc.getId() == 2043 || npc.getId() == 2042) {
				return true;
			}

			if (npc.getSpawnedFor() != null && npc.getSpawnedFor().getIndex() != ((Player) entity).getIndex()) {
				((Player) entity).getPacketSender().sendMessage("That's not your enemy to fight.");
				entity.getCombatBuilder().reset(true);
				return false;
			}
			if (npc.isFamiliar()) {
				Player player = ((Player) entity);
				if (player.getLocation() != Location.WILDERNESS) {
					player.getPacketSender().sendMessage("You can only attack familiars in the wilderness.");
					player.getCombatBuilder().reset(true);
					return false;
				} else if (npc.getLocation() != Location.WILDERNESS) {
					player.getPacketSender().sendMessage("That familiar is not in the wilderness.");
					player.getCombatBuilder().reset(true);
					return false;
				}
				/** DEALING DMG TO THEIR OWN FAMILIAR **/
				if (player.getSummoning().getFamiliar() != null
						&& player.getSummoning().getFamiliar().getSummonNpc() != null
						&& player.getSummoning().getFamiliar().getSummonNpc().getIndex() == npc.getIndex()) {
					return false;
				}
			}
			if (Nex.nexMob(npc.getId()) || npc.getId() == 6260 || npc.getId() == 6261 || npc.getId() == 6263
					|| npc.getId() == 6265 || npc.getId() == 6222 || npc.getId() == 6223 || npc.getId() == 6225
					|| npc.getId() == 6227 || npc.getId() == 6203 || npc.getId() == 6208 || npc.getId() == 6204
					|| npc.getId() == 6206 || npc.getId() == 6247 || npc.getId() == 6248 || npc.getId() == 6250
					|| npc.getId() == 6252) {
				if (!((Player) entity).getMinigameAttributes().getGodwarsDungeonAttributes().hasEnteredRoom()) {
					((Player) entity).getPacketSender()
							.sendMessage("You must enter the room before being able to attack.");
					entity.getCombatBuilder().reset(true);
					return false;
				}
			}
			if (Nex.nexMob(npc.getId())) {
				Player attacker = (Player) entity;
				if (!Nex.getNex(attacker).checkAttack(attacker, npc.getId())) {
					entity.getCombatBuilder().reset(true);
					return false;
				}
			} else if (npc.getId() == 6222) { // Kree'arra
				if (entity.getCombatBuilder().getStrategy().getCombatType() == CombatType.MELEE) {
					((Player) entity).getPacketSender().sendMessage("Kree'arra is resistant to melee attacks.");
					entity.getCombatBuilder().reset(true);
					return false;
				}
			}
			if (npc.getLocation() != Location.DUNGEONEERING &&
					npc.getDefinition().getSlayerLevel() > ((Player) entity).getSkillManager().getCurrentLevel(Skill.SLAYER)
					&& !((Player) entity).getSlayer().getSlayerTask().getNpcIds().contains(npc.getId())) {
				((Player) entity).getPacketSender().sendMessage("You need a Slayer level of at least "
						+ npc.getDefinition().getSlayerLevel() + " to attack this creature.");
				entity.getCombatBuilder().reset(true);
				return false;
			}
			if (npc.getId() == 6715 || npc.getId() == 6716 || npc.getId() == 6701 || npc.getId() == 6725
					|| npc.getId() == 6691 || npc.getId() == 22940 || npc.getId() == 22938) {
				if (entity.getLocation() != Location.WILDERNESS) {
					((Player) entity).getPacketSender().sendMessage("You cannot reach that.");
					entity.getCombatBuilder().reset(true);
					return false;
				}
			}
			if (npc.getId() == 4291 && entity.getPosition().getZ() == 2
					&& !((Player) entity).getMinigameAttributes().getWarriorsGuildAttributes().enteredTokenRoom()) {
				((Player) entity).getPacketSender().sendMessage("You cannot reach that.");
				entity.getCombatBuilder().reset(true);
				return false;
			}
		}

		boolean bypassUnderAttack = false;
		if(victim.isNpc()) {
			if(((NPC)victim).getId() == 21332) {
				bypassUnderAttack = true;
			}
		}
		// Here we check if we are already in combat with another entity.
		if (!bypassUnderAttack && entity.getCombatBuilder().getLastAttacker() != null && !Location.inMulti(entity)
				&& entity.getCombatBuilder().isBeingAttacked()
				&& !victim.equals(entity.getCombatBuilder().getLastAttacker())) {
			if (entity.isPlayer())
				((Player) entity).getPacketSender().sendMessage("You are already under attack!");
			entity.getCombatBuilder().reset(true);
			return false;
		}

		if (entity.isNpc() && ((NPC) entity).isFamiliar() && victim instanceof Player) {
			Familiar familiar = ((NPC) entity).getAsFamiliar();
			if (victim.getLocation() != Location.WILDERNESS) {
				entity.getCombatBuilder().reset(true);
				return false;
			} else if (entity.getLocation() != Location.WILDERNESS) {
				entity.getCombatBuilder().reset(true);
				return false;
			}
			if (!Location.inMulti(entity) && !Location.inMulti(victim)) {
				entity.getCombatBuilder().reset(true);
				return false;
			}

			if (familiar.getSummoner() != null
					&& familiar.getSummoner().getSkillManager().getCurrentLevel(Skill.CONSTITUTION) < 1) {
				entity.getCombatBuilder().reset(true);
				return false;
			}
		}

		// Here we check if the entity we are attacking is already in
		// combat.
		if (!(entity.isNpc() && ((NPC) entity).isFamiliar())) {
			boolean allowAttack = false;
			if (victim.getCombatBuilder().getLastAttacker() != null && !Location.inMulti(entity)
					&& victim.getCombatBuilder().isBeingAttacked()
					&& !victim.getCombatBuilder().getLastAttacker().equals(entity)) {

				if (victim.getCombatBuilder().getLastAttacker().isNpc()) {
					NPC npc = (NPC) victim.getCombatBuilder().getLastAttacker();

					if (npc.isFamiliar()) {
						if (entity.isPlayer()) {
							Player player = (Player) entity;
							if (player.getSummoning().getFamiliar() != null
									&& player.getSummoning().getFamiliar().getSummonNpc() != null && player
											.getSummoning().getFamiliar().getSummonNpc().getIndex() == npc.getIndex()) {
								allowAttack = true;
							}
						}
					}
				}

				if(victim.isNpc()) {
					if(((NPC)victim).getId() == 21332) {
						allowAttack = true;
					}
				}

				if (!allowAttack) {
					if (entity.isPlayer())
						((Player) entity).getPacketSender().sendMessage("They are already under attack!");
					entity.getCombatBuilder().reset(true);
					return false;
				}
			}
		}

		// Check if the victim is still in the wilderness, and check if the
		if (entity.isPlayer()) {
			if (victim.isPlayer()) {
				if (!properLocation((Player) entity, (Player) victim)) {
					entity.getCombatBuilder().reset(true);
					entity.setPositionToFace(victim.getPosition());
					return false;
				}
			}
			if (((Player) entity).isCrossingObstacle()) {
				entity.getCombatBuilder().reset(true);
				return false;
			}
		}

		// Check if the npc needs to retreat.
		if (entity.isNpc()) {
			NPC n = (NPC) entity;
			if (!Location.ignoreFollowDistance(n) && !Nex.nexMob(n.getId()) && !n.isFamiliar()) { // Stops combat for
																									// npcs if too far
																									// away
				if (n.getPosition().isWithinDistance(victim.getPosition(), 1)) {
					return true;
				}
				if (!(n instanceof AncestralGlyph) && !(n instanceof VerzikVitur)) {
					if (!n.getPosition().isWithinDistance(n.getDefaultPosition(),
							10 + n.getMovementCoordinator().getCoordinator().getRadius())) {
						n.getMovementQueue().reset();
						n.getMovementCoordinator().setCoordinateState(CoordinateState.AWAY);
						return false;
					}
				}
			}
		}

		return true;
	}

	/**
	 * Checks if the entity is close enough to attack.
	 * 
	 * @param builder the builder used to perform the check.
	 * @return true if the entity is close enough to attack, false otherwise.
	 */
	public static boolean checkAttackDistance(CombatBuilder builder) {
		return checkAttackDistance(builder.getCharacter(), builder.getVictim());
	}

	public static boolean checkAttackDistance(GameCharacter a, GameCharacter b) {

		Position attacker = a.getPosition();
		Position victim = b.getPosition();

		if (a.isNpc() && ((NPC) a).isFamiliar()) {
			return Locations.goodDistance(attacker, victim, a.getSize());
		}

		if (a.getCombatBuilder().getStrategy() == null)
			a.getCombatBuilder().determineStrategy();
		CombatStrategy strategy = a.getCombatBuilder().getStrategy();
		if (a.getRegionID() == 9043) {
			if (a instanceof NPC || strategy instanceof DefaultRangedCombatStrategy
					|| strategy instanceof DefaultMagicCombatStrategy) {
				return true;
			}
		}

		int distance = strategy.attackDistance(a);
		if (a.isPlayer() && strategy.getCombatType() != CombatType.MELEE) {
			if (b.getSize() >= 2)
				distance += b.getSize() - 1;
		}

		MovementQueue movement = a.getMovementQueue();
		MovementQueue otherMovement = b.getMovementQueue();

		// We're moving so increase the distance.
		if (!movement.isMovementDone() && !otherMovement.isMovementDone() && !movement.isLockMovement()
				&& !a.isFrozen()) {
			distance += 1;

			// We're running so increase the distance even more.
			// XXX: Might have to change this back to 1 or even remove it, not
			// sure what it's like on actual runescape. Are you allowed to
			// attack when the entity is trying to run away from you?
			if (movement.isRunToggled()) {
				distance += 2;
			}
		}

		if (b.isNpc() && (b.getAsNpc().getId() >= 2042 && b.getAsNpc().getId() <= 2044)) {
			distance = 50;
		}

		if (b.isNpc() && ((b.getAsNpc().getId() == 22555 || b.getAsNpc().getId() == 22553) && b.getPosition().getX() <= 3231)) {
			distance = 7;
		}


		/*
		 * Clipping checks and diagonal blocking by gabbe
		 */

		boolean sameSpot = attacker.equals(victim) && !a.getMovementQueue().isMoving()
				&& !b.getMovementQueue().isMoving();
		boolean goodDistance = !sameSpot && Locations.goodDistance(attacker.getX(), attacker.getY(), victim.getX(),
				victim.getY(), distance + a.getSize() - 1);
		boolean projectilePathBlocked = false;
		if (a.isPlayer()
				&& (strategy.getCombatType() == CombatType.RANGED || strategy.getCombatType() == CombatType.MAGIC)
				|| a.isNpc() && strategy.getCombatType() == CombatType.MELEE) {
			if (!RegionClipping.canProjectileAttack(b, a) && !RegionClipping.canProjectileAttack(a, b))
				projectilePathBlocked = a.getLocation() != Location.RAIDS;
		}
		if (!projectilePathBlocked && goodDistance) {
			if (strategy.getCombatType() == CombatType.MELEE && RegionClipping.isInDiagonalBlock(b, a)) {
				PathFinder.findPath(a, victim.getX(), victim.getY() + 1, true, 1, 1);
				return false;
			} else
				a.getMovementQueue().reset();
			return true;
		} else if (projectilePathBlocked || !goodDistance) {
			a.getMovementQueue().setFollowCharacter(b);
			return false;
		}
		// Check if we're within the required distance.

		return Position.isWithinDistance(a, b, distance);
	}

	/**
	 * Applies combat prayer effects to the calculated hits.
	 * 
	 * @param container the combat container that holds the hits.
	 * @param builder   the builder to apply prayer effects to.
	 */
	protected static void applyPrayerProtection(CombatContainer container, CombatBuilder builder) {

		// If we aren't checking the accuracy, then don't bother doing any of
		// this.

		if (builder.getVictim() == null) {
			return;
		}

		// The attacker is an npc, and the victim is a player so we completely
		// cancel the hits if the right prayer is active.

		if (builder.getVictim().isPlayer()) {

			Player victim = (Player) builder.getVictim();
			if (victim.getEquipment().getItems()[Equipment.SHIELD_SLOT].getId() == 13740) {
				container.allHits(context -> {
					if (context.getHit().getDamage() > 0) {
						if (victim.getSkillManager().getCurrentLevel(Skill.PRAYER) > 0) {
							int prayerLost = (int) (context.getHit().getDamage() * 0.09);
							if (victim.getSkillManager().getCurrentLevel(Skill.PRAYER) >= prayerLost) {
								context.getHit().incrementAbsorbedDamage(
										(int) (context.getHit().getDamage() - (context.getHit().getDamage() * 0.65)));
								victim.getSkillManager().setCurrentLevel(Skill.PRAYER,
										victim.getSkillManager().getCurrentLevel(Skill.PRAYER) - prayerLost);
							}
						}
					}
				});
			}

			if (victim.getEquipment().getItems()[Equipment.SHIELD_SLOT].getId() == 13742) {
				container.allHits(context -> {
					if (context.getHit().getDamage() > 0) {
						int chance = Misc.inclusiveRandom(1, 100);
						if(chance < 70) {
							context.getHit().setDamage((int) (context.getHit().getDamage() * .75));
						}
					}
				});
			}

			if (builder.getCharacter().isNpc()) {
				NPC attacker = (NPC) builder.getCharacter();
				// Except for verac of course :)
				if (attacker.getId() == 2030) {
					return;
				}
				if (attacker.getId() == 6260) {
					return;
				}
				if (attacker.getId() == 6261) {
					return;
				}
				if (attacker.getId() == 6203) {
					return;
				}
				if (attacker.getId() == 6247) {
					return;
				}
				if (attacker.getId() == 6222) {
					return;
				}
				if (attacker.getId() == 7134) {
					return;
				}
				if (attacker.getId() == 7286) {
					return;
				}
				if (attacker.getId() == 8349) {
					return;
				}
				if (attacker.getId() == 22405) {
					return;
				}
				if (attacker.getId() == 1608) {
					return;
				}
				if (attacker.getId() == 22287) {
					return;
				}
				if (attacker.getId() == 22940) {
					return;
				}
				if (attacker.getId() == 22938) {
					return;
				}

				// It's not verac so we cancel all of the hits.
				if (PrayerHandler.isActivated(victim, PrayerHandler.getProtectingPrayer(container.getCombatType()))
						|| CurseHandler.isActivated(victim,
								CurseHandler.getProtectingPrayer(container.getCombatType()))) {
					container.allHits(context -> {
						int hit = context.getHit().getDamage();
						if (attacker.getId() == 2745) { // Jad
							context.setAccurate(false);
							context.getHit().incrementAbsorbedDamage(hit);
						} else {
							double reduceRatio = 1.0;
							double mod = Math.abs(1 - reduceRatio);
							context.getHit().incrementAbsorbedDamage((int) (hit - (hit * mod)));
							mod = Math.round(RandomUtility.RANDOM.nextDouble() * 100.0) / 100.0;
							if (mod <= CombatFactory.PRAYER_ACCURACY_REDUCTION) {
								context.setAccurate(false);
							}
						}
					});
				}
			} else if (builder.getCharacter().isPlayer()) {
				Player attacker = (Player) builder.getCharacter();
				// If wearing veracs, the attacker will hit through prayer
				// protection.
				if (CombatFactory.fullVeracs(attacker)) {
					return;
				}

				// They aren't wearing veracs so lets reduce the accuracy and hits.
				if (PrayerHandler.isActivated(victim, PrayerHandler.getProtectingPrayer(container.getCombatType()))
						|| CurseHandler.isActivated(victim,
								CurseHandler.getProtectingPrayer(container.getCombatType()))) {
					container.allHits(context -> {
						// First reduce the damage.
						int hit = context.getHit().getDamage();
						double mod = Math.abs(1 - 0.4);
						context.getHit().incrementAbsorbedDamage((int) (hit - (hit * mod)));
						// Then reduce the accuracy.
						/*
						 * mod = Math.round(RandomUtility.RANDOM.nextDouble() * 100.0) / 110.0; if (mod
						 * <= CombatFactory.PRAYER_ACCURACY_REDUCTION) { context.setAccurate(false); }
						 */
					});
				}
			}
		} else if (builder.getVictim().isNpc() && builder.getCharacter().isPlayer()) {
			Player attacker = (Player) builder.getCharacter();
			NPC npc = (NPC) builder.getVictim();
			if (npc.getId() == 8349 && container.getCombatType() == CombatType.MELEE) {
				container.allHits(context -> {
					int hit = context.getHit().getDamage();
					double mod = Math.abs(1 - 0.5);
					context.getHit().incrementAbsorbedDamage((int) (hit - (hit * mod)));
					mod = Math.round(RandomUtility.RANDOM.nextDouble() * 100.0) / 100.0;
					if (mod <= CombatFactory.PRAYER_ACCURACY_REDUCTION) {
						context.setAccurate(false);
					}
				});
			} else if ((npc.getId() == 22145 && container.getCombatType() == CombatType.RANGED)
					|| (npc.getId() == 22146 && container.getCombatType() == CombatType.MAGIC)
					|| (npc.getId() == 22147 && container.getCombatType() == CombatType.MELEE)) {
				container.allHits(context -> {
					int hit = context.getHit().getDamage();
					double mod = Math.abs(1 - 0.95);
					context.getHit().incrementAbsorbedDamage((int) (hit - (hit * mod)));
					mod = Math.round(RandomUtility.RANDOM.nextDouble() * 100.0) / 100.0;
					if (mod <= CombatFactory.PRAYER_ACCURACY_REDUCTION) {
						context.setAccurate(false);
					}
				});

			} else if (npc.getId() == 1158
					&& (container.getCombatType() == CombatType.MAGIC || container.getCombatType() == CombatType.RANGED)
					|| npc.getId() == 1160 && container.getCombatType() == CombatType.MELEE) {
				container.allHits(context -> {
					int hit = context.getHit().getDamage();
					double mod = Math.abs(1 - 0.95);
					context.getHit().incrementAbsorbedDamage((int) (hit - (hit * mod)));
					mod = Math.round(RandomUtility.RANDOM.nextDouble() * 100.0) / 100.0;
					if (mod <= CombatFactory.PRAYER_ACCURACY_REDUCTION) {
						context.setAccurate(false);
					}
				});
				attacker.getPacketSender().sendMessage("Your "
						+ (container.getCombatType() == CombatType.MAGIC ? "magic"
								: container.getCombatType() == CombatType.RANGED ? "ranged" : "melee")
						+ " attack has" + (!container.getHits()[0].isAccurate() ? "" : " close to")
						+ " no effect on the queen.");
			} else if (npc.getId() == 13347 && Nex.getNex(attacker).zarosStage()) {
				container.allHits(context -> {
					int hit = context.getHit().getDamage();
					double mod = Math.abs(1 - 0.4);
					context.getHit().incrementAbsorbedDamage((int) (hit - (hit * mod)));
					mod = Math.round(RandomUtility.RANDOM.nextDouble() * 100.0) / 100.0;
					if (mod <= CombatFactory.PRAYER_ACCURACY_REDUCTION) {
						context.setAccurate(false);
					}
				});
			}
		}
	}

	/**
	 * Gives experience for the total amount of damage dealt in a combat hit.
	 * 
	 * @param builder   the attacker's combat builder.
	 * @param container the attacker's combat container.
	 * @param damage    the total amount of damage dealt.
	 * @param victim
	 */
	protected static void giveExperience(CombatBuilder builder, CombatContainer container, int damage,
			GameCharacter victim) {

		// This attack does not give any experience.
		if (container.getExperience().length == 0 && container.getCombatType() != CombatType.MAGIC)
			return;

		if (victim != null && victim.isNpc()) {
			NPC n = (NPC) victim;

			if (n != null && n instanceof MaxHitDummy)
				return;
		}

		// Otherwise we give experience as normal.
		if (builder.getCharacter().isPlayer()) {
			Player player = (Player) builder.getCharacter();

			if (container.getCombatType() == CombatType.MAGIC) {
				if (player.getCurrentlyCasting() != null)
					player.getSkillManager().addExperience(Skill.MAGIC,
							(int) (((damage * .90) * Skill.MAGIC.getModifier())
									/ container.getExperience().length)
									+ builder.getCharacter().getCurrentlyCasting().baseExperience());
			} else {
				for (int i : container.getExperience()) {
					Skill skill = Skill.forId(i);

					int combatXp = ((((damage * 4) * skill.getModifier()) / 10) / container.getExperience().length);

					player.getSkillManager().addExperience(skill, combatXp);

					//System.out.println("damage : "+damage+","+combatXp);
				}
			}

			double constitutionXp = ((damage * 1.33) * Skill.CONSTITUTION.getModifier() / 10);

			//System.out.println("Constitution xp : "+constitutionXp);

			player.getSkillManager().addExperience(Skill.CONSTITUTION, constitutionXp);
		}
	}

	/**
	 * Handles various armor effects for the attacker and victim.
	 * 
	 * @param builder   the attacker's combat builder.
	 * @param container the attacker's combat container.
	 * @param damage    the total amount of damage dealt.
	 */
	// TODO: Use abstraction for this, will need it when more effects are added.
	protected static void handleArmorEffects(GameCharacter attacker, GameCharacter target, int damage, CombatType combatType) {

		if (attacker.getConstitution() > 0 && damage > 0) {
			if (target != null && target.isPlayer()) {
				Player t2 = (Player) target;
				/** RECOIL **/
				if (t2.getEquipment().getItems()[Equipment.RING_SLOT].getId() == 2550) {
					int recDamage = (int) (damage * 0.10);
					if (recDamage <= 0)
						return;
					if (recDamage > t2.getConstitution())
						recDamage = t2.getConstitution();
					attacker.dealDamage(new Hit(recDamage, Hitmask.RED, CombatIcon.DEFLECT));
					ItemDegrading.handleItemDegrading(t2, DegradingItem.RING_OF_RECOIL);
				} else if (t2.getEquipment().getItems()[Equipment.RING_SLOT].getId() == 19672) { //Eye of the ranger
					if(combatType == CombatType.RANGED) {
						int recDamage = (int) (damage * 0.13);
						if (recDamage <= 0)
							return;
						if (recDamage > t2.getConstitution())
							recDamage = t2.getConstitution();
						attacker.dealDamage(new Hit(recDamage, Hitmask.RED, CombatIcon.BLUE_SHIELD));
					}
				} else if (t2.getEquipment().getItems()[Equipment.RING_SLOT].getId() == 19674) { //Eye of the mage
					if(combatType == CombatType.MAGIC) {
						int recDamage = (int) (damage * 0.13);
						if (recDamage <= 0)
							return;
						if (recDamage > t2.getConstitution())
							recDamage = t2.getConstitution();
						attacker.dealDamage(new Hit(recDamage, Hitmask.RED, CombatIcon.BLUE_SHIELD));
					}
				} else if (t2.getEquipment().getItems()[Equipment.RING_SLOT].getId() == 19673) { //Eye of the warrior
					if(combatType == CombatType.MELEE) {
						int recDamage = (int) (damage * 0.13);
						if (recDamage <= 0)
							return;
						if (recDamage > t2.getConstitution())
							recDamage = t2.getConstitution();
						attacker.dealDamage(new Hit(recDamage, Hitmask.RED, CombatIcon.BLUE_SHIELD));
					}
				}

				/** PHOENIX NECK **/
				else if (t2.getEquipment().getItems()[Equipment.AMULET_SLOT].getId() == 11090
						&& t2.getLocation() != Location.DUEL_ARENA) {
					int restore = (int) (t2.getSkillManager().getMaxLevel(Skill.CONSTITUTION) * .3);
					if (t2.getSkillManager().getCurrentLevel(
							Skill.CONSTITUTION) <= t2.getSkillManager().getMaxLevel(Skill.CONSTITUTION) * .2) {
						t2.performGraphic(new Graphic(1690));
						t2.getEquipment().delete(t2.getEquipment().getItems()[Equipment.AMULET_SLOT]);
						t2.getSkillManager().setCurrentLevel(Skill.CONSTITUTION,
								t2.getSkillManager().getCurrentLevel(Skill.CONSTITUTION) + restore);
						t2.getPacketSender().sendMessage(
								"Your Phoenix Necklace restored your Constitution, but was destroyed in the process.");
						t2.getUpdateFlag().flag(Flag.APPEARANCE);
					}
				}

				/** RING OF LIFE **/
				else if (t2.getEquipment().getItems()[Equipment.RING_SLOT].getId() == 2570
						&& t2.getLocation() != Location.DUEL_ARENA && t2.getLocation() != Location.WILDERNESS) {
					if (t2.getSkillManager().getCurrentLevel(
							Skill.CONSTITUTION) <= t2.getSkillManager().getMaxLevel(Skill.CONSTITUTION) * .1) {
						t2.getEquipment().delete(t2.getEquipment().getItems()[Equipment.RING_SLOT]);
						TeleportHandler.teleportPlayer(t2, GameSettings.DEFAULT_POSITION.copy(),
								TeleportType.RING_TELE);
						t2.getPacketSender().sendMessage(
								"Your Ring of Life tried to teleport you away, but was destroyed in the process.");
					}
				}
				// WeaponPoison.handleWeaponPoison(((Player)attacker), t2);
			}
		}

		// 25% chance of these barrows armor effects happening.
		if (RandomUtility.exclusiveRandom(4) == 0) {

			// The guthans effect is here.
			if (CombatFactory.fullGuthans(attacker)) {
				target.performGraphic(new Graphic(398));
				attacker.heal(damage);
				return;
			}
			// The rest of the effects only apply to victims that are players.
			/*
			 * if (builder.getVictim().isPlayer()) { Player victim = (Player)
			 * builder.getVictim();
			 * 
			 * // The torags effect is here. if
			 * (CombatFactory.fullTorags(builder.getEntity())) {
			 * victim.decrementRunEnergy(RandomUtility.inclusiveRandom(1, 100));
			 * victim.performGraphic(new Graphic(399)); return; }
			 * 
			 * // The ahrims effect is here. if
			 * (CombatFactory.fullAhrims(builder.getEntity()) &&
			 * victim.getSkills()[Skills.STRENGTH].getLevel() >=
			 * victim.getSkills()[Skills.STRENGTH].getLevelForExperience()) {
			 * victim.getSkills()[Skills.STRENGTH].decreaseLevel(Utility.inclusiveRandom( 1,
			 * 10)); Skills.reload(victim, Skills.STRENGTH); victim.performGraphic(new
			 * Graphic(400)); return; }
			 * 
			 * // The karils effect is here. if
			 * (CombatFactory.fullKarils(builder.getEntity()) &&
			 * victim.getSkills()[Skills.AGILITY].getLevel() >=
			 * victim.getSkills()[Skills.AGILITY].getLevelForExperience()) {
			 * victim.performGraphic(new Graphic(401));
			 * victim.getSkills()[Skills.AGILITY].decreaseLevel(Utility.inclusiveRandom( 1,
			 * 10)); Skills.reload(victim, Skills.AGILITY); return; } }
			 */
		}
	}

	/**
	 * Handles various prayer effects for the attacker and victim.
	 * 
	 * @param builder   the attacker's combat builder.
	 * @param container the attacker's combat container.
	 * @param damage    the total amount of damage dealt.
	 */
	protected static void handlePrayerEffects(GameCharacter attacker, GameCharacter target, int damage, CombatType combatType) {
		if (attacker == null || target == null)
			return;
		// Prayer effects can only be done with victims that are players.
		if (target.isPlayer() && damage > 0) {
			Player victim = (Player) target;

			// The redemption prayer effect.
			if (PrayerHandler.isActivated(victim, PrayerHandler.REDEMPTION)
					&& victim.getConstitution() <= (victim.getSkillManager().getMaxLevel(Skill.CONSTITUTION) / 10)) {
				int amountToHeal = (int) (victim.getSkillManager().getMaxLevel(Skill.PRAYER) * .25);
				victim.performGraphic(new Graphic(436));
				victim.getSkillManager().setCurrentLevel(Skill.PRAYER, 0);
				victim.getSkillManager().updateSkill(Skill.PRAYER);
				victim.getSkillManager().setCurrentLevel(Skill.CONSTITUTION, victim.getConstitution() + amountToHeal);
				victim.getSkillManager().updateSkill(Skill.CONSTITUTION);
				victim.getPacketSender().sendMessage("You've run out of prayer points!");
				PrayerHandler.deactivateAll(victim);
				return;
			}

			// These last prayers can only be done with player attackers.
			if (attacker.isPlayer()) {

				Player p = (Player) attacker;
				// The retribution prayer effect.
				if (PrayerHandler.isActivated(victim, PrayerHandler.RETRIBUTION) && victim.getConstitution() < 1) {
					victim.performGraphic(new Graphic(437));
					if (p.getPosition().isWithinDistance(victim.getPosition(), CombatFactory.RETRIBUTION_RADIUS)) {
						p.dealDamage(new Hit(RandomUtility.inclusiveRandom(CombatFactory.MAXIMUM_RETRIBUTION_DAMAGE),
								Hitmask.RED, CombatIcon.DEFLECT));
					}
				} else if (CurseHandler.isActivated(victim, CurseHandler.WRATH) && victim.getConstitution() < 1) {
					victim.performGraphic(new Graphic(2259));
					victim.performAnimation(new Animation(12583));
					if (p.getPosition().isWithinDistance(victim.getPosition(), CombatFactory.RETRIBUTION_RADIUS)) {
						p.performGraphic(new Graphic(2260));
						p.dealDamage(new Hit(RandomUtility.inclusiveRandom(CombatFactory.MAXIMUM_RETRIBUTION_DAMAGE),
								Hitmask.RED, CombatIcon.DEFLECT));
					}
				}

				if (PrayerHandler.isActivated((Player) attacker, PrayerHandler.SMITE)) {
					victim.getSkillManager().setCurrentLevel(Skill.PRAYER,
							victim.getSkillManager().getCurrentLevel(Skill.PRAYER) - damage / 4);
					if (victim.getSkillManager().getCurrentLevel(Skill.PRAYER) < 0)
						victim.getSkillManager().setCurrentLevel(Skill.PRAYER, 0);
					victim.getSkillManager().updateSkill(Skill.PRAYER);
				}
			}
		}

		if (attacker.isPlayer()) {

			Player p = (Player) attacker;
			if (CurseHandler.isActivated(p, CurseHandler.TURMOIL)) {
				if (Misc.getRandom(5) >= 3) {
					int increase = Misc.getRandom(2);
					if (p.getLeechedBonuses()[increase] + 1 < 30) {
						p.getLeechedBonuses()[increase] += 1;
						BonusManager.sendCurseBonuses(p);
					}
				}
			}
			if ((CurseHandler.isActivated(p, CurseHandler.SOUL_SPLIT) || p.isSoulSplit() || SuperiorOlmlet.hasEffect(p))
					&& damage > 0) {
				new Projectile(attacker, target, 2263, 44, 3, 43, 31, 0).sendProjectile();
				TaskManager.submit(new Task(1, p, false) {
					@Override
					public void execute() {
						if (!(attacker == null || target == null || attacker.getConstitution() <= 0)) {
							target.performGraphic(new Graphic(2264, GraphicHeight.LOW));
							boolean nexEffect = p.getEquipment().wearingNexAmours();
							int form = (int) (damage / 5);
							p.getSkillManager().setCurrentLevel(Skill.CONSTITUTION,
									p.getSkillManager().getCurrentLevel(Skill.CONSTITUTION) + form);
							if (p.getSkillManager().getCurrentLevel(Skill.CONSTITUTION) > p.getSkillManager()
									.getMaxLevel(Skill.CONSTITUTION)) {
								if (nexEffect) {
									if (p.getSkillManager().getCurrentLevel(Skill.CONSTITUTION) > 1390) {
										p.getSkillManager().setCurrentLevel(Skill.CONSTITUTION, 1390);
									}
								} else {
									p.getSkillManager().setCurrentLevel(Skill.CONSTITUTION,
											p.getSkillManager().getMaxLevel(Skill.CONSTITUTION));
								}
							}
							if (target.isPlayer()) {
								Player victim = (Player) target;
								victim.getSkillManager().setCurrentLevel(Skill.PRAYER,
										victim.getSkillManager().getCurrentLevel(Skill.PRAYER) - form);
								if (victim.getSkillManager().getCurrentLevel(Skill.PRAYER) < 0) {
									victim.getSkillManager().setCurrentLevel(Skill.PRAYER, 0);
									CurseHandler.deactivateCurses(victim);
									PrayerHandler.deactivatePrayers(victim);
								}
								victim.getSkillManager().updateSkill(Skill.PRAYER);
							}
						}
						stop();
					}
				});
			}
			if (p.getCurseActive()[CurseHandler.LEECH_ATTACK] || p.getCurseActive()[CurseHandler.LEECH_DEFENCE]
					|| p.getCurseActive()[CurseHandler.LEECH_STRENGTH] || p.getCurseActive()[CurseHandler.LEECH_MAGIC]
					|| p.getCurseActive()[CurseHandler.LEECH_RANGED]
					|| p.getCurseActive()[CurseHandler.LEECH_SPECIAL_ATTACK]
					|| p.getCurseActive()[CurseHandler.LEECH_ENERGY]) {
				int i, gfx, projectileGfx;
				i = gfx = projectileGfx = -1;
				if (Misc.getRandom(10) >= 7 && p.getCurseActive()[CurseHandler.LEECH_ATTACK]) {
					i = 0;
					projectileGfx = 2252;
					gfx = 2253;
				} else if (Misc.getRandom(15) >= 11 && p.getCurseActive()[CurseHandler.LEECH_DEFENCE]) {
					i = 1;
					projectileGfx = 2248;
					gfx = 2250;
				} else if (Misc.getRandom(11) <= 3 && p.getCurseActive()[CurseHandler.LEECH_STRENGTH]) {
					i = 2;
					projectileGfx = 2236;
					gfx = 2238;
				} else if (Misc.getRandom(20) >= 16 && p.getCurseActive()[CurseHandler.LEECH_RANGED]) {
					i = 4;
					projectileGfx = 2236;
					gfx = 2238;
				} else if (Misc.getRandom(30) >= 24 && p.getCurseActive()[CurseHandler.LEECH_MAGIC]) {
					i = 6;
					projectileGfx = 2244;
					gfx = 2242;
				} else if (Misc.getRandom(30) <= 4 && p.getCurseActive()[CurseHandler.LEECH_SPECIAL_ATTACK]) {
					i = 7;
					projectileGfx = 2256;
					gfx = 2257;
				} else if (Misc.getRandom(30) <= 4 && p.getCurseActive()[CurseHandler.LEECH_ENERGY]) {
					i = 8;
					projectileGfx = 2256;
					gfx = 2257;
				}
				if (i != -1) {
					p.performAnimation(new Animation(12575));
					if (i != 7 && i != 8) {
						if (p.getLeechedBonuses()[i] < 2)
							p.getLeechedBonuses()[i] += Misc.getRandom(2);
						BonusManager.sendCurseBonuses(p);
					}
					if (target.isPlayer()) {
						Player victim = (Player) target;
						new Projectile(attacker, target, projectileGfx, 44, 3, 43, 31, 0).sendProjectile();
						victim.performGraphic(new Graphic(gfx));
						if (i != 7 && i != 8) {
							CurseHandler.handleLeech(victim, i, 2, -25, true);
							BonusManager.sendCurseBonuses((Player) victim);
						} else if (i == 7) {
							// Leech spec
							boolean leeched = false;
							if ((victim.getSpecialPercentage() - 10) >= 0) {
								victim.setSpecialPercentage(victim.getSpecialPercentage() - 10);
								CombatSpecial.updateBar(victim);
								victim.getPacketSender()
										.sendMessage("Your Special Attack has been leeched by an enemy curse!");
								leeched = true;
							}
							if (leeched) {
								p.setSpecialPercentage(p.getSpecialPercentage() + 10);
								if (p.getSpecialPercentage() > 100)
									p.setSpecialPercentage(100);
							}
						} else if (i == 8) {
							// Leech energy
							boolean leeched = false;
							if ((victim.getRunEnergy() - 30) >= 0) {
								victim.setRunEnergy(victim.getRunEnergy() - 30);
								victim.getPacketSender().sendMessage("Your energy has been leeched by an enemy curse!");
								leeched = true;
							}
							if (leeched) {
								p.setRunEnergy(p.getRunEnergy() + 30);
								if (p.getRunEnergy() > 100)
									p.setRunEnergy(100);
							}
						}
					}
					p.getPacketSender()
							.sendMessage(
									"You manage to leech your target's "
											+ (i == 8 ? ("energy")
													: i == 7 ? ("Special Attack")
															: Misc.formatText(Skill.forId(i).toString().toLowerCase()))
											+ ".");
				}
			} else {
				boolean sapWarrior = p.getCurseActive()[CurseHandler.SAP_WARRIOR];
				boolean sapRanger = p.getCurseActive()[CurseHandler.SAP_RANGER];
				boolean sapMage = p.getCurseActive()[CurseHandler.SAP_MAGE];
				if (sapWarrior || sapRanger || sapMage) {
					if (sapWarrior && Misc.getRandom(8) <= 2) {
						CurseHandler.handleLeech(target, 0, 1, -10, true);
						CurseHandler.handleLeech(target, 1, 1, -10, true);
						CurseHandler.handleLeech(target, 2, 1, -10, true);
						p.performGraphic(new Graphic(2214));
						p.performAnimation(new Animation(12575));
						new Projectile(p, target, 2215, 44, 3, 43, 31, 0).sendProjectile();
						p.getPacketSender().sendMessage("You decrease the your Attack, Strength and Defence level..");
					} else if (sapRanger && Misc.getRandom(16) >= 9) {
						CurseHandler.handleLeech(target, 4, 1, -10, true);
						CurseHandler.handleLeech(target, 1, 1, -10, true);
						p.performGraphic(new Graphic(2217));
						p.performAnimation(new Animation(12575));
						new Projectile(p, target, 2218, 44, 3, 43, 31, 0).sendProjectile();
						p.getPacketSender().sendMessage("You decrease your target's Ranged and Defence level..");
					} else if (sapMage && Misc.getRandom(15) >= 10) {
						CurseHandler.handleLeech(target, 6, 1, -10, true);
						CurseHandler.handleLeech(target, 1, 1, -10, true);
						p.performGraphic(new Graphic(2220));
						p.performAnimation(new Animation(12575));
						new Projectile(p, target, 2221, 44, 3, 43, 31, 0).sendProjectile();
						p.getPacketSender().sendMessage("You decrease your target's Magic and Defence level..");
					}
				}
			}
		}
		if (target.isPlayer()) {
			Player victim = (Player) target;
			if (damage > 0 && Misc.getRandom(10) <= 4) {
				int deflectDamage = -1;
				if (CurseHandler.isActivated(victim, CurseHandler.DEFLECT_MAGIC) && combatType == CombatType.MAGIC) {
					victim.performGraphic(new Graphic(2228, GraphicHeight.MIDDLE));
					victim.performAnimation(new Animation(12573));
					deflectDamage = (int) (damage * 0.20);
				} else if (CurseHandler.isActivated(victim, CurseHandler.DEFLECT_MISSILES)
						&& combatType == CombatType.RANGED) {
					victim.performGraphic(new Graphic(2229, GraphicHeight.MIDDLE));
					victim.performAnimation(new Animation(12573));
					deflectDamage = (int) (damage * 0.20);
				} else if (CurseHandler.isActivated(victim, CurseHandler.DEFLECT_MELEE)
						&& combatType == CombatType.MELEE) {
					victim.performGraphic(new Graphic(2230, GraphicHeight.MIDDLE));
					victim.performAnimation(new Animation(12573));
					deflectDamage = (int) (damage * 0.20);
				}
				if (deflectDamage > 0) {
					if (deflectDamage > attacker.getConstitution())
						deflectDamage = attacker.getConstitution();
					final int toDeflect = deflectDamage;
					TaskManager.submit(new Task(1, victim, false) {
						@Override
						public void execute() {
							if (attacker == null || attacker.getConstitution() <= 0) {
								stop();
							} else
								attacker.dealDamage(new Hit(toDeflect, Hitmask.RED, CombatIcon.DEFLECT));
							stop();
						}
					});
				}
			}
		}

	}

	protected static void handleSpellEffects(GameCharacter attacker, GameCharacter target, int damage, CombatType combatType) {
		if (damage <= 0)
			return;
		if (target.isPlayer()) {
			Player t = (Player) target;
			if (t.hasVengeance()) {
				t.setHasVengeance(false);
				t.forceChat("Taste Vengeance!");
				int returnDamage = (int) (damage * 0.75);
				if (attacker.getConstitution() < returnDamage)
					returnDamage = attacker.getConstitution();
				attacker.dealDamage(new Hit(returnDamage, Hitmask.RED, CombatIcon.DEFLECT));
				attacker.getCombatBuilder().addDamage(target, returnDamage);
			}
		}
	}

	public static void chargeDragonFireShield(Player player) {
		if (player.getDfsCharges() >= 20) {
			player.getPacketSender().sendMessage("Your Dragonfire shield is fully charged and can be operated.");
			return;
		}
		player.performAnimation(new Animation(6695));
		player.performGraphic(new Graphic(1164));
		player.incrementDfsCharges(1);
		BonusManager.update(player);
		player.getPacketSender().sendMessage("Your shield absorbs some of the Dragon's fire..");
	}

	public static void handleDragonFireShield(final Player player, final GameCharacter target) {
		if (player == null || target == null || target.getConstitution() <= 0 || player.getConstitution() <= 0)
			return;
		player.getCombatBuilder().cooldown(false);
		player.setEntityInteraction(target);
		player.performAnimation(new Animation(6696));
		player.performGraphic(new Graphic(1165));
		TaskManager.submit(new Task(1, player, false) {
			int ticks = 0;

			@Override
			public void execute() {
				switch (ticks) {
				case 3:
					new Projectile(player, target, 1166, 44, 3, 43, 31, 0).sendProjectile();
					break;
				case 4:
					Hit h = new Hit(50 + Misc.getRandom(150), Hitmask.RED, CombatIcon.MAGIC);
					target.dealDamage(h);
					target.performGraphic(new Graphic(1167, GraphicHeight.HIGH));
					target.getCombatBuilder().addDamage(player, h.getDamage());
					target.getLastCombat().reset();
					stop();
					break;
				}
				ticks++;
			}
		});
		player.incrementDfsCharges(-20);
		BonusManager.update(player);
	}

	public static boolean properLocation(Player player, Player player2) {
		return player.getLocation().canAttack(player, player2);
	}
}
