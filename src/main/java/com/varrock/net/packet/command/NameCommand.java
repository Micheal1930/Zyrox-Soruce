package com.varrock.net.packet.command;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public abstract class NameCommand extends Command {

	/**
	 * A set that contains the names of the players that are able to execute
	 * this command.
	 */
	private Set<String> validNames;

	/**
	 * Constructs a new {@link NameCommand}.
	 * 
	 * @param names
	 *            The names.
	 */
	public NameCommand(String... names) {
		validNames = new HashSet<String>(Arrays.asList(names));
	}

	/**
	 * Constructs a new {@link NameCommand}.
	 * 
	 * @param names
	 *            The names.
	 */
	public NameCommand(Set<String> names) {
		validNames = names;
	}
	
	/**
	 * Constructs a new {@link NameCommand}.
	 * 
	 * @param names
	 *            The set of names.
	 */
	@SafeVarargs
	public NameCommand(Set<String>...names) {
		validNames = new HashSet<String>();
		Arrays.asList(names).forEach(n -> validNames.addAll(n));
	}

	/**
	 * Gets the valid names.
	 * 
	 * @return The names.
	 */
	public Set<String> getValidNames() {
		return validNames;
	}

}
