package com.zyrox;

import java.io.*;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import com.zyrox.model.Position;
import com.zyrox.model.container.impl.Equipment;
import com.zyrox.model.item.Items;
import com.zyrox.net.login.FloodCheckStatus;
import com.zyrox.net.security.ConnectionHandler;
import com.zyrox.util.Misc;
import com.zyrox.world.entity.impl.player.Player;

public class GameSettings {

	public static boolean STAFF_ONLY = false;

	public static final double CLIENT_VERSION = 1.05;

	public static final double LAST_OUDATED_VERSION = -1;

	public final static String TEXT_SEPERATOR = "#=#";

	public static GameType GAME_TYPE = GameType.LIVE;

	/**
	 * If true, the SQL network is mocked.
	 */
	public static boolean MOCK_SQL = true;

	public static boolean FORUM_INTEGRATION = false;

	/**
	 * True, when developing.
	 */
	public static boolean DEBUG_MODE = false;

	/**
	 * The special staff names.
	 */
	public static final Set<String> SPECIAL_STAFF_NAMES = new HashSet<>(
			Arrays.asList("Hobos", "Sedveka", " ", " "));

	public static final Set<String> HIGHER_STAFF_NAMES = new HashSet<>(Arrays.asList());

	public static final Set<String> DONATION_PUSHERS = new HashSet<>(Arrays.asList("Hobos", "Sedveka", " ", " "));

	public static final Set<String> INVESTIGATOR = new HashSet<>(Arrays.asList());

	public static final Set<String> STAFF_NAMES = new HashSet<>(
			Arrays.asList("Hobos", "Sedveka", " ", " "));

	public static final Set<String> GOD_MODE = new HashSet<>(Arrays.asList());

	public static final Set<String> ONEHIT = new HashSet<>(Arrays.asList());

	public static final HashMap<String, FloodCheckStatus> CHECKED_IPS = new HashMap<>();

	public static final Set<Integer> OLD_DONOR_ITEMS = new HashSet<>(
			Arrays.asList(Items.TWISTED_BOW_2, Items.DARK_SLED, Items.SLED, Items.ONE_BILLION_NOTE));

	public static final boolean CENSOR_ENABLED = true;

	public static int HIDDEN_PRIZE = 0;

	public static int HIDDEN_CHANCE = 0;

	public static Position EVENT_LOCATION = new Position(0, 0);

	public static boolean SPECIAL_EVENT_ENABLED = false;

	public static boolean DROP_ENABLED = true;

	public static boolean PARTY_ROOM_ENABLED = true;

	public static boolean ZULRAH_ENABLED = true;

	public static boolean TRADE_ENABLED = true;

	public static boolean POS_ENABLED = true;

	public static boolean DUEL_ENABLED = true;

	public static boolean HIGH_SECURITY = false;

	public static boolean COWS_ENABLED = false;

	public static int SUS_IP_SCORE = 74;

	/**
	 *
	 */
	public static final String[] CLIENT_HASH = { "d41d8cd98f00b204e9800998ecf8427e" };

	/**
	 * Mute newcomers.
	 */
	public static boolean MUTE_NEWCOMERS = false;

	/**
	 * Dzone activation
	 */
	public static boolean DZONEON = false;

	/**
	 * The OSRS item id offset.
	 */
	public static final int OSRS_ITEM_OFFSET = 30000;

	/**
	 * The OSRS object id offset.
	 */
	public static final int OSRS_OBJECT_OFFSET = 100_000;

	/**
	 * The OSRS npc id offset.
	 */
	public static final int OSRS_NPC_OFFSET = 15000;

	/**
	 * The OSRS animation id offset.
	 */
	public static final int OSRS_ANIM_OFFSET = 15260;

	/**
	 * The OSRS gfx id offset.
	 */
	public static final int OSRS_GFX_OFFSET = 2964;

	/**
	 * The game port
	 */
	public static final int GAME_PORT = 43595; // dev world = port 43595

