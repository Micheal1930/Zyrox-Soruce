package com.zyrox.world.entity.impl.npc.impl;

import com.zyrox.model.Position;
import com.zyrox.world.content.combat.strategy.CombatStrategy;
import com.zyrox.world.content.combat.strategy.impl.GreatOlmCombatStrategy;
import com.zyrox.world.entity.Entity;
import com.zyrox.world.entity.impl.GameCharacter;
import com.zyrox.world.entity.impl.npc.NPC;

public class GreatOlm extends NPC {

    public GreatOlm(int id, Position position) {
        super(id, position);
        getMovementQueue().setLockMovement(true);
    }

    @Override
    public GameCharacter setPositionToFace(Position positionToFace) {
        return this;
    }

    @Override
    public GameCharacter setEntityInteraction(Entity entity) {
        return this;
    }

    @Override
    public CombatStrategy determineStrategy() {
        return new GreatOlmCombatStrategy();
    }

}
