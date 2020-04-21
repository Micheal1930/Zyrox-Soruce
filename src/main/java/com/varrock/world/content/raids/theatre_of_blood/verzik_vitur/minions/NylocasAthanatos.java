package com.varrock.world.content.raids.theatre_of_blood.verzik_vitur.minions;

import com.varrock.GameSettings;
import com.varrock.model.CombatIcon;
import com.varrock.model.Hit;
import com.varrock.model.Hitmask;
import com.varrock.model.Position;
import com.varrock.model.projectile.Projectile;
import com.varrock.world.content.raids.theatre_of_blood.verzik_vitur.VerzikVitur;
import com.varrock.world.content.raids.theatre_of_blood.verzik_vitur.VerzikViturConstants;
import com.varrock.world.entity.impl.npc.NPC;
import com.varrock.world.entity.impl.player.Player;

/**
 * Created by Jonny on 7/23/2019
 * Also known as 'healer'
 **/
public class NylocasAthanatos extends NPC {

    private final VerzikVitur verzikVitur;

    private final Player target;

    private int healTicks = 0;

    public NylocasAthanatos(int id, Position position, VerzikVitur verzikVitur) {
        super(id, position);
        this.verzikVitur = verzikVitur;
        this.target = getRandomTarget();
    }

    public VerzikVitur getVerzikVitur() {
        return verzikVitur;
    }

    @Override
    public void sequence() {
        if(target != null) {

            setEntityInteraction(verzikVitur);
            getMovementQueue().setFollowCharacter(verzikVitur);

            if(healTicks >= 5) {
                if (getPosition().isWithinDistance(verzikVitur.getPosition(), 2)) {
                    this.verzikVitur.heal(100);
                    new Projectile(this, verzikVitur, 1587 + GameSettings.OSRS_GFX_OFFSET, 50, 1, 105, 43, 0).sendProjectile();
                    healTicks = 0;
                }
            }

            healTicks++;
        }
    }

}
