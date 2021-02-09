package com.zyrox.world.content.teleport;

import com.zyrox.model.Locations;
import com.zyrox.world.content.transportation.TeleportHandler;
import com.zyrox.world.entity.impl.player.Player;

/**
 * Created by Jonny on 4/25/2019
 **/
public class TeleportManager {

    private static final int INTERFACE_ID = 51000;

    public static boolean isButton(Player player, int buttonId) {
        switch(buttonId) {
            case 1716: //teleport icon
            case 51009: //teleport interface
            case 11008: //training in moderns
                TeleportManager.sendInterface(player, TeleportCategory.MONSTERS);
                return true;
            case 51013: //teleport interface
            case 11017: //minigames in moderns
                TeleportManager.sendInterface(player, TeleportCategory.MINIGAMES);
                return true;
            case 51017: //teleport interface
            case 11014: //bosses in moderns
                TeleportManager.sendInterface(player, TeleportCategory.BOSSES);
                return true;
            case 11020: //wilderness in moderns
            case 51021: //teleport interface
                TeleportManager.sendInterface(player, TeleportCategory.WILDERNESS);
                return true;
            case 11011: //dungeons in moderns
            case 51025: //teleport interface
                TeleportManager.sendInterface(player, TeleportCategory.DUNGEONS);
                return true;
        }

        if(player.teleportCategory != null) {
            if (buttonId >= 51101 && buttonId <= 51247) {
                int index = buttonId - 51101;

                if (index > 0) {
                    index /= 5;
                }

                if (index >= player.teleportCategory.getValues().length) {
                    return true;
                }

                Teleport teleport = player.teleportCategory.getValues()[index];

                switch(buttonId) {
                    case 51102:
                    case 51107:
                    case 51112:
                    case 51117:
                    case 51122:
                    case 51127:
                    case 51132:
                    case 51137:
                    case 51142:
                    case 51147:
                    case 51152:
                    case 51157:
                    case 51162:
                    case 51167:
                    case 51172:
                    case 51177:
                    case 51182:
                    case 51187:
                    case 51192:
                    case 51197:
                    case 51202:
                    case 51207:
                    case 51212:
                    case 51217:
                    case 51222:
                    case 51227:
                    case 51232:
                    case 51237:
                    case 51242:
                    case 51247:
                        addFavorite(player, teleport);
                        return true;
                }

                addRecentTeleport(player, teleport);

                if(!teleport.customTeleport(player)) {
                    TeleportHandler.teleportPlayer(player, teleport.getPosition(), player.getSpellbook().getTeleportType());
                }

                return true;
            }
            if (buttonId >= 51029 && buttonId <= 51041) {
                int index = buttonId - 51029;

                if (index > 0) {
                    index /= 2;
                }

                if (index >= player.getFavoriteTeleports().size()) {
                    return true;
                }

                Teleport teleport = player.getFavoriteTeleports().get(index);

                switch(buttonId) {
                    case 51030:
                    case 51032:
                    case 51034:
                    case 51036:
                    case 51038:
                    case 51040:
                        removeFavorite(player, teleport);
                        return true;
                }

                addRecentTeleport(player, teleport);

                if(!teleport.customTeleport(player)) {
                    TeleportHandler.teleportPlayer(player, teleport.getPosition(), player.getSpellbook().getTeleportType());
                }

                return true;
            }
            if (buttonId >= 51043 && buttonId <= 51047) {
                int index = buttonId - 51043;

                if (index > 0) {
                    index /= 2;
                }

                if (index >= player.getRecentTeleports().size()) {
                    return true;
                }

                Teleport teleport = player.getRecentTeleports().get(index);

                if(!teleport.customTeleport(player)) {
                    TeleportHandler.teleportPlayer(player, teleport.getPosition(), player.getSpellbook().getTeleportType());
                }

                return true;
            }
        }

        return false;
    }

    public static void addFavorite(Player player, Teleport teleport) {
        if(player.getFavoriteTeleports().size() >= 10) {
            player.sendMessage("You have too many favorite teleports. Please right click a teleport and select 'remove'");
            return;
        }

        for(Teleport favTeleport : player.getFavoriteTeleports()) {
            if(favTeleport.getName().equalsIgnoreCase(teleport.getName())) {
                player.sendMessage("You already have this teleport added as a favorite.");
                return;
            }
        }

        player.getFavoriteTeleports().add(teleport);

        updateFavorites(player);
    }

    public static void removeFavorite(Player player, Teleport teleport) {

        player.getFavoriteTeleports().remove(teleport);

        updateFavorites(player);
    }

    public static void updateFavorites(Player player) {
        int lineId = 51028;

        for(int i = 0; i < 7; i++) {
            Teleport teleport = player.getFavoriteTeleports().size() > i ? player.getFavoriteTeleports().get(i) : null;
            player.getPacketSender().sendString(lineId+=2, teleport == null ? "" : teleport.getName());
        }

    }

    public static void updateRecent(Player player) {
        int lineId = 51042;

        for(int i = 0; i < 3; i++) {
            Teleport teleport = player.getRecentTeleports().size() > i ? player.getRecentTeleports().get(i) : null;
            player.getPacketSender().sendString(lineId+=2, teleport == null ? "" : teleport.getName());
        }

    }

    public static void sendInterface(Player player, TeleportCategory category) {
        player.teleportCategory = category;

        player.getPacketSender().sendString(51003, category.getName());

        player.getPacketSender().sendTeleportContainer("", 0, 0, false, true);

        for (Teleport teleport : category.getValues()) {

            int y = teleport.getPosition().getY();
            int wilderness = ((((y > 6400 ? y - 6400 : y) - 3520) / 8) + 1);

            if(wilderness > 0 && !Locations.Location.inLocation(teleport.getPosition().getX(), y, Locations.Location.WILDERNESS)) {
                wilderness = 0;
            }

            Locations.Location location = Locations.Location.getLocation(teleport.getPosition());

            boolean multi = location == null ? false : location.multi;

            player.getPacketSender().sendTeleportContainer(teleport.getName(), wilderness, teleport.getSpriteId(), multi, false);
        }

        int scrollMax = (int) (Math.ceil(((float)category.getValues().length / 3f)) * 95 + 10);
        if(scrollMax < 239) {
            scrollMax = 239;
        }

        player.getPacketSender().sendScrollMax(51_100, scrollMax);

        updateFavorites(player);
        updateRecent(player);

        player.getPacketSender().sendInterface(INTERFACE_ID);
    }

    public static void addRecentTeleport(Player player, Teleport teleport) {
        if(player.getRecentTeleports().contains(teleport))
            return;

        if(player.getRecentTeleports().size() >= 3) {
            player.getRecentTeleports().remove(0);
        }

        player.getRecentTeleports().add(teleport);
    }
}
