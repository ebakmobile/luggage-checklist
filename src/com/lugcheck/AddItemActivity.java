package com.lugcheck;

import java.util.ArrayList;
import java.util.Locale;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AddItemActivity extends Activity {
	int limit;
	int suitcaseId;
	SQLiteDatabase db;
	ArrayList<String> insertList; //inserts this list into QuickAdd table

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_item);
		db = openOrCreateDatabase("data.db", SQLiteDatabase.CREATE_IF_NECESSARY, null);
		db.setVersion(1);
		db.setLocale(Locale.getDefault());
		insertList = new ArrayList<String>();
		Bundle extras = getIntent().getExtras();
		suitcaseId = extras.getInt("suitcase_id");
		Log.w("Suitcase id is ", " " + suitcaseId);
		limit = 0;

		Cursor c = db.rawQuery("SELECT * from QuickAdd", null);
		if (c.getCount() <= 0) {// if there is nothing in the QuickAdd Table

			addIntoArrayList(); // add all the items into InserList. Then we shove it into the DB

			for (int i = 0; i < insertList.size(); i++) {
				String tempName = insertList.get(i);
				Log.w("tempName is  ", tempName);
				String INSERT_STATEMENT = "INSERT INTO QuickAdd (name) Values ('" + tempName + "')";
				db.execSQL(INSERT_STATEMENT); // insert into trip_table db
			}

		}
		createLayoutsFromDB();
		c.close();

	}

	public void createLayoutsFromDB() {

		/* Code Below fetches trips from trip_table and creates a layout*/
		Cursor c = db.rawQuery("SELECT * from QuickAdd", null);
		c.moveToFirst();
		while (c.isAfterLast() == false) {

			TextView hw = new TextView(this);
			String text = c.getString(c.getColumnIndex("name"));
			hw.setText(text);
			hw.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
			ImageView im = new ImageView(this);
			im.setImageResource(R.drawable.opensuitcase);
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
			LinearLayout tripContainer = (LinearLayout) findViewById(R.id.trips_container);
			tripContainer.addView(newTab);

			View ruler = new View(this);
			ruler.setBackgroundColor(Color.BLACK); // this code draws the black lines
			tripContainer.addView(ruler, new ViewGroup.LayoutParams(
					ViewGroup.LayoutParams.MATCH_PARENT, 2));

			c.moveToNext();

		}
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
		insertList.add("Flip Flops / Sandals");
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

}
