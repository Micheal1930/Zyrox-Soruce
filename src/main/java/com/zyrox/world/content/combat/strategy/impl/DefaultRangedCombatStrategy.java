package com.zyrox.world.content.combat.strategy.impl;

import com.google.common.collect.Multiset.Entry;
import com.zyrox.engine.task.Task;
import com.zyrox.engine.task.TaskManager;
import com.zyrox.model.Animation;
import com.zyrox.model.CombatIcon;
import com.zyrox.model.Flag;
import com.zyrox.model.Graphic;
import com.zyrox.model.GraphicHeight;
import com.zyrox.model.GroundItem;
import com.zyrox.model.Hit;
import com.zyrox.model.Hitmask;
import com.zyrox.model.Item;
import com.zyrox.model.Locations;
import com.zyrox.model.Position;
import com.zyrox.model.Skill;
import com.zyrox.model.container.impl.Equipment;
import com.zyrox.model.definitions.WeaponAnimations;
import com.zyrox.model.definitions.WeaponInterfaces;
import com.zyrox.model.definitions.WeaponInterfaces.WeaponInterface;
import com.zyrox.model.item.CrawsBow;
import com.zyrox.model.item.Items;
import com.zyrox.model.projectile.Projectile;
import com.zyrox.util.Misc;
import com.zyrox.world.content.combat.CombatContainer;
import com.zyrox.world.content.combat.CombatFactory;
import com.zyrox.world.content.combat.CombatType;
import com.zyrox.world.content.combat.CombatContainer.CombatHit;
import com.zyrox.world.content.combat.effect.CombatPoisonEffect.PoisonType;
import com.zyrox.world.content.combat.range.CombatRangedAmmo.AmmunitionData;
import com.zyrox.world.content.combat.range.CombatRangedAmmo.RangedWeaponData;
import com.zyrox.world.content.combat.range.CombatRangedAmmo.RangedWeaponType;
import com.zyrox.world.content.combat.strategy.CombatStrategy;
import com.zyrox.world.content.combat.weapon.CombatSpecial;
import com.zyrox.world.content.combat.weapon.FightStyle;
import com.zyrox.world.content.minigames.impl.Dueling;
import com.zyrox.world.content.minigames.impl.Dueling.DuelRule;
import com.zyrox.world.entity.impl.GameCharacter;
import com.zyrox.world.entity.impl.GroundItemManager;
import com.zyrox.world.entity.impl.npc.NPC;
import com.zyrox.world.entity.impl.player.Player;

/**
 * The default combat strategy assigned to an {@link GameCharacter} during a ranged
 * based combat session.
 * 
 * @author lare96
 */
public class DefaultRangedCombatStrategy implements CombatStrategy {

	@Override
	public boolean canAttack(GameCharacter entity, GameCharacter victim) {

		// We do not need to check npcs.
		if (entity.isNpc()) {
			return true;
		}

		// Create the player instance.
		Player player = (Player) entity;

		if (Dueling.checkRule(player, DuelRule.NO_RANGED)) {
			player.getPacketSender().sendMessage("Ranged-attacks have been turned off in this duel!");
			player.getCombatBuilder().reset(true);
			return false;
		}

		// Check the ammo before proceeding.
		if (!checkAmmo(player)) {
			if (player.isSpecialActivated()) {
				player.setSpecialActivated(false);
				player.getPacketSender().sendSpriteChange(41006, 945);
				CombatSpecial.updateBar(player);
			}
			return false;
		}
		return true;
	}

