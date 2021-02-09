package com.zyrox.net.packet.impl;

import com.zyrox.net.packet.Packet;
import com.zyrox.net.packet.PacketListener;
import com.zyrox.util.Misc;
import com.zyrox.world.content.youtube.YouTubeManager;
import com.zyrox.world.entity.impl.player.Player;

public class InputPacketListener implements PacketListener {

    @Override
    public void handleMessage(Player player, Packet packet) {

        String inputText = Misc.readString(packet.getBuffer());

        YouTubeManager.postVideo(player, inputText);
    }
}