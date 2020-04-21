package com.varrock.world.content.combat.strategy.impl;

import com.varrock.GameSettings;
import com.varrock.engine.task.Task;
import com.varrock.engine.task.TaskManager;
import com.varrock.model.*;
import com.varrock.model.container.impl.Equipment;
import com.varrock.model.projectile.Projectile;
import com.varrock.util.Misc;
import com.varrock.world.content.combat.CombatContainer;
import com.varrock.world.content.combat.CombatType;
import com.varrock.world.content.combat.range.CombatRangedAmmo;
import com.varrock.world.content.combat.strategy.CombatStrategy;
import com.varrock.world.entity.impl.GameCharacter;
import com.varrock.world.entity.impl.npc.NPC;
import com.varrock.world.entity.impl.player.Player;

public class DemonicGorilla implements CombatStrategy {

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
        NPC gorilla = (NPC) entity;
        if (gorilla.isChargingAttack() || victim.getConstitution() <= 0) {
            return true;
        }
        if (gorilla.gorillaDamageTaken >= 500) {
            gorilla.switchGorilla = true;
            gorilla.gorillaDamageTaken = 0;
        }
        if (gorilla.switchGorilla) {
            switchGorilla(gorilla, (Player) victim);
        }
        if (gorilla.gorillaType == null || gorilla.gorillaType == CombatType.ROCK_FALL) {
            gorilla.gorillaSwitchAttacks = true;
        }
        if (gorilla.gorillaAttacks >= 3) {
            gorilla.gorillaSwitchAttacks = true;
            gorilla.gorillaAttacks = 0;
        }
        if (gorilla.gorillaSwitchAttacks) {
            setAttackType(gorilla);
        }
        attack(gorilla, (Player) victim, gorilla.gorillaType);
        return true;
    }

    private void attack(NPC gorilla, Player player, CombatType type) {
        if (type == CombatType.MELEE) {
            gorilla.performAnimation(new Animation(7226 + GameSettings.OSRS_ANIM_OFFSET));
            gorilla.getCombatBuilder().setContainer(new CombatContainer(gorilla, player, 1, 0, CombatType.MELEE, true));
            gorilla.gorillaAttacks++;
        }

        if (type == CombatType.RANGED) {
            gorilla.setChargingAttack(true);
            gorilla.performAnimation(new Animation(7227 + GameSettings.OSRS_ANIM_OFFSET));
            gorilla.getCombatBuilder().setContainer(new CombatContainer(gorilla, player, 1, 2, CombatType.RANGED, true));
            TaskManager.submit(new Task(1, gorilla, false) {
                int tick = 0;

                @Override
                public void execute() {
                    if (tick == 1) {
                        new Projectile(gorilla, player, 1302 + GameSettings.OSRS_GFX_OFFSET, 44, 3, 43, 43, 0).sendProjectile();
                        gorilla.setChargingAttack(false);
                        gorilla.gorillaAttacks++;
                        stop();
                    }
                    tick++;
                }
            });
        }
        if (type == CombatType.MAGIC) {
            gorilla.setChargingAttack(true);
            gorilla.performAnimation(new Animation(7238 + GameSettings.OSRS_ANIM_OFFSET));
            gorilla.getCombatBuilder().setContainer(new CombatContainer(gorilla, player, 1, 2, CombatType.MAGIC, true));
            TaskManager.submit(new Task(1, gorilla, false) {
                int tick = 0;

                @Override
                public void execute() {
                    if (tick == 1) {
                        new Projectile(gorilla, player, 1305 + GameSettings.OSRS_GFX_OFFSET, 44, 3, 43, 43, 0).sendProjectile();
                        gorilla.setChargingAttack(false);
                        gorilla.gorillaAttacks++;
                        stop();
                    }
                    tick++;
                }
            });
        }
        Position playerPosition = player.getPosition();
        if (type == CombatType.ROCK_FALL) {
            gorilla.setChargingAttack(true);
            gorilla.performAnimation(new Animation(7228 + GameSettings.OSRS_ANIM_OFFSET));
            TaskManager.submit(new Task(1, gorilla, false) {
                int tick = 0;

                @Override
                public void execute() {
                    if (tick == 1) {
                        new Projectile(gorilla.getPosition(), playerPosition, -1, 856 + GameSettings.OSRS_GFX_OFFSET, 100, 0, 100, 0, 0).sendProjectile();
                    }
                    if (tick >= 3) {
                        if (playerPosition.sameAs(player.getPosition())) {
                            player.dealDamage(new Hit(200 + Misc.getRandom(250), Hitmask.RED, CombatIcon.NONE));
                        }
                        gorilla.setChargingAttack(false);
                        stop();
                    }
                    tick++;
                }
            });
        }
    }

    private void setAttackType(NPC gorilla) {
        int z = Misc.random(10);
        if (z == 0) {
            gorilla.gorillaType = CombatType.ROCK_FALL;
            return;
        }
        int x = Misc.random(2);
        if (x == 0) {
            if (gorilla.gorillaType == CombatType.MELEE) {
                int y = Misc.random(1);
                if (y == 0) {
                    gorilla.gorillaType = CombatType.RANGED;
                } else if (y == 1) {
                    gorilla.gorillaType = CombatType.MAGIC;
                }
                return;
            }
            gorilla.gorillaType = CombatType.MELEE;
        } else if (x == 1) {
            if (gorilla.gorillaType == CombatType.RANGED) {
                int y = Misc.random(1);
                if (y == 0) {
                    gorilla.gorillaType = CombatType.MELEE;
                } else if (y == 1) {
                    gorilla.gorillaType = CombatType.MAGIC;
                }
                return;
            }
            gorilla.gorillaType = CombatType.RANGED;
        } else if (x == 2) {
            if (gorilla.gorillaType == CombatType.MAGIC) {
                int y = Misc.random(1);
                if (y == 0) {
                    gorilla.gorillaType = CombatType.MELEE;
                } else if (y == 1) {
                    gorilla.gorillaType = CombatType.RANGED;
                }
                return;
            }
            gorilla.gorillaType = CombatType.MAGIC;
        }
        gorilla.gorillaSwitchAttacks = false;
    }

    private void switchGorilla(NPC gorilla, Player player) {
        determineStrategy(player);
        if (player.getCurrentCombatType() == CombatType.MELEE) {
            if (gorilla.getId() == 22147 || gorilla.getTransformationId() == 22147) {
                int x = Misc.random(1);
                if (x == 0) {
                    gorilla.setTransformationId(22145);
                } else if (x == 1) {
                    gorilla.setTransformationId(22146);
                }
            } else {
                gorilla.setTransformationId(22147);
            }
        }
        if (player.getCurrentCombatType() == CombatType.RANGED) {
            if (gorilla.getId() == 22146 || gorilla.getTransformationId() == 22146) {
                int x = Misc.random(1);
                if (x == 0) {
                    gorilla.setTransformationId(22145);
                } else if (x == 1) {
                    gorilla.setTransformationId(22147);
                }
            } else {
                gorilla.setTransformationId(22146);
            }
        }
        if (player.getCurrentCombatType() == CombatType.MAGIC) {
            if (gorilla.getId() == 22145 || gorilla.getTransformationId() == 22145) {
                int x = Misc.random(1);
                if (x == 0) {
                    gorilla.setTransformationId(22146);
                } else if (x == 1) {
                    gorilla.setTransformationId(22147);
                }
            } else {
                gorilla.setTransformationId(22145);
            }
        }
        gorilla.getUpdateFlag().flag(Flag.TRANSFORM);
        gorilla.switchGorilla = false;
    }

    @Override
    public int attackDelay(GameCharacter entity) {
        return entity.getAttackSpeed();
    }

    @Override
    public int attackDistance(GameCharacter entity) {
        if (((NPC) entity).gorillaType == CombatType.MELEE)
            return 1;
        return 6;
    }

    @Override
    public CombatType getCombatType() {
        return CombatType.MIXED;
    }

    public static void determineStrategy(Player player) {
        if (player.isSpecialActivated() && player.getCastSpell() == null) {
            if (player.getCombatSpecial().getCombatType() == CombatType.MELEE) {
                player.setCurrentCombatType(CombatType.MELEE);
            } else if (player.getCombatSpecial().getCombatType() == CombatType.RANGED) {
                player.setCurrentCombatType(CombatType.RANGED);
            } else if (player.getCombatSpecial().getCombatType() == CombatType.MAGIC) {
                player.setCurrentCombatType(CombatType.MAGIC);
            }
            return;
        }

        if (player.getCastSpell() != null || player.getAutocastSpell() != null || player.getEquipment().get(Equipment.WEAPON_SLOT).getId() == 42899
                || player.getEquipment().get(Equipment.WEAPON_SLOT).getId() == 41907) {
            player.setCurrentCombatType(CombatType.MAGIC);
            return;
        }

        CombatRangedAmmo.RangedWeaponData data = CombatRangedAmmo.RangedWeaponData.getData(player);
        if (data != null) {
            player.setCurrentCombatType(CombatType.RANGED);
            return;
        }
        player.setCurrentCombatType(CombatType.MELEE);
    }

}
