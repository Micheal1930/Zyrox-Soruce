package com.zyrox.world.content.skill.impl.woodcutting;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.zyrox.engine.task.Task;
import com.zyrox.engine.task.TaskManager;
import com.zyrox.model.Animation;
import com.zyrox.model.GameObject;
import com.zyrox.model.Position;
import com.zyrox.model.Skill;
import com.zyrox.model.Locations.Location;
import com.zyrox.model.container.impl.Equipment;
import com.zyrox.model.item.Items;
import com.zyrox.util.Misc;
import com.zyrox.util.Time;
import com.zyrox.world.World;
import com.zyrox.world.clip.region.RegionClipping;
import com.zyrox.world.content.Achievements;
import com.zyrox.world.content.CustomObjects;
import com.zyrox.world.content.EvilTrees;
import com.zyrox.world.content.Sounds;
import com.zyrox.world.content.Achievements.AchievementData;
import com.zyrox.world.content.Sounds.Sound;
import com.zyrox.world.content.skill.impl.firemaking.LogData;
import com.zyrox.world.content.skill.impl.firemaking.LogData.Log;
import com.zyrox.world.content.skill.impl.woodcutting.WoodcuttingData.Hatchet;
import com.zyrox.world.content.skill.impl.woodcutting.WoodcuttingData.Trees;
import com.zyrox.world.entity.impl.player.Player;

public class Woodcutting {

    public static final int AFK_TREE = 14309;
    private static ChristmasTreePositions lastTree = ChristmasTreePositions.FIRST;
    private static long lastTreeRespawn = -1;

