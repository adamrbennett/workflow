package com.lwolf.wf.repository;

import java.util.List;

import com.lwolf.wf.dto.ModelDto;
import com.lwolf.wf.exceptions.RepositoryException;

public interface ModelRepository {
	
	public void get(ModelDto model) throws RepositoryException;
	public void add(ModelDto model) throws RepositoryException;
	public void update(ModelDto model) throws RepositoryException;
	public void remove(ModelDto model) throws RepositoryException;

	public List<ModelDto> list() throws RepositoryException;

}
