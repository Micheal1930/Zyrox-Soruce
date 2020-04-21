package com.varrock.world.content.raids.theatre_of_blood.verzik_vitur.combat_strategy;

import com.varrock.world.content.combat.CombatContainer;
import com.varrock.world.content.combat.CombatType;
import com.varrock.world.content.combat.strategy.CombatStrategy;
import com.varrock.world.content.raids.theatre_of_blood.TheatreOfBlood;
import com.varrock.world.content.raids.theatre_of_blood.verzik_vitur.VerzikVitur;
import com.varrock.world.entity.impl.GameCharacter;

/**
 * Created by Jonny on 7/2/2019
 **/
public class VerzikViturCombatStrategy implements CombatStrategy {

    private VerzikVitur verzikVitur;

    @Override
    public boolean canAttack(GameCharacter entity, GameCharacter victim) {
        return true;
    }

    @Override
    public CombatContainer attack(GameCharacter entity, GameCharacter victim) {
        return null;
    }

    @Override
    public boolean customContainerAttack(GameCharacter entity, GameCharacter victim) {

        return true;
    }

    @Override
    public int attackDelay(GameCharacter entity) {
        return 7;
    }

    @Override
    public int attackDistance(GameCharacter entity) {
        return 30;
    }

    @Override
    public CombatType getCombatType() {
        return null;
    }

    public VerzikVitur getVerzikVitur() {
        return verzikVitur;
    }

    public TheatreOfBlood getTheatre() {
        return verzikVitur == null ? null : verzikVitur.getTheatreOfBlood();
    }

    public VerzikViturCombatStrategy setVerzikVitur(VerzikVitur verzikVitur) {
        this.verzikVitur = verzikVitur;
        return this;
    }

    public void changePhase() {

    }

}
