package com.zyrox.world;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.zyrox.GameServer;
import com.zyrox.GameSettings;
import com.zyrox.GameType;
import com.zyrox.engine.task.Task;
import com.zyrox.engine.task.TaskManager;
import com.zyrox.model.Locations;
import com.zyrox.model.PlayerRights;
import com.zyrox.model.container.impl.Shop;
import com.zyrox.model.log.impl.GlobalEventLog;
import com.zyrox.model.log.impl.GlobalStatisticLog;
import com.zyrox.util.GitDeploy;
import com.zyrox.util.Misc;
import com.zyrox.util.Stopwatch;
import com.zyrox.util.font.FontUtils;
import com.zyrox.world.content.*;
import com.zyrox.world.content.auction_house.AuctionHouseManager;
import com.zyrox.world.content.doors.DoorManager;
import com.zyrox.world.content.minigames.impl.FightPit;
import com.zyrox.world.content.minigames.impl.PestControl;
import com.zyrox.world.content.shop.ShopManager;
import com.zyrox.world.content.social.SocialCodeManager;
import com.zyrox.world.content.well_of_goodwill.WellOfGoodwill;
import com.zyrox.world.content.youtube.YouTubeManager;
import com.zyrox.world.entity.Entity;
import com.zyrox.world.entity.EntityHandler;
import com.zyrox.world.entity.impl.CharacterList;
import com.zyrox.world.entity.impl.npc.NPC;
import com.zyrox.world.entity.impl.player.Player;
import com.zyrox.world.entity.impl.player.PlayerHandler;
import com.zyrox.world.entity.updating.NpcUpdateSequence;
import com.zyrox.world.entity.updating.PlayerUpdateSequence;
import com.zyrox.world.entity.updating.UpdateSequence;

import org.apache.commons.lang3.time.DateUtils;
import org.eclipse.jgit.api.PullResult;
import org.zeroturnaround.zip.ZipUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Phaser;

/**
 * @author Gabriel Hannason Thanks to lare96 for help with parallel updating
 * system
 */
public class World {

    /**
     * All of the registered players.
     */
    private static CharacterList<Player> players = new CharacterList<>(1000);

    /**
     * All of the registered NPCs.
     */
    private static CharacterList<NPC> npcs = new CharacterList<>(30000);

    /**
     * Used to block the game thread until updating has completed.
     */
    private static Phaser synchronizer = new Phaser(1);

    /**
     * A thread pool that will update players in parallel.
     */
    private static ExecutorService updateExecutor = Executors.newFixedThreadPool(
            Runtime.getRuntime().availableProcessors(),
            new ThreadFactoryBuilder().setNameFormat("UpdateThread").setPriority(Thread.MAX_PRIORITY).build());

    /**
     * The queue of {@link Player}s waiting to be logged in.
     **/
    private static Queue<Player> logins = new ConcurrentLinkedQueue<>();

    /**
     * The queue of {@link Player}s waiting to be logged out.
     **/
    private static Queue<Player> logouts = new ConcurrentLinkedQueue<>();

    /**
     * The queue of {@link Player}s waiting to be given their vote reward.
     **/
    private static Queue<Player> voteRewards = new ConcurrentLinkedQueue<>();

    public static List<String> votes = new ArrayList<String>();
    private static WellOfGoodwill well = new WellOfGoodwill();

    public static Date today = new Date();
// i g2g for 5 mins daughters crying/.
    public static int lastHour = 0;

    public static int lastMinute = 0;

    public static Stopwatch UPTIME = new Stopwatch().reset();

    public static int FAKE_PLAYER_COUNT = 0;

    public static WellOfGoodwill getWell() {
        return well;
    }

    public static void handleVotes(Player player) {
        if (!votes.contains(player.getUsername()))
            votes.add(player.getUsername());
        if (votes.size() >= 5) {
            sendMessage("<img=10> <col=E9E919><shad=0>10 Players have voted for a reward! ::vote");
            sendMessage("<img=10> <col=E9E919><shad=0>Claim your rewards for voting every 12 hours! ::vote");
            votes.clear();
        }
    }

    public static void register(Entity entity) {
        EntityHandler.register(entity);
    }

    public static void register(Player c, NPC entity) {
        EntityHandler.register(entity);
        c.getRegionInstance().getNpcsList().add(entity);
    }

    public static void deregister(Entity entity) {
        EntityHandler.deregister(entity);
    }

