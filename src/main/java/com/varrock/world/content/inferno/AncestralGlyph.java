package com.varrock.world.content.inferno;

import com.varrock.model.Direction;
import com.varrock.model.Position;
import com.varrock.world.entity.impl.npc.NPC;
import com.varrock.world.entity.impl.player.Player;

public class AncestralGlyph extends NPC {
	
	private static final int INDEX = 22707;
	
	private static final int width = 3, height = 3;
	
	private boolean direction, up, inited;
	
	int ready = 5;
	
	private Position base, line;

	protected AncestralGlyph(Position position, boolean direction, boolean up, int dir) {
		super(INDEX, position);
		this.line = position.transform(0, -2, 0);
		setDirection(Direction.NORTH);
        getMovementQueue().reset();
        setPosition(position);
		getMovementQueue().walkStep(0,-2);
	}
	
    @Override
    public void sequence() {
        super.sequence();
        onTick();
    }

	private void onTick() {
		if (!getPosition().equals(line) && !inited)  {
			return;
		}
		inited = true;
		if (ready == 0) {
			Position dest = line.transform(this.up ? 0 : direction ? -13 : 13, 0, 0);
            if (dest.equals(getPosition())) {
                direction = !direction;
            } else {
        		getMovementQueue().walkStep(dest.getX() -getPosition().getX(), dest.getY() - getPosition().getY());
            }
		} else {
			ready--;
		}
	}

	public boolean isProtected(Player target) {
		return Math.abs(target.getPosition().getX() - getCentrePosition().getX()) < 4;
	}

	@Override
	public Position getCentrePosition() {
		return getPosition().transform(1, 0, 0);
	}
	
	

}
