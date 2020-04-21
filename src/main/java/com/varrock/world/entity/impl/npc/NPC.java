package com.varrock.world.entity.impl.npc;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.CopyOnWriteArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.varrock.GameSettings;
import com.varrock.engine.task.TaskManager;
import com.varrock.engine.task.impl.NPCDeathTask;
import com.varrock.model.Direction;
import com.varrock.model.Position;
import com.varrock.model.Locations.Location;
import com.varrock.model.definitions.NPCDrops;
import com.varrock.model.definitions.NpcDefinition;
import com.varrock.util.JsonLoader;
import com.varrock.world.World;
import com.varrock.world.content.*;
import com.varrock.world.content.combat.CombatFactory;
import com.varrock.world.content.combat.CombatType;
import com.varrock.world.content.combat.effect.CombatPoisonEffect.PoisonType;
import com.varrock.world.content.combat.effect.CombatVenomEffect.VenomType;
import com.varrock.world.content.combat.strategy.CombatStrategies;
import com.varrock.world.content.combat.strategy.CombatStrategy;
import com.varrock.world.content.combat.strategy.impl.GalvekCombatStrategy;
import com.varrock.world.content.combat.strategy.impl.KalphiteQueen;
import com.varrock.world.content.combat.strategy.impl.Nex;
import com.varrock.world.content.greatolm.OlmAnimations;
import com.varrock.world.content.greatolm.RaidsParty;
import com.varrock.world.content.skill.impl.hunter.Hunter;
import com.varrock.world.content.skill.impl.hunter.PuroPuro;
import com.varrock.world.content.skill.impl.runecrafting.DesoSpan;
import com.varrock.world.entity.impl.GameCharacter;
import com.varrock.world.entity.impl.npc.NPCMovementCoordinator.Coordinator;
import com.varrock.world.entity.impl.npc.click_type.NpcClickType;
import com.varrock.world.entity.impl.npc.impl.AncientWyvern;
import com.varrock.world.entity.impl.npc.impl.GreatOlm;
import com.varrock.world.entity.impl.npc.impl.KingKurask;
import com.varrock.world.entity.impl.npc.impl.MaxHitDummy;
import com.varrock.world.entity.impl.npc.impl.Tekton;
import com.varrock.world.entity.impl.npc.impl.Vorkath;
import com.varrock.world.entity.impl.npc.impl.Warmonger;
import com.varrock.world.entity.impl.npc.impl.Zulrah;
import com.varrock.world.entity.impl.npc.impl.summoning.Familiar;
import com.varrock.world.entity.impl.npc.impl.summoning.Pet;
import com.varrock.world.entity.impl.npc.impl.summoning.SuperiorOlmlet;
import com.varrock.world.entity.impl.player.Player;


/**
 * Represents a non-playable character, which players can interact with.
 * @author Gabriel Hannason
 */

public class NPC extends GameCharacter {

	public NPC(int id, Position position) {
		super(position);
		NpcDefinition definition = NpcDefinition.forId(id);
		if(definition == null)
			throw new NullPointerException("NPC "+id+" is not defined!");
		this.defaultPosition = position;
		this.id = id;
		this.definition = definition;
		this.defaultConstitution = definition.getHitpoints() < 100 ? 100 : definition.getHitpoints();
		this.setConstitution(defaultConstitution);
		this.maxHitDummy = false;

		if(position != null)
			setLocation(Location.getLocation(this));
	}

	/**
	 * Creates a custom non-playable character implementation if possible for
	 * the given type, or null if one cannot be created.
	 * <p>
	 *
	 * @param index the index in the list of the npc.
	 * @param type the unique type of the npc.
	 * @return the custom npc, or a default new npc.
	 */
	public static NPC createCustomOrDefault(int npcId, Position position) {
		NPC custom = NPCMap.getSingleton().createCopyOrNull(npcId, position);

		if (NpcDefinition.getDefinitions()[npcId] == null) {
			System.out.println("Nulled npc spawned: " + npcId);
		}
		if (custom != null && custom.getIndex() != -1) {
			return custom;
		}
		return new NPC(npcId, position);
	}

