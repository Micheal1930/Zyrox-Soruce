package com.zyrox.world.content.skill.impl.mining;

import com.zyrox.engine.task.Task;
import com.zyrox.engine.task.TaskManager;
import com.zyrox.world.content.skill.impl.mining.MiningData.Ores;
import com.zyrox.world.entity.impl.player.Player;

public class Prospecting {

	public static boolean prospectOre(final Player plr, Ores oreData) {
		if(oreData != null) {
			if(!plr.getClickDelay().elapsed(2800))
				return true;
			plr.getSkillManager().stopSkilling();
			plr.getPacketSender().sendMessage("You examine the ore...");
			TaskManager.submit(new Task(2, plr, false) {
				@Override
				public void execute() {
					plr.getPacketSender().sendMessage("..the rock contains "+oreData.getName()+" ore.");
					this.stop();
				}
			});
			plr.getClickDelay().reset();
			return true;
		}
		return false;
	}
}
