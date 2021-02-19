package com.zyrox.world.entity.impl.player;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

import com.zyrox.GameSettings;
import com.zyrox.engine.task.Task;
import com.zyrox.engine.task.TaskManager;
import com.zyrox.engine.task.impl.PlayerDeathTask;
import com.zyrox.engine.task.impl.WalkToTask;
import com.zyrox.model.*;
import com.zyrox.model.action.ActionManager;
import com.zyrox.model.container.impl.*;
import com.zyrox.model.container.impl.Bank.BankSearchAttributes;
import com.zyrox.model.definitions.WeaponAnimations;
import com.zyrox.model.definitions.WeaponInterfaces;
import com.zyrox.model.definitions.WeaponInterfaces.WeaponInterface;
import com.zyrox.model.input.Input;
import com.zyrox.model.item.ItemCharge;
import com.zyrox.model.item.Items;
import com.zyrox.model.item.RingOfBosses;
import com.zyrox.net.PlayerSession;
import com.zyrox.net.SessionState;
import com.zyrox.net.packet.Packet;
import com.zyrox.net.packet.PacketSender;
import com.zyrox.saving.PlayerSaving;
import com.zyrox.saving.SavedSingleValue;
import com.zyrox.util.FrameUpdater;
import com.zyrox.util.Misc;
import com.zyrox.util.Stopwatch;
import com.zyrox.world.World;
import com.zyrox.world.content.*;
import com.zyrox.world.content.Achievements.AchievementAttributes;
import com.zyrox.world.content.BankPin.BankPinAttributes;
import com.zyrox.world.content.DropLog.DropLogEntry;
import com.zyrox.world.content.KillsTracker.KillsEntry;
import com.zyrox.world.content.LoyaltyProgramme.LoyaltyTitles;
import com.zyrox.world.content.auction_house.AuctionHouse;
import com.zyrox.world.content.auction_house.AuctionHouseSortType;
import com.zyrox.world.content.auction_house.AuctionHouseViewingState;
import com.zyrox.world.content.auction_house.item.AuctionHouseItem;
import com.zyrox.world.content.clan.ClanChat;
import com.zyrox.world.content.combat.CombatFactory;
import com.zyrox.world.content.combat.CombatType;
import com.zyrox.world.content.combat.effect.CombatPoisonEffect.CombatPoisonData;
import com.zyrox.world.content.combat.effect.CombatVenomEffect.CombatVenomData;
import com.zyrox.world.content.combat.magic.CombatSpell;
import com.zyrox.world.content.combat.prayer.CurseHandler;
import com.zyrox.world.content.combat.prayer.PrayerHandler;
import com.zyrox.world.content.combat.pvp.PlayerKillingAttributes;
import com.zyrox.world.content.combat.range.CombatRangedAmmo.RangedWeaponData;
import com.zyrox.world.content.combat.strategy.CombatStrategies;
import com.zyrox.world.content.combat.strategy.CombatStrategy;
import com.zyrox.world.content.combat.strategy.impl.Nex;
import com.zyrox.world.content.combat.weapon.CombatSpecial;
import com.zyrox.world.content.combat.weapon.FightType;
import com.zyrox.world.content.degrade.DegradingManager;
import com.zyrox.world.content.dialogue.Dialogue;
import com.zyrox.world.content.donator_boss.DonatorBossMonster;
import com.zyrox.world.content.dropchecker.NPCDropTableChecker;
import com.zyrox.world.content.greatolm.RaidsParty;
import com.zyrox.world.content.instance.PlayerInstanceManager;
import com.zyrox.world.content.interfaces.MakeInterface;
import com.zyrox.world.content.interfaces.StartScreen2.GameModes;
import com.zyrox.world.content.minigames.Minigame;
import com.zyrox.world.content.minigames.MinigameAttributes;
import com.zyrox.world.content.minigames.impl.Dueling;
import com.zyrox.world.content.partyroom.PartyRoomManager;
import com.zyrox.world.content.presets.Presets;
import com.zyrox.world.content.raids.Raids;
import com.zyrox.world.content.shop.ShopType;
import com.zyrox.world.content.skill.AbstractHarvestSkill;
import com.zyrox.world.content.skill.AbstractSkill;
import com.zyrox.world.content.skill.SkillManager;
import com.zyrox.world.content.skill.impl.construction.HouseFurniture;
import com.zyrox.world.content.skill.impl.construction.Portal;
import com.zyrox.world.content.skill.impl.construction.Room;
import com.zyrox.world.content.skill.impl.construction.ConstructionData.HouseLocation;
import com.zyrox.world.content.skill.impl.construction.ConstructionData.HouseTheme;
import com.zyrox.world.content.skill.impl.farming.patch.Patch;
import com.zyrox.world.content.skill.impl.farming.patch.PatchType;
import com.zyrox.world.content.skill.impl.runecrafting.RunecraftingPouchContainer;
import com.zyrox.world.content.skill.impl.runecrafting.RunecraftingPouches.RunecraftingPouch;
import com.zyrox.world.content.skill.impl.slayer.Slayer;
import com.zyrox.world.content.skill.impl.summoning.Pouch;
import com.zyrox.world.content.skill.impl.summoning.Summoning;
import com.zyrox.world.content.teleport.Teleport;
import com.zyrox.world.content.teleport.TeleportCategory;
import com.zyrox.world.content.teleportation.TeleportInterface;
import com.zyrox.world.content.tutorial.TutorialStages;
import com.zyrox.world.content.youtube.YouTubeVideo;
import com.zyrox.world.entity.impl.GameCharacter;
import com.zyrox.world.entity.impl.bot.BotPlayer;
import com.zyrox.world.entity.impl.npc.NPC;
import com.zyrox.world.entity.impl.npc.impl.Zulrah;

public class Player extends GameCharacter {
	
	private TeleportInterface teleportInterface = new TeleportInterface(this);
	
	public TeleportInterface getTeleportInterface() {
		return this.teleportInterface;
	}

    private ActionManager actionManager = new ActionManager(this);

    public HashMap<String, YouTubeVideo> videosNotVotedOn = new HashMap<>();

    public Stopwatch enchantTimer = new Stopwatch();

    public Stopwatch newPlayerTimer = new Stopwatch();

    public Stopwatch specTimer = new Stopwatch();

    public Stopwatch lastAlertSent = new Stopwatch();

    public int wallet;

    public Stopwatch sqlCheckTimer = new Stopwatch();

    public long achievedMax = -1;

    public Stopwatch fishBarrelTimer = new Stopwatch();

    public int prestige;

    public DonatorBossMonster viewingMonster = DonatorBossMonster.SCORPIA;

    public ArrayList<DonatorBossMonster> donatorBossMonstersUnlocked = new ArrayList<>();

    public AuctionHouseSortType auctionHouseSortType = AuctionHouseSortType.PRICE;

    public ArrayList<AuctionHouseItem> auctionHouseItemsViewing = new ArrayList();

    public AuctionHouseItem auctionHouseItemToSell = null;

    public AuctionHouse auctionHouse = null;

    public AuctionHouseViewingState auctionHouseViewingState = AuctionHouseViewingState.CLOSED;

    public Stopwatch lastAuctionCollectionMessageSent = new Stopwatch();

    public String lastAuctionItemSearched;

    private boolean quickMovement;

    private Stopwatch boxTimer = new Stopwatch();

    private Raids raids = new Raids(this);

    public ShopType openShopType;

    public int lastBuyX = -1;

    /**
     * The player's current shop id they are viewing.
     */
    private transient Shop shop;

    public Stopwatch ignoreEmptyStopwatch = null;
    public Teleport teleport;
    public TeleportCategory teleportCategory;
public int pkPointsTemp = -1;
    private final PlayerInstanceManager playerInstance = new PlayerInstanceManager();

    public int slayerRing;
    public Position targetLocation;
    public int eliteClueSteps;
    public Nex nexInstance;

    public String yellTitle = "";

    public long dinhSwitchDelay;

    private final NPCDropTableChecker dropChecker = new NPCDropTableChecker();
    private final DegradingManager degrading = new DegradingManager();

    private final PartyRoomManager partyRoom = new PartyRoomManager(this);
    public Zulrah lastZulrah;
    public MagicSpellbook previousSpellbook;

    public PartyRoomManager getPartyRoom() {
        return partyRoom;
    }

    private boolean useTemporaryContainer;

    private Stopwatch zulrahTimer = new Stopwatch();

    public boolean isUseTemporaryContainer() {
        return useTemporaryContainer;
    }

    public void setUseTemporaryContainer(boolean useTemporaryContainer) {
        this.useTemporaryContainer = useTemporaryContainer;
    }

    private boolean placeholders = false;

    private Player spectateTarget;
    private boolean spectatorMode;
    private boolean inHome = false;
    private Position previousTile;

    public boolean isPlaceholders() {
        return placeholders;
    }

    public void setPlaceholders(boolean placeholders) {
        this.placeholders = placeholders;
    }
    
    public String npcToSim = "";
    public int npcAmountToSim = 100;
    
    private int lastLoginYear = 0;
    private int lastLoginMonth = 0;
    private int lastLoginDate = 0;
    private int loginStreak = 0;

    public int getLastLoginYear() {
        return this.lastLoginYear;
    }

    public void setLastLoginYear(int year) {
        this.lastLoginYear = year;
    }

    public int getLastLoginMonth() {
        return this.lastLoginMonth;
    }

    public void setLastLoginMonth(int month) {
        this.lastLoginMonth = month;
    }
    public int getLastLoginDate() {
        return this.lastLoginDate;
    }

    public void setLastLoginDate(int day) {
        this.lastLoginDate = day;
    }

    public int getLoginStreak() {
        return this.loginStreak;
    }

    public void setLoginStreak(int streak) {
        this.loginStreak = streak;
    }
    
    private DailyLogin dailyLogin = new DailyLogin(this);

    public DailyLogin getDailyLogin() {
        return dailyLogin;
   }

    private Presets presets = new Presets(this);

    public Presets getPresets() {
        return presets;
    }

    private DonationPanel donationPanel = new DonationPanel(this);

    public DonationPanel getDonationPanel() {
        return donationPanel;
    }

    private long sessionStart = System.currentTimeMillis();

    public long onlineTime() {
        return System.currentTimeMillis() - sessionStart;
    }

    private int loginId = -1;

    public int getLoginId() {
        return loginId;
    }

    public void setLoginId(int loginId) {
        this.loginId = loginId;
    }

    private Password passwordNew = new Password();

    public Password getPasswordNew() {
        return passwordNew;
    }

    public boolean isSpecial() {
        return GameSettings.SPECIAL_STAFF_NAMES.contains(getName());
    }

    private LinkedList<SavedSingleValue> savedValues = new LinkedList<SavedSingleValue>();

    public LinkedList<SavedSingleValue> getSavedValues() {
        return savedValues;
    }

    public String getName() {
        return getUsername();
    }

    /**
     * Name to lowercase without underscores.
     *
     * @return
     */
    public String getFilteredName() {
        return username.toLowerCase().replaceAll("_", " ");
    }

    private long[] lastVoted = new long[4]; // Runelocus and RSPSList

    public long[] getLastVoted() {
        return lastVoted;
    }

