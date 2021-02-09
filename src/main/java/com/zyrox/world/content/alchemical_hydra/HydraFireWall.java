package com.zyrox.world.content.alchemical_hydra;

import com.zyrox.GameSettings;
import com.zyrox.engine.task.Task;
import com.zyrox.engine.task.TaskManager;
import com.zyrox.model.Animation;
import com.zyrox.model.Position;
import com.zyrox.model.projectile.Projectile;
import com.zyrox.world.entity.Entity;
import com.zyrox.world.entity.impl.GameCharacter;

public enum HydraFireWall {
	
	SOUTH_WEST(8) {
		
		@Override
		public Position[] getFireShift(Position to, boolean first) {
			return new Position[] { to, to.transform(first ? 0 : 1, first ? 1 : 0,0), to.transform(first ? 0 : 2, first ? 2 : 0,0), to.transform(first ? 0 : 3, first ? 3 : 0,0) };
		}
		
		@Override
		public Position getFirstDirection(AlchemicalHydra hydra) {
			return hydra.getBase().transform(28, 18, 0);
		}

		@Override
		public Position getFirstBase(AlchemicalHydra hydra) {
			return hydra.getBase().transform(35, 18, 0);
		}
		
		@Override
		public int[] getFirstShift() {
			return new int[] { -1, 0 };
		}
		
		@Override
		public Position getSecondDirection(AlchemicalHydra hydra) {
			return hydra.getBase().transform(37, 9, 0);
		}

		@Override
		public Position getSecondBase(AlchemicalHydra hydra) {
			return hydra.getBase().transform(37, 16, 0);
		}

		@Override
		public int[] getSecondShift() {
			return new int[] { 0, -1 };
		}
	},
	

	NORTH_WEST(8) {
		
		@Override
		public Position[] getFireShift(Position to, boolean first) {
			return new Position[] { to, to.transform(first ? 1 : 0, first ? 0 : 1, 0), to.transform(first ? 2 : 0, first ? 0 : 2, 0), to.transform(first ? 3 : 0, first ? 0 : 3, 0) };
		}
		
		@Override
		public Position getFirstDirection(AlchemicalHydra hydra) {
			return hydra.getBase().transform(38, 29, 0);
		}

		@Override
		public Position getFirstBase(AlchemicalHydra hydra) {
			return hydra.getBase().transform(37, 23, 0);
		}
		
		@Override
		public int[] getFirstShift() {
			return new int[] { 0, 1 };
		}
		
		@Override
		public Position getSecondDirection(AlchemicalHydra hydra) {
			return hydra.getBase().transform(29, 18, 0);
		}

		@Override
		public Position getSecondBase(AlchemicalHydra hydra) {
			return hydra.getBase().transform(35, 18, 0);
		}

		@Override
		public int[] getSecondShift() {
			return new int[] { -1, 0 };
		}
	},
	

	NORTH_EAST(8) {
		
		@Override
		public Position[] getFireShift(Position to, boolean first) {
			return new Position[] { to, to.transform(first ? 1 : 0, first ? 0 : 1, 0), to.transform(first ? 2 : 0, first ? 0 : 2, 0), to.transform(first ? 3 : 0, first ? 0 : 3, 0) };
		}
		
		@Override
		public Position getFirstDirection(AlchemicalHydra hydra) {
			return hydra.getBase().transform(38, 29, 0);
		}

		@Override
		public Position getFirstBase(AlchemicalHydra hydra) {
			return hydra.getBase().transform(37, 23, 0);
		}
		
		@Override
		public int[] getFirstShift() {
			return new int[] { 0, 1 };
		}
		
		@Override
		public Position getSecondDirection(AlchemicalHydra hydra) {
			return hydra.getBase().transform(49, 18, 0);
		}

		@Override
		public Position getSecondBase(AlchemicalHydra hydra) {
			return hydra.getBase().transform(42, 18, 0);
		}

		@Override
		public int[] getSecondShift() {
			return new int[] { 1, 0 };
		}
	},
	

