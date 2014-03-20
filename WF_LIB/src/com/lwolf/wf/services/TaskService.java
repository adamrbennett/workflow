package com.lwolf.wf.services;

import com.lwolf.wf.dto.TaskDto;
import com.lwolf.wf.dto.TaskListDto;
import com.lwolf.wf.exceptions.ServiceException;

public interface TaskService {
	
	public void complete(TaskDto task) throws ServiceException;
	
	public void get(TaskDto task) throws ServiceException;
	public void get(TaskListDto list) throws ServiceException;
	public void add(TaskDto task) throws ServiceException;
	public void remove(TaskDto task) throws ServiceException;
	public void update(TaskDto task) throws ServiceException;

}
