package com.zyrox.world.content.greatolm.attacks.lefthand;

import com.zyrox.engine.task.Task;
import com.zyrox.engine.task.TaskManager;
import com.zyrox.model.CombatIcon;
import com.zyrox.model.GameObject;
import com.zyrox.model.Graphic;
import com.zyrox.model.GraphicHeight;
import com.zyrox.world.content.CustomObjects;
import com.zyrox.world.content.combat.CombatType;
import com.zyrox.world.content.greatolm.OlmAnimations;
import com.zyrox.world.content.greatolm.RaidsParty;
import com.zyrox.world.content.greatolm.attacks.Attacks;
import com.zyrox.world.entity.impl.player.Player;

public class CrystalBurst {

	public static void performAttack(RaidsParty party, int height) {
		party.setLeftHandAttackTimer(20);

		TaskManager.submit(new Task(1, party, true) {
			int tick = 0;

			@Override
			public void execute() {
				if (party.isLeftHandDead()) {
					stop();
				}
				if (tick == 1) {
					party.getLeftHandObject().performAnimation(OlmAnimations.flashingCrystalLeftHand);

				}
				if (tick == 3) {
					int i = 0;

					for (Player member : party.getPlayers()) {
						if (member != null && member.isInsideRaids()) {
							party.getCrystalBursts()[i] = member.getPosition();
							CustomObjects.spawnGlobalObject(new GameObject(130033, party.getCrystalBursts()[i], 10, 3));
							i++;
						}
					}
					party.setCrystalAmount(i);

				}

				if (tick == 7) {
					party.getLeftHandObject().performAnimation(OlmAnimations.leftHand);

					for (int i = 0; i < party.getCrystalAmount(); i++) {
						CustomObjects.spawnGlobalObject(new GameObject(130034, party.getCrystalBursts()[i], 10, 3));
						for (Player member : party.getPlayers()) {
							if (member != null && member.isInsideRaids()) {
								if (member.getPosition().sameAs(party.getCrystalBursts()[i])) {
									member.sendMessage(
											"The crystal beneath your feet grows rapidly and shunts you to the side.");
									member.getMovementQueue().stepAway(member);
									Attacks.hitPlayer(member, 200, 400, CombatType.MAGIC, CombatIcon.NONE, 2, false);
								}
							}
						}
					}
				}
				if (tick == 9) {
					for (int i = 0; i < party.getCrystalAmount(); i++) {
						party.getOwner().getPacketSender().sendGlobalGraphic(
								new Graphic(Attacks.LEFTOVER_CRYSTALS, GraphicHeight.MIDDLE),
								party.getCrystalBursts()[i]);
						CustomObjects.spawnGlobalObject(new GameObject(-1, party.getCrystalBursts()[i], 10, 3));
					}

					stop();
				}
				tick++;
			}
		});
	}

}
