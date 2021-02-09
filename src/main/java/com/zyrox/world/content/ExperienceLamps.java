package com.zyrox.world.content;

import com.zyrox.model.Skill;
import com.zyrox.util.Misc;
import com.zyrox.world.content.skill.SkillManager;
import com.zyrox.world.entity.impl.player.Player;

public class ExperienceLamps {

	public static boolean handleLamp(Player player, int item) {
		LampData lamp = LampData.forId(item);
		if(lamp == null)
			return false;
		if(player.getInterfaceId() > 0) {
			player.getPacketSender().sendMessage("Please close the interface you have open before doing this.");
		} else {
			player.getPacketSender().sendString(38006, "Choose XP type...").sendString(38090, "What sort of XP would you like?");
			player.getPacketSender().sendInterface(38000);
			player.setUsableObject(new Object[3]).setUsableObject(0, "xp").setUsableObject(2, lamp);
		}
		return true;
	}

	public static boolean handleButton(Player player, int button) {
		if(button == 38085) {
			try {
				player.getPacketSender().sendInterfaceRemoval();
				player.getPacketSender().sendString(38006, "Choose XP type...");
				if(player.getUsableObject()[0] != null) {
					Skill skill = (Skill) player.getUsableObject()[1];
					switch(((String)player.getUsableObject()[0]).toLowerCase()){
					case "reset":
						player.getSkillManager().resetSkill(skill, false);
						break;
					case "xp":
						LampData lamp = (LampData) player.getUsableObject()[2];
						if(!player.getInventory().contains(lamp.getItemId()))
							return true;
						int exp = getExperienceReward(player, lamp, skill);
						player.getInventory().delete(lamp.getItemId(), 1);
						player.getSkillManager().addExperience(skill, exp);
						player.getPacketSender().sendMessage("You've received some experience in "+Misc.formatText(skill.toString().toLowerCase())+".");
						break;
					}
				}
			} catch(Exception e) {}
			return true;
		} else {
			Interface_Buttons interfaceButton = Interface_Buttons.forButton(button);
			if(interfaceButton == null)
				return false;
			
			Skill skill = Skill.forName(interfaceButton.toString());
			if(skill == null)
				return true;
			player.setUsableObject(1, skill);
			player.getPacketSender().sendString(38006, Misc.formatText(interfaceButton.toString().toLowerCase()));
		}
		return false;
	}

	enum LampData {
		NORMAL_XP_LAMP(11137),
		DRAGONKIN_LAMP(18782),
		DREAMY_LAMP(41157), // doesn't work yet
		CHAMPION_LAMP(52320); // doesn't work yet

		LampData(int itemId) {
			this.itemId = itemId;
		}

		private int itemId;

		public int getItemId() {
			return this.itemId;
		}

		public static LampData forId(int id) {
			for(LampData lampData : LampData.values()) {
				if(lampData != null && lampData.getItemId() == id)
					return lampData;
			}
			return null;
		}
	}

	enum Interface_Buttons {

		ATTACK(38007),
		MAGIC(38010),
		MINING(38013),
		WOODCUTTING(38016),
		AGILITY(38019),
		FLETCHING(38022),
		THIEVING(38025),
		STRENGTH(38028),
		RANGED(38031),
		SMITHING(38034),
		FIREMAKING(38037),
		HERBLORE(38040),
		SLAYER(38043),
		CONSTRUCTION(38046),
		DEFENCE(38049),
		PRAYER(38052),
		FISHING(38055),
		CRAFTING(38058),
		FARMING(38061),
		HUNTER(38064),
		SUMMONING(38067),
		CONSTITUTION(38070),
		DUNGEONEERING(38073),
		COOKING(38076),
		RUNECRAFTING(38079);

		Interface_Buttons(int button) {
			this.button = button;
		}

		private int button;

		public static Interface_Buttons forButton(int button) {
			for(Interface_Buttons skill : Interface_Buttons.values()) {
				if(skill != null && skill.button == button) {
					return skill;
				}
			}
			return null;
		}
	}

	public static int getExperienceReward(Player player, LampData lamp, Skill skill) {
		int base = lamp == LampData.DRAGONKIN_LAMP ? 50_000 : 2000;
		int maxLvl = player.getSkillManager().getMaxLevel(skill);
		if(SkillManager.isNewSkill(skill))
			maxLvl = maxLvl / 10;
		return (int) (base+10*(Math.pow(maxLvl, 2.5)));
	}

	public static boolean selectingExperienceReward(Player player) {
		return player.getInterfaceId() == 38000;
	}
}
