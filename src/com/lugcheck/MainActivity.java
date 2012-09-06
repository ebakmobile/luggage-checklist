package com.lugcheck;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity {
	static int limit; //limit to only creating one trip at a time
	private static final int DATABASE_VERSION = 2;
	SQLiteDatabase db; 
    private static final String TRIP_TABLE_CREATE = "CREATE TABLE IF NOT EXISTS trip_table(trip_id integer PRIMARY KEY autoincrement, trip_name text);";
    private static final String SUITCASE_TABLE_CREATE = "CREATE TABLE IF NOT EXISTS suitcase_table(suitcase_id integer PRIMARY KEY autoincrement, suitcase_name text, trip_id INTEGER REFERENCES trip_table (trip_id) );";
    private static final String ITEM_TABLE_CREATE = "CREATE TABLE IF NOT EXISTS item_table(item_id integer PRIMARY KEY autoincrement, item_name text, quantity integer, suitcase_id INTEGER REFERENCES suitcase_table(suitcase_id)) ;";
    
    //db.execSQL("INSERT INTO suitcase_table (suitcase_name, trip_id) Values ('suitcase test', 2)"); // insert into suticase_table db
	//db.execSQL("INSERT INTO item_table (item_name, quantity, suitcase_id) Values ('item test', 100, 1)"); // insert into item_table db
	
	
    
    
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		limit=0;
		db=openOrCreateDatabase("data.db", SQLiteDatabase.CREATE_IF_NECESSARY, null);
		db.setVersion(1);
		db.setLocale(Locale.getDefault());
		db.setLocale(Locale.getDefault());
		db.execSQL(TRIP_TABLE_CREATE);
		db.execSQL(SUITCASE_TABLE_CREATE);
		db.execSQL(ITEM_TABLE_CREATE);

		
		/* Code Below fetches trips from trip_table and creates a layout*/
		 Cursor c = db.rawQuery("SELECT * from trip_table", null);
		 c.moveToFirst();
		while (c.isAfterLast() == false)
		 {  
			
			
			TextView hw= new TextView(this);
			String text=c.getString(c.getColumnIndex("trip_name")); 
			hw.setText(text);
			
			//LinearLayout newTab = (LinearLayout) findViewById(R.id.existing_trip);
			//LinearLayout newTab= (LinearLayout)View.inflate(this, R.layout.activity_main, null);
		    LinearLayout newTab=new LinearLayout(this);
			newTab.addView(hw);   

		    LayoutParams lp = new LayoutParams(MATCH_PARENT, WRAP_CONTENT);
		    newTab.setOrientation(LinearLayout.VERTICAL);
			
			
			LinearLayout tripContainer = (LinearLayout) findViewById(R.id.trips_container);
			tripContainer.addView(newTab, 0,lp);
			//tripContainer.addView(newTab);
			c.moveToNext();
			 			 
		 }//end while*/
		 
		c.close();
				
		}

	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	public void addTrip(View view) {


		if (limit==0)//checking to make sure there is no open layouts 
		{ 
			limit =1;
			LayoutParams lp = new LayoutParams(MATCH_PARENT, WRAP_CONTENT);
			final LinearLayout newTab = new LinearLayout(this);
			newTab.setOrientation(LinearLayout.VERTICAL);
			EditText hw = new EditText(this);
			hw.setText("Enter Trip Name");
			final Button okButton=new Button(this);
			okButton.setText("Add");
			final Button cancelButton=new Button(this);
			cancelButton.setText("Cancel");

			LinearLayout ll=new LinearLayout(this);
			ll.setOrientation(LinearLayout.HORIZONTAL);
			ll.addView(hw);
			ll.addView(okButton);
			ll.addView(cancelButton);

			newTab.addView(ll);

			LinearLayout tripContainer = (LinearLayout) findViewById(R.id.trips_container);
			//int count = tripContainer.getChildCount();
			tripContainer.addView(newTab, 0,lp);

			// TODO: Set id for the new trip tab to new_trip_tab. Once name is entered, change the ID.
			// TODO: Check for uniqueness

			okButton.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					limit =0; // allow to recreate a new button
					
					//TODO: check to make sure name doesnt already exist in Database
					
					LinearLayout buttonParent = (LinearLayout) okButton.getParent();
					EditText textBox = (EditText) buttonParent.getChildAt(0);//gets value of textbox 
					String tripName = textBox.getText().toString();
					String INSERT_STATEMENT= "INSERT INTO trip_table (trip_name) Values ('"+ tripName+ "')";
					db.execSQL(INSERT_STATEMENT); // insert into trip_table db
										
					// TODO: Switch to trip activity
				}
			});

			cancelButton.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					limit=0;
					LinearLayout tripContainer = (LinearLayout) findViewById(R.id.trips_container); 	
					int count=tripContainer.getChildCount();
					tripContainer.removeViewAt(0);	// TODO: Look on internet on how to find the proper count

					/*cancelButton.getParent();
					tripContainer.removeView(parent);*/
				}
			});



		}

	}




}



