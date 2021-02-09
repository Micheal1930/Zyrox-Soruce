package com.zyrox.saving.impl;

import com.zyrox.model.item.RingOfBosses;
import com.zyrox.saving.SaveString;
import com.zyrox.world.entity.impl.player.Player;

public class SaveRingCollection extends SaveString {

    public SaveRingCollection(String name) {
        super(name);
    }

    @Override
    public void setValue(Player player, String value) {
        player.setRingOfBossesColletion(RingOfBosses.CollectionLocation.valueOf(value));
    }

    @Override
    public String getValue(Player player) {
        return player.getRingOfBossesCollection().name();
    }
}
