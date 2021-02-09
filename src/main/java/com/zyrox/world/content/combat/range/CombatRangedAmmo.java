package com.zyrox.world.content.combat.range;

import com.google.common.collect.Multiset;
import com.zyrox.GameSettings;
import com.zyrox.model.Item;
import com.zyrox.model.container.impl.Equipment;
import com.zyrox.world.entity.impl.GameCharacter;
import com.zyrox.world.entity.impl.player.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * A table of constants that hold data for all ranged ammo.
 *
 * @author lare96
 */
public class CombatRangedAmmo {

    // TODO: Add poisonous ammo
    public enum RangedWeaponData {

        LONGBOW(new int[]{839}, new AmmunitionData[]{AmmunitionData.BRONZE_ARROW}, RangedWeaponType.LONGBOW),
        SHORTBOW(new int[]{841}, new AmmunitionData[]{AmmunitionData.BRONZE_ARROW}, RangedWeaponType.SHORTBOW),
        OAK_LONGBOW(new int[]{845},
                new AmmunitionData[]{AmmunitionData.BRONZE_ARROW, AmmunitionData.IRON_ARROW,
                        AmmunitionData.STEEL_ARROW},
                RangedWeaponType.LONGBOW),
        OAK_SHORTBOW(new int[]{843},
                new AmmunitionData[]{AmmunitionData.BRONZE_ARROW, AmmunitionData.IRON_ARROW,
                        AmmunitionData.STEEL_ARROW},
                RangedWeaponType.SHORTBOW),
        WILLOW_LONGBOW(new int[]{847},
                new AmmunitionData[]{AmmunitionData.BRONZE_ARROW, AmmunitionData.IRON_ARROW,
                        AmmunitionData.STEEL_ARROW, AmmunitionData.MITHRIL_ARROW},
                RangedWeaponType.LONGBOW),
        WILLOW_SHORTBOW(new int[]{849},
                new AmmunitionData[]{AmmunitionData.BRONZE_ARROW, AmmunitionData.IRON_ARROW,
                        AmmunitionData.STEEL_ARROW, AmmunitionData.MITHRIL_ARROW},
                RangedWeaponType.SHORTBOW),
        MAPLE_LONGBOW(new int[]{851},
                new AmmunitionData[]{AmmunitionData.BRONZE_ARROW, AmmunitionData.IRON_ARROW,
                        AmmunitionData.STEEL_ARROW, AmmunitionData.MITHRIL_ARROW, AmmunitionData.ADAMANT_ARROW},
                RangedWeaponType.LONGBOW),
        MAPLE_SHORTBOW(new int[]{853},
                new AmmunitionData[]{AmmunitionData.BRONZE_ARROW, AmmunitionData.IRON_ARROW,
                        AmmunitionData.STEEL_ARROW, AmmunitionData.MITHRIL_ARROW, AmmunitionData.ADAMANT_ARROW},
                RangedWeaponType.SHORTBOW),
        YEW_LONGBOW(new int[]{855},
                new AmmunitionData[]{AmmunitionData.BRONZE_ARROW, AmmunitionData.IRON_ARROW,
                        AmmunitionData.STEEL_ARROW, AmmunitionData.MITHRIL_ARROW, AmmunitionData.ADAMANT_ARROW,
                        AmmunitionData.RUNE_ARROW, AmmunitionData.ICE_ARROW},
                RangedWeaponType.LONGBOW),
        YEW_SHORTBOW(new int[]{857},
                new AmmunitionData[]{AmmunitionData.BRONZE_ARROW, AmmunitionData.IRON_ARROW,
                        AmmunitionData.STEEL_ARROW, AmmunitionData.MITHRIL_ARROW, AmmunitionData.ADAMANT_ARROW,
                        AmmunitionData.RUNE_ARROW, AmmunitionData.ICE_ARROW},
                RangedWeaponType.SHORTBOW),
        MAGIC_LONGBOW(new int[]{859},
                new AmmunitionData[]{AmmunitionData.BRONZE_ARROW, AmmunitionData.IRON_ARROW,
                        AmmunitionData.STEEL_ARROW, AmmunitionData.MITHRIL_ARROW, AmmunitionData.ADAMANT_ARROW,
                        AmmunitionData.RUNE_ARROW, AmmunitionData.ICE_ARROW, AmmunitionData.BROAD_ARROW},
                RangedWeaponType.LONGBOW),
        MAGIC_SHORTBOW(new int[]{861, 6724, 16887},
                new AmmunitionData[]{AmmunitionData.BRONZE_ARROW, AmmunitionData.IRON_ARROW,
                        AmmunitionData.STEEL_ARROW, AmmunitionData.MITHRIL_ARROW, AmmunitionData.ADAMANT_ARROW,
                        AmmunitionData.RUNE_ARROW, AmmunitionData.ICE_ARROW, AmmunitionData.BROAD_ARROW},
                RangedWeaponType.SHORTBOW),
        GODBOW(new int[]{19143, 19149, 19146},
                new AmmunitionData[]{AmmunitionData.BRONZE_ARROW, AmmunitionData.IRON_ARROW,
                        AmmunitionData.STEEL_ARROW, AmmunitionData.MITHRIL_ARROW, AmmunitionData.ADAMANT_ARROW,
                        AmmunitionData.RUNE_ARROW, AmmunitionData.BROAD_ARROW, AmmunitionData.DRAGON_ARROW},
                RangedWeaponType.SHORTBOW),

