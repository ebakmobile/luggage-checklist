package com.lugcheck;

import java.util.Locale;

import android.os.Bundle;
import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.support.v4.app.NavUtils;

public class ItemActivity extends Activity {

	SQLiteDatabase db; 
	int suitcase_id;
	int limit;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);
        db=openOrCreateDatabase("data.db", SQLiteDatabase.CREATE_IF_NECESSARY, null);
		db.setVersion(1);
		db.setLocale(Locale.getDefault());
		limit=0;
		
    	Bundle extras = getIntent().getExtras();
		suitcase_id = extras.getInt("suitcase_id"); // receiving trip_id from previous activity 
		String bah= "In Item Class, suitcase_id is " + suitcase_id;
		Log.w("SDFDS", bah);
		
		/*code below is to set the activity title to the trip_name*/
		String GET_TRIP_NAME= "select * from suitcase_table where suitcase_id = '" + suitcase_id + "'";
		Cursor c = db.rawQuery(GET_TRIP_NAME, null);
		c.moveToFirst();		
		String suitcase_name= c.getString(c.getColumnIndex("suitcase_name")); 
		setTitle("Displaying items for "+ suitcase_name );
        
		LinearLayout tripContainer = (LinearLayout) findViewById(R.id.item_container);
		View ruler = new View(this); ruler.setBackgroundColor(Color.BLACK); // this code draws the black lines
		tripContainer.addView(ruler,
		new ViewGroup.LayoutParams( ViewGroup.LayoutParams.FILL_PARENT, 2));
		createLayoutsFromDB();
		
        
    }//end onCreate


    public void createLayoutsFromDB()
	{

    
    
	}    

} //end activity
