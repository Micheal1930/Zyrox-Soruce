package com.zyrox.net.packet.command.impl;

import com.zyrox.model.Animation;
import com.zyrox.model.Flag;
import com.zyrox.model.Locations;
import com.zyrox.model.PlayerRights;
import com.zyrox.model.container.impl.Equipment;
import com.zyrox.model.definitions.WeaponAnimations;
import com.zyrox.net.packet.command.Command;
import com.zyrox.net.packet.command.CommandHeader;
import com.zyrox.world.entity.impl.player.Player;

@CommandHeader(command = {"bf", "backflip"}, description = "Backflip animation.")
public class BlackflipCommand extends Command {

    /**
     * Constructs a new {@link BlackflipCommand}.
     */
    public BlackflipCommand() {
        super(PlayerRights.PLATINUM_DONATOR, true);
    }

    @Override
    public boolean execute(Player player, String[] args) throws Exception {
        if(player.getLocation() == Locations.Location.WILDERNESS) {
            player.sendMessage("You can't do this command while in the wilderness.");
            return true;
        }
        if (player.isFloating()) {
            WeaponAnimations.assign(player, player.getEquipment().get(Equipment.WEAPON_SLOT));
        } else {
            player.performAnimation(new Animation(10301));
        }

        player.getUpdateFlag().flag(Flag.APPEARANCE);
        player.toggleFloating();

        return true;
    }

    @Override
    public String[] getUsage() {
        return new String[]{"::bf", "::backflip"};
    }

}
