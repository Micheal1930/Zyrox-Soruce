package com.zyrox.world.content.skill.impl.dungeoneering;
import com.zyrox.model.GameObject;
import com.zyrox.model.Position;
import com.zyrox.world.entity.impl.npc.NPC;

/**
 * I couldn't be arsed to put all npc spawns in the enum.
 * @author Gabriel Hannason
 */
public enum DungeoneeringFloor {

	FIRST_FLOOR(new Position(2451, 4935), new Position(2448, 4939), new GameObject[] {new GameObject(1276, new Position(2451, 4939)), new GameObject(45803, new Position(2455, 4940)), new GameObject(1767, new Position(2477, 4940))}, new NPC[][]{{NPC.of(491, new Position(2440, 4958)), NPC.of(688, new Position(2443, 4954)), NPC.of(13, new Position(2444, 4958)), NPC.of(5664, new Position(2460, 4965)), NPC.of(90, new Position(2460, 4961)), NPC.of(90, new Position(2462, 4965)), NPC.of(1624, new Position(2474, 4958)), NPC.of(174, new Position(2477, 4954)), NPC.of(2060, new Position(2473, 4940)), NPC.of(688, new Position(2471, 4937)), NPC.of(688, new Position(2474, 4943))}, {NPC.of(124, new Position(2441, 4958)), NPC.of(108, new Position(2441, 4954)), NPC.of(688, new Position(2443, 4956)), NPC.of(111, new Position(2460, 4965)), NPC.of(52, new Position(2457, 4961)), NPC.of(1643, new Position(2477, 4954)), NPC.of(13, new Position(2477, 4958)), NPC.of(13, new Position(2474, 4958)), NPC.of(8549, new Position(2473, 4940))}, {NPC.of(13, new Position(2441, 4958)), NPC.of(13, new Position(2441, 4955)), NPC.of(13, new Position(2443, 4954)), NPC.of(1643, new Position(2445, 4956)), NPC.of(13, new Position(2443, 4958)), NPC.of(13, new Position(2445, 4958)), NPC.of(13, new Position(2445, 4954)), NPC.of(2019, new Position(2461, 4965)), NPC.of(27, new Position(2458, 4966)), NPC.of(27, new Position(2458, 4961)), NPC.of(27, new Position(2458, 4967)), NPC.of(5361, new Position(2476, 4957)), NPC.of(3495, new Position(2475, 4954)), NPC.of(491, new Position(2472, 4957)), NPC.of(1382, new Position(2473, 4940))}, {NPC.of(8162, new Position(2441, 4954)), NPC.of(8162, new Position(2441, 4957)), NPC.of(90, new Position(2443, 4958)), NPC.of(90, new Position(2443, 4954)), NPC.of(90, new Position(2440, 4956)), NPC.of(2896, new Position(2458, 4967)), NPC.of(2896, new Position(2462, 4967)), NPC.of(2896, new Position(2462, 4960)), NPC.of(2896, new Position(2457, 4960)), NPC.of(2896, new Position(2459, 4964)), NPC.of(1880, new Position(2456, 4964)), NPC.of(110, new Position(2472, 4955)), NPC.of(688, new Position(2477, 4954)), NPC.of(84, new Position(2477, 4957)), NPC.of(9939, new Position(2472, 4940))}});

	DungeoneeringFloor(Position entrance, Position smuggler, GameObject[] objects, NPC[][] npcs) {
		this.entrance = entrance;
		this.smugglerPosition = smuggler;
		this.objects = objects;
		this.npcs = npcs;
	}

	private Position entrance, smugglerPosition;
	private GameObject[] objects;
	private NPC[][] npcs;
	
	public Position getEntrance() {
		return entrance;
	}

	public Position getSmugglerPosition() {
		return smugglerPosition;
	}

	public GameObject[] getObjects() {
		return objects;
	}
	
	public NPC[][] getNpcs() {
		return npcs;
	}

	public static DungeoneeringFloor forId(int id) {
		for(DungeoneeringFloor floors : DungeoneeringFloor.values()) {
			if(floors != null && floors.ordinal() == id) {
				return floors;
			}
		}
		return null;
	}
}