package com.zyrox.world.content.combat.strategy.zulrah.npc;

import com.zyrox.engine.task.Task;
import com.zyrox.engine.task.TaskManager;
import com.zyrox.model.Animation;
import com.zyrox.model.GameObject;
import com.zyrox.model.Position;
import com.zyrox.model.projectile.Projectile;
import com.zyrox.util.Misc;
import com.zyrox.world.World;
import com.zyrox.world.content.CustomObjects;
import com.zyrox.world.content.combat.CombatContainer;
import com.zyrox.world.content.combat.CombatHitTask;
import com.zyrox.world.content.combat.CombatType;
import com.zyrox.world.content.combat.strategy.CombatStrategy;
import com.zyrox.world.content.combat.strategy.zulrah.ZulrahConstants;
import com.zyrox.world.entity.impl.GameCharacter;
import com.zyrox.world.entity.impl.npc.NPC;
import com.zyrox.world.entity.impl.npc.impl.Zulrah;
import com.zyrox.world.entity.impl.player.Player;

public class Green implements CombatStrategy {
	
	private static NPC SNAKELING1;
	private static NPC SNAKELING2;
	private static NPC CLOUD1;
	private static NPC CLOUD2;
	private static NPC CLOUD3;
	private static NPC CLOUD4;
	private static NPC CLOUD5;
	private static NPC CLOUD6;
	private static NPC CLOUD7;
	private static NPC CLOUD8;
	
	private static NPC[] CloudTiles = {CLOUD1, CLOUD2, CLOUD3, CLOUD4, CLOUD5, CLOUD6, CLOUD7, CLOUD8};
	
	public static void spawnCloudTiles(GameCharacter victim) {
		for(int i = 0; i < 8; i++) {
			CloudTiles[i] = NPC.of(0, new Position(ZulrahConstants.TOXIC_FUME_LOCATIONS_1[i][0], ZulrahConstants.TOXIC_FUME_LOCATIONS_1[i][1], victim.getIndex() * 4));
			World.register(CloudTiles[i]);
		}
	}
	public static void despawnCloudTiles(Player player) { 
		for(int i = 0; i < 8; i++) {
			World.deregister(CloudTiles[i]);
		}
	}
	
	@Override
	public boolean canAttack(GameCharacter entity, GameCharacter victim) {
		return true;
	}

	@Override
	public CombatContainer attack(GameCharacter entity, GameCharacter victim) {
		return null;
	}

	@Override
	public boolean customContainerAttack(GameCharacter entity, GameCharacter victim) {
		if (((Zulrah) entity).isIdle()) {
			return true;
		}
		
		switch(((Zulrah) entity).getPhase()) {
			case 1:
				if(victim.isPlayer()) {
					greenZulrahFullCloudSpawn(entity, victim);
				}
				break;
			case 4:
			case 7:
				greenZulrahRightCloudSpawn(entity, victim);
				break;
			case 8:
				if(victim.isPlayer()) {
					rangeMage(entity, victim);
				}
				break;
		}
		return true; 
	}

	@Override
	public int attackDelay(GameCharacter entity) {
		return 7;
	}

	@Override
	public int attackDistance(GameCharacter entity) {
		return 30;
	}

	@Override
	public CombatType getCombatType() {
		return null;
	}
	
	public static void greenZulrahFullCloudSpawn(GameCharacter entity, GameCharacter victim) {
		((Zulrah) entity).setIdle(true);
		spawnCloudTiles(victim);
		TaskManager.submit(new Task(1, true) {
			int tick;
			int cloud = 1;
			
			@Override
			public void execute() {
				if(entity.getConstitution() < 1 || entity == null) {
					stop();
				}
				Player player = (Player) victim;
				player.setCloudsSpawned(true);
				for(int i = 1; i < 4; i++) {
					if(tick == i*4) {
						entity.getCombatBuilder().attack(CloudTiles[cloud]);
					}
					
					if(tick == i*4+2){
						entity.performAnimation(new Animation(5069));
						new Projectile(entity, CloudTiles[cloud], 1045, 44, 1, 43, 0, 0).sendProjectile();
						new Projectile(entity, CloudTiles[cloud+1], 1045, 44, 1, 43, 0, 0).sendProjectile();
					}
					
					if(tick == i*4+3){
						CustomObjects.zulrahToxicClouds(new GameObject(11700, CloudTiles[cloud].getPosition()), (Player) victim, 40);
						CustomObjects.zulrahToxicClouds(new GameObject(11700, CloudTiles[cloud+1].getPosition()), (Player) victim, 40);
						cloud = cloud + 2;
					} 
					if(tick == 16) {
						entity.getCombatBuilder().attack(CloudTiles[4]);
					}
				}
				if(tick == 17) {
					((Zulrah) entity).setIdle(false);
					
					((Zulrah) entity).despawn(entity, (Player) victim);
					
					stop();
				}
				tick++;
			}
		});
	}
	
