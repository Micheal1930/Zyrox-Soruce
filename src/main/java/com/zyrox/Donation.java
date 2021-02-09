package com.zyrox;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.zyrox.world.content.dialogue.DialogueManager;
import com.zyrox.world.entity.impl.player.Player;



/**
 * Using this class:
 * To call this class, it's best to make a new thread. You can do it below like so:
 * new Thread(new Donation(player)).start();
 */
public class Donation implements Runnable {

	public static final String HOST = "localhost";
	public static final String USER = "varrnzgh_swag";
	public static final String PASS = "BPH7UnjGuC19";
	public static final String DATABASE = "varrnzgh_store";

	private Player player;
	private Connection conn;
	private Statement stmt;

	/**
	 * The constructor
	 * @param player
	 */
	public Donation(Player player) {
		this.player = player;
	}

	@Override
	public void run() {
		try {
			if (!connect(HOST, DATABASE, USER, PASS)) {
				return;
			}

			String name = player.getUsername().replace("_", " ");
			ResultSet rs = executeQuery("SELECT * FROM payments WHERE player_name='"+name+"' AND status='Completed' AND claimed=0");
			boolean claimedAnything = false;
			while (rs.next()) {
				int item_number = rs.getInt("item_number");
				double paid = rs.getDouble("amount");
				int quantity = rs.getInt("quantity");
				switch (item_number) {// add products according to their ID in the ACP

				case 18: // example
					player.getPointsHandler().setDonationPoints(10, true);
					claimedAnything = true;
					break;
				case 19:
					player.getPointsHandler().setDonationPoints(20, true);
					claimedAnything = true;
					break;
				case 20:
					player.getPointsHandler().setDonationPoints(25, true);
					claimedAnything = true;
					break;
				case 21:
					player.getPointsHandler().setDonationPoints(35, true);
					claimedAnything = true;
					break;
				case 22:
					player.getPointsHandler().setDonationPoints(45, true);
					claimedAnything = true;
					break;
				case 23:
					player.getPointsHandler().setDonationPoints(50, true);
					claimedAnything = true;
					break;
				case 24:
					player.getPointsHandler().setDonationPoints(60, true);
					claimedAnything = true;
					break;
				case 25:
					player.getPointsHandler().setDonationPoints(65, true);
					claimedAnything = true;
					break;
				case 26:
					player.getPointsHandler().setDonationPoints(75, true);
					claimedAnything = true;
					break;
				case 27:
					player.getPointsHandler().setDonationPoints(100, true);
					claimedAnything = true;
					break;
				case 28:
					player.getPointsHandler().setDonationPoints(150, true);
					claimedAnything = true;
					break;
				case 29:
					player.getPointsHandler().setDonationPoints(250, true);
					claimedAnything = true;
					break;
				}

				rs.updateInt("claimed", 1); // do not delete otherwise they can reclaim!
				rs.updateRow();
			}
			if(claimedAnything) {
				DialogueManager.sendStatement(player, "Thank you for your donation! Your items are in your bank.");
			} else {
				DialogueManager.sendStatement(player, "No donations have been found.");
			}
			destroy();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 *
	 * @param host the host ip address or url
	 * @param database the name of the database
	 * @param user the user attached to the database
	 * @param pass the users password
	 * @return true if connected
	 */
	public boolean connect(String host, String database, String user, String pass) {
		try {
			this.conn = DriverManager.getConnection("jdbc:mysql://"+host+":3306/"+database, user, pass);
			return true;
		} catch (SQLException e) {
			System.out.println("Failing connecting to database!");
			return false;
		}
	}

	/**
	 * Disconnects from the MySQL server and destroy the connection
	 * and statement instances
	 */
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

	/**
	 * Executes an update query on the database
	 * @param query
	 * @see {@link Statement#executeUpdate}
	 */
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

	/**
	 * Executres a query on the database
	 * @param query
	 * @see {@link Statement#executeQuery(String)}
	 * @return the results, never null
	 */
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
