package com.zyrox.world.content.skill.impl.slayer;

import com.zyrox.model.Position;
import com.zyrox.model.Skill;
import com.zyrox.util.Misc;
import com.zyrox.util.NameUtils;
import com.zyrox.world.content.PlayerPanel;
import com.zyrox.world.content.skill.impl.slayer.KonarQuoMaten.SlayerTask;
import com.zyrox.world.entity.impl.player.Player;

public enum SlayerMaster {
	
	VANNAKA(1, 1597, new Position(2724, 2524)),
	DURADEL(50, 8275, new Position(2723, 2524)),
	KRYSTILIA(50, 22663, new Position(2722, 2524)),
	KONAR_QUO_MATEN(75, 23623, new Position(2721, 2524)),
	KURADEL(80, 9085, new Position(2720, 2524)),
	SUMONA(92, 7780, new Position(2719, 2524));

	// Old Rooms
	/*VANNAKA(1, 1597, new Position(3145, 9912)),
	DURADEL(50, 8275, new Position(2826, 2958)),
	KRYSTILIA(50, 22663, new Position(3111, 3514)),
	KONAR_QUO_MATEN(75, 23623, new Position(1748, 5326)),
	KURADEL(80, 9085, new Position(1748, 5326)),
	SUMONA(92, 7780, new Position(3068, 3518));*/
	
	private SlayerMaster(int slayerReq, int npcId, Position telePosition) {
		this.slayerReq = slayerReq;
		this.npcId = npcId;
		this.position = telePosition;
	}
	
	private int slayerReq;
	private int npcId;
	private Position position;
	
	public int getSlayerReq() {
		return this.slayerReq;
	}
	
	public int getNpcId() {
		return this.npcId;
	}

	public String getName() {
		return NameUtils.capitalizeWords(name().replaceAll("_", " ").toLowerCase());
	}
	
	public Position getPosition() {
		return this.position;
	}
	
	public static SlayerMaster forId(int id) {
		for (SlayerMaster master : SlayerMaster.values()) {
			if (master.ordinal() == id) {
				return master;
			}
		}
		return null;
	}
	
	public static void changeSlayerMaster(Player player, SlayerMaster master) {
		player.getPacketSender().sendInterfaceRemoval();
		if(player.getSkillManager().getCurrentLevel(Skill.SLAYER) < master.getSlayerReq()) {
			player.getPacketSender().sendMessage("This Slayer master does not teach noobies. You need a Slayer level of at least "+master.getSlayerReq()+".");
			return;
		}
		if(player.getSlayer().getSlayerTask() != SlayerTasks.NO_TASK
				|| !player.getSlayer().getTask().equals(SlayerTask.NO_TASK)) {
			player.getPacketSender().sendMessage("Please finish your current task before changing Slayer master.");
			return;
		}
		if(player.getSlayer().getSlayerMaster() == master) {
			player.getPacketSender().sendMessage(""+Misc.formatText(master.toString().toLowerCase())+" is already your Slayer master.");
			return;
		}
		player.getSlayer().setSlayerMaster(master);
		PlayerPanel.refreshPanel(player);
		player.getPacketSender().sendMessage("You've sucessfully changed Slayer master to " + master.getName() + ".");
	}
}
