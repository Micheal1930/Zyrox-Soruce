package com.zyrox.engine.task.impl;

import java.text.SimpleDateFormat;

import com.google.common.collect.ImmutableSet;
import com.zyrox.GameSettings;
import com.zyrox.engine.task.Task;
import com.zyrox.engine.task.TaskManager;
import com.zyrox.model.Animation;
import com.zyrox.model.Flag;
import com.zyrox.model.GameObject;
import com.zyrox.model.Locations.Location;
import com.zyrox.model.Position;
import com.zyrox.world.World;
import com.zyrox.world.content.Achievements;
import com.zyrox.world.content.Achievements.AchievementData;
import com.zyrox.world.content.CustomObjects;
import com.zyrox.world.content.Galvek;
import com.zyrox.world.content.KillsTracker;
import com.zyrox.world.content.KillsTracker.KillsEntry;
import com.zyrox.world.content.TreasureChest;
import com.zyrox.world.content.TrioBosses;
import com.zyrox.world.content.combat.strategy.impl.GalvekCombatStrategy;
import com.zyrox.world.content.combat.strategy.impl.KalphiteQueen;
import com.zyrox.world.content.combat.strategy.impl.Nex;
import com.zyrox.world.content.combat.strategy.zulrah.ZulrahConstants;
import com.zyrox.world.content.event.GameEvent;
import com.zyrox.world.content.event.GameEventManager;
import com.zyrox.world.content.event.impl.GalvekEvent;
import com.zyrox.world.content.greatolm.OlmAnimations;
import com.zyrox.world.content.greatolm.Phases;
import com.zyrox.world.content.minigames.impl.castlewars.CastleWarsManager;
import com.zyrox.world.content.minigames.impl.castlewars.item.CastleWarsBarricade;
import com.zyrox.world.content.skill.impl.slayer.KonarQuoMaten;
import com.zyrox.world.content.treasuretrails.EliteClueScroll;
import com.zyrox.world.entity.impl.npc.NPC;
import com.zyrox.world.entity.impl.npc.impl.Zulrah;
import com.zyrox.world.entity.impl.player.Player;

/**
 * Represents an npc's death task, which handles everything an npc does before
 * and after their death animation (including it), such as dropping their drop
 * table items.
 *
 * @author relex lawl
 */

public class NPCDeathTask extends Task {

    /*
     * The ImmutableSet which handles what bosses will give a player boss points after death.
     */
    private static final ImmutableSet<Integer> GIVE_BOSS_POINTS = ImmutableSet.of(1999, 2882, 2881, 2883, 7134, 3847, 3340, 6766, 5666,
            7286, 4540, 6222, 6260, 6247, 6203, 8349, 50, 2001, 11558, 4540, 1160, 21475, 21476, 5420, 21477, 8133, 3200, 13447, 8549, 3851,
            1382, 8133, 13447, 2000, 2009, 2006, 2060, 5421, 23098);

    /**
     * The NPCDeathTask constructor.
     *
     * @param npc The npc being killed.
     */
    public NPCDeathTask(NPC npc) {
        super(2);
        this.npc = npc;
        this.ticks = 2;
    }

    /**
     * The npc setting off the death task.
     */
    private final NPC npc;

    /**
     * The amount of ticks on the task.
     */
    private int ticks = 2;

    /**
     * The player who killed the NPC
     */
    private Player killer = null;

