package com.zyrox.world.content.treasuretrails;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.zyrox.model.GroundItem;
import com.zyrox.model.Item;
import com.zyrox.model.Position;
import com.zyrox.model.item.Items;
import com.zyrox.model.item.RingOfBosses;
import com.zyrox.util.Misc;
import com.zyrox.util.RandomUtility;
import com.zyrox.world.content.ClueScrolls;
import com.zyrox.world.entity.impl.GroundItemManager;
import com.zyrox.world.entity.impl.npc.NPC;
import com.zyrox.world.entity.impl.player.Player;

import java.util.EnumSet;
import java.util.Optional;

/**
 * Handles Elite clue scrolls
 *
 * @author 2012
 */
public class EliteClueScroll {

    /**
     * The chance of dropping the scroll
     */
    public static final int CHANCE = 50;

    /**
     * The casket
     */
    public static final int CASKET = 42_084;

    /**
     * The rewards
     */
    private static final Item[] REWARDS = {new Item(18339), new Item(6739), new Item(13896), new Item(13884),
            new Item(13890), new Item(13876), new Item(13870), new Item(13873), new Item(13887), new Item(13893),
            new Item(13864), new Item(13861), new Item(13858), new Item(13867), new Item(13902), new Item(13905),
            new Item(13899), new Item(6573), new Item(11848),
            new Item(11846), new Item(11850), new Item(11856), new Item(11854), new Item(11852), new Item(10025),
            new Item(52428),

    };

    /**
     * The low level rewards
     */
    private static final Item[] LOW_LEVEL_REWARD = {new Item(Items.UNCUT_SAPPHIRE_NOTED, 250), new Item(Items.UNCUT_EMERALD_NOTED, 250), new Item(Items.UNCUT_DIAMOND_NOTED, 250),
            new Item(Items.UNCUT_RUBY_NOTED, 250), new Item(Items.UNCUT_DRAGONSTONE_NOTED, 250), new Item(2364, 100), new Item(2358, 250), new Item(2362, 250),
            new Item(2360, 250), new Item(454, 1500),};

    /**
     * Those who drop the elite clue
     */
    public static final String[] DROPPERS = {"Nex", "Corporeal Beast", "Bork", "Warmonger", "Tekton", "Muhammad Ali", "Wildywyrm", "Alchemical Hydra", "Callisto", "Venenatis", "Scorpia", "Skeletal Mystic", "Chaos elemental", "Tarn", "Raids"};

    /**
     * The elite clues
     */
    public enum EliteClue {

        CLUE_1(42073, new Position(3102, 3568, 0), "Dig at level 7 wildy at mining rocks."),

        CLUE_2(42074, new Position(3099, 3624, 0), "Dig at level 14 wildy at a statue of a warrior."),

        CLUE_3(42075, new Position(3036, 3687, 0), "Dig at level 21 wildy at a Den between 2 fires"),

        CLUE_4(42076, new Position(2980, 3763, 0), "Dig at level 31 wildy at a graveyard between coffins"),

        CLUE_5(42077, new Position(3286, 3881, 0), "Dig at level 46 wildy on a star guarded by demons"),

        CLUE_6(42078, new Position(3367, 3935, 0), "Dig at level 52 wildy on a bridge where no one goes."),

        CLUE_7(42079, new Position(3190, 3957, 0), "Dig at level 55 wildy by a pick-lock door"),

        CLUE_8(42080, new Position(3095, 3959, 0), "Dig at level 55 wildy outside a wooden door"),

        CLUE_9(42081, new Position(3062, 3951, 0), "Dig at level 54 wildy by an anvil that no one uses"),

        CLUE_10(42082, new Position(3018, 3962, 0), "Dig at level 56 wildy on a ship wreckage");

        public static final ImmutableMap<Integer, EliteClue> CLUE_MAP;

        static {
            ImmutableMap.Builder<Integer, EliteClue> map = ImmutableMap.builder();
            for (EliteClue clue : values()) {
                map.put(clue.id, clue);
            }
            CLUE_MAP = map.build();
        }

        /**
         * The id
         */
        private final int id;

        /**
         * The position
         */
        private final Position position;

        /**
         * The description
         */
        private final String description;

        /**
         * Represents an elite clue
         *
         * @param id the id
         * @param position the position
         * @param description the description
         */
        EliteClue(final int id, final Position position, final String description) {
            this.id = id;
            this.position = position;
            this.description = description;
        }

        /**
         * The values
         */
        private static final ImmutableSet<EliteClue> VALUES = Sets.immutableEnumSet(EnumSet.allOf(EliteClue.class));

        /**
         * The clues
         */
        private static final EliteClue[] CLUES = EliteClue.values();

