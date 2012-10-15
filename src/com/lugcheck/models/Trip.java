/*	
	NOTICE for Luggage & Suitcase Checklist, an Android app:
    Copyright (C) 2012 EBAK Mobile

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
    */
package com.lugcheck.models;

import java.util.ArrayList;
import java.util.List;

public class Trip extends LugCheckObject {

	private int tripId;
	private String tripName;
	private List<Suitcase> suitcaseArray;

	public Trip() {
		super(ModelType.TRIP);
		suitcaseArray = new ArrayList<Suitcase>();
	}

	public Trip(int tripId, String tripName) {
		super(ModelType.TRIP);
		this.tripId = tripId;
		this.tripName = tripName;
	}

	public int getTripId() {
		return tripId;
	}

	public void setTripId(int tripId) {
		this.tripId = tripId;
	}

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
	 * IMPORTANT NOTE!!! The getter/setter for the itemArray will return a reference to the object. If we need to mutate the object (change the list's contents), then we need to do object copying
	 * instead of just using the reference.
	 */
	public void setSuitcaseArray(List<Suitcase> suitcaseArray) {
		this.suitcaseArray = suitcaseArray;
	}

}