	@Override
	public CombatContainer attack(GameCharacter entity, GameCharacter victim) {
		if (entity.isNpc()) {
			NPC npc = (NPC) entity;

			AmmunitionData ammo = AmmunitionData.ADAMANT_ARROW;

			switch (npc.getId()) {
			case 688:
				ammo = AmmunitionData.BRONZE_ARROW;
				break;
			case 27:
				ammo = AmmunitionData.STEEL_ARROW;
				break;
			case 2028:
				ammo = AmmunitionData.BOLT_RACK;
				break;
			case 6220:
			case 6256:
			case 6276:
				ammo = AmmunitionData.RUNE_ARROW;
				break;
			case 6225:
				ammo = AmmunitionData.STEEL_JAVELIN;
				break;
			case 6252:
				ammo = AmmunitionData.RUNE_ARROW;
				break;
			}

			entity.performAnimation(new Animation(npc.getDefinition().getAttackAnimation()));

			entity.performGraphic(new Graphic(ammo.getStartGfxId(),
					ammo.getStartHeight() >= 43 ? GraphicHeight.HIGH : GraphicHeight.MIDDLE));

			fireProjectile(npc, victim, ammo, false);

			return new CombatContainer(entity, victim, 1, CombatType.RANGED, true);
		}

		Player player = (Player) entity;
		final boolean dBow = CombatFactory.darkBow(player);

		player.setFireAmmo(0);

		startAnimation(player);

		AmmunitionData ammo = RangedWeaponData.getAmmunitionData(player);
		if (ammo != null) {
			entity.performGraphic(new Graphic(dBow ? ammo.getDarkbowStartGfxId() : ammo.getStartGfxId(),
					ammo.getStartHeight() >= 43 ? GraphicHeight.HIGH : GraphicHeight.MIDDLE));
		}

		if (!player.isSpecialActivated()) {
			if (!CombatFactory.crystalBow(player) && !CombatFactory.zaryteBow(player) && !CrawsBow.isWielding(player)) {
				decrementAmmo(player, victim.getPosition());
				if (dBow || player.getRangedWeaponData() == RangedWeaponData.MAGIC_SHORTBOW
						&& player.isSpecialActivated() && player.getCombatSpecial() != null
						&& player.getCombatSpecial() == CombatSpecial.MAGIC_SHORTBOW) {
					decrementAmmo(player, victim.getPosition());
				}
			}

			fireProjectile(player, victim, ammo, dBow);
		}

		CombatContainer container = new CombatContainer(entity, victim, dBow ? 2 : 1, CombatType.RANGED, true);

		/** CROSSBOW BOLTS EFFECT **/
		if (player.getEquipment().get(Equipment.WEAPON_SLOT).getDefinition() != null && player.getEquipment()
				.get(Equipment.WEAPON_SLOT).getDefinition().getName().toLowerCase().contains("crossbow")) {
			if (Misc.getRandom(12) >= 10) {
				container.setModifiedDamage(getModifiedDamage(player, victim, container));
			}
		}

		return container;
	}

	public static void fireProjectile(GameCharacter e, GameCharacter victim, final AmmunitionData ammo, boolean dBow) {
		TaskManager.submit(new Task(1, e.getCombatBuilder(), false) {
			@Override
			protected void execute() {
				new Projectile(e, victim, ammo.getProjectileId(), ammo.getProjectileDelay(), ammo.getProjectileSpeed(),
						ammo.getStartHeight(), ammo.getEndHeight(), 0).sendProjectile();
				if (dBow) {
					new Projectile(e, victim, ammo.getProjectileId(), ammo.getProjectileDelay(),
							ammo.getProjectileSpeed(), ammo.getStartHeight() + 3, ammo.getEndHeight() + 3, 0)
									.sendProjectile();
				}

				stop();
			}
		});
	}

	@Override
	public int attackDelay(GameCharacter entity) {
		return entity.getAttackSpeed();
	}

	@Override
	public int attackDistance(GameCharacter entity) {

		// The default distance for all npcs using ranged is 6.
		if (entity.isNpc()) {
			return 6;
		}

		// Create the player instance.

		Player player = (Player) entity;

		int distance = 5;
		if (player.getRangedWeaponData() != null)
			distance = player.getRangedWeaponData().getType().getDistanceRequired();

		return distance + (player.getFightType().getStyle() == FightStyle.DEFENSIVE ? 2 : 0);
	}

	/**
	 * Starts the performAnimation for the argued {@link Player} in the current
	 * combat hook.
	 * 
	 * @param player the player to start the performAnimation for.
	 */
	private void startAnimation(Player player) {
		if (player.getEquipment().get(Equipment.WEAPON_SLOT).getDefinition().getName().startsWith("Karils")) {
			player.performAnimation(new Animation(2075));
		} else if (player.getEquipment().get(Equipment.WEAPON_SLOT).getId() == 12926) {
			player.performAnimation(new Animation(5061));
		} else if (player.getEquipment().get(Equipment.WEAPON_SLOT).getId() == 12927) {
			player.performAnimation(new Animation(5061));
		} else if (player.getEquipment().get(Equipment.WEAPON_SLOT).getId() == 3065) {
			player.performAnimation(new Animation(5061));
		} else {
			player.performAnimation(new Animation(WeaponAnimations.getAttackAnimation(player)));
		}
	}

