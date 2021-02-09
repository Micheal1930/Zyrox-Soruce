package com.zyrox.net.packet.command.impl;

import java.util.Arrays;

import com.zyrox.GameSettings;
import com.zyrox.model.Skill;
import com.zyrox.net.packet.command.CommandHeader;
import com.zyrox.net.packet.command.NameCommand;
import com.zyrox.world.World;
import com.zyrox.world.content.skill.SkillManager;
import com.zyrox.world.entity.impl.player.Player;

@CommandHeader(command = { "setlevel" }, description = "Sets the level of a player.")
public class SetLevelCommand extends NameCommand {
	
	public SetLevelCommand() {
		super(GameSettings.SPECIAL_STAFF_NAMES);
	}

	@Override
	public boolean execute(Player player, String[] args) throws Exception {
		if (args.length < 2) {
			throw new IllegalArgumentException();
		}
		
		String username = args.length == 2 ? player.getUsername() : String.join(" ", Arrays.copyOfRange(args, 2, args.length));
		
		Player p = World.getPlayerByName(username);
		
		if (p == null) {
			player.sendMessage("That player doesn't seem to be online!");
			return false;
		}
		
		int level = Integer.parseInt(args[1]);
		
		for (Skill skill : Skill.values()) {
			if (skill.getName().toLowerCase().startsWith(args[0].toLowerCase())) {
				int max = skill.equals(Skill.DUNGEONEERING) ? 120 : 9999;
				
				int min = skill.equals(Skill.CONSTITUTION) ? 10 : 1;
				
				if (level > max) {
					level = max;
				}
				
				if (level < min) {
					level = min;
				}
				
				if (skill.equals(Skill.CONSTITUTION)) {
					level *= 10;
				}
				
				p.getSkillManager().setSkill(skill.ordinal(), level, SkillManager.getExperienceForLevel(skill, level));
				p.getSkillManager().setMaxLevel(skill, level);
				player.sendMessage("You have successfully set @dre@" + p.getUsername() + "</col>'s @dre@" + skill.getName() + " </col>to @dre@" + level + " </col>!");
				return true;
			}
		}

		return false;
	}

	@Override
	public String[] getUsage() {
		return new String[] {
			"::setlevel [skill] [level]",
			"::setlevel [skill] [level] [player_name]"
		};
	}

}
