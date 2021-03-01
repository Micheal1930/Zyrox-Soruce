package com.zyrox.engine.task.impl;

import com.zyrox.engine.task.Task;
import com.zyrox.model.Position;
import com.zyrox.model.RegionInstance.RegionInstanceType;
import com.zyrox.util.RandomUtility;
import com.zyrox.world.World;
import com.zyrox.world.content.Dracula;
import com.zyrox.world.content.Wildywyrm;
import com.zyrox.world.content.skill.impl.hunter.Hunter;
import com.zyrox.world.entity.impl.npc.NPC;
import com.zyrox.world.entity.impl.npc.NPCMovementCoordinator.Coordinator;
import com.zyrox.world.entity.impl.npc.impl.Tekton;
import com.zyrox.world.entity.impl.npc.impl.Warmonger;
import com.zyrox.world.entity.impl.player.Player;

public class NPCRespawnTask extends Task {

	public NPCRespawnTask(NPC npc, int respawn, Player player) {
		super(respawn);
		this.npc = npc;
		this.player = player;
	}

	final NPC npc;
	final Player player;

	@Override
	public void execute() {
		if (npc.getDefinition().getId() == Wildywyrm.NPC_ID || npc.getDefinition().getId() == Warmonger.NPC_ID || npc.getDefinition().getId() == Tekton.NPC_ID || npc.getDefinition().getId() == Dracula.NPC_ID) {
			stop();
			return;
		}
		NPC npc_ = NPC.of(npc.getId(), npc.getDefaultPosition());
		npc_.getMovementCoordinator().setCoordinator(npc.getMovementCoordinator().getCoordinator());

		if (npc_.getId() == 8022 || npc_.getId() == 8028) { // Desospan, respawn at random locations
			npc_.moveTo(new Position(2595 + RandomUtility.getRandom(12), 4772 + RandomUtility.getRandom(8)));
		} else if (npc_.getId() > 5070 && npc_.getId() < 5081) {
			Hunter.HUNTER_NPC_LIST.add(npc_);
		}
		
		if(player != null) {
			if(player.getRegionInstance() != null && player.getRegionInstance().getType() == RegionInstanceType.BOSS_INSTANCE) {
				npc_.setSpawnedFor(player);
				player.getRegionInstance().getNpcsList().add(npc_);
				npc_.setInstancedPlayer(player);
				npc_.getMovementCoordinator().setCoordinator(new Coordinator(false, -1));
				npc_.getMovementQueue().setLockMovement(true);
				npc_.getDefinition().setAggressive(true);
				player.getRegionInstance().getNpcsList().add(npc_);
			}
		}

		World.register(npc_);
		stop();
	}

}
