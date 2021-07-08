package com.zyrox.world.content.teleportation;

import com.zyrox.model.Item;
import com.zyrox.model.Position;

public enum TeleportData {

	DEFAULT(false, TeleportDescription.NONE, "N/A", new Position(1, 1), TeleportCategory.DEFAULT, 1, 200, new Item(995, 1)),
//Bosses Teleporter

	ZULRAH(true, TeleportDescription.ZULRAH, "Zulrah", new Position(2268, 3070, 0), TeleportCategory.BOSSES, 2043, 4500, new Item(995, 100), new Item(995, 100), new Item(995, 100)),
	VORKATH(true, TeleportDescription.VORKATH, "Vorkath", new Position(2272, 4054, 4), TeleportCategory.BOSSES, 23060, 4500, new Item(4151, 1), new Item(4152, 100), new Item(15501, 100)),
	TEKTON(true, TeleportDescription.TEKTON, "Tekton", new Position(3057, 5200, 0), TeleportCategory.BOSSES, 22542, 4500, new Item(4151, 1), new Item(4152, 100), new Item(15501, 100)),
	SKOTIZO(true, TeleportDescription.SKOTIZO, "Skotizo", new Position(3378, 9816, 0), TeleportCategory.BOSSES, 7286, 4500, new Item(4151, 1), new Item(4152, 100), new Item(15501, 100)),
	TARN(true, TeleportDescription.TARN, "Tarn", new Position(3186, 4621, 4), TeleportCategory.BOSSES, 21477, 4500, new Item(4151, 1), new Item(4152, 100), new Item(15501, 100)),
	SLASH(true, TeleportDescription.SLASH, "Slash Bash", new Position(2421, 4688, 0), TeleportCategory.BOSSES, 2060, 4500, new Item(4151, 1), new Item(4152, 100), new Item(15501, 100)),
	HYDRA(true, TeleportDescription.HYDRA, "Hydra", new Position(1351, 10261, 0), TeleportCategory.BOSSES, 23615, 4500, new Item(4151, 1), new Item(4152, 100), new Item(15501, 100)),
	KARKEN(true, TeleportDescription.KARKEN, "Karken", new Position(3696, 5807, 4), TeleportCategory.BOSSES, 3847, 4500, new Item(4151, 1), new Item(4152, 100), new Item(15501, 100)),
	GODWARS(true, TeleportDescription.GODWARS, "Godwars", new Position(2871, 5319, 2), TeleportCategory.BOSSES, 6260, 4500, new Item(4151, 1), new Item(4152, 100), new Item(15501, 100)),
	BORK(true, TeleportDescription.BORK, "Bork", new Position(3104, 5536, 0), TeleportCategory.BOSSES, 7134, 4500, new Item(4151, 1), new Item(4152, 100), new Item(15501, 100)),
	BARREL(true, TeleportDescription.BARREL, "Barrelchest", new Position(2973, 9517, 1), TeleportCategory.BOSSES, 5666, 4500, new Item(4151, 1), new Item(4152, 100), new Item(15501, 100)),
	BAVATAR(true, TeleportDescription.BAVATAR, "Bandos Avatar", new Position(2891, 4767, 0), TeleportCategory.BOSSES, 4540, 4500, new Item(4151, 1), new Item(4152, 100), new Item(15501, 100)),
	CORP(true, TeleportDescription.CORP, "Corporeal Beast", new Position(2884, 4373, 0), TeleportCategory.BOSSES, 8133, 4500, new Item(4151, 1), new Item(4152, 100), new Item(15501, 100)),