        TOXIC_BLOWPIPE(new int[]{12926}, new AmmunitionData[]{AmmunitionData.BLOWPIPE},
                RangedWeaponType.BLOWPIPE),

        MAGMA_BLOWPIPE(new int[]{12927}, new AmmunitionData[]{AmmunitionData.BLOWPIPE},
                RangedWeaponType.BLOWPIPE),

        MAGMA_BLOWPIPE_C(new int[]{3065}, new AmmunitionData[]{AmmunitionData.BLOWPIPE},
                RangedWeaponType.BLOWPIPE),

        DARK_BOW(new int[]{11235, 13405, 15701, 15702, 15703, 15704},
                new AmmunitionData[]{AmmunitionData.BRONZE_ARROW, AmmunitionData.IRON_ARROW,
                        AmmunitionData.STEEL_ARROW, AmmunitionData.MITHRIL_ARROW, AmmunitionData.ADAMANT_ARROW,
                        AmmunitionData.RUNE_ARROW, AmmunitionData.DRAGON_ARROW},
                RangedWeaponType.DARK_BOW),
        TWISTED_BOW(new int[]{20998},
                new AmmunitionData[]{AmmunitionData.BRONZE_ARROW, AmmunitionData.IRON_ARROW,
                        AmmunitionData.STEEL_ARROW, AmmunitionData.MITHRIL_ARROW, AmmunitionData.ADAMANT_ARROW,
                        AmmunitionData.RUNE_ARROW, AmmunitionData.BROAD_ARROW, AmmunitionData.DRAGON_ARROW},
                RangedWeaponType.LONGBOW),
        TWISTED_BOW_2(new int[]{20999},
                new AmmunitionData[]{AmmunitionData.BRONZE_ARROW, AmmunitionData.IRON_ARROW,
                        AmmunitionData.STEEL_ARROW, AmmunitionData.MITHRIL_ARROW, AmmunitionData.ADAMANT_ARROW,
                        AmmunitionData.RUNE_ARROW, AmmunitionData.BROAD_ARROW, AmmunitionData.DRAGON_ARROW},
                RangedWeaponType.LONGBOW),
        TWISTED_BOW_3(new int[]{21010},
                new AmmunitionData[]{AmmunitionData.BRONZE_ARROW, AmmunitionData.IRON_ARROW,
                        AmmunitionData.STEEL_ARROW, AmmunitionData.MITHRIL_ARROW, AmmunitionData.ADAMANT_ARROW,
                        AmmunitionData.RUNE_ARROW, AmmunitionData.BROAD_ARROW, AmmunitionData.DRAGON_FIRE_ARROW,
                        AmmunitionData.DRAGON_ARROW},
                RangedWeaponType.LONGBOW),
        TWISTED_BOW_4(new int[]{21020},
                new AmmunitionData[]{AmmunitionData.BRONZE_ARROW, AmmunitionData.IRON_ARROW,
                        AmmunitionData.STEEL_ARROW, AmmunitionData.MITHRIL_ARROW, AmmunitionData.ADAMANT_ARROW,
                        AmmunitionData.RUNE_ARROW, AmmunitionData.BROAD_ARROW, AmmunitionData.DRAGON_FIRE_ARROW,
                        AmmunitionData.DRAGON_ARROW},
                RangedWeaponType.LONGBOW),

