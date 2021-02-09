package com.zyrox.world.content.raids.theatre_of_blood;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zyrox.GameServer;
import com.zyrox.GameSettings;
import com.zyrox.engine.task.Task;
import com.zyrox.engine.task.TaskManager;
import com.zyrox.model.*;
import com.zyrox.world.World;
import com.zyrox.world.clip.region.RegionClipping;
import com.zyrox.world.content.CustomObjects;
import com.zyrox.world.content.dialogue.Dialogue;
import com.zyrox.world.content.dialogue.DialogueManager;
import com.zyrox.world.content.dialogue.DialogueType;
import com.zyrox.world.content.raids.RaidsConstants;
import com.zyrox.world.content.raids.theatre_of_blood.exit.VerzikViturExitCave;
import com.zyrox.world.content.raids.theatre_of_blood.pillar.Pillar;
import com.zyrox.world.content.raids.theatre_of_blood.pillar.PillarConstants;
import com.zyrox.world.content.raids.theatre_of_blood.pillar.PillarObject;
import com.zyrox.world.content.raids.theatre_of_blood.skeleton.VerzikViturSkeletonObject;
import com.zyrox.world.content.raids.theatre_of_blood.verzik_vitur.VerzikVitur;
import com.zyrox.world.content.raids.theatre_of_blood.verzik_vitur.VerzikViturConstants;
import com.zyrox.world.content.transportation.TeleportHandler;
import com.zyrox.world.entity.impl.npc.NPC;
import com.zyrox.world.entity.impl.player.Player;

/**
 * A theatre of blood is shared by multiple different people
 * Created by Jonny on 7/2/2019
 **/
public class TheatreOfBlood {

    private Player player;

    private TheatreOfBloodFight fight;

    public VerzikVitur verzikVitur;

    private TheatreOfBloodParty party;

    private final List<Pillar> pillars = new ArrayList<>();

    private VerzikViturSkeletonObject skeleton;

    private VerzikViturExitCave exitCave;

    private static Map<String, TheatreOfBloodParty> parties = new HashMap<>();

    public TheatreOfBlood(Player player) {
        this.player = player;
    }

    public void openTheatre() {
        checkIfEmpty();

        if(party.getStatus() != TheatreOfBloodPartyStatus.COLLECTING_MEMBERS) {
            DialogueManager.sendStatement(player, "You can't enter a raid after it has started.");
            return;
        }

        if(party.getPartyMembers().size() > 1) {
            sendMessageToParty("The theatre is now open to joining. You have 60 seconds to join before it starts.");
        }

        createPillars();

        teleportToFinalRoom();
        party.setStatus(TheatreOfBloodPartyStatus.STARTED_MEMBERS_JOINING);
        party.getJoiningTimer().reset();
    }

    public void start() {
        sendMessageToParty("The theatre has been started, and there is no more joining allowed.");

        party.setStatus(TheatreOfBloodPartyStatus.STARTED_NO_MORE_JOINING);

        this.fight = TheatreOfBloodFight.values()[0];

        this.skeleton = new VerzikViturSkeletonObject(132741, new Position(3166, 4303, party.getFloor()), 10, 0, false);

        TaskManager.submit(new Task(5, player, true) {

            @Override
            protected void execute() {
                if(party == null) {
                    forceEnd();
                    stop();
                    return;
                }

                verzikVitur = new VerzikVitur(player.getRaids().getTheatreOfBlood(), fight.getNpcId(), fight.getSpawnPosition().copy().setZ(party.getFloor()));
                World.register(verzikVitur);

                skeleton.setCanCollectStaff(true);

                stop();
            }

        });
    }

    public void forceEnd() {
        sendMessageToParty("The party has ended due to a server error. Please contact an administrator with this.");

        for(Player partyMember : party.getPartyMembers()) {
            if(partyMember == null)
                continue;
            teleportToEntrance();
        }
    }

    public void sendMessageToParty(String message) {
        for(Player partyMember : party.getPartyMembers()) {
            if(partyMember == null)
                continue;
            partyMember.sendMessage("<col=830303>"+message);
        }
    }

    public void teleportToEntrance() {
        TeleportHandler.teleportPlayer(player, new Position(3670, 3219, 0), player.getSpellbook().getTeleportType());
    }

