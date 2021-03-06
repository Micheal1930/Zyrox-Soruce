package com.zyrox.world.content.raids.theatre_of_blood.exit;

import com.zyrox.model.GameObject;
import com.zyrox.model.GameObjectClickType;
import com.zyrox.model.Position;
import com.zyrox.world.content.CustomObjects;
import com.zyrox.world.entity.impl.player.Player;

/**
 * Created by Jonny on 7/8/2019
 **/
public class VerzikViturExitCave extends GameObject {

    public VerzikViturExitCave(int id, Position position, int type, int face, boolean collectedStaff) {
        super(id, position, type, face);

        CustomObjects.spawnGlobalObject(this);
    }

    @Override
    public void clickObject(Player player, GameObjectClickType gameObjectClickType) {
        if(gameObjectClickType == GameObjectClickType.FIRST_CLICK) {
            player.moveTo(new Position(3237, 4308, player.getPosition().getZ()));
        }
    }

}
