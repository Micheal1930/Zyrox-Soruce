package com.zyrox.world.content.skill;

import com.zyrox.GameSettings;
import com.zyrox.engine.task.Task;
import com.zyrox.engine.task.TaskManager;
import com.zyrox.model.*;
import com.zyrox.model.Locations.Location;
import com.zyrox.model.container.impl.Equipment;
import com.zyrox.model.definitions.ItemDefinition;
import com.zyrox.model.definitions.WeaponAnimations;
import com.zyrox.model.definitions.WeaponInterfaces;
import com.zyrox.util.Misc;
import com.zyrox.world.World;
import com.zyrox.world.content.Achievements;
import com.zyrox.world.content.BonusManager;
import com.zyrox.world.content.BrawlingGloves;
import com.zyrox.world.content.Sounds;
import com.zyrox.world.content.Achievements.AchievementData;
import com.zyrox.world.content.Sounds.Sound;
import com.zyrox.world.content.combat.prayer.CurseHandler;
import com.zyrox.world.content.combat.prayer.PrayerHandler;
import com.zyrox.world.content.skill.impl.summoning.BossPets;
import com.zyrox.world.content.well_of_goodwill.WellBenefit;
import com.zyrox.world.entity.impl.player.Player;

/**
 * Represents a player's skills in the game, also manages
 * calculations such as combat level and total level.
 * 
 * @author relex lawl
 * @editor Gabbe
 */

public class SkillManager {

	/**
	 * The skillmanager's constructor
	 * @param player	The player's who skill set is being represented.
	 */
	public SkillManager(Player player) {
		this.player = player;
		newSkillManager();
	}

	/**
	 * Creates a new skillmanager for the player
	 * Sets current and max appropriate levels.
	 */
	public void newSkillManager() {
		this.skills = new Skills();
		for (int i = 0; i < MAX_SKILLS; i++) {
			skills.level[i] = skills.maxLevel[i] = 1;
			skills.experience[i] = 0;
		}
		skills.level[Skill.CONSTITUTION.ordinal()] = skills.maxLevel[Skill.CONSTITUTION.ordinal()] = 100;
		skills.experience[Skill.CONSTITUTION.ordinal()] = 1184;
		skills.level[Skill.PRAYER.ordinal()] = skills.maxLevel[Skill.PRAYER.ordinal()] = 10;
	}
	
	
	/*if (skills.maxLevel[skill.ordinal()] == getMaxAchievingLevel(skill)) {
			for(int i = 0; i < Skill.values().length; i++) {
				if(i == 21)
					continue;
				if(player.getSkillManager().getMaxLevel(i) < (i == 3 || i == 5 ? 990 : 99)) {
					return true;
				}
			World.sendMessage("Testing");
				}
			}
	return false;
	}*/
	
	/**
	 * Checks if the specified skill is a combat skill.
	 * 
	 * @param skill
	 *            the skill
	 * @return <code>true</code> if the skill is a combat skill
	 */
	public boolean isCombatSkill(Skill skill) {
		return skill.equals(Skill.ATTACK) || skill.equals(Skill.DEFENCE) || skill.equals(Skill.STRENGTH) || skill.equals(Skill.CONSTITUTION) || skill.equals(Skill.RANGED) || skill.equals(Skill.PRAYER) || skill.equals(Skill.MAGIC);
	}
	
