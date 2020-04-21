package com.varrock.world.content.tutorial;

/**
 * The constants for the tutorial interface.
 *
 * @author Gabriel || Wolfsdarker
 */
public interface TutorialConstants {

    int[] skill_teleport_button_ids = {8654, 8657, 8660, 8663, 8666, 8669, 8672, 28177, 28180, 8655, 8658, 8861, 8664, 8667, 8670, 12162, 28178, 8656, 8659, 8662, 8665, 8668, 8671, 13928, 28179};

    /**
     * Returns if the button is a teleport to skill button.
     *
     * @param buttonId
     * @return
     */
    static boolean isSkillTeleportButton(int buttonId) {
        for (int skill_teleport_button_id : skill_teleport_button_ids) {
            if (skill_teleport_button_id == buttonId)
                return true;
        }
        return false;
    }

}