        BRONZE_CROSSBOW(new int[]{9174}, new AmmunitionData[]{AmmunitionData.BRONZE_BOLT},
                RangedWeaponType.CROSSBOW),
        IRON_CROSSBOW(new int[]{9177},
                new AmmunitionData[]{AmmunitionData.BRONZE_BOLT, AmmunitionData.OPAL_BOLT, AmmunitionData.IRON_BOLT},
                RangedWeaponType.CROSSBOW),
        STEEL_CROSSBOW(new int[]{9179},
                new AmmunitionData[]{AmmunitionData.BRONZE_BOLT, AmmunitionData.OPAL_BOLT, AmmunitionData.IRON_BOLT,
                        AmmunitionData.JADE_BOLT, AmmunitionData.STEEL_BOLT, AmmunitionData.PEARL_BOLT},
                RangedWeaponType.CROSSBOW),
        MITHRIL_CROSSBOW(new int[]{9181},
                new AmmunitionData[]{AmmunitionData.BRONZE_BOLT, AmmunitionData.OPAL_BOLT, AmmunitionData.IRON_BOLT,
                        AmmunitionData.JADE_BOLT, AmmunitionData.STEEL_BOLT, AmmunitionData.PEARL_BOLT,
                        AmmunitionData.MITHRIL_BOLT, AmmunitionData.TOPAZ_BOLT},
                RangedWeaponType.CROSSBOW),
        ADAMANT_CROSSBOW(new int[]{9183},
                new AmmunitionData[]{AmmunitionData.BRONZE_BOLT, AmmunitionData.OPAL_BOLT, AmmunitionData.IRON_BOLT,
                        AmmunitionData.JADE_BOLT, AmmunitionData.STEEL_BOLT, AmmunitionData.PEARL_BOLT,
                        AmmunitionData.MITHRIL_BOLT, AmmunitionData.TOPAZ_BOLT, AmmunitionData.ADAMANT_BOLT,
                        AmmunitionData.SAPPHIRE_BOLT, AmmunitionData.EMERALD_BOLT, AmmunitionData.RUBY_BOLT},
                RangedWeaponType.CROSSBOW),
        /* Crossbows who accept all ammo */HIGHEST_CROSSBOWS(new int[]{51012, 9185, 18357, 13051, 14684},
                new AmmunitionData[]{AmmunitionData.BRONZE_BOLT, AmmunitionData.OPAL_BOLT, AmmunitionData.IRON_BOLT,
                        AmmunitionData.JADE_BOLT, AmmunitionData.STEEL_BOLT, AmmunitionData.PEARL_BOLT,
                        AmmunitionData.MITHRIL_BOLT, AmmunitionData.TOPAZ_BOLT, AmmunitionData.ADAMANT_BOLT,
                        AmmunitionData.SAPPHIRE_BOLT, AmmunitionData.EMERALD_BOLT, AmmunitionData.RUBY_BOLT,
                        AmmunitionData.RUNITE_BOLT, AmmunitionData.BROAD_BOLT, AmmunitionData.DIAMOND_BOLT,
                        AmmunitionData.ONYX_BOLT, AmmunitionData.DRAGON_BOLT},
                RangedWeaponType.CROSSBOW),

        BRONZE_DART(new int[]{806}, new AmmunitionData[]{AmmunitionData.BRONZE_DART}, RangedWeaponType.THROW),
        IRON_DART(new int[]{807}, new AmmunitionData[]{AmmunitionData.IRON_DART}, RangedWeaponType.THROW),
        STEEL_DART(new int[]{808}, new AmmunitionData[]{AmmunitionData.STEEL_DART}, RangedWeaponType.THROW),
        MITHRIL_DART(new int[]{809}, new AmmunitionData[]{AmmunitionData.MITHRIL_DART}, RangedWeaponType.THROW),
        ADAMANT_DART(new int[]{810}, new AmmunitionData[]{AmmunitionData.ADAMANT_DART}, RangedWeaponType.THROW),
        RUNE_DART(new int[]{811}, new AmmunitionData[]{AmmunitionData.RUNE_DART}, RangedWeaponType.THROW),
        DRAGON_DART(new int[]{11230}, new AmmunitionData[]{AmmunitionData.DRAGON_DART}, RangedWeaponType.THROW),

