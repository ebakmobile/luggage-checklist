package com.lugcheck.db;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import com.lugcheck.models.Item;
import com.lugcheck.models.LugCheckObject;
import com.lugcheck.models.Suitcase;
import com.lugcheck.models.Trip;

public class DAOImpl implements DAO {

	private final DBHelper dbHelper;

	public DAOImpl(DBHelper dbHelper) {
		this.dbHelper = dbHelper;
	}

	public <T extends LugCheckObject> void insertObject(T obj) {
		if (obj.getClass() == Item.class) {
			insertItem((Item) obj);
		} else if (obj.getClass() == Suitcase.class) {
			insertSuitcase((Suitcase) obj);
		} else if (obj.getClass() == Trip.class) {
			insertTrip((Trip) obj);
		} else {
			throw new IllegalArgumentException("Illegal or unsupported object passed into insertObject().");
		}
	}

	private void insertItem(Item item) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		SQLiteStatement statement = db
				.compileStatement("INSERT INTO Item(item_name, quantity, suitcase_id) VALUES(?, ?, ?)");
		db.beginTransaction();
		try {
			int i = 1;
			statement.bindString(i++, item.getItemName());
			statement.bindLong(i++, (long) item.getQuantity());
			statement.bindLong(i++, item.getSuitcaseId());
			if (statement.executeInsert() > -1) {
				db.setTransactionSuccessful();
			}
		} finally {
			db.endTransaction();
		}
	}

	private void insertSuitcase(Suitcase suitcase) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		SQLiteStatement statement = db
				.compileStatement("INSERT INTO Suitcase(suitcase_name, trip_id) VALUES(?, ?)");
		db.beginTransaction();
		try {
			int i = 1;
			statement.bindString(i++, suitcase.getSuitcaseName());
			statement.bindLong(i++, (long) suitcase.getTripId());
			if (statement.executeInsert() > -1) {
				db.setTransactionSuccessful();
			}
		} finally {
			db.endTransaction();
		}
	}

	private void insertTrip(Trip trip) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		SQLiteStatement statement = db.compileStatement("INSERT INTO Trip(trip_name) VALUES(?)");
		db.beginTransaction();
		try {
			statement.bindString(1, trip.getTripName());
			if (statement.executeInsert() > -1) {
				db.setTransactionSuccessful();
			}
		} finally {
			db.endTransaction();
		}
	}

	/**
	 * Temporary code to return a database reference to the rest of the application. Upon completion
	 * of refactoring, THIS METHOD SHOULD BE REMOVED!
	 * 
	 * @return Database reference
	 */
	public SQLiteDatabase temporaryFixForDb() {
		return dbHelper.getWritableDatabase();
	}

}