	/**
	 * Returns a new instance of a NPC.
	 * @param id
	 * @param position
	 * @return
	 */
	public static NPC of(int id, Position position) {
		switch (id) {
			case 2043:
				return new Zulrah(id, position);
			case SuperiorOlmlet.ID:
				return new SuperiorOlmlet(id, position);
			case 23060:
				return new Vorkath(id, position);
			case 22405:
				return new KingKurask(id, position);
			case AncientWyvern.ANCIENT_WYVERN:
				return new AncientWyvern(id, position);
			case 22553:
			case 22554:
			case 22555:
				return new GreatOlm(id, position);
			case 22413:
			case 17668:
			case 3975:
				return new MaxHitDummy(id, position);
			case Wildywyrm.NPC_ID:
				return new Wildywyrm(position);
			case Dracula.NPC_ID:
				return new Dracula(position);
			case 23095:
			case 23096:
			case 23097:
			case 23098:
				int stageOrdinal = (id - GalvekCombatStrategy.Stage.ORANGE.getId()) + 1;
				GalvekCombatStrategy.Stage newStage = GalvekCombatStrategy.Stage.values()[stageOrdinal];
				return new Galvek(newStage, position);
			case Warmonger.NPC_ID:
				return new Warmonger(position);
			case Tekton.NPC_ID:
				return new Tekton(position);
		}
		return createCustomOrDefault(id, position);
	}

	public boolean maxHitDummy;

	public void sequence() {

	}

	@Override
	public void appendDeath() {
		if(!isDying && !isFamiliar()) {
			TaskManager.submit(new NPCDeathTask(this));
			isDying = true;
		}
	}

	@Override
	public int getConstitution() {
		return constitution;
	}

	@Override
	public NPC setConstitution(int constitution) {
		this.constitution = constitution;
		if(this.constitution <= 0)
			appendDeath();
		
		return this;
	}

	@Override
	public void heal(int heal) {
		if ((this.getConstitution() + heal) > getDefaultConstitution()) {
			setConstitution(getDefaultConstitution());
			return;
		}
		setConstitution(this.getConstitution() + heal);
	}


	@Override
	public int getBaseAttack(CombatType type) {
		return getDefinition().getAttackBonus();
	}

	@Override
	public int getAttackSpeed() {
		return this.getDefinition().getAttackSpeed();
	}


	@Override
	public int getBaseDefence(CombatType type) {

		if (type == CombatType.MAGIC)
			return getDefinition().getDefenceMage();
		else if (type == CombatType.RANGED)
			return getDefinition().getDefenceRange();

		return getDefinition().getDefenceMelee();
	}

	@Override
	public boolean isNpc() {
		return true;
	}

	@Override
	public boolean equals(Object other) {
		return other instanceof NPC && ((NPC)other).getIndex() == getIndex();
	}

	@Override
	public int getSize() {
		return getDefinition().getSize();
	}

	@Override
	public void poisonVictim(GameCharacter victim, CombatType type) {
		if (getDefinition().isPoisonous()) {
			CombatFactory.poisonEntity(
					victim,
					type == CombatType.RANGED || type == CombatType.MAGIC ? PoisonType.MILD
							: PoisonType.EXTRA);
		}

	}
	
    @Override
    public void venomVictim(GameCharacter victim, CombatType type) {
        if (getDefinition().isVenomous()) {
            CombatFactory.venomEntity(victim, VenomType.SUPER);
        }

    }

