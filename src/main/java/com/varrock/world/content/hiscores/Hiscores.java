package com.varrock.world.content.hiscores;

import java.sql.PreparedStatement;

import com.varrock.GameServer;
import com.varrock.model.PlayerRights;
import com.varrock.model.Skill;
import com.varrock.net.sql.SQLTable;
import com.varrock.world.entity.impl.player.Player;

/**
 * Created by Jonny on 10/3/2019
 **/
public class Hiscores {

    public static void update(Player player) {
        if(player.isHigherStaff() || player.getRights() == PlayerRights.OWNER || player.getRights() == PlayerRights.ADMINISTRATOR) {
            return;
        }

        GameServer.getSqlNetwork().submit(connection -> {
            try (PreparedStatement statement = connection.prepareStatement("DELETE FROM " + SQLTable.getGameSchemaTable(SQLTable.SAVES_HISCORES) + " WHERE username=?")) {
                statement.setString(1, player.getName());
                statement.executeUpdate();
            }

            try (PreparedStatement statement = connection.prepareStatement(generateQuery())) {

                int parameterIndex = 1;

                statement.setString(parameterIndex++, player.getName());
                statement.setString(parameterIndex++, player.getGameMode().toString().toLowerCase());
                statement.setInt(parameterIndex++, player.prestige);
                statement.setLong(parameterIndex++, player.getSkillManager().getTotalExp());
                statement.setInt(parameterIndex++, player.getSkillManager().getTotalLevel());
                statement.setLong(parameterIndex++, player.achievedMax);

                for(Skill skill : Skill.values()) {
                    statement.setInt(parameterIndex++, player.getSkillManager().getMaxLevel(skill));
                }

                for(Skill skill : Skill.values()) {
                    statement.setInt(parameterIndex++, player.getSkillManager().getExperience(skill));
                }


                statement.executeUpdate();
            }
        });
    }

    public static String generateQuery() {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO " + SQLTable.getGameSchemaTable(SQLTable.SAVES_HISCORES) + " (");
        sb.append("username, ");
        sb.append("game_mode, ");
        sb.append("prestige, ");
        sb.append("total_xp, ");
        sb.append("total_level, ");
        sb.append("achieved_max, ");

        sb.append("attack_level, ");
        sb.append("defence_level, ");
        sb.append("strength_level, ");
        sb.append("constitution_level, ");
        sb.append("ranged_level, ");
        sb.append("prayer_level, ");
        sb.append("magic_level, ");
        sb.append("cooking_level, ");
        sb.append("woodcutting_level, ");
        sb.append("fletching_level, ");
        sb.append("fishing_level, ");
        sb.append("firemaking_level, ");
        sb.append("crafting_level, ");
        sb.append("smithing_level, ");
        sb.append("mining_level, ");
        sb.append("herblore_level, ");
        sb.append("agility_level, ");
        sb.append("thieving_level, ");
        sb.append("slayer_level, ");
        sb.append("farming_level, ");
        sb.append("runecrafting_level, ");
        sb.append("construction_level, ");
        sb.append("hunter_level, ");
        sb.append("dungeoneering_level, ");
        sb.append("summoning_level, ");

        sb.append("attack_xp, ");
        sb.append("defence_xp, ");
        sb.append("strength_xp, ");
        sb.append("constitution_xp, ");
        sb.append("ranged_xp, ");
        sb.append("prayer_xp, ");
        sb.append("magic_xp, ");
        sb.append("cooking_xp, ");
        sb.append("woodcutting_xp, ");
        sb.append("fletching_xp, ");
        sb.append("fishing_xp, ");
        sb.append("firemaking_xp, ");
        sb.append("crafting_xp, ");
        sb.append("smithing_xp, ");
        sb.append("mining_xp, ");
        sb.append("herblore_xp, ");
        sb.append("agility_xp, ");
        sb.append("thieving_xp, ");
        sb.append("slayer_xp, ");
        sb.append("farming_xp, ");
        sb.append("runecrafting_xp, ");
        sb.append("construction_xp, ");
        sb.append("hunter_xp, ");
        sb.append("dungeoneering_xp, ");
        sb.append("summoning_xp)");

        sb.append("VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
        return sb.toString();
    }

}
