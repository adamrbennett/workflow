package com.lwolf.wf.persistence;

import com.lwolf.scud.locators.DataSourceLocator;
import com.lwolf.scud.locators.IdLocator;
import com.lwolf.wf.persistence.jdbc.JdbcDataFactory;

public abstract class DataFactory {
	
	public enum FactoryType {
		JDBC
	}
	
	public static DataFactory getDataFactory(FactoryType type, DataSourceLocator dataSourceLocator, IdLocator idLocator) {
		switch (type) {
		case JDBC:
			return new JdbcDataFactory(dataSourceLocator, idLocator);
			default:
				return new JdbcDataFactory(dataSourceLocator, idLocator);
		}
	}
	
	public abstract ProcessDao processDao();
	public abstract ModelDao modelDao();
	public abstract TaskDao taskDao();
	public abstract InputDao inputDao();
	public abstract InputFileDao inputFileDao();
	public abstract KeyDao keyDao();
	public abstract UserDao userDao();

}
