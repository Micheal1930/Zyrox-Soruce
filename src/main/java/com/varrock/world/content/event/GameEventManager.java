package com.varrock.world.content.event;

import com.google.common.reflect.ClassPath;
import com.varrock.engine.task.Task;
import com.varrock.engine.task.TaskManager;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * The event manager.
 *
 * @author Gabriel || Wolfsdarker
 */
public class GameEventManager {

    /**
     * Logger for the game event manager.
     */
    private static final Logger log = Logger.getLogger(GameEventManager.class.getName());

    /**
     * The location for the events.
     */
    private static final String eventsLocation = GameEventManager.class.getPackage().getName() + ".impl";

    /**
     * The current events.
     */
    private static final HashMap<String, GameEvent> events = new HashMap<>(0);

    /**
     * The delay to start the events.
     */
    private static final long startDelay = TimeUnit.SECONDS.toMillis(90);

    /**
     * The instant the events got loaded
     */
    private static long loadInstant;

    /**
     * Return the game events.
     *
     * @return the events
     */
    public static HashMap<String, GameEvent> getEvents() {
        return events;
    }

    /**
     * Loads all the events.
     */
    public static void loadEvents() {
        loadInstant = System.currentTimeMillis();
        try {
            ClassPath cp = ClassPath.from(GameEvent.class.getClassLoader());
            for (ClassPath.ClassInfo ci : cp.getTopLevelClasses(eventsLocation)) {
                Class c = Class.forName(ci.getName());
                if (c != null && c.getSuperclass() == GameEvent.class) {
                    GameEvent event = (GameEvent) Class.forName(ci.getName()).newInstance();
                    events.put(event.name(), event);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        log.info("Loaded " + events.size() + " game events.");
        log.info("Starting all game events in " + startDelay + " seconds.");
        TaskManager.submit(eventTask);
    }

    /**
     * The task to process all events.
     */
    private static final Task eventTask = new Task(1) {
        @Override
        protected void execute() {

            if (System.currentTimeMillis() - loadInstant < startDelay) {
                return;
            }

            if (events.isEmpty()) {
                return;
            }

            for (GameEvent event : events.values()) {
                if (event.isActive()) {
                    if (event.isOver()) {
                        event.end();
                        event.setActive(false);
                        continue;
                    }
                    event.process();
                    event.eventMessages(event.getEventDuration() + event.getLastEventInstant() - System.currentTimeMillis());
                } else {
                    if (System.currentTimeMillis() - event.getLastEventInstant() > event.getDelayBetweenEvents()) {
                        if (event.canStart() && event.start()) {
                            event.setActive(true);
                            event.setLastEventInstant(System.currentTimeMillis());
                        }
                    } else {
                        event.preEventMessages(event.getDelayBetweenEvents() + event.getLastEventInstant() - System.currentTimeMillis());
                    }

                }
            }
        }
    };
}
