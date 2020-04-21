package com.varrock.model.action;

import com.varrock.world.entity.impl.player.Player;

/**
 * Created by Greg on 18/11/2016.
 */
public abstract class Action {

	public abstract boolean start(Player player);

	public abstract boolean process(Player player);

	public abstract int processWithDelay(Player player);

	public abstract void stop(Player player);

	protected final void setActionDelay(Player player, int delay) {
		player.getActionManager().setActionDelay(delay);
	}
}