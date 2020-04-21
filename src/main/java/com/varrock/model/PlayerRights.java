package com.varrock.model;

import java.util.Arrays;
import java.util.Comparator;

import com.varrock.GameSettings;
import com.varrock.util.Misc;
import com.varrock.world.entity.impl.player.Player;
import com.varrock.world.entity.impl.player.link.rights.DonatorPrivilegeLevel;
import com.varrock.world.entity.impl.player.link.rights.StaffPrivilegeLevel;


/**
 * Represents a player's privilege rights.
 *
 * @author Gabriel Hannason
 */

public enum PlayerRights {

    PLAYER(0),
    
    MODERATOR(1, StaffPrivilegeLevel.MODERATOR, DonatorPrivilegeLevel.EXECUTIVE_DONATOR, 1, "<col=20B2AA><shad=0>"),
    ADMINISTRATOR(2, StaffPrivilegeLevel.ADMINISTRATOR, DonatorPrivilegeLevel.EXECUTIVE_DONATOR, 2, "<col=d4c303><shad=0>"),
    OWNER(3, StaffPrivilegeLevel.OWNER, DonatorPrivilegeLevel.EXECUTIVE_DONATOR, 2, "<col=fa0505><shad=0>"),
    DEVELOPER(4, StaffPrivilegeLevel.DEVELOPER, DonatorPrivilegeLevel.EXECUTIVE_DONATOR, 4, "<col=fa0505><shad=0>"),
    //int id, int donatorPrivilegelLevel, int amountDonatedRequired, int yellDelay,
    DONATOR(5, DonatorPrivilegeLevel.REGULAR_DONATOR, 10/* <- this is when it update*/, 60, -1, "<col=ff0000><shad=0>", 1.5, 1.1),
    SUPER_DONATOR(6, DonatorPrivilegeLevel.SUPER_DONATOR, 50/* <- this is when it update*/, 40, -1, "<col=195582><shad=0>", 1.5, 1.25),
    EXTREME_DONATOR(7, DonatorPrivilegeLevel.EXTREME_DONATOR, 250/* <- this is when it update*/, 20, -1, "<col=008000><shad=0>", 2, 1.5),
    PLATINUM_DONATOR(8, DonatorPrivilegeLevel.PLATINUM_DONATOR, 500/* <- this is when it update*/, 10, -1, "<col=6c6c6c><shad=77776c>", 2.5, 1.75),
    EXECUTIVE_DONATOR(9, DonatorPrivilegeLevel.EXECUTIVE_DONATOR, 1000/* <- this is when it update*/, 0, -1, "<col=ffa500><shad=0>", 3, 2),
    SUPPORT(10, StaffPrivilegeLevel.SUPPORT, DonatorPrivilegeLevel.EXECUTIVE_DONATOR, -1, "<col=3333ff><shad=0>"),
    YOUTUBER(11, DonatorPrivilegeLevel.EXTREME_DONATOR, 0, 0, -1, "<col=cc181e><shad=0>", 1, 1),
    //RUBY_MEMBER(12, DonatorPrivilegeLevel.RUBY_MEMBER, 2000, 0, -1, "<col=bf0000><shad=0>", 3.25, 3.5),// great oka
   
    MANAGER(13, StaffPrivilegeLevel.MANAGER, DonatorPrivilegeLevel.EXECUTIVE_DONATOR, 355, "<col=ff8d15><shad=0>"),
    
    //SAPPHIRE(14, DonatorPrivilegeLevel.SAPPHIRE, 1000, 0, -1, "<col=6f6f93><shad=0>", 3.75, 2.5),
   // EMERALD(15, DonatorPrivilegeLevel.EMERALD, 1500, 0, -1, "<col=4ed092><shad=0>", 3.5, 2.75),
   // ONYX(16, DonatorPrivilegeLevel.ONYX, 3000, 0, -1, "<col=bf0000><shad=0>", 4, 3),
   // CRYSTAL(17, DonatorPrivilegeLevel.CRYSTAL, 8000, 0, 462, "<col=a5eeff><shad=0>", 5, 4),

