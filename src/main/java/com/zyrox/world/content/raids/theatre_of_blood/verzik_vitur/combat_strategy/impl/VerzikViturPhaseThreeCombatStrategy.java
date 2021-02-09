package com.zyrox.world.content.raids.theatre_of_blood.verzik_vitur.combat_strategy.impl;

import java.util.List;

import com.zyrox.GameSettings;
import com.zyrox.model.Flag;
import com.zyrox.model.Locations;
import com.zyrox.model.projectile.Projectile;
import com.zyrox.util.Misc;
import com.zyrox.world.content.combat.CombatContainer;
import com.zyrox.world.content.combat.CombatType;
import com.zyrox.world.content.raids.theatre_of_blood.verzik_vitur.VerzikViturConstants;
import com.zyrox.world.content.raids.theatre_of_blood.verzik_vitur.combat_strategy.VerzikViturCombatStrategy;
import com.zyrox.world.entity.impl.GameCharacter;
import com.zyrox.world.entity.impl.player.Player;


/**
 * Created by Jonny on 7/2/2019
 **/
public class VerzikViturPhaseThreeCombatStrategy extends VerzikViturCombatStrategy {

    @Override
    public boolean customContainerAttack(GameCharacter entity, GameCharacter victim) {

        entity.getUpdateFlag().flag(Flag.TRANSFORM);

        int random = Misc.random(0, 2);

        List<Player> targets = getVerzikVitur().getPossibleTargets();

        boolean canMelee = victim.getPosition().isWithinDistance(entity.getPosition(), 3);

        if (random == 0) {
            entity.performAnimation(VerzikViturConstants.SPIDER_ATTACK_MELEE);

            if(canMelee) {
                entity.getCombatBuilder().setContainer(new CombatContainer(entity, victim, 2, 2, CombatType.MELEE, false));
            }
        } else if (random == 1) {
            entity.performAnimation(VerzikViturConstants.SPIDER_ATTACK_MAGE);
            for (Player t : targets) {
                if (t == null || t.asPlayer().getConstitution() <= 0 || t.getLocation() != Locations.Location.THEATRE_OF_BLOOD_FINAL_ROOM) {
                    continue;
                }

                new Projectile(entity, t, VerzikViturConstants.ELECTRIC_PROJECTILE_ID, 85, 1, 105, 43, 0).sendProjectile();

                entity.getCombatBuilder().setContainer(new CombatContainer(entity, t, 1, 3, CombatType.MAGIC, false));
            }
        } else {
            entity.performAnimation(VerzikViturConstants.SPIDER_ATTACK_RANGE);
            for (Player t : targets) {
                if (t == null || t.asPlayer().getConstitution() <= 0 || t.getLocation() != Locations.Location.THEATRE_OF_BLOOD_FINAL_ROOM) {
                    continue;
                }

                new Projectile(entity, t, 1578 + GameSettings.OSRS_GFX_OFFSET, 85, 1, 105, 43, 0).sendProjectile();

                entity.getCombatBuilder().setContainer(new CombatContainer(entity, t, 1, 3, CombatType.RANGED, false));
            }
        }
        return true;
    }

    @Override
    public int attackDistance(GameCharacter entity) {
        return 3;
    }

}

