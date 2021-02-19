package com.zyrox.world.content;

import com.zyrox.GameSettings;
import com.zyrox.world.entity.impl.player.Player;

public class CustomQuestTab {
	
	public static boolean handleButtonClicks(Player player, int id) {
		switch(id) {
		case 55030:
		case 55037://Button to switch to Tab 1
			player.getPacketSender().sendTabInterface(GameSettings.QUESTS_TAB, 55000);
			updateTabOne(player);
			return true;
		case 55018:
		case 55036://Button to switch to Tab 2
			player.getPacketSender().sendTabInterface(GameSettings.QUESTS_TAB, 55020);
			updateTabTwo(player);
			return true;
		case 55019:
		case 55031://Button To switch to Tab 3
			player.getPacketSender().sendTabInterface(GameSettings.QUESTS_TAB, 55040);
			updateTabThree(player);
			return true;
		case -11533://Button one on Server Statistics Tab
			
			return true;
		case -11532://Button two on Server Statistics Tab
					
			return true;
		case -11531://Button three on Server Statistics Tab
			
			return true;
		case -11530://Button four on Server Statistics Tab
			
			return true;
		case -11529://Button five on Server Statistics Tab
			
			return true;
		default: return false;
		}
	}
	
	public static void updateTabOne(Player player) {
		int counter = 55003;
		player.getPacketSender().sendString(counter++, "@or2@Players Online: " + "@yel@" + "Default");
		player.getPacketSender().sendString(counter++, "@or2@Server Uptime: " + "@yel@" + "Default");
		player.getPacketSender().sendString(counter++, "@or2@Bonus: " + "@yel@" + "Default");
		player.getPacketSender().sendString(counter++, "@or2@Well of Goodwill: " + "@yel@" + "Default");
		player.getPacketSender().sendString(counter++, "@or2@Drops & Experience: " + "@yel@" + "Default");
		
		player.getPacketSender().sendString(counter++, "@yel@Website");
		player.getPacketSender().sendString(counter++, "@yel@Rules");
		player.getPacketSender().sendString(counter++, "@yel@Forums");
		player.getPacketSender().sendString(counter++, "@yel@Staff");
		player.getPacketSender().sendString(counter++, "@yel@Store");
		player.getPacketSender().sendString(counter++, "@yel@Guides");
		player.getPacketSender().sendString(counter++, "@yel@Discord");
		player.getPacketSender().sendString(counter++, "@yel@Youtube");
		player.getPacketSender().sendString(counter++, "@yel@Help");
		player.getPacketSender().sendString(counter++, "@yel@Default");
	}
	
	public static void updateTabTwo(Player player) {
		int counter = 55023;
		player.getPacketSender().sendString(counter++, "@or2@Points: " + "@yel@" + "Default");
		player.getPacketSender().sendString(counter++, "@or2@Points: " + "@yel@" + "Default");
		player.getPacketSender().sendString(counter++, "@or2@Points: " + "@yel@" + "Default");
		player.getPacketSender().sendString(counter++, "@or2@Points: " + "@yel@" + "Default");
		player.getPacketSender().sendString(counter++, "@or2@Points: " + "@yel@" + "Default");
	}
	
	public static void updateTabThree(Player player) {
		int counter = 55042;
		player.getPacketSender().sendString(counter++, "@or2@Points: " + "@yel@" + "Default");
		player.getPacketSender().sendString(counter++, "@or2@Points: " + "@yel@" + "Default");
		player.getPacketSender().sendString(counter++, "@or2@Points: " + "@yel@" + "Default");
		player.getPacketSender().sendString(counter++, "@or2@Points: " + "@yel@" + "Default");
		player.getPacketSender().sendString(counter++, "@or2@Points: " + "@yel@" + "Default");
	}

}
