package com.varrock.world.content.combat.strategy.impl;

import java.util.HashMap;

import com.varrock.engine.task.Task;
import com.varrock.engine.task.TaskManager;
import com.varrock.model.*;
import com.varrock.model.Locations.Location;
import com.varrock.model.projectile.Projectile;
import com.varrock.util.Misc;
import com.varrock.world.World;
import com.varrock.world.content.combat.CombatContainer;
import com.varrock.world.content.combat.CombatHitTask;
import com.varrock.world.content.combat.CombatType;
import com.varrock.world.content.combat.strategy.CombatStrategy;
import com.varrock.world.content.dialogue.DialogueManager;
import com.varrock.world.content.transportation.TeleportHandler;
import com.varrock.world.entity.impl.GameCharacter;
import com.varrock.world.entity.impl.npc.NPC;
import com.varrock.world.entity.impl.player.Player;

public class CorporealBeast implements CombatStrategy {

	private static final Animation attack_anim = new Animation(10496);
	private static final Animation attack_anim2 = new Animation(10410);
	private static final Graphic attack_graphic = new Graphic(1834);

	private static HashMap<String, NPC> CORPOREAL_BEASTS = new HashMap<>();

	@Override
	public boolean canAttack(GameCharacter entity, GameCharacter victim) {
		return victim.isPlayer();
	}

	@Override
	public CombatContainer attack(GameCharacter entity, GameCharacter victim) {
		return null;
	}

	@Override
	public boolean customContainerAttack(GameCharacter entity, GameCharacter victim) {
		NPC cB = (NPC)entity;
		if(cB.isChargingAttack() || cB.getConstitution() <= 0) {
			return true;
		}
		Player target = (Player)victim;
		boolean stomp = false;
		for (Player t : Misc.getCombinedPlayerList(target)) {
			if(t == null || t.getLocation() != Location.CORPOREAL_BEAST)
				continue;
			if (Locations.goodDistance(t.getPosition(), cB.getPosition(), 1)) {
				stomp = true;
				cB.getCombatBuilder().setVictim(t);
				new CombatHitTask(cB.getCombatBuilder(), new CombatContainer(cB, t, 1, CombatType.MAGIC, true)).handleAttack();
			}
		}
		if (stomp) {
			cB.performAnimation(attack_anim);
			cB.performGraphic(attack_graphic);
		}

		int attackStyle = Misc.getRandom(4);
		if (attackStyle == 0 || attackStyle == 1) { // melee
			int distanceX = target.getPosition().getX() - cB.getPosition().getX();
			int distanceY = target.getPosition().getY() - cB.getPosition().getY();
			if (distanceX > 4 || distanceX < -1 || distanceY > 4 || distanceY < -1)
				attackStyle = 4;
			else {

				cB.performAnimation(new Animation(attackStyle == 0 ? 10057 : 10058));
				if(target.getLocation() == Location.CORPOREAL_BEAST)
					cB.getCombatBuilder().setContainer(new CombatContainer(cB, target, 1, 1, CombatType.MELEE, true));
				return true;
			}
		} else if (attackStyle == 2) { // powerfull mage spiky ball
			cB.performAnimation(attack_anim2);
			cB.getCombatBuilder().setContainer(new CombatContainer(cB, target, 1, 2, CombatType.MAGIC, true));
			new Projectile(cB, target, 1825, 44, 3, 43, 43, 0).sendProjectile();
		} else if (attackStyle == 3) { // translucent ball of energy
			cB.performAnimation(attack_anim2);
			if(target.getLocation() == Location.CORPOREAL_BEAST)
				cB.getCombatBuilder().setContainer(new CombatContainer(cB, target, 1, 2, CombatType.MAGIC, true));
			new Projectile(cB, target, 1823, 44, 3, 43, 43, 0).sendProjectile();
			TaskManager.submit(new Task(1, target, false) {
				@Override
				public void execute() {
					int skill = Misc.getRandom(4);
					Skill skillT = Skill.forId(skill);
					Player player = (Player) target;
					int lvl = player.getSkillManager().getCurrentLevel(skillT);
					lvl -= 1 + Misc.getRandom(4);
					player.getSkillManager().setCurrentLevel(skillT, player.getSkillManager().getCurrentLevel(skillT) - lvl <= 0 ?  1 : lvl);
					target.getPacketSender().sendMessage("Your " + skillT.getFormatName() +" has been slighly drained!");
					stop();
				}
			});
		}
		if(attackStyle == 4) {
			cB.performAnimation(attack_anim2);
			for (Player t : Misc.getCombinedPlayerList(target)) {
				if(t == null || t.getLocation() != Location.CORPOREAL_BEAST)
					continue;
				new Projectile(cB, target, 1824, 44, 3, 43, 43, 0).sendProjectile();
			}
			TaskManager.submit(new Task(1, target, false) {
				@Override
				public void execute() {
					for (Player t : Misc.getCombinedPlayerList(target)) {
						if(t == null || t.getLocation() != Location.CORPOREAL_BEAST)
							continue;
						cB.getCombatBuilder().setVictim(t);
						new CombatHitTask(cB.getCombatBuilder(), new CombatContainer(cB, t, 1, CombatType.RANGED, true)).handleAttack();
					}
					stop();
				}
			});
		}
		return true;
	}

