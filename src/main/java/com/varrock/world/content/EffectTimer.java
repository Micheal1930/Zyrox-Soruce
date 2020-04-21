package com.varrock.world.content;

public enum EffectTimer {

	EXPERIENCE(1051),
	BARRAGE(1049),
    VENGEANCE(1050),
    
    DOUBLE_DONATION(1057);

    private int clientSprite;

    EffectTimer(int clientSprite) {
        this.clientSprite = clientSprite;
    }

    public int getClientSprite() {
        return clientSprite;
    }

    public void setClientSprite(int sprite) {
        this.clientSprite = sprite;
    }
}
