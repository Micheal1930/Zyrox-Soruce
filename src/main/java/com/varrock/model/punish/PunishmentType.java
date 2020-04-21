package com.varrock.model.punish;

import java.util.HashMap;

import com.varrock.model.PlayerRights;
import com.varrock.util.Misc;
import com.varrock.world.entity.impl.player.Player;

/**
 * Created by Jonny on 8/12/2019
 **/
public enum PunishmentType {

    BANNED(PlayerRights.MODERATOR, true),
    IP_BANNED(PlayerRights.GLOBAL_MOD, true),
    J_BANNED(PlayerRights.GLOBAL_MOD, true),
    //MAC_BANNED(PlayerRights.GLOBAL_MOD, true),
    MUTED(PlayerRights.SUPPORT, false),
    J_MUTED(PlayerRights.MODERATOR, false),
    GAMBLE_BAN(PlayerRights.MODERATOR, false),

    ;

    public PlayerRights minimumStaffRights;
    public boolean needsKicked;

    public HashMap<String, Punishment> punishments = new HashMap<>();

    PunishmentType(PlayerRights staffRights, boolean needsKicked) {
        this.minimumStaffRights = staffRights;
        this.needsKicked = needsKicked;
    }

    public String getName() {
        return Misc.formatPlayerName(name().toLowerCase().replaceAll("_", " "));
    }

    public String getDataForPlayer(Player player) {
        if(player == null) {
            return null;
        }
        switch(this) {
            case MUTED:
            case BANNED:
            case GAMBLE_BAN:
                return Misc.formatPlayerName(player.getName());
            case IP_BANNED:
                return player.getHostAddress();
            case J_MUTED:
            case J_BANNED:
                return player.getjSerial();
          /*  case MAC_BANNED:
                return player.getSerialNumber();*/
        }
        return null;
    }

}
