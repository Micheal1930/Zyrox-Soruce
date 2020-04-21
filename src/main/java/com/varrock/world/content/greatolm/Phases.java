package com.varrock.world.content.greatolm;

import com.varrock.engine.task.Task;
import com.varrock.engine.task.TaskManager;
import com.varrock.model.Direction;
import com.varrock.model.GameObject;
import com.varrock.model.Position;
import com.varrock.util.Misc;
import com.varrock.world.World;
import com.varrock.world.content.CustomObjects;
import com.varrock.world.entity.impl.npc.NPC;

public class Phases {

	private static String[] phases = new String[]{"@gre@acid", "@mag@crystal", "@red@flame"};

	public static void raisePower(RaidsParty party, int height) {

		int random = Misc.getRandom(2);
		if(!party.getPhaseAttack().contains(phases[random])) {
			party.sendMessage("The Great Olm rises with the power of " + phases[random] + ".");
			party.getPhaseAttack().add(phases[random]);
		} else {
			if(!party.getPhaseAttack().contains(phases[0])) {
				party.sendMessage("The Great Olm rises with the power of " + phases[0] + ".");
				party.getPhaseAttack().add(phases[0]);
			} else if(!party.getPhaseAttack().contains(phases[1])) {
				party.sendMessage("The Great Olm rises with the power of " + phases[1] + ".");
				party.getPhaseAttack().add(phases[1]);
			} else if(!party.getPhaseAttack().contains(phases[2])) {
				party.sendMessage("The Great Olm rises with the power of " + phases[2] + ".");
				party.getPhaseAttack().add(phases[2]);
			}
		}
	}

	public static final int OLM_LEFT_HAND = 22555;
	public static final int OLM_HEAD = 22554;
	public static final int OLM_RIGHT_HAND = 22553;

	public static void startPhase1(RaidsParty party, int height) {

		if(party.getCurrentPhase() >= 1) {
			party.getPlayers().forEach(player -> System.out.println("Multiple attempts to create the first phase for great olm."));
			return;
		}

		party.setCurrentPhase(1);

		party.setLeftHandPosition(new Position(3238, 5733, height));
		party.setGreatOlmPosition(new Position(3238, 5738, height));
		party.setRightHandPosition(new Position(3238, 5743, height));

		NPC leftHandNpc = NPC.of(22555, party.getLeftHandPosition()); // left claw
		NPC greatolmNpc = NPC.of(22554, party.getGreatOlmPosition()); // olm head
		NPC rightHandNpc = NPC.of(22553, party.getRightHandPosition());// right claw

		party.setLeftHandNpc(leftHandNpc); // left claw
		party.setGreatOlmNpc(greatolmNpc); // olm head
		party.setRightHandNpc(rightHandNpc);// right claw

		World.register(party.getLeftHandNpc());
		World.register(party.getGreatOlmNpc());
		World.register(party.getRightHandNpc());

		party.setLeftHandObject(new GameObject(129883, party.getLeftHandPosition(), 10, 1));
		party.setGreatOlmObject(new GameObject(129880, party.getGreatOlmPosition(), 10, 1));
		party.setRightHandObject(new GameObject(129886, party.getRightHandPosition(), 10, 1));

		CustomObjects.spawnGlobalObject(party.getLeftHandObject());
		CustomObjects.spawnGlobalObject(party.getGreatOlmObject());
		CustomObjects.spawnGlobalObject(party.getRightHandObject());

		TaskManager.submit(new Task(1, party, false) {
			@Override
			public void execute() {
				party.getLeftHandObject().performAnimation(OlmAnimations.goingUpLeftHand);
				party.getGreatOlmObject().performAnimation(OlmAnimations.goingUp);
				party.getRightHandObject().performAnimation(OlmAnimations.goingUpRightHand);
				stop();
			}
		});
		TaskManager.submit(new Task(5, party, false) {
			@Override
			public void execute() {
				party.setLeftHandObject(new GameObject(129884, party.getLeftHandPosition(), 10, 1));
				party.setGreatOlmObject(new GameObject(129881, party.getGreatOlmPosition(), 10, 1));
				party.setRightHandObject(new GameObject(129887, party.getRightHandPosition(), 10, 1));

				CustomObjects.spawnGlobalObject(party.getLeftHandObject());
				CustomObjects.spawnGlobalObject(party.getGreatOlmObject());
				CustomObjects.spawnGlobalObject(party.getRightHandObject());
				party.setTransitionPhase(false);
				party.setLeftHandDead(false);
				party.setRightHandDead(false);
				party.setSwitchingPhases(false);
				raisePower(party, height);

				stop();
			}
		});
		party.getGreatOlmNpc().directionFacing = Direction.NONE;
		party.getGreatOlmNpc().previousDirectionFacing = Direction.NONE;

	}

