package com.varrock.world.content.combat.magic;

import java.util.Optional;

import com.varrock.model.Animation;
import com.varrock.model.Graphic;
import com.varrock.model.GraphicHeight;
import com.varrock.model.Item;
import com.varrock.model.PlayerRights;
import com.varrock.model.Skill;
import com.varrock.model.Locations.Location;
import com.varrock.world.content.EffectTimer;
import com.varrock.world.entity.impl.GameCharacter;
import com.varrock.world.entity.impl.player.Player;

/**
 * Holds data for all no-combat spells
 * @author Gabriel Hannason
 */
public enum MagicSpells {
	
	BONES_TO_BANANAS(new Spell() {

		@Override
		public int spellId() {
			return 1159;
		}

		@Override
		public int levelRequired() {
			return 15;
		}

		@Override
		public int baseExperience() {
			return 650;
		}

		@Override
		public Optional<Item[]> itemsRequired(Player player) {
			return Optional.of(new Item[] {new Item(561), new Item(555, 2), new Item(557, 2)});
		}

		@Override
		public Optional<Item[]> equipmentRequired(Player player) {
			return Optional.empty();
		}

		@Override
		public void startCast(GameCharacter cast, GameCharacter castOn) {
			
			
		}
		
	}),
	LOW_ALCHEMY(new Spell() {

		@Override
		public int spellId() {
			return 1162;
		}

		@Override
		public int levelRequired() {
			return 21;
		}

		@Override
		public int baseExperience() {
			return 4000;
		}

		@Override
		public Optional<Item[]> itemsRequired(Player player) {
			return Optional.of(new Item[] {new Item(554, 3), new Item(561)});
		}

		@Override
		public Optional<Item[]> equipmentRequired(Player player) {
			return Optional.empty();
		}
		
		@Override
		public void startCast(GameCharacter cast, GameCharacter castOn) {
			
			
		}
		
	}),
	TELEKINETIC_GRAB(new Spell() {

		@Override
		public int spellId() {
			return 1168;
		}

		@Override
		public int levelRequired() {
			return 33;
		}

		@Override
		public int baseExperience() {
			return 3988;
		}

		@Override
		public Optional<Item[]> itemsRequired(Player player) {
			return Optional.of(new Item[] {new Item(563), new Item(556)});
		}

		@Override
		public Optional<Item[]> equipmentRequired(Player player) {
			return Optional.empty();
		}
		
		@Override
		public void startCast(GameCharacter cast, GameCharacter castOn) {
			
			
		}
		
	}),
	SUPERHEAT_ITEM(new Spell() {

		@Override
		public int spellId() {
			return 1173;
		}

		@Override
		public int levelRequired() {
			return 43;
		}

		@Override
		public int baseExperience() {
			return 6544;
		}

		@Override
		public Optional<Item[]> itemsRequired(Player player) {
			return Optional.of(new Item[] {new Item(554, 4), new Item(561)});
		}

		@Override
		public Optional<Item[]> equipmentRequired(Player player) {
			return Optional.empty();
		}
		
		@Override
		public void startCast(GameCharacter cast, GameCharacter castOn) {
			
			
		}
		
	}),
	HIGH_ALCHEMY(new Spell() {

		@Override
		public int spellId() {
			return 1178;
		}

		@Override
		public int levelRequired() {
			return 55;
		}

		@Override
		public int baseExperience() {
			return 20000;
		}

		@Override
		public Optional<Item[]> itemsRequired(Player player) {
			return Optional.of(new Item[] {new Item(554, 5), new Item(561)});
		}

		@Override
		public Optional<Item[]> equipmentRequired(Player player) {
			return Optional.empty();
		}

		@Override
		public void startCast(GameCharacter cast, GameCharacter castOn) {
			
			
		}
		
	}),
	BONES_TO_PEACHES(new Spell() {

		@Override
		public int spellId() {
			return 15877;
		}

		@Override
		public int levelRequired() {
			return 60;
		}

		@Override
		public int baseExperience() {
			return 4121;
		}

		@Override
		public Optional<Item[]> itemsRequired(Player player) {
			return Optional.of(new Item[] {new Item(561, 2), new Item(555, 4), new Item(557, 4)});
		}

		@Override
		public Optional<Item[]> equipmentRequired(Player player) {
			return Optional.empty();
		}

		@Override
		public void startCast(GameCharacter cast, GameCharacter castOn) {
			
			
		}
		
	}),
	BAKE_PIE(new Spell() {

		@Override
		public int spellId() {
			return 30017;
		}

		@Override
		public int levelRequired() {
			return 65;
		}

		@Override
		public int baseExperience() {
			return 5121;
		}

		@Override
		public Optional<Item[]> itemsRequired(Player player) {
			return Optional.of(new Item[] {new Item(9075, 1), new Item(554, 5), new Item(555, 4)});
		}

		@Override
		public Optional<Item[]> equipmentRequired(Player player) {
			
			return Optional.empty();
		}

		@Override
		public void startCast(GameCharacter cast, GameCharacter castOn) {
			
			
		}
	}),
	VENGEANCE_OTHER(new Spell() {

		@Override
		public int spellId() {
			return 30298;
		}

		@Override
		public int levelRequired() {
			return 93;
		}

		@Override
		public int baseExperience() {
			return 10000;
		}

		@Override
		public Optional<Item[]> itemsRequired(Player player) {
			return Optional.of(new Item[] {new Item(9075, 3), new Item(557, 10), new Item(560, 2)});
		}

		@Override
		public Optional<Item[]> equipmentRequired(Player player) {
			return Optional.empty();
		}

		@Override
		public void startCast(GameCharacter cast, GameCharacter castOn) {
			
			
		}
	}),
	VENGEANCE(new Spell() {

		@Override
		public int spellId() {
			return 30306;
		}

		@Override
		public int levelRequired() {
			return 94;
		}

		@Override
		public int baseExperience() {
			return 14000;
		}

		@Override
		public Optional<Item[]> itemsRequired(Player player) {
			return Optional.of(new Item[]{new Item(9075, 4), new Item(557, 10), new Item(560, 2)});
		}

		@Override
		public Optional<Item[]> equipmentRequired(Player player) {
			return Optional.empty();
		}

		@Override
		public void startCast(GameCharacter cast, GameCharacter castOn) {
			
			
		}
	}),
	CURE_ME(new Spell() {

		@Override
		public int spellId() {
			return 30091;
		}

		@Override
		public int levelRequired() {
			return 71;
		}

		@Override
		public int baseExperience() {
			return 6250;
		}

		@Override
		public Optional<Item[]> itemsRequired(Player player) {
			return Optional.of(new Item[]{new Item(564, 2), new Item(9075, 2), new Item(563, 1)});
		}

		@Override
		public Optional<Item[]> equipmentRequired(Player player) {
			return Optional.empty();
		}

		@Override
		public void startCast(GameCharacter cast, GameCharacter castOn) {
			
			
		}
	}),

