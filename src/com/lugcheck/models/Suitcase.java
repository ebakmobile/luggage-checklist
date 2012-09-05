package com.lugcheck.models;

import java.util.ArrayList;
import java.util.List;

public class Suitcase {
	
	private String suitcaseName = null;
	private List<Item> itemArray;
	
	public Suitcase() {
		itemArray = new ArrayList<Item>();
	}

}
