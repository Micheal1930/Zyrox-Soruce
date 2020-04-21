package com.varrock.world.clip.region;

/**
 * Created by Jonny on 5/13/2019
 **/
class MapDefinition {
    private final int id;
    private final int objects;
    private final int terrain;

    public MapDefinition(int id, int objects, int terrain) {
        this.id = id;
        this.objects = objects;
        this.terrain = terrain;
    }

    public int getObjects() {
        return objects;
    }

    public int getId() {
        return id;
    }

    public int getTerrain() {
        return terrain;
    }
}
