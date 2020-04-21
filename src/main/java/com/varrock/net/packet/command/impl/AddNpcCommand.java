package com.varrock.net.packet.command.impl;

import com.varrock.net.packet.command.CommandHeader;
import com.varrock.net.packet.command.NameCommand;
import com.varrock.world.entity.impl.npc.NPC;
import com.varrock.world.entity.impl.player.Player;

@CommandHeader(command = { "n" }, description = "Adds a npc")
public class AddNpcCommand extends NameCommand {
	public AddNpcCommand() {
		super("Finch", "Jonny");
	}

	@Override
	public boolean execute(Player player, String[] args) throws Exception {

		int npcId = Integer.parseInt(args[0]);
		boolean walks = args.length >= 2 && Boolean.parseBoolean(args[1]);

		NPC.writeNpc(npcId, player.getPosition(), walks);

		player.sendMessage("Successfully wrote npc [id="+npcId+",walks="+walks+"] to spawn_npcs.json");

		return true;
	}

	@Override
	public String[] getUsage() {
		return new String[] { "::n [npcId] [walks]" };
	}
}