    public void checkIfEmpty() {
        if(party == null) {
            System.out.println("Party is null");
            return;
        }

        boolean empty = isRoomEmpty();

        ArrayList<NPC> npcsToDelete = new ArrayList<>();

        for(NPC npc : World.getNpcs()) {
            if(npc == null)
                continue;

            if(npc.getPosition().getZ() != party.getFloor())
                continue;

            if(npc.getId() == VerzikViturConstants.VITUR_SITTING_IDLE_NPC_ID
                || npc.getId() == VerzikViturConstants.VITUR_SITTING_ATTACKING_NPC_ID
                || npc.getId() == VerzikViturConstants.VITUR_WALKING_SOUTH_NPC_ID
                || npc.getId() == VerzikViturConstants.VITUR_OUT_OF_CHAIR_NPC_ID
                || npc.getId() == VerzikViturConstants.VITUR_SPIDER_NPC_ID
                || npc.getId() == VerzikViturConstants.HEALER_NPC_ID
                || npc.getId() == VerzikViturConstants.BOMBER_NPC_ID
                || npc.getId() == PillarConstants.SUPPORTING_PILLAR_NPC_ID) {
                npcsToDelete.add(npc);
            }
        }

        for(NPC npc : npcsToDelete) {
            World.deregister(npc);
        }

        if(empty) {
            party.setStatus(TheatreOfBloodPartyStatus.COLLECTING_MEMBERS);

            RegionClipping.deleteObjectsOnFloor(PillarConstants.PILLAR_OBJECT_ID, party.getFloor());
            RegionClipping.deleteObjectsOnFloor(132741, party.getFloor());
            RegionClipping.deleteObjectsOnFloor(132742, party.getFloor());
            RegionClipping.deleteObjectsOnFloor(132738, party.getFloor());
        }
    }

    public boolean isRoomEmpty() {
        boolean empty = true;

        for(Player member : party.getPartyMembers()) {
            if(member.getLocation() == Locations.Location.THEATRE_OF_BLOOD_FINAL_ROOM) {
                empty = false;
            }
        }

        return empty;
    }

    public void teleportToFinalRoom() {
        checkIfEmpty();

        if(party == null)
            return;

        if(party.getStatus() == TheatreOfBloodPartyStatus.STARTED_NO_MORE_JOINING) {
            DialogueManager.sendStatement(player, "You can't enter a raid after it has started.");
            return;
        }

        clearDawnbringer();
        player.moveTo(new Position(3168, 4303, party.getFloor()));
    }

    public void entranceProcess() {
        processParty();
    }

    public void bossRoomProcess() {
        processParty();
    }

    public void processParty() {
        if(party == null)
            return;
        if(!party.isLeader(player))
            return;

        if (party.getStatus() == TheatreOfBloodPartyStatus.STARTED_MEMBERS_JOINING) {
            if (party.getJoiningTimer().elapsed(60_000) || GameServer.isBeta() || party.getPartyMembers().size() <= 1) {
                start();
            }
        }
    }

    public void completed() {
        this.exitCave = new VerzikViturExitCave(132738, new Position(3167, 4324, party.getFloor()), 10, 0, false);

        List<Player> targets = verzikVitur.getPossibleTargets();
        targets.forEach(target -> {
            target.sendMessage("You have defeated Verzik Vitur!");
            target.sendMessage("You have completed Theatre of Blood. Use the exit cave to leave.");
        });

        TheatreOfBloodReward.grantLoot(party);
    }

