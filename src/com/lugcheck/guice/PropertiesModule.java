package com.lugcheck.guice;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import android.util.Log;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import com.lugcheck.db.DAO;
import com.lugcheck.db.DAOImpl;

public class PropertiesModule extends AbstractModule {

	@Override
	protected void configure() {
		Properties dbProp = new Properties();
		InputStream fis = null;
		try {
			fis = new FileInputStream("database/db.properties");
			dbProp.load(fis);
		} catch (FileNotFoundException e) {
			Log.e("Guice problem", "Unable to find db.properties.");
			e.printStackTrace();
		} catch (IOException e) {
			Log.e("Guice problem", "Problem reading from db.properties.");
			e.printStackTrace();
		} finally {
			try {
				if (fis != null) fis.close();
			} catch (IOException e) {
				Log.e("Guice problem", "Problem closing the stream to db.properties.");
			}
		}
		Names.bindProperties(binder(), dbProp);
		
		bind(DAO.class).to(DAOImpl.class);
	}

}