package com.zyrox.world.content.raids.theatre_of_blood.verzik_vitur.minions;

import com.zyrox.model.*;
import com.zyrox.world.content.raids.theatre_of_blood.verzik_vitur.VerzikVitur;
import com.zyrox.world.entity.impl.npc.NPC;
import com.zyrox.world.entity.impl.player.Player;

/**
 * Created by Jonny on 7/23/2019
 * Also known as 'bomber'
 **/
public class NylocasMatomenos extends NPC {

    private final VerzikVitur verzikVitur;
    private final Player target;

    private boolean bombed;

    public NylocasMatomenos(int id, Position position, VerzikVitur verzikVitur, Player target) {
        super(id, position);
        this.verzikVitur = verzikVitur;
        this.target = target;
    }

    public VerzikVitur getVerzikVitur() {
        return verzikVitur;
    }

    @Override
    public void sequence() {
        if(target == null || target.getLocation() != Locations.Location.THEATRE_OF_BLOOD_FINAL_ROOM) {
            dealDamage(new Hit(getConstitution(), Hitmask.DARK_PURPLE, CombatIcon.NONE));
            return;
        }

        if(!isFrozen()) {
            walkToPosition(target.getPosition());
        }

        if(getPosition().isWithinDistance(target.getPosition(), 1) && !bombed) {
            target.dealDamage(new Hit(500, Hitmask.DARK_PURPLE, CombatIcon.NONE));
            dealDamage(new Hit(getConstitution(), Hitmask.DARK_PURPLE, CombatIcon.NONE));
            bombed = true;
        }
    }

}
