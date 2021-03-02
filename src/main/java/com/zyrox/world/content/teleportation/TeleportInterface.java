package com.zyrox.world.content.teleportation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.CopyOnWriteArrayList;

import com.zyrox.engine.task.Task;
import com.zyrox.engine.task.TaskManager;
import com.zyrox.model.Item;
import com.zyrox.model.Position;
import com.zyrox.model.RegionInstance;
import com.zyrox.model.RegionInstance.RegionInstanceType;
import com.zyrox.world.World;
import com.zyrox.world.content.dialogue.Dialogue;
import com.zyrox.world.content.dialogue.DialogueExpression;
import com.zyrox.world.content.dialogue.DialogueType;
import com.zyrox.world.content.transportation.TeleportHandler;
import com.zyrox.world.content.transportation.TeleportType;
import com.zyrox.world.entity.impl.npc.NPC;
import com.zyrox.world.entity.impl.npc.NPCMovementCoordinator.Coordinator;
import com.zyrox.world.entity.impl.player.Player;

public class TeleportInterface {
	
	private Player player;
	private TeleportCategory currentCategory = TeleportCategory.BOSSES;
	private TeleportData currentTeleport, previousTeleport;
	private ArrayList<TeleportData> favorites = new ArrayList<TeleportData>();
	private boolean instance;
	
	public TeleportInterface(Player player) {
		this.player = player;
	}
	
	public void open(TeleportData data) {
		if(player.getInterfaceId() == 55500 && getCurrentTeleport() == data) {
			player.sendMessage("You have already selected the " + data.getName() + " teleport.");
			return;
		}
		refresh(data);
		player.getPacketSender().sendInterface(55500);
	}
	
	public void refresh(TeleportData data) {
		setCategory(data.getCategory());
		populateData();
		sendDescription(data);
	}
	
	public void sendTab(int identifier) {
		TeleportData data = TeleportData.findFirst(TeleportCategory.values()[identifier]);
		if(data == null) {
			player.sendMessage("There are no teleports added to this category yet.");
			return;
		}
		refresh(data);
	}
	
	public void sendDescription(TeleportData data) {
		if(data.getItems() != null)
			player.getPacketSender().sendInterfaceItems(55607, getItemList(data));
		if(data.getNPCID() > 0)
			player.getPA().sendNpcOnInterface(data.getNPCID(), (data.getNPCZoom() > 0 ? data.getNPCZoom() : 2500));
		setCurrentTeleport(data);
		player.getPacketSender().sendNPCDescription(data.getDescription().getDescription());
	}
	
	public void populateData() {
		player.getPacketSender().sendString(55609, getCategory().getName());
		int index = 55536;
		for(TeleportData data : TeleportData.values()) {
			if(data == null)
				continue;
			if(data.getCategory() != getCategory())
				continue;
			player.getPacketSender().sendString(data.getName(), index);
			player.getPacketSender().sendToggle(1200 + (index - 55536), (player.getTeleportInterface().getFavourites().contains(data) ? 1 : 0));
			index++;
		}
		refreshFavourites();
	}
	
	public void selectFavorite(int button) {
		int index = (button - 55577) / 3;
		if(getFavourites().size() <= index) {
			player.sendMessage("You do not have a favourite to select.");
			return;
		}
		open(getFavourites().get(index));
	}
	
	public void refreshFavourites() {
		int favorites = getFavourites().size();
		for(int i = 55577, k = 0; i <= 55604; i+=3, k++)
			player.getPacketSender().sendString(i, (k < favorites ? getFavourites().get(k).getName() : TeleportData.DEFAULT.getName()));
	}
	
	public void setFavourite(int button) {
		if(getFavourites().size() >= 10) {
			player.sendMessage("You have reached the maximum amount of favourites.");
			return;
		}
		int id = button - 55556;
		TeleportData data = TeleportData.findSpot(getCategory(), id);
		if(data == null) {
			player.sendMessage("Please select a different favourite option.");
			populateData();
			return;
		}
		if(getFavourites().contains(data)) {
			player.sendMessage("You already have " + data.getName() + " in your favourites list!");
			populateData();
			return;
		}
		getFavourites().add(data);
		player.sendMessage("You add " + data.getName() + " to your favourites list!");
		refreshFavourites();
		populateData();
	}
	
