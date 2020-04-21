package com.varrock.saving;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

import com.varrock.world.content.skill.SkillManager;
import com.varrock.world.entity.impl.player.Player;


public class SaveSkills extends SaveObject {

	public static final int DEFAULT_EXP = 0;

	public SaveSkills(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean save(Player player, BufferedWriter writer)
			throws IOException {
		writer.newLine();
		writer.write(getName());
		writer.newLine();
		for (int i = 0; i < SkillManager.MAX_SKILLS; i++) {
			int exp = player.getSkillManager().getExperience(i);
			if (exp != DEFAULT_EXP) {
				int lvl = player.getSkillManager().getLevel(i);
				writer.write(i + " " + lvl + " " + exp + " " + player.getSkillManager().getMaxLevel(i));
				writer.newLine();
			}
		}
		return true;
	}

	@Override
	public void load(Player player, String values, BufferedReader reader)
			throws IOException {
		String line;
		while ((line = reader.readLine()).length() > 0) {
			String[] parts = line.split(" ");
			int skill = Integer.parseInt(parts[0]);
			int level = Integer.parseInt(parts[1]);
			int exp = Integer.parseInt(parts[2]);
			//level = level > 200 ? 200 : level;
			player.getSkillManager().setSkill(skill, level, exp);
			if(parts.length >= 4) {
				int maxLevel = Integer.parseInt(parts[3]);
				if(maxLevel > 1200)
					maxLevel = 1200;
				player.getSkillManager().setMaxLevel(skill, maxLevel);
				//player.getSkillManager().setMaxLevel(Skill.getDefinition(skill), maxLevel);
			}
		}
		//player.getPA().refreshSkills();
	}

}
