package com.zyrox.world.content.teleport;

import com.zyrox.model.Position;
import com.zyrox.world.entity.impl.player.Player;

/**
 * Created by Jonny on 4/25/2019
 **/
public interface Teleport {

    public Position getPosition();

    public String getName();

    public String getEnumName();

    public TeleportCategory getCategory();

    public boolean customTeleport(Player player);

    public int getSpriteId();

}
