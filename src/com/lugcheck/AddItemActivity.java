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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;

public class AddItemActivity extends Activity {
	private int suitcaseId;
	private SQLiteDatabase db;
	private ArrayList<String> insertList; //inserts this list into QuickAdd table
	private float density;
	private AdView adView;
	
	HashMap<String, String> wantedList;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		wantedList=new HashMap<String, String>();
		density = this.getResources().getDisplayMetrics().density;
		setContentView(R.layout.activity_add_item);
		db = openOrCreateDatabase("data.db", SQLiteDatabase.CREATE_IF_NECESSARY, null);
		db.setVersion(1);
		db.setLocale(Locale.getDefault());
		insertList = new ArrayList<String>();
		Bundle extras = getIntent().getExtras();
		suitcaseId = extras.getInt("suitcase_id");
		setTitle("Suggested Items");
		Cursor c = db.rawQuery("SELECT * from QuickAdd", null);
		if (c.getCount() <= 0) {// if there is nothing in the QuickAdd Table

			addIntoArrayList(); // add all the items into InserList. Then we shove it into the DB

			for (int i = 0; i < insertList.size(); i++) {
				String tempName = insertList.get(i);
				String INSERT_STATEMENT = "INSERT INTO QuickAdd (name) Values ('" + tempName + "')";
				db.execSQL(INSERT_STATEMENT); // insert into trip_table db
			}

		}
		String myAdmobPublisherID="a1508d762ede868";
		adView = new AdView(this, AdSize.SMART_BANNER, myAdmobPublisherID);  
		LinearLayout layout = (LinearLayout)findViewById(R.id.add_item_container);
		adView.loadAd(new AdRequest());
		layout.addView(adView,0);
		createLayoutsFromDB();
		c.close();

	}

	public void createLayoutsFromDB() {

		/* Code Below fetches trips from trip_table and creates a layout*/
		Cursor c = db.rawQuery("SELECT * from QuickAdd ORDER BY name", null);
		c.moveToFirst();
		while (c.isAfterLast() == false) {

			TextView hw = new TextView(this);
			final String text = c.getString(c.getColumnIndex("name"));
			hw.setText(text);
			hw.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
			final CheckBox checkBox=new CheckBox(this);
			final EditText quantity=new EditText(this);
			quantity.setHint("Enter Quantity");
			quantity.setVisibility(View.INVISIBLE);
			int width = (int) (58 * density);
			int height = (int) (50 * density);
			checkBox.setLayoutParams(new LayoutParams(width, height));//set dimensions of checkbox
			
			//LayoutParams lp = new LayoutParams(new ViewGroup.MarginLayoutParams(100,100));
			
			//quantity.setLayoutParams(lp);
			
			int txtPadding = (int) (20 * density);
			hw.setPadding(0, txtPadding, 0, 0);
			
			
			RelativeLayout relativeLayoutAdd = new RelativeLayout(this); // put the quantity edittext on this relative layout to push to the right
			RelativeLayout.LayoutParams paramRight = new RelativeLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			paramRight.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
			//paramRight.addRule(RelativeLayout.ALIGN_BOTTOM, RelativeLayout.TRUE);
			relativeLayoutAdd.addView(quantity, paramRight);
			
			LinearLayout newTab = new LinearLayout(this);
			newTab.setOrientation(LinearLayout.HORIZONTAL);
			newTab.addView(checkBox);
			newTab.addView(hw);	
			newTab.addView(relativeLayoutAdd);
			newTab.setBackgroundColor(Color.WHITE);

			LinearLayout tripContainer = (LinearLayout) findViewById(R.id.add_item_container);
			tripContainer.addView(newTab);

			View ruler = new View(this);
			ruler.setBackgroundColor(Color.BLACK); // this code draws the black lines
			tripContainer.addView(ruler, new ViewGroup.LayoutParams(
					ViewGroup.LayoutParams.MATCH_PARENT, 2));
			c.moveToNext();

			checkBox.setOnClickListener(new View.OnClickListener()
			{
			public void onClick(View v2)
			    {
				checkboxFunctions(checkBox,quantity);
			    }
			});
			
			newTab.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
				checkboxFunctions(checkBox,quantity);
				
				}
			});
		}
		c.close();
	}
	
	public void checkboxFunctions(CheckBox checkBox, EditText quantity){
		
		if(checkBox.isChecked())
		{
			checkBox.setChecked(false);
			quantity.setVisibility(View.INVISIBLE);
	
		}
			
			
			
			else {
			checkBox.setChecked(true);
			quantity.setVisibility(View.VISIBLE);
			}
			
	}
	
	
	public void addSuggestedItem(View view)
	{
		
		/*final EditText quantityEditText = new EditText(AddItemActivity.this);
		quantityEditText.setHint("Quantity");

		AlertDialog.Builder builder = new AlertDialog.Builder(AddItemActivity.this);
		builder.setMessage("Please enter a quantity for " + text).setCancelable(false)
				.setView(quantityEditText)
				.setPositiveButton("Add", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {// when they click "add" after entering quantity
						String quantity = quantityEditText.getText().toString();
						boolean isDupe = false;//code below checks for dupes in database
						Cursor c = db.rawQuery("SELECT * from Item where suitcase_id='"
								+ suitcaseId + "'", null);
						c.moveToFirst();

						while (c.isAfterLast() == false) {
							String itemName = c.getString(c.getColumnIndex("item_name"));
							c.moveToNext();
							if (text.equals(itemName)) {
								isDupe = true;
								break;
							}
						}

						c.close();

						if (!quantity.matches("\\d+")) {
							AlertDialog dupe = new AlertDialog.Builder(
									AddItemActivity.this).create();
							dupe.setMessage("Please enter a numeric value for 'Quantity'");
							dupe.setButton("Ok", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int which) {
								}
							});

							dupe.show();

						}

						else {
							if (isDupe == true) {//if there is a duplicate
								AlertDialog dupe = new AlertDialog.Builder(
										AddItemActivity.this).create();
								dupe.setTitle("Duplicate Found");
								dupe.setMessage("Item already exists. Please use that item instead");
								dupe.setButton("Ok",
										new DialogInterface.OnClickListener() {
											public void onClick(DialogInterface dialog,
													int which) {
											}
										});
								dupe.show();

							} else {
								
								String INSERT_STATEMENT = new StringBuilder(
										"INSERT INTO Item (item_name, quantity, suitcase_id, is_slashed) Values ('")
										.append(text).append("', '").append(quantity)
										.append("','").append(suitcaseId)
										.append("','0')").toString();
								db.execSQL(INSERT_STATEMENT);
								Intent resultIntent = new Intent();
								resultIntent.putExtra("suitcase_id", suitcaseId);
								setResult(RESULT_OK, resultIntent);
								finish();
							}
						}
					}
				}).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});

		AlertDialog alert = builder.create();
		alert.show();
*/
		
	}
	
	
	
	
	

	public void addIntoArrayList() {
		insertList.add("Shoes");
		insertList.add("Underwear");
		insertList.add("Jacket");
		insertList.add("Sweater");
		insertList.add("Pajama");
		insertList.add("Swimsuit");
		insertList.add("T-Shirt");
		insertList.add("Socks");
		insertList.add("Dresses");
		insertList.add("Blouse");
		insertList.add("Umbrella");
		insertList.add("Scarf");
		insertList.add("Gloves");
		insertList.add("Hat");
		insertList.add("Cap");
		insertList.add("Pants");
		insertList.add("Boots");
		insertList.add("Sandals");
		insertList.add("Slippers");
		insertList.add("Belt");
		insertList.add("Toilet Paper");
		insertList.add("Sunscreen");
		insertList.add("Lip Balm");
		insertList.add("First Aid Kit");
		insertList.add("Sunglasses");
		insertList.add("Maps");
		insertList.add("Computer");
		insertList.add("Tablet");
		insertList.add("Phone");
		insertList.add("Toothbrush");
		insertList.add("Toothpaste");
		insertList.add("Comb");
		insertList.add("Shampoo");
		insertList.add("Nail Clippers");
		insertList.add("Towel");
		insertList.add("Camera");
		insertList.add("Passport");
		insertList.add("Travel Ticket");
		insertList.add("Fanny Pack");
	}

	public boolean isInteger(String s) {
		boolean result = false;
		try {
			Integer.parseInt("-1234");
			result = true;
		} catch (NumberFormatException nfe) {
			// no need to handle the exception
		}
		return result;
	}
}
