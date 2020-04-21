package com.varrock.world.content.auction_house;

/**
 * Created by Jonny on 9/8/2019
 **/
public enum AuctionHouseSortType {

    PRICE(0),
    NEWEST(1),
    OLDEST(2);

    private final int identifier;

    AuctionHouseSortType(int identifier) {
        this.identifier = identifier;
    }

    public int getIdentifier() {
        return identifier;
    }

    public static AuctionHouseSortType getTypeForIdentifier(int identifier) {
        for(AuctionHouseSortType auctionHouseSortType : AuctionHouseSortType.values()) {
            if(auctionHouseSortType.getIdentifier() == identifier) {
                return auctionHouseSortType;
            }
        }
        return AuctionHouseSortType.PRICE;
    }
}
