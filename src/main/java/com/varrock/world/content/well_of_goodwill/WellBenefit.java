package com.varrock.world.content.well_of_goodwill;

import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author lare96 <http://github.com/lare96>
 */
public enum WellBenefit {

    NONE(-1, ""),
    BONUS_XP(100, "30% Bonus XP"),
    SLAYER_POINTS(200, "2x Slayer points"),
    BLOOD_MONEY(300, "2x Blood money"),
    VOTE_POINTS(400, "2x Vote points");

    public static final ImmutableList<WellBenefit> ALL;

    static {
        ImmutableList.Builder<WellBenefit> list = ImmutableList.builder();
        for (WellBenefit benefit : values()) {
            list.add(benefit);
        }
        ALL = list.build();
    }

    public final int triggerAmount;
    public final String description;

    WellBenefit(int triggerAmount, String description) {
        this.triggerAmount = triggerAmount * 1_000_000;
        this.description = description;
    }

    public static WellBenefit getBenefit(int amount) {
        for (int index = ALL.size() - 1; index > 0; index--) {
            WellBenefit benefit = ALL.get(index);
            if (amount >= benefit.triggerAmount) {
                return benefit;
            }
        }
        return WellBenefit.NONE;
    }

    public static WellBenefit getLastBenefit() {
        return ALL.get(ALL.size() - 1);
    }

    public static void forActiveBenefits(int amount, Consumer<WellBenefit> action) {
        for (WellBenefit benefit : ALL) {
            if (benefit == WellBenefit.NONE) {
                continue;
            }
            if (amount >= benefit.triggerAmount) {
                action.accept(benefit);
            }
        }
    }

    public static List<WellBenefit> getActiveBenefits(int amount) {
        List<WellBenefit> benefits = new ArrayList<>();
        for (WellBenefit benefit : ALL) {
            if (benefit == WellBenefit.NONE) {
                continue;
            }
            if (amount >= benefit.triggerAmount) {
                benefits.add(benefit);
            }
        }
        return benefits;
    }
}