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
package com.lugcheck;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;
import com.google.inject.Injector;
import com.lugcheck.db.DAO;
import com.lugcheck.db.DAOImpl;
import com.lugcheck.db.DBException;
import com.lugcheck.guice.GuiceManager;


public class MainActivity extends Activity {

	private AdView adView;
	static int limit; //limit to only creating one trip at a time
	SQLiteDatabase db;
	private DAO dao;
	private float density;
	int intTripId;
	final private CharSequence longClickOptions[]={"Edit Trip","Delete Trip", "Cancel"};

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		GuiceManager.getInitialInstance(this);
		setContentView(R.layout.activity_main);
		limit = 0;
		String myAdmobPublisherID="a1508d762ede868";
		adView = new AdView(this, AdSize.SMART_BANNER, myAdmobPublisherID); 

		try {
			initDb();
		} catch (DBException e) {
			e.printStackTrace();
			// Close because of error
			System.exit(1);
		}

		/*
		 * Will set the global db to support "Legacy Code".
		 * The goal is to replace all references to a DB object in ALL CODE, except for anything in the db package.
		 * This major refactoring step will encapsulate db code from the rest of the application, like it have been all along.
		 * For now, the following line of code is the temporary fix.
		 */
		db = ((DAOImpl) dao).temporaryFixForDb();

		density = this.getResources().getDisplayMetrics().density;

