package com.zyrox.world.content.combat.magic;

import com.zyrox.world.entity.impl.GameCharacter;

/**
 * A {@link Spell} implementation primarily used for spells that have no effects
 * at all when they hit the player.
 * 
 * @author lare96
 */
public abstract class CombatNormalSpell extends CombatSpell {

    @Override
    public void finishCast(GameCharacter cast, GameCharacter castOn, boolean accurate,
                           int damage) {}
}