package com.zyrox.model.container.impl.currency;

import com.zyrox.model.Item;

/**
 * An implementation of {@link ItemCurrency} to load
 * through the shops.xml file.
 *
 * @author relex lawl
 */
public class CoinCurrency extends ItemCurrency {

	/**
	 * The CoinCurrency constructor.
	 */
	public CoinCurrency() {
		super(new Item(995));
	}
}
