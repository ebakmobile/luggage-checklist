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
import android.os.Bundle;
import com.googlecode.androidannotations.annotations.EActivity;
import com.lugcheck.activity.TripScreenActivity_;
import com.lugcheck.util.LugCheckConstants;
import com.parse.Parse;
import com.parse.ParseAnalytics;

@EActivity(R.layout.activity_main)
public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		initApp();

		// Redirect right away
		TripScreenActivity_.intent(this).start();
	}

	/**
	 * Setup script that should only be run once when the app boots up or restarts.
	 */
	private void initApp() {
		// Stupid thing that we need to do in order to prevent a NoClassDefFoundError for Parse: https://code.google.com/p/android/issues/detail?id=20915
		try {
			Class.forName("android.os.AsyncTask");
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Unable to initialize the application due to an internal error.", e);
		}

		// Parse setup
		Parse.initialize(this, LugCheckConstants.PARSE_APP_ID, LugCheckConstants.PARSE_CLIENT_KEY);
		ParseAnalytics.trackAppOpened(getIntent()); // Tracks statistics regarding "app opens". Optional.
	}

}
