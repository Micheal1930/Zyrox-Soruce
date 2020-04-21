package com.varrock;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import com.varrock.world.entity.impl.player.Player;


 
public class Highscores implements Runnable {

	public static final String HOST = "localhost";
	public static final String USER = "varrnzgh_swag";
	public static final String PASS = "BPH7UnjGuC19";
	public static final String DATABASE = "varrnzgh_hs";

	public static final String TABLE = "hs_users";
	
	private Player player;
	private Connection conn;
	private Statement stmt;
	
	public Highscores(Player player) {
		this.player = player;
	}
	
	public boolean connect(String host, String database, String user, String pass) {
		try {
			this.conn = DriverManager.getConnection("jdbc:mysql://"+host+":3306/"+database, user, pass);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	@Override
	public void run() {
		try {
			if (!connect(HOST, DATABASE, USER, PASS)) {
				return;
			}
			
			String name = player.getUsername();
			
			PreparedStatement stmt1 = prepare("DELETE FROM "+TABLE+" WHERE username=?");
			stmt1.setString(1, player.getUsername());
			stmt1.execute();
				
			PreparedStatement stmt2 = prepare(generateQuery());
			stmt2.setString(1, player.getUsername());
			stmt2.setInt(2, 0);
			/*
			 *  case NORMAL:
                return 0;
            case ULTIMATE_IRONMAN:
                return 1;
            case HARDCORE_IRONMAN:
                return 3;
            case IRONMAN:
                return 2;
			 */
			if(player.getGameMode().getId() == 1) {
				stmt2.setInt(3, 2);
			}else if(player.getGameMode().getId() == 2) {
				stmt2.setInt(3, 1);
			}else if(player.getGameMode().getId() == 3) {
				stmt2.setInt(3, 1);
			}else
				stmt2.setInt(3, 0);
			 // not sure how it is set on website with id for what type so need to test it .... how we test
			stmt2.setInt(4, player.getSkillManager().getTotalLevel()); // total level

			stmt2.setLong(5, player.getSkillManager().getTotalExp());
			
			for (int i = 0; i < 25; i++)
				stmt2.setInt(6 + i, player.getSkillManager().getExperience(i));//player.getSkills().getXp()[i]
			stmt2.execute();
			
			destroy();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public PreparedStatement prepare(String query) throws SQLException {
		return conn.prepareStatement(query);
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
	
	public static String generateQuery() {
		StringBuilder sb = new StringBuilder();
		sb.append("INSERT INTO "+TABLE+" (");
		sb.append("username, ");
		sb.append("rights, ");
		sb.append("mode, ");
		sb.append("total_level, ");
		sb.append("overall_xp, ");
		sb.append("attack_xp, ");
		sb.append("defence_xp, ");
		sb.append("strength_xp, ");
		sb.append("constitution_xp, ");
		sb.append("ranged_xp, ");
		sb.append("prayer_xp, ");
		sb.append("magic_xp, ");
		sb.append("cooking_xp, ");
		sb.append("woodcutting_xp, ");
		sb.append("fletching_xp, ");
		sb.append("fishing_xp, ");
		sb.append("firemaking_xp, ");
		sb.append("crafting_xp, ");
		sb.append("smithing_xp, ");
		sb.append("mining_xp, ");
		sb.append("herblore_xp, ");
		sb.append("agility_xp, ");
		sb.append("thieving_xp, ");
		sb.append("slayer_xp, ");
		sb.append("farming_xp, ");
		sb.append("runecrafting_xp, ");
		sb.append("hunter_xp, ");
		sb.append("construction_xp, ");
		sb.append("summoning_xp, ");
		sb.append("dungeoneering_xp) ");
		sb.append("VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
		return sb.toString();
	}
	
}