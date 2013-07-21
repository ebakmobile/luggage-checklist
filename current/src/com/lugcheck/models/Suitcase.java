/*	
	NOTICE for Luggage & Suitcase Checklist, an Android app:
    Copyright (C) 2013 EBAK Mobile

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

public class Suitcase extends LugCheckObject {

	private final int suitcaseId;
	private final int tripId;
	private final String suitcaseName;
	private final List<Item> items = new ArrayList<Item>();

	public Suitcase(int suitcaseId, int tripId, String suitcaseName) {
		this.suitcaseId = suitcaseId;
		this.tripId = tripId;
		this.suitcaseName = suitcaseName;
	}

	public int getSuitcaseId() {
		return suitcaseId;
	}

	public int getTripId() {
		return tripId;
	}

	public String getSuitcaseName() {
		return suitcaseName;
	}

	public List<Item> getItems() {
		return items;
	}

	public void addItem(Item item) {
		items.add(item);
	}
}
