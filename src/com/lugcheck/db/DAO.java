package com.lugcheck.db;

import com.lugcheck.models.LugCheckObject;

import android.content.Context;

public interface DAO {
	
	public void setup(Context context) throws DBException;
	
	public void insertObject(LugCheckObject obj);
	
}
