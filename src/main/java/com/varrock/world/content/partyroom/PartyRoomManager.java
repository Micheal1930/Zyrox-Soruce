package com.varrock.world.content.partyroom;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Objects;

import com.varrock.GameSettings;
import com.varrock.engine.task.Task;
import com.varrock.engine.task.TaskManager;
import com.varrock.model.Animation;
import com.varrock.model.Boundary;
import com.varrock.model.GameMode;
import com.varrock.model.GameObject;
import com.varrock.model.Graphic;
import com.varrock.model.GroundItem;
import com.varrock.model.Item;
import com.varrock.model.Position;
import com.varrock.util.Misc;
import com.varrock.world.World;
import com.varrock.world.content.BankPin;
import com.varrock.world.content.CustomObjects;
import com.varrock.world.content.dialogue.DialogueManager;
import com.varrock.world.entity.impl.GroundItemManager;
import com.varrock.world.entity.impl.npc.NPC;
import com.varrock.world.entity.impl.player.Player;

/**
 * Handles the Party Room
 * 
 * @author 2012 <https://www.rune-server.ee/members/dexter+morgan/>
 * 
 */
public class PartyRoomManager {

	/**
	 * The party room manager
	 * 
	 * @param player the player
	 */
	public PartyRoomManager(Player player) {
		setDeposit(new PartyRoomDepositItemContainer(player));
	}

	/**
	 * The dialogue
	 */
	private static final PartyRoomLeverDialogue DIALOGUE = new PartyRoomLeverDialogue();

	/**
	 * The confirm dialogue
	 */
	private static final PartyRoomConfirmDepositDialogue CONFIRM = new PartyRoomConfirmDepositDialogue();

	/**
	 * The chest
	 */
	private static PartyRoomChestItemContainer chest = new PartyRoomChestItemContainer();

	/**
	 * The deposit container
	 */
	private PartyRoomDepositItemContainer deposit;

	/**
	 * Pulling lever
	 */
	private static final Animation PULL_LEVER = new Animation(2140);

	/**
	 * Popping balloons
	 */
	private static final Animation POP_BALLOON = new Animation(794);

	/**
	 * The balloon popping
	 */
	private static final Graphic BALLOON_POP = new Graphic(524);

	/**
	 * The boundaries in which the balloons can fall in
	 */
	private static final Boundary[] BALLOON_BOUNDARIES = {
			new Boundary(new Position(2730, 3462, 0), new Position(2744, 3467, 0)),
			new Boundary(new Position(2730, 3469, 0), new Position(2744, 3475, 0)), };

	/**
	 * The white knights sayings
	 */
	private static final String[] WHITE_KNIGHT_SHOUT = { "We're knights of the party room",
			"We dance round and round like a loon", "Quite often we like to sing", "Unfortunately we make a din",
			"We're knights of the party room", "Do you like our helmet plumes?", "Everyone's happy now we can move",
			"Like a party animal in the groove" };

	/**
	 * The balloons
	 */
	private static ArrayList<Position> balloons = new ArrayList<Position>();

	/**
	 * The balloon cost
	 */
	public static final Item BALLOON_COST = new Item(995, 1000);

	/**
	 * The white knight costs
	 */
	public static final Item WHITE_KNIGHTS_COST = new Item(995, 500);

	/**
	 * The chance of getting an item
	 */
	private static final int CHANCE = 7;

	/**
	 * How often it can be pulled in minutes
	 */
	private static final int BALLOON_INTERVAL = 10;

	/**
	 * The chest id
	 */
	public static final int CHEST = 26193;

	/**
	 * The lever id
	 */
	public static final int LEVER = 26194;

	/**
	 * The white knight id
	 */
	public static final int WHITE_KNIGHT = 660;

	/**
	 * The balloon id
	 */
	private static final int BALLOON_ID = 115;

	/**
	 * The range of balloons
	 */
	private static final int BALLOON_RANGE = 7;

