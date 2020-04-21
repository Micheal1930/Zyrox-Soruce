package com.varrock.world.content.minigames.impl.castlewars.object.catapult;

import java.util.Optional;

import com.varrock.engine.task.Task;
import com.varrock.engine.task.TaskManager;
import com.varrock.model.Animation;
import com.varrock.model.GameObject;
import com.varrock.model.Graphic;
import com.varrock.model.Hit;
import com.varrock.model.Position;
import com.varrock.util.Misc;
import com.varrock.world.content.CustomObjects;
import com.varrock.world.content.minigames.impl.castlewars.CastleWarsManager;
import com.varrock.world.content.minigames.impl.castlewars.team.CastleWarsTeam;
import com.varrock.world.entity.impl.player.Player;

/**
 * Handles the Castle Wars catapult
 * 
 * @author 2012 <https://www.rune-server.ee/members/dexter+morgan/>
 * 
 */
public class CastleWarsCatapultManager {

	/**
	 * The broken catapult
	 */
	private static final int BROKEN_CATAPULT = 4385;

	/**
	 * The toolkit used to fix the catapult
	 */
	private static final int TOOLKIT = 4051;

	/**
	 * The fixing animation
	 */
	private static final Animation FIX_ANIMATION = new Animation(898);

	/**
	 * Getting hit by catapult
	 */
	private static final Graphic HIT = new Graphic(287);

	/**
	 * The interface id
	 */
	public static final int INTERFACE_ID = 11169;

	/**
	 * The hit radius impact
	 */
	private static final int HIT_RADIUS_IMPACT = 3;

	/**
	 * The max distance
	 */
	private static final int MAX_DISTANCE = 30;

	/**
	 * The max damage
	 */
	private static final int MAX_DAMAGE = 30;

	/**
	 * The miss fire chance
	 */
	private static final int MISS_FIRE_CHACE = 8;

	/**
	 * The firing interval in ms
	 */
	private static final int FIRE_INTERVAL = 15_000;

	/**
	 * The fire breakdown time
	 */
	private static final int ON_FIRE_BREAKDOWN_TIME = 50;

	/**
	 * Firing catapult
	 * 
	 * @param player
	 * @param id
	 */
	private static boolean fire(Player player, int id) {
		/*
		 * The catapult
		 */
		Optional<CastleWarsCatapult> catapult = CastleWarsCatapult.getCatapult(id);
		/*
		 * Not preset
		 */
		if (!catapult.isPresent()) {
			return false;
		}
		/*
		 * The team
		 */
		CastleWarsTeam team = CastleWarsManager.getTeam(player);
		/*
		 * Non operational
		 */
		if (!team.isOperational()) {
			return true;
		}
		/*
		 * Delay
		 */
		if (System.currentTimeMillis() - team.getLastCatapultFire() < FIRE_INTERVAL) {
			player.getPacketSender().sendMessage("The catapult needs to be cooled down before firing again.");
			return true;
		}
		/*
		 * Wrong team
		 */
		if (!team.getCatapult().equals(catapult.get())) {
			player.getPacketSender().sendMessage("You can only fire your teams catapult.");
			return true;
		}
		/*
		 * Doesn't have rocks
		 */
		if (!player.getInventory().contains(CastleWarsManager.ROCK.getId())) {
			player.getPacketSender().sendMessage("You don't have any rocks in your inventory.");
			return true;
		}
		/*
		 * Delete rock
		 */
		player.getInventory().delete(CastleWarsManager.ROCK);
		/*
		 * Misfire and damage
		 */
		if (Misc.random(MISS_FIRE_CHACE) == 1 || CastleWarsManager.TESTING) {
			team.setOperational(false);
			player.getLocalPlayers().forEach(p -> {
				if (p.getPosition().isWithinDistance(player.getPosition(), 3)) {
					p.dealDamage(new Hit(MAX_DAMAGE));
					p.getPacketSender().sendMessage("You get hit by the cannons misfire!");
				}
			});
			player.dealDamage(new Hit(MAX_DAMAGE));
			player.getPacketSender().sendMessage("The cannon misfires and damages nearby players and you.");
			final Position pos = team.getCatapult().getPosition().copy();
			/*
			 * Breaks the catapult
			 */
			GameObject broken = new GameObject(BROKEN_CATAPULT, pos, 10, team.getCatapult().getFace());
			CustomObjects.spawnGlobalObject(broken);
			return true;
		}
		/*
		 * The position
		 */
		Position hitPosition = catapult.get().getPosition().copy().add(Misc.exclusiveRandom(3, MAX_DISTANCE),
				Misc.exclusiveRandom(3, MAX_DISTANCE));
		/*
		 * Loops through the players
		 */
		for (Player victim : CastleWarsManager.getAllPlayers()) {
			/*
			 * Invalid player
			 */
			if (victim == null) {
				continue;
			}
			/*
			 * Within distance
			 */
			if (victim.getPosition().isWithinDistance(hitPosition, HIT_RADIUS_IMPACT)) {
				victim.dealDamage(new Hit(MAX_DAMAGE));
			}
			/*
			 * Display the graphic
			 */
			victim.getPacketSender().sendGraphic(HIT, hitPosition);
		}
		team.setOperational(true);
		team.setLastCatapultFire(System.currentTimeMillis());
		player.getPacketSender().sendMessage("You fire the catapult");
		return true;
	}

