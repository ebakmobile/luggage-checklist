package com.lugcheck.db;

import com.lugcheck.models.LugCheckObject;

public interface DAO {
	<T extends LugCheckObject> void insertObject(T obj);
}