    GLOBAL_MOD(12, StaffPrivilegeLevel.HEAD_MODERATOR, DonatorPrivilegeLevel.EXECUTIVE_DONATOR, 481, "<col=20B2AA><shad=0>"),

    BETA_TESTER(13, 0, DonatorPrivilegeLevel.EXECUTIVE_DONATOR, 699, "<col=ff0000><shad=0>"),

   // SUPER_ADMIN(19, StaffPrivilegeLevel.SUPER_ADMINISTRATOR, DonatorPrivilegeLevel.EXECUTIVE_DONATOR, 354, "<col=20B2AA><shad=0>"),

    ;

    /**
     * The right's ID.
     */
    private int id;
    /**
     * The staff right's privilege level.
     */
    private int privilegeLevel;

    /**
     * The right's donator privilege level.
     */
    private int donatorPrivilegelLevel;

    /**
     * The amount donated required to have this rank.
     */
    private int amountDonatedRequired;

    /**
     * The right's sprite ID.
     */
    private int spriteId;

    /**
     * The right's yell delay.
     */
    private int yellDelay;

    /**
     * The right's yell hex color prefix.
     */
    private String yellHexColorPrefix;

    /**
     * The right's loyalty points gain modifier.
     */
    private double loyaltyPointsGainModifier;

    /**
     * The right's experience gain modifier.
     */
    private double experienceGainModifier;


    /**
     * Constructor for the minimum right.
     *
     * @param id
     */
    PlayerRights(int id) {
        this(id, 0, 0, -1, -1, null, 1, 1);
    }

    /**
     * Constructor for staffs.
     *
     * @param id
     * @param privilegeLevel
     * @param spriteId
     * @param yellHexColorPrefix
     */
    PlayerRights(int id, int privilegeLevel, int donatorPrivilegelLevel, int spriteId, String yellHexColorPrefix) {
        this(id, privilegeLevel, donatorPrivilegelLevel, 0, spriteId, -1, yellHexColorPrefix, 1, 2);
    }

    /**
     * Constructor for staffs.
     *
     * @param id
     * @param privilegeLevel
     * @param spriteId
     * @param yellHexColorPrefix
     */
    PlayerRights(int id, int privilegeLevel, int spriteId, String yellHexColorPrefix) {
        this(id, privilegeLevel, DonatorPrivilegeLevel.REGULAR_DONATOR, 0, spriteId, -1, yellHexColorPrefix, 1, 2);
    }

    /**
     * Constructor for donators.
     *
     * @param id
     * @param donatorPrivilegelLevel
     * @param yellDelay
     * @param spriteId
     * @param yellHexColorPrefix
     * @param loyaltyPointsGainModifier
     * @param experienceGainModifier
     */
    PlayerRights(int id, int donatorPrivilegelLevel, int amountDonatedRequired, int yellDelay, int spriteId, String yellHexColorPrefix, double loyaltyPointsGainModifier, double experienceGainModifier) {
        this(id, 0, donatorPrivilegelLevel, amountDonatedRequired, spriteId, yellDelay, yellHexColorPrefix, loyaltyPointsGainModifier, experienceGainModifier);
    }

    /**
     * Constructor for any kind of right.
     *
     * @param id
     * @param privilegeLevel
     * @param donatorPrivilegelLevel
     * @param spriteId
     * @param yellDelaySeconds
     * @param yellHexColorPrefix
     * @param loyaltyPointsGainModifier
     * @param experienceGainModifier
     */
    PlayerRights(int id, int privilegeLevel, int donatorPrivilegelLevel, int amountDonatedRequired, int spriteId, int yellDelaySeconds, String yellHexColorPrefix, double loyaltyPointsGainModifier, double experienceGainModifier) {
        this.id = id;
        this.privilegeLevel = privilegeLevel;
        this.donatorPrivilegelLevel = donatorPrivilegelLevel;
        this.amountDonatedRequired = amountDonatedRequired;
        this.spriteId = spriteId;
        this.yellDelay = yellDelaySeconds;
        this.yellHexColorPrefix = yellHexColorPrefix;
        this.loyaltyPointsGainModifier = loyaltyPointsGainModifier;
        this.experienceGainModifier = experienceGainModifier;
    }

