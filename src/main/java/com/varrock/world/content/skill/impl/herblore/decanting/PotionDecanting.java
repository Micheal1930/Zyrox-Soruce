package com.varrock.world.content.skill.impl.herblore.decanting;

import com.varrock.model.Item;
import com.varrock.model.definitions.ItemDefinition;
import com.varrock.world.content.Consumables;
import com.varrock.world.content.skill.impl.herblore.PotionCombinating;
import com.varrock.world.entity.impl.player.Player;

/**
 * Handles the action of decanting a potion.
 * 
 * @author Andys1814.
 */
public final class PotionDecanting {

	/**
	 * Represents the empty vial item that will be added upon decanting a
	 * potion.
	 */
	private static final Item EMPTY_VIAL = new Item(229);

	/**
	 * Handles the action of decanting all the items in the player's inventory.
	 * 
	 * @param player
	 *            The player participating in this action.
	 */
	public static void decantPotions(Player player) {
		for (Item item : player.getInventory().getItems()) {
			ItemDefinition definition = ItemDefinition.forId(item.getId());
			if (!Consumables.isPotion(item.getId())) {
				continue;
			}
			DecantablePotion potion = DecantablePotion.forId(item.getId());
			int dose = potion.doseForId(item.getId());
			if (dose == 4) {
				continue;
			}
			int oneDoseAmt = player.getInventory().getAmount(potion.getIds()[3]);
			int twoDoseAmt = player.getInventory().getAmount(potion.getIds()[2]);
			int threeDoseAmt = player.getInventory().getAmount(potion.getIds()[1]);
			System.out.println(oneDoseAmt + ", " + twoDoseAmt + ", " + threeDoseAmt + ", " + item.getId());
			
//			if (dose == 1 && threeDoseAmt > 0) {
//				PotionCombinating.startCombiningPotion(player, potion.getIds()[1], item.getId());
//			}
//			if (dose == 2 && twoDoseAmt > 0) {
//				PotionCombinating.startCombiningPotion(player, potion.getIds()[2], item.getId());
//			}	
			PotionCombinating.startCombiningPotion(player, potion.getIds()[dose], item.getId());

		}
	}
}