		/* code below just adds a black horizontal line*/
		LinearLayout tripContainer = (LinearLayout) findViewById(R.id.trips_container);
		View ruler = new View(this);
		ruler.setBackgroundColor(Color.BLACK); // this code draws the black lines
		tripContainer.addView(ruler, new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT, 2));
		createLayoutsFromDB();

		/* Code below pops up dialogue explaining the app*/
		Cursor c = db.rawQuery("SELECT * from Read", null);

		if (c.getCount() <= 0) {
			AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
			builder.setMessage(
					"This app will help you to keep track of your travel checklist. Your checklist will be permanently saved onto your device so you can re-use it for future trips!")
					.setTitle("Welcome!").setCancelable(false)
					.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							dialog.cancel();
						}
					})
					.setNegativeButton("Do not show again", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							db.execSQL("INSERT INTO Read (read) Values ('1');");
							dialog.cancel();

						}
					});
			AlertDialog alert = builder.create();
			alert.show();
		}
		c.close();
	}

	public void initDb() throws DBException {
		Injector injector = GuiceManager.getInstance().getInjector();
		dao = injector.getInstance(DAO.class);
		dao.setup(this);
	}

	public void createLayoutsFromDB() {


		/* Code Below fetches trips from trip_table and creates a layout*/
		Cursor c = db.rawQuery("SELECT * from Trip", null);
		c.moveToFirst();
		while (c.isAfterLast() == false) {

			TextView hw = new TextView(this);
			String text = c.getString(c.getColumnIndex("trip_name"));
			int trip_id = c.getInt(c.getColumnIndex("trip_id"));
			hw.setText(text);
			hw.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
			ImageView im = new ImageView(this);
			im.setImageResource(R.drawable.plane);
			// FROM STACKOVERFLOW!
			int width = (int) (58 * density);
			int height = (int) (50 * density);
			im.setLayoutParams(new LayoutParams(width, height));
			int pad = (int) (5 * density);
			im.setPadding(pad, pad, 0, 0);
			// END
			int txtPadding = (int) (20 * density);
			hw.setPadding(0, txtPadding, 0, 0);

			LinearLayout newTab = new LinearLayout(this);
			newTab.setOrientation(LinearLayout.HORIZONTAL);
			newTab.addView(im);
			newTab.addView(hw);
			newTab.setBackgroundColor(Color.WHITE);
			LinearLayout tripContainer = (LinearLayout) findViewById(R.id.trips_container);
			tripContainer.addView(newTab);

			View ruler = new View(this);
			ruler.setBackgroundColor(Color.BLACK); // this code draws the black lines
			tripContainer.addView(ruler, new ViewGroup.LayoutParams(
					ViewGroup.LayoutParams.MATCH_PARENT, 2));

			c.moveToNext();

			/*Code Below handles the long click situation*/
			final String text2 = text; // text2 is the name of the trip
			final int trip_id2 = trip_id;
			newTab.setOnLongClickListener(new OnLongClickListener() { //code to delete a list
				public boolean onLongClick(View v) {

					AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
					builder.setTitle("Select your option");
					builder.setItems(longClickOptions, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							switch(which){
							case 0://edit 
								editFromDB(text2,trip_id2); 
								return;
							case 1://delete    
								deleteFromDB(text2);
								return;
							case 2: //cancel
								dialog.cancel();
								return;
							}
						}
					});


					AlertDialog alert = builder.create();
					alert.show();
					return true;
				}
			});
			/* Code below handles the situation where u click a trip */
			newTab.setOnClickListener(new Button.OnClickListener() {
				public void onClick(View v) {

					Intent intent = new Intent(MainActivity.this, SuitcaseActivity.class);
					intent.putExtra("trip_id", trip_id2);
					startActivity(intent);

				}
			});
		}
		c.close();
	}

	public void editFromDB(final String name,final int trip_id)
	{	
		final EditText editText = new EditText(MainActivity.this);
		editText.setHint("New Trip Name");
		editText.setText(name);
		AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
		builder.setMessage("Please enter a new name for '" + name+"'").setCancelable(false)
		.setView(editText)
		.setPositiveButton("Complete", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				String newName = editText.getText().toString();	
									
				String currTripName;
				boolean canInsert= canInsert(newName,name, trip_id);				
				if(canInsert==true){
					String editDB = "UPDATE Trip SET trip_name=\"" + newName + "\" WHERE trip_id=\"" + trip_id + "\" and " +
							"trip_name=\""+ name +"\"";
					db.execSQL(editDB);
					limit=0;
					LinearLayout tripContainer = (LinearLayout) findViewById(R.id.trips_container);
					LinearLayout addTrip = (LinearLayout) findViewById(R.id.add_trip);
					tripContainer.removeAllViews();
					tripContainer.addView(addTrip);
					View ruler = new View(MainActivity.this);
					ruler.setBackgroundColor(Color.BLACK); // this code draws the black lines
					tripContainer.addView(ruler, new ViewGroup.LayoutParams(
							ViewGroup.LayoutParams.MATCH_PARENT, 2));
					createLayoutsFromDB();
				}		
			}
		}).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
			}
		});
		AlertDialog alert = builder.create();
		alert.show();

	}
	public void deleteFromDB(final String i) {
		AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
		builder.setMessage("Are you sure you want to delete?").setCancelable(false)
		.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				String deleteFromDB = "delete from Trip where trip_name = \"" + i + "\"";
				db.execSQL(deleteFromDB);
				limit=0;
				LinearLayout tripContainer = (LinearLayout) findViewById(R.id.trips_container);
				LinearLayout addTrip = (LinearLayout) findViewById(R.id.add_trip);
				tripContainer.removeAllViews();
				tripContainer.addView(addTrip);
				View ruler = new View(MainActivity.this);
				ruler.setBackgroundColor(Color.BLACK); // this code draws the black lines
				tripContainer.addView(ruler, new ViewGroup.LayoutParams(
						ViewGroup.LayoutParams.MATCH_PARENT, 2));
				createLayoutsFromDB();
			}
		}).setNegativeButton("No", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
			}
		});
		AlertDialog alert = builder.create();
		alert.show();

	}


	public void showDupeItem(){
		AlertDialog dupe = new AlertDialog.Builder(
				MainActivity.this).create();
		dupe.setTitle("Duplicate Found");
		dupe.setMessage("Trip name already exists");
		dupe.setButton("Ok",
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,
					int which) {
			}
		});
		dupe.show();
	}

	public boolean canInsert(String itemName, String oldName, int trip_id){

		if (itemName.equals("")) // if they try to add a null
			// trip to database
		{
			AlertDialog dupe = new AlertDialog.Builder(MainActivity.this).create();
			dupe.setMessage("You cannot enter a blank trip name. Please enter a trip name");
			dupe.setButton("Ok", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
				}
			});

			dupe.show();
			return false;

		} 
		else if(itemName.contains("\""))
		{
			AlertDialog dupe = new AlertDialog.Builder(MainActivity.this).create();
			dupe.setMessage("Invalid character detected. Please enter alphabets or numbers for trip name");
			dupe.setButton("Ok", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
				}
			});

			dupe.show();
			return false;
			
		}

		else {
			String currItemName;
			Cursor c = db.rawQuery("SELECT * from Trip", null);
			c.moveToFirst();
			while (c.isAfterLast() == false) {// code will check for duplicates
				currItemName = c.getString(c.getColumnIndex("trip_name"));
				c.moveToNext();
				if (itemName.equals(currItemName)) {
					showDupeItem();				
					return false;
				}
			}
			c.close();
		}

		return true;

	}		

	public void addTrip(View view) {
		if (limit == 0)//checking to make sure there is no open layouts 
		{	AdView bottomAd=(AdView)findViewById(R.id.adView);
		bottomAd.setVisibility(View.INVISIBLE);
		limit = 1;
		LayoutParams lp = new LayoutParams(MATCH_PARENT, WRAP_CONTENT);
		final LinearLayout newTab = new LinearLayout(this);
		newTab.setOrientation(LinearLayout.VERTICAL);
		final EditText textBox = new EditText(this);
		textBox.setHint("Enter trip name");
		textBox.setHeight((int) (60 * density));
		final Button okButton = new Button(this);
		okButton.setText("Add");
		final Button cancelButton = new Button(this);
		cancelButton.setText("Cancel");

		LinearLayout ll = new LinearLayout(this);
		ll.setOrientation(LinearLayout.VERTICAL);
		ll.addView(textBox);

		LinearLayout horizontalButtons = new LinearLayout(this);
		horizontalButtons.setOrientation(LinearLayout.HORIZONTAL);// used to make button horizontal
		LayoutParams param = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT, 1.0f); //param sets it so the width is 50% horizontally. Looked nicer!
		horizontalButtons.addView(okButton, param);
		horizontalButtons.addView(cancelButton, param);
		ll.addView(horizontalButtons);

		newTab.addView(ll);
		View ruler = new View(this);
		ruler.setBackgroundColor(Color.BLACK); // this code draws the black lines
		newTab.addView(ruler,
				new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 2));

		LinearLayout tripContainer = (LinearLayout) findViewById(R.id.trips_container);
		adView.loadAd(new AdRequest());
		tripContainer.addView(adView,0); //adds the advertisement in the top		
		tripContainer.addView(newTab, 1, lp);

		//Code below is for adding a trip to the database
		okButton.setOnClickListener(new View.OnClickListener() {
			@SuppressWarnings("deprecation")
			public void onClick(View v) {

				LinearLayout buttonParent = (LinearLayout) okButton.getParent().getParent();
				EditText textBox = (EditText) buttonParent.getChildAt(0);//gets value of textbox 
				String tripName = textBox.getText().toString();

				if (tripName.equals("")) //if they try to add a null trip to database
				{
					AlertDialog dupe = new AlertDialog.Builder(MainActivity.this).create();
					dupe.setMessage("You cannot enter a blank trip name. Please enter a trip name");
					dupe.setButton("Ok", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
						}
					});

					dupe.show();

				}
				else if(tripName.contains("\""))
					{
						AlertDialog dupe = new AlertDialog.Builder(MainActivity.this).create();
						dupe.setMessage("Invalid character detected. Please enter alphabets or numbers for trip name");
						dupe.setButton("Ok", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
							}
						});

						dupe.show();
					}
				//code below checks for duplicates in database
				else {
					Cursor c = db.rawQuery("SELECT * from Trip", null);
					c.moveToFirst();

					boolean isDupe = false;
					while (c.isAfterLast() == false) {
						String text = c.getString(c.getColumnIndex("trip_name"));
						c.moveToNext();
						if (text.equals(tripName)) {
							isDupe = true;
							break;
						}
					}//end while*/

					c.close();

					if (isDupe == false) {
						String INSERT_STATEMENT = "INSERT INTO Trip (trip_name) Values (\""
								+ tripName + "\")";
						db.execSQL(INSERT_STATEMENT); // insert into trip_table db

						limit = 0;
						LinearLayout tripContainer = (LinearLayout) findViewById(R.id.trips_container);
						LinearLayout addTrip = (LinearLayout) findViewById(R.id.add_trip);
						tripContainer.removeAllViews();
						tripContainer.addView(addTrip);
						View ruler = new View(MainActivity.this);
						ruler.setBackgroundColor(Color.BLACK); // this code draws the black lines
						tripContainer.addView(ruler, new ViewGroup.LayoutParams(
								ViewGroup.LayoutParams.MATCH_PARENT, 2));
						createLayoutsFromDB();
						AdView bottomAd=(AdView)findViewById(R.id.adView);
						bottomAd.setVisibility(View.VISIBLE);
					} else {

						AlertDialog dupe = new AlertDialog.Builder(MainActivity.this).create();
						dupe.setTitle("Duplicate Found");
						dupe.setMessage("Trip name already exists. Please use that trip instead");
						dupe.setButton("Ok", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
							}
						});

						dupe.show();
					}//end else
				}
			}
		});

		cancelButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				limit = 0;
				LinearLayout tripContainer = (LinearLayout) findViewById(R.id.trips_container);
				LinearLayout addTrip = (LinearLayout) findViewById(R.id.add_trip);
				tripContainer.removeAllViews();
				tripContainer.addView(addTrip);
				View ruler = new View(MainActivity.this);
				ruler.setBackgroundColor(Color.BLACK); // this code draws the black lines
				tripContainer.addView(ruler, new ViewGroup.LayoutParams(
						ViewGroup.LayoutParams.MATCH_PARENT, 2));
				createLayoutsFromDB();
				AdView bottomAd=(AdView)findViewById(R.id.adView);
				bottomAd.setVisibility(View.VISIBLE);
			}
		});
		}
	}

}