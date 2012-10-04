package com.lugcheck.db;

import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {
	
	//private final String DB_NAME;
	private final int DB_CURRENT_VERSION;
	
	private Context context;
	
	private static DBHelper thisInstance = null;
	
	private DBHelper(Context context, String dbName, int dbVersion) {
		super(context, dbName, null, dbVersion);
		this.context = context;
		DB_CURRENT_VERSION = dbVersion;
		// Create the database by getting a writable DB
		getWritableDatabase();
	}
	
	public static DBHelper getInstance(Context context, String dbName, int dbVersion) {
		if (thisInstance == null) {
			thisInstance = new DBHelper(context, dbName, dbVersion);
		}
		return thisInstance;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		List<String> statements = DBUtil.getStatementsFromSqlFile(context, "database/create/schema-"
				+ DB_CURRENT_VERSION + ".sql");
		executeStatements(statements, db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Not supported because there are no db version updates available yet.
	}
	
	public void executeStatements(List<String> statements) {
		executeStatements(statements, getWritableDatabase());
	}
	
	public void executeStatement(String statement) {
		List<String> temp = Arrays.asList(new String[] {statement});
		executeStatements(temp, getWritableDatabase());
	}
	
	private void executeStatements(List<String> statements, SQLiteDatabase db) {
		db.beginTransaction();
		try {
			for (String statement : statements) {
				db.execSQL(statement);
				// TODO: Maybe choose a smarter db function for the SQL command (ie, check first word of command whether it is insert, update or delete)
			}
			db.setTransactionSuccessful();
		} catch (SQLiteException e) {
			// Do something about the failed transaction
		} finally {
			db.endTransaction();
		}
	}

}