        BRONZE_KNIFE(new int[]{864, 870, 5654}, new AmmunitionData[]{AmmunitionData.BRONZE_KNIFE},
                RangedWeaponType.THROW),
        IRON_KNIFE(new int[]{863, 871, 5655}, new AmmunitionData[]{AmmunitionData.IRON_KNIFE},
                RangedWeaponType.THROW),
        STEEL_KNIFE(new int[]{865, 872, 5656}, new AmmunitionData[]{AmmunitionData.STEEL_KNIFE},
                RangedWeaponType.THROW),
        BLACK_KNIFE(new int[]{869, 874, 5658}, new AmmunitionData[]{AmmunitionData.BLACK_KNIFE},
                RangedWeaponType.THROW),
        MITHRIL_KNIFE(new int[]{866, 873, 5657}, new AmmunitionData[]{AmmunitionData.MITHRIL_KNIFE},
                RangedWeaponType.THROW),
        ADAMANT_KNIFE(new int[]{867, 875, 5659}, new AmmunitionData[]{AmmunitionData.ADAMANT_KNIFE},
                RangedWeaponType.THROW),
        RUNE_KNIFE(new int[]{868, 876, 5660, 5667}, new AmmunitionData[]{AmmunitionData.RUNE_KNIFE},
                RangedWeaponType.THROW),

        BRONZE_THROWNAXE(new int[]{800}, new AmmunitionData[]{AmmunitionData.BRONZE_THROWNAXE},
                RangedWeaponType.THROW),
        IRON_THROWNAXE(new int[]{801}, new AmmunitionData[]{AmmunitionData.IRON_THROWNAXE},
                RangedWeaponType.THROW),
        STEEL_THROWNAXE(new int[]{802}, new AmmunitionData[]{AmmunitionData.STEEL_THROWNAXE},
                RangedWeaponType.THROW),
        MITHRIL_THROWNAXE(new int[]{803}, new AmmunitionData[]{AmmunitionData.MITHRIL_THROWNAXE},
                RangedWeaponType.THROW),
        ADAMANT_THROWNAXE(new int[]{804}, new AmmunitionData[]{AmmunitionData.ADAMANT_THROWNAXE},
                RangedWeaponType.THROW),
        RUNE_THROWNAXE(new int[]{805}, new AmmunitionData[]{AmmunitionData.RUNE_THROWNAXE},
                RangedWeaponType.THROW),
        DRAGON_THROWNAXE(new int[]{50849}, new AmmunitionData[]{AmmunitionData.DRAGON_THROWNAXE},
                RangedWeaponType.THROW),
        MORRIGANS_THROWNAXE(new int[]{13883, 13957}, new AmmunitionData[]{AmmunitionData.MORRIGANS_THROWNAXE},
                RangedWeaponType.THROW),

        TOKTZ_XIL_UL(new int[]{6522}, new AmmunitionData[]{AmmunitionData.TOKTZ_XIL_UL}, RangedWeaponType.THROW),

        BRONZE_JAVELIN(new int[]{825}, new AmmunitionData[]{AmmunitionData.BRONZE_JAVELIN},
                RangedWeaponType.BALLISTA),
        IRON_JAVELIN(new int[]{826}, new AmmunitionData[]{AmmunitionData.IRON_JAVELIN},
                RangedWeaponType.BALLISTA),
        STEEL_JAVELIN(new int[]{827}, new AmmunitionData[]{AmmunitionData.STEEL_JAVELIN},
                RangedWeaponType.BALLISTA),
        MITHRIL_JAVELIN(new int[]{828}, new AmmunitionData[]{AmmunitionData.MITHRIL_JAVELIN},
                RangedWeaponType.BALLISTA),
        ADAMANT_JAVELIN(new int[]{829}, new AmmunitionData[]{AmmunitionData.ADAMANT_JAVELIN},
                RangedWeaponType.BALLISTA),
        RUNE_JAVELIN(new int[]{830}, new AmmunitionData[]{AmmunitionData.RUNE_JAVELIN},
                RangedWeaponType.BALLISTA),

