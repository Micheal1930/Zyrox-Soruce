package com.zyrox.world.content.event.impl;

import static com.zyrox.util.Misc.inclusiveRandom;

import java.util.concurrent.TimeUnit;

import com.zyrox.model.Position;
import com.zyrox.util.NameUtils;
import com.zyrox.world.World;
import com.zyrox.world.content.Galvek;
import com.zyrox.world.content.combat.strategy.impl.GalvekCombatStrategy;
import com.zyrox.world.content.event.GameEvent;

public class GalvekEvent extends GameEvent {

    private static GalvekEvent instance = new GalvekEvent();

    public static GalvekEvent getInstance() {
        return instance;
    }

    /**
     * The delay between the events in minutes.
     */
    private static final int delay = 30;

    /**
     * Galvek's location.
     */
    private static Locations location;

    /**
     * The galvek NPC.
     */
    public Galvek galvek;

    /**
     * If the event is over.
     */
    private boolean over;

    private boolean firstStageFinished, finalStageStarted;


    /**
     * Constructor for the event.
     */
    public GalvekEvent() {
        super("Galvek Boss", TimeUnit.MINUTES.toMillis(delay));
    }

    /**
     *
     * @return the location
     */
    public static Locations getLocation() {
        return location;
    }

    @Override
    public boolean canStart() {
        if (galvek == null) {
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

        location = Locations.values()[inclusiveRandom(0, Locations.values().length - 1)];
        galvek = new Galvek(GalvekCombatStrategy.Stage.ORANGE, location.getPosition());
        World.register(galvek);
        sendMessage("Galvek event has started! The boss is located near " + location.getName() + "!");

        World.getPlayers().forEach(
                p -> p.getPacketSender().sendString(26707, "@or2@Galvek: @gre@" + location.getName() + ""));

        return true;
    }

    @Override
    public void process() {

        if (galvek.stage == GalvekCombatStrategy.Stage.GREEN) {
            if (galvek.getConstitution() < 1 || galvek.isDying()) {
                over = true;
            }
        }

    }

    @Override
    public void end() {
        galvek = null;
        over = false;
        sendMessage("Galvek has been defeated!");
        location = null;
        setLastEventInstant(System.currentTimeMillis());
    }

    @Override
    public void preEventMessages(long timeLeft) {
        int secondsLeft = (int) TimeUnit.MILLISECONDS.toSeconds(timeLeft);

        if (secondsLeft == 600) {
            sendMessage("Galvek boss event will start in 10 minutes!");
        } else if (secondsLeft == 300) {
            sendMessage("Galvek boss event will start in less than 5 minutes! Get ready!");
        } else if (secondsLeft == 60) {
            sendMessage("Galvek boss event is about to begin!");
        }
    }

    @Override
    public void eventMessages(long timeLeft) {
//        System.out.println("Galvek - " + galvek.stage.name());
        if (galvek == null || galvek.isDying()) {
            return;
        }
        if (galvek.stage == GalvekCombatStrategy.Stage.BLUE) {
            //orange is done - we will flag 0 on 2nd phase spawn.
            if (!firstStageFinished) {
                firstStageFinished = true;
                sendMessage("Galvek's first phase was defeated!.");
            }
        }
        if (galvek.stage == GalvekCombatStrategy.Stage.GREEN) {
            if (!finalStageStarted) {
                finalStageStarted = true;
                sendMessage("Galvek is on his final stage. Be careful of his dragon fire!");
            }
        }
    }

    public enum Locations {

        CHAOS_TEMPLE(new Position(3227, 3654)),
        LAVA_DRAGON_ISLE(new Position(3200, 3876));

        Locations(Position position) {
            this.position = position;
        }

        private Position position;

        public Position getPosition() {
            return position;
        }

        public String getName() {
            return NameUtils.capitalizeWords(name().toLowerCase().replaceAll("_", " "));
        }
    }
}
