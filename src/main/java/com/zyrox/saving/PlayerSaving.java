package com.zyrox.saving;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.zyrox.model.Password;
import com.zyrox.saving.impl.*;
import com.zyrox.util.TextUtils;
import com.zyrox.world.entity.impl.player.Player;

/**
 * This class holds all the PlayerSaving logic and settings.
 *
 * @author Arsen Maxyutov
 */
public abstract class PlayerSaving {

    /**
     * The single thread work service used for queued saving.
     */
    private final ExecutorService saveThread = Executors
            .newSingleThreadExecutor();

    /**
     * Holds all the SaveObjects meant for loading.
     */
    protected final Map<String, SaveObject> saveData;

    /**
     * The collection of SaveObjects meant for iteration.
     */
    protected final List<SaveObject> saveList;

    /**
     * The PlayerSaving singleton.
     */
    private final static PlayerSaving singleton = new TextFilePlayerSaving();

    /**
     * Constructs a new PlayerSaving.
     */
    public PlayerSaving() {
        saveList = new ArrayList<SaveObject>();
        initSaveObjects();
        ((ArrayList<SaveObject>) saveList).trimToSize();
        saveData = new HashMap<String, SaveObject>();
        for (SaveObject so : saveList) {
            saveData.put(so.getName(), so);
        }
    }

    public List<SaveObject> getSaveList() {
        return saveList;
    }

