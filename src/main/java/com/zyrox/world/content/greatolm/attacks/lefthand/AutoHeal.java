package com.zyrox.world.content.greatolm.attacks.lefthand;

import com.zyrox.engine.task.Task;
import com.zyrox.engine.task.TaskManager;
import com.zyrox.world.content.greatolm.OlmAnimations;
import com.zyrox.world.content.greatolm.RaidsParty;

public class AutoHeal {

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
                    party.getLeftHandObject().performAnimation(OlmAnimations.flashingInfinityLeftHand);

                }
                if (tick == 3) {
                    party.setHealingOlmLeftHand(true);
                }

                if (tick == 15) {
                    party.getLeftHandObject().performAnimation(OlmAnimations.leftHand);
                    party.setHealingOlmLeftHand(false);
                    stop();
                }
                tick++;
            }
        });
    }

}
