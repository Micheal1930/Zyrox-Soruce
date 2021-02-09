package com.zyrox.world.content.combat.pvp;

/**
 * Created by Jonny on 5/3/2019
 **/
public enum KillStreakBonus {

    KILLS_100(100,150, true),
    KILLS_50(50,100, true),
    KILLS_20(20,75, true),
    KILLS_10(10,50, true),
    KILLS_5(5,25, true);

    private final int requiredKills;
    private final int bonusBloodMoney;
    private final boolean announce;

    KillStreakBonus(int requiredKills, int bonusBloodMoney, boolean announce) {
        this.requiredKills = requiredKills;
        this.bonusBloodMoney = bonusBloodMoney;
        this.announce = announce;
    }

    public int getBonusBloodMoney() {
        return bonusBloodMoney;
    }

    public boolean isAnnounce() {
        return announce;
    }

    public int getRequiredKills() {
        return requiredKills;
    }
}