	/**
	 * Checks the ammo to make sure the argued {@link Player} has the right type and
	 * amount before attacking.
	 * 
	 * @param player the player's ammo to check.
	 * @return <code>true</code> if the player has the right ammo,
	 *         <code>false</code> otherwise.
	 */
	private boolean checkAmmo(Player player) {

		if (CombatFactory.crystalBow(player) || CombatFactory.zaryteBow(player) || CrawsBow.isWielding(player)) {
			return true;
		}

		RangedWeaponData data = player.getRangedWeaponData();

		if (data.getType() == RangedWeaponType.BLOWPIPE) {
			if (player.getEquipment().get(Equipment.WEAPON_SLOT).getId() == 12926
					|| player.getEquipment().get(Equipment.WEAPON_SLOT).getId() == 12927
					|| player.getEquipment().get(Equipment.WEAPON_SLOT).getId() == 3065) {
				if (!player.getBlowpipeLoading().getContents().isEmpty()) {
					return true;
				} else {
					player.getPacketSender().sendMessage("@red@You have run out of ammunition!");
					player.getCombatBuilder().reset(true);
					return false;
				}
			}
		}
		if (data.getType() == RangedWeaponType.THROW)
			return true;

		Item ammunition = player.getEquipment().getItems()[data.getType() == RangedWeaponType.THROW
				? Equipment.WEAPON_SLOT
				: Equipment.AMMUNITION_SLOT];
		boolean darkBow = data.getType() == RangedWeaponType.DARK_BOW && ammunition.getAmount() < 2
				|| data == RangedWeaponData.MAGIC_SHORTBOW && player.isSpecialActivated()
						&& player.getCombatSpecial() != null
						&& player.getCombatSpecial() == CombatSpecial.MAGIC_SHORTBOW && ammunition.getAmount() < 2;
		if (ammunition.getId() == -1 || ammunition.getAmount() < 1 || darkBow) {
			player.getPacketSender().sendMessage(darkBow ? "You need at least 2 arrows to fire this bow."
					: "You don't have any ammunition to fire.");
			player.getCombatBuilder().reset(true);
			return false;
		}

		boolean properEquipment = false;
		for (AmmunitionData ammo : data.getAmmunitionData()) {
			for (int i : ammo.getItemIds()) {
				if (i == ammunition.getId()) {
					properEquipment = true;
					break;
				}
			}
		}

		if (!properEquipment) {
			String ammoName = ammunition.getDefinition().getName(),
					weaponName = player.getEquipment().getItems()[Equipment.WEAPON_SLOT].getDefinition().getName(),
					add = !ammoName.endsWith("s") && !ammoName.endsWith("(e)") ? "s" : "";
			player.getPacketSender().sendMessage("You can not use " + ammoName + "" + add + " with "
					+ Misc.anOrA(weaponName) + " " + weaponName + ".");
			player.getCombatBuilder().reset(true);
			return false;
		}

		return true;
	}

