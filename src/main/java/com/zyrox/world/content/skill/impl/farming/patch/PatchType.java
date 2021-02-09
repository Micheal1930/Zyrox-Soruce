package com.zyrox.world.content.skill.impl.farming.patch;

import java.util.ArrayList;
import java.util.List;

import com.zyrox.util.Misc;
import com.zyrox.world.content.skill.impl.farming.seed.*;

/**
 * 
 * @author relex lawl
 */
public enum PatchType {
	
	VARROCK_CASTLE_TREE_PATCH(502, 8390, TreeSeedType.values()),
	
	FALADOR_TREE_PATCH(502, 8389, TreeSeedType.values()),
	
	TAVERLEY_TREE_PATCH(502, 8388, TreeSeedType.values()),
	
	
	FALADOR_NORTH_WEST_ALLOTMENT(504, 8550, AllotmentSeedType.values()),
	
	FALADOR_SOUTH_EAST_ALLOTMENT(504, 8551, AllotmentSeedType.values()),
	
	CATHERBY_NORTH_ALLOTMENT(504, 8552, AllotmentSeedType.values()),
	
	CATHERBY_SOUTH_ALLOTMENT(504, 8553, AllotmentSeedType.values()),
	
	
	ARDOUGNE_NORTH_ALLOTMENT(505, 8554, AllotmentSeedType.values()),
	
	ARDOUGNE_SOUTH_ALLOTMENT(505, 8555, AllotmentSeedType.values()),
	
	CANIFIS_NORTH_WEST_ALLOTMENT(505, 8556, AllotmentSeedType.values()),
	
	CANIFIS_SOUTH_EAST_ALLOTMENT(505, 8557, AllotmentSeedType.values()),
	
	
	FALADOR_FLOWER_PATCH(508, 7847, FlowerSeedType.values()),
	
	CATHERBY_FLOWER_PATCH(508, 7848, FlowerSeedType.values()),
	
	ARDOUGNE_FLOWER_PATCH(508, 7849, FlowerSeedType.values()),
	
	CANIFIS_FLOWER_PATCH(508, 7850, FlowerSeedType.values()),
	
	
	VARROCK_BUSH_PATCH(509, 7577, BushSeedType.values()), //3181, 3357
	
	RIMMINGTON_BUSH_PATCH(509, 7578, BushSeedType.values()), //2940, 3221
	
	ARDOUGNE_BUSH_PATCH(509, 7580, BushSeedType.values()), //2617, 3225
	
	ETCETERIA_BUSH_PATCH(509, 7579, BushSeedType.values()), //2593, 3863
	
	
	CANAFIS_MUSHROOM_PATCH(512, 8337, MushroomSeedType.values()), //3453, 3472
	
	
	FALADOR_HERB_PATCH(515, 8150, HerbSeedType.values()),
	
	CATHERBY_HERB_PATCH(515, 8151, HerbSeedType.values()),
	
	ARDOUGNE_HERB_PATCH(515, 8152, HerbSeedType.values()),
	
	CANIFIS_HERB_PATCH(515, 8153, HerbSeedType.values()),
	;
	
	private PatchType(int configId, int objectId, SeedType[]... validSeedsArray) {
		this.configId = configId;
		this.objectId = objectId;
		
		validSeeds = new ArrayList<>();
		for (SeedType[] seedTypes : validSeedsArray) {
			for (SeedType seedType : seedTypes)
				validSeeds.add(seedType);
		}
	}
	
	private final int configId;
	
	private final int objectId;
	
	private final List<SeedType> validSeeds;
	
	public int getConfigId() {
		return configId;
	}
	
	public int getObjectId() {
		return objectId;
	}
	
	public List<SeedType> getValidSeeds() {
		return validSeeds;
	}

	public String getCityName() {
		return Misc.capitalizeFirstLetter(this.toString().toLowerCase().split("_")[0]);
	}
	public static PatchType forObjectId(int objectId) {
		for (PatchType patchType : PatchType.values()) {
			if (patchType.getObjectId() == objectId)
				return patchType;
		}
		return null;
	}
}
