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
	public Integer create(ModelEntity entity) throws DataException {
		return super.create(entity);
	}

	@Override
	public void delete(ModelEntity entity) throws DataException {
		super.delete(entity);
	}

	@Override
	public void read(ModelEntity entity) throws DataException {
		super.read(entity);
	}

	@Override
	public void update(ModelEntity entity) throws DataException {
		super.update(entity);
	}

	@Override
	public List<ModelEntity> findAll() throws DataException {
		return super.findAll(ModelEntity.class);
	}
}