	/**
	 * Adds experience to {@code skill} by the {@code experience} amount.
	 * @param skill			The skill to add experience to.
	 * @param experience	The amount of experience to add to the skill.
	 * @return				The Skills instance.
	 */
	public SkillManager addExperience(Skill skill, double experience) {
		
		if(player.experienceLocked())
			return this;

		/*
		 * If the experience in the skill is already greater or equal to
		 * {@code MAX_EXPERIENCE} then stop.
		 */
		if (this.skills.experience[skill.ordinal()] >= MAX_EXPERIENCE)
			return this;

		double expBoost = 1.0;

		if (player.getXPMode() == XPMode.CLASSIC) {
			expBoost = 0.5;
		}

		if(World.getWell().isActive(WellBenefit.BONUS_XP))
			expBoost += .3;
		
		if (GameSettings.isBonusXp()) {
			expBoost += .15;
		}

		if(player.getLocation() == Location.DONATOR_ZONE) {
			expBoost += player.getRights().getExperienceGainModifier(player);
		}
		
		switch (skill) {
		case AGILITY:
			if (player.getEquipment().get(Equipment.CAPE_SLOT).getId() == 13356) {
				expBoost += .15;
			}
			if (player.getEquipment().containsAll(14936, 14938)) {
				expBoost += .05;
			}
			if(player.hasFamiliar(BossPets.BossPet.SQUIRREL.getNpcID())) {
				expBoost += .10;
			}
			break;
		case ATTACK:
			if (player.getEquipment().get(Equipment.CAPE_SLOT).getId() == 13657) {
				expBoost += .15;
			}
			break;
		case CONSTITUTION:
			if (player.getEquipment().get(Equipment.CAPE_SLOT).getId() == 13668) {
				expBoost += .15;
			}
			break;
		case CONSTRUCTION:
			if (player.getEquipment().get(Equipment.CAPE_SLOT).getId() == 13658) {
				expBoost += .15;
			}
			break;
		case COOKING:
			if (player.getEquipment().get(Equipment.CAPE_SLOT).getId() == 13671) {
				expBoost += .15;
			}
			break;
		case CRAFTING:
			if (player.getEquipment().get(Equipment.CAPE_SLOT).getId() == 13360) {
				expBoost += .15;
			}
			break;
		case DEFENCE:
			if (player.getEquipment().get(Equipment.CAPE_SLOT).getId() == 13361) {
				expBoost += .15;
			}
			break;
		case DUNGEONEERING:
			if (player.getEquipment().get(Equipment.CAPE_SLOT).getId() == 19709) {
				expBoost += .15;
			}
			break;
		case FARMING:
			if (player.getEquipment().get(Equipment.CAPE_SLOT).getId() == 13362) {
				expBoost += .15;
			}
			if(player.hasFamiliar(BossPets.BossPet.TANGLEROOT.getNpcID())) {
				expBoost += .10;
			}
			break;
		case FIREMAKING:
			if (player.getEquipment().get(Equipment.CAPE_SLOT).getId() == 13664) {
				expBoost += .15;
			}
			break;
		case FISHING:
			if (player.getEquipment().get(Equipment.CAPE_SLOT).getId() == 13665) {
				expBoost += .15;
			}
			if(player.hasFamiliar(BossPets.BossPet.HERON.getNpcID())) {
				expBoost += .10;
			}
			break;
		case FLETCHING:
			if (player.getEquipment().get(Equipment.CAPE_SLOT).getId() == 13666) {
				expBoost += .15;
			}
			break;
		case HERBLORE:
			if (player.getEquipment().get(Equipment.CAPE_SLOT).getId() == 13667) {
				expBoost += .15;
			}
			break;
		case HUNTER:
			if (player.getEquipment().get(Equipment.CAPE_SLOT).getId() == 13669) {
				expBoost += .15;
			}
			break;
		case MAGIC:
			if (player.getEquipment().get(Equipment.CAPE_SLOT).getId() == 13670) {
				expBoost += .15;
			}
			break;
		case MINING:
			if (player.getEquipment().get(Equipment.CAPE_SLOT).getId() == 13671) {
				expBoost += 1.15;
			}
			if(player.hasFamiliar(BossPets.BossPet.ROCK_GOLEM.getNpcID())) {
				expBoost += .10;
			}
			break;
		case PRAYER:
			if (player.getEquipment().get(Equipment.CAPE_SLOT).getId() == 13672) {
				expBoost += .15;
			}
			break;
		case RANGED:
			if (player.getEquipment().get(Equipment.CAPE_SLOT).getId() == 13673) {
				expBoost += .15;
			}
			break;
		case RUNECRAFTING:
			if (player.getEquipment().get(Equipment.CAPE_SLOT).getId() == 13676) {
				expBoost += .15;
			}
			if (player.getEquipment().containsAll(13626, 13624, 13627, 13628)) {
				expBoost += .05;
			}
			if(player.hasFamiliar(BossPets.BossPet.RIFT_GUARDIAN.getNpcID())) {
				expBoost += .10;
			}
			break;
		case SLAYER:
			if (player.getEquipment().get(Equipment.CAPE_SLOT).getId() == 13675) {
				expBoost += .15;
			}
			break;
		case SMITHING:
			if (player.getEquipment().get(Equipment.CAPE_SLOT).getId() == 13676) {
				expBoost += .15;
			}
			break;
		case STRENGTH:
			if (player.getEquipment().get(Equipment.CAPE_SLOT).getId() == 13690) {
				expBoost += .15;
			}
			break;
		case SUMMONING:
			if (player.getEquipment().get(Equipment.CAPE_SLOT).getId() == 13678) {
				expBoost += .15;
			}
			break;
		case THIEVING:
			if (player.getEquipment().get(Equipment.CAPE_SLOT).getId() == 13679) {
				expBoost += .15;
			}
			if(player.hasFamiliar(BossPets.BossPet.ROCKY.getNpcID())) {
				expBoost += .10;
			}
			break;
		case WOODCUTTING:
			if (player.getEquipment().get(Equipment.CAPE_SLOT).getId() == 13680) {
				expBoost += .15;
			}

			//lumberjack
			if(player.getEquipment().contains(10941)) {
				expBoost += .03;
			}
			if(player.getEquipment().contains(10939)) {
				expBoost += .03;
			}
			if(player.getEquipment().contains(10940)) {
				expBoost += .03;
			}
			if(player.getEquipment().contains(10933)) {
				expBoost += .03;
			}

			if(player.hasFamiliar(BossPets.BossPet.BEAVER.getNpcID())) {
				expBoost += .10;
			}
			break;
		}
		
		/**
		 * Symbol of Artisans.
		 */
		if (player.getEquipment().get(Equipment.SHIELD_SLOT).getId() == 15459
				&& (skill.equals(Skill.COOKING) || skill.equals(Skill.SMITHING) || skill.equals(Skill.CRAFTING) || skill.equals(Skill.CONSTRUCTION) || skill.equals(Skill.FLETCHING)
						|| skill.equals(Skill.HERBLORE) || skill.equals(Skill.FARMING) || skill.equals(Skill.COOKING) || skill.equals(Skill.RUNECRAFTING) || skill.equals(Skill.FIREMAKING))) {
			expBoost += .20;
		}

		/**
		 * Symbol of Life
		 */
		if (player.getEquipment().get(Equipment.SHIELD_SLOT).getId() == 15464
				&& (skill.equals(Skill.AGILITY) || skill.equals(Skill.DUNGEONEERING) || skill.equals(Skill.SUMMONING) || skill.equals(Skill.SLAYER))) {
			expBoost += .20;
		}

		/**
		 * Symbol of Combat.
		 */
		if (player.getEquipment().get(Equipment.SHIELD_SLOT).getId() == 15449 && isCombatSkill(skill)) {
			expBoost += .20;
		}

		/**
		 * Symbol of Collectors.
		 */
		if (player.getEquipment().get(Equipment.SHIELD_SLOT).getId() == 15454
				&& (skill.equals(Skill.FISHING) || skill.equals(Skill.MINING) || skill.equals(Skill.THIEVING) || skill.equals(Skill.WOODCUTTING))) {
			expBoost += .20;
		}
		
		if(player.getMinutesBonusExp() != -1) {
			expBoost += .20;
		}
		
		if (player.inResourceArea() && (skill.equals(Skill.FISHING) || skill.equals(Skill.WOODCUTTING) || skill.equals(Skill.MINING) || skill.equals(Skill.SMITHING))) {
			expBoost += .50;
		}

		if(GameSettings.DOUBLE_EXPERIENCE) {
			expBoost += 1.0;
		}

		expBoost = BrawlingGloves.getExperienceIncrease(player, skill.ordinal(), expBoost);

		if(expBoost > 4.0) { //3x max
			expBoost = 4.0;
		}

		experience *= expBoost;

		/*
		 * The skill's level before adding experience.
		 */
		int startingLevel = isNewSkill(skill) ? (int) (skills.maxLevel[skill.ordinal()]/10) : skills.maxLevel[skill.ordinal()];
		/*
		 * Adds the experience to the skill's experience.
		 */
		this.skills.experience[skill.ordinal()] = this.skills.experience[skill.ordinal()] + experience > MAX_EXPERIENCE ? MAX_EXPERIENCE : (int) (this.skills.experience[skill.ordinal()] + experience);

		if(this.skills.experience[skill.ordinal()] >= MAX_EXPERIENCE) {
			String skillName = Misc.formatText(skill.toString().toLowerCase());

			Achievements.finishAchievement(player, AchievementData.REACH_MAX_EXP_IN_A_SKILL);
			World.sendMessage("<img=483> @red@[ Player News ]: </col>"+player.getUsername()+" has just achieved 200 million xp in "+skillName+"!");

		}
		/*
		 * The skill's level after adding the experience.
		 */

		int newLevel = getLevelForExperience(skill, this.skills.experience[skill.ordinal()]);
		/*
		 * If the starting level less than the new level, level up.
		 */
		if (newLevel > startingLevel) {
			int level = newLevel - startingLevel;
			String skillName = Misc.formatText(skill.toString().toLowerCase());
			skills.maxLevel[skill.ordinal()] += isNewSkill(skill) ? level * 10 : level;
			/*
			 * If the skill is not constitution, prayer or summoning, then set the current level
			 * to the max level.
			 */
			if (!isNewSkill(skill) && !skill.equals(Skill.SUMMONING) && skills.maxLevel[skill.ordinal()] > getCurrentLevel(skill)) {
				setCurrentLevel(skill, skills.maxLevel[skill.ordinal()]);
			}
			//player.getPacketSender().sendFlashingSidebar(Constants.SKILLS_TAB);

			player.setDialogue(null);
			player.getPacketSender().sendString(4268, "Congratulations! You have achieved a " + skillName + " level!");
			player.getPacketSender().sendString(4269, "Well done. You are now level " + newLevel + ".");
			player.getPacketSender().sendString(358, "Click here to continue.");
			player.getPacketSender().sendChatboxInterface(skill.getChatboxInterface());
			player.performGraphic(new Graphic(312));
			player.getPacketSender().sendMessage("You've just advanced " + skillName + " level! You have reached level " + newLevel);
			Sounds.sendSound(player, Sound.LEVELUP);
			
			if (skills.maxLevel[skill.ordinal()] == getMaxAchievingLevel(skill)) {
				player.getPacketSender().sendMessage("Well done! You've achieved the highest possible level in this skill!");
				//World.sendMessage("@red@[ Player News ] @bla@"+player.getUsername()+" has just achieved level 99 in "+skillName+"!");
				TaskManager.submit(new Task(2, player, true) {
					int localGFX = 1634;
					@Override
					public void execute() {
						player.performGraphic(new Graphic(localGFX));
						if (localGFX == 1637) {
							stop();
							return;
						}
						localGFX++;
						player.performGraphic(new Graphic(localGFX));
					}
				});
			} else {
				TaskManager.submit(new Task(2, player, false) {
					@Override
					public void execute() {
						player.performGraphic(new Graphic(199));
						stop();
					}
				});
			}
			player.getUpdateFlag().flag(Flag.APPEARANCE);

			boolean maxedOut = true;

			for(int i = 0; i < Skill.values().length; i++) {
				if(player.getSkillManager().getMaxLevel(i) < (i == 3 || i == 5 ? 990 : 99)) {
					maxedOut = false;
					break;
				}
			}

			if(maxedOut && player.achievedMax <= 0) {

				World.sendMessage("<img=733><shad=FFA500>["+Misc.capitalize(player.getGameMode().toString())+"] " + player.getName() + " has achieved all 99s"+(player.prestige > 0 ? " on prestige "+player.prestige+"" : "")+" on the "+player.getGameMode().toString()+" game mode!");

				player.achievedMax = System.currentTimeMillis();
			}

		}
		updateSkill(skill);
		this.totalGainedExp += experience;
		return this;
	}

