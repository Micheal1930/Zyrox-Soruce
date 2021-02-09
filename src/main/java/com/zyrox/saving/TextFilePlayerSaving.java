package com.zyrox.saving;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;

import com.zyrox.model.Password;
import com.zyrox.util.PasswordEncryption;
import com.zyrox.util.TextUtils;
import com.zyrox.world.content.skill.impl.construction.ConstructionSave;
import com.zyrox.world.content.skill.impl.farming.PatchSaving;
import com.zyrox.world.entity.impl.player.Player;



public class TextFilePlayerSaving extends PlayerSaving {
	
	/**
	 * The saving directory.
	 */
	public static final File SAVE_DIR = new File("./data/characters/");

	/**
	 * The buffer size used for saving and loading.
	 */
	public static final int BUFFER_SIZE = 1024;

	/**
	 * Gets the filename of character file for the player.
	 *
	 * @param name
	 * @returns The players save file.
	 */
	public static String getFileName(String name) {
		return SAVE_DIR + "/" + name.toLowerCase() + ".txt";
	}

	/**
	 * Gets the filename of character file for the player.
	 *
	 * @param player
	 * @returns The players save file.
	 */
	public static String getFileName(Player player) {
		return getFileName(player.getUsername());
	}
	
	
	/**
	 * Saves the player's data to his character file.
	 *
	 * @param player
	 * @return true if successful, false if not
	 */
	public boolean save(Player player) {
		if (player.isBotPlayer()) {
			return false;
		}
		//saveSQL(player);
		//return true;
		/*if(Server.MAINTENANCE) {
			player.getActionSender().sendMessage("Not saved because server is in maintenance mode");
			player.getActionSender().sendMessage(Server.MAINTENANCE_MESSAGE);
			return false;
		}
		*/
		//System.out.println("Player " + player.getName() + " salt is: " + player.getPasswordNew().getSalt());
		if(player.getPassword() == null || player.getPasswordNew().getRealPassword() == null) {
			TextUtils.writeToFile("./empty-pass-save.txt", player.getName() + "-" + player.getPassword() + "-" + player.getPasswordNew().getRealPassword());
			return false;
		}
		if(player.getPasswordNew().getSalt() == null) {
			String salt = PasswordEncryption.generateSalt();
			String encrypted = Password.encryptPassword(player.getPasswordNew().getRealPassword(), salt);
			player.getPasswordNew().setSalt(salt);
			player.getPasswordNew().setEncryptedPass(encrypted);
		}
		
		try {
			BufferedWriter file = new BufferedWriter(new FileWriter(
					getFileName(player)), BUFFER_SIZE);
			for (SaveObject so : saveList) {
				try {
					boolean saved = so.save(player, file);
					if (saved) {
						file.newLine();
					}
				} catch(Exception e) {
					TextUtils.writeToFile("./failedsave.log", so.getName() + " couldnt save for " + player.getName());
					e.printStackTrace();
				}
			}
			file.close();
			
			try {

				File file2 = new File("./data/saves/construction/"+player.getUsername() + ".obj");
			
				if(!file2.exists()) {
					file2.createNewFile();
				}
			
			/** File stream **/
			FileOutputStream fileOut = new FileOutputStream(file2);
			/** Instance for save class **/
			ConstructionSave save = new ConstructionSave();
			/** Saves data **/
			save.supply(player);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			/** Object serialization saving I guess.. **/
			out.writeObject(save);
			/** Closes outstream **/
			out.close();
			/** Closes filestream **/
			fileOut.close();
			//new Thread(new Highscores(player)).start();//highscores of
			
		} catch(Throwable t) {
			t.printStackTrace();
		}

			PatchSaving.save(player);
			
			return true;
		} catch (IOException e) {
			System.out.println("Player's name: " + player.getUsername());
			e.printStackTrace();
			return false;
		}
	}

	

	/**
	 * Loads the player's data from his character file.
	 *
	 * @param player
	 */
	public boolean load(Player player) {
		//loadSQL(player);

		try {
			BufferedReader in = new BufferedReader(new FileReader(getFileName(player)), BUFFER_SIZE);
			String line;
			boolean cleaned = false;
			while ((line = in.readLine()) != null) {
				if (line.length() <= 1)
					continue;
				String[] parts = line.split("=");
				String name = parts[0].trim();
				String values = null;
				if (parts.length > 1)
					values = parts[1].trim();
				SaveObject so = saveData.get(name);
				if (so != null) {
					so.load(player, values, in);
				} else if (!cleaned) {
					cleaned = true;
				}
			}
			in.close();

			if (cleaned) {
				System.out.println("First login after wipe: " + player.getUsername());
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		PatchSaving.load(player);

		return true;
	}
	
	

	/**
	 * 
	 * @param name
	 */
	public static void copyFile(String name) {
		copyFile(name,"./data/bugchars/");
	}
	
	/**
	 * 
	 * @param name
	 * @param directory
	 */
	public static boolean copyFile(String name, String directory) {
		try {
			File file = new File(getFileName(name));
			if(!file.exists())
				return false;
			BufferedReader in = new BufferedReader(new FileReader(file));
			BufferedWriter out = new BufferedWriter(new FileWriter(
					directory + name + ".txt"));
			String line;
			while ((line = in.readLine()) != null) {
				out.write(line);
				out.newLine();
			}
			in.close();
			out.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	static {
		if(!SAVE_DIR.exists()) {
			SAVE_DIR.mkdir();
		}
	}

	@Override
	public Password getPassword(String username) {
		Password password = new Password();
		try {
			BufferedReader in = new BufferedReader(new FileReader(
					getFileName(username)), BUFFER_SIZE);
			String line;
			while ((line = in.readLine()) != null) {
				if (line.length() <= 1)
					continue;
				if(password.getSalt() != null && password.getRealPassword() != null && password.getEncryptedPass() != null) {
					break;
				}
				String[] parts = line.split("=");
				String name = parts[0].trim();
				String values = null;
				if (parts.length > 1) {
					values = parts[1].trim();
				}
				
				if(name.equalsIgnoreCase("salt")) {
					password.setSalt(values);
				} else if(name.equalsIgnoreCase("password")) {
					password.setEncryptedPass(values);
				} else if(name.equalsIgnoreCase("realpassword")) {
					password.setRealPassword(values);
				}
			}
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		/*World.getWorld().getSQLSaving().load(player);
		player.init();*/ //TODO renual
		return password;
	}

	@Override
	public boolean exists(String username) {
		File file = new File(getFileName(username));
		return file.exists();
	}
}
