package com.varrock.engine.task.impl;

import com.varrock.engine.task.Task;
import com.varrock.model.Position;
import com.varrock.util.RandomUtility;
import com.varrock.world.World;
import com.varrock.world.content.Dracula;
import com.varrock.world.content.Wildywyrm;
import com.varrock.world.content.skill.impl.hunter.Hunter;
import com.varrock.world.entity.impl.npc.NPC;
import com.varrock.world.entity.impl.npc.impl.Tekton;
import com.varrock.world.entity.impl.npc.impl.Warmonger;

public class NPCRespawnTask extends Task {

	public NPCRespawnTask(NPC npc, int respawn) {
		super(respawn);
		this.npc = npc;
	}

	final NPC npc;

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

		World.register(npc_);
		stop();
	}

}