    @SuppressWarnings("incomplete-switch")
    @Override
    public void execute() {
        try {
            npc.setEntityInteraction(null);
            switch (ticks) {
                case 2:
                    npc.getMovementQueue().setLockMovement(true).reset();
                    killer = npc.getCombatBuilder().getKiller(npc.clearDamageMapOnDeath());

                    if (npc.getId() == 23612) {
                        npc.setAnimation(new Animation(23537));
                        TaskManager.submit(new Task(2) {
                            @Override
                            protected void execute() {
                                npc.setTransformationId(23613);
                                npc.getUpdateFlag().flag(Flag.TRANSFORM);
                                npc.setAnimation(new Animation(23538));
                                stop();
                            }
                        });
                    } else if (npc.getId() == 23621 || npc.getId() == 23622) {
                        npc.setAnimation(new Animation(8257 + GameSettings.OSRS_ANIM_OFFSET));
                        TaskManager.submit(new Task(2) {
                            @Override
                            protected void execute() {
                                npc.setTransformationId(8622 + GameSettings.OSRS_NPC_OFFSET);
                                npc.getUpdateFlag().flag(Flag.TRANSFORM);
                                npc.setAnimation(new Animation(8258 + GameSettings.OSRS_ANIM_OFFSET));
                                stop();
                            }
                        });
                   } else if (npc.getId() == GalvekCombatStrategy.Stage.ORANGE.getId()
                            || npc.getId() == GalvekCombatStrategy.Stage.BLUE.getId()
                            || npc.getId() == GalvekCombatStrategy.Stage.WHITE.getId()) {

                        int stageOrdinal = (npc.getId() - GalvekCombatStrategy.Stage.ORANGE.getId()) + 1;
                        Position spawn = npc.getPosition().copy();
                        GalvekCombatStrategy.Stage newStage = GalvekCombatStrategy.Stage.values()[stageOrdinal];
                        Galvek galvek = new Galvek(newStage, spawn);
                        galvek.getCombatBuilder().setDamageMap(npc.getCombatBuilder().getDamageMap());
                        for (GameEvent event : GameEventManager.getEvents().values()) {
                            if (event instanceof GalvekEvent) {
                                ((GalvekEvent) event).galvek = galvek;
                            }
                        }
                        TaskManager.submit(new Task(3) {
                            @Override
                            protected void execute() {
                                World.register(galvek);
                                if (galvek.isRegistered())
                                    galvek.performAnimation(GalvekCombatStrategy.PHASE_DOWN_ANIM);
                                stop();
                            }
                        });
                    } else if (npc.getId() == 13447) {
                        Nex.getNex(killer).handleDeath();
                    } else if (npc.getId() == Phases.OLM_LEFT_HAND) {
                        if (killer != null) {
                            killer.getMinigameAttributes().getRaidsAttributes().getParty().getLeftHandObject()
                                    .performAnimation(OlmAnimations.goingDownLeftHand);
                            TaskManager.submit(new Task(2) {
                                @Override
                                public void execute() {
                                    CustomObjects.spawnGlobalObject(new GameObject(29885,
                                            new Position(3238, 5733, killer.getPosition().getZ()), 10, 1));
                                    CustomObjects.spawnGlobalObject(new GameObject(29885,
                                            new Position(3220, 5743, killer.getPosition().getZ()), 10, 3));
                                    // killer.getMinigameAttributes().getRaidsAttributes().getParty().getLeftHandObject()
                                    // .performAnimation(OlmAnimations.goingDownLeftHand);
                                    stop();
                                }
                            });
                            killer.getMinigameAttributes().getRaidsAttributes().getParty().setLeftHandDead(true);
                        }
                    } else if (npc.getId() == Phases.OLM_RIGHT_HAND) {
                        if (killer != null) {
                            killer.getMinigameAttributes().getRaidsAttributes().getParty().getRightHandObject()
                                    .performAnimation(OlmAnimations.goingDownRightHand);
                            TaskManager.submit(new Task(2) {
                                @Override
                                public void execute() {
                                    killer.getMinigameAttributes().getRaidsAttributes().getParty().getRightHandObject()
                                            .performAnimation(OlmAnimations.deadLeftHand);
                                    stop();
                                }
                            });
                            killer.getMinigameAttributes().getRaidsAttributes().getParty().setRightHandDead(true);
                        }
                    } else if (npc.getId() == ZulrahConstants.GREEN_ZULRAH_ID) {
                        npc.getSpawnedFor().getPacketSender().sendMessage("Fight duration: @red@"
                                + new SimpleDateFormat("m:ss").format(System.currentTimeMillis() - ((Zulrah) npc).getTime())
                                + "</col>.");
                    } else if (!(npc.getId() >= 6142 && npc.getId() <= 6145)
                            && !(npc.getId() > 5070 && npc.getId() < 5081)) {
                        npc.performAnimation(new Animation(npc.getDefinition().getDeathAnimation()));
                    }

                    break;
                case 0:
                    if (killer != null) {

                        boolean boss = (npc.getDefaultConstitution() > 2000);
                        if (!Nex.nexMinion(npc.getId()) && npc.getId() != 1158
                                && !(npc.getId() >= 3493 && npc.getId() <= 3497)) {
                            KillsTracker.submit(killer, new KillsEntry(npc.getDefinition().getName(), 1, boss));
                            if (boss) {
                                Achievements.doProgress(killer, AchievementData.DEFEAT_500_BOSSES);
                            }
                        }
                        if (GIVE_BOSS_POINTS.contains(npc.getId())) {
                            killer.setBossPoints(killer.getBossPoints() + 1);

                            killer.sendMessage("<img=0> You now have @red@" + killer.getBossPoints() + " Boss Points!");
                        }
                        Achievements.doProgress(killer, AchievementData.DEFEAT_10000_MONSTERS);
                        if (npc.getId() == 50) {
                            Achievements.finishAchievement(killer, AchievementData.DEFEAT_THE_KING_BLACK_DRAGON);
                        } else if (npc.getId() == 3200) {
                            Achievements.finishAchievement(killer, AchievementData.DEFEAT_THE_CHAOS_ELEMENTAL);
                        } else if (npc.getId() == 8349) {
                            Achievements.finishAchievement(killer, AchievementData.DEFEAT_A_TORMENTED_DEMON);
                        } else if (npc.getId() == 3491) {
                            Achievements.finishAchievement(killer, AchievementData.DEFEAT_THE_CULINAROMANCER);
                        } else if (npc.getId() == 8528) {
                            Achievements.finishAchievement(killer, AchievementData.DEFEAT_NOMAD);
                        } else if (npc.getId() == 2745) {
                            Achievements.finishAchievement(killer, AchievementData.DEFEAT_JAD);
                        } else if (npc.getId() == 4540) {
                            Achievements.finishAchievement(killer, AchievementData.DEFEAT_BANDOS_AVATAR);
                        } else if (npc.getId() == 6260) {
                            Achievements.finishAchievement(killer, AchievementData.DEFEAT_GENERAL_GRAARDOR);
                            killer.getAchievementAttributes().setGodKilled(0, true);
                        } else if (npc.getId() == 6222) {
                            Achievements.finishAchievement(killer, AchievementData.DEFEAT_KREE_ARRA);
                            killer.getAchievementAttributes().setGodKilled(1, true);
                        } else if (npc.getId() == 6247) {
                            Achievements.finishAchievement(killer, AchievementData.DEFEAT_COMMANDER_ZILYANA);
                            killer.getAchievementAttributes().setGodKilled(2, true);
                        } else if (npc.getId() == 6203) {
                            Achievements.finishAchievement(killer, AchievementData.DEFEAT_KRIL_TSUTSAROTH);
                            killer.getAchievementAttributes().setGodKilled(3, true);
                        } else if (npc.getId() == 8133) {
                            Achievements.finishAchievement(killer, AchievementData.DEFEAT_THE_CORPOREAL_BEAST);
                        } else if (npc.getId() == 13447) {
                            Achievements.finishAchievement(killer, AchievementData.DEFEAT_NEX);
                            killer.getAchievementAttributes().setGodKilled(4, true);
                        }
                        if (npc.getId() == 3847) {
                            killer.resetKraken();
                            killer.getKrakenRespawn().reset();
                        }
                        /** ACHIEVEMENTS **/
                        switch (killer.getLastCombatType()) {
                            case MAGIC:
                                Achievements.finishAchievement(killer, AchievementData.KILL_A_MONSTER_USING_MAGIC);
                                break;
                            case MELEE:
                                Achievements.finishAchievement(killer, AchievementData.KILL_A_MONSTER_USING_MELEE);
                                break;
                            case RANGED:
                                Achievements.finishAchievement(killer, AchievementData.KILL_A_MONSTER_USING_RANGED);
                                break;
                        }

                        /** LOCATION KILLS **/
                        if (npc.getLocation().handleKilledNPC(killer, npc)) {
                            stop();
                            break;
                        }
                        /*
                         * Trio bosses
                         */

                        if (npc.getId() == 1973) {
                            TrioBosses.handleSkeleton(killer, npc.getPosition());
                        }
                        if (npc.getId() == 75) {
                            TrioBosses.handleZombie(killer, npc.getPosition());
                        }
                        if (npc.getId() == 103) {
                            TrioBosses.handleGhost(killer, npc.getPosition());
                        }

                        if (npc.getId() == 133 || npc.getId() == 135 || npc.getId() == 1472 || npc.getId() == 132) {
                            TreasureChest.handleDrops(killer, npc, npc.getPosition());
                        }

                        if (npc.getId() == 708) {
                            if (killer.getJailKills() <= 1) {
                                killer.moveTo(GameSettings.DEFAULT_POSITION);
                                killer.getPA().sendMessage("@red@You have been unjailed. Don't do it again!");
                            } else {
                                killer.setJailKills(killer.getJailKills() - 1);
                                killer.getPA().sendMessage(killer.getJailKills() + " / " + killer.getJailAmount()
                                        + " imps still have to be killed.");
                            }
                        }

                        npc.dropItems(killer);
                        EliteClueScroll.dropClue(killer, npc);

                        killer.getSlayer().killedNpc(npc);
                        KonarQuoMaten.slainAssignment(killer, npc);
                    }
                    stop();
                    break;
            }
            ticks--;
        } catch (Exception e) {
            e.printStackTrace();
            stop();
        }
    }

