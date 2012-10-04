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

public class SuitcaseActivity extends Activity {
	int tripId;
	SQLiteDatabase db;
	static int limit; //limit to only creating one trip at a time
	public static int SUITCASE_ID = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_suitcase);

		db = openOrCreateDatabase("data.db", SQLiteDatabase.CREATE_IF_NECESSARY, null);
		db.setVersion(1);
		db.setLocale(Locale.getDefault());
		limit = 0;

		Bundle extras = getIntent().getExtras();
		tripId = extras.getInt("trip_id"); // receiving trip_id from previous activity 
		String bah = "In Suitcase Class, Trip_id is " + tripId;
		Log.w("SDFDS", bah);

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
			float d = this.getResources().getDisplayMetrics().density;
			int width = (int) (58 * d);
			int height = (int) (50 * d);
			im.setLayoutParams(new LayoutParams(width, height));
			int pad = (int) (5 * d);
			im.setPadding(pad, pad, 0, 0);
			// END
			int txtPadding = (int) (20 * d);
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

			/*Code Below handles the delete situation*/
			final String text2 = text;
			newTab.setOnLongClickListener(new OnLongClickListener() { //code to delete a list
				public boolean onLongClick(View v) {

					AlertDialog.Builder builder = new AlertDialog.Builder(SuitcaseActivity.this);
					builder.setMessage("Are you sure you want delete?").setCancelable(false)
							.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int id) {
									deleteFromDB(text2, tripId);
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

			/* Code below handles the situation where u click a item */
			final int suitcase_id2 = SUITCASE_ID;
			newTab.setOnClickListener(new Button.OnClickListener() {
				public void onClick(View v) {

					Intent intent = new Intent(SuitcaseActivity.this, ItemActivity.class);
					intent.putExtra("suitcase_id", suitcase_id2);
					startActivity(intent);

				}
			});

		}//end while*/

		c.close();

	}//end method

	public void deleteFromDB(String i, int trip_id) {

		String deleteFromDB = "delete from Suitcase where suitcase_name = '" + i
				+ "' and trip_id = '" + trip_id + "'";
		db.execSQL(deleteFromDB);

		LinearLayout tripContainer = (LinearLayout) findViewById(R.id.suitcase_container);
		LinearLayout addTrip = (LinearLayout) findViewById(R.id.add_suitcase);
		tripContainer.removeAllViews();
		tripContainer.addView(addTrip);
		View ruler = new View(this);
		ruler.setBackgroundColor(Color.BLACK); // this code draws the black lines
		tripContainer.addView(ruler, new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT, 2));
		createLayoutsFromDB();

	}

	public void addSuitcase(View view) {

		if (limit == 0)//checking to make sure there is no open layouts 
		{
			limit = 1;
			LayoutParams lp = new LayoutParams(MATCH_PARENT, WRAP_CONTENT);
			final LinearLayout newTab = new LinearLayout(this);
			newTab.setOrientation(LinearLayout.VERTICAL);
			final EditText hw = new EditText(this);
			hw.setHint("Enter suitcase name");
			final Button okButton = new Button(this);
			okButton.setText("Add");
			final Button cancelButton = new Button(this);
			cancelButton.setText("Cancel");
			LinearLayout ll = new LinearLayout(this);
			ll.setOrientation(LinearLayout.HORIZONTAL);
			ll.addView(hw);
			ll.addView(okButton);
			ll.addView(cancelButton);

			newTab.addView(ll);

			View ruler = new View(this);
			ruler.setBackgroundColor(Color.BLACK); // this code draws the black lines
			newTab.addView(ruler,
					new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 2));

			LinearLayout tripContainer = (LinearLayout) findViewById(R.id.suitcase_container);
			tripContainer.addView(newTab, 0, lp);

			//Code below is for adding a trip to the database
			okButton.setOnClickListener(new View.OnClickListener() {
				@SuppressWarnings("deprecation")
				public void onClick(View v) {

					LinearLayout buttonParent = (LinearLayout) okButton.getParent();
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

						}//end else

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

				}
			});

		}

	}

} //end activity
