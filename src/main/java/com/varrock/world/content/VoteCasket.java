package com.varrock.world.content;

import com.varrock.GameServer;
import com.varrock.engine.task.impl.BonusExperienceTask;
import com.varrock.model.GameMode;
import com.varrock.model.Item;
import com.varrock.model.definitions.ItemDefinition;
import com.varrock.model.item.Items;
import com.varrock.util.Misc;
import com.varrock.world.World;
import com.varrock.world.content.well_of_goodwill.WellBenefit;
import com.varrock.world.content.well_of_goodwill.WellOfGoodwill;
import com.varrock.world.entity.impl.player.Player;

/**
 * Created by Jonny on 10/5/2019
 **/
public class VoteCasket {

    public static int CASKET_ID = 13077;

    public static void open(Player player) {
        if (!player.getInventory().contains(13077)) {
            return;
        }

        int minutes = player.getGameMode() == GameMode.NORMAL ? 20 : 10;
        BonusExperienceTask.addBonusXp(player, minutes);

        player.getPointsHandler().setDungeoneeringTokens(10_000, true);
        player.getPointsHandler().setVotingPoints(World.getWell().isActive(WellBenefit.VOTE_POINTS) ? 2 : 1, true);

        player.getInventory().delete(CASKET_ID, 1);

        Item item;

        int chance = Misc.random(50000);

        boolean superRare = false;
        boolean rare = false;

        if (chance == 0) { //super rare
            item = SUPER_RARE_REWARDS[Misc.random(SUPER_RARE_REWARDS.length - 1)];
            superRare = true;
        } else if (chance <= 75) { //rare
            item = RARE_REWARDS[Misc.random(RARE_REWARDS.length - 1)];
            rare = true;
        } else if (chance <= 2000) { //uncommon
            item = UNCOMMON_REWARDS[Misc.random(UNCOMMON_REWARDS.length - 1)];
        } else { //common
            item = COMMON_REWARDS[Misc.random(COMMON_REWARDS.length - 1)];
        }

        if(rare || superRare) {
            String text = player.getUsername() + " received a " + item.getDefinition().getName() + " "
                    + (item.getAmount() > 1 ? ("x" + item.getAmount() + " ") : "") + "from ::vote!";
            sendAnnouncement("<img=464><shad=CA7936>" + text);
        }

        String alert = ":shortalert:";

        if(superRare) {
            sendAnnouncement(alert + " :n:" + player.getUsername()
                    + " received a " + ItemDefinition.forId(item.getId()).getName() + " from ::vote! ");
        }

        player.getInventory().add(item);

        player.sendMessage("<img=464><shad=CA7936>You open the voting casket and receive 1 vote point, 10k dungeoneering tokens");
        player.sendMessage("<img=464><shad=CA7936> ... and "+item.getAmount()+"x "+item.getDefinition().getName()+"!");

        PlayerPanel.refreshPanel(player);
    }

    static void sendAnnouncement(String announcement) {
        for (Player player : World.getPlayers()) {
            if (player != null) {
                if (player.boxAlertEnabled)
                    player.getPacketSender().sendMessage(announcement);
            }
        }

    }

    public static Item[] COMMON_REWARDS = new Item[]{
            new Item(Items.AMULET_OF_GLORY_4, 1),
            new Item(Items.FIRE_CAPE, 1),
            new Item(Items.AVAS_ACCUMULATOR, 1),
            new Item(Items.SARADOMIN_CAPE, 1),
            new Item(Items.DRAGON_SCIMITAR_OR, 1),
            new Item(Items.SEERS_RING, 1),
            new Item(Items.WARRIOR_RING, 1),
            new Item(Items.ARCHERS_RING, 1),
            new Item(Items.BERSERKER_RING, 1),
            new Item(Items.RUNE_PLATEBODY, 1),
            new Item(Items.RUNE_PLATELEGS, 1),
            new Item(Items.RUNE_FULL_HELM, 1),
            new Item(Items.BARROWS_GLOVES, 1),
            new Item(441, 25),
            new Item(452, 5),
            new Item(450, 15),
            new Item(1514, 20),
            new Item(1632, 10),
            new Item(15332, 1),
            new Item(15273, 25),
            new Item(560, 50),
            new Item(565, 50),
            new Item(555, 100),
            new Item(557, 100),
            new Item(7158, 1),
            new Item(43440, 20),
    };

