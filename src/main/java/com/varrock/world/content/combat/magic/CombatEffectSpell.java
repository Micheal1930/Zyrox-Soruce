package com.varrock.world.content.combat.magic;

import java.util.Optional;

import com.varrock.model.Item;
import com.varrock.world.entity.impl.GameCharacter;
import com.varrock.world.entity.impl.player.Player;

/**
 * A {@link Spell} implementation primarily used for spells that have effects
 * when they hit the player.
 * 
 * @author lare96
 */
public abstract class CombatEffectSpell extends CombatSpell {

    @Override
    public int maximumHit() {

        // These types of spells don't have a 'hit'.
        return -1;
    }

    @Override
    public Optional<Item[]> equipmentRequired(Player player) {

        // These types of spells never require any equipment, although the
        // method can still be overridden if by some chance a spell does.
        return Optional.empty();
    }

    @Override
    public void finishCast(GameCharacter cast, GameCharacter castOn, boolean accurate,
                           int damage) {
        if (accurate) {
            spellEffect(cast, castOn);
        }
    }

    /**
     * The effect that will take place once the spell hits the target.
     * 
     * @param cast
     *            the entity casting the spell.
     * @param castOn
     *            the entity being hit by the spell.
     */
    public abstract void spellEffect(GameCharacter cast, GameCharacter castOn);
}
