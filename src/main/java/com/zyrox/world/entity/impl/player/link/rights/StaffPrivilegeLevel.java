package com.zyrox.world.entity.impl.player.link.rights;

/**
 * The privilege levels for staffs.
 *
 * @author Gabriel || Wolfsdarker
 */
public interface StaffPrivilegeLevel {

    /**
     * The privilege for support.
     */
    int SUPPORT = 10;

    /**
     * The privilege for moderators.
     */
    int MODERATOR = 20;

    /**
     * The privilege for the head of the moderators.
     */
    int HEAD_MODERATOR = 30;

    /**
     * The privilege for administrators.
     */
    int ADMINISTRATOR = 40;

    /**
     * The privilege for super administrators.
     */
    int SUPER_ADMINISTRATOR = 50;

    /**
     * The privilege for managers.
     */
    int MANAGER = 60;

    /**
     * The privilege for developers.
     */
    int DEVELOPER = 70;

    /**
     * The privilege for owners.
     */
    int OWNER = 80;

}
