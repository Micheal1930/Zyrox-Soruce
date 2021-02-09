package com.zyrox.net.security;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zyrox.GameServer;
import com.zyrox.GameSettings;
import com.zyrox.model.punish.PunishmentType;
import com.zyrox.net.login.LoginDetailsMessage;
import com.zyrox.net.login.LoginResponses;
import com.zyrox.util.Misc;
import com.zyrox.world.entity.impl.player.Player;

/**
 * A lot of connection-related stuff.
 * Really messy.
 */

public class ConnectionHandler {
		
	public static void init() {
		loadHostBlacklist();
		loadBannedComputers();
	}
	
	public static int getResponse(Player player, LoginDetailsMessage msg) {

		if (!GameSettings.SPECIAL_STAFF_NAMES.contains(player.getUsername())) {

			if(GameServer.punishmentManager.isPunished(Misc.formatPlayerName(player.getName()), PunishmentType.BANNED)) {
				return LoginResponses.LOGIN_DISABLED_ACCOUNT;
			}

			if(GameServer.punishmentManager.isPunished(Misc.formatPlayerName(player.getHostAddress()), PunishmentType.IP_BANNED)) {
				return LoginResponses.LOGIN_DISABLED_IP;
			}

			/*if(GameServer.punishmentManager.isPunished(Misc.formatPlayerName(player.getHostAddress()), PunishmentType.MAC_BANNED)) {
				return LoginResponses.LOGIN_DISABLED_COMPUTER;
			}*/

			if(GameServer.punishmentManager.isPunished(Misc.formatPlayerName(player.getHostAddress()), PunishmentType.J_BANNED)) {
				return LoginResponses.LOGIN_DISABLED_COMPUTER;
			}
		}

		return LoginResponses.LOGIN_SUCCESSFUL;
	}

	/** BLACKLISTED CONNECTIONS SUCH AS PROXIES **/
	private static final String BLACKLIST_DIR = "./data/saves/blockedhosts.txt";
	private static List<String> BLACKLISTED_HOSTNAMES = new ArrayList<String>();
	
	/** BLACKLISTED HARDWARE NUMBERS **/
	private static final String BLACKLISTED_SERIAL_NUMBERS_DIR = "./data/saves/blockedhardwares.txt";
	private static List<String> BLACKLISTED_SERIAL_NUMBERS = new ArrayList<>();
	
	/**
	 * The concurrent map of registered connections.
	 */
	private static final Map<String, Integer> CONNECTIONS = Collections.synchronizedMap(new HashMap<String, Integer>());
	
	private static void loadHostBlacklist() {
		String word = null;
		try {
			BufferedReader in = new BufferedReader(
					new FileReader(BLACKLIST_DIR));
			while ((word = in.readLine()) != null)
				BLACKLISTED_HOSTNAMES.add(word.toLowerCase());
			in.close();
			in = null;
		} catch (final Exception e) {
			System.out.println("Could not load blacklisted hosts.");
		}
	}

	public static boolean isBlocked(String host) {
		return BLACKLISTED_HOSTNAMES.contains(host.toLowerCase());
	}

	
	private static void loadBannedComputers() {
		String line = null;
		try {
			BufferedReader in = new BufferedReader(
					new FileReader(BLACKLISTED_SERIAL_NUMBERS_DIR));
			while ((line = in.readLine()) != null) {
				if(line.contains("="))
					BLACKLISTED_SERIAL_NUMBERS.add(line.substring(line.indexOf("=")+1));
			}
			in.close();
			in = null;
		} catch (final Exception e) {
			System.out.println("Could not load blacklisted hadware numbers.");
		}
	}
	/*private static void loadBannedComputers() {
		String line = null;
		try {
			BufferedReader in = new BufferedReader(
					new FileReader(BLACKLISTED_SERIAL_NUMBERS_DIR));
			while ((line = in.readLine()) != null) {
				if(line.contains("="))
					BLACKLISTED_SERIAL_NUMBERS.add(String.valueOf(line.substring(line.indexOf("=")+1)));
			}
			in.close();
			in = null;
		} catch (final Exception e) {
			e.printStackTrace();
			System.out.println("Could not load blacklisted hadware numbers.");
		}
	}*/

	/*public static void banComputer(String playername, String serial_number) {
		if(BLACKLISTED_SERIAL_NUMBERS.contains(serial_number))
			return;
		BLACKLISTED_SERIAL_NUMBERS.add(serial_number);
		GameServer.getLoader().getEngine().submit(() -> {
			try {
				BufferedWriter writer = new BufferedWriter(new FileWriter(BLACKLISTED_SERIAL_NUMBERS_DIR, true));
				writer.write(""+playername+"="+serial_number);
				writer.newLine();
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}*/
	public static void banComputer(String playername, String mac) {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(BLACKLISTED_SERIAL_NUMBERS_DIR, true));
			writer.write(""+playername+"="+mac);
			writer.newLine();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(!BLACKLISTED_SERIAL_NUMBERS.contains(mac))
			BLACKLISTED_SERIAL_NUMBERS.add(mac);
	}
	
	public static void reloadUUIDBans() {
		BLACKLISTED_SERIAL_NUMBERS.clear();
		loadBannedComputers();
	}

	public static boolean isBlockedSerial(String host) {
		return BLACKLISTED_SERIAL_NUMBERS.contains(host);
	}

	public static void add(String host) {
		if(!isLocal(host)) {
			if(CONNECTIONS.get(host) == null) {
				CONNECTIONS.put(host, 1);
			} else {
				int amt = CONNECTIONS.get(host) + 1;
				CONNECTIONS.put(host, amt);
			}
		}
	}

	public static void remove(String host) {
		if(!isLocal(host)) {
			if(CONNECTIONS.get(host) != null) {
				int amt = CONNECTIONS.get(host) - 1;
				if(amt == 0) {
					CONNECTIONS.remove(host);
				} else {
					CONNECTIONS.put(host, amt);
				}
			}
		}
	}


	/**
	 * Determines if the specified host is connecting locally.
	 *
	 * @param host
	 *            the host to check if connecting locally.
	 * @return {@code true} if the host is connecting locally, {@code false}
	 *         otherwise.
	 */
	public static boolean isLocal(String host) {
		return host == null || host.equals("null") || host.equals("127.0.0.1") || host.equals("localhost");
	}
}
