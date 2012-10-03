package com.lugcheck.models;

public class Item extends LugCheckObject {
	
	private String itemName = null;
	private int quantity;
	
	public Item() {
		// default quantity to 1
		quantity = 1;
		type = ModelType.ITEM;
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