	/**
	 * The chest inventory id
	 */
	public static final int CHEST_INVENTORY = 2273;

	/**
	 * The adding inventory
	 */
	public static final int DEPOSIT_INVENTORY = 2274;

	/**
	 * The inventory items
	 */
	public static final int INVENTORY_ITEMS = 2006;

	/**
	 * The inventory id
	 */
	public static final int MAIN_INVENTORY = 2005;

	/**
	 * The interface id
	 */
	public static final int MAIN_INTERFACE = 2156;

	/**
	 * The dialogue action id
	 */
	public static final int DIALOGUE_ACTION_ID = 1_900;

	/**
	 * The white knights dancing
	 */
	private static boolean whiteKnights;

	/**
	 * The ballooon delay
	 */
	private static long lastBalloons;

	/**
	 * Opening the party chest
	 * 
	 * @param player the player
	 */
	public static void open(Player player) {
		/*
		 * Main screen
		 */
		player.getPacketSender().sendItemContainer(chest, CHEST_INVENTORY);
		/*
		 * Deposit inventory
		 */
		player.getPacketSender().sendItemContainer(player.getInventory(), INVENTORY_ITEMS);
		/*
		 * Deposit
		 */
		player.getPartyRoom().getDeposit().refreshItems();
		/*
		 * Main interface
		 */
		player.getPacketSender().sendString(2275, "                        Total Chest Wealth: @or1@"
				+ NumberFormat.getInstance().format(chest.getWealth()) + " gp");
		player.getPacketSender().sendInterfaceSet(MAIN_INTERFACE, MAIN_INVENTORY);
	}

	/**
	 * Sending the dialogue
	 * 
	 * @param player the player
	 */
	public static void sendLever(Player player) {
		player.setDialogueActionId(DIALOGUE_ACTION_ID);
		DialogueManager.start(player, DIALOGUE);
	}

	/**
	 * Sending the confirm dialogue
	 * 
	 * @param player the player
	 */
	public static void sendConfirmation(Player player) {
		if (player.getPartyRoom().getDeposit().isEmpty()) {
			player.getPacketSender().sendMessage("There are no items to accept.");
			return;
		}
		DialogueManager.start(player, CONFIRM);
		player.setDialogueActionId(DIALOGUE_ACTION_ID + 1);
	}

	/**
	 * Depositing items to chest
	 * 
	 * @param player the player
	 * @param item   the item
	 * @param slot   the slot
	 */
	public static void deposit(Player player, Item item, int slot) {
		if(!GameSettings.PARTY_ROOM_ENABLED){
			player.sendMessage("Party room is disabled at the moment.");
			return;
		}
		/*
		 * Invalid amount
		 */
		if (item.getAmount() < 1) {
			return;
		}
		/*
		 * Doesn't exist
		 */
		if (!player.getInventory().slotContainsItem(slot, item.getId())) {
			return;
		}
		/*
		 * No space
		 */
		if (chest.getFreeSlots() == 0) {
			player.getPacketSender().sendMessage("The chest is full");
			return;
		}
		/*
		 * No space
		 */
		if (player.getPartyRoom().getDeposit().getFreeSlots() == 0) {
			player.getPacketSender().sendMessage("You can't add anymore items. Either accept or remove items.");
			return;
		}
		/*
		 * Untradeables
		 */
		for (int i : GameSettings.UNTRADEABLE_ITEMS) {
			if (i == item.getId()) {
				player.sendMessage("You can't trade this item.");
				return;
			}
		}
		/*
		 * The amount
		 */
		int amount = item.getAmount();
		/*
		 * Item is stackable
		 */
		if (item.getDefinition().isStackable()) {
			/*
			 * Fixes amount in slot
			 */
			if (amount > player.getInventory().getAmountForSlot(slot)) {
				amount = player.getInventory().getAmountForSlot(slot);
			}
			/*
			 * No available slots
			 */
			if (!player.getPartyRoom().getDeposit().contains(item.getId())
					&& player.getPartyRoom().getDeposit().getFreeSlots() == 0) {
				return;
			}
		} else {
			/*
			 * Fixes existing amount
			 */
			if (amount > player.getInventory().getAmount(item.getId())) {
				amount = player.getInventory().getAmount(item.getId());
			}
			/*
			 * Fixes amount available
			 */
			if (amount > chest.getFreeSlots()) {
				amount = chest.getFreeSlots();
			}
			/*
			 * Fixes deposit amount available
			 */
			if (amount > player.getPartyRoom().getDeposit().getFreeSlots()) {
				amount = player.getPartyRoom().getDeposit().getFreeSlots();
			}
		}
		/*
		 * The existing amount in chest
		 */
		long existing = chest.getAmount(item.getId());
		/*
		 * Exceeds value so fix
		 */
		if (existing + amount > Integer.MAX_VALUE) {
			amount = (int) (Integer.MAX_VALUE - existing);
		}
		/*
		 * None available to add
		 */
		if (amount == 0) {
			return;
		}
		/*
		 * Depositing the items
		 */
		Item deposit = new Item(item.getId(), amount);
		/*
		 * Delete from inventory
		 */
		player.getInventory().delete(deposit);
		/*
		 * Add to deposit container
		 */
		player.getPartyRoom().getDeposit().add(deposit).refreshItems();
		player.getPacketSender().sendItemContainer(player.getInventory(), INVENTORY_ITEMS);
	}

