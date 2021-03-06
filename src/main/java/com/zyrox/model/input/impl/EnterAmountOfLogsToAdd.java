package com.zyrox.model.input.impl;

import com.zyrox.model.GameObject;
import com.zyrox.model.Item;
import com.zyrox.model.Position;
import com.zyrox.model.definitions.ItemDefinition;
import com.zyrox.model.input.EnterAmount;
import com.zyrox.world.content.skill.impl.firemaking.Firemaking;
import com.zyrox.world.content.skill.impl.firemaking.LogData;
import com.zyrox.world.content.skill.impl.firemaking.LogData.Log;
import com.zyrox.world.entity.impl.player.Player;

public class EnterAmountOfLogsToAdd extends EnterAmount {

	private boolean canBeNoted;

	public EnterAmountOfLogsToAdd(boolean canBeNoted) {
		this.canBeNoted = canBeNoted;
	}

	@Override
	public boolean handleAmount(Player player, int amount) {

		int logId = LogData.getFirstLogInInventory(player, canBeNoted);

		Log log = LogData.getLogForLogId(player, logId);

		ItemDefinition itemDefinition = ItemDefinition.forId(logId);
		if(itemDefinition.isNoted()) {
			log = LogData.getLogForLogId(player, itemDefinition.getUnnotedId());
		}

		if(log == null) {
			player.sendMessage("You don't have any logs in your inventory.");
			return false;
		}

		if(!player.getInventory().contains(new Item(logId, amount))) {
			amount = player.getInventory().getAmount(logId);
		}

		Firemaking.lightFire(player, log.getLogId(), true, amount, itemDefinition.isNoted());

		if (player.getInteractingObject() != null)
			player.setPositionToFace(player.getInteractingObject().getPosition());

		return false;
	}

	public static void openInterface(Player player, GameObject gameObject) {
		player.getPacketSender().sendInterfaceRemoval();
		player.getSkillManager().stopSkilling();

		boolean canBeNoted = false;

		if(gameObject.getPosition().equals(new Position(3276, 3938))) { //fire at rogues castle
			canBeNoted = true;
		}

		int logId = LogData.getFirstLogInInventory(player, canBeNoted);

		Log log = LogData.getLogForLogId(player, logId);

		ItemDefinition itemDefinition = ItemDefinition.forId(logId);
		if(itemDefinition.isNoted()) {
			log = LogData.getLogForLogId(player, itemDefinition.getUnnotedId());
		}

		if (log == null) {
			player.getPacketSender().sendMessage("You do not have any logs to add to this fire.");
			return;
		}

		player.setInputHandling(new EnterAmountOfLogsToAdd(canBeNoted));
		player.getPacketSender().sendEnterAmountPrompt("How many logs would you like to add to the fire?");
	}

}
