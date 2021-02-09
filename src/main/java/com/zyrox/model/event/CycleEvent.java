package com.zyrox.model.event;

import java.util.Objects;

/**
 * What the event must implement
 *
 * @author Stuart <RogueX>
 */
public abstract class CycleEvent<T> {

    /**
     * The state of the cycle event, first being idle, never null.
     */
    private CycleEventState state = CycleEventState.IDLE;

    /**
     * Code which should be ran when the event is executed
     *
     * @param container
     */
    public abstract void execute(CycleEventContainer<T> container);

    /**
     * Code which should be ran when the event stops. If this function is overriden,
     */
    public abstract void stop();

    /**
     * The current state of this cycle event.
     *
     * @return the state.
     */
    public CycleEventState getState() {
        return state;
    }

    /**
     * Sets the current state of this event to another state, even itself if needed.
     *
     * @param state the new state of the cycle event.
     */
    public void setState(CycleEventState state) {
        this.state = Objects.requireNonNull(state, "The event state cannot be null.");
    }

}
