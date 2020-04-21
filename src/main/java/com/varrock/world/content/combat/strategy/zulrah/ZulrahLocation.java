package com.varrock.world.content.combat.strategy.zulrah;

import java.awt.Point;

public enum ZulrahLocation {
	
	
	NORTH(new Point(2266, 3073)), 
	SOUTH(new Point(2266, 3063)), 
	WEST(new Point(2258, 3072)),
	EAST(new Point(2276, 3072));

	private final Point location;

	private ZulrahLocation(Point location) {
		this.location = location;
	}

	public Point getLocation() {
		return location;
	}
}
