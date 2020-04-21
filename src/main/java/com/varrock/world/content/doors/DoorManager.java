package com.varrock.world.content.doors;

import java.util.HashMap;
import java.util.Map;

import com.varrock.model.GameObject;
import com.varrock.model.Position;
import com.varrock.model.definitions.GameObjectDefinition;
import com.varrock.util.Misc;
import com.varrock.util.Stopwatch;
import com.varrock.world.HashedPosition;
import com.varrock.world.clip.region.RegionClipping;
import com.varrock.world.content.CustomObjects;
import com.varrock.world.content.doors.DoorConstants.Direction;
import com.varrock.world.entity.impl.player.Player;

/**
 * @author Jonny
 * @author relex lawl
 */
public final class DoorManager {

	public static Stopwatch PROCESS_TIMER = new Stopwatch();

	public static boolean addDoor(int objectId, int directionId, int type, Position position) {
		DoorHingeType doorHingeType = null;

		GameObjectDefinition objectDef = GameObjectDefinition.getDefinition(objectId);

		if(type != 0) { //not a door
			return false;
		}

		//check if the object is a left hinge door
		if(DoorConstants.LEFT_HINGE_DOOR_IDS.contains(objectId)) {
			doorHingeType = DoorHingeType.LEFT;
		}

		//check if the object is a right hinge door
		if(DoorConstants.RIGHT_HINGE_DOOR_IDS.contains(objectId)) {
			doorHingeType = DoorHingeType.RIGHT;
		} else if(doorHingeType == null) {
			if(objectDef != null && objectDef.name != null) {
				if (objectDef.name.toLowerCase().equalsIgnoreCase("door")) {
					if (objectDef.getSizeX() == 1 && objectDef.getSizeY() == 1) {
						doorHingeType = DoorHingeType.RIGHT;
						//use this to dump all object ids/positions, but not needed since we cheaphax it
						/*if(DOOR_DUMP.get(objectId) == null) {
							Misc.print("objectId=" + objectId + ", position=" + position);
							DOOR_DUMP.put(objectId, position);
						}*/
					}
				}
			}
		}

		HashedPosition hashedPosition = new HashedPosition(position);
		if(DoorConstants.UNCLOSEABLE_POSITIONS.contains(hashedPosition)) {
			doorHingeType = null;
		}

		//check if the object is a door
		if(doorHingeType != null) {
			DoorConstants.Direction closedDirection = DoorConstants.Direction.getDirection(directionId);
			DoorConstants.Direction openDirection = getOpenDirection(doorHingeType, closedDirection);

			DoorState openState = new DoorState(objectId, openDirection, new HashedPosition(getOpenPosition(closedDirection, position)));

			//gates such as lumbridge chickens, entrana skilling zone, etc
			if(DoorConstants.DOUBLE_WIDE_GATE_IDS.contains(objectId)) {
				if(closedDirection == Direction.EAST) {
					openState = new DoorState(objectId, Direction.NORTH, new HashedPosition(getOpenPosition(closedDirection, position).translate(1, 1)));
				} else if(closedDirection == Direction.WEST) {
					openState = new DoorState(objectId, Direction.SOUTH, new HashedPosition(getOpenPosition(closedDirection, position).translate(-1, -1)));
				} else if(closedDirection == Direction.SOUTH) {
					openState = new DoorState(objectId, Direction.EAST, new HashedPosition(getOpenPosition(closedDirection, position).translate(1, -1)));
				} else if(closedDirection == Direction.NORTH) {
					openState = new DoorState(objectId, Direction.WEST, new HashedPosition(getOpenPosition(closedDirection, position).translate(-1, 1)));
				}
			}

			Door door = new Door(doorHingeType, openState, new DoorState(objectId, closedDirection, new HashedPosition(position)));

			//locations that doors are open by default
			if(objectDef != null) {
				if(objectDef.name != null) {
					if(objectDef.interactive && objectDef.actions != null) {
						for(String action : objectDef.actions) {
							if(action == null) {
								continue;
							}
							if(action.equalsIgnoreCase("Close")) {
								return true;
							}
						}
					}
				}
			}

			GLOBAL_DOORS.put(new HashedPosition(door.getClosedState().getPosition()), door);
			return true;
		}

		return false;
	}

