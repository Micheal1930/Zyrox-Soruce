package com.zyrox.world.content.skill.impl.farming.seed;

import com.zyrox.model.Animation;
import com.zyrox.model.Item;
import com.zyrox.world.content.skill.impl.farming.patch.Patch;

/**
 * 
 * @author relex lawl
 */
public interface SeedType {
	
	public SeedClass getSeedClass();
		
	public int getLevelRequirement();
	
	public int getToolId();
	
	public Animation getAnimation();
	
	public Item getSeed();
	
	public int[] getGrowthTime();
		
	public Item[] getRewards();
	
	public Item[] getProtectionFee();
	
	public int[] getExperience();

	public int[] getValues();
	
	public int getHarvestAmount(Patch patch);
}
