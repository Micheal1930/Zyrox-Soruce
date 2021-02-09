package com.zyrox.model.input.impl;

import com.zyrox.model.Flag;
import com.zyrox.model.input.Input;
import com.zyrox.util.NameUtils;
import com.zyrox.world.entity.impl.player.Player;

/*
 * @author ajw
 * www.simplicityps.org
 */

public class SetTitle extends Input {

	/*
	 * Handle the setting of loyalty titles. Makes sure they can't abuse them / use shop titles. 
	 */
	@Override
	public void handleSyntax(Player player, String syntax) {
		if (syntax == null || syntax.length() <= 2 || syntax.length() > 9 || !NameUtils.isValidName(syntax)) {
			player.getPacketSender().sendMessage("You can not set your title to that!");
			return;
		}
		
		if (syntax.contentEquals("Moderator") || syntax.contentEquals("moderator") || syntax.contentEquals("good") || syntax.contentEquals("evil") || syntax.contentEquals("lady") || syntax.contentEquals("sir") || syntax.contentEquals("baroness") || syntax.contentEquals("baron") || syntax.contentEquals("duchess") || syntax.contentEquals("duke") || syntax.contentEquals("lord") || syntax.contentEquals("queen") || syntax.contentEquals("king") || syntax.contentEquals("gambler") || syntax.contentEquals("veteran") || syntax.contentEquals("loyalist") || syntax.contentEquals("godslayer") || syntax.contentEquals("maxed") || syntax.contentEquals("combatant") || syntax.contentEquals("skiller") || syntax.contentEquals("immortal") || syntax.contentEquals("genocidal") || syntax.contentEquals("slaughterer") || syntax.contentEquals("killer") || syntax.contentEquals("Mod") || syntax.contentEquals("staff") || syntax.contentEquals("Admin") || syntax.contentEquals("administrator") || syntax.contentEquals("owner") || syntax.contentEquals("dev") || syntax.contentEquals("varrock") || syntax.contentEquals("fuck") || syntax.contentEquals("cunt") || syntax.contentEquals("developer") || syntax.contentEquals("support") || syntax.contentEquals("trusted")) {
			if (!player.getRights().isStaff()) {
				player.getPacketSender().sendMessage("You can not set your title to that!");
				return;
			}
		}
		
		player.setTitle("@or2@" + syntax);
		player.getUpdateFlag().flag(Flag.APPEARANCE);
	}
}