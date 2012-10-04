package com.lugcheck.util;

import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.content.res.AssetManager;

public class AppUtil {
	
	private AppUtil() {}
	
	public static InputStream getInputStreamFromAssets(AssetManager assets, String file) throws IOException {
		return assets.open(file);
	}
	
	public static InputStream getInputStreamFromAssets(Context context, String file) throws IOException {
		return context.getAssets().open(file);
	}
	
}
