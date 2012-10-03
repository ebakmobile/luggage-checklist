package com.lugcheck.models;

public abstract class LugCheckObject {
	public static enum ModelType {
		ITEM,
		SUITCASE,
		TRIP
	};
	
	protected ModelType type;	// Can this possibly be a final?
	
	public ModelType getType() {
		return type;
	}
}
