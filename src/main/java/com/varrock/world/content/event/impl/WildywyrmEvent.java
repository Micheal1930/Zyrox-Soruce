package com.varrock.world.content.event.impl;

import java.util.concurrent.TimeUnit;

import com.varrock.util.Misc;
import com.varrock.world.World;
import com.varrock.world.content.Wildywyrm;
import com.varrock.world.content.event.GameEvent;
import com.varrock.world.entity.impl.npc.NPC;

/**
 * The wildywyrm boss event.
 *
 * @author Gabriel || Wolfsdarker
 */
public class WildywyrmEvent extends GameEvent {

    /**
     * The delay between the events in minutes.
     */
    private static final int delay = 30;

    /**
     * The wildywyrm's location.
     */
    private static Wildywyrm.WildywyrmLocation currentLocation;

    /**
     * The wildywyrm NPC.
     */
    private NPC wildywyrm;

    /**
     * The hitpoints that was last broadcasted.
     */
    private int lastHitpointsBroadcasted;

    /**
     * If the event is over.
     */
    private boolean over;

    /**
     * Constructor for the event.
     */
    public WildywyrmEvent() {
        super("Wildywyrm Boss", TimeUnit.MINUTES.toMillis(delay));
    }

    /**
     * Returns the wildywyrms location.
     *
     * @return the location
     */
    public static Wildywyrm.WildywyrmLocation getCurrentLocation() {
        return currentLocation;
    }

    @Override
    public boolean canStart() {

        if (wildywyrm == null) {
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

        currentLocation = Misc.randomElement(Wildywyrm.LOCATIONS);
        wildywyrm = NPC.of(Wildywyrm.NPC_ID, currentLocation);
        World.register(wildywyrm);
        lastHitpointsBroadcasted = wildywyrm.getDefaultConstitution();
        sendMessage("Wildywyrm boss event has begun! It is located around " + currentLocation.getLocation() + "!");

        World.getPlayers().forEach(
                p -> p.getPacketSender().sendString(26706, "@or2@WildyWyrm: @gre@" + currentLocation.getLocation() + ""));

        return true;
    }

    @Override
    public void process() {

        if (wildywyrm.getConstitution() < 1 || wildywyrm.isDying()) {
            over = true;
        }

    }

    @Override
    public void end() {
        wildywyrm = null;
        over = false;
        sendMessage("The wildywyrm has been defeated!");
        lastHitpointsBroadcasted = 0;
        currentLocation = null;
        setLastEventInstant(System.currentTimeMillis());
    }

    @Override
    public void preEventMessages(long timeLeft) {
        int secondsLeft = (int) TimeUnit.MILLISECONDS.toSeconds(timeLeft);

        if (secondsLeft == 600) {
            sendMessage("Wildywyrm boss event will start in 10 minutes!");
        } else if (secondsLeft == 300) {
            sendMessage("Wildywyrm boss event will start in less than 5 minutes! Get ready!");
        } else if (secondsLeft == 60) {
            sendMessage("Wildywyrm boss event is about to begin!");
        }
    }

    @Override
    public void eventMessages(long timeLeft) {

        if (wildywyrm == null || wildywyrm.isDying()) {
            return;
        }

        int currentHitpoints = wildywyrm.getConstitution();

        if (currentHitpoints == lastHitpointsBroadcasted) {
            return;
        }

        if (currentHitpoints <= wildywyrm.getDefaultConstitution() * 0.75 && lastHitpointsBroadcasted > wildywyrm.getDefaultConstitution() * 0.75) {
            sendMessage("The wildywyrm has lost 25% of his health!");
            lastHitpointsBroadcasted = currentHitpoints;
        } else if (currentHitpoints <= wildywyrm.getDefaultConstitution() * 0.5 && lastHitpointsBroadcasted > wildywyrm.getDefaultConstitution() * 0.5) {
            sendMessage("The wildywyrm is half dead!");
            lastHitpointsBroadcasted = currentHitpoints;
        } else if (currentHitpoints <= wildywyrm.getDefaultConstitution() * 0.25 && lastHitpointsBroadcasted > wildywyrm.getDefaultConstitution() * 0.25) {
            sendMessage("The wildywyrm is nearly dead!");
            lastHitpointsBroadcasted = currentHitpoints;
        }
    }
}
