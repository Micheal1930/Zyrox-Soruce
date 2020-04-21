package com.varrock.world.content.alchemical_hydra;

import com.varrock.GameSettings;
import com.varrock.engine.task.Task;
import com.varrock.engine.task.TaskManager;
import com.varrock.model.Graphic;
import com.varrock.model.Hit;
import com.varrock.model.Position;
import com.varrock.util.Misc;
import com.varrock.world.clip.region.RegionClipping;
import com.varrock.world.entity.Entity;
import com.varrock.world.entity.impl.player.Player;

public class HydraFire {

    public HydraFire(Entity entity, Position position) {

        if(!entity.isPlayer()) {
            return;
        }

        Player player = entity.asPlayer();

        if (!KaruulmSlayerHydra.isWithinHydraLair(position.getX(), position.getY())) {
            return;
        }

        //RegionClipping.addClipping(position.getX(), position.getY(), position.getZ(), RegionClipping.BLOCKED_TILE);
        if (!KaruulmSlayerHydra.isWithinHydraLair(player.getPosition().getX(), player.getPosition().getY()) || player.getPosition().getZ() != position.getZ()) {
            return;
        }
        player.getPacketSender().sendGraphic(new Graphic(1668 + GameSettings.OSRS_GFX_OFFSET), position);
        TaskManager.submit(new Task(1) {

            int cycle = 0;

            @Override
            protected void execute() {
                if (++cycle == 50) {
                    RegionClipping.removeClipping(position.getX(), position.getY(), position.getZ(), RegionClipping.BLOCKED_TILE);
                    stop();
                }
                if (!KaruulmSlayerHydra.isWithinHydraLair(player.getPosition().getX(), player.getPosition().getY())) {
                    stop();
                    return;
                }
                if (player.getPosition().equals(position)) {
                    player.dealDamage(new Hit(Misc.randomInclusive(1, 15)));
                }
            }
        });
    }


}
