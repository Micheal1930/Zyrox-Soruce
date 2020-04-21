package com.varrock.model.container.impl;

import com.varrock.model.GameMode;
import com.varrock.model.Item;
import com.varrock.model.container.ItemContainer;
import com.varrock.model.container.StackType;
import com.varrock.model.container.impl.currency.CoinCurrency;
import com.varrock.model.container.impl.currency.DungeoneeringCoinCurrency;
import com.varrock.model.definitions.ItemDefinition;
import com.varrock.model.log.impl.ShopPurchaseLog;
import com.varrock.util.Misc;
import com.varrock.world.World;
import com.varrock.world.content.shop.ShopManager;
import com.varrock.world.entity.impl.player.Player;

/**
 * An implementation of {@link org.niobe.model.container.ItemContainer}
 * in which players can purchase or sell {@link org.niobe.model.Item}.
 *
 * @author relex lawl
 */
public class Shop extends ItemContainer {

	/**
	 * The Shop constructor.
	 * @param player	The player viewing the shop.
	 * @param id		The unique shop id to retrieve from {@link org.niobe.world.content.ShopManager}'s shop map.
	 * @param name		The name that will appear as the title on the shop interface.
	 * @param currency	The {@link org.niobe.model.Item} used as currency in the shop.
	 * @param stock		All of the {@link org.niobe.model.Item}s that can be purchased or sold to the shop.
	 */
	public Shop(Player player, int id, String name, Currency currency, Item[] stock) {
		super(player);
		if (stock.length > capacity())
			throw new ArrayIndexOutOfBoundsException("Stock cannot have more than " + capacity() + " items; check shop[" + id + "]: stockLength: " + stock.length);
		this.id = id;
		this.name = name != null && name.length() > 0 ? name : "General Store";
		this.stock = stock;
		this.currency = currency;
		if (id == 21 || id == 44) {
			this.type = ShopType.BUY_ANYTHING;
		/*} else if (id == 38) {
			this.type = ShopType.BUY_ANYTHING_NO_SALE;*/
		} else {
			this.type = ShopType.NORMAL;
		}
		for (Item item : stock) {
			if (item != null)
				add(item.copy(), false);
		}
	}
	
	/**
	 * The unique shop id.
	 */
	private final int id;
	
	/**
	 * The name of the shop used as the title
	 * in the shop interface.
	 */
	private String name;
	
	/**
	 * The currency the shop is receiving in exchange
	 * for goods.
	 */
	private Currency currency;
	
	/**
	 * The stock the shop currently has.
	 */
	private Item[] stock;
	
	/**
	 * The {@link ShopType} value.
	 */
	private final ShopType type;
	
	/**
	 * This flag is used for personal shops.
	 */
	private boolean personal;
	
	public int getId() {
		return id;
	}

	/**
	 * Gets the shop name used as the title.
	 * @return	The name of the shop.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Sets the shop name.
	 * @param name	The name of the shop.
	 * @return		The Shop instance.
	 */
	public Shop setName(String name) {
		this.name = name;
		return this;
	}
	
	/**
	 * Gets the {@link Currency} that is used as the
	 * {@link Shop} trade currency.
	 * @return	The {@link #currency} value.
	 */
	public Currency getCurrency() {
		return currency;
	}
	
	/**
	 * Sets the {@link Currency}.
	 * @param currency	The {@link Currency} value.
	 * @return			The Shop instance.
	 */
	public Shop setCurrency(Currency currency) {
		this.currency = currency;
		return this;
	}
	
	/**
	 * Gets the {@link Shop} stock {@link org.niobe.model.Item}s.
	 * @return	The {@link #stock} array.
	 */
	public Item[] getStock() {
		return stock;
	}
	
	/**
	 * Sets the {@link Shop}'s {@link org.niobe.model.Item}s
	 * for sale.
	 * @param stock	The array containing the {@link org.niobe.model.Item}s.
	 * @return		The Shop instance.
	 */
	public Shop setStock(Item[] stock) {
		this.stock = stock;
		return this;
	}
	
	/**
	 * Gets the {@link #type} value.
	 * @return	The {@link #type}.
	 */
	public ShopType getType() {
		return type;
	}
	
	public void setPersonal(boolean personal) {
		this.personal = personal;
	}
	
	public boolean isPersonal() {
		return personal;
	}
	
	public boolean deleteOnOutOfStock() {
		return false;
	}
	