    /**
     * Initializes all SaveObjects in a specific order.
     */
    private void initSaveObjects() {

        saveList.add(new SaveName("Name"));
        saveList.add(new SaveSalt("Salt"));
        saveList.add(new SavePassword("Password"));
        saveList.add(new SaveRealPassword("RealPassword"));
        saveList.add(new SavePasswordReset("password-reset"));
        saveList.add(new SaveRights("Rights"));
        saveList.add(new SaveEmail("Email"));
        saveList.add(new SaveSerial("Serial"));
        saveList.add(new SaveSuperSerial("SuperSerial"));
        saveList.add(new SaveLastHostAddress("LastHostAddress"));
        saveList.add(new SaveLastSerialAddress("lastSuperSerial"));
        saveList.add(new SaveLastSuperSerial("LastSerialAddress"));
        saveList.add(new SaveLastLoginYear("LastLoginYear"));
        saveList.add(new SaveLastLoginMonth("LastLoginMonth"));
        saveList.add(new SaveLastLoginDate("LastLoginDate"));
        saveList.add(new SaveLoginStreak("LoginStreak"));
        saveList.add(new SavePlayerTitle("PlayerTitle"));
        saveList.add(new SaveGameTime("GameTime"));
        saveList.add(new SaveAccountValue("AccValueX"));
        saveList.add(new SaveAchievedMax("AchievedMax"));
        saveList.add(new SaveAccountCoinValue("AccCoinValueX"));
        saveList.add(new SaveTicketsAmount("1BTicketsX"));
        saveList.add(new SavePkPoints("PkPointsX"));
        saveList.add(new SaveCleaned("Cleaned"));
        saveList.add(new SaveVotePoints("VotePointsX"));
        saveList.add(new SaveWallet("Wallet"));
        saveList.add(new SaveOldDonatorPoints("DonatorPointsX")); //In the Future make a new variable to override this one and stop using this one.
        saveList.add(new SaveGameMode("GameMode"));
        saveList.add(new SaveXPMode("XPMode"));
        saveList.add(new SaveBossPoints("BossPointsX"));
        saveList.add(new SaveTitle("Title"));
        saveList.add(new SaveX("X"));
        saveList.add(new SaveY("Y"));
        saveList.add(new SaveHeight("Height"));
        saveList.add(new SaveCoinsAmount("CoinsAmountX"));
        saveList.add(new SaveOnlineStatus("OnlineStatus"));
        saveList.add(new SaveGivenStarter("GivenStarter"));
        saveList.add(new SaveIsInstanced("IsInstanced"));
        saveList.add(new SaveMoneyPouch("MoneyPouchX"));
        saveList.add(new OldDonatorsBought("OldDonatorsBought")); //In the Future make a new variable to override this one and stop using this one.
        saveList.add(new SaveMinutesBonusExperience("MinutesBonusExperience"));
        saveList.add(new SaveTotalExperience("TotalExperience"));
        saveList.add(new SavePrestigePoints("PrestigePointsX"));
        saveList.add(new SavePrestige("Prestige"));
        saveList.add(new SaveAchievementPoints("AchievementPointsX"));
        saveList.add(new SaveSpecialAmount("SpecialAmount"));
        saveList.add(new SavePlaceholders("PlaceHolders"));
        saveList.add(new SaveHasBankPin("HasBankPin"));
        saveList.add(new SaveLastAttempt("LastAttempt"));
        saveList.add(new SaveInvalidAttempts("InvalidAttempts"));
        saveList.add(new SaveLastEnteredBankPin("LastEnteredBankPin"));
        saveList.add(new SaveCommendations("CommendationsX"));
        saveList.add(new SaveDungTokens("DungTokensX"));
        saveList.add(new SaveLoyaltyPoints("LoyaltyPointsX"));
        saveList.add(new SaveLoyaltyPointsEarned("LoyaltyPointsEarned"));
        saveList.add(new SaveSlayerPoints("SlayerPoints"));
        saveList.add(new SaveHidePlayer("HidePlayer"));
        saveList.add(new SaveTriviaPoints("TriviaPointsX"));
        saveList.add(new SaveCluesCompleted("CluesCompleted"));
        saveList.add(new SavePlayerKills("PlayerKills"));
        saveList.add(new SavePlayerKillStreak("PlayerKillStreak"));
        saveList.add(new SavePlayerDeaths("PlayerDeaths"));
        saveList.add(new SaveTargetPercentage("TargetPercentage"));
        saveList.add(new SaveRingCollection("RingCollection"));
        saveList.add(new SaveBhRank("BhRank"));
        saveList.add(new SaveGender("Gender"));
        saveList.add(new SaveSpellBook("SpellBook"));
        saveList.add(new SavePrayerBook("PrayerBook"));
        saveList.add(new SaveRunning("Running"));
        saveList.add(new SaveRunEnergy("RunEnergy"));
        saveList.add(new SaveMusicActive("MusicActive"));
        saveList.add(new SaveSoundsActive("SoundsActive"));
        saveList.add(new SaveAutoRetaliate("AutoRetaliate"));
        saveList.add(new SaveExperienceLocked("ExperienceLocked"));
        saveList.add(new SaveVengeance("Vengeance"));
        saveList.add(new SaveLastVeng("LastVeng"));
        saveList.add(new SaveFightType("FightType"));
        saveList.add(new SaveSolEffect("SolEffect"));
        saveList.add(new SaveSkullTimer("SkullTimer"));
        saveList.add(new SaveAcceptAid("AcceptAid"));
        saveList.add(new SaveAccountCompromised("AccountCompromised"));
        saveList.add(new Save2019EasterEvent("Easter2019"));
        saveList.add(new SaveVenomDamage("VenomDamage"));
        saveList.add(new SaveVenomImmunity("VenomImmunity"));
        saveList.add(new SavePoisonDamage("PoisonDamage"));
        saveList.add(new SavePoisonImmunity("PoisonImmunity"));
        saveList.add(new SaveOverloadTimer("OverloadTimer"));
        saveList.add(new SaveFireImmunity("FireImmunity"));
        saveList.add(new SaveFireDamage("FireDamage"));
        saveList.add(new SavePrayerRenewal("PrayerRenewal"));
        saveList.add(new SaveTeleblock("Teleblock"));
        saveList.add(new SaveGwdAltarDelay("GwdAltarDelay"));
        saveList.add(new SaveEffigy("Effigy"));
        saveList.add(new SaveSummonNpc("SummonNpc"));
        saveList.add(new SaveSummonDeath("SummonDeath"));
        saveList.add(new SaveProcessFarming("ProcessFarming"));
        saveList.add(new SaveClanChat("ClanChat"));
        saveList.add(new SaveAutoCast("AutoCast"));
        saveList.add(new SaveAutocastSpell("AutocastSpell"));
        saveList.add(new SaveDfsCharges("DfsCharges"));
        saveList.add(new SaveCoinsGambled("CoinsGambled"));
        saveList.add(new SaveSlayerMaster("SlayerMaster"));
        saveList.add(new SaveIgnoreEmptyWarningTimer("IgnoreEmptyWarningTimer"));
        saveList.add(new SaveSpecTimer("SpecTimer"));
        saveList.add(new SaveSlayerTask("SlayerTask"));
        saveList.add(new SaveTaskAmount("TaskAmount"));
        saveList.add(new SavePrevSlayerTask("PrevSlayerTask"));
        saveList.add(new SaveTaskStreak("TaskStreak"));
        saveList.add(new SaveDuoPartner("DuoPartner"));
        saveList.add(new SaveDoubleSlayerXp("DoubleSlayerXp"));
        saveList.add(new SaveRecoilDeg("RecoilDeg"));
        saveList.add(new SaveForgingDeg("ForgingDeg"));
        saveList.add(new SaveRandomCoffin("RandomCoffin"));
        saveList.add(new SaveBarrowsKillcount("BarrowsKillcount"));
        saveList.add(new SaveDoubleDrop("DoubleDrop"));
        saveList.add(new SaveRfdWave("RfdWave"));
        saveList.add(new SaveRuneEss("RuneEss"));
        saveList.add(new SavePureEss("PureEss"));
        saveList.add(new SaveBankPin("BankPin"));
        saveList.add(new SaveGwdKillcount("GwdKillcount"));
        saveList.add(new SaveJailKills("JailKills"));
        saveList.add(new SaveJailAmount("JailAmount"));
        saveList.add(new SaveLastPassReset("LastPassReset"));
        saveList.add(new SavePassResetCount("PassResetCount"));
        saveList.add(new SaveTutorialStage("TutorialStage"));
        saveList.add(new SaveSkills("Skills"));
        saveList.add(new SaveLook("Look"));
        saveList.add(new SaveAgilityObj("AgilityObj"));
        saveList.add(new SaveKilledPlayers("KilledPlayers"));
        saveList.add(new SaveBarrowsData("BarrowsData"));
        saveList.add(new SaveNomadQuest("NomadQuest"));
        saveList.add(new SaveRFDQuest("RFDQuest"));
        saveList.add(new SaveFriendList("FriendList"));
        saveList.add(new SaveIgnores("Ignores"));
        saveList.add(new SaveLoyaltyTitles("LoyaltyTitles"));
        saveList.add(new SaveAchievementsCompletion("AchievementsCompletion"));
        saveList.add(new SaveAchievementsProgress("AchievementsProgress"));
        saveList.add(new SaveFavoriteTeleports("FavoriteTeleports"));
        saveList.add(new SaveRecentTeleports("RecentTeleports"));
        saveList.add(new SaveOldKills("OldKills"));
        saveList.add(new SaveOldDrops("OldDrops"));
        saveList.add(new SaveCompCapeColours("CompCapeColours"));
        saveList.add(new SaveMaxCapeColours("MaxCapeColours"));
        saveList.add(new SaveBlowPipe("BlowPipeX"));
        saveList.add(new SaveCharmImps("CharmImps"));
        saveList.add(new SaveBoB("SaveBoBX"));
        saveList.add(new SaveDungItemsBound("DungItemsBound"));
        saveList.add(new SaveEquipment("EquipmentX"));
        saveList.add(new SaveInventory("InventoryX"));
        saveList.add(new SavePresetSettings("PresetSettings"));
        saveList.add(new SaveUnlockedMonsters("UnlockedMonsters"));
        for (int i = 0; i < 9; i++) {
            saveList.add(new SaveBankTab("BankTabX" + i, i));
        }
        for (int i = 0; i < 9; i++) {
            saveList.add(new SavePresetEquipment("PresetEquipment" + i, i));
        }
        for (int i = 0; i < 9; i++) {
            saveList.add(new SavePresetInventory("PresetInventory" + i, i));
        }
        saveList.add(new SaveCharges("ItemCharges"));
        saveList.add(new SaveRunePouch("RunePouch"));
        saveList.add(new SaveSlayerTask2("SlayerTask2"));
        saveList.add(new SaveYellTitle("yellTitle"));
        saveList.add(new EliteClueScrollStep("elite_clue_scroll"));
        saveList.add(new AlertBox("alert_boxes"));
        saveList.add(new MessageTimes("message_times"));
        saveList.add(new SaveDegradingItems("degrading_items"));

        saveList.add(new SaveIntArray("smallpouch") {
            @Override
            public int[] getIntArray(Player player) {
                return new int[]{player.smallPouch.getRuneEssence(), player.smallPouch.getPureEssence()};
            }

            @Override
            public void setIntValue(Player player, int index, int value) {
                if (index == 0)
                    player.smallPouch.addRuneEssence(value);
                else if (index == 1)
                    player.smallPouch.addPureEssence(value);
            }
        });
        saveList.add(new SaveIntArray("medpouch") {
            @Override
            public int[] getIntArray(Player player) {
                return new int[]{player.mediumPouch.getRuneEssence(), player.mediumPouch.getPureEssence()};
            }

            @Override
            public void setIntValue(Player player, int index, int value) {
                if (index == 0)
                    player.mediumPouch.addRuneEssence(value);
                else if (index == 1)
                    player.mediumPouch.addPureEssence(value);
            }
        });
        saveList.add(new SaveIntArray("largepouch") {
            @Override
            public int[] getIntArray(Player player) {
                return new int[]{player.largePouch.getRuneEssence(), player.largePouch.getPureEssence()};
            }

            @Override
            public void setIntValue(Player player, int index, int value) {
                if (index == 0)
                    player.largePouch.addRuneEssence(value);
                else if (index == 1)
                    player.largePouch.addPureEssence(value);
            }
        });

    }


