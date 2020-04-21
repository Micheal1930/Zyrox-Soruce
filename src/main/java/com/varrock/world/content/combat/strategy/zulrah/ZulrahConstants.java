package com.varrock.world.content.combat.strategy.zulrah;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.varrock.model.Animation;
import com.varrock.model.Position;
import com.varrock.world.World;
import com.varrock.world.entity.impl.npc.NPC;
import com.varrock.world.entity.impl.npc.impl.Zulrah;
import com.varrock.world.entity.impl.player.Player;


public class ZulrahConstants {

	public final static Position NORTH_POSITION = new Position(2268, 3074);
	public final static Position WEST_POSITION = new Position(2258, 3075);
	public final static Position EAST_POSITION = new Position(2278, 3075);
	public final static Position SOUTH_POSITION = new Position(2268, 3064);
	
	
	
	public final static int[][] TOXIC_FUME_LOCATIONS_1 = { { 2263, 3076 }, { 2263, 3073 }, { 2263, 3070 }, { 2266, 3069 },
													{ 2269, 3069 }, { 2272, 3070 }, { 2272, 3073 }, { 2273, 3076 } };
	
	public final static int[][] TOXIC_FUME_LOCATIONS_2 = { { 2263, 3070 }, { 2266, 3069 }, { 2269, 3069 }, { 2272, 3070 } };
	
	public final static int CRIMSON_ZULRAH_ID = 2044;
	public final static int GREEN_ZULRAH_ID = 2043;
	public final static int BLUE_ZULRAH_ID = 2042;
	
	public final static Animation RISE = new Animation(5073);
	public final static Animation DIVE = new Animation(5072);
	
	public static List<ZulrahRotation> zulrahRotations = new ArrayList<>();
	public static Map<Integer, ZulrahPhase> zulrahPhases = new HashMap<>();
	
	public static void initialize() {
		setRotations();
		setPhases();
	}

	private static void setRotations() {
		zulrahRotations.add(new ZulrahRotation(1, "first rotation"));
	}
	
	private static void setPhases() {
		zulrahPhases.put(1, new ZulrahPhase(2, ZulrahConstants.GREEN_ZULRAH_ID, "Full cloud", NORTH_POSITION.getX(), NORTH_POSITION.getY()));
		zulrahPhases.put(2, new ZulrahPhase(3, ZulrahConstants.CRIMSON_ZULRAH_ID, "Normal melee attack", NORTH_POSITION.getX(), NORTH_POSITION.getY()));
		zulrahPhases.put(3, new ZulrahPhase(4, ZulrahConstants.BLUE_ZULRAH_ID, "Normal mage attack", NORTH_POSITION.getX(), NORTH_POSITION.getY()));
		zulrahPhases.put(4, new ZulrahPhase(5, ZulrahConstants.GREEN_ZULRAH_ID, "Snakelings, right clouds", SOUTH_POSITION.getX(), SOUTH_POSITION.getY()));
		zulrahPhases.put(5, new ZulrahPhase(6, ZulrahConstants.CRIMSON_ZULRAH_ID, "Normal melee attack", NORTH_POSITION.getX(), NORTH_POSITION.getY()));
		zulrahPhases.put(6, new ZulrahPhase(7, ZulrahConstants.BLUE_ZULRAH_ID, "Normal mage attack", WEST_POSITION.getX(), WEST_POSITION.getY()));
		zulrahPhases.put(7, new ZulrahPhase(8, ZulrahConstants.GREEN_ZULRAH_ID, "Snakelings, right clouds", SOUTH_POSITION.getX(), SOUTH_POSITION.getY()));
		zulrahPhases.put(8, new ZulrahPhase(9, ZulrahConstants.GREEN_ZULRAH_ID, "Range/Mage switch", WEST_POSITION.getX(), WEST_POSITION.getY()));
		zulrahPhases.put(9, new ZulrahPhase(1, ZulrahConstants.CRIMSON_ZULRAH_ID, "Normal melee", NORTH_POSITION.getX(), NORTH_POSITION.getY()));
	}
	
	public static void startBossFight(Player player) {

		ZulrahRotation rotation = zulrahRotations.get(0);
		
		int initialPhase = 0;
		switch(rotation.getRotationID()) {
			case 1:
				initialPhase = 1;
				break;	
			case 2:
				initialPhase = 2;
				break;	
			case 3:
				initialPhase = 3;
				break;
		}
		
		Zulrah zulrah = (Zulrah) NPC.of(ZulrahConstants.GREEN_ZULRAH_ID, new Position(ZulrahConstants.zulrahPhases.get(initialPhase).getZulrahX(), ZulrahConstants.zulrahPhases.get(initialPhase).getZulrahY(), player.getIndex() *4 ));
		zulrah.setPlayer(player);
		zulrah.setSpawnedFor(player);
		zulrah.spawn(initialPhase);
		player.lastZulrah = zulrah;
		
		
		World.register(zulrah);

		player.sendMessage("Your fight with Zulrah has begun.");
		return;
	}

}
