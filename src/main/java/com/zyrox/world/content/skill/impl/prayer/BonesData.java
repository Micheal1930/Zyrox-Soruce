package com.zyrox.world.content.skill.impl.prayer;

public enum BonesData {

	 BONES(526, 15.75),
	 BAT_BONES(530, 18.55),
	 WOLF_BONES(2859, 15.75),
	 BIG_BONES(532, 52.5),
	 FEMUR_BONES(15182, 52.5),
	 BABYDRAGON_BONES(534, 105),
	 JOGRE_BONE(3125, 52.5),
	 ZOGRE_BONES(4812, 78.75),
	 LONG_BONES(10976, 80),
	 CURVED_BONE(10977, 80),
	 SHAIKAHAN_BONES(3123, 87.5),
	 DRAGON_BONES(536, 252),
	 FAYRG_BONES(4830, 294),
	 RAURG_BONES(4832, 294),
	 DAGANNOTH_BONES(6729, 437.5),
	 OURG_BONES(14793, 490),
	 FROSTDRAGON_BONES(18830, 585.5),
	 WYRM_BONES(52780, 175),
	 HYDRA_BONES(52786, 385),
	/* DRAKE_BONES(52783, 12_500),
	 DRAKE_BONES_1(-12753, 12_500),
	 WYRM_BONES_1(-12756, ),*/
	 SUPERIORDRAGON_BONES(52124, 525);

	
	BonesData(int boneId, double buryXP) {
		this.boneId = boneId;
		this.buryXP = buryXP;
	}

	private int boneId;
	private double buryXP;
	
	public int getBoneID() {
		return this.boneId;
	}
	
	public double getBuryingXP() {
		return this.buryXP;
	}
	
	public static BonesData forId(int bone) {
		for(BonesData prayerData : BonesData.values()) {
			if(prayerData.getBoneID() == bone) {
				return prayerData;
			}
		}
		return null;
	}
	
}
