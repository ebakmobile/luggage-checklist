package com.lugcheck.models;

public class Item {
	
	private String itemName = null;
	private int quantity;
	
	public Item() {
		// default quantity to 1
		quantity = 1;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
}
