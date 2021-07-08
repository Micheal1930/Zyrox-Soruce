package com.zyrox; // dont forget to change packaging ^-^

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.zyrox.world.content.dialogue.DialogueManager;
import com.zyrox.world.entity.impl.player.Player;


public class FoxVote implements Runnable {

	public static final String HOST = "199.33.112.227"; // Website Ip
	public static final String USER = "zyro8340_vote"; // Username
	public static final String PASS = "corymccain12"; // Password
	public static final String DATABASE = "zyro8340_vote"; // Database

	private Player player;
	private Connection conn;
	private Statement stmt;

	public FoxVote(Player player) {
		this.player = player;
	}

	@Override
	public void run() {
		try {
			if (!connect(HOST, DATABASE, USER, PASS)) {
				return;
			}

			String name = player.getUsername().replace(" ", "_");
			ResultSet rs = executeQuery("SELECT * FROM votes WHERE username='" + name + "' AND claimed=0 AND voted_on != -1");

			while (rs.next()) {
				String ipAddress = rs.getString("ip_address");
				int siteId = rs.getInt("site_id");

				player.getInventory().add(995, 1000000);
				player.getInventory().add(989, 1);
				player.getInventory().add(13077, 1);
				player.sendMessage("<img=678> <shad=786518>A voting casket has been added to your inventory.");
				//claimedAnything = true;

				// -- ADD CODE HERE TO GIVE TOKENS OR WHATEVER

				System.out.println("[Vote] Vote claimed by " + name + ". (sid: " + siteId + ", ip: " + ipAddress + ")");

				rs.updateInt("claimed", 1); // do not delete otherwise they can reclaim!
				rs.updateRow();
			}

			destroy();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String getUsername() {
		return null;
	}


	public boolean connect(String host, String database, String user, String pass) {
		try {
			this.conn = DriverManager.getConnection("jdbc:mysql://"+host+":3306/"+database, user, pass);
			return true;
		} catch (SQLException e) {
			System.out.println("Failing connecting to database!");
			return false;
		}
	}

	public void destroy() {
		try {
			conn.close();
			conn = null;
			if (stmt != null) {
				stmt.close();
				stmt = null;
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public int executeUpdate(String query) {
		try {
			this.stmt = this.conn.createStatement(1005, 1008);
			int results = stmt.executeUpdate(query);
			return results;
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return -1;
	}

	public ResultSet executeQuery(String query) {
		try {
			this.stmt = this.conn.createStatement(1005, 1008);
			ResultSet results = stmt.executeQuery(query);
			return results;
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return null;
	}

}