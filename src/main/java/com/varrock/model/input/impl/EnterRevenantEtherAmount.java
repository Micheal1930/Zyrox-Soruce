package com.varrock.model.input.impl;

import com.varrock.model.input.EnterAmount;
import com.varrock.model.item.CrawsBow;
import com.varrock.world.entity.impl.player.Player;

public class EnterRevenantEtherAmount extends EnterAmount {

    @Override
    public boolean handleAmount(Player player, int amount) {
        CrawsBow.load(player, amount);

        return false;
    }

}
