package com.zyrox.world.content;

import java.util.concurrent.CopyOnWriteArrayList;

import com.zyrox.engine.task.Task;
import com.zyrox.engine.task.TaskManager;
import com.zyrox.model.GameObject;
import com.zyrox.model.GroundItem;
import com.zyrox.model.Item;
import com.zyrox.model.Locations;
import com.zyrox.model.Position;
import com.zyrox.model.Locations.Location;
import com.zyrox.world.World;
import com.zyrox.world.clip.region.RegionClipping;
import com.zyrox.world.entity.impl.GroundItemManager;
import com.zyrox.world.entity.impl.player.Player;

/**
 * Handles customly spawned objects (mostly global but also privately for players)
 *
 * @author Gabriel Hannason
 */
public class CustomObjects {

    private static final int DISTANCE_SPAWN = 70; //Spawn if within 70 squares of distance
    public static final CopyOnWriteArrayList<GameObject> CUSTOM_OBJECTS = new CopyOnWriteArrayList<GameObject>();

    public static final CopyOnWriteArrayList<GameObject> OBJECTS_TO_DELETE = new CopyOnWriteArrayList<GameObject>();

    public static void init() {
        for (int i = 0; i < CLIENT_OBJECTS.length; i++) {
            int id = CLIENT_OBJECTS[i][0];
            int x = CLIENT_OBJECTS[i][1];
            int y = CLIENT_OBJECTS[i][2];
            int z = CLIENT_OBJECTS[i][3];
            int face = CLIENT_OBJECTS[i][4];
            int type = CLIENT_OBJECTS[i].length > 5 ? CLIENT_OBJECTS[i][5] : 10;
            GameObject object = new GameObject(id, new Position(x, y, z));
            object.setFace(face);
            object.setType(type);
            RegionClipping.addObject(object);
        }
        for (int i = 0; i < CUSTOM_OBJECTS_SPAWNS.length; i++) {
            int id = CUSTOM_OBJECTS_SPAWNS[i][0];
            int x = CUSTOM_OBJECTS_SPAWNS[i][1];
            int y = CUSTOM_OBJECTS_SPAWNS[i][2];
            int z = CUSTOM_OBJECTS_SPAWNS[i][3];
            int face = CUSTOM_OBJECTS_SPAWNS[i][4];
            GameObject object = new GameObject(id, new Position(x, y, z));
            object.setFace(face);
            CUSTOM_OBJECTS.add(object);
            World.register(object);
        }

        //Gambling block
        RegionClipping.addClipping(2454, 3076, 0, RegionClipping.BLOCKED_TILE);
        RegionClipping.addClipping(2455, 3076, 0, RegionClipping.BLOCKED_TILE);
        RegionClipping.addClipping(2436, 3123, 0, RegionClipping.BLOCKED_TILE);
        RegionClipping.addClipping(2436, 3124, 0, RegionClipping.BLOCKED_TILE);

        RegionClipping.removeClipping(3153, 3923, 0, 0x000000);
    }

    private static void handleList(GameObject object, String handleType) {
        switch (handleType.toUpperCase()) {
            case "DELETE":
                for (GameObject objects : CUSTOM_OBJECTS) {
                    if (objects.getId() == object.getId() && object.getPosition().equals(objects.getPosition())) {
                        CUSTOM_OBJECTS.remove(objects);
                    }
                }
                break;
            case "ADD":
                GameObject existing = getGameObject(object.getPosition());
                if (existing != null) {
                    handleList(existing, "delete");
                }
                if (!CUSTOM_OBJECTS.contains(object)) {
                    CUSTOM_OBJECTS.add(object);
                }
                break;
            case "EMPTY":
                CUSTOM_OBJECTS.clear();
                break;
        }
    }

    public static void acidPool(final GameObject ob, final Player player, final int delay, final int cycles) {
        TaskManager.submit(new Task(delay) {
            @Override
            public void execute() {

                spawnGlobalObject(ob);
                TaskManager.submit(new Task(cycles) {
                    @Override
                    public void execute() {
                        deleteGlobalObject(ob);
                        if (player.getInteractingObject() != null && player.getInteractingObject().getId() == 32000) {
                            player.setInteractingObject(null);
                        }
                        this.stop();
                    }

                    @Override
                    public void stop() {
                        setEventRunning(false);
                    }
                });
                stop();
            }
        });
    }

    public static void spawnObject(Player p, GameObject object) {
        if (object.getId() != -1) {
            p.getPacketSender().sendObject(object);
            if (!RegionClipping.objectExists(object)) {
                RegionClipping.addObject(object);
            }
        } else {
            deleteObject(p, object);
        }
    }

    public static void deleteObject(Player p, GameObject object) {
        p.getPacketSender().sendObjectRemoval(object);
        if (RegionClipping.objectExists(object)) {
            RegionClipping.removeObject(object);
        }
    }

    public static void deleteGlobalObject(GameObject object) {
        handleList(object, "delete");
        World.deregister(object);
    }

    public static void spawnGlobalObject(GameObject object) {
        handleList(object, "add");
        World.register(object);
    }

    public static void spawnGlobalObjectWithinDistance(GameObject object) {
        for (Player player : World.getPlayers()) {
            if (player == null)
                continue;
            if (object.getPosition().isWithinDistance(player.getPosition(), DISTANCE_SPAWN)) {
                spawnObject(player, object);
            }
        }
    }

    public static void deleteGlobalObjectWithinDistance(GameObject object) {
        for (Player player : World.getPlayers()) {
            if (player == null)
                continue;
            if (object.getPosition().isWithinDistance(player.getPosition(), DISTANCE_SPAWN)) {
                deleteObject(player, object);
            }
        }
    }

    public static boolean objectExists(Position pos) {
        return getGameObject(pos) != null;
    }

    public static GameObject getGameObject(Position pos) {
        for (GameObject objects : CUSTOM_OBJECTS) {
            if (objects != null && objects.getPosition().equals(pos)) {
                return objects;
            }
        }

        GameObject gameObject = RegionClipping.getGameObject(pos);

        return gameObject;
    }

    public static GameObject getGameObject(Position pos, int... types) {
        for (GameObject objects : CUSTOM_OBJECTS) {
            if (objects != null && objects.getPosition().equals(pos)) {
                for(int type : types) {
                    if(objects.getType() == type) {
                        return objects;
                    }
                }
                return null;
            }
        }

        return RegionClipping.getGameObject(pos, types);
    }

    public static void handleRegionChange(Player p) {
        for (GameObject object : CUSTOM_OBJECTS) {
            if (object == null)
                continue;
            if (object.getPosition().isWithinDistance(p.getPosition(), DISTANCE_SPAWN)) {
                spawnObject(p, object);
            }
        }
    }

    public static void objectRespawnTask(Player p, final GameObject tempObject, final GameObject mainObject, final int cycles) {
        deleteObject(p, mainObject);
        spawnObject(p, tempObject);
        TaskManager.submit(new Task(cycles) {
            @Override
            public void execute() {
                deleteObject(p, tempObject);
                spawnObject(p, mainObject);
                this.stop();
            }
        });
    }

    public static void globalObjectRespawnTask(final GameObject tempObject, final GameObject mainObject, final int cycles) {
        deleteGlobalObject(mainObject);
        spawnGlobalObject(tempObject);
        TaskManager.submit(new Task(cycles) {
            @Override
            public void execute() {
                deleteGlobalObject(tempObject);
                spawnGlobalObject(mainObject);
                this.stop();
            }
        });
    }

    public static void globalObjectRemovalTask(final GameObject object, final int cycles) {
        spawnGlobalObject(object);
        TaskManager.submit(new Task(cycles) {
            @Override
            public void execute() {
                deleteGlobalObject(object);
                this.stop();
            }
        });
    }