    public boolean isObject(GameObject gameObject) {
        switch(gameObject.getId()) {
            case 132_653:
                if(party == null) {
                    DialogueManager.sendStatement(player, "You are not in a raid party.");
                    return false;
                }
                if(!party.isLeader(player)) {
                    if(party.getStatus() == TheatreOfBloodPartyStatus.COLLECTING_MEMBERS) {
                        player.sendMessage("The party leader has not yet started the raid.");
                        return true;
                    } else if(party.getStatus() == TheatreOfBloodPartyStatus.STARTED_MEMBERS_JOINING) {
                        DialogueManager.start(player, new Dialogue() {

                            @Override
                            public DialogueType type() {
                                return DialogueType.OPTION;
                            }

                            @Override
                            public String[] dialogue() {
                                return new String[] {
                                        "Enter Theatre of Blood",
                                        "Cancel",
                                };
                            }

                            @Override
                            public boolean action(int option) {
                                player.getPA().closeDialogueOnly();
                                switch(option) {
                                    case 1:
                                        teleportToFinalRoom();
                                        break;
                                }
                                return false;
                            }

                        });
                        return true;
                    }
                    return true;
                }

                DialogueManager.start(player, new Dialogue() {

                    @Override
                    public DialogueType type() {
                        return DialogueType.OPTION;
                    }

                    @Override
                    public String[] dialogue() {
                        return new String[] {
                                "Start Theatre of Blood",
                                "Cancel",
                        };
                    }

                    @Override
                    public boolean action(int option) {
                        player.getPA().closeDialogueOnly();
                        switch(option) {
                            case 1:
                                openTheatre();
                                break;
                        }
                        return false;
                    }

                });
                break;
        }
        return false;
    }

    public void destroy() {
        if(party == null)
            return;

        if(party.getPartyMembers().size() == 0) {
            parties.remove(player.getName().toLowerCase());
        } else {
            parties.remove(party.getPartyLeader().getName().toLowerCase());
        }

        party.getPartyMembers().clear();

        if(verzikVitur != null) {
            World.deregister(verzikVitur);
            removePillars();
        }

        if(skeleton != null) {
            World.deregister(skeleton);
            CustomObjects.deleteGlobalObject(skeleton);
        }

        if(exitCave != null) {
            World.deregister(exitCave);
            CustomObjects.deleteGlobalObject(exitCave);
        }

        party = null;

        displayParty();
    }

    public void attemptLeave() {
        DialogueManager.start(player, new Dialogue() {

            @Override
            public DialogueType type() {
                return DialogueType.OPTION;
            }

            @Override
            public String[] dialogue() {
                return new String[] {
                        "Leave",
                        "Cancel",
                };
            }

            @Override
            public boolean action(int option) {
                player.getPA().closeDialogueOnly();
                switch(option) {
                    case 1:
                        leave(player.getLocation() == Locations.Location.THEATRE_OF_BLOOD_FINAL_ROOM);
                        break;
                }
                return false;
            }

        });
    }

    public void attemptKick(Player partyMember) {
        if(partyMember == null) {
            return;
        }

        if(partyMember.getName().equalsIgnoreCase(player.getName())) {
            player.sendMessage("You can't kick yourself. Leave the party instead!");
            return;
        }

        DialogueManager.start(player, new Dialogue() {

            @Override
            public DialogueType type() {
                return DialogueType.OPTION;
            }

            @Override
            public String[] dialogue() {
                return new String[] {
                        "Kick "+partyMember.getName(),
                        "Cancel",
                };
            }

            @Override
            public boolean action(int option) {
                player.getPA().closeDialogueOnly();
                switch(option) {
                    case 1:
                        partyMember.getRaids().getTheatreOfBlood().leave(player.getLocation() == Locations.Location.THEATRE_OF_BLOOD_FINAL_ROOM);
                        break;
                }
                return false;
            }

        });
    }

    public void leave(boolean teleport) {
        System.out.println("Leaving");
        if(party == null)
            return;

        if (party.getStatus() == TheatreOfBloodPartyStatus.STARTED_MEMBERS_JOINING) {
            return;
        }

        System.out.println("Leaving party");

        if(party.getPartyMembers() != null) {
            party.getPartyMembers().remove(player);
            for (Map.Entry<String, TheatreOfBloodParty> entry : parties.entrySet()) {
                System.out.println(entry.getKey() + " " + entry.getValue());
            }
            if(party.getPartyMembers().size() == 0) {
                destroy();
            } else {
                if (party.isLeader(player)) {
                    party.setPartyLeader(party.getPartyMembers().get(0));
                    party.getPartyLeader().sendMessage(player.getUsername() + " left. You got leadership now.");
                }
                parties.put(party.getPartyLeader().getUsername().toLowerCase(), party);
                parties.remove(player.getUsername().toLowerCase());
            }
            for (Map.Entry<String, TheatreOfBloodParty> entry : parties.entrySet()) {
                System.out.println(entry.getKey() + " " + entry.getValue());
            }

        }

        clearDawnbringer();

        player.getEquipment().refreshItems();
        refreshAll();

        party = null;
        displayParty();
        if(teleport)
            TeleportHandler.teleportPlayer(player, new Position(3670, 3219, 0), player.getSpellbook().getTeleportType());
    }

