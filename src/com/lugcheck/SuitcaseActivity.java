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

import java.util.Locale;

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
import com.google.ads.*;

public class SuitcaseActivity extends Activity {
	int tripId;
	SQLiteDatabase db;
	static int limit; //limit to only creating one trip at a time
	public static int SUITCASE_ID = 0;
	private float density;
	private AdView adView;
	final private CharSequence longClickOptions[]={"Edit Suitcase","Delete Suitcase", "Cancel"};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_suitcase);
		density = this.getResources().getDisplayMetrics().density;
		
		String myAdmobPublisherID="a1508d762ede868";
		adView = new AdView(this, AdSize.SMART_BANNER, myAdmobPublisherID); 
		
		db = openOrCreateDatabase("data.db", SQLiteDatabase.CREATE_IF_NECESSARY, null);
		db.setVersion(1);
		db.setLocale(Locale.getDefault());
		limit = 0;

		Bundle extras = getIntent().getExtras();
		tripId = extras.getInt("trip_id"); // receiving trip_id from previous activity 
		
		/*code below is to set the activity title to the trip_name*/
		String GET_TRIP_NAME = "select * from Trip where trip_id = '" + tripId + "'";
		Cursor c = db.rawQuery(GET_TRIP_NAME, null);
		c.moveToFirst();
		String trip_name = c.getString(c.getColumnIndex("trip_name"));
		setTitle("Displaying Suitcases for " + trip_name);

		LinearLayout tripContainer = (LinearLayout) findViewById(R.id.suitcase_container);
		View ruler = new View(this);
		ruler.setBackgroundColor(Color.BLACK); // this code draws the black lines
		tripContainer.addView(ruler, new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT, 2));
		createLayoutsFromDB();
		c.close();

	} //end onCreate

	public void createLayoutsFromDB() {

		/* Code Below fetches trips from trip_table and creates a layout*/
		Cursor c = db.rawQuery("SELECT * from Suitcase where trip_id = '" + tripId + "'", null);
		c.moveToFirst();
		while (c.isAfterLast() == false) {

			TextView hw = new TextView(this);
			String text = c.getString(c.getColumnIndex("suitcase_name"));
			int SUITCASE_ID = c.getInt(c.getColumnIndex("suitcase_id"));
			hw.setText(text);
			hw.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
			ImageView im = new ImageView(this);
			im.setImageResource(R.drawable.suitcase_icon);
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
			LinearLayout tripContainer = (LinearLayout) findViewById(R.id.suitcase_container);

			tripContainer.addView(newTab);
			View ruler = new View(this);
			ruler.setBackgroundColor(Color.BLACK); // this code draws the black lines
			tripContainer.addView(ruler, new ViewGroup.LayoutParams(
					ViewGroup.LayoutParams.MATCH_PARENT, 2));
			c.moveToNext();

			/*Code Below handles the long click situation*/
			final String text2 = text;
			newTab.setOnLongClickListener(new OnLongClickListener() { //code to delete a list
				public boolean onLongClick(View v) {

					 AlertDialog.Builder builder = new AlertDialog.Builder(SuitcaseActivity.this);
					    builder.setTitle("Select your option");
					           builder.setItems(longClickOptions, new DialogInterface.OnClickListener() {
					               public void onClick(DialogInterface dialog, int which) {
					            switch(which){
					            case 0://edit 
					              editFromDB(text2);
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

			/* Code below handles the situation where u click a item */
			final int suitcase_id2 = SUITCASE_ID;
			newTab.setOnClickListener(new Button.OnClickListener() {
				public void onClick(View v) {

					Intent intent = new Intent(SuitcaseActivity.this, ItemActivity.class);
					intent.putExtra("suitcase_id", suitcase_id2);
					startActivity(intent);
				}
			});
		}
		c.close();
	}

	public void editFromDB(final String name)
	{
		final EditText editText = new EditText(SuitcaseActivity.this);
		editText.setHint("New Suitcase Name");
		AlertDialog.Builder builder = new AlertDialog.Builder(SuitcaseActivity.this);
		builder.setMessage("Please enter a new name for " + name).setCancelable(false)
				.setView(editText)
				.setPositiveButton("Complete", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						String newName = editText.getText().toString();		
						String currTripName;
						boolean isDupe=false;
						Cursor c = db.rawQuery("SELECT * from Suitcase where trip_id='"+tripId+"'", null);
						c.moveToFirst();
						while (c.isAfterLast() == false) {// code will check for duplicates
							currTripName = c.getString(c.getColumnIndex("suitcase_name"));
							c.moveToNext();
							if (newName.equals(currTripName)) {
								isDupeTrue();
								isDupe=true;
							}
						}
						c.close();
						
					if(isDupe==false){
					String editDB = "UPDATE Suitcase SET suitcase_name='" + newName + "' WHERE suitcase_name='" + name + 
							"' and trip_id = '"+tripId +"'";
					db.execSQL(editDB);
					
					LinearLayout tripContainer = (LinearLayout) findViewById(R.id.suitcase_container);
					LinearLayout addTrip = (LinearLayout) findViewById(R.id.add_suitcase);
					tripContainer.removeAllViews();
					tripContainer.addView(addTrip);
					View ruler = new View(SuitcaseActivity.this);
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
			AlertDialog.Builder builder = new AlertDialog.Builder(SuitcaseActivity.this);
			builder.setMessage("Are you sure you want to delete?").setCancelable(false)
					.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
						String deleteFromDB = "delete from Suitcase where suitcase_name = '" + i + "' and trip_id='"+ tripId +"'";
						db.execSQL(deleteFromDB);
						
						LinearLayout tripContainer = (LinearLayout) findViewById(R.id.suitcase_container);
						LinearLayout addTrip = (LinearLayout) findViewById(R.id.add_suitcase);
						tripContainer.removeAllViews();
						tripContainer.addView(addTrip);
						View ruler = new View(SuitcaseActivity.this);
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
		
		public void isDupeTrue(){
			AlertDialog dupe = new AlertDialog.Builder(
					SuitcaseActivity.this).create();
			dupe.setTitle("Duplicate Found");
			dupe.setMessage("Suitcase name already exists");
			dupe.setButton("Ok",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int which) {
						}
					});
			dupe.show();
		}

	public void addSuitcase(View view) {
		if (limit == 0)//checking to make sure there is no open layouts 
		{	AdView bottomAd=(AdView)findViewById(R.id.adView);
			bottomAd.setVisibility(View.INVISIBLE);
			limit = 1;
			LayoutParams layoutParams = new LayoutParams(MATCH_PARENT, WRAP_CONTENT);
			final LinearLayout newTab = new LinearLayout(this);
			newTab.setOrientation(LinearLayout.VERTICAL);
			final EditText textBox = new EditText(this);
			textBox.setHint("Enter suitcase name");
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

			LinearLayout tripContainer = (LinearLayout) findViewById(R.id.suitcase_container);
			adView.loadAd(new AdRequest());
			tripContainer.addView(adView, 0);
			tripContainer.addView(newTab, 1, layoutParams);

			//Code below is for adding a trip to the database
			okButton.setOnClickListener(new View.OnClickListener() {
				@SuppressWarnings("deprecation")
				public void onClick(View v) {

					LinearLayout buttonParent = (LinearLayout) okButton.getParent().getParent();
					EditText textBox = (EditText) buttonParent.getChildAt(0);//gets value of textbox 
					String suitcaseName = textBox.getText().toString();

					if (suitcaseName.equals("")) //if they try to add a null trip to database
					{
						AlertDialog dupe = new AlertDialog.Builder(SuitcaseActivity.this).create();
						dupe.setMessage("You cannot enter a blank suitcase name. Please enter a suitcase name");
						dupe.setButton("Ok", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
							}
						});

						dupe.show();

					}

					//code below checks for duplicates in database
					else {

						Cursor c = db.rawQuery("SELECT * from Suitcase where trip_id='" + tripId
								+ "'", null);
						c.moveToFirst();

						boolean isDupe = false;
						while (c.isAfterLast() == false) {
							String text = c.getString(c.getColumnIndex("suitcase_name"));
							c.moveToNext();
							if (text.equals(suitcaseName)) {
								isDupe = true;
								break;
							}
						}//end while*/

						c.close();

						if (isDupe == false) {
							String INSERT_STATEMENT = "INSERT INTO Suitcase (suitcase_name, trip_id) Values ('"
									+ suitcaseName + "', '" + tripId + "')";
							db.execSQL(INSERT_STATEMENT); // insert into suitcase_table db
							limit = 0; // allow to recreate a new trip

							LinearLayout tripContainer = (LinearLayout) findViewById(R.id.suitcase_container);
							LinearLayout addTrip = (LinearLayout) findViewById(R.id.add_suitcase);
							tripContainer.removeAllViews();
							tripContainer.addView(addTrip);
							View ruler = new View(SuitcaseActivity.this);
							ruler.setBackgroundColor(Color.BLACK); // this code draws the black lines
							tripContainer.addView(ruler, new ViewGroup.LayoutParams(
									ViewGroup.LayoutParams.MATCH_PARENT, 2));
							createLayoutsFromDB();
							AdView bottomAd=(AdView)findViewById(R.id.adView);
							bottomAd.setVisibility(View.VISIBLE);
						}

						else {

							AlertDialog dupe = new AlertDialog.Builder(SuitcaseActivity.this)
									.create();
							dupe.setTitle("Duplicate Found");
							dupe.setMessage("Suitcase name already exists. Please use that suitcase instead");
							dupe.setButton("Ok", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int which) {
								}
							});

							dupe.show();

						}
					}
				}
			});

			cancelButton.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					limit = 0;
					LinearLayout tripContainer = (LinearLayout) findViewById(R.id.suitcase_container);
					LinearLayout addTrip = (LinearLayout) findViewById(R.id.add_suitcase);
					tripContainer.removeAllViews();
					tripContainer.addView(addTrip);
					View ruler = new View(SuitcaseActivity.this);
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
