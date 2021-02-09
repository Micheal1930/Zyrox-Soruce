package com.zyrox.world.content.skill.impl.runecrafting;

import com.zyrox.world.entity.impl.player.Player;

public class RunecraftingPouches {


    public static final int RUNE_ESS = 1436, PURE_ESS = 7936;

    public enum RunecraftingPouch {
        SMALL(5509, 7),
        MEDIUM_POUCH(5510, 16),
        LARGE_POUCH(5512, 28);

        RunecraftingPouch(int id, int maxEssence) {
            this.id = id;
            this.maxEssence = maxEssence;
        }

        public final int id;
        public final int maxEssence;

        public RunecraftingPouchContainer getContainer(Player player) {
            switch (this) {
                case SMALL:
                    return player.smallPouch;
                case MEDIUM_POUCH:
                    return player.mediumPouch;
                case LARGE_POUCH:
                    return player.largePouch;
                default:
                    throw new IllegalStateException("unreachable");
            }
        }

        public static RunecraftingPouch forId(int id) {
            for (RunecraftingPouch pouch : RunecraftingPouch.values()) {
                if (pouch.id == id)
                    return pouch;
            }
            return null;
        }
    }

    public static void fill(Player p, RunecraftingPouch pouch) {
        if (p.getInterfaceId() > 0) {
            p.getPacketSender().sendMessage("Please close the interface you have open before doing this.");
            return;
        }
        RunecraftingPouchContainer container = pouch.getContainer(p);
        int pEss = p.getInventory().getAmount(PURE_ESS);
        if (pEss > 0 && container.getRuneEssence() <= 0) {
            fillEssence(p, PURE_ESS, pEss, container);
            return;
        }
        int rEss = p.getInventory().getAmount(RUNE_ESS);
        if (rEss > 0 && container.getPureEssence() <= 0) {
            fillEssence(p, RUNE_ESS, rEss, container);
            return;
        }
        // Something fucked up, somewhere.
        if (container.getPureEssence() > 0 && container.getRuneEssence() > 0) {
            p.sendMessage("Your pouch has both rune and pure essence. Please empty your pouches and report this message to a staff member.");
            return;
        }
        p.getPacketSender().sendMessage("You do not have any essence in your inventory.");
    }

    public static void empty(Player p, RunecraftingPouch pouch) {
        if (p.getInterfaceId() > 0) {
            p.getPacketSender().sendMessage("Please close the interface you have open before doing this.");
            return;
        }
        RunecraftingPouchContainer container = pouch.getContainer(p);
        int invSpace = p.getInventory().getFreeSlots();
        if (invSpace == 0) {
            p.getPacketSender().sendMessage("You do not have enough inventory space to empty your pouch.");
            return;
        }
        if (container.getEssence() == 0) {
            p.getPacketSender().sendMessage("There is no essence in your pouch.");
            return;
        }
        int essenceType = container.getEssenceType();
        int amount = container.getEssence();
        if (amount > invSpace) {
            amount = invSpace;
        }
        p.getPacketSender().sendMessage("You remove " + amount + " essence from your pouch.");
        p.getInventory().add(essenceType, amount, "Runecrafting empty pouch");
        container.removeEssence(amount);
    }

    public static void check(Player p, RunecraftingPouch pouch) {
        RunecraftingPouchContainer container = pouch.getContainer(p);
        if (container.getPureEssence() > 0 && container.getRuneEssence() > 0) {
            p.sendMessage("Your pouch has both rune and pure essence. Please empty your pouches and report this message to a staff member.");
            return;
        }
        if (container.getRuneEssence() > 0)
            p.getPacketSender().sendMessage("Your pouch currently contains " + container.getRuneEssence() + "/" + pouch.maxEssence + " Rune essence.");
        else if (container.getPureEssence() > 0)
            p.getPacketSender().sendMessage("Your pouch currently contains " + container.getPureEssence() + "/" + pouch.maxEssence + " Pure essence.");
        else
            p.getPacketSender().sendMessage("The pouch is empty!");
    }

    public static void fillEssence(Player p, int id, int amount, RunecraftingPouchContainer pouch) {
        int maxEssence = pouch.getType().maxEssence;
        int currentEssence = pouch.getEssence();
        if (currentEssence >= maxEssence) {
            p.getPacketSender().sendMessage("Your pouch can not hold any more essence.");
            return;
        }
        if (currentEssence + amount > maxEssence) {
            amount = maxEssence - currentEssence;
        }
        if (id == RUNE_ESS) {
            p.getInventory().delete(RUNE_ESS, amount, true);
            pouch.addRuneEssence(amount);
        } else if (id == PURE_ESS) {
            p.getInventory().delete(PURE_ESS, amount, true);
            pouch.addPureEssence(amount);
        }
        p.getPacketSender().sendMessage("You fill your pouch with " + amount + " essence.");
    }
}
