package com.zyrox.world.content.skill.impl.crafting.jewellery;

import java.util.Arrays;

import com.zyrox.engine.task.Task;
import com.zyrox.engine.task.TaskManager;
import com.zyrox.model.Animation;
import com.zyrox.model.Position;
import com.zyrox.model.Skill;
import com.zyrox.model.definitions.ItemDefinition;
import com.zyrox.world.content.interfaces.MakeInterface;
import com.zyrox.world.entity.impl.player.Player;

/**
 * Created by Jonny on 10/10/2019
 **/
public class JewelleryMaking {

    public enum GemData {

        SAPPHIRE(1607,
                new JewelleryItem[] {new JewelleryItem(1637, 20, 40, JewelleryType.RING),
                new JewelleryItem(1656, 22, 55, JewelleryType.NECKLACE),
                new JewelleryItem(11072, 23, 60, JewelleryType.BRACELET),
                new JewelleryItem(1675, 24, 65, JewelleryType.AMULET),
        }),

        EMERALD(1605,
                new JewelleryItem[] {new JewelleryItem(1639, 27, 55, JewelleryType.RING),
                new JewelleryItem(1658, 29, 60, JewelleryType.NECKLACE),
                new JewelleryItem(11076, 30, 65, JewelleryType.BRACELET),
                new JewelleryItem(1677, 31, 70, JewelleryType.AMULET),
        }),

        RUBY(1603,
                new JewelleryItem[] {new JewelleryItem(1641, 34, 70, JewelleryType.RING),
                new JewelleryItem(1660, 40, 75, JewelleryType.NECKLACE),
                new JewelleryItem(11085, 42, 80, JewelleryType.BRACELET),
                new JewelleryItem(1679, 50, 85, JewelleryType.AMULET),
        }),

        DIAMOND(1601,
                new JewelleryItem[] {new JewelleryItem(1643, 43, 85, JewelleryType.RING),
                new JewelleryItem(1662, 56, 90, JewelleryType.NECKLACE),
                new JewelleryItem(11092, 58, 95, JewelleryType.BRACELET),
                new JewelleryItem(1681, 70, 100, JewelleryType.AMULET),
        }),

        DRAGONSTONE(1615,
                new JewelleryItem[] {new JewelleryItem(1645, 55, 100, JewelleryType.RING),
                new JewelleryItem(1683, 80, 115, JewelleryType.AMULET),
        }),

        ONYX(6573,
                new JewelleryItem[] {new JewelleryItem(6575, 67, 100, JewelleryType.RING),
                new JewelleryItem(6577, 82, 120, JewelleryType.NECKLACE),
                new JewelleryItem(11130, 84, 125, JewelleryType.BRACELET),
                new JewelleryItem(6579, 90, 165, JewelleryType.AMULET),
        }),

        NONE(-1,
                new JewelleryItem[] {new JewelleryItem(1635, 5, 15, JewelleryType.RING),
                        new JewelleryItem(1654, 6, 20, JewelleryType.NECKLACE),
                        new JewelleryItem(11069, 7, 25, JewelleryType.BRACELET),
                        new JewelleryItem(1673, 8, 30, JewelleryType.AMULET),
                }),

        ;

        GemData(int gemId, JewelleryItem[] jewellery) {
            this.gemId = gemId;
            this.jewllery = jewellery;
        }

        private int gemId;

        private JewelleryItem[] jewllery;

        public int getGemId() {
            return gemId;
        }

        public static GemData getFirstGem(Player player) {
            for (GemData data : GemData.values()) {
                if(player.getInventory().contains(data.gemId))
                    return data;
            }
            return GemData.NONE;
        }

        public static GemData getGemData(int gemId) {
            for (GemData data : GemData.values()) {
                if(gemId == data.gemId)
                    return data;
            }
            return GemData.NONE;
        }

        public JewelleryItem[] getJewllery() {
            return jewllery;
        }
    }

    public static void selectionInterface(Player player) {

        player.getPacketSender().sendInterfaceRemoval();

        GemData data = GemData.getFirstGem(player);

        /*player.setSelectedSkillingItem(data.getGemId());

        player.setInputHandling(new EnterAmountOfJewelleryToMake());

        Item[] items = new Item[data.getJewllery().length];
        int index = 0;
        for(JewelleryItem jewelleryItem : data.getJewllery()) {
            items[index] = new Item(jewelleryItem.getItemId(), 1);
            index++;
        }
        player.getSkillManager().sendChatboxInterface(items);*/
        player.setSelectedSkillingItem(data.getGemId());
        int[] items = Arrays.stream(data.getJewllery()).map(JewelleryItem::getItemId).mapToInt(i -> i).toArray();
        MakeInterface.open(player, items, MakeInterface.MakeType.JEWELLERY);

    }

    public static void make(final  Player player, final int amount, int index) {
        GemData gemData = JewelleryMaking.GemData.getGemData(player.getSelectedSkillingItem());
        JewelleryItem jewelleryItem = gemData.getJewllery()[index];
        make(player, amount, jewelleryItem, gemData);
    }

    public static void make(final Player player, final int amount, final JewelleryItem toMake, GemData gemData) {
        player.getPacketSender().sendInterfaceRemoval();
        player.getSkillManager().stopSkilling();

        if (player.getSkillManager().getMaxLevel(Skill.CRAFTING) < toMake.getLevelRequirement()) {
            player.getPacketSender()
                    .sendMessage("You need a Crafting level of atleast " + toMake.getLevelRequirement() + " to craft this.");
            return;
        }

        if (!player.getInventory().contains(toMake.getType().mouldId)) {
            player.getPacketSender()
                    .sendMessage("You must have a "+ ItemDefinition.forId(toMake.getType().mouldId).getName()+" in your inventory to craft this.");
            return;
        }

        player.setCurrentTask(new Task(2, player, true) {
            int amountCut = 0;

            @Override
            public void execute() {
                if (!player.getInventory().contains(gemData.getGemId()) || !player.getInventory().contains(2357)) {
                    stop();
                    return;
                }

                player.setPositionToFace(new Position(3022, 9742, 0));
                player.performAnimation(new Animation(896));

                player.getInventory().delete(gemData.getGemId(), 1);
                player.getInventory().delete(2357, 1);
                player.getInventory().add(toMake.getItemId(), 1);

                player.getSkillManager().addExperience(Skill.CRAFTING, toMake.getXp() * Skill.CRAFTING.getModifier());
                amountCut++;
                if (amountCut >= amount)
                    stop();
            }
        });
        TaskManager.submit(player.getCurrentTask());
    }

}
