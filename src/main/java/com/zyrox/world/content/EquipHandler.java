package com.zyrox.world.content;

import com.zyrox.world.entity.impl.player.Player;

public class EquipHandler {


    public static void open(Player player) {
        player.getPacketSender().sendInterface(66000);
        player.getPacketSender().sendString(66004,  player.getUsername());
        player.getPacketSender().sendString(66005, player.getTitle());
        player.getPacketSender().sendString(66006,"Cb Level: " + player.getSkillManager().getCombatLevel());
        player.getPacketSender().sendString(66007,"Total Level: " + player.getSkillManager().getTotalLevel());
    }
}
