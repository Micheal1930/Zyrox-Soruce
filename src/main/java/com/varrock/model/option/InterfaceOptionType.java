package com.varrock.model.option;

import com.varrock.model.option.impl.AuctionHouseSellOption;
import com.varrock.world.content.auction_house.AuctionHouseConstants;

/**
 * Created by Jonny on 6/22/2019
 **/
public enum InterfaceOptionType {

    AUCTION_HOUSE_SELL(AuctionHouseConstants.INVENTORY_CONTAINER_ID, AuctionHouseSellOption.values()),

    ;

    private final int interfaceId;
    private final InterfaceOption[] optionValues;

    public final static InterfaceOptionType[] VALUES = InterfaceOptionType.values();

    InterfaceOptionType(int interfaceId, InterfaceOption[] optionValues) {
        this.interfaceId = interfaceId;
        this.optionValues = optionValues;
    }

    public int getInterfaceId() {
        return interfaceId;
    }

    public InterfaceOption[] getOptionValues() {
        return optionValues;
    }

}
