package com.lwolf.wf.services.impl;

import java.util.Date;
import java.util.Locale;

import com.lwolf.scud.locators.TransactionLocator;
import com.lwolf.wf.dto.InputMapDto;
import com.lwolf.wf.dto.ProcessDto;
import com.lwolf.wf.dto.TaskDto;
import com.lwolf.wf.dto.TaskListDto;
import com.lwolf.wf.exceptions.RepositoryException;
import com.lwolf.wf.exceptions.ServiceException;
import com.lwolf.wf.repository.InputRepository;
import com.lwolf.wf.repository.ProcessRepository;
import com.lwolf.wf.repository.TaskRepository;
import com.lwolf.wf.script.ProcessEngine;
import com.lwolf.wf.script.impl.ProcessEngineImpl;
import com.lwolf.wf.services.ServiceObject;
import com.lwolf.wf.services.TaskService;

public class TaskServiceImpl extends ServiceObject<TaskDto> implements TaskService {
	
	private final ProcessRepository _processRepository;
	private final TaskRepository _taskRepository;
	private final InputRepository _inputRepository;
	
	public TaskServiceImpl(ProcessRepository processRepository, TaskRepository taskRepository, InputRepository inputRepository, TransactionLocator transactionLocator) {
		super(Locale.getDefault(), transactionLocator);
		_processRepository = processRepository;
		_taskRepository = taskRepository;
		_inputRepository = inputRepository;
	}
	
	public TaskServiceImpl(Locale locale, ProcessRepository processRepository, TaskRepository taskRepository, InputRepository inputRepository, TransactionLocator transactionLocator) {
		super(locale, transactionLocator);
		_processRepository = processRepository;
		_taskRepository = taskRepository;
		_inputRepository = inputRepository;
	}
	
	@Override
	public void complete(TaskDto dto) throws ServiceException {
		try {
			
			if (dto.modifiedOn == null)
				dto.modifiedOn = new Date();
			
			if (dto.completedOn == null)
				dto.completedOn = new Date();
			
			// begin tx
			begin();
			
			// update the task and inputs
			_taskRepository.update(dto);
			_inputRepository.update(dto.inputs);
			
			// get the process
			ProcessDto process = new ProcessDto(dto.processId);
			_processRepository.get(process);
			
			// update the process revision history
			process.modifiedOn = new Date();
			process.modifiedBy = dto.modifiedBy;
			_processRepository.update(process);
			
			// execute workflow
			ProcessEngine engine = new ProcessEngineImpl(_taskRepository, _inputRepository, process.processId, process.data, dto.modifiedBy);
			engine.completeTask(dto.scriptId, dto.handler);
			
			// re-read the task and inputs
			get(dto);
			
			// commit tx
			commit();
			
		} catch (Throwable th) {
			rollback();
			throw new ServiceException(th);
		}
	}

	@Override
	public void get(TaskDto dto) throws ServiceException {
		try {
			// get the task
			_taskRepository.get(dto);
			
			// get the inputs
			if (dto.inputs == null)
				dto.inputs = new InputMapDto();
			
			dto.inputs.processId = dto.processId;
			dto.inputs.taskId = dto.taskId;
			
			_inputRepository.get(dto.inputs);
			
			// get the process
			ProcessDto process = new ProcessDto(dto.processId);
			_processRepository.get(process);
			
			// get the task form data
			ProcessEngine engine = new ProcessEngineImpl(_taskRepository, _inputRepository, process.processId, process.data, dto.modifiedBy);
			dto.form = engine.get(dto.scriptId).formData();
			
		} catch (RepositoryException ex) {
			throw new ServiceException(ex);
		}
	}

	@Override
	public void get(TaskListDto list) throws ServiceException {
		try {
			_taskRepository.get(list);
			_inputRepository.get(list);
		} catch (RepositoryException ex) {
			throw new ServiceException(ex);
		}
	}

	@Override
	public void remove(TaskDto dto) throws ServiceException {
		try {
			_taskRepository.remove(dto);
		} catch (RepositoryException ex) {
			throw new ServiceException(ex);
		}
	}

	@Override
	protected void doAdd(TaskDto dto) throws ServiceException {
		try {
			
			// begin tx
			begin();
			
			// add task
			_taskRepository.add(dto);
			
			// add inputs
			dto.inputs.taskId = dto.taskId;
			_inputRepository.add(dto.inputs);
			
			// commit tx
			commit();
		} catch (Throwable th) {
			rollback();
			throw new ServiceException(th);
		}
	}

	@Override
	protected void doUpdate(TaskDto dto) throws ServiceException {
		try {
			// begin tx
			begin();
			
			// update task and inputs
			_taskRepository.update(dto);
			_inputRepository.update(dto.inputs);
			
			// get the process
			ProcessDto process = new ProcessDto(dto.processId);
			_processRepository.get(process);
			
			// update the process revision history
			process.modifiedOn = new Date();
			process.modifiedBy = dto.modifiedBy;
			_processRepository.update(process);
			
			// re-read the task and inputs
			get(dto);
			
			// commit tx
			commit();
		} catch (Throwable th) {
			rollback();
			throw new ServiceException(th);
		}
	}

}
