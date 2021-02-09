package com.zyrox.world.content.combat.strategy.impl;

import static com.zyrox.util.Misc.inclusiveRandom;

import java.util.ArrayList;

import com.zyrox.GameSettings;
import com.zyrox.engine.task.Task;
import com.zyrox.engine.task.TaskManager;
import com.zyrox.model.*;
import com.zyrox.model.projectile.Projectile;
import com.zyrox.util.Misc;
import com.zyrox.world.content.combat.CombatContainer;
import com.zyrox.world.content.combat.CombatType;
import com.zyrox.world.content.combat.strategy.CombatStrategy;
import com.zyrox.world.entity.impl.GameCharacter;
import com.zyrox.world.entity.impl.npc.NPC;
import com.zyrox.world.entity.impl.player.Player;

public class GalvekCombatStrategy implements CombatStrategy {

    public final Animation MELEE_ANIM_AOE = new Animation(7912 + GameSettings.OSRS_ANIM_OFFSET);

    public final Animation RANGE_ANIM = new Animation(7900 + GameSettings.OSRS_ANIM_OFFSET);

    public final Animation MAGIC_ANIM_AOE = new Animation(7901 + GameSettings.OSRS_ANIM_OFFSET);
    public final int MAGIC_PROJECTILE_SPECIAL = 134;
    public final Graphic MAGIC_IMPACT_SPECIAL = new Graphic(197, GraphicHeight.LOW);

    public static final Animation PHASE_UP_ANIM = new Animation(7906 + GameSettings.OSRS_ANIM_OFFSET);
    public static final Animation PHASE_DOWN_ANIM = new Animation(7908 + GameSettings.OSRS_ANIM_OFFSET);

    public final int RANGED_PROJECTILE = 60;

    private Stage stage;

    public GalvekCombatStrategy(int id) {
        stage = Stage.forId(id);
    }

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
        if (entity.getConstitution() <= 0 || victim.getConstitution() <= 0) {
            return true;
        }

