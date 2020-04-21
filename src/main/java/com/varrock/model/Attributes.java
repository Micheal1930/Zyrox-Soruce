package com.varrock.model;

import java.util.HashMap;
import java.util.Map;

/**
 * An entities attributes.
 * 
 * @author Blake
 *
 */
public class Attributes {

	/**
	 * A map of all the attributes.
	 */
	private Map<String, Object> attributes = new HashMap<String, Object>();

	/**
	 * Puts an element in the attributes map.
	 *
	 * @param key
	 * @param value
	 */
	public void put(String key, Object value) {
		attributes.put(key, value);
	}

	/**
	 * Checks if the specified key exists in the attributes map.
	 */
	public boolean exists(String key) {
		return attributes.containsKey(key);
	}

	/**
	 * Removes the element from the attributes map.
	 *
	 * @param key
	 */
	public void remove(String key) {
		attributes.remove(key);
	}

	/**
	 * Gets a string from the attributes map.
	 *
	 * @param key
	 * @return the string value
	 */
	public String getString(String key) {
		Object value = attributes.get(key);

		if (value == null) {
			return null;
		}

		return (String) value;
	}

	/**
	 * Gets the integer value from the attributes map, if the value is not found
	 * 0 is returned.
	 *
	 * @param key
	 * @return the int value
	 */
	public int getInt(String key) {
		Object value = attributes.get(key);

		if (value == null) {
			return 0;
		}

		return (Integer) value;
	}

	/**
	 * Gets the long value from the attributes map, if the value is not found 0
	 * is returned.
	 *
	 * @param key
	 * @return the long value
	 */
	public long getLong(String key) {
		Object value = attributes.get(key);

		if (value == null) {
			return 0;
		}

		return (Long) value;
	}

	/**
	 * Gets the boolean from the attributes map, if the value is not found
	 * <code>false</code> is returned.
	 *
	 * @param key
	 * @return the boolean value
	 */
	public boolean getBoolean(String key) {
		Object value = attributes.get(key);

		if (value == null) {
			return false;
		}

		return (Boolean) value;
	}

	/**
	 * Gets the Object value from the attributes map.
	 *
	 * @param key
	 * @return the value
	 */
	public Object get(String key) {
		return attributes.get(key);
	}

	/**
	 * Clears the attributes.
	 */
	public void clear() {
		attributes.clear();
	}
}
