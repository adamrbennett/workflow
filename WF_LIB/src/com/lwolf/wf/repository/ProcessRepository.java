package com.lwolf.wf.repository;

import java.util.List;

import com.lwolf.wf.dto.ProcessDto;
import com.lwolf.wf.dto.ProcessListDto;
import com.lwolf.wf.exceptions.RepositoryException;

public interface ProcessRepository {
	
	public void get(ProcessDto process) throws RepositoryException;
	public void get(ProcessListDto list) throws RepositoryException;
	public void add(ProcessDto process) throws RepositoryException;
	public void update(ProcessDto process) throws RepositoryException;
	public void remove(ProcessDto process) throws RepositoryException;
	
	public List<ProcessDto> list() throws RepositoryException;

}