	public SkillManager stopSkilling() {
		if(player.getCurrentTask() != null) {
			player.getCurrentTask().stop();
			player.setCurrentTask(null);
		}
		player.setResetPosition(null);
		player.setInputHandling(null);
		return this;
	}

	/**
	 * Updates the skill strings, for skill tab and orb updating.
	 * @param skill	The skill who's strings to update.
	 * @return		The Skills instance.
	 */
	public SkillManager updateSkill(Skill skill) {
		int maxLevel = getMaxLevel(skill), currentLevel = getCurrentLevel(skill);
		if (skill == Skill.PRAYER)
			player.getPacketSender().sendString(687, currentLevel + "/" + maxLevel);
		if (isNewSkill(skill)) {
			maxLevel = (maxLevel / 10);
			currentLevel = (currentLevel / 10);
		}
		player.getPacketSender().sendString(31200, ""+getTotalLevel());
		player.getPacketSender().sendString(19000, "Combat level: " + getCombatLevel());
		player.getPacketSender().sendSkill(skill);
		return this;
	}

	public SkillManager resetSkill(Skill skill, boolean prestige) {

		if(player.getEquipment().getFreeSlots() != player.getEquipment().capacity()) {
			player.getPacketSender().sendMessage("Please unequip all your items first.");
			return this;
		}
		if(player.getLocation() == Location.WILDERNESS || player.getCombatBuilder().isBeingAttacked()) {
			player.getPacketSender().sendMessage("You cannot do this at the moment");
			return this;
		}

		if(!prestige) {
			player.getInventory().delete(13663, 1);
		}

		setCurrentLevel(skill, skill == Skill.PRAYER ? 10 : skill == Skill.CONSTITUTION ? 100 : 1).setMaxLevel(skill, skill == Skill.PRAYER ? 10 : skill == Skill.CONSTITUTION ? 100 : 1).setExperience(skill, SkillManager.getExperienceForLevel(skill, skill == Skill.CONSTITUTION ? 10 : 1));

		PrayerHandler.deactivateAll(player);
		CurseHandler.deactivateAll(player); 
		BonusManager.update(player);
		WeaponInterfaces.assign(player, player.getEquipment().get(Equipment.WEAPON_SLOT));
		WeaponAnimations.assign(player, player.getEquipment().get(Equipment.WEAPON_SLOT));

		if(!prestige)
			player.getPacketSender().sendMessage("You have reset your "+skill.getFormatName()+" level.");
		return this;
	}

