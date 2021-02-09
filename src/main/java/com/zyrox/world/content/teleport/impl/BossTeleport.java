package com.zyrox.world.content.teleport.impl;

import java.util.Arrays;
import java.util.Comparator;

import com.zyrox.model.Locations;
import com.zyrox.model.Position;
import com.zyrox.util.Misc;
import com.zyrox.world.content.combat.strategy.zulrah.ZulrahConstants;
import com.zyrox.world.content.teleport.Teleport;
import com.zyrox.world.content.teleport.TeleportCategory;
import com.zyrox.world.content.transportation.TeleportHandler;
import com.zyrox.world.entity.impl.player.Player;

public enum BossTeleport implements Teleport {

    KING_BLACK_DRAGON(new Position(3001, 3849, 0), 1270),

    GODWARS(new Position(2871, 5319, 2), 1313),

    KALPHITE_QUEEN(new Position(3488, 9516, 0), 1314),

    SLASH_BASH(new Position(2421, 4690, 0), 1315),

    DAGANNOTH_KINGS(new Position(1912, 4367, 0), 1316),

    TORMENTED_DEMONS(new Position(2540, 5774, 0), 1317),

    CHAOS_ELEMENTAL(new Position(3276, 3915, 0), 1318),

    CORPOREAL_BEAST(new Position(2886, 4376, 0), 1319),

    BORK_CAVE(new Position(3104, 5536, 0), 1320),

    BARRELCHEST(new Position(2973, 9517, 1), 1321),

    LIZARDMAN_SHAMAN(new Position(2718, 9811, 0), 1322),

    PHOENIX_CAVE(new Position(2839, 9557, 0), 1323),

    BANDOS_AVATAR(new Position(2891, 4767, 0), 1324),

    GLACORS_CAVE(new Position(3050, 9573, 0), 1325),

    NEX_DUNGEON(new Position(2903, 5203, 0), 1326),

    SCORPIA(new Position(3236, 3941, 0), 1327),

    VENENATIS(new Position(3350, 3734, 0), 1328),

    CERBERUS(new Position(1240, 1226, 0), 1312),

    SKOTIZO(new Position(3378, 9816, 0), 1329),

    ABYSSAL_SIRE(new Position(3370, 3888, 0), 1330),

    GIANT_MOLE(new Position(1761, 5181, 0), 1331),

    KRAKEN(new Position(2804, 9146, 0), 1332) {
        @Override
        public boolean customTeleport(Player player) {
            player.getKraken().enter(player, true);
            return true;
        }
    },

    VORKATH(new Position(2272, 4054, 0), 1333) {
        @Override
        public boolean customTeleport(Player player) {
            TeleportHandler.startVorkath(player);
            return true;
        }
    },

    HYDRA(new Position(1351, 10258), 1334),

    TEKTON(new Position(3053, 5210), 1348),

    ZULRAH(new Position(1, 1), 1349) {
        @Override
        public boolean customTeleport(Player player) {
            if(!player.getZulrahTimer().elapsed(10_000)) {
                player.sendMessage("You can only use this teleport every 10 seconds.");
                return true;
            }
            if (Locations.inZulrah(player)) {
                if (player.lastZulrah != null && player.lastZulrah.isRegistered()) {
                    player.sendMessage("You must kill Zulrah before using this command again.");
                    return true;
                }
                ZulrahConstants.startBossFight(player);
                return true;
            }
            TeleportHandler.teleportZulrah(player);
            player.getZulrahTimer().reset();
            return true;
        }
    },

    TARN(new Position(1351, 10258), 1335) {
        @Override
        public boolean customTeleport(Player player) {
            TeleportHandler.startTarn(player);
            return true;
        }
    };

    private final Position position;

    private final int spriteId;

    BossTeleport(Position position, int spriteId) {
        this.position = position;
        this.spriteId = spriteId;
    }

    public static final BossTeleport[] VALUES = values();

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
        return TeleportCategory.BOSSES;
    }

    static {
        Arrays.sort(VALUES, new Comparator<BossTeleport>() {

            public int compare(BossTeleport left, BossTeleport right){
                return left.getName().compareTo(right.getName());
            }
        });
    }

    @Override
    public int getSpriteId() {
        return spriteId;
    }
}