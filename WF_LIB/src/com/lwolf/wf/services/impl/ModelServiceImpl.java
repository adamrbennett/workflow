package com.lwolf.wf.services.impl;

import java.util.List;
import java.util.Locale;

import com.lwolf.scud.locators.TransactionLocator;
import com.lwolf.wf.dto.ModelDto;
import com.lwolf.wf.dto.ProcessDto;
import com.lwolf.wf.dto.ProcessListDto;
import com.lwolf.wf.exceptions.RepositoryException;
import com.lwolf.wf.exceptions.ServiceException;
import com.lwolf.wf.repository.ModelRepository;
import com.lwolf.wf.repository.ProcessRepository;
import com.lwolf.wf.services.ModelService;
import com.lwolf.wf.services.ServiceObject;
import com.lwolf.wf.vo.Notification;

public class ModelServiceImpl extends ServiceObject<ModelDto> implements ModelService {
	
	private final ModelRepository _modelRepository;
	private final ProcessRepository _processRepository;
	
	public ModelServiceImpl(ModelRepository modelRepository, ProcessRepository processRepository, TransactionLocator transactionLocator) {
		super(Locale.getDefault(), transactionLocator);
		_modelRepository = modelRepository;
		_processRepository = processRepository;
	}
	
	public ModelServiceImpl(Locale locale, ModelRepository modelRepository, ProcessRepository processRepository, TransactionLocator transactionLocator) {
		super(locale, transactionLocator);
		_modelRepository = modelRepository;
		_processRepository = processRepository;
	}

	@Override
	public void get(ModelDto dto) throws ServiceException {
		try {
			_modelRepository.get(dto);
		} catch (RepositoryException ex) {
			throw new ServiceException(ex);
		}
	}

	@Override
	public ModelDto remove(ModelDto dto) throws ServiceException {
		try {
			// check for existing processes
			ProcessListDto processes = new ProcessListDto(dto.modelId);
			_processRepository.get(processes);
			
			if (processes != null && !processes.isEmpty()) {
				for (ProcessDto process : processes) {
					Notification notification = new Notification(Notification.NotificationType.PARENT_KEY_DEPENDENCY);
					notification.message = notificationBundle.modelDeleteDependencyProcess(process.name);
					dto.notifications.add(notification);
				}
				return dto;
			}
			
			_modelRepository.remove(dto);
			
			return dto;
		} catch (RepositoryException ex) {
			throw new ServiceException(ex);
		}
	}
	
	@Override
	public List<ModelDto> list() throws ServiceException {
		try {
			return _modelRepository.list();
		} catch (RepositoryException ex) {
			throw new ServiceException(ex);
		}
	}

	@Override
	protected void doAdd(ModelDto dto) throws ServiceException {
		try {
			if (dto.versionNumber == null)
				dto.versionNumber = 1;
			
			_modelRepository.add(dto);
		} catch (RepositoryException ex) {
			throw new ServiceException(ex);
		}
	}

	@Override
	protected void doUpdate(ModelDto dto) throws ServiceException {
		try {
			if (dto.fileData != null)
				dto.versionNumber++;
			
			_modelRepository.update(dto);
		} catch (RepositoryException ex) {
			throw new ServiceException(ex);
		}
	}

}
