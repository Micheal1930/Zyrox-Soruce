package com.zyrox.world.content.inferno;

import com.zyrox.GameSettings;
import com.zyrox.engine.task.Task;
import com.zyrox.engine.task.TaskManager;
import com.zyrox.model.*;
import com.zyrox.model.projectile.Projectile;
import com.zyrox.util.Misc;
import com.zyrox.world.World;
import com.zyrox.world.content.combat.prayer.PrayerHandler;
import com.zyrox.world.content.inferno.node.InfernoNode;
import com.zyrox.world.content.inferno.node.impl.JalMejJak;
import com.zyrox.world.content.inferno.node.impl.JalTokJad;
import com.zyrox.world.content.inferno.node.impl.JalXilPlugin;
import com.zyrox.world.content.inferno.node.impl.JalZekPlugin;
import com.zyrox.world.entity.impl.GameCharacter;
import com.zyrox.world.entity.impl.npc.NPC;
import com.zyrox.world.entity.impl.player.Player;

public class TzkalZuk extends NPC {
	
	public static final Hitmask HEAL_MASK = Hitmask.LIGHT_YELLOW;
	
	private AncestralGlyph glyph;
	
	private int glyphAttack = 10, combatCycle;
	
	private boolean paused, healersSpawned;
	
	private Player target;
	
	private Inferno inferno;

	protected TzkalZuk(Player target, Position position, Inferno inferno) {
		super(22706, position);
		this.target = target;
		this.setGlyph(new AncestralGlyph(position.transform(2, 0, 0), false, false, Direction.SOUTH.toInteger()));
        World.register(getGlyph());
        this.inferno = inferno;
        combatCycle = 75;
	}
	
	@Override
    public void sequence() {
        super.sequence();
        tick();
        setPositionToFace(target.getPosition());
    }

	@Override
	public GameCharacter moveTo(Position teleportTarget) {
		return this;
	}
	
	@Override
	public GameCharacter moveTo(Position teleportTarget, boolean force) {
		return this;
	}
	
	@Override
	public void dealDamage(Hit hit) {
		boolean above600 = getConstitution() > 6000, above480 = getConstitution() > 4800, above240 = getConstitution() > 2400;
		super.dealDamage(hit);
		if (above600 && getConstitution() <= 6000) {
			paused = true;
			combatCycle += 175;
		}
		if (above480 && getConstitution() <= 4800) {
			paused = false;
			inferno.spawn(new JalTokJad(22704, new Position(2266, 5345, inferno.getPlane())));
			inferno.spawn(new JalTokJad(22704, new Position(2276, 5345, inferno.getPlane())));
		}
		if (above240 && getConstitution() <= 2400) {//healers
			spawnHealers();
		}
		if (getConstitution() == 0) {
			inferno.removeAll();
			inferno.reward(target);
		}
	}
	
	@Override
	public Hit decrementHealth(Hit hit) {
		if (getConstitution() <= 0)
			return hit;
		if(hit.getDamage() > getConstitution())
			hit.setDamage(getConstitution());
		if(hit.getDamage() < 0)
			hit.setDamage(0);
		if (hit.getHitmask() == HEAL_MASK && healersSpawned) {
			hit.setDamage(hit.getDamage() * -1);
		}
		int outcome = getConstitution() - hit.getDamage();
		if (outcome < 0)
			outcome = 0;
		setConstitution(outcome);
		return hit;
	}
	
	@Override
	public NPC setConstitution(int constitution) {
		this.constitution = constitution;
		if(this.getConstitution() <= 0)
			appendDeath();
		
		return this;
	}
	
	private void spawnHealers() {
		if (healersSpawned) return;
		healersSpawned = true;
		inferno.spawn(new JalMejJak(new Position(2276, 5363, inferno.getPlane()), this));
		inferno.spawn(new JalMejJak(new Position(2280, 5363, inferno.getPlane()), this));
		inferno.spawn(new JalMejJak(new Position(2266, 5363, inferno.getPlane()), this));
		inferno.spawn(new JalMejJak(new Position(2262, 5363, inferno.getPlane()), this));
	}

	private void tick() {
		if (!paused && --combatCycle == 0) {
			boolean xil = true, zek = true;
			for (InfernoNode node : inferno.getNodes()) {
				if (node.getId() == 22702 || node.getId() == 22703) {
					if (node.getConstitution() > 0) {
						if (node.getId() == 22702) {
							xil = false;
						} else if (node.getId() == 22703) {
							zek = false;
						}
					}
				}
			}
			Position first = inferno.getBASE().transform(Inferno.SPAWNS[1][0].getX(), Inferno.SPAWNS[1][0].getY(), 0), second = inferno.getBASE().transform(Inferno.SPAWNS[1][1].getX(), Inferno.SPAWNS[1][1].getY(), 0);
			first = first.transform(2, -2, 0);
			second = second.transform(-2, -2, 0);
			if (xil) inferno.spawn(new JalXilPlugin(22702, second));
			if (zek) inferno.spawn(new JalZekPlugin(22703, first));
			combatCycle = 350;
		}
		if (--glyphAttack == 2) {
			performAnimation(new Animation(7566, true));
		} else if (glyphAttack == 0) {
			glyphAttack = 10;
			boolean hit = !getGlyph().isProtected(target);
			double distance = this.getPosition().getDistance(target.getPosition());
			Projectile proj =  new Projectile(this, hit ? target : getGlyph(), 1375 + GameSettings.OSRS_GFX_OFFSET, 44, 3, 43, 43, 0);
            proj.sendProjectile();
            int hitDelay = 0;
    		if(distance <= 1) {
                hitDelay = 80;
            } else if(distance <= 5) {
                hitDelay = 100;
            } else if(distance <= 8) {
                hitDelay = 120;
            } else {
                hitDelay = 140;
            }
            int delay = (hitDelay / 20) - 4;
            
			if (hit) {
				TaskManager.submit(new Task(delay) {
					
					@Override
					protected void execute() {
					    //If player is dying or already dead in edge don't hit em.
					    if (target.isDying() || target.getLocation().equals(Locations.Location.EDGE)) {
					        stop();
					        return;
                        }
						int damage = PrayerHandler.isActivated(target, PrayerHandler.PROTECT_FROM_MAGIC) ? Misc.random((int) (target.getConstitution() * 0.8)) : target.getConstitution();
						if(target.getLocation() == Locations.Location.INFERNO)
							target.dealDamage(new Hit(damage, Hitmask.RED, CombatIcon.MAGIC));
						stop();
					}
				});
			}
		}
		if (!paused && --combatCycle == 0) {
			combatCycle = 350;
		}
		
	}
    
	@Override
	public Position getCentrePosition() {
		return getPosition().transform(3, 1, 0);
	}
	
    
	@Override
    public boolean clearDamageMapOnDeath() {
        return false;
    }

	public Player getTarget() {
		return target;
	}

	public void setTarget(Player target) {
		this.target = target;
	}

	public AncestralGlyph getGlyph() {
		return glyph;
	}

	public void setGlyph(AncestralGlyph glyph) {
		this.glyph = glyph;
	}

}
