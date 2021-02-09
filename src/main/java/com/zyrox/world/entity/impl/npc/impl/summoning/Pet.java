package com.zyrox.world.entity.impl.npc.impl.summoning;

import com.zyrox.model.Position;
import com.zyrox.world.entity.impl.npc.NPC;
import com.zyrox.world.entity.impl.player.Player;

/**
 * Handle the player's special summoning with effects.
 *
 * @author Gabriel || Wolfsdarker
 */
public abstract class Pet extends NPC {

    /**
     * The owner of this pet.
     */
    private Player owner;

    /**
     * If the pet's effect is active.
     */
    private boolean effectActive;

    public Pet(int id, Position position) {
        super(id, position);
    }

    /**
     * Returns the owner of this pet.
     *
     * @return the owner instance
     */
    public Player getOwner() {
        return owner;
    }

    /**
     * Sets the player that owns this pet.
     *
     * @param player
     */
    public void setOwner(Player player) {
        this.owner = player;
    }

    /**
     * Returns if the effect is active.
     *
     * @return if is active
     */
    public boolean isEffectActive() {
        return effectActive;
    }

    /**
     * Sets if the effect is active.
     *
     * @param effectActive
     */
    public void setEffectActive(boolean effectActive) {
        this.effectActive = effectActive;
    }

    @Override
    public void sequence() {
        super.sequence();
        onTick();
    }

    /**
     * Executes every tick to handle the pet's special abilities.
     */
    abstract void onTick();

    /**
     * Toggles the pet's effect.
     *
     * @param player
     */
    public static void toggleEffect(Player player) {
        if (player.getSummoning().getFamiliar() == null || player.getSummoning().getFamiliar().getSummonNpc() == null
                || !(player.getSummoning().getFamiliar().getSummonNpc() instanceof Pet)) {
            return;
        }
        Pet pet = (Pet) player.getSummoning().getFamiliar().getSummonNpc();
        pet.setEffectActive(!pet.isEffectActive());
        player.sendMessage("Your pet's effect has been " + (pet.isEffectActive() ? "activated" : "deactivated") + ".");
    }
}
