package com.varrock.world.content;

import com.varrock.model.Flag;
import com.varrock.model.Item;
import com.varrock.model.container.impl.Equipment;
import com.varrock.model.definitions.ItemDefinition;
import com.varrock.world.entity.impl.player.Player;

public class ItemDegrading {

	public static boolean handleItemDegrading(Player p, DegradingItem d) {
		int equipId = p.getEquipment().getItems()[d.equipSlot].getId();
		if(equipId == d.nonDeg || equipId == d.deg) {
			int maxCharges = d.degradingCharges;
			int currentCharges = getAndIncrementCharge(p, d, false);
			boolean degradeCompletely = currentCharges >= maxCharges;
			if(equipId == d.deg && !degradeCompletely) {
				return true;
			}
			degradeCompletely = degradeCompletely && equipId == d.deg;
			p.getEquipment().setItem(d.equipSlot, new Item(degradeCompletely ? - 1 : d.deg)).refreshItems();
			getAndIncrementCharge(p, d, true);
			p.getUpdateFlag().flag(Flag.APPEARANCE);
			String ext = !degradeCompletely ? "degraded slightly" : "turned into dust";
			p.getPacketSender().sendMessage("Your "+ItemDefinition.forId(equipId).getName().replace(" (deg)", "")+" has "+ext+"!");
			return true;
		} else {
			return false;
		}
	}

	public static int getAndIncrementCharge(Player p, DegradingItem d, boolean reset) {
		switch(d) {
		case BRAWLING_GLOVES_COOKING:
		case BRAWLING_GLOVES_FIREMAKING:
		case BRAWLING_GLOVES_FISHING:
		case BRAWLING_GLOVES_HUNTER:
		case BRAWLING_GLOVES_MINING:
		case BRAWLING_GLOVES_PRAYER:
		case BRAWLING_GLOVES_SMITHING:
		case BRAWLING_GLOVES_THIEVING:
		case BRAWLING_GLOVES_WOODCUTTING:
			int index = d.ordinal() - 1;
			if(reset) {
				return p.getBrawlerChargers()[index] = 0;
			} else {
				return p.getBrawlerChargers()[index]++;
			}
		case RING_OF_RECOIL:
			if(reset) {
				return p.setRecoilCharges(0);
			} else {
				return p.setRecoilCharges(p.getRecoilCharges() + 1);
			}
        case RING_OF_FORGING:
            if(reset) {
                return p.setForgingCharges(0);
            } else {
                return p.setForgingCharges(p.getForgingCharges() + 1);
            }
		}
		return d.degradingCharges;
	}

	/*
	 * The enum holding all degradeable equipment
	 */
	public enum DegradingItem {

		/*
		 * Recoil
		 */
		RING_OF_RECOIL(2550, 2550, Equipment.RING_SLOT, 100),

        RING_OF_FORGING(2568, 2568, Equipment.RING_SLOT, 140),
		/*
		 * Statius's equipment
		 *
		CORRUPT_STATIUS_FULL_HELM(13920, 13922, Equipment.HEAD_SLOT, 200),
		CORRUPT_STATIUS_PLATEBODY(13908, 13910, Equipment.BODY_SLOT, 200),
		CORRUPT_STATIUS_PLATELEGS(13914, 13916, Equipment.LEG_SLOT, 200),
		CORRUPT_STATIUS_WARHAMMER(13926, 13928, Equipment.WEAPON_SLOT, 200),

		/*
		 * Vesta's equipment
		 *
		CORRUPT_VESTAS_CHAINBODY(13911, 13913, Equipment.BODY_SLOT, 200),
		CORRUPT_VESTAS_PLATESKIRT(13917, 13919, Equipment.LEG_SLOT, 200),
		CORRUPT_VESTAS_LONGSWORD(13923, 13925, Equipment.WEAPON_SLOT, 160),
		CORRUPT_VESTAS_SPEAR(13929, 13931, Equipment.WEAPON_SLOT, 160),

		/*
		 * Zuriel's equipment
		 *
		CORRUPT_ZURIELS_HOOD(13938, 13940, Equipment.HEAD_SLOT, 200),
		CORRUPT_ZURIELS_ROBE_TOP(13932, 13934, Equipment.BODY_SLOT, 200),
		CORRUPT_ZURIELS_ROBE_BOTTOM(13935, 13937, Equipment.LEG_SLOT, 200),
		CORRUPT_ZURIELS_STAFF(13941, 13943, Equipment.WEAPON_SLOT, 200),

		/*
		 * Morrigan's equipment
		 *
		CORRUPT_MORRIGANS_COIF(13950, 13952, Equipment.HEAD_SLOT, 200),
		CORRUPT_MORRIGANS_LEATHER_BODY(13944, 13946, Equipment.BODY_SLOT, 200),
		CORRUPT_MORRIGANS_LEATHER_CHAPS(13944, 13946, Equipment.LEG_SLOT, 200),
		 */
		/*
		 * Brawling gloves
		 */
		BRAWLING_GLOVES_SMITHING(13855, 13855, Equipment.HANDS_SLOT, 600),
		BRAWLING_GLOVES_PRAYER(13848, 13848, Equipment.HANDS_SLOT, 600),
		BRAWLING_GLOVES_COOKING(13857, 13857, Equipment.HANDS_SLOT, 600),
		BRAWLING_GLOVES_FISHING(13856, 13856, Equipment.HANDS_SLOT, 600),
		BRAWLING_GLOVES_THIEVING(13854, 13854, Equipment.HANDS_SLOT, 600),
		BRAWLING_GLOVES_HUNTER(13853, 13853, Equipment.HANDS_SLOT, 600),
		BRAWLING_GLOVES_MINING(13852, 13852, Equipment.HANDS_SLOT, 600),
		BRAWLING_GLOVES_FIREMAKING(13851, 13851, Equipment.HANDS_SLOT, 600),
		BRAWLING_GLOVES_WOODCUTTING(13850, 13850, Equipment.HANDS_SLOT, 600);

		DegradingItem(int nonDeg, int deg, int equipSlot, int degradingCharges) {
			this.nonDeg = nonDeg;
			this.deg = deg;
			this.equipSlot = equipSlot;
			this.degradingCharges = degradingCharges;
		}

		private int nonDeg, deg;
		private int equipSlot;
		private int degradingCharges;
		
		public static DegradingItem forNonDeg(int item) {
			for(DegradingItem d : DegradingItem.values()) {
				if(d.nonDeg == item) {
					return d;
				}
			}
			return null;
		}
	}
}
