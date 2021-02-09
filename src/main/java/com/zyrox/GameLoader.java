package com.zyrox;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.util.HashedWheelTimer;

import com.google.common.base.Preconditions;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.zyrox.engine.GameEngine;
import com.zyrox.engine.task.TaskManager;
import com.zyrox.engine.task.impl.ServerTimeUpdateTask;
import com.zyrox.model.definitions.ItemDefinition;
import com.zyrox.model.definitions.NPCDrops;
import com.zyrox.model.definitions.NpcDefinition;
import com.zyrox.model.definitions.WeaponInterfaces;
import com.zyrox.model.item.ItemCombination;
import com.zyrox.net.PipelineFactory;
import com.zyrox.net.packet.impl.CommandPacketListener;
import com.zyrox.net.security.ConnectionHandler;
import com.zyrox.web.WebServer;
import com.zyrox.world.World;
import com.zyrox.world.clip.region.RegionClipping;
import com.zyrox.world.content.CustomObjects;
import com.zyrox.world.content.ProfileViewing;
import com.zyrox.world.content.Scoreboards;
import com.zyrox.world.content.auction_house.AuctionHouseManager;
import com.zyrox.world.content.clan.ClanChatManager;
import com.zyrox.world.content.combat.effect.CombatPoisonEffect.CombatPoisonData;
import com.zyrox.world.content.combat.effect.CombatVenomEffect.CombatVenomData;
import com.zyrox.world.content.combat.strategy.CombatStrategies;
import com.zyrox.world.content.combat.strategy.zulrah.ZulrahConstants;
import com.zyrox.world.content.degrade.DegradingItemJSONLoader;
import com.zyrox.world.content.dialogue.DialogueManager;
import com.zyrox.world.content.donation.DonationDeal;
import com.zyrox.world.content.donation.DonatorEvilTree;
import com.zyrox.world.content.event.GameEventManager;
import com.zyrox.world.content.lootboxes.LootBox;
import com.zyrox.world.content.minigames.impl.castlewars.CastleWarsManager;
import com.zyrox.world.entity.impl.npc.NPC;
import com.zyrox.world.entity.impl.npc.NPCMap;
import com.zyrox.world.entity.impl.player.PlayerLogout;

/**
 * Credit: lare96, Gabbe
 */
public final class GameLoader {

	private final ExecutorService serviceLoader = Executors.newSingleThreadExecutor(new ThreadFactoryBuilder().setNameFormat("GameLoadingThread").build());
	private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor(new ThreadFactoryBuilder().setNameFormat("GameThread").build());
	private final GameEngine engine;
	private final int port;

	protected GameLoader(int port) {
		this.port = port;
		this.engine = new GameEngine();
	}

	public void init() {
		Preconditions.checkState(!serviceLoader.isShutdown(), "The bootstrap has been bound already!");
		executeServiceLoad();
		serviceLoader.shutdown();
	}

	public void finish() throws IOException, InterruptedException {
		if (!serviceLoader.awaitTermination(15, TimeUnit.MINUTES))
			throw new IllegalStateException("The background service load took too long!");

		boolean bind = false;

		int attempts = 0;

		ExecutorService networkExecutor = Executors.newCachedThreadPool();
		ServerBootstrap serverBootstrap = new ServerBootstrap (new NioServerSocketChannelFactory(networkExecutor, networkExecutor));
        serverBootstrap.setPipelineFactory(new PipelineFactory(new HashedWheelTimer()));

		do {
			try {
				serverBootstrap.bind(new InetSocketAddress(port));

				bind = true;
			} catch (Exception e) {
				Thread.sleep(1000);

				System.out.println("Unable to bind to port: " + port + ", retrying...");

				if (attempts++ > 300) {
					System.exit(0);
				}
			}
		} while (!bind);

		executor.scheduleAtFixedRate(engine, 0, GameSettings.ENGINE_PROCESSING_CYCLE_RATE, TimeUnit.MILLISECONDS);
		TaskManager.submit(new ServerTimeUpdateTask());
	}

	private void executeServiceLoad() {
		if(GameSettings.GAME_PORT == 43594) {
			serviceLoader.execute(() -> WebServer.initialize());
		}
		serviceLoader.execute(() -> Server.init());
		serviceLoader.execute(() -> ZulrahConstants.initialize());
		serviceLoader.execute(() -> PlayerLogout.init());
		serviceLoader.execute(() -> ConnectionHandler.init());

		serviceLoader.execute(() -> {
			try {
				RegionClipping.init();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
		serviceLoader.execute(() -> CustomObjects.init());
		serviceLoader.execute(() -> ItemDefinition.init());
		serviceLoader.execute(() -> Scoreboards.init());
		serviceLoader.execute(() -> World.getWell().load());
		serviceLoader.execute(() -> ClanChatManager.init());
		serviceLoader.execute(() -> CombatPoisonData.init());
		serviceLoader.execute(() -> CombatVenomData.init());
		serviceLoader.execute(() -> CombatStrategies.init());
		serviceLoader.execute(() -> NpcDefinition.parseNpcs().load());
		serviceLoader.execute(() -> NPCMap.getSingleton().load());
		serviceLoader.execute(() -> NPCDrops.parseDrops().load());
		serviceLoader.execute(() -> WeaponInterfaces.parseInterfaces().load());
		serviceLoader.execute(() -> DialogueManager.parseDialogues().load());
		serviceLoader.execute(NPC::init);
		serviceLoader.execute(ItemCombination::init);
		serviceLoader.execute(() -> ProfileViewing.init());
		serviceLoader.execute(CommandPacketListener::init);
		serviceLoader.execute(() -> DonatorEvilTree.start());
		serviceLoader.execute(() -> LootBox.init());
		serviceLoader.execute(() -> CastleWarsManager.init());
		serviceLoader.execute(() -> DonationDeal.load());
		serviceLoader.execute(() -> new DegradingItemJSONLoader());
		serviceLoader.execute(GameEventManager::loadEvents);
		serviceLoader.execute(AuctionHouseManager::loadAll);
		serviceLoader.execute(World::load);
	}

	public GameEngine getEngine() {
		return engine;
	}
}