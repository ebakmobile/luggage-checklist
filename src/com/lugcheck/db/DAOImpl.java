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
package com.lugcheck.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.lugcheck.models.Item;
import com.lugcheck.models.LugCheckObject;
import com.lugcheck.models.Suitcase;
import com.lugcheck.models.Trip;

public class DAOImpl implements DAO {

	private final String DB_NAME;
	private final int DB_VERSION;

	private DBHelper dbHelper;

	@Inject
	public DAOImpl(@Named("db.name") String dbName, @Named("db.version") String dbVersion) {
		DB_NAME = dbName;
		int tempVersion;
		try {
			tempVersion = Integer.parseInt(dbVersion);
		} catch (NumberFormatException e) {
			tempVersion = -1; // This default value will signify an error;
			Log.e("DB Error", "An illegal version number was used in the db properties file. "
					+ "A non-numeric value was probably used.");
		}
		DB_VERSION = tempVersion;
	}

	public void setup(Context context) throws DBException {
		dbHelper = DBHelper.getInstance(context, DB_NAME, DB_VERSION);
	}

	public void insertObject(LugCheckObject obj) {
		switch (obj.getType()) {
		case ITEM:
			insertItem((Item) obj);
			break;
		case SUITCASE:
			insertSuitcase((Suitcase) obj);
			break;
		case TRIP:
			insertTrip((Trip) obj);
			break;
		default:
			// ERROR
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
