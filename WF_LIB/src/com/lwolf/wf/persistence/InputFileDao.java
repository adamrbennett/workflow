package com.lwolf.wf.persistence;

import java.util.List;

import com.lwolf.scud.exceptions.DataException;
import com.lwolf.wf.persistence.entities.InputEntity;
import com.lwolf.wf.persistence.entities.InputFileEntity;

public interface InputFileDao {
	public Integer create(InputFileEntity entity) throws DataException;
	public void read(InputFileEntity entity) throws DataException;
	public void update(InputFileEntity entity) throws DataException;
	public void delete(InputFileEntity entity) throws DataException;
	public List<InputFileEntity> findByInput(InputEntity input) throws DataException;
}
