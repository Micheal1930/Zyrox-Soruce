package com.zyrox.saving.impl;

import com.zyrox.saving.SaveBoolean;
import com.zyrox.world.entity.impl.player.Player;

public class Save2019EasterEvent extends SaveBoolean {

    public Save2019EasterEvent(String name) {
        super(name);
    }

    @Override
    public void setValue(Player player, boolean value) {
        player.set2019EasterEventComplete(value);
    }

    @Override
    public Boolean getValue(Player player) {
        return player.hasComplete2019EasterEvent();
    }

    @Override
    public boolean getDefaultValue() {
        return false;
    }
}
