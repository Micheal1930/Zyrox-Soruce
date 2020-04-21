package com.varrock.world.content.donation;

import com.varrock.util.Misc;
import com.varrock.util.Stopwatch;
import com.varrock.world.World;

/*
 * @author Aj
 * www.Simplicity-ps.com
 */

public class DonationYeller {
	
	
    private static final int TIME = 1454300; //20 minutes - 1454300
	private static Stopwatch timer = new Stopwatch().reset();
	public static String currentMessage;
	
	/*
	 * Random Message Data
	 */
	private static final String[][] MESSAGE_DATA = { 
			{"@blu@[STORE]</col> There are currently bonus donations and limited items in shop 3!"},
			{"@blu@[STORE]</col> There are currently bonus donations and limited items in shop 3!"},
	
		
	};

	/*
	 * Sequence called in world.java
	 * Handles the main method
	 * Grabs random message and announces it
	 */
	public static void sequence() {
		if(timer.elapsed(TIME)) {
			timer.reset();
			{
				
			currentMessage = MESSAGE_DATA[Misc.getRandom(MESSAGE_DATA.length - 1)][0];
			World.sendMessage(currentMessage);
					
					
				}
				
			new Thread(() -> { World.savePlayers(); }).start();
			}
		

          }
  }
