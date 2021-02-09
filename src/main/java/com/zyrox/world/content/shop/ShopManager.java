package com.zyrox.world.content.shop;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.zyrox.model.Item;
import com.zyrox.model.PlayerRights;
import com.zyrox.model.container.impl.Shop;
import com.zyrox.model.container.impl.Shop.Currency;
import com.zyrox.model.definitions.ItemDefinition;
import com.zyrox.util.XmlUtil;
import com.zyrox.world.entity.impl.npc.click_type.NpcClickType;
import com.zyrox.world.entity.impl.player.Player;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Loads and manages all the {@link org.niobe.model.container.impl.Shop}s
 * in the world.  
 *
 * @author relex lawl
 */
public final class ShopManager {
	
	/**
	 * The map holds all the {@link org.niobe.model.container.impl.Shop} 
	 * loaded from the file in directory {@link #FILE_DIRECTORY}.
	 */
	private static final Map<Integer, Shop> shops = new HashMap<Integer, Shop>();
	
	/**
	 * The directory where the shop file is located.
	 */
	private static final String FILE_DIRECTORY = "./data/def/xml/shops.xml";
	
	/**
	 * Loads all the {@link org.niobe.model.container.impl.Shop}
	 * information and populates the {@link #shops} map.
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws ClassNotFoundException
	 */
	public static void init() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		try {
			System.out.println("Loading shop definitions...");
			long startup = System.currentTimeMillis();
			DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc = docBuilder.parse(FILE_DIRECTORY);
			NodeList shopList = doc.getElementsByTagName("shop");
			for (int i = 0; i < shopList.getLength(); i++) {
				Element shopElement = (Element) shopList.item(i);
				int shopId = Integer.parseInt(XmlUtil.getEntry("id", shopElement));
				String name = XmlUtil.getEntry("name", shopElement);
				@SuppressWarnings("unchecked")
                Class<Currency> currencyClass = (Class<Currency>) Class.forName(XmlUtil.getEntry("currency", shopElement));
				Currency currency = currencyClass.newInstance();
				NodeList stockList = shopElement.getElementsByTagName("item");
				Item[] stock = new Item[stockList.getLength()];
				int index = 0;
				for (int j = 0; j < stockList.getLength(); j++) {
					Element stockElement = (Element) stockList.item(j);

					int itemId = Integer.valueOf(XmlUtil.getEntry("id", stockElement));
					int amount;

					if(XmlUtil.elementExists("amount", stockElement)) {
						amount = Integer.valueOf(XmlUtil.getEntry("amount", stockElement));
					} else {
						amount = ItemDefinition.forId(itemId).isStackable() ? 10000 : 1000;
					}

					Item item = new Item(itemId, amount);

					stock[index] = item;
					index++;
				}
				shops.put(shopId, new Shop(null, shopId, name, currency, stock));
			}
			System.out.println("Loaded " + shops.size() + " shop" + (shops.size() == 1 ? "s" : "") + " in " + (System.currentTimeMillis() - startup) + "ms");
		} catch (IOException | ParserConfigurationException | SAXException exception) {
			exception.printStackTrace();
		}
	}
	
	/**
	 * Gets all the shops in the world.
	 * @return	The shops map.
	 */
	public static Map<Integer, Shop> getShops() {
		return shops;
	}

	/**
	 * Checks if a shop exists for the npc id and opens it
	 * @param player
	 * @param npcId
	 * @return successful
	 */
	public static boolean isShop(Player player, int npcId, NpcClickType clickType) {
		for(ShopType data : ShopType.values()) {
			if(data.getNpcId() == npcId) {
				int index = 0;
				for(NpcClickType npcClickType : data.getNpcClickType()) {
					if(npcClickType == clickType) {

						ShopData shopData = data.getShopData()[data.isFollowClickType() ? index : 0];
						Shop shop = ShopManager.getShops().get(shopData.getShopId());

						if(shop == null)
							continue;

						if(!shopData.isIronman() && player.isIronman()) {
							player.sendMessage("You are not allowed to open this shop in the ironman mode.");
							return true;
						}

						if(shopData == ShopData.IRONMAN_SHOP && !player.isIronman()) {
							player.sendMessage("Only ironman can access this shop.");
							return true;
						}

						player.getPacketSender().sendString(20704, shop.getName());
						player.getPacketSender().sendResetScroll(20711);

						player.openShopType = data;
						player.getPA().sendClickedId(20706 + (data.isFollowClickType() ? index : 0));

						for(int i = 0; i < 4; i++) {
							Shop displayShop = data.getShopData().length > i ? ShopManager.getShops().get(data.getShopData()[i].getShopId()) : null;
							if(displayShop != null)
								player.getPacketSender().sendString(20706 + i, data.getShopData()[i].getName());
							player.getPacketSender().sendInterfaceDisplayState(20706 + i, displayShop == null);
						}

						shop.open(player);
						return true;
					}
					index++;
				}
			}
		}
		return false;
	}

	/**
	 * Checks if a shop exists for the npc id and opens it
	 * @param player
	 * @param npcId
	 * @return successful
	 */
	public static boolean openShop(Player player, int shopId) {

		Shop shop = ShopManager.getShops().get(shopId);

		if(shop == null)
			return false;

		player.getPacketSender().sendString(20704, shop.getName());
		player.getPacketSender().sendResetScroll(20711);

		player.getPA().sendClickedId(20706);

		player.openShopType = null;

		player.getPacketSender().sendString(20706, "Stock");
		player.getPacketSender().sendInterfaceDisplayState(20706, false);

		for(int i = 1; i < 4; i++) {

			player.getPacketSender().sendString(20706 + i,"");
			player.getPacketSender().sendInterfaceDisplayState(20706 + i, true);
		}

		shop.open(player);
		return true;
	}

	public static boolean isButton(Player player, int buttonId) {
		if(buttonId >= 20706 && buttonId <= 20709) {
			if(player.openShopType == null)
				return true;

			int index = (buttonId - 20709) + 3;

			player.getPA().sendClickedId(20706 + index);

			Shop shop = ShopManager.getShops().get(player.openShopType.getShopData()[index].getShopId());

			ShopData shopData = player.openShopType.getShopData()[index];

			if(!shopData.isIronman() && player.isIronman()) {
				player.sendMessage("You are not allowed to open this shop in the ironman mode.");
				return true;
			}

			if(shopData == ShopData.IRONMAN_SHOP && !player.isIronman()) {
				player.sendMessage("Only ironman can access this shop.");
				return true;
			}

			if(shopData == ShopData.DONATOR_EXTREME && !PlayerRights.EXTREME_DONATOR.hasEnoughDonated(player)) {
				player.sendMessage("This shop is for Extreme donator+ ~ Type ::donate to get extreme.");
				return true;
			}

			player.getPacketSender().sendResetScroll(20711);

			player.getPacketSender().sendString(20704, shop.getName());

			shop.open(player);

			return true;
		}
		return false;
	}
}
