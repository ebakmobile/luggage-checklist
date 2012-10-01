package com.lugcheck.guice;

import com.google.inject.AbstractModule;
import com.lugcheck.db.DAO;
import com.lugcheck.db.DAOImpl;

public class DbModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(DAO.class).to(DAOImpl.class);
	}
}
