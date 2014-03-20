package com.lwolf.wf.api.rs.impl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Path;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response.Status;

import com.lwolf.scud.locators.DataSourceLocator;
import com.lwolf.scud.locators.IdLocator;
import com.lwolf.wf.api.auth.RequestAuthenticator;
import com.lwolf.wf.api.locators.TomeeResourceLocator;
import com.lwolf.wf.api.rs.ProcessResource;
import com.lwolf.wf.dto.ProcessDto;
import com.lwolf.wf.exceptions.ServiceException;
import com.lwolf.wf.locators.UniqueIdLocator;
import com.lwolf.wf.persistence.DataFactory;
import com.lwolf.wf.repository.InputRepository;
import com.lwolf.wf.repository.KeyRepository;
import com.lwolf.wf.repository.ModelRepository;
import com.lwolf.wf.repository.ProcessRepository;
import com.lwolf.wf.repository.TaskRepository;
import com.lwolf.wf.repository.impl.InputRepositoryImpl;
import com.lwolf.wf.repository.impl.KeyRepositoryImpl;
import com.lwolf.wf.repository.impl.ModelRepositoryImpl;
import com.lwolf.wf.repository.impl.ProcessRepositoryImpl;
import com.lwolf.wf.repository.impl.TaskRepositoryImpl;
import com.lwolf.wf.services.ProcessService;
import com.lwolf.wf.services.impl.ProcessServiceImpl;

@Path("/process")
public class ProcessResourceImpl implements ProcessResource {
	
	private final DataSourceLocator dataSourceLocator = TomeeResourceLocator.locator();
	private final IdLocator idLocator = UniqueIdLocator.load(new UniqueIdLocator(TomeeResourceLocator.locator()));
	private final DataFactory dataFactory = DataFactory.getDataFactory(DataFactory.FactoryType.JDBC, dataSourceLocator, idLocator);
	
	private final KeyRepository keyRepository = new KeyRepositoryImpl(dataFactory.keyDao());
	private final ProcessRepository processRepository = new ProcessRepositoryImpl(dataFactory.processDao());
	private final ModelRepository modelRepository = new ModelRepositoryImpl(dataFactory.modelDao());
	private final TaskRepository taskRepository = new TaskRepositoryImpl(dataFactory.taskDao());
	private final InputRepository inputRepository = new InputRepositoryImpl(dataFactory.inputDao(), dataFactory.inputFileDao());
	
	private final ProcessService service = new ProcessServiceImpl(processRepository, modelRepository, taskRepository, inputRepository, TomeeResourceLocator.locator());
	
	private final RequestAuthenticator requestAuthenticator = new RequestAuthenticator(keyRepository);
	
	public List<ProcessDto> list(HttpServletRequest request) {
		try {
			requestAuthenticator.authenticate(request);
			return service.list();
		} catch (ServiceException ex) {
			throw new WebApplicationException(ex, Status.INTERNAL_SERVER_ERROR);
		}
	}
	
	public ProcessDto create(HttpServletRequest request, ProcessDto process) {
		try {
			requestAuthenticator.authenticate(request, process);
			service.add(process);
			return process;
		} catch (ServiceException ex) {
			throw new WebApplicationException(ex, Status.INTERNAL_SERVER_ERROR);
		}
	}
	
	public ProcessDto read(HttpServletRequest request, Integer processId) {
		try {
			requestAuthenticator.authenticate(request);
			
			ProcessDto process = new ProcessDto(processId);
			
			service.get(process);
			
			if (process.processId == null) {
				throw new WebApplicationException(Status.NOT_FOUND);
			} else {
				return process;
			}
		} catch (ServiceException ex) {
			throw new WebApplicationException(ex, Status.INTERNAL_SERVER_ERROR);
		}
	}
	
	public ProcessDto update(HttpServletRequest request, Integer processId, ProcessDto process) {
		try {
			requestAuthenticator.authenticate(request, process);
			process.processId = processId;
			service.update(process);
			return process;
		} catch (ServiceException ex) {
			throw new WebApplicationException(ex, Status.INTERNAL_SERVER_ERROR);
		}
	}
	
	public void delete(HttpServletRequest request, Integer processId) {
		try {
			requestAuthenticator.authenticate(request);
			ProcessDto process = new ProcessDto(processId);
			service.remove(process);
		} catch (ServiceException ex) {
			throw new WebApplicationException(ex, Status.INTERNAL_SERVER_ERROR);
		}
	}

}
