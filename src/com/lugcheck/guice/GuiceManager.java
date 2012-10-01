package com.lugcheck.guice;

import com.google.inject.Guice;
import com.google.inject.Injector;

/**
 * Manager the one and only global Guice injector for the entire application.
 * Static methods only.
 * @author ecbrodie
 */
public class GuiceManager {
	
	private static Injector injector = null;
	
	static {
		injector = Guice.createInjector(new PropertiesModule(), new DbModule());
	}
	
	/**
	 * Private constructor to disable instantiation of a GuiceManager object.
	 */
	private GuiceManager() {}
	
	/**
	 * Gets the injector.
	 *
	 * @return the injector
	 */
	public static Injector getInjector() {
		return injector;
	}
	
}
