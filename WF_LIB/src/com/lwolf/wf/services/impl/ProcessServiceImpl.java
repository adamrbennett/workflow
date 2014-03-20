package com.lwolf.wf.services.impl;

import java.util.List;
import java.util.Locale;

import com.lwolf.scud.locators.TransactionLocator;
import com.lwolf.wf.dto.ModelDto;
import com.lwolf.wf.dto.ProcessDto;
import com.lwolf.wf.dto.TaskListDto;
import com.lwolf.wf.exceptions.RepositoryException;
import com.lwolf.wf.exceptions.ServiceException;
import com.lwolf.wf.repository.InputRepository;
import com.lwolf.wf.repository.ModelRepository;
import com.lwolf.wf.repository.ProcessRepository;
import com.lwolf.wf.repository.TaskRepository;
import com.lwolf.wf.script.ProcessEngine;
import com.lwolf.wf.script.impl.ProcessEngineImpl;
import com.lwolf.wf.services.ProcessService;
import com.lwolf.wf.services.ServiceObject;

public class ProcessServiceImpl extends ServiceObject<ProcessDto> implements ProcessService {
	
	private final ProcessRepository _processRepository;
	private final ModelRepository _modelRepository;
	private final TaskRepository _taskRepository;
	private final InputRepository _inputRepository;
	
	public ProcessServiceImpl(ProcessRepository processRepository, ModelRepository modelRepository, TaskRepository taskRepository, InputRepository inputRepository, TransactionLocator transactionLocator) {
		super(Locale.getDefault(), transactionLocator);
		_processRepository = processRepository;
		_modelRepository = modelRepository;
		_taskRepository = taskRepository;
		_inputRepository = inputRepository;
	}
	
	public ProcessServiceImpl(Locale locale, ProcessRepository processRepository, ModelRepository modelRepository, TaskRepository taskRepository, InputRepository inputRepository, TransactionLocator transactionLocator) {
		super(locale, transactionLocator);
		_processRepository = processRepository;
		_modelRepository = modelRepository;
		_taskRepository = taskRepository;
		_inputRepository = inputRepository;
	}

	@Override
	public void get(ProcessDto dto) throws ServiceException {
		try {
			_processRepository.get(dto);
			_taskRepository.get(dto.tasks);
		} catch (RepositoryException ex) {
			throw new ServiceException(ex);
		}
	}

	@Override
	public void remove(ProcessDto dto) throws ServiceException {
		try {
			_processRepository.remove(dto);
		} catch (RepositoryException ex) {
			throw new ServiceException(ex);
		}
	}
	
	@Override
	public List<ProcessDto> list() throws ServiceException {
		try {
			return _processRepository.list();
		} catch (RepositoryException ex) {
			throw new ServiceException(ex);
		}
	}
	
	@Override
	protected void doAdd(ProcessDto dto) throws ServiceException {
		try {
			
			// get the model
			ModelDto model = new ModelDto(dto.modelId);
			_modelRepository.get(model);
			
			// set the process name, if necessary
			if (dto.name == null || dto.name.equals("")) {
				dto.name = model.name;
			}
			
			// set the process data
			dto.data = model.fileData;
			
			// begin tx
			begin();
			
			_processRepository.add(dto);
			ProcessEngine engine = new ProcessEngineImpl(_taskRepository, _inputRepository, dto.processId, dto.data, dto.createdBy);
			engine.invoke();
			
			// commit tx
			commit();
			
			dto.tasks = new TaskListDto(dto.processId);
			
			_processRepository.get(dto);
			_taskRepository.get(dto.tasks);
			
		} catch (Throwable th) {
			rollback();
			throw new ServiceException(th);
		}
	}

	@Override
	protected void doUpdate(ProcessDto dto) throws ServiceException {
		try {
			_processRepository.update(dto);
		} catch (RepositoryException ex) {
			throw new ServiceException(ex);
		}
	}

}
