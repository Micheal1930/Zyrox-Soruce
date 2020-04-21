package com.varrock.net.sql;

/**
 * All table names added here for easy renaming.
 */
public enum SQLTable {

    SAVES_STARTERS,
    SAVES_PUNISHMENTS,
    SAVES_HISCORES,
    SAVES_REDEEMS,
    SAVES_WALLET_CODES,

    STATISTICS_TOP_DONATORS,
    STATISTICS_TOP_PKERS,
    STATISTICS_TOP_VOTERS,
    STATISTICS_ONLINE,

    LOGS_TRADED_ITEMS,
    LOGS_STAKED_ITEMS,
    LOGS_AUCTION_POSTS,
    LOGS_AUCTION_COLLECTIONS,
    LOGS_AUCTION_PURCHASES,
    LOGS_AUCTION_BIDS,
    LOGS_AUCTION_CLEAR,
    LOGS_DROPPED_ITEMS,
    LOGS_PICKUP_ITEMS,
    LOGS_CONNECTIONS,
    LOGS_COMMANDS,
    LOGS_GLOBAL_EVENTS,
    LOGS_SHOP_SELLS,
    LOGS_SHOP_PURCHASES,
    LOGS_DEATH_ITEMS_LOST,
    LOGS_SUSPICIOUS_LOGINS,
    LOGS_GLOBAL_STATISTICS,
    LOGS_PUNISHMENTS,
    LOGS_PRIVATE_MESSAGES,
    LOGS_YELL_MESSAGES,
    LOGS_CLAN_MESSAGES,
    LOGS_RARE_LOOT,
    LOGS_REFERRAL_CODES,
    LOGS_SOCIAL_CODES,
    LOGS_STORE_CLAIMS,
    LOGS_INTRODUCTION_CLAIMS,
    LOGS_VOTED,
    LOGS_YOUTUBE_COMMENTS,

    DATA_REFERRAL_CODES,
    DATA_SOCIAL_CODES,
    DATA_YOUTUBE_VIDEOS,

    STORE_TRANSACTIONS,

    FORUMS_TOPICS,

    STORE_ITEMS

    ;

    public static String GAME_SCHEMA = "varrnzgh_game";

    public static String FORUM_SCHEMA = "varrnzgh_forums";

    public static String getGameSchemaTable(SQLTable entry) {
        return GAME_SCHEMA + entry.toTableName();
    }

    public static String getForumSchemaTable(SQLTable entry) {
        return FORUM_SCHEMA + entry.toTableName();
    }

    public String toTableName() {
        return name().toLowerCase();
    }
}
