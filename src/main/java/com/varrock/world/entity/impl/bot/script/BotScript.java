package com.varrock.world.entity.impl.bot.script;

import com.varrock.world.entity.impl.bot.BotPlayer;

public abstract class BotScript {
	
	protected BotPlayer player;
	
	public BotPlayer getPlayer() {
		return player;
	}
	
	public void setPlayer(BotPlayer player) {
		this.player = player;
	}
	
	private boolean stopped = false;
	
	public boolean stopped() {
		return stopped;
	}
	
	private long lastAction;
	
	public long getLastAction() {
		return lastAction;
	}
	
	private long actionDelay;
	
	public long getActionDelay() {
		return actionDelay;
	}
	
	public void sleep(long sleep) {
		lastAction = System.currentTimeMillis();
		actionDelay = sleep;
	}
	
	public abstract void initialize();

	public abstract void execute();
	
	public abstract void onStop();
	
	public void stop() {
		stopped = true;
	}
	
}