	SOUTH_EAST(8) {
		
		@Override
		public Position[] getFireShift(Position to, boolean first) {
			return new Position[] { to, to.transform(first ? -1 : 0, first ? 0 : 1, 0), to.transform(first ? -2 : 0, first ? 0 : 2, 0), to.transform(first ? -3 : 0, first ? 0 : 3, 0) };
		}
		
		@Override
		public Position getFirstDirection(AlchemicalHydra hydra) {
			return hydra.getBase().transform(38, 8, 0);
		}

		@Override
		public Position getFirstBase(AlchemicalHydra hydra) {
			return hydra.getBase().transform(40, 16, 0);
		}
		
		@Override
		public int[] getFirstShift() {
			return new int[] { 0, -1 };
		}
		
		@Override
		public Position getSecondDirection(AlchemicalHydra hydra) {
			return hydra.getBase().transform(49, 18, 0);
		}

		@Override
		public Position getSecondBase(AlchemicalHydra hydra) {
			return hydra.getBase().transform(42, 18, 0);
		}

		@Override
		public int[] getSecondShift() {
			return new int[] { 1, 0 };
		}
	},
	
	WEST(8) {
		
		@Override
		public Position[] getFireShift(Position to, boolean first) {
			return new Position[] { to, to.transform(0, first ? -1 : 1, 0), to.transform(0, first ? -2 : 2, 0), to.transform(1,0,0), to.transform(2, 0, 0) };
		}
		
		@Override
		public Position getFirstDirection(AlchemicalHydra hydra) {
			return hydra.getBase().transform(35, 23, 0);
		}

		@Override
		public Position getFirstBase(AlchemicalHydra hydra) {
			return hydra.getBase().transform(35, 23, 0);
		}
		
		@Override
		public int[] getFirstShift() {
			return new int[] { -1, 1 };
		}
		
		@Override
		public Position getSecondDirection(AlchemicalHydra hydra) {
			return hydra.getBase().transform(35, 16, 0);
		}

		@Override
		public Position getSecondBase(AlchemicalHydra hydra) {
			return hydra.getBase().transform(35, 16, 0);
		}

		@Override
		public int[] getSecondShift() {
			return new int[] { -1, -1 };
		}
	},

	
	NORTH(8) {
		
		@Override
		public Position[] getFireShift(Position to, boolean first) {
			return new Position[] { to, to.transform(first ? 0 : -1, first ? -1 : 0, 0), to.transform(first ? 0 : -2, first ? -2 : 0, 0), to.transform(first ? 1 : 0, first ? 0 : -1,0), to.transform(first ? 2 : 0, first ?0 : -2, 0) };
		}
		
		@Override
		public Position getFirstDirection(AlchemicalHydra hydra) {
			return hydra.getBase().transform(35, 23, 0);
		}

		@Override
		public Position getFirstBase(AlchemicalHydra hydra) {
			return hydra.getBase().transform(35, 23, 0);
		}
		
		@Override
		public int[] getFirstShift() {
			return new int[] { -1, 1 };
		}
		
		@Override
		public Position getSecondDirection(AlchemicalHydra hydra) {
			return hydra.getBase().transform(42, 23, 0);
		}

		@Override
		public Position getSecondBase(AlchemicalHydra hydra) {
			return hydra.getBase().transform(42, 23, 0);
		}

		@Override
		public int[] getSecondShift() {
			return new int[] { 1, 1 };
		}
	},

	
	EAST(8) {
		
		@Override
		public Position[] getFireShift(Position to, boolean first) {
			return new Position[] { to, to.transform(first ? -1 : -1, first ? 0 : 0, 0), to.transform(first ? -2 : -2, first ? 0 : 0, 0), to.transform(0, first ? 1 : -1,0), to.transform(0, first ?2 : -2, 0) };
		}
		
		@Override
		public Position getFirstDirection(AlchemicalHydra hydra) {
			return hydra.getBase().transform(42, 16, 0);
		}

		@Override
		public Position getFirstBase(AlchemicalHydra hydra) {
			return hydra.getBase().transform(42, 16, 0);
		}
		
		@Override
		public int[] getFirstShift() {
			return new int[] { 1, -1 };
		}
		
		@Override
		public Position getSecondDirection(AlchemicalHydra hydra) {
			return hydra.getBase().transform(42, 23, 0);
		}

		@Override
		public Position getSecondBase(AlchemicalHydra hydra) {
			return hydra.getBase().transform(42, 23, 0);
		}

		@Override
		public int[] getSecondShift() {
			return new int[] { 1, 1 };
		}
	},

	
	SOUTH(8) {
		
		@Override
		public Position[] getFireShift(Position to, boolean first) {
			return new Position[] { to, to.transform(first ? -1 : 0, first ? 0 : 1, 0), to.transform(first ? -2 : 0, first ? 0 : 2, 0), to.transform(first ? 0 : 1, first ? 1 : 0,0), to.transform(first ? 0 : 2, first ?2 : 0, 0) };
		}
		
		@Override
		public Position getFirstDirection(AlchemicalHydra hydra) {
			return hydra.getBase().transform(42, 16, 0);
		}

		@Override
		public Position getFirstBase(AlchemicalHydra hydra) {
			return hydra.getBase().transform(42, 16, 0);
		}
		
		@Override
		public int[] getFirstShift() {
			return new int[] { 1, -1 };
		}
		
		@Override
		public Position getSecondDirection(AlchemicalHydra hydra) {
			return hydra.getBase().transform(35, 16, 0);
		}

		@Override
		public Position getSecondBase(AlchemicalHydra hydra) {
			return hydra.getBase().transform(35, 16, 0);
		}

		@Override
		public int[] getSecondShift() {
			return new int[] { -1, -1 };
		}
	}
	
	
	;
	private int length;
	
