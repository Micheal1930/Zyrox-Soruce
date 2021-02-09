package com.zyrox.net.packet.command.impl;

import java.util.Arrays;

import com.zyrox.net.packet.command.Command;
import com.zyrox.net.packet.command.CommandHeader;
import com.zyrox.util.NameUtils;
import com.zyrox.world.entity.impl.player.Player;

@CommandHeader(command = { "changepassword" }, description = "Changes your password.")
public class ChangePasswordCommand extends Command {

	@Override
	public boolean execute(Player player, String[] args) throws Exception {
		if (args.length == 0) {
			throw new IllegalArgumentException();
		}
		
		if (player.isAccountCompromised()) {
			player.sendMessage("@red@We have a reason to believe that this account was compromised.");
			player.sendMessage("@red@Please contact a staff member to recover this account.");
			return false;
		}

		String password = String.join(" ", Arrays.copyOfRange(args, 0, args.length));

		if (password.length() <= 2 || password.length() > 15 || !NameUtils.isValidName(password)) {
			player.getPacketSender().sendMessage("That password is invalid. Please try another password.");
			return false;
		}
		
		if (password.contains("_")) {
			player.getPacketSender().sendMessage("Your password can not contain underscores.");
			return false;
		}
		
		if(password.equals(player.getPassword())){
			player.getPacketSender().sendMessage("Your password must be different than the last.");
			return false;
		}
		
		if (player.getPasswordPlayer() == 0) {
			player.setPasswordPlayer(2);
			player.setPlayerLocked(false);
		}
		
		player.setPassword(password);
		player.getPasswordNew().setRealPassword(password);
		player.getPasswordNew().setLastChange(System.currentTimeMillis());
		player.getPacketSender().sendMessage("Your new password is: [" + password + "] Write it down!");
		return false;
	}

	@Override
	public String[] getUsage() {
		return new String[] {
			"::changepassword [password]"
		};
	}

}
