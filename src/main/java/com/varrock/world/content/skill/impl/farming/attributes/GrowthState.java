package com.varrock.world.content.skill.impl.farming.attributes;

/**
 * 
 * @author relex lawl
 */
public enum GrowthState {

	GROWING(0x00),
	
	WATERED(0x01),
	
	DISEASED(0x02),
	
	DEAD(0x03),
	
	;
	
	private GrowthState(int shift) {
		this.shift = shift;
	}
	
	private final int shift;
	
	public int getShift() {
		return shift;
	}
}
