package com.lugcheck;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.app.NavUtils;

public class ItemActivity extends Activity {

	
	int suitcase_id;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        
    	Bundle extras = getIntent().getExtras();
		suitcase_id = extras.getInt("suitcase_id"); // receiving trip_id from previous activity 
		String bah= "In Item Class, suitcase_id is " + suitcase_id;
		Log.w("SDFDS", bah);
        
        
        
        
        
    }//end onCreate


    

} //end activity