    public void clearDawnbringer() {
        if(party == null)
            return;

        for(Player member : party.getPartyMembers()) {
            if(member == null)
                continue;

            if (member.getInventory().contains(VerzikViturConstants.DAWNBRINGER_ID)) {
                member.getInventory().delete(VerzikViturConstants.DAWNBRINGER_ID, player.getInventory().getAmount(VerzikViturConstants.DAWNBRINGER_ID));
            }

            if (member.getEquipment().contains(VerzikViturConstants.DAWNBRINGER_ID)) {
                member.getEquipment().delete(VerzikViturConstants.DAWNBRINGER_ID, player.getEquipment().getAmount(VerzikViturConstants.DAWNBRINGER_ID));
            }
        }
    }

    public void enter() {
        player.getPacketSender().sendInteractionOption("Invite", 2, true);
        displayParty();
    }

    public void displayParty() {
        player.getPacketSender().sendTabInterface(GameSettings.QUESTS_TAB, RaidsConstants.TAB_INTERFACE_ID);
        player.getPacketSender().sendDungeoneeringTabIcon(false);
        player.getPacketSender().sendTab(GameSettings.QUESTS_TAB);
        displayMembers();
    }

    public void leaveLobby() {
        leave(false);
        player.getPacketSender().sendTabInterface(GameSettings.QUESTS_TAB, 26600);
    }

    public void displayMembers() {
        player.getPacketSender().sendString(85002, "Raiding Party: "+(party == null ? "0" : party.getPartyMembers().size()));
        int id = 85017;
        for(int i = 0; i < 12; i++) {
            Player partyMember = (party != null && party.getPartyMembers().size() > i) ? party.getPartyMembers().get(i) : null;
            if (partyMember != null && party.isLeader(partyMember)) {
                player.getPacketSender().sendString(id++, "[L]" + partyMember.getName());
            } else {
                player.getPacketSender().sendString(id++, partyMember == null ? "" : partyMember.getName());
            }
            player.getPacketSender().sendString(id++, partyMember == null ? "" : partyMember.getSkillManager().getCombatLevel());
            player.getPacketSender().sendString(id++, partyMember == null ? "" : partyMember.getSkillManager().getTotalLevel());
            id++;
        }
    }

    public boolean isButton(int button) {
        if(!inArea()) {
           return false;
        }

        if(button >= 85016 && button <= 85060) {
            if(party == null) {
                return true;
            }

            int memberId = (85060 - button) / 4;

            Player partyMember = party.getPartyMembers().size() > memberId ? party.getPartyMembers().get(memberId) : null;

            if(partyMember != null) {
                attemptKick(partyMember);
            }
            return true;
        }

        switch(button) {
            case 85003:
                createParty();
                return true;
            case 85006:
                attemptLeave();
                return true;
        }
        return false;
    }

    public void createParty() {
        System.out.println("Creating party");
        if(parties.containsKey(player.getName().toLowerCase())) {
            party = parties.get(player.getName().toLowerCase());
            party.getPartyMembers().remove(player);
            party.getPartyMembers().add(player);
            displayParty();
            refreshAll();
        } else {
            if (party == null) {
                party = new TheatreOfBloodParty(player);
                displayParty();
                parties.put(player.getName().toLowerCase(), party);
            } else {
                player.sendMessage("You are already in a party.");
            }
        }
    }

    public void attemptInvite(Player toInvite) {
        DialogueManager.start(player, new Dialogue() {

            @Override
            public DialogueType type() {
                return DialogueType.OPTION;
            }

            @Override
            public String[] dialogue() {
                return new String[] {
                        "Invite "+toInvite.getName(),
                        "Cancel",
                };
            }

            @Override
            public boolean action(int option) {
                player.getPA().closeDialogueOnly();
                switch(option) {
                    case 1:
                        invite(toInvite);
                        break;
                }
                return false;
            }

        });
    }

