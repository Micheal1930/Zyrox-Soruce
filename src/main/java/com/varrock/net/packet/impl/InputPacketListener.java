package com.varrock.net.packet.impl;

import com.google.common.primitives.Ints;
import com.google.common.reflect.ClassPath;
import com.google.common.reflect.ClassPath.ClassInfo;
import com.varrock.GameSettings;
import com.varrock.commands.CommandHandler;
import com.varrock.engine.task.Task;
import com.varrock.engine.task.TaskManager;
import com.varrock.engine.task.impl.PlayerDeathTask;
import com.varrock.model.*;
import com.varrock.model.Locations.Location;
import com.varrock.model.container.impl.Inventory;
import com.varrock.model.definitions.ItemDefinition;
import com.varrock.model.definitions.NPCDrops;
import com.varrock.model.definitions.NpcDefinition;
import com.varrock.model.input.impl.SetTitle;
import com.varrock.model.log.impl.CommandLog;
import com.varrock.net.packet.Packet;
import com.varrock.net.packet.PacketListener;
import com.varrock.net.packet.command.Command;
import com.varrock.net.packet.command.CommandHeader;
import com.varrock.net.packet.command.NameCommand;
import com.varrock.net.security.ConnectionHandler;
import com.varrock.util.Misc;
import com.varrock.util.NameUtils;
import com.varrock.util.TreasureIslandLootDumper;
import com.varrock.world.World;
import com.varrock.world.content.*;
import com.varrock.world.content.Achievements.AchievementData;
import com.varrock.world.content.clan.ClanChatManager;
import com.varrock.world.content.combat.strategy.CombatStrategies;
import com.varrock.world.content.combat.strategy.impl.CorporealBeast;
import com.varrock.world.content.combat.strategy.zulrah.ZulrahConstants;
import com.varrock.world.content.combat.weapon.CombatSpecial;
import com.varrock.world.content.droppreview.SLASHBASH;
import com.varrock.world.content.freeforall.FreeForAll;
import com.varrock.world.content.greatolm.GreatOlm;
import com.varrock.world.content.greatolm.RaidsParty;
import com.varrock.world.content.greatolm.RaidsReward;
import com.varrock.world.content.skill.impl.dungeoneering.Dungeoneering;
import com.varrock.world.content.skill.impl.herblore.Decanting;
import com.varrock.world.content.transportation.TeleportHandler;
import com.varrock.world.content.transportation.TeleportType;
import com.varrock.world.content.youtube.YouTubeManager;
import com.varrock.world.entity.impl.npc.NPC;
import com.varrock.world.entity.impl.player.Player;
import com.varrock.world.entity.impl.player.PlayerHandler;
import com.varrock.world.entity.impl.player.link.rights.StaffPrivilegeLevel;

import org.apache.commons.lang.StringUtils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class InputPacketListener implements PacketListener {

    @Override
    public void handleMessage(Player player, Packet packet) {

        String inputText = Misc.readString(packet.getBuffer());

        YouTubeManager.postVideo(player, inputText);
    }
}