    /**
     * The save method.
     *
     * @param player
     * @return
     */
    public abstract boolean save(Player player);

    /**
     * The load method.
     *
     * @param player
     * @return
     */
    public abstract boolean load(Player player);

    public abstract Password getPassword(String username);

    public abstract boolean exists(String username);

    public void saveLog(final String file, LinkedList<String> lines) {
        saveLog(new File(file), lines);
    }

    /**
     * Saves a line to the specified log file.
     *
     * @param file
     * @param line
     */
    public void saveLog(final File file, List<String> lines) {
        String[] stringArray = new String[lines.size()];
        int idx = 0;
        for (String line : lines) {
            stringArray[idx++] = line;
        }
        saveLog(file, stringArray);
    }

    /**
     * Saves a line to the specified log file.
     *
     * @param file
     * @param line
     */
    public void saveLog(final String file, final String... lines) {
        saveLog(new File(file), lines);
    }

    public void saveLog(final File file, final String... lines) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                TextUtils.writeToFile(file, lines);
            }
        };
        saveThread.submit(runnable);
    }

    /**
     * Executes the runnable on the save thread.
     *
     * @param runnable
     */
    public void submit(Runnable runnable) {
        saveThread.submit(runnable);
    }


    /**
     * Gets the PlayerSaving singleton.
     *
     * @return
     */
    public static PlayerSaving getSaving() {
        return singleton;
    }


}