	/**
	 * Gets the minimum experience in said level.
	 * @param level		The level to get minimum experience for.
	 * @return			The least amount of experience needed to achieve said level.
	 */
	public static int getExperienceForLevel(Skill skill, int level) {
		int cap = skill == Skill.DUNGEONEERING ? 119 : 98;
		return EXPERIENCE_LEVELS[--level > cap ? cap : level];
	}

	private static final int EXPERIENCE_LEVELS[] = {
			/*
             * Experience for levels 1-99
             */
			0,83,174,276,388,512,650,801,969,1154,1358,1584,1833,2107,2411,2746,3115,3523,
			3973,4470,5018,5624,6291,7028,7842,8740,9730,10824,12031,13363,14833,16456,18247,
			20224,22406,24815,27473,30408,33648,37224,41171,45529,50339,55649,61512,67983,75127,
			83014,91721,101333,111945,123660,136594,150872,166636,184040,203254,224466,247886,
			273742,302288,333804,368599,407015,449428,496254,547953,605032,668051,737627,814445,
			899257,992895,1096278,1210421,1336443,1475581,1629200,1798808,1986068,2192818,2421087,
			2673114,2951373,3258594,3597792,3972294,4385776,4842295,5346332,5902831,6517253,7195629,
			7944614,8771558,9684577,10692629,11805606,13034431,

			/*
             * Experience for levels 100-120
             */
			14391160, 15889109, 17542976, 19368992, 21385073, 23611006, 26068632, 28782069, 31777943,
			35085654, 38737661, 42769801, 47221641, 52136869, 57563718, 63555443, 70170840, 77474828,
			85539082, 94442737, 104273167,

	};

