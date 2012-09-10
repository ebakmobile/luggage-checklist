package com.lugcheck;

import java.util.Locale;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.support.v4.app.NavUtils;

public class SuitcaseActivity extends Activity {
	int trip_id;
	SQLiteDatabase db; 

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_suitcase);

		db=openOrCreateDatabase("data.db", SQLiteDatabase.CREATE_IF_NECESSARY, null);
		db.setVersion(1);
		db.setLocale(Locale.getDefault());
		
		
		Bundle extras = getIntent().getExtras();
		trip_id = extras.getInt("trip_id"); // receiving trip_id from previous activity 
		String bah= "In Suitcase Class, Trip_id is " + trip_id;
		Log.w("SDFDS", bah);
		
		
		/*code below is to set the activity title to the trip_name*/
		String GET_TRIP_NAME= "select * from trip_table where trip_id = '" + trip_id+ "'";
		Cursor c = db.rawQuery(GET_TRIP_NAME, null);
		c.moveToFirst();		
		String trip_name= c.getString(c.getColumnIndex("trip_name")); 
		setTitle("Suitcases for "+ trip_name + " trip");


	} //end onCreate


}
