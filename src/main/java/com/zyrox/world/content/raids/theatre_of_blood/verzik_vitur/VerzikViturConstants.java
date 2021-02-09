package com.zyrox.world.content.raids.theatre_of_blood.verzik_vitur;

import com.zyrox.GameSettings;
import com.zyrox.model.Animation;
import com.zyrox.model.Position;

/**
 * Created by Jonny on 7/2/2019
 **/
public class VerzikViturConstants {

    public static final int VITUR_SITTING_IDLE_NPC_ID = GameSettings.OSRS_NPC_OFFSET + 8369;

    public static final int VITUR_SITTING_ATTACKING_NPC_ID = GameSettings.OSRS_NPC_OFFSET + 8370;

    public static final int VITUR_WALKING_SOUTH_NPC_ID = GameSettings.OSRS_NPC_OFFSET + 8371;

    public static final int VITUR_OUT_OF_CHAIR_NPC_ID = GameSettings.OSRS_NPC_OFFSET + 8372;

    public static final int VITUR_SPIDER_NPC_ID = GameSettings.OSRS_NPC_OFFSET + 8374;

    public static final Animation OUT_OF_CHAIR = new Animation(GameSettings.OSRS_ANIM_OFFSET + 8111);

    public static final Animation SPIDER_TRANSFORM = new Animation(GameSettings.OSRS_ANIM_OFFSET + 8119);

    public static final Animation SPIDER_ATTACK_MELEE = new Animation(GameSettings.OSRS_ANIM_OFFSET + 8123);

    public static final Animation SPIDER_ATTACK_MAGE = new Animation(GameSettings.OSRS_ANIM_OFFSET + 8124);

    public static final Animation SPIDER_ATTACK_RANGE = new Animation(GameSettings.OSRS_ANIM_OFFSET + 8125);

    public static final Animation CHAIR_ATTACK = new Animation(GameSettings.OSRS_ANIM_OFFSET + 8109);

    public static final Animation STANDING_ATTACK = new Animation(GameSettings.OSRS_ANIM_OFFSET + 8114);

    public static final Position CENTER_OF_ROOM = new Position(3167, 4310);

    public static final int DAWNBRINGER_ID = 22516 + GameSettings.OSRS_ITEM_OFFSET;

    public static final int EMPTY_THRONE_OBJECT_ID = GameSettings.OSRS_OBJECT_OFFSET + 32737;

    public static final int TREASURE_ROOM_OBJECT_ID = GameSettings.OSRS_OBJECT_OFFSET + 32738;

    public static final int ELECTRIC_PROJECTILE_ID = 1580 + GameSettings.OSRS_GFX_OFFSET;

    public static final int ELECTRIC_GRAPHIC_ID = 1582 + GameSettings.OSRS_GFX_OFFSET;

    public static final int PILLAR_DISSAPEARING_ID = 8104 + GameSettings.OSRS_ANIM_OFFSET;

    public static final int BOMB_PROJECTILE_ID = 1583 + GameSettings.OSRS_GFX_OFFSET;

    public static final int BOMB_IMPACT_GRAPHIC_ID = 1584 + GameSettings.OSRS_GFX_OFFSET;

    public static final int SPIDER_PROJECTILE_ID = 1586 + GameSettings.OSRS_GFX_OFFSET;

    public static final int HEALER_NPC_ID = 8384 + GameSettings.OSRS_NPC_OFFSET;

    public static final int BOMBER_NPC_ID = 8385 + GameSettings.OSRS_NPC_OFFSET;

    public static int HEALER_SPAWN_ANIMATION_ID = 8079 + GameSettings.OSRS_ANIM_OFFSET;

    public static int BOMBER_SPAWN_ANIMATION_ID = 8098 + GameSettings.OSRS_ANIM_OFFSET;

    public static final Position SPIDER_SPAWN = new Position(3171, 4315);


}
