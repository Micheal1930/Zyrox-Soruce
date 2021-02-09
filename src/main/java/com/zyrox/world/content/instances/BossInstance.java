package com.zyrox.world.content.instances;

import com.zyrox.engine.task.Task;
import com.zyrox.world.entity.impl.player.Player;

public abstract class BossInstance extends Task {

	protected int plane;

	protected Player player;
	
	public abstract void start();
	
	public BossInstance(Player player) {
		this.player = player;
	}
	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}
	public int getPlane() {
		return plane;
	}

	public void setPlane(int plane) {
		this.plane = plane;
	}
	
}
