package com.varrock.world.content.alchemical_hydra;

import com.varrock.GameSettings;
import com.varrock.model.Position;
import com.varrock.world.World;
import com.varrock.world.content.instances.BossInstance;
import com.varrock.world.content.instances.InstanceManager;
import com.varrock.world.entity.impl.player.Player;

public class KaruulmSlayerHydra extends BossInstance {

	private Position BASE = new Position(1328, 10248, 0);

	private AlchemicalHydra hydra;
	
	public KaruulmSlayerHydra(Player player) {
		super(player);
	}
	
	@Override
	public void start() {
		BASE.setZ(plane);
		hydra = new AlchemicalHydra(8615 + GameSettings.OSRS_NPC_OFFSET, BASE.transform(36, 17, 0), player);
		hydra.setBase(BASE);
		hydra.findObjs();
		World.register(hydra);
		player.moveTo(BASE.transform(28, 10, 0), true);
	}
	

	@Override
	protected void execute() {
		if(player == null || !player.isActive() || !isWithinHydraLair(player.getPosition().getX(), player.getPosition().getY())) {
			World.deregister(hydra);
			InstanceManager.get().remove(this);
		}
	}

	/**
	 * @return the hydra
	 */
	public AlchemicalHydra getHydra() {
		return hydra;
	}

	public static boolean isWithinHydraLair(int x, int y) {
		return x >= 1356 && x <= 1378 && y >= 10255 && y <= 10278;
	}

}
