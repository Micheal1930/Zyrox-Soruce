package com.zyrox.world.content.skill.impl.farming.attributes;

/**
 * 
 * @author relex lawl
 */
public enum PatchAttribute {

	COMPOSTED(0x1),
	
	SUPER_COMPOSTED(0x2),
	
	PROTECTED(0x4),
	
	MAGIC_SECATEURS(0x8),
	
	CHECKED_HEALTH(0x10),
	;
	
	private PatchAttribute(int shift) {
		this.shift = shift;
	}
	
	private final int shift;
	
	public int getShift() {
		return shift;
	}
}
