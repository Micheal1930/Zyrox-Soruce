package com.varrock.world.content.inferno;

import java.util.ArrayList;
import java.util.List;

import com.varrock.GameSettings;
import com.varrock.engine.task.Task;
import com.varrock.engine.task.TaskManager;
import com.varrock.model.Animation;
import com.varrock.model.GameObject;
import com.varrock.model.Graphic;
import com.varrock.model.Position;
import com.varrock.model.item.Items;
import com.varrock.util.Misc;
import com.varrock.world.World;
import com.varrock.world.content.inferno.node.InfernoNode;
import com.varrock.world.content.inferno.node.impl.JalMejJak;
import com.varrock.world.content.inferno.node.impl.JalXilPlugin;
import com.varrock.world.content.inferno.node.impl.JalZekPlugin;
import com.varrock.world.content.instances.BossInstance;
import com.varrock.world.content.instances.InstanceManager;
import com.varrock.world.entity.impl.player.Player;

public class Inferno extends BossInstance {
	
	private static final Position ENTRANCE = new Position(2273, 5358, 0);
	
	private Position BASE = new Position(2240, 5312, 0);
	
	private static final Position DEATH_LOCATION = new Position(2496, 5119, 0);
	
	private List<InfernoNode> nodes;
	
	private boolean rewarded;
	
	
	/**
	 * Inferno spawns.
	 */
	public static final Position[][] SPAWNS = {
			{
				/*
				 * Normal waves
				 */
				new Position(18, 40), new Position(38, 40), new Position(19, 34), new Position(39, 33), new Position(32, 28), new Position(20, 22), new Position(18, 18), new Position(31, 18), new Position(40, 21),
			},
			{ 
				/*
				 * Boss wave
				 */
				new Position(27, 40), new Position(35, 41), new Position(32, 36)
			}
	};
	
	private GameObject[] walls;
	
	private int wave = 69, bossDelta = 0;
	
	private TzkalZuk boss;
	
	public Inferno(Player player) {
		super(player);
		this.setNodes(new ArrayList<InfernoNode>());
		player.setResetPosition(getDeathLocation());
	}
	
	@Override
	protected void execute() {
		if (player.getRegionID() != 9043) {
			removeAll();
			InstanceManager.get().remove(this);
		}
		switch(wave) {
		case 69:
			switch(++bossDelta) {
			/*
			 * Wave 1 of bosses.
			 */
			case 75:
				Position first = BASE.transform(SPAWNS[1][0].getX(), SPAWNS[1][0].getY(), 0), second = BASE.transform(SPAWNS[1][1].getX(), SPAWNS[1][1].getY(), 0);
				spawn(new JalZekPlugin(22703, first));
				spawn(new JalXilPlugin(22702, second));
				break;
			}
			break;
		}
	}
	
	public void spawn(InfernoNode node) {
		getNodes().add(node);
        World.register(node);
        node.getCombatBuilder().attack(node instanceof JalMejJak ? boss : getPlayer());
	}

	public void start() {
		BASE.setZ(plane);
		getPlayer().moveTo(ENTRANCE.transform(0, 0, plane), true).setPosition(ENTRANCE.transform(0, 0, plane));
		getPlayer().getPacketSender().sendMessage("@red@Wave: " + wave);
		getPlayer().getPacketSender().sendCameraShake(3, 2, 3, 2);
		if (wave == 69) {
			TaskManager.submit(new Task(1) {
				@Override
				public void execute() {
					breakWall();
					boss.performAnimation(new Animation(7563, true));
					TaskManager.submit(new Task(1) {
						
						int cycle = 0;
						
						@Override
						public void execute() {
							if (cycle == 0) {
								for (GameObject obj : Inferno.this.walls) {
									getPlayer().getPacketSender().sendObjectAnimation(obj, new Animation(7560, true));
								}
							}
							cycle++;
							if (cycle == 10) {
								getPlayer().getPacketSender().sendCameraNeutrality();
								stop();
							}
						}
					});
					stop();
				}
			});
		}
	}
	
	public Position getBASE() {
		return BASE;
	}

	private void breakWall() {
		boss = new TzkalZuk(getPlayer(), BASE.transform(28, 51, 0), this);
		boss.setPositionToFace(new Position(2272, 5360, plane));
        World.register(boss);
		GameObject[] old = new GameObject[] { new GameObject(130336, new Position(2273, 5364, 0), 10, 0), new GameObject(130337, new Position(2268, 5364, 0), 10, 0), new GameObject(130338, new Position(2270, 5363, 0), 10, 0) };
		for (GameObject obj : old) {
			getPlayer().getPacketSender().sendObjectRemoval(obj);
		}
		GameObject[] walls = new GameObject[] {
				new GameObject(130339, BASE.transform(35, 52, 0), 10, 3), new GameObject(130340,BASE.transform(27, 52, 0),  10, 1), new GameObject(30341, BASE.transform(35, 54, 0), 10, 3), new GameObject(130342, BASE.transform(27, 54, 0), 10, 1),
		};
		this.walls = new GameObject[] {
				new GameObject(130343, BASE.transform(33, 52, 0), 10, 3), new GameObject(130344, BASE.transform(28, 52, 0), 10, 3)
		};
		for (GameObject obj : walls) {
			getPlayer().getPacketSender().sendObject(obj);
		}
		for (GameObject obj : this.walls) {
			getPlayer().getPacketSender().sendObject(obj);
		}
		
	}

	/**
	 * Finished inferno
	 * 
	 * TODO rewards
	 */
	public void removeAll() {
		for (InfernoNode node : nodes) {
			World.deregister(node);
		}
		World.deregister(boss.getGlyph());
		World.deregister(boss);
	}

	public List<InfernoNode> getNodes() {
		return nodes;
	}

	public void setNodes(List<InfernoNode> nodes) {
		this.nodes = nodes;
	}

	public static Position getDeathLocation() {
		return true ? GameSettings.DEFAULT_POSITION.copy() : DEATH_LOCATION.copy();
	}

	public void reward(Player target) {
		if (rewarded) {
			return;
		}
		rewarded = true;
		target.addBossPoints(50);
		target.performAnimation(new Animation(862));
		target.performGraphic(new Graphic(312));
		target.getInventory().add(51295, 1);
		World.sendMessage("<img=483> @blu@[Amazing News]</col>@red@" + target.getUsername() + " has just defeated the Inferno! Congratulations!!");
		if(Misc.inclusiveRandom(1, 100) == 1){
			World.sendMessage("<img=483> @blu@[Amazing News]</col>@red@" + target.getUsername() + " just received Tzrek Zuk Pet from Inferno!");
			player.getBank(player.getCurrentBankTab()).add(Items.TZREK_ZUK, 1);
		}
		target.delayedMoveTo(getDeathLocation(), 4, true);
	}

}