    /*
     * Handles creating the instance for the player
     */
    public static void createInstance(Player player) {
        removeInstancesFull(player);
        player.getPacketSender().sendInterfaceRemoval();
        if (player.getLocation().equals(Location.CORPOREAL_BEAST)) {
            player.moveTo(new Position(2886, 4376, player.getIndex() * 4));
            player.setPlayerInstanced(true);
            instanceNpcs(player);
        } else if (TeleportHandler.teleportPlayer(player, new Position(2886, 4376, player.getIndex() * 4), player.getSpellbook().getTeleportType())) {
            player.setPlayerInstanced(true);
            instanceNpcs(player);
        }
    }

    /*
     * Handles creating the instanced npcs
     */
    public static void instanceNpcs(Player player) {
		NPC corporealBeast = CORPOREAL_BEASTS.get(player.getName());

    	if(corporealBeast == null || corporealBeast.getConstitution() <= 0 || !corporealBeast.isRegistered()) {
			corporealBeast = NPC.of(8133, new Position(2902, 4394, player.getIndex() * 4)).setSpawnedFor(player);
			CORPOREAL_BEASTS.put(player.getName(),corporealBeast);
			World.register(corporealBeast);
		}
    }

    /*
     * Handles removing the instanced npcs fully when leaving the area
     */
    public static void removeInstancesFull(Player player) {
        World.getNpcs().forEach(n -> n.removeInstancedNpcs(Locations.Location.CORPOREAL_BEAST,  player.getIndex() * 4));
        player.setPlayerInstanced(false);
    }
    /*
     * Handles removing the instances temporarily when staying in the instance like
     * logging in/out of the game
     */
    public static void removeInstancesTemp(Player player) {
        World.getNpcs().forEach(n -> n.removeInstancedNpcs(Location.CORPOREAL_BEAST,  player.getIndex() * 4));
        player.setPlayerInstanced(true);
    }

    /*
     * Start multi area
     */
    public static void startMulti(Player player) {
        TeleportHandler.teleportPlayer(player, new Position(2886, 4376, 0), player.getSpellbook().getTeleportType());
    }

    public static void getDialogue(Player player) {
        player.getPacketSender().sendInterfaceRemoval();
        DialogueManager.start(player, 200);
        player.setDialogueActionId(202);
    }

	@Override
	public int attackDelay(GameCharacter entity) {
		return entity.getAttackSpeed();
	}

	@Override
	public int attackDistance(GameCharacter entity) {
		return 8;
	}

	@Override
	public CombatType getCombatType() {
		return CombatType.MIXED;
	}
}
