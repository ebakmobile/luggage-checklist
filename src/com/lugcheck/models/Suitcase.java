package com.lugcheck.models;

import java.util.ArrayList;
import java.util.List;

public class Suitcase extends LugCheckObject {

	private int suitcaseId;
	private int tripId;
	private String suitcaseName;
	private List<Item> itemArray;

	public Suitcase() {
		super(ModelType.SUITCASE);
		itemArray = new ArrayList<Item>();
	}

	public Suitcase(int suitcaseId, int tripId, String suitcaseName) {
		super(ModelType.SUITCASE);
		this.suitcaseId = suitcaseId;
		this.tripId = tripId;
		this.suitcaseName = suitcaseName;
		itemArray = new ArrayList<Item>();
	}

	public int getSuitcaseId() {
		return suitcaseId;
	}

	public void setSuitcaseId(int suitcaseId) {
		this.suitcaseId = suitcaseId;
	}

	public int getTripId() {
		return tripId;
	}

	public void setTripId(int tripId) {
		this.tripId = tripId;
	}

	public String getSuitcaseName() {
		return suitcaseName;
	}

	public void setSuitcaseName(String suitcaseName) {
		this.suitcaseName = suitcaseName;
	}

	/*
	 * IMPORTANT NOTE!!! The getter/setter for the itemArray will return a reference to the object. If we need to mutate the object (change the list's contents), then we need to do object copying
	 * instead of just using the reference.
	 */
	public List<Item> getItemArray() {
		return itemArray;
	}

	public void setItemArray(List<Item> itemArray) {
		this.itemArray = itemArray;
	}

}
