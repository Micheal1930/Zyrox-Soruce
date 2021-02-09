package com.zyrox.world.content.lootboxes;

import com.zyrox.util.Misc;

/**
 * Created by Jonny on 8/24/2019
 **/
public enum LootColumn {

    ULTIMATE(0xff0000),
    SUPER_RARE(0xadd8e6),
    RARE(0x00ff00),
    UNCOMMON(0xff9040),
    COMMON(0xff9040),
    ;

    public int color;

    LootColumn(int color) {
        this.color = color;
    }

    public String getName() {
        return Misc.capitalize(name().toLowerCase().replaceAll("_", " "));
    }


}
