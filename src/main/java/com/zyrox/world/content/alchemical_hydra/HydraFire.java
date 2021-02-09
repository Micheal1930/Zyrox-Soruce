package com.zyrox.world.content.alchemical_hydra;

import com.zyrox.GameSettings;
import com.zyrox.engine.task.Task;
import com.zyrox.engine.task.TaskManager;
import com.zyrox.model.Graphic;
import com.zyrox.model.Hit;
import com.zyrox.model.Position;
import com.zyrox.util.Misc;
import com.zyrox.world.clip.region.RegionClipping;
import com.zyrox.world.entity.Entity;
import com.zyrox.world.entity.impl.player.Player;

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
