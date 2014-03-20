package com.lwolf.wf.persistence;

import java.util.Map;

import com.lwolf.scud.exceptions.DataException;
import com.lwolf.wf.persistence.entities.TaskEntity;
import com.lwolf.wf.persistence.entities.InputEntity;

public interface InputDao {
	public Integer create(InputEntity entity) throws DataException;
	public void read(InputEntity entity) throws DataException;
	public void update(InputEntity entity) throws DataException;
	public void delete(InputEntity entity) throws DataException;
	public Map<String, InputEntity> findByTask(TaskEntity task) throws DataException;
}
