package com.varrock.world.content;

import com.varrock.engine.task.Task;
import com.varrock.engine.task.TaskManager;
import com.varrock.model.Animation;
import com.varrock.model.Position;
import com.varrock.world.content.treasuretrails.CoordinateScrolls;
import com.varrock.world.content.treasuretrails.DiggingScrolls;
import com.varrock.world.content.treasuretrails.EliteClueScroll;
import com.varrock.world.content.treasuretrails.MapScrolls;
import com.varrock.world.entity.impl.player.Player;

public class Digging {

	public static void dig(final Player player) {
		if (!player.getClickDelay().elapsed(2000))
			return;
		player.getMovementQueue().reset();
		player.getPacketSender().sendMessage("You start digging..");
		player.performAnimation(new Animation(830));
		TaskManager.submit(new Task(2, player, false) {
			@Override
			public void execute() {
				if (EliteClueScroll.dig(player)) {
					stop();
					return;
				}

				/**
				 * Clue scrolls
				 */
				if (DiggingScrolls.digClue(player) || MapScrolls.digClue(player) || CoordinateScrolls.digClue(player)) {
					stop();
					return;
				}

				/** clue scrolls** x,y **/
				if (inClue(player.getPosition(), 3096, 3496) && player.getInventory().contains(2677)) {
					player.getInventory().delete(2677, 1);
					player.getInventory().add(2714, 1, "Casket digging");
					ClueScrolls.incrementCluesCompleted(1);
					player.getPacketSender().sendMessage("You found a casket, open it for your reward!");
				} else if (inClue(player.getPosition(), 3028, 9741) && player.getInventory().contains(2678)) {
					player.getInventory().delete(2678, 1);
					player.getInventory().add(2714, 1, "Casket digging");
					ClueScrolls.incrementCluesCompleted(1);
					player.getPacketSender().sendMessage("You found a casket, open it for your reward!");
				} else if (inClue(player.getPosition(), 3366, 3267) && player.getInventory().contains(2679)) {
					player.getInventory().delete(2679, 1);
					player.getInventory().add(2714, 1, "Casket digging");
					ClueScrolls.incrementCluesCompleted(1);
					player.getPacketSender().sendMessage("You found a casket, open it for your reward!");
				} else if (inClue(player.getPosition(), 3145, 9915) && player.getInventory().contains(2680)) {
					player.getInventory().delete(2680, 1);
					player.getInventory().add(2714, 1, "Casket digging");
					ClueScrolls.incrementCluesCompleted(1);
					player.getPacketSender().sendMessage("You found a casket, open it for your reward!");
				} else if (inClue(player.getPosition(), 2341, 3698) && player.getInventory().contains(2681)) {
					player.getInventory().delete(2681, 1);
					player.getInventory().add(2714, 1, "Casket digging");
					ClueScrolls.incrementCluesCompleted(1);
					player.getPacketSender().sendMessage("You found a casket, open it for your reward!");
				} else if (inClue(player.getPosition(), 3451, 3717) && player.getInventory().contains(2682)) {
					player.getInventory().delete(2682, 1);
					player.getInventory().add(2714, 1, "Casket digging");
					ClueScrolls.incrementCluesCompleted(1);
					player.getPacketSender().sendMessage("You found a casket, open it for your reward!");
				} else if (inClue(player.getPosition(), 2280, 4697) && player.getInventory().contains(2683)) {
					player.getInventory().delete(2683, 1);
					player.getInventory().add(2714, 1, "Casket digging");
					ClueScrolls.incrementCluesCompleted(1);
					player.getPacketSender().sendMessage("You found a casket, open it for your reward!");
				} else if (inClue(player.getPosition(), 2660, 2651) && player.getInventory().contains(2684)) {
					player.getInventory().delete(2684, 1);
					player.getInventory().add(2714, 1, "Casket digging");
					ClueScrolls.incrementCluesCompleted(1);
					player.getPacketSender().sendMessage("You found a casket, open it for your reward!");
				} else if (inClue(player.getPosition(), 2441, 3096) && player.getInventory().contains(2685)) {
					player.getInventory().delete(2685, 1);
					player.getInventory().add(2714, 1, "Casket digging");
					ClueScrolls.incrementCluesCompleted(1);
					player.getPacketSender().sendMessage("You found a casket, open it for your reward!");
				} else if (inClue(player.getPosition(), 3209, 3435) && player.getInventory().contains(2686)) {
					player.getInventory().delete(2686, 1);
					player.getInventory().add(2714, 1, "Casket digging");
					ClueScrolls.incrementCluesCompleted(1);
					player.getPacketSender().sendMessage("You found a casket, open it for your reward!");
				} else if (inClue(player.getPosition(), 3231, 3299) && player.getInventory().contains(2687)) {
					player.getInventory().delete(2687, 1);
					player.getInventory().add(2714, 1, "Casket digging");
					ClueScrolls.incrementCluesCompleted(1);
					player.getPacketSender().sendMessage("You found a casket, open it for your reward!");
				} else if (inClue(player.getPosition(), 3564, 3289) && player.getInventory().contains(2688)) {
					player.getInventory().delete(2688, 1);
					player.getInventory().add(2714, 1, "Casket digging");
					ClueScrolls.incrementCluesCompleted(1);
					player.getPacketSender().sendMessage("You found a casket, open it for your reward!");
				} else if (inClue(player.getPosition(), 2833, 9564) && player.getInventory().contains(2689)) {
					player.getInventory().delete(2689, 1);
					player.getInventory().add(2714, 1, "Casket digging");
					ClueScrolls.incrementCluesCompleted(1);
					player.getPacketSender().sendMessage("You found a casket, open it for your reward!");
				} else if (inClue(player.getPosition(), 3413, 3537) && player.getInventory().contains(2690)) {
					player.getInventory().delete(2690, 1);
					player.getInventory().add(2714, 1, "Casket digging");
					ClueScrolls.incrementCluesCompleted(1);
					player.getPacketSender().sendMessage("You found a casket, open it for your reward!");
				} else {
					/**
					 * Barrows
					 */
					Position targetPosition = null;
					if (inArea(player.getPosition(), 3553, 3301, 3561, 3294)) {
						targetPosition = new Position(3578, 9706, -1);
					} else if (inArea(player.getPosition(), 3550, 3287, 3557, 3278)) {
						targetPosition = new Position(3568, 9683, -1);
					} else if (inArea(player.getPosition(), 3561, 3292, 3568, 3285)) {
						targetPosition = new Position(3557, 9703, -1);
					} else if (inArea(player.getPosition(), 3570, 3302, 3579, 3293)) {
						targetPosition = new Position(3556, 9718, -1);
					} else if (inArea(player.getPosition(), 3571, 3285, 3582, 3278)) {
						targetPosition = new Position(3534, 9704, -1);
					} else if (inArea(player.getPosition(), 3562, 3279, 3569, 3273)) {
						targetPosition = new Position(3546, 9684, -1);
					} else if (inArea(player.getPosition(), 2986, 3370, 3013, 3388)) {
						targetPosition = new Position(3546, 9684, -1);
					}
					if (targetPosition != null) {
						player.moveTo(targetPosition);
					}
				}
				stop();
			}
		});
		player.getClickDelay().reset();
	}

	private static boolean inArea(Position pos, int x, int y, int x1, int y1) {
		return pos.getX() > x && pos.getX() < x1 && pos.getY() < y && pos.getY() > y1;
	}

	private static boolean inClue(Position pos, int x, int y) {
		return pos.getX() == x && pos.getY() == y;
	}
}