	/**
	 * Decrements the amount ammo the {@link Player} currently has equipped.
	 * 
	 * @param player the player to decrement ammo for.
	 */
	public static void decrementAmmo(Player player, Position pos) {
		if (player.getEquipment().get(Equipment.WEAPON_SLOT).getId() == 4706) {
			return;
		}
		if (player.getEquipment().get(Equipment.WEAPON_SLOT).getId() == 52550) {
			return;
		}
		if (player.getEquipment().get(Equipment.WEAPON_SLOT).getId() == 12926
				|| player.getEquipment().get(Equipment.WEAPON_SLOT).getId() == 12927
				|| player.getEquipment().get(Equipment.WEAPON_SLOT).getId() == 3065) {
			if (!player.getBlowpipeLoading().getContents().isEmpty()) {
				for (Entry<Integer> dart : player.getBlowpipeLoading().getContents().entrySet()) {
					player.getBlowpipeLoading().getContents().remove(dart.getElement());
					return;
				}
			}
			return;
		}

		// Determine which slot we are decrementing ammo from.
		int slot = player.getWeapon() == WeaponInterface.SHORTBOW || player.getWeapon() == WeaponInterface.LONGBOW
				|| player.getWeapon() == WeaponInterface.CROSSBOW ? Equipment.AMMUNITION_SLOT : Equipment.WEAPON_SLOT;

		// Set the ammo we are currently using.
		player.setFireAmmo(player.getEquipment().get(slot).getId());

		boolean avas = player.getEquipment().get(Equipment.CAPE_SLOT).getId() == 10499
				|| player.getEquipment().get(Equipment.CAPE_SLOT).getId() == 9756
				|| player.getEquipment().get(Equipment.CAPE_SLOT).getId() == 50834
				|| player.getEquipment().get(Equipment.WEAPON_SLOT).getId() == 21010
				|| player.getEquipment().get(Equipment.WEAPON_SLOT).getId() == 21020
				|| player.getEquipment().get(Equipment.CAPE_SLOT).getId() == 9757;
		
		if (player.getEquipment().get(Equipment.CAPE_SLOT).getId() == Items.AVAS_ASSEMBLER
				|| player.getEquipment().get(Equipment.CAPE_SLOT).getId() == Items.MAX_CAPE) {
			avas = true;
		}
		
		final boolean comp = player.getEquipment().get(Equipment.CAPE_SLOT).getId() == 14022;
		final boolean max = player.getEquipment().get(Equipment.CAPE_SLOT).getId() == 14019;
		final boolean fireTwisted = player.getEquipment().get(Equipment.WEAPON_SLOT).getId() == 21020;
		final boolean lightTwisted = player.getEquipment().get(Equipment.WEAPON_SLOT).getId() == 21999;
		final boolean darkTwisted = player.getEquipment().get(Equipment.WEAPON_SLOT).getId() == Items.TWISTED_BOW_2;

		if (avas || comp || max || fireTwisted || lightTwisted || darkTwisted) {
			if (Misc.getRandom(8) == 1 || Misc.getRandom(8) == 2 || Misc.getRandom(8) == 3 || Misc.getRandom(8) == 4
					|| Misc.getRandom(8) == 5 || Misc.getRandom(8) == 6 || Misc.getRandom(8) == 7)
				return;
		}

		boolean ignore = false;

		ignore = player.getEquipment().get(Equipment.WEAPON_SLOT).getId() == 49481
				|| player.getEquipment().get(Equipment.WEAPON_SLOT).getId() == 49478;

		// Decrement the ammo in the selected slot.
		player.getEquipment().get(slot).decrementAmount();
		if (!avas && player.getFireAmmo() != 15243 && player.getLocation() != Locations.Location.KRAKEN && !ignore) {
			int random = Misc.random(10);
			if (random > 2) {
				final GroundItem arrows = new GroundItem(new Item(player.getFireAmmo()), pos,
						player.getUsername(), false, 120, pos.getZ() > 4? false : true, 120);
				if(player.getPlayerInstance().getInstance()!=null) {
					player.getPlayerInstance().getInstance().getGroundItems().add(arrows);
				}
				GroundItemManager.spawnGroundItem(player, arrows);
			}
		}

		// If we are at 0 ammo remove the item from the equipment completely.
		if (player.getEquipment().get(slot).getAmount() == 0)

		{
			player.getPacketSender().sendMessage("You have run out of ammunition!");
			player.getEquipment().set(slot, new Item(-1));

			if (slot == Equipment.WEAPON_SLOT) {
				WeaponInterfaces.assign(player, new Item(-1));
			}
			player.getUpdateFlag().flag(Flag.APPEARANCE);
		}

		// Refresh the equipment interface.
		player.getEquipment().refreshItems();

	}

	@Override
	public boolean customContainerAttack(GameCharacter entity, GameCharacter victim) {
		return false;
	}

