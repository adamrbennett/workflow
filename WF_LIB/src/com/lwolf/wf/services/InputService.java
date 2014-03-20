package com.lwolf.wf.services;

import com.lwolf.wf.dto.InputFileDto;
import com.lwolf.wf.exceptions.ServiceException;

public interface InputService {
	
	public void get(InputFileDto file) throws ServiceException;

}
