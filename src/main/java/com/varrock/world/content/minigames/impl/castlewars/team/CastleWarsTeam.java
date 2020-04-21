package com.varrock.world.content.minigames.impl.castlewars.team;

import java.util.ArrayList;

import com.varrock.model.Boundary;
import com.varrock.model.Item;
import com.varrock.model.Position;
import com.varrock.world.World;
import com.varrock.world.content.minigames.impl.castlewars.item.flag.CastleWarsFlag;
import com.varrock.world.content.minigames.impl.castlewars.npc.CastleWarsBarricadeNPC;
import com.varrock.world.content.minigames.impl.castlewars.object.catapult.CastleWarsCatapult;
import com.varrock.world.entity.impl.player.Player;

/**
 * Represents a single castle wars team
 * 
 * @author 2012 <https://www.rune-server.ee/members/dexter+morgan/>
 */
public class CastleWarsTeam {

	/**
	 * The team players
	 */
	private final ArrayList<Player> players = new ArrayList<Player>();

	/**
	 * The flag taken state
	 */
	private boolean flagTaken;

	/**
	 * The door health
	 */
	private int health;

	/**
	 * The door locked state
	 */
	private boolean doorLocked;

	/**
	 * The catapult state
	 */
	private boolean operational;

	/**
	 * If the flag dropped
	 */
	private boolean flagDropped;

	/**
	 * The lobby position
	 */
	private Position lobby;

	/**
	 * The cloak
	 */
	private Item cloak;

	/**
	 * The laid barricades
	 */
	private ArrayList<CastleWarsBarricadeNPC> barricades;

	/**
	 * The flag
	 */
	private CastleWarsFlag flag;

	/**
	 * The castle boundary
	 */
	private Boundary castle;
	
	/**
	 * The catapult
	 */
	private CastleWarsCatapult catapult;
	
	/**
	 * The last catapult fire
	 */
	private long lastCatapultFire;
	
	/**
	 * The prep room
	 */
	private Position prepRoom;
	
