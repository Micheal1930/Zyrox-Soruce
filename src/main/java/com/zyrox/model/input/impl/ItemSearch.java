package com.zyrox.model.input.impl;

import com.zyrox.engine.task.Task;
import com.zyrox.engine.task.TaskManager;
import com.zyrox.model.definitions.ItemDefinition;
import com.zyrox.model.definitions.NPCDrops;
import com.zyrox.model.definitions.NpcDefinition;
import com.zyrox.model.definitions.NPCDrops.NpcDropItem;
import com.zyrox.model.input.Input;
import com.zyrox.world.content.dialogue.DialogueManager;
import com.zyrox.world.content.dialogue.impl.ExplorerJack;
import com.zyrox.world.entity.impl.player.Player;

public class ItemSearch extends Input {

	@Override
	public void handleSyntax(Player player, final String syntax) {
		DialogueManager.start(player, 72);
		player.getMovementQueue().setLockMovement(true);
		TaskManager.submit(new Task(5, player, false) {
			@Override
			protected void execute() {
				try {
					String syntaxCopy = syntax;
					Object[] data = getFixedSyntax(syntaxCopy);
					syntaxCopy = (String) data[0];

					player.getPacketSender().sendInterfaceRemoval();
					if(syntaxCopy.length() <= 3) {
						DialogueManager.start(player, 74);
						return;
					}

					int itemId = (int) data[1];
					int npcDropId = -1;
					
					for(ItemDefinition def : ItemDefinition.getDefinitions()) {
						if(def != null) {
							if(def.getName().contains(syntaxCopy) && itemId == -1) {
								itemId = def.getId();
							}
							if(def.getName().equalsIgnoreCase(syntaxCopy)) {
								itemId = def.getId();
								break;
							}
						}
					}
					
					if(itemId > 0 && ItemDefinition.forId(itemId).isNoted()) {
						itemId--;
					}
					
					if(itemId == 14486) {
						itemId = 14484;
					}

					if(itemId > 0) {
						for(NPCDrops npcDrops : NPCDrops.getDrops().values()) {
							if(npcDrops != null) {
								for(NpcDropItem item : npcDrops.getDropList()) {
									if(item != null && item.getId() == itemId) {
										for (int npcId : npcDrops.getNpcIds()) {
											if(npcId == -1)
												continue;
											if(NpcDefinition.forId(npcId) != null && !NpcDefinition.forId(npcId).getName().equalsIgnoreCase("null")) {
												npcDropId = npcId;
												break;
											}
										}
									}
								}
							}
						}
					}

					if(itemId == -1 || npcDropId == -1) {
						DialogueManager.start(player, 73);
					} else {
						DialogueManager.start(player, ExplorerJack.foundDrop(syntaxCopy, NpcDefinition.forId(npcDropId).getName()));
					}
				} catch(Exception e) {}

				stop();
			}

			@Override
			public void stop() {
				setEventRunning(false);
				player.getMovementQueue().setLockMovement(false);
			}
		});
	}


	public static Object[] getFixedSyntax(String searchSyntax) {
		searchSyntax = searchSyntax.toLowerCase();
		switch(searchSyntax) {
		case "ags":
			return new Object[]{"armadyl godsword", 11694};
		case "sgs":
			return new Object[]{"saradomin godsword", 11698};
		case "bgs":
			return new Object[]{"bandos godsword", 11696};
		case "zgs":
			return new Object[]{"zamorak godsword", 11700};
		case "dclaws":
		case "d claws":
			return new Object[]{"dragon claws", 14484};
		case "bcp":
			return new Object[]{"bandos chestplate", 11724};
		case "dds":
			return new Object[]{"dragon dagger", 1215};
		case "sol":
			return new Object[]{"staff of light", 15486};
		case "vls":
			return new Object[]{"vesta's longsword", 13899};
		case "tassy":
			return new Object[]{"bandos tassets", 11726};
		case "swh":
			return new Object[]{"statius's warhammer", 13902};
		case "steads":
			return new Object[]{"steadfast boots", 20000};
		case "obby maul":
			return new Object[]{"Tthaar-ket-om", 6528};
		case "g maul":
		case "gmaul":
			return new Object[]{"granite maul", 4153};
		case "nat":
			return new Object[]{"nature rune", 561};
		case "dfs":
			return new Object[]{"dragonfire shield", 11283};
			case "dkite":
			return new Object[]{"dragon kiteshield", 11613};
		case "dbones":
			return new Object[]{"dragon bones", 536};
		case "dboots":
		case "d boots":
			return new Object[]{"dragon boots", 11732};
		}
		return new Object[]{searchSyntax, -1};
	}
}
