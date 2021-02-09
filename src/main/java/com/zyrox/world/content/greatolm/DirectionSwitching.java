package com.zyrox.world.content.greatolm;

import com.zyrox.engine.task.Task;
import com.zyrox.engine.task.TaskManager;
import com.zyrox.model.Direction;

public class DirectionSwitching {
	
	public static void switchDirectionsWest(RaidsParty party) {
		if(party.getGreatOlmNpc().previousDirectionFacing == Direction.NORTH && party.getGreatOlmNpc().directionFacing == Direction.SOUTH) {
			party.getGreatOlmObject().performAnimation(OlmAnimations.leftToRight);
		}
		if(party.getGreatOlmNpc().previousDirectionFacing == Direction.NORTH && party.getGreatOlmNpc().directionFacing == Direction.NONE) {
			party.getGreatOlmObject().performAnimation(OlmAnimations.leftToMiddle);
		}
		if(party.getGreatOlmNpc().previousDirectionFacing == Direction.SOUTH && party.getGreatOlmNpc().directionFacing == Direction.NORTH) {
			party.getGreatOlmObject().performAnimation(OlmAnimations.rightToLeft);
		}
		if(party.getGreatOlmNpc().previousDirectionFacing == Direction.SOUTH && party.getGreatOlmNpc().directionFacing == Direction.NONE) {
			party.getGreatOlmObject().performAnimation(OlmAnimations.rightToMiddle);
		}
		if(party.getGreatOlmNpc().previousDirectionFacing == Direction.NONE && party.getGreatOlmNpc().directionFacing == Direction.NORTH) {
			party.getGreatOlmObject().performAnimation(OlmAnimations.middleToLeft);
		}
		if(party.getGreatOlmNpc().previousDirectionFacing == Direction.NONE && party.getGreatOlmNpc().directionFacing == Direction.SOUTH) {
			party.getGreatOlmObject().performAnimation(OlmAnimations.middleToRight);
		}
		TaskManager.submit(new Task(2, party, false) {
			@Override
			public void execute() {
				OlmAnimations.resetAnimation(party);
				stop();
			}
		});
	}
	
	public static void switchDirectionsEast(RaidsParty party) {
		if(party.getGreatOlmNpc().previousDirectionFacing == Direction.NORTH && party.getGreatOlmNpc().directionFacing == Direction.SOUTH) {
			party.getGreatOlmObject().performAnimation(OlmAnimations.rightToLeft);
		}
		if(party.getGreatOlmNpc().previousDirectionFacing == Direction.NORTH && party.getGreatOlmNpc().directionFacing == Direction.NONE) {
			party.getGreatOlmObject().performAnimation(OlmAnimations.rightToMiddle);
		}
		if(party.getGreatOlmNpc().previousDirectionFacing == Direction.SOUTH && party.getGreatOlmNpc().directionFacing == Direction.NORTH) {
			party.getGreatOlmObject().performAnimation(OlmAnimations.leftToRight);
		}
		if(party.getGreatOlmNpc().previousDirectionFacing == Direction.SOUTH && party.getGreatOlmNpc().directionFacing == Direction.NONE) {
			party.getGreatOlmObject().performAnimation(OlmAnimations.leftToMiddle);
		}
		if(party.getGreatOlmNpc().previousDirectionFacing == Direction.NONE && party.getGreatOlmNpc().directionFacing == Direction.NORTH) {
			party.getGreatOlmObject().performAnimation(OlmAnimations.middleToRight);
		}
		if(party.getGreatOlmNpc().previousDirectionFacing == Direction.NONE && party.getGreatOlmNpc().directionFacing == Direction.SOUTH) {
			party.getGreatOlmObject().performAnimation(OlmAnimations.middleToLeft);
		}
		TaskManager.submit(new Task(2, party, false) {
			@Override
			public void execute() {
				OlmAnimations.resetAnimation(party);
				stop();
			}
		});
	}
	
}
