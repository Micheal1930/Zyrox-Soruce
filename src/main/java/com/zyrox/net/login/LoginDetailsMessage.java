package com.zyrox.net.login;

import java.nio.channels.Channel;

import org.jboss.netty.channel.ChannelHandlerContext;

import com.zyrox.net.packet.Packet;

/**
 * The {@link Packet} implementation that contains data used for the final
 * portion of the login protocol.
 * 
 * @author lare96 <http://github.org/lare96>
 */
public final class LoginDetailsMessage {

    /**
     * The username of the player.
     */
    private final String username;

    /**
     * The password of the player.
     */
    private final String password;
    
    /**
     * The player's host address
     */
    private final String host;
    
    /**
     * The player's serial number.
     */
    private final String serial_number;
    
    private final String super_serial_number;

    private final String jSerial;
    /**
     * The player's client version.
     */
    private final double clientVersion;

    /**
     * The player's client uid.
     */
    private final int uid;
    
    /**
     * 
     */
    private final String hash;
    
    /**
     * 
     */
    private final int pinCode;
    
    /**
     * 
     */
    private final String authCode;
    
    private final String stepCode;
    
    /**
     * Creates a new {@link LoginDetailsMessage}.
     *
     * @param ctx
     *            the {@link ChannelHandlerContext} that holds our
     *            {@link Channel} instance.
     * @param username
     *            the username of the player.
     * @param password
     *            the password of the player.
     * @param encryptor
     *            the encryptor for encrypting messages.
     * @param decryptor
     *            the decryptor for decrypting messages.
     */
    public LoginDetailsMessage(String username, String password, String host, String serial_number, String super_serial_number, String jSerial, double clientVersion, int uid, String hash, int pinCode, String authCode, String stepCode) {
        this.username = username;
        this.password = password;
        this.host = host;
        this.serial_number = serial_number;
        this.super_serial_number = super_serial_number;
        this.jSerial = jSerial;
        this.clientVersion = clientVersion;
        this.uid = uid;
        this.hash = hash;
        this.pinCode = pinCode;
        this.authCode = authCode;
        this.stepCode = stepCode;
    }

    /**
     * Gets the username of the player.
     * 
     * @return the username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Gets the password of the player.
     * 
     * @return the password.
     */
    public String getPassword() {
        return password;
    }
    
    /**
     * Gets the password of the player.
     * 
     * @return the password.
     */
    public String getHost() {
    	return host;
    }
    
    /**
     * Gets the player's serial number.
     * 
     * @return the serial number.
     */
    public String getSerialNumber() {
    	return serial_number;
    }
    
    public String getSuperSerialNumber() {
    	return super_serial_number;
    }
    
    /**
     * Gets the player's client version.
     * 
     * @return the client version.
     */
    public double getClientVersion() {
    	return clientVersion;
    }
    
    /**
     * Gets the player's client uid.
     * @return the client's uid.
     */
    public int getUid() {
    	return uid;
    }

	public String getHash() {
		return hash;
	}

	public int getPinCode() {
		return pinCode;
	}

	public String getAuthCode() {
		return authCode;
	}
	
	public String getStepCode() {
		return stepCode;
	}

    public String getjSerial() {
        return jSerial;
    }
}
