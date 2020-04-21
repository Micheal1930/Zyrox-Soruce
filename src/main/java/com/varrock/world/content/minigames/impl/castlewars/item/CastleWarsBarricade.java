package com.varrock.world.content.minigames.impl.castlewars.item;

import com.varrock.engine.task.Task;
import com.varrock.engine.task.TaskManager;
import com.varrock.model.Boundary;
import com.varrock.model.Flag;
import com.varrock.model.Graphic;
import com.varrock.model.Position;
import com.varrock.world.World;
import com.varrock.world.clip.region.RegionClipping;
import com.varrock.world.content.minigames.impl.castlewars.CastleWarsManager;
import com.varrock.world.content.minigames.impl.castlewars.npc.CastleWarsBarricadeNPC;
import com.varrock.world.content.minigames.impl.castlewars.team.CastleWarsTeam;
import com.varrock.world.entity.impl.npc.NPC;
import com.varrock.world.entity.impl.player.Player;

/**
 * Represents a castle wars barricade
 * 
 * @author 2012 <https://www.rune-server.ee/members/dexter+morgan/>
 *
 */
public class CastleWarsBarricade {

	/**
	 * The exploding barricade
	 */
	private static final Graphic BARRICADE_EXPLODE = new Graphic(157);

	/**
	 * The max barricades per team
	 */
	private static final int MAX_TEAM_BARRICADE = 10;

	/**
	 * The time to remove the barricade on fire
	 */
	private static final int BARRICADE_FIRE_REMOVAL_TIME = 10;

	/**
	 * The normal barricade id
	 */
	public static final int BARRICADE = 1532;

	/**
	 * The barricade on fire
	 */
	private static final int BARRICADE_ON_FIRE = 1533;

	/**
	 * Setting up a barricade
	 * 
	 * @param player the player
	 */
	public static void setup(Player player) {
		/*
		 * The team
		 */
		CastleWarsTeam team = CastleWarsManager.getTeam(player);
		/*
		 * No team
		 */
		if (team == null) {
			return;
		}
		/*
		 * Check barricades
		 */
		if (team.getBarricades().size() == MAX_TEAM_BARRICADE) {
			player.getPacketSender().sendMessage("Your team has laid out the max barricades of " + MAX_TEAM_BARRICADE);
			return;
		}
		/*
		 * Not in safae zones
		 */
		if (Boundary.inside(player, CastleWarsManager.SAFE_AREAS)) {
			player.getPacketSender().sendMessage("You cannot lay a barricade here.");
			return;
		}
		/*
		 * The position
		 */
		final Position position = player.getPosition().copy();
		/*
		 * Check existing
		 */
		for (NPC npc : team.getBarricades()) {
			if (npc == null) {
				continue;
			}
			if (npc.getPosition().sameAs(position)) {
				player.getPacketSender().sendMessage("You can't lay a barricade here.");
				return;
			}
		}
		/*
		 * Delete
		 */
		player.getInventory().delete(CastleWarsManager.BARRICADE);
		/*
		 * The barricade
		 */
		CastleWarsBarricadeNPC barricade = new CastleWarsBarricadeNPC(BARRICADE, position);
		RegionClipping.addClipping(position.getX(), position.getY(), position.getZ(), RegionClipping.BLOCKED_TILE);
		/*
		 * Add to team
		 */
		team.getBarricades().add(barricade);
		/*
		 * Add to world
		 */
		World.register(barricade);
		player.getPacketSender().sendMessage("You setup a barricade");
	}

	/**
	 * Exploding barricade
	 * 
	 * @param player    the player
	 * @param barricade the barricade
	 */
	public static void explodeBarricade(Player player, NPC barricade) {
		/*
		 * Explode
		 */
		player.getInventory().delete(CastleWarsManager.EXPLOSIVE_POTION);
		player.getPacketSender().sendGraphic(BARRICADE_EXPLODE, barricade.getPosition());
		/*
		 * Remove barricade
		 */
		removeBarricade(barricade);
	}

	/**
	 * Setting barricade on fire
	 * 
	 * @param player    the player
	 * @param barricade the barricade
	 */
	public static void setBarricadeOnFire(Player player, NPC barricade) {
		/*
		 * The team
		 */
		CastleWarsTeam team = CastleWarsManager.getTeam(player);
		/*
		 * No team
		 */
		if (team == null) {
			return;
		}
		/*
		 * Set on fire
		 */
		barricade.setTransformationId(BARRICADE_ON_FIRE);
		barricade.getUpdateFlag().flag(Flag.TRANSFORM);
		player.getPacketSender().sendMessage("You set the barricade on fire.");
		TaskManager.submit(new Task(BARRICADE_FIRE_REMOVAL_TIME) {

			@Override
			protected void execute() {
				removeBarricade(barricade);
				stop();
			}
		});
	}

	/**
	 * Removing barricade
	 * 
	 * @param team      the team
	 * @param barricade the barricade
	 */
	public static void removeBarricade(NPC barricade) {
		CastleWarsTeam team = null;
		for(NPC n : CastleWarsManager.saradomin.getBarricades()) {
			if(n == null) {
				continue;
			}
			if(n.getPosition().sameAs(barricade.getPosition())) {
				team = CastleWarsManager.saradomin;
				break;
			}
		}
		if(team == null) {
			for(NPC n : CastleWarsManager.zamorak.getBarricades()) {
				if(n == null) {
					continue;
				}
				if(n.getPosition().sameAs(barricade.getPosition())) {
					team = CastleWarsManager.zamorak;
					break;
				}
			}
		}
		if(team == null) {
			team = CastleWarsManager.zamorak;
		}
		RegionClipping.removeClipping(barricade.getPosition().getX(), barricade.getPosition().getY(),
				barricade.getPosition().getZ(), 0x000000);
		team.getBarricades().remove(barricade);
		team.getBarricades().trimToSize();
		World.deregister(barricade);
	}
}