    /**
     * Returns the player right's ID.
     *
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the delay between each yell.
     *
     * @return the delay in seconds
     */
    public int getYellDelay() {
        return yellDelay;
    }

    /**
     * Returns the privilege level for this rank.
     *
     * @return the privilege level
     */
    public int getPrivilegeLevel() {
        return privilegeLevel;
    }

    /**
     * Returns the donator privilege level.
     *
     * @return the privilege level
     */
    public int getDonatorPrivilegelLevel() {
        return donatorPrivilegelLevel;
    }

    public static int getPrivilegeLevelForAmountDonated(Player player) {
        int currentPrivilegeLevel = 0;
        int currentAmountDonated = 0;
        for(PlayerRights playerRights : PlayerRights.values()) {
            if(!playerRights.isDonator()) {
                continue;
            }
            if(player.getAmountDonated() >= playerRights.getAmountDonatedRequired()) {
                if(player.getAmountDonated() >= currentAmountDonated) {
                    currentPrivilegeLevel = playerRights.getDonatorPrivilegelLevel();
                    currentAmountDonated = playerRights.getAmountDonatedRequired();
                }
            }
        }

        return currentPrivilegeLevel;
    }

    /**
     * Returns the amount the player has to donate to receive this rank.
     *
     * @return the amount
     */
    public int getAmountDonatedRequired() {
        return amountDonatedRequired;
    }

    /**
     * Returns the yell colour prefix.
     *
     * @return the colour
     */
    public String getYellPrefix() {
        return yellHexColorPrefix;
    }

    /**
     * The amount of loyalty points the rank gain per 4 seconds
     */
    public double getLoyaltyPointsGainModifier() {
        return loyaltyPointsGainModifier;
    }

    public double getExperienceGainModifier(Player player) {
        double donatorModifier = 0.0;

/*        if (isRegularDonator(player)) {
            donatorModifier = .1;
        }

        if (isSuperDonator(player)) {
            donatorModifier = .25;
        }

        if (isExtremeDonator(player)) {
            donatorModifier = .5;
        }

        if (isPlatinumDonator(player)) {
            donatorModifier = .75;
        }

        if (isExecutiveDonator(player)) {
            donatorModifier = 2;
        }*/

        return Math.max(donatorModifier, experienceGainModifier);
    }

    public int getSpriteId() {
        return spriteId == -1 ? ordinal() : spriteId;
    }

    public boolean isStaff() {
        return privilegeLevel >= StaffPrivilegeLevel.SUPPORT;
    }

    public boolean isDonator() {
        return this == DONATOR || this == SUPER_DONATOR || this == EXTREME_DONATOR || this == PLATINUM_DONATOR || this == EXECUTIVE_DONATOR;
    }

    public boolean isExtraStaff() {
        return this == YOUTUBER;
    }

    public boolean isHighStaff() {
        return privilegeLevel >= StaffPrivilegeLevel.MANAGER;
    }

    public boolean isHigherStaff(Player player) {
        return GameSettings.HIGHER_STAFF_NAMES.contains(player.getName());
    }

    public boolean isMember() {
        return getDonatorPrivilegelLevel() >= DonatorPrivilegeLevel.REGULAR_DONATOR;
    }

    public static boolean isRegularDonator(Player player) {
        return player.getRights().getDonatorPrivilegelLevel() >= DONATOR.getDonatorPrivilegelLevel() || player.getRights() == DONATOR || player.getAmountDonated() >= 10;
    }

    public static boolean isSuperDonator(Player player) {
        return player.getRights().getDonatorPrivilegelLevel() >= SUPER_DONATOR.getDonatorPrivilegelLevel() || player.getRights() == SUPER_DONATOR || player.getAmountDonated() >= 50;
    }

    public static boolean isExtremeDonator(Player player) {
        return player.getRights().getDonatorPrivilegelLevel() >= EXTREME_DONATOR.getDonatorPrivilegelLevel() || player.getRights() == EXTREME_DONATOR || player.getAmountDonated() >= 250;
    }

