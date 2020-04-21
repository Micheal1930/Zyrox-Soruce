package com.varrock;

import com.google.api.services.youtube.YouTube;
import com.google.common.collect.ImmutableList;
import com.varrock.dump.Dumper;
import com.varrock.engine.GameEngine;
import com.varrock.model.benchmark.GameBenchmark;
import com.varrock.model.event.CycleEventHandler;
import com.varrock.model.maintick.Task;
import com.varrock.model.maintick.TaskScheduler;
import com.varrock.model.punish.PunishmentManager;
import com.varrock.net.sql.MockSQLNetwork;
import com.varrock.net.sql.SQLNetwork;
import com.varrock.net.sql.create.SQLTableCreationHandler;
import com.varrock.tools.discord.DiscordBot;
import com.varrock.util.ErrorFile;
import com.varrock.util.Misc;
import com.varrock.util.ShutdownHook;
import com.varrock.world.content.donator_boss.DonatorBossManager;
import com.varrock.world.content.starter.StarterHandler;
import com.varrock.world.content.youtube.YouTubeAuthentication;
import com.zaxxer.hikari.HikariConfig;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The starting point of Ruse.
 *
 * @author Gabriel
 * @author Samy
 */
public class GameServer {

    private static YouTube youtube;

    public static DonatorBossManager donatorBossManager = new DonatorBossManager();

    public static DiscordBot discordBot = new DiscordBot();

    private static SQLNetwork sqlNetwork;

    public static PunishmentManager punishmentManager = new PunishmentManager();

    public static StarterHandler starterHandler = new StarterHandler();

    private static final GameLoader loader = new GameLoader(GameSettings.GAME_PORT);
    private static final Logger logger = Logger.getLogger("Varrock");
    private static boolean updating;

    /**
     * The tasks that occur during a game tick.
     */
    private static List<GameTickTask> tasks;

    public static final TaskScheduler scheduler = new TaskScheduler();

