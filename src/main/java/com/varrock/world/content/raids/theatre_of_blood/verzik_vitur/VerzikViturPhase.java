package com.varrock.world.content.raids.theatre_of_blood.verzik_vitur;

import com.varrock.world.content.raids.theatre_of_blood.verzik_vitur.combat_strategy.VerzikViturCombatStrategy;
import com.varrock.world.content.raids.theatre_of_blood.verzik_vitur.combat_strategy.impl.VerzikViturPhaseOneCombatStrategy;
import com.varrock.world.content.raids.theatre_of_blood.verzik_vitur.combat_strategy.impl.VerzikViturPhaseThreeCombatStrategy;
import com.varrock.world.content.raids.theatre_of_blood.verzik_vitur.combat_strategy.impl.VerzikViturPhaseTwoCombatStrategy;

/**
 * Created by Jonny on 7/2/2019
 **/
public enum VerzikViturPhase {

    PHASE_X(VerzikViturConstants.VITUR_SITTING_IDLE_NPC_ID, null),

    PHASE_1(VerzikViturConstants.VITUR_SITTING_ATTACKING_NPC_ID, new VerzikViturPhaseOneCombatStrategy()),
    PHASE_2(VerzikViturConstants.VITUR_WALKING_SOUTH_NPC_ID, new VerzikViturPhaseTwoCombatStrategy()),
    PHASE_3(VerzikViturConstants.VITUR_SPIDER_NPC_ID, new VerzikViturPhaseThreeCombatStrategy()),

    DEATH(-1, null),

    ;

    private final int npcId;
    private final VerzikViturCombatStrategy combatStrategy;

    VerzikViturPhase(int npcId, VerzikViturCombatStrategy combatStrategy) {
        this.npcId = npcId;
        this.combatStrategy = combatStrategy;
    }

    public int getNpcId() {
        return npcId;
    }

    public VerzikViturCombatStrategy getCombatStrategy() {
        return combatStrategy;
    }

    public VerzikViturPhase getNextPhase() {
        return ordinal() + 1 >= VerzikViturPhase.values().length ? VerzikViturPhase.DEATH :  values()[this.ordinal() + 1];
    }
}
