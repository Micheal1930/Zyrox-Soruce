package com.varrock.net.login.captcha;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import com.github.cage.Cage;
import com.github.cage.GCage;
import com.varrock.util.RandomUtility;

/**
 * 
 * @author Nick Hartskeerl <apachenick@hotmail.com>
 *
 */
public class CaptchaManager {

	/**
	 * 
	 */
	private static final Map<String, Captcha> collection = new HashMap<>();

	/**
	 * 
	 * @param username
	 * @return
	 */
	public static Captcha create(String username) {
		
		Captcha captcha = getCollection().get(username = username.toLowerCase());
		
		if(captcha == null) {
			getCollection().put(username, captcha = new Captcha());
		}
		
		Cage cage = new GCage();
		String text = RandomUtility.randomString(4);
		BufferedImage image = cage.drawImage(text);
		
		captcha.setText(text);
		captcha.setImage(image);
		
		return captcha;
		
	}
	
	/**
	 * 
	 * @param username
	 * @return
	 */
	public static Captcha get(String username) {
		return getCollection().get(username.toLowerCase());
	}
	
	/**
	 * 
	 * @return
	 */
	public static Map<String, Captcha> getCollection() {
		return collection;
	}
	
}
