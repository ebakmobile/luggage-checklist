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
package com.lugcheck.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.OptionsMenu;
import com.lugcheck.R;

@EActivity(R.layout.activity_trip_screen)
@OptionsMenu(R.menu.trip_screen)
public class TripScreenActivity extends BaseScreenActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);

		LinearLayout linearLayout = new LinearLayout(getApplicationContext());
		linearLayout.setOrientation(LinearLayout.HORIZONTAL);
		TextView textView = new TextView(getApplicationContext());
		textView.setText("TripScreenActivity");
		textView.setTextColor(Color.BLACK);
		textView.setLayoutParams(lp);
		linearLayout.addView(textView);
		addContentView(linearLayout, lp);
	}

}
