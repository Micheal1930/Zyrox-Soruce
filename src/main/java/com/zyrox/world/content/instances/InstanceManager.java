package com.zyrox.world.content.instances;

import java.util.AbstractCollection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.zyrox.engine.task.TaskManager;
import com.zyrox.model.Animation;
import com.zyrox.model.GameObject;
import com.zyrox.model.Item;
import com.zyrox.model.Position;
import com.zyrox.world.content.alchemical_hydra.KaruulmSlayerHydra;
import com.zyrox.world.content.inferno.Inferno;
import com.zyrox.world.content.skill.impl.summoning.SummoningData;
import com.zyrox.world.entity.impl.player.Player;

public class InstanceManager<E extends BossInstance> extends AbstractCollection<E> {
	
	private static final InstanceManager<?> INSTANCE = new InstanceManager<>(2048);
	
	private static final int BASE_OFFSET = 1;
	
	private BossInstance[] instances;
	
	private Set<Integer> indices;
	
	private int pointer = 0;
	
	public InstanceManager(int capacity) {
		this.instances = new BossInstance[capacity];
		this.indices = new HashSet<>();
	}

	@Override
	public Iterator<E> iterator() {
		return new InstanceIterator<E>(instances, indices, this);
	}

	@Override
	public int size() {
		int size = 0;
		for (BossInstance instance : instances) {
			if (instance != null) size++;
		}
		return size;
	}
	
	@Override
	public boolean add(BossInstance instance) {
		int slot = getFreeSlot();
		if (slot == -1) {
			return false;
		}
		instances[slot] = instance;
		instance.setPlane(slot * 4);
		TaskManager.submit(instance);
		return true;
	}
	
	private int getFreeSlot() {
		for (int i = BASE_OFFSET; i < instances.length; i++) {
			if (instances[i] == null) {
				return i;
			}
		}
		return -1;
	}

	public Set<Integer> getIndices() {
		return indices;
	}

	public static InstanceManager<?> get() {
		return INSTANCE;
	}
	
	@Override
	public boolean remove(Object o) {
		for (int i = BASE_OFFSET; i < instances.length; i++) {
			if (instances[i] == null) {
				continue;
			}
			if (instances[i] == o) {
				instances[i].stop();
				instances[i] = null;
				return true;
			}
		}
		return false;
	}

	public <T> T getInstance(Player player) {
		for (int i = BASE_OFFSET; i < instances.length; i++) {
			if (instances[i] == null) {
				continue;
			}
			if (instances[i].getPlayer() == player) {
				return (T) instances[i];
			}
		}
		return null;
	}
	
	public void enterHydra(Player player) {
		KaruulmSlayerHydra hydra = new KaruulmSlayerHydra(player);
		add(hydra);
		hydra.start();
	}
	
	public void enterInferno(Player player) {
		if (player.getSummoning().getFamiliar() != null) {
			player.sendMessage("You cannot take a familiar into the Inferno.");
			return;
		}
		for (Item item : player.getInventory().getItems()) {
			if (item == null) continue;
			if (SummoningData.isPouch(player, item.getId(), 2) || SummoningData.isPouch(player, item.getId(), 3)) {
				player.sendMessage("You cannot take a familiar into the Inferno.");
				return;
			}
		}
		Inferno inferno = new Inferno(player);
		add(inferno);
		player.performAnimation(new Animation(4003, true));
		inferno.start();
	}
	
	/**
	 * Inferno spectator mode.
	 * 
	 * @param player the spectating player.
	 */
	public void prepare(Player player) {
		Position BASE = new Position(2240, 5312, 0);
		GameObject[] old = new GameObject[] { new GameObject(30336, new Position(2273, 5364, 0), 10, 0), new GameObject(30337, new Position(2268, 5364, 0), 10, 0), new GameObject(30338, new Position(2270, 5363, 0), 10, 0) };
		for (GameObject obj : old) {
			player.getPacketSender().sendObjectRemoval(obj);
		}
		GameObject[] walls = new GameObject[] {
				new GameObject(30339, BASE.transform(35, 52, 0), 10, 3), new GameObject(30340,BASE.transform(27, 52, 0),  10, 1), new GameObject(30341, BASE.transform(35, 54, 0), 10, 3), new GameObject(30342, BASE.transform(27, 54, 0), 10, 1),
		};
		GameObject[] newWalls = new GameObject[] {
				new GameObject(30343, BASE.transform(33, 52, 0), 10, 3), new GameObject(30344, BASE.transform(28, 52, 0), 10, 3)
		};
		for (GameObject obj : walls) {
			player.getPacketSender().sendObject(obj);
		}
		for (GameObject obj : newWalls) {
			player.getPacketSender().sendObject(obj);
		}
		for (GameObject obj : newWalls) {
			player.getPacketSender().sendObjectAnimation(obj, new Animation(7560, true));
		}
	}
}
