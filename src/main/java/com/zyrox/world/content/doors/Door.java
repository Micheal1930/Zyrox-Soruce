package com.zyrox.world.content.doors;

import com.zyrox.util.Stopwatch;

/**
 *
 * @author Relex
 */
public final class Door {

	public Door(DoorHingeType doorHingeType, DoorState openState, DoorState closedState) {
		this.doorHingeType = doorHingeType;
		this.openState = openState;
		this.closedState = closedState;
		this.closeTimer = new Stopwatch();
		this.interactionTimer = new Stopwatch();
	}

	private DoorStatus doorStatus = DoorStatus.CLOSED;

	private final DoorHingeType doorHingeType;
	
	private final DoorState openState;
	
	private final DoorState closedState;

	private Stopwatch closeTimer;

	private Stopwatch interactionTimer;

	private int interactions;
	
	public DoorState getOpenState() {
		return openState;
	}
	
	public DoorState getClosedState() {
		return closedState;
	}

	public DoorHingeType getDoorHingeType() {
		return doorHingeType;
	}

	public DoorStatus getDoorStatus() {
		return doorStatus;
	}

	public void setDoorStatus(DoorStatus doorStatus) {
		this.doorStatus = doorStatus;
	}

	public Stopwatch getCloseTimer() {
		return closeTimer;
	}

	public int getInteractions() {
		return interactions;
	}

	public void setInteractions(int interactions) {
		this.interactions = interactions;
	}

	public Stopwatch getInteractionTimer() {
		return interactionTimer;
	}
}