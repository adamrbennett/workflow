package com.lwolf.wf.persistence;

import java.util.List;

import com.lwolf.scud.exceptions.DataException;
import com.lwolf.wf.persistence.entities.ProcessEntity;

public interface ProcessDao {
	public Integer create(ProcessEntity entity) throws DataException;
	public void read(ProcessEntity entity) throws DataException;
	public void update(ProcessEntity entity) throws DataException;
	public void delete(ProcessEntity entity) throws DataException;
	public List<ProcessEntity> findAll() throws DataException;
	public List<ProcessEntity> findByModel(ProcessEntity entity) throws DataException;
}
