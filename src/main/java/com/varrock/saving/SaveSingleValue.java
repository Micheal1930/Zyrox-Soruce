package com.varrock.saving;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.varrock.world.entity.impl.player.*;

public abstract class SaveSingleValue extends SaveObject {

	/**
	 * 
	 * @param name
	 */
	public SaveSingleValue(String name) {
		super(name);
	}

	/**
	 * Gets the value for a player.
	 * @param player
	 * @return
	 */
	public abstract Object getValue(Player player);
	
	/**
	 * Gets the value from the ResultSet.
	 * @param columnName
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	public abstract Object getValue(String columnName, ResultSet rs) throws SQLException;
	 
	/**
	 * Sets a value for the player.
	 * @param player
	 * @param value
	 */
	public abstract void setSingleValue(Player player, Object value);
	
	/**
	 * 
	 * @param player
	 * @param columnName
	 * @param rs
	 * @throws SQLException
	 */
	public void loadValue(Player player, String columnName, ResultSet rs) throws SQLException {
		Object value = getValue(columnName,rs);
		setSingleValue(player,value);
	}
	
	@Override
	public abstract boolean save(Player player, BufferedWriter writer) throws IOException;

	@Override
	public abstract void load(Player player, String values, BufferedReader reader) throws IOException;
	

}
