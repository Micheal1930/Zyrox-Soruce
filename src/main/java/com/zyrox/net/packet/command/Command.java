package com.zyrox.net.packet.command;

import com.zyrox.model.PlayerRights;
import com.zyrox.world.entity.impl.player.Player;

public abstract class Command {

    /**
     * The minimum staff privilege required.
     */
    private final int mininumStaffPrivilege;

    /**
     * The minimum donator privilege required.
     */
    private final int minimumDonatorPrivilege;

    /**
     * The constructor of a {@link Command} that takes an single argument.
     *
     * @param staffRights The minimum staff rights required to execute this command.
     */
    public Command(PlayerRights staffRights) {
        this.mininumStaffPrivilege = staffRights.getPrivilegeLevel();
        this.minimumDonatorPrivilege = 0;
    }

    /**
     * The constructor of a {@link Command} that takes an single argument.
     *
     * @param staffRights The minimum staff rights required to execute this command.
     * @param isDonator determines if this command is donator related
     */
    public Command(PlayerRights staffRights, boolean isDonator) {
        this.mininumStaffPrivilege = staffRights.getPrivilegeLevel();
        if (isDonator) {
            this.minimumDonatorPrivilege = staffRights.getDonatorPrivilegelLevel();
        } else {
            this.minimumDonatorPrivilege = 0;
        }
    }

    /**
     * The constructor of a {@link Command} that takes an single argument.
     *
     * @param staffRights   The minimum staff rights required to execute this command.
     * @param donatorRights The minimum donator rights required to execute this command.
     */
    public Command(PlayerRights staffRights, PlayerRights donatorRights) {
        this.mininumStaffPrivilege = staffRights.getPrivilegeLevel();
        this.minimumDonatorPrivilege = donatorRights.getDonatorPrivilegelLevel();
    }

    /**
     * Another constructor instance that allows the developer to not have to add
     * an explicit constructor for a command that can be used by everyone.
     */
    public Command() {
        this(PlayerRights.PLAYER, false);
    }

    /**
     * The abstract method that is called when a player executes a specific
     * command that exists.
     *
     * @param player The player executing this command.
     * @param text   The arguments split by spaces.
     * @return Whether the command has executed successfully.
     */
    public abstract boolean execute(Player player, String[] args) throws Exception;

    /**
     * Returns an array of string that will hold example usages of the command,
     * as shown below in the example provided.
     *
     * @return An array of strings that will show example usages of this
     * command.
     */
    public abstract String[] getUsage();

    /**
     * Returns the minimum staff privilege needed to use this command.
     *
     * @return The minimum privilege.
     */
    public int getMininumStaffPrivilege() {
        return mininumStaffPrivilege;
    }

    /**
     * Returns the minimum donator privilege needed to use this command.
     *
     * @return The minimum privilege.
     */
    public int getMinimumDonatorPrivilege() {
        return minimumDonatorPrivilege;
    }

    /**
     * Gets the {@link CommandHeader} of this command.
     *
     * @return The {@link CommandHeader}.
     */
    public CommandHeader getHeader() {
        return this.getClass().getAnnotation(CommandHeader.class);
    }

}
