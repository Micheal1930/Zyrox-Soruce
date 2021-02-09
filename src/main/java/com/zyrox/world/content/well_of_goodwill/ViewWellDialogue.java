package com.zyrox.world.content.well_of_goodwill;

import com.zyrox.util.Misc;
import com.zyrox.world.content.dialogue.Dialogue;
import com.zyrox.world.content.dialogue.DialogueExpression;
import com.zyrox.world.content.dialogue.DialogueManager;
import com.zyrox.world.content.dialogue.DialogueType;

/**
 * @author lare96 <http://github.com/lare96>
 */
public final class ViewWellDialogue extends Dialogue {

    private final WellOfGoodwill well;

    public ViewWellDialogue(WellOfGoodwill well) {
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
        return new String[]{"It looks like the well could hold another " + Misc.format(well.getCoinsNeeded()) + " coins."};
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