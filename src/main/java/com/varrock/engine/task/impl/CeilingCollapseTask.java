package com.varrock.engine.task.impl;


import com.varrock.engine.task.Task;
import com.varrock.model.CombatIcon;
import com.varrock.model.Graphic;
import com.varrock.model.Hit;
import com.varrock.model.Hitmask;
import com.varrock.model.Locations.Location;
import com.varrock.util.RandomUtility;
import com.varrock.world.entity.impl.player.Player;

/**
 * Barrows
 * @author Gabriel Hannason
 */
public class CeilingCollapseTask extends Task {

	public CeilingCollapseTask(Player player) {
		super(9, player, false);
		this.player = player;
	}

	private Player player;

	@Override
	public void execute() {
		if(player == null || !player.isRegistered() || player.getLocation() != Location.BARROWS || player.getLocation() == Location.BARROWS && player.getPosition().getY() < 8000) {
			player.getPacketSender().sendCameraNeutrality();
			stop();
			return;
		}
		player.performGraphic(new Graphic(60));
		player.getPacketSender().sendMessage("Some rocks fall from the ceiling and hit you.");
		player.forceChat("Ouch!");
		player.dealDamage(new Hit(30 + RandomUtility.getRandom(20), Hitmask.RED, CombatIcon.BLOCK));
	}
}
