package com.lwolf.wf.test.unit;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.BufferedInputStream;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.lwolf.wf.dto.ProcessDto;
import com.lwolf.wf.persistence.InputDao;
import com.lwolf.wf.persistence.InputFileDao;
import com.lwolf.wf.persistence.ModelDao;
import com.lwolf.wf.persistence.ProcessDao;
import com.lwolf.wf.persistence.TaskDao;
import com.lwolf.wf.persistence.entities.ModelEntity;
import com.lwolf.wf.persistence.entities.ProcessEntity;
import com.lwolf.wf.persistence.jdbc.JdbcInputDao;
import com.lwolf.wf.persistence.jdbc.JdbcInputFileDao;
import com.lwolf.wf.persistence.jdbc.JdbcModelDao;
import com.lwolf.wf.persistence.jdbc.JdbcProcessDao;
import com.lwolf.wf.persistence.jdbc.JdbcTaskDao;
import com.lwolf.wf.repository.InputRepository;
import com.lwolf.wf.repository.ModelRepository;
import com.lwolf.wf.repository.ProcessRepository;
import com.lwolf.wf.repository.TaskRepository;
import com.lwolf.wf.repository.impl.InputRepositoryImpl;
import com.lwolf.wf.repository.impl.ModelRepositoryImpl;
import com.lwolf.wf.repository.impl.ProcessRepositoryImpl;
import com.lwolf.wf.repository.impl.TaskRepositoryImpl;
import com.lwolf.wf.services.ProcessService;
import com.lwolf.wf.services.impl.ProcessServiceImpl;
import com.lwolf.wf.test.locators.TestResourceLocator;

public class ProcessUT {
	
	private final ArgumentCaptor<ProcessEntity> processEntityArg = ArgumentCaptor.forClass(ProcessEntity.class);
	
	private final ProcessDao processDaoMock = mock(JdbcProcessDao.class);
	private final ModelDao modelDaoMock = mock(JdbcModelDao.class);
	private final TaskDao taskDaoMock = mock(JdbcTaskDao.class);
	private final InputDao inputDaoMock = mock(JdbcInputDao.class);
	private final InputFileDao inputFileDaoMock = mock(JdbcInputFileDao.class);
	
	private final ProcessRepository processRepository = new ProcessRepositoryImpl(processDaoMock);
	private final ModelRepository modelRepository = new ModelRepositoryImpl(modelDaoMock);
	private final TaskRepository taskRepository = new TaskRepositoryImpl(taskDaoMock);
	private final InputRepository inputRepository = new InputRepositoryImpl(inputDaoMock, inputFileDaoMock);
	
	private final ProcessService processService = new ProcessServiceImpl(processRepository, modelRepository, taskRepository, inputRepository, new TestResourceLocator());
	
	@Test
	public void shouldCreateProcess() throws Exception {
		// given
		doAnswer(new ModelAnswer()).when(modelDaoMock).read(any(ModelEntity.class));
		
		ProcessDto process = new ProcessDto();
		process.name = "shouldCreateProcess";
		
		// when
		processService.add(process);
		
		// then
		verify(processDaoMock).create(processEntityArg.capture());
		assertEquals("ProcessDao.create called with improper name value", "shouldCreateProcess", processEntityArg.getValue().name);
	}
	
	@Test
	public void shouldReadProcess() throws Exception {
		// given
		ProcessDto process = new ProcessDto(new Integer(100));
		
		// when
		processService.get(process);
		
		// then
		verify(processDaoMock).read(processEntityArg.capture());
		assertEquals("ProcessDao.read called with improper processId value", new Integer(100), processEntityArg.getValue().processId);
	}
	
	@Test
	public void shouldUpdateProcess() throws Exception {
		// given
		ProcessDto process = new ProcessDto();
		process.name = "shouldUpdateProcess";
		
		// when
		processService.update(process);
		
		// then
		verify(processDaoMock).update(processEntityArg.capture());
		assertEquals("ProcessDao.update called with improper name value", "shouldUpdateProcess", processEntityArg.getValue().name);
	}
	
	@Test
	public void shouldDeleteProcess() throws Exception {
		// given
		ProcessDto process = new ProcessDto(new Integer(100));
		
		// when
		processService.remove(process);
		
		// then
		verify(processDaoMock).delete(processEntityArg.capture());
		assertEquals("ProcessDao.delete called with improper processId value", new Integer(100), processEntityArg.getValue().processId);
	}
	
	private class ModelAnswer implements Answer<Void> {
		@Override
		public Void answer(InvocationOnMock invocation) throws Throwable {
			Object[] args = invocation.getArguments();
			ModelEntity entity = (ModelEntity) args[0];
			entity.fileData = new BufferedInputStream(getClass().getResourceAsStream("/com/lwolf/wf/test/resources/test.html"));
			return null;
		}
		
	}

}
