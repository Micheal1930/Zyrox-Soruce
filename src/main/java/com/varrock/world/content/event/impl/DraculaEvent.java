package com.varrock.world.content.event.impl;

import java.util.concurrent.TimeUnit;

import com.varrock.util.Misc;
import com.varrock.world.World;
import com.varrock.world.content.Dracula;
import com.varrock.world.content.Wildywyrm;
import com.varrock.world.content.event.GameEvent;
import com.varrock.world.entity.impl.npc.NPC;

/**
 * The dracula boss event.
 *
 * @author Gabriel || Wolfsdarker
 */
/*public class DraculaEvent extends GameEvent {

    /**
     * The delay between the events in minutes.
     */
  /*  private static final int delay = 30;

    /**
     * The dracula's location.
     */
   /* private static Dracula.DraculaLocation currentLocation;

    /**
     * The dracula NPC.
     */
    /*private NPC dracula;

    /**
     * The hitpoints that was last broadcasted.
     */
   // private int lastHitpointsBroadcasted;

    /**
     * If the event is over.
     */
   // private boolean over;

    /**
     * Constructor for the event.
     */
  //  public DraculaEvent() {
      //  super("Dracula Boss", TimeUnit.MINUTES.toMillis(delay));
  //  }

    /**
     * Returns the wildywyrms location.
     *
     * @return the location
     */
   /*public static Dracula.DraculaLocation getCurrentLocation() {
        return currentLocation;
    }

    @Override
    public boolean canStart() {

        if (dracula == null) {
            return true;
        }

        return false;
    }

    @Override
    public boolean isOver() {
        return super.isOver() || over;
    }

    @Override
    public boolean start() {

        currentLocation = Misc.randomElement(Dracula.LOCATIONS);
        dracula = NPC.of(Dracula.NPC_ID, currentLocation);
        World.register(dracula);
        lastHitpointsBroadcasted = dracula.getDefaultConstitution();
        sendMessage("Dracula boss event has begun! It is located around " + currentLocation.getLocation() + "!");

        return true;
    }

    @Override
    public void process() {

        if (dracula.getConstitution() < 1 || dracula.isDying()) {
            over = true;
        }

    }

    @Override
    public void end() {
        dracula = null;
        over = false;
        sendMessage("The dracula has been defeated!");
        lastHitpointsBroadcasted = 0;
        currentLocation = null;
        setLastEventInstant(System.currentTimeMillis());
    }

    @Override
    public void preEventMessages(long timeLeft) {
        int secondsLeft = (int) TimeUnit.MILLISECONDS.toSeconds(timeLeft);

        if (secondsLeft == 600) {
            sendMessage("Dracula boss event will start in 10 minutes!");
        } else if (secondsLeft == 300) {
            sendMessage("Dracula boss event will start in less than 5 minutes! Get ready!");
        } else if (secondsLeft == 60) {
            sendMessage("Dracula boss event is about to begin!");
        }
    }

    @Override
    public void eventMessages(long timeLeft) {

        if (dracula == null || dracula.isDying()) {
            return;
        }

        int currentHitpoints = dracula.getConstitution();

        if (currentHitpoints == lastHitpointsBroadcasted) {
            return;
        }

        if (currentHitpoints <= dracula.getDefaultConstitution() * 0.75 && lastHitpointsBroadcasted > dracula.getDefaultConstitution() * 0.75) {
            sendMessage("The dracula has lost 25% of his health!");
            lastHitpointsBroadcasted = currentHitpoints;
        } else if (currentHitpoints <= dracula.getDefaultConstitution() * 0.5 && lastHitpointsBroadcasted > dracula.getDefaultConstitution() * 0.5) {
            sendMessage("The dracula is half dead!");
            lastHitpointsBroadcasted = currentHitpoints;
        } else if (currentHitpoints <= dracula.getDefaultConstitution() * 0.25 && lastHitpointsBroadcasted > dracula.getDefaultConstitution() * 0.25) {
            sendMessage("The dracula is nearly dead!");
            lastHitpointsBroadcasted = currentHitpoints;
        }
    }
}
*/