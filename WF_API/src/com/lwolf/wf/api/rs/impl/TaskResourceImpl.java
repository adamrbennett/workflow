package com.lwolf.wf.api.rs.impl;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Path;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response.Status;

import com.lwolf.scud.locators.DataSourceLocator;
import com.lwolf.scud.locators.IdLocator;
import com.lwolf.wf.api.auth.RequestAuthenticator;
import com.lwolf.wf.api.locators.TomeeResourceLocator;
import com.lwolf.wf.api.rs.TaskResource;
import com.lwolf.wf.api.utils.ApplicationProperties;
import com.lwolf.wf.dto.InputDto;
import com.lwolf.wf.dto.InputFileDto;
import com.lwolf.wf.dto.TaskDto;
import com.lwolf.wf.dto.TaskListDto;
import com.lwolf.wf.exceptions.ServiceException;
import com.lwolf.wf.locators.UniqueIdLocator;
import com.lwolf.wf.persistence.DataFactory;
import com.lwolf.wf.repository.InputRepository;
import com.lwolf.wf.repository.KeyRepository;
import com.lwolf.wf.repository.ProcessRepository;
import com.lwolf.wf.repository.TaskRepository;
import com.lwolf.wf.repository.impl.InputRepositoryImpl;
import com.lwolf.wf.repository.impl.KeyRepositoryImpl;
import com.lwolf.wf.repository.impl.ProcessRepositoryImpl;
import com.lwolf.wf.repository.impl.TaskRepositoryImpl;
import com.lwolf.wf.services.TaskService;
import com.lwolf.wf.services.impl.TaskServiceImpl;

@Path("/process/{processId}/task")
public class TaskResourceImpl implements TaskResource {
	
	private final DataSourceLocator dataSourceLocator = TomeeResourceLocator.locator();
	private final IdLocator idLocator = UniqueIdLocator.load(new UniqueIdLocator(TomeeResourceLocator.locator()));
	private final DataFactory dataFactory = DataFactory.getDataFactory(DataFactory.FactoryType.JDBC, dataSourceLocator, idLocator);
	
	private final KeyRepository keyRepository = new KeyRepositoryImpl(dataFactory.keyDao());
	private final ProcessRepository processRepository = new ProcessRepositoryImpl(dataFactory.processDao());
	private final TaskRepository taskRepository = new TaskRepositoryImpl(dataFactory.taskDao());
	private final InputRepository inputRepository = new InputRepositoryImpl(dataFactory.inputDao(), dataFactory.inputFileDao());
	
	private final TaskService service = new TaskServiceImpl(processRepository, taskRepository, inputRepository, TomeeResourceLocator.locator());
	
	private final RequestAuthenticator requestAuthenticator = new RequestAuthenticator(keyRepository);

	public TaskListDto listByProcess(HttpServletRequest request, Integer processId) {
		try {
			requestAuthenticator.authenticate(request);
			TaskListDto list = new TaskListDto(processId);
			service.get(list);
			return list;
		} catch (ServiceException ex) {
			throw new WebApplicationException(ex, Status.INTERNAL_SERVER_ERROR);
		}
	}
	
	public TaskDto read(HttpServletRequest request, Integer processId, Integer taskId) {
		try {
			requestAuthenticator.authenticate(request);
			
			TaskDto task = new TaskDto(processId, taskId);
			
			service.get(task);
			
			if (task.taskId == null)
				throw new WebApplicationException(Status.NOT_FOUND);
			else
				return task;
		} catch (ServiceException ex) {
			throw new WebApplicationException(ex, Status.INTERNAL_SERVER_ERROR);
		}
	}
	
	public TaskDto update(HttpServletRequest request, Integer processId, Integer taskId, TaskDto task) {
		try {
			requestAuthenticator.authenticate(request, task);
			
			for (Entry<String, InputDto> entry : task.inputs.entrySet()) {
				InputDto input = entry.getValue();
				if (input.files != null) {
					for (InputFileDto inputFile : input.files) {
						if (inputFile.tempName != null && !inputFile.tempName.equals("")) {
							File file = new File(ApplicationProperties.uploadTempDir() + "/" + inputFile.tempName);
							inputFile.fileData = new BufferedInputStream(new FileInputStream(file));
						}
					}
				}
			}
			
			task.processId = processId;
			task.taskId = taskId;
			
			if (TaskDto.ActionType.COMPLETE.is(task.action)) {
				service.complete(task);
			} else {
				service.update(task);
			}
			
			return task;
		} catch (ServiceException ex) {
			throw new WebApplicationException(ex, Status.INTERNAL_SERVER_ERROR);
		} catch (FileNotFoundException ex) {
			throw new WebApplicationException(ex, Status.INTERNAL_SERVER_ERROR);
		} catch (IOException ex) {
			throw new WebApplicationException(ex, Status.INTERNAL_SERVER_ERROR);
		}
	}
	
	public void delete(HttpServletRequest request, Integer processId, Integer taskId) {
		try {
			requestAuthenticator.authenticate(request);
			TaskDto task = new TaskDto(processId, taskId);
			service.remove(task);
		} catch (ServiceException ex) {
			throw new WebApplicationException(ex, Status.INTERNAL_SERVER_ERROR);
		}
	}
}
