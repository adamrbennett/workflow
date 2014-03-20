package com.lwolf.wf.services;

import java.util.List;

import com.lwolf.wf.dto.ModelDto;
import com.lwolf.wf.exceptions.ServiceException;

public interface ModelService {

	public void get(ModelDto model) throws ServiceException;
	public void add(ModelDto model) throws ServiceException;
	public ModelDto remove(ModelDto model) throws ServiceException;
	public void update(ModelDto model) throws ServiceException;
	
	public List<ModelDto> list() throws ServiceException;

}
