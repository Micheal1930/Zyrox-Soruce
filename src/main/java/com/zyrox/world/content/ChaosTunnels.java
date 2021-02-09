package com.zyrox.world.content;

import com.zyrox.model.GameObject;
import com.zyrox.model.Position;
import com.zyrox.world.entity.impl.player.Player;

public class ChaosTunnels {

	public static boolean usePortal(GameObject object, Player player) {
		if (object.getPosition().equals(new Position(2998, 3302, 0))) {
			 player.moveTo(new Position(3183, 5470, 0));
			 return true;
		}
		
		if (object.getPosition().equals(new Position(3183, 5470, 0))) {
			player.moveTo(new Position(2998, 3302, 0));
			 return true;
		}
		
		if (object.getPosition().equals(new Position(3118, 3570, 0))) {
			player.moveTo(new Position(3248, 5490, 0));
			 return true;
		}
		
		if (object.getPosition().equals(new Position(3248, 5490, 0))) {
			player.moveTo(new Position(3118, 3570, 0));
			 return true;
		}
				
		if (object.getPosition().equals(new Position(3164, 3561, 0))) {
			player.moveTo(new Position(3292, 5479, 0));
			 return true;
		}
				
		if (object.getPosition().equals(new Position(3292, 5479, 0))) {
			player.moveTo(new Position(3164, 3561, 0));
			 return true;
		}
				
		if (object.getPosition().equals(new Position(3176, 3585, 0))) {
			player.moveTo(new Position(3291, 5538, 0));
			 return true;
		}
		
		if (object.getPosition().equals(new Position(3291, 5538, 0))) {
			player.moveTo(new Position(3176, 3585, 0));
			 return true;
		}
				
		if (object.getPosition().equals(new Position(3129, 3587, 0))) {
			player.moveTo(new Position(3234, 5559, 0));
			 return true;
		}
				
		if (object.getPosition().equals(new Position(3234, 5559, 0))) {
			player.moveTo(new Position(3129, 3587, 0));
			 return true;
		}
		
		return ChaosTunnel.goThrough(object, player);

	}
	
	public enum ChaosTunnel {
		
		DAGANNOTH_NECHRYAEL(new Position(3158, 5561, 0), new Position(3162, 5557, 0)),
		NECHRYAEL_DRAGONS(new Position(3174, 5558, 0), new Position(3180, 5557, 0)),

		DRAGONS_WATERFIENDS(new Position(3190, 5554, 0), new Position(3190, 5549, 0)),
		WATEFIENDS_TUROTH(new Position(3171, 5542, 0), new Position(3168, 5541, 0)),

		TUROTH_EARTHWARRIORS(new Position(3153, 5537, 0), new Position(3148, 5533, 0)),
		EARTHWARRIORS_BLACKDEMONS(new Position(3152, 5520, 0), new Position(3156, 5523, 0)),

		NECHRYAEL_DAGANNOTH(new Position(3162, 5545, 0), new Position(3166, 5553, 0)),
		DAGANNOTH_EARTHWARRIORS(new Position(3147, 5541, 0), new Position(3143, 5535, 0)),

		BLACKDEMONS_DUSTDEVILS(new Position(3173, 5530, 0), new Position(3165, 5515, 0)),
		DUSTDEVILS_INFERNALMAGE(new Position(3182, 5530, 0), new Position(3187, 5531, 0)),

		DUSTDEVILS_CRYPTRATS(new Position(3181, 5517, 0), new Position(3185, 5518, 0)),
		DUSTDEVILS_MONKSOFZAMORAK(new Position(3169, 5510, 0), new Position(3159, 5501, 0)),

		MONKSOFZAMORAK_MUMMIES(new Position(3142, 5489, 0), new Position(3141, 5480, 0)),
		MUMMIES_ICETROLLS(new Position(3142, 5462, 0), new Position(3154, 5462, 0)),

		ICETROLLS_ICETROLLS(new Position(3143, 5443, 0), new Position(3155, 5449, 0)),
		//MUMMIES_ZOMBIE_SKELE(new Position(3168, 5456, 0), new Position(X, X, 0)),

		ZOMBIE_SKELE_ZOMBIE_SKELE(new Position(3187, 5460, 0), new Position(3189, 5444, 0)),
		ZOMBIE_SKELE_ICEWAR_GIANTS(new Position(3197, 5448, 0), new Position(3204, 5445, 0)),

		GARGOYLES_ZOMBIE_SKELE(new Position(3186, 5472, 0), new Position(3192, 5472, 0)),
		GARGOYLES_ZOMBIE_SKELE_(new Position(3185, 5478, 0), new Position(3191, 5482, 0)),

		GARGOYLES_MUMMIES(new Position(3178, 5460, 0), new Position(3168, 5456, 0)),
		GARGOYLES_ZOMBIES(new Position(3171, 5473, 0), new Position(3167, 5471, 0)),

		SKELES_MOSSGIANTS(new Position(3191, 5495, 0), new Position(3194, 5490, 0)),
		MOSSGIANTS_MONKS(new Position(3210, 5477, 0), new Position(3208, 5471, 0)),

		MONKS_ZAMORAKWARRIORS(new Position(3215, 5475, 0), new Position(3218, 5478, 0)),
		MONKS_ICEWARRIORS(new Position(3214, 5456, 0), new Position(3212, 5452, 0)),

		ICE_WARRIORS_GIANTS_INFERNALMAGES(new Position(3233, 5445, 0), new Position(3241, 5445, 0)),
		INFERNALMAGES_ZAMORAKFIGHTERS(new Position(3235, 5457, 0), new Position(3229, 5454, 0)),

