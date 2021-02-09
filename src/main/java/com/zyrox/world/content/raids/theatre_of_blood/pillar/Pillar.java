package com.zyrox.world.content.raids.theatre_of_blood.pillar;

import com.zyrox.model.Position;
import com.zyrox.world.clip.region.RegionClipping;

/**
 * Created by Jonny on 7/8/2019
 **/
public class Pillar {

    private PillarObject pillarObject;
    private PillarNpc pillarNpc;

    public Pillar(Position positionToSpawn) {
        this.pillarObject = new PillarObject(PillarConstants.PILLAR_OBJECT_ID, positionToSpawn, 10, 0);
        this.pillarNpc = new PillarNpc(PillarConstants.SUPPORTING_PILLAR_NPC_ID, positionToSpawn);
        setProjectilesBlocked(positionToSpawn);
    }

    public PillarObject getPillarObject() {
        return pillarObject;
    }

    public void setPillarObject(PillarObject pillarObject) {
        this.pillarObject = pillarObject;
    }

    public PillarNpc getPillarNpc() {
        return pillarNpc;
    }

    public void setProjectilesBlocked(Position positionToSpawn) {
        RegionClipping.addClipping(positionToSpawn.getX(), positionToSpawn.getY(), positionToSpawn.getZ(), RegionClipping.PROJECTILE_TILE_BLOCKED);
        RegionClipping.addClipping(positionToSpawn.getX() + 1, positionToSpawn.getY(), positionToSpawn.getZ(), RegionClipping.PROJECTILE_TILE_BLOCKED);
        RegionClipping.addClipping(positionToSpawn.getX() + 2, positionToSpawn.getY(), positionToSpawn.getZ(), RegionClipping.PROJECTILE_TILE_BLOCKED);

        RegionClipping.addClipping(positionToSpawn.getX(), positionToSpawn.getY() + 1, positionToSpawn.getZ(), RegionClipping.PROJECTILE_TILE_BLOCKED);
        RegionClipping.addClipping(positionToSpawn.getX(), positionToSpawn.getY() + 2, positionToSpawn.getZ(), RegionClipping.PROJECTILE_TILE_BLOCKED);
    }

    public void setProjectilesUnblocked(Position positionToSpawn) {
        RegionClipping.removeClipping(positionToSpawn.getX(), positionToSpawn.getY(), positionToSpawn.getZ(), RegionClipping.PROJECTILE_TILE_BLOCKED);
        RegionClipping.removeClipping(positionToSpawn.getX() + 1, positionToSpawn.getY(), positionToSpawn.getZ(), RegionClipping.PROJECTILE_TILE_BLOCKED);
        RegionClipping.removeClipping(positionToSpawn.getX() + 2, positionToSpawn.getY(), positionToSpawn.getZ(), RegionClipping.PROJECTILE_TILE_BLOCKED);

        RegionClipping.removeClipping(positionToSpawn.getX(), positionToSpawn.getY() + 1, positionToSpawn.getZ(), RegionClipping.PROJECTILE_TILE_BLOCKED);
        RegionClipping.removeClipping(positionToSpawn.getX(), positionToSpawn.getY() + 2, positionToSpawn.getZ(), RegionClipping.PROJECTILE_TILE_BLOCKED);

        RegionClipping.removeClipping(positionToSpawn.getX() + 1, positionToSpawn.getY() + 1, positionToSpawn.getZ(), RegionClipping.PROJECTILE_TILE_BLOCKED);
        RegionClipping.removeClipping(positionToSpawn.getX() + 1, positionToSpawn.getY() + 2, positionToSpawn.getZ(), RegionClipping.PROJECTILE_TILE_BLOCKED);

        RegionClipping.removeClipping(positionToSpawn.getX() + 2, positionToSpawn.getY() + 1, positionToSpawn.getZ(), RegionClipping.PROJECTILE_TILE_BLOCKED);
        RegionClipping.removeClipping(positionToSpawn.getX() + 2, positionToSpawn.getY() + 2, positionToSpawn.getZ(), RegionClipping.PROJECTILE_TILE_BLOCKED);
    }
}