	/**
	 * The maximum amount of players that can be logged in on a single game
	 * sequence.
	 */
	public static final int LOGIN_THRESHOLD = 15;

	/**
	 * The maximum amount of players that can be logged in on a single game
	 * sequence.
	 */
	public static final int LOGOUT_THRESHOLD = 25;

	/**
	 * The maximum amount of players who can receive rewards on a single game
	 * sequence.
	 */
	public static final int VOTE_REWARDING_THRESHOLD = 15;

	/**
	 * The maximum amount of connections that can be active at a time, or in other
	 * words how many clients can be logged in at once per connection. (0 is counted
	 * too)
	 */
	public static final int CONNECTION_AMOUNT = 2;

	/**
	 * The throttle interval for incoming connections accepted by the
	 * {@link ConnectionHandler}.
	 */
	public static final long CONNECTION_INTERVAL = 1000;

	/**
	 * The number of seconds before a connection becomes idle.
	 */
	public static final int IDLE_TIME = 15;

	/**
	 * The login throttler. If this is enabled, then only a set amount of players
	 * are allowed to login per hour.
	 */
	public static boolean LOGIN_NEW_PLAYERS = true;

	/**
	 * The keys used for encryption on login
	 */
	
	public static final BigInteger RSA_MODULUS = new BigInteger("112126816726996843361991677568556313410060034725163997197622567729550364610933585225536866530033935786529007438259713466898849392890255394070802495078269596663965288977846684101761009193889726507869959690162814613066378271397065105111932484461869453799642246565778680782000524142034328289784998263568262923823");

	public static final BigInteger RSA_EXPONENT = new BigInteger("18263786388768044049762136778374637924415076532205100478884003090055238143670232422640738059540599425075867897575757835408169694510024510302665923599806639274480522769184668267932320730447835645153172512878923335418276219833475653465534373514619103864724815101416672489876320735733567273061517463914618475673");
	

	/**
	 * The maximum amount of messages that can be decoded in one sequence.
	 */
	public static final int DECODE_LIMIT = 30;

	/** GAME **/

	/**
	 * Processing the engine
	 */
	public static final int ENGINE_PROCESSING_CYCLE_RATE = 600;
	public static final int GAME_PROCESSING_CYCLE_RATE = 600;

	/**
	 * Spawn enabled.
	 */
	public static boolean SPAWN_ENABLED = true;

	/**
	 * Are the MYSQL services enabled?
	 */
	public static boolean MYSQL_ENABLED = true;

	/**
	 * The bonus experience timer.
	 */
	private static long bonusXpTimer;

	/**
	 * Checks if the bonus experience is enabled.
	 *
	 * @return <code>true</code> if enabled
	 */
	public static boolean isBonusXp() {
		return bonusXpTimer > System.currentTimeMillis();
	}

	/**
	 * Gets the minutes left of bonus xp.
	 *
	 * @return the minutes left
	 */
	public static long getBonusXpSecondsLeft() {
		if (bonusXpTimer < System.currentTimeMillis()) {
			return 0;
		}

		return TimeUnit.MILLISECONDS.toSeconds(bonusXpTimer - System.currentTimeMillis());
	}

	/**
	 * Increments the bonus xp.
	 *
	 * @param minutes the minutes
	 */
	public static void incrementBonusXp(long minutes) {
		long time = TimeUnit.MINUTES.toMillis(minutes);

		if (bonusXpTimer > System.currentTimeMillis()) {
			bonusXpTimer += time;
		} else {
			bonusXpTimer = System.currentTimeMillis() + time;
		}
	}

	/**
	 * Sets the bonus xp timer.
	 *
	 * @param bonusXp the bonus xp
	 */
	public static void setBonusXp(long bonusXp) {
		bonusXpTimer = bonusXp;
	}

	/**
	 * The donations deals.
	 */
	public static boolean DONATION_DEALS = true;

	/**
	 * The double donations.
	 */
	public static boolean DOUBLE_DONATIONS = true;

	/**
	 * The double voting. // Hobos False/True
	 */
	public static boolean DOUBLE_VOTING = true;

