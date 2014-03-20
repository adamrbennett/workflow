package com.lwolf.wf.repository;

import com.lwolf.wf.dto.InputDto;
import com.lwolf.wf.dto.InputFileDto;
import com.lwolf.wf.dto.InputMapDto;
import com.lwolf.wf.dto.TaskListDto;
import com.lwolf.wf.exceptions.RepositoryException;

public interface InputRepository {
	
	public void get(InputDto input) throws RepositoryException;
	public void add(InputDto input) throws RepositoryException;
	public void update(InputDto input) throws RepositoryException;
	public void remove(InputDto input) throws RepositoryException;

	public void get(InputMapDto map) throws RepositoryException;
	public void add(InputMapDto map) throws RepositoryException;
	public void update(InputMapDto map) throws RepositoryException;
	
	public void get(TaskListDto list) throws RepositoryException;
	
	public void get(InputFileDto file) throws RepositoryException;

}