	/**
	 * Gets the level from said experience.
	 * @param experience	The experience to get level for.
	 * @return				The level you obtain when you have specified experience.
	 */
	public static int getLevelForExperience(Skill skill, int exp) {
		for(int j = skill == Skill.DUNGEONEERING ? 119 : 98; j != -1; j--)
			if(EXPERIENCE_LEVELS[j] <= exp)
				return j + 1;
		return 0;
	}

	/**
	 * Calculates the player's combat level.
	 * @return	The average of the player's combat skills.
	 */
	public int getCombatLevel() {
		final int attack = skills.maxLevel[Skill.ATTACK.ordinal()];
		final int defence = skills.maxLevel[Skill.DEFENCE.ordinal()];
		final int strength = skills.maxLevel[Skill.STRENGTH.ordinal()];
		final int hp = (int) (skills.maxLevel[Skill.CONSTITUTION.ordinal()] / 10);
		final int prayer = (int) (skills.maxLevel[Skill.PRAYER.ordinal()] / 10);
		final int ranged = skills.maxLevel[Skill.RANGED.ordinal()];
		final int magic = skills.maxLevel[Skill.MAGIC.ordinal()];
		final int summoning = skills.maxLevel[Skill.SUMMONING.ordinal()];
		int combatLevel = 3;
		combatLevel = (int) ((defence + hp + Math.floor(prayer / 2)) * 0.2535) + 1;
		final double melee = (attack + strength) * 0.325;
		final double ranger = Math.floor(ranged * 1.5) * 0.325;
		final double mage = Math.floor(magic * 1.5) * 0.325;
		if (melee >= ranger && melee >= mage) {
			combatLevel += melee;
		} else if (ranger >= melee && ranger >= mage) {
			combatLevel += ranger;
		} else if (mage >= melee && mage >= ranger) {
			combatLevel += mage;
		}
		if(player.getLocation() != Location.WILDERNESS) {
			combatLevel += summoning * 0.125;
		} else {
			if (combatLevel > 126) {
				return 126;
			}
		}
		if (combatLevel > 138) {
			return 138;
		} else if (combatLevel < 3) {
			return 3;
		}
		return combatLevel;
	}

