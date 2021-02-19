package com.zyrox.world.content;

import com.zyrox.GameSettings;
import com.zyrox.model.Item;
import com.zyrox.model.Position;

import java.util.ArrayList;

public enum TeleportEnum {
        /* Mobs */
    STARTER(100, 1, "Starter", new Position(2542, 2533, 0), "tier: 1", "Melee", "All styles"),
    DONKEY_KONG(101, 1, "Donkey zone", new Position(3103, 2983, 0), "tier: 4", "Melee", "magic"),
    /* Bosses */
    TARN(102, 2, "Tarn", new Position(3104, 2973, 0), "tier: 6", "Magic", "Melee"),
    /* Minigames */
    CAPE_GAME(100, 3, "Wyvern capes", new Position(1750, 3410, 0), "tier: all", "melee", "Magic"),
    /* Others */
    AFK5(100, 4, "AFK Zone", new Position(2721, 2528), "tier: all", "None", "None");

    private TeleportEnum(int npcId, int id, String teleportName, Position position, String difficulty){
        this.npcId = npcId;
        this.id = id;
        this.teleportName = teleportName;
        this.position = position;
        this.difficulty = difficulty;
    }

    private TeleportEnum(int npcId, int id, String teleportName, Position position, String difficulty, String attackWith, String weakness){
        this.npcId = npcId;
        this.id = id;
        this.teleportName = teleportName;
        this.position = position;
        this.difficulty = difficulty;
        this.attackWith = attackWith;
        this.weakness = weakness;
    }

    private String attackWith;
    private String weakness;
    private int npcId;
    private String teleportName;
    private Position position;
    private int id;
    private String difficulty;
    private Item[] drops;

    public static ArrayList<TeleportEnum> dataByTier(int tier){
        ArrayList<TeleportEnum> teleports = new ArrayList<>();
        for(TeleportEnum data : TeleportEnum.values()) {
            if(data.id == tier){
                teleports.add(data);
            }
        }
        return teleports;
    }


    public Item[] getDrops() {
        return drops;
    }

    public String getAttackWith() {
        return attackWith;
    }

    public String getWeakness() {
        return weakness;
    }

    public int getNpcId() {
        return npcId;
    }

    public int getId() {
        return id;
    }

    public Position getPosition() {
        return position;
    }

    public String getTeleportName() {
        return teleportName;
    }

    public String getDifficulty() {
        return difficulty;
    }
}
