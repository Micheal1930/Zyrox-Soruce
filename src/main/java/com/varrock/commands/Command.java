package com.varrock.commands;

import com.varrock.world.entity.impl.player.Player;

/**
 * @author Jack Daniels.
 */
public abstract class Command {

	/**
	 * The type of minimum rights. TESTING FOR CHANGES SAVED
	 */
	public static final int
			NORMAL_RIGHTS = 0,
			DONATOR_RIGHTS = 1,
			HELPER_RIGHTS = 2,
			MOD_RIGHTS = 3,
			ADMIN_RIGHTS = 4,
			OWNER_RIGHTS = 5;

	/**
	 * Name of the command which would be used as ::startsWith.
	 */
	private final String key;
	
	private final String prefix;


	/**
	 * The rights.
	 */
	private final int rights;

	/**
	 * Flag indicating whether the command should be logged/saved/recorded.
	 */
	private boolean recorded;


	/**
	 * Executes the command
	 *
	 * @param player
	 * @param input
	 * @return true if successful, false if not
	 * @throws Exception
	 */
	public abstract boolean execute(Player player, String input) throws Exception;

	/**
	 * Gets the rights of the command.
	 *
	 * @return the rights
	 */
	public int getRights() {
		return rights;
	}

	/**
	 * @return the command key
	 */
	public String getKey() {
		return key;
	}

	/**
	 * @return whether the command should be recorded/saved/logged.
	 */
	public boolean isRecorded() {
		return recorded;
	}
	
	/**
	 * Creates a new command, this command can be added to the server
	 * by submitting this command in the CommandHandler class.
	 *
	 * @param key
	 * @param rights
	 * @param recorded
	 */
	public Command(String key, int rights, boolean recorded, String prefix) {
		this.key = key.toLowerCase();
		this.rights = rights;
		this.recorded = recorded;
		this.prefix = prefix;
	}

	
	/**
	 * Creates a new command, this command can be added to the server
	 * by submitting this command in the CommandHandler class.
	 *
	 * @param key
	 * @param rights
	 * @param recorded
	 */
	public Command(String key, int rights, boolean recorded) {
		this(key, rights, rights >= MOD_RIGHTS && rights != OWNER_RIGHTS, "");
	}
	
	/**
	 * Creates a new command, this command can be added to the server
	 * by submitting this command in the CommandHandler class.
	 *
	 * @param key
	 * @param rights
	 */
	public Command(String key, int rights, String prefix) {
		this(key, rights, rights >= MOD_RIGHTS && rights != OWNER_RIGHTS, prefix);
	}
	
	/**
	 * Creates a new command, this command can be added to the server
	 * by submitting this command in the CommandHandler class.
	 *
	 * @param key
	 * @param rights
	 */
	public Command(String key, int rights) {
		this(key, rights, rights >= MOD_RIGHTS && rights != OWNER_RIGHTS);

	}

	/**
	 * Removes the command name with space from the input
	 * e.g. converts "update 30" to "30"
	 *
	 * @param input
	 * @returns the actual input of the command, in lower case
	 */
	public String filterInput(String input) {
		return filterInput(input,key);
	}
	
	public static String filterInput(String input, String key) {
		return input.replace(key + " ", "").toLowerCase();
	}

	/**
	 * Removes the command name with space from the input
	 * and splits the remaining String into integer parts
	 * e.g. converts "update 30" to {30}
	 *
	 * @param input
	 * @returns Integer Array of the input
	 */
	public int[] getIntArray(String input) {
		input = filterInput(input);
		String[] parts = input.split(" ");
		int[] intArray = new int[parts.length];
		for (int i = 0; i < intArray.length; i++) {
			intArray[i] = Integer.parseInt(parts[i]);
		}
		return intArray;
	}
	
	public void sendMessage(Player player, String message)
	{
		player.sendMessage(prefix + message);
	}
	
	public String getDescription() {
		return "none";
	}
}
