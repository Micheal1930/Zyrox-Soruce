package com.varrock.world.content;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import com.varrock.model.Item;
import com.varrock.world.entity.impl.player.Player;

/*public class DailyLogin {

	static Calendar cal = Calendar.getInstance();
    static int year = cal.get(Calendar.YEAR);
    static int date = cal.get(Calendar.DAY_OF_MONTH);
    static int month = cal.get(Calendar.MONTH)+1;
    
    List<Item> item0 = Arrays.asList(new Item(995, 100000));
    List<Item> item1 = Arrays.asList(new Item(995, 200000));
    List<Item> item2 = Arrays.asList(new Item(995, 300000));
    List<Item> item3 = Arrays.asList(new Item(995, 400000));
    List<Item> item4 = Arrays.asList(new Item(995, 500000));
    List<Item> item5 = Arrays.asList(new Item(995, 600000));
    List<Item> item6 = Arrays.asList(new Item(995, 700000));
    List<Item> item7 = Arrays.asList(new Item(995, 800000));
    List<Item> item8 = Arrays.asList(new Item(995, 900000));
    List<Item> item9 = Arrays.asList(new Item(995, 1000000));
    List<List> whole =  Arrays.asList(item0, item1, item2, item3, item4, item5, item6, item7, item8, item9);
    
    private Player c;
    
	public DailyLogin(Player client) {
		this.c = client;
	}
	public static int getLastDayOfMonth(int month) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(year, month -1, date);
		int maxDay = calendar.getActualMaximum(date);
        return maxDay;
	}
	
	public void openInterface(Player c) {
		c.getPacketSender().sendConfig(330, c.getLoginStreak() - 1);
		c.getPacketSender().sendString(73005, "");
		c.getPacketSender().sendString(73006, "");
		c.getPacketSender().sendString(73007, "Current Streak: "+c.getLoginStreak());
		c.getPacketSender().sendItemsOnInterface(58801, 10, item0, true);
		c.getPacketSender().sendItemsOnInterface(58802, 10, item1, true);
		c.getPacketSender().sendItemsOnInterface(58803, 10, item2, true);
		c.getPacketSender().sendItemsOnInterface(58804, 10, item3, true);
		c.getPacketSender().sendItemsOnInterface(58805, 10, item4, true);
		c.getPacketSender().sendItemsOnInterface(58806, 10, item5, true);
		c.getPacketSender().sendItemsOnInterface(58807, 10, item6, true);
		c.getPacketSender().sendItemsOnInterface(58808, 10, item7, true);
		c.getPacketSender().sendItemsOnInterface(58809, 10, item8, true);
		c.getPacketSender().sendItemsOnInterface(58810, 10, item9, true);
		c.getPacketSender().sendItemsOnInterface(58811, 10, whole.get(c.getLoginStreak() - 1), true);
		c.getPacketSender().sendItemsOnInterface(58812, 10, whole.get(c.getLoginStreak()), true);
		c.getPacketSender().sendInterface(73000);
	}
	
	public void determineNext(Player c) {
		
	}
	
	public void IncreaseStreak() {
		c.setLastLoginYear(year);
		c.setLastLoginDate(date);
		c.setLastLoginMonth(month);
		c.setLoginStreak(c.getLoginStreak() + 1);
		switch(c.getLoginStreak()) { //Add rewards here!
		case 1:
			
			c.sendMessage("You have logged in for the first time! Log in tomorrow for a better reward");
			break;
		case 2:
			
			c.sendMessage("You have logged in for the second time! Log in tomorrow for a better reward");
			break;
		case 3:
			
			c.sendMessage("You have logged in for the third time! Log in tomorrow for a better reward");
			break;
			
		case 4:
			
			break;
			
		case 5:
			
			break;
			
		case 6:
			
			break;
			
		case 7:
			
			break;
			
		case 8:
			
			break;
			
		case 9:
			
			break;
			
		case 10:
			
			c.setLoginStreak(0);
			break;
			
			
		}
	}
	public void RefreshDates() {
		cal = Calendar.getInstance();
		year = cal.get(Calendar.YEAR);
		date = cal.get(Calendar.DAY_OF_MONTH);
		month = cal.get(Calendar.MONTH)+1;
	}
	public void LoggedIn() {
		RefreshDates();
		if ((c.getLastLoginYear() == year) && (c.getLastLoginMonth() == month) && (c.getLastLoginDate() == date)) {
			c.sendMessage("You have already recieved your daily reward for today.");
			return;
		}
		if ((c.getLastLoginYear() == year) && (c.getLastLoginMonth() == month) && (c.getLastLoginDate() == (date - 1)))
			IncreaseStreak();
		else if ((c.getLastLoginYear() == year) && ((c.getLastLoginMonth() + 1) == month) && (1 == date))
			IncreaseStreak();
		else if ((c.getLastLoginYear() == year-1) && (1 == month) && (1 == date))
			IncreaseStreak();
		else {
			//c.setLoginStreak(0);
			IncreaseStreak();
		}
	}
	
}*/
