package com.varrock.saving;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.varrock.world.entity.impl.player.Player;


/**
 * @author Arsen Maxyutov.
 */
public abstract class SaveString extends SaveSingleValue {

	/**
	 * Constructs a new SaveInteger.
	 *
	 * @param name
	 */
	public SaveString(String name) {
		super(name);
	}

	@Override
	public boolean save(Player player, BufferedWriter writer) throws IOException {
		String value = getValue(player);
		writer.write(getName() + "=" + value);
		return true;
	}

	@Override
	public void load(Player player, String value, BufferedReader reader) {
		setValue(player, value);
	}
	
	@Override
	public String getValue(String columnName, ResultSet rs) throws SQLException {
		return rs.getString(columnName);
	}

	@Override
	public void setSingleValue(Player player, Object value) {
		try {
			if(value == null)
				return;
			String strValue = (String) value;
			if(strValue.length() == 0)
				return;
		setValue(player, (String) value);
		} catch(Exception e) {
			System.out.println(player.getName() + " - Trting to set: " + value + " for : " + this.getName());
			e.printStackTrace();
			
		}
	}
	
	/**
	 * Sets the value for the specified Player.
	 *
	 * @param player
	 * @param value
	 */
	public abstract void setValue(Player player, String value);

	/**
	 * Gets the value of the Player.
	 *
	 * @param player
	 * @return the value
	 */
    @Override
    public abstract String getValue(Player player);

}
