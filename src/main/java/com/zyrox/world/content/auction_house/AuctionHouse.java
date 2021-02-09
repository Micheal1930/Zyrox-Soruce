package com.zyrox.world.content.auction_house;

import java.util.ArrayList;

import com.zyrox.world.content.auction_house.item.AuctionHouseItem;

/**
 * Created by Jonny on 9/5/2019
 **/
public class AuctionHouse {

    private String owner;

    private ArrayList<AuctionHouseItem> items = new ArrayList<>();

    public AuctionHouse(String owner) {
        this.owner = owner;
    }

    public ArrayList<AuctionHouseItem> getItems() {
        return items;
    }

    public String getOwner() {
        return owner;
    }
}
