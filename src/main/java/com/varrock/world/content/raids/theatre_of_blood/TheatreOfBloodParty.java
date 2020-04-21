package com.varrock.world.content.raids.theatre_of_blood;

import java.util.ArrayList;

import com.varrock.util.Stopwatch;
import com.varrock.world.entity.impl.player.Player;

/**
 * Created by Jonny on 7/8/2019
 **/
public class TheatreOfBloodParty {

    private ArrayList<Player> partyMembers;

    private Player partyLeader;

    private TheatreOfBloodPartyStatus status;

    private Stopwatch joiningTimer;

    private int floor;

    public TheatreOfBloodParty(Player partyLeader) {
        this.partyLeader = partyLeader;
        this.partyMembers = new ArrayList<>(5);
        this.partyMembers.add(partyLeader);
        this.status = TheatreOfBloodPartyStatus.COLLECTING_MEMBERS;
        this.joiningTimer = new Stopwatch();
        this.floor = partyLeader.getIndex() * 4;
    }

    public Player getPartyLeader() {
        if (partyMembers.size() == 0) {
            return null;
        }
        return partyMembers.get(0);
    }

    public void addPlayer(Player toAdd) {
        partyMembers.add(toAdd);
    }

    public void setPartyLeader(Player partyLeader) {
        this.partyLeader = partyLeader;
    }

    public ArrayList<Player> getPartyMembers() {
        return partyMembers;
    }

    public TheatreOfBloodPartyStatus getStatus() {
        return status;
    }

    public void setStatus(TheatreOfBloodPartyStatus status) {
        this.status = status;
    }

    public boolean isLeader(Player player) {
        return player.getName().equalsIgnoreCase(partyLeader.getName());
    }

    public Stopwatch getJoiningTimer() {
        return joiningTimer;
    }

    public int getFloor() {
        return floor;
    }
}
