package com.zyrox.model.input.impl;

import com.zyrox.model.input.EnterAmount;
import com.zyrox.model.item.CrawsBow;
import com.zyrox.world.entity.impl.player.Player;

public class EnterRevenantEtherAmount extends EnterAmount {

    @Override
    public boolean handleAmount(Player player, int amount) {
        CrawsBow.load(player, amount);

        return false;
    }

}
