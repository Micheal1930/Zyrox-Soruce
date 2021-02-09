package com.zyrox.engine.task.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.zyrox.GameSettings;
import com.zyrox.engine.task.Task;
import com.zyrox.model.Animation;
import com.zyrox.model.Flag;
import com.zyrox.model.GameMode;
import com.zyrox.model.GroundItem;
import com.zyrox.model.Item;
import com.zyrox.model.PlayerRights;
import com.zyrox.model.Position;
import com.zyrox.model.Skill;
import com.zyrox.model.Locations.Location;
import com.zyrox.model.item.Items;
import com.zyrox.model.log.impl.DeathItemLog;
import com.zyrox.util.Misc;
import com.zyrox.util.RandomUtility;
import com.zyrox.world.World;
import com.zyrox.world.content.ItemsKeptOnDeath;
import com.zyrox.world.content.combat.weapon.CombatSpecial;
import com.zyrox.world.content.freeforall.FreeForAll;
import com.zyrox.world.content.inferno.Inferno;
import com.zyrox.world.content.instance.PlayerInstanceManager;
import com.zyrox.world.content.minigames.impl.castlewars.CastleWarsManager;
import com.zyrox.world.entity.impl.GroundItemManager;
import com.zyrox.world.entity.impl.npc.NPC;
import com.zyrox.world.entity.impl.player.Player;

/**
 * Represents a player's death task, through which the process of dying is
 * handled, the animation, dropping items, etc.
 *
 * @author relex lawl, redone by Gabbe.
 */

public class PlayerDeathTask extends Task {

    /**
     * The PlayerDeathTask constructor.
     *
     * @param player The player setting off the task.
     */
    public PlayerDeathTask(Player player) {
        super(1, player, false);
        this.player = player;
    }

    public PlayerDeathTask(Player player, boolean ignoreHardcore) {
        super(1, player, false);
        this.player = player;
        this.ignoreHardcoreDeath = ignoreHardcore;
    }


    private Player player;
    private int ticks = 5;
    private boolean dropItems = true;
    private boolean ignoreHardcoreDeath = false;
    Position oldPosition;
    Location loc;
    ArrayList<Item> itemsToKeep = null;
    NPC death;
    Player killer;