    public void invite(Player toInvite) {
        if(party == null) {
            player.sendErrorMessage("You are not in a raiding party.");
            return;
        }
        if(toInvite == null) {
            player.sendMessage("You have attempted to invite an invalid player.");
            return;
        }

        if(!toInvite.getRaids().getTheatreOfBlood().inEntrance()) {
            player.sendMessage(toInvite.getName()+" isn't at the Theatre Of Blood entrance.");
            return;
        }

        DialogueManager.start(toInvite, new Dialogue() {

            @Override
            public DialogueType type() {
                return DialogueType.OPTION;
            }

            @Override
            public String[] dialogue() {
                return new String[] {
                        "Join "+player.getName()+"'s Raiding Party",
                        "Cancel",
                };
            }

            @Override
            public boolean action(int option) {
                toInvite.getPA().closeDialogueOnly();
                switch(option) {
                    case 1:
                        if(player == null) {
                            toInvite.sendMessage("The party leader that invited you has now logged out.");
                            return false;
                        }

                        toInvite.getRaids().getTheatreOfBlood().joinParty(party);

                        break;
                }
                return false;
            }

        });
    }

    public void joinParty(TheatreOfBloodParty party) {
        System.out.println("Joining party");
        this.party = party;

        if(!this.party.getPartyMembers().contains(player)) {
            this.party.getPartyMembers().add(player);
        }

        refreshAll();
    }

    public void createPillars() {
        for(Position positionToSpawn : PillarConstants.PILLAR_POSITIONS) {
            getPillars().add(new Pillar(positionToSpawn.copy().setZ(party.getFloor())));
        }
    }

    public void refreshAll() {
        System.out.println("refreshing1");
        if(this.party != null) {
            System.out.println("refreshing2");
            for(Player partyMember : party.getPartyMembers()) {
                System.out.println("refreshing3");
                if(partyMember != null) {
                    System.out.println("refreshing4");
                    partyMember.getRaids().getTheatreOfBlood().displayMembers();
                }
            }
        }
    }

    public boolean inEntrance() {
        return player.getLocation() == Locations.Location.THEATRE_OF_BLOOD_ENTRANCE;
    }

    public boolean inFinalRoom() {
        return player.getLocation() == Locations.Location.THEATRE_OF_BLOOD_FINAL_ROOM;
    }

    public boolean inArea() {
        return inEntrance() || inFinalRoom();
    }

    public TheatreOfBloodParty getParty() {
        return this.party;
    }

    public List<Pillar> getPillars() {
        return this.pillars;
    }

    public void removePillar(Pillar pillar, boolean killedPillar) {
        World.deregister(pillar.getPillarNpc());
        CustomObjects.deleteGlobalObject(pillar.getPillarObject());
        pillar.setProjectilesUnblocked(pillar.getPillarObject().getPosition());

        pillar.setPillarObject(new PillarObject(132689, pillar.getPillarObject().getPosition().copy(), 10, 0));
        TaskManager.submit(new Task(2) {
           @Override
           protected void execute() {
               CustomObjects.deleteGlobalObject(pillar.getPillarObject());
               pillar.setProjectilesUnblocked(pillar.getPillarObject().getPosition());

               for(int x = 0; x <= 6; x++) {
                   for(int y = 0; y <= 6; y++) {
                       RegionClipping.removeClipping(pillar.getPillarObject().getPosition().getX() + x, pillar.getPillarObject().getPosition().getY() + y, pillar.getPillarObject().getPosition().getZ());
                   }
               }
               stop();
           }
       });


        if(killedPillar) {
            List<Player> targets = getParty().getPartyMembers();

            /*for(Player target : targets) {
                if(pillar.getPillarNpc().getCentrePosition().isWithinDistance(target.getPosition(), 3)) {
                    target.dealDamage(new Hit(target.getConstitution()));
                }
            }*/

            getPillars().remove(pillar);
        }
    }

    public void removePillars() {
        for (Pillar pillar : getPillars()) {
            if (pillar != null) {
                removePillar(pillar, false);
            }
        }

        getPillars().clear();
    }

    public void spawnBlankSkeleton() {
        this.skeleton = new VerzikViturSkeletonObject(132742, new Position(3166, 4303, party.getFloor()), 10, 0, true);
    }

    public boolean handleObject(GameObject gameObject) {
        switch (gameObject.getId()) {
            case 132655:
                player.sendMessage("Opening party interface.");
                break;
        }
        return false;
    }
}
