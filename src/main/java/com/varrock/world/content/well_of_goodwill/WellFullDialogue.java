package com.varrock.world.content.well_of_goodwill;

import com.varrock.world.content.dialogue.Dialogue;
import com.varrock.world.content.dialogue.DialogueExpression;
import com.varrock.world.content.dialogue.DialogueManager;
import com.varrock.world.content.dialogue.DialogueType;

/**
 * @author lare96 <http://github.com/lare96>
 */
public final class WellFullDialogue extends Dialogue {

    private final WellOfGoodwill well;

    public WellFullDialogue(WellOfGoodwill well) {
        this.well = well;
    }

    @Override
    public DialogueType type() {
        return DialogueType.NPC_STATEMENT;
    }

    @Override
    public DialogueExpression animation() {
        return DialogueExpression.NORMAL;
    }

    @Override
    public String[] dialogue() {
        return new String[]{"The well is already full of coins and Saradomin", "has granted players with lots of rewards for their", "generosity! They will expire in " + well.getRemainingTime("a few moments") + "."};
    }

    @Override
    public int npcId() {
        return 802;
    }

    @Override
    public Dialogue nextDialogue() {
        return DialogueManager.getDialogues().get(75);
    }

}