    public static boolean isPlatinumDonator(Player player) {
        return player.getRights().getDonatorPrivilegelLevel() >= PLATINUM_DONATOR.getDonatorPrivilegelLevel() || player.getRights() == PLATINUM_DONATOR || player.getAmountDonated() >= 500;
    }

    public static boolean isExecutiveDonator(Player player) {
        return player.getRights().getDonatorPrivilegelLevel() >= EXECUTIVE_DONATOR.getDonatorPrivilegelLevel() || player.getRights() == EXECUTIVE_DONATOR || player.getAmountDonated() >= 1000;
    }

    /**
     * Returns the best donator rank for the player.
     *
     * @param player
     * @return the best rank
     */
    public static PlayerRights getDonatorRights(Player player) {

        int amountDonated = player.getAmountDonated();
        PlayerRights best = PLAYER;

        PlayerRights[] VALUES = PlayerRights.values();

        Arrays.sort(VALUES, new Comparator<PlayerRights>() {

            public int compare(PlayerRights left, PlayerRights right) {
                return left.getAmountDonatedRequired() - right.getAmountDonatedRequired();
            }
        });

        for (PlayerRights value : VALUES) {

            if (value.getAmountDonatedRequired() < 1 || value.getPrivilegeLevel() > 0) {
                continue;
            }
            if (player.getRights() == value
                    || (amountDonated >= value.getAmountDonatedRequired() && value.getAmountDonatedRequired() > best.getAmountDonatedRequired())) {
                best = value;
            }
        }
        return best;
    }

    /**
     * Gets the rank for a certain id.
     *
     * @param id The id (ordinal()) of the rank.
     * @return rights.
     */
    public static PlayerRights forOrdinal(int id) {
        for (PlayerRights rights : PlayerRights.values()) {
            if (rights.ordinal() == id)
                return rights;
        }
        return null;
    }

    /**
     * Gets the rank for a certain id.
     *
     * @param id The id (ordinal()) of the rank.
     * @return rights.
     */
    public static PlayerRights forId(int id) {
        for (PlayerRights rights : PlayerRights.values()) {
            if (rights.getId() == id) //autism
                return rights;
        }
        return null;
    }

    /**
     * Gets the rank for a certain staff privilege level.
     *
     * @param privilegeLevel the privilege level.
     * @return rights.
     */
    public static PlayerRights forStaffPrivilege(int privilegeLevel) {
        for (PlayerRights rights : PlayerRights.values()) {
            if (rights.getPrivilegeLevel() == privilegeLevel)
                return rights;
        }
        return PLAYER;
    }

    /**
     * Gets the rank for a certain donator privilege level.
     *
     * @param privilegeLevel the privilege level.
     * @return rights.
     */
    public static PlayerRights forDonatorPrivilege(int privilegeLevel) {
        for (PlayerRights rights : PlayerRights.values()) {
            if (rights.getDonatorPrivilegelLevel() == privilegeLevel)
                return rights;
        }
        return PLAYER;
    }

    @Override
    public String toString() {
        return Misc.capitalizeFirstLetters(name().toLowerCase().replaceAll("_", " ").replaceAll(" donator", ""));
    }

    /**
     * The donator's chance for luck based items.
     *
     * @return the luck from 0.0 to 1.0
     */
    public double getLuckChance() {
        switch (this) {
            case DONATOR:
                return 1.0 / 15.0;
            case SUPER_DONATOR:
                return 1.0 / 12.0;
            case EXTREME_DONATOR:
                return 1.0 / 9.0;
            case PLATINUM_DONATOR:
                return 1.0 / 6.0;
            case EXECUTIVE_DONATOR:
                return 1.0 / 5.0;
            case MODERATOR:
            case GLOBAL_MOD:
            case SUPPORT:
            case ADMINISTRATOR:
            case YOUTUBER:
            case OWNER:
            case DEVELOPER:
                return 1.0 / 3.0;
        }
        return 0.0;
    }

    public boolean hasEnoughDonated(Player player) {
        return player.getAmountDonated() >= this.getAmountDonatedRequired() || player.getRights().getDonatorPrivilegelLevel() >= this.getDonatorPrivilegelLevel();
    }

}
