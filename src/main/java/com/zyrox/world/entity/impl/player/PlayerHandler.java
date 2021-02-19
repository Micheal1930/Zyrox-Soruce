package com.zyrox.world.entity.impl.player;

import com.zyrox.GameServer;
import com.zyrox.GameSettings;
import com.zyrox.Server;
import com.zyrox.engine.task.TaskManager;
import com.zyrox.engine.task.impl.*;
import com.zyrox.model.*;
import com.zyrox.model.container.impl.Bank;
import com.zyrox.model.container.impl.Equipment;
import com.zyrox.model.definitions.WeaponAnimations;
import com.zyrox.model.definitions.WeaponInterfaces;
import com.zyrox.model.log.LogField;
import com.zyrox.model.log.LogFieldType;
import com.zyrox.model.log.impl.ConnectionLog;
import com.zyrox.model.log.impl.SuspiciousLoginLog;
import com.zyrox.net.PlayerSession;
import com.zyrox.net.SessionState;
import com.zyrox.util.Misc;
import com.zyrox.world.World;
import com.zyrox.world.content.Achievements;
import com.zyrox.world.content.BankPin;
import com.zyrox.world.content.BonusManager;
import com.zyrox.world.content.EffectTimer;
import com.zyrox.world.content.PlayerLogs;
import com.zyrox.world.content.PlayerPanel;
import com.zyrox.world.content.StaffList;
import com.zyrox.world.content.alchemical_hydra.KaruulmSlayerHydra;
import com.zyrox.world.content.auction_house.AuctionHouseManager;
import com.zyrox.world.content.clan.ClanChatManager;
import com.zyrox.world.content.combat.effect.CombatPoisonEffect;
import com.zyrox.world.content.combat.effect.CombatTeleblockEffect;
import com.zyrox.world.content.combat.effect.CombatVenomEffect;
import com.zyrox.world.content.combat.magic.Autocasting;
import com.zyrox.world.content.combat.prayer.CurseHandler;
import com.zyrox.world.content.combat.prayer.PrayerHandler;
import com.zyrox.world.content.combat.pvp.BountyHunter;
import com.zyrox.world.content.combat.range.DwarfMultiCannon;
import com.zyrox.world.content.combat.weapon.CombatSpecial;
import com.zyrox.world.content.dialogue.Dialogue;
import com.zyrox.world.content.dialogue.DialogueExpression;
import com.zyrox.world.content.dialogue.DialogueManager;
import com.zyrox.world.content.dialogue.DialogueType;
import com.zyrox.world.content.dropchecker.NPCDropTableChecker;
import com.zyrox.world.content.hiscores.Hiscores;
import com.zyrox.world.content.inferno.Inferno;
import com.zyrox.world.content.instances.BossInstance;
import com.zyrox.world.content.instances.InstanceManager;
import com.zyrox.world.content.minigames.impl.Barrows;
import com.zyrox.world.content.skill.impl.farming.FarmingManager;
import com.zyrox.world.content.skill.impl.hunter.Hunter;
import com.zyrox.world.content.skill.impl.runecrafting.RunecraftingPouches;
import com.zyrox.world.content.skill.impl.slayer.Slayer;
import com.zyrox.world.content.skill.impl.summoning.BossPets;
import com.zyrox.world.content.tutorial.TutorialStages;
import com.zyrox.world.content.well_of_goodwill.WellOfGoodwill;

public class PlayerHandler {

    /**
     * Gets the player according to said name.
     *
     * @param name The name of the player to search for.
     * @return The player who has the same name as said param.
     */
    public static Player getPlayerForName(String name) {
        for (Player player : World.getPlayers()) {
            if (player == null)
                continue;
            if (player.getUsername().equalsIgnoreCase(name))
                return player;
        }
        return null;
    }