	/**
	 * Gets the player's total level.
	 * @return	The value of every skill summed up.
	 */
	public int getTotalLevel() {
		int total = 0;
		for (Skill skill : Skill.values()) {
			/*
			 * If the skill is not equal to constitution or prayer, total can 
			 * be summed up with the maxLevel.
			 */
			if (!isNewSkill(skill)) {
				total += skills.maxLevel[skill.ordinal()];
				/*
				 * Other-wise add the maxLevel / 10, used for 'constitution' and prayer * 10.
				 */
			} else {
				total += skills.maxLevel[skill.ordinal()] / 10;
			}
		}
		return total;
	}

	/**
	 * Gets the player's total experience.
	 * @return	The experience value from the player's every skill summed up.
	 */
	public long getTotalExp() {
		long xp = 0;
		for (Skill skill : Skill.values())
			xp += player.getSkillManager().getExperience(skill);
		return xp;
	}

	/**
	 * Checks if the skill is a x10 skill.
	 * @param skill		The skill to check.
	 * @return			The skill is a x10 skill.
	 */
	public static boolean isNewSkill(Skill skill) {
		return skill == Skill.CONSTITUTION || skill == Skill.PRAYER;
	}

	/**
	 * Gets the max level for <code>skill</code>
	 * @param skill		The skill to get max level for.
	 * @return			The max level that can be achieved in said skill.
	 */
	public static int getMaxAchievingLevel(Skill skill) {
		int level = 99;
		if (isNewSkill(skill)) {
			level = 990;
		}
		if (skill == Skill.DUNGEONEERING) {
			level = 120;
		}
		return level;
	}

	/**
	 * Gets the current level for said skill.
	 * @param skill		The skill to get current/temporary level for.
	 * @return			The skill's level.
	 */
	public int getCurrentLevel(Skill skill) {
		return getLevel(skill.ordinal());
	}
	
	public int getLevel(int skill) {
		return skills.level[skill];
	}

	/**
	 * Gets the max level for said skill.
	 * @param skill		The skill to get max level for.
	 * @return			The skill's maximum level.
	 */
	public int getMaxLevel(Skill skill) {
		return skills.maxLevel[skill.ordinal()];
	}

	/**
	 * Gets the max level for said skill.
	 * @param skill		The skill to get max level for.
	 * @return			The skill's maximum level.
	 */
	public int getMaxLevel(int skill) {
		return skills.maxLevel[skill];
	}

	/**
	 * Gets the experience for said skill.
	 * @param skill		The skill to get experience for.
	 * @return			The experience in said skill.
	 */
	public int getExperience(Skill skill) {
		return getExperience(skill.ordinal());
	}
	
	public int getExperience(int skill) {
		return skills.experience[skill];
	}

	/**
	 * Sets the current level of said skill.
	 * @param skill		The skill to set current/temporary level for.
	 * @param level		The level to set the skill to.
	 * @param refresh	If <code>true</code>, the skill's strings will be updated.
	 * @return			The Skills instance.
	 */
	public SkillManager setCurrentLevel(Skill skill, int level, boolean refresh) {
		this.skills.level[skill.ordinal()] = level < 0 ? 0 : level;
		if (refresh)
			updateSkill(skill);
		return this;
	}

