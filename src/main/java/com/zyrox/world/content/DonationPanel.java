package com.zyrox.world.content;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.zyrox.model.Item;
import com.zyrox.model.definitions.ItemDefinition;
import com.zyrox.world.entity.impl.player.Player;

/**
 * A class that handles the functionality of the donation panel.
 * 
 * @author Blake
 *
 */
public class DonationPanel {
	
	/**
	 * The interface id.
	 */
	private static final int INTERFACE_ID = 50_000;
	
	/**
	 * The item container id.
	 */
	private static final int ITEM_CONTAINER_ID = 50_010;
	
	/**
	 * The cart string id.
	 */
	private static final int CART_STRING_ID = 50075;
	
	/**
	 * The subtotal string id.
	 */
	private static final int SUBTOTAL_STRING_ID = 50007;
	
	/**
	 * The total amount of items.
	 */
	private static final int TOTAL_ITEMS = 9;
	
	/**
	 * The first offer's start string id.
	 */
	private static final int FIRST_OFFER_START_ID = 50078;
	
	/**
	 * The second offer's start string id.
	 */
	private static final int SECOND_OFFER_START_ID = 50090;
	
	/**
	 * The third offer's start string id.
	 */
	private static final int THIRD_OFFER_START_ID = 50102;
	
	/**
	 * The amount string ids.
	 */
	private static final int[] AMOUNT_IDS = new int[] { 50011, 50018, 50025, 50032, 50039, 50046, 50053, 50060, 50067, 50074 };
	
	/**
	 * The default items in the container.
	 */
	private List<CartItem> defaultItems = new ArrayList<CartItem>(Arrays.asList(new CartItem(5, new Item(4151)), new CartItem(25, new Item(1050)), new CartItem(25, new Item(1048)), new CartItem(25, new Item(1046)), new CartItem(25, new Item(1044)), new CartItem(25, new Item(1042))));
	
	/**
	 * The items in the shopping cart.
	 */
	private List<CartItem> shoppingCart = new ArrayList<CartItem>(defaultItems.size());
	
	/**
	 * The player.
	 */
	private Player player;
	
	/**
	 * Constructs a new {@link DonationPanel}.
	 * 
	 * @param player
	 *            the player
	 */
	public DonationPanel(Player player) {
		this.player = player;
	}
	
	/**
	 * Opens the donation panel interface.
	 */
	public void open() {
		reset();
		
		updateCart();
		
		sendPromotions();
		
		player.getPacketSender().sendInterface(INTERFACE_ID);
	}
	
	/**
	 * Resets the shopping cart.
	 */
	private void reset() {
		shoppingCart.clear();
		
		List<Item> containerItems = new ArrayList<Item>(defaultItems.size());
		
		for (int i = 0; i < defaultItems.size(); i++) {
			CartItem cartItem = defaultItems.get(i);
			containerItems.add(i, new Item(cartItem.getItem().getId(), cartItem.getItem().getAmount()));
			shoppingCart.add(i, new CartItem(cartItem.getPrice(), new Item(cartItem.getItem().getId(), 0)));
		}
		
		player.getPacketSender().sendItemContainer(containerItems.toArray(new Item[0]), ITEM_CONTAINER_ID);
	}
	
	/**
	 * Sends the promotions data.
	 */
	private void sendPromotions() {
		int id = FIRST_OFFER_START_ID;
		
		player.getPacketSender().sendString(id++, "+200 Donator Points");
		player.getPacketSender().sendString(id++, "Torva/Pernix/Virtus set");
		player.getPacketSender().sendString(id++, "Vorkath Vine Whip");
		player.getPacketSender().sendString(id++, "10 Diamond Mystery Boxes");
		player.getPacketSender().sendString(id++, "10 Warrior Boxes");
		player.getPacketSender().sendString(id++, "10 Archery Boxes");
		player.getPacketSender().sendString(id++, "");
		player.getPacketSender().sendString(id++, "");
		player.getPacketSender().sendString(id++, "");
		player.getPacketSender().sendString(id++, "");
		
		id = SECOND_OFFER_START_ID;
		
		player.getPacketSender().sendString(id++, "+400 Donator Points");
		player.getPacketSender().sendString(id++, "Torva/Pernix/Virtus set");
		player.getPacketSender().sendString(id++, "1x Flame Gloves");
		player.getPacketSender().sendString(id++, "1x Dragon Tooth Neck");
		player.getPacketSender().sendString(id++, "1x $100 Donation Scroll");
		player.getPacketSender().sendString(id++, "1x Guardian Boots");
		player.getPacketSender().sendString(id++, "20 Warrior Boxes");
		player.getPacketSender().sendString(id++, "20 Archery Boxes");
		player.getPacketSender().sendString(id++, "");
		player.getPacketSender().sendString(id++, "");
		
		id = THIRD_OFFER_START_ID;
		player.getPacketSender().sendString(id++, "+600 Donator Points");
		player.getPacketSender().sendString(id++, "Full Justiciar Set");
		player.getPacketSender().sendString(id++, "1x Ring Of Bosses");
		player.getPacketSender().sendString(id++, "1x Guardian Boots");
		player.getPacketSender().sendString(id++, "30 Warrior Boxes");
		player.getPacketSender().sendString(id++, "30 Archery Boxes");
		player.getPacketSender().sendString(id++, "1x $100 Donation Scroll");
		player.getPacketSender().sendString(id++, "1x $50 Donation Scroll");
		player.getPacketSender().sendString(id++, "");
		player.getPacketSender().sendString(id++, "");
	}

