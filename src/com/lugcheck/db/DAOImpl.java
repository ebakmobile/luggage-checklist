package com.lugcheck.db;

import android.content.Context;
import android.util.Log;

import com.google.inject.Inject;
import com.google.inject.name.Named;

public class DAOImpl implements DAO {
	
	private final String DB_NAME;
	private final int DB_VERSION;
	
	DBHelper dbHelper;
	
	@Inject
	public DAOImpl(@Named("db.name")String dbName, @Named("db.version")String dbVersion) {
		DB_NAME = dbName;
		int tempVersion;
		try {
			tempVersion = Integer.parseInt(dbVersion);
		} catch (NumberFormatException e) {
			tempVersion = -1;	// This default value will signify an error;
			Log.e("DB Error", "An illegal version number was used in the db properties file. " +
					"A non-numeric value was probably used.");
		}
		DB_VERSION = tempVersion;	// Set to this value to signify an error;
	}

	public void setup(Context context) throws DBException {
		dbHelper = DBHelper.getInstance(context, DB_NAME, DB_VERSION);
	}
	
}
