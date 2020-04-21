package com.varrock.world.content.combat.pvp;

import java.util.ArrayList;
import java.util.List;

import com.varrock.GameSettings;
import com.varrock.model.GroundItem;
import com.varrock.model.Item;
import com.varrock.model.Locations.Location;
import com.varrock.util.Misc;
import com.varrock.world.World;
import com.varrock.world.content.Achievements;
import com.varrock.world.content.Artifacts;
import com.varrock.world.content.LoyaltyProgramme;
import com.varrock.world.content.Achievements.AchievementData;
import com.varrock.world.content.LoyaltyProgramme.LoyaltyTitles;
import com.varrock.world.content.well_of_goodwill.WellBenefit;
import com.varrock.world.entity.impl.GroundItemManager;
import com.varrock.world.entity.impl.player.Player;

public class PlayerKillingAttributes {

    private final Player player;
    private Player target;
    private int playerKills;
    private int killstreak;
    private int playerDeaths;
    private int targetPercentage;
    private long lastPercentageIncrease;
    private int safeTimer;

    Location loc;

    private final int WAIT_LIMIT = 2;
    private List<String> killedPlayers = new ArrayList<String>();

    public PlayerKillingAttributes(Player player) {
        this.player = player;
    }

    public void add(Player other) {

        if (other.getAppearance().getBountyHunterSkull() >= 0)
            other.getAppearance().setBountyHunterSkull(-1, true);

        boolean target = player.getPlayerKillingAttributes().getTarget() != null && player.getPlayerKillingAttributes().getTarget().getIndex() == other.getIndex() || other.getPlayerKillingAttributes().getTarget() != null && other.getPlayerKillingAttributes().getTarget().getIndex() == player.getIndex();
        if (target)
            killedPlayers.clear();

        if (killedPlayers.size() >= WAIT_LIMIT) {
            killedPlayers.clear();
            handleReward(other, target);
        } else {
            if (!killedPlayers.contains(other.getUsername()))
                handleReward(other, target);
            else
                player.getPacketSender().sendMessage("You were not given points because you have recently defeated " + other.getUsername() + ".");
        }
        Item item1 = new Item(995, 100000 + Misc.getRandom(300000));

        if (target)

            GroundItemManager.spawnGroundItem(player, new GroundItem(item1, player.getPosition(),
                    player.getUsername(), false, 150, false, 200));

        BountyHunter.resetTargets(player, other, true, "You have defeated your target!");
    }

    /**
     * Gives the player a reward for defeating his opponent
     *
     * @param other
     */
    private void handleReward(Player o, boolean targetKilled) {
        if (!o.getSerialNumber().equals(player.getSerialNumber()) && !player.getHostAddress().equalsIgnoreCase(o.getHostAddress()) && player.getLocation() == Location.WILDERNESS) {
            if (!killedPlayers.contains(o.getUsername()))
                killedPlayers.add(o.getUsername());
            player.getPacketSender().sendMessage(getRandomKillMessage(o.getUsername()));

            this.killstreak += 1;

            int wildernessLevel = player.getWildernessLevel();
            boolean inHotspot = player.getPosition().isWithinBoundary(3023, 3143, 3078, 3552);

            int maxBloodMoney;

            if(wildernessLevel >= 0 && wildernessLevel < 10) {
                maxBloodMoney = 90;
            } else if(wildernessLevel >= 10 && wildernessLevel < 20) {
                maxBloodMoney = 110;
            } else if(wildernessLevel >= 20 && wildernessLevel < 30) {
                maxBloodMoney = 130;
            } else if(wildernessLevel >= 30 && wildernessLevel < 40) {
                maxBloodMoney = 150;
            } else if(wildernessLevel >= 40 && wildernessLevel < 50) {
                maxBloodMoney = 170;
            } else {
                maxBloodMoney = 200;
            }

            if(inHotspot) {
                maxBloodMoney *= 2;
            }

            int totalBloodMoney = Misc.inclusiveRandom(maxBloodMoney - 30, maxBloodMoney);

            if (World.getWell().isActive(WellBenefit.BLOOD_MONEY)) {
                totalBloodMoney *= 2;
            }

            if(GameSettings.DOUBLE_POINTS) {
                totalBloodMoney *= 2;
            }

            Artifacts.handleDrops(player, o, targetKilled);
            if (player.getAppearance().getBountyHunterSkull() < 4)
                player.getAppearance().setBountyHunterSkull(player.getAppearance().getBountyHunterSkull() + 1, true);

            /** ACHIEVEMENTS AND LOYALTY TITLES **/
            LoyaltyProgramme.unlock(player, LoyaltyTitles.KILLER);
            Achievements.doProgress(player, AchievementData.DEFEAT_10_PLAYERS);
            Achievements.doProgress(player, AchievementData.DEFEAT_30_PLAYERS);
            if (this.playerKills >= 20) {
                LoyaltyProgramme.unlock(player, LoyaltyTitles.SLAUGHTERER);
            }
            if (this.playerKills >= 50) {
                LoyaltyProgramme.unlock(player, LoyaltyTitles.GENOCIDAL);
            }

            for (KillStreakBonus killStreakBonus : KillStreakBonus.values()) {
                if (this.killstreak >= killStreakBonus.getRequiredKills()) {
                    totalBloodMoney += killStreakBonus.getBonusBloodMoney();
                    player.getPacketSender().sendMessage("You've received bonus an additional "+killStreakBonus.getBonusBloodMoney()+" blood money for being on a killstreak of "+this.killstreak+"!");
                    if(this.killstreak == killStreakBonus.getRequiredKills() && killStreakBonus.isAnnounce())
                        World.sendMessage("@blu@[Killstreak]</col> " + player.getUsername() + " is on a kill streak of "+killstreak+"! Kill them to end their streak!");
                    break;
                }
            }

            if (this.killstreak >= 3) {
                Achievements.finishAchievement(player, AchievementData.REACH_A_KILLSTREAK_OF_3);
            }
            if (this.killstreak >= 6) {
                Achievements.finishAchievement(player, AchievementData.REACH_A_KILLSTREAK_OF_6);
            }
            if (this.killstreak >= 15) {
                LoyaltyProgramme.unlock(player, LoyaltyTitles.IMMORTAL);
            }

            GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(43307, totalBloodMoney), o.getPosition(),
                    player.getUsername(), false, 150, false, 200));