	//Begginer Teleporter
	ROCK(true, TeleportDescription.ROCK, "Rock Crabs", new Position(2679, 3714, 0), TeleportCategory.MONSTERS, 1265, 1500, new Item(4151, 1), new Item(4152, 100), new Item(15501, 100)),
	CHICKEN(true, TeleportDescription.CHICKEN, "Chicken Pen", new Position(3235, 3295, 0), TeleportCategory.MONSTERS, 41, 1500, new Item(4151, 1), new Item(4152, 100), new Item(15501, 100)),
	YAKS(true, TeleportDescription.YAKS, "Yaks Field", new Position(3206, 3263, 0), TeleportCategory.MONSTERS, 5529, 1500, new Item(4151, 1), new Item(4152, 100), new Item(15501, 100)),
	COWS(true, TeleportDescription.COWS, "Cows", new Position(3257, 3280, 0), TeleportCategory.MONSTERS, 81, 1500, new Item(4151, 1), new Item(4152, 100), new Item(15501, 100)),
	BANDIT(true, TeleportDescription.BANDIT, "Bandit Cap", new Position(3169, 2982, 0), TeleportCategory.MONSTERS, 1880, 1500, new Item(4151, 1), new Item(4152, 100), new Item(15501, 100)),
	GHOUL(true, TeleportDescription.GHOUL, "Ghoul Field", new Position(3420, 3510, 0), TeleportCategory.MONSTERS, 1218, 1500, new Item(4151, 1), new Item(4152, 100), new Item(15501, 100)),
	EXPERMIENTS(true, TeleportDescription.EXPERMIENTS, "Experiments", new Position(3561, 9948, 0), TeleportCategory.MONSTERS, 1677, 1500, new Item(4151, 1), new Item(4152, 100), new Item(15501, 100)),
	DEVIL(true, TeleportDescription.DEVIL, "Dust Devil", new Position(3277, 2964, 0), TeleportCategory.MONSTERS, 1624, 1500, new Item(4151, 1), new Item(4152, 100), new Item(15501, 100)),
	DEMONIC(true, TeleportDescription.DEMONIC, "Demonic Gorilla", new Position(2128, 5647, 0), TeleportCategory.MONSTERS, 22147, 1500, new Item(4151, 1), new Item(4152, 100), new Item(15501, 100)),
	DRAGON(true, TeleportDescription.DRAGON, "Frost Dragons", new Position(2835, 9517, 0), TeleportCategory.MONSTERS, 51, 2500, new Item(4151, 1), new Item(4152, 100), new Item(15501, 100)),
	WYVERN(true, TeleportDescription.WYVERN, "Ancient Wyverns ", new Position(1667, 5676, 0), TeleportCategory.MONSTERS, 22792, 2500, new Item(4151, 1), new Item(4152, 100), new Item(15501, 100)),
	ARMOURED(true, TeleportDescription.ARMOURED, "Armoured Zombies", new Position(3086, 9672, 0), TeleportCategory.MONSTERS, 8162, 1500, new Item(4151, 1), new Item(4152, 100), new Item(15501, 100)),
	MONKEYGUARDS(true, TeleportDescription.MONKEYGUARDS, "Monkey Guards", new Position(2793, 2773, 0), TeleportCategory.MONSTERS, 1459, 1500, new Item(4151, 1), new Item(4152, 100), new Item(15501, 100)),
	MONKEYSKELETON(true, TeleportDescription.MONKEYSKELETON, "Monkey Skeletons", new Position(2805, 9143, 0), TeleportCategory.MONSTERS, 1471, 1500, new Item(4151, 1), new Item(4152, 100), new Item(15501, 100)),
	TZHAARMINION(true, TeleportDescription.TZHAARMINION, "Tzhaar Minions", new Position(2480, 5174, 0), TeleportCategory.MONSTERS, 2605, 1500, new Item(4151, 1), new Item(4152, 100), new Item(15501, 100)),