	public static boolean DOUBLE_EXPERIENCE = true;

	public static boolean DOUBLE_DROP_RATE = true;

	public static boolean DOUBLE_POINTS = true;

	public static final Position EDGEVILLE = new Position(3087, 3495,0); // (2551, 2550,1); OLD Custom Home

	/**
	 * The default position
	 */
	public static final Position DEFAULT_POSITION = new Position((3090 + Misc.getRandom(2)), 3497 + Misc.getRandom(2),
			0);
	//public static final Position DEFAULT_POSITION = new Position(2551, 2550,1);

	public static final int MAX_STARTERS_PER_IP = 2;

	public static final Set<String> UNTRADABLE_ITEM_NAMES = new HashSet<>(
			Arrays.asList("Clue scroll", "Casket", "Scroll box"));

	public static final int[] KEEP_ITEMS = { 18349, 18351, 18353, 18355, 18357, 18359, 18361, 18363, 18335, 16955,
			16425, 16909, 18346, 6500, 8839, 8840, 8841, 8842, 10611, 11663, 11664, 11665, 18337, 19669, 6570, 19111,
			8844, 8845, 8846, 8847, 8848, 8849, 8850, 20072, 7453, 7454, 7455, 7456, 7457, 7458, 7459, 7460, 7461, 7462,
			51295, 51285

	};

	/**
	 * Untradeable items Items which cannot be traded or staked
	 */

	public static final int[] OVERPOWERED_WEAPONS = { 15000, 20999, 21010, 21020, 52370, 52323, 21012, 16753, 16863,
			16929, 17169, 17235 };

	public static final int[] UNTRADEABLE_ITEMS = { 14596, 10933, 10939, 10940, 10941, 10945, 14936, 14937, 14938,
			14939, 13612, 13613, 13614, 13615, 13616, 13617, 13618, 13619, 13620, 13621, 13622, 13623, 13624, 13625,
			13626, 13627, 13628, 43241, 52316, 13661, 13262, 20072, 16956, 16426, 16910, 605, 13320, 17273, 13321,
			13322, 13323, 13324, 13325, 13326, 13327, 11995, 11996, 11997, 11978, 12001, 12002, 12003, 12004, 12005,
			12006, 11990, 11991, 11992, 11993, 11994, 11989, 11988, 11987, 11986, 11985, 11984, 11983, 11982, 11981,
			11980, 11979, 11967, 11969, 11970, 11971, 11972, 11973, 11974, 11975, 11976, 1590, 993, 6529, 6950, 1464,
			2996, 6570, 51214, 12158, 12159, 12160, 7986, 7981, 7980, 12163, 13247, 12655, 12646, 12161, 12162, 19143,
			19149, 19146, 6500, 19157, 19162, 19152, 4155, 1543, 51992, 50851, 52319, 50851, 8850, 10551, 8839, 16910,
			19669, 8840, 16909, 8842, 11663, 11664, 19712, 52191, 52378, 11665, 3842, 3844, 3840, 8844, 8845, 8846,
			8847, 51507, 52441, 52746, 52747, 52748, 52749, 52750, 52751, 52752, 8848, 8849, 8850, 10551, 7462, 7461,
			7460, 51637, 52284, 10637, 52607, 50445, 14019, 50447, 43329, 52606, 7459, 7458, 7457, 7456, 7455, 7454,
			7453, 11665, 10499, 9748, 9754, 9751, 9769, 9757, 9760, 9763, 9802, 9808, 14433, 14434, 14435, 14438, 9784,
			9799, 9805, 9781, 9796, 9793, 9775, 9772, 9778, 9787, 9811, 9766, 9749, 9755, 9752, 9770, 9758, 9761, 9764,
			9803, 9809, 9785, 9800, 9806, 9782, 9797, 9794, 9776, 9773, 9779, 9788, 9812, 52_109, 9767, 9747, 9753,
			9750, 9768, 9756, 9759, 9762, 810, 51285, 9801, 9807, 9783, 9798, 9804, 9780, 9795, 9792, 9774, 9771, 9777,
			9786, 9810, 9765, 9948, 9949, 3064, 9950, 12169, 12170, 12171, 20671, 14641, 14642, 3065, 6188, 10954,
			10956, 10958, 52316, 3057, 3058, 3059, 3060, 3061, 7594, 7592, 7593, 7595, 7596, 14076, 14077, 14081, 10840,
			10836, 6858, 6859, 10837, 16045, 16184, 10838, 10839, 9925, 9924, 9923, 9922, 9921, 20046, 20044, 20045,
			14595, 14603, 14602, 14605, 11789, 19708, 19706, 19707, 4860, 4866, 4872, 4878, 4884, 4896, 4890, 4896,
			4902, 4932, 4938, 4944, 4950, 4908, 4914, 4920, 4926, 4956, 4926, 4968, 4994, 4980, 4986, 4992, 4998, 18778,
			18779, 18780, 18781, 13450, 13444, 13405, 15502, 10548, 10549, 10550, 10551, 10555, 10552, 10553, 2412,
			2413, 2414, 20747, 18365, 18373, 18371, 15246, 12964, 12971, 12978, 14017, 757, 8851, 13855, 13848, 13849,
			13857, 13856, 13854, 13853, 13852, 13851, 13850, 5509, 13653, 14021, 14020, 19111, 14019, 14022, 19785,
			19786, 18782, 18351, 16955, 18349, 18361, 18363, 18353, 18357, 18355, 18359, 18335, 16425, 7453, 7454, 7455,
			7456, 7457, 7458, 7459, 7460, 7461, 7462, 19709, 51295, 21012, 7928, 7929, 7930, 7931, 7932, 7933, 50792,
			50794, 50796, 51295, 52322, };

