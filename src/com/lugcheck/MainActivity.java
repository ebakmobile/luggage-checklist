package com.lugcheck;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

import java.util.ArrayList;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.support.v4.app.NavUtils;

public class MainActivity extends Activity {
	static int limit; //limit to only creating one trip at a time
	private static final int DATABASE_VERSION = 2;
	SQLiteDatabase db; 
	private static final String TRIP_TABLE_CREATE = "CREATE TABLE IF NOT EXISTS trip_table(trip_id integer PRIMARY KEY autoincrement, trip_name text);";
	private static final String SUITCASE_TABLE_CREATE = "CREATE TABLE IF NOT EXISTS suitcase_table(suitcase_id integer PRIMARY KEY autoincrement, suitcase_name text, trip_id INTEGER REFERENCES trip_table (trip_id) );";
	private static final String ITEM_TABLE_CREATE = "CREATE TABLE IF NOT EXISTS item_table(item_id integer PRIMARY KEY autoincrement, item_name text, quantity integer, suitcase_id INTEGER REFERENCES suitcase_table(suitcase_id)) ;";
	
	//db.execSQL("INSERT INTO suitcase_table (suitcase_name, trip_id) Values ('suitcase test', 2)"); // insert into suticase_table db
	//db.execSQL("INSERT INTO item_table (item_name, quantity, suitcase_id) Values ('item test', 100, 1)"); // insert into item_table db