	//Mingames Teleporter
	OLM(false, TeleportDescription.OLM, "Raids Great Olm", new Position(1234, 3566, 0), TeleportCategory.MINIGAMES, 6731, 1500, new Item(4151, 1), new Item(4152, 100), new Item(15501, 100)),
	INFERNO(false, TeleportDescription.INFERNO, "Inferno Waves", new Position(2273, 5348, 4), TeleportCategory.MINIGAMES, 22706, 5000, new Item(4151, 1), new Item(4152, 100), new Item(15501, 100)),
	DUEL(false, TeleportDescription.DUEL, "Duel Arena", new Position(3365, 3266, 0), TeleportCategory.MINIGAMES, 6666, 1500, new Item(4151, 1), new Item(4152, 100), new Item(15501, 100)),
	CASTLE(false, TeleportDescription.CASTLE, "Castle Wars", new Position(2440, 3089, 0), TeleportCategory.MINIGAMES, 1526, 1500, new Item(4151, 1), new Item(4152, 100), new Item(15501, 100)),
	PC(false, TeleportDescription.PC, "Pest Control", new Position(2657, 2644, 0), TeleportCategory.MINIGAMES, 3789, 1500, new Item(4151, 1), new Item(4152, 100), new Item(15501, 100)),
	BARROWS(false, TeleportDescription.BARROWS, "Barrows", new Position(3565, 3313, 0), TeleportCategory.MINIGAMES, 2026, 1500, new Item(4151, 1), new Item(4152, 100), new Item(15501, 100)),
	FIGHTCAVE(false, TeleportDescription.FIGHTCAVE, "Fight Caves", new Position(2445, 5174, 0), TeleportCategory.MINIGAMES, 2625, 1500, new Item(4151, 1), new Item(4152, 100), new Item(15501, 100)),
	FIGHTPITS(false, TeleportDescription.FIGHTPITS, "Fight Pits", new Position(2399, 5177, 0), TeleportCategory.MINIGAMES, 2622, 1500, new Item(4151, 1), new Item(4152, 100), new Item(15501, 100)),
	DISASTER(false, TeleportDescription.DISASTER, "Recipe For Disaster", new Position(1863, 5354, 0), TeleportCategory.MINIGAMES, 3385, 1500, new Item(4151, 1), new Item(4152, 100), new Item(15501, 100)),
	ISLAND(false, TeleportDescription.ISLAND, "Treasure Island", new Position(3039, 2910, 0), TeleportCategory.MINIGAMES, 133, 2000, new Item(4151, 1), new Item(4152, 100), new Item(15501, 100)),
	BLOOD(false, TeleportDescription.BLOOD, "Theatre Of Blood", new Position(3668, 3219, 0), TeleportCategory.MINIGAMES, 23369, 5500, new Item(4151, 1), new Item(4152, 100), new Item(15501, 100)),
	NOMAND(false, TeleportDescription.ZOMBIE, "Nomands Requeim", new Position(1891, 3177, 0), TeleportCategory.MINIGAMES, 8591, 1500, new Item(4151, 1), new Item(4152, 100), new Item(15501, 100)),
	ZOMBIE(false, TeleportDescription.ZOMBIE, "Zombie Minigame", new Position(3503, 3564, 0), TeleportCategory.MINIGAMES, 2714, 1500, new Item(4151, 1), new Item(4152, 100), new Item(15501, 100)),
	WARRIORSGUILD(false, TeleportDescription.WARRIORSGUILD, "Warriors Guild", new Position(2855, 3543, 0), TeleportCategory.MINIGAMES, 541, 1500, new Item(4151, 1), new Item(4152, 100), new Item(15501, 100)),