	/**
	 * Decrements this level by {@code amount} to {@code minimum}.
	 *
	 * @param amount the amount to decrease this level by.
	 */
	public void decreaseCurrentLevel(Skill skill, int amount, int minimum) {
		final int curr = getCurrentLevel(skill);
		setCurrentLevel(skill, Math.max(minimum, curr - amount));
	}
	
	public void setSkill(int skill, int level, int exp) {
		this.skills.level[skill] = level;
		this.skills.experience[skill] = exp;
		//this.skills.maxLevel[skill] = getLevelForExperience(exp);
	}
	
	public void setMaxLevel(int skill, int level) {
		this.skills.maxLevel[skill] = level;
	}

	/**
	 * Sets the maximum level of said skill.
	 * @param skill		The skill to set maximum level for.
	 * @param level		The level to set skill to.
	 * @param refresh	If <code>true</code>, the skill's strings will be updated.
	 * @return			The Skills instance.
	 */
	public SkillManager setMaxLevel(Skill skill, int level, boolean refresh) {
		skills.maxLevel[skill.ordinal()] = level;
		if (refresh)
			updateSkill(skill);
		return this;
	}

	/**
	 * Sets the experience of said skill.
	 * @param skill			The skill to set experience for.
	 * @param experience	The amount of experience to set said skill to.
	 * @param refresh		If <code>true</code>, the skill's strings will be updated.
	 * @return				The Skills instance.
	 */
	public SkillManager setExperience(Skill skill, int experience, boolean refresh) {
		this.skills.experience[skill.ordinal()] = experience < 0 ? 0 : experience;
		if (refresh)
			updateSkill(skill);
		return this;
	}

	/**
	 * Sets the current level of said skill.
	 * @param skill		The skill to set current/temporary level for.
	 * @param level		The level to set the skill to.
	 * @return			The Skills instance.
	 */
	public SkillManager setCurrentLevel(Skill skill, int level) {
		setCurrentLevel(skill, level, true);
		return this;
	}

	/**
	 * Sets the maximum level of said skill.
	 * @param skill		The skill to set maximum level for.
	 * @param level		The level to set skill to.
	 * @return			The Skills instance.
	 */
	public SkillManager setMaxLevel(Skill skill, int level) {
		setMaxLevel(skill, level, true);
		return this;
	}

	/**
	 * Sets the experience of said skill.
	 * @param skill			The skill to set experience for.
	 * @param experience	The amount of experience to set said skill to.
	 * @return				The Skills instance.
	 */
	public SkillManager setExperience(Skill skill, int experience) {
		setExperience(skill, experience, true);
		return this;
	}

	/**
	 * The player associated with this Skills instance.
	 */
	private Player player;
	private Skills skills;
	private long totalGainedExp;

	public class Skills {
		
		/**
		 * Constants for the skill numbers.
		 */
		public static final int ATTACK = 0, DEFENCE = 1, STRENGTH = 2, HITPOINTS = 3, RANGED = 4, PRAYER = 5, MAGIC = 6,
				COOKING = 7, WOODCUTTING = 8, FLETCHING = 9, FISHING = 10, FIREMAKING = 11, CRAFTING = 12, SMITHING = 13,
				MINING = 14, HERBLORE = 15, AGILITY = 16, THIEVING = 17, SLAYER = 18, FARMING = 19, RUNECRAFTING = 20,
				CONSTRUCTION = 21, HUNTER = 22, SUMMONING = 23, DUNGEONINEERING = 24;

		public Skills() {
			level = new int[MAX_SKILLS];
			maxLevel = new int[MAX_SKILLS];
			experience = new int[MAX_SKILLS];
		}

		private int[] level, maxLevel, experience;

	}

	public Skills getSkills() {
		return skills;
	}

	public void setSkills(Skills skills) {
		this.skills = skills;
	}

	public long getTotalGainedExp() {
		return totalGainedExp;
	}

	public void setTotalGainedExp(long totalGainedExp) {
		this.totalGainedExp = totalGainedExp;
	}

	/**
	 * The maximum amount of skills in the game.
	 */
	public static final int MAX_SKILLS = 25;

	/**
	 * The maximum amount of experience you can
	 * achieve in a skill.
	 */
	private static final int MAX_EXPERIENCE = 200000000;

