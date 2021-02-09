package com.zyrox.saving;


public class SavedSingleValue {
	
	private SaveSingleValue sv;
	
	private Object previousValue;
	
	public SavedSingleValue(SaveSingleValue sv, Object value) {
		this.setSavedSingleValue(sv);
		this.setPreviousValue(value);
	}

	public Object getPreviousValue() {
		return previousValue;
	}

	public void setPreviousValue(Object previousValue) {
		this.previousValue = previousValue;
	}

	public SaveSingleValue getSavedSingleValue() {
		return sv;
	}

	public void setSavedSingleValue(SaveSingleValue sv) {
		this.sv = sv;
	}
	
}
