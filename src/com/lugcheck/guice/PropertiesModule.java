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