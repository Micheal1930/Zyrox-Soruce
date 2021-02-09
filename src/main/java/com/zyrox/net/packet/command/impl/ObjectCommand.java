package com.zyrox.net.packet.command.impl;

import com.zyrox.model.GameObject;
import com.zyrox.model.PlayerRights;
import com.zyrox.model.Position;
import com.zyrox.net.packet.command.Command;
import com.zyrox.net.packet.command.CommandHeader;
import com.zyrox.world.content.CustomObjects;
import com.zyrox.world.entity.impl.player.Player;

@CommandHeader(command = { "object" }, description = "Spawns a game object")
public class ObjectCommand extends Command {

	public ObjectCommand() {
		super(PlayerRights.OWNER, false);
	}
	
	@Override
	public boolean execute(Player player, String[] args) throws Exception {
		try {
			int objectId = Integer.valueOf(args[0]);
			int type = args.length > 1 ? Integer.valueOf(args[1]) : 10;
			int face = args.length > 2 ? Integer.valueOf(args[2]) : 10;
			
			CustomObjects.spawnObject(player, new GameObject(objectId,
					new Position(player.getPosition().getX(), player.getPosition().getY(), player.getPosition().getZ()), type, face));
			player.sendMessage("Spawning ObjectId="+objectId);
		} catch (Exception e) {
			player.sendMessage("Error! try using ::object id");
		}
		return true;
	}

	@Override
	public String[] getUsage() {
		return new String[] {
			"::object"
		};
	}

}
