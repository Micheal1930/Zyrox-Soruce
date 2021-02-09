package com.zyrox.world.content;

import com.zyrox.model.Animation;
import com.zyrox.model.GameObject;
import com.zyrox.model.Position;
import com.zyrox.util.Misc;
import com.zyrox.util.Stopwatch;
import com.zyrox.world.World;
import com.zyrox.world.entity.impl.player.Player;

/**
 * Evil Tree's Spawning every 40 minutes
 **/
/*@author Levi <www.rune-server.org/members/AuguryPS>
 */

/*
 * Evil Trees
 * Object id: 11434
 */

public class EvilTrees {


    public static final int MAX_CUT_AMOUNT = 2500;//Amount of logs the tree will give before
    private static final int TIME = 4000000; //40 minutes? not sure lol
    //despawning
    public static EvilTree SPAWNED_TREE = null;
    public static Stopwatch timer = new Stopwatch().reset();
    private static LocationData LAST_LOCATION = null;

    public static LocationData getRandom() {
        LocationData tree = LocationData.values()[Misc.getRandom(LocationData.values().length - 1)];
        return tree;
    }

    /*
     * Sequences the spawning so you don't have the same location back to back
     *
     */
    public static void sequence() {
        if (SPAWNED_TREE == null) {
            if (timer.elapsed(TIME)) {
                LocationData locationData = getRandom();
                if (LAST_LOCATION != null) {
                    if (locationData == LAST_LOCATION) {
                        locationData = getRandom();
                    }
                }
                LAST_LOCATION = locationData;
                SPAWNED_TREE = new EvilTree(new GameObject(11434, locationData.spawnPos), locationData);
                CustomObjects.spawnGlobalObject(SPAWNED_TREE.treeObject);
                World.sendMessage("<img=731> <shad=228B22>The Evil Tree has sprouted " + locationData.clue + "!</col>");
                World.getPlayers().forEach(p -> p.getPacketSender().sendString(26704, "@or2@Evil Tree: @gre@" + EvilTrees.SPAWNED_TREE.getTreeLocation().playerPanelFrame + ""));

                timer.reset();
            }
        } else {
            if (SPAWNED_TREE.treeObject.getCutAmount() >= MAX_CUT_AMOUNT) {
                despawn(false);
                timer.reset();
            }
        }
    }

    /*
     * Handles the despawning of the tree
     * and resets the timer
     */
    public static void despawn(boolean respawn) {
        if (respawn) {
            timer.reset(0);
        } else {
            timer.reset();
        }
        if (SPAWNED_TREE != null) {

            CustomObjects.deleteGlobalObject(SPAWNED_TREE.treeObject);
            SPAWNED_TREE = null;

            for (Player p : World.getPlayers()) {
                if (p == null) {
                    continue;
                }
                if (p.getInteractingObject() != null && p.getInteractingObject().getId() == SPAWNED_TREE.treeObject.getId()) {
                    p.performAnimation(new Animation(65535));
                    p.getPacketSender().sendClientRightClickRemoval();
                    p.getSkillManager().stopSkilling();
                    p.getPacketSender().sendMessage("<img=483> <shad=1><col=FF9933> [ EVIL TREE ]: </col>The Evil Tree has been chopped down.");
                    p.getPacketSender().sendString(26704, "@or2@Evil Tree: @gre@" + EvilTrees.SPAWNED_TREE.getTreeLocation().playerPanelFrame + "");
                    PlayerPanel.refreshPanel(p);
                }
            }
        }
    }

    /*
     * Holds the location data in an enum for where the treee's will spawn
     *
     */
    public enum LocationData {

        LOCATION_1(new Position(3052, 3516), "Outside of the monastery", "Monastery"),
        LOCATION_2(new Position(3093, 3535), "In the wilderness (Level 2)", "Edge wildy bank"),
        LOCATION_3(new Position(2470, 5166), "Somewhere in the Tzhaar-Dungeon", "TzHaar dungeon"),
        LOCATION_4(new Position(3321, 3238), "In the Duel Arena", "Duel Arena"),
        LOCATION_5(new Position(2928, 3453), "In the taverley entrance", "Taverley"),
        LOCATION_6(new Position(2782, 3483), "East of Camelot castle", "Camelot"),
        LOCATION_7(new Position(2994, 3376), "In the Falador Garden", "Falador"),
        LOCATION_8(new Position(3211, 3420), "at Varrock square", "Varrock"),
        LOCATION_9(new Position(3270, 3925), "at Rogues Castle", "Rogues Castle");

        public String playerPanelFrame;
        private Position spawnPos;
        private String clue;
        private LocationData(Position spawnPos, String clue, String playerPanelFrame) {
            this.spawnPos = spawnPos;
            this.clue = clue;
            this.playerPanelFrame = playerPanelFrame;
        }
    }

    public static class EvilTree {

        private GameObject treeObject;
        private LocationData treeLocation;
        public EvilTree(GameObject treeObject, LocationData treeLocation) {
            this.treeObject = treeObject;
            this.treeLocation = treeLocation;
        }

        public GameObject getTreeObject() {
            return treeObject;
        }

        public LocationData getTreeLocation() {
            return treeLocation;
        }
    }
}

