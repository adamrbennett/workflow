package com.lwolf.wf.repository;

import com.lwolf.wf.dto.TaskDto;
import com.lwolf.wf.dto.TaskListDto;
import com.lwolf.wf.exceptions.RepositoryException;

public interface TaskRepository {
	
	public void get(TaskDto task) throws RepositoryException;
	public void get(TaskListDto list) throws RepositoryException;
	public void add(TaskDto task) throws RepositoryException;
	public void update(TaskDto task) throws RepositoryException;
	public void remove(TaskDto task) throws RepositoryException;

}