    public static void main(String[] params) {

        try {
            System.setErr(new PrintStream(new ErrorFile("./errors", "ErrorLog"), true));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        GameSettings.load();

        logger.info("Starting Varrock in "+GameSettings.GAME_TYPE+" mode.");

        if(GameServer.isLive()) {
            logger.info("Starting discord bot...");//disable mysql it is
            //discordBot.start();
        }

        logger.info("Starting youtube api...");
        //youtube = YouTubeAuthentication.start();

        Runtime.getRuntime().addShutdownHook(new ShutdownHook());

         loadSqlNetwork();

        if (!GameSettings.MOCK_SQL) {
            createSqlTables(sqlNetwork, Paths.get("data", "sql", "create"));
        }

        try {// also you know this?

            logger.info("Initializing the loader...");
            loader.init();
            loader.finish(); //networking is here
            logger.info("The loader has finished loading utility tasks.");
            logger.info("Varrock is now online on port " + GameSettings.GAME_PORT + "!");
            Thread.sleep(5000);
            //BucketConverter.main(null);


        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Could not start Varrock! Program terminated.", ex);
            System.exit(1);
        }

        //CheckDupedAccounts.check();

        tasks = createGameTickTasks();
        gameTick();

        Dumper.dump();

        if(GameSettings.GAME_TYPE != GameType.LOCAL) {
            //discordBot.sendStartupMessage();
        }

    }

    public static GameLoader getLoader() {
        return loader;
    }

    public static GameEngine getEngine() {
        return getLoader().getEngine();
    }

    public static Logger getLogger() {
        return logger;
    }

    public static void setUpdating(boolean updating) {
        GameServer.updating = updating;
    }

    public static boolean isUpdating() {
        return GameServer.updating;
    }

    public static SQLNetwork getSqlNetwork() {
        return sqlNetwork;
    }

    public static boolean isLive() {
        return GameSettings.GAME_TYPE == GameType.LIVE;
    }

    public static boolean isLocal() {
        return GameSettings.GAME_TYPE == GameType.LOCAL;
    }

    public static boolean isBeta() {
        return GameSettings.GAME_TYPE == GameType.BETA;
    }

    private static void createSqlTables(SQLNetwork network, Path creationDirectory) {
        network.reschedule(TimeUnit.MILLISECONDS, 50);

        SQLTableCreationHandler sqlTableCreationHandler = new SQLTableCreationHandler(network);

        sqlTableCreationHandler.addAll(creationDirectory);

        sqlTableCreationHandler.updateBlocking(50, TimeUnit.MILLISECONDS);

        network.reschedule();
    }

    public static void loadSqlNetwork() {
        Properties properties = new Properties();

        Path path = Paths.get("data", "sql", "sql.properties");

        try {
            Misc.createFileIfAbsent(path,
                    "dataSource.user=varrnzgh_swag",
                    "dataSource.password=123123!",
                    "dataSource.databaseName=varrnzgh_",
                    "dataSourceClassName=com.mysql.jdbc.jdbc2.optional.MysqlConnectionPoolDataSource",
                    "dataSource.url=jdbc:mysql://localhost:3306");
        } catch (IOException e) {
            if (!Files.exists(path)) {
                throw new RuntimeException("Unable to create sql.properties default file.", e);
            }
        }

        try (BufferedReader reader = Files.newBufferedReader(path)) {
            properties.load(reader);
        } catch (IOException ioe) {
            throw new RuntimeException("Unable to load properties: " + properties, ioe);
        }
        if (GameSettings.DEBUG_MODE && GameSettings.MOCK_SQL) {
            System.out.println("deze mofo");
            sqlNetwork = new MockSQLNetwork(new HikariConfig(properties));
            return;
        }
        System.out.println("dan niet");
        HikariConfig hikariConfig = new HikariConfig(properties);

        if (!properties.containsKey("dataSource.databaseName")) {
            throw new RuntimeException("No database name selected, for example; dataSource.databaseName=server");
        }
        if (!properties.containsKey("dataSource.url")) {
            throw new RuntimeException("No jdbc url, for example; dataSource.url=jdbc:mysql://localhost:3306/");
        }
        String database = properties.getProperty("dataSource.databaseName");

        String jdbcUrl = properties.getProperty("dataSource.url");

        hikariConfig.setSchema(database);
        hikariConfig.addDataSourceProperty("url", jdbcUrl + database);

        sqlNetwork = new SQLNetwork(hikariConfig, 500, TimeUnit.MILLISECONDS, 20);
        sqlNetwork.start();
        sqlNetwork.blockingTest();
    }

    /**
     * The main game tick.
     */
    public static void gameTick() {
        scheduler.schedule(new Task() {
            @Override
            protected void execute() {
                if (GameSettings.DEBUG_MODE) {
                    tasks.forEach(task ->
                            new GameBenchmark(String.format("%s#%s", task.getIdentifyingClass(),
                                    task.getIdentifyingMethod()), task.task, 60, TimeUnit.MILLISECONDS).execute());
                } else {
                    tasks.forEach(task -> task.getTask().run());
                }
            }
        });
    }

    private static List<GameTickTask> createGameTickTasks() {
        return ImmutableList.copyOf(Arrays.asList(
                new GameTickTask(CycleEventHandler.class, "cycle", CycleEventHandler.getSingleton()::cycle)
        ));
    }

    public static YouTube getYoutube() {
        return youtube;
    }

    static class GameTickTask {

        final Class<?> identifyingClass;

        final String identifyingMethod;

        final Runnable task;

        GameTickTask(Class<?> identifyingClass, String identifyingMethod, Runnable task) {
            this.identifyingClass = identifyingClass;
            this.identifyingMethod = identifyingMethod;
            this.task = task;
        }

        public Class<?> getIdentifyingClass() {
            return identifyingClass;
        }

        public String getIdentifyingMethod() {
            return identifyingMethod;
        }

        public Runnable getTask() {
            return task;
        }
    }
}