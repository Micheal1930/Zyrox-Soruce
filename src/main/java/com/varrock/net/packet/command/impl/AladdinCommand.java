package com.varrock.net.packet.command.impl;

import com.varrock.model.Animation;
import com.varrock.model.CharacterAnimations;
import com.varrock.model.Flag;
import com.varrock.model.container.impl.Equipment;
import com.varrock.model.definitions.WeaponAnimations;
import com.varrock.net.packet.command.CommandHeader;
import com.varrock.net.packet.command.NameCommand;
import com.varrock.world.entity.impl.player.Player;

@CommandHeader(command = { "carpet" }, description = "Aladdin Gang.")
public class AladdinCommand extends NameCommand {
	
	public AladdinCommand() {
		super("Finch","Jonny");
	}

	   @Override
	    public boolean execute(Player player, String[] args) throws Exception {

	        if (player.isFloating()) {
	            WeaponAnimations.assign(player, player.getEquipment().get(Equipment.WEAPON_SLOT));
	        } else {
	            player.performAnimation(new Animation(330));
	            player.setCharacterAnimations(new CharacterAnimations(330, 330, 330, 330, 330, 330, 330));
	        }

	        player.getUpdateFlag().flag(Flag.APPEARANCE);
	        player.toggleFloating();

	        return true;
	    }

	    @Override
	    public String[] getUsage() {
	        return new String[]{"::carpet"};
	    }

	}