    public static Player getPlayerByName(String username) {
        Optional<Player> op = players.search(p -> p != null && p.getUsername().equals(Misc.formatText(username)));
        return op.isPresent() ? op.get() : null;
    }

    public static Player getPlayerHeight(int heightLevel) {
        Optional<Player> op = players.search(p -> p != null && p.getPosition().getZ() == heightLevel);
        return op.isPresent() ? op.get() : null;
    }

    public static NPC getNpcById(int id) {
        Optional<NPC> op = npcs.search(p -> p != null && p.getDefinition() != null && p.getDefinition().getId() == id);
        return op.isPresent() ? op.get() : null;
    }

    public static NPC getNpcByIdAndHeight(int id, int plane) {
        Optional<NPC> op = npcs.search(p -> p != null && p.getDefinition() != null && p.getDefinition().getId() == id
                && p.getPosition().getZ() == plane);
        return op.isPresent() ? op.get() : null;
    }

    public static Player getPlayerByIndex(int username) {
        Optional<Player> op = players.search(p -> p != null && p.getIndex() == username);
        return op.isPresent() ? op.get() : null;
    }

    public static Player getPlayerByLong(long encodedName) {
        Optional<Player> op = players.search(p -> p != null && p.getLongUsername().equals(encodedName));
        return op.isPresent() ? op.get() : null;
    }

    /**
     * Send a message to the chatbox and split it onto seperate lines depending on the width of the message.
     * @param message
     */
    public static void sendSplitMessage(String message, String style) {
        String[] messages = FontUtils.wrapText(FontUtils.FontSize.REGULAR, message, 480);
        for(String splitMessage : messages) {
            sendMessage(style + splitMessage);
        }
    }

    public static void sendMessage(String message) {
        players.forEach(p -> p.getPacketSender().sendMessage(message));
    }

    public static void sendPlayerYell(Player player, String message) {
        players.forEach(p -> {
            if (!p.getRelations().getIgnoreList().contains(player.getLongUsername())) {
                p.getPacketSender().sendMessage(message);
            }
        });
    }

    public static void sendBoxMessage(String message) {
        players.forEach(p -> {
            if (p.boxAlertEnabled)
                p.getPacketSender().sendMessage(message);
        });
    }

    public static void sendStaffMessage(String message) {
        players.stream()
                .filter(p -> p != null && (p.getRights().isStaff()))
                .forEach(p -> p.getPacketSender().sendMessage(message));
    }

    public static void sendModeratorMessage(String message) {
        players.stream()
                .filter(p -> p != null && (p.getRights() == PlayerRights.OWNER
                        || p.getRights() == PlayerRights.DEVELOPER || p.getRights() == PlayerRights.ADMINISTRATOR
                        || p.getRights() == PlayerRights.MODERATOR))
                .forEach(p -> p.getPacketSender().sendMessage(message));
    }

    public static void sendAdminMessage(String message) {
        players.stream()
                .filter(p -> p != null && (p.getRights() == PlayerRights.OWNER
                        || p.getRights() == PlayerRights.DEVELOPER || p.getRights() == PlayerRights.ADMINISTRATOR || p.getRights() == PlayerRights.GLOBAL_MOD))
                .forEach(p -> p.getPacketSender().sendMessage(message));
    }

    public static void updateServerTime() {
        players.forEach(p -> p.getPacketSender().sendString(26702,
                "@or2@Server time: @or2@[ @gre@" + Misc.getCurrentServerTime() + "@or2@ ]"));
    }

    public static void sendGlobalMessage(String prefix, String message) {
        sendGlobalMessage(prefix, "<col=6666FF>", message, "<col=000000>");
    }

    public static void sendGlobalMessage(String prefix, String prefixColour, String message, String messageColour) {
        String formattedMessage;
        if (prefix.equalsIgnoreCase("Loot")) {
            formattedMessage = Misc.getLootIcon() + messageColour + message;
        } else if (prefix.equalsIgnoreCase("Security") || prefix.equalsIgnoreCase("Server")) {
            formattedMessage = Misc.getServerIcon() + messageColour + message;
        } else {
            formattedMessage = prefixColour + "[ " + prefix + " ]</col> " + messageColour + message;
        }
        players.forEach(p -> p.getPacketSender().sendMessage(formattedMessage));
    }

    public static int getPlayersOnline() {
        return World.getPlayers().size() + FAKE_PLAYER_COUNT;
    }

    public static int getStaffOnline() {
        int total = 0;

        for (Player player : World.getPlayers()) {
            if (player == null)
                continue;

            if (player.isStaff())
                total++;

        }
        return total;
    }