	/**
	 * The direction that a door will be after the door is openeed
	 * @param doorHingeType
	 * @param originalDirection
	 * @return
	 */
	public static Direction getOpenDirection(DoorHingeType doorHingeType, DoorConstants.Direction originalDirection) {
		switch(doorHingeType) {
			case LEFT:
				switch(originalDirection) {
					case NORTH:
						return Direction.WEST;
					case SOUTH:
						return Direction.EAST;
					case WEST:
						return Direction.SOUTH;
					case EAST:
						return Direction.NORTH;
				}
				break;
			case RIGHT:
				switch(originalDirection) {
					case NORTH:
						return Direction.EAST;
					case SOUTH:
						return Direction.WEST;
					case WEST:
						return Direction.NORTH;
					case EAST:
						return Direction.SOUTH;
				}
				break;
		}
		Misc.print("Invalid door hinge for door!");
		return originalDirection;
	}

	/**
	 * The position that a door is placed at when it is opened
	 * @param direction
	 * @param position
	 * @return
	 */
	public static Position getOpenPosition(Direction direction, Position position) {
		switch(direction) {
			case WEST:
				return new Position(position.getX() - 1, position.getY(), position.getZ());
			case EAST:
				return new Position(position.getX() + 1, position.getY(), position.getZ());
			case NORTH:
				return new Position(position.getX(), position.getY() + 1, position.getZ());
			case SOUTH:
				return new Position(position.getX(), position.getY() - 1, position.getZ());
		}
		Misc.print("Invalid direction for door!");
		return position;
	}

	/**
	 * Open a door and return if it has been opened
	 * @param gameObject
	 * @param secondary
	 * @return
	 */
	public static DoorResponse isDoorOperated(GameObject gameObject, boolean secondary) {

		Door door = GLOBAL_DOORS.get(new HashedPosition(gameObject.getPosition()));

		if(DoorConstants.CUSTOM_DOOR_IDS.contains(gameObject.getId())) {
			return DoorResponse.INVALID_DOOR;
		}

		GameObjectDefinition objectDef = GameObjectDefinition.getDefinition(gameObject.getId());

		//cheaphax to remove diaganol doors because they run off another formula
		if(gameObject.getType() == 9) {
			if(objectDef != null) {
				if(objectDef.name != null) {
					if(objectDef.name.equalsIgnoreCase("door")) {
						CustomObjects.addDeletedObject(gameObject);
						CustomObjects.deleteGlobalObject(gameObject);
						RegionClipping.removeClippingForVariableObject(gameObject.getPosition().getX(), gameObject.getPosition().getY(), gameObject.getPosition().getZ(), gameObject.getType(), gameObject.getFace(), door.getDoorStatus() == DoorStatus.OPEN);
						return DoorResponse.OPENED;
					}
				}
			}
		}

		//cheaphax for opening 2 double doors at the same time
		if(objectDef != null && !secondary) {
			if(objectDef.name != null) {
				if(objectDef.name.toLowerCase().contains("large door") || objectDef.name.toLowerCase().contains("gate") || DoorConstants.DOUBLE_DOOR_IDS.contains(gameObject.getId())) {
					for (int x = -2; x < 2; x++) {
						for (int y = -2; y < 2; y++) {
							 if(x == 0 && y == 0) {
							 	continue;
							 }
							HashedPosition positionToCheck = new HashedPosition(new Position(x + gameObject.getPosition().getX(), y + gameObject.getPosition().getY(), gameObject.getPosition().getZ()));
							Door secondaryDoor = GLOBAL_DOORS.get(positionToCheck);

							//open the second side of the door/gate
							if(secondaryDoor != null) {
								if (door == null) {
									continue;
								}
								if(secondaryDoor.getDoorStatus() != door.getDoorStatus()) {
									continue;
								}

								GameObject realGameObject = CustomObjects.getGameObject(new Position(x + gameObject.getPosition().getX(), y + gameObject.getPosition().getY(), gameObject.getPosition().getZ()), 0);

								if(realGameObject != null) {
									isDoorOperated(realGameObject, true);
								}
							}
						}
					}
				}
			}
		}

		if (door != null) {
			GameObject newDoor;

			if(door.getDoorStatus() == DoorStatus.CLOSED) {
				newDoor = new GameObject(door.getOpenState().getId(), new Position(door.getOpenState().getPosition().getX(), door.getOpenState().getPosition().getY(), door.getOpenState().getPosition().getZ()), gameObject.getType(), door.getOpenState().getDirection().getDirectionId());
			} else {
				if(door.getInteractions() >= 20) {
					return DoorResponse.STUCK_OPEN;
				}
				newDoor = new GameObject(door.getClosedState().getId(), new Position(door.getClosedState().getPosition().getX(), door.getClosedState().getPosition().getY(), door.getClosedState().getPosition().getZ()), gameObject.getType(), door.getClosedState().getDirection().getDirectionId());
			}

			if (newDoor != null) {

				gameObject.setType(0);
				newDoor.setType(0);


				CustomObjects.addDeletedObject(gameObject);
				CustomObjects.deleteGlobalObject(gameObject);

				CustomObjects.removeDeletedObject(newDoor);
				CustomObjects.spawnGlobalObject(newDoor);

				RegionClipping.removeClippingForVariableObject(gameObject.getPosition().getX(), gameObject.getPosition().getY(), gameObject.getPosition().getZ(), gameObject.getType(), gameObject.getFace(), door.getDoorStatus() == DoorStatus.OPEN);
				//toggle the door status
				door.setDoorStatus(door.getDoorStatus() == DoorStatus.CLOSED ? DoorStatus.OPEN : DoorStatus.CLOSED);

				door.setInteractions(door.getInteractions() + 1);

				if(door.getInteractions() == 20) {
					door.getInteractionTimer().reset();
				}

				door.getCloseTimer().reset();

				GLOBAL_DOORS.remove(new HashedPosition(gameObject.getPosition()));
				GLOBAL_DOORS.put(new HashedPosition(door.getDoorStatus() == DoorStatus.CLOSED ? door.getClosedState().getPosition() : door.getOpenState().getPosition()), door);
			}

			return DoorResponse.OPENED;
		}
		return DoorResponse.INVALID_DOOR;
	}

