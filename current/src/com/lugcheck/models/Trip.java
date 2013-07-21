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

public class Trip extends LugCheckObject {

	private final int tripId;
	private final String tripName;
	private final List<Suitcase> suitcases = new ArrayList<Suitcase>();

	public Trip(int tripId, String tripName) {
		this.tripId = tripId;
		this.tripName = tripName;
	}

	public int getTripId() {
		return tripId;
	}

	public String getTripName() {
		return tripName;
	}

	public List<Suitcase> getSuitcases() {
		return suitcases;
	}
}