    public static void globalFiremakingTask(final GameObject fire, final Player player, final int cycles) {
        spawnGlobalObject(fire);
        TaskManager.submit(new Task(cycles) {
            @Override
            public void execute() {
                deleteGlobalObject(fire);
                if (player.getInteractingObject() != null && player.getInteractingObject().getId() == 2732) {
                    player.setInteractingObject(null);
                }
                this.stop();
            }

            @Override
            public void stop() {
                setEventRunning(false);
                GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(592), fire.getPosition(), player.getUsername(), false, 150, true, 100));
            }
        });
    }


    /**
     * Contains
     *
     * @param ObjectId - The object ID to spawn
     * @param absX - The X position of the object to spawn
     * @param absY - The Y position of the object to spawn
     * @param Z - The Z position of the object to spawn
     * @param face - The position the object will face
     */

    //Only adds clips to these objects, they are spawned clientsided
    //NOTE: You must add to the client's customobjects array to make them spawn, this is just clipping!
    private static final int[][] CLIENT_OBJECTS = {

            /**
             * Magic trees
            */
            {130087, 3675, 3222, 0, 2},
            {1306, 3277, 3940, 0, 0},
            {1306, 3279, 3942, 0, 0},
            {1306, 3281, 3944, 0, 0},
            {-1, 3281, 3941, 0, 0},

            {-1, 3158, 3951, 0, 0}, // Web
            {1817, 3153, 3923, 0, 0, 4}, // Wildy lever

            {10087, 3187, 3927, 0, 0}, // Dark crab fishing spot

            //Well for upgradable at home and gold bars

            {36263, 3104, 3503, 0, 0},
            {36263, 3104, 3498, 0, 0},

            // Warmonger Boss area //

            {31427, 3108, 4354, 0, 0},
            {31427, 3107, 4354, 0, 0},
            {31427, 3106, 4354, 0, 0},
            {31427, 3105, 4354, 0, 0},
            {31427, 3104, 4354, 0, 0},
            {31427, 3103, 4354, 0, 0},
            {31427, 3102, 4354, 0, 0},

            // Warmonger Boss area //

            {31427, 3101, 4355, 0, 0},
            {31427, 3101, 4356, 0, 0},
            {31427, 3101, 4357, 0, 0},
            {31427, 3101, 4358, 0, 0},
            {31427, 3101, 4359, 0, 0},
            {31427, 3101, 4360, 0, 0},
            {31427, 3101, 4361, 0, 0},
            {31427, 3101, 4362, 0, 0},
            {31427, 3101, 4363, 0, 0},
            {31427, 3101, 4364, 0, 0},
            {31427, 3101, 4365, 0, 0},

            {31427, 3102, 4366, 0, 0},
            {31427, 3103, 4366, 0, 0},
            {31427, 3104, 4366, 0, 0},
            {31427, 3105, 4366, 0, 0},
            {31427, 3106, 4366, 0, 0},
            {31427, 3107, 4366, 0, 0},
            {31427, 3108, 4366, 0, 0},

            {31427, 3109, 4365, 0, 0},
            {31427, 3109, 4364, 0, 0},
            {31427, 3109, 4363, 0, 0},
            {31427, 3109, 4362, 0, 0},

            {31427, 3109, 4360, 0, 0},
            {31427, 3109, 4359, 0, 0},
            {31427, 3109, 4358, 0, 0},
            {31427, 3109, 4357, 0, 0},
            {31427, 3109, 4356, 0, 0},
            {31427, 3109, 4355, 0, 0},

            // stones at warmonger boss
            {31429, 3116, 4352, 0, 0},
            {31429, 3099, 4372, 0, 0},
            {31429, 3115, 4364, 0, 0},
            {31429, 3102, 4345, 0, 0},
            {31425, 3112, 4345, 0, 0},
            {31425, 3092, 4353, 0, 0},
            {31423, 3104, 4356, 0, 0},

            {31424, 3104, 4359, 0, 1},

            {31422, 3104, 4362, 0, 0},

            {31427, 3109, 4354, 0, 0},

            //Treasure island chests
            {18804, 3039, 2912, 0, 2},

            /**Donator Zone**/
            {11758, 3035, 4413, 0, 0},//bank
            {11758, 3036, 4413, 0, 0},//bank
            {11758, 3037, 4413, 0, 0},//bank
            {11758, 3038, 4413, 0, 0},//bank
            {11758, 3039, 4413, 0, 0},//bank
            {11758, 3040, 4413, 0, 0},//bank
            {11758, 3041, 4413, 0, 0},//bank
            {11758, 3042, 4413, 0, 0},//bank
            {11758, 3043, 4413, 0, 0},//bank
            {-1, 3037, 4402, 0, 0}, //Pipes around rocktail
            {-1, 3038, 4402, 0, 0}, //Pipes around rocktail
            {-1, 3037, 4398, 0, 0}, //Pipes around rocktail
            {-1, 3038, 4398, 0, 0}, //Pipes around rocktail
            {-1, 3040, 4398, 0, 0}, //Pipes around rocktail
            {-1, 3041, 4398, 0, 0}, //Pipes around rocktail
            {-1, 3041, 4401, 0, 0}, //Pipes around rocktail
            {-1, 3041, 4402, 0, 0}, //Pipes around rocktail
            {-1, 3041, 4399, 0, 0}, //Pipes around rocktail
            {-1, 3037, 4399, 0, 0}, //Pipes around rocktail
            {-1, 3037, 4401, 0, 0}, //Pipes around rocktail
            {-1, 3040, 4402, 0, 0}, //Pipes around rocktail
            {-1, 3043, 4391, 0, 0}, //cages
            {-1, 3043, 4393, 0, 0}, //cages
            {-1, 3041, 4391, 0, 0}, //cages

            {-1, 2573, 9876, 0, 0}, //cages
            {-1, 2575, 9876, 0, 0}, //cages
            {-1, 2578, 9875, 0, 0}, //cages
            {-1, 2576, 9873, 0, 0}, //cages
            {-1, 2577, 9893, 0, 0}, //cages
            {-1, 2567, 9886, 0, 0}, //cages
            {-1, 2566, 9889, 0, 0}, //cages
            {-1, 2577, 9894, 0, 0}, //cages
            {-1, 2580, 9894, 0, 0}, //cages


            {-1, 3041, 4393, 0, 0}, //cages
            {-1, 3039, 4391, 0, 0}, //cages
            {-1, 3039, 4393, 0, 0}, //cages
            {-1, 3037, 4393, 0, 0}, //cages
            {-1, 3037, 4391, 0, 0}, //cages
            {-1, 3042, 4388, 0, 0}, //Expiriment Tables
            {-1, 3037, 4388, 0, 0}, //Expiriment Tables
            {-1, 3036, 4385, 0, 0}, //Expiriment Tables
            {-1, 3044, 4385, 0, 0}, //Expiriment Tables
            {-1, 3037, 4384, 0, 0}, //Expiriment Tables
            {-1, 3042, 4384, 0, 0}, //Expiriment Tables
            {-1, 3036, 4388, 0, 0}, //Expiriment Tables
            {-1, 3044, 4388, 0, 0}, //Expiriment Tables
            {-1, 3041, 4385, 0, 0}, //Expiriment Tables
            {-1, 3039, 4385, 0, 0}, //Expiriment Table


            //onyx city

            // onyxrocks
            {54613, 2716, 5312, 0, 0}, // Onyx city
            {54613, 2717, 5312, 0, 0}, // Onyx city
            {54613, 2717, 5311, 0, 0}, // Onyx city
            {54613, 2717, 5310, 0, 0}, // Onyx city
            {54613, 2717, 5309, 0, 0}, // Onyx city
            {54613, 2716, 5311, 0, 0}, // Onyx city
            {54613, 2716, 5310, 0, 0}, // Onyx city
            {54613, 2716, 5309, 0, 0}, // Onyx city


            // Arthur's dream
            {10089, 1826, 5168, 2, 0}, // anglerfish spot fishworld
            {10089, 1827, 5168, 2, 0}, // anglerfish spot fishworld
            {10089, 1823, 5168, 2, 0}, // anglerfish spot fishworld
            {10089, 1822, 5168, 2, 0}, // anglerfish spot fishworld

            {61551, 1820, 5160, 2, 0}, // white tree arthur's dream
            {61551, 1827, 5159, 2, 0}, // white tree arthur's dream

            {52709, 1824, 5161, 2, 0}, // arthur's dream campfire

            {-1, 3087, 3475, 0, 0}, // arthur's dream campfire

            // Edge arthur's dream teleporter
            {48661, 3082, 3483, 0, 3}, // statue
            {27254, 1831, 5163, 2, 0}, // Dark portal arthur's dream

            /*{54613, 3089, 3483, 0, 0}, // Onyx ore edge bank
            {54613, 3088, 3483, 0, 0}, // Onyx ore edge bank
            {54613, 3087, 3483, 0, 0}, // Onyx ore edge bank
            {54613, 3086, 3483, 0, 0}, // Onyx ore edge bank
            {54613, 3085, 3483, 0, 0}, // Onyx ore edge bank
            {54613, 3084, 3483, 0, 0}, // Onyx ore edge bank
            {54613, 3083, 3483, 0, 0}, // Onyx ore edge bank */

            //{11435, 3414, 2762, 0, 0}, // gold tree

            //dzone

            //runerocks
            {14859, 2331, 9798, 0, 0},
            {14859, 2331, 9799, 0, 0},
            {14859, 2331, 9800, 0, 0},
            {14859, 2331, 9801, 0, 0},
            {14859, 2331, 9802, 0, 0},

            {14859, 2330, 9796, 0, 0},
            {14859, 2330, 9795, 0, 0},
            {14859, 2330, 9794, 0, 0},

            //banks
            {2213, 2323, 9800, 0, 0},
            {2213, 2322, 9800, 0, 0},

            {2213, 2320, 9800, 0, 0},
            {2213, 2319, 9800, 0, 0},
            {2213, 2318, 9800, 0, 0},

            {2213, 1715, 5604, 0, 0},

            // MMA ZONE

            {-1, 2035, 4492, 0, 0},
            {-1, 2035, 4494, 0, 0},
            {-1, 2037, 4492, 0, 0},
            {-1, 2037, 4494, 0, 0},

            {57164, 2036, 4486, 0, 0},

            {-1, 2039, 4500, 0, 0},
            {-1, 2034, 4500, 0, 0},

            // Lava fire giants zone

            {-1, 2456, 5212, 0, 0},
            {-1, 2457, 5212, 0, 0},

            //{54408, 3089, 3496, 0, 3},

            {-1, 2456, 5215, 0, 0},
            {-1, 2457, 5215, 0, 0},
            {-1, 2456, 5216, 0, 0},
            {-1, 2457, 5216, 0, 0},
            {-1, 2456, 5217, 0, 0},
            {-1, 2457, 5217, 0, 0},
            {-1, 2456, 5218, 0, 0},
            {-1, 2457, 5218, 0, 0},
            {-1, 2456, 5219, 0, 0},
            {-1, 2457, 5219, 0, 0},

            {-1, 2460, 5215, 0, 0},
            {-1, 2461, 5215, 0, 0},
            {-1, 2460, 5216, 0, 0},
            {-1, 2461, 5216, 0, 0},
            {-1, 2460, 5217, 0, 0},
            {-1, 2461, 5217, 0, 0},
            {-1, 2460, 5218, 0, 0},
            {-1, 2461, 5218, 0, 0},
            {-1, 2460, 5219, 0, 0},
            {-1, 2461, 5219, 0, 0},

            {-1, 2464, 5215, 0, 0},
            {-1, 2465, 5215, 0, 0},
            {-1, 2464, 5216, 0, 0},
            {-1, 2465, 5216, 0, 0},
            {-1, 2464, 5217, 0, 0},
            {-1, 2465, 5217, 0, 0},
            {-1, 2464, 5218, 0, 0},
            {-1, 2465, 5218, 0, 0},
            {-1, 2464, 5219, 0, 0},
            {-1, 2465, 5219, 0, 0},

            {-1, 2457, 5212, 0, 0},
            {-1, 2457, 5212, 0, 0},
            {-1, 2456, 5211, 0, 0},
            {-1, 2457, 5211, 0, 0},
            {-1, 2456, 5210, 0, 0},
            {-1, 2457, 5210, 0, 0},
            {-1, 2456, 5209, 0, 0},
            {-1, 2457, 5209, 0, 0},
            {-1, 2456, 5208, 0, 0},
            {-1, 2457, 5208, 0, 0},
            
        {-1, 3095, 3499, 0, 0},//bank table
        {-1, 3096, 3499, 0, 0},//bank table

            {-1, 2460, 5212, 0, 0},
            {-1, 2461, 5212, 0, 0},
            {-1, 2460, 5211, 0, 0},
            {-1, 2461, 5211, 0, 0},
            {-1, 2460, 5210, 0, 0},
            {-1, 2461, 5210, 0, 0},
            {-1, 2460, 5209, 0, 0},
            {-1, 2461, 5209, 0, 0},
            {-1, 2460, 5208, 0, 0},
            {-1, 2461, 5208, 0, 0},

            {-1, 2464, 5212, 0, 0},
            {-1, 2465, 5212, 0, 0},
            {-1, 2464, 5211, 0, 0},
            {-1, 2465, 5211, 0, 0},
            {-1, 2464, 5210, 0, 0},
            {-1, 2465, 5210, 0, 0},
            {-1, 2464, 5209, 0, 0},
            {-1, 2465, 5209, 0, 0},
            {-1, 2464, 5208, 0, 0},
            {-1, 2465, 5208, 0, 0},


            // TARN INSTANCED ZONE

            {-1, 3185, 4618, 0, 0},
            {-1, 3189, 4619, 0, 0},
            {-1, 3185, 4621, 0, 0},

            {20465, 3186, 4611, 0, 1},
            {20467, 3186, 4627, 0, 3},

            // CHRYSTAL ZONE Updated

            {57164, 3039, 4530, 0, 0},
            {57164, 3035, 4530, 0, 0},
            {-1, 3035, 4532, 0, 0},
            {57164, 3032, 4529, 0, 0},
            //{-1, 3032, 4528, 0, 0}, // smaller crystal
            {57164, 3031, 4532, 0, 0},
            {57164, 3037, 4536, 0, 0},
            {57164, 3038, 4536, 0, 0},
            {57164, 3033, 4528, 0, 0},
            {57164, 3033, 4528, 0, 0},
            {57164, 3038, 4528, 0, 0},
            {57164, 3046, 4528, 0, 0},
            {57164, 3041, 4534, 0, 0},
            {57164, 3042, 4534, 0, 0},
            {57164, 3043, 4534, 0, 0},
            {57164, 3045, 4534, 0, 0},
            {57164, 3046, 4534, 0, 0},
            {57164, 3042, 4534, 0, 0},
            {57164, 3042, 4534, 0, 0},

            {57164, 3047, 4530, 0, 0},
            {57164, 3046, 4533, 0, 0},

            {57164, 3048, 4532, 0, 0},
            {57164, 3037, 4535, 0, 0},
            {57164, 3038, 4535, 0, 0},
            {57164, 3032, 4530, 0, 0},
            {57164, 3032, 4532, 0, 0},
            {57164, 3032, 4531, 0, 0},
            //{57164, 3032, 4530, 0, 0},
            {57164, 3033, 4529, 0, 0},
            {57164, 3033, 4531, 0, 0},
            {57164, 3034, 4534, 0, 0},
            {57164, 3040, 4534, 0, 0},
            {57164, 3033, 4530, 0, 0},
            {57164, 3033, 4532, 0, 0},
            {57164, 3034, 4529, 0, 0},

            {57164, 3047, 4529, 0, 0},
            {57164, 3047, 4532, 0, 0},
            {57164, 3047, 4531, 0, 0},

            {57164, 3039, 4534, 0, 0},


            {-1, 3039, 4534, 0, 0},
            {-1, 3043, 4534, 0, 0},
            {-1, 3043, 4530, 0, 0},
            {-1, 3039, 4530, 0, 0},

            //{-1, 3038, 4535, 0, 0},
            //{-1, 3037, 4535, 0, 0},
            {-1, 3034, 4532, 0, 0},
            {-1, 3036, 4532, 0, 0},
            //{-1, 3037, 4535, 0, 0},
            {-1, 3034, 4531, 0, 0},
            //{-1, 3035, 4531, 0, 0},
            {-1, 3036, 4531, 0, 0},
            {-1, 3036, 4530, 0, 0},
            {-1, 3034, 4530, 0, 0},
            {-1, 3035, 4530, 0, 0},
            {-1, 3035, 4529, 0, 0},
            {-1, 3035, 4533, 0, 0},
            //{-1, 3046, 4533, 0, 0},
            {-1, 3045, 4532, 0, 0},
            //{-1, 3045, 4531, 0, 0},
            {-1, 3046, 4530, 0, 0},
            //	{-1, 3047, 4531, 0, 0},
            //{-1, 3047, 4532, 0, 0},
            //{-1, 3048, 4532, 0, 0},


            {14073, 3041, 4532, 0, 0},

            //{-1, 3032, 4532, 0, 0},
            //{-1, 3032, 4531, 0, 0},
            //{-1, 3032, 4530, 0, 0},
            //{-1, 3033, 4529, 0, 0},
            {57164, 3034, 4528, 0, 0},

            {-1, 3046, 4532, 0, 0},
            {-1, 3046, 4531, 0, 0},

            {57164, 3031, 4531, 0, 0},
            {-1, 3035, 4529, 0, 0},
            //{-1, 3047, 4529, 0, 0},

            {57164, 3047, 4528, 0, 0},
            {57164, 3049, 4532, 0, 0},
            {57164, 3040, 4535, 0, 0},
            {57164, 3039, 4535, 0, 0},
            {57164, 3036, 4535, 0, 0},
            {57164, 3035, 4535, 0, 0},
            {57164, 3034, 4535, 0, 0},
            {57164, 3033, 4534, 0, 0},
            {57164, 3033, 4533, 0, 0},

            {57164, 3031, 4530, 0, 0},
            {57164, 3033, 4533, 0, 0},
            {57164, 3033, 4533, 0, 0},
            {57164, 3033, 4533, 0, 0},

            {57164, 3035, 4528, 0, 0},
            {57164, 3036, 4528, 0, 0},
            {57164, 3037, 4528, 0, 0},
            {57164, 3039, 4528, 0, 0},

            {57164, 3042, 4528, 0, 0},
            {57164, 3043, 4528, 0, 0},
            {57164, 3044, 4528, 0, 0},
            {57164, 3045, 4528, 0, 0},

            {57164, 3048, 4530, 0, 0},
            {57164, 3048, 4531, 0, 0},
            {57164, 3048, 4533, 0, 0},
            {57164, 3047, 4533, 0, 0},

            // 2nd teleporter karuulm

            {35469, 2035, 4492, 0, 0},
            {16859, 2378, 3842, 0, 0}, // Block Electro Wyrm
            {16859, 2378, 3845, 0, 0}, // Block Electro Wyrm
            {29828, 2374, 3851, 0, 0}, // Hole Electro Wyrm

            {0, 2378, 3841, 0, 0}, // Block Broken Bridge

            {0, 2355, 3846, 0, 0}, // Block Broken Bridge
            {0, 2355, 3841, 0, 0}, // Block Broken Bridge

            {57163, 2348, 3879, 0, 0}, // Block Drake island
            {57163, 2347, 3882, 0, 0}, // Block Drake island

            {57163, 2345, 3830, 0, 0}, // Block Drake island

            {46327, 2314, 3848, 0, 0}, // Block Drake island
            {46327, 2345, 3833, 0, 0}, // Block Bridge Drake island

            {57165, 2031, 4496, 0, 0},
            {57165, 2031, 4491, 0, 0},

            {57165, 2033, 4486, 0, 0},
            {-1, 2034, 4486, 0, 0},
            {57165, 2039, 4486, 0, 0},

            {57165, 2042, 4496, 0, 0},
            {57165, 2042, 4491, 0, 0},

            // Teleport to new slayer dung

            {-1, 3079, 3510, 0, 0},

            {-1, 3078, 3510, 0, 0},
            {10780, 2557, 2544, 1, 0},
            {-1, 3081, 3510, 0, 0},


            // Great Kourand
            {-1, 1634, 3671, 0, 0},
            //magetrees
            {1306, 2326, 9795, 0, 0},


            {6189, 2328, 9798, 0, 0},

            {-1, 2324, 9799, 0, 0},
            {-1, 2324, 9798, 0, 0},
            {-1, 2325, 9798, 0, 0},
            {-1, 2326, 9798, 0, 0},
            {-1, 2328, 9799, 0, 0},
            {-1, 2327, 9799, 0, 0},
            {-1, 2327, 9800, 0, 0},
            {-1, 2327, 9798, 0, 0},

            {-1, 2321, 9800, 0, 0},
            {-1, 2320, 9798, 0, 0},
            {-1, 2319, 9799, 0, 0},
            {-1, 2318, 9801, 0, 0},
            {-1, 2321, 9798, 0, 0},
            {-1, 2318, 9798, 0, 0},
            {-1, 2316, 9798, 0, 0},
            {-1, 2309, 9799, 0, 0},

            {-1, 2309, 9806, 0, 0},

            {8135, 2309, 9804, 0, 0},
            {8135, 2310, 9804, 0, 0},
            {8135, 2308, 9804, 0, 0},
            {8135, 2307, 9804, 0, 0},
            {8135, 2309, 9805, 0, 0},
            {8135, 2310, 9805, 0, 0},
            {8135, 2308, 9805, 0, 0},
            {8135, 2307, 9805, 0, 0},

            {-1, 2319, 9798, 0, 0},

            {2213, 2321, 9800, 0, 0},

            {4306, 2325, 9798, 0, 0},
            {2644, 2326, 9798, 0, 0},

            {52709, 2323, 9806, 0, 0},

            {2646, 2328, 9804, 0, 0},
            {2646, 2329, 9804, 0, 0},
            {2646, 2327, 9804, 0, 0},
            {2646, 2328, 9803, 0, 0},
            {2646, 2329, 9803, 0, 0},
            {2646, 2327, 9803, 0, 0},
            {2646, 2330, 9803, 0, 0},

            {170, 3294, 3946, 0, 0}, // rogue castel crates
            {1, 3293, 3946, 0, 0},
            {1, 3295, 3945, 0, 0},
            {1, 3294, 3945, 0, 0},


            //rocktail fishing
            {8702, 2327, 9801, 0, 0},
            //frost drags portal
            //rune armour stealing
            {13493, 2316, 9801, 0, 2},
            //special altar
            {8749, 2342, 9805, 0, 3},

            //superior hall recharge
            {21893, 3018, 4410, 0, 0},
            {21893, 3012, 4380, 0, 3},
            {21893, 3034, 4361, 0, 1},
            {21893, 3043, 4361, 0, 3},
            {21893, 3066, 4380, 0, 1},
            {21893, 3058, 4410, 0, 0},

            //Superior portal at dzone
            {46935, 2345, 9801, 0, 2},


            //dzone thieve altars
            {4875, 2342, 9799, 0, 0},
            {4874, 2342, 9800, 0, 0},
            {4876, 2342, 9801, 0, 0},
            {4877, 2342, 9802, 0, 0},
            {4878, 2342, 9803, 0, 0},


            //dzone
            {2213, 2340, 9808, 0, 0},
            {2213, 2339, 9808, 0, 0},
            {2213, 2341, 9808, 0, 0},
            {2213, 2338, 9808, 0, 0},
            {2213, 2337, 9808, 0, 0},
            {2213, 2336, 9808, 0, 0},
            {2213, 2335, 9808, 0, 0},
            {2213, 2334, 9808, 0, 0},
            {2213, 2313, 9798, 0, 0},
            /*End of new Donator Zone*/

            //Wilderness box area

            // {18321, 3104, 3621, 0, 0}, //Tier 1 Wilderness Box
            {37010, 3170, 3886, 0, 0}, //Tier 2 Wilderness Box
            // {29578, 3170, 3886, 0, 0}, //Tier 3 Wilderness Box


            {-1, 3171, 3886, 0, 0}, //Removal objects
            {-1, 3168, 3884, 0, 0}, //Removal objects

            /*End of wilderness box area*/

            /*Misc*/
            {-1, 3091, 3495, 0, 0},
            {-1, 2268, 3067, 0, 0},
            {401, 3503, 3567, 0, 0},
            {401, 3504, 3567, 0, 0},

            {-1, 3213, 3430, 0, 0},
            {-1, 3213, 3426, 0, 0},

            {13405, 2552, 2599, 1, 2},

            {2274, 2912, 5300, 2, 0},

            {2274, 2914, 5300, 1, 0},
            {2274, 2919, 5276, 1, 0},
            {2274, 2918, 5274, 0, 0},
            {2274, 3001, 3931, 0, 0},
            {-1, 2884, 9797, 0, 2},
            {1746, 2532, 2574, 1, 2},
            {29942, 2532, 2572, 1, 2},
            {4483, 2444, 3083, 0, 1}, // Castle-wars Gamble
            {4483, 2909, 4832, 0, 3},
            {4483, 2901, 5201, 0, 2},
            {4483, 2902, 5201, 0, 2},
            {1662, 3112, 9677, 0, 2},
            {1661, 3114, 9677, 0, 2},
            {1661, 3122, 9664, 0, 1},
            {1662, 3123, 9664, 0, 2},
            {1661, 3124, 9664, 0, 3},
            {4483, 2918, 2885, 0, 3},

            //rfd
            {12356, 1863, 5352, 0, 0},
            {2182, 1861, 5355, 0, 2},

            {2644, 2737, 3444, 0, 0},
            {2644, 2737, 3444, 0, 0},
            {-1, 2608, 4777, 0, 0},
            {-1, 2601, 4774, 0, 0},
            {-1, 2611, 4776, 0, 0},
            /**New Member Zone*/
            {2344, 3421, 2908, 0, 0}, //Rock blocking
            {2345, 3438, 2909, 0, 0},
            {2344, 3435, 2909, 0, 0},
            {2344, 3432, 2909, 0, 0},
            {2345, 3431, 2909, 0, 0},
            {2344, 3428, 2921, 0, 1},
            {2344, 3428, 2918, 0, 1},
            {2344, 3428, 2915, 0, 1},
            {2344, 3428, 2912, 0, 1},
            {2345, 3428, 2911, 0, 1},
            {2344, 3417, 2913, 0, 1},
            {2344, 3417, 2916, 0, 1},
            {2344, 3417, 2919, 0, 1},
            {2344, 3417, 2922, 0, 1},
            {2345, 3417, 2912, 0, 0},
            {2346, 3418, 2925, 0, 0},
            {10378, 3426, 2907, 0, 0},
            {8749, 3426, 2923, 0, 2}, //Altar
            {-1, 3420, 2909, 0, 10}, //Remove crate by mining
            {-1, 3420, 2923, 0, 10}, //Remove Rockslide by Woodcutting
            {14859, 3421, 2909, 0, 0}, //Mining
            {14859, 3419, 2909, 0, 0},
            {14859, 3418, 2910, 0, 0},
            {14859, 3418, 2911, 0, 0},
            {14859, 3422, 2909, 0, 0},
            {1306, 3418, 2921, 0, 0}, //Woodcutting
            {1306, 3421, 2924, 0, 0},
            {1306, 3420, 2924, 0, 0},
            {1306, 3419, 2923, 0, 0},
            {1306, 3418, 2922, 0, 0},
            {-1, 3430, 2912, 0, 2},
            {13493, 3424, 2916, 0, 1},//Armour  stall

            {-1, 3206, 3263, 0, 0},
            {-1, 2794, 2773, 0, 0},
            {2, 2692, 3712, 0, 3},
            {2, 2688, 3712, 0, 1},

            {13179, 2543, 2544, 1, 2}, //regular altar at home
            {411, 2554, 2544, 1, 0}, //ancients altar at home

            {6552, 2539, 2544, 1, 2},

            {409, 2551, 2544, 1, 3},
            {-1, 3084, 3487, 0, 2},

            {884, 2561, 2564, 1, 0},

            {4875, 2556, 2575, 1, 0},
            {4874, 2555, 2575, 1, 0},
            {4876, 2554, 2575, 1, 0},
            {4877, 2553, 2575, 1, 0},
            {4878, 2552, 2575, 1, 0},

            {11758, 3019, 9740, 0, 0},
            {11758, 3020, 9739, 0, 1},
            {11758, 3019, 9738, 0, 2},
            {11758, 3018, 9739, 0, 3},
            {11933, 3028, 9739, 0, 1},
            {11933, 3032, 9737, 0, 2},
            {11933, 3032, 9735, 0, 0},
            {11933, 3035, 9742, 0, 3},
            {11933, 3034, 9739, 0, 0},
            {11936, 3028, 9737, 0, 1},
            {11936, 3029, 9734, 0, 2},
            {11936, 3031, 9739, 0, 0},
            {11936, 3032, 9741, 0, 3},
            {11936, 3035, 9734, 0, 0},
            {11954, 3037, 9739, 0, 1},
            {11954, 3037, 9735, 0, 2},
            {11954, 3037, 9733, 0, 0},
            {11954, 3039, 9741, 0, 3},
            {11954, 3039, 9738, 0, 0},
            {11963, 3039, 9733, 0, 1},
            {11964, 3040, 9732, 0, 2},
            {11965, 3042, 9734, 0, 0},
            {11965, 3044, 9737, 0, 3},
            {11963, 3042, 9739, 0, 0},
            {11963, 3045, 9740, 0, 1},
            {11965, 3043, 9742, 0, 2},
            {11964, 3045, 9744, 0, 0},
            {11965, 3048, 9747, 0, 3},
            {11951, 3048, 9743, 0, 0},
            {11951, 3049, 9740, 0, 1},
            {11951, 3047, 9737, 0, 2},
            {11951, 3050, 9738, 0, 0},
            {11951, 3052, 9739, 0, 3},
            {11951, 3051, 9735, 0, 0},
            {11947, 3049, 9735, 0, 1},
            {11947, 3049, 9734, 0, 2},
            {11947, 3047, 9733, 0, 0},
            {11947, 3046, 9733, 0, 3},
            {11947, 3046, 9735, 0, 0},
            {11941, 3053, 9737, 0, 1},
            {11939, 3054, 9739, 0, 2},
            {11941, 3053, 9742, 0, 0},
            {14859, 3038, 9748, 0, 3},
            {14859, 3044, 9753, 0, 0},
            {14859, 3048, 9754, 0, 1},
            {14859, 3054, 9746, 0, 2},
            {4306, 3026, 9741, 0, 0},
            {6189, 3022, 9742, 0, 1},
            {172, 2532, 2554, 1, 3},
            {3192, 3082, 3485, 0, 4},


            {75, 2914, 3452, 0, 2},


            //	{ 47947 , 2720, 5352, 0, 2}, // waterfall o city

            {16284, 3028, 4514, 0, 0}, // Crystal afk
            {49653, 3035, 4531, 0, 3}, // Stattuete of Axemurdera
            {41026, 3045, 4531, 0, 1}, // Stattuete of Risen Siren
            {53979, 3044, 4536, 0, 0}, // Teleporter to AFK Bosses

            {-1, 2461, 5295, 0, 0}, // remove object crystal cave
            {-1, 2463, 5291, 0, 0}, // remove object crystal cave
            {-1, 2462, 5287, 0, 0}, // remove object crystal cave


            {10624, 2532, 2556, 1, 3}, // Stone chest crate at edgeville

            {11758, 3449, 3722, 0, 0},
            {11758, 3450, 3722, 0, 0},
            {50547, 3445, 3717, 0, 3},
            {-1, 3085, 3512, 0, 0},
            //{ -1, 3090, 3496, 0, 0},
            //{ -1, 3090, 3494, 0, 0},
            {-1, 3092, 3496, 0, 0},
            {-1, 3659, 3508, 0, 0},
            {4053, 3660, 3508, 0, 0},
            {4051, 3659, 3508, 0, 0},
            {1, 3649, 3506, 0, 0},
            {1, 3650, 3506, 0, 0},
            {1, 3651, 3506, 0, 0},
            {1, 3652, 3506, 0, 0},
            {8702, 3423, 2911, 0, 0},

            {8702, 3281, 3940, 0, 0}, // rogues castle rocktail

            {47180, 3422, 2918, 0, 0},
            {11356, 3418, 2917, 0, 1},
            {-1, 2860, 9734, 0, 1},
            {-1, 2857, 9736, 0, 1},
            {664, 2859, 9742, 0, 1},
            {1167, 2860, 9742, 0, 1},
            {5277, 3691, 3465, 0, 2},
            {5277, 3690, 3465, 0, 2},
            {5277, 3688, 3465, 0, 2},
            {5277, 3687, 3465, 0, 2},

            {30087, 1231, 3559, 0, 3}, //west raids bank booth

            /**
             * Level 60+ woodcutting area
            */
            {-1, 2709, 3459, 0, 0},
            {-1, 2712, 3459, 0, 0},
            {-1, 2711, 3461, 0, 0},
            {-1, 2709, 3461, 0, 0},
            {-1, 2708, 3458, 0, 0},
            {-1, 2712, 3457, 0, 0},
            {-1, 2714, 3458, 0, 0},
            {-1, 2713, 3461, 0, 0},
            {-1, 2709, 3460, 0, 0},
            {-1, 2709, 3458, 0, 0},

            {1306, 2710, 3459, 0, 1},

            {-1, 2709, 3464, 0, 0},
            {-1, 2708, 3466, 0, 0},
            {-1, 2713, 3464, 0, 0},
            {-1, 2712, 3466, 0, 0},
            {-1, 2711, 3467, 0, 0},
            {-1, 2710, 3465, 0, 0},
            {-1, 2709, 3465, 0, 0},
            {-1, 2709, 3466, 0, 0},
            {-1, 2709, 3467, 0, 0},
            {-1, 2710, 3467, 0, 0},

            {-1, 3217, 3436, 0, 0},
            {-1, 3218, 3436, 0, 0},
            {-1, 3219, 3436, 0, 0},

            {-1, 3217, 3437, 0, 0},
            {-1, 3218, 3437, 0, 0},
            {-1, 3219, 3437, 0, 0},

            {1306, 2710, 3465, 0, 1},

            {-1, 2715, 3466, 0, 0},
            {-1, 2714, 3466, 0, 0},
            {-1, 2715, 3465, 0, 0},
            {-1, 2714, 3467, 0, 0},

            {1306, 2715, 3465, 0, 1},


            {1817, 3091, 3487, 0, 0, 4}, // Edge lever

            {1306, 3092, 3463, 0, 0}, // Magic tree

            {24161, 2546, 2594, 1, 0}, // fountain

            { 2982, 2446, 3091, 0, 0 }, { 2981, 2446, 3088, 0, 0 }, { 2986, 2444, 3093, 0, 0 },
            { 2986, 2444, 3094, 0, 0 }, { 2986, 2444, 3086, 0, 0 }, { 2986, 2444, 3085, 0, 0 },
            { -1, 2444, 3084, 0, 0 }, { 2987, 2447, 3091, 0, 0 }, { 2988, 2447, 3088, 0, 0 },

    };



















    /**
     * Contains
     *
     * @param ObjectId - The object ID to spawn
     * @param absX - The X position of the object to spawn
     * @param absY - The Y position of the object to spawn
     * @param Z - The Z position of the object to spawn
     * @param face - The position the object will face
     */

    //Objects that are handled by the server on regionchange
    private static final int[][] CUSTOM_OBJECTS_SPAWNS = {


            {26193, 2729, 3469, 0, 3},
            {26194, 2729, 3468, 0, 1},


            {-1, 3092, 3488, 0, 0},

            {-1, 3294, 3946, 0, 0},
            {-1, 3293, 3945, 0, 0},

            {2732, 3276, 3938, 0, 0},


            {170, 3290, 3944, 0, 3},

            {-1, 3283, 3944, 0, 0},
            {-1, 3286, 3944, 0, 0},

            {14859, 3283, 3946, 0, 0},
            {14859, 3284, 3946, 0, 0},
            {14859, 3285, 3946, 0, 0},
            {14859, 3286, 3946, 0, 0},
            {14859, 3287, 3946, 0, 0},
            {11963, 3288, 3946, 0, 0},
            {11963, 3289, 3946, 0, 0},
            {11951, 3290, 3946, 0, 0},

            {4306, 3284, 3942, 0, 0},

            {2646, 3289, 3940, 0, 0},
            {2646, 3288, 3940, 0, 0},
            {2646, 3289, 3941, 0, 0},
            {2646, 3288, 3941, 0, 0},

            {2644, 3285, 3942, 0, 0},

            {1306, 3277, 3940, 0, 0},
            {1306, 3279, 3942, 0, 0},
            {1306, 3281, 3944, 0, 0},
            {-1, 3281, 3941, 0, 0},


            {1, 3302, 9830, 0, 0},


            {-1, 2309, 9804, 0, 0},
            {-1, 2310, 9804, 0, 0},
            {-1, 2308, 9804, 0, 0},
            {-1, 2307, 9804, 0, 0},
            {-1, 2309, 9805, 0, 0},
            {-1, 2310, 9805, 0, 0},
            {-1, 2308, 9805, 0, 0},
            {-1, 2307, 9805, 0, 0},

            {130087, 1231, 3559, 0, 3}, //west raids bank booth

            {2079, 2576, 9876, 0, 0},


            /* ZULRAH */

            {1, 3038, 3415, 0, 0},
            {357, 3034, 3422, 0, 0},
            {25136, 2278, 3070, 0, 0},
            {25136, 2278, 3065, 0, 0},
            {25138, 2273, 3066, 0, 0},
            {25136, 2272, 3065, 0, 0},
            {25139, 2267, 3065, 0, 0},
            {25136, 2260, 3081, 0, 0},
            {401, 3503, 3567, 0, 0},
            {401, 3504, 3567, 0, 0},


            {2274, 2912, 5300, 2, 0},
            {2274, 2914, 5300, 1, 0},
            {2274, 2919, 5276, 1, 0},
            {2274, 2918, 5274, 0, 0},
            {2274, 3001, 3931, 0, 0},
            {-1, 2884, 9797, 0, 2},
            {4483, 2909, 4832, 0, 3},
            {4483, 2901, 5201, 0, 2},
            {4483, 2902, 5201, 0, 2},
            {1662, 3112, 9677, 0, 2},
            {1661, 3114, 9677, 0, 2},
            {1661, 3122, 9664, 0, 1},
            {1662, 3123, 9664, 0, 2},
            {1661, 3124, 9664, 0, 3},
            {4483, 2918, 2885, 0, 3},
            {12356, 3207, 3415, 0, 0},
            {2182, 3206, 3415, 0, 0},
            {2644, 2737, 3444, 0, 0},
            {-1, 2608, 4777, 0, 0},
            {-1, 2601, 4774, 0, 0},
            {-1, 2611, 4776, 0, 0},

            //staffzone
            {2213, 2030, 4498, 0, 0},//bank
            {2213, 2031, 4498, 0, 0},//bank
            {2213, 2032, 4498, 0, 0},//bank
            {2213, 2033, 4498, 0, 0},//bank
            {2213, 2034, 4498, 0, 0},//bank
            {2213, 2035, 4498, 0, 0},//bank
            {2213, 2036, 4498, 0, 0},//bank
            {2213, 2037, 4498, 0, 0},//bank
            {2213, 2038, 4498, 0, 0},//bank
            {2213, 2039, 4498, 0, 0},//bank
            {2213, 2040, 4498, 0, 0},//bank
            {2213, 2041, 4498, 0, 0},//bank
            {2213, 2042, 4498, 0, 0},//bank
            {2213, 2043, 4498, 0, 0},//bank

            {-1, 3084, 3487, 0, 2},

            {2274, 3652, 3488, 0, 0},


            {48675, 2342, 9798, 0, 1}, // demon teleporter home
            {48675, 3228, 9696, 0, 1}, // demon teleporter
            {48675, 3228, 9697, 0, 1}, // demon teleporter

            //{48669, 3090, 3494, 0, 3}, // sapphire zone teleporter

            //{48673, 3090, 3496, 0, 3}, // Ruby evil tree teleport

            // Trees to block for Gold afk tree
            {28474, 3410, 2770, 0, 0},
            {28474, 3410, 2769, 0, 0},
            {28474, 3410, 2768, 0, 0},
            {28474, 3410, 2767, 0, 0},
            {28474, 3410, 2766, 0, 0},
            {28474, 3410, 2765, 0, 0},
            {28474, 3410, 2764, 0, 0},
            {28474, 3410, 2763, 0, 0},
            {28474, 3410, 2763, 0, 0},
            {28474, 3410, 2762, 0, 0},
            {28474, 3410, 2761, 0, 0},

            //{65269, 3414, 2762, 0, 0}, //gold tree

            {11435, 3413, 2768, 0, 0}, //gold tree


            {48673, 2710, 5290, 0, 3}, // block o-city
            {48673, 2725, 5290, 0, 3}, // block o-city

            {22775, 2712, 5325, 0, 2}, // o-city barrier
            {22775, 2714, 5325, 0, 2}, // o-city barrier
            {22775, 2720, 5325, 0, 2}, // o-city barrier	'
            {22775, 2718, 5325, 0, 2}, // o-city barrier
            {22775, 2717, 5325, 0, 2}, // o-city barrier
            {22775, 2716, 5325, 0, 2}, // o-city barrier

            {129777, 1232, 3573, 0, 0}, // Raids gate looks brokenn
            //{-1, 1232, 3573, 0, 0}, // Raids gate / Didnt work


            //gamble zone
            {2213, 2842, 5143, 0, 0},
            {2213, 2843, 5143, 0, 0},
            {2213, 2844, 5143, 0, 0},
            {2213, 2845, 5143, 0, 0},
            {2213, 2846, 5143, 0, 0},
            {2213, 2847, 5143, 0, 0},
            {2213, 2848, 5143, 0, 0},
            {2213, 2849, 5143, 0, 0},
            {2213, 2850, 5143, 0, 0},
            {2213, 2851, 5143, 0, 0},


            {2274, 3652, 3488, 0, 0},
            /**Jail Start*/
            {12269, 3093, 3933, 0, 0},
            {1864, 3093, 3932, 0, 1},//Cell 1
            {1864, 3094, 3932, 0, 1},
            {1864, 3095, 3932, 0, 1},
            {1864, 3096, 3932, 0, 1},
            {1864, 3097, 3932, 0, 1},
            {1864, 3097, 3931, 0, 2},
            {1864, 3097, 3930, 0, 2},
            {1864, 3097, 3929, 0, 2},
            {1864, 3097, 3928, 0, 3},
            {1864, 3096, 3928, 0, 3},
            {1864, 3095, 3928, 0, 3},
            {1864, 3094, 3928, 0, 3},
            {1864, 3093, 3928, 0, 3},
            {1864, 3093, 3929, 0, 4},
            {1864, 3093, 3930, 0, 4},
            {1864, 3093, 3931, 0, 4},
            {1864, 3098, 3932, 0, 1},//Cell 2
            {1864, 3099, 3932, 0, 1},
            {1864, 3100, 3932, 0, 1},
            {1864, 3101, 3932, 0, 1},
            {1864, 3102, 3932, 0, 1},
            {1864, 3102, 3931, 0, 2},
            {1864, 3102, 3930, 0, 2},
            {1864, 3102, 3929, 0, 2},
            {1864, 3102, 3928, 0, 3},
            {1864, 3101, 3928, 0, 3},
            {1864, 3100, 3928, 0, 3},
            {1864, 3099, 3928, 0, 3},
            {1864, 3098, 3928, 0, 3},
            {1864, 3098, 3929, 0, 4},
            {1864, 3098, 3930, 0, 4},
            {1864, 3098, 3931, 0, 4},
            {1864, 3093, 3939, 0, 1},//Cell 3
            {1864, 3094, 3939, 0, 1},
            {1864, 3095, 3939, 0, 1},
            {1864, 3096, 3939, 0, 1},
            {1864, 3097, 3939, 0, 1},
            {1864, 3097, 3938, 0, 2},
            {1864, 3097, 3937, 0, 2},
            {1864, 3097, 3936, 0, 2},
            {1864, 3097, 3935, 0, 3},
            {1864, 3096, 3935, 0, 3},
            {1864, 3095, 3935, 0, 3},
            {1864, 3094, 3935, 0, 3},
            {1864, 3093, 3935, 0, 3},
            {1864, 3093, 3936, 0, 4},
            {1864, 3093, 3937, 0, 4},
            {1864, 3093, 3938, 0, 4},
            {1864, 3098, 3939, 0, 1},//Cell 4
            {1864, 3099, 3939, 0, 1},
            {1864, 3100, 3939, 0, 1},
            {1864, 3101, 3939, 0, 1},
            {1864, 3102, 3939, 0, 1},
            {1864, 3102, 3938, 0, 2},
            {1864, 3102, 3937, 0, 2},
            {1864, 3102, 3936, 0, 2},
            {1864, 3102, 3935, 0, 3},
            {1864, 3101, 3935, 0, 3},
            {1864, 3100, 3935, 0, 3},
            {1864, 3099, 3935, 0, 3},
            {1864, 3098, 3935, 0, 3},
            {1864, 3098, 3936, 0, 4},
            {1864, 3098, 3937, 0, 4},
            {1864, 3098, 3938, 0, 4},
            {1864, 3103, 3932, 0, 1},//Cell 5
            {1864, 3104, 3932, 0, 1},
            {1864, 3105, 3932, 0, 1},
            {1864, 3106, 3932, 0, 1},
            {1864, 3107, 3932, 0, 1},
            {1864, 3107, 3931, 0, 2},
            {1864, 3107, 3930, 0, 2},
            {1864, 3107, 3929, 0, 2},
            {1864, 3107, 3928, 0, 3},
            {1864, 3106, 3928, 0, 3},
            {1864, 3105, 3928, 0, 3},
            {1864, 3104, 3928, 0, 3},
            {1864, 3103, 3928, 0, 3},
            {1864, 3103, 3929, 0, 4},
            {1864, 3103, 3930, 0, 4},
            {1864, 3103, 3931, 0, 4},
            {1864, 3108, 3932, 0, 1},//Cell 6
            {1864, 3109, 3932, 0, 1},
            {1864, 3110, 3932, 0, 1},
            {1864, 3111, 3932, 0, 1},
            {1864, 3112, 3932, 0, 1},
            {1864, 3112, 3931, 0, 2},
            {1864, 3112, 3930, 0, 2},
            {1864, 3112, 3929, 0, 2},
            {1864, 3112, 3928, 0, 3},
            {1864, 3111, 3928, 0, 3},
            {1864, 3110, 3928, 0, 3},
            {1864, 3109, 3928, 0, 3},
            {1864, 3108, 3928, 0, 3},
            {1864, 3108, 3929, 0, 4},
            {1864, 3108, 3930, 0, 4},
            {1864, 3108, 3931, 0, 4},
            {1864, 3103, 3939, 0, 1},//Cell 7
            {1864, 3104, 3939, 0, 1},
            {1864, 3105, 3939, 0, 1},
            {1864, 3106, 3939, 0, 1},
            {1864, 3107, 3939, 0, 1},
            {1864, 3107, 3938, 0, 2},
            {1864, 3107, 3937, 0, 2},
            {1864, 3107, 3936, 0, 2},
            {1864, 3107, 3935, 0, 3},
            {1864, 3106, 3935, 0, 3},
            {1864, 3105, 3935, 0, 3},
            {1864, 3104, 3935, 0, 3},
            {1864, 3103, 3935, 0, 3},
            {1864, 3103, 3936, 0, 4},
            {1864, 3103, 3937, 0, 4},
            {1864, 3103, 3938, 0, 4},
            {1864, 3108, 3939, 0, 1},//Cell 8
            {1864, 3109, 3939, 0, 1},
            {1864, 3110, 3939, 0, 1},
            {1864, 3111, 3939, 0, 1},
            {1864, 3112, 3939, 0, 1},
            {1864, 3112, 3938, 0, 2},
            {1864, 3112, 3937, 0, 2},
            {1864, 3112, 3936, 0, 2},
            {1864, 3112, 3935, 0, 3},
            {1864, 3111, 3935, 0, 3},
            {1864, 3110, 3935, 0, 3},
            {1864, 3109, 3935, 0, 3},
            {1864, 3108, 3935, 0, 3},
            {1864, 3108, 3936, 0, 4},
            {1864, 3108, 3937, 0, 4},
            {1864, 3108, 3938, 0, 4},
            {1864, 3113, 3932, 0, 1},//Cell 9
            {1864, 3114, 3932, 0, 1},
            {1864, 3115, 3932, 0, 1},
            {1864, 3116, 3932, 0, 1},
            {1864, 3117, 3932, 0, 1},
            {1864, 3117, 3931, 0, 2},
            {1864, 3117, 3930, 0, 2},
            {1864, 3117, 3929, 0, 2},
            {1864, 3117, 3928, 0, 3},
            {1864, 3116, 3928, 0, 3},
            {1864, 3115, 3928, 0, 3},
            {1864, 3114, 3928, 0, 3},
            {1864, 3113, 3928, 0, 3},
            {1864, 3113, 3929, 0, 4},
            {1864, 3113, 3930, 0, 4},
            {1864, 3113, 3931, 0, 4},
            {1864, 3113, 3939, 0, 1},//Cell 10

            {1864, 3114, 3939, 0, 1},
            {1864, 3115, 3939, 0, 1},
            {1864, 3116, 3939, 0, 1},
            {1864, 3117, 3939, 0, 1},
            {1864, 3117, 3938, 0, 2},
            {1864, 3117, 3937, 0, 2},
            {1864, 3117, 3936, 0, 2},
            {1864, 3117, 3935, 0, 3},
            {1864, 3116, 3935, 0, 3},
            {1864, 3115, 3935, 0, 3},
            {1864, 3114, 3935, 0, 3},
            {1864, 3113, 3935, 0, 3},
            {1864, 3113, 3936, 0, 4},
            {1864, 3113, 3937, 0, 4},
            {1864, 3113, 3938, 0, 4},

    };

    public static boolean cloudExists(Location loc) {
        return getCloudObject(loc);
    }


    public static boolean getCloudObject(Location loc) {
        for (GameObject objects : CUSTOM_OBJECTS) {
            if (objects.inLocation(objects.getPosition().getX(), objects.getPosition().getY(), Locations.Location.ZULRAH_CLOUD_FIVE)) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }


    public static void zulrahToxicClouds(final GameObject cloud, final Player player, final int cycles) {
        player.setInteractingObject(cloud);
        spawnGlobalObject(cloud);
        TaskManager.submit(new Task(1) {

            int tick = cycles;

            @Override
            public void execute() {
                if (tick > 0 && player.cloudsSpawned()) {
                    tick--;
                    return;
                }
                deleteGlobalObject(cloud);
                player.setCloudsSpawned(false);
                if (player.getInteractingObject() != null
                        && player.getInteractingObject().getId() == 11700) {
                    player.setInteractingObject(null);
                }
                this.stop();
            }

            @Override
            public void stop() {
                setEventRunning(false);
            }
        });

    }

    public static void spawnTempObject(final GameObject ob, final Player player, final int cycles) {
        spawnGlobalObject(ob);
        TaskManager.submit(new Task(cycles) {
            @Override
            public void execute() {
                deleteGlobalObject(ob);
                if (player.getInteractingObject() != null && player.getInteractingObject().getId() == 32000) {
                    player.setInteractingObject(null);
                }
                this.stop();
            }

            @Override
            public void stop() {
                setEventRunning(false);
            }
        });
    }

    public static void addDeletedObject(GameObject gameObject) {
        for(GameObject object : OBJECTS_TO_DELETE) {
            if(object.getId() == gameObject.getId() && gameObject.getPosition().getX() == object.getPosition().getX() && gameObject.getPosition().getY() == object.getPosition().getY()) {
                return;
            }
        }

        OBJECTS_TO_DELETE.add(gameObject);
    }

    public static void removeDeletedObject(GameObject gameObject) {
        for(GameObject object : OBJECTS_TO_DELETE) {
            if(object.getId() == gameObject.getId() && gameObject.getPosition().getX() == object.getPosition().getX() && gameObject.getPosition().getY() == object.getPosition().getY()) {
                OBJECTS_TO_DELETE.remove(object);
            }
        }
    }
}