	/**
	 * Fixing catapult
	 * 
	 * @param player the player
	 * @param id     the id
	 * @param x      the x
	 * @param y      the y
	 */
	private static boolean fixCatapult(Player player, int id, int x, int y) {
		/*
		 * The catapult
		 */
		Optional<CastleWarsCatapult> catapult = CastleWarsCatapult.getCatapultByPosition(new Position(x, y, 0));
		/*
		 * Not preset
		 */
		if (!catapult.isPresent()) {
			return false;
		}
		/*
		 * The team
		 */
		CastleWarsTeam team = CastleWarsManager.getTeam(player);
		/*
		 * Already operational
		 */
		if (team.isOperational()) {
			return true;
		}
		/*
		 * Wrong team
		 */
		if (!team.getCatapult().equals(catapult.get())) {
			player.getPacketSender().sendMessage("Why would you fix the enemies catapult?");
			return true;
		}
		/*
		 * No toolkit
		 */
		if (!player.getInventory().contains(TOOLKIT)) {
			player.getPacketSender().sendMessage("You need a toolkit to fix the catapult.");
			return true;
		}
		/*
		 * Fixes catapult
		 */
		team.setOperational(true);
		player.performAnimation(FIX_ANIMATION);
		GameObject object = new GameObject(team.getCatapult().getId(), team.getCatapult().getPosition(), 10,
				team.getCatapult().getFace());
		CustomObjects.spawnGlobalObject(object);
		player.getPacketSender().sendMessage("You fix the catapult.");
		return true;
	}

	/**
	 * Setting catapult on fire
	 * 
	 * @param player the player
	 * @param id     the id
	 */
	public static boolean setCatapultOnFire(Player player, int id) {
		/*
		 * The catapult
		 */
		Optional<CastleWarsCatapult> catapult = CastleWarsCatapult.getCatapult(id);
		/*
		 * Not preset
		 */
		if (!catapult.isPresent()) {
			return false;
		}
		/*
		 * The team
		 */
		CastleWarsTeam team = CastleWarsManager.getTeam(player);
		/*
		 * Wrong team
		 */
		if (team.getCatapult().equals(catapult.get())) {
			player.getPacketSender().sendMessage("You can't set your teams catapult on fire!");
			return true;
		}
		/*
		 * The other team
		 */
		CastleWarsTeam opposite = CastleWarsManager.getOppositeTeam(player);
		/*
		 * Already operational
		 */
		if (!opposite.isOperational()) {
			return true;
		}
		/*
		 * Set on fire
		 */
		opposite.setOperational(false);
		/*
		 * The fire object
		 */
		GameObject object = new GameObject(opposite.getCatapult().getOnFire(), opposite.getCatapult().getPosition(), 10,
				opposite.getCatapult().getFace());
		CustomObjects.spawnGlobalObject(object);
		/*
		 * Set on fire
		 */
		player.getPacketSender().sendMessage("You set the catapult on fire!");
		TaskManager.submit(new Task(1) {
			int delay = 0;

			@Override
			protected void execute() {
				if (opposite.isOperational()) {
					stop();
				} else {
					delay++;
					if (delay == ON_FIRE_BREAKDOWN_TIME) {
						GameObject object = new GameObject(BROKEN_CATAPULT, opposite.getCatapult().getPosition(), 10,
								opposite.getCatapult().getFace());
						CustomObjects.spawnGlobalObject(object);
						stop();
					}
				}
			}

		});
		return true;
	}

	/**
	 * Extinguish catapult fire
	 * 
	 * @param player the player
	 * @param id     the id
	 */
	public static boolean extinguishFire(Player player, int id) {
		/*
		 * The catapult
		 */
		Optional<CastleWarsCatapult> catapult = CastleWarsCatapult.getOnFire(id);
		/*
		 * Not preset
		 */
		if (!catapult.isPresent()) {
			return false;
		}
		/*
		 * The team
		 */
		CastleWarsTeam team = CastleWarsManager.getTeam(player);
		/*
		 * Already operational
		 */
		if (team.isOperational()) {
			return true;
		}
		/*
		 * Wrong team
		 */
		if (!team.getCatapult().equals(catapult.get())) {
			player.getPacketSender().sendMessage("Why would you want to do that?");
			return true;
		}
		/*
		 * Set on fire
		 */
		team.setOperational(false);
		GameObject object = new GameObject(BROKEN_CATAPULT, team.getCatapult().getPosition(), 10,
				team.getCatapult().getFace());
		CustomObjects.spawnGlobalObject(object);
		player.getPacketSender().sendMessage("You extinguish the flames from the catapult");
		return true;
	}

	/**
	 * Preparing for gameplay
	 */
	public static void prepare() {
		GameObject saradomin = new GameObject(CastleWarsManager.saradomin.getCatapult().getId(),
				CastleWarsManager.saradomin.getCatapult().getPosition(), 10,
				CastleWarsManager.saradomin.getCatapult().getFace());
		CustomObjects.spawnGlobalObject(saradomin);
		GameObject zamorak = new GameObject(CastleWarsManager.zamorak.getCatapult().getId(),
				CastleWarsManager.zamorak.getCatapult().getPosition(), 10,
				CastleWarsManager.zamorak.getCatapult().getFace());
		CustomObjects.spawnGlobalObject(zamorak);
	}

	/**
	 * Handles catapult interaction
	 * 
	 * @param player the player
	 * @param id     the id
	 * @param x      the x
	 * @param y      the y
	 * @return the interaction
	 */
	public static boolean handleObject(Player player, int id, int x, int y) {
		return fire(player, id) || fixCatapult(player, id, x, y);
	}
}
