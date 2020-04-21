package com.varrock.world.content.alchemical_hydra;

import com.varrock.GameSettings;
import com.varrock.model.Animation;

/**
 * 
 * @author Adil
 *
 */
public enum HydraState {
	
	NORMAL(8616, 8237, 3, new Animation(8234 + GameSettings.OSRS_ANIM_OFFSET), new Animation(8235 + GameSettings.OSRS_ANIM_OFFSET), new Animation(8236 + GameSettings.OSRS_ANIM_OFFSET)) {
		@Override
		public void renderDeath(AlchemicalHydra hydra) {
			hydra.performAnimation(new Animation(getAnimation(), 2));
			hydra.setTransformTicks(4);
			hydra.transformNPC(getPnpc());
		}
	},
	
	BLUE(8619, 8244, 3, new Animation(8241 + GameSettings.OSRS_ANIM_OFFSET), new Animation(8242 + GameSettings.OSRS_ANIM_OFFSET), new Animation(8243 + GameSettings.OSRS_ANIM_OFFSET)) {
		@Override
		public void renderDeath(AlchemicalHydra hydra) {
			hydra.performAnimation(new Animation(getAnimation(), 2));
			hydra.setTransformTicks(3);
			hydra.transformNPC(8617 + GameSettings.OSRS_NPC_OFFSET);
		}
	},

	RED(8620, 8251, 3, new Animation(8248 + GameSettings.OSRS_ANIM_OFFSET), new Animation(8249 + GameSettings.OSRS_ANIM_OFFSET), new Animation(8250 + GameSettings.OSRS_ANIM_OFFSET)) {
		@Override
		public void renderDeath(AlchemicalHydra hydra) {
			hydra.transformNPC(8618 + GameSettings.OSRS_NPC_OFFSET);
			hydra.performAnimation(new Animation(getAnimation(), 2));
			hydra.setTransformTicks(4);
		}
	},
	
	JAD(8621, 8262, 1, new Animation(8255 + GameSettings.OSRS_ANIM_OFFSET), new Animation(8256 + GameSettings.OSRS_ANIM_OFFSET)) {
		@Override
		public void renderDeath(AlchemicalHydra hydra) {
			/*hydra.performAnimation(new Animation(8257 + GameSettings.OSRS_ANIM_OFFSET, 2));
			TaskManager.submit(new Task(1) {
				int cycle = 0;
				@Override
				public void execute() {
					cycle++;
					switch(cycle) {
					case 2:
						hydra.transformNPC(8622 + GameSettings.OSRS_NPC_OFFSET);
						hydra.setTransformTicks(0);
						hydra.performAnimation(new Animation(8258 + GameSettings.OSRS_ANIM_OFFSET));
						break;
					case 5:
						stop();
						hydra.setConstitution(0);
						hydra.appendDeath();
						break;
					}
				}
			});*/
		}
	}
	;
	
	private int pnpc, animation, attackCount;
	
	private Animation[] attackAnimations;
	
	private HydraState() {
		// TODO Auto-generated constructor stub
	}
	
	private HydraState(int pnpc, int animation, int attackCount, Animation...attackAnimations) {
		this.pnpc = pnpc + GameSettings.OSRS_NPC_OFFSET;
		this.animation = animation + GameSettings.OSRS_ANIM_OFFSET;
		this.attackAnimations = attackAnimations;
		this.attackCount = attackCount;
	}

	/**
	 * @return the pnpc
	 */
	public int getPnpc() {
		return pnpc;
	}

	/**
	 * @return the animation
	 */
	public int getAnimation() {
		return animation;
	}

	public void renderDeath(AlchemicalHydra hydra) {
		
	}

	/**
	 * @return the attackAnimations
	 */
	public Animation[] getAttackAnimations() {
		return attackAnimations;
	}

	/**
	 * @return the attackCount
	 */
	public int getAttackCount() {
		return attackCount;
	}

}