        MORRIGANS_JAVELIN(new int[]{13879, 13953}, new AmmunitionData[]{AmmunitionData.MORRIGANS_JAVELIN},
                RangedWeaponType.THROW),

        CHINCHOMPA(new int[]{10033}, new AmmunitionData[]{AmmunitionData.CHINCHOMPA}, RangedWeaponType.THROW),
        RED_CHINCHOMPA(new int[]{10034}, new AmmunitionData[]{AmmunitionData.RED_CHINCHOMPA},
                RangedWeaponType.THROW),

        HAND_CANNON(new int[]{15241}, new AmmunitionData[]{AmmunitionData.HAND_CANNON_SHOT},
                RangedWeaponType.HAND_CANNON),

        KARILS_CROSSBOW(new int[]{4734}, new AmmunitionData[]{AmmunitionData.BOLT_RACK},
                RangedWeaponType.CROSSBOW),
        CRYSTAL(new int[]{4212}, new AmmunitionData[]{AmmunitionData.CRYSTAL_AMMO}, RangedWeaponType.SHORTBOW),

        BALLISTA(new int[]{49478, 49481},
                new AmmunitionData[]{AmmunitionData.BRONZE_JAVELIN, AmmunitionData.IRON_JAVELIN,
                        AmmunitionData.STEEL_JAVELIN, AmmunitionData.MITHRIL_JAVELIN, AmmunitionData.ADAMANT_JAVELIN,
                        AmmunitionData.RUNE_JAVELIN, AmmunitionData.DRAGON_JAVELIN},
                RangedWeaponType.BALLISTA),

        ZARYTE(new int[]{4706}, new AmmunitionData[]{AmmunitionData.ZARYTE_AMMO}, RangedWeaponType.SHORTBOW),
        CRAWS_BOW(new int[]{52550}, new AmmunitionData[]{AmmunitionData.CRAW_AMMO}, RangedWeaponType.SHORTBOW);

        RangedWeaponData(int[] weaponIds, AmmunitionData[] ammunitionData, RangedWeaponType type) {
            this.weaponIds = weaponIds;
            this.ammunitionData = ammunitionData;
            this.type = type;
        }

        private int[] weaponIds;
        private AmmunitionData[] ammunitionData;
        private RangedWeaponType type;

        public int[] getWeaponIds() {
            return weaponIds;
        }

        public AmmunitionData[] getAmmunitionData() {
            return ammunitionData;
        }

        public RangedWeaponType getType() {
            return type;
        }

        public static RangedWeaponData getData(Player p) {
            int weapon = p.getEquipment().getItems()[Equipment.WEAPON_SLOT].getId();
            for (RangedWeaponData data : RangedWeaponData.values()) {
                for (int i : data.getWeaponIds()) {
                    if (i == weapon)
                        return data;
                }
            }
            return null;
        }

        public static AmmunitionData getAmmunitionData(Player p) {
            RangedWeaponData data = p.getRangedWeaponData();

            if (data == CRYSTAL) {
                return AmmunitionData.CRYSTAL_AMMO;
            }

            if (data == ZARYTE) {
                return AmmunitionData.ZARYTE_AMMO;
            }

            if (data == CRAWS_BOW) {
                return AmmunitionData.CRAW_AMMO;
            }

            if (p.getEquipment().contains(49481) || p.getEquipment().contains(49478)) {
                return AmmunitionData.DRAGON_JAVELIN;
            }
            if (data != null) {
                int ammunition = p.getEquipment()
                        .getItems()[data.getType() == RangedWeaponType.THROW
                        || data.getType() == RangedWeaponType.BLOWPIPE ? Equipment.WEAPON_SLOT
                        : Equipment.AMMUNITION_SLOT].getId();
                for (AmmunitionData ammoData : AmmunitionData.VALUES) {
                    for (int i : ammoData.getItemIds()) {
                        if (i == ammunition)
                            return ammoData;
                    }
                }
            }
            return AmmunitionData.NONE;
        }