	ArrayList <LinearLayout> arrayLL; //array list that stores all layouts
	public static int TRIP_ID = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		arrayLL= new ArrayList<LinearLayout>();
		limit=0;
		db=openOrCreateDatabase("data.db", SQLiteDatabase.CREATE_IF_NECESSARY, null);
		db.setVersion(1);
		db.setLocale(Locale.getDefault());
		db.setLocale(Locale.getDefault());
		db.execSQL(TRIP_TABLE_CREATE);
		db.execSQL(SUITCASE_TABLE_CREATE);
		db.execSQL(ITEM_TABLE_CREATE);
		createLayoutsFromDB();

	}



	public void createLayoutsFromDB()
	{

		/* Code Below fetches trips from trip_table and creates a layout*/
		Cursor c = db.rawQuery("SELECT * from trip_table", null);
		c.moveToFirst();
		while (c.isAfterLast() == false)
		{  

			TextView hw= new TextView(this);
			String text=c.getString(c.getColumnIndex("trip_name")); 
			int trip_id=c.getInt(c.getColumnIndex("trip_id")); 
			hw.setText(text);
			hw.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
			ImageView im=new ImageView(this);
			im.setImageResource(R.drawable.plane);
			// FROM STACKOVERFLOW!
			float d = this.getResources().getDisplayMetrics().density;
			int width = (int)(58 * d);
			int height = (int)(50 * d);
			im.setLayoutParams(new LayoutParams(width, height));
			int pad = (int)(5*d);
			im.setPadding(pad, pad, 0, 0);
			// END
			int txtPadding=(int)(20*d);
			hw.setPadding(0, txtPadding, 0, 0);


			LinearLayout newTab =new LinearLayout(this);
			newTab.setOrientation(LinearLayout.HORIZONTAL);
			newTab.addView(im);
			newTab.addView(hw);   
			newTab.setBackgroundColor(Color.WHITE);
			LinearLayout tripContainer = (LinearLayout) findViewById(R.id.trips_container);

			tripContainer.addView(newTab);
			arrayLL.add(newTab);
			c.moveToNext();



			/*Code Below handles the delete situation*/
			final String text2=text;
			newTab.setOnLongClickListener(new OnLongClickListener() { //code to delete a list
				public boolean onLongClick(View v) {

					AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
					builder.setMessage("Are you sure you want delete?")
					.setCancelable(false)
					.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							deleteFromDB(text2);	 
						}
					})
					.setNegativeButton("No", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							dialog.cancel();
						}
					});

					AlertDialog alert = builder.create();
					alert.show();
					return true;	
				}
			});


			/* Code below handles the situation where u want to open a trip */
			final int trip_id2=trip_id;
			newTab.setOnClickListener(new Button.OnClickListener() {
				public void onClick(View v) {

					Intent intent = new Intent(MainActivity.this, TripActivity.class);
					intent.putExtra("trip_id", trip_id2);
					startActivity(intent);

				}});





		}//end while*/


		c.close();

	}//end method



	public void deleteFromDB(String i)
	{

		String deleteFromDB= "delete from trip_table where trip_name = '" + i+ "'";
		db.execSQL(deleteFromDB);	

		LinearLayout tripContainer = (LinearLayout) findViewById(R.id.trips_container); 
		LinearLayout addTrip=(LinearLayout) findViewById(R.id.add_trip); 
		tripContainer.removeAllViews();
		tripContainer.addView(addTrip);
		createLayoutsFromDB();	

	}

	public void addTrip(View view) {


		if (limit==0)//checking to make sure there is no open layouts 
		{ 
			limit =1;
			LayoutParams lp = new LayoutParams(MATCH_PARENT, WRAP_CONTENT);
			final LinearLayout newTab = new LinearLayout(this);
			newTab.setOrientation(LinearLayout.VERTICAL);
			final EditText hw = new EditText(this);
			hw.setHint("Enter trip name");	
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
			tripContainer.addView(newTab, 0,lp);

			//Code below is for adding a trip to the database
			okButton.setOnClickListener(new View.OnClickListener() { 
				@SuppressWarnings("deprecation")
				public void onClick(View v) {
					limit =0; // allow to recreate a new trip

					LinearLayout buttonParent = (LinearLayout) okButton.getParent();
					EditText textBox = (EditText) buttonParent.getChildAt(0);//gets value of textbox 
					String tripName = textBox.getText().toString();

					if(tripName.equals("")) //if they try to add a null trip to database
					{
						AlertDialog dupe = new AlertDialog.Builder(MainActivity.this).create();
						dupe.setMessage("You cannot enter a blank trip name. Please enter a trip name");
						dupe.setButton("Ok", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {			 
							}
						} );

						dupe.show();

					}


					//code below checks for duplicates in database
					else {
						Cursor c = db.rawQuery("SELECT * from trip_table", null);
						c.moveToFirst();

						boolean isDupe=false;
						while (c.isAfterLast() == false)
						{  
							String text=c.getString(c.getColumnIndex("trip_name")); 
							c.moveToNext();
							if(text.equals(tripName))
							{isDupe=true;
							break;
							}
						}//end while*/

						c.close();

						if (isDupe==false)
						{String INSERT_STATEMENT= "INSERT INTO trip_table (trip_name) Values ('"+ tripName+ "')";
						db.execSQL(INSERT_STATEMENT); // insert into trip_table db


						LinearLayout tripContainer = (LinearLayout) findViewById(R.id.trips_container); 
						LinearLayout addTrip=(LinearLayout) findViewById(R.id.add_trip); 
						tripContainer.removeAllViews();
						tripContainer.addView(addTrip);
						createLayoutsFromDB();
						}

						else
						{

							AlertDialog dupe = new AlertDialog.Builder(MainActivity.this).create();
							dupe.setTitle("Duplicate Found");
							dupe.setMessage("Trip already exists. Please use that trip instead");
							dupe.setButton("Ok", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int which) {			 
								}
							} );

							dupe.show();



						}//end else


					}
				}
			});

			cancelButton.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					limit=0;
					LinearLayout tripContainer = (LinearLayout) findViewById(R.id.trips_container); 	
					LinearLayout addTrip=(LinearLayout) findViewById(R.id.add_trip); 

					tripContainer.removeAllViews();
					tripContainer.addView(addTrip);
					createLayoutsFromDB();

				}
			});



		}

	}




}



