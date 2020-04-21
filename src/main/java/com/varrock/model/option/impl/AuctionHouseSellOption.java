package com.varrock.model.option.impl;


import com.varrock.model.Item;
import com.varrock.model.input.Input;
import com.varrock.model.option.InterfaceOption;
import com.varrock.world.content.auction_house.AuctionHouseManager;
import com.varrock.world.entity.impl.player.Player;

/**
 * Created by Jonny on 9/6/2019
 **/
public enum AuctionHouseSellOption implements InterfaceOption {

    SELL_1 {

        @Override
        public void onAction(Player player, int itemId) {
            Item gameItem = new Item(itemId, 1);
            AuctionHouseManager.promptItemForSell(player, gameItem);
        }

    },

    SELL_5 {
        @Override
        public void onAction(Player player, int itemId) {
            Item gameItem = new Item(itemId, 5);
            AuctionHouseManager.promptItemForSell(player, gameItem);
        }
    },

    SELL_10 {
        @Override
        public void onAction(Player player, int itemId) {
            Item gameItem = new Item(itemId, 10);
            AuctionHouseManager.promptItemForSell(player, gameItem);
        }
    },

    SELL_X {
        @Override
        public void onAction(Player player, int itemId) {
            player.setInputHandling(new Input() {
                @Override
                public boolean handleAmount(Player player, int amount) {
                    Item gameItem = new Item(itemId, amount);
                    AuctionHouseManager.promptItemForSell(player, gameItem);
                    return true;
                }
            });
            player.getPacketSender().sendEnterAmountPrompt("Enter amount:");
        }
    },

    SELL_ALL {
        @Override
        public void onAction(Player player, int itemId) {
            AuctionHouseManager.promptItemForSell(player, new Item(itemId, player.getInventory().getAmount(itemId)));
        }
    },

    ;


}
