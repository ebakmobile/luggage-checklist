package com.lugcheck.models;

import java.util.ArrayList;
import java.util.List;

public class Suitcase extends LugCheckObject {
	
	private String suitcaseName = null;
	private List<Item> itemArray;
	
	public Suitcase() {
		itemArray = new ArrayList<Item>();
		type = ModelType.SUITCASE;
	}

	public String getSuitcaseName() {
		return suitcaseName;
	}

	public void setSuitcaseName(String suitcaseName) {
		this.suitcaseName = suitcaseName;
	}
	
	/*
	 * IMPORTANT NOTE!!!
	 * The getter/setter for the itemArray will return a reference to the object.
	 * If we need to mutate the object (change the list's contents), then we need to
	 * do object copying instead of just using the reference.
	 */
	public List<Item> getItemArray() {
		return itemArray;
	}

	public void setItemArray(List<Item> itemArray) {
		this.itemArray = itemArray;
	}

}
