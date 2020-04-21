package com.varrock.engine;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor.CallerRunsPolicy;
import java.util.concurrent.TimeUnit;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.varrock.engine.task.TaskManager;
import com.varrock.world.World;
import com.varrock.world.content.clan.ClanChatManager;
import com.varrock.world.entity.impl.player.Player;

/**
 * 
 * @author lare96
 * @author Gabriel Hannason
 */
public final class GameEngine implements Runnable {

	private final ScheduledExecutorService logicService = GameEngine.createLogicService();

	//private static final int PROCESS_GAME_TICK = 2;

	//private EngineState engineState = EngineState.PACKET_PROCESSING;
	
	//private int engineTick = 0;
	
	@Override
	public void run() {
		try {
			long s = System.nanoTime();
			/*switch(engineState) {
			case PACKET_PROCESSING:
				World.getPlayers().forEach($it -> $it.getSession().handlePrioritizedMessageQueue());
				break;
			case GAME_PROCESSING:
				TaskManager.sequence();
				World.sequence();
				break;
			}
			engineState = next();*/
			
			TaskManager.sequence();
			World.sequence();

		} catch (Throwable e) {
			e.printStackTrace();
			
			try {
				World.savePlayers();
				ClanChatManager.save();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
	
	private static void subcycle() {
		for (Player p : World.getPlayers()) {
			if (p != null) {
				p.getSession().handleQueuedMessages();
			}
		}
	}

	/*private EngineState next() {
		if (engineTick == PROCESS_GAME_TICK) {
			engineTick = 0;
			return EngineState.GAME_PROCESSING;
		}
		engineTick++;
		return EngineState.PACKET_PROCESSING;
	}

	private enum EngineState {
		PACKET_PROCESSING,
		GAME_PROCESSING;
	}*/

	public void submit(Runnable t) {
		try {
			logicService.execute(t);
		} catch(Throwable e) {
			e.printStackTrace();
		}
	}

	/** STATIC **/

	public static ScheduledExecutorService createLogicService() {
		ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(2);
		executor.setRejectedExecutionHandler(new CallerRunsPolicy());
		executor.setThreadFactory(new ThreadFactoryBuilder().setNameFormat("LogicServiceThread").build());
		executor.setKeepAliveTime(45, TimeUnit.SECONDS);
		executor.allowCoreThreadTimeOut(true);
		return Executors.unconfigurableScheduledExecutorService(executor);
	}
}