	/**
	 * Withdrawing items from deposit
	 * 
	 * @param player the player
	 * @param item   the item
	 * @param slot   the slot
	 */
	public static void withdraw(Player player, Item item, int slot) {
		/*
		 * Doesn't exist
		 */
		if (!player.getPartyRoom().getDeposit().slotContainsItem(slot, item.getId())) {
			return;
		}
		/*
		 * The amount
		 */
		int amount = item.getAmount();
		/*
		 * Item is stackable
		 */
		if (item.getDefinition().isStackable()) {
			/*
			 * Fixes amount in slot
			 */
			if (amount > player.getPartyRoom().getDeposit().getAmountForSlot(slot)) {
				amount = player.getPartyRoom().getDeposit().getAmountForSlot(slot);
			}
			/*
			 * No available slots
			 */
			if (!player.getInventory().contains(item.getId()) && player.getInventory().getFreeSlots() == 0) {
				return;
			}
		} else {
			/*
			 * Fixes existing amount
			 */
			if (amount > player.getPartyRoom().getDeposit().getAmount(item.getId())) {
				amount = player.getPartyRoom().getDeposit().getAmount(item.getId());
			}
			/*
			 * Fixes amount available
			 */
			if (amount > chest.getFreeSlots()) {
				amount = chest.getFreeSlots();
			}
			/*
			 * Fixes deposit amount available
			 */
			if (amount > player.getInventory().getFreeSlots()) {
				amount = player.getInventory().getFreeSlots();
			}
		}
		/*
		 * None available to add
		 */
		if (amount == 0) {
			return;
		}
		/*
		 * Withdraw the items
		 */
		Item withdraw = new Item(item.getId(), amount);
		/*
		 * Delete from inventory
		 */
		player.getPartyRoom().getDeposit().delete(withdraw);
		/*
		 * Add to deposit container
		 */
		player.getInventory().add(withdraw);
		/*
		 * Refresh inventory
		 */
		player.getPartyRoom().getDeposit().refreshItems();
		/*
		 * Refresh deposit inventory
		 */
		player.getPacketSender().sendItemContainer(player.getInventory(), INVENTORY_ITEMS);
	}

