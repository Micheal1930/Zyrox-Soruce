package com.zyrox.world.content;

import com.zyrox.util.Misc;
import com.zyrox.util.Stopwatch;
import com.zyrox.world.World;

/*
 * @author Aj
 * www.Simplicity-ps.com
 */

public class SecurityYeller {
	
	
    private static final int TIME = 1454300; //20 minutes - 1454300
	private static Stopwatch timer = new Stopwatch().reset();
	public static String currentMessage;
	
	/*
	 * Random Message Data
	 */
	private static final String[][] MESSAGE_DATA = { 
			//{" Use ::2fa to fully secure your account from hackings!"},
			//{" It is always a good idea to have a bank pin! Set one now with the town crier!"},
			//{" Use ::2fa to fully secure your account from hackings!"},
			{"Never use the same password from an old server!"},
			{"Keep your computer safe by using an antivirus software."}
		
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
			World.sendGlobalMessage("Security", currentMessage);
					
					
				}
				
			new Thread(() -> { World.savePlayers(); }).start();
			}
		

          }
  }