	/**
	 * Unsellable items Items which cannot be sold to shops
	 */
	public static int UNSELLABLE_ITEMS[] = new int[] { 14596, 10933, 10939, 10940, 10941, 10945, 14936, 14937, 14938,
			14939, 13612, 13613, 13614, 13615, 13616, 13617, 13618, 13619, 13620, 13621, 13622, 13623, 13624, 13625,
			13626, 13627, 13628, 43241, 52316, 13058, 52191, 52516, 3064, 13263, 13281, 14019, 14022, 16956, 16426,
			16910, 16045, 18361, 18363, 16184, 16955, 16425, 13235, 19785, 19786, 1419, 4084, 15403, 10887, 15441,
			15442, 15443, 15444, 14004, 14005, 14006, 14007, 11848, 11850, 11856, 11854, 11852, 11846, 14000, 14001,
			14002, 14003, 2577, 19335, 15332, 19336, 19337, 19338, 19339, 19340, 9813, 20084, 8851, 6529, 14641, 14642,
			14017, 2996, 10941, 10939, 14938, 10933, 14936, 10940, 18782, 52746, 52747, 52748, 52749, 52750, 52751,
			52752, 14021, 14020, 13653, 5512, 5509, 5510, 10942, 10934, 10935, 10943, 10944, 7774, 7775, 7776, 10936,
			11852, 11854, 11856, 11858, 11848, 11850, 1038, 1040, 1042, 1044, 1046, 16909, 1048, // Phats
			1053, 1055, 1057, // Hween
			1050, // Santa
			19780, // Korasi's
			20671, // Brackish
			14485, 12926, 12927, 3065, 11695, 13741, 13743, 13745, 13747, 4706, 20998, 51214, 20999, 21010, 21020,
			20135, 20139, 20143, 20147, 20151, 20155, 20159, 20163, 20167, // Nex armors
			6570, // Fire cape
			19143, 19146, 19149, // God bows
			8844, 8845, 8846, 8847, 8848, 8849, 8850, 13262, 20072, 810, // Defenders
			20135, 20139, 20143, 20147, 20151, 20155, 20159, 20163, 20167, // Torva, pernix, virtus
			13746, 13748, 13750, 13752, 13738, 13740, 13742, 13744, // Spirit shields & Sigil
			11694, 11696, 11698, 11700, 11702, 11704, 11706, 11708, 11686, 11688, 11690, 11692, 11710, 11712, 11714, // Godswords,
																														// hilts,
																														// pieces
			15486, // sol
			11730, // ss
			11718, 11720, 11722, // armadyl
			11724, 11726, 11728, // bandos
			11286, 11283, 11613, // dfs & visage & dkite
			14472, 14474, 14476, 14479, // dragon pieces and plate
			14484, // dragon claws
			13887, 13888, 13893, 13895, 13899, 13901, 13905, 13907, 13911, 13913, 13917, 13919, 13923, 13925, 13929,
			13931, // Vesta's
			13884, 13886, 13890, 13892, 13896, 13898, 13902, 13904, 13908, 13910, 13914, 13916, 13920, 13922, 13926,
			13928, // Statius's
			13870, 13872, 13873, 13875, 13876, 13878, 13880, 13881, 13882, 13944, 13946, 13947, 13949, 13950, 13952,
			13953, 13954, 13955, 13956, 13957, // Morrigan's
			13858, 13860, 13861, 13863, 13864, 13866, 13867, 13869, 13932, 13934, 13935, 13937, 13938, 13940, 13941,
			13943, // Zuriel's
			20147, 20149, 20151, 20153, 20155, 20157, // Pernix
			20159, 20161, 20163, 20165, 20167, 20169, // Virtus
			20135, 20137, 20139, 20141, 20143, 20145, // Torva
			11335, // D full helm
			19111, // warrior ring, seers ring, archer ring
			962, // Christmas Cracker
			21787, 21790, 21793, // Steadfast, glaiven, ragefire
			20674, // Something something..... pvp armor, statuettes
			13958, 13961, 13964, 13967, 13970, 13973, 13976, 13979, 13982, 13985, 13988, 13908, 13914, 13926, 13911,
			13917, 13923, 13929, 13932, 13935, 13938, 13941, 13944, 13947, 13950, 13953, 13957, 13845, 13846, 13847,
			13848, 13849, 13850, 13851, 13852, 13853, 13854, 13855, 13856, 13857, // Le corrupted items
			11995, 6500, 19670, 20000, 20001, 20002, 11996, 18782, 18351, 18349, 18353, 18357, 13051, 18355, 18359,
			18335, 11997, 19712, 12001, 12002, 12003, 12005, 12006, 11990, 11991, 11992, 11993, 11994, 11989, 11988,
			11987, 11986, 11985, 11984, 11983, 11982, 11981, 11979,

			13661, 13262, 20072, 16956, 16426, 16910, 605, 13320, 17273, 13321, 13322, 13323, 13324, 13325, 13326,
			13327, 11995, 11996, 11997, 11978, 12001, 12002, 12003, 12004, 12005, 12006, 11990, 11991, 11992, 11993,
			11994, 11989, 11988, 11987, 11986, 11985, 11984, 11983, 11982, 11981, 11980, 11979, 11967, 11969, 11970,
			11971, 11972, 11973, 11974, 11975, 11976, 1590, 993, 6529, 6950, 1464, 2996, 6570, 12158, 12159, 12160,
			7986, 7981, 7980, 12163, 13247, 12655, 12646, 12161, 12162, 19143, 19149, 19146, 6500, 19157, 19162, 19152,
			4155, 8850, 10551, 8839, 16910, 19669, 8840, 16909, 8842, 11663, 11664, 19712, 11665, 3842, 3844, 3840,
			8844, 8845, 8846, 8847, 8848, 8849, 8850, 10551, 7462, 7461, 7460, 51214, 7459, 7458, 7457, 7456, 7455,
			7454, 7453, 11665, 10499, 9748, 9754, 9751, 9769, 9757, 9760, 9763, 9802, 9808, 9784, 9799, 9805, 9781,
			9796, 9793, 9775, 9772, 9778, 9787, 9811, 9766, 9749, 9755, 9752, 9770, 9758, 9761, 9764, 9803, 9809, 9785,
			9800, 9806, 9782, 9797, 9794, 9776, 9773, 9779, 9788, 9812, 9767, 9747, 9753, 9750, 9768, 9756, 9759, 9762,
			810, 9801, 9807, 9783, 9798, 9804, 9780, 9795, 9792, 9774, 9771, 9777, 9786, 9810, 9765, 9948, 9949, 9950,
			12169, 12170, 12171, 20671, 14641, 14642, 6188, 10954, 10956, 10958, 3057, 3058, 3059, 3060, 3061, 7594,
			7592, 7593, 7595, 7596, 14076, 14077, 14081, 10840, 10836, 6858, 6859, 10837, 16045, 16184, 10838, 10839,
			9925, 9924, 9923, 9922, 9921, 20046, 20044, 20045, 14595, 14603, 14602, 14605, 11789, 19708, 19706, 19707,
			4860, 4866, 4872, 4878, 4884, 4896, 4890, 4896, 4902, 4932, 4938, 4944, 4950, 4908, 4914, 4920, 4926, 4956,
			4926, 4968, 4994, 4980, 4986, 4992, 4998, 18778, 18779, 18780, 18781, 13450, 13444, 13405, 15502, 10548,
			10549, 10550, 10551, 10555, 10552, 10553, 2412, 2413, 2414, 20747, 18365, 18373, 18371, 15246, 12964, 12971,
			12978, 14017, 757, 8851, 13855, 13848, 13849, 13857, 13856, 13854, 13853, 13852, 13851, 13850, 5509, 13653,
			14021, 14020, 19111, 14019, 14022, 19785, 19786, 18782, 18351, 16955, 18349, 18361, 18363, 18353, 18357,
			18355, 18359, 18335, 16425, 7928, 7929, 7930, 7931, 7932, 7933

	};