    public static void handleLogin(Player player) {

        player.setPassiveRegenCounter(System.currentTimeMillis());
        player.setActive(true);
        World.getPlayers().add(player);
        
      /*  if (!GameSettings.CHECKED_IPS.contains(player.getHostAddress())) {
            new Thread(() -> {
                Misc.detectVPN(player.getHostAddress(), player.getName());
            }).start();
        }*/


        player.getSession().setState(SessionState.LOGGED_IN);

        player.getPacketSender().sendMapRegion().sendDetails();

        player.getRecordedLogin().reset();

        player.getRunePouch().refreshItems();

        player.getPacketSender().sendTabs();

        for (int i = 0; i < player.getBanks().length; i++) {
            if (player.getBank(i) == null) {
                player.setBank(i, new Bank(player));
            }
        }
        player.getInventory().refreshItems();
        player.getEquipment().refreshItems();

        WeaponAnimations.assign(player, player.getEquipment().get(Equipment.WEAPON_SLOT));
        WeaponInterfaces.assign(player, player.getEquipment().get(Equipment.WEAPON_SLOT));
        CombatSpecial.updateBar(player);
        BonusManager.update(player);
        player.getDailyLogin().LoggedIn();

        player.getSummoning().login();

        Slayer.checkDuoSlayer(player, true);
        for (Skill skill : Skill.values()) {
            player.getSkillManager().updateSkill(skill);
        }

        player.getRelations().setPrivateMessageId(1).onLogin(player).updateLists(true);

        player.getPacketSender().sendConfig(172, player.isAutoRetaliate() ? 1 : 0)
                .sendTotalXp(player.getSkillManager().getTotalGainedExp())
                .sendConfig(player.getFightType().getParentId(), player.getFightType().getChildId()).sendRunStatus()
                .sendRunEnergy(player.getRunEnergy()).sendConstitutionOrbPoison(player.isPoisoned())
                .sendConstitutionOrbVenom(player.isVenomed()).sendString(8135, "" + player.getMoneyInPouch())
                .sendInteractionOption("Follow", 3, false)
                .sendInteractionOption("Trade With", 4, false)
                .sendInterfaceRemoval()
                .sendString(39161, "@or2@Server time: @or2@[ @yel@" + Misc.getCurrentServerTime() + "@or2@ ]");

        player.getPA().setInterfaceActions(20703, "Value", "Buy 1", "Buy 5", "Buy 10", "Buy X");

        Autocasting.onLogin(player);
        PrayerHandler.deactivateAll(player);
        CurseHandler.deactivateAll(player);
        BonusManager.sendCurseBonuses(player);
        Achievements.updateInterface(player);
        Barrows.updateInterface(player);

        TaskManager.submit(new PlayerSkillsTask(player));
        TaskManager.submit(new ZulrahClouds(player));
        if (player.isPoisoned()) {
            TaskManager.submit(new CombatPoisonEffect(player));
        }
        if (player.isVenomed()) {
            TaskManager.submit(new CombatVenomEffect(player));
        }
        if (player.getPrayerRenewalPotionTimer() > 0) {
            TaskManager.submit(new PrayerRenewalPotionTask(player));
        }
        if (player.getOverloadPotionTimer() > 0) {
            TaskManager.submit(new OverloadPotionTask(player));
        }
        if (player.getTeleblockTimer() > 0) {
            TaskManager.submit(new CombatTeleblockEffect(player));
        }
        if (player.getSkullTimer() > 0) {
            player.setSkullIcon(1);
            TaskManager.submit(new CombatSkullEffect(player));
        }
        if (player.getFireImmunity() > 0) {
            FireImmunityTask.makeImmune(player, player.getFireImmunity(), player.getFireDamageModifier());
        }
        if (player.getSpecialPercentage() < 100) {
            TaskManager.submit(new PlayerSpecialAmountTask(player));
        }
        if (player.hasStaffOfLightEffect()) {
            TaskManager.submit(new StaffOfLightSpecialAttackTask(player));
        }
        if (player.getMinutesBonusExp() >= 0) {
            TaskManager.submit(new BonusExperienceTask(player));
        }

        player.getPacketSender().sendPrestige(player.prestige);

        player.getUpdateFlag().flag(Flag.APPEARANCE);

        Locations.login(player);

        if (player.didReceiveStarter() == false) {
            // player.getInventory().add(995, 1000000).add(15501, 1).add(1153, 1).add(1115,
            // 1).add(1067, 1).add(1323, 1).add(1191, 1).add(841, 1).add(882, 50).add(1167,
            // 1).add(1129, 1).add(1095, 1).add(1063, 1).add(579, 1).add(577, 1).add(1011,
            // 1).add(1379, 1).add(556, 50).add(558, 50).add(557, 50).add(555, 50).add(1351,
            // 1).add(1265, 1).add(1712, 1).add(11118, 1).add(1007, 1).add(1061,
            // 1).add(1419, 1);

            // player.setReceivedStarter(true);
        }
        // DialogueManager.start(player, 177);
        if (player.getStoredRuneEssence() > 0 && !player.getBank().isFull()) {
            player.getBank().add(RunecraftingPouches.RUNE_ESS, player.getStoredRuneEssence());
        }
        if (player.getStoredPureEssence() > 0 && !player.getBank().isFull()) {
            player.getBank().add(RunecraftingPouches.PURE_ESS, player.getStoredPureEssence());
        }
        if (KaruulmSlayerHydra.isWithinHydraLair(player.getPosition().getX(), player.getPosition().getY())) {
            player.moveTo(player.getPosition().transform(player.getPosition().getX() <= 1355 ? 1 : -1, 0, 0), true);
        }
        player.getPacketSender().sendMessage("<col=0000ff><img=475> Welcome to Zyrox<col=000000> - There is currently <col=0000ff> " + World.getPlayersOnline() + " playes online.");
        
        if(GameSettings.DOUBLE_EXPERIENCE)
            player.getPacketSender().sendMessage("<col=7b450c><img=690> Double experience is activated!");

        if(GameSettings.DOUBLE_DROP_RATE)
            player.getPacketSender().sendMessage("<col=7b450c><img=464> Double drop rate is active!");

        if(GameSettings.DOUBLE_POINTS)
            player.getPacketSender().sendMessage("<col=7b450c><img=464> Double points & blood money is active!");

        if (GameServer.isBeta()) {
            player.getPacketSender().sendMessage("<col=7b450c> This is the beta server. Things may be a little different!");
        }

      //  player.getPacketSender().sendMessage("<col=7b450c><img=475> Make an introduction @ ::intro to get a free mystery box!");

        if (GameSettings.DOUBLE_VOTING) {
            player.sendMessage(
                    "<col=7b450c><shad=1><img=473> - Double Voting is currently ON!");
        }

        if (player.experienceLocked()) {
            player.getPacketSender().sendMessage("@red@Warning: your experience is currently locked.");
        }
        ClanChatManager.handleLogin(player);

        if (GameSettings.isBonusXp()) {
            player.getPacketSender().sendEffectTimer((int) GameSettings.getBonusXpSecondsLeft(),
                    EffectTimer.EXPERIENCE);
        } else {
            player.getPacketSender().sendEffectTimer(0, EffectTimer.EXPERIENCE);
        }

        WellOfGoodwill well = World.getWell();
        if (well.isActive()) {
            well.sendMessage(player, "The rewards will expire in " + well.getRemainingTime("a few moments") + "!");
        }

        PlayerPanel.refreshPanel(player);

        if(player.getClientVersion() < GameSettings.CLIENT_VERSION) {
            DialogueManager.start(player, new Dialogue() {
                @Override
                public DialogueType type() {
                    return DialogueType.NPC_STATEMENT;
                }

                @Override
                public DialogueExpression animation() {
                    return DialogueExpression.NORMAL;
                }

                @Override
                public String[] dialogue() {
                    return new String[]{"It seems as if your client is outdated!", "Restart your client to apply the latest updates", "or download them from the website.", "If you don't some things may not seem right in Varrock!"};
                }

                @Override
                public int npcId() {
                    return 5057;
                }
            });
        }

        player.getPacketSender().updateSpecialAttackOrb()
                .sendIronmanMode(player.getGameMode().getId())
                .sendPrestige(player.prestige);

        if (player.getRights() == PlayerRights.MODERATOR || player.getRights() == PlayerRights.GLOBAL_MOD || player.getRights() == PlayerRights.ADMINISTRATOR
                || player.getRights() == PlayerRights.SUPPORT
                || player.getRights() == PlayerRights.DEVELOPER || player.getRights() == PlayerRights.OWNER) {
            if (!StaffList.staff.contains(player.getUsername())) {
                StaffList.login(player);
            }
        }

        if (player.getPointsHandler().getAchievementPoints() == 0) {
            Achievements.setPoints(player);
        }

        NPCDropTableChecker.getSingleton().refreshDropTableChilds(player);

        PlayerLogs.log(player.getUsername(),
                "Login from host " + player.getHostAddress() + ", serial number: " + player.getSerialNumber());

        if (player.newPlayer()) {
            player.newPlayerTimer.reset();

            player.setLoginReward(true);
        }

        if(player.getTutorialStage() != TutorialStages.COMPLETED) {
            if(player.getTutorialStage() == null) {
                player.setTutorialStage(TutorialStages.INITIAL_STAGE);
            }
            player.getTutorialStage().sendDialogueText(player);
        } else {
            if (player.requiresUnlocking()) {
                BankPin.init(player, false);
            }
        }

        FarmingManager.uponLogin(player);
        AuctionHouseManager.onLogin(player);

        new ConnectionLog().submit(
                new LogField("username", LogFieldType.STRING, player.getName()),
                new LogField("action", LogFieldType.STRING, "login"),
                new LogField("ip_address", LogFieldType.STRING, player.getHostAddress()),
                new LogField("serial_address", LogFieldType.STRING, player.getSuperSerialNumber()),
                new LogField("time", LogFieldType.STRING, Misc.getTime())
        );

        Achievements.finishAchievement(player, Achievements.AchievementData.ENTER_THE_LOTTERY_THREE_TIMES);
        Achievements.finishAchievement(player, Achievements.AchievementData.ENTER_THE_LOTTERY);

        if(player.getLastSerialNumber() == null || player.getLastSerialNumber().equals("null"))
            player.setLastSerialNumber(player.getLastSerialNumber());

        if(player.getLastSuperSerial() == null || player.getLastSuperSerial().equals("null"))
            player.setLastSuperSerial(player.getSuperSerialNumber());

        if(player.getLastHostAddress() == null || player.getLastHostAddress().equals("null"))
            player.setLastHostAddress(player.getHostAddress());

        if (!player.getLastHostAddress().equalsIgnoreCase(player.getHostAddress()) && !player.getSuperSerialNumber().equalsIgnoreCase(player.getLastSuperSerial())) {

            World.sendStaffMessage("<img=483> [ HIGH SECURITY ] @dre@" + player.getName() + " has logged in with a different ip address than usual.");

            new SuspiciousLoginLog(player.getName(), player.getHostAddress(), player.getLastHostAddress(), player.getSuperSerialNumber(), player.getLastSuperSerial(), Misc.getTime()).submit();
        }

        System.out.println("[LOGGED IN] " + player.getUsername() + " | Players Online: " + World.getPlayersOnline());
    }

