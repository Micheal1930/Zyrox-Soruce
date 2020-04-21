package com.varrock.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class PasswordEncryption {

    public static String generateSalt() {
    	try {
	        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
	        byte[] salt = new byte[16];
	        sr.nextBytes(salt);
	        return Base64.getEncoder().encodeToString(salt).replace("=", "");
    	} catch(Exception e) {
    		e.printStackTrace();
    		return "gXt4XA4xT08Zy9DUB8Y0Ug";
    	}
    }
    
    public static String sha1(String str) {
    	String sha = null;
    	try {
	        MessageDigest md = MessageDigest.getInstance("SHA-1");
	        md.reset();
	        byte[] bytes = md.digest(str.getBytes());
	        sha = new BigInteger(1, bytes).toString(16);
	    } catch (NoSuchAlgorithmException e) {
	        e.printStackTrace();
	    }
    	return sha;
    }

}
