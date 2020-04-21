package com.varrock.world.content;

import com.varrock.util.Misc;
import com.varrock.util.Stopwatch;
import com.varrock.world.World;

/*
 * @author Aj
 * www.simplicityps.org
 */

public class Reminders {


    private static final int TIME = 1200000; //20 minutes
    private static Stopwatch timer = new Stopwatch().reset();
    public static String currentMessage;
    private static final String prefix = "Server";

    /*
     * Random Message Data
     */
    private static final String[][] MESSAGE_DATA = {
            {"[Varrock]Remember to ::vote for rewards every 12 hours!"},
            {"[Varrock]We also accept OSRS & RS3 gold donations!"},
            {"[Varrock]Use the command 'finddrop monster-name' for drop tables"},
            {"[Varrock]Are you strong enough to defeat Vorkath? ::finddrop vorkath"},
            {"[Varrock]Use the ::help command to ask staff for help"},
            {"[Varrock]There are so many donator benefits! Check it out at ::benefits"},
            {"[Varrock]Read the forums for information at ::forums"},
            {"[Varrock]Register on the forums now! ::forums"},
            {"[Varrock]Dont forget to ::vote and join us at ::discord"},
            {"[Varrock]Vote everyday and spend your points at the vote shop!"},
            {"[Varrock]Earn the youtuber rank by uploading videos about Varrock!"},
            {"[Varrock]Timers for event bosses can be found at ::events"},
            {"[Varrock]Remember to spread the word and invite your friends to play!"},
            {"[Varrock]Claim your vote rewards and Vote today! ::vote"},
            {"[Varrock]Use ::commands to find a list of commands"},
            {"[Varrock]To become staff, it is a must to register on the forums! ::forums"},
            {"[Varrock]Toggle your client settings to your preference in the wrench tab!"},
            {"[Varrock]Register and post your intro on the forums! ::Forums"},
            {"[Varrock]Type ::osrs to toggle your items to oldschool mode!"},
            {"[Varrock]Donating comes with a lot of benefits! ::benefits"},
            {"[Varrock]Donators + can use ::title to set a custom loyalty title!"},
            {"[Varrock]Join our official discord server! ::discord"},
            {"[Varrock]Visit our forums to read all the donator benefits! ::benefits"}


    };

    /*
     * Sequence called in world.java
     * Handles the main method
     * Grabs random message and announces it
     */
    public static void sequence() {
        if (timer.elapsed(TIME)) {
            timer.reset();
            {
                currentMessage = MESSAGE_DATA[Misc.getRandom(MESSAGE_DATA.length - 1)][0];
                World.sendGlobalMessage(prefix, currentMessage);
            }
            new Thread(() -> {
                World.savePlayers();
            }).start();
        }
    }
}