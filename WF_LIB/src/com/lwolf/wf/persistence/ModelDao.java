package com.lwolf.wf.persistence;

import java.util.List;

import com.lwolf.scud.exceptions.DataException;
import com.lwolf.wf.persistence.entities.ModelEntity;

public interface ModelDao {
	public Integer create(ModelEntity entity) throws DataException;
	public void read(ModelEntity entity) throws DataException;
	public void update(ModelEntity entity) throws DataException;
	public void delete(ModelEntity entity) throws DataException;
	public List<ModelEntity> findAll() throws DataException;
}
