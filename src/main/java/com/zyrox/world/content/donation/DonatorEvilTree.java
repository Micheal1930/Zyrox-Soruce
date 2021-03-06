package com.zyrox.world.content.donation;

import com.zyrox.engine.task.Task;
import com.zyrox.engine.task.TaskManager;
import com.zyrox.model.GameObject;
import com.zyrox.model.Position;
import com.zyrox.util.Stopwatch;
import com.zyrox.util.Time;
import com.zyrox.world.content.CustomObjects;

public class DonatorEvilTree extends Task {

    /**
     * The position for the tree.
     */
    private static final Position treePos = new Position(3420, 2777, 0);

    /**
     * The time the tree will stay alive until respawn.
     */
    private static final long respawnTimer = Time.ONE_MINUTE * 15;

    /**
     * The time the tree will be dead.
     */
    private static final long timeBetweenSpawns = Time.ONE_MINUTE;

    /**
     * The tree's instance.
     */
    private GameObject evilTree;

    /**
     * The stopwatch that handles the tree timers.
     */
    private final Stopwatch actionWatch;

    public DonatorEvilTree() {
        super(10);
        this.actionWatch = new Stopwatch();
        this.actionWatch.reset();
        this.evilTree = new GameObject(11921, treePos, 10, 1);
        CustomObjects.spawnGlobalObject(evilTree);
    }

    @Override
    protected void execute() {

        /*if (actionWatch.elapsed(respawnTimer) && evilTree != null) {
            CustomObjects.deleteGlobalObject(evilTree);
            evilTree = null;
            this.actionWatch.reset();
            return;
        }

        if (actionWatch.elapsed(timeBetweenSpawns) && evilTree == null) {
            evilTree = new GameObject(11921, treePos, 10, 1);
            CustomObjects.spawnGlobalObject(evilTree);
            this.actionWatch.reset();
        }*/
    }

    /**
     * Starts the donator evil tree task.
     */
    public static void start() {
        TaskManager.submit(new DonatorEvilTree());
    }

}
