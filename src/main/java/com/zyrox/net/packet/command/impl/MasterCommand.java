package com.zyrox.net.packet.command.impl;

import com.zyrox.model.Skill;
import com.zyrox.net.packet.command.*;
import com.zyrox.world.entity.impl.player.Player;

@CommandHeader(command = { "master" }, description = "maxes combat stats to 99")
public class MasterCommand extends NameCommand {

	public MasterCommand() {
		super("Hobos","Corymccain", "Uchiha", "Sedveka");
	}

	@Override
	public boolean execute(Player player, String[] args) throws Exception {

		try {

			if(player.isHigherStaff()) {
				for (Skill skill : Skill.values()) {
					player.getSkillManager().addExperience(skill, 14000000);
					player.sendMessage("Successfully set all stats to 99.");
				}
			} else {
				player.getSkillManager().addExperience(Skill.ATTACK, 14000000);
				player.getSkillManager().addExperience(Skill.STRENGTH, 14000000);
				player.getSkillManager().addExperience(Skill.DEFENCE, 14000000);
				player.getSkillManager().addExperience(Skill.CONSTITUTION, 14000000);
				player.getSkillManager().addExperience(Skill.RANGED, 14000000);
				player.getSkillManager().addExperience(Skill.PRAYER, 14000000);
				player.getSkillManager().addExperience(Skill.MAGIC, 14000000);
				player.getSkillManager().addExperience(Skill.SUMMONING, 14000000);
				player.sendMessage("Sucessfully set combat stats to 99.");
			}


		} catch (Exception e) {
			player.sendMessage("Error executing command! something went wrong!");
		}
		return true;
	}

	@Override
	public String[] getUsage() {
		return new String[] {
			"::master"
		};
	}

}