	public static void greenZulrahRightCloudSpawn(GameCharacter entity, GameCharacter victim) {
		((Zulrah) entity).setIdle(true);
		spawnCloudTiles(victim);
		Player player = (Player) victim;
		player.setCloudsSpawned(true);
		TaskManager.submit(new Task(1, true) {
			int tick;
			int cloud = 3;
			
			@Override
			public void execute() {
				if(entity.getConstitution() < 1 || entity == null) {
					stop();
				}
				for(int i = 1; i < 3; i++) {
					if(tick == i*4) {
						entity.getCombatBuilder().attack(CloudTiles[cloud]);
					}
					
					if(tick == i*4+2){
						entity.performAnimation(new Animation(5069));
						new Projectile(entity, CloudTiles[cloud], 1045, 44, 1, 43, 0, 0).sendProjectile();
						new Projectile(entity, CloudTiles[cloud+1], 1045, 44, 1, 43, 0, 0).sendProjectile();
					}
					
					if(tick == i*4+3){
						if(victim.isNpc()) {
							
						} else {
							CustomObjects.zulrahToxicClouds(new GameObject(11700, CloudTiles[cloud].getPosition()), (Player) victim, 40);
							CustomObjects.zulrahToxicClouds(new GameObject(11700, CloudTiles[cloud+1].getPosition()), (Player) victim, 40);
							cloud = cloud + 2;
						}
						
					} 
					if(tick == 16) {
						entity.getCombatBuilder().attack(CloudTiles[4]);
					}
				}
				if(tick == 17) {
					((Zulrah) entity).setIdle(false);
					if (victim.isPlayer()) {
						((Zulrah) entity).despawn(entity, (Player) victim);
					}
					stop();
				}
				tick++;
			}
		});
	}
	
	public static void snakelingAndClouds(GameCharacter entity, GameCharacter victim) {
		((Zulrah) entity).setIdle(true);
		Position position1 = new Position(victim.getPosition().getX(),victim.getPosition().getY()+1, victim.getIndex() * 4);
		Position position2 = new Position(victim.getPosition().getX(),victim.getPosition().getY()-1, victim.getIndex() * 4);
		CloudTiles[1] = NPC.of(0, position1);
		CloudTiles[2] = NPC.of(0, position2);
		World.register(CloudTiles[1]);
		World.register(CloudTiles[2]);
		TaskManager.submit(new Task(1, true) {
			int tick;
			
			@Override
			public void execute() {
				if(entity.getConstitution() < 1 || entity == null) {
					stop();
				}
				if(tick == 3) {
					entity.performAnimation(new Animation(5069));
					new Projectile(entity, CloudTiles[1], 1044, 44, 1, 43, 43, 0).sendProjectile();
					new Projectile(entity, CloudTiles[2], 1044, 44, 1, 43, 43, 0).sendProjectile();
					World.deregister(CloudTiles[1]);
					World.deregister(CloudTiles[2]);
				} else if(tick == 4) {
					SNAKELING1 = NPC.of(2045, position1);
					SNAKELING2 = NPC.of(2045, position2);
					World.register(SNAKELING1);
					World.register(SNAKELING2);
				} else if(tick == 6) {
					greenZulrahRightCloudSpawn(entity, victim);
					stop();
				}
				tick++;
			}
		});
	}
	
	private void rangeMage(GameCharacter entity, GameCharacter victim) {
		int rand = Misc.getRandom(12);
		if(rand != 9 && rand != 4 && rand != 5 && rand != 6 && rand != 7) {
			TaskManager.submit(new Task(2, entity, true) {
				int tick;
				@Override
				public void execute() {
					if(tick == 0) {
						entity.performAnimation(new Animation(5069));
						new Projectile(entity, victim, 1044, 44, 1, 43, 43, 0).sendProjectile();
					} 
					if(tick == 1) {
						new CombatHitTask(entity.getCombatBuilder(), new CombatContainer(entity, victim, 1, CombatType.RANGED, true)).handleAttack();
						stop();
					}
					tick++;
				}
			});
		} else if(rand == 4 || rand == 5 || rand == 6 || rand == 7) {
			TaskManager.submit(new Task(2, entity, true) {
				int tick;
				@Override
				public void execute() {
					if(tick == 0) {
						entity.performAnimation(new Animation(5069));
						new Projectile(entity, victim, 1046, 44, 1, 43, 43, 0).sendProjectile();
					}
					if(tick == 1) {
						new CombatHitTask(entity.getCombatBuilder(), new CombatContainer(entity, victim, 1, CombatType.MAGIC, true)).handleAttack();
						stop();
					}
					tick++;
				}
			});
		} else {
			if(victim.isNpc()) {
				return;
			}
			((Zulrah) entity).despawn(entity, (Player) victim);
			
		}
	}

}

