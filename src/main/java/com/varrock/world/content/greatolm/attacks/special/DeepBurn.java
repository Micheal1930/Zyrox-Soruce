package com.varrock.world.content.greatolm.attacks.special;

import com.varrock.engine.task.Task;
import com.varrock.engine.task.TaskManager;
import com.varrock.model.*;
import com.varrock.model.projectile.Projectile;
import com.varrock.util.Misc;
import com.varrock.world.content.greatolm.OlmAnimations;
import com.varrock.world.content.greatolm.RaidsParty;
import com.varrock.world.content.greatolm.attacks.Attacks;
import com.varrock.world.entity.impl.player.Player;

public class DeepBurn {

	public static void performAttack(RaidsParty party, int height) {
		party.getGreatOlmNpc().performGreatOlmAttack(party);
		party.setOlmAttackTimer(6);
		party.sendMessage("The Great Olm sounds a cry...");

		TaskManager.submit(new Task(1, party, true) {
			int tick = 0;

			@Override
			public void execute() {
				if (party.getGreatOlmNpc().isDying() || party.isSwitchingPhases()) {
					stop();
				}
				if (tick == 1) {
					int random = Misc.getRandom(party.getPlayers().size() - 1);
					party.getBurnPlayers().add(party.getPlayers().get(random));
					new Projectile(party.getGreatOlmNpc(), party.getBurnPlayers().get(0), Attacks.GREEN_PROJECTILE, 60,
							8, 70, 10, 0).sendProjectile();

				}
				if (tick == 2) {
					OlmAnimations.resetAnimation(party);
					if (party.getBurnPlayers().get(0).isInsideRaids()) {
						party.getBurnPlayers().get(0).forceChat("Burn with me!");
						party.getBurnPlayers().get(0).dealDamage(new Hit(50, Hitmask.RED, CombatIcon.NONE));
					}
				}
				if (tick == 19) {
					if (party.getBurnPlayers() != null) {
						for (int i = 0; i < party.getBurnPlayers().size(); i++) {
							if (party.getBurnPlayers().get(i) != null
									&& party.getBurnPlayers().get(i).isInsideRaids()) {
								party.getBurnPlayers().get(i)
										.sendMessage("You feel the deep burning inside dissipate.");
							}
						}
					}
					party.getBurnPlayers().clear();
					stop();
				}
				for (int iz = 0; iz < 19; iz++) {
					if (tick == (4 * iz) + 6) {
						if (party.getBurnPlayers() != null) {
							for (Player member : party.getPlayers()) {
								if (member != null && member.isInsideRaids()) {
									for (int i = 0; i < party.getBurnPlayers().size() - 1; i++) {
										if (Locations.goodDistance(member.getPosition(),
												party.getBurnPlayers().get(i).getPosition(), 1)) {
											if (!party.getBurnPlayers().contains(member))
												party.getBurnPlayers().add(member);
										}
									}
								}
							}
							for (int i = 0; i < party.getBurnPlayers().size(); i++) {

								if (party.getBurnPlayers().get(i) != null
										&& party.getBurnPlayers().get(i).isInsideRaids()) {
									party.getBurnPlayers().get(i).forceChat("Burn with me!");
									party.getBurnPlayers().get(i).dealDamage(new Hit(50, Hitmask.RED, CombatIcon.NONE));
									for (Skill skill : Skill.values()) {
										party.getBurnPlayers().get(i).getSkillManager().setCurrentLevel(skill,
												party.getBurnPlayers().get(i).getSkillManager().getCurrentLevel(skill)
														- 2);
									}
								}
							}
						} else {
							party.sendMessage("NULL");
						}

					}
				}

				tick++;
			}
		});

	}

}
