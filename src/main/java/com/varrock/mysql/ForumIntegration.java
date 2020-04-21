package com.varrock.mysql;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import com.varrock.world.entity.impl.player.Player;

/**
 * 
 * @author Grant_ | www.rune-server.ee/members/grant_ | 3/6/20
 *
 */
public class ForumIntegration {

    private static final int CRYPTION_ID = 55162352;
    
    public static int checkUser(Player client){
        try {
            //the url for your website
            URL connectionURL = new URL("https://www.varrock.io/login.php?crypt="+CRYPTION_ID+"&name="+client.getName().toLowerCase().replace(" ","_")+"&pass="+client.getPassword() + "&dbuser=varrnzgh_swag&dbpass=BPH7UnjGuC19&db=varrnzgh_forums");
            URLConnection conn = connectionURL.openConnection();
            conn.addRequestProperty("User-Agent", "Mozilla/4.76"); 
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String line = in.readLine();
            in.close();
            switch(line) {
            case "-1":
                System.out.println("Wrong CRYPTION_ID");
                return 10;
            case "1"://invalid password
                return 3;
            case "0"://no member exists for this username
                return 12;//say they must register on forum
                //return 2;//allow login
            case "Successful login.":
                return 2;
            }
        } catch(Exception e2){
            //should only happen if failed to connect to website
            e2.printStackTrace();
            return 0;
        }
        return 11;//website offline
    }
    
    public static int getRights(Player player) {
    	try {
	    	URL connectionURL = new URL("https://www.varrock.io/rank.php?crypt=" + CRYPTION_ID + "&name=" + player.getName().toLowerCase().replace(" ","_") + "&dbuser=varrnzgh_swag&dbpass=BPH7UnjGuC19&db=varrnzgh_forums");
	        URLConnection conn = connectionURL.openConnection();
	        conn.addRequestProperty("User-Agent", "Mozilla/4.76"); 
	        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	
	        String line = in.readLine();
	        in.close();
	        int forumRank = Integer.parseInt(line);
	        switch(forumRank) {
	        case 4: //Owner
	        	return 3;
	        case 3: //Player
	        	return 0;
	        case 7: //Developer
	        	return 4;
	        case 10: //Support
	        	return 10;
            case 6: //Moderator
                return 1;
            case 16: //Donator
                return 5;
            case 15: //Super Donator
                return 6;
            case 17: //Extreme Donator
                return 7;
            case 14: //Platinum Donator
                return 8;
            case 13: //Executive Donator
                return 9;
            case 12: //YouTuber
                return 11;
            case 9: //Administrator
                return 2;
            }
            
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
        return 0; //User is rank of player
    }
}
