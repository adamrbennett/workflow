package com.lwolf.wf.persistence.jdbc;

import java.util.List;

import com.lwolf.scud.exceptions.DataException;
import com.lwolf.scud.locators.DataSourceLocator;
import com.lwolf.scud.locators.IdLocator;
import com.lwolf.scud.persistence.jdbc.JdbcDataAccessObject;
import com.lwolf.wf.persistence.ModelDao;
import com.lwolf.wf.persistence.entities.ModelEntity;

public class JdbcModelDao extends JdbcDataAccessObject<ModelEntity> implements ModelDao {
	
	public JdbcModelDao(DataSourceLocator dataSourceLocator, IdLocator idLocator) {
		super(dataSourceLocator, idLocator);
	}

	@Override
	public List<ModelEntity> findAll() throws DataException {
		return super.findAll(ModelEntity.class);
	}
}