	public static void startPhase2(RaidsParty party, int height) {

		party.getGreatOlmObject().performAnimation(OlmAnimations.goingDown);
		party.getGreatOlmNpc().setVisible(false);

		TaskManager.submit(new Task(3, party, false) {

			int tick = 0;

			@Override
			public void execute() {
				if(tick == 1) {
					CustomObjects.spawnGlobalObject(new GameObject(129885, new Position(3238, 5733, height), 10, 1));
					CustomObjects.spawnGlobalObject(new GameObject(129882, new Position(3238, 5738, height), 10, 1));
					CustomObjects.spawnGlobalObject(new GameObject(129888, new Position(3238, 5743, height), 10, 1));

				}
				if(tick == 3) {
					CustomObjects.spawnGlobalObject(new GameObject(129885, new Position(3238, 5733, height), 10, 1));
					CustomObjects.spawnGlobalObject(new GameObject(129882, new Position(3238, 5738, height), 10, 1));
					CustomObjects.spawnGlobalObject(new GameObject(129888, new Position(3238, 5743, height), 10, 1));
					party.setTransitionPhase(true);

				}
				if(tick == 30) {
					party.setTransitionPhase(false);
					party.setLeftHandDead(false);
					party.setRightHandDead(false);
					party.setClenchedHand(false);
					party.setLeftHandProtected(false);
					party.setHeight(height);
					party.setClenchedHandFirst(false);
					party.setClenchedHandSecond(false);
					party.setLeftHandPosition(new Position(3220, 5743, height));
					party.setGreatOlmPosition(new Position(3220, 5738, height));
					party.setRightHandPosition(new Position(3220, 5733, height));

					Position leftHandNpc = new Position(party.getLeftHandPosition().getX() + 3, party.getLeftHandPosition().getY(), height);
					Position RightHandNpc = new Position(party.getRightHandPosition().getX() + 3, party.getRightHandPosition().getY(), height);
					Position greatolmNpc = new Position(party.getGreatOlmPosition().getX() + 3, party.getGreatOlmPosition().getY(), height);

					party.setLeftHandNpc(NPC.of(OLM_LEFT_HAND, leftHandNpc)); // left claw
					party.setRightHandNpc(NPC.of(OLM_RIGHT_HAND, RightHandNpc));// right claw

					party.getGreatOlmNpc().setVisible(true);
					party.getGreatOlmNpc().moveTo(greatolmNpc);

					World.register(party.getLeftHandNpc());
					World.register(party.getRightHandNpc());

					party.setLeftHandObject(new GameObject(129883, party.getLeftHandPosition(), 10, 3));
					party.setGreatOlmObject(new GameObject(129880, party.getGreatOlmPosition(), 10, 3));
					party.setRightHandObject(new GameObject(129886, party.getRightHandPosition(), 10, 3));

					CustomObjects.spawnGlobalObject(party.getLeftHandObject());
					CustomObjects.spawnGlobalObject(party.getGreatOlmObject());
					CustomObjects.spawnGlobalObject(party.getRightHandObject());

					party.getGreatOlmObject().performAnimation(OlmAnimations.goingUp);
					party.getLeftHandObject().performAnimation(OlmAnimations.goingUpLeftHand);
					party.getRightHandObject().performAnimation(OlmAnimations.goingUpRightHand);

					TaskManager.submit(new Task(5, party, false) {
						@Override
						public void execute() {
							party.setLeftHandObject(new GameObject(129884, party.getLeftHandPosition(), 10, 3));
							party.setGreatOlmObject(new GameObject(129881, party.getGreatOlmPosition(), 10, 3));
							party.setRightHandObject(new GameObject(129887, party.getRightHandPosition(), 10, 3));

							CustomObjects.spawnGlobalObject(party.getLeftHandObject());
							CustomObjects.spawnGlobalObject(party.getGreatOlmObject());
							CustomObjects.spawnGlobalObject(party.getRightHandObject());
							party.setCurrentPhase(2);
							party.setSwitchingPhases(false);
							raisePower(party, height);

							stop();
						}
					});
					stop();

				}
				tick++;

			}
		});

	}

