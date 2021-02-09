package com.zyrox.world.entity.impl.npc.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zyrox.model.GameMode;
import com.zyrox.model.Position;
import com.zyrox.model.container.impl.Equipment;
import com.zyrox.model.definitions.ItemDefinition;
import com.zyrox.model.definitions.NPCDrops;
import com.zyrox.model.item.RingOfBosses;
import com.zyrox.util.Misc;
import com.zyrox.world.content.Wildywyrm;
import com.zyrox.world.content.combat.CombatBuilder;
import com.zyrox.world.content.combat.CombatFactory;
import com.zyrox.world.entity.impl.npc.NPC;
import com.zyrox.world.entity.impl.player.Player;

public class KingKurask extends NPC {

    public KingKurask(int id, Position position) {
        super(id, position);
    }

    @Override
    public boolean clearDamageMapOnDeath() {
        return false;
    }

    @Override
    public void dropItems(Player pla) {

        if (getCombatBuilder().getDamageMap().size() == 0) {
            return;
        }

        Map<Player, Integer> killers = new HashMap<>();

        for (Map.Entry<Player, CombatBuilder.CombatDamageCache> entry : getCombatBuilder().getDamageMap().entrySet()) {

            if (entry == null) {
                continue;
            }

            long timeout = entry.getValue().getStopwatch().elapsed();

            if (timeout > CombatFactory.DAMAGE_CACHE_TIMEOUT) {
                continue;
            }

            Player player = entry.getKey();

            if (player.getConstitution() <= 0 || !player.isRegistered()) {
                continue;
            }

            killers.put(player, entry.getValue().getDamage());

        }

        getCombatBuilder().getDamageMap().clear();

        List<Map.Entry<Player, Integer>> result = Wildywyrm.sortEntries(killers);
        boolean[] dropsReceived = new boolean[12];
        final Position npcPos = getPosition().copy();
        NPCDrops drops = NPCDrops.forId(getId());

        int count = 0;

        for (Map.Entry<Player, Integer> entry : result) {

            Player killer = entry.getKey();

            final boolean goGlobal = killer.getPosition().getZ() >= 0 && killer.getPosition().getZ() < 4;
            final boolean ringOfWealth = killer.getEquipment().get(Equipment.RING_SLOT).getId() == 2572;
            final boolean ringOfBosses = RingOfBosses.isWearing(killer);

            for (int i = 0; i < drops.getDropList().length; i++) {
                if (drops.getDropList()[i].getItem().getId() <= 0 || drops.getDropList()[i].getItem().getId() > ItemDefinition.getMaxAmountOfItems() || drops.getDropList()[i].getItem().getAmount() <= 0) {
                    continue;
                }

                NPCDrops.DropChance dropChance = drops.getDropList()[i].getChance();

                if (dropChance == NPCDrops.DropChance.ALWAYS) {
                    NPCDrops.drop(killer, drops.getDropList()[i].getItem(), this, npcPos, goGlobal);
                } else {
                    if (NPCDrops.shouldDrop(dropsReceived, this.getId(), dropChance, ringOfWealth, ringOfBosses, false, false, false, false, killer.getGameMode() == GameMode.HARDCORE_IRONMAN || killer.getGameMode() == GameMode.IRONMAN || killer.getGameMode() == GameMode.ULTIMATE_IRONMAN, killer)) {
                        NPCDrops.drop(killer, drops.getDropList()[i].getItem(), this, npcPos, goGlobal);
                        dropsReceived[dropChance.ordinal()] = true;
                    }
                }
            }

            if (Misc.random(5) == 1) {
                killer.addBossPoints(2);
            }

            count++;

            if (count >= 2) {
                break;
            }
        }
    }
}
