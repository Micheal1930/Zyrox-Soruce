package com.varrock.world.content;

import com.varrock.util.Misc;
import com.varrock.world.World;
import com.varrock.world.content.event.impl.GalvekEvent;
import com.varrock.world.content.event.impl.WildywyrmEvent;
import com.varrock.world.content.minigames.impl.Nomad;
import com.varrock.world.content.minigames.impl.RecipeForDisaster;
import com.varrock.world.content.skill.impl.slayer.KonarQuoMaten.SlayerTask;
import com.varrock.world.entity.impl.player.Player;

public class PlayerPanel {

    public static void refreshPanel(Player player) {
        /**
         * General info
         */
        player.getPacketSender().sendString(26701, "@or2@  @whi@ <img=42>General Information");

        if (ShootingStar.CRASHED_STAR == null) {
            player.getPacketSender().sendString(26703, "@or2@Crashed star: @gre@N/A");
        } else {
            player.getPacketSender().sendString(26703,
                    "@or2@Crashed star: @gre@" + ShootingStar.CRASHED_STAR.getStarLocation().playerPanelFrame + "");
        }

        if (EvilTrees.SPAWNED_TREE == null) {
            player.getPacketSender().sendString(26704, "@or2@Evil Tree: @gre@N/A");
        } else {
            player.getPacketSender().sendString(26704,
                    "@or2@Evil Tree: @gre@" + EvilTrees.SPAWNED_TREE.getTreeLocation().playerPanelFrame + "");
        }

        if (WildywyrmEvent.getCurrentLocation() == null) {
            player.getPacketSender().sendString(26706, "@or2@WildyWyrm: @gre@N/A");
        } else {
            player.getPacketSender().sendString(26706,
                    "@or2@WildyWyrm: @gre@" + WildywyrmEvent.getCurrentLocation().getLocation() + "");
        }

        if (GalvekEvent.getLocation() == null) {
            player.getPacketSender().sendString(26707, "@or2@Galvek: @gre@N/A");
        } else {
            player.getPacketSender().sendString(26707,
                    "@or2@Galvek: @gre@" + GalvekEvent.getLocation().getName() + "");
        }

        World.getWell().refreshPanel(player);

        player.getPacketSender().sendString(26709, "@or2@  <img=41></img>@whi@ Account Information");

        /**
         * Account info
         */

        player.getPacketSender().sendString(26711, "@or2@Username:  @gre@" + player.getUsername());
        player.getPacketSender().sendString(26713, "@or2@Donated:  @gre@$" + player.getAmountDonated());

        player.getPacketSender().sendString(26716, "@or2@Music:  @gre@" + (player.musicActive() ? "On" : "Off") + "");
        player.getPacketSender().sendString(26717, "@or2@Sounds:  @gre@" + (player.soundsActive() ? "On" : "Off") + "");
        player.getPacketSender().sendString(26715,
                "@or2@Exp Lock:  @gre@" + (player.experienceLocked() ? "Locked" : "Unlocked") + "");

        /**
         * Points
         */
        // player.getPacketSender().sendString(39174, "@or3@ - @whi@ Statistics");

        /**
         * Slayer
         */
        player.getPacketSender().sendString(26736, "@or2@  <img=39>@whi@ Slayer Information");

        player.getPacketSender().sendString(26737, "@or2@Master: @gre@"
                + Misc.formatText(player.getSlayer().getSlayerMaster().toString().toLowerCase().replaceAll("_", " ")));

        if (!player.getSlayer().getTask().equals(SlayerTask.NO_TASK)) {
            player.getPacketSender().sendString(26738,
                    "@or2@Task: @gre@"
                            + Misc.formatText(
                            player.getSlayer().getTask().toString().toLowerCase().replaceAll("_", " "))
                            + "s");
        } else {
            player.getPacketSender().sendString(26738,
                    "@or2@Task: @gre@"
                            + Misc.formatText(
                            player.getSlayer().getSlayerTask().toString().toLowerCase().replaceAll("_", " "))
                            + "s");
        }

        player.getPacketSender().sendString(26739, "@or2@Task Streak: @gre@" + player.getSlayer().getTaskStreak() + "");
        player.getPacketSender().sendString(26740,
                "@or2@Task Amount: @gre@" + player.getSlayer().getAmountToSlay() + "");
        if (player.getSlayer().getDuoPartner() != null)
            player.getPacketSender().sendString(26741,
                    "@or2@Duo Partner: @gre@" + player.getSlayer().getDuoPartner() + "");
        else
            player.getPacketSender().sendString(26741, "@or2@Duo Partner: @gre@None");

        player.getPacketSender().sendString(26743, "@or2@  <img=39>@whi@ Tools Information");
        player.getPacketSender().sendString(26744, "@or2@Drop Log");
        player.getPacketSender().sendString(26745, "@or2@Kill Log");
        player.getPacketSender().sendString(26746, "@or2@Npc Drop Table");
        player.getPacketSender().sendString(26747, "@or2@Coming Soon");
        player.getPacketSender().sendString(26748, " ");

        /**
         * Quests
         */
        player.getPacketSender().sendString(26749, "@or2@  <img=39>@whi@ Quests Information");
        player.getPacketSender().sendString(26750, RecipeForDisaster.getQuestTabPrefix(player) + "Recipe For Disaster");
        player.getPacketSender().sendString(26751, Nomad.getQuestTabPrefix(player) + "Nomad's Requeim");

        /**
         * Links
         */
        player.getPacketSender().sendString(39202, "@or3@ - @whi@ Links");
        player.getPacketSender().sendString(39203, "@or2@Forum");
        player.getPacketSender().sendString(39204, "@or2@Rules");
        player.getPacketSender().sendString(39205, "@or2@Store");
        player.getPacketSender().sendString(39206, "@or2@Vote");
        player.getPacketSender().sendString(39207, "@or2@Hiscores");
        player.getPacketSender().sendString(39208, "@or2@Report");
    }

}
