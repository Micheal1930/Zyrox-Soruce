package com.zyrox.world.content.well_of_goodwill;

import com.google.common.collect.ConcurrentHashMultiset;
import com.google.common.collect.Multiset;
import com.zyrox.GameServer;
import com.zyrox.engine.task.TaskManager;
import com.zyrox.util.Misc;
import com.zyrox.world.World;
import com.zyrox.world.content.Achievements;
import com.zyrox.world.content.TextScroll;
import com.zyrox.world.content.Achievements.AchievementData;
import com.zyrox.world.content.dialogue.DialogueManager;
import com.zyrox.world.entity.impl.player.Player;

/**
 * @author lare96 <http://github.com/lare96>
 */
public final class WellOfGoodwill {

    private static final int MIN_DONATE_AMOUNT = 1_000_000;
    private static final int ANNOUNCE_AT = 25_000_000;

    final WellDatabase wellDatabase = new WellDatabase(this);
    final Multiset<String> playersDonated = ConcurrentHashMultiset.create();
    private WellBenefit currentBenefit = WellBenefit.NONE;
    WellResetTask wellResetTask;

    public void sendMessage(Player player, Object msg) {
        player.getPacketSender().sendMessage("<img=10> <col=6666FF>The Well of Goodwill " + msg.toString());
    }

    public void sendGlobalMessage(Object msg) {
        World.sendMessage("<img=10> <col=6666FF>[ WOG ]: " + msg.toString());
    }

    public void load() {
        wellDatabase.load();
        currentBenefit = WellBenefit.getBenefit(getDonated());
        if (isActive()) {
            startWellTimer();
        }
    }

    public void save() {
        wellDatabase.save();
    }

    public boolean isActive(WellBenefit benefit) {
        WellBenefit currentBenefit = getCurrentBenefit();
        return currentBenefit != WellBenefit.NONE && benefit.triggerAmount <= currentBenefit.triggerAmount;
    }

    public boolean isActive() {
        return getCurrentBenefit() != WellBenefit.NONE;
    }

    public boolean isFull() {
        return getCurrentBenefit() == WellBenefit.getLastBenefit();
    }

    public int getDonated() {
        return playersDonated.size();
    }

    public int getCoinsNeeded() {
        return WellBenefit.getLastBenefit().triggerAmount - getDonated();
    }

    public void view(Player player) {
        if (isWellClosed(player)) {
            return;
        }
        DialogueManager.start(player, new ViewWellDialogue(this));
    }

    public boolean isWellClosed(Player player) {
        if (isFull()) {
            DialogueManager.start(player, new WellFullDialogue(this));
            return true;
        }
        return false;
    }

    public void add(Player player, int amount) {
        if (isWellClosed(player)) {
            return;
        }
        if (amount < MIN_DONATE_AMOUNT) {
            DialogueManager.sendStatement(player, "You must donate at least " + Misc.format(MIN_DONATE_AMOUNT) + " coins.");
            return;
        }
        if (amount > getCoinsNeeded()) {
            amount = getCoinsNeeded();
            player.sendMessage("The well is too full to hold your complete donation. Only " + Misc.format(amount) + " coins will be removed from your coin pouch/inventory.");
        }
        String username = player.getUsername();
        boolean usePouch = player.getMoneyInPouch() >= amount;
        if (!usePouch && player.getInventory().getAmount(995) < amount) {
            DialogueManager.sendStatement(player, "You do not have that much money in your inventory or money pouch.");
            return;
        }
        if (usePouch) {
            player.setMoneyInPouch(player.getMoneyInPouch() - amount);
            player.getPacketSender().sendString(8135, player.getMoneyInPouch());
        } else {
            player.getInventory().delete(995, amount);
        }
        playersDonated.add(username, amount);
        final int finalAmount = amount;
        GameServer.getEngine().submit(() -> wellDatabase.saveOne(username, finalAmount));

        if (amount > ANNOUNCE_AT) {
            sendGlobalMessage(player.getUsername() + " has donated " + Misc.format(amount) + " coins!");
        }
        DialogueManager.sendStatement(player, "Thank you for your donation.");
        Achievements.finishAchievement(player, AchievementData.FILL_WELL_OF_GOODWILL_1M);
        Achievements.doProgress(player, AchievementData.FILL_WELL_OF_GOODWILL_50M, amount);
        Achievements.doProgress(player, AchievementData.FILL_WELL_OF_GOODWILL_250M, amount);
        Achievements.doProgress(player, AchievementData.FILL_WELL_OF_GOODWILL_1000M, amount);

        WellBenefit nextBenefit = WellBenefit.getBenefit(getDonated());
        if (nextBenefit.triggerAmount > getCurrentBenefit().triggerAmount) {
            startWellTimer();
            setCurrentBenefit(nextBenefit);
            sendGlobalMessage("A new benefit has been granted for 2 hours ~ " + currentBenefit.description + "!");
        }
    }

    public boolean isBonusLoyaltyPoints(Player player) {
        return isFull() && playersDonated.contains(player.getUsername());
    }

    public String getRemainingTime(String defaultStr) {
        if (wellResetTask == null) {
            throw new IllegalStateException("The Well of Goodwill not active!");
        }
        String str = wellResetTask.getRemainingTime();
        return str == null ? defaultStr : str;
    }

    public void displayStatus(Player player) {
        int totalDonated = getDonated();
        String donatedFormatted = totalDonated > 0 ? "@mag@" + Misc.format(totalDonated) + "@mag@" : "no";

        TextScroll scroll = new TextScroll("Well of Goodwill ~ Status");
        scroll.addLine("There are " + donatedFormatted + " coins in the well.");
        scroll.newLine();
        if (isActive()) {
            scroll.addLine("The well is currently @mag@active@mag@.");
            scroll.addLine("The benefits:");
            WellBenefit.forActiveBenefits(totalDonated,
                    benefit -> scroll.addLine("~ " + benefit.description));
            scroll.addLine("Will expire in @mag@" + getRemainingTime("@red@a few seconds@red@") + ".");
        } else {
            scroll.addLine("The well is currently @red@inactive@red@.");
        }
        scroll.display(player);
    }

    private void startWellTimer() {
        if (wellResetTask != null) {
            wellResetTask.stop();
        }
        wellResetTask = new WellResetTask(this);
        TaskManager.submit(wellResetTask);
    }

    public void refreshPanel(Player player) {
        if (World.getWell().isActive()) {
            String str = "@or2@Well of Goodwill: @gre@Active";
            int currentBenefits = WellBenefit.getActiveBenefits(getDonated()).size();
            if (currentBenefits > 0) {
                int totalBenefits = WellBenefit.ALL.size() - 1;
                str += " (" + currentBenefits + "/" + totalBenefits + ")";
            }
            player.getPacketSender().sendString(26705, str);
        } else {
            player.getPacketSender().sendString(26705, "@or2@Well of Goodwill: @gre@N/A");
        }
    }

    public void setCurrentBenefit(WellBenefit currentBenefit) {
        this.currentBenefit = currentBenefit;
        World.getPlayers().forEach(this::refreshPanel);
    }

    public WellBenefit getCurrentBenefit() {
        return currentBenefit;
    }
}