	//Skilling Teleporter
	SLAYER(false, TeleportDescription.SLAYER, "Slayer Room", new Position(2725, 2531, 0), TeleportCategory.SKILLING, 9085, 1500, new Item(4151, 1), new Item(4152, 100), new Item(15501, 100)),
	MINING(false, TeleportDescription.MINING, "Mining Guild", new Position(3023, 9740, 0), TeleportCategory.SKILLING, 948, 1500, new Item(4151, 1), new Item(4152, 100), new Item(15501, 100)),
	ESSENCE(false, TeleportDescription.ESSENCE, "Essence Mine", new Position(2911, 4832, 0), TeleportCategory.SKILLING, 1396, 1500, new Item(4151, 1), new Item(4152, 100), new Item(15501, 100)),
	THIEVING(false, TeleportDescription.THIEVING, "Thieving", new Position(2558, 2571, 1), TeleportCategory.SKILLING, 2292, 1500, new Item(4151, 1), new Item(4152, 100), new Item(15501, 100)),
	HERBLORE(false, TeleportDescription.HERBLORE, "Herblore", new Position(2798, 3436, 0), TeleportCategory.SKILLING, 8459, 1500, new Item(4151, 1), new Item(4152, 100), new Item(15501, 100)),
	RUNECRAFTING(false, TeleportDescription.RUNECRAFTING, "Runecrafting", new Position(2595, 4772, 0), TeleportCategory.SKILLING, 8022, 1500, new Item(4151, 1), new Item(4152, 100), new Item(15501, 100)),
	CRAFTING(false, TeleportDescription.CRAFTING, "Crafting", new Position(2742, 3443, 0), TeleportCategory.SKILLING, 805, 1500, new Item(4151, 1), new Item(4152, 100), new Item(15501, 100)),
	AGILITY1(false, TeleportDescription.AGILITY1, "Gnome Course ", new Position(2480, 3436, 0), TeleportCategory.SKILLING, 437, 1500, new Item(4151, 1), new Item(4152, 100), new Item(15501, 100)),
	AGILITY2(false, TeleportDescription.AGILITY2, "Barbarian Cours", new Position(2552, 3556, 0), TeleportCategory.SKILLING, 8707, 1500, new Item(4151, 1), new Item(4152, 100), new Item(15501, 100)),
	AGILITY3(false, TeleportDescription.AGILITY3, "Wilderness Course", new Position(2998, 3914, 0), TeleportCategory.SKILLING, 8706, 1500, new Item(4151, 1), new Item(4152, 100), new Item(15501, 100)),
	FISHANDCOOK(false, TeleportDescription.FISHANDCOOK, "Fishing & Cooking", new Position(2604, 3408, 0), TeleportCategory.SKILLING, 308, 1500, new Item(4151, 1), new Item(4152, 100), new Item(15501, 100)),
	FIREMAKING(false, TeleportDescription.FIREMAKING, "Firemaking", new Position(2709, 3437, 0), TeleportCategory.SKILLING, 4946, 1500, new Item(4151, 1), new Item(4152, 100), new Item(15501, 100)),
	FLETCHING(false, TeleportDescription.FLETCHING, "Fletching", new Position(2717, 3499, 0), TeleportCategory.SKILLING, 4906, 1500, new Item(4151, 1), new Item(4152, 100), new Item(15501, 100)),
	WOODCUTING1(false, TeleportDescription.WOODCUTING1, "L-Level Woodcuting", new Position(2717, 3499, 0), TeleportCategory.SKILLING, 4910, 1500, new Item(4151, 1), new Item(4152, 100), new Item(15501, 100)),
	WOODCUTING2(false, TeleportDescription.WOODCUTING2, "H-Level Woodcuting", new Position(2711, 3463, 0), TeleportCategory.SKILLING, 4930, 1500, new Item(4151, 1), new Item(4152, 100), new Item(15501, 100)),
	HUNTER(false, TeleportDescription.HUNTER, "Puro Puro", new Position(2589, 4319, 0), TeleportCategory.SKILLING, 6056, 1500, new Item(4151, 1), new Item(4152, 100), new Item(15501, 100)),
	HUNTER1(false, TeleportDescription.HUNTER1, "Trap Area", new Position(2922, 2885, 0), TeleportCategory.SKILLING, 5112, 1500, new Item(4151, 1), new Item(4152, 100), new Item(15501, 100)),
	FARMING(false, TeleportDescription.FARMING, "Farming", new Position(2717, 499, 0), TeleportCategory.SKILLING, 3021, 1500, new Item(4151, 1), new Item(4152, 100), new Item(15501, 100)),
	CONSTRUCTION(false, TeleportDescription.CONSTRUCTION, "Construction", new Position(2553, 2597, 0), TeleportCategory.SKILLING, 4247, 1500, new Item(4151, 1), new Item(4152, 100), new Item(15501, 100)),
	SUMMONING(false, TeleportDescription.SUMMONING, "Summoning", new Position(2209, 5348, 0), TeleportCategory.SKILLING, 6970, 1500, new Item(4151, 1), new Item(4152, 100), new Item(15501, 100)),
	DUNGEONEERING(true, TeleportDescription.DUNGEONEERING, "Dungeoneering", new Position(3450, 3715, 0), TeleportCategory.SKILLING, 3002, 1500, new Item(4151, 1), new Item(4152, 100), new Item(15501, 100)),