	/**
	 * Prepares the dynamic json loader for loading world npcs.
	 * 
	 * @return the dynamic json loader.
	 * @throws Exception
	 *             if any errors occur while preparing for load.
	 */
	public static void init() {
		new JsonLoader() {
			@Override
			public void load(JsonObject reader, Gson builder) {

				int id = reader.get("npc-id").getAsInt();
				Position position = builder.fromJson(reader.get("position").getAsJsonObject(), Position.class);
				Coordinator coordinator = builder.fromJson(reader.get("walking-policy").getAsJsonObject(), Coordinator.class);
				Direction direction = Direction.valueOf(reader.get("face").getAsString());
				NPC npc = NPC.of(id, position);
				npc.movementCoordinator.setCoordinator(coordinator);
				npc.setDirection(direction);
				
				World.register(npc);
				if(id > 5070 && id < 5081) {
					Hunter.HUNTER_NPC_LIST.add(npc);
				}
				position = null;
				coordinator = null;
				direction = null;
			}
			

			@Override
			public String filePath() {
				return "./data/def/json/world_npcs.json";
			}
		}.load();

		Nex.globalNex.spawn(0);
		PuroPuro.spawn();
		DesoSpan.spawn();

		KalphiteQueen.spawn(1158, new Position(3485, 9509));
	}