	/**
	 * Accepting the items
	 * 
	 * @param player the player
	 */
	public static void accept(Player player) {
		/*
		 * No items
		 */
		if (player.getPartyRoom().getDeposit().isEmpty()) {
			player.getPacketSender().sendMessage("There are no items to accept.");
			return;
		}
		/*
		 * Adds items
		 */
		player.getPartyRoom().getDeposit().getValidItems().stream().filter(Objects::nonNull).forEach(item -> {
			chest.add(item);
		});
		/*
		 * Refresh deposit
		 */
		player.getPartyRoom().getDeposit().resetItems().refreshItems();
		/*
		 * Refresh chest
		 */
		player.getPacketSender().sendItemContainer(chest, CHEST_INVENTORY);
		/*
		 * Update players
		 */
		updateLocalPlayers(player);
	}

	/**
	 * Pulling the lever
	 * 
	 * @param player the player
	 */
	public static void sendBalloons(Player player) {
		/*
		 * Delay
		 */
		if (!player.getRights().isStaff()) {
			if (System.currentTimeMillis() - lastBalloons < BALLOON_INTERVAL * 60_000) {
				player.getPacketSender().sendMessage("The balloons cannot be pulled right now.");
				return;
			}
		}
		/*
		 * The chest is empty
		 */
		if (chest.isEmpty()) {
			player.getPacketSender().sendMessage("You need to add some items to the chest before pulling it.");
			return;
		}
		/*
		 * Cost
		 */
		if (!player.getInventory().contains(BALLOON_COST)) {
			player.getPacketSender()
					.sendMessage("It costs " + BALLOON_COST.getAmount() + " gp to pull the lever for balloons.");
			return;
		}
		/*
		 * Delete cost
		 */
		player.getInventory().delete(BALLOON_COST);
		player.performAnimation(PULL_LEVER);
		lastBalloons = System.currentTimeMillis();
		/*
		 * Announce
		 */
		World.sendMessage("<img=483> @blu@[ Party Room ]: @bla@[" + player.getUsername() + "] has pulled the lever! "
				+ Misc.getTotalAmount(chest.getWealth()) + " GP Drop Party starts now!");
		/*
		 * Dropping balloons
		 */
		sendBalloons();
	}

	/**
	 * Sending white knights
	 * 
	 * @param player the player
	 */
	public static void sendWhiteKnights(Player player) {
		/*
		 * Already pulled
		 */
		if (whiteKnights) {
			player.getPacketSender().sendMessage("The White Knights are already dancing.");
			return;
		}
		/*
		 * Cost
		 */
		if (!player.getInventory().contains(WHITE_KNIGHTS_COST)) {
			player.getPacketSender().sendMessage("It costs " + WHITE_KNIGHTS_COST.getAmount()
					+ " gp to pull the lever for the White Knights to dance.");
			return;
		}
		/*
		 * Delete cost
		 */
		player.getInventory().delete(WHITE_KNIGHTS_COST);
		/*
		 * Spawn them
		 */
		for (int x = 2735; x < 2741; x++) {
			NPC knight = NPC.of(WHITE_KNIGHT, new Position(x, 3468));
			World.register(knight);
			TaskManager.submit(new Task(5) {

				int amount = 0;

				@Override
				protected void execute() {

					if (amount == WHITE_KNIGHT_SHOUT.length - 1) {
						stop();
						whiteKnights = false;
						World.deregister(knight);
						return;
					}
					knight.forceChat(WHITE_KNIGHT_SHOUT[amount]);
					amount++;
				}
			});
		}
	}

