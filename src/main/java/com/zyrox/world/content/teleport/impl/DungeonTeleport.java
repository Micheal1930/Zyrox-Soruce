package com.zyrox.world.content.teleport.impl;

import java.util.Arrays;
import java.util.Comparator;

import com.zyrox.model.Position;
import com.zyrox.util.Misc;
import com.zyrox.world.content.teleport.Teleport;
import com.zyrox.world.content.teleport.TeleportCategory;
import com.zyrox.world.entity.impl.player.Player;

public enum DungeonTeleport implements Teleport {

    EDGEVILLE_DUNGEON(new Position(3097, 9870, 0), 1300),
    CHAOS_TUNNELS(new Position(3185, 5471, 0), 1301),
    BRIMHAVEN_DUNGEON(new Position(2713, 9564, 0), 1302),
    TAVERLY_DUNGEON(new Position(2884, 9797, 0), 1303),
    STRYKEWYRM_DUNGEON(new Position(2731, 5095, 0), 1304),
    ANCIENT_CAVERN(new Position(1745, 5325, 0), 1305),
    METAL_DRAGONS(new Position(2711, 9464, 0), 1306),
    APE_ATOLL_DUNGEON(new Position(2804, 9146, 0), 1307),
    SLAYER_TOWER(new Position(3429, 3538, 0), 1308),
    FREMMENIK_DUNGEON(new Position(2805, 10001, 0), 1309),
    KURASK_DUNGEON(new Position(2516, 4635, 0), 1310),
    WARMONGER_DUNGEON(new Position(3105, 4378, 0), 1311),

    ;

    private final Position position;
    private final int spriteId;

    DungeonTeleport(Position position, int spriteId) {
        this.position = position;
        this.spriteId = spriteId;
    }

    public static final DungeonTeleport[] VALUES = values();

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
        return TeleportCategory.DUNGEONS;
    }

    static {
        Arrays.sort(VALUES, new Comparator<DungeonTeleport>() {

            public int compare(DungeonTeleport left, DungeonTeleport right){
                return left.getName().compareTo(right.getName());
            }
        });
    }

    @Override
    public int getSpriteId() {
        return spriteId;
    }
}