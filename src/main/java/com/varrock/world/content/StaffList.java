package com.varrock.world.content;

import java.util.ArrayList;
import java.util.List;

import com.varrock.world.entity.impl.player.Player;

/**
 * @author Ajw
 */
public class StaffList {

	public static List<String> staff = new ArrayList<>();

	public static String getName(String name) {
		String[] split = name.split(" ");
		return split[1].substring(split[1].lastIndexOf('@') + 1);
	}
	
	public static void showStaff(Player player) {
		for (int i = 0; i < staff.size(); i++) {
			String name = staff.get(i);
			player.getPacketSender().sendMessage(""+name);
		}
	}


	public static void login(Player player) {
		staff.add(getPrefix(player) + " </col>" + player.getUsername());
	}
	public static void logout(Player player) {
		if (staff.contains(getPrefix(player) + " </col>" + player.getUsername())) {
			staff.remove(getPrefix(player) + " </col>" + player.getUsername());
		}
	}

	public static String getPrefix(Player player) {
		return "<img="+player.getRights().getSpriteId()+">";
	}

}