	/**
	 * Opens a {@link Shop} interface for said
	 * {@link org.niobe.world.Player}.
	 * @param player	The {@link org.niobe.world.Player} to open {@link Shop} for.
	 * @return			The Shop instance.
	 */
	public Shop open(Player player) {
		player.getPacketSender().sendInterfaceActions(false);
		updateCurrencyOnInterface(player);
		player.setShop(ShopManager.getShops().get(id)).setInterfaceId(INTERFACE_ID);

		player.getPacketSender().sendString(NAME_INTERFACE_CHILD_ID, name);
		player.getPacketSender().sendItemContainer(this, ITEM_CHILD_ID);
		player.getPacketSender().sendInterfaceSet(INTERFACE_ID, INVENTORY_INTERFACE_ID - 1);
		player.getPacketSender().sendItemContainer(player.getInventory(), INVENTORY_INTERFACE_ID);

		return this;
	}

	public void updateCurrencyOnInterface(Player player) {
		long currentAmount = this.getCurrency().getAmount(player);
		String currencyName = this.getCurrency().getName();

		if(this.getCurrency() instanceof CoinCurrency) {
		    currentAmount += player.getMoneyInPouch();
        }

		player.getPacketSender().sendString(50840, "You currently have "+ Misc.insertCommasToNumber(currentAmount)+" "+currencyName);
	}
	