	private HydraFireWall(int length) {
		this.length = length;
	}

	public Position[] getFireShift(Position to, boolean first) {
		return null;
	}
	
	public Position getFirstDirection(AlchemicalHydra hydra) {
		return null;
	}
	
	public int[] getFirstShift() {
		return null;
	}

	public Position getSecondDirection(AlchemicalHydra hydra) {
		return null;
	}
	
	public int[] getSecondShift() {
		return null;
	}

	public void renderWall(AlchemicalHydra hydra, GameCharacter victim) {
		hydra.getCombatBuilder().reset(true);
		hydra.setPositionToFace(hydra.getPosition().transform(-2, -1, 0));
		hydra.performAnimation(new Animation(8248 + GameSettings.OSRS_ANIM_OFFSET));
		for (Position next : getFireShift(getFirstBase(hydra), true)) {
			Projectile projectile = new Projectile(hydra.getCentrePosition(), next, -1, 1667 + GameSettings.OSRS_GFX_OFFSET, 44, 3, 70, 20, 0);
			projectile.sendProjectile();
			fireWall(victim, next, true);
		}
		TaskManager.submit(new Task(3) {
			@Override
			public void execute() {
				hydra.performAnimation(new Animation(8248 + GameSettings.OSRS_ANIM_OFFSET));
				hydra.setPositionToFace(getSecondDirection(hydra));
				for (Position next : getFireShift(getSecondBase(hydra), false)) {
					Projectile projectile = new Projectile(hydra.getCentrePosition(), next,-1, 1667 + GameSettings.OSRS_GFX_OFFSET, 44, 3, 70, 20, 0);
					projectile.sendProjectile();
					fireWall(victim, next, false);
				}
				TaskManager.submit(new Task(3) {
					@Override
					public void execute() {
						hydra.performAnimation(new Animation(8248 + GameSettings.OSRS_ANIM_OFFSET));
						int gfxDelay;
						if (Position.isWithinDistance(hydra, victim, 1)) {
							gfxDelay = 80;
						} else if (Position.isWithinDistance(hydra, victim, 5)) {
							gfxDelay = 100;
						} else if (Position.isWithinDistance(hydra, victim, 8)) {
							gfxDelay = 120;
						} else {
							gfxDelay = 140;
						}
						hydra.setPositionToFace(victim.getPosition());
						hydra.getCombatBuilder().attack(victim);
						Projectile proj1 = new Projectile(hydra.getPosition(), victim.getPosition(), victim.getProjectileIndex(), 1667 + GameSettings.OSRS_GFX_OFFSET, 44, 3, 70, 20, 0);
						proj1.sendProjectile();
						int hitDelay = (gfxDelay / 20) - 1;

						TaskManager.submit(new Task(hitDelay) {
							@Override
							public void execute() {
								victim.asPlayer().setAttribute("hydra_fire", System.currentTimeMillis());
								stop();
							}
						});
						stop();
					}
				});
			
				stop();
			}
		});
	
	}

	public Position getFirstBase(AlchemicalHydra hydra) {
		return null;
	}

	public Position getSecondBase(AlchemicalHydra hydra) {
		return null;
	}
	
	public void fireWall(Entity target, Position to, boolean first) {
		TaskManager.submit(new Task(1) {
			int count = 0;
			@Override
			public void execute() {
				Position position = to.transform(first ? count * getFirstShift()[0] : count * getSecondShift()[0], first ? count * getFirstShift()[1] : count * getSecondShift()[1], 0);
				new HydraFire(target, position);
				count++;
				if (count == length) {
					stop();
				}
			}
		});
	}

}
