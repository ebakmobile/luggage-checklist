package com.lugcheck.models;

public abstract class LugCheckObject {
	public static enum ModelType {
		ITEM, SUITCASE, TRIP
	};

	protected LugCheckObject(ModelType type) {
		this.type = type;
	}

	protected ModelType type; // Can this possibly be a final?

	public ModelType getType() {
		return type;
	}
}