	/**
	 * Checks if the {@link org.niobe.model.Item} can be
	 * sold to this {@link Shop}.
	 * @param item	The {@link org.niobe.model.Item} to sell.
	 * @return		{@code true} if this {@link Shop} sells/buys the {@link org.niobe.model.Item}.
	 */
	public boolean buys(Item item) {
		if ((type == ShopType.BUY_ANYTHING || type == ShopType.BUY_ANYTHING_NO_SALE)
				&& item.getId() != 995 
				&& item.tradeable(getPlayer()) && item.sellable())
			return true;
		for (Item containerItem : getItems()) {
			if (containerItem != null && 
					(containerItem.getId() == item.getId() || ItemDefinition.forId(item.getId()).isNoted() && containerItem.getId() + 1 == item.getId())) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Handles the reload of the {@link Shop#getItems()}
	 * for every {@link org.niobe.world.Player} currently viewing
	 * this {@link Shop}.
	 */
	public void publicRefresh() {
		if (isPersonal())
			return;
		Shop publicShop = ShopManager.getShops().get(id);
		if (publicShop == null)
			return;
		publicShop.setItems(getItems());
		for (Player player : World.getPlayers()) {
			if (player != null && player.getShop() != null &&
					player.getShop().id == id) {
				player.getShop().setItems(publicShop.getItems());
				player.getPacketSender().sendItemContainer(this, ITEM_CHILD_ID);
				player.getPacketSender().sendItemContainer(player.getInventory(), INVENTORY_INTERFACE_ID);
			}
		}
	}
	
	/**
	 * Handles shop being restocked with their
	 * original {@link stock} item array.
	 */
	public void restock() {
		if (isPersonal())
			return;

		for (int i = 0; i < capacity(); i++) {

			Item stockItem = i < stock.length ? stock[i].copy() : null;
			Item actualItem = i < getItems().length ? getItems()[i].copy() : null;

			//removing items from shops that don't belong
			if(stockItem == null && actualItem != null) {
				delete(actualItem.getId(), 1);
				continue;
			}

			if(stockItem != null && actualItem != null) {
				if(actualItem.getAmount() < stockItem.getAmount()) {
					add(actualItem.getId(), 1);
				}
			}
		}

		refreshItems();
	}
	
	public enum ShopType {
		NORMAL,
		
		BUY_ANYTHING,
		
		BUY_ANYTHING_NO_SALE;
	}
	
	@Override
	public Shop switchItemShops(ItemContainer to, Item item, int slot, boolean sort, boolean refresh) {
		if (item.getAmount() <= 0)
			return this;

		if (item.getId() == 50792
				&& to.getPlayer().getGameMode() != GameMode.HARDCORE_IRONMAN) {
			to.getPlayer().sendMessage("You need to be a Hardcore Iron Man to buy this.");
			return this;
		} else if (item.getId() == 50794
				&& to.getPlayer().getGameMode() != GameMode.HARDCORE_IRONMAN) {
			to.getPlayer().sendMessage("You need to be a Hardcore Iron Man to buy this.");
			return this;
		} else if (item.getId() == 50796
				&& to.getPlayer().getGameMode() != GameMode.HARDCORE_IRONMAN) {
			to.getPlayer().sendMessage("You need to be a Hardcore Iron Man to buy this.");
			return this;
		}

		int[][] rfdGloves = new int[][] {
				new int[] {7458, 4},
				new int[] {7459, 5},
				new int[] {7460, 6},
				new int[] {7461, 7},
				new int[] {7462, 8},
		};

		for(int[] rfdGlove : rfdGloves) {
			int gloveId = rfdGlove[0];
			int partId = rfdGlove[1];
			if(item.getId() == gloveId) {
				if(!to.getPlayer().getMinigameAttributes().getRecipeForDisasterAttributes().hasFinishedPart(partId)) {
					to.getPlayer().sendMessage("You have not progressed enough in Recipe For Disaster to purchase this.");
					return this;
				}
			}
		}
		
		int freeSlots = to.getFreeSlots();
		int slots_required = to.stackType() == StackType.STACKS ||
								item.getDefinition().isStackable() ? 1 : item.getAmount();
		
		if (freeSlots < slots_required
				&& !(to.contains(item.getId()) && item.getDefinition().isStackable())) {
			if (!to.hasEmptySlot()) {
				to.full();
				return this;
			} else {
				item.setAmount(freeSlots);
				if (freeSlots <= 0)
					return this;
			}
		}
		
		if (item.getAmount() > getItems()[slot].getAmount()) {
			item.setAmount(getItems()[slot].getAmount());
		}

		if (item.getAmount() <= 0)
			return this;


		//if (to.getPlayer().getRights() != PlayerRights.OWNER) {
			long value = item.getAmount() * currency.getSellPrice(item);
			if (value > Integer.MAX_VALUE)
				value = Integer.MAX_VALUE;


			long playerCurrencyAmount = 0;
			if (!currency.getName().equals("coins") || currency instanceof DungeoneeringCoinCurrency) {
				playerCurrencyAmount = currency.getAmount(to.getPlayer());
				if (playerCurrencyAmount < currency.getSellPrice(item)) {
					to.getPlayer().getPacketSender().sendMessage("You do not have enough " + currency.getName() + " to purchase this!");
					return this;
				}
				/*if ((item.getId() == 2542 || item.getId() == 22315) && !to.getPlayer().getInventory().contains(20135)) {
					to.getPlayer().sendMessage("You @red@need@bla@ a torva full helm to @blu@upgrade@bla@ a " + item.getName());
					return this;
				} else if ((item.getId() == 2544 || item.getId() == 22317) && !to.getPlayer().getInventory().contains(20139)) {
					to.getPlayer().sendMessage("You @red@need@bla@ a torva platebody to @blu@upgrade@bla@ a " + item.getName());
					return this;
				} else if ((item.getId() == 2546 || item.getId() == 22319) && !to.getPlayer().getInventory().contains(20143)) {
					to.getPlayer().sendMessage("You @red@need@bla@ a torva plateleg to @blu@upgrade@bla@ a " + item.getName());
					return this;
				}*/
			} else {
				int amount = to.getPlayer().getInventory().getAmount(995);
				if (amount >= value) {
					playerCurrencyAmount = amount;
				} else if (to.getPlayer().getMoneyInPouch() >= value) {
					playerCurrencyAmount = to.getPlayer().getMoneyInPouch();
				} else {
					playerCurrencyAmount = amount;
				}
			}

			if (playerCurrencyAmount < value) {
				int amount = (int) Math.floor((playerCurrencyAmount * item.getAmount()) / value);
				if (amount <= 0)
					return this;
				item.setAmount(amount);
				value = item.getAmount() * currency.getSellPrice(item);
				if (value < 0)
					return this;
				to.getPlayer().getPacketSender().sendMessage("You do not have enough " + currency.getName() + " to purchase that amount.");
			}
			if (value < 0)
				return this;
			currency.delete(to.getPlayer(), (int) value);
			if ((item.getId() == 2542 || item.getId() == 22315) && to.getPlayer().getInventory().contains(20135)) {
				to.getPlayer().getInventory().delete(20135, 1);
			} else if ((item.getId() == 2544 || item.getId() == 22317) && to.getPlayer().getInventory().contains(20139)) {
				to.getPlayer().getInventory().delete(20139, 1);
			} else if ((item.getId() == 2546 || item.getId() == 22319) && to.getPlayer().getInventory().contains(20143)) {
				to.getPlayer().getInventory().delete(20143, 1);
			}
			updateCurrencyOnInterface(to.getPlayer());

		new ShopPurchaseLog(to.getPlayer().getName(),
				item.getDefinition().getName(),
				item.getId(), item.getAmount(),
				value, currency.getName(),
				Misc.getTime()).submit();
		//}
		//to.getPlayer().getLogs().getNpcShopLogs().log("purchase", item.getName(), item.getId(), item.getAmount(), getId());

		super.switchItemShops(to, item, slot, sort, refresh);

		publicRefresh();
		
		/*for (Achievement achievement : Achievement.BUY_ITEM_ACHIEVEMENTS) {
			if (achievement.canAdvance(to.getPlayer()))
				achievement.buyHook(to.getPlayer(), this, item);
		}*/
		return this;
	}

	@Override
	public Shop delete(Item item, int slot, boolean refresh, ItemContainer toContainer) {

		if (item == null || slot < 0)
			return this;
		if (item.getAmount() > getAmount(item.getId()))
			item.setAmount(getAmount(item.getId()));


		getItems()[slot].setAmount(getItems()[slot].getAmount() - item.getAmount());
		if (getItems()[slot].getAmount() <= 0) {
			getItems()[slot].setAmount(0);
			if (type == ShopType.BUY_ANYTHING || type == ShopType.BUY_ANYTHING_NO_SALE
					|| deleteOnOutOfStock()) {
				getItems()[slot].setId(-1);
				sortItems();
			}
		}
		if (refresh)
			refreshItems();
		publicRefresh();
		return this;
	}
	
	@Override
	public Shop add(Item item, boolean refresh) {
		if (isPersonal()) {
			super.add(item, refresh);
			return this;
		}
		if (type == ShopType.BUY_ANYTHING_NO_SALE)
			item = new Item(-1);
		super.add(item, refresh);
		publicRefresh();
		return this;
	}
	
	@Override
	public int capacity() {
		return 60;
	}

	@Override
	public StackType stackType() {
		return StackType.STACKS;
	}

	@Override
	public Shop refreshItems() {
		for (Player player : World.getPlayers()) {
			if (player == null || player.getShop() == null ||
					player.getShop().id != id)
				continue;
			//player.getPacketSender().sendString(NAME_INTERFACE_CHILD_ID, name);
			player.getPacketSender().sendItemContainer(this, ITEM_CHILD_ID);
			//player.getPacketSender().sendInterfaceSet(INTERFACE_ID, INVENTORY_INTERFACE_ID - 1);
			//player.getPacketSender().sendItemContainer(player.getInventory(), INVENTORY_INTERFACE_ID);
		}
		return this;
	}

	@Override
	public ItemContainer full() {
		getPlayer().getPacketSender().sendMessage("Not enough space in this shop.");
		return this;
	}
	
	public static void writeLog(Player player, Shop shop, Item item, String description) {

	}

	/**
	 * The shop interface id.
	 */
	public static final int INTERFACE_ID = 20700;
	
	/**
	 * The starting interface child id of items.
	 */
	public static final int ITEM_CHILD_ID = 20703;
	
	/**
	 * The interface child id of the shop's name.
	 */
	public static final int NAME_INTERFACE_CHILD_ID = 20704;
	
	/**
	 * The inventory interface id, used to set the items right click values
	 * to 'sell'.
	 */
	public static final int INVENTORY_INTERFACE_ID = 3823;
	
	/**
	 * This constants contains the sprite index for
	 * coin in the client's {@link RSImageLoader#sprites} array.
	 */
	public static final int COIN_SPRITE_ID = 586;

	public static final int ENERGY_FRAGMENT_SPRITE_ID = 1376;

	public static final int BLOOD_MONEY_SPRITE_ID = 1372;

	public static final int STARDUST_SPRITE_ID = 1358;

	public static final int TRIVIA_SPRITE_ID = 1154;

	public static final int VOTING_SPRITE_ID = 1143;

	public static final int SLAYER_POINT_SPRITE = 1363;

	public static final int PRESTIGE_POINT_SPRITE = 1367;

	public static final int DUNGEONEERING_TOKEN_SPRITE = 716;

	public static final int TOKKUL_SPRITE = 1377;

	/**
	 * This constants contains the sprite index for
	 * tokens in the client's {@link RSImageLoader#sprites} array.
	 */
	public static final int TOKEN_SPRITE_ID = 587;
	
	/**
	 * This constants contains the sprite index for
	 * donator symbol in the client's {@link RSImageLoader#sprites} array,
	 * the same sprite as the {@link org.niobe.model.PlayerRights#DONATOR} 
	 * mod icon.
	 */
	public static final int DONATOR_SPRITE_ID = 680;
	
	/**
	 * This constants contains the sprite index for
	 * tokens in the client's {@link RSImageLoader#sprites} array.
	 */
	public static final int TICKET_SPRITE_ID = 1375;

	/**
	 * This constants contains the sprite index for
	 * tokens in the client's {@link RSImageLoader#sprites} array.
	 */
	public static final int SKILLING_COIN_SPRITE_ID = 1338;
	
	/**
	 * This constants contains the sprite index for
	 * tokkul item in the client's {@link RSImageLoader#sprites} array.
	 */
	public static final int TOKKUL_SPRITE_ID = 693;
	
	/**
	 * This constants contains the sprite index for
	 * green hatchet in the client's {@link RSImageLoader#sprites} array.
	 */
	public static final int GREEN_HATCHET_SPRITE_ID = 730;
	
	/**
	 * This constants contains the sprite index for
	 * the secondary token in the client's {@link RSImageLoader#sprites} array.
	 */
	public static final int SECONDARY_TOKEN_SPRITE_ID = 731;
	
	/**
	 * This constants contains the sprite index for
	 * the color splat sprite in the client's {@link RSImageLoader#sprites} array.
	 */
	public static final int COLOR_SPLATS_SPRITE_ID = 732;
	
	/**
	 * This constants contains the sprite index for
	 * two rocks sprite in the client's {@link RSImageLoader#sprites} array.
	 */
	public static final int ROCK_SPRITE_ID = 733;
	
	/**
	 * This constants contains the sprite index for
	 * the chef's hat sprite in the client's {@link RSImageLoader#sprites} array.
	 */
	public static final int CHEF_HAT_SPRITE_ID = 734;
	
	/**
	 * This constants contains the sprite index for
	 * soul sprite in the client's {@link RSImageLoader#sprites} array.
	 */
	public static final int SOUL_SPRITE_ID = 738;
	
	/**
	 * This constants contains the sprite index for
	 * the hunger game's token sprite in the client's {@link RSImageLoader#sprites} array.
	 */
	public static final int SURIVAL_TOKEN_SPRITE_ID = 730;
	
	/**
	 * Represents what can be a {@link Shop#currency}, so you
	 * can have multiple currencies, such as points, items, etc.
	 * 
	 * @author relex lawl
	 */
	public static interface Currency {
		
		/**
		 * Gets the currency's name.
		 * @return	The name to handle things as message for fund issues.
		 */
		public String getName();
		
		/**
		 * Gets the amount of currency the {@link player}
		 * currently has.
		 * @param player	The {@link org.niobe.world.Player} to fetch amount for.
		 * @return			The amount of the currency.
		 */
		public int getAmount(Player player);
		
		/**
		 * Handles the deletion of the currency used in
		 * the shop.
		 * @param player	The {@link org.niobe.world.Player} to doAction currency for.
		 * @param amount	The amount of this currency to doAction.
		 */
		public void delete(Player player, int amount);
		
		/**
		 * Handles the addition of the currency back to
		 * the {@link player} - used when they sell items back to the shop.
		 * @param player	The {@link org.niobe.world.Player} selling the item back.
		 * @param amount	The amount of items they're selling back.
		 */
		public void add(Player player, int amount);
		
		/**
		 * Gets the price the {@link item} is sold for 
		 * in the shop.
		 * @param item	The {@link org.niobe.model.Item} to get value for.
		 * @return		The sell price.
		 */
		public int getSellPrice(Item item);
		
		/**
		 * Gets the price the {@link item} is bought for 
		 * in the shop.
		 * @param item	The {@link org.niobe.model.Item} to get value for.
		 * @return		The buy price.
		 */
		public int getBuyPrice(Item item);
		
		/**
		 * Gets the sprite id used in the client's
		 * sprite loader to fetch the currency sprite
		 * next to the item box.
		 * @return	The sprite's sprite loader index.
		 */
		public int getSpriteId();
	}
}