	public void removeFavourite(int button) {
		int index = (button - 55578 > 0 ? (button - 55578) / 3 : 0);
		if(getFavourites().size() <= index) {
			player.sendMessage("You do not have a favourite selected.");
			return;
		}
		TeleportData data = getFavourites().get(index);
		getFavourites().remove(data);
		player.sendMessage("You sucessfully removed " + data.getName() + " from your favourites.");
		refreshFavourites();
		populateData();
	}
	
	public void teleport(TeleportData data) {
		if(data == null) {
			player.sendMessage("You don't have a teleport selected.");
			return;
		}
		if(isInstance() && data.isInstanced()) {
			handleInstanceTeleport();
			return;
		}
		player.sendMessage("You attempt to teleport to " + data.getName() + ".");
		TeleportHandler.teleportPlayer(player, data.getPosition(), TeleportType.NORMAL);
		setPreviousTeleport(data);
	}
	
	public boolean handleButtonClick(int button) {
		if(player.getInterfaceId() != 55500)
			return false;
		if(button == 55504) {
			teleport(getCurrentTeleport());
			return true;
		}
		if(button == 55507) {
			teleport(getPreviousTeleport());
			return true;
		}
		if(button >= 55556 && button <= 55575) {
			setFavourite(button);
			return true;
		}
		if(button == 55614) {
			setInstance(!isInstance());
			player.sendMessage("You have toggled instances " + (isInstance() ? "on" : "off") + ".");
			player.getPacketSender().sendToggle(1250, (isInstance() ? 1 : 0)); 
			return true;
		}
		if(button >= 55536 && button <= 55555) {
			TeleportData data = TeleportData.findSpot(getCategory(), button - 55536);
			if(data == null) {
				player.sendMessage("Please select a different teleport.");
				return true;
			}
			open(data);
			return true;
		}
		if((55604 - button) % 3 == 0) {
			selectFavorite(button);
			return true;
		}
		if(button % 3 == 0) {
			removeFavourite(button);
			return true;
		}
		return false;
	}
	
	public void handleInstanceTeleport() {
		TeleportData data = getCurrentTeleport();
		if(player.getRegionInstance() != null) {
			player.getRegionInstance().destruct();
		}
		NPC npc = new NPC(data.getNPCID(), new Position(2715, 9186, player.getIndex() * 4));
		TeleportHandler.teleportPlayer(player, new Position(2714, 9174, player.getIndex() * 4), player.getSpellbook().getTeleportType());
		player.sendMessage("Starting instance for " + npc.getDefinition().getName() + "...");
		TaskManager.submit(new Task(1) {
			int tick = 0;
			@Override
			protected void execute() {
				player.sendMessage("Spawning " + npc.getDefinition().getName() + " in " + (7 - tick) + "...");
				if(tick == 7) {
					player.setRegionInstance(new RegionInstance(player, RegionInstanceType.BOSS_INSTANCE));
					npc.setInstancedPlayer(player);
					npc.getMovementCoordinator().setCoordinator(new Coordinator(false, -1));
					npc.getMovementQueue().setLockMovement(true);
					npc.getDefinition().setAggressive(true);
					player.getRegionInstance().getNpcsList().add(npc);
					World.register(npc);
					setPreviousTeleport(data);
					stop();
				}
				tick++;
			}
		});
		player.sendMessage("@red@Warning! @blu@Items are lost on Death!");
		player.sendMessage("@blu@Please do not bring anything you are @red@NOT @blu@willing to lose.");
	}
	
	public CopyOnWriteArrayList<Item> getItemList(TeleportData data) {
		CopyOnWriteArrayList<Item> items = new CopyOnWriteArrayList<Item>();
		Collections.addAll(items, data.getItems());
		return items;
	}
	
	public TeleportCategory getCategory() {
		return this.currentCategory;
	}
	
	public void setCategory(TeleportCategory category) {
		this.currentCategory = category;
	}
	
	public ArrayList<TeleportData> getFavourites() {
		return this.favorites;
	}
	
	public void setFavorites(ArrayList<TeleportData> favorites) {
		this.favorites = favorites;
	}
	
	public TeleportData getCurrentTeleport() {
		return this.currentTeleport;
	}
	
	public void setCurrentTeleport(TeleportData currentTeleport) {
		this.currentTeleport = currentTeleport;
	}
	
	public TeleportData getPreviousTeleport() {
		return this.previousTeleport;
	}
	
	public void setPreviousTeleport(TeleportData previousTeleport) {
		this.previousTeleport = previousTeleport;
	}
	
	public boolean isInstance() {
		return this.instance;
	}
	
	public void setInstance(boolean instance) {
		this.instance = instance;
	}

}