        public static int getRangedStrengthBonus(Player player, GameCharacter victim) {
            int bonus = 0;

            Item weapon = player.getEquipment().get(Equipment.WEAPON_SLOT);

            if (weapon != null) {
                if (weapon.getId() == 4706) {
                    return victim == null || victim.isPlayer() ? 75 : 125;
                }
                AmmunitionData weaponAmmo = AmmunitionData.getAmmo().get(weapon.getId());
                if (weaponAmmo == AmmunitionData.BLOWPIPE || weaponAmmo == AmmunitionData.MAGMA_BLOWPIPE || weaponAmmo == AmmunitionData.MAGMA_BLOWPIPE_C) {
                    for (Multiset.Entry<Integer> dart : player.getBlowpipeLoading().getContents().entrySet()) {
                        AmmunitionData ammo = AmmunitionData.getAmmo().get(dart.getElement());
                        if (ammo != null && ammo.getStrength() > bonus) {
                            bonus = ammo.getStrength();
                        }
                    }
                    bonus += 40;
                    if (weaponAmmo == AmmunitionData.MAGMA_BLOWPIPE || weaponAmmo == AmmunitionData.MAGMA_BLOWPIPE_C) {
                        bonus += 3;
                    }
                } else {

                    if(weaponAmmo != null)
                        bonus = weaponAmmo.strength;
                }
            }

            if (bonus <= 0) {
                Item ammo = player.getEquipment().get(Equipment.AMMUNITION_SLOT);

                if (ammo != null) {
                    AmmunitionData ammoData = AmmunitionData.getAmmo().get(ammo.getId());
                    if (ammoData != null) {
                        bonus = ammoData.getStrength();
                    }
                }
            }

            if(weapon != null)
                bonus += weapon.getDefinition().getBonus()[15];

            return bonus;
        }
    }

    public enum AmmunitionData {

        NONE(new int[]{-1}, 0, 0, 0, 0, 0, 0, 0, 0),

        BLOWPIPE(new int[]{12926}, -1, 1123, 10, 20, 40, 23, 25),
        MAGMA_BLOWPIPE(new int[]{12927}, -1, 1123, 10, 20, 45, 23, 25),
        MAGMA_BLOWPIPE_C(new int[]{3065}, -1, 1123, 10, 20, 45, 23, 25),

        BRONZE_ARROW(new int[]{882}, 19, 1104, 10, 3, 44, 7, 43, 31),
        IRON_ARROW(new int[]{884}, 18, 1105, 9, 3, 44, 10, 43, 31),
        STEEL_ARROW(new int[]{886}, 20, 1106, 11, 3, 44, 16, 43, 31),
        MITHRIL_ARROW(new int[]{888}, 21, 1107, 12, 3, 44, 22, 43, 31),
        ADAMANT_ARROW(new int[]{890}, 22, 1108, 13, 3, 44, 31, 43, 31),
        RUNE_ARROW(new int[]{892}, 24, 1109, 15, 3, 44, 49, 43, 31),
        ICE_ARROW(new int[]{78}, 25, 1110, 16, 3, 44, 58, 34, 31),
        BROAD_ARROW(new int[]{4160}, 20, 1112, 11, 3, 44, 58, 43, 31),
        DRAGON_ARROW(new int[]{11212}, 1111, 1111, 1120, 3, 44, 60, 43, 31),
        DRAGON_FIRE_ARROW(new int[]{11217}, 1113, 1113, 445, 3, 44, 60, 43, 31),
        ZARYTE_AMMO(new int[]{-1}, -1, 249, 3, 44, 62, 43, 31),
        CRAW_AMMO(new int[]{-1}, -1, 249, 3, 44, 55, 43, 31),
        CRYSTAL_AMMO(new int[]{-1}, -1, 249, 3, 44, 55, 43, 31),

