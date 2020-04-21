package com.varrock.world.entity.impl.npc.impl.summoning;

import com.varrock.model.Position;
import com.varrock.world.entity.impl.player.Player;

public class SuperiorOlmlet extends Pet {

    public static final int ID = 22519;

    public SuperiorOlmlet(int id, Position position) {
        super(id, position);
    }

    @Override
    void onTick() {
    }

    /**
     * Checks if the player has the effect on.
     *
     * @param player
     * @return if the effect is active.
     */
    public static boolean hasEffect(Player player) {
        if (player.getSummoning().getFamiliar() == null || player.getSummoning().getFamiliar().getSummonNpc() == null
                || !(player.getSummoning().getFamiliar().getSummonNpc() instanceof SuperiorOlmlet)) {
            return false;
        }

        SuperiorOlmlet olmlet = (SuperiorOlmlet) player.getSummoning().getFamiliar().getSummonNpc();

        return olmlet.isEffectActive();
    }
}