        switch (stage) {
            case ORANGE:
                meleeAttack((NPC) entity, (Player) victim);
                //melee attack
                return true;
            case GREEN:
                //magic attack
                magicAttack((NPC) entity, (Player) victim);
                return true;
            case WHITE:
            case BLUE:
                //range attack
                rangeAttack((NPC) entity, (Player) victim);
                return true;
        }
        return false;
    }

    @Override
    public int attackDelay(GameCharacter entity) {
        return stage.getAttackDelay();
    }

    @Override
    public int attackDistance(GameCharacter entity) {
        return 10;
    }

    @Override
    public CombatType getCombatType() {
        return stage.getCombatType();
    }

    private void meleeAttack(NPC galvek, Player victim) {
        galvek.performAnimation(MELEE_ANIM_AOE);
        for (Player target : Misc.getCombinedPlayerList(victim)) {
            /*if (target.getName().equalsIgnoreCase(victim.getName())) {
                System.out.println("Same name.... " + victim.getName());
                continue;
            }*/
//            System.out.println("galvek target: " + target.getName());
            if (Locations.goodDistance(victim.getPosition().copy(), target.getPosition().copy(), 2)
                    || Locations.goodDistance(galvek.getPosition().copy(), target.getPosition().copy(), 4)) {
                galvek.getCombatBuilder().setContainer(new CombatContainer(galvek, target, 1, 1, CombatType.MELEE, true));
                target.dealDamage(new Hit(50 + Misc.getRandom(50), Hitmask.RED, CombatIcon.MELEE));
            }
        }
    }

    private void rangeAttack(NPC galvek, Player victim) {
        galvek.performAnimation(RANGE_ANIM);
        for (Player target : Misc.getCombinedPlayerList(victim)) {
            new Projectile(galvek, target, RANGED_PROJECTILE, 44, 3, 31, 31, 0).sendProjectile();
            target.dealDamage(new Hit(100 + Misc.getRandom(50), Hitmask.RED, CombatIcon.RANGED));
        }

    }

    public String[] CHATS = new String[] {
            "ROOOOAAAAARRRRR!!!",
            "THIS IS MY TERRITORY!!", // Stay out of my territory
            "ROOOOAAARRR!!!!",
            "YOU ARE MINE NOW!!",
            "YOU CAN NOT DEFEAT ME!!!",
            "ROOOOAAAARRR!!",
            "YOU FOOLS!!"
    };

    public Position[] massProjectiles(Position targetPos) {
        final int SIZE = 10;
        Position[] coords = new Position[15];
        Position left = targetPos.copy().add(-3, 0);
        Position bottom = targetPos.copy().add(0, -2);
        for (int i = 0; i < SIZE; i += 2) {
            coords[i] = left.copy().add(i, 0);
            coords[i + 1] = bottom.copy().add(0, i - 1);
        }
        return coords;
    }

    private void magicAttack(NPC galvek, Player victim) {
        galvek.performAnimation(MAGIC_ANIM_AOE);
        galvek.forceChat(CHATS[inclusiveRandom(0, CHATS.length - 1)]);
        galvek.getCombatBuilder().setContainer(new CombatContainer(galvek, victim, 1, 3, CombatType.DRAGON_FIRE, true));
        ArrayList<String> targets = new ArrayList<>();
        Position[] pos = massProjectiles(victim.getPosition());
        for (Player target : Misc.getCombinedPlayerList(victim)) {
            boolean canAttack = true;
            for (String name : targets) {
                if (target.getName().equalsIgnoreCase(name))
                    canAttack = false;
            }
            if (!canAttack) {
                continue;
            }
            new Projectile(galvek, target, MAGIC_PROJECTILE_SPECIAL, 44, 3, 31, 31, 0).sendProjectile();
            new Projectile(galvek.getPosition(), pos[inclusiveRandom(0, CHATS.length - 1)], 0, MAGIC_PROJECTILE_SPECIAL, 44, 3, 31, 31, 0).sendProjectile();
            new Projectile(galvek.getPosition(), pos[inclusiveRandom(0, CHATS.length - 1)], 0, MAGIC_PROJECTILE_SPECIAL, 44, 3, 31, 31, 0).sendProjectile();
            new Projectile(galvek.getPosition(), pos[inclusiveRandom(0, CHATS.length - 1)], 0, MAGIC_PROJECTILE_SPECIAL, 44, 3, 31, 31, 0).sendProjectile();
            target.dealDamage(new Hit(100 + Misc.getRandom(50), Hitmask.RED, CombatIcon.MAGIC));
            targets.add(target.getName());
        }
    }

    public static void phase(NPC galvek) {
        galvek.performAnimation(PHASE_UP_ANIM);
        TaskManager.submit(new Task(2) {
            @Override
            protected void execute() {
                int id = galvek.getId() + 1;
                galvek.setTransformationId(id);
                galvek.getUpdateFlag().flag(Flag.TRANSFORM);
                galvek.performAnimation(PHASE_DOWN_ANIM);
                stop();
            }
        });
    }

    public enum Stage {

        ORANGE(23095, CombatType.MELEE,  8),

        BLUE(23096, CombatType.RANGED, 6),

        WHITE(23097, CombatType.RANGED, 6),

        GREEN(23098, CombatType.DRAGON_FIRE, 7);

        Stage(int id, CombatType combatType, int attackDelay) {
            this.id = id;
            this.attackDelay = attackDelay;
            this.combatType = combatType;
        }

        private int id;

        private int attackDelay;

        private CombatType combatType;

        public int getId() {
            return id;
        }

        public int getAttackDelay() {
            return attackDelay;
        }

        public CombatType getCombatType() {
            return combatType;
        }

        public static Stage forId(int id) {
            for (Stage x : Stage.values()) {
                if (x.getId() == id) {
                    return x;
                }
            }
            return null;
        }
    }
}