    public static boolean handleLogout(Player player) {
        try {

            PlayerSession session = player.getSession();

            if (session.getChannel() != null && session.getChannel().isOpen()) {
                session.getChannel().close();
            }

            if (!player.isRegistered()) {
                return true;
            }
            
            boolean exception = GameServer.isUpdating()
                    || World.getLogoutQueue().contains(player) && player.getLogoutTimer().elapsed(90000);
            if (player.logout() || exception) {
                if(!Server.isLocal)

                // System.out.println("[World] Deregistering player - [username, host] : [" +
                // player.getUsername() + ", " + player.getHostAddress() + "]");
                player.getSession().setState(SessionState.LOGGING_OUT);
                player.setTotalPlayTime(player.getTotalPlayTime() + player.getRecordedLogin().elapsed());
                player.getPacketSender().sendInterfaceRemoval();
                BossPets.onLogout(player);
                if (player.getSpectateTarget() != null) {
                    player.setSpectateTarget(null);
                    if (player.getPreviousTile() != null) {
                        player.moveTo(player.getPreviousTile());
                    } else {
                        player.moveTo(new Position(2551, 2550,1));
                    }
                }
                if (player.getCannon() != null) {
                    DwarfMultiCannon.pickupCannon(player, player.getCannon(), true);
                }
                if (exception && player.getResetPosition() != null) {
                    player.moveTo(player.getResetPosition());
                    player.setResetPosition(null);
                }
                if (player.getRegionInstance() != null) {
                    player.getRegionInstance().destruct();
                }
                if (player.getRights() == PlayerRights.MODERATOR || player.getRights() == PlayerRights.GLOBAL_MOD || player.getRights() == PlayerRights.ADMINISTRATOR
                        || player.getRights() == PlayerRights.SUPPORT
                        || player.getRights() == PlayerRights.DEVELOPER || player.getRights() == PlayerRights.OWNER) {
                    StaffList.logout(player);
                }
                Hunter.handleLogout(player);
                Locations.logout(player);
                player.getSummoning().unsummon(false, false);
                BossInstance instance = InstanceManager.get().getInstance(player);
                Inferno inferno = instance != null && instance instanceof Inferno ? (Inferno) instance : null;
                if (inferno != null) {
                    inferno.removeAll();
                    InstanceManager.get().remove(inferno);
                    player.setPosition(Inferno.getDeathLocation());
                }

                BountyHunter.handleLogout(player);
                ClanChatManager.leave(player, false);
                player.getRelations().updateLists(false);
                TaskManager.cancelTasks(player.getCombatBuilder());
                TaskManager.cancelTasks(player);
                player.save();
                World.getPlayers().remove(player);
                session.setState(SessionState.LOGGED_OUT);

                //Hiscores.update(player);

                new ConnectionLog().submit(
                        new LogField("username", LogFieldType.STRING, player.getName()),
                        new LogField("action", LogFieldType.STRING, "logout"),
                        new LogField("ip_address", LogFieldType.STRING, player.getHostAddress()),
                        new LogField("serial_address", LogFieldType.STRING, player.getSuperSerialNumber()),
                        new LogField("time", LogFieldType.STRING, Misc.getTime())
                );

                System.out.println(
                        "[LOGGED OUT] " + player.getUsername() + " | Players Online: " + World.getPlayersOnline());
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
}