	// ENCHANTS

	SAPPHIRE(new Spell() {

		@Override
		public int spellId() {
			return 1155;
		}

		@Override
		public int levelRequired() {
			return 7;
		}

		@Override
		public Optional<Item[]> itemsRequired(Player player) {
			return Optional.of(new Item[] { new Item(WATER, 1), new Item(COSMIC, 1) });
		}

		@Override
		public Optional<Item[]> equipmentRequired(Player player) {
			return Optional.empty();
		}

		@Override
		public void startCast(GameCharacter cast, GameCharacter castOn) {

		}

		@Override
		public int baseExperience() {
			return 0;
		}

	}), EMERALD(new Spell() {

		@Override
		public int spellId() {
			return 1165;
		}

		@Override
		public int levelRequired() {
			return 27;
		}

		@Override
		public Optional<Item[]> itemsRequired(Player player) {
			return Optional.of(new Item[] { new Item(AIR, 3), new Item(COSMIC, 1) });
		}

		@Override
		public Optional<Item[]> equipmentRequired(Player player) {
			return Optional.empty();
		}

		@Override
		public void startCast(GameCharacter cast, GameCharacter castOn) {

		}

		@Override
		public int baseExperience() {
			return 0;
		}
	}), RUBY(new Spell() {

		@Override
		public int spellId() {
			return 1176;
		}

		@Override
		public int levelRequired() {
			return 49;
		}

		@Override
		public Optional<Item[]> itemsRequired(Player player) {
			return Optional.of(new Item[] { new Item(FIRE, 5), new Item(COSMIC, 1) });
		}

		@Override
		public Optional<Item[]> equipmentRequired(Player player) {
			return Optional.empty();
		}

		@Override
		public void startCast(GameCharacter cast, GameCharacter castOn) {

		}

		@Override
		public int baseExperience() {
			return 0;
		}
	}), DIAMOND(new Spell() {

		@Override
		public int spellId() {
			return 1180;
		}

		@Override
		public int levelRequired() {
			return 57;
		}

		@Override
		public Optional<Item[]> itemsRequired(Player player) {
			return Optional.of(new Item[] { new Item(EARTH, 10), new Item(COSMIC, 1) });
		}

		@Override
		public Optional<Item[]> equipmentRequired(Player player) {
			return Optional.empty();
		}

		@Override
		public void startCast(GameCharacter cast, GameCharacter castOn) {

		}

		@Override
		public int baseExperience() {
			return 0;
		}
	}), DRAGONSTONE(new Spell() {

		@Override
		public int spellId() {
			return 1187;
		}

		@Override
		public int levelRequired() {
			return 68;
		}

		@Override
		public Optional<Item[]> itemsRequired(Player player) {
			return Optional.of(new Item[] { new Item(WATER, 15), new Item(EARTH, 15), new Item(COSMIC, 1) });
		}

		@Override
		public Optional<Item[]> equipmentRequired(Player player) {
			return Optional.empty();
		}

		@Override
		public void startCast(GameCharacter cast, GameCharacter castOn) {

		}

		@Override
		public int baseExperience() {
			return 0;
		}
	}), ONYX(new Spell() {

		@Override
		public int spellId() {
			return 6003;
		}

		@Override
		public int levelRequired() {
			return 87;
		}

		@Override
		public Optional<Item[]> itemsRequired(Player player) {
			return Optional.of(new Item[] { new Item(EARTH, 20), new Item(FIRE, 20), new Item(COSMIC, 1) });
		}

		@Override
		public Optional<Item[]> equipmentRequired(Player player) {
			return Optional.empty();
		}

		@Override
		public void startCast(GameCharacter cast, GameCharacter castOn) {

		}

		@Override
		public int baseExperience() {
			return 0;
		}
	})
	;
	
