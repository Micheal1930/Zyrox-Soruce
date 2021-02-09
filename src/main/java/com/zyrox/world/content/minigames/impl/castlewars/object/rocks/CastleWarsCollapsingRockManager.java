package com.zyrox.world.content.minigames.impl.castlewars.object.rocks;

import java.util.Optional;

import com.zyrox.engine.task.Task;
import com.zyrox.engine.task.TaskManager;
import com.zyrox.model.Animation;
import com.zyrox.model.GameObject;
import com.zyrox.model.Hit;
import com.zyrox.model.Skill;
import com.zyrox.world.content.CustomObjects;
import com.zyrox.world.content.minigames.impl.castlewars.CastleWarsManager;
import com.zyrox.world.content.minigames.impl.castlewars.CastleWarsMinigame;
import com.zyrox.world.content.minigames.impl.castlewars.team.CastleWarsTeam;
import com.zyrox.world.entity.impl.player.Player;

/**
 * Handles collapsing the underground cave wall and rocks
 * 
 * @author 2012 <https://www.rune-server.ee/members/dexter+morgan/>
 */
public class CastleWarsCollapsingRockManager {

	/**
	 * The mining animation
	 */
	private static final Animation MINE = new Animation(6753);

	/**
	 * The cave wall
	 */
	public static final int CAVE_WALL = 4_448;

	/**
	 * The full rocks
	 */
	public static final int FULL_ROCKS = 4_437;

	/**
	 * The half rocks
	 */
	public static final int HALF_ROCKS = 4_438;

	/**
	 * Collapsing rocks
	 * 
	 * @param player  the player
	 * @param pickaxe whether using pickaxe
	 */
	public static void collapse(Player player, boolean pickaxe) {
		/*
		 * The minigame
		 */
		CastleWarsMinigame minigame = CastleWarsManager.MINIGAME;
		/*
		 * No minigame
		 */
		if (minigame == null) {
			return;
		}
		/*
		 * The rock
		 */
		CastleWarCollapsingRock rock = verify(player, pickaxe, true);
		/*
		 * Verify action
		 */
		if (rock == null) {
			return;
		}
		/*
		 * Already collapsed
		 */
		if (!minigame.getRocksCollapsed()[rock.ordinal()]) {
			return;
		}
		/*
		 * The rocks
		 */
		GameObject object = new GameObject(FULL_ROCKS, rock.getPosition());
		/*
		 * Set
		 */
		minigame.getRocksCollapsed()[rock.ordinal()] = false;
		/*
		 * Mining animation
		 */
		if (pickaxe) {
			player.performAnimation(MINE);
		}
		/*
		 * Delay
		 */
		TaskManager.submit(new Task(pickaxe ? 3 : 1) {

			@Override
			protected void execute() {
				/*
				 * Add clipping
				 */
				rock.getBoundary().addClipping();
				/*
				 * Show in world
				 */
				CustomObjects.spawnGlobalObject(object);
				player.getPacketSender().sendAnimationReset();
				stop();
			}
		});
		TaskManager.submit(new Task(pickaxe ? 4 : 2) {

			@Override
			protected void execute() {
				/*
				 * The local players
				 */
				for (Player p : player.getLocalPlayers()) {
					/*
					 * Invalid player
					 */
					if (p == null) {
						continue;
					}
					/*
					 * Player is inside
					 */
					if (rock.getBoundary().inside(p)) {
						p.getCombatBuilder().reset(true);
						p.getMovementQueue().reset();
						p.dealDamage(new Hit(p.getSkillManager().getCurrentLevel(Skill.CONSTITUTION)));
						p.getPacketSender().sendMessage("The collapsing rocks fall on you and instantly kill you!");
					}
				}
				/*
				 * Player
				 */
				if (rock.getBoundary().inside(player)) {
					player.getCombatBuilder().reset(true);
					player.getMovementQueue().reset();
					player.dealDamage(new Hit(player.getSkillManager().getCurrentLevel(Skill.CONSTITUTION)));
					player.getPacketSender().sendMessage("The collapsing rocks fall on you and instantly kill you!");
				}
				stop();
			}
		});
	}

	/**
	 * Breaking the rocks
	 * 
	 * @param player  the player
	 * @param id      the object id
	 * @param pickaxe wether using pickaxe
	 */
	public static void breakRock(Player player, int id, boolean pickaxe) {
		/*
		 * The minigame
		 */
		CastleWarsMinigame minigame = CastleWarsManager.MINIGAME;
		/*
		 * No minigame
		 */
		if (minigame == null) {
			return;
		}
		/*
		 * The rock
		 */
		CastleWarCollapsingRock rock = verify(player, pickaxe, false);
		/*
		 * Verify action
		 */
		if (rock == null) {
			return;
		}
		/*
		 * Already broken
		 */
		if (minigame.getRocksCollapsed()[rock.ordinal()]) {
			return;
		}
		/*
		 * The next id
		 */
		final int nextId = id == FULL_ROCKS ? HALF_ROCKS : -1;
		/*
		 * Mining animation
		 */
		if (pickaxe) {
			player.performAnimation(MINE);
		}
		/*
		 * Remove rock time
		 */
		TaskManager.submit(new Task(pickaxe ? 5 : 1) {

			@Override
			protected void execute() {
				/*
				 * The rocks
				 */
				GameObject object = new GameObject(nextId, rock.getPosition());
				/*
				 * Add clipping
				 */
				if (nextId == -1) {
					rock.getBoundary().removeClipping();
					minigame.getRocksCollapsed()[rock.ordinal()] = true;
				}
				/*
				 * Show in world
				 */
				CustomObjects.spawnGlobalObject(object);
				player.getPacketSender().sendAnimationReset();
				stop();
			}

		});
	}

	/**
	 * Verify to proceed
	 * 
	 * @param player   the player
	 * @param pickaxe  whether using pickaxe
	 * @param collapse whether to collapse
	 * @return verified action
	 */
	private static CastleWarCollapsingRock verify(Player player, boolean pickaxe, boolean collapse) {
		/*
		 * The team
		 */
		CastleWarsTeam team = CastleWarsManager.getTeam(player);
		/*
		 * No team
		 */
		if (team == null) {
			return null;
		}
		/*
		 * The rock
		 */
		Optional<CastleWarCollapsingRock> rock = CastleWarCollapsingRock.getWall(player);
		/*
		 * Not present
		 */
		if (!rock.isPresent()) {
			return null;
		}
		/*
		 * No pickaxe
		 */
		if (pickaxe) {
			if (!player.getEquipment().contains(CastleWarsManager.BRONZE_PICK_AXE)
					&& !player.getInventory().contains(CastleWarsManager.BRONZE_PICK_AXE)) {
				player.getPacketSender().sendMessage("You need a Mining Pickaxe which you need a Mining level to use.");
				return null;
			}
		}
		/*
		 * Not using pickaxe
		 */
		if (!pickaxe) {
			player.getInventory().delete(CastleWarsManager.EXPLOSIVE_POTION);
		}
		return rock.get();
	}

	/**
	 * preparing rocks for the game
	 */
	public static void prepare() {
		for (CastleWarCollapsingRock rock : CastleWarCollapsingRock.ROCKS) {
			GameObject object = new GameObject(FULL_ROCKS, rock.getPosition());
			rock.getBoundary().addClipping();
			CustomObjects.spawnGlobalObject(object);
		}
	}
}
