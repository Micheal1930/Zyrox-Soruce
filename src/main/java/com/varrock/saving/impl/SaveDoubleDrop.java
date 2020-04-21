package com.varrock.saving.impl;

import com.varrock.saving.SaveLong;
import com.varrock.util.Stopwatch;
import com.varrock.world.entity.impl.player.Player;

public class SaveDoubleDrop extends SaveLong {

    public SaveDoubleDrop(String name) {
        super(name);
    }

    @Override
    public long getDefaultValue() {
        return 0;
    }

    @Override
    public void setValue(Player player, long value) {
        player.setLastDoubleDrop(new Stopwatch(value));
    }

    @Override
    public Long getValue(Player player) {
        return player.getLastDoubleDrop().getTime();
    }
}