	MagicSpells(Spell spell) {
		this.spell = spell;
	}
	
	private Spell spell;
	
	public Spell getSpell() {
		return spell;
	}
	
	public static MagicSpells forSpellId(int spellId) {
		for(MagicSpells spells : MagicSpells.values()) {
			if(spells.getSpell().spellId() == spellId)
				return spells;
		}
		return null;
	}
	
	@SuppressWarnings("incomplete-switch")
	public static boolean handleMagicSpells(Player p, int buttonId) {
		MagicSpells spell = forSpellId(buttonId);
		if(!spell.getSpell().canCast(p, false))
			return true;
		switch(spell) {
		case BONES_TO_PEACHES:
		case BONES_TO_BANANAS:
			Item item = new Item(526);
			String sa = !item.getDefinition().getName().endsWith("s") ? "s" : "";
			if(!p.getInventory().contains(item.getId())) {
				p.getPacketSender().sendMessage("You do not have any "+item.getDefinition().getName()+""+sa+" in your inventory.");
				return true;
			}
			p.getInventory().delete(557, spell == BONES_TO_PEACHES ? 4 : 2).delete(555, spell == BONES_TO_PEACHES ? 4 : 2).delete(561, spell == BONES_TO_PEACHES ? 2 : 1);
			int i = 0;
			for(Item invItem : p.getInventory().getValidItems()) {
				if(invItem.getId() == item.getId()) {
					p.getInventory().delete(item.getId(), 1).add(spell == BONES_TO_PEACHES ? 6883 : 1963, 1, "Magic spells");
					i++;
				}
			}
			p.performGraphic(new Graphic(141, GraphicHeight.MIDDLE));
			p.performAnimation(new Animation(722));
			p.getSkillManager().addExperience(Skill.MAGIC, spell.getSpell().baseExperience() * i);
			break;
		case VENGEANCE:
			if (!p.getLocation().isAidingAllowed() || p.getLocation() == Location.DUEL_ARENA) {
				p.getPacketSender().sendMessage("This spell cannot be cast here.");
				return true;
			}
			
			if (p.hasVengeance()) {
				p.getPacketSender().sendMessage("You already have Vengeance's effect.");
				return true;
			}
			
			double seconds = 30;
			
			if (PlayerRights.isExecutiveDonator(p)) {
				seconds = 20;
			} else if (PlayerRights.isPlatinumDonator(p)) {
				seconds = 25;
			} else if (PlayerRights.isExtremeDonator(p)) {
				seconds = 27.5;
			}
			
			if (!p.getLastVengeance().elapsed((long) (seconds * 1000))) {
				p.getPacketSender().sendMessage("You must wait " + seconds + " seconds until you can cast vengance!");
				return true;
			}
			
			p.getPacketSender().sendEffectTimer(30, EffectTimer.VENGEANCE);
			p.getInventory().deleteItemSet(spell.getSpell().itemsRequired(p));
			p.performAnimation(new Animation(4410));
			p.performGraphic(new Graphic(726, GraphicHeight.HIGH));
			p.getLastVengeance().reset();
			p.setHasVengeance(true);
			break;
		case CURE_ME:
			double timer = 7;
			if (p.isPoisoned() || p.isVenomed()) {
				if (!p.getLastCure().elapsed((long) (timer * 1000))) {
					p.getPacketSender().sendMessage("You Have Casted Cure Me Too Recently please wait "
							+ (timer - (p.getLastCure().elapsed() / 1000)) + " more seconds to try again!");
					return true;
				}
				p.getPacketSender().sendMessage("your " + (p.isVenomed() ? "venom " : "poison ") + " has been cured");
				p.setPoisonDamage(0);
				p.setVenomDamage(0);
				p.getInventory().deleteItemSet(spell.getSpell().itemsRequired(p));
				p.performAnimation(new Animation(4411));
				p.performGraphic(new Graphic(729, GraphicHeight.HIGH));
				p.getLastCure().reset();
			} else
				p.getPacketSender().sendMessage("You are not poisoned there is no need to cast this spell");
			break;
		}
		return true;
	}

	private final static int ASTRAL = 9075;
	private final static int DEATH = 560;
	private final static int BLOOD = 565;
	private final static int WRATH = 51880;
	private final static int WATER = 555;
	private final static int AIR = 556;
	private final static int EARTH = 557;
	private final static int FIRE = 554;
	private final static int MIND = 558;

	private final static int NATURE = 561;
	private final static int COSMIC = 564;
	private final static int CHAOS = 562;
	private final static int LAW = 563;
	private final static int SOUL = 566;
}
