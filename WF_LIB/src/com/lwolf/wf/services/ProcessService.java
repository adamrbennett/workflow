package com.lwolf.wf.services;

import java.util.List;

import com.lwolf.wf.dto.ProcessDto;
import com.lwolf.wf.exceptions.ServiceException;

public interface ProcessService {

	public void get(ProcessDto process) throws ServiceException;
	public void add(ProcessDto process) throws ServiceException;
	public void remove(ProcessDto process) throws ServiceException;
	public void update(ProcessDto process) throws ServiceException;
	
	public List<ProcessDto> list() throws ServiceException;

}
