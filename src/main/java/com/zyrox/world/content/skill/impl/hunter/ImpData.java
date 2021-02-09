package com.zyrox.world.content.skill.impl.hunter;

public enum ImpData {
	/**
	 * Baby Impling.
	 */
	BABY( "Baby Impling", 11238, 20, 1, 6055 ),
	/**
	 * Young Impling.
	 */
	YOUNG( "Young Impling", 11240, 22, 17, 6056 ),
	/**
	 * Gourmet Impling.
	 */
	GOURMET( "Gourmet Impling", 11244, 24, 34, 6057),
	/**
	 * Earth Impling.
	 */
	EARTH( "Earth Impling", 11244, 27, 34, 6058 ),
	/**
	 * Essence Impling.
	 */
	ESSENCE( "Essence Impling", 11246, 29, 40, 6059 ),
	/**
	 * Electic Impling.
	 */
	ELECTIC( "Electic Impling", 11248, 32, 50, 6060),
	/**
	 * Nature Impling.
	 */
	NATURE( "Nature Impling", 11250, 36, 58,6061 ),
	/**
	 * Magpie Impling.
	 */
	MAGPIE( "Magpie Impling", 11252, 216, 65, 6062 ),
	/**
	 * Ninja Impling.
	 */
	NINJA( "Ninja Impling", 11254, 240, 74, 6063 ),
	/**
	 * Dragon Impling.
	 */
	DRAGON( "Dragon Impling", 11256, 300, 83, 6064 ),
	/**
	 * Dragon Impling.
	 */
	KINGLY( "Kingly Impling", 15517, 380, 91, 7903 );
	
	/**
	 * Variables.
	 */
	public String name;
	public int impJar, XPReward, levelReq, npcId;

	/**
	 * Creating the Impling.
	 * @param name
	 * @param JarAdded
	 * @param XPAdded
	 * @param LevelNeed
	 * @param Npc
	 */
	ImpData(String name, int JarAdded, int XPAdded, int LevelNeed, int Npc) {
		this.name = name;
		this.impJar = JarAdded;
		this.XPReward = XPAdded;
		this.levelReq = LevelNeed;
		this.npcId = Npc;
	}
	
	public static ImpData forId(int npcId) {
		for(ImpData imps : ImpData.values()) {
			if(imps.npcId == npcId) {
				return imps;
			}
		}
		return null;
	}
}
