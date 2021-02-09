package com.zyrox.world.content.donator_zone;

import com.zyrox.engine.task.Task;
import com.zyrox.engine.task.TaskManager;
import com.zyrox.model.*;
import com.zyrox.world.entity.impl.player.Player;

/**
 * Created by Jonny on 8/31/2019
 **/
public class DonatorZone {

    /**
     * The animation
     */
    private static final int STEPPING_STONE_ANIMATION = 769;

    public static boolean isObject(Player player, GameObject gameObject) {
        if(player.getLocation() != Locations.Location.DONATOR_ZONE) {
            return false;
        }

        switch(gameObject.getId()) {
            case 104411:
                handleSteppingStone(player, gameObject.getId(), gameObject.getPosition().getX(), gameObject.getPosition().getY());
                return true;
        }
        return false;
    }

    /**
     * Handles stepping stones
     *
     * @param player the player
     * @param id     the object id
     * @param x      the object x
     * @param y      the object y
     * @return stepping stones
     */
    public static void handleSteppingStone(Player player, int id, int x, int y) {
        int obX = x;
        int obY = y;
        int myX = player.getPosition().getX();
        int myY = player.getPosition().getY();
        int toX = 0;
        int toY = 0;

        if (obX == 2057 && obY == 3284) {
            if(!donatorCheck(player, PlayerRights.SUPER_DONATOR)) {
                return;
            }

            if (myX == 2057 && myY == 3283)
                toY = 1;
            if (myX == 2057 && myY == 3285)
                toY = -1;
        }

        if (obX == 2057 && obY == 3285) {
            if(!donatorCheck(player, PlayerRights.SUPER_DONATOR)) {
                return;
            }

            if (myX == 2057 && myY == 3284)
                toY = 1;
            if (myX == 2057 && myY == 3286)
                toY = -1;
        }

        if (obX == 2051 && obY == 3291) {
            if(!donatorCheck(player, PlayerRights.EXTREME_DONATOR)) {
                return;
            }

            if (myX == 2052 && myY == 3291)
                toX = -1;
            if (myX == 2050 && myY == 3291)
                toX = 1;
        }

        if (obX == 2050 && obY == 3291) {
            if(!donatorCheck(player, PlayerRights.EXTREME_DONATOR)) {
                return;
            }

            if (myX == 2051 && myY == 3291)
                toX = -1;
            if (myX == 2049 && myY == 3291)
                toX = 1;
        }

        if (obX == 2049 && obY == 3291) {
            if(!donatorCheck(player, PlayerRights.EXTREME_DONATOR)) {
                return;
            }

            if (myX == 2050 && myY == 3291)
                toX = -1;
            if (myX == 2048 && myY == 3291)
                toX = 1;
        }

        if (obX == 2062 && obY == 3263) {
            if(!donatorCheck(player, PlayerRights.PLATINUM_DONATOR)) {
                return;
            }

            if (myX == 2062 && myY == 3264)
                toY = -1;

            if (myX == 2062 && myY == 3262)
                toY = 1;
        }

        if (obX == 2062 && obY == 3264) {
            if(!donatorCheck(player, PlayerRights.PLATINUM_DONATOR)) {
                return;
            }

            if (myX == 2062 && myY == 3263)
                toY = 1;

            if (myX == 2062 && myY == 3265)
                toY = -1;
        }

        /*
         * Unreachable
         */
        if ((toX < -1 && toX > 1) || (toY < -1 && toY > 1) || (toX == 0 && toY == 0)) {
            player.getPacketSender().sendMessage("You can't reach that.");
            return;
        } /*
         * Player moving
         */
        move(player, myX + toX, myY + toY, toX, toY);
    }

    /**
     * Moving across stepping stone
     *
     * @param player  the player
     * @param x       the x
     * @param y       the y
     * @param xOffset the x offset
     * @param yOffset the y offset
     */
    private static void move(Player player, int x, int y, int xOffset, int yOffset) {
        /*
         * Already in action
         */
        if (player.getSkillAnimation() > 0) {
            return;
        }
        /*
         * Set the animation
         */
        player.setSkillAnimation(STEPPING_STONE_ANIMATION);
        player.getUpdateFlag().flag(Flag.APPEARANCE);
        player.getPacketSender().sendMessage("You jump across the stepping stone..");
        player.getMovementQueue().walkStep(xOffset, yOffset);
        TaskManager.submit(new Task(1, player, false) {

            @Override
            public void execute() {
                player.moveTo(new Position(x, y));
                stop();
            }

            @Override
            public void stop() {
                setEventRunning(false);
                player.setSkillAnimation(-1);
                player.getUpdateFlag().flag(Flag.APPEARANCE);
            }
        });
    }

    public static boolean donatorCheck(Player player, PlayerRights rights) {
        if (!rights.hasEnoughDonated(player)) {
            player.sendMessage("You must be atleast a "+rights.toString()+" Donator to access this island.");
            return false;
        }
        return true;
    }

}
