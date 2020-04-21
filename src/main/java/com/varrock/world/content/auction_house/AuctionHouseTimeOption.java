package com.varrock.world.content.auction_house;

/**
 * Created by Jonny on 9/6/2019
 **/
public enum AuctionHouseTimeOption {

    MINUTE_1(1000 * 60),

    HOUR_1(1000 * 60 * 60),

    HOUR_8(1000 * 60 * 60 * 8),

    HOUR_24(1000 * 60 * 60 * 24);

    private final long duration;

    AuctionHouseTimeOption(long duration) {
        this.duration = duration;
    }

    public long getDuration() {
        return duration;
    }
}