	/**
	 * Handles the buttons of the interface.
	 * 
	 * @param player
	 *            the player
	 * @param buttonId
	 *            the button id
	 * @return <code>true</code> if handled
	 */
	public boolean handleButton(Player player, int buttonId) {
		switch (buttonId) {
		case 50012:
		case 50019:
		case 50026:
		case 50033:
		case 50040:
		case 50047:
		case 50054:
		case 50061:
		case 50058:
			removeFromCart(buttonId);
			return true;
			
		case 50015:
		case 50022:
		case 50029:
		case 50036:
		case 50043:
		case 50050:
		case 50057:
		case 50064:
		case 50071:
			addToCart(buttonId);
			return true;
		}
		
		return false;
	}
	
	/**
	 * Removes an item from the cart.
	 * 
	 * @param buttonId
	 *            the button id
	 */
	private void removeFromCart(int buttonId) {
		int index = buttonId - 50012;
		
		if (index > 0) {
			index -= index / 1.17;
		}
		
		if (index >= defaultItems.size()) {
			return;
		}
		
		CartItem cartItem = shoppingCart.get(index);
		
		cartItem.getItem().decrementAmountBy(1);
		
		updateCart();
	}
	
	/**
	 * Adds an item to the cart.
	 * 
	 * @param buttonId
	 *            the button id
	 */
	private void addToCart(int buttonId) {
		
		int index = buttonId - 50015;
		
		if (index > 0) {
			index -= index / 1.17;
		}
		
		if (index >= defaultItems.size()) {
			return;
		}
		
		Item item = shoppingCart.get(index).getItem();
		
		item.incrementAmountBy(1);
		
		updateCart();
	}
	
	/**
	 * Updates the cart.
	 */
	private void updateCart() {
		StringBuilder sb = new StringBuilder();
		
		double subtotal = 0;
		
		for (int i = 0; i < TOTAL_ITEMS; i++) {
			if (i >= shoppingCart.size()) {
				player.getPacketSender().sendString(AMOUNT_IDS[i], "");
				continue;
			}
			
			CartItem cartItem = shoppingCart.get(i);
			
			player.getPacketSender().sendString(AMOUNT_IDS[i], Integer.toString(cartItem.getItem().getAmount()));
			
			if (cartItem.getItem().getAmount() == 0) {
				continue;
			}
			
			sb.append("@gre@" + (cartItem.getItem().getAmount() + "x " + ItemDefinition.forId(cartItem.getItem().getId()).getName() + ": @whi@" + "$" + (cartItem.getPrice() * cartItem.getItem().getAmount()) + "\\n"));
			subtotal += cartItem.getPrice() * cartItem.getItem().getAmount();
		}
		
		player.getPacketSender().sendString(CART_STRING_ID, sb.toString());
		player.getPacketSender().sendString(SUBTOTAL_STRING_ID, "@or1@Subtotal: @whi@$" + subtotal);
	}
	
	/**
	 * A class that represents a single item in a shopping cart.
	 * 
	 * @author Blake
	 *
	 */
	class CartItem {
		/**
		 * The price of the item.
		 */
		private double price;
		
		/**
		 * The item.
		 */
		private Item item;
		
		/**
		 * Constructs a new {@link CartItem}.
		 * 
		 * @param price
		 *            the price
		 * @param item
		 *            the item
		 */
		CartItem(double price, Item item) {
			this.price = price;
			this.item = item;
		}
		
		/**
		 * Gets the price.
		 * 
		 * @return the price
		 */
		private double getPrice() {
			return price;
		}
		
		/**
		 * Gets the item.
		 * 
		 * @return the item
		 */
		private Item getItem() {
			return item;
		}
	}

}
