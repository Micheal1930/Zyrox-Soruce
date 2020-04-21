package com.varrock.world.entity;

import com.varrock.engine.task.TaskManager;
import com.varrock.model.GameObject;
import com.varrock.net.PlayerSession;
import com.varrock.net.SessionState;
import com.varrock.world.World;
import com.varrock.world.clip.region.RegionClipping;
import com.varrock.world.content.CustomObjects;
import com.varrock.world.entity.impl.npc.NPC;
import com.varrock.world.entity.impl.player.Player;

public class EntityHandler {

	public static void register(Entity entity) {
		if (entity.isPlayer()) {
			Player player = (Player) entity;
			PlayerSession session = player.getSession();
			player.setAttribute("skip_update_tick", true);
			if (session.getState() == SessionState.LOGGING_IN && !World.getLoginQueue().contains(player)) {
				World.getLoginQueue().add(player);
			}
			player.setAttribute("skip_update_tick", true);
		}
		if (entity.isNpc()) {
			NPC npc = (NPC) entity;
			World.getNpcs().add(npc);
		} else if (entity.isGameObject()) {
			GameObject gameObject = (GameObject) entity;
			RegionClipping.addObject(gameObject);
			CustomObjects.spawnGlobalObjectWithinDistance(gameObject);
		}
	}

	public static void deregister(Entity entity) {
		if (entity.isPlayer()) {
			Player player = (Player) entity;
			World.getPlayers().remove(player);
		} else if (entity.isNpc()) {
			NPC npc = (NPC) entity;
			TaskManager.cancelTasks(npc.getCombatBuilder());
			TaskManager.cancelTasks(entity);
			World.getNpcs().remove(npc);
		} else if (entity.isGameObject()) {
			GameObject gameObject = (GameObject) entity;
			RegionClipping.removeObject(gameObject);
			CustomObjects.deleteGlobalObjectWithinDistance(gameObject);
		}
	}
}
