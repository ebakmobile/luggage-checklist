package com.lugcheck.models;

import java.util.ArrayList;
import java.util.List;

public class Trip {

	private String tripName = null;
	private List<Suitcase> suitcaseArray;

	public Trip() {
		suitcaseArray = new ArrayList<Suitcase>();
	}

}
