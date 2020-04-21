package com.varrock.world.content;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import com.varrock.GameServer;
import com.varrock.GameSettings;
import com.varrock.model.Position;
import com.varrock.world.entity.impl.player.Player;


public class PlayerPunishment {

	public static void addToFile(String file, String data) {
		GameServer.getLoader().getEngine().submit(() -> {
			try {
				BufferedWriter out = new BufferedWriter(new FileWriter(file, true));
				try {
					out.newLine();
					out.write(data);
				} finally {
					out.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}

	public static class Jail {

		private static Player[] JAILED_PLAYERS = new Player[10];

		public static boolean jailPlayer(Player p) {
			int emptyCell = findSlot();
			if(emptyCell == -1) {
				return false;
			}
			Position pos = null;
			switch(emptyCell) {
			case 0:
				pos = new Position(2095, 4429);
				break;
			case 1:
				pos = new Position(2095, 4429);
				break;
			case 2:
				pos = new Position(2095, 4429);
				break;
			case 3:
				pos = new Position(2095, 4429);
				break;
			case 4:
				pos = new Position(2095, 4429);
				break;
			case 5:
				pos = new Position(2095, 4429);
				break;
			case 6:
				pos = new Position(2095, 4429);
				break;
			case 7:
				pos = new Position(2095, 4429);
				break;
			case 8:
				pos = new Position(2095, 4429);
				break;
			case 9:
				pos = new Position(2095, 4429);
				break;
			}
			p.moveTo(pos);
			JAILED_PLAYERS[emptyCell] = p;
			return true;
		}

		public static void unjail(Player plr) {
			int index = getIndex(plr);
			if(index >= 0) {
				JAILED_PLAYERS[index] = null;
			}
			plr.moveTo(GameSettings.DEFAULT_POSITION.copy());
		}

		public static boolean isJailed(Player plr) {
			return getIndex(plr) >= 0;
		}

		public static int getIndex(Player plr) {
			for(int i = 0; i < JAILED_PLAYERS.length; i++) {
				Player p = JAILED_PLAYERS[i];
				if(p == null)
					continue;
				if(p == plr) {
					return i;
				}
			}
			return -1;
		}

		public static int findSlot() {
			for(int i = 0; i < JAILED_PLAYERS.length; i++) {
				if(JAILED_PLAYERS[i] == null) {
					return i;
				}
			}
			return -1;
		}

	}

}
