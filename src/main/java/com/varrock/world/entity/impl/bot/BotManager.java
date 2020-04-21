package com.varrock.world.entity.impl.bot;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.varrock.model.Position;
import com.varrock.net.PlayerSession;
import com.varrock.world.World;
import com.varrock.world.entity.impl.bot.script.BotScript;
import com.varrock.world.entity.impl.bot.script.BotScriptRepository;

public class BotManager {

	/**
	 * A hash collection of bot names.
	 */
	private static final Set<String> BOT_NAMES = new HashSet<>();

	/**
	 * Gets the bot names.
	 * 
	 * @return The bot names.
	 */
	public static Set<String> getBotNames() {
		return BOT_NAMES;
	}

	/**
	 * A hash collection of the currently active scripts.
	 */
	private static final Set<BotScript> ACTIVE_SCRIPTS = ConcurrentHashMap.newKeySet();

	public static void addBot(String script, String username, Position position) {
		BotPlayer plr = new BotPlayer(username, position, new PlayerSession(null));

		startScript(plr, script.toLowerCase() + "script");

		BOT_NAMES.add(username);
	}

	/**
	 * Processes the active scripts.
	 */
	public static void process() {
		for (Iterator<BotScript> it = ACTIVE_SCRIPTS.iterator(); it.hasNext();) {
			BotScript script = it.next();

			try {
				if (World.getPlayerByName(script.getPlayer().getName()) == null) {
					script.stop();
				}
				
				/*if (System.currentTimeMillis() >= script.getPlayer().getSessionEnd()) {
					script.stop();
					script.getPlayer().logout(true);
				}*/

				if (script.stopped()) {
					script.onStop();
					it.remove();
					continue;
				}

				if (System.currentTimeMillis() - script.getLastAction() < script.getActionDelay()) {
					continue;
				}

				script.execute();
			} catch (Exception e) {
				System.out.println("Error processing script: " + script.getClass().getName() + " for " + script.getPlayer().getName() + ".");
				e.printStackTrace();
			}
		}
	}

	/**
	 * Executes the specified {@link BotScript} for the specified
	 * {@link BotPlayer}.
	 */
	public static void startScript(BotPlayer player, String name) {
		try {
			BotScript script = BotScriptRepository.getScripts().get(name).getClass().newInstance();
			player.setActiveScript(script);
			script.setPlayer(player);
			script.initialize();
			ACTIVE_SCRIPTS.add(script);
		} catch (Exception e) {
			System.out.println("Error starting script: " + name + " for " + player.getName() + ".");
			e.printStackTrace();
		}
	}

	/**
	 * Stops the specified {@link BotScript}.
	 * 
	 * @param script
	 *            The script.
	 */
	public static void stopScript(BotScript script) {
		ACTIVE_SCRIPTS.remove(script);
	}

}