    @Override
    public void stop() {
        npc.setDying(false);
        if (killer != null) {
            if (CastleWarsManager.inCastleWars(killer)) {
                if (npc.getId() == CastleWarsBarricade.BARRICADE) {
                    CastleWarsBarricade.removeBarricade(npc);
                    return;
                }
            }
        }
        World.deregister(npc);

        if (Nex.nexMob(npc.getId()) && killer != null) {
            Nex.getNex(killer).death(killer, npc.getId());
        }
        
        boolean special = npc.getDefinition().getRespawnTime() > 0 && npc.getLocation() != Location.GRAVEYARD && npc.getLocation() != Location.DUNGEONEERING && npc.getId() != 3334 
				|| killer.getLocation() == Location.BOSS_INSTANCE && npc.getLocation() != Location.CASTLE_WARS_GAME && npc.getLocation() != Location.DONATOR_ZONE
	               && npc.getRegionID() != 9043;
        
        if(killer != null) {
			if(killer.getRegionInstance() != null) 
				if(killer.getRegionInstance().getNpcsList().contains(npc))
					killer.getRegionInstance().getNpcsList().remove(npc);
			if(special) {
				if(npc.getInstancedPlayer() == null)
					TaskManager.submit(new NPCRespawnTask(npc, npc.getDefinition().getRespawnTime(), killer));
				else if(World.getPlayerByName(npc.getInstancedPlayer().getUsername()) != null)
					TaskManager.submit(new NPCRespawnTask(npc, 15, killer));
			}
			return;
		}
        if(special)
			TaskManager.submit(new NPCRespawnTask(npc, npc.getDefinition().getRespawnTime(), null));

//        if (killer != null) {
//        	
//            if (killer.getPlayerInstance().getInstance() != null) {
//                return;
//            }
//        }
//
//        // respawn
//        if (npc.getDefinition().getRespawnTime() > 0 && npc.getLocation() != Location.GRAVEYARD
//                && npc.getLocation() != Location.DUNGEONEERING && npc.getLocation() != Location.CASTLE_WARS_GAME && npc.getLocation() != Location.DONATOR_ZONE
//                && npc.getRegionID() != 9043) {
//            TaskManager.submit(new NPCRespawnTask(npc, npc.getDefinition().getRespawnTime()));
//        }

        if (npc.getId() == 1158 || npc.getId() == 1160) {
            KalphiteQueen.death(npc.getId(), npc.getPosition());
        }
    }
}
