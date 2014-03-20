package com.lwolf.wf.persistence.jdbc;

import com.lwolf.scud.locators.DataSourceLocator;
import com.lwolf.scud.locators.IdLocator;
import com.lwolf.scud.persistence.jdbc.JdbcDataAccessObject;
import com.lwolf.wf.persistence.UserDao;
import com.lwolf.wf.persistence.entities.UserEntity;

public class JdbcUserDao extends JdbcDataAccessObject<UserEntity> implements UserDao {
	
	public JdbcUserDao(DataSourceLocator dataSourceLocator, IdLocator idLocator) {
		super(dataSourceLocator, idLocator);
	}

}
