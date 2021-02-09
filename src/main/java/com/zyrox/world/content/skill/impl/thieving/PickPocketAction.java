package com.zyrox.world.content.skill.impl.thieving;

import com.zyrox.model.*;
import com.zyrox.model.action.Action;
import com.zyrox.model.container.impl.Equipment;
import com.zyrox.util.Misc;
import com.zyrox.world.World;
import com.zyrox.world.entity.impl.npc.NPC;
import com.zyrox.world.entity.impl.player.Player;

/**
 * Created by Greg on 27/12/2016.
 */
public class PickPocketAction extends Action {

	/**
	 * Pick pocketing npc.
	 */
	private NPC npc;

	/**
	 * Data of an npc.
	 */
	private PickPocketableNPC npcData;

	/**
	 * The npc stun animation.
	 */
	private static final Animation STUN_ANIMATION = new Animation(422),

			/**
			 * The pick pocketing animation.
			 */
			PICKPOCKETING_ANIMATION = new Animation(881),

			/**
			 * The double loot animation.
			 */
			DOUBLE_LOOT_ANIMATION = new Animation(5074),

			/**
			 * The triple loot animation.
			 */
			TRIPLE_LOOT_ANIMATION = new Animation(5075),

			/**
			 * The quadruple loot animation.
			 */
			QUADRUPLE_LOOT_ANIMATION = new Animation(5078);

	/**
	 * The double loot gfx.
	 */
	private static final Graphic DOUBLE_LOOT_GFX = new Graphic(873),

			/**
			 * The triple loot gfx.
			 */
			TRIPLE_LOOT_GFX = new Graphic(874),

			/**
			 * The quadruple loot gfx.
			 */
			QUADRUPLE_LOOT_GFX = new Graphic(875);

	/**
	 * The index to use in the levels required arrays.
	 */
	private int index;

	/**
	 * Constructs a new {@code PickpocketAction} {@code Object}.
	 * 
	 * @param npc     The npc to whom the player is pickpocketing.
	 * @param npcData Data of an npc.
	 */
	public PickPocketAction(NPC npc, PickPocketableNPC npcData) {
		this.npc = npc;
		this.npcData = npcData;
	}

	public boolean start(Player player) {
		if (checkAll(player)) {
			int thievingLevel = player.getSkillManager().getCurrentLevel(Skill.THIEVING);
			int agilityLevel = player.getSkillManager().getCurrentLevel(Skill.AGILITY);
			if (Misc.getRandom(50) < 5) {
				for (int i = 0; i < 4; i++) {
					if (npcData.getThievingLevels()[i] <= thievingLevel
							&& npcData.getAgilityLevels()[i] <= agilityLevel)
						index = i;
				}
			}
			player.setPositionToFace(npc.getPosition());
			player.performAnimation(getAnimation());
			player.performGraphic(getGraphics());
			player.getPacketSender()
					.sendMessage("You attempt to pick the " + npc.getDefinition().getName().toLowerCase() + "'s pocket...");
			// setActionDelay(player, 1);
			return true;
		}
		return false;
	}

	public boolean process(Player player) {
		return checkAll(player);
	}

	public int processWithDelay(Player player) {
		if (!isSuccessful(player)) {
			player.getPacketSender().sendMessage("You fail to pick the " + npc.getDefinition().getName().toLowerCase() + "'s pocket.");
			npc.setPositionToFace(player.getPosition());
			player.performAnimation(new Animation(424));
			player.performGraphic(new Graphic(80, 5, GraphicHeight.HIGH));
			player.getPacketSender().sendMessage("You've been stunned.");
			// player.dealDamage(new Hit(npcData.getStunDamage(), Hitmask.RED,
			// CombatIcon.NONE));
			if (npcData.equals(PickPocketableNPC.MASTER_FARMER) || npcData.equals(PickPocketableNPC.FARMER)) {
				npc.forceChat("Cor blimey mate, what are ye doing in me pockets?");
				npc.performAnimation(STUN_ANIMATION);
			} else if (npcData.equals(PickPocketableNPC.DESERT_PHONIX))
				npc.forceChat("Squawk!");
			else {
				npc.forceChat("What do you think you're doing?");
				npc.performAnimation(STUN_ANIMATION);
			}
			player.getMovementQueue().stun(npcData.getStunTime());
			stop(player);
		} else {
			player.getPacketSender().sendMessage(getMessage());
			double totalXp = npcData.getExperience();
			if (hasThievingSuit(player))
				totalXp *= 1.025;
			player.getSkillManager().addExperience(Skill.THIEVING, totalXp * Skill.THIEVING.getModifier());

			for (int id : PickPocketableNPC.MAN.getNpcIds()) {
				if (npc.getId() == id) {
					/*player.getDiariesManager().incr(DiaryTask.PICKPOCKET_MAN);
					player.getDiariesManager().incr(DiaryTask.PICKPOCKET_10_MEN);*/
				}
			}
			for (int id : PickPocketableNPC.GUARD.getNpcIds()) {
				if (npc.getId() == id) {
					//player.getDiariesManager().incr(DiaryTask.PICKPOCKET_100_GUARDS);
				}
			}
			for (int id : PickPocketableNPC.HERO.getNpcIds()) {
				if (npc.getId() == id) {
				//	player.getDiariesManager().incr(DiaryTask.PICKPOCKET_300_HEROES);
				}
			}
			if (Misc.getRandom(14000) == 3) {
				player.getInventory().add(13324, 1, "Steal from stall");
				World.sendMessage("<col=6666FF> <img=10> [ Skilling Pets ]:</col> " + player.getUsername() + " has received the Rocky pet!");
				player.getPacketSender().sendMessage("@red@You have received a skilling pet!");
				player.getPacketSender().sendMessage("@red@Your account has been saved!");
				player.save();
			}
			for (int i = 0; i <= index; i++) {
				Item item = npcData.getLoot()[Misc.random(npcData.getLoot().length - 1)];
				player.getInventory().add(item);
			}
		}
		return -1;
	}

