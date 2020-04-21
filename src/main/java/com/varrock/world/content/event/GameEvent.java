package com.varrock.world.content.event;

import com.varrock.util.Misc;
import com.varrock.world.World;
import com.varrock.world.content.Wildywyrm;
import com.varrock.world.content.event.impl.WildywyrmEvent;

/**
 * The abstract for game events.
 *
 * @author Gabriel || Wolfsdarker
 */
public abstract class GameEvent {

    /**
     * The event's name.
     */
    private final String name;

    /**
     * If the event is active.
     */
    private boolean active = false;

    /**
     * The delay between events.
     */
    protected final long delayBetweenEvents;

    /**
     * The event's duration.
     */
    protected final long eventDuration;

    /**
     * The last event instant.
     */
    protected long lastEventInstant;

    /**
     * The last instant a message was sent.
     */
    protected long lastEventMessage;

    /**
     * Constructor for the event.
     *
     * @param delayBetweenEvents
     */
    public GameEvent(String name, long delayBetweenEvents) {
        this.name = name;
        this.delayBetweenEvents = delayBetweenEvents;
        this.eventDuration = -1;
        this.lastEventInstant = 0L;
        this.lastEventMessage = 0L;
    }

    /**
     * Constructor for the event.
     *
     * @param delayBetweenEvents
     * @param eventDuration
     */
    public GameEvent(String name, long delayBetweenEvents, long eventDuration) {
        this.name = name;
        this.delayBetweenEvents = delayBetweenEvents;
        this.eventDuration = eventDuration;
        this.lastEventInstant = 0L;
    }

    /**
     * The event's name.
     *
     * @return the name
     */
    public String name() {
        return name;
    }

    /**
     * Returns if the event is active.
     *
     * @return if is active
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Returns if the event is over.
     *
     * @return if is over
     */
    public boolean isOver() {
        return eventDuration > 0 && (System.currentTimeMillis() > getLastEventInstant() + eventDuration);
    }

    /**
     * Returns the delay between events.
     *
     * @return the delay in milliseconds
     */
    public long getDelayBetweenEvents() {
        return delayBetweenEvents;
    }

    /**
     * Returns the duration of the event.
     *
     * @return the duration in milliseconds
     */
    public long getEventDuration() {
        return eventDuration;
    }

    /**
     * Returns the last instant the event was executed.
     *
     * @return the last instant
     */
    public long getLastEventInstant() {
        return lastEventInstant;
    }

    /**
     * Sets if the event is active.
     *
     * @param active the active status
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * Sets the instant the event last executed.
     *
     * @param lastEventInstant
     */
    public void setLastEventInstant(long lastEventInstant) {
        this.lastEventInstant = lastEventInstant;
    }

    /**
     * If the event can start.
     *
     * @return if it can be started
     */
    public abstract boolean canStart();

    /**
     * Executed when the event is started.
     *
     * @return if the event was started
     */
    public abstract boolean start();

    /**
     * The process for the event to be executed every tick.
     */
    public abstract void process();

    /**
     * Executed when the event is ended.
     */
    public abstract void end();

    /**
     * Sends a message to all players based on the time left for the event to start.
     *
     * @param timeLeft
     */
    public abstract void preEventMessages(long timeLeft);

    /**
     * Sends a message to all players when the event is happening.
     *
     * @param timeLeft
     */
    public abstract void eventMessages(long timeLeft);

    /**
     * Sends the message to all players.
     *
     * @param message
     */
    protected void sendMessage(String message) {
        if (System.currentTimeMillis() - lastEventMessage < 1000) {
            return;
        }

        String icon = Misc.getEventIcon();

     ///   if(this instanceof DraculaEvent) {
        //    icon = Misc.getHalloweenIcon();
       // }

        if(this instanceof WildywyrmEvent) {
            icon = Misc.getWildywyrmIcon();
        }

        World.sendMessage(icon + message);
        lastEventMessage = System.currentTimeMillis();
    }

}
