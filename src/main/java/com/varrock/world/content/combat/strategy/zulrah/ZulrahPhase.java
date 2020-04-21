package com.varrock.world.content.combat.strategy.zulrah;

public class ZulrahPhase {

	private int nextPhase;
	
	private int transformId;
	
	private String phaseName;
	
	private int zulrahX;
	
	private int zulrahY;
	
	public ZulrahPhase(int nextPhase, int transformId, String phaseName, int zulrahX, int zulrahY) {
		this.nextPhase = nextPhase;
		this.transformId = transformId;
		this.phaseName = phaseName;
		this.zulrahX = zulrahX;
		this.zulrahY = zulrahY;
	}
	
	public int getNextPhase() {
		return nextPhase;
	}
	
	public int getTransformId() {
		return transformId;
	}
	
	public String getPhaseName() {
		return phaseName;
	}
	
	public int getZulrahX() {
		return zulrahX;
	}
	
	public int getZulrahY() {
		return zulrahY;
	}
	
}
