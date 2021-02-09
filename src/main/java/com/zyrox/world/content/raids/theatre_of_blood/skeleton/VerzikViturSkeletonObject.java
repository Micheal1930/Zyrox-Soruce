package com.zyrox.world.content.raids.theatre_of_blood.skeleton;

import com.zyrox.model.GameObject;
import com.zyrox.model.GameObjectClickType;
import com.zyrox.model.Position;
import com.zyrox.util.Misc;
import com.zyrox.world.World;
import com.zyrox.world.content.CustomObjects;
import com.zyrox.world.content.raids.theatre_of_blood.verzik_vitur.VerzikViturConstants;
import com.zyrox.world.entity.impl.player.Player;

/**
 * Created by Jonny on 7/8/2019
 **/
public class VerzikViturSkeletonObject extends GameObject {

    private boolean collectedStaff;

    private boolean canCollectStaff;

    public VerzikViturSkeletonObject(int id, Position position, int type, int face, boolean collectedStaff) {
        super(id, position, type, face);

        CustomObjects.spawnGlobalObject(this);

        this.collectedStaff = collectedStaff;
    }

    @Override
    public void clickObject(Player player, GameObjectClickType gameObjectClickType) {

        if(gameObjectClickType == GameObjectClickType.FIRST_CLICK) {
            if(!canCollectStaff || collectedStaff || player.getInventory().getFreeSlots() <= 0) {
                player.sendMessage("This skeleton doesn't seem to have anything of interest.");
                return;
            }
            collectStaff(player);
        }
    }

    public void collectStaff(Player player) {
        World.deregister(this);
        CustomObjects.deleteGlobalObject(this);

        this.collectedStaff = true;

        player.getRaids().getTheatreOfBlood().spawnBlankSkeleton();

        player.getInventory().add(VerzikViturConstants.DAWNBRINGER_ID, 1);
        player.getRaids().getTheatreOfBlood().sendMessageToParty("<col=4EB2C7><shad=0>"+Misc.formatPlayerName(player.getName())+" has collected the dawnbringer's staff.");

    }

    public void setCanCollectStaff(boolean canCollectStaff) {
        this.canCollectStaff = canCollectStaff;
    }
}
