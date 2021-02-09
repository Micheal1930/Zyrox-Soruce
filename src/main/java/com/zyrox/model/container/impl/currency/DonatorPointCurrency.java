package com.zyrox.model.container.impl.currency;

import com.zyrox.model.GameMode;
import com.zyrox.model.Item;
import com.zyrox.model.PlayerRights;
import com.zyrox.model.container.impl.Shop;
import com.zyrox.model.definitions.ItemDefinition;
import com.zyrox.util.Misc;
import com.zyrox.world.World;
import com.zyrox.world.entity.impl.player.Player;

/**
 * An implementation of {@link org.niobe.model.container.impl.Shop.Currency}
 * that represents an {@link org.niobe.model.GameCharacter#getPlayerFields()#getBossKillPoints()}
 * currency for shops.
 *
 * @author relex lawl
 */
public final class DonatorPointCurrency implements Shop.Currency {

	@Override
	public String getName() {
		return "Donator points";
	}

	@Override
	public int getAmount(Player player) {
		return player.getPointsHandler().getDonationPoints();
	}

	@Override
	public void delete(Player player, int amount) {
		System.out.println("used shop1");
		if (amount > 0) {
			int delete = getAmount(player) - amount;
			int points = player.getPointsHandler().getDonationPoints();
			player.getPointsHandler().setDonationPoints(delete, false);
			int pointsAfter = points - player.getPointsHandler().getDonationPoints();
			player.incrementAmountDonated(pointsAfter);
			System.out.println("spend "+pointsAfter);
			checkForRankUpdate(player);
			player.getPacketSender().sendMessage("You now have " + Misc.insertCommasToNumber(delete) + " " + getName() + ".");
		}
	}
	
	public static void checkForRankUpdate(Player player) {
		if (player.getRights().isStaff()) {
			return;
		}
		if (player.getGameMode() == GameMode.IRONMAN || player.getGameMode() == GameMode.ULTIMATE_IRONMAN || player.getGameMode() == GameMode.HARDCORE_IRONMAN) {
			player.getPacketSender().sendMessage("@red@You did not receive donator rank because you are an iron man!");
			return;
		}

		PlayerRights rights = PlayerRights.getDonatorRights(player);

		if (rights != null && rights != player.getRights()) {
			player.getPacketSender().sendMessage("You've become a " + Misc.formatText(rights.toString().toLowerCase()) + "! Congratulations! You have spend totally "+player.getAmountDonated());
			player.setRights(rights);
			player.getPacketSender().sendRights();
			player.save();
			World.sendMessage("<img=678> <shad=d4af37>"+player.getUsername() + " has upgraded their donator rank to " +
					rights.getYellPrefix() + rights.toString() + "</col></shad>!");

		}

	}

	@Override
	public void add(Player player, int amount) {//you are on localhost ? or have to upload it to server ?
		if (amount > 0) {
			int add = getAmount(player) + amount;
			player.getPointsHandler().setDonationPoints(add, false);
			player.getPacketSender().sendMessage("You now have " +  Misc.insertCommasToNumber(add) + " " + getName() + ".");
		}
	}

	@Override
	public int getSellPrice(Item item) {
		return getPointPrice(item);
	}
	
	@Override
	public int getBuyPrice(Item item) {
		return 0;
	}
	
	@Override
	public int getSpriteId() {
		return Shop.VOTING_SPRITE_ID;
	}
	
	/**
	 * Gets the point price for said {@link item}.
	 * @param item	The {@link org.niobe.model.Item} to get special price for.
	 * @return		The points needed in order to purchase said item.
	 */
	private static int getPointPrice(Item item) {
		final String name = ItemDefinition.forId(item.getId()).getName().toLowerCase();
		for (String[] value : POINT_VALUE) {
			if (name.contains(value[0])) {
				return Integer.valueOf(value[1]);
			}
			if (item.getId() == 6199) {
				return 3;
			}
			if(item.getId() == 11288 | item.getId() == 11292 | item.getId() == 11289 | item.getId() == 11291) {
				
				return 100;
			}
		}
		return 2147000000;
	}
	
