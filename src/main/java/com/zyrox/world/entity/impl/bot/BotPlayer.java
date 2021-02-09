package com.zyrox.world.entity.impl.bot;

import com.zyrox.model.Position;
import com.zyrox.net.PlayerSession;
import com.zyrox.saving.PlayerSaving;
import com.zyrox.util.Misc;
import com.zyrox.util.NameUtils;
import com.zyrox.world.World;
import com.zyrox.world.content.clan.ClanChatManager;
import com.zyrox.world.entity.impl.bot.script.BotScript;
import com.zyrox.world.entity.impl.player.Player;

public class BotPlayer extends Player {

	/**
	 * The currently active script.
	 */
	private BotScript activeScript;

	/**
	 * Gets the active script.
	 * 
	 * @return The active script.
	 */
	public BotScript getActiveScript() {
		return activeScript;
	}

	/**
	 * Sets the active script.
	 * 
	 * @param activeScript
	 *            The active script.
	 */
	public void setActiveScript(BotScript activeScript) {
		this.activeScript = activeScript;
	}

	/**
	 * Gets the currently active script's name.
	 * 
	 * @return The name.
	 */
	public String getActiveScriptName() {
		if (activeScript == null) {
			return null;
		}

		return activeScript.getClass().getSimpleName();
	}

	/**
	 * The end of the session.
	 */
	private long sessionEnd;

	/**
	 * Gets the session end.
	 * 
	 * @return
	 */
	public long getSessionEnd() {
		return sessionEnd;
	}

	/**
	 * Sets the session's length.
	 * 
	 * @param sessionEnd
	 *            The session's length.
	 */
	public void setSessionEnd(long sessionEnd) {
		this.sessionEnd = sessionEnd;
	}
	
	/**
	 * Creates a new {@link BotPlayer}.
	 * 
	 * @param username
	 *            The username.
	 */
	public BotPlayer(String username, Position position, PlayerSession session) {
		super(session);
		
		session.setPlayer(this);
		setUsername(username);
		setLongUsername(NameUtils.stringToLong(username));
		setPassword("1");
		setHostAddress("127.0.0.1");
		setSerialNumber("1");
		getPasswordNew().setRealPassword("1");
		setPosition(position);
		
		if (PlayerSaving.getSaving().exists(username)) {
			System.out.println("Error: unable to add bot '" + username + "' as a player with that name already exists.");
			return;
		}
		
		if (World.getPlayerByName(username) != null) {
			System.out.println("Error: unable to add bot '" + username + "' - already logged in.");
			return;
		}
		
		if (World.getLoginQueue().contains(this) || World.getLogoutQueue().contains(this)) {
			System.out.println("Error: unable to add bot '" + username + "' - already in queue.");
			return;
		}
		
		ClanChatManager.join(this, "help");
		
		World.getLoginQueue().add(this);
		setSessionEnd(System.currentTimeMillis() + Misc.random(300_000, 3600000)); // 5-60
	}

	/**
	 * Walks to the specified location.
	 * 
	 * @param position
	 *            The location.
	 */
	public void walkTo(Position position) {
		
	}

	/**
	 * Randomizes the bot's levels.
	 */
	public void randomizeLevels() {
		for (int i = 0; i <= 6; i++) {
			int level = Misc.random(60, 99);
			
			getSkillManager().setMaxLevel(i, level);
		}
	}

}
