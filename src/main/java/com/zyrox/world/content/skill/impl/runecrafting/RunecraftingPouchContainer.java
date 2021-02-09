package com.zyrox.world.content.skill.impl.runecrafting;

import static com.google.common.base.Preconditions.checkState;

import com.zyrox.world.content.skill.impl.runecrafting.RunecraftingPouches.RunecraftingPouch;
import com.zyrox.world.entity.impl.player.Player;

/**
 * @author lare96 <http://github.com/lare96>
 */
public final class RunecraftingPouchContainer {

    private final Player player;
    private final RunecraftingPouch type;
    private int runeEssence;
    private int pureEssence;

    public RunecraftingPouchContainer(Player player, RunecraftingPouch type) {
        this.player = player;
        this.type = type;
    }

    public void addRuneEssence(int amount) {
        checkState(amount + runeEssence <= type.maxEssence, "Will exceed capacity.");
        checkState(pureEssence == 0, "Cannot add rune essence alongside pure essence.");
        runeEssence += amount;
    }

    public void removeRuneEssence(int amount) {
        checkState(runeEssence - amount >= 0, "Cannot remove below 0.");
      runeEssence -= amount;
    }

    public int getRuneEssence() {
        return runeEssence;
    }

    public void addPureEssence(int amount) {
        checkState(amount + pureEssence <= type.maxEssence, "Will exceed capacity.");
        checkState(runeEssence == 0, "Cannot add pure essence alongside rune essence.");
        pureEssence += amount;
    }

    public void removePureEssence(int amount) {
        checkState(pureEssence - amount >= 0, "Cannot remove below 0.");
        pureEssence -= amount;
    }

    public int getPureEssence() {
        return pureEssence;
    }

    public int getEssence() {
        if (runeEssence > 0) {
            return runeEssence;
        } else if (pureEssence > 0) {
            return pureEssence;
        } else {
            return 0;
        }
    }

    public int getEssenceType() {
        if (runeEssence > 0) {
            return RunecraftingPouches.RUNE_ESS;
        } else if (pureEssence > 0) {
            return RunecraftingPouches.PURE_ESS;
        } else {
           return -1;
        }
    }
    public void removeEssence(int amount) {
        if (runeEssence > 0) {
            removeRuneEssence(amount);
        } else if (pureEssence > 0) {
            removePureEssence(amount);
        } else {
            throw new IllegalStateException("cannot remove essence");
        }
    }

    public RunecraftingPouch getType() {
        return type;
    }

    public boolean has() {
        return player.getInventory().contains(type.id);
    }
}