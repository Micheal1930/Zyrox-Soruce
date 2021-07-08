package com.zyrox.world.content;

import com.zyrox.util.Misc;
import com.zyrox.world.World;
import com.zyrox.world.content.event.impl.GalvekEvent;
import com.zyrox.world.content.event.impl.WildywyrmEvent;
import com.zyrox.world.content.minigames.impl.Nomad;
import com.zyrox.world.content.minigames.impl.RecipeForDisaster;
import com.zyrox.world.content.skill.impl.slayer.KonarQuoMaten;
import com.zyrox.world.entity.impl.player.Player;

/**
 * Created by Jonny on 8/27/2019
 **/
public class PlayerTextUpdate {

    public static void updatePanel(Player player) {
        int id = 23607;

        player.getPacketSender().sendString(57003, "Players:  @gre@" + World.getPlayersOnline());
        player.getPacketSender().sendString(57042, ""+ player.getRights() + "");
        player.getPacketSender().sendString(57043, ""+ player.getRights() + "");
        player.getPacketSender().sendString(57044, ""+ player.getRights() + "");

        player.getPacketSender().sendString(id += 2, "Zyrox")
                .sendString(id += 1, "@or2@Players Online: @or4@"+World.getPlayersOnline())
                .sendString(id += 2, "@or2@Wilderness: @or4@"+ World.getWildernessCount())
                .sendString(id += 2, "@or2@Staff Online: @or4@"+World.getStaffOnline())
                .sendString(id += 2, "Points")
                .sendString(id += 1, "@or2@Prestige Points: @or4@" + Misc.insertCommasToNumber(player.getPointsHandler().getPrestigePoints()))
                .sendString(id += 2, "@or2@Trivia Points: @or4@" + Misc.insertCommasToNumber(player.getPointsHandler().getTriviaPoints()))
                .sendString(id += 2, "@or2@Commendations: @or4@ " + Misc.insertCommasToNumber(player.getPointsHandler().getCommendations()))
                .sendString(id += 2, "@or2@Loyalty Points: @or4@" + Misc.insertCommasToNumber(player.getPointsHandler().getLoyaltyPoints()))
                .sendString(id += 2, "@or2@Bossing Points: @or4@ " + Misc.insertCommasToNumber(player.getBossPoints()))
                .sendString(id += 2, "@or2@Dung. Tokens: @or4@ " + Misc.insertCommasToNumber(player.getPointsHandler().getDungeoneeringTokens()))
                .sendString(id += 2, "@or2@Voting Points: @or4@ " + Misc.insertCommasToNumber(player.getPointsHandler().getVotingPoints()))
                .sendString(id += 2, "@or2@Slayer Points: @or4@" + Misc.insertCommasToNumber(player.getPointsHandler().getSlayerPoints()))
                .sendString(id += 2, "@or2@Placeholder")
                .sendString(id += 2, "Statistics")
                .sendString(id += 1, "@or2@Killstreak: @or4@" + Misc.insertCommasToNumber(player.getPlayerKillingAttributes().getKillstreak()))
                .sendString(id += 2, "@or2@Kills: @or4@" + Misc.insertCommasToNumber(player.getPlayerKillingAttributes().getPlayerKills()))
                .sendString(id += 2, "@or2@Deaths: @or4@" + Misc.insertCommasToNumber(player.getPlayerKillingAttributes().getPlayerDeaths()))
                .sendString(id += 2, "@or2@Arena Victories: @or4@" + Misc.insertCommasToNumber(player.getDueling().arenaStats[0]))
                .sendString(id += 2, "@or2@Arena Losses: @or4@" + Misc.insertCommasToNumber(player.getDueling().arenaStats[1]))
                .sendString(id += 2, "@or2@Time played:  @or4@"+Misc.getTimePlayed((player.getTotalPlayTime() + player.getRecordedLogin().elapsed())))
                .sendString(id += 2, "Personal")
                .sendString(id += 1, "@or2@Name: @or4@" + player.getName())
                .sendString(id += 2, "@or2@Donated: @or4@" + Misc.insertCommasToNumber(player.getAmountDonated()))
                .sendString(id += 2, "@or2@Rank: @or4@" + Misc.formatText(player.getRights().toString().toLowerCase()))
                .sendString(id += 2, "@or2@Email: @or4@" + (player.getEmailAddress() == null || player.getEmailAddress().equals("null") ? "-" : player.getEmailAddress()))
                .sendString(id += 2, "Events")
                .sendString(id += 1, "@or2@Star: @or4@"+(ShootingStar.CRASHED_STAR == null ? "N/A" : ShootingStar.CRASHED_STAR.getStarLocation().playerPanelFrame))
                .sendString(id += 2, "@or2@Evil Tree: @or4@"+(EvilTrees.SPAWNED_TREE == null ? "N/A" : EvilTrees.SPAWNED_TREE.getTreeLocation().playerPanelFrame))
                .sendString(id += 2, "@or2@WildyWyrm: @or4@"+(WildywyrmEvent.getCurrentLocation() == null ? "N/A" : WildywyrmEvent.getCurrentLocation().getLocation()))
                .sendString(id += 2, "@or2@Galvek: @or4@"+(GalvekEvent.getLocation() == null ? "N/A" : GalvekEvent.getLocation().getName()))
                .sendString(id += 2, "Slayer")
                .sendString(id += 1, "@or2@Master: @or4@"+Misc.formatText(player.getSlayer().getSlayerMaster().toString().toLowerCase().replaceAll("_", " ")))
                .sendString(id += 2, "@or2@Task: @or4@"+
                        (!player.getSlayer().getTask().equals(KonarQuoMaten.SlayerTask.NO_TASK)
                                ? Misc.formatText(player.getSlayer().getTask().toString().toLowerCase().replaceAll("_", " ")) + "s"
                                : Misc.formatText(player.getSlayer().getSlayerTask().toString().toLowerCase().replaceAll("_", " ")) + "s"))
                .sendString(id += 2, "@or2@Task Streak: @or4@"+Misc.insertCommasToNumber(player.getSlayer().getTaskStreak()))
                .sendString(id += 2, "@or2@Task Amount: @or4@"+Misc.insertCommasToNumber(player.getSlayer().getAmountToSlay()))
                .sendString(id += 2, "@or2@Partner: @or4@"+(player.getSlayer().getDuoPartner() != null ? player.getSlayer().getDuoPartner() : "None"))
                ;

        id = 23811;
        player.getPacketSender().sendString(id, "Drop Log")
        .sendString(id += 3, "Kill Log")
        .sendString(id += 3, "Npc Drop Table")
        .sendString(id += 3, "Npc Drop Simulator")
        .sendString(id += 3, "Sounds: "+(player.soundsActive() ? "@gre@On" : "@red@Off"))
        .sendString(id += 3, "Music: "+(player.musicActive() ? "@gre@On" : "@red@Off"))
        .sendString(id += 3, "EXP Lock: "+(player.experienceLocked() ? "@red@Locked" : "@gre@Unlocked"))
                ;

        id = 50502;
        player.getPacketSender().sendString(id, RecipeForDisaster.getQuestTabPrefix(player) + "Recipe For Disaster")
        .sendString(id += 2, Nomad.getQuestTabPrefix(player) + "Nomad's Requeim")
        ;

    }
}
