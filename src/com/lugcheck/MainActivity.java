package com.lugcheck;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

public class MainActivity extends Activity {
	static int limit; //limit to only creating one trip at a time

	Map<String, LinearLayout> tabs;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		limit=0;
		tabs = new HashMap<String, LinearLayout>();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	public void addTrip(View view) {


		if (limit==0)//checking to make sure there is no open layouts 
		{ 
			limit =1;
			LayoutParams lp = new LayoutParams(MATCH_PARENT, WRAP_CONTENT);
			final LinearLayout newTab = new LinearLayout(this);
			newTab.setOrientation(LinearLayout.VERTICAL);
			EditText hw = new EditText(this);
			hw.setText("Enter Trip Name");
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
			//int count = tripContainer.getChildCount();
			tripContainer.addView(newTab, 0,lp);

			// TODO: Set id for the new trip tab to new_trip_tab. Once name is entered, change the ID.
			// TODO: Check for uniqueness

			okButton.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					limit =0; // allow to recreate a new button
					//do stuff to add
					LinearLayout buttonParent = (LinearLayout) okButton.getParent();
					EditText textBox = (EditText) buttonParent.getChildAt(0);
					String tripName = textBox.getText().toString();
					if (tabs.get(tripName) == null) {
						// TODO: Block due to duplicate name
					} else {
						tabs.put(tripName, buttonParent);
					}
					// TODO: Hide the OK button. Cancel replaced by delete button.
					// TODO: Switch to trip activity
				}
			});

			cancelButton.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					limit=0;
					LinearLayout tripContainer = (LinearLayout) findViewById(R.id.trips_container); 	
					int count=tripContainer.getChildCount();
					tripContainer.removeViewAt(0);	// TODO: Look on internet on how to find the proper count

					/*cancelButton.getParent();
					tripContainer.removeView(parent);*/
				}
			});



		}

	}




}