    public static Item[] UNCOMMON_REWARDS = new Item[]{
            new Item(Items.AMULET_OF_FURY, 1),
            new Item(Items.ARCANE_STREAM_NECKLACE, 1),
            new Item(Items.AMULET_OF_RANGING, 1),
            new Item(Items.TOKHAAR_KAL, 1),
            new Item(Items.AVAS_ASSEMBLER, 1),
            new Item(Items.ABYSSAL_WHIP, 1),
            new Item(Items.ABYSSAL_TENTACLE, 1),
            new Item(Items.STAFF_OF_LIGHT, 1),
            new Item(Items.MASTER_WAND, 1),
            new Item(Items.DRAGON_HUNTER_CROSSBOW, 1),
            new Item(Items.DRAGON_DEFENDER, 1),
            new Item(Items.FLAMEBURST_DEFENDER, 1),
            new Item(Items.MAGES_BOOK, 1),
            new Item(Items.DRAGON_BOOTS, 1),
            new Item(Items.BARROWS___KARILS_SET, 1),
            new Item(Items.BARROWS___AHRIM_SET, 1),
            new Item(Items.BARROWS___DHAROK_SET, 1),
            new Item(Items.BARROWS___GUTHAN_SET, 1),
            new Item(Items.BARROWS___TORAG_SET, 1),
            new Item(Items.BARROWS___VERAC_SET, 1),
            new Item(Items.SEERS_RING_I, 1),
            new Item(Items.ARCHERS_RING_I, 1),
            new Item(Items.WARRIOR_RING_I, 1),
            new Item(Items.ZURIELS_HOOD, 1),
            new Item(Items.ZURIELS_ROBE_BOTTOM, 1),
            new Item(Items.ZURIELS_ROBE_TOP, 1),
            new Item(Items.ZURIELS_STAFF, 1),
            new Item(Items.MORRIGANS_COIF, 1),
            new Item(Items.MORRIGANS_JAVELIN, 25),
            new Item(Items.MORRIGANS_LEATHER_BODY, 1),
            new Item(Items.MORRIGANS_LEATHER_CHAPS, 1),
            new Item(Items.MORRIGANS_THROWING_AXE, 25),
    };

    public static Item[] RARE_REWARDS = new Item[]{
            new Item(Items.AMULET_OF_TORTURE, 1),
            new Item(Items.ABYSSAL_VINE_WHIP_1, 1),
            new Item(Items.BERSERKER_RING_I, 1),
            new Item(Items.VOLCANIC_ABYSSAL_WHIP, 1),
            new Item(Items.TRIDENT_OF_THE_SEAS, 1),
            new Item(Items.TOXIC_STAFF_OF_THE_DEAD, 1),
            new Item(Items.ARMADYL_CROSSBOW, 1),
            new Item(Items.TOXIC_BLOWPIPE, 1),
            new Item(Items.TWISTED_BUCKLER, 1),
            new Item(Items.DRAGONFIRE_SHIELD, 1),
            new Item(Items.SPECTRAL_SPIRIT_SHIELD, 1),
            new Item(Items.BANDOS_CHESTPLATE, 1),
            new Item(Items.BANDOS_TASSETS, 1),
            new Item(Items.ARMADYL_HELMET, 1),
            new Item(Items.ARMADYL_CHESTPLATE, 1),
            new Item(Items.ARMADYL_CHAINSKIRT, 1),
            new Item(Items.RAGEFIRE_BOOTS, 1),
            new Item(Items.GLAIVEN_BOOTS, 1),
            new Item(Items.STEADFAST_BOOTS, 1),
            new Item(Items.EYE_OF_THE_MAGE, 1),
            new Item(Items.EYE_OF_THE_WARRIOR, 1),
            new Item(Items.EYE_OF_THE_RANGE, 1),
            new Item(Items.ARMADYL_GODSWORD, 1),
            new Item(Items.DRAGON_CLAWS, 1),
            new Item(Items.KORASIS_SWORD, 1),
            new Item(Items.SERPENTINE_HELM, 1),
            new Item(Items.ELYSIAN_SPIRIT_SHIELD, 1),
            new Item(Items.ARCANE_SPIRIT_SHIELD, 1),
    };

    public static Item[] SUPER_RARE_REWARDS = new Item[]{
            new Item(Items.GREEN_PARTYHAT, 1),
            new Item(Items.PURPLE_PARTYHAT, 1),
            new Item(Items.RED_PARTYHAT, 1),
            new Item(Items.WHITE_PARTYHAT, 1),
            new Item(Items.YELLOW_PARTYHAT, 1),
            new Item(Items.BLUE_PARTYHAT, 1),
            new Item(Items.SANTA_HAT, 1),
            new Item(Items.BLUE_HALLOWEEN_MASK, 1),
            new Item(Items.GREEN_HALLOWEEN_MASK, 1),
            new Item(Items.RED_HALLOWEEN_MASK, 1),
            new Item(Items.JUSTICIAR_CHESTGUARD, 1),
            new Item(Items.JUSTICIAR_FACEGUARD, 1),
            new Item(Items.JUSTICIAR_LEGGUARDS, 1),
            new Item(Items.ANCESTRAL_HAT, 1),
            new Item(Items.ANCESTRAL_ROBE_BOTTOM, 1),
            new Item(Items.ANCESTRAL_ROBE_TOP, 1),
            new Item(Items.DEVOUT_BOOTS, 1),
            new Item(Items.SCYTHE_OF_VITUR, 1),
            new Item(Items.GHRAZI_RAPIER, 1),
            new Item(Items.TWISTED_BOW, 1),
    };
}
