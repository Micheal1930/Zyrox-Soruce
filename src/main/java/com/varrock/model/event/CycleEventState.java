package com.varrock.model.event;

/**
 * Created by Jason MK on 2018-11-07 at 10:05 AM
 * <p>
 * The state of a {@link CycleEvent} object that determines where in the lifecycle
 * the event is at whether it be idle, active, or stopped.
 */
public enum CycleEventState {
    /**
     * When an event is first created by not active and running.
     */
    IDLE,

    /**
     * The active state, valid when the event is added to the active queue.
     */
    ACTIVE,

    /**
     * When an event is stopped by any means, this is the state.
     */
    STOPPED
}
