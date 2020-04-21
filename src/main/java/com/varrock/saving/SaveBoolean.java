package com.varrock.saving;



import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.varrock.world.entity.impl.player.*;


public abstract class SaveBoolean extends SaveSingleValue {

	/**
	 * Constructs a new SaveBoolean.
	 *
	 * @param name
	 */
	public SaveBoolean(String name) {
		super(name);
	}

	@Override
	public void setSingleValue(Player player, Object value) {
		setValue(player, (Boolean) value);
	}
	
	/**
	 * Sets the value for the specified Player.
	 *
	 * @param player
	 * @param value
	 */
	public abstract void setValue(Player player, boolean value);

	/**
	 * Gets the value of the Player.
	 *
	 * @param player
	 * @return the value
	 */
    @Override
    public abstract Boolean getValue(Player player);


	public abstract boolean getDefaultValue();

	@Override
	public boolean save(Player player, BufferedWriter writer) throws IOException {
        boolean value = getValue(player);
		//if(value != getDefaultValue()) { 
		writer.write(getName() + "=" + value);
		return true;
		//} 
		//return false;
	}


	@Override
	public void load(Player player, String values, BufferedReader reader) throws IOException {
		boolean value = Boolean.parseBoolean(values);
		setValue(player, value);
	}
	
	
	@Override
	public Boolean getValue(String columnName, ResultSet rs) throws SQLException {
		return rs.getBoolean(columnName);
	}

}