    public static int getWildernessCount() {
        int total = 0;

        for (Player player : World.getPlayers()) {
            if (player == null)
                continue;

            if (player.getLocation() == Locations.Location.WILDERNESS)
                total++;

        }
        return total;
    }

    public static void updateStaffList() {
        TaskManager.submit(new Task(false) {
            @Override
            protected void execute() {
                // players.forEach(p -> StaffList.updateInterface(p));
                stop();
            }
        });
    }

    public static void savePlayers() {
        players.forEach(p -> p.save());
    }

    public static CharacterList<Player> getPlayers() {
        return players;
    }

    public static CharacterList<NPC> getNpcs() {
        return npcs;
    }

    public static int ANTI_FLOOD_COUNT = 100;

    public static boolean ANTI_FLOOD = false;

    public static void sequence() {

        // Handle queued logins.
        for (int amount = 0; amount < GameSettings.LOGIN_THRESHOLD; amount++) {

            Player player = logins.poll();

            if (player == null) {
                break;
            }

            if (logouts.contains(player)) {
                continue;
            }

            PlayerHandler.handleLogin(player);

        }

        // Handle queued logouts.
        /*
         * int amount = 0; Iterator<Player> $it = logouts.iterator(); while
         * ($it.hasNext()) { Player player = $it.next(); if (player == null || amount >=
         * GameSettings.LOGOUT_THRESHOLD) break; if (PlayerHandler.handleLogout(player))
         * { $it.remove(); amount++; } }
         */

        FightPit.sequence();
        Reminders.sequence();
        //Cows.spawnMainNPCs();
        //Cows.spawnCows();
        SecurityYeller.sequence();
        PestControl.sequence();
        ShootingStar.sequence();
        EvilTrees.sequence();
        DoorManager.process();
        AuctionHouseManager.process();

        // DonationYeller.sequence();
        TriviaBot.sequence();

        Date rightNow = new Date();
        if(!DateUtils.isSameDay(today, rightNow)) {
            today = rightNow;
            newDay();
        }

        Calendar calendar = Calendar.getInstance();
        if(calendar.get(Calendar.HOUR_OF_DAY) != lastHour) {
            newHour();
            lastHour = calendar.get(Calendar.HOUR_OF_DAY);
        }
        if(calendar.get(Calendar.MINUTE) != lastMinute) {
            newMinute();
            lastMinute = calendar.get(Calendar.MINUTE);
        }

        if(World.getPlayersOnline() >= ANTI_FLOOD_COUNT && !ANTI_FLOOD) {
            ANTI_FLOOD = true;
            World.sendAdminMessage("<col=ff0000>Anti flood mode has been enabled.");
        }

        // ShopRestocking.sequence();

        // First we construct the update sequences.
        UpdateSequence<Player> playerUpdate = new PlayerUpdateSequence(synchronizer, updateExecutor);
        UpdateSequence<NPC> npcUpdate = new NpcUpdateSequence();

        players.shuffle();
        npcs.shuffle();

        // Then we execute pre-updating code.
        players.shuffledEach(playerUpdate::executePreUpdate);
        npcs.shuffledEach(npcUpdate::executePreUpdate);

        // Then we execute parallelized updating code.
        synchronizer.bulkRegister(players.size());
        players.forEach(playerUpdate::executeUpdate);
        synchronizer.arriveAndAwaitAdvance();

        // Then we execute post-updating code.
        players.forEach(playerUpdate::executePostUpdate);
        npcs.forEach(npcUpdate::executePostUpdate);

        try {
            if (GameServer.discordBot != null)
                GameServer.discordBot.process();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void load() {

        //GameServer.starterHandler.load();
        //GameServer.punishmentManager.load();
        //YouTubeManager.load();
        //ReferralCode.load();
        //SocialCodeManager.load();

        //BetaTesters.load();

        TaskManager.submit(new Task(2) { //1 second
            @Override
            public void execute() {

                for (Shop shop : ShopManager.getShops().values()) {
                    if (shop != null) {
                        shop.restock();
                    }
                }

            }
        });

    }

    public static Queue<Player> getLoginQueue() {
        return logins;
    }

    public static Queue<Player> getLogoutQueue() {
        return logouts;
    }

    public static Queue<Player> getVoteRewardingQueue() {
        return voteRewards;
    }

    public static void newDay() {
        if(GameServer.isLocal())
            return;

        /*if(!GameServer.isUpdating() && UPTIME.elapsed(1000 * 60 * 60 * 24 * 2)) {
            restart(null, 300, false);
        }*/
    }

    public static void newHour() {
        if(GameServer.isLive()) {
            World.backup(System.currentTimeMillis());
            new GlobalStatisticLog(World.getPlayers().size(), getStaffOnline(), getWildernessCount(), Misc.getTime()).submit();
        }
    }

    public static void newMinute() {
       // World.backup(System.currentTimeMillis());
       // new GlobalStatisticLog(getPlayersOnline(), getStaffOnline(), getWildernessCount(), Misc.getTime()).submit();
    }

    public static void restart(Player player, int time, boolean update) {

        AuctionHouseManager.saveAll();

        if(time == -1) {
            time = 10;

            GameServer.discordBot.sendUpdateEventMessage(
                    "Zyrox has been turned off.",
                    "Zyrox",
                    "This command was initiated during defcon 4.",
                    "Zyrox will be back online shortly.");

            GameServer.setUpdating(true);

            for (Player plr : World.getPlayers()) {
                if (plr == null) {
                    continue;
                }

                plr.getPacketSender().sendSystemUpdate((int) Math.ceil((time * 5) / 3.0));
            }

            TaskManager.submit(new Task((int) Math.ceil((time * 5) / 3.0), "system_update", false) {
                @Override
                protected void execute() {
                    GameServer.getLogger().info("Update task finished!");
                    stop();

                    System.exit(0);

                }
            });

            return;
        }

        if (time >= 60) {

            new GlobalEventLog(update ? "Update" : "Restart", "Initiated by "+(player == null ? "Server" : player.getName()+" for "+time+" seconds"), Misc.getTime()).submit();
            if(player == null) {

                GameServer.discordBot.sendUpdateEventMessage(
                        "Zyrox is being restarted!",
                        "Zyrox",
                        "This is a scheduled restart that happens every 24 hours.",
                        "Restart will be completed in 5 minutes.");

            } else {
                if(update) {
                    GameServer.discordBot.sendUpdateEventMessage(
                            "\uD83D\uDCDC Zyrox is being updated!",
                            player.getName(),
                            "Check #game-updates  to view the latest updates.",
                            "The update will be completed in "+Misc.getTimeLeft(time)+"");
                } else {

                    GameServer.discordBot.sendUpdateEventMessage(
                            "Zyrox is being restarted!",
                            player.getName(),
                            null,
                            "The restart will be completed in "+Misc.getTimeLeft(time)+"");
                }
            }

            GameServer.setUpdating(true);

            for (Player plr : World.getPlayers()) {
                if (plr == null) {
                    continue;
                }

                plr.getPacketSender().sendSystemUpdate((int) Math.ceil((time * 5) / 3.0));
            }

            TaskManager.submit(new Task((int) Math.ceil((time * 5) / 3.0), "system_update", false) {
                @Override
                protected void execute() {
                    GameServer.getLogger().info("Update task finished!");
                    stop();

                    if(update && GameSettings.GAME_TYPE != GameType.LOCAL) {
                        PullResult result = GitDeploy.deploy();
                        GameServer.getLogger().info(result.toString());
                    }

                    try {
                        Process proc = Runtime.getRuntime().exec(new String[]{ "screen", "java", "-server", "-Xms3584m", "-Xmx7G", "-XX:+UseParallelGC", "-XX:MaxPermSize=512M", "-jar", "./gameserver.jar"});
                        BufferedReader stdInput = new BufferedReader(new
                                InputStreamReader(proc.getInputStream()));

                        BufferedReader stdError = new BufferedReader(new
                                InputStreamReader(proc.getErrorStream()));

                        GameServer.getLogger().info("Here is the standard output of the command:\n");
                        String s = null;
                        while ((s = stdInput.readLine()) != null) {
                            GameServer.getLogger().info(s);
                        }

                        // Read any errors from the attempted command
                        GameServer.getLogger().info("Here is the standard error of the command (if any):\n");
                        while ((s = stdError.readLine()) != null) {
                            GameServer.getLogger().info(s);
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    System.exit(0);

                }
            });

            return;
        } else if(player != null) {
            player.sendMessage("Please enter more than 60 seconds");
        }
    }

    public static void backup(long timeToBackup) {
        ZipUtil.pack(new File("./data/characters"), new File("./data/backup/char/backup_"+ timeToBackup+".zip"), true);
        ZipUtil.pack(new File("./data/saves/auction"), new File("./data/backup/auction/backup_"+ timeToBackup+".zip"), true);
    }
}
