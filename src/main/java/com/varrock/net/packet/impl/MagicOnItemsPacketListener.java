package com.varrock.net.packet.impl;

import com.varrock.GameSettings;
import com.varrock.model.Animation;
import com.varrock.model.Graphic;
import com.varrock.model.GraphicHeight;
import com.varrock.model.Item;
import com.varrock.model.Skill;
import com.varrock.model.item.ItemCombination;
import com.varrock.net.packet.Packet;
import com.varrock.net.packet.PacketListener;
import com.varrock.world.content.Achievements;
import com.varrock.world.content.Debug;
import com.varrock.world.content.combat.magic.Enchantables;
import com.varrock.world.content.combat.magic.MagicSpells;
import com.varrock.world.content.combat.magic.Spell;
import com.varrock.world.content.combat.magic.Enchantables.*;
import com.varrock.world.content.skill.impl.smithing.Smelting;
import com.varrock.world.content.skill.impl.smithing.SmithingData;
import com.varrock.world.entity.impl.player.Player;

/**
 * Handles magic on items. 
 * @author Gabriel Hannason
 */
public class MagicOnItemsPacketListener implements PacketListener {

	@SuppressWarnings("unused")
	@Override
	public void handleMessage(Player player, Packet packet) {
		if(packet.getOpcode() == MAGIC_ON_GROUNDITEMS) {
			final int itemY = packet.readLEShort();
			int itemId = packet.readUnsignedShort();
			final int itemX = packet.readLEShort();
			final int spellId = packet.readUnsignedShortA();
			final MagicSpells spell = MagicSpells.forSpellId(spellId);
			if(spell == null)
				return;
			player.getMovementQueue().reset();
			//switch(spell) {}
		} else if(packet.getOpcode() == MAGIC_ON_ITEMS) {
			int slot = packet.readShort();
			int itemId = packet.readUnsignedShortA();
			int childId = packet.readShort();
			int spellId = packet.readShortA();


			if(!player.getClickDelay().elapsed(1300))
				return;

			if(slot < 0 || slot > player.getInventory().capacity())
				return;

			if(player.getInventory().getItems()[slot].getId() != itemId)
				return;

			Debug.write(player.getName(), "MagicOnItemsPacketListener - magic on items", new String[] {
					"itemId: "+itemId,
					"childId: "+childId,
					"spellId: "+spellId,
			});

			System.out.println(spellId);

			Item item = new Item(itemId);
			MagicSpells magicSpell = MagicSpells.forSpellId(spellId);
			if(magicSpell == null)
				return;
			Spell spell = magicSpell.getSpell();
			switch(magicSpell) {
			case LOW_ALCHEMY:
			case HIGH_ALCHEMY:
				if(!item.tradeable(player) || !item.sellable() || item.getId() == 995) {
					player.getPacketSender().sendMessage("This spell can not be cast on this item.");
					return;
				}
				if(spell == null || !spell.canCast(player, true))
					return;

				player.getInventory().delete(itemId, 1).add(995, 200 + (int) (item.getDefinition().getValue() * (magicSpell == MagicSpells.HIGH_ALCHEMY ? 1 : 0.8)), "Alching " + item.getId());
				player.performAnimation(new Animation(712));
				player.performGraphic(new Graphic(magicSpell == MagicSpells.HIGH_ALCHEMY ? 113 : 112, GraphicHeight.LOW));
				player.getSkillManager().addExperience(Skill.MAGIC, spell.baseExperience());
				player.getPacketSender().sendTab(GameSettings.MAGIC_TAB);
				break;
				case SAPPHIRE:
					Sapphire sapphire = null;
					for (Sapphire x : Sapphire.values()) {
						if (x.getItemNeeded() == itemId) {
							sapphire = x;
						}
					}
					if (sapphire == null) {
						player.sendMessage("This item cannot be enchanted.");
						return;
					}
					if (spell == null || !spell.canCast(player, true))
						return;
					player.getSkillManager().addExperience(Skill.MAGIC, (spell.levelRequired() + 10) * Skill.MAGIC.getModifier());
					player.sendMessage("You enchant the " + item.getDefinition().getName());
					player.getInventory().delete(itemId, 1).add(sapphire.getProduct(), 1);
					break;
				case EMERALD:
					Emerald emerald = null;
					for (Emerald x : Emerald.values()) {
						if (x.getItemNeeded() == itemId) {
							emerald = x;
						}
					}
					if (emerald == null) {
						player.sendMessage("This item cannot be enchanted.");
						return;
					}

					if (spell == null || !spell.canCast(player, true))
						return;
					player.getSkillManager().addExperience(Skill.MAGIC, (spell.levelRequired() + 10) * Skill.MAGIC.getModifier());
					player.sendMessage("You enchant the " + item.getDefinition().getName());
					player.getInventory().delete(itemId, 1).add(emerald.getProduct(), 1);
					break;
				case RUBY:
					Ruby ruby = null;
					for (Ruby x : Ruby.values()) {
						if (x.getItemNeeded() == itemId) {
							ruby = x;
						}
					}
					if (ruby == null) {
						player.sendMessage("This item cannot be enchanted.");
						return;
					}

					if (spell == null || !spell.canCast(player, true))
						return;
					player.getSkillManager().addExperience(Skill.MAGIC, (spell.levelRequired() + 10) * Skill.MAGIC.getModifier());
					player.sendMessage("You enchant the " + item.getDefinition().getName());
					player.getInventory().delete(itemId, 1).add(ruby.getProduct(), 1);
					break;
				case DIAMOND:
					Diamond diamond = null;
					for (Diamond x : Diamond.values()) {
						if (x.getItemNeeded() == itemId) {
							diamond = x;
						}
					}
					if (diamond == null) {
						player.sendMessage("This item cannot be enchanted.");
						return;
					}
					if (spell == null || !spell.canCast(player, true))
						return;

					player.getSkillManager().addExperience(Skill.MAGIC, (spell.levelRequired() + 10) * Skill.MAGIC.getModifier());
					player.sendMessage("You enchant the " + item.getDefinition().getName());
					player.getInventory().delete(itemId, 1).add(diamond.getProduct(), 1);
					break;
				case DRAGONSTONE:
					Dragonstone dragonstone = null;
					for (Dragonstone x : Dragonstone.values()) {
						if (x.getItemNeeded() == itemId) {
							dragonstone = x;
						}
					}
					if (dragonstone == null) {
						player.sendMessage("This item cannot be enchanted.");
						return;
					}

					if (spell == null || !spell.canCast(player, true))
						return;
					player.getSkillManager().addExperience(Skill.MAGIC, (spell.levelRequired() + 10) * Skill.MAGIC.getModifier());
					player.sendMessage("You enchant the " + item.getDefinition().getName());
					player.getInventory().delete(itemId, 1).add(dragonstone.getProduct(), 1);

					if(dragonstone == Dragonstone.AMULET) {
						Achievements.doProgress(player, Achievements.AchievementData.MAKE_1000_GLORYS);
					}

					break;
				case ONYX:
					Onyx onyx = null;
					for (Onyx x : Onyx.values()) {
						if (x.getItemNeeded() == itemId) {
							onyx = x;
						}
					}
					if (onyx == null) {
						player.sendMessage("This item cannot be enchanted.");
						return;
					}
					if (spell == null || !spell.canCast(player, true))
						return;
					player.getSkillManager().addExperience(Skill.MAGIC, (spell.levelRequired() + 10) * Skill.MAGIC.getModifier());
					player.sendMessage("You enchant the " + item.getDefinition().getName());
					player.getInventory().delete(itemId, 1).add(onyx.getProduct(), 1);
					break;
			case SUPERHEAT_ITEM:
				for(int i = 0; i < ORE_DATA.length; i++) {
					if(item.getId() == ORE_DATA[i][0]) {

						int experience = ORE_DATA[i][4];

						if(!SmithingData.canSmelt(player, ORE_DATA[i][4]))
							return;

						if(player.getInventory().getAmount(ORE_DATA[i][2]) < ORE_DATA[i][3]) {
							player.getPacketSender().sendMessage("You do not have enough "+new Item(ORE_DATA[i][2]).getDefinition().getName()+"s for this spell.");
							return;
						}

						if(spell == null || !spell.canCast(player, true))
							return;
						player.getInventory().delete(item.getId(), 1);
						for(int k = 0; k < ORE_DATA[i][3]; k++)
							player.getInventory().delete(ORE_DATA[i][2], 1);
						player.performAnimation(new Animation(725));
						player.performGraphic(new Graphic(148, GraphicHeight.HIGH));
						player.getInventory().add(ORE_DATA[i][4], 1, "Magic on items");
						player.getPacketSender().sendTab(GameSettings.MAGIC_TAB);
						player.getSkillManager().addExperience(Skill.MAGIC, spell.baseExperience());
						player.getSkillManager().addExperience(Skill.SMITHING, Smelting.getExperience(ORE_DATA[i][4]) * Skill.SMITHING.getModifier());

						return;
					}		
				}
				player.getPacketSender().sendMessage("This spell can only be cast on Mining ores.");
				break;
			case BAKE_PIE:
				if (itemId == 2317 || itemId == 2319 || itemId == 2321) {
					player.getSkillManager().addExperience(Skill.MAGIC, spell.baseExperience());
					player.performAnimation(new Animation(4413));
					player.performGraphic(new Graphic(746, GraphicHeight.HIGH));
					player.getInventory().delete(item.getId(), 1);
					player.getPacketSender().sendMessage("You bake the pie");
					player.getInventory().add(itemId == 2317 ? 2323 : itemId == 2319 ? 2327 : itemId == 2321 ? 2325 : -1, 1, "Magic on items");
				} else
					player.getPacketSender().sendMessage("This spell can only be cast on an uncooked pie.");
				break;
			default:
				break;
			}
			player.getClickDelay().reset();
			player.getInventory().refreshItems();
		}
	}

	final static int[][] ORE_DATA = {
		{436, 1, 438, 1, 2349, 53}, // TIN
		{438, 1, 436, 1, 2349, 53}, // COPPER
		{440, 1, -1, -1, 2351, 53}, // IRON ORE
		{442, 1, -1, -1, 2355, 53}, // SILVER ORE
		{444, 1, -1, -1, 2357, 23}, // GOLD BAR
		{447, 1, 453, 4, 2359, 30}, // MITHRIL ORE
		{449, 1, 453, 6, 2361, 38}, // ADDY ORE
		{451, 1, 453, 8, 2363, 50}, // RUNE ORE
	};

	public static final int MAGIC_ON_GROUNDITEMS = 181;
	public static final int MAGIC_ON_ITEMS = 237;
}
