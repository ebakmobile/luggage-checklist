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
