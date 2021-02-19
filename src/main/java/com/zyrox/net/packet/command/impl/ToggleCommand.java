package com.zyrox.net.packet.command.impl;

import com.zyrox.GameSettings;
import com.zyrox.model.definitions.ItemDefinition;
import com.zyrox.net.packet.command.CommandHeader;
import com.zyrox.net.packet.command.NameCommand;
import com.zyrox.world.World;
import com.zyrox.world.content.skill.impl.dungeoneering.Dungeoneering;
import com.zyrox.world.entity.impl.player.Player;

@CommandHeader(command = { "toggle" }, description = "Toggles the specified attribute of the server.")

public class ToggleCommand extends NameCommand {
	
	public static boolean COWS_SPAWNED = false;
	
	public ToggleCommand() {
		super(GameSettings.SPECIAL_STAFF_NAMES, GameSettings.HIGHER_STAFF_NAMES, GameSettings.INVESTIGATOR);
	}

	@Override
	public boolean execute(Player player, String[] args) throws Exception {
		if (args.length < 1) {
			throw new IllegalArgumentException();
		}

		switch (args[0]) {
		case "dd":
			GameSettings.DOUBLE_DONATIONS = !GameSettings.DOUBLE_DONATIONS;
			if (GameSettings.DOUBLE_DONATIONS) {
				World.sendMessage("@blu@<img=3> @red@DOUBLE DONATION hour is ON! Donate now and receive double points!");
			} else {
				World.sendMessage("@blu@<img=3> @red@Double donations are no longer active.");
			}
			break;
		case "dv":
			GameSettings.DOUBLE_VOTING = !GameSettings.DOUBLE_VOTING;
			if (GameSettings.DOUBLE_VOTING) {
				World.sendMessage("@blu@<img=3> @red@Double voting is now active. Vote now and receive double rewards!");
			} else {
				World.sendMessage("@blu@<img=3> @red@Double voting is no longer active.");
			}
			break;
		case "xp":
			GameSettings.DOUBLE_EXPERIENCE = !GameSettings.DOUBLE_EXPERIENCE;
			if (GameSettings.DOUBLE_EXPERIENCE) {
				World.sendMessage("@blu@<img=3> @red@Double experience is now active.");
			} else {
				World.sendMessage("@blu@<img=3> @red@Double experience is no longer active.");
			}
			break;
		case "drop_rate":
			GameSettings.DOUBLE_DROP_RATE = !GameSettings.DOUBLE_DROP_RATE;
			if (GameSettings.DOUBLE_DROP_RATE) {
				World.sendMessage("@blu@<img=3> @red@Double drop rate is now active.");
			} else {
				World.sendMessage("@blu@<img=3> @red@Double drop rate is no longer active.");
			}
			break;
		case "points":
			GameSettings.DOUBLE_POINTS = !GameSettings.DOUBLE_POINTS;
			if (GameSettings.DOUBLE_POINTS) {
				World.sendMessage("@blu@<img=3> @red@Double points & blood money is now active.");
			} else {
				World.sendMessage("@blu@<img=3> @red@Double points & blood money is no longer active.");
			}
			break;
		case "deals":
			GameSettings.DONATION_DEALS = !GameSettings.DONATION_DEALS;
			player.sendMessage("Discount deals are now @dre@" + (GameSettings.DONATION_DEALS ? "enabled" : "disabled") + "</col>.");
			break;
		case "dung":
			Dungeoneering.DUNGEONEERING_ENABLED = !Dungeoneering.DUNGEONEERING_ENABLED;
			player.sendMessage("Dungeoneering is now @dre@" + (Dungeoneering.DUNGEONEERING_ENABLED ? "enabled" : "disabled") + "</col>.");
			break;
		case "zulrah":
			GameSettings.ZULRAH_ENABLED = !GameSettings.ZULRAH_ENABLED;
			player.sendMessage("Zulrah is now @dre@" + (GameSettings.ZULRAH_ENABLED ? "enabled" : "disabled") + "</col>.");
			break;
		case "drop":
			GameSettings.DROP_ENABLED = !GameSettings.DROP_ENABLED;
			player.sendMessage("Dropping is now @dre@" + (GameSettings.DROP_ENABLED ? "enabled" : "disabled") + "</col>.");
			break;
		case "party":
			GameSettings.PARTY_ROOM_ENABLED = !GameSettings.PARTY_ROOM_ENABLED;
			player.sendMessage("Party Room is now @dre@" + (GameSettings.PARTY_ROOM_ENABLED ? "enabled" : "disabled") + "</col>.");
			break;
		case "trade":
			GameSettings.TRADE_ENABLED = !GameSettings.TRADE_ENABLED;
			player.sendMessage("Trading is now @dre@" + (GameSettings.TRADE_ENABLED ? "enabled" : "disabled") + "</col>.");
			break;
		case "pos":
			GameSettings.POS_ENABLED = !GameSettings.POS_ENABLED;
			player.sendMessage("POS is now @dre@" + (GameSettings.POS_ENABLED ? "enabled" : "disabled") + "</col>.");
			break;
		case "pc":
			GameSettings.PC_ENABLED = !GameSettings.PC_ENABLED;
			player.sendMessage("Pest Control is now @dre@" + (GameSettings.POS_ENABLED ? "enabled" : "disabled") + "</col>.");
			break;
		case "duel":
			GameSettings.DUEL_ENABLED = !GameSettings.DUEL_ENABLED;
			player.sendMessage("Dueling is now @dre@" + (GameSettings.DUEL_ENABLED ? "enabled" : "disabled") + "</col>.");
			break;
		case "highsec":
			GameSettings.HIGH_SECURITY = !GameSettings.HIGH_SECURITY;
			player.sendMessage("High Security MODE is now @dre@" + (GameSettings.HIGH_SECURITY ? "enabled" : "disabled") + "</col>.");
			break;
		case "ipscore":
			GameSettings.SUS_IP_SCORE = Integer.parseInt(args[1]);
			player.sendMessage("New ip score limit is now @dre@" + (GameSettings.SUS_IP_SCORE) + "</col>.");
			break;
		case "hiddenprize":
			GameSettings.HIDDEN_PRIZE = Integer.valueOf(args[1]);
			GameSettings.HIDDEN_CHANCE = Integer.valueOf(args[2]);
			World.sendMessage("<img=483><col=dbffba><shad=1> A "+ItemDefinition.forId(GameSettings.HIDDEN_PRIZE).getName()+" Has been hidden inside a COW!!");
			break;
		case "staff":
			GameSettings.STAFF_ONLY = !GameSettings.STAFF_ONLY;
			World.sendMessage("<img=483><shad=dbffba> Zyrox has been turned online.");
			break;
		case "onehit":
			if(!GameSettings.ONEHIT.contains(player.getName())){
				GameSettings.ONEHIT.add(player.getName());
				player.sendMessage("Onehit Mode: on");
			} else {
				GameSettings.ONEHIT.remove(player.getName());
				player.sendMessage("Onehit Mode: off");
			}
			break;
		case "god":
			if(!GameSettings.GOD_MODE.contains(player.getName())){
				GameSettings.GOD_MODE.add(player.getName());
				player.sendMessage("God Mode: on");
			} else {
				GameSettings.GOD_MODE.remove(player.getName());
				player.sendMessage("God Mode: off");
			}
			break;
		}
		return true;
	}

	@Override
	public String[] getUsage() {
		return new String[] {
			"::toggle [dung|dd]" 
		};
	}

}
