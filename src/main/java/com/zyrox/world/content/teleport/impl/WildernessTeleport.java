package com.zyrox.world.content.teleport.impl;

import java.util.Arrays;
import java.util.Comparator;

import com.zyrox.model.Position;
import com.zyrox.util.Misc;
import com.zyrox.world.content.teleport.Teleport;
import com.zyrox.world.content.teleport.TeleportCategory;
import com.zyrox.world.entity.impl.player.Player;

public enum WildernessTeleport implements Teleport {

    ZOMBIE_GRAVEYARD(new Position(3166, 3682, 0), 1337),
    GREATER_DEMONS(new Position(3288, 3886, 0), 1338),
    WILDERNESS_CASTLE(new Position(3005, 3631, 0), 1339),
    WEST_DRAGONS(new Position(2980, 3599, 0), 1340),
    EAST_DRAGONS(new Position(3339, 3667, 0), 1336),
    CHAOS_ALTAR(new Position(3239, 3619, 0), 1341),
    RUNE_ROCKS(new Position(3061, 3886, 0), 1342),
    REVENANT_CAVE(new Position(3126, 3833, 0), 1343),
    ROGUES_CASTLE(new Position(3286, 3922, 0), 1344),
    ICE_PLATEAU(new Position(2953, 3901, 0), 1345),
    SAFE_PVP_ARENA(new Position(2815, 5511, 0), 1346),

    ;

    private final Position position;
    private final int spriteId;

    WildernessTeleport(Position position, int spriteId) {
        this.position = position;
        this.spriteId = spriteId;
    }

    public static final WildernessTeleport[] VALUES = values();

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
        return TeleportCategory.WILDERNESS;
    }

    static {
        Arrays.sort(VALUES, new Comparator<WildernessTeleport>() {

            public int compare(WildernessTeleport left, WildernessTeleport right){
                return left.getName().compareTo(right.getName());
            }
        });
    }

    @Override
    public int getSpriteId() {
        return spriteId;
    }
}