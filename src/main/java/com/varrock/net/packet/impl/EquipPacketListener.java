package com.varrock.net.packet.impl;

import com.varrock.model.Animation;
import com.varrock.model.Flag;
import com.varrock.model.Graphic;
import com.varrock.model.Item;
import com.varrock.model.Skill;
import com.varrock.model.Locations.Location;
import com.varrock.model.container.impl.Equipment;
import com.varrock.model.container.impl.Inventory;
import com.varrock.model.definitions.WeaponAnimations;
import com.varrock.model.definitions.WeaponInterfaces;
import com.varrock.model.definitions.ItemDefinition.EquipmentType;
import com.varrock.net.packet.Packet;
import com.varrock.net.packet.PacketListener;
import com.varrock.util.Misc;
import com.varrock.world.content.BonusManager;
import com.varrock.world.content.Debug;
import com.varrock.world.content.Sounds;
import com.varrock.world.content.Sounds.Sound;
import com.varrock.world.content.combat.magic.Autocasting;
import com.varrock.world.content.combat.weapon.CombatSpecial;
import com.varrock.world.content.itemtransform.ItemTransformation;
import com.varrock.world.content.minigames.impl.Dueling;
import com.varrock.world.content.minigames.impl.Dueling.DuelRule;
import com.varrock.world.content.minigames.impl.castlewars.CastleWarsManager;
import com.varrock.world.content.minigames.impl.castlewars.item.flag.CastleWarsFlagManager;
import com.varrock.world.entity.impl.player.Player;

/**
 * This packet listener manages the equip action a player executes when wielding
 * or equipping an item.
 * 
 * @author relex lawl
 */

public class EquipPacketListener implements PacketListener {