	//Donator Teleporter
	REGULARDONATOR(false, TeleportDescription.REGULARDONATOR, "Donator Zone", new Position(2855, 3543, 0), TeleportCategory.DONATORS, 541, 1500, new Item(4151, 1), new Item(4152, 100), new Item(15501, 100)),
	SUPERDONATOR(false, TeleportDescription.SUPERDONATOR, "Super Zone", new Position(2855, 3543, 0), TeleportCategory.DONATORS, 541, 1500, new Item(4151, 1), new Item(4152, 100), new Item(15501, 100)),
	EXTRAMEDONATOR(false, TeleportDescription.EXTRAMEDONATOR, "Extrame Zone", new Position(2855, 3543, 0), TeleportCategory.DONATORS, 541, 1500, new Item(4151, 1), new Item(4152, 100), new Item(15501, 100));


	TeleportData(boolean instance, TeleportDescription description, String name, Position position, TeleportCategory category, int npcID, int npcZoom, Item... items) {
		this.description = description;
		this.name = name;
		this.position = position;
		this.category = category;
		this.npcID = npcID;
		this.npcZoom = npcZoom;
		this.items = items;
		this.instance = instance;
	}
	
	private String name;
	private Position position;
	private TeleportDescription description;
	private TeleportCategory category;
	private int npcID, npcZoom;
	private Item[] items;
	boolean instance;
	
	public static TeleportData findFirst(TeleportCategory category) {
		for(TeleportData data : TeleportData.values()) {
			if(data == null)
				continue;
			if(data.getCategory() == category)
				return data;
		}
		return null;
	}

	public static TeleportData findSpot(TeleportCategory category, int value) {
		int index = 0;
		for(TeleportData data : TeleportData.values()) {
			if(data == null)
				continue;
			if(category == TeleportCategory.DEFAULT)
				continue;
			if(data.getCategory() != category)
				continue;
			if(index == value)
				return data;
			index++;
		}
		return null;
	}

	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public Position getPosition() {
		return this.position;
	}
	
	public void setPosition(Position position) {
		this.position = position;
	}
	
	public TeleportCategory getCategory() {
		return this.category;
	}
	
	public void setPosition(TeleportCategory category) {
		this.category = category;
	}
	
	public TeleportDescription getDescription() {
		return this.description;
	}
	
	public void setDescription(TeleportDescription description) {
		this.description = description;
	}
	
	public int getNPCID() {
		return this.npcID;
	}
	
	public void setNPCID(int npcID) {
		this.npcID = npcID;
	}
	
	public int getNPCZoom() {
		return this.npcZoom;
	}
	
	public void setNPCZoom(int npcZoom) {
		this.npcZoom = npcZoom;
	}
	
	public Item[] getItems() {
		return this.items;
	}
	
	public void setItems(Item[] items) {
		this.items = items;
	}
	
	public boolean isInstanced() {
		return this.instance;
	}
	
	public void setInstance(boolean instance) {
		this.instance = instance;
	}
}
