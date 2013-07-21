package com.lugcheck;

import roboguice.RoboGuice;
import android.app.Application;
import com.lugcheck.guice.DbModule;
import com.lugcheck.guice.PropertiesModule;

/**
 * Application subclass is required to setup RoboGuice.
 * This class is used by setting the android:name attribute of the 'application' tag in Manifest.
 */
public class LugCheckApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();

		RoboGuice.setBaseApplicationInjector(this, RoboGuice.DEFAULT_STAGE,
				RoboGuice.newDefaultRoboModule(this), new DbModule(), new PropertiesModule(getApplicationContext()));
	}
}
