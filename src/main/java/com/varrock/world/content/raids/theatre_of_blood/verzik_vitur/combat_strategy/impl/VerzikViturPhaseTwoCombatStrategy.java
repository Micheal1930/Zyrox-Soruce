package com.varrock.world.content.raids.theatre_of_blood.verzik_vitur.combat_strategy.impl;

import java.util.ArrayList;
import java.util.List;

import com.varrock.GameSettings;
import com.varrock.engine.task.Task;
import com.varrock.engine.task.TaskManager;
import com.varrock.model.*;
import com.varrock.model.projectile.Projectile;
import com.varrock.util.Misc;
import com.varrock.world.World;
import com.varrock.world.content.combat.CombatContainer;
import com.varrock.world.content.combat.CombatType;
import com.varrock.world.content.raids.theatre_of_blood.verzik_vitur.VerzikViturConstants;
import com.varrock.world.content.raids.theatre_of_blood.verzik_vitur.combat_strategy.VerzikViturCombatStrategy;
import com.varrock.world.content.raids.theatre_of_blood.verzik_vitur.minions.NylocasAthanatos;
import com.varrock.world.content.raids.theatre_of_blood.verzik_vitur.minions.NylocasMatomenos;
import com.varrock.world.entity.impl.GameCharacter;
import com.varrock.world.entity.impl.npc.NPC;
import com.varrock.world.entity.impl.player.Player;

/**
 * Created by Jonny on 7/2/2019
 **/
public class VerzikViturPhaseTwoCombatStrategy extends VerzikViturCombatStrategy {

    private NPC healer;
    private ArrayList<NPC> bombers = new ArrayList<>();

    private int spiderAttacks = 0;

    @Override
    public boolean customContainerAttack(GameCharacter entity, GameCharacter victim) {

        entity.getUpdateFlag().flag(Flag.TRANSFORM);

        final Position tile = victim.getPosition().copy();

        List<Player> targets = getVerzikVitur().getPossibleTargets();

        entity.performAnimation(VerzikViturConstants.STANDING_ATTACK);

        int attackChance = Misc.inclusiveRandom(1, 10);

        if(attackChance <= 5) {

            for (Player t : targets) {
                if (t == null || t.asPlayer().getConstitution() <= 0 || t.getLocation() != Locations.Location.THEATRE_OF_BLOOD_FINAL_ROOM) {
                    continue;
                }

                final Position targetPosition = t.getPosition().copy();

                new Projectile(entity.getCentrePosition(), targetPosition, -1, VerzikViturConstants.BOMB_PROJECTILE_ID, 85, 1, 105, 43, 0).sendProjectile();

                entity.getCombatBuilder().setContainer(new CombatContainer(entity, t, 1, 4, CombatType.MAGIC, false) {
                    @Override
                    public void onHit(int damage, boolean accurate) {
                        t.getPacketSender().sendGlobalGraphic(new Graphic(VerzikViturConstants.BOMB_IMPACT_GRAPHIC_ID, GraphicHeight.LOW), targetPosition);
                    }

                    @Override
                    public void dealDamage() {
                        if(t.getPosition().sameAsMinusFloor(targetPosition)) {
                            t.dealDamage(new Hit(100 + Misc.random(250), Hitmask.RED, CombatIcon.NONE));
                        }
                    }
                });

            }

            getVerzikVitur().setBombCount(getVerzikVitur().getBombCount() + 1);

        } else {

            if(getVerzikVitur().getElectricCount() < 2) {
                for (Player t : targets) {
                    if (t == null || t.asPlayer().getConstitution() <= 0 || t.getLocation() != Locations.Location.THEATRE_OF_BLOOD_FINAL_ROOM) {
                        continue;
                    }

                    final Position targetPosition = t.getPosition().copy();

                    new Projectile(entity, t, 1585 + GameSettings.OSRS_GFX_OFFSET, 85, 1, 105, 43, 0).sendProjectile();

                    entity.getCombatBuilder().setContainer(new CombatContainer(entity, t, 1, 3, CombatType.MAGIC, false) {

                    });
                }

                getVerzikVitur().setElectricCount(getVerzikVitur().getElectricCount() + 1);
            } else {
                final Position targetPosition = victim.getPosition();

                new Projectile(entity.getCentrePosition(), targetPosition, -1, VerzikViturConstants.SPIDER_PROJECTILE_ID, 85, 1, 105, 43, 0).sendProjectile();

                entity.getCombatBuilder().setContainer(new CombatContainer(entity, victim, 1, 3, CombatType.MAGIC, false) {
                    @Override
                    public void onHit(int damage, boolean accurate) {

                        if(healer == null && bombers.isEmpty()) {
                            healer = new NylocasAthanatos(VerzikViturConstants.HEALER_NPC_ID, tile, getVerzikVitur());
                            getVerzikVitur().getMinions().add(healer);
                            World.register(healer);
                            healer.performAnimation(new Animation(VerzikViturConstants.HEALER_SPAWN_ANIMATION_ID));

                            for (Player t : targets) {
                                if (t == null || t.asPlayer().getConstitution() <= 0 || t.getLocation() != Locations.Location.THEATRE_OF_BLOOD_FINAL_ROOM) {
                                    continue;
                                }

                                NPC bomber = new NylocasMatomenos(VerzikViturConstants.BOMBER_NPC_ID, getVerzikVitur().getPosition().copy().setZ(getVerzikVitur().getPosition().getZ()), getVerzikVitur(), t);

                                bombers.add(bomber);
                                getVerzikVitur().getMinions().add(bomber);
                                World.register(bomber);
                                bomber.performAnimation(new Animation(VerzikViturConstants.BOMBER_SPAWN_ANIMATION_ID));
                                spiderAttacks = 0;
                            }

                        }

                        if(spiderAttacks >= 6) {
                            for(NPC npc : bombers) {
                                if (npc != null)
                                    npc.dealDamage(new Hit(npc.getConstitution()));
                            }

                            if (healer != null)
                                healer.dealDamage(new Hit(healer.getConstitution()));

                            healer = null;
                            bombers.clear();
                        }

                        spiderAttacks++;
                    }
                });

                getVerzikVitur().setElectricCount(0);
                getVerzikVitur().setBombCount(0);
            }
        }
        return true;
    }

    @Override
    public void changePhase() {

        getVerzikVitur().getCombatBuilder().reset(true);

        getVerzikVitur().getCombatBuilder().setTransforming(true);

        TaskManager.submit(new Task(4, getVerzikVitur(), false) {
            @Override
            public void execute() {

                for(NPC npc : bombers) {
                    if (npc != null)
                        npc.dealDamage(new Hit(npc.getConstitution()));
                }

                if (healer != null)
                    healer.dealDamage(new Hit(healer.getConstitution()));

                healer = null;
                bombers.clear();

                getVerzikVitur().performAnimation(VerzikViturConstants.SPIDER_TRANSFORM);

                getVerzikVitur().transform(getVerzikVitur().getPhase().getNextPhase(), getVerzikVitur().getPhase().getNextPhase().getNpcId());
                getVerzikVitur().setConstitution(getVerzikVitur().getDefaultConstitution());

                getVerzikVitur().forceChat("Behold my true nature!");

                List<Player> targets = getVerzikVitur().getPossibleTargets();

                if(!targets.isEmpty()) {
                    getVerzikVitur().getCombatBuilder().attack(targets.get(0));
                }
                getVerzikVitur().getCombatBuilder().setTransforming(false);

                stop();
            }
        });

    }

}

