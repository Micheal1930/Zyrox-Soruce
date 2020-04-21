package com.varrock.world.content.minigames.impl.castlewars.team.impl;

import com.varrock.model.Boundary;
import com.varrock.model.Item;
import com.varrock.model.Position;
import com.varrock.world.content.minigames.impl.castlewars.item.flag.CastleWarsFlag;
import com.varrock.world.content.minigames.impl.castlewars.object.catapult.CastleWarsCatapult;
import com.varrock.world.content.minigames.impl.castlewars.team.CastleWarsTeam;

/**
 * Represents the Saradomin team of Castle Wars
 * 
 * @author 2012 <https://www.rune-server.ee/members/dexter+morgan/>
 *
 */
public class SaradominCastleWarsTeam extends CastleWarsTeam {

	/**
	 * Saradomin waiting lobby
	 */
	public static final Position SARADOMIN_WAITING_LOBBY = new Position(2377, 9485, 0);

	/**
	 * The saradomin cloak
	 */
	public static final Item SARADOMIN_CLOAK = new Item(4041);

	/**
	 * The castle
	 */
	private static final Boundary CASTLE = new Boundary(new Position(2412, 3072, 0), new Position(2431, 3091, 3));


	/**
	 * The saradomin start position
	 */
	private static final Position SARADOMIN_PREP_ROOM = new Position(2429, 3076, 1);
	
	/**
	 * Creates a new Saradomin team
	 */
	public SaradominCastleWarsTeam() {
		super(SARADOMIN_WAITING_LOBBY, SARADOMIN_CLOAK, CastleWarsFlag.SARADOMIN, CASTLE, CastleWarsCatapult.SARADOMIN, SARADOMIN_PREP_ROOM);
	}
}
