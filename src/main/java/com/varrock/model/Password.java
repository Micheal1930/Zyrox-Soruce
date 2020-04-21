package com.varrock.model;

import com.varrock.Server;
import com.varrock.util.PasswordEncryption;

public class Password {
	
	private String encryptedPass;
	
	private String salt;

	private String realPassword;
	
	private long lastChange = 0;
	
	private String tempPassword;
	
	public void setLastChange(long time) {
		lastChange = time;
	}
	
	public long getLastChange() {
		if(lastChange == 0)
			lastChange = System.currentTimeMillis();
		return lastChange;
	}
	
	public String getRealPassword() {
		return realPassword;
	}

	public String getEncryptedPass() {
		return encryptedPass;
	}
	
	public String getSalt() {
		return salt;
	}
	
	public void setRealPassword(String password) {
		this.realPassword = password;
		if (this.salt != null) {
			String encrypted = Password.encryptPassword(realPassword, salt);
			setEncryptedPass(encrypted);
		}
	}
	
	public void setSalt(String salt) {
		String encrypted = Password.encryptPassword(this.getRealPassword(), salt);
		this.setEncryptedPass(encrypted);
		this.salt = salt;
	}
	
	public void setEncryptedPass(String encrypted) {
		this.encryptedPass = encrypted;
	}

	public Password(String password, String salt) {
		setRealPassword(password);
		this.salt = salt;
	}
	
	public Password(String password) {
		this(password,null);
	}
	
	public Password() {
		this(null,null);
	}
	
	public static String encryptPassword(String password, String salt) {
		return PasswordEncryption.sha1(password + salt);
	}


	public String getTempPassword() {
		return tempPassword;
	}


	public void setTempPassword(String tempPassword) {
		this.tempPassword = tempPassword;
	}
	
	public String toString() {
		return "[real]:" + this.realPassword + ",[salt]:" + salt + ",[enc]:" + this.encryptedPass;
	}

}
