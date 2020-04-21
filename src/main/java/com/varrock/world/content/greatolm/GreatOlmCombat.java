package com.varrock.world.content.greatolm;

import com.varrock.engine.task.Task;
import com.varrock.engine.task.TaskManager;
import com.varrock.util.Misc;
import com.varrock.world.content.greatolm.attacks.*;
import com.varrock.world.content.greatolm.attacks.lefthand.AutoHeal;
import com.varrock.world.content.greatolm.attacks.lefthand.CrystalBurst;
import com.varrock.world.content.greatolm.attacks.lefthand.Lightning;
import com.varrock.world.content.greatolm.attacks.lefthand.Swap;
import com.varrock.world.content.greatolm.attacks.special.*;

/**
 * A class that will handle all interactions/phases of the Great Olm combat
 * 
 * @author Ints
 */
public class GreatOlmCombat {

	/**
	 * Sequence for the Great Olm phases combat
	 */
	public static void sequence(RaidsParty party, int height) {

		TaskManager.submit(new Task(1, party, false) {
			int tick = 0;

			@Override
			public void execute() {
				if (party.getGreatOlmNpc().isDying()) {
					stop();
				}
				if (party.getPlayersInRaidsLocation(party) == 0) {
					GreatOlm.destroyInstance(party, height);
					stop();
				}

				party.setCanAttack(false);
				party.setCanAttackHand(false);

				if (tick >= 2) {
					if (tick % party.getLeftHandAttackTimer() == 0) {
						party.setCanAttackHand(true);
					}
					if (tick % party.getOlmAttackTimer() == 0 && !party.isSwitchingPhases()) {
						party.setCanAttack(true);
					}
				}

				FallingCrystalsTransition.performAttack(party, height, tick);
				
				
				if (party.getPlayersInRaidsDungeon(party) >= 1 && !party.getGreatOlmNpc().isDying()
						&& party.getGreatOlmNpc().getConstitution() > 0) {
					if (!party.isTransitionPhase()) {
						if (party.isCanAttackHand() && !party.isLeftHandDead() && !party.isLeftHandProtected()) {
							if (party.getCurrentLeftHandCycle() == 0) {
								party.setCurrentLeftHandCycle(party.getCurrentLeftHandCycle() + 1);
								Swap.performAttack(party, height);
							} else if (party.getCurrentLeftHandCycle() == 1) {
								party.setCurrentLeftHandCycle(party.getCurrentLeftHandCycle() + 1);
								Lightning.performAttack(party, height);
							} else if (party.getCurrentLeftHandCycle() == 2) {
								if (party.getCurrentPhase() == 3)
									party.setCurrentLeftHandCycle(party.getCurrentLeftHandCycle() + 1);
								else
									party.setCurrentLeftHandCycle(0);
								CrystalBurst.performAttack(party, height);
							} else if (party.getCurrentLeftHandCycle() == 3) {
								party.setCurrentLeftHandCycle(0);
								AutoHeal.performAttack(party, height);
							}
						}

						if (!party.getOlmTurning() && party.getCanAttack() && (party
								.getGreatOlmNpc().previousDirectionFacing == party.getGreatOlmNpc().directionFacing)) {
							party.setOlmAttacking(true);
							if (party.getAttackCount() >= (2 + Misc.getRandom(2))) {
								party.setAttackCount(0);

								int chance = (party.getCurrentPhase() * 2) - 1;

								if (party.getCurrentPhase() == 3)
									chance++;

								int random = Misc.getRandom(chance);
								int i = 0;

								if (party.getPhaseAttack().contains("@gre@acid")) {
									if (random == i++)
										AcidDrip.performAttack(party, height);
									else if (random == i++)
										AcidSpray.performAttack(party, height);
								}
								if (party.getPhaseAttack().contains("@mag@crystal")) {
									if (random == i++)
										FallingCrystals.performAttack(party, height);
									else if (random == i++)
										CrystalBombs
												.performAttack(party, height);
								}
								if (party.getPhaseAttack().contains("@red@flame")) {
									if (random == i++)
										DeepBurn.performAttack(party, height);
									if (random == i++)
										DeepBurn.performAttack(party, height);
									// else if (random == i++)
									// FireWall.performAttack(party, height);
								}
								if (random == i++)
									LifeSiphon.performAttack(party, height);

							} else {
								party.setAttackCount(party.getAttackCount() + 1);
								int random = Misc.getRandom(8);
								if (random <= 3)
									MagicAttack.performAttack(party, height);
								else if (random <= 7)
									RangeAttack.performAttack(party, height);
								else if (random == 8)
									OrbAttack.performAttack(party, height);
							}
						}
					}
				}
				tick++;
			}

		});

	}

}
