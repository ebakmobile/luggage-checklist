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

import java.util.Arrays;
import java.util.List;
import roboguice.inject.ContextSingleton;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.google.inject.Inject;
import com.google.inject.name.Named;

@ContextSingleton
public class DBHelper extends SQLiteOpenHelper {

	private final int DB_CURRENT_VERSION;

	private final Context context;

	@Inject
	public DBHelper(Context context, @Named("db.name") String dbName, @Named("db.version") int dbVersion) {
		super(context, dbName, null, dbVersion);
		this.context = context;
		DB_CURRENT_VERSION = dbVersion;
		// Create the database by getting a writable DB
		getWritableDatabase();
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		List<String> statements = DBUtil.getStatementsFromSqlFile(context, "database/create/schema-" + DB_CURRENT_VERSION + ".sql");
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
		List<String> temp = Arrays.asList(new String[] { statement });
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
			Log.w("DB WARNING", "Failed transaction, SQLite statements aborted and rolledback.");
		} finally {
			db.endTransaction();
		}
	}

}
