package com.lugcheck.guice;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import com.lugcheck.db.DAO;
import com.lugcheck.db.DAOImpl;
import com.lugcheck.util.AppUtil;

public class PropertiesModule extends AbstractModule {
	
	private Context context;
	
	public PropertiesModule(Context context) {
		super();
		this.context = context;
	}
	
	@Override
	protected void configure() {
		Properties dbProp = new Properties();
		InputStream fis = null;
		AssetManager assets = context.getAssets();
		try {
			fis = AppUtil.getInputStreamFromAssets(assets, "database/db.properties");
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
		Log.i("Prop test", "Value of db.version=" + dbProp.getProperty("db.version"));
		Names.bindProperties(binder(), dbProp);
		
		bind(DAO.class).to(DAOImpl.class);
	}

}