package com.varrock.world.content.skill.impl.hunter;

import com.varrock.model.GameObject;
import com.varrock.world.entity.impl.player.Player;

/**
 * 
 * @author Faris
 */
public class BoxTrap extends Trap {

	private TrapState state;

	public BoxTrap(GameObject obj, TrapState state, int ticks, Player p) {
		super(obj, state, ticks, p);
	}

	/**
	 * @return the state
	 */
	public TrapState getState() {
		return state;
	}

	/**
	 * @param state
	 *            the state to set
	 */
	public void setState(TrapState state) {
		this.state = state;
	}

}
