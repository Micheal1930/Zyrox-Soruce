package com.zyrox.world.content.combat.effect;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.zyrox.engine.task.Task;
import com.zyrox.model.CombatIcon;
import com.zyrox.model.Hit;
import com.zyrox.model.Hitmask;
import com.zyrox.model.Item;
import com.zyrox.world.content.PlayerPunishment.Jail;
import com.zyrox.world.entity.impl.GameCharacter;
import com.zyrox.world.entity.impl.player.Player;

/**
 * A {@link Task} implementation that handles the venom process.
 * 
 * @author lare96
 */
public class CombatVenomEffect extends Task {

	/** The entity being inflicted with venom. */
	private GameCharacter entity;

	/**
	 * Create a new {@link CombatVenomEffect}.
	 * 
	 * @param entity
	 *            the entity being inflicted with venom.
	 */
	public CombatVenomEffect(GameCharacter entity) {
		super(33, entity, false);
		this.entity = entity;
	}

	/**
	 * Holds all of the different strengths of venom.
	 * 
	 * @author lare96
	 */
	public enum VenomType {
		MILD(130), EXTRA(150), SUPER(200);

		/** The starting damage for this venom type. */
		private int damage;

		/**
		 * Create a new {@link VenomType}.
		 * 
		 * @param damage
		 *            the starting damage for this venom type.
		 */
		private VenomType(int damage) {
			this.damage = damage;
		}

		/**
		 * Gets the starting damage for this venom type.
		 * 
		 * @return the starting damage for this venom type.
		 */
		public int getDamage() {
			return damage;
		}
	}

	@Override
	public void execute() {
		// Stop the task if the entity is unregistered.
		if (!entity.isRegistered() || !entity.isVenomed()) {
			this.stop();
			return;
		}
		if(entity.isPlayer()) {
			Player player = (Player) entity;
			if(Jail.isJailed(player)) {
				return;
			}
		}
		// Deal the damage, then try and decrement the damage count.
		entity.dealDamage(new Hit(entity.getAndDecrementVenomDamage(), Hitmask.DARK_GREEN, CombatIcon.NONE));
		/*
		 * if(entity.isPlayer()) {
		 * ((Player)entity).getPacketSender().sendInterfaceRemoval(); }
		 */
	}

	/**
	 * The small utility class that manages all of the combat venom data.
	 * 
	 * @author lare96
	 * @author Advocatus
	 */
	public static final class CombatVenomData {

		/** The map of all of the different weapons that venom. */
		// Increase the capacity of the map as more elements are added.
		private static final Map<Integer, VenomType> types = new HashMap<>(2);

		/** Load all of the venom data. */
		public static void init() {
			types.put(12926, VenomType.EXTRA);
			types.put(12927, VenomType.EXTRA);
			types.put(3065, VenomType.EXTRA);
			types.put(12284, VenomType.EXTRA);
			types.put(12282, VenomType.SUPER);
			types.put(12279, VenomType.SUPER);
			types.put(12278, VenomType.SUPER);
		}

		/**
		 * Gets the venom type of the specified item.
		 * 
		 * @param item
		 *            the item to get the venom type of.
		 * 
		 * @return the venom type of the specified item, or <code>null</code> if
		 *         the item is not able to venom the victim.
		 */
		public static Optional<VenomType> getVenomType(Item item) {
			if (item == null || item.getId() < 1 || item.getAmount() < 1)
				return Optional.empty();
			return Optional.ofNullable(types.get(item.getId()));
		}

		/** Default private constructor. */
		private CombatVenomData() {
		}
	}
}