	/**
	 * This is a process to check if a door has been open for 300 seconds.
	 * We run this every 10 seconds.
	 */
	public static void process() {
		if(!PROCESS_TIMER.elapsed(10_000)) {
			return;
		}

		Map<HashedPosition, Door> DOORS = new HashMap<>();
		DOORS.putAll(GLOBAL_DOORS);

		for(Door door : DOORS.values()) {
			if(door == null) {
				continue;
			}

			if(door.getInteractionTimer().elapsed(30_000)) {
				if(door.getInteractionTimer().elapsed(60_000) || door.getInteractions() >= 20) {
					door.setInteractions(0);
					door.getInteractionTimer().reset();
				}
			}

			if(door.getCloseTimer().elapsed(300_000)) {
				if (door.getDoorStatus() == DoorStatus.OPEN) {

					GameObject realGameObject = CustomObjects.getGameObject(new Position(door.getOpenState().getPosition().getX(), door.getOpenState().getPosition().getY(), door.getOpenState().getPosition().getZ()), 0);

					if (realGameObject != null) {
						isDoorOperated(realGameObject, false);
					}
				}
			}
		}
		PROCESS_TIMER.reset();
	}
	
	public static final Map<HashedPosition, Door> GLOBAL_DOORS = new HashMap<>();

	public static final Map<Integer, Position> DOOR_DUMP = new HashMap<>();

	public static void doorUsed(Player player) {
		// To prevent the bug where the door opens but the attacker does not re-path to attack.
		// https://dl.dropboxusercontent.com/s/0rc0j2hm0egmsfz/2019-03-14_18-05-17.gif
		/*List<Player> players = World.getPlayersWithinDistance(player.getPosition(), 16);
		for (Player found : players) {
			found.timePathingChanged = System.currentTimeMillis();
		}*/
	}
}