	/**
	 * Bursting a balloon
	 * 
	 * @param player     the player
	 * @param gameObject the object
	 * @return bursting balloon
	 */
	public static void burstBalloon(Player player, GameObject object) {
		if (player == null || player.getConstitution() <= 0 || player.isTeleporting())
			return;
        if (player.getGameMode() != GameMode.NORMAL) {
            player.getPacketSender().sendMessage("Your game mode restricts you from doing this action.");
            return;
        }
		if (player.isAccountCompromised() || player.requiresUnlocking()) {
			return;
		}
		if (player.requiresUnlocking()) {
			BankPin.init(player, false);
			return;
		}
		/*
		 * Perform action
		 */
		player.performAnimation(POP_BALLOON);
		/*
		 * Found item
		 */
		boolean test = false;
		/*
		 * Pop and remove
		 */
		TaskManager.submit(new Task(1) {

			@Override
			protected void execute() {
				player.performGraphic(BALLOON_POP);
				CustomObjects.deleteGlobalObject(object);
				stop();
			}

		});
		/*
		 * Empty chest
		 */
		if (chest.isEmpty()) {
			return;
		}
		/*
		 * Found
		 */
		if (balloons.contains(object.getPosition())) {
			if (Misc.random(CHANCE) == 1 || test) {
				/*
				 * The reward
				 */
				Item reward = chest.getRandom();
				/*
				 * Delete from chest
				 */
				chest.delete(reward);
				/*
				 * Update players
				 */
				updateLocalPlayers(player);
				/*
				 * Ground item spawn
				 */
				GroundItemManager.spawnGroundItem(player, new GroundItem(reward, object.getPosition().copy(),
						player.getUsername(), false, 100, true, 100));
				player.getPacketSender()
						.sendMessage("You find: " + reward.getDefinition().getName() + " x" + reward.getAmount());
				if (test) {
					player.getPacketSender()
							.sendMessage("Balloons: " + balloons.size() + ". items: " + chest.getValidItems().size());
				}
			}
		}
		/*
		 * Remove position
		 */
		balloons.remove(object.getPosition());
		/*
		 * Resend balloons
		 */
		if (balloons.size() < 15 && !chest.isEmpty()) {
			sendBalloons();
		}
	}

	/**
	 * Sending balloons
	 */
	private static void sendBalloons() {
		/*
		 * Remove existing
		 */
		for (Position pos : balloons) {
			GameObject object = CustomObjects.getGameObject(pos);
			if (object != null) {
				CustomObjects.deleteGlobalObject(object);
			}
		}
		/*
		 * Clear list
		 */
		balloons.clear();
		/*
		 * Add new
		 */
		for (Boundary boundary : BALLOON_BOUNDARIES) {
			for (Position pos : boundary.getAllPositionsWithinBoundary()) {
				if (Misc.random(10) == 1) {
					continue;
				}
				GameObject balloon = new GameObject(BALLOON_ID + Misc.random(BALLOON_RANGE), pos);
				CustomObjects.spawnGlobalObject(balloon);
				balloons.add(pos);
			}
		}
	}

	/**
	 * Closing the interface
	 * 
	 * @param player the player
	 */
	public static void close(Player player) {
		/*
		 * Add the items
		 */
		if (player.getPartyRoom().getDeposit().getValidItems().size() > 0) {
			player.getInventory().addItemSet(player.getPartyRoom().getDeposit().getValidItems());
		}
		/*
		 * Reset deposit
		 */
		player.getPartyRoom().getDeposit().resetItems();
	}

	/**
	 * Updating local players
	 * 
	 * @param player the player
	 */
	private static void updateLocalPlayers(Player player) {
		/*
		 * Refresh local players
		 */
		for (Player local : player.getLocalPlayers()) {
			/*
			 * Invalid
			 */
			if (local == null) {
				continue;
			}
			/*
			 * Has opened interface
			 */
			if (local.getInterfaceId() == MAIN_INTERFACE) {
				open(local);
			}
		}
		/*
		 * Update player
		 */
		if (player.getInterfaceId() == MAIN_INTERFACE) {
			open(player);
		}
	}

	/**
	 * Gets the deposit
	 *
	 * @return the deposit
	 */
	public PartyRoomDepositItemContainer getDeposit() {
		return deposit;
	}

	/**
	 * Sets the deposit
	 *
	 * @param deposit the deposit
	 */
	public void setDeposit(PartyRoomDepositItemContainer deposit) {
		this.deposit = deposit;
	}
}