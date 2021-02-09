package com.zyrox.world.entity.impl.player;

import java.io.File;

public class PrintPoints {
	public static void main(String[] args) {

	
		/*
		 * Loading character files
		 */
		for (File file : new File("data/saves/characters/").listFiles()) {
			Player player = new Player(null);
			player.setUsername(file.getName().substring(0, file.getName().length()-5));
		
			PlayerLoading.getResult(player, true); //comment out line 78-81 in playerloading.java for this
			
			/*
			 * Printing:
			 * 
			 * 	if (reader.has("username")) {
				player.setUsername(reader.get("username").getAsString());
				System.out.println(" - Name: " +reader.get("username").getAsString());
			}

			if (reader.has("donation-points")) {
				player.getPointsHandler().setDonationPoints(reader.get("donation-points").getAsInt(), false);
				System.out.print(reader.get("donation-points").getAsInt());
				
			 */
			
		
			}
			System.out.println("Print out ran successfully");
		}
	}

