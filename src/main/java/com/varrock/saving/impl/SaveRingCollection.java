package com.varrock.saving.impl;

import com.varrock.model.item.RingOfBosses;
import com.varrock.saving.SaveString;
import com.varrock.world.entity.impl.player.Player;

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
