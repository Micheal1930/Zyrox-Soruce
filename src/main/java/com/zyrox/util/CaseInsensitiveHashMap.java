package com.zyrox.util;

import java.util.HashMap;

/**
 * A class representing a hash table holding case insensitive key-value pairs.
 * @author Nick Hartskeerl <apachenick@hotmail.com>
 *
 * @param <V> The value type.
 */
public class CaseInsensitiveHashMap<V> extends HashMap<String, V> {

	/**
	 * The serial version UID.
	 */
	private static final long serialVersionUID = 4406685010914080000L;
	
	/**
	 * Returns the value to which the specified case insensitive key is mapped, or null if this map contains no mapping for the case insensitive key.
	 * More formally, if this map contains a mapping from a key k to a value v, then this method returns v; otherwise it returns null. 
	 * A return value of null does not necessarily indicate that the map contains no mapping for the case insensitive key; it's also possible that the map explicitly maps the key to {@code null}. 
	 * The {@link #containsKey()} operation may be used to distinguish these two cases.
	 * @param key The key.
	 * @return The value in the mapping for this key.
	 */
	public V get(String key) {
		return super.get(key.toLowerCase());
	}
	
	/**
	 * Associates the specified value with the specified case insensitive key in this map.
	 * If the map previously contained a mapping for the key, the old value is replaced.
	 * @return The previous value associated with key, or {@code null} if there was no mapping for key.
	 * (A {@code null} return can also indicate that the map previously associated null with key.)
	 */
	public V put(String key, V value) {
		return super.put(key.toLowerCase(), value);
	}
	
	/**
	 * Returns true if this map contains a mapping for the specified key.
	 * @param key The case insensitive key whose presence in this map is to be tested
	 * @return If this map contains a mapping for the specified key {@code true}.
	 */
	public boolean containsKey(String key) {
		return super.containsKey(key.toLowerCase());
	}
	
}
