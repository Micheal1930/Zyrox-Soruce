package com.varrock.net.packet.impl;

import com.varrock.net.packet.Packet;
import com.varrock.net.packet.PacketListener;
import com.varrock.world.World;
import com.varrock.world.content.Debug;
import com.varrock.world.content.combat.magic.CombatSpell;
import com.varrock.world.content.combat.magic.CombatSpells;
import com.varrock.world.entity.impl.player.Player;

public class MagicOnPlayerPacketListener implements PacketListener {

	@Override
	public void handleMessage(Player player, Packet packet) {
		int playerIndex = packet.readShortA();
		if(playerIndex < 0 || playerIndex > World.getPlayers().capacity())
			return;
		int spellId = packet.readLEShort();
		if (spellId < 0) {
			return;
		}
		
		if (player.isSpectatorMode()) {
			player.sendMessage("You cannot do this in spectator mode.");
			return;
		}
		
		/**
		 * Logging
		 */
		
		Player attacked = World.getPlayers().get(playerIndex);
		if(attacked == null){
			player.getMovementQueue().reset();
			return;
		}

		if (attacked == null || attacked.equals(player)) {
			player.getMovementQueue().reset();
			return;
		}
		
		if(player.getEquipment().contains(51015)) {
			player.getPacketSender().sendMessage("Your bulwark gets in the way.");
			player.getMovementQueue().reset();
			player.getCombatBuilder().reset(true);
			return; 
		}

		CombatSpell spell = CombatSpells.getSpell(spellId);
		if(spell == null) {
			player.getMovementQueue().reset();
			return;
		}
		
		if(attacked.getConstitution() <= 0) {
			player.getMovementQueue().reset();
			return;
		}
		
		// Start combat!
		player.setPositionToFace(attacked.getPosition());
		player.getCombatBuilder().resetCooldown();
		player.setCastSpell(spell);
		player.getCombatBuilder().attack(attacked);
	}

}
