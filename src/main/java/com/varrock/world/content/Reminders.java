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
            {"<col=0037B1>[Varrock]Remember to ::vote for rewards every 12 hours!"},
            {"<col=0037B1>[Varrock]We also accept OSRS & RS3 gold donations!"},
            {"<col=0037B1>[Varrock]Use the command 'finddrop monster-name' for drop tables"},
            {"<col=0037B1>[Varrock]Are you strong enough to defeat Vorkath? ::finddrop vorkath"},
            {"<col=0037B1>[Varrock]Use the ::help command to ask staff for help"},
            {"<col=0037B1>[Varrock]There are so many donator benefits! Check it out at ::benefits"},
            {"<col=0037B1>[Varrock]Read the forums for information at ::forums"},
            {"<col=0037B1>[Varrock]Register on the forums now! ::forums"},
            {"<col=0037B1>[Varrock]Dont forget to ::vote and join us at ::discord"},
            {"<col=0037B1>[Varrock]Vote everyday and spend your points at the vote shop!"},
            {"<col=0037B1>[Varrock]Earn the youtuber rank by uploading videos about Varrock!"},
            {"<col=0037B1>[Varrock]Timers for event bosses can be found at ::events"},
            {"<col=0037B1>[Varrock]Remember to spread the word and invite your friends to play!"},
            {"<col=0037B1>[Varrock]Claim your vote rewards and Vote today! ::vote"},
            {"<col=0037B1>[Varrock]Use ::commands to find a list of commands"},
            {"<col=0037B1>[Varrock]To become staff, it is a must to register on the forums! ::forums"},
            {"<col=0037B1>[Varrock]Toggle your client settings to your preference in the wrench tab!"},
            {"<col=0037B1>[Varrock]Register and post your intro on the forums! ::Forums"},
            {"<col=0037B1>[Varrock]Type ::osrs to toggle your items to oldschool mode!"},
            {"<col=0037B1>[Varrock]Donating comes with a lot of benefits! ::benefits"},
            {"<col=0037B1>[Varrock]Donators + can use ::title to set a custom loyalty title!"},
            {"<col=0037B1>[Varrock]Join our official discord server! ::discord"},
            {"<col=0037B1>[Varrock]Visit our forums to read all the donator benefits! ::benefits"}


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