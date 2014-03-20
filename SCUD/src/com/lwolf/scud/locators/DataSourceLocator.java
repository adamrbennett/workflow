package com.lwolf.scud.locators;

import javax.sql.DataSource;

import com.lwolf.scud.exceptions.ResourceException;

public interface DataSourceLocator {
	public DataSource getDataSource() throws ResourceException;
}
