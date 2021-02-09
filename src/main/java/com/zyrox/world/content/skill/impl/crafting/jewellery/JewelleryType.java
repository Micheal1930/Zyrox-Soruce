package com.zyrox.world.content.skill.impl.crafting.jewellery;

/**
 * Created by Jonny on 10/11/2019
 **/
public enum JewelleryType {

    RING(1592),
    NECKLACE(1597),
    BRACELET(11065),
    AMULET(1595);

    public int mouldId;

    JewelleryType(int mouldId) {
        this.mouldId = mouldId;
    }
}
