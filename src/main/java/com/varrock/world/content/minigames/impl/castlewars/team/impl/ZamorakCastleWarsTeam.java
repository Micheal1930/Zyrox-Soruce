package com.varrock.world.content.minigames.impl.castlewars.team.impl;

import com.varrock.model.Boundary;
import com.varrock.model.Item;
import com.varrock.model.Position;
import com.varrock.world.content.minigames.impl.castlewars.item.flag.CastleWarsFlag;
import com.varrock.world.content.minigames.impl.castlewars.object.catapult.CastleWarsCatapult;
import com.varrock.world.content.minigames.impl.castlewars.team.CastleWarsTeam;

/**
 * Represents the Zamorak team of Castle Wars
 * 
 * @author 2012 <https://www.rune-server.ee/members/dexter+morgan/>
 *
 */
public class ZamorakCastleWarsTeam extends CastleWarsTeam {

	/**
	 * Zamorak waiting lobby
	 */
	public static final Position ZAMORAK_WAITING_LOBBY = new Position(2421, 9524, 0);

	/**
	 * The zamorak cloak
	 */
	public static final Item ZAMORAK_CLOAK = new Item(4042);

	/**
	 * The castle
	 */
	private static final Boundary CASTLE = new Boundary(new Position(2368, 3117, 0), new Position(2386, 3135, 3));

	/**
	 * The zamorak start position
	 */
	private static final Position ZAMORAK_PREP_ROOM = new Position(2370, 3131, 1);
	
	/**
	 * Creates a new Zamorak team
	 */
	public ZamorakCastleWarsTeam() {
		super(ZAMORAK_WAITING_LOBBY, ZAMORAK_CLOAK, CastleWarsFlag.ZAMORAK, CASTLE, CastleWarsCatapult.ZAMORAK, ZAMORAK_PREP_ROOM);
	}

}
