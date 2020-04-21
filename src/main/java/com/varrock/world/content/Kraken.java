package com.varrock.world.content;

import com.varrock.engine.task.Task;
import com.varrock.engine.task.TaskManager;
import com.varrock.model.Locations;
import com.varrock.model.Position;
import com.varrock.world.World;
import com.varrock.world.content.transportation.TeleportHandler;
import com.varrock.world.content.transportation.TeleportType;
import com.varrock.world.entity.impl.npc.NPC;
import com.varrock.world.entity.impl.player.Player;

/**
 * @author Jonathan Sirens
 * Class created to handle all Kraken instancing
 */

public class Kraken {

	/**
	 * Tells you what stage you are on in Kraken
	 */
	public enum KrakenStage {
		DEFAULT(),
		TELEPORTED_IN(),
		DISTURBED_POOLS(),
		SPAWNED_BOSS(),
		DEFEATED();

		KrakenStage() {

		}
	}

	private int pools = 0;
	private NPC[] whirlpool = new NPC[5];
	private NPC[] tentacle = new NPC[4];
	public NPC kraken = null;
	public KrakenStage krakenStage = KrakenStage.DEFAULT;

	/**
	 * Obtain what kraken stage you are currently on.
	 * @return
	 */
	public KrakenStage getKrakenStage() {
		return this.krakenStage;
	}

	/**
	 * Sets your new kraken stage.
	 * Used to change the instance.
	 * @param stage
	 */
	public void setKrakenStage(KrakenStage stage) {
		this.krakenStage = stage;
	}

	/**
	 * Tells you how many pools you have disturbed
	 * @return pools
	 */
	public int getPoolsDisturbed() {
		return this.pools;
	}

	/**
	 * Add a pool onto the total value of pools
	 * you have disturbed.
	 */
	public void incrementPools(Player player, NPC npc) {
		int transformationId = getTransformationId(npc);
		if(tentacle[transformationId - 1] != null && npc.getId() != 149) {
			return;
		}
		this.pools++;
		if(pools == 4) {
			this.setKrakenStage(KrakenStage.DISTURBED_POOLS);
		} else if(pools == 5) {
			this.setKrakenStage(KrakenStage.SPAWNED_BOSS);
			this.spawnKraken(player, npc);
		}
		if(pools <= 4) {
			this.transformPool(player, npc);
		}
	}

	/**
	 * Obtain a whirlpool npc instance.
	 * @param index
	 * @return
	 */
	public NPC getWhirlpool(int index) {
		return this.whirlpool[index];
	}

	/**
	 * Obtain a tentacle npc instance.
	 * @param index
	 * @return
	 */
	public NPC getTentacle(int index) {
		return this.tentacle[index];
	}

	/**
	 * Obtain the kraken instance.
	 * @return
	 */
	public NPC getKraken() {
		return this.kraken;
	}

	/**
	 * Teleports you to Kraken
	 * Initiates all spawns within entering Kraken
	 * @param player
	 */
	public void enter(Player player, boolean teleport) {
		boolean falseEntry = false;
		if(teleport) {
			if(player.getLocation() == Locations.Location.KRAKEN && player.getKraken().getKrakenStage() != KrakenStage.DEFAULT) {
				falseEntry = true;
			}
			TeleportHandler.teleportPlayer(player, new Position(3696, 5807, player.getIndex() * 4), player.getSpellbook().getTeleportType());
		}
		if(falseEntry) {
			return;
		}
		player.getKraken().setKrakenStage(KrakenStage.TELEPORTED_IN);
			TaskManager.submit(new Task(1, player, false) {
			int tick = 0;
			@Override
			public void execute() {
				int spawnTick = 3;
				if(player.getSpellbook().getTeleportType() == TeleportType.ANCIENT) {
					spawnTick = 4;
				}
				if(tick == spawnTick) {
					whirlpool[0] = NPC.of(149, new Position(3695, 5811, player.getPosition().getZ())).setSpawnedFor(player); //Boss
					World.register(whirlpool[0]);
					whirlpool[1] = NPC.of(150, new Position(3700, 5809, player.getPosition().getZ())).setSpawnedFor(player);
					World.register(whirlpool[1]);
					whirlpool[2] = NPC.of(150, new Position(3700, 5814, player.getPosition().getZ())).setSpawnedFor(player);
					World.register(whirlpool[2]);
					whirlpool[3] = NPC.of(150, new Position(3692, 5809, player.getPosition().getZ())).setSpawnedFor(player);
					World.register(whirlpool[3]);
					whirlpool[4] = NPC.of(150, new Position(3692, 5814, player.getPosition().getZ())).setSpawnedFor(player);
					World.register(whirlpool[4]);
					stop();
				}
				tick++;
			}
		});
	}

