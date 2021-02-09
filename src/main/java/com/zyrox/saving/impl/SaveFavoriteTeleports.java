package com.zyrox.saving.impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

import com.zyrox.saving.SaveObject;
import com.zyrox.world.content.teleport.Teleport;
import com.zyrox.world.content.teleport.TeleportCategory;
import com.zyrox.world.content.teleport.impl.*;
import com.zyrox.world.entity.impl.player.Player;

public class SaveFavoriteTeleports extends SaveObject {

	public SaveFavoriteTeleports(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean save(Player player, BufferedWriter writer) throws IOException {
		writer.write(getName());
		writer.newLine();
		for(Teleport entry: player.getFavoriteTeleports()) {
			if(entry != null) {
				writer.write(entry.getEnumName()+","+entry.getCategory());
				writer.newLine();
			}
		}
		return true;
	}

	@Override
	public void load(Player player, String values, BufferedReader reader) throws IOException {
		// TODO Auto-generated method stub
		String line;
		while ((line = reader.readLine()).length() > 0) {
			String[] split = line.split(",");

			try {
				TeleportCategory teleportCategory = TeleportCategory.valueOf(split[1]);
				switch (teleportCategory) {
					case BOSSES:
						player.getFavoriteTeleports().add(BossTeleport.valueOf(split[0]));
						break;
					case WILDERNESS:
						player.getFavoriteTeleports().add(WildernessTeleport.valueOf(split[0]));
						break;
					case MONSTERS:
						player.getFavoriteTeleports().add(MonsterTeleport.valueOf(split[0]));
						break;
					case MINIGAMES:
						player.getFavoriteTeleports().add(MinigameTeleport.valueOf(split[0]));
						break;
					case DUNGEONS:
						player.getFavoriteTeleports().add(DungeonTeleport.valueOf(split[0]));
						break;
				}
			} catch (Exception e) {

			}
		}
		

	}

}
