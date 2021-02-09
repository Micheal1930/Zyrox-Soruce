package com.zyrox.world.content.combat.strategy;

import com.zyrox.world.content.combat.CombatContainer;
import com.zyrox.world.content.combat.CombatType;
import com.zyrox.world.entity.Entity;
import com.zyrox.world.entity.impl.GameCharacter;
import com.zyrox.world.entity.impl.player.Player;

/**
 * A container used to determine how an entity will act during a combat session.
 * New combat strategies do not need to be made for {@link Player}s because
 * everything is handled for them in the three default factory strategy classes.
 * 
 * @author lare96
 */
public interface CombatStrategy {

    /**
     * Determines if the attacking {@link Entity} is able to make an attack.
     * Used for miscellaneous checks such as runes, arrows, target, etc.
     * 
     * @param entity
     *            the entity to check.
     * @return <code>true</code> if the attack can be successfully made,
     *         <code>false</code> if it cannot.
     */
    public boolean canAttack(GameCharacter entity, GameCharacter victim);

    /**
     * Fired when the attacking {@link Entity} has passed the
     * <code>canAttack(Entity e1, Entity e2)</code> check and is ready to
     * attack.
     * 
     * @param entity
     *            the attacking entity in this combat hook.
     * @param victim
     *            the defending entity in this combat hook.
     * @return the combat container that will be used to deal damage to the
     *         victim during this hook.
     */
    public CombatContainer attack(GameCharacter entity, GameCharacter victim);

    public boolean customContainerAttack(GameCharacter entity, GameCharacter victim);
    
    /**
     * How long the attacking {@link Entity} must wait in intervals to attack.
     * 
     * @param entity
     *            the attacking entity in this combat hook.
     * @return the amount of time that the attacker must wait.
     */
    public int attackDelay(GameCharacter entity);

    /**
     * How close the attacking {@link Entity} must be to attack the victim.
     * 
     * @param entity
     *            the attacking entity in this combat hook.
     * @return the radius that the attacker has to be within in order to attack.
     */
    public int attackDistance(GameCharacter entity);
    
    public CombatType getCombatType();
  
}