        BRONZE_BOLT(new int[]{877}, -1, 27, 3, 44, 10, 43, 31),
        OPAL_BOLT(new int[]{879, 9236}, -1, 27, 3, 44, 14, 43, 31),
        IRON_BOLT(new int[]{9140}, -1, 27, 3, 44, 46, 43, 31),
        JADE_BOLT(new int[]{9335, 9237}, -1, 27, 3, 44, 30, 43, 31),
        STEEL_BOLT(new int[]{9141}, -1, 27, 3, 44, 64, 43, 31),
        PEARL_BOLT(new int[]{880, 9238}, -1, 27, 3, 44, 48, 43, 31),
        MITHRIL_BOLT(new int[]{9142}, -1, 27, 3, 44, 82, 43, 31),
        TOPAZ_BOLT(new int[]{9336, 9239}, -1, 27, 3, 44, 66, 43, 31),
        ADAMANT_BOLT(new int[]{9143}, -1, 27, 3, 44, 100, 43, 31),
        SAPPHIRE_BOLT(new int[]{9337, 9240}, -1, 27, 3, 44, 83, 43, 31),
        EMERALD_BOLT(new int[]{9338, 9241}, -1, 27, 3, 44, 85, 43, 31),
        RUBY_BOLT(new int[]{9339, 9242}, -1, 27, 3, 44, 103, 43, 31),
        RUNITE_BOLT(new int[]{9144}, -1, 27, 3, 44, 115, 43, 31),
        BROAD_BOLT(new int[]{13280}, -1, 27, 3, 44, 100, 43, 31),
        DIAMOND_BOLT(new int[]{9340, 9243}, -1, 27, 3, 44, 105, 43, 31),
        ONYX_BOLT(new int[]{9342, 9245}, -1, 27, 3, 44, 120, 43, 31),
        DRAGON_BOLT(new int[]{9341, 9244}, -1, 27, 3, 44, 122, 43, 31),
        BRONZE_DART(new int[]{806}, 1234, 226, 3, 33, 1, 45, 37),
        IRON_DART(new int[]{807}, 1235, 227, 3, 33, 3, 45, 37),
        STEEL_DART(new int[]{808}, 1236, 228, 3, 33, 4, 45, 37),
        MITHRIL_DART(new int[]{809}, 1238, 229, 3, 33, 7, 45, 37),
        ADAMANT_DART(new int[]{810}, 1239, 230, 3, 33, 10, 45, 37),
        RUNE_DART(new int[]{811}, 1240, 231, 3, 33, 14, 45, 37),
        DRAGON_DART(new int[]{11230}, 1123, 226, 3, 33, 20, 45, 37),

        BRONZE_KNIFE(new int[]{864, 870, 5654}, 219, 212, 3, 33, 3, 45, 37),
        IRON_KNIFE(new int[]{863, 871, 5655}, 220, 213, 3, 33, 4, 45, 37),
        STEEL_KNIFE(new int[]{865, 872, 5656}, 221, 214, 3, 33, 7, 45, 37),
        BLACK_KNIFE(new int[]{869, 874, 5658}, 222, 215, 3, 33, 8, 45, 37),
        MITHRIL_KNIFE(new int[]{866, 873, 5657}, 223, 215, 3, 33, 10, 45, 37),
        ADAMANT_KNIFE(new int[]{867, 875, 5659}, 224, 217, 3, 33, 14, 45, 37),
        RUNE_KNIFE(new int[]{868, 876, 5660, 5667}, 225, 218, 3, 33, 24, 45, 37),

        BRONZE_THROWNAXE(new int[]{800}, 43, 36, 3, 44, 5, 43, 31),
        IRON_THROWNAXE(new int[]{801}, 42, 35, 3, 44, 7, 43, 31),
        STEEL_THROWNAXE(new int[]{802}, 44, 37, 3, 44, 11, 43, 31),
        MITHRIL_THROWNAXE(new int[]{803}, 45, 38, 3, 44, 16, 43, 31),
        ADAMANT_THROWNAXE(new int[]{804}, 46, 39, 3, 44, 23, 43, 31),
        RUNE_THROWNAXE(new int[]{805}, 48, 41, 3, 44, 26, 43, 31),
        DRAGON_THROWNAXE(new int[]{50849}, 4284, 4283, 3, 44, 47, 43, 31),
        MORRIGANS_THROWNAXE(new int[]{13883, 13957}, 1856, 1839, 1, 50, 117, 23, 26),

