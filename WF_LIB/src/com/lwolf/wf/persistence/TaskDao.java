package com.lwolf.wf.persistence;

import java.util.List;

import com.lwolf.scud.exceptions.DataException;
import com.lwolf.wf.persistence.entities.TaskEntity;

public interface TaskDao {
	public Integer create(TaskEntity entity) throws DataException;
	public void read(TaskEntity entity) throws DataException;
	public void update(TaskEntity entity) throws DataException;
	public void delete(TaskEntity entity) throws DataException;
	public List<TaskEntity> findAll() throws DataException;
	public List<TaskEntity> findByProcess(TaskEntity entity) throws DataException;
	public void findByProcessScript(TaskEntity entity) throws DataException;
}