        /**
         * Gets the clue by id
         *
         * @param id the id
         * @return the clue
         */
        public static java.util.Optional<EliteClue> getId(int id) {
            return VALUES.stream().filter(source -> source.getId() == id).findAny();
        }

        /**
         * Gets the clue by position
         *
         * @param position the position
         * @return the clue
         */
        public static java.util.Optional<EliteClue> forPosition(Position position) {
            return VALUES.stream().filter(source -> source.getPosition().sameAs(position)).findAny();
        }

        /**
         * Gets the id
         *
         * @return the id
         */
        public int getId() {
            return id;
        }

        /**
         * Gets the position
         *
         * @return the position
         */
        public Position getPosition() {
            return position;
        }

        /**
         * Gets the description
         *
         * @return the description
         */
        public String getDescription() {
            return description;
        }

        /**
         * Gets a random clue scroll
         *
         * @return the clue
         */
        public static int getRandomScroll() {
            return CLUES[Misc.getRandom(CLUES.length - 1)].getId();
        }
    }

    /**
     * Dropping the clue
     *
     * @param player the player
     * @param npc the npc
     */
    public static void dropClue(Player player, NPC npc) {
        String name = npc.getDefinition().getName().toLowerCase();

        boolean drop = false;

        for (String s : DROPPERS) {
            if (s.toLowerCase().contains(name)) {
                drop = true;
                break;
            }
        }

        int chance = chanceForID(npc.getId());

        if (drop) {
            if (Misc.getRandom(chance) == 1) {
                Item clue = new Item(EliteClue.getRandomScroll());
                if (!RingOfBosses.collectLoot(player, clue)) {
                    GroundItemManager.spawnGroundItem(player, new GroundItem(clue,
                            npc.getPosition().copy(), player.getUsername(), false, 100, true, 100));
                }
                player.getPacketSender().sendMessage("@red@An elite clue scroll has appeared on the ground!");
            }
        }
    }

    public static boolean drops(String name) {
        for (String s : DROPPERS) {
            if (s.toLowerCase().contains(name.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Adds reward
     *
     * @param player the player
     */
    public static void addReward(Player player) {
        if (!player.getInventory().contains(CASKET)) {
            return;
        }

        player.getInventory().delete(CASKET, 1);

        player.getInventory().add(ClueScrolls.LOW_LEVEL_REWARD[Misc.getRandom(ClueScrolls.LOW_LEVEL_REWARD.length - 1)],
                1, "Clue reward");
        player.getInventory().add(LOW_LEVEL_REWARD[Misc.getRandom(LOW_LEVEL_REWARD.length - 1)]);
        player.getInventory().add(ClueScrolls.BASIC_STACKS[Misc.getRandom(ClueScrolls.BASIC_STACKS.length - 1)],
                RandomUtility.RANDOM.nextInt(200) + Misc.random(60), "Clue reward");

        player.getInventory().add(REWARDS[Misc.getRandom(REWARDS.length - 1)]);
    }

    /**
     * Digging for clue
     *
     * @param player the player
     * @return the clue
     */
    public static boolean dig(Player player) {
        Optional<EliteClue> clue = EliteClue.forPosition(player.getPosition());

        if (!clue.isPresent()) {
            return false;
        }

        if (!player.getInventory().contains(clue.get().getId())) {
            return false;
        }

        player.getInventory().delete(clue.get().getId(), 1);

        player.eliteClueSteps++;

        if (player.eliteClueSteps == 6) {
            player.getInventory().add(CASKET, 1);
            player.eliteClueSteps = 0;
            player.getPacketSender().sendMessage("You find a casket!");
        } else {
            player.getInventory().add(EliteClue.getRandomScroll(), 1);
            player.getPacketSender().sendMessage("You find a another clue!");
        }
        return true;
    }

    /**
     * Reading the scroll
     *
     * @param player the player
     * @param id the id
     */
    public static boolean read(Player player, int id) {
        Optional<EliteClue> clue = EliteClue.getId(id);

        if (!clue.isPresent()) {
            return false;
        }

        player.getPacketSender().sendInterface(47700);
        player.getPacketSender().sendString(47704, clue.get().getDescription());
        player.getPacketSender().sendString(47703, "");
        return true;
    }

    public static int chanceForID(int id) {
        switch (id) {
            case 23098:
                return 40;
            case 7134: //Bork
                return 100;
            case 23615: //Hydra
                return 70;
            case 22542: //Tekton
                return 50;
            case 3334: //Wildywyrm
                return 30;
            case 2009: //Callisto
                return 30;
            case 2000: //Venenatis
                return 30;
            case 2001: //Scorpia
                return 30;
            case 22606: //Skeletal Mystic
                return 30;
            case 3200: //Chaos Elemental
                return 20;
            case 21475: //Tarn
                return 50;

        }
        return 50;
    }
}
