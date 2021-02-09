package com.zyrox.world.content.skill.impl.crafting.jewellery;

/**
 * Created by Jonny on 10/11/2019
 **/
public class JewelleryItem {

    private int itemId;
    private int levelRequirement;
    private int xp;
    private JewelleryType type;

    public JewelleryItem(int itemId, int levelRequirement, int xp, JewelleryType type) {
        this.itemId = itemId;
        this.levelRequirement = levelRequirement;
        this.xp = xp;
        this.type = type;
    }

    public int getLevelRequirement() {
        return levelRequirement;
    }

    public int getItemId() {
        return itemId;
    }

    public int getXp() {
        return xp;
    }

    public JewelleryType getType() {
        return type;
    }
}