	/**
	 * Transforms your pool into a tentacle.
	 * @param player
	 * @param npc
	 */
	public void transformPool(Player player, NPC npc) {
		int transformationId = getTransformationId(npc);
		if(tentacle[transformationId - 1] != null) {
			return;
		}
		tentacle[transformationId - 1] = NPC.of(148, npc.getPosition()).setSpawnedFor(player);
		whirlpool[transformationId] = null;

		World.register(tentacle[transformationId - 1]);
		tentacle[transformationId - 1].getCombatBuilder().attack(player);
		World.deregister(npc);
	}

	/**
	 * This gets the ID for the tentacle you are transforming to.
	 * @param npc
	 * @return
	 */
	public int getTransformationId(NPC npc) {
		int transform = 1;
		if(matchCoordinates(npc, whirlpool[2])) {
			transform = 2;
		} else if(matchCoordinates(npc, whirlpool[3])) {
			transform = 3;
		} else if(matchCoordinates(npc, whirlpool[4])) {
			transform = 4;
		}
		return transform;
	}

	/**
	 * Check if 2 npcs have the same coordinates.
	 * @param npc
	 * @param npc
	 * @return
	 */
	public boolean matchCoordinates(NPC npc, NPC npc2) {
		if(npc2 == null || npc == null) {
			return false;
		}
		if(npc.getPosition().getX() == npc2.getPosition().getX()) {
			if(npc.getPosition().getY() == npc2.getPosition().getY()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Transforms the biggest whirlpool into
	 * a huge Kraken boss.
	 * @param player
	 * @param npc
	 */
	public void spawnKraken(Player player, NPC npc) {
		kraken = NPC.of(3847, npc.getPosition()).setSpawnedFor(player);
		World.register(kraken);
		kraken.getCombatBuilder().attack(player);
		World.deregister(npc);
	}

	/**
	 * Causes instance to reset
	 * @Exiting
	 */
	public void reset() {
		deregisterPools();
		deregisterTentacles();
		if(kraken != null) {
			World.deregister(kraken);
		}
	}

	/**
	 * Remove all tentacles from instance.
	 */
	public void deregisterTentacles() {
		for(NPC tentacles: tentacle) {
			if(tentacles == null) {
				continue;
			}
			tentacles.appendDeath();
		}
	}

	/**
	 * Resets the combat of the tentacles
	 * Used after the death of Kraken.
	 */
	public void stopTentacleAttack() {
		for(NPC tentacles: tentacle) {
			if(tentacles == null) {
				continue;
			}
			tentacles.getCombatBuilder().reset(true);
		}
	}

	/**
	 * Remove all whirlpools from instance.
	 */
	public void deregisterPools() {
		for(NPC pools: whirlpool) {
			if(pools == null) {
				continue;
			}
			World.deregister(pools);
		}
	}

	public Kraken() {
		this.pools = 0;
		this.whirlpool = new NPC[5];
		this.tentacle = new NPC[4];
		this.kraken = null;
		this.krakenStage = KrakenStage.DEFAULT;
	}
}