		INFERNAL_MAGES_DHAIMONKS(new Position(3259, 5448, 0), new Position(3254, 5451, 0)),
		INFERNAL_MAGES_ROCKCRABS(new Position(3241, 5469, 0), new Position(3233, 5470, 0)),

		ZAMORAKFIGHTERS_ZAMORAKWARRIORS(new Position(3222, 5474, 0), new Position(3224, 5479, 0)),
		ZAMORAKWARRIORS_FIREGIANTS(new Position(3222, 5488, 0), new Position(3218, 5479, 0)),

		FIREGIANTS_ROCKCRABS(new Position(3239, 5498, 0), new Position(3244, 5495, 0)),
		FIREGIANTS_ZAMORAKFIGHTERS(new Position(3232, 5501, 0), new Position(3238, 5507, 0)),

		ZAMORAKFIGHTERS_FIREGIANTS(new Position(3241, 5529, 0), new Position(3243, 5526, 0)),
		FIREGIANTS_ROCKCRABS_(new Position(3214, 5533, 0), new Position(3211, 5533, 0)),

		ROCKCRABS_FIREGIANTS(new Position(3208, 5527, 0), new Position(3211, 5523, 0)),
		FIREGIANTS_CRYPTRATS(new Position(3202, 5516, 0), new Position(3196, 5512, 0)),

		CRYPTRATS_INFERNALMAGES(new Position(3190, 5515, 0), new Position(3190, 5519, 0)),
		INFERNALMAGES_ROCKCRABS(new Position(3197, 5529, 0), new Position(3201, 5531, 0)),

		ROCKCRABS_ICEGIANTS(new Position(3204, 5546, 0), new Position(3206, 5553, 0)),
		ICEGIANTS_ZAMORAKFIGHTERS(new Position(3226, 5553, 0), new Position(3230, 5547, 0)),

		FIREGIANTS_MOSSGIANTS(new Position(3252, 5543, 0), new Position(3248, 5547, 0)),
		MOSSGIANTS_REDSPIDERS(new Position(3253, 5561, 0), new Position(3256, 5561, 0)),

		REDSPIDERS_CAVEBUGS(new Position(3262, 5552, 0), new Position(3266, 5552, 0)),
		FIREGIANTS_POISONSPIDERS(new Position(3261, 5536, 0), new Position(3268, 5534, 0)),

		CAVEBUGS_DRAGONS(new Position(3285, 5556, 0), new Position(3291, 5555, 0)),
		DRAGONS_SHADOWHOUNDS(new Position(3315, 5552, 0), new Position(3321, 5554, 0)),

		SHADOWHOUNDS_WOLVES(new Position(3323, 5531, 0), new Position(3325, 5518, 0)),
		SHADOWHOUNDS_DRAGONS(new Position(3299, 5533, 0), new Position(3297, 5536, 0)),

		WOLVES_HOUNDS_CAVEBUGS(new Position(3289, 5532, 0), new Position(3288, 5536, 0)),
		WOLVES_HOUNDS_POISONSPIDERS(new Position(3285, 5527, 0), new Position(3282, 5531, 0)),

		WOLVES_HOUNDS_WOLVES_HOUNDS(new Position(3300, 5514, 0), new Position(3297, 5510, 0)),
		WOLVES_HOUNDS_CRYPTRATS(new Position(3285, 5508, 0), new Position(3280, 5501, 0)),

		CRYPTRATS_MONKS(new Position(3265, 5491, 0), new Position(3259, 5446, 0)),
		ROCKCRABS_POISONSPIDERS(new Position(3260, 5491, 0), new Position(3266, 5446, 0)),

		POISONSPIDERS_LEECHES(new Position(3283, 5448, 0), new Position(3287, 5548, 0)),
		POISON_SPIDERS_CRYPT_SHADOWSPIDERS(new Position(3280, 5460, 0), new Position(3273, 5460, 0)),

		CRPYT_SHADOWSPIDERS_POISONSPIDERS(new Position(3285, 5474, 0), new Position(3286, 5470, 0)),
		POISONSPIDERS_DRAGONS(new Position(3290, 5463, 0), new Position(3302, 5469, 0)),

		POISONSPIDERS_GIANTANTS(new Position(3303, 5477, 0), new Position(3299, 5484, 0)),
		CRYPTRATS_GIANTANTS(new Position(3307, 5496, 0), new Position(3317, 5496, 0)),

		GIANTANTS_GIANTANTS(new Position(3318, 5481, 0), new Position(3322, 5480, 0)),
		DRAGONS_LEECHES(new Position(3302, 5469, 0), new Position(3303, 5477, 0)),
		
		GREEN_DRAGON_BLOODWORM(new Position(3299, 5450, 0), new Position(3296, 5455, 0)),
		LEECH_JELLY(new Position(3287, 5448, 0), new Position(3283, 5448, 0)),
		
		ZAM_WARRIOR_INFERNAL_MAGE(new Position(3254, 5451, 0), new Position(3250, 5448, 0));
		
		;
		
		public Position p1;
		public Position p2;
		
		ChaosTunnel(Position p1, Position p2) {
			this.p1 = p1;
			this.p2 = p2;
		}
		
		public static boolean goThrough(GameObject object, Player player) {
			Position to = null;
			for (ChaosTunnel l : ChaosTunnel.values()) {
				if (object.getPosition().equals(l.p1))
					to = l.p2;
				if (object.getPosition().equals(l.p2))
					to = l.p1;
			}
			if (to != null) {
				player.moveTo(to);
				return true;
			}
			return false;
		}
		
	}
}