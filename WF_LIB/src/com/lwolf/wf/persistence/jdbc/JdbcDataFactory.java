package com.lwolf.wf.persistence.jdbc;

import com.lwolf.scud.locators.DataSourceLocator;
import com.lwolf.scud.locators.IdLocator;
import com.lwolf.wf.persistence.DataFactory;
import com.lwolf.wf.persistence.InputFileDao;
import com.lwolf.wf.persistence.KeyDao;
import com.lwolf.wf.persistence.ProcessDao;
import com.lwolf.wf.persistence.ModelDao;
import com.lwolf.wf.persistence.TaskDao;
import com.lwolf.wf.persistence.InputDao;
import com.lwolf.wf.persistence.UserDao;

public class JdbcDataFactory extends DataFactory {
	
	private DataSourceLocator dataSourceLocator;
	private IdLocator idLocator;
	
	public JdbcDataFactory(DataSourceLocator dataSourceLocator, IdLocator idLocator) {
		this.dataSourceLocator = dataSourceLocator;
		this.idLocator = idLocator;
	}
	
	public ProcessDao processDao() {
		return new JdbcProcessDao(dataSourceLocator, idLocator);
	}
	
	public ModelDao modelDao() {
		return new JdbcModelDao(dataSourceLocator, idLocator);
	}
	
	public TaskDao taskDao() {
		return new JdbcTaskDao(dataSourceLocator, idLocator);
	}
	
	public InputDao inputDao() {
		return new JdbcInputDao(dataSourceLocator, idLocator);
	}
	
	public InputFileDao inputFileDao() {
		return new JdbcInputFileDao(dataSourceLocator, idLocator);
	}
	
	public KeyDao keyDao() {
		return new JdbcKeyDao(dataSourceLocator);
	}
	
	public UserDao userDao() {
		return new JdbcUserDao(dataSourceLocator, idLocator);
	}

}
