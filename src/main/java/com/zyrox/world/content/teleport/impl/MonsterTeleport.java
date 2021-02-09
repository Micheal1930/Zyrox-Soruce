package com.zyrox.world.content.teleport.impl;

import java.util.Arrays;
import java.util.Comparator;

import com.zyrox.model.Position;
import com.zyrox.util.Misc;
import com.zyrox.world.content.teleport.Teleport;
import com.zyrox.world.content.teleport.TeleportCategory;
import com.zyrox.world.entity.impl.player.Player;

public enum MonsterTeleport implements Teleport {

    CHICKEN_PEN(new Position(3235, 3295, 0), 1276),
    ROCK_CRABS(new Position(2679, 3714, 0), 1284),
    CHAOS_DRUIDS(new Position(2933, 9848, 0), 1275),
    YAK_FIELD(new Position(3206, 3263, 0), 1286),
    EXPERIMENTS(new Position(3561, 9948, 0), 1279),
    GHOUL_FIELD(new Position(3420, 3510, 0), 1281),
    BANDIT_CAMP(new Position(3169, 2982, 0), 1274),
    ARMOURED_ZOMBIES(new Position(3086, 9672, 0), 1273),
    DUST_DEVILS(new Position(3277, 2964, 0), 1278),
    MONKEY_SKELETONS(new Position(2805, 9143, 0), 1283),
    MONKEY_GUARDS(new Position(2793, 2773, 0), 1282),
    TZHAAR_MINIONS(new Position(2480, 5174, 0), 1285),
    FROST_DRAGONS(new Position(2835, 9517, 0), 1280),
    ANCIENT_WYVERNS(new Position(1667, 5676, 0), 1272),
    DEMONIC_GORILLAS(new Position(2128, 5647, 0), 1277),

    ;

    private final Position position;
    private final int spriteId;

    MonsterTeleport(Position position, int spriteId) {
        this.position = position;
        this.spriteId = spriteId;
    }

    public static final MonsterTeleport[] VALUES = values();

    @Override
    public String getName() {
        return Misc.formatText(name().toLowerCase().replaceAll("_", " "));
    }

    @Override
    public String getEnumName() {
        return this.toString();
    }

    @Override
    public Position getPosition() {
        return position;
    }

    @Override
    public boolean customTeleport(Player player) {
        return false;
    }

    @Override
    public TeleportCategory getCategory() {
        return TeleportCategory.MONSTERS;
    }

    static {
        Arrays.sort(VALUES, new Comparator<MonsterTeleport>() {

            public int compare(MonsterTeleport left, MonsterTeleport right){
                return left.getName().compareTo(right.getName());
            }
        });
    }

    @Override
    public int getSpriteId() {
        return spriteId;
    }
}