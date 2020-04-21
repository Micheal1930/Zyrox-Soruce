package com.varrock.world.content.minigames.impl.castlewars.npc.lanthus;

import com.varrock.model.Item;

/**
 * Represents an arrow being collection
 * 
 * @author 2012 <https://www.rune-server.ee/members/dexter+morgan/>
 *
 */
public class CaslteWarsArrowCollection {

	/**
	 * The user
	 */
	private String user;

	/**
	 * The arrow
	 */
	private Item arrow;

	/**
	 * Represents an arrow collection
	 * 
	 * @param user  the user
	 * @param arrow the arrow
	 */
	public CaslteWarsArrowCollection(String user, Item arrow) {
		this.setUser(user);
		this.setArrow(arrow);
	}

	/**
	 * Gets the user
	 *
	 * @return the user
	 */
	public String getUser() {
		return user;
	}

	/**
	 * Sets the user
	 *
	 * @param user the user
	 */
	public void setUser(String user) {
		this.user = user;
	}

	/**
	 * Gets the arrow
	 *
	 * @return the arrow
	 */
	public Item getArrow() {
		return arrow;
	}

	/**
	 * Sets the arrow
	 *
	 * @param arrow the arrow
	 */
	public void setArrow(Item arrow) {
		this.arrow = arrow;
	}

}