	@Override
	public void handleMessage(Player player, Packet packet) {
		if (player.getConstitution() <= 0) {
			return;
		}

		if (player.isAccountCompromised() || player.requiresUnlocking()) {
			return;
		}

		int id = packet.readUnsignedShort();
		int slot = packet.readShortA();
		int interfaceId = packet.readShortA();

		if (player.getInterfaceId() > 0 && player.getInterfaceId() != 21172 /* EQUIP SCREEN */) {
			player.getPacketSender().sendInterfaceRemoval();
			// return;
		}

		Debug.write(player.getName(), "EquipPacketListener", new String[] {
				"id: "+id,
				"slot: "+slot,
				"interfaceId: "+interfaceId,
		});


		switch (interfaceId) {
		case Inventory.INTERFACE_ID:
			/*
			 * Making sure slot is valid.
			 */
			if (slot >= 0 && slot <= 28) {
				Item item = player.getInventory().getItems()[slot].copy();
				if (!player.getInventory().contains(item.getId()))
					return;
				/*
				 * Making sure item exists and that id is consistent.
				 */
				if (item != null && id == item.getId()) {
					for (Skill skill : Skill.values()) {
						if (skill == Skill.CONSTRUCTION)
							continue;
						if (item.getDefinition().getRequirement()[skill.ordinal()] > player.getSkillManager()
								.getMaxLevel(skill)) {
							StringBuilder vowel = new StringBuilder();
							if (skill.getName().startsWith("a") || skill.getName().startsWith("e")
									|| skill.getName().startsWith("i") || skill.getName().startsWith("o")
									|| skill.getName().startsWith("u")) {
								vowel.append("an ");
							} else {
								vowel.append("a ");
							}
							player.getPacketSender().sendMessage("You need " + vowel.toString()
									+ Misc.formatText(skill.getName()) + " level of at least "
									+ item.getDefinition().getRequirement()[skill.ordinal()] + " to wear this.");
							return;
						}
					}
					int equipmentSlot = item.getDefinition().getEquipmentSlot();
					if (equipmentSlot == -1) {
						return;
					}
					if ((item.getDefinition().getEquipmentType() == EquipmentType.WEAPON
							&& !item.getDefinition().isWeapon()) || item.getDefinition().isNoted()) {
						return;
					}

					if (CastleWarsManager.inCastleWars(player)) {
						if (equipmentSlot == Equipment.WEAPON_SLOT) {
							if (CastleWarsManager.isHoldingFlag(player)) {
								if(!CastleWarsFlagManager.dropFlag(player)) {
									return;
								}
							}
						}
					}
					

					Item equipItem = player.getEquipment().forSlot(equipmentSlot).copy();
					if (player.getLocation() == Location.DUEL_ARENA) {
						for (int i = 10; i < player.getDueling().selectedDuelRules.length; i++) {
							if (player.getDueling().selectedDuelRules[i]) {
								DuelRule duelRule = DuelRule.forId(i);
								if (equipmentSlot == duelRule.getEquipmentSlot()
										|| duelRule == Dueling.DuelRule.NO_SHIELD
												&& item.getDefinition().isTwoHanded()) {
									player.getPacketSender().sendMessage(
											"The rules that were set do not allow this item to be equipped.");
									return;
								}
							}
						}
						if (player.getDueling().selectedDuelRules[DuelRule.LOCK_WEAPON.ordinal()]) {
							if (equipmentSlot == Equipment.WEAPON_SLOT || item.getDefinition().isTwoHanded()) {
								player.getPacketSender().sendMessage("Weapons have been locked during this duel!");
								return;
							}
						}
					}
					if (player.hasStaffOfLightEffect()
							&& equipItem.getDefinition().getName().toLowerCase().contains("staff of light")) {
						player.setStaffOfLightEffect(-1);
						player.getPacketSender()
								.sendMessage("You feel the spirit of the Staff of Light begin to fade away...");
					}
					if (equipItem.getDefinition().isStackable() && equipItem.getId() == item.getId()) {
						ItemTransformation.onWieldAction(player, equipItem, true);

						int playerInvAmount = item.getAmount();
						long totalAmount = (long) playerInvAmount + equipItem.getAmount();
						int amount = item.getAmount() + equipItem.getAmount();

						if (totalAmount > Integer.MAX_VALUE) {
							amount = Integer.MAX_VALUE;
							int remainingAmount = (int) totalAmount - amount;
							player.getInventory().delete(item);
							player.getInventory().add(new Item(item.getId(), remainingAmount));
						} else {
							player.getInventory().delete(item);
						}

						player.getEquipment().getItems()[equipmentSlot].setAmount(amount);
						equipItem.setAmount(amount);
						player.getEquipment().refreshItems();
					} else {
						if (item.getDefinition().isTwoHanded()
								&& item.getDefinition().getEquipmentSlot() == Equipment.WEAPON_SLOT) {
							int slotsNeeded = 0;
							if (player.getEquipment().isSlotOccupied(Equipment.SHIELD_SLOT)
									&& player.getEquipment().isSlotOccupied(Equipment.WEAPON_SLOT)) {
								slotsNeeded++;
							}
							if (player.getInventory().getFreeSlots() >= slotsNeeded) {
								ItemTransformation.onWieldAction(player, equipItem, true);
								Item shield = player.getEquipment().getItems()[Equipment.SHIELD_SLOT];
								player.getInventory().add(shield, "equip shield");
								player.getInventory().delete(item);
								player.getEquipment().delete(shield);
								player.getInventory().add(equipItem, "equip item");
								player.getInventory().add(shield, "equip shield");
								player.getEquipment().setItem(equipmentSlot, item);
							} else {
								player.getInventory().full();
								return;
							}
						} else if (equipmentSlot == Equipment.SHIELD_SLOT
								&& player.getEquipment().getItems()[Equipment.WEAPON_SLOT].getDefinition()
										.isTwoHanded()) {
							player.getInventory().setItem(slot,
									player.getEquipment().getItems()[Equipment.WEAPON_SLOT]);
							player.getEquipment().setItem(Equipment.WEAPON_SLOT, new Item(-1));
							player.getEquipment().setItem(Equipment.SHIELD_SLOT, item);
							resetWeapon(player);
						} else {
							ItemTransformation.onWieldAction(player, equipItem, true);
							if (item.getDefinition().getEquipmentSlot() == equipItem.getDefinition().getEquipmentSlot()
									&& equipItem.getId() != -1) {
								if (player.getInventory().contains(equipItem.getId())) {
									player.getInventory().delete(item);
									player.getInventory().add(equipItem, "equip item");
								} else
									player.getInventory().setItem(slot, equipItem);
								player.getEquipment().setItem(equipmentSlot, item);
							} else {
								player.getInventory().setItem(slot, new Item(-1, 0));
								player.getEquipment().setItem(item.getDefinition().getEquipmentSlot(), item);
							}
						}
					}
					if (equipmentSlot == Equipment.WEAPON_SLOT) {
						resetWeapon(player);
					} else if (equipmentSlot == Equipment.RING_SLOT && item.getId() == 2570) {
						player.getPacketSender().sendMessage(
								"<img=10> <col=996633>Warning! The Ring of Life special effect does not work in the Wilderness or")
								.sendMessage("<col=996633> Duel Arena.");
					}

					if (player.getEquipment().get(Equipment.WEAPON_SLOT).getId() != 4153) {
						player.getCombatBuilder().cooldown(false);
					}

					if(item.getId() == 51015) {
						player.dinhSwitchDelay = System.currentTimeMillis();
					}
					
					player.setCastSpell(null);
					BonusManager.update(player);
					player.getEquipment().refreshItems();
					player.getInventory().refreshItems();
					player.getUpdateFlag().flag(Flag.APPEARANCE);
					Sounds.sendSound(player, Sound.EQUIP_ITEM);

					if(item.getId() == 21010) {
	                    player.performAnimation(new Animation(12806));
						player.setGraphic(new Graphic(435));
					}

					if(item.getId() == 14913) {
						player.setGraphic(new Graphic(1745));
					}
					
					if(item.getId() == 21020) {
	                    player.performAnimation(new Animation(12806));
						player.setGraphic(new Graphic(1745));
					}
					
					if(item.getId() == 50834) {
	                    player.performAnimation(new Animation(11974));
						player.setGraphic(new Graphic(6));
					}

					ItemTransformation.onWieldAction(player, item, false);
				}
			}
			break;
		}
	}

	public static void resetWeapon(Player player) {
		Item weapon = player.getEquipment().get(Equipment.WEAPON_SLOT);
		WeaponInterfaces.assign(player, weapon);
		WeaponAnimations.assign(player, weapon);
		if (player.getAutocastSpell() != null || player.isAutocast()) {
			Autocasting.resetAutocast(player, true);
			player.getPacketSender().sendMessage("Autocast spell cleared.");
		}
		player.setSpecialActivated(false);
		player.getPacketSender().sendSpriteChange(41006, 945);
		CombatSpecial.updateBar(player);
	}

	public static final int OPCODE = 41;
}