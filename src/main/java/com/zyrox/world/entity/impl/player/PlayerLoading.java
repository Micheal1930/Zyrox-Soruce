package com.zyrox.world.entity.impl.player;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.zyrox.model.Password;
import com.zyrox.net.login.LoginResponses;
import com.zyrox.saving.PlayerSaving;
import com.zyrox.world.content.skill.impl.construction.ConstructionSave;


public class PlayerLoading {
	
	public static String construction_save = "./data/saves/construction/";

	public static int getResult(Player player) {
		return getResult(player, false);
	}
	
	public static int getResult(Player player, boolean force) {
		if (player == null) {
			System.out.println("Player is null");
			return -1;
		} else if (player.getUsername() == null) {
			System.out.println("User player is null");
			return -1;
		}
		
		if (!PlayerSaving.getSaving().exists(player.getUsername())) {
			return LoginResponses.NEW_ACCOUNT;
		}
		
		if (true) {
			
			Password loaded = PlayerSaving.getSaving().getPassword(player.getName());
			
			if (force) {
				player.setPassword(loaded.getRealPassword());
				player.getPasswordNew().setRealPassword(loaded.getRealPassword());
			}
			
			try {

				Path path2 = Paths.get(construction_save+player.getUsername() + ".obj");
				File file2 = path2.toFile();

				if(file2.exists()) {
					FileInputStream fileIn = new FileInputStream(file2);
					ObjectInputStream in = new ObjectInputStream(fileIn);
					ConstructionSave save = (ConstructionSave) in.readObject();
					player.setHouseRooms(save.getHouseRooms());
					player.setHouseFurtinture(save.getHouseFurniture());
					player.setHousePortals(save.getHousePortals());
					in.close();
					fileIn.close();
				}
				
			} catch(Throwable t) {
				t.printStackTrace();
				return LoginResponses.LOGIN_COULD_NOT_COMPLETE;
			}
			
			if (!force && !player.getPassword().equals(loaded.getRealPassword())) {
				return LoginResponses.LOGIN_INVALID_CREDENTIALS;
			}
			
			try {
				PlayerSaving.getSaving().load(player);
			} catch(Exception e) {
				e.printStackTrace();
			}
			
			return LoginResponses.LOGIN_SUCCESSFUL;
		}
		return LoginResponses.LOGIN_SUCCESSFUL;
	}
	
}