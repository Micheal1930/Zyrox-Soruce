package com.zyrox.saving.impl;

import com.zyrox.saving.SaveLong;
import com.zyrox.util.Stopwatch;
import com.zyrox.world.entity.impl.player.Player;

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
