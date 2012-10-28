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
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.inject.Injector;
import com.lugcheck.db.DAO;
import com.lugcheck.db.DAOImpl;
import com.lugcheck.db.DBException;
import com.lugcheck.guice.GuiceManager;

import com.google.ads.*;


public class MainActivity extends Activity {
	
	private AdView adView;
	static int limit; //limit to only creating one trip at a time
	SQLiteDatabase db;
	public static int TRIP_ID = 0;
	private DAO dao;
	private float density;
	

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

			/*Code Below handles the delete situation*/
			final String text2 = text;
			newTab.setOnLongClickListener(new OnLongClickListener() { //code to delete a list
				public boolean onLongClick(View v) {

					AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
					builder.setMessage("Are you sure you want delete?").setCancelable(false)
							.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int id) {
									deleteFromDB(text2);
								}
							}).setNegativeButton("No", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int id) {
									dialog.cancel();
								}
							});

					AlertDialog alert = builder.create();
					alert.show();
					return true;
				}
			});
			/* Code below handles the situation where u click a trip */
			final int trip_id2 = trip_id;
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

	public void deleteFromDB(String i) {

		String deleteFromDB = "delete from Trip where trip_name = '" + i + "'";
		db.execSQL(deleteFromDB);

		LinearLayout tripContainer = (LinearLayout) findViewById(R.id.trips_container);
		LinearLayout addTrip = (LinearLayout) findViewById(R.id.add_trip);
		tripContainer.removeAllViews();
		tripContainer.addView(addTrip);
		View ruler = new View(this);
		ruler.setBackgroundColor(Color.BLACK); // this code draws the black lines
		tripContainer.addView(ruler, new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT, 2));
		createLayoutsFromDB();
	}

	public void addTrip(View view) {
		if (limit == 0)//checking to make sure there is no open layouts 
		{
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
							String INSERT_STATEMENT = "INSERT INTO Trip (trip_name) Values ('"
									+ tripName + "')";
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
				}
			});
		}
	}

}
