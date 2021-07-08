package com.zyrox.world.content.teleportation;

public enum TeleportDescription {
	
	NONE(""),
//Bosses Teleport
	ZULRAH("The King Black Dragon is an easy monster to practice bossing on."),
	VORKATH("The King Black Dragon is an easy monster to practice bossing on."),
	TEKTON("The King Black Dragon is an easy monster to practice bossing on."),
	SKOTIZO("The King Black Dragon is an easy monster to practice bossing on."),
	TARN("The King Black Dragon is an easy monster to practice bossing on."),
	SLASH("The King Black Dragon is an easy monster to practice bossing on."),
	HYDRA("The King Black Dragon is an easy monster to practice bossing on."),
	KARKEN("The King Black Dragon is an easy monster to practice bossing on."),
	GODWARS("The King Black Dragon is an easy monster to practice bossing on."),
	BORK("The King Black Dragon is an easy monster to practice bossing on."),
	BARREL("The King Black Dragon is an easy monster to practice bossing on."),
	BAVATAR("The King Black Dragon is an easy monster to practice bossing on."),
	CORP("The King Black Dragon is an easy monster to practice bossing on."),

	//Monsters Teleporter
	ROCK("The King Black Dragon is an easy monster to practice bossing on."),
	YAKS("The King Black Dragon is an easy monster to practice bossing on."),
	COWS("The King Black Dragon is an easy monster to practice bossing on."),
	CHICKEN("The King Black Dragon is an easy monster to practice bossing on."),

	BANDIT("The King Black Dragon is an easy monster to practice bossing on."),
	GHOUL("The King Black Dragon is an easy monster to practice bossing on."),
	EXPERMIENTS("The King Black Dragon is an easy monster to practice bossing on."),
	DEVIL("The King Black Dragon is an easy monster to practice bossing on."),
	DEMONIC("The King Black Dragon is an easy monster to practice bossing on."),
	DRAGON("The King Black Dragon is an easy monster to practice bossing on."),
	WYVERN("The King Black Dragon is an easy monster to practice bossing on."),
	ARMOURED("The King Black Dragon is an easy monster to practice bossing on."),
	MONKEYGUARDS("The King Black Dragon is an easy monster to practice bossing on."),
	MONKEYSKELETON("The King Black Dragon is an easy monster to practice bossing on."),
	TZHAARMINION("The King Black Dragon is an easy monster to practice bossing on."),

	//Minigames Telepoter
	OLM("The King Black Dragon is an easy monster to practice bossing on."),
	INFERNO("The King Black Dragon is an easy monster to practice bossing on."),
	DUEL("The King Black Dragon is an easy monster to practice bossing on."),
	CASTLE("The King Black Dragon is an easy monster to practice bossing on."),
	PC("The King Black Dragon is an easy monster to practice bossing on."),
	BARROWS("The King Black Dragon is an easy monster to practice bossing on."),
	FIGHTCAVE("The King Black Dragon is an easy monster to practice bossing on."),
	FIGHTPITS("The King Black Dragon is an easy monster to practice bossing on."),
	DISASTER("The King Black Dragon is an easy monster to practice bossing on."),
	ISLAND("The King Black Dragon is an easy monster to practice bossing on."),
	BLOOD("The King Black Dragon is an easy monster to practice bossing on."),
	NOMAND("The King Black Dragon is an easy monster to practice bossing on."),
	ZOMBIE("The King Black Dragon is an easy monster to practice bossing on."),
	WARRIORSGUILD("The King Black Dragon is an easy monster to practice bossing on."),

//SKILLING TELEPORTER
	SLAYER("The King Black Dragon is an easy monster to practice bossing on."),
	MINING("The King Black Dragon is an easy monster to practice bossing on."),
	ESSENCE("The King Black Dragon is an easy monster to practice bossing on."),
	THIEVING("The King Black Dragon is an easy monster to practice bossing on."),
	HERBLORE("The King Black Dragon is an easy monster to practice bossing on."),
	RUNECRAFTING("The King Black Dragon is an easy monster to practice bossing on."),
	CRAFTING("The King Black Dragon is an easy monster to practice bossing on."),
	AGILITY1("The King Black Dragon is an easy monster to practice bossing on."),
	AGILITY2("The King Black Dragon is an easy monster to practice bossing on."),
	AGILITY3("The King Black Dragon is an easy monster to practice bossing on."),
	FISHANDCOOK("The King Black Dragon is an easy monster to practice bossing on."),
	FLETCHING("The King Black Dragon is an easy monster to practice bossing on."),
	FIREMAKING("The King Black Dragon is an easy monster to practice bossing on."),
	WOODCUTING1("The King Black Dragon is an easy monster to practice bossing on."),
	WOODCUTING2("The King Black Dragon is an easy monster to practice bossing on."),
	HUNTER("The King Black Dragon is an easy monster to practice bossing on."),
	HUNTER1("The King Black Dragon is an easy monster to practice bossing on."),
	FARMING("The King Black Dragon is an easy monster to practice bossing on."),
	CONSTRUCTION("The King Black Dragon is an easy monster to practice bossing on."),
	SUMMONING("The King Black Dragon is an easy monster to practice bossing on."),
	DUNGEONEERING("The King Black Dragon is an easy monster to practice bossing on."),

//Donator Teleporter
	REGULARDONATOR("The King Black Dragon is an easy monster to practice bossing on."),
	SUPERDONATOR("The King Black Dragon is an easy monster to practice bossing on."),
	EXTRAMEDONATOR("The King Black Dragon is an easy monster to practice bossing on.");

	TeleportDescription(String description) {
		this.description = description;
	}
	
	private String description;
	
	public String getDescription() {
		return this.description;
	}
	
	public void setDescription(String description) {
		
	}

}
