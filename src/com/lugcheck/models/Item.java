package com.lugcheck.models;

public class Item extends LugCheckObject {

	private int itemId;
	private String itemName;
	private int quantity;
	private int suitcaseId;

	public Item() {
		super(ModelType.ITEM);
		// default quantity to 1
		quantity = 1;
		type = ModelType.ITEM;
	}

	public Item(int itemId, String itemName, int quantity, int suitcaseId) {
		super(ModelType.ITEM);
		this.itemId = itemId;
		this.itemName = itemName;
		this.quantity = quantity;
		this.suitcaseId = suitcaseId;
	}

	public int getItemId() {
		return itemId;
	}

	public void setItemId(int itemId) {
		this.itemId = itemId;
	}

	public int getSuitcaseId() {
		return suitcaseId;
	}

	public void setSuitcaseId(int suitcaseId) {
		this.suitcaseId = suitcaseId;
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