	public void stop(Player player) {
		npc.setPositionToFace(null);
		setActionDelay(player, 3);
	}

	private boolean hasThievingSuit(Player player) {
		return player.getEquipment().get(Equipment.HEAD_SLOT).getId() == 21482
				&& player.getEquipment().get(Equipment.BODY_SLOT).getId() == 21480
				&& player.getEquipment().get(Equipment.LEG_SLOT).getId() == 21481
				&& player.getEquipment().get(Equipment.FEET_SLOT).getId() == 21483;
	}

	/**
	 * Checks if the player is succesfull to thiev or not.
	 * 
	 * @param player The player.
	 * @return {@code True} if succesfull, {@code false} if not.
	 */
	private boolean isSuccessful(Player player) {
		int thievingLevel = player.getSkillManager().getCurrentLevel(Skill.THIEVING);
		int increasedChance = getIncreasedChance(player);
		int level = Misc.getRandom(thievingLevel + increasedChance);
		double ratio = level / npcData.getThievingLevels()[0];
		// if (Math.round(ratio * thievingLevel) < npcData.getThievingLevels()[0] /
		// player.getAuraManager().getThievingAccurayMultiplier())
		// return false;
		return Misc.random(4) != 0;
	}

	/**
	 * Gets the increased chance for succesfully pickpocketing.
	 * 
	 * @param player The player.
	 * @return The amount of increased chance.
	 */
	private int getIncreasedChance(Player player) {
		int chance = 0;
		if (player.getEquipment().get(Equipment.HANDS_SLOT).getId() == 10075)
			chance += 12;
		// if (Equipment.wearingArdyCloak(player, 2))
		// chance += 15;
		if (npc.getDefinition().getName().contains("H.A.M")) {
			for (Item item : player.getEquipment().getItems()) {
				if (item != null && item.getDefinition().getName().contains("H.A.M")) {
					chance += 3;
				}
			}
		}
		return chance;
	}

	/**
	 * Gets the message to send when finishing.
	 * 
	 * @return The message.
	 */
	private String getMessage() {
		switch (index) {
		case 0:
			return "You successfully pick the " + npc.getDefinition().getName().toLowerCase() + "'s pocket.";
		case 1:
			return "Your lighting-fast reactions allow you to steal double loot.";
		case 2:
			return "Your lighting-fast reactions allow you to steal triple loot.";
		case 3:
			return "Your lighting-fast reactions allow you to steal quadruple loot.";
		}
		return null;
	}

	/**
	 * Checks everything before starting.
	 * 
	 * @param player The player.
	 */
	private boolean checkAll(Player player) {
		if (player.getSkillManager().getCurrentLevel(Skill.THIEVING) < npcData.getThievingLevels()[0]) {
			player.getPacketSender().sendMessage(
					"You need a thieving level of " + npcData.getThievingLevels()[0] + " to steal from this npc.");
			return false;
		}

		if (player.getInventory().getFreeSlots() < 1) {
			player.getPacketSender().sendMessage("You don't have enough space in your inventory.");
			return false;
		}
		if (player.getCombatBuilder().isBeingAttacked()) {
			player.getPacketSender().sendMessage("You can't do this while you're in combat.");
			return false;
		}
		if (npc.getCombatBuilder().isBeingAttacked()) {
			player.getPacketSender().sendMessage("The npc is currently in combat.");
			return false;
		}
		// if (npc.hasFinished()) {
		// player.getPacketSender().sendMessage("Too late, the npc is dead.");
		// return false;
		// }
		return true;

	}

	/**
	 * Gets the animation to perform.
	 * 
	 * @return The animation.
	 */
	private Animation getAnimation() {
		switch (index) {
		case 0:
			return PICKPOCKETING_ANIMATION;
		case 1:
			return DOUBLE_LOOT_ANIMATION;
		case 2:
			return TRIPLE_LOOT_ANIMATION;
		case 3:
			return QUADRUPLE_LOOT_ANIMATION;
		}
		return null;
	}

	/**
	 * Gets the graphic to perform.
	 * 
	 * @return The graphic.
	 */
	private Graphic getGraphics() {
		switch (index) {
		case 0:
			return null;
		case 1:
			return DOUBLE_LOOT_GFX;
		case 2:
			return TRIPLE_LOOT_GFX;
		case 3:
			return QUADRUPLE_LOOT_GFX;
		}
		return null;
	}
}