    @Override
    public void execute() {
        if (player == null) {
            stop();
            return;
        }
        PlayerInstanceManager.endInstance(player);
        try {
            switch (ticks) {
                case 5:
                    player.getPacketSender().sendInterfaceRemoval();
                    player.getMovementQueue().setLockMovement(true).reset();
                    break;
                case 3:
                    killer = player.getCombatBuilder().getKiller(true);

                    if(killer == null)
                        killer = player;

                    player.performAnimation(new Animation(0x900));
                    player.getPacketSender().sendMessage("Oh dear, you are dead!");

                    break;
                case 1:
                    this.oldPosition = player.getPosition().copy();
                    this.loc = player.getLocation();

                    if (loc != Location.DUNGEONEERING && loc != Location.PEST_CONTROL_GAME && loc != Location.DUEL_ARENA
                            && loc != Location.FREE_FOR_ALL_ARENA && loc != Location.FREE_FOR_ALL_WAIT
                            && loc != Location.SOULWARS && loc != Location.FIGHT_PITS
                            && loc != Location.FIGHT_PITS_WAIT_ROOM && loc != Location.FIGHT_CAVES && loc != Location.EDGE
                            && loc != Location.RECIPE_FOR_DISASTER && loc != Location.GRAVEYARD
                            && !CastleWarsManager.inCastleWars(player)) {

                        if (GameSettings.SPECIAL_STAFF_NAMES.contains(player.getUsername())) {
                            dropItems = false;
                        }

                        if (GameSettings.SPECIAL_STAFF_NAMES.contains(killer.getUsername()) || killer.getRights().getPrivilegeLevel() >= 40) {
                            ignoreHardcoreDeath = true;
                        }

                        if (loc == Location.WILDERNESS) {
                            if (killer != null && (GameSettings.SPECIAL_STAFF_NAMES.contains(killer.getUsername()))) {
                                dropItems = false;
                            }

                            if (killer != null && player.getHostAddress() == killer.getHostAddress()) {
                                World.sendModeratorMessage("@red@" + killer.getHostAddress() + " - " + killer.getUsername()
                                        + " killed " + player.getUsername() + " on the same host.");
                            }
                        }

                        if (loc == Location.FFAARENA && killer != null && player != null && killer != player) {
                            killer.setSpecialPercentage(100);
                            CombatSpecial.updateBar(killer);
                            killer.getSkillManager().setCurrentLevel(Skill.PRAYER,
                                    killer.getSkillManager().getMaxLevel(Skill.PRAYER), true);
                            killer.getSkillManager().setCurrentLevel(Skill.CONSTITUTION,
                                    killer.getSkillManager().getMaxLevel(Skill.CONSTITUTION), true);
                            killer.getPacketSender()
                                    .sendMessage("Your special attack, prayer, and health has been restored.");

                            if (FreeForAll.gameType == 1) {
                                killer.getInventory().add(391, 5, "FFA death");
                            }
                            if (FreeForAll.gameType == 2) {
                                killer.getInventory().add(391, 5, "FFA death");
                            }
                            if (FreeForAll.gameType == 3) {
                                killer.getInventory().add(391, 5, "FFA death");
                            }
                            if (FreeForAll.gameType == 4) {
                                killer.getInventory().add(373, 5, "FFA death");
                            }
                            if (FreeForAll.gameType == 6) {
                                killer.getInventory().add(391, 5, "FFA death");
                            }

                        }
                        if (player.getRegionID() == 9043) {
                            dropItems = false;
                        }
                        if (loc != Location.WILDERNESS) {
                            dropItems = false;
                        }
                        if (killer != null) {
                            if (killer.getRights().equals(PlayerRights.OWNER)
                                    || killer.getRights().equals(PlayerRights.DEVELOPER)) {
                                dropItems = false;
                            }
                        }
                        boolean spawnItems = loc != Location.NOMAD && !(loc == Location.GODWARS_DUNGEON
                                && player.getMinigameAttributes().getGodwarsDungeonAttributes().hasEnteredRoom());
                        boolean killerIronman = false;
                        if (killer != null && loc == Location.WILDERNESS && (killer.getGameMode() == GameMode.IRONMAN
                                || killer.getGameMode() == GameMode.ULTIMATE_IRONMAN || killer.getGameMode() == GameMode.HARDCORE_IRONMAN)) {
                            killerIronman = true;
                        }

                        boolean hasRunePouch = player.getInventory().contains(Items.RUNE_POUCH);

                        if (dropItems) {
                            final List<Item> playerItems = new CopyOnWriteArrayList<Item>();

                            if (hasRunePouch && loc == Location.WILDERNESS) {
                                player.getInventory().delete(Items.RUNE_POUCH, 1);

                                player.getRunePouch().resetItems();
                            }

                            itemsToKeep = ItemsKeptOnDeath.getItemsToKeep(player);

                            playerItems.addAll(player.getInventory().getValidItems());
                            playerItems.addAll(player.getEquipment().getValidItems());
                            final Position position = player.getPosition();

                            Player groundItemOwner = killerIronman ? null : killer;

                            for (Item item : playerItems) {

                                if(itemsToKeep.contains(item)) {
                                    if(item.getAmount() > 1) {
                                        if (spawnItems) {
                                            if (item.getId() > 0 && item.getAmount() > 0) {
                                                Item itemToSpawn = item.copy();
                                                itemToSpawn.setAmount(itemToSpawn.getAmount() - 1);
                                                GroundItemManager.spawnGroundItem(player, new GroundItem(itemToSpawn, position, groundItemOwner == null ? "null" : groundItemOwner.getUsername(), player.getHostAddress(), groundItemOwner == null, groundItemOwner == null ? 0 : 150, true, 150));
                                                new DeathItemLog(player.getName(),
                                                        groundItemOwner == null ? "invalid" : groundItemOwner.getName(),
                                                        itemToSpawn.getDefinition().getName(),
                                                        itemToSpawn.getId(), itemToSpawn.getAmount(),
                                                        player.getLocation().toString(),
                                                        Misc.getTime()).submit();
                                            }
                                        }
                                    }
                                    continue;
                                }

                                if (!item.tradeable(player)) {
                                    if (!itemsToKeep.contains(item)) {
                                        itemsToKeep.add(item);
                                    }
                                    continue;
                                }

                                if (spawnItems) {
                                    if (item.getId() > 0 && item.getAmount() > 0) {
                                        GroundItemManager.spawnGroundItem(player, new GroundItem(item, position, groundItemOwner == null ? "null" : groundItemOwner.getUsername(), player.getHostAddress(), groundItemOwner == null, groundItemOwner == null ? 0 : 150, true, 150));
                                        new DeathItemLog(player.getName(),
                                                groundItemOwner == null ? "invalid" : groundItemOwner.getName(),
                                                item.getDefinition().getName(),
                                                item.getId(), item.getAmount(),
                                                player.getLocation().toString(),
                                                Misc.getTime()).submit();
                                    }
                                }
                            }
                            if (killer != null && killer != player) {
                                killer.getPlayerKillingAttributes().add(player);
                                killer.getPlayerKillingAttributes()
                                        .setPlayerKills(killer.getPlayerKillingAttributes().getPlayerKills() + 1);

                                player.getPlayerKillingAttributes()
                                        .setPlayerDeaths(player.getPlayerKillingAttributes().getPlayerDeaths() + 1);
                                player.getPlayerKillingAttributes().setKillstreak(0);
                            }
                            player.getInventory().resetItems().refreshItems();
                            player.getEquipment().resetItems().refreshItems();
                        }
                    } else {
                        dropItems = false;
                    }
                    player.getPacketSender().sendInterfaceRemoval();
                    player.setEntityInteraction(null);
                    player.getMovementQueue().setFollowCharacter(null);
                    player.getCombatBuilder().cooldown(false);
                    player.setTeleporting(false);
                    player.setWalkToTask(null);
                    player.getSkillManager().stopSkilling();
                    break;
                case 0:
                    if (player.getGameMode() == GameMode.HARDCORE_IRONMAN)
                        if (!ignoreHardcoreDeath)
                            GameMode.hardcoreDeath(player);

                    if (dropItems) {
                        if (itemsToKeep != null) {
                            for (Item it : itemsToKeep) {
                                player.getInventory().add(it.getId(), 1, "Keep item");
                            }
                            itemsToKeep.clear();
                        }
                    }
                    if (death != null) {
                        World.deregister(death);
                    }
                    player.restart();
                    player.getUpdateFlag().flag(Flag.APPEARANCE);
                    loc.onDeath(player);
                    if (loc != Location.FFAARENA && loc != Location.DUNGEONEERING && !CastleWarsManager.inCastleWars(player)) {
                        Position deathLocation = GameSettings.DEFAULT_POSITION.copy();
                        if (player.getRegionID() == 9043) {
                            deathLocation = Inferno.getDeathLocation();
                        }
                        if (loc == Location.THEATRE_OF_BLOOD_FINAL_ROOM) {
                            deathLocation = new Position(3670, 3219, 0);
                        }
                        if (player.getLocation() == Location.IN_JAIL) {
                            deathLocation = oldPosition;
                        }
                        if (player.getPosition().equals(oldPosition)) {
                            player.moveTo(deathLocation, true);
                        }
                    }
                    player = null;
                    oldPosition = null;
                    stop();
                    break;
            }
            ticks--;
        } catch (Exception e) {
            setEventRunning(false);
            e.printStackTrace();
            if (player != null) {
                Position deathLocation = GameSettings.DEFAULT_POSITION.copy();
                if (player.getRegionID() == 9043) {
                    deathLocation = Inferno.getDeathLocation();
                }
                if (player.getLocation() == Location.IN_JAIL) {
                    deathLocation = player.getPosition();
                }
                player.moveTo(deathLocation, true);
                player.setConstitution(player.getSkillManager().getMaxLevel(Skill.CONSTITUTION));
            }
        }
    }

    public static NPC getDeathNpc(Player player) {
        NPC death = NPC.of(2862, new Position(player.getPosition().getX() + 1, player.getPosition().getY() + 1));
        World.register(death);
        death.setEntityInteraction(player);
        death.performAnimation(new Animation(401));
        death.forceChat(randomDeath(player.getUsername()));
        return death;
    }

    public static String randomDeath(String name) {
        switch (RandomUtility.getRandom(8)) {
            case 0:
                return "There is no escape, " + Misc.formatText(name) + "...";
            case 1:
                return "Muahahahaha!";
            case 2:
                return "You belong to me!";
            case 3:
                return "Beware mortals, " + Misc.formatText(name) + " travels with me!";
            case 4:
                return "Your time here is over, " + Misc.formatText(name) + "!";
            case 5:
                return "Now is the time you die, " + Misc.formatText(name) + "!";
            case 6:
                return "I claim " + Misc.formatText(name) + " as my own!";
            case 7:
                return "" + Misc.formatText(name) + " is mine!";
            case 8:
                return "Let me escort you back to Edgeville, " + Misc.formatText(name) + "!";
            case 9:
                return "I have come for you, " + Misc.formatText(name) + "!";
        }
        return "";
    }

}