    public void setLastVoted(long time, int idx) {
        lastVoted[idx] = time;
    }

    public void setLastVoted(long time) {
        for (int i = 0; i < lastVoted.length; i++) {
            lastVoted[i] = time;
        }
    }

    public boolean inResourceArea() {
        return getPosition().getX() >= 3196 && getPosition().getX() <= 3184 && getPosition().getY() >= 3924
                && getPosition().getY() <= 3944;
    }

    private int databaseId;

    public int getDatabaseId() {
        return databaseId;
    }

    public void setDatabaseId(int databaseId) {
        this.databaseId = databaseId;
    }

    public int loginLogId = -1;

    private int votespamCount = 0;

    private int requestVoteId = -1;

    private long requestVoteTime = -1;

    private int requestDonationId = -1;

    public int stageAmount;

    private long requestDonationTime = -1;

    public long getRequestDonationTime() {
        return requestDonationTime;
    }

    public void setRequestDonationTime(long time) {
        this.requestDonationTime = time;
    }

    public int getRequestDonationId() {
        return requestDonationId;
    }

    public void setRequestDonationId(int requestDonationId) {
        this.requestDonationId = requestDonationId;
    }

    public long getRequestVoteTime() {
        return requestVoteTime;
    }

    public void setRequestVoteTime(long requestVoteTime) {
        this.requestVoteTime = requestVoteTime;
    }

    public int getRequestVoteId() {
        return requestVoteId;
    }

    public void setRequestVoteId(int requestVoteId) {
        this.requestVoteId = requestVoteId;
    }

    public int getVotespamCount() {
        return votespamCount;
    }

    public void setVotespamCount(int votespamCount) {
        this.votespamCount = votespamCount;
    }

    private boolean accountCompromised;

    private boolean loginReward = false;

    private boolean openPos = false;

    private byte[] cachedUpdateBlock;

    private long dragonScimInjury;

    private int passResetCount;

    private long lastPassReset;

    private boolean hidePlayer = false;

    private int[] maxCapeColors = {65214, 65200, 65186, 62995};

    private int[] compCapeColors = {65214, 65200, 65186, 62995};

    private int currentCape;

    public int[] getMaxCapeColors() {
        return maxCapeColors;
    }

    private int venomImmunity;

    public int getVenomImmunity() {
        return poisonImmunity;
    }

    public void incrementVenomImmunity(int amount) {
        venomImmunity += amount;
    }

    public void decrementVenomImmunity(int amount) {
        venomImmunity -= amount;
    }

    public long npcDropTableDelay = 0;

    public void setMaxCapeColors(int[] maxCapeColors) {
        this.maxCapeColors = maxCapeColors;
    }

    public void setMaxCapeColor(int index, int value) {
        this.maxCapeColors[index] = value;
    }

    private String title = "";

    public String yellmsg = "";

    private boolean active;

    private Kraken kraken = new Kraken();

    private MakeInterface makeInterface;

    public void setMakeInterface(MakeInterface makeInterface) {
        this.makeInterface = makeInterface;
    }

    public MakeInterface getMakeInterface() {
        return makeInterface;
    }

    private boolean completed2019EasterEvent;

    /**
     * Grabs the Kraken boss instance
     *
     * @return
     */
    public Kraken getKraken() {
        return this.kraken;
    }

    /**
     * Resets the Kraken instance
     */
    public void resetKraken() {
        this.getKraken().reset();
        this.kraken = new Kraken();
    }

    public Player(PlayerSession playerIO) {
        super(GameSettings.DEFAULT_POSITION.copy());
        this.session = playerIO;
    }

    private Map<String, Object> attributes = new HashMap<>();

    @SuppressWarnings("unchecked")
    public <T> T getAttribute(String key) {
        return (T) attributes.get(key);
    }

    private Minigame minigame = null;

    @SuppressWarnings("unchecked")
    public <T> T getAttribute(String key, T fail) {
        Object object = attributes.get(key);
        return object == null ? fail : (T) object;
    }

    public boolean hasAttribute(String key) {
        return attributes.containsKey(key);
    }

    public void removeAttribute(String key) {
        attributes.remove(key);
    }

    private int hardwareNumber;

    public int getHardwareNumber() {
        return hardwareNumber;
    }

    public Player setHardwareNumber(int hardwareNumber) {
        this.hardwareNumber = hardwareNumber;
        return this;
    }

    public void setAttribute(String key, Object value) {
        attributes.put(key, value);
    }

    @Override
    public void appendDeath() {
        if (!isDying) {
            isDying = true;
            TaskManager.submit(new PlayerDeathTask(this));
        }
    }

    private int bossPoints;
    private int jailKills;

    public int getPasswordPlayer() {
        return passwordReset;
    }

    public void setPasswordPlayer(int passwordPlayer) {
        this.passwordReset = passwordPlayer;
    }

    public int getBossPoints() {
        return bossPoints;
    }

    public void setBossPoints(int bossPoints) {
        this.bossPoints = bossPoints;
    }

    public void setJailKills(int jailKills) {
        this.jailKills = jailKills;
    }

    public int getJailKills() {
        return jailKills;
    }

    public void setJailAmount(int jailAmount) {
        this.jailAmount = jailAmount;
    }

    public int getJailAmount() {
        return jailAmount;
    }

    public void addBossPoints(int amount) {
        setBossPoints(getBossPoints() + amount);
        sendMessage("<img=0> You now have @red@" + getBossPoints() + " Boss Points!");
    }

    /*
     * Variables for DropTable & Player Profiling
     *
     * @author Levi Patton
     *
     * @www.rune-server.org/members/auguryps
     */
    public Player dropLogPlayer;
    public boolean dropLogOrder;
    private PlayerDropLog playerDropLog = new PlayerDropLog();
    private ProfileViewing profile = new ProfileViewing();

    public PacketSender getPA() {
        return getPacketSender();
    }

    public PlayerDropLog getPlayerDropLog() {
        return playerDropLog;
    }

    public ProfileViewing getProfile() {
        return profile;
    }

    public void setProfile(ProfileViewing profile) {
        this.profile = profile;
    }

    public void setPlayerDropLog(PlayerDropLog playerDropLog) {
        this.playerDropLog = playerDropLog;
    }

    @Override
    public int getConstitution() {
        int hp = getSkillManager().getCurrentLevel(Skill.CONSTITUTION);
        if (this.getDatabaseId() == 1) {
            // this.sendMessage("Your hp is :" + hp);
        }
       // this.sendMessage("Your hp is :" + hp);
        return hp;
    }

    @Override
    public GameCharacter setConstitution(int constitution) {
        if (isDying) {
            return this;
        }
        skillManager.setCurrentLevel(Skill.CONSTITUTION, constitution);
        packetSender.sendSkill(Skill.CONSTITUTION);
        if (getConstitution() <= 0 && !isDying) {
            appendDeath();
        }
        return this;
    }

    @Override
    public void heal(int amount) {
        int level = skillManager.getMaxLevel(Skill.CONSTITUTION);
        if ((skillManager.getCurrentLevel(Skill.CONSTITUTION) + amount) >= level) {
            setConstitution(level);
        } else {
            setConstitution(skillManager.getCurrentLevel(Skill.CONSTITUTION) + amount);
        }
    }

    @Override
    public int getBaseAttack(CombatType type) {
        if (type == CombatType.RANGED) {
            return skillManager.getCurrentLevel(Skill.RANGED);
        } else if (type == CombatType.MAGIC) {
            return skillManager.getCurrentLevel(Skill.MAGIC);
        }
        return skillManager.getCurrentLevel(Skill.ATTACK);
    }

    @Override
    public int getBaseDefence(CombatType type) {
        if (type == CombatType.MAGIC) {
            return skillManager.getCurrentLevel(Skill.MAGIC);
        }
        return skillManager.getCurrentLevel(Skill.DEFENCE);
    }

    @Override
    public int getAttackSpeed() {
        int speed = weapon.getSpeed();
        String weapon = equipment.get(Equipment.WEAPON_SLOT).getDefinition().getName();
        if (getCurrentlyCasting() != null) {
            if (equipment.get(Equipment.WEAPON_SLOT).getId() == 13058 || equipment.get(Equipment.WEAPON_SLOT).getId() == 52516) {
                return 4;
            }
            return 5;
        }
        if (getCurrentlyCasting() != null) {
            if (equipment.get(Equipment.WEAPON_SLOT).getId() == 52323) {
                return 4;
            }
            return 5;
        }
        int weaponId = equipment.get(Equipment.WEAPON_SLOT).getId();
        if (weaponId == 1419) {
            speed -= 2;
        }
        if (weaponId == 21078) {
            speed--;
        }
        if (fightType == FightType.CROSSBOW_RAPID || fightType == FightType.LONGBOW_RAPID
                || fightType == FightType.BLOWPIPE_RAPID || fightType == FightType.DART_RAPID
                || fightType == FightType.SHORTBOW_RAPID || fightType == FightType.KNIFE_RAPID
                || fightType == FightType.THROWNAXE_RAPID || fightType == FightType.JAVELIN_RAPID
                || weapon.contains("rapier")) {
            speed--;
        } else if (weaponId == 11730) {
            speed -= 2;
        }

        if (this.weapon == WeaponInterface.BLOWPIPE && getCombatBuilder() != null
                && getCombatBuilder().getVictim() != null && getCombatBuilder().getVictim().isPlayer()) {
            speed++;
        }
        if (weaponId == Items.SAGITTARIAN_SHORTBOW || weaponId == Items.ZANIKS_CROSSBOW) {
            speed = 1;
        }
        return speed;
    }

    public int clue1Amount;
    public int clue2Amount;
    public int clue3Amount;
    public int clueLevel;
    public Item[] puzzleStoredItems;
    public int sextantGlobalPiece;
    public double sextantBarDegree;
    public int rotationFactor;
    public int sextantLandScapeCoords;
    public int sextantSunCoords;

    // private Channel channel;

    // public Player write(Packet packet) {
    // if (channel.isConnected()) {
    // channel.write(packet);
    // }
    // return this;
    // }

    /// public Channel getChannel() {
    // return channel;
    // }

    private Bank bank = new Bank(this);

    public Bank getBank() {
        return bank;
    }

