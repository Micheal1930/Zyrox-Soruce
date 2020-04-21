package com.varrock.world.content.skill.impl.agility;

import com.varrock.model.GameObject;
import com.varrock.model.Skill;
import com.varrock.model.container.impl.Equipment;
import com.varrock.util.Misc;
import com.varrock.world.World;
import com.varrock.world.content.Achievements;
import com.varrock.world.content.Achievements.AchievementData;
import com.varrock.world.entity.impl.player.Player;

public class Agility {

	public static boolean handleObject(Player p, GameObject object) {
		if(object.getId() == 2309) {
			if(p.getSkillManager().getMaxLevel(Skill.AGILITY) < 55) {
				p.getPacketSender().sendMessage("You need an Agility level of at least 55 to enter this course.");
				return true;
			}
		}
		ObstacleData agilityObject = ObstacleData.forId(object.getId());
		if(agilityObject != null) {
			if(p.isCrossingObstacle())
				return true;
			p.setPositionToFace(object.getPosition());
			p.setResetPosition(p.getPosition());
			p.setCrossingObstacle(true);
			//boolean wasRunning = p.getAttributes().isRunning();
			//if(agilityObject.mustWalk()) {
				//p.getAttributes().setRunning(false);
			//	p.getPacketSender().sendRunStatus();
			//}
			if(Misc.getRandom(7000) == 3) {
				p.getInventory().add(13325, 1, "Agility");
				World.sendMessage("<col=6666FF> <img=10> [ Skilling Pets ]:</col> "+p.getUsername()+" has received the Giant Squirrel pet!");
				p.getPacketSender().sendMessage("@red@You have received a skilling pet!");
				p.getPacketSender().sendMessage("@red@Your account has been saved!");
				p.save();
			}
			agilityObject.cross(p);
			Achievements.finishAchievement(p, AchievementData.CLIMB_AN_AGILITY_OBSTACLE);
			Achievements.doProgress(p, AchievementData.CLIMB_50_AGILITY_OBSTACLES);
		}
		return false;
	}

	public static boolean passedAllObstacles(Player player) {
		for(boolean crossedObstacle : player.getCrossedObstacles()) {
			if(!crossedObstacle)
				return false;
		}
		return true;
	}

	public static void resetProgress(Player player) {
		for(int i = 0; i < player.getCrossedObstacles().length; i++)
			player.setCrossedObstacle(i, false);
	}
	
	public static boolean isSucessive(Player player) {
		return Misc.getRandom(player.getSkillManager().getCurrentLevel(Skill.AGILITY) / 2) > 1;
	}
	
	public static void addExperience(Player player, double experience) {
		player.getSkillManager().addExperience(Skill.AGILITY, experience * Skill.AGILITY.getModifier());
	}
}
