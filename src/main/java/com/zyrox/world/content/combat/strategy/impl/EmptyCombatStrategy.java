package com.zyrox.world.content.combat.strategy.impl;

import com.zyrox.world.content.combat.CombatContainer;
import com.zyrox.world.content.combat.CombatType;
import com.zyrox.world.content.combat.strategy.CombatStrategy;
import com.zyrox.world.entity.impl.GameCharacter;

public class EmptyCombatStrategy implements CombatStrategy {
    @Override
    public boolean canAttack(GameCharacter entity, GameCharacter victim) {
        return false;
    }

    @Override
    public CombatContainer attack(GameCharacter entity, GameCharacter victim) {
        return null;
    }

    @Override
    public boolean customContainerAttack(GameCharacter entity, GameCharacter victim) {
        return false;
    }

    @Override
    public int attackDelay(GameCharacter entity) {
        return 0;
    }

    @Override
    public int attackDistance(GameCharacter entity) {
        return 0;
    }

    @Override
    public CombatType getCombatType() {
        return null;
    }
}
