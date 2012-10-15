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
package com.lugcheck.guice;

import android.content.Context;
import android.util.Log;

import com.google.inject.Guice;
import com.google.inject.Injector;

/**
 * Manager the one and only global Guice injector for the entire application. Singleton.
 * 
 * @author ecbrodie
 */
public class GuiceManager {

	private Injector injector = null;

	private Context applicationContext = null;

	private static GuiceManager thisInstance = null;

	/**
	 * Private constructor to disable instantiation of a GuiceManager object.
	 */
	private GuiceManager(Context context) {
		applicationContext = context;
		injector = Guice.createInjector(new PropertiesModule(applicationContext), new DbModule());
	}

	public static GuiceManager getInitialInstance(Context context) {
		if (thisInstance == null) {
			thisInstance = new GuiceManager(context);
			return thisInstance;
		}
		Log.w("GuiceManager",
				"In order to get initial instance of GuiceManager, the application's context must be provided.");
		return null;
	}

	public static GuiceManager getInstance() {
		// This should only be called after getInitialInstance has been called also.
		return thisInstance;
	}

	/**
	 * Gets the injector.
	 * 
	 * @return the injector
	 */
	public Injector getInjector() {
		return injector;
	}

}
