package com.lwolf.wf.services.impl;

import java.util.Locale;

import com.lwolf.scud.locators.TransactionLocator;
import com.lwolf.wf.dto.InputDto;
import com.lwolf.wf.dto.InputFileDto;
import com.lwolf.wf.exceptions.RepositoryException;
import com.lwolf.wf.exceptions.ServiceException;
import com.lwolf.wf.repository.InputRepository;
import com.lwolf.wf.services.InputService;
import com.lwolf.wf.services.ServiceObject;

public class InputServiceImpl extends ServiceObject<InputDto> implements InputService {

	private final InputRepository _inputRepository;
	
	public InputServiceImpl(InputRepository inputRepository, TransactionLocator transactionLocator) {
		super(Locale.getDefault(), transactionLocator);
		_inputRepository = inputRepository;
	}
	
	@Override
	public void get(InputFileDto file) throws ServiceException {
		try {
			_inputRepository.get(file);
		} catch (RepositoryException ex) {
			throw new ServiceException(ex);
		}
	}

	@Override
	protected void doAdd(InputDto dto) throws ServiceException {
	}

	@Override
	protected void doUpdate(InputDto dto) throws ServiceException {
	}

}