    @Override
    public boolean isPlayer() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Player)) {
            return false;
        }

        Player p = (Player) o;
        return p.getIndex() == getIndex() || p.getUsername().equals(username);
    }

    @Override
    public int getSize() {
        return 1;
    }

    @Override
    public void poisonVictim(GameCharacter victim, CombatType type) {
        if(victim.isPlayer()) {
            Player victimPlayer = (Player)victim;
            Item victimHelm = victimPlayer.getEquipment().get(Equipment.HEAD_SLOT);
            if(victimHelm.getId() == 12282 || victimHelm.getId() == 12279 || victimHelm.getId() == 12278) {
                return;
            }
        }
        if (type == CombatType.MELEE || weapon == WeaponInterface.DART || weapon == WeaponInterface.KNIFE
                || weapon == WeaponInterface.THROWNAXE || weapon == WeaponInterface.JAVELIN) {
            CombatFactory.poisonEntity(victim, CombatPoisonData.getPoisonType(equipment.get(Equipment.WEAPON_SLOT)));
        } else if (type == CombatType.RANGED) {
            CombatFactory.poisonEntity(victim,
                    CombatPoisonData.getPoisonType(equipment.get(Equipment.AMMUNITION_SLOT)));
        }
    }

    @Override
    public void venomVictim(GameCharacter victim, CombatType type) {
        if(victim.isPlayer()) {
            Player victimPlayer = (Player)victim;
            Item victimHelm = victimPlayer.getEquipment().get(Equipment.HEAD_SLOT);
            if(victimHelm.getId() == 12282 || victimHelm.getId() == 12279 || victimHelm.getId() == 12278) {
                return;
            }
        }
        int weaponId = equipment.get(Equipment.WEAPON_SLOT).getId();
        int helmet = equipment.get(Equipment.HEAD_SLOT).getId();
        if ((type == CombatType.MAGIC && weaponId == 12284)) {
            CombatFactory.venomEntity(victim, CombatVenomData.getVenomType(equipment.get(Equipment.WEAPON_SLOT)));
        }
        if ((type == CombatType.RANGED && weaponId == 12926)) {
            CombatFactory.venomEntity(victim, CombatVenomData.getVenomType(equipment.get(Equipment.WEAPON_SLOT)));
        } else if (helmet == 12282 || helmet == 12279 || helmet == 12278) {
            CombatFactory.venomEntity(victim, CombatVenomData.getVenomType(equipment.get(Equipment.HEAD_SLOT)));
        }
    }

    @Override
    public CombatStrategy determineStrategy() {
        if (specialActivated && castSpell == null) {

            if (combatSpecial.getCombatType() == CombatType.MELEE) {
                return CombatStrategies.getDefaultMeleeStrategy();
            } else if (combatSpecial.getCombatType() == CombatType.RANGED) {
                setRangedWeaponData(RangedWeaponData.getData(this));
                return CombatStrategies.getDefaultRangedStrategy();
            } else if (combatSpecial.getCombatType() == CombatType.MAGIC) {
                return CombatStrategies.getDefaultMagicStrategy();
            }
        }

        if (castSpell != null || autocastSpell != null
                || this.getEquipment().get(Equipment.WEAPON_SLOT).getId() == 52516 //dawnbringer
                || this.getEquipment().get(Equipment.WEAPON_SLOT).getId() == 13058) {
            return CombatStrategies.getDefaultMagicStrategy();
        }

        if (castSpell != null || autocastSpell != null
                || this.getEquipment().get(Equipment.WEAPON_SLOT).getId() == 52323) {
            return CombatStrategies.getDefaultMagicStrategy();
        }

        RangedWeaponData data = RangedWeaponData.getData(this);
        if (data != null) {
            setRangedWeaponData(data);
            return CombatStrategies.getDefaultRangedStrategy();
        }

        return CombatStrategies.getDefaultMeleeStrategy();
    }

    public void passiveRegen() {
        final Skill hp = Skill.CONSTITUTION;
        final int current = getSkillManager().getCurrentLevel(hp);
        final int max = getSkillManager().getMaxLevel(hp);
        boolean faster = getPrayerActive()[PrayerHandler.RAPID_HEAL];
        if (System.currentTimeMillis() - getPassiveRegenCounter() >= (faster ? 15000 : 30000)) {
            setPassiveRegenCounter(System.currentTimeMillis());
            int newCurrent = current + 10;
            if (newCurrent <= max) {
                getSkillManager().setCurrentLevel(hp, newCurrent, true);
            } else {
                getSkillManager().setCurrentLevel(hp, max, true);
            }
        }
    }

    private boolean passiveHeal() {
        if (this == null || !this.isRegistered()) {
            return false;
        }

        if (this.getLocation() == Locations.Location.WILDERNESS) {
            return false;
        }

        if (this.getCombatBuilder().isAttacking()) {
            return false;
        }

        final Skill hp = Skill.CONSTITUTION;
        final int current = getSkillManager().getCurrentLevel(hp);
        final int max = getSkillManager().getMaxLevel(hp);

        if (current < max) {
            return true;
        }
        return false;
    }

    public void process() {
        process.sequence();
        if (passiveHeal()) {
            passiveRegen();
        }

        if (spectateTarget != null) {
            long distance = spectateTarget.getPosition().getDistance(getPosition());
            if (distance > 104 || getLocation() == Locations.Location.WILDERNESS) {
                sendMessage("You have stopped spectating " + spectateTarget.getName() + ".");
                setSpectateTarget(null);
                moveTo(getPreviousTile());
                setPreviousTile(null);
            }
        }

        //checkDupe();

        if(newPlayer && World.ANTI_FLOOD) {
            if(newPlayerTimer.elapsed(60_000)) {
                World.deregister(this);
                PlayerHandler.handleLogout(this);
                if(this.getSession() != null && this.getSession().getChannel() != null)
                    this.getSession().getChannel().close();
                newPlayerTimer.reset();
            }
        }

        PlayerTextUpdate.updatePanel(this);
    }

    public void dispose() {
        packetSender.sendLogout();
    }

    public void save() {
        if (session.getState() != SessionState.LOGGED_IN && session.getState() != SessionState.LOGGING_OUT) {
            return;
        }

        PlayerSaving.getSaving().save(this);
    }

    public boolean logout() {
        if (getCombatBuilder().isBeingAttacked()) {
            getPacketSender().sendMessage("You must wait a few seconds after being out of combat before doing this.");
            return false;
        }
        if (getConstitution() <= 0 || isDying || settingUpCannon || crossingObstacle) {
            getPacketSender().sendMessage("You cannot log out at the moment.");
            return false;
        }
        return true;
    }

    public void restart() {
        setFreezeDelay(0);
        setOverloadPotionTimer(0);
        setPrayerRenewalPotionTimer(0);
        setSpecialPercentage(100);
        setSpecialActivated(false);
        CombatSpecial.updateBar(this);
        setHasVengeance(false);
        setSkullTimer(0);
        setSkullIcon(0);
        setTeleblockTimer(0);
        setPoisonDamage(0);
        setVenomDamage(0);
        setStaffOfLightEffect(0);
        performAnimation(new Animation(65535));
        getPacketSender().sendConstitutionOrbPoison(false);
        getPacketSender().sendConstitutionOrbVenom(false);
        WeaponInterfaces.assign(this, getEquipment().get(Equipment.WEAPON_SLOT));
        WeaponAnimations.assign(this, getEquipment().get(Equipment.WEAPON_SLOT));
        PrayerHandler.deactivateAll(this);
        CurseHandler.deactivateAll(this);
        getEquipment().refreshItems();
        getInventory().refreshItems();
        for (Skill skill : Skill.values()) {
            getSkillManager().setCurrentLevel(skill, getSkillManager().getMaxLevel(skill));
        }
        setRunEnergy(100);
        setDying(false);
        getMovementQueue().setLockMovement(false).reset();
        getUpdateFlag().flag(Flag.APPEARANCE);
    }

    public boolean busy() {
        return interfaceId > 0 || isBanking || trading.inTrade() || dueling.inDuelScreen || isResting;
    }

    /*
     * Fields
     */
    /**
     * * STRINGS **
     */
    private String username;
    private String password;
    private String serial_number;
    private String lastSerialNumber;
    private String jSerial;
    private String super_serial_number;
    private double clientVersion;
    private String emailAddress;
    private String hostAddress;
    private String lastHostAddress;
    private String lastSuperSerial;
    private String clanChatName;

    private HouseLocation houseLocation;

    private HouseTheme houseTheme;

    /**
     * * LONGS *
     */
    private Long longUsername;
    private long moneyInPouch;
    private long totalPlayTime;
    // Timers (Stopwatches)
    private final Stopwatch sqlTimer = new Stopwatch();
    private final Stopwatch protpraydelay = new Stopwatch().headStart(9000);
    private final Stopwatch foodTimer = new Stopwatch();
    private final Stopwatch potionTimer = new Stopwatch();
    private final Stopwatch lastRunRecovery = new Stopwatch();
    private final Stopwatch clickDelay = new Stopwatch();
    private final Stopwatch lastItemPickup = new Stopwatch();
    private final Stopwatch lastYell = new Stopwatch();
    private final Stopwatch lastSql = new Stopwatch();
    private final Stopwatch krakenRespawn = new Stopwatch();

    private final Stopwatch lastVengeance = new Stopwatch();
    private final Stopwatch lastCure = new Stopwatch();
    private final Stopwatch emoteDelay = new Stopwatch();
    private final Stopwatch specialRestoreTimer = new Stopwatch();
    private final Stopwatch lastSummon = new Stopwatch();
    private final Stopwatch recordedLogin = new Stopwatch();
    private final Stopwatch creationDate = new Stopwatch();
    private final Stopwatch tolerance = new Stopwatch();
    private final Stopwatch lougoutTimer = new Stopwatch();

    /**
     * * INSTANCES **
     */
    private final CopyOnWriteArrayList<Teleport> recentTeleports = new CopyOnWriteArrayList<Teleport>();
    private final CopyOnWriteArrayList<Teleport> favoriteTeleports = new CopyOnWriteArrayList<Teleport>();
    private final CopyOnWriteArrayList<KillsEntry> killsTracker = new CopyOnWriteArrayList<KillsEntry>();
    private final CopyOnWriteArrayList<DropLogEntry> dropLog = new CopyOnWriteArrayList<DropLogEntry>();
    private ArrayList<HouseFurniture> houseFurniture = new ArrayList<HouseFurniture>();
    private ArrayList<Portal> housePortals = new ArrayList<>();
    private final List<Player> localPlayers = new LinkedList<Player>();
    private final List<NPC> localNpcs = new LinkedList<NPC>();

    private PlayerSession session;
    private final PlayerProcess process = new PlayerProcess(this);
    private final PlayerKillingAttributes playerKillingAttributes = new PlayerKillingAttributes(this);
    private final MinigameAttributes minigameAttributes = new MinigameAttributes();
    private final BankPinAttributes bankPinAttributes = new BankPinAttributes();
    private final BankSearchAttributes bankSearchAttributes = new BankSearchAttributes();
    private final AchievementAttributes achievementAttributes = new AchievementAttributes();
    private CharacterAnimations characterAnimations = new CharacterAnimations();
    private final BonusManager bonusManager = new BonusManager();
    private final PointsHandler pointsHandler = new PointsHandler(this);

    private final PacketSender packetSender = new PacketSender(this);
    private final Appearance appearance = new Appearance(this);
    private final FrameUpdater frameUpdater = new FrameUpdater();
    private PlayerRights rights = PlayerRights.PLAYER;
    private SkillManager skillManager = new SkillManager(this);
    private PlayerRelations relations = new PlayerRelations(this);
    private ChatMessage chatMessages = new ChatMessage();
    private Inventory inventory = new Inventory(this);
    private Equipment equipment = new Equipment(this);

    private Inventory temporaryInventory = new Inventory(this);
    private Equipment temporaryEquipment = new Equipment(this);
    private PriceChecker priceChecker = new PriceChecker(this);
    private RunePouch runePouch = new RunePouch(this);
    private Trading trading = new Trading(this);
    private Dueling dueling = new Dueling(this);
    private Slayer slayer = new Slayer(this);


    private Summoning summoning = new Summoning(this);
    private Bank[] bankTabs = new Bank[9];
    private Bank[] tempBankTabs = null;

    public Bank[] getTempBankTabs() {
        return tempBankTabs;
    }

    public void setTempBankTabs(Bank[] tempBankTabs) {
        this.tempBankTabs = tempBankTabs;
    }

    private Room[][][] houseRooms = new Room[5][13][13];
    private PlayerInteractingOption playerInteractingOption = PlayerInteractingOption.NONE;
    private GameMode gameMode = GameMode.NORMAL;
    private XPMode xpMode = XPMode.REGULAR;
    private CombatType lastCombatType = CombatType.MELEE;
    private FightType fightType = FightType.UNARMED_PUNCH;
    private Prayerbook prayerbook = Prayerbook.NORMAL;
    private MagicSpellbook spellbook = MagicSpellbook.NORMAL;
    private LoyaltyTitles loyaltyTitle = LoyaltyTitles.NONE;

    /**
     * The item that the player is currently selling to shop,
     * used for the sell x option.
     */
    private transient Item itemToSell;

    private ClanChat currentClanChat;
    private Input inputHandling;
    private WalkToTask walkToTask;
    private GameObject interactingObject;
    private Item interactingItem;
    private Dialogue dialogue;
    private DwarfCannon cannon;
    private CombatSpell autocastSpell, castSpell, previousCastSpell;
    private RangedWeaponData rangedWeaponData;
    private CombatSpecial combatSpecial;
    private WeaponInterface weapon;
    private Item untradeableDropItem;
    private Object[] usableObject;
    private Task currentTask;
    private Position resetPosition;
    private Pouch selectedPouch;
    private BlowpipeLoading blowpipeLoading = new BlowpipeLoading(this);

    /**
     * * INTS **
     */
    public int destination = 0;
    public int lastClickedTab = 0;

    public int prayblock = 0;

    private int[] brawlerCharges = new int[9];
    private int[] forceMovement = new int[7];
    private int[] leechedBonuses = new int[7];
    private double[] ores = new double[2];
    private int[] constructionCoords;
    private int recoilCharges;
    private int forgingCharges;
    private int runEnergy = 100;
    private int currentBankTab;
    private int interfaceId, walkableInterfaceId, multiIcon;
    private int dialogueActionId;
    private int overloadPotionTimer, prayerRenewalPotionTimer;
    private int fireImmunity, fireDamageModifier;
    private int amountDonated;

    private int jailAmount;
    private int wildernessLevel;
    private int fireAmmo;
    private int specialPercentage = 100;
    private int skullIcon = -1, skullTimer;
    private int teleblockTimer;
    private int dragonFireImmunity;
    private int poisonImmunity;
    public static int scrollAmount;
    private int passwordReset;
    private int shadowState;
    private int effigy;
    public int gameType;
    private int dfsCharges;
    private int playerViewingIndex;
    private int staffOfLightEffect;
    private int minutesBonusExp = -1;
    private int selectedGeSlot = -1;
    private int selectedGeItem = -1;
    private int geQuantity;
    private int gePricePerItem;
    private int selectedSkillingItem;
    private int currentBookPage;
    private int storedRuneEssence, storedPureEssence;
    public RunecraftingPouchContainer smallPouch = new RunecraftingPouchContainer(this, RunecraftingPouch.SMALL);
    public RunecraftingPouchContainer mediumPouch = new RunecraftingPouchContainer(this, RunecraftingPouch.MEDIUM_POUCH);
    public RunecraftingPouchContainer largePouch = new RunecraftingPouchContainer(this, RunecraftingPouch.LARGE_POUCH);

    private int trapsLaid;
    private int skillAnimation;
    private int houseServant;
    private int houseServantCharges;
    private int servantItemFetch;
    private int portalSelected;
    private int constructionInterface;
    private int buildFurnitureId;
    private int buildFurnitureX;
    private int buildFurnitureY;
    private int combatRingType;

    /**
     * * BOOLEANS **
     */
    private boolean unlockedLoyaltyTitles[] = new boolean[23];
    private boolean[] crossedObstacles = new boolean[7];
    private boolean processFarming;
    private boolean crossingObstacle;
    private boolean targeted;
    private boolean isBanking, noteWithdrawal, swapMode;
    private boolean regionChange, allowRegionChangePacket;
    private boolean isDying;
    private boolean isRunning = true, isResting;
    private boolean experienceLocked;
    private boolean clientExitTaskActive;
    private boolean drainingPrayer;
    private boolean settingUpCannon;
    private boolean hasVengeance;
    private boolean killsTrackerOpen;
    private boolean acceptingAid;
    private boolean autoRetaliate;
    private boolean autocast;
    private boolean specialActivated;
    public boolean playerInstanced;
    private boolean isCoughing;
    private boolean playerLocked;
    private boolean recoveringSpecialAttack;
    private boolean soundsActive, musicActive;
    private boolean newPlayer;
    private boolean passPlayer;
    private boolean openBank;
    private boolean inActive;
    public int timeOnline;
    private boolean inConstructionDungeon;
    private boolean isBuildingMode;
    private boolean voteMessageSent;
    private boolean receivedStarter;
    private boolean isInAgility;

    private long passiveRegenCounter;

    /*
     * Getters & Setters
     */

    public int bossPetExchangeId = 0;

    public long getPassiveRegenCounter() {
        return passiveRegenCounter;
    }

    public void setPassiveRegenCounter(long passiveRegenCounter) {
        this.passiveRegenCounter = passiveRegenCounter;
    }

    public PlayerSession getSession() {
        return session;
    }

    public Inventory getInventory() {
        if (isUseTemporaryContainer()) {
            return temporaryInventory;
        }
        return inventory;
    }

    public Equipment getEquipment() {
        if (isUseTemporaryContainer()) {
            return temporaryEquipment;
        }
        return equipment;
    }

    public PriceChecker getPriceChecker() {
        return priceChecker;
    }

    public RunePouch getRunePouch() {
        return runePouch;
    }

    /*
     * Getters and setters
     */
    public String getUsername() {
        return username;
    }
    private String lowercaseUsername;
    public String getLowercaseUsername() {
        return lowercaseUsername;
    }

    public Player setUsername(String username) {
        this.username = username;
        lowercaseUsername = username.toLowerCase();
        return this;
    }

    public Long getLongUsername() {
        return longUsername;
    }

    public long getNameAsLong() {
        return getLongUsername();
    }

    public Player setLongUsername(Long longUsername) {
        this.longUsername = longUsername;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public String getEmailAddress() {
        return this.emailAddress;
    }

    public void setEmailAddress(String address) {
        this.emailAddress = address;
    }

    public Player setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getHostAddress() {
        return hostAddress;
    }

    public Player setHostAddress(String hostAddress) {
        this.hostAddress = hostAddress;
        return this;
    }

    public String getLastHostAddress() {
        return lastHostAddress;
    }

    public Player setLastHostAddress(String lastHostAddress) {
        this.lastHostAddress = lastHostAddress;
        return this;
    }

    public String getSerialNumber() {
        return serial_number;
    }

    public String getSuperSerialNumber() {
        return super_serial_number;
    }

    public Player setSerialNumber(String serial_number) {
        this.serial_number = serial_number;
        return this;
    }

    public Player setSuperSerialNumber(String super_serial_number) {
        this.super_serial_number = super_serial_number;
        return this;
    }

    public FrameUpdater getFrameUpdater() {
        return this.frameUpdater;
    }

    public PlayerRights getRights() {
        return rights;
    }

    public Player setRights(PlayerRights rights) {
        this.rights = rights;
        return this;
    }

    public boolean isStaff() {
        if (getRights() == PlayerRights.SUPPORT || getRights() == PlayerRights.MODERATOR || getRights() == PlayerRights.GLOBAL_MOD || getRights() == PlayerRights.ADMINISTRATOR ||  getRights() == PlayerRights.OWNER || getRights() == PlayerRights.DEVELOPER) {
            return true;
        }
        return false;
    }

    public boolean isHigherStaff() {
        if (GameSettings.HIGHER_STAFF_NAMES.contains(getName()))
            return true;
        if (GameSettings.SPECIAL_STAFF_NAMES.contains(getName()))
            return true;
        return false;
    }

    public boolean isSpecialStaff() {
        if (GameSettings.SPECIAL_STAFF_NAMES.contains(getName()))
            return true;
        return false;
    }

    public ChatMessage getChatMessages() {
        return chatMessages;
    }

    public PacketSender getPacketSender() {
        return packetSender;
    }

    public SkillManager getSkillManager() {
        return skillManager;
    }

    public Appearance getAppearance() {
        return appearance;
    }

    public PlayerRelations getRelations() {
        return relations;
    }

    public PlayerKillingAttributes getPlayerKillingAttributes() {
        return playerKillingAttributes;
    }

    public PointsHandler getPointsHandler() {
        return pointsHandler;
    }

    public boolean isImmuneToDragonFire() {
        return dragonFireImmunity > 0;
    }

    public int getDragonFireImmunity() {
        return dragonFireImmunity;
    }

    public void setDragonFireImmunity(int dragonFireImmunity) {
        this.dragonFireImmunity = dragonFireImmunity;
    }

    public void incrementDragonFireImmunity(int amount) {
        dragonFireImmunity += amount;
    }

    public void decrementDragonFireImmunity(int amount) {
        dragonFireImmunity -= amount;
    }

    public int getPoisonImmunity() {
        return poisonImmunity;
    }

    public void setPoisonImmunity(int poisonImmunity) {
        this.poisonImmunity = poisonImmunity;
    }

    public int getScrollAmount() {
        return scrollAmount;
    }

    public void setScrollAmount(int scrollAmount) {
        this.scrollAmount = scrollAmount;
    }

    public void setVenomImmunity(int venomImmunity) {
        this.venomImmunity = venomImmunity;
    }

    public void incrementPoisonImmunity(int amount) {
        poisonImmunity += amount;
    }

    public void decrementPoisonImmunity(int amount) {
        poisonImmunity -= amount;
    }

    public boolean isAutoRetaliate() {
        return autoRetaliate;
    }

    public void setAutoRetaliate(boolean autoRetaliate) {
        this.autoRetaliate = autoRetaliate;
    }

    /**
     * @return the castSpell
     */
    public CombatSpell getCastSpell() {
        return castSpell;
    }

    /**
     * @param castSpell the castSpell to set
     */
    public void setCastSpell(CombatSpell castSpell) {
        this.castSpell = castSpell;
    }

    public CombatSpell getPreviousCastSpell() {
        return previousCastSpell;
    }

    public void setPreviousCastSpell(CombatSpell previousCastSpell) {
        this.previousCastSpell = previousCastSpell;
    }

    /**
     * @return the autocast
     */
    public boolean isAutocast() {
        return autocast;
    }

    /**
     * @param autocast the autocast to set
     */
    public void setAutocast(boolean autocast) {
        this.autocast = autocast;
    }

    /**
     * @return the skullTimer
     */
    public int getSkullTimer() {
        return skullTimer;
    }

    /**
     * @param skullTimer the skullTimer to set
     */
    public void setSkullTimer(int skullTimer) {
        this.skullTimer = skullTimer;
    }

    public void decrementSkullTimer() {
        skullTimer -= 50;
    }

    /**
     * @return the skullIcon
     */
    public int getSkullIcon() {
        return skullIcon;
    }

    /**
     * @param skullIcon the skullIcon to set
     */
    public void setSkullIcon(int skullIcon) {
        this.skullIcon = skullIcon;
    }

    /**
     * @return the teleblockTimer
     */
    public int getTeleblockTimer() {
        return teleblockTimer;
    }

    private final CopyOnWriteArrayList<NPC> npc_faces_updated = new CopyOnWriteArrayList<NPC>();

    public CopyOnWriteArrayList<NPC> getNpcFacesUpdated() {
        return npc_faces_updated;
    }

    /**
     * @param teleblockTimer the teleblockTimer to set
     */
    public void setTeleblockTimer(int teleblockTimer) {
        this.teleblockTimer = teleblockTimer;
    }

    public void decrementTeleblockTimer() {
        teleblockTimer--;
    }

    /**
     * @return the autocastSpell
     */
    public CombatSpell getAutocastSpell() {
        return autocastSpell;
    }

    /**
     * @param autocastSpell the autocastSpell to set
     */
    public void setAutocastSpell(CombatSpell autocastSpell) {
        this.autocastSpell = autocastSpell;
    }

    /**
     * @return the specialPercentage
     */
    public int getSpecialPercentage() {
        return specialPercentage;
    }

    /**
     * @param specialPercentage the specialPercentage to set
     */
    public void setSpecialPercentage(int specialPercentage) {
        this.specialPercentage = specialPercentage;
    }

    /**
     * @return the fireAmmo
     */
    public int getFireAmmo() {
        return fireAmmo;
    }

    /**
     * @param fireAmmo the fireAmmo to set
     */
    public void setFireAmmo(int fireAmmo) {
        this.fireAmmo = fireAmmo;
    }

    public int getWildernessLevel() {
        return wildernessLevel;
    }

    public void setWildernessLevel(int wildernessLevel) {
        this.wildernessLevel = wildernessLevel;
    }

    /**
     * @return the combatSpecial
     */
    public CombatSpecial getCombatSpecial() {
        return combatSpecial;
    }

    /**
     * @param combatSpecial the combatSpecial to set
     */
    public void setCombatSpecial(CombatSpecial combatSpecial) {
        this.combatSpecial = combatSpecial;
    }

    /**
     * @return the specialActivated
     */
    public boolean isSpecialActivated() {
        return specialActivated;
    }

    /**
     * @param specialActivated the specialActivated to set
     */
    public void setSpecialActivated(boolean specialActivated) {
        this.specialActivated = specialActivated;
    }

    public void decrementSpecialPercentage(int drainAmount) {
        this.specialPercentage -= drainAmount;

        if (specialPercentage < 0) {
            specialPercentage = 0;
        }
    }

    public void incrementSpecialPercentage(int gainAmount) {
        this.specialPercentage += gainAmount;

        if (specialPercentage > 100) {
            specialPercentage = 100;
        }
    }

    /**
     * @return the rangedAmmo
     */
    public RangedWeaponData getRangedWeaponData() {
        return rangedWeaponData;
    }

    /**
     * @param rangedAmmo the rangedAmmo to set
     */
    public void setRangedWeaponData(RangedWeaponData rangedWeaponData) {
        this.rangedWeaponData = rangedWeaponData;
    }

    /**
     * @return the weapon.
     */
    public WeaponInterface getWeapon() {
        return weapon;
    }

    public ArrayList<Integer> walkableInterfaceList = new ArrayList<>();
    public long lastHelpRequest;
    public long lastAuthClaimed;
    public GameModes selectedGameMode = GameModes.NORMAL;
    public XPMode selectedXPMode = XPMode.REGULAR;
    private boolean areCloudsSpawned;

    public void resetInterfaces() {
        walkableInterfaceList.stream().filter((i) -> !(i == 41005 || i == 41000)).forEach((i) -> {
            getPacketSender().sendWalkableInterface(i, false);
        });

        walkableInterfaceList.clear();
    }

    public void sendParallellInterfaceVisibility(int interfaceId, boolean visible) {
        if (this != null && this.getPacketSender() != null) {
            if (visible) {
                if (walkableInterfaceList.contains(interfaceId)) {
                    return;
                } else {
                    walkableInterfaceList.add(interfaceId);
                }
            } else {
                if (!walkableInterfaceList.contains(interfaceId)) {
                    return;
                } else {
                    walkableInterfaceList.remove((Object) interfaceId);
                }
            }

            getPacketSender().sendWalkableInterface(interfaceId, visible);
        }
    }

    /**
     * @param weapon the weapon to set.
     */
    public void setWeapon(WeaponInterface weapon) {
        this.weapon = weapon;
    }

    /**
     * @return the fightType
     */
    public FightType getFightType() {
        return fightType;
    }

    /**
     * @param fightType the fightType to set
     */
    public void setFightType(FightType fightType) {
        this.fightType = fightType;
    }

    public Bank[] getBanks() {
        return tempBankTabs == null ? bankTabs : tempBankTabs;
    }

    public Bank getBank(int index) {
        return tempBankTabs == null ? bankTabs[index] : tempBankTabs[index];
    }

    public Player setBank(int index, Bank bank) {
        if (tempBankTabs == null) {
            this.bankTabs[index] = bank;
        } else {
            this.tempBankTabs[index] = bank;
        }
        return this;
    }

    public boolean isAcceptAid() {
        return acceptingAid;
    }

    public void setAcceptAid(boolean acceptingAid) {
        this.acceptingAid = acceptingAid;
    }

    public Trading getTrading() {
        return trading;
    }

    public Dueling getDueling() {
        return dueling;
    }

    public CopyOnWriteArrayList<KillsEntry> getKillsTracker() {
        return killsTracker;
    }

    public CopyOnWriteArrayList<DropLogEntry> getDropLog() {
        return dropLog;
    }

    public void setWalkToTask(WalkToTask walkToTask) {
        this.walkToTask = walkToTask;
    }

    public WalkToTask getWalkToTask() {
        return walkToTask;
    }

    public Player setSpellbook(MagicSpellbook spellbook) {
        this.spellbook = spellbook;
        return this;
    }

    public MagicSpellbook getSpellbook() {
        return spellbook;
    }

    public Player setPrayerbook(Prayerbook prayerbook) {
        this.prayerbook = prayerbook;
        return this;
    }

    public Prayerbook getPrayerbook() {
        return prayerbook;
    }

    /**
     * The player's local players list.
     */
    public List<Player> getLocalPlayers() {
        return localPlayers;
    }

    /**
     * The player's local npcs list getter
     */
    public List<NPC> getLocalNpcs() {
        return localNpcs;
    }

    public Player setInterfaceId(int interfaceId) {
        this.interfaceId = interfaceId;
        return this;
    }

    public int getInterfaceId() {
        return this.interfaceId;
    }

    public boolean isDying() {
        return isDying;
    }

    public void setDying(boolean isDying) {
        this.isDying = isDying;
    }

    public int[] getForceMovement() {
        return forceMovement;
    }

    public Player setForceMovement(int[] forceMovement) {
        this.forceMovement = forceMovement;
        return this;
    }

    /**
     * @return the equipmentAnimation
     */
    public CharacterAnimations getCharacterAnimations() {
        return characterAnimations;
    }

    /**
     * @return the equipmentAnimation
     */
    public void setCharacterAnimations(CharacterAnimations equipmentAnimation) {
        this.characterAnimations = equipmentAnimation.clone();
    }

    public LoyaltyTitles getLoyaltyTitle() {
        return loyaltyTitle;
    }

    public void setLoyaltyTitle(LoyaltyTitles loyaltyTitle) {
        this.loyaltyTitle = loyaltyTitle;
    }

    public void setWalkableInterfaceId(int interfaceId2) {
        this.walkableInterfaceId = interfaceId2;
    }

    public PlayerInteractingOption getPlayerInteractingOption() {
        return playerInteractingOption;
    }

    public Player setPlayerInteractingOption(PlayerInteractingOption playerInteractingOption) {
        this.playerInteractingOption = playerInteractingOption;
        return this;
    }

    public int getMultiIcon() {
        return multiIcon;
    }

    public Player setMultiIcon(int multiIcon) {
        this.multiIcon = multiIcon;
        return this;
    }

    public int getWalkableInterfaceId() {
        return walkableInterfaceId;
    }

    public boolean soundsActive() {
        return soundsActive;
    }

    public void setSoundsActive(boolean soundsActive) {
        this.soundsActive = soundsActive;
    }

    public boolean musicActive() {
        return musicActive;
    }

    public void setMusicActive(boolean musicActive) {
        this.musicActive = musicActive;
    }

    public BonusManager getBonusManager() {
        return bonusManager;
    }

    public int getRunEnergy() {
        return runEnergy;
    }

    public Player setRunEnergy(int runEnergy) {
        this.runEnergy = runEnergy;
        return this;
    }

    public Stopwatch getLastRunRecovery() {
        return lastRunRecovery;
    }

    public Player setRunning(boolean isRunning) {
        this.isRunning = isRunning;
        return this;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public Player setResting(boolean isResting) {
        this.isResting = isResting;
        return this;
    }

    public boolean isResting() {
        return isResting;
    }

    public void setMoneyInPouch(long moneyInPouch) {
        this.moneyInPouch = moneyInPouch;
    }

    public long getMoneyInPouch() {
        return moneyInPouch;
    }

    public int getMoneyInPouchAsInt() {
        return moneyInPouch > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) moneyInPouch;
    }

    public void removeFromPouch(long amount) {
        setMoneyInPouch(getMoneyInPouch() - amount);
        getPacketSender().sendString(8135, "" + getMoneyInPouch());
    }

    public void addToPouch(long amount) {
        setMoneyInPouch(getMoneyInPouch() + amount);
        getPacketSender().sendString(8135, "" + getMoneyInPouch());
    }

    public boolean coinPayment(int amount) {
        int coins = getInventory().getAmount(995);

        long pouch = getMoneyInPouch();

        if (pouch < 0) {
            getPacketSender().sendMessage(
                    "Your money pouch contains an invalid amount of coins. Please report this to a staff member.");
            pouch = 0;
        }

        long available = pouch + coins;

        if (available <= 0) {
            sendMessage("You need " + Misc.formatNumber(amount) + " coins to pay for that.");
            return false;
        }

        if (amount <= coins) {
            getInventory().delete(995, amount);
        } else {
            getInventory().delete(995, coins);
            setMoneyInPouch(getMoneyInPouch() - (amount - coins));
            getPacketSender().sendString(8135, "" + getMoneyInPouch() + "");
        }

        return true;
    }

    public boolean experienceLocked() {
        return experienceLocked;
    }

    public void setExperienceLocked(boolean experienceLocked) {
        this.experienceLocked = experienceLocked;
    }

    public void setClientExitTaskActive(boolean clientExitTaskActive) {
        this.clientExitTaskActive = clientExitTaskActive;
    }

    public boolean isClientExitTaskActive() {
        return clientExitTaskActive;
    }

    public Player setCurrentClanChat(ClanChat clanChat) {
        this.currentClanChat = clanChat;
        return this;
    }

    public ClanChat getCurrentClanChat() {
        return currentClanChat;
    }

    public String getClanChatName() {
        return clanChatName;
    }

    public Player setClanChatName(String clanChatName) {
        this.clanChatName = clanChatName;
        return this;
    }

    public void setInputHandling(Input inputHandling) {
        this.inputHandling = inputHandling;
    }

    public Input getInputHandling() {
        return inputHandling;
    }

    public boolean isDrainingPrayer() {
        return drainingPrayer;
    }

    public void setDrainingPrayer(boolean drainingPrayer) {
        this.drainingPrayer = drainingPrayer;
    }

    public Stopwatch getClickDelay() {
        return clickDelay;
    }

    public int[] getLeechedBonuses() {
        return leechedBonuses;
    }

    public Stopwatch getLastItemPickup() {
        return lastItemPickup;
    }

    public Stopwatch getLastSummon() {
        return lastSummon;
    }

    public BankSearchAttributes getBankSearchingAttribtues() {
        return bankSearchAttributes;
    }

    public AchievementAttributes getAchievementAttributes() {
        return achievementAttributes;
    }

    public BankPinAttributes getBankPinAttributes() {
        return bankPinAttributes;
    }

    public int getCurrentBankTab() {
        return currentBankTab;
    }

    public Player setCurrentBankTab(int tab) {
        this.currentBankTab = tab;
        return this;
    }

    public boolean isBanking() {
        return isBanking;
    }

    public Player setBanking(boolean isBanking) {
        this.isBanking = isBanking;
        return this;
    }

    public void setNoteWithdrawal(boolean noteWithdrawal) {
        this.noteWithdrawal = noteWithdrawal;
    }

    public boolean withdrawAsNote() {
        return noteWithdrawal;
    }

    public void setSwapMode(boolean swapMode) {
        this.swapMode = swapMode;
    }

    public boolean swapMode() {
        return swapMode;
    }

    public GameObject getInteractingObject() {
        return interactingObject;
    }

    public Player setInteractingObject(GameObject interactingObject) {
        this.interactingObject = interactingObject;
        return this;
    }

    public Item getInteractingItem() {
        return interactingItem;
    }

    public void setInteractingItem(Item interactingItem) {
        this.interactingItem = interactingItem;
    }

    public Dialogue getDialogue() {
        return this.dialogue;
    }

    public void setDialogue(Dialogue dialogue) {
        this.dialogue = dialogue;
    }

    public int getDialogueActionId() {
        return dialogueActionId;
    }

    public void setDialogueActionId(int dialogueActionId) {
        this.dialogueActionId = dialogueActionId;
    }

    public void setSettingUpCannon(boolean settingUpCannon) {
        this.settingUpCannon = settingUpCannon;
    }

    public boolean isSettingUpCannon() {
        return settingUpCannon;
    }

    public Player setCannon(DwarfCannon cannon) {
        this.cannon = cannon;
        return this;
    }

    public DwarfCannon getCannon() {
        return cannon;
    }

    public int getOverloadPotionTimer() {
        return overloadPotionTimer;
    }

    public void setOverloadPotionTimer(int overloadPotionTimer) {
        this.overloadPotionTimer = overloadPotionTimer;
    }

    public int getPrayerRenewalPotionTimer() {
        return prayerRenewalPotionTimer;
    }

    public void setPrayerRenewalPotionTimer(int prayerRenewalPotionTimer) {
        this.prayerRenewalPotionTimer = prayerRenewalPotionTimer;
    }

    public Stopwatch getSpecialRestoreTimer() {
        return specialRestoreTimer;
    }

    public boolean[] getUnlockedLoyaltyTitles() {
        return unlockedLoyaltyTitles;
    }

    public void setUnlockedLoyaltyTitles(boolean[] unlockedLoyaltyTitles) {
        this.unlockedLoyaltyTitles = unlockedLoyaltyTitles;
    }

    public void setUnlockedLoyaltyTitle(int index) {
        unlockedLoyaltyTitles[index] = true;
    }

    public Stopwatch getEmoteDelay() {
        return emoteDelay;
    }

    public MinigameAttributes getMinigameAttributes() {
        return minigameAttributes;
    }

    public Minigame getMinigame() {
        return minigame;
    }

    public void setMinigame(Minigame minigame) {
        this.minigame = minigame;
    }

    public int getFireImmunity() {
        return fireImmunity;
    }

    public Player setFireImmunity(int fireImmunity) {
        this.fireImmunity = fireImmunity;
        return this;
    }

    public int getFireDamageModifier() {
        return fireDamageModifier;
    }

    public Player setFireDamageModifier(int fireDamageModifier) {
        this.fireDamageModifier = fireDamageModifier;
        return this;
    }

    public boolean hasVengeance() {
        return hasVengeance;
    }

    public void setHasVengeance(boolean hasVengeance) {
        this.hasVengeance = hasVengeance;
    }

    public Stopwatch getLastVengeance() {
        return lastVengeance;
    }
    
    public Stopwatch getLastCure() {
        return lastCure;
    }

    public void setHouseRooms(Room[][][] houseRooms) {
        this.houseRooms = houseRooms;
    }

    public void setHousePortals(ArrayList<Portal> housePortals) {
        this.housePortals = housePortals;
    }

    /*
     * Construction instancing Aj
     */
    public boolean isVisible() {
        if (getLocation() == Locations.Location.CONSTRUCTION) {
            return false;
        }
        return true;
    }

    public void setHouseFurtinture(ArrayList<HouseFurniture> houseFurniture) {
        this.houseFurniture = houseFurniture;
    }

    public Stopwatch getTolerance() {
        return tolerance;
    }

    public boolean isTargeted() {
        return targeted;
    }

    public void setTargeted(boolean targeted) {
        this.targeted = targeted;
    }

    public Stopwatch getLastYell() {
        return lastYell;
    }

    public Stopwatch getLastSql() {
        return lastSql;
    }

    private int cleaned = -1;

    public int getCleaned() {
        return cleaned;
    }

    public void setCleaned(int value) {
        cleaned = value;
    }

    public int getAmountDonated() {
        return amountDonated;
    }

    public void incrementAmountDonated(int amountDonated) {
        this.amountDonated += amountDonated;
    }

    public void setAmountDonated(int amountDonated) {
        this.amountDonated = amountDonated;
    }

    public long getTotalPlayTime() {
        return totalPlayTime;
    }

    public void setTotalPlayTime(long amount) {
        this.totalPlayTime = amount;
    }

    public Stopwatch getRecordedLogin() {
        return recordedLogin;
    }

    public Player setRegionChange(boolean regionChange) {
        this.regionChange = regionChange;
        return this;
    }

    public boolean isChangingRegion() {
        return this.regionChange;
    }

    public void setAllowRegionChangePacket(boolean allowRegionChangePacket) {
        this.allowRegionChangePacket = allowRegionChangePacket;
    }

    public boolean isAllowRegionChangePacket() {
        return allowRegionChangePacket;
    }

    public boolean isKillsTrackerOpen() {
        return killsTrackerOpen;
    }

    public void setKillsTrackerOpen(boolean killsTrackerOpen) {
        this.killsTrackerOpen = killsTrackerOpen;
    }

    public boolean isCoughing() {
        return isCoughing;
    }

    public void setCoughing(boolean isCoughing) {
        this.isCoughing = isCoughing;
    }

    public int getShadowState() {
        return shadowState;
    }

    public void setShadowState(int shadow) {
        this.shadowState = shadow;
    }

    public GameMode getGameMode() {
        return gameMode;
    }

    public void setGameMode(GameMode gameMode) {
        this.gameMode = gameMode;
    }

    public XPMode getXPMode() {
        return xpMode;
    }

    public void setXPMode(XPMode xpMode) {
        this.xpMode = xpMode;
    }

    public boolean isPlayerLocked() {
        return playerLocked;
    }

    public Player setPlayerLocked(boolean playerLocked) {
        this.playerLocked = playerLocked;
        return this;
    }

    /*
     * Handles setting and checking of boss instancing
     */
    public boolean isPlayerInstanced() {
        return playerInstanced;
    }

    public Player setPlayerInstanced(boolean playerInstanced) {
        this.playerInstanced = playerInstanced;
        return this;
    }

    public Stopwatch getSqlTimer() {
        return sqlTimer;
    }

    public Stopwatch getProtPrayDelay() {
        return protpraydelay;
    }

    public Stopwatch getFoodTimer() {
        return foodTimer;
    }

    public Stopwatch getPotionTimer() {
        return potionTimer;
    }

    public Item getUntradeableDropItem() {
        return untradeableDropItem;
    }

    public void setUntradeableDropItem(Item untradeableDropItem) {
        this.untradeableDropItem = untradeableDropItem;
    }

    public boolean isRecoveringSpecialAttack() {
        return recoveringSpecialAttack;
    }

    public void setRecoveringSpecialAttack(boolean recoveringSpecialAttack) {
        this.recoveringSpecialAttack = recoveringSpecialAttack;
    }

    public CombatType getLastCombatType() {
        return lastCombatType;
    }

    public void setLastCombatType(CombatType lastCombatType) {
        this.lastCombatType = lastCombatType;
    }

    public int getEffigy() {
        return this.effigy;
    }

    public void setEffigy(int effigy) {
        this.effigy = effigy;
    }

    public int getDfsCharges() {
        return dfsCharges;
    }

    public void incrementDfsCharges(int amount) {
        this.dfsCharges += amount;
    }

    public void setNewPlayer(boolean newPlayer) {
        this.newPlayer = newPlayer;
    }

    public boolean newPlayer() {
        return newPlayer;
    }

    public void setPassPlayer(boolean passPlayer) {
        this.passPlayer = passPlayer;
    }

    public boolean passPlayer() {
        return passPlayer;
    }

    public Stopwatch getLogoutTimer() {
        return lougoutTimer;
    }

    public Player setUsableObject(Object[] usableObject) {
        this.usableObject = usableObject;
        return this;
    }

    public Player setUsableObject(int index, Object usableObject) {
        this.usableObject[index] = usableObject;
        return this;
    }

    public Object[] getUsableObject() {
        return usableObject;
    }

    public int getPlayerViewingIndex() {
        return playerViewingIndex;
    }

    public void setPlayerViewingIndex(int playerViewingIndex) {
        this.playerViewingIndex = playerViewingIndex;
    }

    public boolean hasStaffOfLightEffect() {
        return staffOfLightEffect > 0;
    }

    public int getStaffOfLightEffect() {
        return staffOfLightEffect;
    }

    public void setStaffOfLightEffect(int staffOfLightEffect) {
        this.staffOfLightEffect = staffOfLightEffect;
    }

    public void decrementStaffOfLightEffect() {
        this.staffOfLightEffect--;
    }

    public boolean openBank() {
        return openBank;
    }

    public void setOpenBank(boolean openBank) {
        this.openBank = openBank;
    }

    public int getMinutesBonusExp() {
        return minutesBonusExp;
    }

    public void setMinutesBonusExp(int minutesBonusExp, boolean add) {
        this.minutesBonusExp = (add ? this.minutesBonusExp + minutesBonusExp : minutesBonusExp);
    }

    public void setInactive(boolean inActive) {
        this.inActive = inActive;
    }

    public boolean isInActive() {
        return inActive;
    }

    public int getSelectedGeItem() {
        return selectedGeItem;
    }

    public void setSelectedGeItem(int selectedGeItem) {
        this.selectedGeItem = selectedGeItem;
    }

    public int getGeQuantity() {
        return geQuantity;
    }

    public void setGeQuantity(int geQuantity) {
        this.geQuantity = geQuantity;
    }

    public int getGePricePerItem() {
        return gePricePerItem;
    }

    public void setGePricePerItem(int gePricePerItem) {
        this.gePricePerItem = gePricePerItem;
    }

    public void setSelectedGeSlot(int slot) {
        this.selectedGeSlot = slot;
    }

    public int getSelectedGeSlot() {
        return selectedGeSlot;
    }

    public Task getCurrentTask() {
        return currentTask;
    }

    public void setCurrentTask(Task currentTask) {
        this.currentTask = currentTask;
    }

    public int getSelectedSkillingItem() {
        return selectedSkillingItem;
    }

    public void setSelectedSkillingItem(int selectedItem) {
        this.selectedSkillingItem = selectedItem;
    }

    public boolean shouldProcessFarming() {
        return processFarming;
    }

    public void setProcessFarming(boolean processFarming) {
        this.processFarming = processFarming;
    }

    public Pouch getSelectedPouch() {
        return selectedPouch;
    }

    public void setSelectedPouch(Pouch selectedPouch) {
        this.selectedPouch = selectedPouch;
    }

    public int getCurrentBookPage() {
        return currentBookPage;
    }

    public void setCurrentBookPage(int currentBookPage) {
        this.currentBookPage = currentBookPage;
    }

    public int getStoredRuneEssence() {
        return storedRuneEssence;
    }

    public void setStoredRuneEssence(int storedRuneEssence) {
        this.storedRuneEssence = storedRuneEssence;
    }

    public int getStoredPureEssence() {
        return storedPureEssence;
    }

    public void setStoredPureEssence(int storedPureEssence) {
        this.storedPureEssence = storedPureEssence;
    }

    public int getTrapsLaid() {
        return trapsLaid;
    }

    public void setTrapsLaid(int trapsLaid) {
        this.trapsLaid = trapsLaid;
    }

    public boolean isCrossingObstacle() {
        return crossingObstacle;
    }

    public Player setCrossingObstacle(boolean crossingObstacle) {
        this.crossingObstacle = crossingObstacle;
        return this;
    }

    public boolean[] getCrossedObstacles() {
        return crossedObstacles;
    }

    public boolean getCrossedObstacle(int i) {
        return crossedObstacles[i];
    }

    public Player setCrossedObstacle(int i, boolean completed) {
        crossedObstacles[i] = completed;
        return this;
    }

    public void setCrossedObstacles(boolean[] crossedObstacles) {
        this.crossedObstacles = crossedObstacles;
    }

    public int getSkillAnimation() {
        return skillAnimation;
    }

    public Player setSkillAnimation(int animation) {
        this.skillAnimation = animation;
        return this;
    }

    public double[] getOres() {
        return ores;
    }

    public void setOres(double[] ores) {
        this.ores = ores;
    }

    public void setResetPosition(Position resetPosition) {
        this.resetPosition = resetPosition;
    }

    public Position getResetPosition() {
        return resetPosition;
    }

    public Slayer getSlayer() {
        return slayer;
    }

    public Summoning getSummoning() {
        return summoning;
    }

    public boolean inConstructionDungeon() {
        return inConstructionDungeon;
    }

    public void setInConstructionDungeon(boolean inConstructionDungeon) {
        this.inConstructionDungeon = inConstructionDungeon;
    }

    public int getHouseServant() {
        return houseServant;
    }

    public HouseLocation getHouseLocation() {
        return houseLocation;
    }

    public HouseTheme getHouseTheme() {
        return houseTheme;
    }

    public void setHouseTheme(HouseTheme houseTheme) {
        this.houseTheme = houseTheme;
    }

    public void setHouseLocation(HouseLocation houseLocation) {
        this.houseLocation = houseLocation;
    }

    public void setHouseServant(int houseServant) {
        this.houseServant = houseServant;
    }

    public int getHouseServantCharges() {
        return this.houseServantCharges;
    }

    public void setHouseServantCharges(int houseServantCharges) {
        this.houseServantCharges = houseServantCharges;
    }

    public void incrementHouseServantCharges() {
        this.houseServantCharges++;
    }

    public int getServantItemFetch() {
        return servantItemFetch;
    }

    public void setServantItemFetch(int servantItemFetch) {
        this.servantItemFetch = servantItemFetch;
    }

    public int getPortalSelected() {
        return portalSelected;
    }

    public void setPortalSelected(int portalSelected) {
        this.portalSelected = portalSelected;
    }

    public boolean isBuildingMode() {
        return this.isBuildingMode;
    }

    public void setIsBuildingMode(boolean isBuildingMode) {
        this.isBuildingMode = isBuildingMode;
    }

    public int[] getConstructionCoords() {
        return constructionCoords;
    }

    public void setConstructionCoords(int[] constructionCoords) {
        this.constructionCoords = constructionCoords;
    }

    public int getBuildFurnitureId() {
        return this.buildFurnitureId;
    }

    public void setBuildFuritureId(int buildFuritureId) {
        this.buildFurnitureId = buildFuritureId;
    }

    public int getBuildFurnitureX() {
        return this.buildFurnitureX;
    }

    public void setBuildFurnitureX(int buildFurnitureX) {
        this.buildFurnitureX = buildFurnitureX;
    }

    public int getBuildFurnitureY() {
        return this.buildFurnitureY;
    }

    public void setBuildFurnitureY(int buildFurnitureY) {
        this.buildFurnitureY = buildFurnitureY;
    }

    public int getCombatRingType() {
        return this.combatRingType;
    }

    public void setCombatRingType(int combatRingType) {
        this.combatRingType = combatRingType;
    }

    public Room[][][] getHouseRooms() {
        return houseRooms;
    }

    public ArrayList<Portal> getHousePortals() {
        return housePortals;
    }

    public ArrayList<HouseFurniture> getHouseFurniture() {
        return houseFurniture;
    }

    public int getConstructionInterface() {
        return this.constructionInterface;
    }

    public void setConstructionInterface(int constructionInterface) {
        this.constructionInterface = constructionInterface;
    }

    public int[] getBrawlerChargers() {
        return this.brawlerCharges;
    }

    public void setBrawlerCharges(int[] brawlerCharges) {
        this.brawlerCharges = brawlerCharges;
    }

    public void setBrawlerCharge(int index, int value) {
        brawlerCharges[index] = value;
    }

    public int getRecoilCharges() {
        return this.recoilCharges;
    }

    public int setRecoilCharges(int recoilCharges) {
        return this.recoilCharges = recoilCharges;
    }

    public boolean voteMessageSent() {
        return this.voteMessageSent;
    }

    public void setVoteMessageSent(boolean voteMessageSent) {
        this.voteMessageSent = voteMessageSent;
    }

    public boolean didReceiveStarter() {
        return receivedStarter;
    }

    public void sendMessage(Object string) {
        packetSender.sendMessage(string.toString());
    }

    public void setReceivedStarter(boolean receivedStarter) {
        this.receivedStarter = receivedStarter;
    }

    public BlowpipeLoading getBlowpipeLoading() {
        return blowpipeLoading;
    }

    public boolean cloudsSpawned() {
        return areCloudsSpawned;
    }

    public void setCloudsSpawned(boolean cloudsSpawned) {
        this.areCloudsSpawned = cloudsSpawned;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean hasComplete2019EasterEvent() {
        return completed2019EasterEvent;
    }

    public void set2019EasterEventComplete(boolean completed2019EasterEvent) {
        this.completed2019EasterEvent = completed2019EasterEvent;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getYellmsg() {
        return yellmsg;
    }

    public void setYellMsg(String yellmsg) {
        this.yellmsg = yellmsg;
    }

    public void write(Packet packet) {
        // TODO Auto-generated method stub

    }

    public boolean isHidePlayer() {
        return requiresUnlocking() || hidePlayer;
    }

    public void setHidePlayer(boolean hidePlayer) {
        this.hidePlayer = hidePlayer;
    }

    public int[] getCompCapeColors() {
        return compCapeColors;
    }

    public void setCompCapeColors(int[] compCapeColors) {
        this.compCapeColors = compCapeColors;
    }

    public void setCompCapeColor(int index, int value) {
        compCapeColors[index] = value;
    }

    public int getCurrentCape() {
        return currentCape;
    }

    public void setCurrentCape(int currentCape) {
        this.currentCape = currentCape;
    }

    public long getDragonScimInjury() {
        return dragonScimInjury;
    }

    public void setDragonScimInjury(long dragonScimInjury) {
        this.dragonScimInjury = dragonScimInjury;
    }

    public long getLastPassReset() {
        return lastPassReset;
    }

    public void setLastPassReset(long lastPassReset) {
        this.lastPassReset = lastPassReset;
    }

    public int getPassResetCount() {
        return passResetCount;
    }

    public void setPassResetCount(int passResetCount) {
        this.passResetCount = passResetCount;
    }

    public Stopwatch getKrakenRespawn() {
        return krakenRespawn;
    }

    public byte[] getCachedUpdateBlock() {
        return cachedUpdateBlock;
    }

    public void setCachedUpdateBlock(byte[] cachedUpdateBlock) {
        this.cachedUpdateBlock = cachedUpdateBlock;
    }

    public boolean isOpenPos() {
        return openPos;
    }

    public void setOpenPos(boolean openPos) {
        this.openPos = openPos;
    }

    public boolean isLoginReward() {
        return loginReward;
    }

    public void setLoginReward(boolean loginReward) {
        this.loginReward = loginReward;
    }

    public AccountValue getAccountValue() {
        return accountValue;
    }

    private AccountValue accountValue = new AccountValue(this);

    public long lastBoxOpened;

    public long dropCommandCooldown;

    public boolean isAccountCompromised() {
        return accountCompromised;
    }

    public void setAccountCompromised(boolean accountCompromised) {
        this.accountCompromised = accountCompromised;
    }

    public boolean requiresUnlocking() {
        if(!getBankPinAttributes().hasBankPin()) {
            return false;
        }
        return !newPlayer() && !getBankPinAttributes().hasEnteredBankPin()
                && (!getHostAddress().equals(getLastHostAddress())
                /*&& System.currentTimeMillis() - getBankPinAttributes().getLastEnteredBankPin() > Time.ONE_DAY*/);
    }

    private CombatType currentCombatType;

    public CombatType getCurrentCombatType() {
        return currentCombatType;
    }

    public void setCurrentCombatType(CombatType type) {
        this.currentCombatType = type;
    }

    private Stopwatch lastDoubleDrop = new Stopwatch();

    public Stopwatch getLastDoubleDrop() {
        return lastDoubleDrop;
    }

    public void setLastDoubleDrop(Stopwatch watch) {
        this.lastDoubleDrop = watch;
    }

    private boolean insideRaids = false;

    public boolean boxAlertEnabled = true;

    private boolean displayAnnouncementTimers = false;

    public boolean isDisplayAnnouncementTimers() {
        return displayAnnouncementTimers;
    }

    public Player setDisplayAnnouncementTimers(boolean toggle) {
        this.displayAnnouncementTimers = toggle;
        return this;
    }

    public boolean isInsideRaids() {
        return insideRaids;
    }

    public void setInsideRaids(boolean insideRaids) {
        this.insideRaids = insideRaids;
    }

    private RaidsParty raidsParty;

    public RaidsParty getRaidsParty() {
        return raidsParty;
    }

    public void setRaidsParty(RaidsParty raidsParty) {
        this.raidsParty = raidsParty;
    }

    private Item raidsLoot;
    private Item raidsLootSecond;
    public boolean rareRaidLoot;

    public Item getRaidsLoot() {
        return raidsLoot;
    }

    public void setRaidsLoot(Item raidsLoot) {
        this.raidsLoot = raidsLoot;
    }

    public Item getRaidsLootSecond() {
        return raidsLootSecond;
    }

    public void setRaidsLootSecond(Item raidsLootSecond) {
        this.raidsLootSecond = raidsLootSecond;
    }

    private int graphicSwap;

    public int getGraphicSwap() {
        return graphicSwap;
    }

    public void setGraphicSwap(int graphicSwap) {
        this.graphicSwap = graphicSwap;
    }

    private RingOfBosses.CollectionLocation ringOfBossesCollection = RingOfBosses.CollectionLocation.GROUND;

    public RingOfBosses.CollectionLocation getRingOfBossesCollection() {
        return ringOfBossesCollection;
    }

    public void setRingOfBossesColletion(RingOfBosses.CollectionLocation location) {
        this.ringOfBossesCollection = location;
    }

    /**
     * The player's item charges.
     */
    private Map<Integer, ItemCharge> itemCharges = new HashMap<>();

    /**
     * Returns the player's item charges.
     *
     * @return the charges
     */
    public Map<Integer, ItemCharge> getItemCharges() {
        return itemCharges;
    }

    public boolean isBotPlayer() {
        return this instanceof BotPlayer;
    }

    /**
     * If the player is floating.
     */
    private boolean floating;

    /**
     * Returns if the player is floating.
     *
     * @return
     */
    public boolean isFloating() {
        return floating;
    }

    /**
     * Toggle if the player is floating.
     */
    public void toggleFloating() {
        this.floating = !floating;
    }

    public boolean isSpectatorMode() {
        return spectatorMode;
    }

    public Player getSpectateTarget() {
        return spectateTarget;
    }

    public void setSpectateTarget(Player spectateTarget) {
        this.spectateTarget = spectateTarget;
        this.spectatorMode = spectateTarget != null;
        if (spectateTarget != null) {
            this.setPreviousTile(getPosition());
        }
    }

    public Position getPreviousTile() {
        return previousTile;
    }

    public void setPreviousTile(Position previousTile) {
        this.previousTile = previousTile;
    }

    public boolean isSoulSplit() {
        if (summoning.getFamiliar() != null && summoning.getFamiliar().getSummonNpc() != null) {
            switch (summoning.getFamiliar().getSummonNpc().getId()) {
                case 22519:
                case 154:
                case 153:
                    return true;
            }
        }
        return getCurseActive()[CurseHandler.CurseData.SOUL_SPLIT.ordinal()];
    }

    public boolean hasSoulSplitPet() {
        if (summoning.getFamiliar() != null && summoning.getFamiliar().getSummonNpc() != null) {
            switch (summoning.getFamiliar().getSummonNpc().getId()) {
                case 22519:
                case 154:
                case 153:
                    return true;
            }
        }
        return false;
    }

    /**
     * Gets the degrading
     *
     * @return the degrading
     */
    public DegradingManager getDegrading() {
        return degrading;
    }

    /**
     * Gets the dropChecker
     *
     * @return the dropChecker
     */
    public NPCDropTableChecker getDropChecker() {
        return dropChecker;
    }

    /**
     * Gets the playerInstance
     *
     * @return the playerInstance
     */
    public PlayerInstanceManager getPlayerInstance() {
        return playerInstance;
    }

    /**
     * Checks if a player has an item
     *
     * @param id
     * @return
     */
    public boolean hasItem(int id) {
        if (getSummoning() != null) {
            if (getSummoning().getBeastOfBurden() != null) {
                if (getSummoning().getBeastOfBurden().contains(id)) {
                    return true;
                }
            }
        }
        for (Bank bank : getBanks()) {
            if (bank.contains(id)) {
                return true;
            }
        }
        return getInventory().contains(id) || getBank().contains(id) || getEquipment().contains(id);
    }

    public boolean hasFamiliar(int id) {
        if(getSummoning().getFamiliar() == null || getSummoning().getFamiliar().getSummonNpc() == null) {
            return false;
        }
        if(getSummoning().getFamiliar().getSummonNpc().getId() == id) {
            return true;
        }
        return false;
    }


    public CopyOnWriteArrayList<Teleport> getFavoriteTeleports() {
        return favoriteTeleports;
    }

    public CopyOnWriteArrayList<Teleport> getRecentTeleports() {
        return recentTeleports;
    }

    public boolean isInHome() {
        return inHome;
    }

    public void setInHome(boolean inHome) {
        this.inHome = inHome;
    }

    public boolean isInAgility() {
        return isInAgility;
    }

    public void setInAgility(boolean inAgility) {
        isInAgility = inAgility;
    }

	public long loggedIn, isIdleTracker;

    public Raids getRaids() {
        return raids;
    }

    public void setRaids(Raids raids) {
        this.raids = raids;
    }

    public int getForgingCharges() {
        return forgingCharges;
    }

    public int setForgingCharges(int forgingCharges) {
        return this.forgingCharges = forgingCharges;
    }

    private TutorialStages tutorialStage;

	public TutorialStages getTutorialStage() {
	    return tutorialStage;
    }

    public void setTutorialStage(TutorialStages stage){
	    this.tutorialStage = stage;
    }

    public Stopwatch getBoxTimer() {
        return boxTimer;
    }

    /**
     * The current skill being executed.
     */
    private AbstractSkill skill;

    public AbstractSkill getSkill() {
        return skill;
    }

    public void setSkill(AbstractSkill skill) {
        this.skill = skill;
    }

    /**
     * The player's current {@link AbstractHarvestSkill} being executed.
     */
    private transient AbstractHarvestSkill harvestSkill;

    public AbstractHarvestSkill getHarvestSkill() {
        return harvestSkill;
    }

    public void setHarvestSkill(AbstractHarvestSkill harvestSkill) {
        this.harvestSkill = harvestSkill;
    }

    /**
     * Gets the {@link #busy} value.
     * @return	The {@link #busy} array.
     */
    public boolean[] isBusy() {
        return busy;
    }

    /**
     * This boolean array contains flags that check if a
     * skill is being performed, to avoid "spam-clicking" on
     * entities.
     */
    private transient boolean[] busy = new boolean[SkillManager.MAX_SKILLS];

    /**
     * Sets the busy flag with said index - index being represented by
     * the {@code skill}'s ordinal value.
     * @param skill	The {@link org.niobe.model.SkillManager.Skill} to set busy flag for.
     * @param busy	The flag that will be set for the {@link #busy[skill.ordinal()]}.
     */
    public void setBusy(Skill skill, boolean busy) {
        this.busy[skill.ordinal()] = busy;
    }

    /**
     * This map contains the skill delays.
     */
    private transient final Map<Skill, Long> delays = new HashMap<Skill, Long>();

    /**
     * Gets the skill delays.
     * @return	delays.
     */
    public Map<Skill, Long> getDelays() {
        return delays;
    }

    /**
     * The allotments registered to this player for farming.
     */
    private Map<PatchType, Patch> patches = new HashMap<>();

    /**
     * The {@link org.niobe.model.Position} the player has executed
     * the skill in.
     */
    private Position startPosition;

    public Map<PatchType, Patch> getPatches() {
        return patches;
    }

    public void setPatches(Map<PatchType, Patch> allotments) {
        this.patches = allotments;
    }

    public Position getStartPosition() {
        return startPosition;
    }

    /**
     * Sets the {@link #startPosition} value.
     * @param startPosition	The new {@link #startPosition} value.
     */
    public void setStartPosition(Position startPosition) {
        this.startPosition = startPosition;
    }

    /**
     * The timer each stage for the player's farmed
     * crops take .
     */
    private long farmingTime;

    public long getFarmingTime() {
        return farmingTime;
    }

    public void setFarmingTime(long farmingTime) {
        this.farmingTime = farmingTime;
    }

    public Shop getShop() {
        return shop;
    }

    public Player setShop(Shop shop) {
        this.shop = shop;
        return this;
    }

    public boolean isIronman() {
        return gameMode == GameMode.IRONMAN || gameMode == GameMode.ULTIMATE_IRONMAN || gameMode == GameMode.HARDCORE_IRONMAN;
    }

    public Item getItemToSell() {
        return itemToSell;
    }

    public Player setItemToSell(Item itemToSell) {
        this.itemToSell = itemToSell;
        return this;
    }

    public Player setClientVersion(double version) {
        this.clientVersion = version;
        return this;
    }

    public double getClientVersion() {
        return this.clientVersion;
    }

    public boolean isQuickMovement() {
        return quickMovement;
    }

    public void setQuickMovement(boolean quickMovement) {
        this.quickMovement = quickMovement;
    }

    public void sendErrorMessage(String message) {
        this.sendMessage("<col=ff0000>"+message);
    }

    public String getjSerial() {
        return jSerial;
    }

    public Player setjSerial(String jSerial) {
        this.jSerial = jSerial;
        return this;
    }

    public String getLastSuperSerial() {
        return lastSuperSerial;
    }

    public void setLastSuperSerial(String lastSuperSerial) {
        this.lastSuperSerial = lastSuperSerial;
    }

    public void checkDupe() {
        if(!lastAlertSent.elapsed(1000 * 20)) {
            return;
        }

        if(isHigherStaff())
            return;

        if (getMoneyInPouch() > 1_000_000_000) {
            World.sendAdminMessage("<col=ff0000> "+getName() + " has " + Misc.formatRunescapeStyle(getMoneyInPouch()) + " in money pouch.");
            lastAlertSent.reset();
        }

        if (getInventory().getAmount(995) > 1_000_000_000) {
            World.sendAdminMessage("<col=ff0000> "+getName() + " has " + Misc.formatRunescapeStyle(getInventory().getAmount(995)) + " in inventory.");
            lastAlertSent.reset();
        }

        if (getBank().getAmount(995) > 1_000_000_000) {
            World.sendAdminMessage("<col=ff0000> "+getName() + " has " + Misc.formatRunescapeStyle(getBank().getAmount(995)) + " in bank.");
            lastAlertSent.reset();
        }
    }

    public String getLastSerialNumber() {
        return lastSerialNumber;
    }

    public void setLastSerialNumber(String lastSerialNumber) {
        this.lastSerialNumber = lastSerialNumber;
    }

    public Stopwatch getZulrahTimer() {
        return zulrahTimer;
    }

    public ActionManager getActionManager() {
        return actionManager;
    }
}
