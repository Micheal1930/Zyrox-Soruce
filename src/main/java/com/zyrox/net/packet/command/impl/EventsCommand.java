package com.zyrox.net.packet.command.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.zyrox.model.PlayerRights;
import com.zyrox.net.packet.command.Command;
import com.zyrox.net.packet.command.CommandHeader;
import com.zyrox.world.content.event.GameEvent;
import com.zyrox.world.content.event.GameEventManager;
import com.zyrox.world.entity.impl.npc.DropViewer;
import com.zyrox.world.entity.impl.player.Player;

@CommandHeader(command = {"events"}, description = "Shows all the events in game and their timers.")
public class EventsCommand extends Command {

    public EventsCommand() {
        super(PlayerRights.PLAYER, false);
    }

    @Override
    public boolean execute(Player player, String[] args) throws Exception {

        String title = "@whi@Zyrox Events (" + GameEventManager.getEvents().size() + ")";
        List<String> events = new ArrayList<>();

        int index = 0;

        for (GameEvent event : GameEventManager.getEvents().values()) {
            player.getPacketSender().sendString((index > 50 ? 12174 : 8145) + index++, event.name());
            if (index == 1) {
                index++;
            }
            events.add("@whi@" + event.name());
            if (event.isActive()) {
                events.add("The event is currently @gre@[ Active ]");
            } else {
                events.add("The event will start in: " + getFormatedTime(event));
            }
        }

        player.getPacketSender().resetItemsOnInterface(58800, 200);
        DropViewer.sendItemInfoInterface(player, title, new ArrayList<>(), events);

        return false;
    }

    @Override
    public String[] getUsage() {
        return new String[]{"::events"};
    }

    /**
     * Returns the time for the next event formated.
     *
     * @param event
     */
    private String getFormatedTime(GameEvent event) {

        long timeLeft = event.getLastEventInstant() + event.getDelayBetweenEvents() - System.currentTimeMillis();

        int hoursLeft = (int) TimeUnit.MILLISECONDS.toHours(timeLeft);
        int minutesLeft = (int) TimeUnit.MILLISECONDS.toMinutes(timeLeft) - (hoursLeft * 60);
        int secondsLeft = (int) TimeUnit.MILLISECONDS.toSeconds(timeLeft) - (hoursLeft * 60 * 60) - (minutesLeft * 60);

        if(hoursLeft < 0 || minutesLeft < 0 || secondsLeft < 0) {
            return "Soon";
        }

        return (hoursLeft < 10 ? "0" : "") + hoursLeft + ":" + (minutesLeft < 10 ? "0" : "") + minutesLeft + ":" + (secondsLeft < 10 ? "0" : "") + secondsLeft;
    }
}