	public static void startPhase3(RaidsParty party, int height) {
		party.setLeftHandPosition(new Position(3238, 5733, height));
		party.setGreatOlmPosition(new Position(3238, 5738, height));
		party.setRightHandPosition(new Position(3238, 5743, height));

		party.getGreatOlmObject().performAnimation(OlmAnimations.goingDown);
		party.getGreatOlmNpc().setVisible(false);
		// party.getLeftHandObject().performAnimation(OlmAnimations.goingDownLeftHand);
		// party.getRightHandObject().performAnimation(OlmAnimations.goingDownRightHand);

		TaskManager.submit(new Task(3, party, false) {
			@Override
			public void execute() {
				/*
				 * Despawn other side
				 */
				CustomObjects.spawnGlobalObject(new GameObject(129885, new Position(3220, 5743, height), 10, 3));
				CustomObjects.spawnGlobalObject(new GameObject(129882, new Position(3220, 5738, height), 10, 3));
				CustomObjects.spawnGlobalObject(new GameObject(129888, new Position(3220, 5733, height), 10, 3));
				party.setTransitionPhase(true);

				stop();
			}
		});

		party.setTransitionPhase(true);
		TaskManager.submit(new Task(30, party, false) {
			@Override
			public void execute() {
				// party.getLeftHandNpc().moveTo(party.getLeftHandPosition());
				party.getGreatOlmNpc().moveTo(party.getGreatOlmPosition());
				// party.getRightHandNpc().moveTo(party.getRightHandPosition());

				party.setLeftHandNpc(NPC.of(OLM_LEFT_HAND, party.getLeftHandPosition())); // left claw
				party.setRightHandNpc(NPC.of(OLM_RIGHT_HAND, party.getRightHandPosition()));// right claw

				World.register(party.getLeftHandNpc());
				World.register(party.getRightHandNpc());

				party.setLeftHandObject(new GameObject(129883, party.getLeftHandPosition(), 10, 1));
				party.setGreatOlmObject(new GameObject(129880, party.getGreatOlmPosition(), 10, 1));
				party.setRightHandObject(new GameObject(129886, party.getRightHandPosition(), 10, 1));

				CustomObjects.spawnGlobalObject(party.getLeftHandObject());
				CustomObjects.spawnGlobalObject(party.getGreatOlmObject());
				CustomObjects.spawnGlobalObject(party.getRightHandObject());

				party.getLeftHandObject().performAnimation(OlmAnimations.goingUpLeftHand);
				party.getGreatOlmObject().performAnimation(OlmAnimations.goingUpEnraged);
				party.getRightHandObject().performAnimation(OlmAnimations.goingUpRightHand);

				TaskManager.submit(new Task(5, party, false) {
					@Override
					public void execute() {
						party.setLeftHandObject(new GameObject(129884, party.getLeftHandPosition(), 10, 1));
						party.setGreatOlmObject(new GameObject(129881, party.getGreatOlmPosition(), 10, 1));
						party.setRightHandObject(new GameObject(129887, party.getRightHandPosition(), 10, 1));

						CustomObjects.spawnGlobalObject(party.getLeftHandObject());
						CustomObjects.spawnGlobalObject(party.getGreatOlmObject());
						CustomObjects.spawnGlobalObject(party.getRightHandObject());

						party.getGreatOlmNpc().setVisible(true);
						party.getGreatOlmObject().performAnimation(OlmAnimations.faceMiddleEnraged);

						party.setLeftHandDead(false);
						party.setRightHandDead(false);
						party.setTransitionPhase(false);
						party.setClenchedHand(false);
						party.setLeftHandProtected(false);
						party.setHeight(height);
						party.setClenchedHandFirst(false);
						party.setClenchedHandSecond(false);
						party.setCurrentPhase(3);
						party.setSwitchingPhases(false);
						raisePower(party, height);
						stop();
					}
				});
				stop();
			}
		});

	}

}
