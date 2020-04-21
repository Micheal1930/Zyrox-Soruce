package com.varrock.world.content.skill.impl.mining;

import com.varrock.engine.task.Task;
import com.varrock.engine.task.TaskManager;
import com.varrock.world.content.skill.impl.mining.MiningData.Ores;
import com.varrock.world.entity.impl.player.Player;

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
