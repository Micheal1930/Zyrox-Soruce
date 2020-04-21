package com.varrock.net.packet.command.impl;

import java.io.*;

import org.eclipse.jgit.api.PullResult;

import com.varrock.GameServer;
import com.varrock.GameSettings;
import com.varrock.engine.task.Task;
import com.varrock.engine.task.TaskManager;
import com.varrock.net.packet.command.CommandHeader;
import com.varrock.net.packet.command.NameCommand;
import com.varrock.util.GitDeploy;
import com.varrock.world.World;
import com.varrock.world.entity.impl.player.Player;

@CommandHeader(command = { "update" }, description = "Starts the system update.")
public class UpdateCommand extends NameCommand {

	public UpdateCommand() {
		super(GameSettings.SPECIAL_STAFF_NAMES);
	}

	@Override
	public boolean execute(Player player, String[] args) throws Exception {
		if (args.length != 1) {
			throw new IllegalArgumentException();
		}

		World.savePlayers();

		int time = Integer.parseInt(args[0]);

		World.restart(player, time, true);

		return false;
	}

	@Override
	public String[] getUsage() {
		return new String[] {
			"::update [time]"
		};
	}

}
