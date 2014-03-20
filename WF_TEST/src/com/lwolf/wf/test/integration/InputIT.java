package com.lwolf.wf.test.integration;

import static org.junit.Assert.*;

import java.io.BufferedInputStream;
import java.util.Iterator;
import java.util.Map.Entry;

import org.junit.Test;

import com.lwolf.scud.locators.DataSourceLocator;
import com.lwolf.scud.locators.IdLocator;
import com.lwolf.scud.locators.ResourceLocator;
import com.lwolf.scud.locators.TransactionLocator;
import com.lwolf.wf.dto.InputDto;
import com.lwolf.wf.dto.ModelDto;
import com.lwolf.wf.dto.ProcessDto;
import com.lwolf.wf.dto.TaskDto;
import com.lwolf.wf.locators.UniqueIdLocator;
import com.lwolf.wf.persistence.DataFactory;
import com.lwolf.wf.repository.InputRepository;
import com.lwolf.wf.repository.ModelRepository;
import com.lwolf.wf.repository.ProcessRepository;
import com.lwolf.wf.repository.TaskRepository;
import com.lwolf.wf.repository.impl.InputRepositoryImpl;
import com.lwolf.wf.repository.impl.ModelRepositoryImpl;
import com.lwolf.wf.repository.impl.ProcessRepositoryImpl;
import com.lwolf.wf.repository.impl.TaskRepositoryImpl;
import com.lwolf.wf.services.ModelService;
import com.lwolf.wf.services.ProcessService;
import com.lwolf.wf.services.TaskService;
import com.lwolf.wf.services.impl.ModelServiceImpl;
import com.lwolf.wf.services.impl.ProcessServiceImpl;
import com.lwolf.wf.services.impl.TaskServiceImpl;
import com.lwolf.wf.test.locators.TestResourceLocator;

public class InputIT {
	
	private ResourceLocator resourceLocator = new TestResourceLocator();
	private DataSourceLocator dataSourceLocator = new TestResourceLocator();
	private IdLocator idLocator = UniqueIdLocator.load(new UniqueIdLocator(resourceLocator));
	private TransactionLocator transactionLocator = new TestResourceLocator();
	
	private DataFactory dataFactory = DataFactory.getDataFactory(DataFactory.FactoryType.JDBC, dataSourceLocator, idLocator);
	
	private ModelRepository modelRepository = new ModelRepositoryImpl(dataFactory.modelDao());
	private ProcessRepository processRepository = new ProcessRepositoryImpl(dataFactory.processDao());
	private TaskRepository taskRepository = new TaskRepositoryImpl(dataFactory.taskDao());
	private InputRepository inputRepository = new InputRepositoryImpl(dataFactory.inputDao(), dataFactory.inputFileDao());
	
	private ModelService modelService = new ModelServiceImpl(modelRepository, processRepository, transactionLocator);
	private ProcessService processService = new ProcessServiceImpl(processRepository, modelRepository, taskRepository, inputRepository, transactionLocator);
	private TaskService taskService = new TaskServiceImpl(processRepository, taskRepository, inputRepository, transactionLocator);
	
	@Test
	public void shouldCreateInputs() throws Exception {
		// given
		ModelDto createdModel = new ModelDto();
		createdModel.name = "shouldCreateInputs";
		createdModel.fileName = "test.html";
		createdModel.fileType = "text/html";
		createdModel.fileData = new BufferedInputStream(getClass().getResourceAsStream("/com/lwolf/wf/test/resources/test.html"));
		modelService.add(createdModel);
		
		ProcessDto createdProcess = new ProcessDto();
		createdProcess.modelId = createdModel.modelId;
		
		// when
		processService.add(createdProcess);
		taskService.get(createdProcess.tasks.get(0));
		
		// then
		assertTrue("Invoking Process did not generate a task with inputs", createdProcess.tasks.get(0).inputs.size() > 0);
	}
	
	@Test
	public void shouldCopyInputs() throws Exception {
		// given
		ModelDto createdModel = new ModelDto();
		createdModel.name = "shouldCopyInputs";
		createdModel.fileName = "test.html";
		createdModel.fileType = "text/html";
		createdModel.fileData = new BufferedInputStream(getClass().getResourceAsStream("/com/lwolf/wf/test/resources/test.html"));
		modelService.add(createdModel);
		
		ProcessDto createdProcess = new ProcessDto();
		createdProcess.modelId = createdModel.modelId;
		processService.add(createdProcess);

		TaskDto task = createdProcess.tasks.get(0);
		taskService.get(task);
		
		Iterator<Entry<String, InputDto>> iter = task.inputs.entrySet().iterator();
		Entry<String, InputDto> entry = iter.next();
		InputDto updatedInput = entry.getValue();
		updatedInput.value = "shouldCopyInputs";
		
		// when
		taskService.complete(task);
		processService.get(createdProcess);
		
		TaskDto followOnTask = createdProcess.tasks.get(1);
		taskService.get(followOnTask);
		
		// then
		assertEquals("Task completion did not copy input data to follow on task", "shouldCopyInputs", followOnTask.inputs.get(updatedInput.name).value);
	}

}