	private static final String[][] POINT_VALUE = {
		
		{ "overload", "1" },
		{ "super combat", "1" },
		{ "emerald rapier", "5" },
		{ "fire cape", "5" },
		{ "tokhaar", "12" },
		{ "eye of the", "250" },
		{ "Mystery box", "3" },
		{ "super mystery box", "15" },
		{ "ultimate mystery box", "35" },
		{ "Ultimate Mystery box", "35" },
		{ "Ultimate mystery box", "35" },
		{ "Ultimate Mystery Box", "35" },


		{ "charm box", "4" },
		{ "gnome scarf", "1" },
		{ "fishbowl", "1" },
		{ "diving", "1" },
		{ "top hat", "1" },
		{ "flipper", "1" },
		{ "greegree", "25" },
		{ "halo", "75" },
		{ "infinity", "300" },
		{ "Donating hat", "100" },
		{ "fury", "4" },
		{ "dragon boots", "4" },
		{ "staff of light", "25" },
	
		{ "bandos chestplate", "10" },
		{ "bandos tassets", "10" },
		
		{ "armadyl helmet", "5" },
		{ "armadyl chestplate", "10" },
		{ "armadyl chainskirt", "10" },
		{ "dragonfire shield", "5" },
		{ "dragon kiteshield", "30" },
		{ "divine", "50" },
		{ "elysian", "45" },
		{ "arcane", "40" },
		{ "spectral", "35" },
		
		{ "steadfast boots", "15" },
		{ "ragefire boots", "15" },
		{ "glaiven boots", "15" },
		
		{ "dragon claws", "35" },
		{ "korasi's sword", "45" },
		{ "elder maul", "35" },
		{ "armadyl godsword", "40" },
		{ "vine whip", "60" },
		{ "volcanic abyssal whip", "40" },
		{ "heavy ballista", "30" },
		{ "abyssal tentacle", "30" },
		{ "hand cannon", "25" },
		{ "dragon hunter crossbow", "20" },
		{ "armadyl crossbow", "40" },
		{ "toxic staff", "35" },
		{ "toxic blowpipe", "50" },
		{ "zaryte bow", "30" },
		{ "ring of the gods", "20" },
		{ "necklace of anguish", "20" },

		{ "black party hat", "120" },
		{ "lime party hat", "120" },
		{ "pink party hat", "120" },
		{ "lava party hat", "120" },
		{ "black h'ween mask", "100" },
		{ "Black h'ween mask", "100" },

		{ "lime h'ween mask", "100" },
		{ "pink h'ween mask", "100" },
		{ "lava h'ween mask", "100" },
		{ "black santa hat", "100" },
		{ "pink santa hat", "100" },
		{ "lava santa hat", "100" },
		{ "lime santa hat", "100" },
		{ "sled", "80" },
		{ "grain", "70" },
		{ "flaming skull", "125" },


		{ "primordial boots", "20" },
		{ "pegasian boots", "20" },
		{ "eternal boots", "20" },
		{ "amulet of torture", "20" },
		{ "blood necklace", "15" },

		{ "scythe", "75" },

		{ "lvl1", "115" },
		{ "lvl 1", "115" },
		{ "scythe", "75" },
		{ "scythe", "75" },

		{ "vesta's chainbody", "40" },
		{ "vesta's plateskirt", "40" },
		
		{ "serpentine helm", "35" },
		{ "vesta's chainbody", "75" },

		{ "vesta's longsword", "30" },
		
		{ "statius's full helm", "10" },
		{ "statius's platebody", "35" },
		{ "statius's platelegs", "35" },


		
		{ "$10 scroll", "10" },
		{ "$20 scroll", "20" },
		{ "$50 scroll", "50" },
		{ "$100 scroll", "100" },
		{ "rune pouch", "5" },
		{ "ring of wealth", "3" },
		{ "charming imp", "7" },

		{ "torva full helm", "40" },
		{ "torva platebody", "50" },
		{ "torva platelegs", "50" },

		{ "pernix cowl", "35" },
		{ "pernix body", "45" },
		{ "pernix chaps", "45" },
		
		{ "virtus mask", "35" },
		{ "virtus robe top", "45" },
		{ "virtus robe legs", "45" },

		{ "ghrazi rapier", "80" },
		{ "twisted bow", "80" },
		{ "scythe Of Vitur", "80" },
		{ "Scythe Of Vitur", "80" },
		{ "scythe of vitur", "80" },
		{ "Scythe of Vitur", "80" },

		{ "partyhat", "80" },
		{ "h'ween mask", "70" },
		{ "santa hat", "70" },































		
		
	};
}
