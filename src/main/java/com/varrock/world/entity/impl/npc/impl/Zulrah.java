package com.varrock.world.entity.impl.npc.impl;

import java.lang.reflect.InvocationTargetException;

import com.varrock.engine.task.Task;
import com.varrock.engine.task.TaskManager;
import com.varrock.model.Flag;
import com.varrock.model.Position;
import com.varrock.world.content.combat.strategy.zulrah.ZulrahConstants;
import com.varrock.world.content.combat.strategy.zulrah.ZulrahPhase;
import com.varrock.world.entity.impl.GameCharacter;
import com.varrock.world.entity.impl.npc.NPC;
import com.varrock.world.entity.impl.player.Player;

public class Zulrah extends NPC {
	
	private boolean idle;
	private int phase;
	private Player player;
	private long time;
	
	public Zulrah(int id, Position position) {
		super(id, position);
		time = System.currentTimeMillis();
	}
	
	@Override
	public void sequence() {
		super.sequence();
		tick();
	}
	
	private void tick() {
		if (getPlayer() != null) {
			getCombatBuilder().attack(getPlayer());
		}
	}

	public Player getPlayer() {
		return player;
	}
	
	public void setPlayer(Player player) {
		this.player = player;
	}
	
	public boolean isIdle() {
		return idle;
	}
	
	public void setIdle(boolean idle) {
		this.idle = idle;
	}
	
	public int getPhase() {
		return phase;
	}
	
	public void setPhase(int phase) {
		this.phase = phase;
	}
	
	public long getTime() {
		return time;
	}
	
	@Override
	public void dropItems(Player killer) {
		super.dropItems(killer);
		killer.addBossPoints(1);
	}
	
	public void spawn(int phaseID) {
		setPhase(phaseID);
		performAnimation(ZulrahConstants.RISE);
		setEntityInteraction(player);
		getCombatBuilder().setVictim(player);
		getCombatBuilder().attack(player);
		
		TaskManager.submit(new Task(2) {

			@Override
			protected void execute() {
				setTakeDamage(true);
				stop();
			}
			
		});
	}
	
	public void despawn(GameCharacter entity, Player player) {
		setIdle(true);
		
		int next = ZulrahConstants.zulrahPhases.get(getPhase()).getNextPhase();
		
		TaskManager.submit(new Task(1, true) {
			int tick;
			@Override
			public void execute() {
				if(tick == 0) {
					performAnimation(ZulrahConstants.DIVE);
					player.getPacketSender().sendMessage("Zulrah dives into the swamp...");
					setTakeDamage(false);
					player.getCombatBuilder().reset(true);
				}
				if (tick == 2) {
					moveTo(new Position(ZulrahConstants.zulrahPhases.get(next).getZulrahX(), ZulrahConstants.zulrahPhases.get(next).getZulrahY(), player.getIndex() * 4), true);					
				}
				if(tick == 3) {
					try {
						setIdle(false);
						
						nextPhase(player, entity.getConstitution(), ZulrahConstants.zulrahPhases.get(getPhase()).getNextPhase());

					} catch (NoSuchMethodException | SecurityException | IllegalAccessException
							| IllegalArgumentException | InvocationTargetException e) {
						e.printStackTrace();
						player.getPA().sendMessage("ZULRAH DIPPED");
					}
					stop();
				}
				tick++;
			} 
		});
	}
	
	public void nextPhase(Player player, int zulrahConstitution, int phaseID) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		ZulrahPhase phase = ZulrahConstants.zulrahPhases.get(phaseID);
		
		setTransformationId(phase.getTransformId());
		
		getUpdateFlag().flag(Flag.TRANSFORM);
		
		spawn(phaseID);
	}

}