package com.zyrox.world.content.form.accuracy;

import com.zyrox.world.entity.impl.GameCharacter;

/**
 * An interface for an accuracy calculator, which determines whether
 * a {@link org.niobe.model.GameCharacter} will land a hit on another
 * {@link org.niobe.model.GameCharacter} while in combat.
 *
 * @author Relex
 */
public interface AccuracyCalculator {

	/**
	 * Gets the accuracy in a double which can range from 0.0 - 1.0
	 * to make high hits a bit less likely and overall combat a bit less
	 * predictable.
	 * 
	 * @param source	The {@link org.niobe.model.GameCharacter} attempting to deal damage.
	 * @param victim	The {@link org.niobe.model.GameCharacter} that will be hit.
	 * @return			Returns a value from 0.0 - 1.0;
	 * 						- 0: will not hit
	 * 						- 1: normal hit, randomize max hit
	 * 						- 0.01-0.99: hit, but remove some damage from max hit
	 */
	public double getAccuracy(GameCharacter source, GameCharacter victim);
}