	/**
	 * Prepares a Castle Wars team
	 */
	public CastleWarsTeam(Position lobby, Item cloak, CastleWarsFlag flag, Boundary castle, CastleWarsCatapult catapult, Position prepRoom) {
		try {
			setLobby(lobby);
			setCloak(cloak);
			setFlag(flag);
			setCastle(castle);
			setCatapult(catapult);
			setLastCatapultFire(System.currentTimeMillis());
			setPrepRoom(prepRoom);
			setBarricades(new ArrayList<CastleWarsBarricadeNPC>());
			prepare();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Preparing the team
	 */
	public void prepare() {
		setFlagTaken(false);
		setHealth(100);
		setDoorLocked(true);
		setOperational(true);
		setFlagDropped(false);
		clearBarricades();
	}

	/**
	 * Clearing barricades
	 */
	public void clearBarricades() {
		barricades.stream().filter(t -> t != null).forEach(t -> World.deregister(t));
		barricades.clear();
	}

	/**
	 * Gets the players
	 * 
	 * @return the players
	 */
	public ArrayList<Player> getPlayers() {
		return players;
	}

	/**
	 * Gets the flagTaken
	 * 
	 * @return the flagTaken
	 */
	public boolean isFlagTaken() {
		return flagTaken;
	}

	/**
	 * Sets the flagTaken
	 * 
	 * @param flagTaken the flagTaken
	 */
	public void setFlagTaken(boolean flagTaken) {
		this.flagTaken = flagTaken;
	}

	/**
	 * Gets the health
	 * 
	 * @return the health
	 */
	public int getHealth() {
		return health;
	}

	/**
	 * Sets the health
	 * 
	 * @param health the health
	 */
	public void setHealth(int health) {
		this.health = health;
	}

	/**
	 * Gets the doorLocked
	 * 
	 * @return the doorLocked
	 */
	public boolean isDoorLocked() {
		return doorLocked;
	}

	/**
	 * Sets the doorLocked
	 * 
	 * @param doorLocked the doorLocked
	 */
	public void setDoorLocked(boolean doorLocked) {
		this.doorLocked = doorLocked;
	}

	/**
	 * Gets the operational
	 * 
	 * @return the operational
	 */
	public boolean isOperational() {
		return operational;
	}

	/**
	 * Sets the operational
	 * 
	 * @param operational the operational
	 */
	public void setOperational(boolean operational) {
		this.operational = operational;
	}

	/**
	 * Sets the flagDropped
	 * 
	 * @param flagDropped the flagDropped
	 */
	public void setFlagDropped(boolean flagDropped) {
		this.flagDropped = flagDropped;
	}

	/**
	 * Gets the flagDropped
	 * 
	 * @return the flagDropped
	 */
	public boolean isFlagDropped() {
		return flagDropped;
	}

	/**
	 * Gets the lobby
	 *
	 * @return the lobby
	 */
	public Position getLobby() {
		return lobby;
	}

	/**
	 * Sets the lobby
	 *
	 * @param lobby the lobby
	 */
	public void setLobby(Position lobby) {
		this.lobby = lobby;
	}

	/**
	 * Gets the cloak
	 *
	 * @return the cloak
	 */
	public Item getCloak() {
		return cloak;
	}

	/**
	 * Sets the cloak
	 *
	 * @param cloak the cloak
	 */
	public void setCloak(Item cloak) {
		this.cloak = cloak;
	}

	/**
	 * Gets the barricades
	 *
	 * @return the barricades
	 */
	public ArrayList<CastleWarsBarricadeNPC> getBarricades() {
		return barricades;
	}

	/**
	 * Sets the barricades
	 *
	 * @param barricades the barricades
	 */
	public void setBarricades(ArrayList<CastleWarsBarricadeNPC> barricades) {
		this.barricades = barricades;
	}

	/**
	 * Gets the flag
	 *
	 * @return the flag
	 */
	public CastleWarsFlag getFlag() {
		return flag;
	}

	/**
	 * Sets the flag
	 *
	 * @param flag the flag
	 */
	public void setFlag(CastleWarsFlag flag) {
		this.flag = flag;
	}

	/**
	 * Gets the castle
	 *
	 * @return the castle
	 */
	public Boundary getCastle() {
		return castle;
	}

	/**
	 * Sets the castle
	 *
	 * @param castle the castle
	 */
	public void setCastle(Boundary castle) {
		this.castle = castle;
	}

	/**
	 * Gets the catapult
	 *
	 * @return the catapult
	 */
	public CastleWarsCatapult getCatapult() {
		return catapult;
	}

	/**
	 * Sets the catapult
	 *
	 * @param catapult the catapult
	 */
	public void setCatapult(CastleWarsCatapult catapult) {
		this.catapult = catapult;
	}

	/**
	 * Gets the lastCatapultFire
	 *
	 * @return the lastCatapultFire
	 */
	public long getLastCatapultFire() {
		return lastCatapultFire;
	}

	/**
	 * Sets the lastCatapultFire
	 *
	 * @param lastCatapultFire the lastCatapultFire
	 */
	public void setLastCatapultFire(long lastCatapultFire) {
		this.lastCatapultFire = lastCatapultFire;
	}
	
	/**
	 * Gets the flag state
	 * @return the state
	 */
	public String getFlagState() {
		if(isFlagDropped()) {
			return "@red@Dropped";
		}
		if(isFlagTaken()) {
			return "@red@Taken";
		}
		return "@gre@Safe";
	}

	/**
	 * Gets the prepRoom
	 *
	 * @return the prepRoom
	 */
	public Position getPrepRoom() {
		return prepRoom;
	}

	/**
	 * Sets the prepRoom
	 *
	 * @param prepRoom the prepRoom
	 */
	public void setPrepRoom(Position prepRoom) {
		this.prepRoom = prepRoom;
	}
}