	public static final int ATTACK_TAB = 0, SKILLS_TAB = 1, QUESTS_TAB = 2, ACHIEVEMENT_TAB = 14, INVENTORY_TAB = 3,
			EQUIPMENT_TAB = 4, PRAYER_TAB = 5, MAGIC_TAB = 6,

			SUMMONING_TAB = 13, FRIEND_TAB = 8, IGNORE_TAB = 9, CLAN_CHAT_TAB = 7, LOGOUT = 10, OPTIONS_TAB = 11,
			EMOTES_TAB = 12;

	public static boolean PC_ENABLED = true;

	public static boolean hasOverpoweredWeapon(Player player) {
		for (int id = 0; id < GameSettings.OVERPOWERED_WEAPONS.length; id++) {
			if (player.getInventory().contains(GameSettings.OVERPOWERED_WEAPONS[id]) || player.getEquipment()
					.get(Equipment.WEAPON_SLOT).getId() == GameSettings.OVERPOWERED_WEAPONS[id]) {
				return true;
			}
		}
		return false;
	}

	/**
	 * The directory in which developer settings are found.
	 */
	private static final String FILE_DIRECTORY = "./settings.txt";

	/**
	 * Load settings from settings.txt file
	 */
	public static void load() {
		System.out.println("Loading developer settings...");
		File file = new File(FILE_DIRECTORY);
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e) {
			System.out.println("settings.txt (developer settings) file not found.");
			return;
		}
		String line;
		try {
			while ((line = reader.readLine()) != null) {
				String[] args = line.split(": ");
				if (args.length <= 1)
					continue;
				String token = args[0], value = args[1];
				switch (token.toLowerCase()) {
				case "mysql_enabled":
					GameSettings.MYSQL_ENABLED = Boolean.valueOf(value);
					break;
				case "debug_mode":
					GameSettings.DEBUG_MODE = Boolean.valueOf(value);
					break;
				case "gametype":
					GAME_TYPE = GameType.valueOf(value);
					break;
				case "master_password":
					// GameSettings.MASTER_PASSWORD = value;
					break;
				case "staff_only":
					// GameSettings.STAFF_ONLY = Boolean.valueOf(value);
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}