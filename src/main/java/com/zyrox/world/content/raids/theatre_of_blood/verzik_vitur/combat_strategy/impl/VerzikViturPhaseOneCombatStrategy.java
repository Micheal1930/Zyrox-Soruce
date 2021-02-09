package com.zyrox.world.content.raids.theatre_of_blood.verzik_vitur.combat_strategy.impl;

import com.zyrox.engine.task.Task;
import com.zyrox.engine.task.TaskManager;
import com.zyrox.model.*;
import com.zyrox.model.projectile.Projectile;
import com.zyrox.util.Misc;
import com.zyrox.world.clip.region.RegionClipping;
import com.zyrox.world.content.combat.CombatContainer;
import com.zyrox.world.content.combat.CombatType;
import com.zyrox.world.content.raids.theatre_of_blood.pillar.Pillar;
import com.zyrox.world.content.raids.theatre_of_blood.verzik_vitur.VerzikViturConstants;
import com.zyrox.world.content.raids.theatre_of_blood.verzik_vitur.combat_strategy.VerzikViturCombatStrategy;
import com.zyrox.world.entity.impl.GameCharacter;

/**
 * Created by Jonny on 7/2/2019
 **/
public class VerzikViturPhaseOneCombatStrategy extends VerzikViturCombatStrategy {

    @Override
    public boolean customContainerAttack(GameCharacter entity, GameCharacter victim) {

        entity.getUpdateFlag().flag(Flag.TRANSFORM);

        entity.performAnimation(VerzikViturConstants.CHAIR_ATTACK);

        Pillar closestPillar = getClosestPillar(victim.getPosition());

        if (closestPillar == null || RegionClipping.canProjectileAttack(entity, victim) && RegionClipping.canProjectileAttack(victim, entity)) {
            new Projectile(entity, victim, VerzikViturConstants.ELECTRIC_PROJECTILE_ID, 85, 1, 105, 43, 0).sendProjectile();
            TaskManager.submit(new Task(3) {

                @Override
                public void execute() {

                    if(RegionClipping.canProjectileAttack(entity, victim) && RegionClipping.canProjectileAttack(victim, entity)) {
                        if (victim.getLocation() == Locations.Location.THEATRE_OF_BLOOD_FINAL_ROOM) {
                            victim.dealDamage(new Hit(200 + Misc.getRandom(250), Hitmask.RED, CombatIcon.MAGIC));
                        }
                    }

                    stop();
                }
            });

        } else {
            new Projectile(entity, closestPillar.getPillarNpc(), VerzikViturConstants.ELECTRIC_PROJECTILE_ID, 85, 1, 105, 43, 0).sendProjectile();
            entity.getCombatBuilder().setContainer(new CombatContainer(entity, closestPillar.getPillarNpc(), 1, 2, CombatType.MAGIC, false) {
                @Override
                public void onHit(int damage, boolean accurate) {
                    if(closestPillar.getPillarNpc().getConstitution() <= 0) {
                        TaskManager.submit(new Task(3) {

                            @Override
                            public void execute() {
                                getTheatre().removePillar(closestPillar, true);
                                stop();
                            }
                        });
                        return;
                    } else {
                        closestPillar.getPillarNpc().dealDamage(new Hit(100 + Misc.random(100), Hitmask.RED, CombatIcon.MAGIC));
                    }

                    closestPillar.getPillarNpc().performGraphic(new Graphic(VerzikViturConstants.ELECTRIC_GRAPHIC_ID));
                }
            });
        }

        return true;
    }

    public Pillar getClosestPillar(Position victimPosition) {
        Pillar closestPillar = null;

        if(getTheatre() != null) {
            for (Pillar pillar : getTheatre().getPillars()) {
                if (closestPillar == null) {
                    closestPillar = pillar;
                } else {
                    if (victimPosition.getDistance(pillar.getPillarObject().getPosition()) < victimPosition.getDistance(closestPillar.getPillarObject().getPosition())) {
                        closestPillar = pillar;
                    }
                }
            }
        }

        return closestPillar;
    }

    @Override
    public void changePhase() {

        getVerzikVitur().getCombatBuilder().setTransforming(true);

        getVerzikVitur().getCombatBuilder().reset(true);

        getVerzikVitur().performAnimation(VerzikViturConstants.OUT_OF_CHAIR);

        TaskManager.submit(new Task(4, getVerzikVitur(), false) {
            @Override
            public void execute() {

                if(getTheatre().isRoomEmpty()) {
                    stop();
                    return;
                }

                getVerzikVitur().performAnimation(new Animation(-1));

                getTheatre().removePillars();

                getVerzikVitur().transform(getVerzikVitur().getPhase().getNextPhase(), getVerzikVitur().getPhase().getNextPhase().getNpcId());
                getVerzikVitur().setConstitution(getVerzikVitur().getDefaultConstitution());

                getVerzikVitur().walkToPosition(VerzikViturConstants.CENTER_OF_ROOM);

                getTheatre().clearDawnbringer();

                stop();
            }
        });

    }

}