        BRONZE_JAVELIN(new int[]{825}, -1, -1, 2, 40, 25, 41, 31),
        IRON_JAVELIN(new int[]{826}, -1, -1, 2, 40, 42, 41, 31),
        STEEL_JAVELIN(new int[]{827}, -1, -1, 2, 40, 64, 41, 31),
        MITHRIL_JAVELIN(new int[]{828}, -1, -1, 2, 40, 85, 41, 31),
        ADAMANT_JAVELIN(new int[]{829}, -1, -1, 2, 40, 107, 41, 31),
        RUNE_JAVELIN(new int[]{830}, -1, -1, 2, 40, 124, 41, 31),
        DRAGON_JAVELIN(new int[]{19484 + GameSettings.OSRS_ITEM_OFFSET}, -1, -1, 2, 40, 150, 41, 31),

        MORRIGANS_JAVELIN(new int[]{13879, 13953}, 1836, 1837, 2, 50, 145, 47, 35),

        TOKTZ_XIL_UL(new int[]{6522}, -1, 442, 2, 40, 58, 45, 37),

        CHINCHOMPA(new int[]{10033}, -1, -1, 17, 8, 0, 45, 37),
        RED_CHINCHOMPA(new int[]{10034}, -1, -1, 17, 8, 15, 45, 37),

        HAND_CANNON_SHOT(new int[]{15243}, 2138, 2143, 3, 8, 115, 43, 31),
        NO_AMMO(new int[]{-1}, 206, 200, 2, 40, 0, 45, 37),

        BOLT_RACK(new int[]{4740}, -1, 27, 3, 33, 70, 43, 31);

        AmmunitionData(int[] itemIds, int startGfxId, int darkbowStartGfxId, int projectileId, int projectileSpeed,
                       int projectileDelay, int strength, int startHeight, int endHeight) {
            this.itemIds = itemIds;
            this.darkbowStartGfxId = darkbowStartGfxId;
            this.startGfxId = startGfxId;
            this.projectileId = projectileId;
            this.projectileSpeed = projectileSpeed;
            this.projectileDelay = projectileDelay;
            this.strength = strength;
            this.startHeight = startHeight;
            this.endHeight = endHeight;
        }

        AmmunitionData(int[] itemIds, int startGfxId, int projectileId, int projectileSpeed, int projectileDelay,
                       int strength, int startHeight, int endHeight) {
            this.itemIds = itemIds;
            this.startGfxId = startGfxId;
            this.projectileId = projectileId;
            this.projectileSpeed = projectileSpeed;
            this.projectileDelay = projectileDelay;
            this.strength = strength;
            this.startHeight = startHeight;
            this.endHeight = endHeight;
        }

        public static final AmmunitionData[] VALUES = values();

        private int[] itemIds;
        private int startGfxId;
        private int darkbowStartGfxId;
        private int projectileId;
        private int projectileSpeed;
        private int projectileDelay;
        private int strength;
        private int startHeight;
        private int endHeight;

        public int[] getItemIds() {
            return itemIds;
        }

        public boolean hasSpecialEffect() {
            return getItemIds().length >= 2;
        }

        public int getStartGfxId() {
            return startGfxId;
        }

        public int getDarkbowStartGfxId() {
            return darkbowStartGfxId;
        }

        public int getProjectileId() {
            return projectileId;
        }

        public int getProjectileSpeed() {
            return projectileSpeed;
        }

        public int getProjectileDelay() {
            return projectileDelay;
        }

        public int getStrength() {
            return strength;
        }

        public int getStartHeight() {
            return startHeight;
        }

        public int getEndHeight() {
            return endHeight;
        }

        private static final Map<Integer, AmmunitionData> ammo = new HashMap<>();

        static {
            for (AmmunitionData value : values()) {
                for (int itemId : value.getItemIds()) {
                    ammo.put(itemId, value);
                }
            }
        }

        public static Map<Integer, AmmunitionData> getAmmo() {
            return ammo;
        }
    }

    public enum RangedWeaponType {

        LONGBOW(6, 5), SHORTBOW(5, 4), CROSSBOW(5, 5), THROW(4, 3), MORR(5, 4), DARK_BOW(5, 5), HAND_CANNON(5, 4),
        BLOWPIPE(5, 3), BALLISTA(7, 7);

        RangedWeaponType(int distanceRequired, int attackDelay) {
            this.distanceRequired = distanceRequired;
            this.attackDelay = attackDelay;
        }

        private int distanceRequired;
        private int attackDelay;

        public int getDistanceRequired() {
            return distanceRequired;
        }

        public int getAttackDelay() {
            return attackDelay;
        }
    }
}