            player.sendMessage("<col=C3433D><shad=E5C6C1>You have received "+totalBloodMoney+" blood money for defeating "+o.getUsername()+".");

        }
    }

    public List<String> getKilledPlayers() {
        return killedPlayers;
    }

    public void setKilledPlayers(List<String> list) {
        killedPlayers = list;
    }

    /**
     * Gets a random message after killing a player
     *
     * @param killedPlayer The player that was killed
     */
    public static String getRandomKillMessage(String killedPlayer) {
        int deathMsgs = Misc.getRandom(8);
        switch (deathMsgs) {
            case 0:
                return "With a crushing blow, you defeat " + killedPlayer + ".";
            case 1:
                return "It's humiliating defeat for " + killedPlayer + ".";
            case 2:
                return "" + killedPlayer + " didn't stand a chance against you.";
            case 3:
                return "You've defeated " + killedPlayer + ".";
            case 4:
                return "" + killedPlayer + " regrets the day they met you in combat.";
            case 5:
                return "It's all over for " + killedPlayer + ".";
            case 6:
                return "" + killedPlayer + " falls before you might.";
            case 7:
                return "Can anyone defeat you? Certainly not " + killedPlayer + ".";
            case 8:
                return "You were clearly a better fighter than " + killedPlayer + ".";
        }
        return null;
    }

    public int getPlayerKills() {
        return playerKills;
    }

    public void setPlayerKills(int playerKills) {
        this.playerKills = playerKills;
    }

    public int getKillstreak() {
        return killstreak;
    }

    public void setKillstreak(int killstreak) {
        this.killstreak = killstreak;
    }

    public int getPlayerDeaths() {
        return playerDeaths;
    }


    public void setPlayerDeaths(int playerDeaths) {
        this.playerDeaths = playerDeaths;
    }

    public Player getTarget() {
        return target;
    }

    public void setTarget(Player target) {
        this.target = target;
    }

    public int getTargetPercentage() {
        return targetPercentage;
    }

    public void setTargetPercentage(int targetPercentage) {
        this.targetPercentage = targetPercentage;
    }

    public long getLastTargetPercentageIncrease() {
        return lastPercentageIncrease;
    }

    public void setLastTargetPercentageIncrease(long lastPercentageIncrease) {
        this.lastPercentageIncrease = lastPercentageIncrease;
    }

    public int getSafeTimer() {
        return safeTimer;
    }

    public void setSafeTimer(int safeTimer) {
        this.safeTimer = safeTimer;
    }
}