	private static final int getModifiedDamage(Player player, GameCharacter target, CombatContainer container) {
		if (container == null || container.getHits().length < 1)
			return 0;
		CombatHit hit = container.getHits()[0];
		if (!hit.isAccurate())
			return 0;
		int damage = container.getHits()[0].getHit().getDamage();
		int ammo = player.getFireAmmo();
		if (ammo == -1) {
			return damage;
		}
		double multiplier = 1;
		Player pTarget = target.isPlayer() ? ((Player) target) : null;
		switch (ammo) {
		case 9236: // Lucky Lightning
			target.performGraphic(new Graphic(749));
			multiplier = 1.3;
			break;
		case 9237: // Earth's Fury
			target.performGraphic(new Graphic(755));
			multiplier = 1.05;
			break;
		case 9238: // Sea Curse
			target.performGraphic(new Graphic(750));
			multiplier = 1.1;
			break;
		case 9239: // Down To Earth
			target.performGraphic(new Graphic(757));
			if (pTarget != null) {
				pTarget.getSkillManager().setCurrentLevel(Skill.MAGIC,
						pTarget.getSkillManager().getCurrentLevel(Skill.MAGIC) - 3);
				pTarget.getPacketSender().sendMessage("Your Magic level has been reduced.");
			}
			break;
		case 9240: // Clear Mind
			target.performGraphic(new Graphic(751));
			if (pTarget != null) {
				pTarget.getSkillManager().setCurrentLevel(Skill.PRAYER,
						pTarget.getSkillManager().getCurrentLevel(Skill.PRAYER) - 40);
				if (pTarget.getSkillManager().getCurrentLevel(Skill.PRAYER) < 0) {
					pTarget.getSkillManager().setCurrentLevel(Skill.PRAYER, 0);
				}
				pTarget.getPacketSender().sendMessage("Your Prayer level has been leeched.");
				player.getSkillManager().setCurrentLevel(Skill.PRAYER,
						pTarget.getSkillManager().getCurrentLevel(Skill.PRAYER) + 40);
				if (player.getSkillManager().getCurrentLevel(Skill.PRAYER) > player.getSkillManager()
						.getMaxLevel(Skill.PRAYER)) {
					player.getSkillManager().setCurrentLevel(Skill.PRAYER,
							player.getSkillManager().getMaxLevel(Skill.PRAYER));
				} else {
					player.getPacketSender()
							.sendMessage("Your enchanced bolts leech some Prayer points from your opponent..");
				}
			}
			break;
		case 9241: // Magical Posion
			target.performGraphic(new Graphic(752));
			CombatFactory.poisonEntity(target, PoisonType.MILD);
			break;
		case 9242:
			if (player.getSkillManager().getCurrentLevel(Skill.CONSTITUTION)
					- player.getSkillManager().getCurrentLevel(Skill.CONSTITUTION) / 200 < 10) {
				break;
			}
			int priceDamage = (int) (player.getSkillManager().getCurrentLevel(Skill.CONSTITUTION) * 0.08);
			if (priceDamage < 0) {
				return damage;
			}
			int dmg2 = (int) ((int) (target.getConstitution() * 0.1) > 1000 ? 650 + Misc.getRandom(50)
					: (target.getConstitution() * 0.1));
			if (dmg2 <= 0) {
				return damage;
			}
			target.performGraphic(new Graphic(754));
			player.dealDamage(new Hit(priceDamage, Hitmask.RED, CombatIcon.RANGED));
			return dmg2;
		case 9243:
			target.performGraphic(new Graphic(758, GraphicHeight.MIDDLE));
			multiplier = 1.15;
			break;
		case 9244:
			target.performGraphic(new Graphic(756));
			if (pTarget != null && (pTarget.getEquipment().getItems()[Equipment.SHIELD_SLOT].getId() == 1540
					|| pTarget.getEquipment().getItems()[Equipment.SHIELD_SLOT].getId() == 11613
					|| pTarget.getEquipment().getItems()[Equipment.SHIELD_SLOT].getId() == 11283
					|| pTarget.getFireImmunity() > 0)) {
				return damage;
			}
			if (damage < 300 && Misc.getRandom(3) <= 1) {
				damage = 300 + Misc.getRandom(150);
			}
			multiplier = 1.33;
			break;
		case 9245:
			target.performGraphic(new Graphic(753));
			multiplier = 1.26;
			int heal = (int) (damage * 0.25) + 10;
			player.getSkillManager().setCurrentLevel(Skill.CONSTITUTION,
					player.getSkillManager().getCurrentLevel(Skill.CONSTITUTION) + heal);
			if (player.getSkillManager().getCurrentLevel(Skill.CONSTITUTION) >= 1120) {
				player.getSkillManager().setCurrentLevel(Skill.CONSTITUTION, 1120);
			}
			player.getSkillManager().updateSkill(Skill.CONSTITUTION);
			if (damage < 250 && Misc.getRandom(3) <= 1) {
				damage += 150 + Misc.getRandom(80);
			}
			break;
		}
		return (int) (damage * multiplier);
	}

	@Override
	public CombatType getCombatType() {
		return CombatType.RANGED;
	}
}
