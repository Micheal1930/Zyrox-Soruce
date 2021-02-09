package com.zyrox.world.content.skill.impl.farming.seed;

/**
 *
 * @author relex lawl
 */
public enum SeedClass {

	ALLOTMENT {
		@Override
		public SeedType getInstance(String name) {
			return AllotmentSeedType.valueOf(name);
		}
	},
	
	FLOWERS {
		@Override
		public SeedType getInstance(String name) {
			return FlowerSeedType.valueOf(name);
		}
	},
	
	BUSHES {
		@Override
		public SeedType getInstance(String name) {
			return BushSeedType.valueOf(name);
		}
	},
	
	TREES {
		@Override
		public SeedType getInstance(String name) {
			return TreeSeedType.valueOf(name);
		}
	},
	
	FRUIT_TREES,
	
	HOPS,
	
	HERBS {
		@Override
		public SeedType getInstance(String name) {
			return HerbSeedType.valueOf(name);
		}
	},
	
	MUSHROOMS {
		@Override
		public SeedType getInstance(String name) {
			return MushroomSeedType.valueOf(name);
		}
	},
	
	SPECIAL_PLANTS,
	
	
	;
	
	public SeedType getInstance(String name) {
		return null;
	}
}
