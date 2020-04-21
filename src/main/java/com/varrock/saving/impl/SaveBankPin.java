package com.varrock.saving.impl;

import com.varrock.saving.SaveInteger;
import com.varrock.world.entity.impl.player.Player;

public class SaveBankPin extends SaveInteger {

	public SaveBankPin(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void setValue(Player player, int value) {
		//assume 9876
		int a = value/1000;  //9
		int b = (value % 1000)/100; //8
		int c = (value % 100)/10; //7
		int d = value % 10;
		int[] pin = {a,b,c,d};
		player.getBankPinAttributes().setBankPin(pin);
	}

	@Override
	public Integer getValue(Player player) {
		// TODO Auto-generated method stub
		int[] digits = player.getBankPinAttributes().getBankPin();
		int pin = digits[0] * 1000 + digits[1] * 100 + digits[2] * 10 + digits[3];
		return pin;
	}

	@Override
	public int getDefaultValue() {
		// TODO Auto-generated method stub
		return 0;
	}

}
