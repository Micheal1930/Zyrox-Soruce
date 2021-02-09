package com.zyrox.world.content.greatolm.attacks;

import com.zyrox.engine.task.Task;
import com.zyrox.engine.task.TaskManager;
import com.zyrox.model.CombatIcon;
import com.zyrox.model.projectile.Projectile;
import com.zyrox.world.content.combat.CombatType;
import com.zyrox.world.content.greatolm.OlmAnimations;
import com.zyrox.world.content.greatolm.RaidsParty;
import com.zyrox.world.entity.impl.player.Player;

public class RangeAttack {

	public static void performAttack(RaidsParty party, int height) {
		party.setOlmAttackTimer(6);
		party.getGreatOlmNpc().performGreatOlmAttack(party);
		TaskManager.submit(new Task(1, party, true) {
			int tick = 0;

			@Override
			public void execute() {
				if (party.getGreatOlmNpc().isDying() || party.isSwitchingPhases()) {
					stop();
				}
				if (tick == 1) {
					for (Player member : party.getPlayers()) {
						if (member != null && member.isInsideRaids()) {
							new Projectile(party.getGreatOlmNpc(), member, Attacks.GREEN_CRYSTAL_FLYING, 60, 8, 70, 31,
									0).sendProjectile();
						}
					}
				}

				if (tick == 3) {
					for (Player member : party.getPlayers()) {
						if (member != null && member.isInsideRaids()) {
							Attacks.hitPlayer(member, 100, 330, CombatType.RANGED, CombatIcon.RANGED, 2, true);
						}
					}
					stop();
				}
				if (tick == 2) {
					OlmAnimations.resetAnimation(party);
				}
				tick++;
			}
		});
	}

}
