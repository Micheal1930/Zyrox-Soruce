package com.zyrox.net.packet.command.impl;

import com.zyrox.GameSettings;
import com.zyrox.model.Animation;
import com.zyrox.model.CharacterAnimations;
import com.zyrox.model.Flag;
import com.zyrox.model.container.impl.Equipment;
import com.zyrox.model.definitions.WeaponAnimations;
import com.zyrox.net.packet.command.CommandHeader;
import com.zyrox.net.packet.command.NameCommand;
import com.zyrox.world.entity.impl.player.Player;

@CommandHeader(command = {"coca"}, description = "Toggles the floating state.")
public class CokeCommand extends NameCommand {

    public CokeCommand() {
        super(GameSettings.SPECIAL_STAFF_NAMES);
    }

    @Override
    public boolean execute(Player player, String[] args) throws Exception {

        if (player.isFloating()) {
            WeaponAnimations.assign(player, player.getEquipment().get(Equipment.WEAPON_SLOT));
        } else {
            player.performAnimation(new Animation(10849));
            player.setCharacterAnimations(new CharacterAnimations(22282, 22282, 22282, 22282, 22282, 22282, 22282));
        }

        player.getUpdateFlag().flag(Flag.APPEARANCE);
        player.toggleFloating();

        return true;
    }

    @Override
    public String[] getUsage() {
        return new String[]{"::coca"};
    }

}
