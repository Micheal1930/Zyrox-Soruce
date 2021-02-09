package com.zyrox.world.content.teleport.impl;

import java.util.Arrays;
import java.util.Comparator;

import com.zyrox.model.Position;
import com.zyrox.util.Misc;
import com.zyrox.world.content.instances.InstanceManager;
import com.zyrox.world.content.teleport.Teleport;
import com.zyrox.world.content.teleport.TeleportCategory;
import com.zyrox.world.entity.impl.player.Player;

public enum MinigameTeleport implements Teleport {

    BARROWS(new Position(3565, 3313, 0), 1271),
    FIGHT_CAVES(new Position(2445, 5177, 0), 1287),
    FIGHT_PITS(new Position(2399, 5177, 0), 1288),
    PEST_CONTROL(new Position(2663, 2654, 0), 1289),
    DUEL_ARENA(new Position(3364, 3267, 0), 1290),
    WARRIORS_GUILD(new Position(2855, 3543, 0), 1291),
    RECIPE_FOR_DISASTER(new Position(1863, 5354, 0), 1292),
    NOMADS_REQUEIM(new Position(1891, 3177, 0), 1293),
    TREASURE_ISLAND(new Position(3039, 2910, 0), 1294),
    ZOMBIE_MINIGAME(new Position(3503, 3564, 0), 1295),
    RAIDS_GREAT_OLM(new Position(1230, 3558, 0), 1296),
    CASTLE_WARS(new Position(2441, 3091, 0), 1297),

    THEATRE_OF_BLOOD(new Position(3670, 3219, 0), 1298) {

    },


    INFERNO(new Position(2441, 3091, 0), 1299) {
        @Override
        public boolean customTeleport(Player player) {
            if (player.getWildernessLevel() > 0) {
                return false;
            }
            player.getPacketSender().sendInterfaceRemoval();
            InstanceManager.get().enterInferno(player);
            return true;
        }
    },

    ;

    private final Position position;
    private final int spriteId;

    MinigameTeleport(Position position, int spriteId) {
        this.position = position;
        this.spriteId = spriteId;
    }

    public static final MinigameTeleport[] VALUES = values();

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
        return TeleportCategory.MINIGAMES;
    }

    static {
        Arrays.sort(VALUES, new Comparator<MinigameTeleport>() {

            public int compare(MinigameTeleport left, MinigameTeleport right){
                return left.getName().compareTo(right.getName());
            }
        });
    }

    @Override
    public int getSpriteId() {
        return spriteId;
    }
}