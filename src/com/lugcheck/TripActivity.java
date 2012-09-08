package com.lugcheck;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.app.NavUtils;

public class TripActivity extends Activity {
    int trip_id;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suitcase);
      
        
        Bundle extras = getIntent().getExtras();
        trip_id = extras.getInt("trip_id"); // receiving trip_id from previous intent
          	
      String bah= "In Suitcase Class, Trip_id is " + trip_id;
        Log.w("SDFDS", bah);
               
        
        
    }

 
    
 
    
    
    

}