	public static void writeNpc(int npcId, Position position, boolean walking) {
		Gson builder = new GsonBuilder().setPrettyPrinting().create();
		JsonObject object = new JsonObject();

		try {
			String directory = "./data/def/json/spawn_npcs.json";

			object.addProperty("npc-id", npcId);
			object.add("position", builder.toJsonTree(position));
			object.add("walking-policy", builder.toJsonTree(new Coordinator(walking, walking ? 2 : -1)));
			object.addProperty("face", Direction.SOUTH.toString());

			String json = builder.toJson(object);

			FileWriter file = new FileWriter(directory,true);
			file.write(",\n");
			file.write(json);
			file.flush();
			file.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	public CombatStrategy determineStrategy() {
		if (id == 2043 && getTransformationId() != -1) {
			return CombatStrategies.getStrategy(getTransformationId());
		}
		
		return CombatStrategies.getStrategy(id);
	}

	public boolean switchesVictim() {
		if(getLocation() == Location.DUNGEONEERING) {
			return true;
		}
		return id == 6263 || id == 6265 || id == 6203 || id == 6208 || id == 6206 || id == 6247 || id == 6250 || id == 3200 || id == 4540 || id == 1158 || id == 1160 || id == 8133 || id == 13447 || id == 13451 || id == 13452 || id == 13453 || id == 13454 || id == 2896 || id == 2882 || id == 2881 || id == 6260;
	}

	public int getAggressionDistance() {
		int distance = 7;
		
		/*switch(id) {
		}*/
		if(Nex.nexMob(id)) {
			distance = 60;
		} else if(id == 2896 || id == Tekton.NPC_ID) {
			distance = 50;
		}
		return distance;
	}

	/*
	 * Fields
	 */
	/** INSTANCES **/
	private final Position defaultPosition;
	private NPCMovementCoordinator movementCoordinator = new NPCMovementCoordinator(this);
	private Player spawnedFor;
	private NpcDefinition definition;

	/** INTS **/
	protected int id;
	protected int constitution = 100;
	private int defaultConstitution;
	private int transformationId = -1;

	/** BOOLEANS **/
	private boolean[] attackWeakened = new boolean[3], strengthWeakened = new boolean[3];
	private boolean summoningCombat;
	private boolean isDying;
	private boolean visible = true;
	private boolean healed, chargingAttack;
	private boolean findNewTarget;

	/**
	 * GORILLA
	 */
	public CombatType gorillaType;
	public int gorillaAttacks;
	public boolean gorillaSwitchAttacks;
	public boolean switchGorilla;
	public int gorillaDamageTaken;
	
	/*
	 * Getters and setters
	 */

	public int getId() {
		return id;
	}
	
	public Position getDefaultPosition() {
		return defaultPosition;
	}

	public int getDefaultConstitution() {
		return defaultConstitution;
	}

	public int getTransformationId() {
		return transformationId;
	}

	public void setTransformationId(int transformationId) {
		this.transformationId = transformationId;
	}

	public boolean isVisible() {
		return visible;
	}
	
	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public void setDying(boolean isDying) {
		this.isDying = isDying;
	}
	
	public void setDefaultConstitution(int defaultConstitution) {
		this.defaultConstitution = defaultConstitution;
	}

	/**
	 * @return the statsWeakened
	 */
	public boolean[] getDefenceWeakened() {
		return attackWeakened;
	}

	public boolean isFamiliar() {
		return this instanceof Familiar || this instanceof Pet;
	}

	public Familiar getAsFamiliar() {
		return (Familiar) this;
	}

	public boolean isDying() {
		return isDying;
	}

	/**
	 * @return the statsBadlyWeakened
	 */
	public boolean[] getStrengthWeakened() {
		return strengthWeakened;
	}

	public NPCMovementCoordinator getMovementCoordinator() {
		return movementCoordinator;
	}

	public NpcDefinition getDefinition() {
		boolean change = id == 2043;
		switch(id) {
		case 8615 + GameSettings.OSRS_NPC_OFFSET:
		case 8619 + GameSettings.OSRS_NPC_OFFSET:
		case 8620 + GameSettings.OSRS_NPC_OFFSET:
		case 8621 + GameSettings.OSRS_NPC_OFFSET:
			change = true;
			break;
		}
		if (change && getTransformationId() != -1 && definition != NpcDefinition.forId(getTransformationId())) {
			if (NpcDefinition.forId(getTransformationId()) != null) {
				definition = NpcDefinition.forId(getTransformationId());
			}
		}
		
		return definition;
	}

	public Player getSpawnedFor() {
		return spawnedFor;
	}

	public NPC setSpawnedFor(Player spawnedFor) {
		this.spawnedFor = spawnedFor;
		return this;
	}

	public boolean hasHealed() {
		return healed;
	}

	public void setHealed(boolean healed) {
		this.healed = healed;
	}

	public boolean isChargingAttack() {
		return chargingAttack;
	}

	public NPC setChargingAttack(boolean chargingAttack) {
		this.chargingAttack = chargingAttack;
		return this;
	}
	
	public boolean findNewTarget() {
		return findNewTarget;
	}
	
	public void setFindNewTarget(boolean findNewTarget) {
		this.findNewTarget = findNewTarget;
	}
	
	public boolean summoningCombat() {
		return summoningCombat;
	}
	
	public void setSummoningCombat(boolean summoningCombat) {
		this.summoningCombat = summoningCombat;
	}
	
	public void removeInstancedNpcs(Location loc, int height) {
		int checks = loc.getX().length - 1;
		for(int i = 0; i <= checks; i+=2) {
			if(getPosition().getX() >= loc.getX()[i] && getPosition().getX() <= loc.getX()[i + 1]) {
				if(getPosition().getY() >= loc.getY()[i] && getPosition().getY() <= loc.getY()[i + 1]) {
					if(getPosition().getZ() == height) {
						World.deregister(this);
					}
				}
			}
		}
	}
	
	public void countInstancedNpcs(Location loc, int height, int npcId) {
		int checks = loc.getX().length - 1;
		int ii = 0;
		for(int i = 0; i <= checks; i+=2) {
			if(getPosition().getX() >= loc.getX()[i] && getPosition().getX() <= loc.getX()[i + 1]) {
				if(getPosition().getY() >= loc.getY()[i] && getPosition().getY() <= loc.getY()[i + 1]) {
					if(getPosition().getZ() == height) {
						if(getId() == npcId) {
							ii++;
						}
					}
				}
			}
		}
	}

	private CopyOnWriteArrayList<NPC> vorkathSpawns = new CopyOnWriteArrayList<NPC>();

	public CopyOnWriteArrayList<NPC> getVorkathSpawns() {
		return vorkathSpawns;
	}


	/**
	 * RAIDS
	 */
	public Direction previousDirectionFacing;
	public Direction directionFacing;

	public void performGreatOlmAttack(RaidsParty party) {
		// party.getGreatOlmObject().performAnimation(OlmAnimations.shootMiddle);
		if (directionFacing != null) {
			if (party.getCurrentPhase() == 3) {
				if (getPosition().getX() >= 3238) {
					if (directionFacing.equals(Direction.SOUTH))
						party.getGreatOlmObject().performAnimation(OlmAnimations.shootLeftEnraged);
					else if (directionFacing.equals(Direction.NORTH))
						party.getGreatOlmObject().performAnimation(OlmAnimations.shootRightEnraged);
					else if (directionFacing.equals(Direction.NONE))
						party.getGreatOlmObject().performAnimation(OlmAnimations.shootMiddleEnraged);
				} else {
					if (directionFacing.equals(Direction.SOUTH))
						party.getGreatOlmObject().performAnimation(OlmAnimations.shootRightEnraged);
					else if (directionFacing.equals(Direction.NORTH))
						party.getGreatOlmObject().performAnimation(OlmAnimations.shootLeftEnraged);
					else if (directionFacing.equals(Direction.NONE))
						party.getGreatOlmObject().performAnimation(OlmAnimations.shootMiddleEnraged);
				}
			} else {
				if (getPosition().getX() >= 3238) {
					if (directionFacing.equals(Direction.SOUTH))
						party.getGreatOlmObject().performAnimation(OlmAnimations.shootLeft);
					else if (directionFacing.equals(Direction.NORTH))
						party.getGreatOlmObject().performAnimation(OlmAnimations.shootRight);
					else if (directionFacing.equals(Direction.NONE))
						party.getGreatOlmObject().performAnimation(OlmAnimations.shootMiddle);
				} else {
					if (directionFacing.equals(Direction.SOUTH))
						party.getGreatOlmObject().performAnimation(OlmAnimations.shootRight);
					else if (directionFacing.equals(Direction.NORTH))
						party.getGreatOlmObject().performAnimation(OlmAnimations.shootLeft);
					else if (directionFacing.equals(Direction.NONE))
						party.getGreatOlmObject().performAnimation(OlmAnimations.shootMiddle);
				}
			}
		}
	}

	/**
	 * Drop the monster's loot.
	 *
	 * @param killer
	 */
	public void dropItems(Player killer) {
		try {
			NPCDrops.dropItems(killer, this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Returns if the entity should clear its map on death.
	 * @return if clears
	 */
	public boolean clearDamageMapOnDeath() {
		return getId() != 3334 && getId() != 2009 && getId() != 12841 && getId() != 22542 && getId() != 21332 && getId() != 17669 && getId() != 17669 && getId() != 23095 && getId() != 23096 && getId() != 23097 && getId() != 23098;
	}

	/**
	 * Returns if the NPC is a hydra instance.
	 * @return if is hydra
	 */
	public boolean isHydra() {
		return false;
	}

	/**
	 * When a player clicks a npc, this will initiate
	 */
	public void clickNpc(Player player, NpcClickType npcClickType) {

	}

	@Override
	public Position getCentrePosition() {

		final Position centrePosition = getPosition().copy();

		NpcDefinition definition = NpcDefinition.getDefinitions()[getId()];

		if (definition != null && definition.getSize() >= 2) {
			centrePosition.add((int)Math.floor((double) definition.getSize() / 2D), (int)Math.floor((double) definition.getSize() / 2D));
		}

		return centrePosition;
	}

	public ArrayList<Player> getPossibleTargets() {
		ArrayList<Player> possibleTargets = new ArrayList<>();
		for(Player player : World.getPlayers()) {
			if(player == null) {
				continue;
			}
			if(this.getPosition().getDistance(player.getPosition()) <= 12 && this.getPosition().getZ() == player.getPosition().getZ()) {
				possibleTargets.add(player);
			}
		}
		return possibleTargets;
	}

	public Player getRandomTarget() {
		ArrayList<Player> possibleTargets = getPossibleTargets();

		if(possibleTargets.size() >= 1) {
			Collections.shuffle(possibleTargets);
			return possibleTargets.get(0);
		}

		return null;
	}

	public void walkToPosition(Position targetPosition) {
		Position myPosition = this.getPosition().copy();

		int x;
		int y;

		x = targetPosition.getX() - myPosition.getX();
		y = targetPosition.getY() - myPosition.getY();

		this.getMovementQueue().walkStep(x, y);
	}

	/**
	 * Creates a new instance of this npc with the given index.
	 *
	 * @param index
	 *            the new index of this npc.
	 * @return the new instance.
	 */
	public NPC copy(int npcId, Position position) {
		throw new UnsupportedOperationException("This function is not supported.");
	}

}