	private static final int EXPERIENCE_FOR_99 = 13034431;

	private static final int EXP_ARRAY[] = {
			0,83,174,276,388,512,650,801,969,1154,1358,1584,1833,2107,2411,2746,3115,3523,
			3973,4470,5018,5624,6291,7028,7842,8740,9730,10824,12031,13363,14833,16456,18247,
			20224,22406,24815,27473,30408,33648,37224,41171,45529,50339,55649,61512,67983,75127,
			83014,91721,101333,111945,123660,136594,150872,166636,184040,203254,224466,247886,
			273742,302288,333804,368599,407015,449428,496254,547953,605032,668051,737627,814445,
			899257,992895,1096278,1210421,1336443,1475581,1629200,1798808,1986068,2192818,2421087,
			2673114,2951373,3258594,3597792,3972294,4385776,4842295,5346332,5902831,6517253,7195629,
			7944614,8771558,9684577,10692629,11805606,13034431	
	};

	public void sendChatboxInterface(Item... items) {
		final String lines = "\\n\\n\\n\\n\\n";
		switch (items.length) {
			case 1:
				player.getPacketSender()
						.sendString(2799, "\\n\\n\\n\\n" + items[0].getDefinition().getName() + (ItemDefinition.forId(items[0].getId()).isStackable() ? " (" + items[0].getAmount() + ")" : ""));

				player.getPacketSender().sendInterfaceModel(1746, items[0].getId(), 190);
				player.getPacketSender().sendChatboxInterface(4429);
				break;
			case 2:
				player.getPacketSender()
						.sendString(8874, lines + items[0].getDefinition().getName() + (ItemDefinition.forId(items[0].getId()).isStackable() ? " (" + items[0].getAmount() + ")" : ""))
						.sendString(8875, lines + items[1].getDefinition().getName() + (ItemDefinition.forId(items[1].getId()).isStackable() ? " (" + items[1].getAmount() + ")" : ""))

						.sendInterfaceModel(8869, items[0].getId(), 190)
						.sendInterfaceModel(8870, items[1].getId(), 75);
				player.getPacketSender().sendChatboxInterface(8866);
				break;
			case 3:
				player.getPacketSender()
						.sendString(8889, lines + items[0].getDefinition().getName() + (ItemDefinition.forId(items[0].getId()).isStackable() ? " (" + items[0].getAmount() + ")" : ""))
						.sendString(8893, lines + items[1].getDefinition().getName() + (ItemDefinition.forId(items[1].getId()).isStackable() ? " (" + items[1].getAmount() + ")" : ""))
						.sendString(8897, lines + items[2].getDefinition().getName() + (ItemDefinition.forId(items[2].getId()).isStackable() ? " (" + items[1].getAmount() + ")" : ""))

						.sendInterfaceModel(8884, items[1].getId(), 190)
						.sendInterfaceModel(8883, items[0].getId(), 190)
						.sendInterfaceModel(8885, items[2].getId(), 190);
				player.getPacketSender().sendChatboxInterface(8880);
				break;
			case 4:
				player.getPacketSender()
						.sendString(8906, lines + items[0].getDefinition().getName() + (ItemDefinition.forId(items[0].getId()).isStackable() ? " (" + items[0].getAmount() + ")" : ""))
						.sendString(8910, lines + items[1].getDefinition().getName() + (ItemDefinition.forId(items[1].getId()).isStackable() ? " (" + items[1].getAmount() + ")" : ""))
						.sendString(8914, lines + items[2].getDefinition().getName() + (ItemDefinition.forId(items[2].getId()).isStackable() ? " (" + items[2].getAmount() + ")" : ""))
						.sendString(8918, lines + items[3].getDefinition().getName() + (ItemDefinition.forId(items[3].getId()).isStackable() ? " (" + items[3].getAmount() + ")" : ""))

						.sendInterfaceModel(8902, items[0].getId(), 190)
						.sendInterfaceModel(8903, items[1].getId(), 75)
						.sendInterfaceModel(8904, items[2].getId(), 190)
						.sendInterfaceModel(8905, items[3].getId(), 75);
				player.getPacketSender().sendChatboxInterface(8899);
				break;
			case 5:
				player.getPacketSender().sendChatboxInterface(8938);
				break;
		}
	}

	

}
