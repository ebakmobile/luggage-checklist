package com.lugcheck.guice;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.lugcheck.db.DAO;
import com.lugcheck.db.DAOImpl;

public class DbModule implements Module {

	@Override
	public void configure(Binder binder) {
		binder.bind(DAO.class).to(DAOImpl.class);
	}

}
