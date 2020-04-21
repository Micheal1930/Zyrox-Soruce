package com.varrock.world.content;

import java.util.HashMap;

import com.varrock.model.Item;
import com.varrock.model.container.impl.Shop;
import com.varrock.util.Misc;
import com.varrock.world.content.shop.ShopData;
import com.varrock.world.content.shop.ShopManager;
import com.varrock.world.entity.impl.player.Player;

/**
 * Created by Jonny on 10/5/2019
 **/
public class AccountCheck {

    public static void check(Player player) {
        boolean flagged = false;
        String flaggedBecause;

        //long totalWealth = player.getMoneyInPouch() + player.getInventory().getAmount(995) + player.getBank().get
      /*  if(player.getMoneyInPouch() > 10_000_000_000L) {
            flagged = true;
            flaggedBecause = "has "+ Misc.formatRunescapeStyle(player.getMoneyInPouch())+" in pouch";
        } else if(play) {

        }*/
    }

}