    public static void cutWood(final Player player, final GameObject object, boolean restarting) {
        if (!restarting)
            player.getSkillManager().stopSkilling();
        if (player.getInventory().getFreeSlots() == 0) {
            player.getPacketSender().sendMessage("You don't have enough free inventory space.");
            return;
        }
        player.setPositionToFace(object.getPosition());
        final int objId = object.getId();
        final Hatchet h = Hatchet.forId(WoodcuttingData.getHatchet(player));
        if (Misc.getRandom(25000) == 3) {
            player.getInventory().add(13322, 1, "Woodcutting cut wood");
            World.sendMessage("<col=6666FF> <img=10> [ Skilling Pets ]:</col> " + player.getUsername() + " has received the Beaver pet!");
            player.getPacketSender().sendMessage("@red@You have received a skilling pet!");
            player.getPacketSender().sendMessage("@red@Your account has been saved!");
            player.save();
        }
        if (h != null) {
            if (player.getSkillManager().getCurrentLevel(Skill.WOODCUTTING) >= h.getRequiredLevel()) {
                final RegionClipping region = RegionClipping.forPosition(object.getPosition());
                final WoodcuttingData.Trees t = WoodcuttingData.Trees.forId(objId);
                if (t != null) {
                    player.setEntityInteraction(object);
                    if (player.getSkillManager().getCurrentLevel(Skill.WOODCUTTING) >= t.getReq()) {

                        player.performAnimation(new Animation(h.getAnim()));
                        int delay = Misc.getRandom(t.getTicks() - WoodcuttingData.getChopTimer(player, h)) + 1;
                        player.setCurrentTask(new Task(1, player, false) {
                            int cycle = 0, reqCycle = delay >= 2 ? delay : Misc.getRandom(1) + 1;

                            @Override
                            public void execute() {
                                if (player.getInventory().getFreeSlots() == 0) {
                                    player.performAnimation(new Animation(65535));
                                    player.getPacketSender().sendMessage("You don't have enough free inventory space.");
                                    this.stop();
                                    return;
                                }
                                if (object == null || !RegionClipping.objectExists(object)) {
                                    player.getSkillManager().stopSkilling();
                                    this.stop();
                                    return;
                                }
                                if (cycle != reqCycle) {
                                    cycle++;
                                    player.performAnimation(new Animation(h.getAnim()));
                                } else if (cycle >= reqCycle) {
                                    double xp = t.getXp();
                                    if (lumberJack(player))
                                        xp *= 1.5;

                                    player.getSkillManager().addExperience(Skill.WOODCUTTING, xp * Skill.WOODCUTTING.getModifier());
                                    if (player.getEquipment().getItems()[Equipment.WEAPON_SLOT].getId() == 43241) {
                                        if (Misc.getRandom(3) == 1) {
                                            xp *= 1.5;
                                            player.getSkillManager().addExperience(Skill.WOODCUTTING, xp * Skill.WOODCUTTING.getModifier());

                                        }
                                    }
                                    cycle = 0;
                                    BirdNests.dropNest(player);
                                    this.stop();
                                    if (object.getId() == 11434) {
                                        if (EvilTrees.SPAWNED_TREE == null || EvilTrees.SPAWNED_TREE.getTreeObject().getCutAmount() >= EvilTrees.MAX_CUT_AMOUNT) {
                                            player.getPacketSender().sendClientRightClickRemoval();
                                            player.getSkillManager().stopSkilling();
                                            System.out.println("STop");
                                            return;
                                        } else {
                                            EvilTrees.SPAWNED_TREE.getTreeObject().incrementCutAmount();
                                        }
                                        //} else {
                                        //player.performAnimation(new Animation(65535));
                                    }

                                    boolean choppedDown = false;

                                    if (object.getId() !=  11434 && (!t.isMulti() || Misc.getRandom(10) == 2)) {
                                        player.performAnimation(new Animation(65535));
                                        treeRespawn(player, object);
                                        choppedDown = true;
                                        player.getPacketSender().sendMessage("You've chopped the tree down.");
                                    } else {
                                        cutWood(player, object, true);
                                        if (t == Trees.EVIL_TREE) {
                                            player.getPacketSender().sendMessage("You cut the Evil Tree...");
                                            int chance = Misc.inclusiveRandom(1, 1000);
                                            if(chance == 1) {
                                                List<Integer> lumberJackIds = Arrays.asList(10933, 10939, 10940, 10941);
                                                Collections.shuffle(lumberJackIds);
                                                for(Integer lumberJackId : lumberJackIds) {
                                                    if(!player.hasItem(lumberJackId)) {
                                                        player.getInventory().add(lumberJackId, 1);
                                                        break;
                                                    }
                                                }
                                            }

                                            int boxChance = Misc.random(500);
                                            if(boxChance == 1) {
                                                player.getInventory().add(Items.HALLOWEEN_BOX, 1);
                                                player.sendMessage(Misc.getHalloweenIcon()+"A halloween box has appeared in your inventory!");
                                            }

                                        } else {
                                            player.getPacketSender().sendMessage("You get some logs..");
                                        }

                                        if (lastTreeRespawn == -1) {
                                            lastTreeRespawn = System.currentTimeMillis();
                                        } else if (t.equals(WoodcuttingData.Trees.CHRISTMAS_TREE) && System.currentTimeMillis() - lastTreeRespawn > Time.ONE_HOUR * 3) {
                                            treeRespawn(player, object);
                                            player.getPacketSender().sendMessage("The tree will now spawn at another location.");
                                            lastTreeRespawn = -1;
                                        }
                                    }
                                    Sounds.sendSound(player, Sound.WOODCUT);

                                    boolean successful = Misc.inclusiveRandom(1, 3) == 1;
                                    boolean infernoAdze = infernoAdze(player);


                                    if ((!infernoAdze || !successful) || (choppedDown && !successful)) {
                                        if (Location.inResource(player)) {
                                            player.getInventory().add(t.getReward() != 14666 ? t.getReward() + 1 : t.getReward(), 1, "Woodcutting chop wood");
                                            if (player.getEquipment().getItems()[Equipment.WEAPON_SLOT].getId() == 43241) {
                                                if (Misc.getRandom(3) == 1) {
                                                    player.getInventory().add(t.getReward() != 14666 ? t.getReward() + 1 : t.getReward(), 1, "Woodcutting chop wood");
                                                }
                                            }
                                        } else {
                                            player.getInventory().add(t.getReward(), 1, "Woodcutting chop wood");
                                            if (player.getEquipment().getItems()[Equipment.WEAPON_SLOT].getId() == 43241) {
                                                if (Misc.getRandom(3) == 1) {
                                                    player.getInventory().add(t.getReward(), 1, "Woodcutting chop wood");
                                                }
                                            }
                                        }
                                    } else if (successful) {
                                        Log fmLog = LogData.getLogForLogId(player, t.getReward());
                                        if (fmLog != null) {
                                            player.getSkillManager().addExperience(Skill.FIREMAKING, fmLog.getXp());
                                            player.getPacketSender().sendMessage("Your Inferno Adze burns the log, granting you Firemaking experience.");
                                            if (fmLog == Log.OAK) {
                                                Achievements.finishAchievement(player, AchievementData.BURN_AN_OAK_LOG);
                                            } else if (fmLog == Log.MAGIC) {
                                                Achievements.doProgress(player, AchievementData.BURN_100_MAGIC_LOGS);
                                                Achievements.doProgress(player, AchievementData.BURN_2500_MAGIC_LOGS);
                                            }
                                        }
                                    }

                                    if (t == Trees.OAK) {
                                        Achievements.finishAchievement(player, AchievementData.CUT_AN_OAK_TREE);
                                    } else if (t == Trees.MAGIC) {
                                        Achievements.doProgress(player, AchievementData.CUT_100_MAGIC_LOGS);
                                        Achievements.doProgress(player, AchievementData.CUT_5000_MAGIC_LOGS);
                                    }
                                }
                            }
                        });
                        TaskManager.submit(player.getCurrentTask());
                    } else {
                        player.getPacketSender().sendMessage("You need a Woodcutting level of at least " + t.getReq() + " to cut this tree.");
                    }
                }
            } else {
                player.getPacketSender().sendMessage("You do not have a hatchet which you have the required Woodcutting level to use.");
            }
        } else {
            player.getPacketSender().sendMessage("You do not have a hatchet that you can use.");
        }
    }

