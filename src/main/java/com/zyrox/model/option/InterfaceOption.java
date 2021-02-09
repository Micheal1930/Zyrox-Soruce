package com.zyrox.model.option;

import com.zyrox.world.entity.impl.player.Player;

/**
 * Created by Jonny on 6/22/2019
 **/
public interface InterfaceOption {

    void onAction(Player player, int itemId);

}
