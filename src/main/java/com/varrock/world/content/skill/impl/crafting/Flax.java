package com.varrock.world.content.skill.impl.crafting;

import com.varrock.engine.task.Task;
import com.varrock.engine.task.TaskManager;
import com.varrock.model.Animation;
import com.varrock.model.Skill;
import com.varrock.model.Locations.Location;
import com.varrock.model.input.impl.EnterAmountToSpin;
import com.varrock.world.content.interfaces.MakeInterface;
import com.varrock.world.entity.impl.player.Player;

public class Flax {
	
	private static final int FLAX_ID = 1779;
	private static final int FLAX_ID2 = 1780;
	private static final int BOW_STRING_ID = 1777;
	
	/*public static void showSpinInterface(Player player) {
		player.getPacketSender().sendInterfaceRemoval();
		player.getSkillManager().stopSkilling();
		if(Location.inResource(player)) {
			if(!player.getInventory().contains(1780)) {
				player.getPacketSender().sendMessage("You do not have any Flax to spin.");
				return;
			}
		} else {
			if(!player.getInventory().contains(1779)) {
				player.getPacketSender().sendMessage("You do not have any Flax to spin.");
				return;
			}
		}
	
		player.setInputHandling(new EnterAmountToSpin());
		player.getPacketSender().sendString(2799, "Flax").sendInterfaceModel(1746, FLAX_ID, 150).sendChatboxInterface(4429);
		player.getPacketSender().sendString(2800, "How many would you like to make?");
	}*/

	public static void showSpinInterface(Player player) {
		player.getPacketSender().sendInterfaceRemoval();
		player.getSkillManager().stopSkilling();
		if(Location.inResource(player)) {
			if(!player.getInventory().contains(1780)) {
				player.getPacketSender().sendMessage("You do not have any Flax to spin.");
				return;
			}
		} else {
			if(!player.getInventory().contains(1779)) {
				player.getPacketSender().sendMessage("You do not have any Flax to spin.");
				return;
			}
		}
		MakeInterface.open(player, new int[] {BOW_STRING_ID}, MakeInterface.MakeType.FLAX);
	}
	
	public static void spinFlax(final Player player, final int amount) {
		if(amount <= 0)
			return;
		player.getSkillManager().stopSkilling();
		player.getPacketSender().sendInterfaceRemoval();
		player.setCurrentTask(new Task(2, player, true) {
			int amountSpan = 0;
			@Override
			public void execute() {
				if(Location.inResource(player)) {
					if(!player.getInventory().contains(FLAX_ID2)) {
						stop();
						return;
					}
				} else {
					if(!player.getInventory().contains(FLAX_ID)) {
						stop();
						return;
					}	
				}
				
				
				player.getSkillManager().addExperience(Skill.CRAFTING, 15 * Skill.CRAFTING.getModifier());
				player.performAnimation(new Animation(896));
				if(Location.inResource(player)) {
				
				player.getInventory().delete(FLAX_ID2, 1);
				player.getInventory().add(1778, 1, "Flax");
				} else {
				
				player.getInventory().delete(FLAX_ID, 1);
				player.getInventory().add(1777, 1, "Flax");
				}
				amountSpan++;
				if(amountSpan >= amount)
					stop();
			}
		});
		TaskManager.submit(player.getCurrentTask());
	}
}