    public static boolean lumberJack(Player player) {
        return player.getEquipment().get(Equipment.HEAD_SLOT).getId() == 10941 && player.getEquipment().get(Equipment.BODY_SLOT).getId() == 10939 && player.getEquipment().get(Equipment.LEG_SLOT).getId() == 10940 && player.getEquipment().get(Equipment.FEET_SLOT).getId() == 10933;
    }

    public static boolean infernoAdze(Player player) {
        return player.getEquipment().get(Equipment.WEAPON_SLOT).getId() == 13661;
    }

    public static void treeRespawn(final Player player, final GameObject oldTree) {
        if (oldTree == null || oldTree.getPickAmount() >= 1)
            return;
        oldTree.setPickAmount(1);
        for (Player players : player.getLocalPlayers()) {
            if (players == null)
                continue;
            if (players.getInteractingObject() != null && players.getInteractingObject().getPosition().equals(player.getInteractingObject().getPosition().copy())) {
                players.getSkillManager().stopSkilling();
                players.getPacketSender().sendClientRightClickRemoval();
            }
        }
        player.getPacketSender().sendClientRightClickRemoval();
        player.getSkillManager().stopSkilling();
        CustomObjects.globalObjectRespawnTask(new GameObject(1343, oldTree.getPosition().copy(), 10, 0), oldTree, 20 + Misc.getRandom(10));
    }

    private enum ChristmasTreePositions {

        FIRST(new Position(3107, 3508)),
        SECOND(new Position(3073, 3504)),
        THIRD(new Position(3079, 3482));

        private Position position;

        ChristmasTreePositions(Position position) {
            this.position = position;
        }

        public Position getPosition() {
            return position;
        }

        public ChristmasTreePositions next() {
            return values()[(this.ordinal() + 1) % values().length];
        }

    }

}
