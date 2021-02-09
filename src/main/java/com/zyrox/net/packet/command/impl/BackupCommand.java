package com.zyrox.net.packet.command.impl;

import com.zyrox.model.PlayerRights;
import com.zyrox.net.packet.command.Command;
import com.zyrox.net.packet.command.CommandHeader;
import com.zyrox.world.World;
import com.zyrox.world.entity.impl.player.Player;

@CommandHeader(command = { "backup" }, description = "Creates a backup of char files and auction house.")
public class BackupCommand extends Command {

	public BackupCommand() {
		super(PlayerRights.ADMINISTRATOR, false);
	}

	@Override
	public boolean execute(Player player, String[] args) throws Exception {

		World.savePlayers();

		long timeToBackup = System.currentTimeMillis();

		World.backup(timeToBackup);

		player.sendMessage("Backup created: backup_"+timeToBackup+".zip");

		return false;
	}

	@Override
	public String[] getUsage() {
		return new String[] {
			"::backup"
		};
	}

}
