package com.varrock.net.packet.command.impl;

import org.zeroturnaround.zip.FileSource;
import org.zeroturnaround.zip.ZipEntrySource;
import org.zeroturnaround.zip.ZipUtil;

import com.varrock.GameSettings;
import com.varrock.model.PlayerRights;
import com.varrock.net.packet.command.Command;
import com.varrock.net.packet.command.CommandHeader;
import com.varrock.net.packet.command.NameCommand;
import com.varrock.util.Misc;
import com.varrock.world.World;
import com.varrock.world.entity.impl.player.Player;

import java.io.File;

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
