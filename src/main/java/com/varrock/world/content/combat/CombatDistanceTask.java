package com.varrock.world.content.combat;

import com.varrock.engine.task.Task;
import com.varrock.engine.task.TaskManager;
import com.varrock.model.Locations;
import com.varrock.model.Locations.Location;
import com.varrock.world.entity.impl.GameCharacter;

public class CombatDistanceTask extends Task {

	/** The combat builder. */
	private CombatBuilder builder;

	/** The victim being hunted. */
	private GameCharacter victim;

	/**
	 * Create a new {@link CombatDistanceTask}.
	 * 
	 * @param builder
	 *            the combat builder.
	 * @param victim
	 *            the victim being hunted.
	 */
	public CombatDistanceTask(CombatBuilder builder, GameCharacter victim) {
		super(1, builder, true);
		this.builder = builder;
		this.victim = victim;
	}

	@Override
	public void execute() {
		
		builder.determineStrategy();
		builder.attackTimer = 0;
		builder.cooldown = 0;

		if(builder.getVictim() != null && !builder.getVictim().equals(victim)) {
			builder.reset(true);
			this.stop();
			return;
		}
		
		if(!Location.ignoreFollowDistance(builder.getCharacter())) {
			if (!builder.getCharacter().getPosition().isViewableFrom(victim.getPosition())) {
				builder.reset(true);
				this.stop();
				return;
			}
		}

		if(builder.getStrategy() != null) {
			int distance = builder.getStrategy().attackDistance(builder.getCharacter());
			if (victim.isNpc() && ((victim.getAsNpc().getId() == 22555 || victim.getAsNpc().getId() == 22553) && victim.getPosition().getX() <= 3231)) {
				distance = 7;
			}
			if (builder.getCharacter().getRegionID() == 9043 || Locations.goodDistance(builder.getCharacter().getPosition(), victim.getPosition(), distance)) {
				successful();
				this.stop();
				return;
			}
		}
	}
	
	@Override
	public void stop() {
		setEventRunning(false);
		builder.setDistanceTask(null);
	}
	
	public void successful() {
		builder.getCharacter().getMovementQueue().reset();
		builder.setVictim(victim);
		if (builder.getCombatTask() == null || !builder.getCombatTask().isRunning()) {
            builder.setCombatTask(new CombatHookTask(builder));
            TaskManager.submit(builder.getCombatTask());
        } else {
        	builder.getCombatTask().stop();
        	successful();
        }
	}
}
