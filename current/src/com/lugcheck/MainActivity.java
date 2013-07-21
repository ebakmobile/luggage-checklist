/*	
		NOTICE for Luggage & Suitcase Checklist, an Android app:
	    Copyright (C) 2013 EBAK Mobile

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

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import com.lugcheck.activity.TripScreenActivity;
import com.lugcheck.util.LugCheckConstants;
import com.parse.Parse;
import com.parse.ParseAnalytics;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		initApp();

		// Redirect right away
		Intent intent = new Intent(this, TripScreenActivity.class);
		startActivity(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	/**
	 * Setup script that should only be run once when the app boots up or restarts.
	 */
	private void initApp() {
		/* Parse setup */
		Parse.initialize(this, LugCheckConstants.PARSE_APP_ID, LugCheckConstants.PARSE_CLIENT_KEY);
		ParseAnalytics.trackAppOpened(getIntent()); // Tracks statistics regarding "app opens". Optional.
	}

}
