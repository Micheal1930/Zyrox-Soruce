package com.varrock.saving.impl;

import com.varrock.saving.SaveInteger;
import com.varrock.world.content.tutorial.TutorialStages;
import com.varrock.world.entity.impl.player.Player;

public class SaveTutorialStage extends SaveInteger {

    public SaveTutorialStage(String name) {
        super(name);
    }

    @Override
    public void setValue(Player player, int value) {
        player.setTutorialStage(TutorialStages.values()[value]);
    }

    @Override
    public Integer getValue(Player player) {
        return player.getTutorialStage().ordinal();
    }

    @Override
    public int getDefaultValue() {
        return TutorialStages.INITIAL_STAGE.ordinal();
    }
}
