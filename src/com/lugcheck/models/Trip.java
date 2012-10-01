package com.lugcheck.models;

import java.util.ArrayList;
import java.util.List;

public class Trip {

	private String tripName = null;
	public String getTripName() {
		return tripName;
	}

	public void setTripName(String tripName) {
		this.tripName = tripName;
	}

	public List<Suitcase> getSuitcaseArray() {
		return suitcaseArray;
	}

	/*
	 * IMPORTANT NOTE!!!
	 * The getter/setter for the itemArray will return a reference to the object.
	 * If we need to mutate the object (change the list's contents), then we need to
	 * do object copying instead of just using the reference.
	 */
	public void setSuitcaseArray(List<Suitcase> suitcaseArray) {
		this.suitcaseArray = suitcaseArray;
	}

	private List<Suitcase> suitcaseArray;

	public Trip() {
		suitcaseArray = new ArrayList<Suitcase>();
	}

}
